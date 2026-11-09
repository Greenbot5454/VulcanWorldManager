package ir.Greenbot.vulcanworldmanager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldGUI {

    public static void openMainMenu(Player player) {
        String title = ChatColor.translateAlternateColorCodes('&', "&8&lᴡᴏʀʟᴅ ᴍᴀɴᴀɢᴇʀ");
        Inventory inv = Bukkit.createInventory(null, 54, title); // 6 rows

        // Fill empty slots with glass panes
        ItemStack glass = createItem(Material.GRAY_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, glass);
        }

        // Add Create World Button (Slot 4)
        ItemStack createBtn = createItem(Material.GRASS_BLOCK,
                "&a&lᴄʀᴇᴀᴛᴇ ɴᴇᴡ ᴡᴏʀʟᴅ",
                Arrays.asList("&7Click here to create", "&7a new empty world."));
        inv.setItem(4, createBtn);

        // List Worlds
        List<World> worlds = Bukkit.getWorlds();
        int slot = 19; // Start from row 3

        for (World world : worlds) {
            if (slot > 43) break; // Max slots before border

            Material mat = world.getEnvironment() == World.Environment.NETHER ? Material.NETHERRACK :
                           world.getEnvironment() == World.Environment.THE_END ? Material.END_STONE : Material.GRASS_BLOCK;

            ItemStack worldItem = createItem(mat,
                    "&e&l" + world.getName(),
                    Arrays.asList(
                            "&7Players: &f" + world.getPlayers().size(),
                            "&7Environment: &f" + world.getEnvironment().name(),
                            "",
                            "&aLeft-Click &7to manage",
                            "&cRight-Click &7to unload"
                    ));
            inv.setItem(slot, worldItem);
            slot++;
        }

        player.openInventory(inv);
    }

    public static void openWorldMenu(Player player, String worldName) {
        String title = ChatColor.translateAlternateColorCodes('&', "&8&lᴍᴀɴᴀɢᴇ: &7" + worldName);
        Inventory inv = Bukkit.createInventory(null, 27, title);

        ItemStack glass = createItem(Material.GRAY_STAINED_GLASS_PANE, " ", null);
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, glass);
        }

        // Teleport
        inv.setItem(11, createItem(Material.ENDER_PEARL, "&b&lᴛᴇʟᴇᴘᴏʀᴛ",
                Arrays.asList("&7Teleports you to this world.")));

        // Set Spawn
        inv.setItem(13, createItem(Material.RED_BED, "&e&lsᴇᴛ sᴘᴀᴡɴ",
                Arrays.asList("&7Sets the spawn point", "&7to your current location.")));

        // Delete
        inv.setItem(15, createItem(Material.TNT, "&c&lᴅᴇʟᴇᴛᴇ ᴡᴏʀʟᴅ",
                Arrays.asList("&c&lDANGER:", "&7Permanently deletes this world.")));

        player.openInventory(inv);
    }

    private static ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            if (lore != null) {
                List<String> coloredLore = new ArrayList<>();
                for (String l : lore) {
                    coloredLore.add(ChatColor.translateAlternateColorCodes('&', l));
                }
                meta.setLore(coloredLore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }
}
