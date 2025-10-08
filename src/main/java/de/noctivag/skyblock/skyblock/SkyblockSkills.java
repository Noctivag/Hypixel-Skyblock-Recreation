package de.noctivag.skyblock.skyblock;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * Skyblock Skills - Player's skill progression data
 */
public class SkyblockSkills {
    
    private final UUID playerId;
    private Map<String, Integer> skillLevels;
    private Map<String, Double> skillExperience;
    
    public SkyblockSkills(UUID playerId) {
        this.playerId = playerId;
        this.skillLevels = new HashMap<>();
        this.skillExperience = new HashMap<>();
        
        // Initialize default skills
        initializeDefaultSkills();
    }
    
    /**
     * Initialize default skills
     */
    private void initializeDefaultSkills() {
        String[] skills = {"mining", "foraging", "enchanting", "farming", "combat", "fishing", "alchemy", "taming"};
        
        for (String skill : skills) {
            skillLevels.put(skill, 0);
            skillExperience.put(skill, 0.0);
        }
    }
    
    // Getters and setters
    public UUID getPlayerId() { return playerId; }
    
    public Map<String, Integer> getSkillLevels() { return skillLevels; }
    public void setSkillLevels(Map<String, Integer> skillLevels) { this.skillLevels = skillLevels; }
    
    public Map<String, Double> getSkillExperience() { return skillExperience; }
    public void setSkillExperience(Map<String, Double> skillExperience) { this.skillExperience = skillExperience; }
    
    /**
     * Get skill level
     */
    public int getSkillLevel(String skillName) {
        return skillLevels.getOrDefault(skillName, 0);
    }
    
    /**
     * Set skill level
     */
    public void setSkillLevel(String skillName, int level) {
        skillLevels.put(skillName, level);
    }
    
    /**
     * Get skill experience
     */
    public double getSkillExperience(String skillName) {
        return skillExperience.getOrDefault(skillName, 0.0);
    }
    
    /**
     * Set skill experience
     */
    public void setSkillExperience(String skillName, double experience) {
        skillExperience.put(skillName, experience);
    }
    
    /**
     * Add skill experience
     */
    public void addSkillExperience(String skillName, double experience) {
        double currentExp = getSkillExperience(skillName);
        setSkillExperience(skillName, currentExp + experience);
    }

    // Placeholder methods for compatibility
    public int getLevel(String skillName) {
        return getSkillLevel(skillName);
    }
    
    public int getLevel(de.noctivag.skyblock.skyblock.SkyblockManager.SkyblockSkill skill) {
        return getSkillLevel(skill.name().toLowerCase());
    }

    public int getXP(String skillName) {
        return (int) getSkillExperience(skillName);
    }

    public int getXPToNextLevel(String skillName) {
        return 100; // Placeholder
    }
    
    public void addXP(de.noctivag.skyblock.skyblock.SkyblockManager.SkyblockSkill skill, double xp) {
        addSkillExperience(skill.name().toLowerCase(), xp);
    }
    
    public void save() {
        // Placeholder save method
    }
}