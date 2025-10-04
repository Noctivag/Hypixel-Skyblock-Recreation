package de.noctivag.plugin.web;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Web Interface System - Hypixel Skyblock Style
 */
public class WebInterfaceSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerWeb> playerWeb = new ConcurrentHashMap<>();
    private final Map<WebType, WebConfig> webConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> webTasks = new ConcurrentHashMap<>();
    
    public WebInterfaceSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeWebConfigs();
        startWebUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeWebConfigs() {
        webConfigs.put(WebType.DASHBOARD, new WebConfig(
            "Dashboard", "§aDashboard", Material.COMPASS,
            "§7Main dashboard for player statistics.",
            WebCategory.DASHBOARD, 1, Arrays.asList("§7- Player stats", "§7- Progress tracking"),
            Arrays.asList("§7- 1x Compass", "§7- 1x Web Access")
        ));
        
        webConfigs.put(WebType.LEADERBOARDS, new WebConfig(
            "Leaderboards", "§6Leaderboards", Material.ITEM_FRAME,
            "§7View player leaderboards and rankings.",
            WebCategory.LEADERBOARDS, 1, Arrays.asList("§7- Rankings", "§7- Competition"),
            Arrays.asList("§7- 1x Item Frame", "§7- 1x Web Access")
        ));
    }
    
    private void startWebUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerWeb> entry : playerWeb.entrySet()) {
                    PlayerWeb web = entry.getValue();
                    web.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.REDSTONE_LAMP) {
            openWebGUI(player);
        }
    }
    
    private void openWebGUI(Player player) {
        player.sendMessage("§aWeb Interface geöffnet!");
    }
    
    public PlayerWeb getPlayerWeb(UUID playerId) {
        return playerWeb.computeIfAbsent(playerId, k -> new PlayerWeb(playerId));
    }
    
    public WebConfig getWebConfig(WebType type) {
        return webConfigs.get(type);
    }
    
    public List<WebType> getAllWebTypes() {
        return new ArrayList<>(webConfigs.keySet());
    }
    
    public enum WebType {
        DASHBOARD, LEADERBOARDS, STATISTICS, PROFILE, GUILD, ISLAND, AUCTION, BAZAAR
    }
    
    public enum WebCategory {
        DASHBOARD("§aDashboard", 1.0),
        LEADERBOARDS("§6Leaderboards", 1.2),
        STATISTICS("§bStatistics", 1.1),
        PROFILE("§eProfile", 1.3),
        GUILD("§dGuild", 1.4),
        ISLAND("§cIsland", 1.5);

        private final String displayName;
        private final double multiplier;
        
        WebCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class WebConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final WebCategory category;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public WebConfig(String name, String displayName, Material icon, String description,
                        WebCategory category, int maxLevel, List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.maxLevel = maxLevel;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public WebCategory getCategory() { return category; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerWeb {
        private final UUID playerId;
        private final Map<WebType, Integer> webLevels = new ConcurrentHashMap<>();
        private int totalWebs = 0;
        private long totalWebTime = 0;
        private long lastUpdate;
        
        public PlayerWeb(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save web data to database
        }
        
        public void addWeb(WebType type, int level) {
            webLevels.put(type, level);
            totalWebs++;
        }
        
        public int getWebLevel(WebType type) {
            return webLevels.getOrDefault(type, 0);
        }
        
        public int getTotalWebs() { return totalWebs; }
        public long getTotalWebTime() { return totalWebTime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<WebType, Integer> getWebLevels() { return webLevels; }
    }
}
