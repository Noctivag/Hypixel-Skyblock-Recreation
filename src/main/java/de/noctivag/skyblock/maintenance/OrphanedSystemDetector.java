package de.noctivag.skyblock.maintenance;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * OrphanedSystemDetector - Erkennt ungenutzte Systeme und verwaiste Datenbank-Einträge
 * 
 * Features:
 * - Ungenutzte GUI-Systeme erkennen
 * - Verwaiste Datenbank-Einträge finden
 * - Ungenutzte Commands identifizieren
 * - Ungenutzte Permissions finden
 * - Auto-Cleanup-Funktionen
 */
public class OrphanedSystemDetector {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, List<String>> orphanedSystems = new ConcurrentHashMap<>();
    private final Map<String, List<String>> orphanedDatabaseEntries = new ConcurrentHashMap<>();
    private final Map<String, List<String>> orphanedCommands = new ConcurrentHashMap<>();
    private final Map<String, List<String>> orphanedPermissions = new ConcurrentHashMap<>();
    private final Map<String, List<String>> orphanedConfigs = new ConcurrentHashMap<>();
    
    public OrphanedSystemDetector(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    /**
     * Führt eine vollständige Überprüfung aller verwaisten Systeme durch
     */
    public void performFullCheck() {
        SkyblockPlugin.getLogger().info("§e[Maintenance] Starting orphaned system detection...");
        
        // Clear previous results
        orphanedSystems.clear();
        orphanedDatabaseEntries.clear();
        orphanedCommands.clear();
        orphanedPermissions.clear();
        orphanedConfigs.clear();
        
        // Check for orphaned systems
        checkOrphanedSystems();
        
        // Check for orphaned database entries
        checkOrphanedDatabaseEntries();
        
        // Check for orphaned commands
        checkOrphanedCommands();
        
        // Check for orphaned permissions
        checkOrphanedPermissions();
        
        // Check for orphaned configs
        checkOrphanedConfigs();
        
        // Generate report
        generateReport();
        
        SkyblockPlugin.getLogger().info("§a[Maintenance] Orphaned system detection completed!");
    }
    
    /**
     * Überprüft auf verwaiste Systeme
     */
    private void checkOrphanedSystems() {
        // Check GUI systems
        checkOrphanedGUISystems();
        
        // Check command systems
        checkOrphanedCommandSystems();
        
        // Check event systems
        checkOrphanedEventSystems();
        
        // Check manager systems
        checkOrphanedManagerSystems();
    }
    
    /**
     * Überprüft verwaiste GUI-Systeme
     */
    private void checkOrphanedGUISystems() {
        String[] guiClasses = {
            "de.noctivag.skyblock.gui.AdvancedGUISystem",
            "de.noctivag.skyblock.gui.UltimateMainMenu",
            "de.noctivag.skyblock.gui.FeatureBookGUI",
            "de.noctivag.skyblock.gui.AdminMenu",
            "de.noctivag.skyblock.gui.SettingsGUI",
            "de.noctivag.skyblock.gui.ModerationGUI",
            "de.noctivag.skyblock.gui.RestartGUI",
            "de.noctivag.skyblock.gui.QuickActionsGUI",
            "de.noctivag.skyblock.gui.ChatChannelsGUI",
            "de.noctivag.skyblock.gui.ReportsGUI",
            "de.noctivag.skyblock.gui.DiscordGUI",
            "de.noctivag.skyblock.gui.GuildSystemGUI",
            "de.noctivag.skyblock.gui.EventScheduleGUI",
            "de.noctivag.skyblock.gui.CosmeticsMenu",
            "de.noctivag.skyblock.gui.ParticleSettingsGUI",
            "de.noctivag.skyblock.gui.JoinMessagePresetsGUI",
            "de.noctivag.skyblock.gui.BankGUI",
            "de.noctivag.skyblock.gui.ShopGUI",
            "de.noctivag.skyblock.gui.StatisticsGUI",
            "de.noctivag.skyblock.gui.PartyGUI",
            "de.noctivag.skyblock.gui.RulesGUI",
            "de.noctivag.skyblock.gui.TournamentGUI",
            "de.noctivag.skyblock.gui.DuelSystemGUI",
            "de.noctivag.skyblock.gui.FriendsGUI",
            "de.noctivag.skyblock.gui.JoinMessageGUI",
            "de.noctivag.skyblock.gui.PvPArenaGUI",
            "de.noctivag.skyblock.gui.EventRewardsGUI",
            "de.noctivag.skyblock.gui.CustomGUI",
            "de.noctivag.skyblock.gui.WarpGUI",
            "de.noctivag.skyblock.gui.HelpGUI",
            "de.noctivag.skyblock.gui.SupportGUI",
            "de.noctivag.skyblock.gui.TeleportGUI",
            "de.noctivag.skyblock.gui.ServerInfoGUI",
            "de.noctivag.skyblock.gui.JobsGUI",
            "de.noctivag.skyblock.gui.LeaderboardGUI",
            "de.noctivag.skyblock.gui.EconomyGUI",
            "de.noctivag.skyblock.gui.AuctionHouseGUI",
            "de.noctivag.skyblock.gui.MobArenaGUI",
            "de.noctivag.skyblock.gui.KitShopGUI",
            "de.noctivag.skyblock.gui.WebsiteGUI",
            "de.noctivag.skyblock.gui.BattlePassGUI",
            "de.noctivag.skyblock.gui.QuestGUI",
            "de.noctivag.skyblock.gui.InfoGUI",
            "de.noctivag.skyblock.gui.MyCosmeticsGUI",
            "de.noctivag.skyblock.gui.CosmeticShopGUI",
            "de.noctivag.skyblock.gui.GadgetsGUI",
            "de.noctivag.skyblock.gui.MessagesMenu",
            "de.noctivag.skyblock.gui.EnhancedMainMenu",
            "de.noctivag.skyblock.gui.AnimatedGUI",
            "de.noctivag.skyblock.gui.UltimateEventGUI",
            "de.noctivag.skyblock.gui.UltimateGUISystem",
            "de.noctivag.skyblock.gui.CommandUsageGUI",
            "de.noctivag.skyblock.gui.FeatureToggleListener",
            "de.noctivag.skyblock.gui.PotatoBookGUI",
            "de.noctivag.skyblock.gui.RecombobulatorGUI",
            "de.noctivag.skyblock.gui.DungeonStarGUI",
            "de.noctivag.skyblock.gui.PetItemGUI",
            "de.noctivag.skyblock.gui.ArmorAbilityGUI",
            "de.noctivag.skyblock.gui.WeaponAbilityGUI",
            "de.noctivag.skyblock.gui.NPCCreationGUI",
            "de.noctivag.skyblock.gui.NPCManagementGUI",
            "de.noctivag.skyblock.gui.NPCEditGUI",
            "de.noctivag.skyblock.gui.EnhancedPetGUI"
        };
        
        for (String className : guiClasses) {
            try {
                Class<?> clazz = Class.forName(className);
                
                // Check if class has proper methods
                Method[] methods = clazz.getDeclaredMethods();
                boolean hasOpenMethod = false;
                boolean hasConstructor = false;
                
                for (Method method : methods) {
                    if (method.getName().equals("open") || method.getName().equals("openGUI")) {
                        hasOpenMethod = true;
                    }
                }
                
                // Check constructors
                if (clazz.getConstructors().length > 0) {
                    hasConstructor = true;
                }
                
                if (!hasOpenMethod) {
                    addOrphanedSystem("gui_no_open_method", className, "GUI class has no open method");
                }
                
                if (!hasConstructor) {
                    addOrphanedSystem("gui_no_constructor", className, "GUI class has no constructor");
                }
                
            } catch (ClassNotFoundException e) {
                addOrphanedSystem("gui_class_not_found", className, "GUI class not found");
            } catch (Exception e) {
                addOrphanedSystem("gui_class_error", className, "Error checking GUI class: " + e.getMessage());
            }
        }
    }
    
    /**
     * Überprüft verwaiste Command-Systeme
     */
    private void checkOrphanedCommandSystems() {
        String[] commandClasses = {
            "de.noctivag.skyblock.commands.AdvancedCommandSystem",
            "de.noctivag.skyblock.commands.HelpCommand",
            "de.noctivag.skyblock.commands.FeatureCommand",
            "de.noctivag.skyblock.commands.AdminCommand",
            "de.noctivag.skyblock.commands.MiningCommand",
            "de.noctivag.skyblock.commands.CollectionsCommand",
            "de.noctivag.skyblock.commands.PartyCommand",
            "de.noctivag.skyblock.commands.MobCommandSystem"
        };
        
        for (String className : commandClasses) {
            try {
                Class<?> clazz = Class.forName(className);
                
                // Check if class implements CommandExecutor
                boolean implementsCommandExecutor = false;
                for (Class<?> interfaceClass : clazz.getInterfaces()) {
                    if (interfaceClass.getSimpleName().equals("CommandExecutor")) {
                        implementsCommandExecutor = true;
                        break;
                    }
                }
                
                if (!implementsCommandExecutor) {
                    addOrphanedSystem("command_no_executor", className, "Command class does not implement CommandExecutor");
                }
                
            } catch (ClassNotFoundException e) {
                addOrphanedSystem("command_class_not_found", className, "Command class not found");
            } catch (Exception e) {
                addOrphanedSystem("command_class_error", className, "Error checking command class: " + e.getMessage());
            }
        }
    }
    
    /**
     * Überprüft verwaiste Event-Systeme
     */
    private void checkOrphanedEventSystems() {
        String[] eventClasses = {
            "de.noctivag.skyblock.events.AdvancedEventsSystem",
            "de.noctivag.skyblock.events.AdvancedEventSystem",
            "de.noctivag.skyblock.events.EventManager",
            "de.noctivag.skyblock.events.EventScheduler",
            "de.noctivag.skyblock.events.EventsSystem",
            "de.noctivag.skyblock.events.UltimateEventSystem",
            "de.noctivag.skyblock.events.Arena"
        };
        
        for (String className : eventClasses) {
            try {
                Class<?> clazz = Class.forName(className);
                
                // Check if class implements Listener
                boolean implementsListener = false;
                for (Class<?> interfaceClass : clazz.getInterfaces()) {
                    if (interfaceClass.getSimpleName().equals("Listener")) {
                        implementsListener = true;
                        break;
                    }
                }
                
                if (!implementsListener) {
                    addOrphanedSystem("event_no_listener", className, "Event class does not implement Listener");
                }
                
            } catch (ClassNotFoundException e) {
                addOrphanedSystem("event_class_not_found", className, "Event class not found");
            } catch (Exception e) {
                addOrphanedSystem("event_class_error", className, "Error checking event class: " + e.getMessage());
            }
        }
    }
    
    /**
     * Überprüft verwaiste Manager-Systeme
     */
    private void checkOrphanedManagerSystems() {
        String[] managerClasses = {
            "de.noctivag.skyblock.managers.RankManager",
            "de.noctivag.skyblock.managers.EventManager",
            "de.noctivag.skyblock.managers.TeleportManager",
            "de.noctivag.skyblock.managers.PartyManager"
        };
        
        for (String className : managerClasses) {
            try {
                Class<?> clazz = Class.forName(className);
                
                // Check if class has proper initialization
                boolean hasConstructor = clazz.getConstructors().length > 0;
                boolean hasPluginField = false;
                
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.getType().getSimpleName().equals("SkyblockPlugin")) {
                        hasPluginField = true;
                        break;
                    }
                }
                
                if (!hasConstructor) {
                    addOrphanedSystem("manager_no_constructor", className, "Manager class has no constructor");
                }
                
                if (!hasPluginField) {
                    addOrphanedSystem("manager_no_plugin_field", className, "Manager class has no SkyblockPlugin field");
                }
                
            } catch (ClassNotFoundException e) {
                addOrphanedSystem("manager_class_not_found", className, "Manager class not found");
            } catch (Exception e) {
                addOrphanedSystem("manager_class_error", className, "Error checking manager class: " + e.getMessage());
            }
        }
    }
    
    /**
     * Überprüft verwaiste Datenbank-Einträge
     */
    private void checkOrphanedDatabaseEntries() {
        // Check for orphaned player data
        checkOrphanedPlayerData();
        
        // Check for orphaned island data
        checkOrphanedIslandData();
        
        // Check for orphaned guild data
        checkOrphanedGuildData();
        
        // Check for orphaned auction data
        checkOrphanedAuctionData();
    }
    
    /**
     * Überprüft verwaiste Spieler-Daten
     */
    private void checkOrphanedPlayerData() {
        // This would need to be implemented based on your database structure
        // For now, we'll check config files
        
        File playersFile = new File(SkyblockPlugin.getDataFolder(), "players.yml");
        if (playersFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(playersFile);
            
            if (config.contains("players")) {
                for (String playerId : config.getConfigurationSection("players").getKeys(false)) {
                    String path = "players." + playerId;
                    
                    // Check if player has basic data
                    if (!config.contains(path + ".name")) {
                        addOrphanedDatabaseEntry("player_no_name", playerId, "Player has no name");
                    }
                    
                    if (!config.contains(path + ".lastSeen")) {
                        addOrphanedDatabaseEntry("player_no_last_seen", playerId, "Player has no last seen date");
                    }
                }
            }
        }
    }
    
    /**
     * Überprüft verwaiste Insel-Daten
     */
    private void checkOrphanedIslandData() {
        File islandsFile = new File(SkyblockPlugin.getDataFolder(), "islands.yml");
        if (islandsFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(islandsFile);
            
            if (config.contains("islands")) {
                for (String islandId : config.getConfigurationSection("islands").getKeys(false)) {
                    String path = "islands." + islandId;
                    
                    // Check if island has owner
                    if (!config.contains(path + ".owner")) {
                        addOrphanedDatabaseEntry("island_no_owner", islandId, "Island has no owner");
                    }
                    
                    // Check if island has location
                    if (!config.contains(path + ".location")) {
                        addOrphanedDatabaseEntry("island_no_location", islandId, "Island has no location");
                    }
                }
            }
        }
    }
    
    /**
     * Überprüft verwaiste Gilden-Daten
     */
    private void checkOrphanedGuildData() {
        File guildsFile = new File(SkyblockPlugin.getDataFolder(), "guilds.yml");
        if (guildsFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(guildsFile);
            
            if (config.contains("guilds")) {
                for (String guildId : config.getConfigurationSection("guilds").getKeys(false)) {
                    String path = "guilds." + guildId;
                    
                    // Check if guild has name
                    if (!config.contains(path + ".name")) {
                        addOrphanedDatabaseEntry("guild_no_name", guildId, "Guild has no name");
                    }
                    
                    // Check if guild has leader
                    if (!config.contains(path + ".leader")) {
                        addOrphanedDatabaseEntry("guild_no_leader", guildId, "Guild has no leader");
                    }
                }
            }
        }
    }
    
    /**
     * Überprüft verwaiste Auktions-Daten
     */
    private void checkOrphanedAuctionData() {
        File auctionsFile = new File(SkyblockPlugin.getDataFolder(), "auctions.yml");
        if (auctionsFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(auctionsFile);
            
            if (config.contains("auctions")) {
                for (String auctionId : config.getConfigurationSection("auctions").getKeys(false)) {
                    String path = "auctions." + auctionId;
                    
                    // Check if auction has seller
                    if (!config.contains(path + ".seller")) {
                        addOrphanedDatabaseEntry("auction_no_seller", auctionId, "Auction has no seller");
                    }
                    
                    // Check if auction has item
                    if (!config.contains(path + ".item")) {
                        addOrphanedDatabaseEntry("auction_no_item", auctionId, "Auction has no item");
                    }
                    
                    // Check if auction has end time
                    if (!config.contains(path + ".endTime")) {
                        addOrphanedDatabaseEntry("auction_no_end_time", auctionId, "Auction has no end time");
                    }
                }
            }
        }
    }
    
    /**
     * Überprüft verwaiste Commands
     */
    private void checkOrphanedCommands() {
        // Check SkyblockPlugin.yml for commands that might not be used
        File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
        if (pluginFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
            
            if (config.contains("commands")) {
                for (String commandName : config.getConfigurationSection("commands").getKeys(false)) {
                    String path = "commands." + commandName;
                    
                    // Check if command has executor
                    if (!config.contains(path + ".executor")) {
                        addOrphanedCommand("no_executor", commandName, "Command has no executor");
                    }
                    
                    // Check if command has description
                    if (!config.contains(path + ".description")) {
                        addOrphanedCommand("no_description", commandName, "Command has no description");
                    }
                }
            }
        }
    }
    
    /**
     * Überprüft verwaiste Permissions
     */
    private void checkOrphanedPermissions() {
        // Check SkyblockPlugin.yml for permissions that might not be used
        File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
        if (pluginFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
            
            if (config.contains("permissions")) {
                for (String permissionName : config.getConfigurationSection("permissions").getKeys(false)) {
                    String path = "permissions." + permissionName;
                    
                    // Check if permission has description
                    if (!config.contains(path + ".description")) {
                        addOrphanedPermission("no_description", permissionName, "Permission has no description");
                    }
                    
                    // Check if permission has default value
                    if (!config.contains(path + ".default")) {
                        addOrphanedPermission("no_default", permissionName, "Permission has no default value");
                    }
                }
            }
        }
    }
    
    /**
     * Überprüft verwaiste Config-Dateien
     */
    private void checkOrphanedConfigs() {
        File dataFolder = SkyblockPlugin.getDataFolder();
        if (dataFolder.exists()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".yml")) {
                        // Check if config file is empty or has no content
                        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                        if (config.getKeys(false).isEmpty()) {
                            addOrphanedConfig("empty_config", file.getName(), "Config file is empty");
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Fügt ein verwaistes System hinzu
     */
    private void addOrphanedSystem(String type, String name, String reason) {
        orphanedSystems.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt einen verwaisten Datenbank-Eintrag hinzu
     */
    private void addOrphanedDatabaseEntry(String type, String name, String reason) {
        orphanedDatabaseEntries.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt einen verwaisten Command hinzu
     */
    private void addOrphanedCommand(String type, String name, String reason) {
        orphanedCommands.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt eine verwaiste Permission hinzu
     */
    private void addOrphanedPermission(String type, String name, String reason) {
        orphanedPermissions.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt eine verwaiste Config hinzu
     */
    private void addOrphanedConfig(String type, String name, String reason) {
        orphanedConfigs.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Generiert einen Bericht über alle gefundenen verwaisten Systeme
     */
    private void generateReport() {
        SkyblockPlugin.getLogger().info("§6=== ORPHANED SYSTEM DETECTION REPORT ===");
        
        // Orphaned systems
        if (!orphanedSystems.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cOrphaned Systems:");
            for (Map.Entry<String, List<String>> entry : orphanedSystems.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Orphaned database entries
        if (!orphanedDatabaseEntries.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cOrphaned Database Entries:");
            for (Map.Entry<String, List<String>> entry : orphanedDatabaseEntries.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Orphaned commands
        if (!orphanedCommands.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cOrphaned Commands:");
            for (Map.Entry<String, List<String>> entry : orphanedCommands.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Orphaned permissions
        if (!orphanedPermissions.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cOrphaned Permissions:");
            for (Map.Entry<String, List<String>> entry : orphanedPermissions.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Orphaned configs
        if (!orphanedConfigs.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cOrphaned Configs:");
            for (Map.Entry<String, List<String>> entry : orphanedConfigs.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Summary
        int totalIssues = orphanedSystems.values().stream().mapToInt(List::size).sum() +
                         orphanedDatabaseEntries.values().stream().mapToInt(List::size).sum() +
                         orphanedCommands.values().stream().mapToInt(List::size).sum() +
                         orphanedPermissions.values().stream().mapToInt(List::size).sum() +
                         orphanedConfigs.values().stream().mapToInt(List::size).sum();
        
        if (totalIssues == 0) {
            SkyblockPlugin.getLogger().info("§aNo orphaned systems found! System is clean.");
        } else {
            SkyblockPlugin.getLogger().warning("§cTotal orphaned items found: " + totalIssues);
        }
        
        SkyblockPlugin.getLogger().info("§6==========================================");
    }
    
    /**
     * Automatische Bereinigung von verwaisten Systemen
     */
    public void autoCleanup() {
        SkyblockPlugin.getLogger().info("§e[Maintenance] Starting auto-cleanup for orphaned systems...");
        
        int cleanedItems = 0;
        
        // Cleanup orphaned configs
        cleanedItems += cleanupOrphanedConfigs();
        
        // Cleanup orphaned database entries
        cleanedItems += cleanupOrphanedDatabaseEntries();
        
        SkyblockPlugin.getLogger().info("§a[Maintenance] Auto-cleanup completed! Cleaned " + cleanedItems + " items.");
    }
    
    /**
     * Bereinigt verwaiste Config-Dateien
     */
    private int cleanupOrphanedConfigs() {
        int cleaned = 0;
        
        File dataFolder = SkyblockPlugin.getDataFolder();
        if (dataFolder.exists()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".yml")) {
                        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                        if (config.getKeys(false).isEmpty()) {
                            if (file.delete()) {
                                cleaned++;
                                SkyblockPlugin.getLogger().info("§aDeleted empty config file: " + file.getName());
                            }
                        }
                    }
                }
            }
        }
        
        return cleaned;
    }
    
    /**
     * Bereinigt verwaiste Datenbank-Einträge
     */
    private int cleanupOrphanedDatabaseEntries() {
        int cleaned = 0;
        
        // This would need to be implemented based on your database structure
        // For now, we'll just log what would be cleaned
        
        for (Map.Entry<String, List<String>> entry : orphanedDatabaseEntries.entrySet()) {
            for (String issue : entry.getValue()) {
                SkyblockPlugin.getLogger().info("§eWould clean: " + issue);
                cleaned++;
            }
        }
        
        return cleaned;
    }
    
    /**
     * Gibt Statistiken über gefundene verwaiste Systeme zurück
     */
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("orphaned_systems", orphanedSystems.values().stream().mapToInt(List::size).sum());
        stats.put("orphaned_database_entries", orphanedDatabaseEntries.values().stream().mapToInt(List::size).sum());
        stats.put("orphaned_commands", orphanedCommands.values().stream().mapToInt(List::size).sum());
        stats.put("orphaned_permissions", orphanedPermissions.values().stream().mapToInt(List::size).sum());
        stats.put("orphaned_configs", orphanedConfigs.values().stream().mapToInt(List::size).sum());
        
        int total = stats.values().stream().mapToInt(Integer::intValue).sum();
        stats.put("total", total);
        
        return stats;
    }
    
    /**
     * Erstellt ein GUI-Item für den Orphaned-System-Report
     */
    public ItemStack createReportItem() {
        ItemStack item = new ItemStack(org.bukkit.Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            Map<String, Integer> stats = getStatistics();
            int totalIssues = stats.get("total");
            
            meta.displayName(Component.text("§c§lOrphaned Systems Report"));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Total Orphaned Items: §c" + totalIssues);
            lore.add("§7Orphaned Systems: §e" + stats.get("orphaned_systems"));
            lore.add("§7Orphaned Database Entries: §e" + stats.get("orphaned_database_entries"));
            lore.add("§7Orphaned Commands: §e" + stats.get("orphaned_commands"));
            lore.add("§7Orphaned Permissions: §e" + stats.get("orphaned_permissions"));
            lore.add("§7Orphaned Configs: §e" + stats.get("orphaned_configs"));
            lore.add("");
            lore.add("§eClick to view detailed report");
            lore.add("§eRight-click to auto-cleanup");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
}
