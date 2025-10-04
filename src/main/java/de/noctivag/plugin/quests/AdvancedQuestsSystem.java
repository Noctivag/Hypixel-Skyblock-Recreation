package de.noctivag.plugin.quests;
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
 * Advanced Quests System - Hypixel Skyblock Style
 */
public class AdvancedQuestsSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerQuests> playerQuests = new ConcurrentHashMap<>();
    private final Map<QuestType, QuestConfig> questConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> questTasks = new ConcurrentHashMap<>();
    
    public AdvancedQuestsSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeQuestConfigs();
        startQuestUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeQuestConfigs() {
        questConfigs.put(QuestType.TUTORIAL, new QuestConfig(
            "Tutorial Quest", "§aTutorial Quest", Material.BOOK,
            "§7Learn the basics of the game.",
            QuestCategory.TUTORIAL, 1, Arrays.asList("§7- Learn basics", "§7- Get started"),
            Arrays.asList("§7- 1x Book", "§7- 1x Experience")
        ));
        
        questConfigs.put(QuestType.DAILY, new QuestConfig(
            "Daily Quest", "§eDaily Quest", Material.CLOCK,
            "§7Complete daily tasks for rewards.",
            QuestCategory.DAILY, 1, Arrays.asList("§7- Daily rewards", "§7- Repeatable"),
            Arrays.asList("§7- 1x Clock", "§7- 1x Experience")
        ));
    }
    
    private void startQuestUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerQuests> entry : playerQuests.entrySet()) {
                    PlayerQuests quests = entry.getValue();
                    quests.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BOOKSHELF) {
            openQuestGUI(player);
        }
    }
    
    private void openQuestGUI(Player player) {
        player.sendMessage("§aQuest GUI geöffnet!");
    }
    
    public PlayerQuests getPlayerQuests(UUID playerId) {
        return playerQuests.computeIfAbsent(playerId, k -> new PlayerQuests(playerId));
    }
    
    public QuestConfig getQuestConfig(QuestType type) {
        return questConfigs.get(type);
    }
    
    public List<QuestType> getAllQuestTypes() {
        return new ArrayList<>(questConfigs.keySet());
    }
    
    public enum QuestType {
        TUTORIAL, DAILY, WEEKLY, MONTHLY, SPECIAL, EVENT, ACHIEVEMENT, COLLECTION
    }
    
    public enum QuestCategory {
        TUTORIAL("§aTutorial", 1.0),
        DAILY("§eDaily", 1.2),
        WEEKLY("§6Weekly", 1.1),
        MONTHLY("§5Monthly", 1.3),
        SPECIAL("§cSpecial", 1.5);
        
        private final String displayName;
        private final double multiplier;
        
        QuestCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class QuestConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final QuestCategory category;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public QuestConfig(String name, String displayName, Material icon, String description,
                          QuestCategory category, int maxLevel, List<String> features, List<String> requirements) {
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
        public QuestCategory getCategory() { return category; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerQuests {
        private final UUID playerId;
        private final Map<QuestType, Integer> questLevels = new ConcurrentHashMap<>();
        private int totalQuests = 0;
        private long totalQuestTime = 0;
        private long lastUpdate;
        
        public PlayerQuests(UUID playerId) {
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
            // Save quest data to database
        }
        
        public void addQuest(QuestType type, int level) {
            questLevels.put(type, level);
            totalQuests++;
        }
        
        public int getQuestLevel(QuestType type) {
            return questLevels.getOrDefault(type, 0);
        }
        
        public int getTotalQuests() { return totalQuests; }
        public long getTotalQuestTime() { return totalQuestTime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<QuestType, Integer> getQuestLevels() { return questLevels; }
    }
}
