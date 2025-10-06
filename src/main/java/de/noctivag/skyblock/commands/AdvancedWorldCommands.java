package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.worlds.ThreadSafeWorldManager;
import de.noctivag.skyblock.worlds.generators.CustomWorldGenerator;
import de.noctivag.skyblock.network.ExtendedServerTypes;
import org.bukkit.Bukkit;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

import java.io.File;
import java.util.*;

/**
 * Advanced World Commands - Erweiterte Befehle für das Multithreaded Welt-Management
 * 
 * Verantwortlich für:
 * - /advancedworlds list - Zeigt alle Welten mit Metriken
 * - /advancedworlds create <name> <type> - Erstellt eine neue Welt
 * - /advancedworlds load <path> - Lädt eine eigene Welt
 * - /advancedworlds unload <world> - Entlädt eine Welt
 * - /advancedworlds teleport <world> - Teleportiert zu einer Welt
 * - /advancedworlds metrics - Zeigt Performance-Metriken
 * - /advancedworlds custom <name> <config> - Erstellt Custom-Welt
 * - /advancedworlds upload <file> - Lädt Welt-Datei hoch
 */
public class AdvancedWorldCommands implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final ThreadSafeWorldManager worldManager;
    
    public AdvancedWorldCommands(SkyblockPlugin SkyblockPlugin, ThreadSafeWorldManager worldManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.worldManager = worldManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("basics.advancedworlds.admin")) {
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
            case "create":
                if (args.length < 3) {
                    sender.sendMessage(Component.text("§cUsage: /advancedworlds create <name> <type>"));
                    return true;
                }
                createWorld(sender, args[1], args[2]);
                break;
            case "load":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /advancedworlds load <path>"));
                    return true;
                }
                loadCustomWorld(sender, args[1]);
                break;
            case "unload":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /advancedworlds unload <world>"));
                    return true;
                }
                unloadWorld(sender, args[1]);
                break;
            case "teleport":
            case "tp":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /advancedworlds teleport <world>"));
                    return true;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("§cNur Spieler können sich teleportieren!"));
                    return true;
                }
                teleportToWorld((Player) sender, args[1]);
                break;
            case "metrics":
                showMetrics(sender);
                break;
            case "custom":
                if (args.length < 3) {
                    sender.sendMessage(Component.text("§cUsage: /advancedworlds custom <name> <config>"));
                    return true;
                }
                createCustomWorld(sender, args[1], args[2]);
                break;
            case "upload":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /advancedworlds upload <file>"));
                    return true;
                }
                uploadWorld(sender, args[1]);
                break;
            case "info":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /advancedworlds info <world>"));
                    return true;
                }
                showWorldInfo(sender, args[1]);
                break;
            case "performance":
                showPerformance(sender);
                break;
            case "help":
                showHelp(sender);
                break;
            default:
                sender.sendMessage(Component.text("§cUnbekannter Befehl! Verwende /advancedworlds help für Hilfe."));
                break;
        }
        
        return true;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== Advanced World Commands ==="));
        sender.sendMessage(Component.text("§7/advancedworlds list - Zeigt alle Welten mit Metriken"));
        sender.sendMessage(Component.text("§7/advancedworlds create <name> <type> - Erstellt eine neue Welt"));
        sender.sendMessage(Component.text("§7/advancedworlds load <path> - Lädt eine eigene Welt"));
        sender.sendMessage(Component.text("§7/advancedworlds unload <world> - Entlädt eine Welt"));
        sender.sendMessage(Component.text("§7/advancedworlds teleport <world> - Teleportiert zu einer Welt"));
        sender.sendMessage(Component.text("§7/advancedworlds metrics - Zeigt Performance-Metriken"));
        sender.sendMessage(Component.text("§7/advancedworlds custom <name> <config> - Erstellt Custom-Welt"));
        sender.sendMessage(Component.text("§7/advancedworlds upload <file> - Lädt Welt-Datei hoch"));
        sender.sendMessage(Component.text("§7/advancedworlds info <world> - Zeigt Welt-Informationen"));
        sender.sendMessage(Component.text("§7/advancedworlds performance - Zeigt Performance-Statistiken"));
        sender.sendMessage(Component.text("§7/advancedworlds help - Zeigt diese Hilfe"));
    }
    
    private void listWorlds(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== Advanced World List ==="));
        
        if (!worldManager.isInitialized()) {
            sender.sendMessage(Component.text("§cThreadSafeWorldManager ist nicht initialisiert!"));
            return;
        }
        
        // Zeige verwaltete Welten
        sender.sendMessage(Component.text("§e§lVerwaltete Welten:"));
        Set<String> managedWorldNames = worldManager.getManagedWorlds();
        for (String worldName : managedWorldNames) {
            org.bukkit.World world = worldManager.getWorld(worldName);
            
            if (world != null) {
                String status = "§aGeladen";
                int playerCount = world.getPlayers().size();
                int chunkCount = world.getLoadedChunks().length;
                
                sender.sendMessage(Component.text("§7- " + worldName + " " + status));
                sender.sendMessage(Component.text("§7  Spieler: §e" + playerCount + " §7| Chunks: §e" + chunkCount));
            } else {
                sender.sendMessage(Component.text("§7- " + worldName + " §cNicht geladen"));
            }
        }
        
        // Zeige Custom-Welten
        Map<String, Object> customWorlds = worldManager.getCustomWorlds();
        if (!customWorlds.isEmpty()) {
            sender.sendMessage(Component.text(""));
            sender.sendMessage(Component.text("§e§lCustom Welten:"));
            for (Map.Entry<String, Object> entry : customWorlds.entrySet()) {
                String worldName = entry.getKey();
                Object customInfo = entry.getValue();
                
                sender.sendMessage(Component.text("§7- " + worldName));
                sender.sendMessage(Component.text("§7  Info: §e" + customInfo.toString()));
            }
        }
        
        // Zeige alle verfügbaren Welten
        sender.sendMessage(Component.text(""));
        sender.sendMessage(Component.text("§e§lAlle verfügbaren Welten:"));
        for (org.bukkit.World world : SkyblockPlugin.getServer().getWorlds()) {
            String status = managedWorldNames.contains(world.getName()) ? "§aVerwaltet" : "§7Nicht verwaltet";
            sender.sendMessage(Component.text("§7- " + world.getName() + " " + status));
        }
    }
    
    private void createWorld(CommandSender sender, String worldName, String worldTypeStr) {
        sender.sendMessage(Component.text("§aErstelle Welt '" + worldName + "' vom Typ '" + worldTypeStr + "'..."));
        
        try {
            WorldType worldType = WorldType.valueOf(worldTypeStr.toUpperCase());
            
            // Erstelle World Config
            // ThreadSafeWorldManager.WorldConfig worldConfig = new ThreadSafeWorldManager.WorldConfig(
            //     worldName, 
            //     "Custom: " + worldName, 
            //     worldType, 
            //     new CustomWorldGenerator(),
            //     true, 
            //     false, 
            //     "Custom created world"
            // );
            
            // Erstelle Welt
            worldManager.loadWorld(worldName);
            sender.sendMessage(Component.text("§aWelt '" + worldName + "' wird erstellt..."));
            
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Component.text("§cUngültiger Welt-Typ: " + worldTypeStr));
            sender.sendMessage(Component.text("§7Verfügbare Typen: NORMAL, FLAT, AMPLIFIED, NETHER, THE_END"));
        }
    }
    
    private void loadCustomWorld(CommandSender sender, String worldPath) {
        sender.sendMessage(Component.text("§aLade Custom-Welt von Pfad: " + worldPath));
        
        File worldFolder = new File(worldPath);
        if (!worldFolder.exists()) {
            sender.sendMessage(Component.text("§cWelt-Ordner existiert nicht: " + worldPath));
            return;
        }
        
        if (!worldFolder.isDirectory()) {
            sender.sendMessage(Component.text("§cPfad ist kein Verzeichnis: " + worldPath));
            return;
        }
        
        // Extrahiere Welt-Name aus Pfad
        String worldName = worldFolder.getName();
        
        // Lade Welt asynchron
        worldManager.loadCustomWorld(worldName, worldPath, WorldType.NORMAL, null, null);
        sender.sendMessage(Component.text("§aCustom-Welt '" + worldName + "' wird geladen..."));
    }
    
    private void unloadWorld(CommandSender sender, String worldName) {
        sender.sendMessage(Component.text("§aEntlade Welt '" + worldName + "'..."));
        
        worldManager.unloadWorld(worldName);
        sender.sendMessage(Component.text("§aWelt '" + worldName + "' wird entladen..."));
    }
    
    private void teleportToWorld(Player player, String worldName) {
        // Check if world exists or can be created
        if (Bukkit.getWorld(worldName) == null) {
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
    
    private void showMetrics(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== World Performance Metrics ==="));
        
        if (!worldManager.isInitialized()) {
            sender.sendMessage(Component.text("§cThreadSafeWorldManager ist nicht initialisiert!"));
            return;
        }
        
        Object metrics = worldManager.getWorldMetrics();
        
        // TODO: Fix metrics checking when WorldMetrics class is implemented
        // if (metrics instanceof Map && ((Map<?, ?>) metrics).isEmpty()) {
        //     sender.sendMessage(Component.text("§7Keine Metriken verfügbar"));
        //     return;
        // }
        
        // TODO: Fix WorldMetrics class access
        // for (Map.Entry<String, ThreadSafeWorldManager.WorldMetrics> entry : metrics.entrySet()) {
        //     String worldName = entry.getKey();
        //     ThreadSafeWorldManager.WorldMetrics worldMetrics = entry.getValue();
        //     
        //     sender.sendMessage(Component.text("§e§l" + worldName + ":"));
        //     sender.sendMessage(Component.text("§7- Aktuelle Spieler: §e" + worldMetrics.getPlayerCount()));
        //     sender.sendMessage(Component.text("§7- Geladene Chunks: §e" + worldMetrics.getLoadedChunks()));
        //     sender.sendMessage(Component.text("§7- Durchschnittliche Spieler: §e" + String.format("%.1f", worldMetrics.getAveragePlayerCount())));
        //     sender.sendMessage(Component.text("§7- Durchschnittliche Chunks: §e" + String.format("%.1f", worldMetrics.getAverageChunkCount())));
        //     sender.sendMessage(Component.text("§7- Letztes Update: §e" + new java.util.Date(worldMetrics.getLastUpdate())));
        //     sender.sendMessage(Component.text(""));
        // }
        sender.sendMessage(Component.text("§7WorldMetrics display not yet implemented"));
    }
    
    private void createCustomWorld(CommandSender sender, String worldName, String configStr) {
        sender.sendMessage(Component.text("§aErstelle Custom-Welt '" + worldName + "' mit Konfiguration: " + configStr));
        
        try {
            // Parse Konfiguration (vereinfacht)
            Map<String, Object> customConfig = parseCustomConfig(configStr);
            
            // Erstelle Custom World Generator mit Konfiguration
            // CustomWorldGenerator.WorldGenerationConfig genConfig = new CustomWorldGenerator.WorldGenerationConfig();
            
            if (customConfig.containsKey("terrain_type")) {
                // genConfig.setTerrainType(CustomWorldGenerator.TerrainType.valueOf(
                //     customConfig.get("terrain_type").toString().toUpperCase()));
            }
            
            // if (customConfig.containsKey("base_height")) {
            //     genConfig.setBaseHeight(Integer.parseInt(customConfig.get("base_height").toString()));
            // }
            // 
            // if (customConfig.containsKey("height_variation")) {
            //     genConfig.setHeightVariation(Double.parseDouble(customConfig.get("height_variation").toString()));
            // }
            // 
            // CustomWorldGenerator generator = new CustomWorldGenerator(genConfig);
            
            // Erstelle Welt asynchron
            worldManager.loadCustomWorld(worldName, null, WorldType.NORMAL, null, customConfig);
            sender.sendMessage(Component.text("§aCustom-Welt '" + worldName + "' wird erstellt..."));
                
        } catch (Exception e) {
            sender.sendMessage(Component.text("§cFehler beim Parsen der Konfiguration: " + e.getMessage()));
        }
    }
    
    private void uploadWorld(CommandSender sender, String fileName) {
        sender.sendMessage(Component.text("§aLade Welt-Datei hoch: " + fileName));
        
        // Hier würde man die Welt-Datei verarbeiten
        // In einer echten Implementierung würde man:
        // 1. Datei validieren
        // 2. Entpacken
        // 3. Welt-Ordner erstellen
        // 4. Welt laden
        
        sender.sendMessage(Component.text("§eWelt-Upload-Funktion ist noch nicht implementiert"));
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
        sender.sendMessage(Component.text("§7Geladene Chunks: §e" + world.getLoadedChunks().length));
        
        // Zeige Game Rules
        sender.sendMessage(Component.text("§7Game Rules:"));
        sender.sendMessage(Component.text("§7- doDaylightCycle: §e" + world.getGameRuleValue(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE)));
        sender.sendMessage(Component.text("§7- doWeatherCycle: §e" + world.getGameRuleValue(org.bukkit.GameRule.DO_WEATHER_CYCLE)));
        sender.sendMessage(Component.text("§7- doMobSpawning: §e" + world.getGameRuleValue(org.bukkit.GameRule.DO_MOB_SPAWNING)));
        
        // Zeige Metriken falls verfügbar
        Object metrics = worldManager.getWorldMetrics();
        // TODO: Fix WorldMetrics class access
        // if (metrics instanceof Map) {
        //     Map<String, ThreadSafeWorldManager.WorldMetrics> metricsMap = (Map<String, ThreadSafeWorldManager.WorldMetrics>) metrics;
        //     ThreadSafeWorldManager.WorldMetrics worldMetrics = metricsMap.get(worldName);
        //     if (worldMetrics != null) {
        //         sender.sendMessage(Component.text(""));
        //         sender.sendMessage(Component.text("§7Performance Metriken:"));
        //         sender.sendMessage(Component.text("§7- Durchschnittliche Spieler: §e" + String.format("%.1f", worldMetrics.getAveragePlayerCount())));
        //         sender.sendMessage(Component.text("§7- Durchschnittliche Chunks: §e" + String.format("%.1f", worldMetrics.getAverageChunkCount())));
        //     }
        // }
        sender.sendMessage(Component.text("§7WorldMetrics display not yet implemented"));
    }
    
    private void showPerformance(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== Thread-Safe World Manager Performance ==="));
        
        if (!worldManager.isInitialized()) {
            sender.sendMessage(Component.text("§cThreadSafeWorldManager ist nicht initialisiert!"));
            return;
        }
        
        sender.sendMessage(Component.text("§7Status: §a" + (worldManager.isInitialized() ? "Initialisiert" : "Nicht initialisiert")));
        // sender.sendMessage(Component.text("§7Aktive Operationen: §e" + worldManager.getActiveOperations()));
        sender.sendMessage(Component.text("§7Verwaltete Welten: §e" + worldManager.getManagedWorlds().size()));
        sender.sendMessage(Component.text("§7Custom Welten: §e" + worldManager.getCustomWorlds().size()));
        // sender.sendMessage(Component.text("§7Welt-Konfigurationen: §e" + worldManager.getWorldConfigs().size()));
        
        // Zeige Server-Typen
        sender.sendMessage(Component.text(""));
        sender.sendMessage(Component.text("§e§lVerfügbare Server-Typen:"));
        ExtendedServerTypes.ExtendedServerType[] serverTypes = ExtendedServerTypes.getAllServerTypes();
        for (ExtendedServerTypes.ExtendedServerType serverType : serverTypes) {
            sender.sendMessage(Component.text("§7- " + serverType.name() + " §8(" + serverType.getDisplayName() + ")"));
        }
        
        // Zeige Custom-World-fähige Server-Typen
        sender.sendMessage(Component.text(""));
        sender.sendMessage(Component.text("§e§lCustom-World-fähige Server-Typen:"));
        ExtendedServerTypes.ExtendedServerType[] customTypes = ExtendedServerTypes.getCustomWorldSupportedTypes();
        for (ExtendedServerTypes.ExtendedServerType serverType : customTypes) {
            ExtendedServerTypes.ServerTypeConfig config = ExtendedServerTypes.getDefaultConfig(serverType);
            sender.sendMessage(Component.text("§7- " + serverType.name() + " §8(Max: " + config.getMaxCustomWorlds() + ")"));
        }
    }
    
    /**
     * Parst Custom-Konfiguration (vereinfacht)
     */
    private Map<String, Object> parseCustomConfig(String configStr) {
        Map<String, Object> config = new HashMap<>();
        
        // Einfache Key-Value-Parsing
        String[] pairs = configStr.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                
                // Versuche als Zahl zu parsen
                try {
                    if (value.contains(".")) {
                        config.put(key, Double.parseDouble(value));
                    } else {
                        config.put(key, Integer.parseInt(value));
                    }
                } catch (NumberFormatException e) {
                    config.put(key, value);
                }
            }
        }
        
        return config;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (!sender.hasPermission("basics.advancedworlds.admin")) {
            return completions;
        }
        
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("list", "create", "load", "unload", "teleport", "tp", 
                "metrics", "custom", "upload", "info", "performance", "help");
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                // Zeige verfügbare Welt-Typen
                for (WorldType worldType : WorldType.values()) {
                    if (worldType.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(worldType.name().toLowerCase());
                    }
                }
            } else if (args[0].equalsIgnoreCase("unload") || args[0].equalsIgnoreCase("teleport") || 
                      args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("info")) {
                // Zeige verfügbare Welten
                for (String worldName : worldManager.getManagedWorlds()) {
                    if (worldName.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(worldName);
                    }
                }
            } else             if (args[0].equalsIgnoreCase("custom")) {
                // Zeige verfügbare Terrain-Typen
                // for (CustomWorldGenerator.TerrainType terrainType : CustomWorldGenerator.TerrainType.values()) {
                //     if (terrainType.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                //         completions.add(terrainType.name().toLowerCase());
                //     }
                // }
                completions.add("normal");
                completions.add("flat");
                completions.add("amplified");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("custom")) {
                // Zeige Beispiel-Konfigurationen
                completions.add("terrain_type:normal,base_height:64,height_variation:16");
                completions.add("terrain_type:flat,flat_height:64");
                completions.add("terrain_type:amplified,base_height:80,height_variation:32");
            }
        }
        
        return completions;
    }
}
