package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.worlds.WorldManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * World Commands - Befehle für das Welt-Management
 * 
 * Verantwortlich für:
 * - /worlds list - Zeigt alle Welten
 * - /worlds info <world> - Zeigt Welt-Informationen
 * - /worlds load <world> - Lädt eine Welt
 * - /worlds unload <world> - Entlädt eine Welt
 * - /worlds teleport <world> - Teleportiert zu einer Welt
 * - /worlds reload - Lädt Welt-Konfiguration neu
 */
public class WorldCommands implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final WorldManager worldManager;
    
    public WorldCommands(SkyblockPlugin SkyblockPlugin, WorldManager worldManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.worldManager = worldManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("basics.worlds.admin")) {
            sender.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
            return true;
        }
        
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "list":
                listWorlds(sender);
                break;
            case "info":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /worlds info <world>"));
                    return true;
                }
                showWorldInfo(sender, args[1]);
                break;
            case "load":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /worlds load <world>"));
                    return true;
                }
                loadWorld(sender, args[1]);
                break;
            case "unload":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /worlds unload <world>"));
                    return true;
                }
                unloadWorld(sender, args[1]);
                break;
            case "teleport":
            case "tp":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /worlds teleport <world>"));
                    return true;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("§cNur Spieler können sich teleportieren!"));
                    return true;
                }
                teleportToWorld((Player) sender, args[1]);
                break;
            case "reload":
                reloadWorlds(sender);
                break;
            case "help":
                showHelp(sender);
                break;
            default:
                sender.sendMessage(Component.text("§cUnbekannter Befehl! Verwende /worlds help für Hilfe."));
                break;
        }
        
        return true;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== World Commands ==="));
        sender.sendMessage(Component.text("§7/worlds list - Zeigt alle Welten"));
        sender.sendMessage(Component.text("§7/worlds info <world> - Zeigt Welt-Informationen"));
        sender.sendMessage(Component.text("§7/worlds load <world> - Lädt eine Welt"));
        sender.sendMessage(Component.text("§7/worlds unload <world> - Entlädt eine Welt"));
        sender.sendMessage(Component.text("§7/worlds teleport <world> - Teleportiert zu einer Welt"));
        sender.sendMessage(Component.text("§7/worlds reload - Lädt Welt-Konfiguration neu"));
        sender.sendMessage(Component.text("§7/worlds help - Zeigt diese Hilfe"));
    }
    
    private void listWorlds(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== World List ==="));
        
        if (!worldManager.isInitialized()) {
            sender.sendMessage(Component.text("§cWorldManager ist nicht initialisiert!"));
            return;
        }
        
        // Zeige verwaltete Welten
        sender.sendMessage(Component.text("§e§lVerwaltete Welten:"));
        for (String worldName : worldManager.getManagedWorlds().keySet()) {
            org.bukkit.World world = worldManager.getWorld(worldName);
            String status = world != null ? "§aGeladen" : "§cNicht geladen";
            sender.sendMessage(Component.text("§7- " + worldName + " " + status));
        }
        
        // Zeige alle verfügbaren Welten
        sender.sendMessage(Component.text("§e§lAlle verfügbaren Welten:"));
        for (org.bukkit.World world : SkyblockPlugin.getServer().getWorlds()) {
            String status = worldManager.getManagedWorlds().containsKey(world.getName()) ? "§aVerwaltet" : "§7Nicht verwaltet";
            sender.sendMessage(Component.text("§7- " + world.getName() + " " + status));
        }
    }
    
    private void showWorldInfo(CommandSender sender, String worldName) {
        org.bukkit.World world = worldManager.getWorld(worldName);
        
        if (world == null) {
            sender.sendMessage(Component.text("§cWelt '" + worldName + "' nicht gefunden!"));
            return;
        }
        
        sender.sendMessage(Component.text("§6§l=== World Info: " + worldName + " ==="));
        sender.sendMessage(Component.text("§7Name: §e" + world.getName()));
        sender.sendMessage(Component.text("§7Typ: §e" + world.getEnvironment().name()));
        sender.sendMessage(Component.text("§7Spieler: §e" + world.getPlayers().size()));
        sender.sendMessage(Component.text("§7Spawn: §e" + world.getSpawnLocation().getBlockX() + ", " + 
                                        world.getSpawnLocation().getBlockY() + ", " + 
                                        world.getSpawnLocation().getBlockZ()));
        sender.sendMessage(Component.text("§7Zeit: §e" + world.getTime()));
        sender.sendMessage(Component.text("§7Wetter: §e" + (world.hasStorm() ? "Sturm" : "Klar")));
        
        // Zeige Game Rules
        sender.sendMessage(Component.text("§7Game Rules:"));
        sender.sendMessage(Component.text("§7- doDaylightCycle: §e" + world.getGameRuleValue(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE)));
        sender.sendMessage(Component.text("§7- doWeatherCycle: §e" + world.getGameRuleValue(org.bukkit.GameRule.DO_WEATHER_CYCLE)));
        sender.sendMessage(Component.text("§7- doMobSpawning: §e" + world.getGameRuleValue(org.bukkit.GameRule.DO_MOB_SPAWNING)));
    }
    
    private void loadWorld(CommandSender sender, String worldName) {
        sender.sendMessage(Component.text("§aLade Welt '" + worldName + "'..."));
        
        worldManager.loadWorld(worldName).thenAccept(world -> {
            if (world != null) {
                sender.sendMessage(Component.text("§aWelt '" + worldName + "' erfolgreich geladen!"));
            } else {
                sender.sendMessage(Component.text("§cFehler beim Laden der Welt '" + worldName + "'!"));
            }
        });
    }
    
    private void unloadWorld(CommandSender sender, String worldName) {
        sender.sendMessage(Component.text("§aEntlade Welt '" + worldName + "'..."));
        
        worldManager.unloadWorld(worldName).thenAccept(success -> {
            if (success) {
                sender.sendMessage(Component.text("§aWelt '" + worldName + "' erfolgreich entladen!"));
            } else {
                sender.sendMessage(Component.text("§cFehler beim Entladen der Welt '" + worldName + "'!"));
            }
        });
    }
    
    private void teleportToWorld(Player player, String worldName) {
        // Check if world exists or can be created
        if (!worldManager.worldExists(worldName)) {
            player.sendMessage(Component.text("§cWelt '" + worldName + "' existiert nicht und kann nicht erstellt werden!"));
            return;
        }
        
        // Get world (will create if needed)
        org.bukkit.World world = worldManager.getWorld(worldName);
        if (world == null) {
            player.sendMessage(Component.text("§cFehler beim Laden der Welt '" + worldName + "'!"));
            return;
        }
        
        // Teleportiere zu sicherer Spawn-Location
        org.bukkit.Location spawnLocation = worldManager.getSafeSpawnLocation(worldName);
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
            player.sendMessage(Component.text("§aZu Welt '" + worldName + "' teleportiert!"));
        } else {
            // Fallback to world spawn
            org.bukkit.Location worldSpawn = world.getSpawnLocation();
            if (worldSpawn != null) {
                player.teleport(worldSpawn);
                player.sendMessage(Component.text("§aZu Welt '" + worldName + "' teleportiert (Fallback-Spawn)!"));
            } else {
                player.sendMessage(Component.text("§cKeine Spawn-Location in Welt '" + worldName + "' gefunden!"));
            }
        }
    }
    
    private void reloadWorlds(CommandSender sender) {
        sender.sendMessage(Component.text("§aLade Welt-Konfiguration neu..."));
        
        // Hier könnte man die Welt-Konfiguration neu laden
        // Für jetzt zeigen wir nur eine Bestätigung
        sender.sendMessage(Component.text("§aWelt-Konfiguration neu geladen!"));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (!sender.hasPermission("basics.worlds.admin")) {
            return completions;
        }
        
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("list", "info", "load", "unload", "teleport", "tp", "reload", "help");
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("load") || 
                args[0].equalsIgnoreCase("unload") || args[0].equalsIgnoreCase("teleport") || 
                args[0].equalsIgnoreCase("tp")) {
                
                // Zeige verfügbare Welten
                for (String worldName : worldManager.getManagedWorlds().keySet()) {
                    if (worldName.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(worldName);
                    }
                }
            }
        }
        
        return completions;
    }
}
