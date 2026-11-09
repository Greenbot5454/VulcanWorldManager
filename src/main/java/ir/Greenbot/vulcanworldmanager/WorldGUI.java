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
import java.util.List;

public class WorldGUI {

    public static void openMainMenu(Player player) {
        List<World> worlds = Bukkit.getWorlds();
        int size = ((worlds.size() - 1) / 9 + 1) * 9; 
        if (size < 9) size = 9; 
        
        // استفاده از & در عنوان و تبدیل آن به رنگ
        String title = ChatColor.translateAlternateColorCodes('&', "&2&lWorld Manager");
        Inventory inv = Bukkit.createInventory(null, size, title);

        for (int i = 0; i < worlds.size(); i++) {
            World world = worlds.get(i);
            ItemStack item = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta meta = item.getItemMeta();
            
            if (meta != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a" + world.getName()));
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7Players: &f" + world.getPlayers().size()));
                lore.add(ChatColor.translateAlternateColorCodes('&', "&7Environment: &f" + world.getEnvironment().name()));
                lore.add("");
                lore.add(ChatColor.translateAlternateColorCodes('&', "&eClick to manage"));
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            inv.setItem(i, item);
        }
        player.openInventory(inv);
    }

    public static void openWorldMenu(Player player, String worldName) {
        String title = ChatColor.translateAlternateColorCodes('&', "&4&lManage: &c" + worldName);
        Inventory inv = Bukkit.createInventory(null, 27, title);

        // Unload Button
        ItemStack unload = new ItemStack(Material.BARRIER);
        ItemMeta unloadMeta = unload.getItemMeta();
        if (unloadMeta != null) {
            unloadMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cUnload World"));
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7Turns off the world to save RAM."));
            unloadMeta.setLore(lore);
            unload.setItemMeta(unloadMeta);
        }
        inv.setItem(11, unload);

        // Teleport Button
        ItemStack teleport = new ItemStack(Material.ENDER_PEARL);
        ItemMeta tpMeta = teleport.getItemMeta();
        if (tpMeta != null) {
            tpMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bTeleport to World"));
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7Teleports you to this world."));
            tpMeta.setLore(lore);
            teleport.setItemMeta(tpMeta);
        }
        inv.setItem(13, teleport);

        // Delete Button
        ItemStack delete = new ItemStack(Material.TNT);
        ItemMeta delMeta = delete.getItemMeta();
        if (delMeta != null) {
            delMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&lDelete World"));
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.translateAlternateColorCodes('&', "&c&lDANGER: &7This will permanently"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&7delete the world files!"));
            delMeta.setLore(lore);
            delete.setItemMeta(delMeta);
        }
        inv.setItem(15, delete);

        player.openInventory(inv);
    }
}
