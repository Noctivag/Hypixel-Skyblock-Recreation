package de.noctivag.plugin;
import org.bukkit.inventory.ItemStack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinMessageCommand implements CommandExecutor {
    private final Plugin plugin;
    private final de.noctivag.plugin.messages.JoinMessageManager joinMessageManager;
    
    public JoinMessageCommand(Plugin plugin, de.noctivag.plugin.messages.JoinMessageManager joinMessageManager) {
        this.plugin = plugin;
        this.joinMessageManager = joinMessageManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("§aJoin message command executed!");
        }
        return true;
    }
}
