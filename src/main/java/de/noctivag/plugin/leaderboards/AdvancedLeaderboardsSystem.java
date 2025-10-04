package de.noctivag.plugin.leaderboards;
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
 * Advanced Leaderboards System - Hypixel Skyblock Style
 */
public class AdvancedLeaderboardsSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerLeaderboards> playerLeaderboards = new ConcurrentHashMap<>();
    private final Map<LeaderboardType, LeaderboardConfig> leaderboardConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> leaderboardTasks = new ConcurrentHashMap<>();
    
    public AdvancedLeaderboardsSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeLeaderboardConfigs();
        startLeaderboardUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeLeaderboardConfigs() {
        leaderboardConfigs.put(LeaderboardType.SKILLS, new LeaderboardConfig(
            "Skills Leaderboard", "§aSkills Leaderboard", Material.EXPERIENCE_BOTTLE,
            "§7Top players by skill levels.",
            LeaderboardCategory.SKILLS, 1, Arrays.asList("§7- Skill levels", "§7- XP gained"),
            Arrays.asList("§7- 1x Experience Bottle", "§7- 1x Skill Book")
        ));
        
        leaderboardConfigs.put(LeaderboardType.COINS, new LeaderboardConfig(
            "Coins Leaderboard", "§6Coins Leaderboard", Material.GOLD_INGOT,
            "§7Top players by coin balance.",
            LeaderboardCategory.ECONOMY, 1, Arrays.asList("§7- Coin balance", "§7- Wealth"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Coin")
        ));
    }
    
    private void startLeaderboardUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerLeaderboards> entry : playerLeaderboards.entrySet()) {
                    PlayerLeaderboards leaderboards = entry.getValue();
                    leaderboards.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ITEM_FRAME) {
            openLeaderboardGUI(player);
        }
    }
    
    private void openLeaderboardGUI(Player player) {
        player.sendMessage("§aLeaderboard GUI geöffnet!");
    }
    
    public PlayerLeaderboards getPlayerLeaderboards(UUID playerId) {
        return playerLeaderboards.computeIfAbsent(playerId, k -> new PlayerLeaderboards(playerId));
    }
    
    public LeaderboardConfig getLeaderboardConfig(LeaderboardType type) {
        return leaderboardConfigs.get(type);
    }
    
    public List<LeaderboardType> getAllLeaderboardTypes() {
        return new ArrayList<>(leaderboardConfigs.keySet());
    }
    
    public enum LeaderboardType {
        SKILLS, COINS, COLLECTIONS, MINIONS, PETS, DUNGEONS, SLAYERS, GUILDS
    }
    
    public enum LeaderboardCategory {
        SKILLS("§aSkills", 1.0),
        ECONOMY("§6Economy", 1.2),
        COLLECTIONS("§bCollections", 1.1),
        MINIONS("§eMinions", 1.3),
        PETS("§dPets", 1.4),
        DUNGEONS("§cDungeons", 1.5);
        
        private final String displayName;
        private final double multiplier;
        
        LeaderboardCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class LeaderboardConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final LeaderboardCategory category;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public LeaderboardConfig(String name, String displayName, Material icon, String description,
                               LeaderboardCategory category, int maxLevel, List<String> features, List<String> requirements) {
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
        public LeaderboardCategory getCategory() { return category; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerLeaderboards {
        private final UUID playerId;
        private final Map<LeaderboardType, Integer> leaderboardLevels = new ConcurrentHashMap<>();
        private int totalLeaderboards = 0;
        private long totalLeaderboardTime = 0;
        private long lastUpdate;
        
        public PlayerLeaderboards(UUID playerId) {
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
            // Save leaderboard data to database
        }
        
        public void addLeaderboard(LeaderboardType type, int level) {
            leaderboardLevels.put(type, level);
            totalLeaderboards++;
        }
        
        public int getLeaderboardLevel(LeaderboardType type) {
            return leaderboardLevels.getOrDefault(type, 0);
        }
        
        public int getTotalLeaderboards() { return totalLeaderboards; }
        public long getTotalLeaderboardTime() { return totalLeaderboardTime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<LeaderboardType, Integer> getLeaderboardLevels() { return leaderboardLevels; }
    }
}
