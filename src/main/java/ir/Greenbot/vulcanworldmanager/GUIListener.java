package ir.Greenbot.vulcanworldmanager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIListener implements Listener {

    private final VulcanWorldManager plugin;
    private final Map<UUID, String> awaitingWorldName = new HashMap<>();

    public GUIListener(VulcanWorldManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        String title = event.getView().getTitle();

        // --- Main Menu ---
        if (ChatColor.stripColor(title).equals("ᴡᴏʀʟᴅ ᴍᴀɴᴀɢᴇʀ")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            // Check if the clicked slot is the Create World button (Slot 4)
            if (event.getSlot() == 4) {
                player.closeInventory();
                awaitingWorldName.put(player.getUniqueId(), "");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &ePlease type the name of the new world in chat."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &7Type &ccancel &7to abort."));
                return;
            }

            // Ignore glass panes
            if (event.getCurrentItem().getType() == Material.GRAY_STAINED_GLASS_PANE) {
                return;
            }

            // Otherwise, it's a world item
            String worldName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            World world = Bukkit.getWorld(worldName);

            if (world == null) return;

            if (event.getClick() == ClickType.RIGHT) {
                // Right-Click: Unload World
                if (world.getPlayers().isEmpty()) {
                    Bukkit.unloadWorld(world, true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &aWorld &e" + worldName + " &aunloaded."));
                    player.closeInventory();
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &cCannot unload a world with players inside!"));
                }
            } else {
                // Left-Click: Open Manage Menu
                WorldGUI.openWorldMenu(player, worldName);
            }
        }

        // --- Manage World Menu ---
        else if (ChatColor.stripColor(title).startsWith("ᴍᴀɴᴀɢᴇ: ")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            String worldName = ChatColor.stripColor(title).replace("ᴍᴀɴᴀɢᴇ: ", "").trim();
            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &cWorld not found!"));
                return;
            }

            if (event.getCurrentItem().getType() == Material.ENDER_PEARL) {
                player.teleport(world.getSpawnLocation());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &aTeleported to &e" + worldName));
                player.closeInventory();
            } else if (event.getCurrentItem().getType() == Material.RED_BED) {
                world.setSpawnLocation(player.getLocation());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &aSpawn set for world &e" + worldName));
            } else if (event.getCurrentItem().getType() == Material.TNT) {
                if (!world.getPlayers().isEmpty()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &cCannot delete a world with players inside!"));
                } else {
                    Bukkit.unloadWorld(world, false);
                    File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
                    if (worldFolder.exists()) {
                        deleteDirectory(worldFolder);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &aWorld &e" + worldName + " &awas permanently deleted."));
                    }
                    player.closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!awaitingWorldName.containsKey(player.getUniqueId())) return;

        event.setCancelled(true);
        String message = event.getMessage();

        if (message.equalsIgnoreCase("cancel")) {
            awaitingWorldName.remove(player.getUniqueId());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &cCancelled."));
            return;
        }

        awaitingWorldName.remove(player.getUniqueId());
        String worldName = message.replaceAll("[^a-zA-Z0-9_-]", "");

        if (worldName.isEmpty()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &cInvalid world name."));
            return;
        }

        if (Bukkit.getWorld(worldName) != null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &cWorld already exists!"));
            return;
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &aCreating world &e" + worldName + "&a..."));
        
        Bukkit.getScheduler().runTask(plugin, () -> {
            WorldCreator creator = new WorldCreator(worldName);
            creator.generateStructures(true);
            World world = creator.createWorld();
            if (world != null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &aWorld &e" + worldName + " &acreated!"));
                WorldGUI.openMainMenu(player);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&4VulcanWorlds&8] &cFailed to create world."));
            }
        });
    }

    private void deleteDirectory(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDirectory(f);
            }
        }
        file.delete();
    }
}
