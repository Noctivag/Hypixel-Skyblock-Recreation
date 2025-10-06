package de.noctivag.skyblock.commands;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

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
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public IslandCommand(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
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
            var future = SkyblockPlugin.getWorldManager().loadPrivateIsland(playerUUID);
            future.thenAccept(island -> {
                if (island == null) {
                    player.sendMessage(Component.text("§cFehler beim Laden deiner Insel. Bitte versuche es erneut."));
                    return;
                }
                
                // Teleportiere zur Insel
                player.teleport(island.getSpawnLocation());
                player.sendMessage(Component.text("§aWillkommen auf deiner privaten Insel!"));
            }).exceptionally(throwable -> {
                player.sendMessage("§cFehler beim Teleportieren zur Insel: " + throwable.getMessage());
                SkyblockPlugin.getLogger().severe("Fehler beim Laden der Insel für " + player.getName() + ": " + throwable.getMessage());
                return null;
            });
            
        } catch (Exception e) {
            player.sendMessage("§cFehler beim Teleportieren zur Insel: " + e.getMessage());
            SkyblockPlugin.getLogger().severe("Fehler beim Laden der Insel für " + player.getName() + ": " + e.getMessage());
        }
    }
    
    private void unloadIsland(Player player, UUID playerUUID) {
        try {
            SkyblockPlugin.getWorldManager().unloadPrivateIsland(playerUUID);
            player.sendMessage(Component.text("§eDeine Insel wurde entladen und gespeichert."));
        } catch (Exception e) {
            player.sendMessage("§cFehler beim Entladen der Insel: " + e.getMessage());
            SkyblockPlugin.getLogger().severe("Fehler beim Entladen der Insel für " + player.getName() + ": " + e.getMessage());
        }
    }
    
    private void reloadIsland(Player player, UUID playerUUID) {
        try {
            // Entlade die Insel zuerst
            SkyblockPlugin.getWorldManager().unloadPrivateIsland(playerUUID);
            
            // Lade sie neu
            var future = SkyblockPlugin.getWorldManager().loadPrivateIsland(playerUUID);
            future.thenAccept(island -> {
                if (island != null) {
                    player.teleport(island.getSpawnLocation());
                    player.sendMessage(Component.text("§aDeine Insel wurde neu geladen!"));
                } else {
                    player.sendMessage(Component.text("§cFehler beim Neuladen der Insel."));
                }
            }).exceptionally(throwable -> {
                player.sendMessage("§cFehler beim Neuladen der Insel: " + throwable.getMessage());
                SkyblockPlugin.getLogger().severe("Fehler beim Neuladen der Insel für " + player.getName() + ": " + throwable.getMessage());
                return null;
            });
        } catch (Exception e) {
            player.sendMessage("§cFehler beim Neuladen der Insel: " + e.getMessage());
            SkyblockPlugin.getLogger().severe("Fehler beim Neuladen der Insel für " + player.getName() + ": " + e.getMessage());
        }
    }
    
    private void showHelp(Player player) {
        player.sendMessage(Component.text("§6=== Insel Commands ==="));
        player.sendMessage(Component.text("§e/island §f- Teleportiere zu deiner Insel"));
        player.sendMessage(Component.text("§e/island home §f- Teleportiere zu deiner Insel"));
        player.sendMessage(Component.text("§e/island unload §f- Entlade deine Insel"));
        player.sendMessage(Component.text("§e/island reload §f- Lade deine Insel neu"));
    }
}
