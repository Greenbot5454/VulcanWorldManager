package ir.Greenbot.vulcanworldmanager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;

public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        String title = event.getView().getTitle();
        String mainTitle = ChatColor.translateAlternateColorCodes('&', "&2&lWorld Manager");
        
        // --- Main Menu ---
        if (title.equals(mainTitle)) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
            
            String worldName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            WorldGUI.openWorldMenu(player, worldName);
        }

        // --- World Management Menu ---
        else if (ChatColor.stripColor(title).startsWith("Manage: ")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            // Extract world name from title
            String worldName = ChatColor.stripColor(title).replace("Manage: ", "").trim();
            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWorld not found!"));
                player.closeInventory();
                return;
            }

            switch (event.getCurrentItem().getType()) {
                case BARRIER:
                    if (world.getPlayers().size() > 0) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCannot unload a world with players inside!"));
                    } else {
                        Bukkit.unloadWorld(world, true);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWorld &e" + worldName + " &aunloaded successfully."));
                        player.closeInventory();
                    }
                    break;

                case ENDER_PEARL:
                    player.teleport(world.getSpawnLocation());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTeleported to &e" + worldName));
                    player.closeInventory();
                    break;

                case TNT:
                    if (world.getPlayers().size() > 0) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCannot delete a world with players inside!"));
                    } else {
                        Bukkit.unloadWorld(world, false);
                        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
                        if (worldFolder.exists()) {
                            deleteDirectory(worldFolder);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWorld &e" + worldName + " &awas permanently deleted."));
                        } else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWorld folder not found."));
                        }
                        player.closeInventory();
                    }
                    break;
                default:
                    break;
            }
        }
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
