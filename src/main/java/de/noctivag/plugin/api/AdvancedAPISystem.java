package de.noctivag.plugin.api;
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
 * Advanced API System - Hypixel Skyblock Style
 */
public class AdvancedAPISystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerAPI> playerAPI = new ConcurrentHashMap<>();
    private final Map<APIType, APIConfig> apiConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> apiTasks = new ConcurrentHashMap<>();
    
    public AdvancedAPISystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeAPIConfigs();
        startAPIUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeAPIConfigs() {
        apiConfigs.put(APIType.SKILLS, new APIConfig(
            "Skills API", "§aSkills API", Material.EXPERIENCE_BOTTLE,
            "§7Access player skill data via API.",
            APICategory.SKILLS, 1, Arrays.asList("§7- Skill data", "§7- XP information"),
            Arrays.asList("§7- 1x Experience Bottle", "§7- 1x API Key")
        ));
        
        apiConfigs.put(APIType.ECONOMY, new APIConfig(
            "Economy API", "§6Economy API", Material.GOLD_INGOT,
            "§7Access player economy data via API.",
            APICategory.ECONOMY, 1, Arrays.asList("§7- Coin data", "§7- Transaction history"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x API Key")
        ));
    }
    
    private void startAPIUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerAPI> entry : playerAPI.entrySet()) {
                    PlayerAPI api = entry.getValue();
                    api.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.COMMAND_BLOCK) {
            openAPIGUI(player);
        }
    }
    
    private void openAPIGUI(Player player) {
        player.sendMessage("§aAPI GUI geöffnet!");
    }
    
    public PlayerAPI getPlayerAPI(UUID playerId) {
        return playerAPI.computeIfAbsent(playerId, k -> new PlayerAPI(playerId));
    }
    
    public APIConfig getAPIConfig(APIType type) {
        return apiConfigs.get(type);
    }
    
    public List<APIType> getAllAPITypes() {
        return new ArrayList<>(apiConfigs.keySet());
    }
    
    public enum APIType {
        SKILLS, ECONOMY, COLLECTIONS, MINIONS, PETS, DUNGEONS, SLAYERS, GUILDS
    }
    
    public enum APICategory {
        SKILLS("§aSkills", 1.0),
        ECONOMY("§6Economy", 1.2),
        COLLECTIONS("§bCollections", 1.1),
        MINIONS("§eMinions", 1.3),
        PETS("§dPets", 1.4),
        DUNGEONS("§cDungeons", 1.5);
        
        private final String displayName;
        private final double multiplier;
        
        APICategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class APIConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final APICategory category;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public APIConfig(String name, String displayName, Material icon, String description,
                        APICategory category, int maxLevel, List<String> features, List<String> requirements) {
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
        public APICategory getCategory() { return category; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerAPI {
        private final UUID playerId;
        private final Map<APIType, Integer> apiLevels = new ConcurrentHashMap<>();
        private int totalAPIs = 0;
        private long totalAPITime = 0;
        private long lastUpdate;
        
        public PlayerAPI(UUID playerId) {
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
            // Save API data to database
        }
        
        public void addAPI(APIType type, int level) {
            apiLevels.put(type, level);
            totalAPIs++;
        }
        
        public int getAPILevel(APIType type) {
            return apiLevels.getOrDefault(type, 0);
        }
        
        public int getTotalAPIs() { return totalAPIs; }
        public long getTotalAPITime() { return totalAPITime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<APIType, Integer> getAPILevels() { return apiLevels; }
    }
}
