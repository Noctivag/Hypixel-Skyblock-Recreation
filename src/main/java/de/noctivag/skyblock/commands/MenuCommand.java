package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.UltimateMainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Menu Command - Opens the main Skyblock menu
 */
public class MenuCommand implements CommandExecutor {
    
    private final SkyblockPlugin plugin;
    
    public MenuCommand(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Open the main menu
        new UltimateMainMenu(plugin, player).open();
        
        return true;
    }
}