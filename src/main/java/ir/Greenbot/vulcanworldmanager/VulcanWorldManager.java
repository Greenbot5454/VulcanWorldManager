package ir.Greenbot.vulcanworldmanager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class VulcanWorldManager extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getCommand("worlds").setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use this command!");
                return true;
            }
            
            if (!player.hasPermission("vulcanworldmanager.admin")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));
                return true;
            }
            
            WorldGUI.openMainMenu(player);
            return true;
        });

        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
        getLogger().info("VulcanWorldManager enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("VulcanWorldManager disabled.");
    }
}
