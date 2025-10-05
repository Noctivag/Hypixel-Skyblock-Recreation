package de.noctivag.skyblock.skyblock;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Skills System - Manages player skill progression
 */
public class AdvancedSkillsSystem {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerSkills> playerSkills = new ConcurrentHashMap<>();
    
    public AdvancedSkillsSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    /**
     * Initialize player skills
     */
    public void initializePlayer(Player player) {
        UUID playerId = player.getUniqueId();
        
        PlayerSkills skills = playerSkills.computeIfAbsent(playerId, k -> 
            new PlayerSkills(playerId));
        
        plugin.getLogger().info("Initialized skills for player: " + player.getName());
    }
    
    /**
     * Get player skills
     */
    public PlayerSkills getPlayerSkills(UUID playerId) {
        return playerSkills.get(playerId);
    }
    
    /**
     * Add skill experience
     */
    public void addSkillExperience(UUID playerId, String skillName, double experience) {
        PlayerSkills skills = playerSkills.get(playerId);
        if (skills != null) {
            skills.addExperience(skillName, experience);
        }
    }
    
    /**
     * Get skill level
     */
    public int getSkillLevel(UUID playerId, String skillName) {
        PlayerSkills skills = playerSkills.get(playerId);
        if (skills != null) {
            return skills.getLevel(skillName);
        }
        return 0;
    }
    
    /**
     * Player Skills data
     */
    public static class PlayerSkills {
        private final UUID playerId;
        private final Map<String, Double> experience;
        private final Map<String, Integer> levels;
        
        public PlayerSkills(UUID playerId) {
            this.playerId = playerId;
            this.experience = new ConcurrentHashMap<>();
            this.levels = new ConcurrentHashMap<>();
            
            // Initialize default skills
            String[] skills = {"mining", "foraging", "enchanting", "farming", "combat", "fishing", "alchemy", "taming"};
            for (String skill : skills) {
                experience.put(skill, 0.0);
                levels.put(skill, 0);
            }
        }
        
        public void addExperience(String skillName, double exp) {
            double currentExp = experience.getOrDefault(skillName, 0.0);
            experience.put(skillName, currentExp + exp);
            
            // Check for level up
            checkLevelUp(skillName);
        }
        
        private void checkLevelUp(String skillName) {
            double currentExp = experience.get(skillName);
            int currentLevel = levels.get(skillName);
            
            // Simple level calculation (100 exp per level)
            int newLevel = (int) (currentExp / 100);
            if (newLevel > currentLevel) {
                levels.put(skillName, newLevel);
            }
        }
        
        public int getLevel(String skillName) {
            return levels.getOrDefault(skillName, 0);
        }
        
        public double getExperience(String skillName) {
            return experience.getOrDefault(skillName, 0.0);
        }
    }
}