package de.noctivag.skyblock.achievements;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
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
 * Advanced Achievements System - Hypixel Skyblock Style
 */
public class AdvancedAchievementsSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerAchievements> playerAchievements = new ConcurrentHashMap<>();
    private final Map<AchievementType, AchievementConfig> achievementConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> achievementTasks = new ConcurrentHashMap<>();
    
    public AdvancedAchievementsSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeAchievementConfigs();
        startAchievementUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeAchievementConfigs() {
        achievementConfigs.put(AchievementType.FIRST_STEPS, new AchievementConfig(
            "First Steps", "§aFirst Steps", Material.STONE,
            "§7Take your first steps in the world.",
            AchievementCategory.TUTORIAL, 1, Arrays.asList("§7- Complete tutorial", "§7- Get started"),
            Arrays.asList("§7- 1x Stone", "§7- 1x Experience")
        ));
        
        achievementConfigs.put(AchievementType.MINING_MASTER, new AchievementConfig(
            "Mining Master", "§6Mining Master", Material.DIAMOND_PICKAXE,
            "§7Master the art of mining.",
            AchievementCategory.MINING, 1, Arrays.asList("§7- Mine 1000 blocks", "§7- Get rewards"),
            Arrays.asList("§7- 1x Diamond Pickaxe", "§7- 1x Experience")
        ));
    }
    
    private void startAchievementUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerAchievements> entry : playerAchievements.entrySet()) {
                    PlayerAchievements achievements = entry.getValue();
                    achievements.update();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.GOLD_INGOT) {
            openAchievementGUI(player);
        }
    }
    
    private void openAchievementGUI(Player player) {
        player.sendMessage(Component.text("§aAchievement GUI geöffnet!"));
    }
    
    public PlayerAchievements getPlayerAchievements(UUID playerId) {
        return playerAchievements.computeIfAbsent(playerId, k -> new PlayerAchievements(playerId));
    }
    
    public AchievementConfig getAchievementConfig(AchievementType type) {
        return achievementConfigs.get(type);
    }
    
    public List<AchievementType> getAllAchievementTypes() {
        return new ArrayList<>(achievementConfigs.keySet());
    }
    
    public enum AchievementType {
        FIRST_STEPS, MINING_MASTER, COMBAT_WARRIOR, FARMING_EXPERT, FISHING_ANGLER,
        ENCHANTING_WIZARD, ALCHEMY_MASTER, TAMING_TRAINER, CARPENTRY_CRAFTER, RUNECRAFTING_SAGE
    }
    
    public enum AchievementCategory {
        TUTORIAL("§aTutorial", 1.0),
        MINING("§6Mining", 1.2),
        COMBAT("§cCombat", 1.1),
        FARMING("§aFarming", 1.3),
        FISHING("§bFishing", 1.4),
        ENCHANTING("§dEnchanting", 1.5);
        
        private final String displayName;
        private final double multiplier;
        
        AchievementCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class AchievementConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final AchievementCategory category;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public AchievementConfig(String name, String displayName, Material icon, String description,
                               AchievementCategory category, int maxLevel, List<String> features, List<String> requirements) {
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
        public AchievementCategory getCategory() { return category; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerAchievements {
        private final UUID playerId;
        private final Map<AchievementType, Integer> achievementLevels = new ConcurrentHashMap<>();
        private int totalAchievements = 0;
        private long totalAchievementTime = 0;
        private long lastUpdate;
        
        public PlayerAchievements(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = java.lang.System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save achievement data to database
        }
        
        public void addAchievement(AchievementType type, int level) {
            achievementLevels.put(type, level);
            totalAchievements++;
        }
        
        public int getAchievementLevel(AchievementType type) {
            return achievementLevels.getOrDefault(type, 0);
        }
        
        public int getTotalAchievements() { return totalAchievements; }
        public long getTotalAchievementTime() { return totalAchievementTime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<AchievementType, Integer> getAchievementLevels() { return achievementLevels; }
    }
}
