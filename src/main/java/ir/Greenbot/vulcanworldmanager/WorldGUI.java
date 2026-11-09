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
        if (size < 9) size = 9; // حداقل ۱ ردیف
        
        Inventory inv = Bukkit.createInventory(null, size, ChatColor.DARK_GREEN + "World Manager");

        for (int i = 0; i < worlds.size(); i++) {
            World world = worlds.get(i);
            ItemStack item = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + world.getName());
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Players: " + world.getPlayers().size());
                lore.add(ChatColor.GRAY + "Environment: " + world.getEnvironment().name());
                lore.add(ChatColor.YELLOW + "Click to manage");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            inv.setItem(i, item);
        }
        player.openInventory(inv);
    }

    public static void openWorldMenu(Player player, String worldName) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_RED + "Manage: " + worldName);

        // Unload Button
        ItemStack unload = new ItemStack(Material.BARRIER);
        ItemMeta unloadMeta = unload.getItemMeta();
        if (unloadMeta != null) {
            unloadMeta.setDisplayName(ChatColor.RED + "Unload World");
            unload.setItemMeta(unloadMeta);
        }
        inv.setItem(11, unload);

        // Teleport Button
        ItemStack teleport = new ItemStack(Material.ENDER_PEARL);
        ItemMeta tpMeta = teleport.getItemMeta();
        if (tpMeta != null) {
            tpMeta.setDisplayName(ChatColor.AQUA + "Teleport to World");
            teleport.setItemMeta(tpMeta);
        }
        inv.setItem(13, teleport);

        // Delete Button (Dangerous)
        ItemStack delete = new ItemStack(Material.TNT);
        ItemMeta delMeta = delete.getItemMeta();
        if (delMeta != null) {
            delMeta.setDisplayName(ChatColor.DARK_RED + "Delete World (Permanent)");
            delete.setItemMeta(delMeta);
        }
        inv.setItem(15, delete);

        player.openInventory(inv);
    }
}
