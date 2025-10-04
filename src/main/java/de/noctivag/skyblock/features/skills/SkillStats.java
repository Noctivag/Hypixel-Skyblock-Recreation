package de.noctivag.skyblock.features.skills;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.skills.types.SkillType;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents skill-based statistics for a player
 */
public class SkillStats {
    
    private final Map<String, Double> bonuses = new HashMap<>();
    private final Map<SkillType, Integer> skillLevels = new HashMap<>();
    private final double skillPower;
    private final double averageLevel;
    private final int totalLevel;
    
    public SkillStats(Map<String, Double> bonuses, Map<SkillType, Integer> skillLevels, 
                     double skillPower, double averageLevel, int totalLevel) {
        this.bonuses.putAll(bonuses);
        this.skillLevels.putAll(skillLevels);
        this.skillPower = skillPower;
        this.averageLevel = averageLevel;
        this.totalLevel = totalLevel;
    }
    
    /**
     * Get a specific bonus
     */
    public double getBonus(String bonusType) {
        return bonuses.getOrDefault(bonusType, 0.0);
    }
    
    /**
     * Get all bonuses
     */
    public Map<String, Double> getAllBonuses() {
        return new HashMap<>(bonuses);
    }
    
    /**
     * Get skill level for a specific skill
     */
    public int getSkillLevel(SkillType skillType) {
        return skillLevels.getOrDefault(skillType, 0);
    }
    
    /**
     * Get all skill levels
     */
    public Map<SkillType, Integer> getAllSkillLevels() {
        return new HashMap<>(skillLevels);
    }
    
    /**
     * Get skill power (weighted average)
     */
    public double getSkillPower() {
        return skillPower;
    }
    
    /**
     * Get average skill level
     */
    public double getAverageLevel() {
        return averageLevel;
    }
    
    /**
     * Get total skill level
     */
    public int getTotalLevel() {
        return totalLevel;
    }
    
    /**
     * Get combat-related bonuses
     */
    public Map<String, Double> getCombatBonuses() {
        Map<String, Double> combatBonuses = new HashMap<>();
        
        combatBonuses.put("damage", getBonus("damage"));
        combatBonuses.put("critChance", getBonus("critChance"));
        combatBonuses.put("critDamage", getBonus("critDamage"));
        combatBonuses.put("strength", getBonus("strength"));
        combatBonuses.put("defense", getBonus("defense"));
        
        return combatBonuses;
    }
    
    /**
     * Get gathering-related bonuses
     */
    public Map<String, Double> getGatheringBonuses() {
        Map<String, Double> gatheringBonuses = new HashMap<>();
        
        gatheringBonuses.put("miningSpeed", getBonus("miningSpeed"));
        gatheringBonuses.put("foragingSpeed", getBonus("foragingSpeed"));
        gatheringBonuses.put("fortune", getBonus("fortune"));
        gatheringBonuses.put("luck", getBonus("luck"));
        
        return gatheringBonuses;
    }
    
    /**
     * Get magic-related bonuses
     */
    public Map<String, Double> getMagicBonuses() {
        Map<String, Double> magicBonuses = new HashMap<>();
        
        magicBonuses.put("enchantingPower", getBonus("enchantingPower"));
        magicBonuses.put("alchemyPower", getBonus("alchemyPower"));
        magicBonuses.put("intelligence", getBonus("intelligence"));
        magicBonuses.put("mana", getBonus("mana"));
        
        return magicBonuses;
    }
    
    /**
     * Get food-related bonuses
     */
    public Map<String, Double> getFoodBonuses() {
        Map<String, Double> foodBonuses = new HashMap<>();
        
        foodBonuses.put("farmingSpeed", getBonus("farmingSpeed"));
        foodBonuses.put("fishingSpeed", getBonus("fishingSpeed"));
        foodBonuses.put("health", getBonus("health"));
        foodBonuses.put("regeneration", getBonus("regeneration"));
        
        return foodBonuses;
    }
    
    /**
     * Get pet-related bonuses
     */
    public Map<String, Double> getPetBonuses() {
        Map<String, Double> petBonuses = new HashMap<>();
        
        petBonuses.put("petStats", getBonus("petStats"));
        petBonuses.put("petAbility", getBonus("petAbility"));
        petBonuses.put("petXP", getBonus("petXP"));
        
        return petBonuses;
    }
    
    /**
     * Check if player has high skill power
     */
    public boolean hasHighSkillPower() {
        return skillPower >= 25.0; // Consider 25+ as high
    }
    
    /**
     * Check if player is well-rounded (all skills similar level)
     */
    public boolean isWellRounded() {
        if (skillLevels.isEmpty()) return false;
        
        int minLevel = skillLevels.values().stream().mapToInt(Integer::intValue).min().orElse(0);
        int maxLevel = skillLevels.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        
        return (maxLevel - minLevel) <= 10; // Max 10 level difference
    }
    
    /**
     * Get skill distribution summary
     */
    public String getSkillDistributionSummary() {
        StringBuilder summary = new StringBuilder();
        
        for (SkillType skillType : SkillType.values()) {
            int level = getSkillLevel(skillType);
            summary.append(skillType.getIcon()).append(" ").append(level).append(" ");
        }
        
        return summary.toString().trim();
    }
    
    @Override
    public String toString() {
        return String.format("SkillStats{power=%.1f, avgLevel=%.1f, totalLevel=%d}", 
            skillPower, averageLevel, totalLevel);
    }
}
