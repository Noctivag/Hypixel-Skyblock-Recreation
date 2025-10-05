package de.noctivag.skyblock.features.dungeons.classes;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a dungeon class with abilities and stats
 */
public class DungeonClass {
    
    private final DungeonClassType classType;
    private final List<ClassAbility> abilities = new ArrayList<>();
    private final Map<String, Double> statMultipliers = new ConcurrentHashMap<>();
    
    private String description;
    
    public DungeonClass(DungeonClassType classType) {
        this.classType = classType;
        initializeDefaultStats();
    }
    
    /**
     * Set class description
     */
    public DungeonClass setDescription(String description) {
        this.description = description;
        return this;
    }
    
    /**
     * Add an ability to the class
     */
    public DungeonClass addAbility(ClassAbility ability) {
        abilities.add(ability);
        return this;
    }
    
    /**
     * Set stat multiplier
     */
    public DungeonClass setHealthMultiplier(double multiplier) {
        statMultipliers.put("health", multiplier);
        return this;
    }
    
    public DungeonClass setDefenseMultiplier(double multiplier) {
        statMultipliers.put("defense", multiplier);
        return this;
    }
    
    public DungeonClass setStrengthMultiplier(double multiplier) {
        statMultipliers.put("strength", multiplier);
        return this;
    }
    
    public DungeonClass setIntelligenceMultiplier(double multiplier) {
        statMultipliers.put("intelligence", multiplier);
        return this;
    }
    
    public DungeonClass setManaMultiplier(double multiplier) {
        statMultipliers.put("mana", multiplier);
        return this;
    }
    
    public DungeonClass setCritChanceMultiplier(double multiplier) {
        statMultipliers.put("critChance", multiplier);
        return this;
    }
    
    public DungeonClass setCritDamageMultiplier(double multiplier) {
        statMultipliers.put("critDamage", multiplier);
        return this;
    }
    
    public DungeonClass setSpeedMultiplier(double multiplier) {
        statMultipliers.put("speed", multiplier);
        return this;
    }
    
    public DungeonClass setKnockbackResistance(double resistance) {
        statMultipliers.put("knockbackResistance", resistance);
        return this;
    }
    
    public DungeonClass setRegenerationMultiplier(double multiplier) {
        statMultipliers.put("regeneration", multiplier);
        return this;
    }
    
    /**
     * Use an ability
     */
    public boolean useAbility(UUID playerId, int abilitySlot) {
        if (abilitySlot < 0 || abilitySlot >= abilities.size()) {
            return false;
        }
        
        ClassAbility ability = abilities.get(abilitySlot);
        Player player = Bukkit.getPlayer(playerId);
        
        if (player == null || !ability.canUse(playerId)) {
            return false;
        }
        
        return ability.use(playerId);
    }
    
    /**
     * Get stat multiplier
     */
    public double getStatMultiplier(String stat) {
        return statMultipliers.getOrDefault(stat, 1.0);
    }
    
    /**
     * Get all abilities
     */
    public List<ClassAbility> getAbilities() {
        return new ArrayList<>(abilities);
    }
    
    /**
     * Get class type
     */
    public DungeonClassType getClassType() {
        return classType;
    }
    
    /**
     * Get description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Initialize default stats
     */
    private void initializeDefaultStats() {
        statMultipliers.put("health", 1.0);
        statMultipliers.put("defense", 1.0);
        statMultipliers.put("strength", 1.0);
        statMultipliers.put("intelligence", 1.0);
        statMultipliers.put("mana", 1.0);
        statMultipliers.put("critChance", 1.0);
        statMultipliers.put("critDamage", 1.0);
        statMultipliers.put("speed", 1.0);
        statMultipliers.put("knockbackResistance", 0.0);
        statMultipliers.put("regeneration", 1.0);
    }
}
