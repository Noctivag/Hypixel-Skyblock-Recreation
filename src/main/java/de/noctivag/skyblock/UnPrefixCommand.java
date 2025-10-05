package de.noctivag.skyblock;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnPrefixCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;
    private final java.util.Map<String, String> prefixMap;
    private final java.util.Map<String, String> nickMap;
    
    public UnPrefixCommand(java.util.Map<String, String> prefixMap, java.util.Map<String, String> nickMap) {
        this.SkyblockPlugin = null;
        this.prefixMap = prefixMap;
        this.nickMap = nickMap;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(Component.text("§aUnprefix command executed!"));
        }
        return true;
    }
}
