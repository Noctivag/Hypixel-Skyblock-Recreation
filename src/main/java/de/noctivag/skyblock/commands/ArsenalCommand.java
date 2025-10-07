package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Arsenal Command - Öffnet ein Menü mit allen Items aus dem Plugin für OP-Spieler
 */
public class ArsenalCommand implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin plugin;
    
    public ArsenalCommand(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Dieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Prüfe OP-Berechtigung
        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung für diesen Befehl!");
            return true;
        }
        
        // Öffne Arsenal-Menü
        openArsenalMenu(player);
        
        return true;
    }
    
    /**
     * Öffnet das Arsenal-Menü
     */
    private void openArsenalMenu(Player player) {
        // Erstelle Arsenal-GUI
        de.noctivag.skyblock.gui.ArsenalGUI arsenalGUI = new de.noctivag.skyblock.gui.ArsenalGUI(plugin);
        arsenalGUI.openMenu(player);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(); // Keine Tab-Completion nötig
    }
}
