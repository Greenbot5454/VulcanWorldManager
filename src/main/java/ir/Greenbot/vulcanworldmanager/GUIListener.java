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

        // --- منوی اصلی ---
        if (title.equals(ChatColor.DARK_GREEN + "World Manager")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
            
            String worldName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            WorldGUI.openWorldMenu(player, worldName);
        }

        // --- منوی مدیریت دنیا ---
        else if (title.startsWith(ChatColor.DARK_RED + "Manage: ")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;

            String worldName = title.replace(ChatColor.DARK_RED + "Manage: ", "");
            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                player.sendMessage(ChatColor.RED + "World not found!");
                return;
            }

            switch (event.getCurrentItem().getType()) {
                case BARRIER:
                    // خاموش کردن دنیا
                    if (world.getPlayers().size() > 0) {
                        player.sendMessage(ChatColor.RED + "Cannot unload a world with players inside!");
                    } else {
                        Bukkit.unloadWorld(world, true);
                        player.sendMessage(ChatColor.GREEN + "World " + worldName + " unloaded.");
                        player.closeInventory();
                    }
                    break;

                case ENDER_PEARL:
                    // تلپورت به دنیا
                    player.teleport(world.getSpawnLocation());
                    player.sendMessage(ChatColor.GREEN + "Teleported to " + worldName);
                    player.closeInventory();
                    break;

                case TNT:
                    // حذف دنیا
                    if (world.getPlayers().size() > 0) {
                        player.sendMessage(ChatColor.RED + "Cannot delete a world with players inside!");
                    } else {
                        Bukkit.unloadWorld(world, false);
                        File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
                        if (worldFolder.exists()) {
                            deleteDirectory(worldFolder);
                            player.sendMessage(ChatColor.GREEN + "World " + worldName + " deleted.");
                        } else {
                            player.sendMessage(ChatColor.RED + "World folder not found.");
                        }
                        player.closeInventory();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    // متد کمکی برای حذف پوشه به صورت بازگشتی
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
