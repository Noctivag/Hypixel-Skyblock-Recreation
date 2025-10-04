package de.noctivag.skyblock.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DailyRewardCommand implements CommandExecutor {
    private final SkyblockPlugin plugin;
    private final de.noctivag.plugin.rewards.DailyRewardManager dailyRewardManager;
    
    public DailyRewardCommand(SkyblockPlugin plugin, de.noctivag.plugin.rewards.DailyRewardManager dailyRewardManager) {
        this.plugin = plugin;
        this.dailyRewardManager = dailyRewardManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("§aDaily reward claimed!");
        }
        return true;
    }
}
