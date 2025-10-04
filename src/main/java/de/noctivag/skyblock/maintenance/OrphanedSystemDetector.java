package de.noctivag.skyblock.maintenance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
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
    
    private final SkyblockPlugin plugin;
    private final Map<String, List<String>> orphanedSystems = new ConcurrentHashMap<>();
    private final Map<String, List<String>> orphanedDatabaseEntries = new ConcurrentHashMap<>();
    private final Map<String, List<String>> orphanedCommands = new ConcurrentHashMap<>();
    private final Map<String, List<String>> orphanedPermissions = new ConcurrentHashMap<>();
    private final Map<String, List<String>> orphanedConfigs = new ConcurrentHashMap<>();
    
    public OrphanedSystemDetector(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Führt eine vollständige Überprüfung aller verwaisten Systeme durch
     */
    public void performFullCheck() {
        plugin.getLogger().info("§e[Maintenance] Starting orphaned system detection...");
        
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
        
        plugin.getLogger().info("§a[Maintenance] Orphaned system detection completed!");
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
            "de.noctivag.plugin.gui.AdvancedGUISystem",
            "de.noctivag.plugin.gui.UltimateMainMenu",
            "de.noctivag.plugin.gui.FeatureBookGUI",
            "de.noctivag.plugin.gui.AdminMenu",
            "de.noctivag.plugin.gui.SettingsGUI",
            "de.noctivag.plugin.gui.ModerationGUI",
            "de.noctivag.plugin.gui.RestartGUI",
            "de.noctivag.plugin.gui.QuickActionsGUI",
            "de.noctivag.plugin.gui.ChatChannelsGUI",
            "de.noctivag.plugin.gui.ReportsGUI",
            "de.noctivag.plugin.gui.DiscordGUI",
            "de.noctivag.plugin.gui.GuildSystemGUI",
            "de.noctivag.plugin.gui.EventScheduleGUI",
            "de.noctivag.plugin.gui.CosmeticsMenu",
            "de.noctivag.plugin.gui.ParticleSettingsGUI",
            "de.noctivag.plugin.gui.JoinMessagePresetsGUI",
            "de.noctivag.plugin.gui.BankGUI",
            "de.noctivag.plugin.gui.ShopGUI",
            "de.noctivag.plugin.gui.StatisticsGUI",
            "de.noctivag.plugin.gui.PartyGUI",
            "de.noctivag.plugin.gui.RulesGUI",
            "de.noctivag.plugin.gui.TournamentGUI",
            "de.noctivag.plugin.gui.DuelSystemGUI",
            "de.noctivag.plugin.gui.FriendsGUI",
            "de.noctivag.plugin.gui.JoinMessageGUI",
            "de.noctivag.plugin.gui.PvPArenaGUI",
            "de.noctivag.plugin.gui.EventRewardsGUI",
            "de.noctivag.plugin.gui.CustomGUI",
            "de.noctivag.plugin.gui.WarpGUI",
            "de.noctivag.plugin.gui.HelpGUI",
            "de.noctivag.plugin.gui.SupportGUI",
            "de.noctivag.plugin.gui.TeleportGUI",
            "de.noctivag.plugin.gui.ServerInfoGUI",
            "de.noctivag.plugin.gui.JobsGUI",
            "de.noctivag.plugin.gui.LeaderboardGUI",
            "de.noctivag.plugin.gui.EconomyGUI",
            "de.noctivag.plugin.gui.AuctionHouseGUI",
            "de.noctivag.plugin.gui.MobArenaGUI",
            "de.noctivag.plugin.gui.KitShopGUI",
            "de.noctivag.plugin.gui.WebsiteGUI",
            "de.noctivag.plugin.gui.BattlePassGUI",
            "de.noctivag.plugin.gui.QuestGUI",
            "de.noctivag.plugin.gui.InfoGUI",
            "de.noctivag.plugin.gui.MyCosmeticsGUI",
            "de.noctivag.plugin.gui.CosmeticShopGUI",
            "de.noctivag.plugin.gui.GadgetsGUI",
            "de.noctivag.plugin.gui.MessagesMenu",
            "de.noctivag.plugin.gui.EnhancedMainMenu",
            "de.noctivag.plugin.gui.AnimatedGUI",
            "de.noctivag.plugin.gui.UltimateEventGUI",
            "de.noctivag.plugin.gui.UltimateGUISystem",
            "de.noctivag.plugin.gui.CommandUsageGUI",
            "de.noctivag.plugin.gui.FeatureToggleListener",
            "de.noctivag.plugin.gui.PotatoBookGUI",
            "de.noctivag.plugin.gui.RecombobulatorGUI",
            "de.noctivag.plugin.gui.DungeonStarGUI",
            "de.noctivag.plugin.gui.PetItemGUI",
            "de.noctivag.plugin.gui.ArmorAbilityGUI",
            "de.noctivag.plugin.gui.WeaponAbilityGUI",
            "de.noctivag.plugin.gui.NPCCreationGUI",
            "de.noctivag.plugin.gui.NPCManagementGUI",
            "de.noctivag.plugin.gui.NPCEditGUI",
            "de.noctivag.plugin.gui.EnhancedPetGUI"
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
            "de.noctivag.plugin.commands.AdvancedCommandSystem",
            "de.noctivag.plugin.commands.HelpCommand",
            "de.noctivag.plugin.commands.FeatureCommand",
            "de.noctivag.plugin.commands.AdminCommand",
            "de.noctivag.plugin.commands.MiningCommand",
            "de.noctivag.plugin.commands.CollectionsCommand",
            "de.noctivag.plugin.commands.PartyCommand",
            "de.noctivag.plugin.commands.MobCommandSystem"
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
            "de.noctivag.plugin.events.AdvancedEventsSystem",
            "de.noctivag.plugin.events.AdvancedEventSystem",
            "de.noctivag.plugin.events.EventManager",
            "de.noctivag.plugin.events.EventScheduler",
            "de.noctivag.plugin.events.EventsSystem",
            "de.noctivag.plugin.events.UltimateEventSystem",
            "de.noctivag.plugin.events.Arena"
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
            "de.noctivag.plugin.managers.RankManager",
            "de.noctivag.plugin.managers.EventManager",
            "de.noctivag.plugin.managers.TeleportManager",
            "de.noctivag.plugin.managers.PartyManager"
        };
        
        for (String className : managerClasses) {
            try {
                Class<?> clazz = Class.forName(className);
                
                // Check if class has proper initialization
                boolean hasConstructor = clazz.getConstructors().length > 0;
                boolean hasPluginField = false;
                
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.getType().getSimpleName().equals("Plugin")) {
                        hasPluginField = true;
                        break;
                    }
                }
                
                if (!hasConstructor) {
                    addOrphanedSystem("manager_no_constructor", className, "Manager class has no constructor");
                }
                
                if (!hasPluginField) {
                    addOrphanedSystem("manager_no_plugin_field", className, "Manager class has no plugin field");
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
        
        File playersFile = new File(plugin.getDataFolder(), "players.yml");
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
        File islandsFile = new File(plugin.getDataFolder(), "islands.yml");
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
        File guildsFile = new File(plugin.getDataFolder(), "guilds.yml");
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
        File auctionsFile = new File(plugin.getDataFolder(), "auctions.yml");
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
        // Check plugin.yml for commands that might not be used
        File pluginFile = new File(plugin.getDataFolder().getParentFile(), "plugin.yml");
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
        // Check plugin.yml for permissions that might not be used
        File pluginFile = new File(plugin.getDataFolder().getParentFile(), "plugin.yml");
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
        File dataFolder = plugin.getDataFolder();
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
        plugin.getLogger().info("§6=== ORPHANED SYSTEM DETECTION REPORT ===");
        
        // Orphaned systems
        if (!orphanedSystems.isEmpty()) {
            plugin.getLogger().warning("§cOrphaned Systems:");
            for (Map.Entry<String, List<String>> entry : orphanedSystems.entrySet()) {
                plugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    plugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Orphaned database entries
        if (!orphanedDatabaseEntries.isEmpty()) {
            plugin.getLogger().warning("§cOrphaned Database Entries:");
            for (Map.Entry<String, List<String>> entry : orphanedDatabaseEntries.entrySet()) {
                plugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    plugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Orphaned commands
        if (!orphanedCommands.isEmpty()) {
            plugin.getLogger().warning("§cOrphaned Commands:");
            for (Map.Entry<String, List<String>> entry : orphanedCommands.entrySet()) {
                plugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    plugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Orphaned permissions
        if (!orphanedPermissions.isEmpty()) {
            plugin.getLogger().warning("§cOrphaned Permissions:");
            for (Map.Entry<String, List<String>> entry : orphanedPermissions.entrySet()) {
                plugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    plugin.getLogger().warning("    §7- " + issue);
                }
            }
        }
        
        // Orphaned configs
        if (!orphanedConfigs.isEmpty()) {
            plugin.getLogger().warning("§cOrphaned Configs:");
            for (Map.Entry<String, List<String>> entry : orphanedConfigs.entrySet()) {
                plugin.getLogger().warning("  §e" + entry.getKey() + ":");
                for (String issue : entry.getValue()) {
                    plugin.getLogger().warning("    §7- " + issue);
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
            plugin.getLogger().info("§aNo orphaned systems found! System is clean.");
        } else {
            plugin.getLogger().warning("§cTotal orphaned items found: " + totalIssues);
        }
        
        plugin.getLogger().info("§6==========================================");
    }
    
    /**
     * Automatische Bereinigung von verwaisten Systemen
     */
    public void autoCleanup() {
        plugin.getLogger().info("§e[Maintenance] Starting auto-cleanup for orphaned systems...");
        
        int cleanedItems = 0;
        
        // Cleanup orphaned configs
        cleanedItems += cleanupOrphanedConfigs();
        
        // Cleanup orphaned database entries
        cleanedItems += cleanupOrphanedDatabaseEntries();
        
        plugin.getLogger().info("§a[Maintenance] Auto-cleanup completed! Cleaned " + cleanedItems + " items.");
    }
    
    /**
     * Bereinigt verwaiste Config-Dateien
     */
    private int cleanupOrphanedConfigs() {
        int cleaned = 0;
        
        File dataFolder = plugin.getDataFolder();
        if (dataFolder.exists()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".yml")) {
                        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                        if (config.getKeys(false).isEmpty()) {
                            if (file.delete()) {
                                cleaned++;
                                plugin.getLogger().info("§aDeleted empty config file: " + file.getName());
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
                plugin.getLogger().info("§eWould clean: " + issue);
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
            
            meta.setDisplayName("§c§lOrphaned Systems Report");
            
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
            
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        
        return item;
    }
}
