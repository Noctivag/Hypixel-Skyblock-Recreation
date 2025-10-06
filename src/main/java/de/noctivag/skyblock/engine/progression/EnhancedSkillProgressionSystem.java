package de.noctivag.skyblock.engine.progression;
import java.util.UUID;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.engine.progression.types.HypixelSkillType;
import de.noctivag.skyblock.engine.progression.types.SkillLevelData;
import de.noctivag.skyblock.engine.progression.types.SkillProgress;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enhanced Skill Progression System - Exponential XP Curve Implementation
 * 
 * Implements the exponential skill progression system with precise XP curves
 * as specified in the requirements. This system enforces the "Grind" as a
 * core design element through intentional time gates and exponential scaling.
 * 
 * Features:
 * - Loads XP requirements from skill_xp_table.json configuration
 * - Implements discontinuous XP curves without algebraic formulas
 * - Enforces exact milestone requirements (Level 20: 1,722,425 XP, Level 30: 8,022,425 XP, Level 60: 111,672,425 XP)
 * - Provides time-gated progression for long-term engagement
 */
public class EnhancedSkillProgressionSystem implements Service {
    
    private final HypixelSkillSystem skillSystem;
    private final Map<HypixelSkillType, SkillLevelData> skillLevelData;
    private final Map<UUID, Map<HypixelSkillType, SkillProgress>> playerSkills;
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public EnhancedSkillProgressionSystem(HypixelSkillSystem skillSystem) {
        this.skillSystem = skillSystem;
        this.skillLevelData = new ConcurrentHashMap<>();
        this.playerSkills = new ConcurrentHashMap<>();
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        
        // Load skill XP configuration
        loadSkillXPConfiguration();
        
        // Initialize skill level data
        initializeSkillLevelData();
        
        status = SystemStatus.RUNNING;
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        
        // Save player skill data
        savePlayerSkillData();
        
        status = SystemStatus.DISABLED;
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
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
    
    @Override
    public String getName() {
        return "EnhancedSkillProgressionSystem";
    }
    
    /**
     * Load skill XP configuration from JSON file
     */
    private void loadSkillXPConfiguration() {
        try {
            InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("skill_xp_table.json");
            
            if (inputStream == null) {
                System.err.println("skill_xp_table.json not found, using default values");
                return;
            }
            
            JsonObject config = JsonParser.parseReader(new InputStreamReader(inputStream))
                .getAsJsonObject()
                .getAsJsonObject("skill_xp_requirements");
            
            JsonObject skills = config.getAsJsonObject("skills");
            
            for (Map.Entry<String, com.google.gson.JsonElement> entry : skills.entrySet()) {
                String skillName = entry.getKey();
                JsonObject skillData = entry.getValue().getAsJsonObject();
                
                try {
                    HypixelSkillType skillType = HypixelSkillType.valueOf(skillName);
                    int maxLevel = skillData.get("max_level").getAsInt();
                    double scaleFactor = skillData.get("scale_factor").getAsDouble();
                    
                    JsonObject levels = skillData.getAsJsonObject("levels");
                    Map<Integer, Double> levelXPRequirements = new HashMap<>();
                    
                    for (Map.Entry<String, com.google.gson.JsonElement> levelEntry : levels.entrySet()) {
                        int level = Integer.parseInt(levelEntry.getKey());
                        double xp = levelEntry.getValue().getAsDouble();
                        levelXPRequirements.put(level, xp);
                    }
                    
                    // Create enhanced skill level data
                    EnhancedSkillLevelData enhancedData = new EnhancedSkillLevelData(
                        skillType, maxLevel, scaleFactor, levelXPRequirements
                    );
                    
                    skillLevelData.put(skillType, enhancedData);
                    
                    System.out.println("Loaded skill data for " + skillType.getDisplayName() + 
                        " (Max Level: " + maxLevel + ", Scale Factor: " + scaleFactor + ")");
                    
                } catch (IllegalArgumentException e) {
                    System.err.println("Unknown skill type: " + skillName);
                }
            }
            
            inputStream.close();
            
        } catch (Exception e) {
            System.err.println("Failed to load skill XP configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize skill level data for all skills
     */
    private void initializeSkillLevelData() {
        // Ensure all skills have level data
        for (HypixelSkillType skillType : HypixelSkillType.values()) {
            if (!skillLevelData.containsKey(skillType)) {
                skillLevelData.put(skillType, new SkillLevelData(skillType));
            }
        }
    }
    
    /**
     * Add experience to a player's skill
     */
    public CompletableFuture<SkillProgressResult> addExperience(UUID playerId, HypixelSkillType skillType, double experience) {
        return CompletableFuture.supplyAsync(() -> {
            if (experience <= 0) {
                return new SkillProgressResult(playerId, skillType.name(), 0, 0, 0.0, 0.0, 0.0);
            }
            
            // Get or create player skill progress
            SkillProgress skillProgress = getPlayerSkillProgress(playerId, skillType);
            
            // Get current level before adding experience
            int oldLevel = skillProgress.getLevel();
            double oldXP = skillProgress.getTotalExperience();
            
            // Add experience
            skillProgress.addExperience(experience);
            
            // Get new level and experience
            int newLevel = skillProgress.getLevel();
            double newXP = skillProgress.getTotalExperience();
            
            // Calculate level ups
            int levelsGained = newLevel - oldLevel;
            double xpProgress = skillProgress.getXPProgressInLevel();
            double xpToNext = skillProgress.getExperienceToNextLevel();
            
            // Check if player reached a milestone
            boolean reachedMilestone = isMilestoneLevel(skillType, newLevel);
            
            String message = String.format("Added %.1f XP to %s (Level %d -> %d)", 
                experience, skillType.getDisplayName(), oldLevel, newLevel);
            
            return new SkillProgressResult(playerId, skillType.name(), oldLevel, newLevel, 
                skillProgress.getTotalExperience() - experience, skillProgress.getTotalExperience(), experience);
        });
    }
    
    /**
     * Get player skill progress
     */
    public SkillProgress getPlayerSkillProgress(UUID playerId, HypixelSkillType skillType) {
        return playerSkills.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
            .computeIfAbsent(skillType, k -> new SkillProgress(skillType, 0.0));
    }
    
    /**
     * Get player skill level
     */
    public int getPlayerSkillLevel(UUID playerId, HypixelSkillType skillType) {
        return getPlayerSkillProgress(playerId, skillType).getLevel();
    }
    
    /**
     * Get player skill experience
     */
    public double getPlayerSkillExperience(UUID playerId, HypixelSkillType skillType) {
        return getPlayerSkillProgress(playerId, skillType).getTotalExperience();
    }
    
    /**
     * Get XP required for next level
     */
    public double getXPToNextLevel(UUID playerId, HypixelSkillType skillType) {
        return getPlayerSkillProgress(playerId, skillType).getExperienceToNextLevel();
    }
    
    /**
     * Get XP progress within current level
     */
    public double getXPProgressInLevel(UUID playerId, HypixelSkillType skillType) {
        return getPlayerSkillProgress(playerId, skillType).getXPProgressInLevel();
    }
    
    /**
     * Get skill level data
     */
    public SkillLevelData getSkillLevelData(HypixelSkillType skillType) {
        return skillLevelData.get(skillType);
    }
    
    /**
     * Check if a level is a milestone
     */
    public boolean isMilestoneLevel(HypixelSkillType skillType, int level) {
        SkillLevelData data = skillLevelData.get(skillType);
        return data != null && data.isMilestoneLevel(level);
    }
    
    /**
     * Get milestone levels for a skill
     */
    public int[] getMilestoneLevels(HypixelSkillType skillType) {
        SkillLevelData data = skillLevelData.get(skillType);
        return data != null ? data.getMilestoneLevels() : new int[0];
    }
    
    /**
     * Get critical milestone information
     */
    public Map<String, Object> getCriticalMilestones(HypixelSkillType skillType) {
        Map<String, Object> milestones = new HashMap<>();
        
        SkillLevelData data = skillLevelData.get(skillType);
        if (data == null) return milestones;
        
        // Level 20 milestone (for 60-level skills)
        if (skillType.getMaxLevel() >= 20) {
            double xpLevel20 = data.getCumulativeXPForLevel(20);
            milestones.put("level_20", Map.of(
                "level", 20,
                "xp_required", data.getXPRequiredForLevel(20),
                "cumulative_xp", xpLevel20
            ));
        }
        
        // Level 30 milestone (for 60-level skills)
        if (skillType.getMaxLevel() >= 30) {
            double xpLevel30 = data.getCumulativeXPForLevel(30);
            milestones.put("level_30", Map.of(
                "level", 30,
                "xp_required", data.getXPRequiredForLevel(30),
                "cumulative_xp", xpLevel30
            ));
        }
        
        // Level 60 milestone (for 60-level skills)
        if (skillType.getMaxLevel() >= 60) {
            double xpLevel60 = data.getCumulativeXPForLevel(60);
            milestones.put("level_60", Map.of(
                "level", 60,
                "xp_required", data.getXPRequiredForLevel(60),
                "cumulative_xp", xpLevel60
            ));
        }
        
        return milestones;
    }
    
    /**
     * Get player skill statistics
     */
    public PlayerSkillStatistics getPlayerSkillStatistics(UUID playerId) {
        Map<HypixelSkillType, SkillProgress> skills = playerSkills.get(playerId);
        if (skills == null) {
            skills = new HashMap<>();
        }
        
        int totalLevels = skills.values().stream()
            .mapToInt(SkillProgress::getLevel)
            .sum();
        
        double totalXP = skills.values().stream()
            .mapToDouble(SkillProgress::getTotalExperience)
            .sum();
        
        int skillsAtMaxLevel = (int) skills.values().stream()
            .filter(progress -> progress.getLevel() >= progress.getSkillType().getMaxLevel())
            .count();
        
        int milestoneLevelsReached = 0;
        for (SkillProgress progress : skills.values()) {
            int[] milestones = getMilestoneLevels(progress.getSkillType());
            for (int milestone : milestones) {
                if (progress.getLevel() >= milestone) {
                    milestoneLevelsReached++;
                }
            }
        }
        
        // Convert skills map to String -> SkillStatistics map
        Map<String, SkillStatistics> skillsMap = new HashMap<>();
        for (Map.Entry<HypixelSkillType, SkillProgress> entry : skills.entrySet()) {
            SkillProgress progress = entry.getValue();
            skillsMap.put(entry.getKey().name(), new SkillStatistics(
                entry.getKey().name(), 
                progress.getLevel(), 
                progress.getTotalExperience()
            ));
        }
        PlayerSkillStatistics stats = new PlayerSkillStatistics(playerId, skillsMap);
        // Note: Additional statistics would need to be stored in the achievements map
        // or new fields would need to be added to PlayerSkillStatistics
        return stats;
    }
    
    /**
     * Save player skill data
     */
    private void savePlayerSkillData() {
        // TODO: Implement database persistence for player skill data
        // This will integrate with the existing database system
    }
    
    /**
     * Enhanced Skill Level Data implementation
     */
    private static class EnhancedSkillLevelData extends SkillLevelData {
        private final Map<Integer, Double> customLevelXPRequirements;
        
        public EnhancedSkillLevelData(HypixelSkillType skillType, int maxLevel, 
                                      double scaleFactor, Map<Integer, Double> levelXPRequirements) {
            super(skillType);
            this.customLevelXPRequirements = new HashMap<>(levelXPRequirements);
        }
        
        @Override
        public double getXPRequiredForLevel(int level) {
            if (customLevelXPRequirements.containsKey(level)) {
                return customLevelXPRequirements.get(level);
            }
            return super.getXPRequiredForLevel(level);
        }
    }
}
