package de.noctivag.skyblock.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestartCommand implements CommandExecutor {
    private final SkyblockPlugin plugin;
    private final de.noctivag.plugin.managers.RestartManager restartManager;
    
    public RestartCommand(SkyblockPlugin plugin, de.noctivag.plugin.managers.RestartManager restartManager) {
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
