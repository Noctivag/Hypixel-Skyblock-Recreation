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
 * UnusedResourceDetector - Erkennt ungenutzte Ressourcen und verwaiste Dateien
 * 
 * Features:
 * - Ungenutzte Config-Dateien erkennen
 * - Verwaiste Datenbank-Tabellen finden
 * - Ungenutzte Permissions identifizieren
 * - Ungenutzte Commands finden
 * - Auto-Cleanup-Funktionen
 */
public class UnusedResourceDetector {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, List<String>> unusedConfigs = new ConcurrentHashMap<>();
    private final Map<String, List<String>> unusedDatabaseTables = new ConcurrentHashMap<>();
    private final Map<String, List<String>> unusedPermissions = new ConcurrentHashMap<>();
    private final Map<String, List<String>> unusedCommands = new ConcurrentHashMap<>();
    private final Map<String, List<String>> unusedFiles = new ConcurrentHashMap<>();
    private final Map<String, List<String>> unusedClasses = new ConcurrentHashMap<>();
    
    public UnusedResourceDetector(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    /**
     * Führt eine vollständige Überprüfung aller ungenutzten Ressourcen durch
     */
    public void performFullCheck() {
        SkyblockPlugin.getLogger().info("§e[Maintenance] Starting unused resource detection...");
        
        // Clear previous results
        unusedConfigs.clear();
        unusedDatabaseTables.clear();
        unusedPermissions.clear();
        unusedCommands.clear();
        unusedFiles.clear();
        unusedClasses.clear();
        
        // Check for unused configs
        checkUnusedConfigs();
        
        // Check for unused database tables
        checkUnusedDatabaseTables();
        
        // Check for unused permissions
        checkUnusedPermissions();
        
        // Check for unused commands
        checkUnusedCommands();
        
        // Check for unused files
        checkUnusedFiles();
        
        // Check for unused classes
        checkUnusedClasses();
        
        // Generate report
        generateReport();
        
        SkyblockPlugin.getLogger().info("§a[Maintenance] Unused resource detection completed!");
    }
    
    /**
     * Überprüft ungenutzte Config-Dateien
     */
    private void checkUnusedConfigs() {
        File dataFolder = SkyblockPlugin.getDataFolder();
        if (dataFolder.exists()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".yml")) {
                        checkConfigFile(file);
                    }
                }
            }
        }
    }
    
    /**
     * Überprüft eine einzelne Config-Datei
     */
    private void checkConfigFile(File configFile) {
        try {
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            String fileName = configFile.getName();
            
            // Check if config is empty
            if (config.getKeys(false).isEmpty()) {
                addUnusedConfig("empty_config", fileName, "Config file is empty");
                return;
            }
            
            // Check if config has only default values
            if (isDefaultConfig(config)) {
                addUnusedConfig("default_config", fileName, "Config file contains only default values");
            }
            
            // Check if config has unused sections
            checkConfigSections(config, fileName);
            
        } catch (Exception e) {
            addUnusedConfig("error_reading", configFile.getName(), "Error reading config file: " + e.getMessage());
        }
    }
    
    /**
     * Überprüft ob eine Config nur Standardwerte enthält
     */
    private boolean isDefaultConfig(FileConfiguration config) {
        // Check for common default patterns
        Set<String> keys = config.getKeys(false);
        
        // If config has very few keys, it might be unused
        if (keys.size() < 3) {
            return true;
        }
        
        // Check for common default values
        for (String key : keys) {
            Object value = config.get(key);
            if (value instanceof String) {
                String stringValue = (String) value;
                if (stringValue.equals("default") || stringValue.equals("example") || stringValue.isEmpty()) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Überprüft Config-Sektionen auf Nutzung
     */
    private void checkConfigSections(FileConfiguration config, String fileName) {
        for (String key : config.getKeys(false)) {
            // Check if section is referenced in code
            if (!isConfigSectionUsed(key, fileName)) {
                addUnusedConfig("unused_section", fileName + "." + key, "Config section is not used");
            }
        }
    }
    
    /**
     * Überprüft ob eine Config-Sektion verwendet wird
     */
    private boolean isConfigSectionUsed(String section, String fileName) {
        // This is a simplified check - in a real implementation, you would
        // scan the codebase for references to this config section
        
        // Check common patterns
        String[] commonSections = {
            "database", "mysql", "mariadb", "redis", "cache", "backup",
            "economy", "ranks", "permissions", "commands", "gui", "events",
            "skills", "collections", "minions", "pets", "guilds", "auctions",
            "bazaar", "dungeons", "slayers", "fishing", "foraging", "mining",
            "combat", "enchanting", "alchemy", "taming", "carpentry", "runecrafting"
        };
        
        for (String commonSection : commonSections) {
            if (section.toLowerCase().contains(commonSection)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Überprüft ungenutzte Datenbank-Tabellen
     */
    private void checkUnusedDatabaseTables() {
        // Check for database tables that might not be used
        String[] potentialTables = {
            "server_info", "player_profiles", "skyblock_islands", "island_members",
            "player_skills", "player_collections", "player_slayers", "player_minions",
            "player_pets", "auction_house", "bazaar_orders", "player_dungeons",
            "server_events", "guilds", "guild_members", "leaderboards", "player_ranks",
            "player_permissions", "player_homes", "player_warps", "player_friends",
            "player_parties", "player_achievements", "player_rewards", "player_cosmetics",
            "player_settings", "player_statistics", "player_logs", "player_sessions"
        };
        
        for (String table : potentialTables) {
            if (!isDatabaseTableUsed(table)) {
                addUnusedDatabaseTable("unused_table", table, "Database table is not used");
            }
        }
    }
    
    /**
     * Überprüft ob eine Datenbank-Tabelle verwendet wird
     */
    private boolean isDatabaseTableUsed(String tableName) {
        // This is a simplified check - in a real implementation, you would
        // scan the codebase for references to this table
        
        // Check if table is referenced in database manager
        try {
            Class<?> dbManagerClass = Class.forName("de.noctivag.skyblock.data.DatabaseManager");
            Method[] methods = dbManagerClass.getDeclaredMethods();
            
            for (Method method : methods) {
                if (method.getName().toLowerCase().contains(tableName.toLowerCase())) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            // DatabaseManager not found, assume table is unused
        }
        
        return false;
    }
    
    /**
     * Überprüft ungenutzte Permissions
     */
    private void checkUnusedPermissions() {
        // Check SkyblockPlugin.yml for permissions
        File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
        if (pluginFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
            
            if (config.contains("permissions")) {
                for (String permissionName : config.getConfigurationSection("permissions").getKeys(false)) {
                    if (!isPermissionUsed(permissionName)) {
                        addUnusedPermission("unused_permission", permissionName, "Permission is not used");
                    }
                }
            }
        }
    }
    
    /**
     * Überprüft ob eine Permission verwendet wird
     */
    private boolean isPermissionUsed(String permissionName) {
        // This is a simplified check - in a real implementation, you would
        // scan the codebase for references to this permission
        
        // Check if permission is referenced in commands
        try {
            File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
            if (pluginFile.exists()) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
                
                if (config.contains("commands")) {
                    for (String commandName : config.getConfigurationSection("commands").getKeys(false)) {
                        String commandPermission = config.getString("commands." + commandName + ".permission");
                        if (permissionName.equals(commandPermission)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Error reading SkyblockPlugin.yml
        }
        
        return false;
    }
    
    /**
     * Überprüft ungenutzte Commands
     */
    private void checkUnusedCommands() {
        // Check SkyblockPlugin.yml for commands
        File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
        if (pluginFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
            
            if (config.contains("commands")) {
                for (String commandName : config.getConfigurationSection("commands").getKeys(false)) {
                    if (!isCommandUsed(commandName)) {
                        addUnusedCommand("unused_command", commandName, "Command is not used");
                    }
                }
            }
        }
    }
    
    /**
     * Überprüft ob ein Command verwendet wird
     */
    private boolean isCommandUsed(String commandName) {
        // This is a simplified check - in a real implementation, you would
        // scan the codebase for references to this command
        
        // Check if command has executor
        try {
            File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
            if (pluginFile.exists()) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
                
                String executor = config.getString("commands." + commandName + ".executor");
                if (executor != null && !executor.isEmpty()) {
                    // Check if executor class exists
                    try {
                        Class.forName(executor);
                        return true;
                    } catch (ClassNotFoundException e) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            // Error reading SkyblockPlugin.yml
        }
        
        return false;
    }
    
    /**
     * Überprüft ungenutzte Dateien
     */
    private void checkUnusedFiles() {
        File dataFolder = SkyblockPlugin.getDataFolder();
        if (dataFolder.exists()) {
            checkDirectory(dataFolder);
        }
    }
    
    /**
     * Überprüft ein Verzeichnis auf ungenutzte Dateien
     */
    private void checkDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    checkDirectory(file);
                } else {
                    checkFile(file);
                }
            }
        }
    }
    
    /**
     * Überprüft eine einzelne Datei
     */
    private void checkFile(File file) {
        String fileName = file.getName();
        
        // Check for common unused file patterns
        if (fileName.endsWith(".tmp") || fileName.endsWith(".bak") || fileName.endsWith(".old")) {
            addUnusedFile("temporary_file", fileName, "Temporary or backup file");
        }
        
        // Check for empty files
        if (file.length() == 0) {
            addUnusedFile("empty_file", fileName, "File is empty");
        }
        
        // Check for very small files (might be unused)
        if (file.length() < 10) {
            addUnusedFile("small_file", fileName, "File is very small (might be unused)");
        }
    }
    
    /**
     * Überprüft ungenutzte Klassen
     */
    private void checkUnusedClasses() {
        // Check for classes that might not be used
        String[] potentialClasses = {
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
        
        for (String className : potentialClasses) {
            try {
                Class<?> clazz = Class.forName(className);
                
                // Check if class is instantiated anywhere
                if (!isClassUsed(clazz)) {
                    addUnusedClass("unused_class", className, "Class is not used");
                }
                
            } catch (ClassNotFoundException e) {
                addUnusedClass("class_not_found", className, "Class not found");
            } catch (Exception e) {
                addUnusedClass("class_error", className, "Error checking class: " + e.getMessage());
            }
        }
    }
    
    /**
     * Überprüft ob eine Klasse verwendet wird
     */
    private boolean isClassUsed(Class<?> clazz) {
        // This is a simplified check - in a real implementation, you would
        // scan the codebase for references to this class
        
        // Check if class has public constructors
        if (clazz.getConstructors().length > 0) {
            return true;
        }
        
        // Check if class has public methods
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getModifiers() == java.lang.reflect.Modifier.PUBLIC) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Fügt eine ungenutzte Config hinzu
     */
    private void addUnusedConfig(String type, String name, String reason) {
        unusedConfigs.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt eine ungenutzte Datenbank-Tabelle hinzu
     */
    private void addUnusedDatabaseTable(String type, String name, String reason) {
        unusedDatabaseTables.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt eine ungenutzte Permission hinzu
     */
    private void addUnusedPermission(String type, String name, String reason) {
        unusedPermissions.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt einen ungenutzten Command hinzu
     */
    private void addUnusedCommand(String type, String name, String reason) {
        unusedCommands.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt eine ungenutzte Datei hinzu
     */
    private void addUnusedFile(String type, String name, String reason) {
        unusedFiles.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Fügt eine ungenutzte Klasse hinzu
     */
    private void addUnusedClass(String type, String name, String reason) {
        unusedClasses.computeIfAbsent(type, k -> new ArrayList<>()).add(name + " - " + reason);
    }
    
    /**
     * Generiert einen Bericht über alle gefundenen ungenutzten Ressourcen
     */
    private void generateReport() {
        SkyblockPlugin.getLogger().info("§6=== UNUSED RESOURCE DETECTION REPORT ===");
        
        // Unused configs
        if (!unusedConfigs.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cUnused Configs:");
            for (Map.Entry<String, List<String>> entry : unusedConfigs.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Unused database tables
        if (!unusedDatabaseTables.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cUnused Database Tables:");
            for (Map.Entry<String, List<String>> entry : unusedDatabaseTables.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Unused permissions
        if (!unusedPermissions.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cUnused Permissions:");
            for (Map.Entry<String, List<String>> entry : unusedPermissions.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Unused commands
        if (!unusedCommands.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cUnused Commands:");
            for (Map.Entry<String, List<String>> entry : unusedCommands.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Unused files
        if (!unusedFiles.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cUnused Files:");
            for (Map.Entry<String, List<String>> entry : unusedFiles.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Unused classes
        if (!unusedClasses.isEmpty()) {
            SkyblockPlugin.getLogger().warning("§cUnused Classes:");
            for (Map.Entry<String, List<String>> entry : unusedClasses.entrySet()) {
                SkyblockPlugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    SkyblockPlugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Summary
        int totalIssues = unusedConfigs.values().stream().mapToInt(List::size).sum() +
                         unusedDatabaseTables.values().stream().mapToInt(List::size).sum() +
                         unusedPermissions.values().stream().mapToInt(List::size).sum() +
                         unusedCommands.values().stream().mapToInt(List::size).sum() +
                         unusedFiles.values().stream().mapToInt(List::size).sum() +
                         unusedClasses.values().stream().mapToInt(List::size).sum();
        
        if (totalIssues == 0) {
            SkyblockPlugin.getLogger().info("§aNo unused resources found! System is optimized.");
        } else {
            SkyblockPlugin.getLogger().warning("§cTotal unused resources found: " + totalIssues);
        }
        
        SkyblockPlugin.getLogger().info("§6==========================================");
    }
    
    /**
     * Automatische Bereinigung von ungenutzten Ressourcen
     */
    public void autoCleanup() {
        SkyblockPlugin.getLogger().info("§e[Maintenance] Starting auto-cleanup for unused resources...");
        
        int cleanedItems = 0;
        
        // Cleanup unused files
        cleanedItems += cleanupUnusedFiles();
        
        // Cleanup unused configs
        cleanedItems += cleanupUnusedConfigs();
        
        SkyblockPlugin.getLogger().info("§a[Maintenance] Auto-cleanup completed! Cleaned " + cleanedItems + " items.");
    }
    
    /**
     * Bereinigt ungenutzte Dateien
     */
    private int cleanupUnusedFiles() {
        int cleaned = 0;
        
        File dataFolder = SkyblockPlugin.getDataFolder();
        if (dataFolder.exists()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        
                        // Delete temporary files
                        if (fileName.endsWith(".tmp") || fileName.endsWith(".bak") || fileName.endsWith(".old")) {
                            if (file.delete()) {
                                cleaned++;
                                SkyblockPlugin.getLogger().info("§aDeleted temporary file: " + fileName);
                            }
                        }
                        
                        // Delete empty files
                        if (file.length() == 0) {
                            if (file.delete()) {
                                cleaned++;
                                SkyblockPlugin.getLogger().info("§aDeleted empty file: " + fileName);
                            }
                        }
                    }
                }
            }
        }
        
        return cleaned;
    }
    
    /**
     * Bereinigt ungenutzte Configs
     */
    private int cleaned = 0;
    
    private int cleanupUnusedConfigs() {
        int cleaned = 0;
        
        File dataFolder = SkyblockPlugin.getDataFolder();
        if (dataFolder.exists()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".yml")) {
                        try {
                            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                            
                            // Delete empty configs
                            if (config.getKeys(false).isEmpty()) {
                                if (file.delete()) {
                                    cleaned++;
                                    SkyblockPlugin.getLogger().info("§aDeleted empty config: " + file.getName());
                                }
                            }
                            
                        } catch (Exception e) {
                            SkyblockPlugin.getLogger().warning("Error cleaning config " + file.getName() + ": " + e.getMessage());
                        }
                    }
                }
            }
        }
        
        return cleaned;
    }
    
    /**
     * Gibt Statistiken über gefundene ungenutzte Ressourcen zurück
     */
    public Map<String, Integer> getStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("unused_configs", unusedConfigs.values().stream().mapToInt(List::size).sum());
        stats.put("unused_database_tables", unusedDatabaseTables.values().stream().mapToInt(List::size).sum());
        stats.put("unused_permissions", unusedPermissions.values().stream().mapToInt(List::size).sum());
        stats.put("unused_commands", unusedCommands.values().stream().mapToInt(List::size).sum());
        stats.put("unused_files", unusedFiles.values().stream().mapToInt(List::size).sum());
        stats.put("unused_classes", unusedClasses.values().stream().mapToInt(List::size).sum());
        
        int total = stats.values().stream().mapToInt(Integer::intValue).sum();
        stats.put("total", total);
        
        return stats;
    }
    
    /**
     * Erstellt ein GUI-Item für den Unused-Resource-Report
     */
    public ItemStack createReportItem() {
        ItemStack item = new ItemStack(org.bukkit.Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            Map<String, Integer> stats = getStatistics();
            int totalIssues = stats.get("total");
            
            meta.displayName(Component.text("§e§lUnused Resources Report"));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Total Unused Resources: §e" + totalIssues);
            lore.add("§7Unused Configs: §e" + stats.get("unused_configs"));
            lore.add("§7Unused Database Tables: §e" + stats.get("unused_database_tables"));
            lore.add("§7Unused Permissions: §e" + stats.get("unused_permissions"));
            lore.add("§7Unused Commands: §e" + stats.get("unused_commands"));
            lore.add("§7Unused Files: §e" + stats.get("unused_files"));
            lore.add("§7Unused Classes: §e" + stats.get("unused_classes"));
            lore.add("");
            lore.add("§eClick to view detailed report");
            lore.add("§eRight-click to auto-cleanup");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
}
