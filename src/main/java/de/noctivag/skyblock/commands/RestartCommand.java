package de.noctivag.skyblock.commands;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RestartCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;
    private final de.noctivag.skyblock.managers.RestartManager restartManager;
    
    public RestartCommand(SkyblockPlugin SkyblockPlugin, de.noctivag.skyblock.managers.RestartManager restartManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.restartManager = restartManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(Component.text("§aRestart command executed!"));
        }
        return true;
    }
}
