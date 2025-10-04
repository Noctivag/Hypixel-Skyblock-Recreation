package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Command für das Verwalten von privaten Inseln.
 * Nutzt das On-Demand-Loading-System für optimale Performance.
 */
public class IslandCommand implements CommandExecutor {
    
    private final SkyblockPlugin plugin;
    
    public IslandCommand(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Command kann nur von Spielern ausgeführt werden!");
            return true;
        }
        
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        
        if (args.length == 0) {
            // Teleportiere zur eigenen Insel
            teleportToIsland(player, playerUUID);
        } else if (args.length == 1) {
            String subCommand = args[0].toLowerCase();
            
            switch (subCommand) {
                case "home":
                case "go":
                    teleportToIsland(player, playerUUID);
                    break;
                case "unload":
                    unloadIsland(player, playerUUID);
                    break;
                case "reload":
                    reloadIsland(player, playerUUID);
                    break;
                default:
                    showHelp(player);
                    break;
            }
        } else {
            showHelp(player);
        }
        
        return true;
    }
    
    private void teleportToIsland(Player player, UUID playerUUID) {
        try {
            // Lade die private Insel on-demand
            World island = plugin.getWorldManager().loadPrivateIsland(playerUUID);
            
            if (island == null) {
                player.sendMessage("§cFehler beim Laden deiner Insel. Bitte versuche es erneut.");
                return;
            }
            
            // Teleportiere zur Insel
            player.teleport(island.getSpawnLocation());
            player.sendMessage("§aWillkommen auf deiner privaten Insel!");
            
        } catch (Exception e) {
            player.sendMessage("§cFehler beim Teleportieren zur Insel: " + e.getMessage());
            plugin.getLogger().severe("Fehler beim Laden der Insel für " + player.getName() + ": " + e.getMessage());
        }
    }
    
    private void unloadIsland(Player player, UUID playerUUID) {
        try {
            plugin.getWorldManager().unloadPrivateIsland(playerUUID);
            player.sendMessage("§eDeine Insel wurde entladen und gespeichert.");
        } catch (Exception e) {
            player.sendMessage("§cFehler beim Entladen der Insel: " + e.getMessage());
            plugin.getLogger().severe("Fehler beim Entladen der Insel für " + player.getName() + ": " + e.getMessage());
        }
    }
    
    private void reloadIsland(Player player, UUID playerUUID) {
        try {
            // Entlade die Insel zuerst
            plugin.getWorldManager().unloadPrivateIsland(playerUUID);
            
            // Lade sie neu
            World island = plugin.getWorldManager().loadPrivateIsland(playerUUID);
            
            if (island != null) {
                player.teleport(island.getSpawnLocation());
                player.sendMessage("§aDeine Insel wurde neu geladen!");
            } else {
                player.sendMessage("§cFehler beim Neuladen der Insel.");
            }
        } catch (Exception e) {
            player.sendMessage("§cFehler beim Neuladen der Insel: " + e.getMessage());
            plugin.getLogger().severe("Fehler beim Neuladen der Insel für " + player.getName() + ": " + e.getMessage());
        }
    }
    
    private void showHelp(Player player) {
        player.sendMessage("§6=== Insel Commands ===");
        player.sendMessage("§e/island §f- Teleportiere zu deiner Insel");
        player.sendMessage("§e/island home §f- Teleportiere zu deiner Insel");
        player.sendMessage("§e/island unload §f- Entlade deine Insel");
        player.sendMessage("§e/island reload §f- Lade deine Insel neu");
    }
}