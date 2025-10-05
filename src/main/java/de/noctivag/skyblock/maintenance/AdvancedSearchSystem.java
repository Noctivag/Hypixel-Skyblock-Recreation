package de.noctivag.skyblock.maintenance;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * AdvancedSearchSystem - Erweiterte Suchfunktionen für das SkyblockPlugin
 * 
 * Features:
 * - Volltext-Suche in allen Systemen
 * - Filter-basierte Suche
 * - Fuzzy-Search für Commands
 * - Tag-basierte Kategorisierung
 * - Regex-Suche
 * - Auto-Complete-Funktionen
 */
public class AdvancedSearchSystem {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, List<String>> searchIndex = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> tagIndex = new ConcurrentHashMap<>();
    private final Map<String, List<String>> fuzzyIndex = new ConcurrentHashMap<>();
    private final Map<String, String> commandAliases = new ConcurrentHashMap<>();
    
    public AdvancedSearchSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        initializeSearchIndex();
    }
    
    /**
     * Initialisiert den Suchindex
     */
    private void initializeSearchIndex() {
        // Index commands
        indexCommands();
        
        // Index GUIs
        indexGUIs();
        
        // Index permissions
        indexPermissions();
        
        // Index configs
        indexConfigs();
        
        // Index database tables
        indexDatabaseTables();
        
        // Index features
        indexFeatures();
        
        SkyblockPlugin.getLogger().info("§a[Search] Search index initialized with " + searchIndex.size() + " entries");
    }
    
    /**
     * Indiziert alle Commands
     */
    private void indexCommands() {
        File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
        if (pluginFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
            
            if (config.contains("commands")) {
                for (String commandName : config.getConfigurationSection("commands").getKeys(false)) {
                    String path = "commands." + commandName;
                    
                    // Add command to index
                    addToIndex("command", commandName, commandName);
                    
                    // Add description to index
                    String description = config.getString(path + ".description", "");
                    if (!description.isEmpty()) {
                        addToIndex("command_description", commandName, description);
                    }
                    
                    // Add usage to index
                    String usage = config.getString(path + ".usage", "");
                    if (!usage.isEmpty()) {
                        addToIndex("command_usage", commandName, usage);
                    }
                    
                    // Add permission to index
                    String permission = config.getString(path + ".permission", "");
                    if (!permission.isEmpty()) {
                        addToIndex("command_permission", commandName, permission);
                    }
                    
                    // Add aliases
                    List<String> aliases = config.getStringList(path + ".aliases");
                    for (String alias : aliases) {
                        commandAliases.put(alias, commandName);
                        addToIndex("command_alias", alias, commandName);
                    }
                    
                    // Add tags
                    addTags("command", commandName, Arrays.asList("command", "executable", "SkyblockPlugin"));
                }
            }
        }
    }
    
    /**
     * Indiziert alle GUIs
     */
    private void indexGUIs() {
        String[] guiClasses = {
            "AdvancedGUISystem", "UltimateMainMenu", "FeatureBookGUI", "AdminMenu",
            "SettingsGUI", "ModerationGUI", "RestartGUI", "QuickActionsGUI",
            "ChatChannelsGUI", "ReportsGUI", "DiscordGUI", "GuildSystemGUI",
            "EventScheduleGUI", "CosmeticsMenu", "ParticleSettingsGUI", "JoinMessagePresetsGUI",
            "BankGUI", "ShopGUI", "StatisticsGUI", "PartyGUI", "RulesGUI", "TournamentGUI",
            "DuelSystemGUI", "FriendsGUI", "JoinMessageGUI", "PvPArenaGUI", "EventRewardsGUI",
            "CustomGUI", "WarpGUI", "HelpGUI", "SupportGUI", "TeleportGUI", "ServerInfoGUI",
            "JobsGUI", "LeaderboardGUI", "EconomyGUI", "AuctionHouseGUI", "MobArenaGUI",
            "KitShopGUI", "WebsiteGUI", "BattlePassGUI", "QuestGUI", "InfoGUI",
            "MyCosmeticsGUI", "CosmeticShopGUI", "GadgetsGUI", "MessagesMenu",
            "EnhancedMainMenu", "AnimatedGUI", "UltimateEventGUI", "UltimateGUISystem",
            "CommandUsageGUI", "FeatureToggleListener", "PotatoBookGUI", "RecombobulatorGUI",
            "DungeonStarGUI", "PetItemGUI", "ArmorAbilityGUI", "WeaponAbilityGUI",
            "NPCCreationGUI", "NPCManagementGUI", "NPCEditGUI", "EnhancedPetGUI"
        };
        
        for (String guiClass : guiClasses) {
            addToIndex("gui", guiClass, guiClass);
            addTags("gui", guiClass, Arrays.asList("gui", "interface", "menu", "SkyblockPlugin"));
        }
    }
    
    /**
     * Indiziert alle Permissions
     */
    private void indexPermissions() {
        File pluginFile = new File(SkyblockPlugin.getDataFolder().getParentFile(), "SkyblockPlugin.yml");
        if (pluginFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
            
            if (config.contains("permissions")) {
                for (String permissionName : config.getConfigurationSection("permissions").getKeys(false)) {
                    String path = "permissions." + permissionName;
                    
                    // Add permission to index
                    addToIndex("permission", permissionName, permissionName);
                    
                    // Add description to index
                    String description = config.getString(path + ".description", "");
                    if (!description.isEmpty()) {
                        addToIndex("permission_description", permissionName, description);
                    }
                    
                    // Add default value to index
                    String defaultValue = config.getString(path + ".default", "");
                    if (!defaultValue.isEmpty()) {
                        addToIndex("permission_default", permissionName, defaultValue);
                    }
                    
                    // Add tags
                    addTags("permission", permissionName, Arrays.asList("permission", "access", "security"));
                }
            }
        }
    }
    
    /**
     * Indiziert alle Configs
     */
    private void indexConfigs() {
        File dataFolder = SkyblockPlugin.getDataFolder();
        if (dataFolder.exists()) {
            File[] files = dataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".yml")) {
                        String fileName = file.getName();
                        addToIndex("config", fileName, fileName);
                        addTags("config", fileName, Arrays.asList("config", "configuration", "settings"));
                    }
                }
            }
        }
    }
    
    /**
     * Indiziert alle Datenbank-Tabellen
     */
    private void indexDatabaseTables() {
        String[] tables = {
            "server_info", "player_profiles", "skyblock_islands", "island_members",
            "player_skills", "player_collections", "player_slayers", "player_minions",
            "player_pets", "auction_house", "bazaar_orders", "player_dungeons",
            "server_events", "guilds", "guild_members", "leaderboards", "player_ranks",
            "player_permissions", "player_homes", "player_warps", "player_friends",
            "player_parties", "player_achievements", "player_rewards", "player_cosmetics",
            "player_settings", "player_statistics", "player_logs", "player_sessions"
        };
        
        for (String table : tables) {
            addToIndex("database_table", table, table);
            addTags("database_table", table, Arrays.asList("database", "table", "storage"));
        }
    }
    
    /**
     * Indiziert alle Features
     */
    private void indexFeatures() {
        String[] features = {
            "Skills System", "Collections System", "Minions System", "Pets System",
            "Guilds System", "Auctions System", "Bazaar System", "Dungeons System",
            "Slayers System", "Fishing System", "Foraging System", "Mining System",
            "Combat System", "Enchanting System", "Alchemy System", "Taming System",
            "Carpentry System", "Runecrafting System", "Social System", "Dungeoneering System",
            "Economy System", "Rank System", "Party System", "Friends System",
            "Teleportation System", "Cosmetics System", "Achievements System", "Kit System",
            "Daily Rewards System", "Scoreboard System", "Admin Tools", "Event System"
        };
        
        for (String feature : features) {
            addToIndex("feature", feature, feature);
            addTags("feature", feature, Arrays.asList("feature", "system", "functionality"));
        }
    }
    
    /**
     * Fügt einen Eintrag zum Suchindex hinzu
     */
    private void addToIndex(String category, String key, String content) {
        searchIndex.computeIfAbsent(category, k -> new ArrayList<>()).add(key + ":" + content);
    }
    
    /**
     * Fügt Tags zu einem Eintrag hinzu
     */
    private void addTags(String category, String key, List<String> tags) {
        String fullKey = category + ":" + key;
        tagIndex.computeIfAbsent(fullKey, k -> new HashSet<>()).addAll(tags);
    }
    
    /**
     * Führt eine Volltext-Suche durch
     */
    public List<SearchResult> fullTextSearch(String query) {
        List<SearchResult> results = new ArrayList<>();
        
        if (query == null || query.trim().isEmpty()) {
            return results;
        }
        
        String lowerQuery = query.toLowerCase();
        
        for (Map.Entry<String, List<String>> entry : searchIndex.entrySet()) {
            String category = entry.getKey();
            List<String> items = entry.getValue();
            
            for (String item : items) {
                String[] parts = item.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0];
                    String content = parts[1];
                    
                    if (content.toLowerCase().contains(lowerQuery)) {
                        results.add(new SearchResult(category, key, content, calculateRelevance(query, content)));
                    }
                }
            }
        }
        
        // Sort by relevance
        results.sort((a, b) -> Double.compare(b.getRelevance(), a.getRelevance()));
        
        return results;
    }
    
    /**
     * Führt eine Filter-basierte Suche durch
     */
    public List<SearchResult> filterSearch(String category, String query) {
        List<SearchResult> results = new ArrayList<>();
        
        if (category == null || category.trim().isEmpty()) {
            return fullTextSearch(query);
        }
        
        List<String> items = searchIndex.get(category);
        if (items == null) {
            return results;
        }
        
        String lowerQuery = query != null ? query.toLowerCase() : "";
        
        for (String item : items) {
            String[] parts = item.split(":", 2);
            if (parts.length == 2) {
                String key = parts[0];
                String content = parts[1];
                
                if (query == null || query.trim().isEmpty() || content.toLowerCase().contains(lowerQuery)) {
                    results.add(new SearchResult(category, key, content, calculateRelevance(query, content)));
                }
            }
        }
        
        // Sort by relevance
        results.sort((a, b) -> Double.compare(b.getRelevance(), a.getRelevance()));
        
        return results;
    }
    
    /**
     * Führt eine Tag-basierte Suche durch
     */
    public List<SearchResult> tagSearch(String tag) {
        List<SearchResult> results = new ArrayList<>();
        
        if (tag == null || tag.trim().isEmpty()) {
            return results;
        }
        
        String lowerTag = tag.toLowerCase();
        
        for (Map.Entry<String, Set<String>> entry : tagIndex.entrySet()) {
            String fullKey = entry.getKey();
            Set<String> tags = entry.getValue();
            
            for (String tagItem : tags) {
                if (tagItem.toLowerCase().contains(lowerTag)) {
                    String[] parts = fullKey.split(":", 2);
                    if (parts.length == 2) {
                        String category = parts[0];
                        String key = parts[1];
                        
                        // Find content in search index
                        List<String> items = searchIndex.get(category);
                        if (items != null) {
                            for (String item : items) {
                                String[] itemParts = item.split(":", 2);
                                if (itemParts.length == 2 && itemParts[0].equals(key)) {
                                    results.add(new SearchResult(category, key, itemParts[1], 1.0));
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        
        return results;
    }
    
    /**
     * Führt eine Regex-Suche durch
     */
    public List<SearchResult> regexSearch(String pattern) {
        List<SearchResult> results = new ArrayList<>();
        
        if (pattern == null || pattern.trim().isEmpty()) {
            return results;
        }
        
        try {
            Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            
            for (Map.Entry<String, List<String>> entry : searchIndex.entrySet()) {
                String category = entry.getKey();
                List<String> items = entry.getValue();
                
                for (String item : items) {
                    String[] parts = item.split(":", 2);
                    if (parts.length == 2) {
                        String key = parts[0];
                        String content = parts[1];
                        
                        if (regex.matcher(content).find()) {
                            results.add(new SearchResult(category, key, content, 1.0));
                        }
                    }
                }
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Invalid regex pattern: " + pattern);
        }
        
        return results;
    }
    
    /**
     * Führt eine Fuzzy-Suche durch
     */
    public List<SearchResult> fuzzySearch(String query) {
        List<SearchResult> results = new ArrayList<>();
        
        if (query == null || query.trim().isEmpty()) {
            return results;
        }
        
        String lowerQuery = query.toLowerCase();
        
        for (Map.Entry<String, List<String>> entry : searchIndex.entrySet()) {
            String category = entry.getKey();
            List<String> items = entry.getValue();
            
            for (String item : items) {
                String[] parts = item.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0];
                    String content = parts[1];
                    
                    double similarity = calculateSimilarity(lowerQuery, content.toLowerCase());
                    if (similarity > 0.3) { // Threshold for fuzzy matching
                        results.add(new SearchResult(category, key, content, similarity));
                    }
                }
            }
        }
        
        // Sort by similarity
        results.sort((a, b) -> Double.compare(b.getRelevance(), a.getRelevance()));
        
        return results;
    }
    
    /**
     * Gibt Auto-Complete-Vorschläge zurück
     */
    public List<String> getAutoCompleteSuggestions(String partialQuery) {
        List<String> suggestions = new ArrayList<>();
        
        if (partialQuery == null || partialQuery.trim().isEmpty()) {
            return suggestions;
        }
        
        String lowerQuery = partialQuery.toLowerCase();
        
        for (Map.Entry<String, List<String>> entry : searchIndex.entrySet()) {
            List<String> items = entry.getValue();
            
            for (String item : items) {
                String[] parts = item.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0];
                    String content = parts[1];
                    
                    if (content.toLowerCase().startsWith(lowerQuery)) {
                        suggestions.add(content);
                    }
                }
            }
        }
        
        // Remove duplicates and sort
        suggestions = new ArrayList<>(new HashSet<>(suggestions));
        suggestions.sort(String.CASE_INSENSITIVE_ORDER);
        
        return suggestions.subList(0, Math.min(10, suggestions.size())); // Limit to 10 suggestions
    }
    
    /**
     * Berechnet die Relevanz eines Suchergebnisses
     */
    private double calculateRelevance(String query, String content) {
        if (query == null || query.trim().isEmpty()) {
            return 1.0;
        }
        
        String lowerQuery = query.toLowerCase();
        String lowerContent = content.toLowerCase();
        
        // Exact match gets highest relevance
        if (lowerContent.equals(lowerQuery)) {
            return 1.0;
        }
        
        // Starts with query gets high relevance
        if (lowerContent.startsWith(lowerQuery)) {
            return 0.9;
        }
        
        // Contains query gets medium relevance
        if (lowerContent.contains(lowerQuery)) {
            return 0.7;
        }
        
        // Word boundary match gets lower relevance
        if (lowerContent.matches(".*\\b" + Pattern.quote(lowerQuery) + "\\b.*")) {
            return 0.5;
        }
        
        return 0.1;
    }
    
    /**
     * Berechnet die Ähnlichkeit zwischen zwei Strings (Levenshtein-Distanz)
     */
    private double calculateSimilarity(String s1, String s2) {
        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) {
            return 1.0;
        }
        
        int distance = levenshteinDistance(s1, s2);
        return 1.0 - (double) distance / maxLength;
    }
    
    /**
     * Berechnet die Levenshtein-Distanz zwischen zwei Strings
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)
                    );
                }
            }
        }
        
        return dp[s1.length()][s2.length()];
    }
    
    /**
     * Gibt alle verfügbaren Kategorien zurück
     */
    public Set<String> getAvailableCategories() {
        return new HashSet<>(searchIndex.keySet());
    }
    
    /**
     * Gibt alle verfügbaren Tags zurück
     */
    public Set<String> getAllTags() {
        Set<String> allTags = new HashSet<>();
        for (Set<String> tags : tagIndex.values()) {
            allTags.addAll(tags);
        }
        return allTags;
    }
    
    /**
     * Gibt Statistiken über den Suchindex zurück
     */
    public Map<String, Integer> getIndexStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        for (Map.Entry<String, List<String>> entry : searchIndex.entrySet()) {
            stats.put(entry.getKey(), entry.getValue().size());
        }
        
        stats.put("total_entries", searchIndex.values().stream().mapToInt(List::size).sum());
        stats.put("total_categories", searchIndex.size());
        stats.put("total_tags", getAllTags().size());
        
        return stats;
    }
    
    /**
     * Erstellt ein GUI-Item für die Suche
     */
    public ItemStack createSearchItem() {
        ItemStack item = new ItemStack(org.bukkit.Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            Map<String, Integer> stats = getIndexStatistics();
            
            meta.displayName(Component.text("§6§lAdvanced Search"));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Total Entries: §a" + stats.get("total_entries"));
            lore.add("§7Categories: §a" + stats.get("total_categories"));
            lore.add("§7Tags: §a" + stats.get("total_tags"));
            lore.add("");
            lore.add("§7Available Search Types:");
            lore.add("§7• Full Text Search");
            lore.add("§7• Filter Search");
            lore.add("§7• Tag Search");
            lore.add("§7• Regex Search");
            lore.add("§7• Fuzzy Search");
            lore.add("");
            lore.add("§eClick to open search interface");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * SearchResult - Repräsentiert ein Suchergebnis
     */
    public static class SearchResult {
        private final String category;
        private final String key;
        private final String content;
        private final double relevance;
        
        public SearchResult(String category, String key, String content, double relevance) {
            this.category = category;
            this.key = key;
            this.content = content;
            this.relevance = relevance;
        }
        
        public String getCategory() {
            return category;
        }
        
        public String getKey() {
            return key;
        }
        
        public String getContent() {
            return content;
        }
        
        public double getRelevance() {
            return relevance;
        }
        
        @Override
        public String toString() {
            return String.format("[%s] %s: %s (%.2f)", category, key, content, relevance);
        }
    }
}
