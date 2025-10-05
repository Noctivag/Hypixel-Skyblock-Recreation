package de.noctivag.skyblock.quests;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Quest system for managing player quests
 */
public class QuestSystem implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<String, Quest> quests = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerQuestData> playerData = new ConcurrentHashMap<>();
    
    public QuestSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing QuestSystem...");
        
        // Load quests from configuration
        loadQuestsFromConfig();
        
        // Register event listeners
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("QuestSystem initialized with " + quests.size() + " quests.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down QuestSystem...");
        
        // Save all player data
        saveAllPlayerData();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("QuestSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "QuestSystem";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Load quests from configuration file
     */
    private void loadQuestsFromConfig() {
        File configFile = new File(plugin.getDataFolder(), "quests.yml");
        if (!configFile.exists()) {
            createDefaultQuestsConfig(configFile);
        }
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        ConfigurationSection questsSection = config.getConfigurationSection("quests");
        
        if (questsSection == null) {
            plugin.getLogger().warning("No quests section found in quests.yml");
            return;
        }
        
        for (String questId : questsSection.getKeys(false)) {
            try {
                Quest quest = loadQuestFromConfig(questsSection.getConfigurationSection(questId));
                if (quest != null) {
                    quests.put(questId, quest);
                    plugin.getLogger().info("Loaded quest: " + quest.getName());
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Failed to load quest " + questId + ": " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Create default quests configuration
     */
    private void createDefaultQuestsConfig(File configFile) {
        try {
            YamlConfiguration config = new YamlConfiguration();
            
            // Example mining quest
            config.set("quests.first_mining.id", "first_mining");
            config.set("quests.first_mining.name", "First Mining");
            config.set("quests.first_mining.description", "Mine your first block!");
            config.set("quests.first_mining.type", "MINING");
            config.set("quests.first_mining.difficulty", "EASY");
            config.set("quests.first_mining.required_level", 1);
            config.set("quests.first_mining.rewards.coins", 100);
            config.set("quests.first_mining.rewards.xp", 50);
            config.set("quests.first_mining.objectives.mine_blocks.type", "MINE_BLOCKS");
            config.set("quests.first_mining.objectives.mine_blocks.amount", 1);
            config.set("quests.first_mining.objectives.mine_blocks.material", "STONE");
            
            // Example combat quest
            config.set("quests.first_combat.id", "first_combat");
            config.set("quests.first_combat.name", "First Combat");
            config.set("quests.first_combat.description", "Defeat your first enemy!");
            config.set("quests.first_combat.type", "COMBAT");
            config.set("quests.first_combat.difficulty", "EASY");
            config.set("quests.first_combat.required_level", 1);
            config.set("quests.first_combat.rewards.coins", 150);
            config.set("quests.first_combat.rewards.xp", 75);
            config.set("quests.first_combat.objectives.kill_mobs.type", "KILL_MOBS");
            config.set("quests.first_combat.objectives.kill_mobs.amount", 1);
            config.set("quests.first_combat.objectives.kill_mobs.entity_type", "ZOMBIE");
            
            config.save(configFile);
            plugin.getLogger().info("Created default quests configuration: " + configFile.getName());
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create default quests configuration", e);
        }
    }
    
    /**
     * Load a single quest from configuration
     */
    private Quest loadQuestFromConfig(ConfigurationSection questSection) {
        if (questSection == null) return null;
        
        String id = questSection.getString("id");
        String name = questSection.getString("name");
        String description = questSection.getString("description");
        String typeStr = questSection.getString("type", "CUSTOM");
        String difficultyStr = questSection.getString("difficulty", "EASY");
        int requiredLevel = questSection.getInt("required_level", 1);
        
        // Load rewards
        Map<String, Object> rewards = new HashMap<>();
        ConfigurationSection rewardsSection = questSection.getConfigurationSection("rewards");
        if (rewardsSection != null) {
            for (String rewardKey : rewardsSection.getKeys(false)) {
                rewards.put(rewardKey, rewardsSection.get(rewardKey));
            }
        }
        
        // Load objectives
        List<QuestObjective> objectives = new ArrayList<>();
        ConfigurationSection objectivesSection = questSection.getConfigurationSection("objectives");
        if (objectivesSection != null) {
            for (String objectiveKey : objectivesSection.getKeys(false)) {
                QuestObjective objective = loadObjectiveFromConfig(objectivesSection.getConfigurationSection(objectiveKey));
                if (objective != null) {
                    objectives.add(objective);
                }
            }
        }
        
        Quest.QuestType type;
        try {
            type = Quest.QuestType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = Quest.QuestType.CUSTOM;
        }
        
        Quest.Difficulty difficulty;
        try {
            difficulty = Quest.Difficulty.valueOf(difficultyStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            difficulty = Quest.Difficulty.EASY;
        }
        
        return new Quest(id, name, description, type, difficulty, requiredLevel, rewards, objectives);
    }
    
    /**
     * Load a single objective from configuration
     */
    private QuestObjective loadObjectiveFromConfig(ConfigurationSection objectiveSection) {
        if (objectiveSection == null) return null;
        
        String typeStr = objectiveSection.getString("type", "CUSTOM");
        int amount = objectiveSection.getInt("amount", 1);
        Map<String, Object> parameters = new HashMap<>();
        
        // Load all parameters
        for (String key : objectiveSection.getKeys(false)) {
            if (!key.equals("type") && !key.equals("amount")) {
                parameters.put(key, objectiveSection.get(key));
            }
        }
        
        QuestObjective.ObjectiveType type;
        try {
            type = QuestObjective.ObjectiveType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = QuestObjective.ObjectiveType.CUSTOM;
        }
        
        return new QuestObjective(type, amount, parameters);
    }
    
    /**
     * Give quest to player
     */
    public boolean giveQuest(UUID playerUuid, String questId) {
        Quest quest = quests.get(questId);
        if (quest == null) {
            return false;
        }
        
        PlayerQuestData data = getPlayerQuestData(playerUuid);
        if (data.hasQuest(questId)) {
            return false; // Player already has this quest
        }
        
        // Check if player meets requirements
        if (!meetsQuestRequirements(playerUuid, quest)) {
            return false;
        }
        
        // Add quest to player
        data.addQuest(questId);
        
        // Notify player
        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            player.sendMessage("§a§lNEW QUEST! §e" + quest.getName());
            player.sendMessage("§7" + quest.getDescription());
        }
        
        plugin.getLogger().info("Gave quest " + questId + " to player " + playerUuid);
        return true;
    }
    
    /**
     * Complete quest for player
     */
    public boolean completeQuest(UUID playerUuid, String questId) {
        Quest quest = quests.get(questId);
        if (quest == null) {
            return false;
        }
        
        PlayerQuestData data = getPlayerQuestData(playerUuid);
        if (!data.hasQuest(questId) || data.isQuestCompleted(questId)) {
            return false;
        }
        
        // Check if all objectives are completed
        if (!areAllObjectivesCompleted(playerUuid, quest)) {
            return false;
        }
        
        // Complete quest
        data.completeQuest(questId);
        
        // Give rewards
        giveQuestRewards(playerUuid, quest);
        
        // Notify player
        Player player = Bukkit.getPlayer(playerUuid);
        if (player != null) {
            player.sendMessage("§a§lQUEST COMPLETED! §e" + quest.getName());
            player.sendMessage("§aYou received rewards!");
        }
        
        plugin.getLogger().info("Completed quest " + questId + " for player " + playerUuid);
        return true;
    }
    
    /**
     * Update quest progress
     */
    public void updateQuestProgress(UUID playerUuid, String questId, String objectiveType, int amount) {
        Quest quest = quests.get(questId);
        if (quest == null) {
            return;
        }
        
        PlayerQuestData data = getPlayerQuestData(playerUuid);
        if (!data.hasQuest(questId) || data.isQuestCompleted(questId)) {
            return;
        }
        
        // Update objective progress
        data.updateObjectiveProgress(questId, objectiveType, amount);
        
        // Check if quest is now completed
        if (areAllObjectivesCompleted(playerUuid, quest)) {
            completeQuest(playerUuid, questId);
        }
    }
    
    /**
     * Check if player meets quest requirements
     */
    private boolean meetsQuestRequirements(UUID playerUuid, Quest quest) {
        // Check level requirement
        // Implementation would check player's level
        
        // Check if quest is already completed
        PlayerQuestData data = getPlayerQuestData(playerUuid);
        if (data.isQuestCompleted(quest.getId())) {
            return false;
        }
        
        return true; // Placeholder
    }
    
    /**
     * Check if all objectives are completed
     */
    private boolean areAllObjectivesCompleted(UUID playerUuid, Quest quest) {
        PlayerQuestData data = getPlayerQuestData(playerUuid);
        
        for (QuestObjective objective : quest.getObjectives()) {
            if (!data.isObjectiveCompleted(quest.getId(), objective.getType().name())) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Give quest rewards
     */
    private void giveQuestRewards(UUID playerUuid, Quest quest) {
        Player player = Bukkit.getPlayer(playerUuid);
        if (player == null) return;
        
        Map<String, Object> rewards = quest.getRewards();
        
        // Give coins
        if (rewards.containsKey("coins")) {
            double coins = ((Number) rewards.get("coins")).doubleValue();
            // Give coins to player (implementation depends on economy system)
            player.sendMessage("§a+ " + coins + " coins");
        }
        
        // Give XP
        if (rewards.containsKey("xp")) {
            double xp = ((Number) rewards.get("xp")).doubleValue();
            // Give XP to player (implementation depends on skill system)
            player.sendMessage("§a+ " + xp + " XP");
        }
        
        // Add more reward types as needed
    }
    
    /**
     * Save all player data
     */
    private void saveAllPlayerData() {
        // Implementation would save to database
        plugin.getLogger().info("Saved quest data for " + playerData.size() + " players");
    }
    
    /**
     * Get player quest data
     */
    public PlayerQuestData getPlayerQuestData(UUID playerUuid) {
        return playerData.computeIfAbsent(playerUuid, PlayerQuestData::new);
    }
    
    /**
     * Get all quests
     */
    public Map<String, Quest> getQuests() {
        return new HashMap<>(quests);
    }
    
    /**
     * Get quest by ID
     */
    public Quest getQuest(String questId) {
        return quests.get(questId);
    }
    
    // Event handlers
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Load player quest data when they join
        getPlayerQuestData(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Save player quest data when they leave
        // Implementation would save to database
    }
}