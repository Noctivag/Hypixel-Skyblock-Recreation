package de.noctivag.skyblock;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinMessageCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;
    private final de.noctivag.skyblock.messages.JoinMessageManager joinMessageManager;
    
    public JoinMessageCommand(SkyblockPlugin SkyblockPlugin, de.noctivag.skyblock.messages.JoinMessageManager joinMessageManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.joinMessageManager = joinMessageManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(Component.text("§aJoin message command executed!"));
        }
        return true;
    }
}
