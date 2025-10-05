package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

/**
 * Hub Command für das Rolling-Restart-System
 * Teleportiert Spieler zur aktuellen LIVE-Instanz des Hubs
 */
public class HubCommand implements CommandExecutor {
    
    private final SkyblockPlugin plugin;
    
    public HubCommand(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("§cDieser Befehl kann nur von Spielern ausgeführt werden!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        // Verwende das Rolling-Restart-System um die aktuelle Hub-Instanz zu bekommen
        if (plugin.getRollingRestartWorldManager() != null) {
            World hub = plugin.getRollingRestartWorldManager().getLiveWorld("hub");
            
            if (hub != null) {
                player.teleport(hub.getSpawnLocation());
                player.sendMessage(Component.text("§aDu wurdest zum Hub teleportiert!"));
                player.sendMessage(Component.text("§7Aktuelle Hub-Instanz: §e" + hub.getName()));
                return true;
            } else {
                player.sendMessage(Component.text("§cDer Hub ist momentan nicht verfügbar!"));
                return true;
            }
        }
        
        // Fallback: Verwende den Standard-Hub
        World defaultHub = Bukkit.getWorld("hub");
        if (defaultHub != null) {
            player.teleport(defaultHub.getSpawnLocation());
            player.sendMessage(Component.text("§aDu wurdest zum Hub teleportiert!"));
            player.sendMessage(Component.text("§7Fallback-Hub verwendet."));
            return true;
        }
        
        // Kein Hub verfügbar
        player.sendMessage(Component.text("§cKein Hub verfügbar! Bitte kontaktiere einen Administrator."));
        return true;
    }
}