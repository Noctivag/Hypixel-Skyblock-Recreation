package de.noctivag.skyblock.commands;
import org.bukkit.inventory.ItemStack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { sender.sendMessage("Players only"); return true; }
        int ping = player.getPing();
        player.sendMessage("§eDein Ping: §a" + ping + "ms");
        return true;
    }
}


