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

public class DailyRewardCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;
    private final de.noctivag.skyblock.rewards.DailyRewardManager dailyRewardManager;
    
    public DailyRewardCommand(SkyblockPlugin SkyblockPlugin, de.noctivag.skyblock.rewards.DailyRewardManager dailyRewardManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.dailyRewardManager = dailyRewardManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(Component.text("§aDaily reward claimed!"));
        }
        return true;
    }
}
