package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestartCommand implements CommandExecutor {
    private final Plugin plugin;
    private final de.noctivag.plugin.managers.RestartManager restartManager;
    
    public RestartCommand(Plugin plugin, de.noctivag.plugin.managers.RestartManager restartManager) {
        this.plugin = plugin;
        this.restartManager = restartManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("§aRestart command executed!");
        }
        return true;
    }
}
