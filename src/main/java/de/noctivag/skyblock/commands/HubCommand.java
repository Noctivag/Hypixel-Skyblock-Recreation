package de.noctivag.skyblock.commands;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command für das Teleportieren zum Hub.
 * Nutzt das neue Rolling-Restart-System für nahtlose Übergänge.
 */
public class HubCommand implements CommandExecutor {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public HubCommand(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Command kann nur von Spielern ausgeführt werden!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Hole die aktuelle Live-Instanz des Hubs
        World hub = SkyblockPlugin.getWorldManager().getLiveWorld("hub");
        
        if (hub == null) {
            player.sendMessage(Component.text("§cDer Hub ist momentan nicht verfügbar. Bitte versuche es später erneut."));
            return true;
        }
        
        // Teleportiere den Spieler zum Hub
        player.teleport(hub.getSpawnLocation());
        player.sendMessage(Component.text("§aDu wurdest zum Hub teleportiert!"));
        
        return true;
    }
}
