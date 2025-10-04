package de.noctivag.skyblock.features.armor;
import org.bukkit.inventory.ItemStack;

/**
 * Represents armor statistics
 */
public class ArmorStats {
    
    private final int health;
    private final int defense;
    private final int strength;
    private final int intelligence;
    private final int speed;
    
    public ArmorStats(int health, int defense, int strength, int intelligence, int speed) {
        this.health = health;
        this.defense = defense;
        this.strength = strength;
        this.intelligence = intelligence;
        this.speed = speed;
    }
    
    /**
     * Add another ArmorStats to this one
     */
    public ArmorStats add(ArmorStats other) {
        return new ArmorStats(
            this.health + other.health,
            this.defense + other.defense,
            this.strength + other.strength,
            this.intelligence + other.intelligence,
            this.speed + other.speed
        );
    }
    
    /**
     * Multiply stats by a factor
     */
    public ArmorStats multiply(double factor) {
        return new ArmorStats(
            (int) (this.health * factor),
            (int) (this.defense * factor),
            (int) (this.strength * factor),
            (int) (this.intelligence * factor),
            (int) (this.speed * factor)
        );
    }
    
    /**
     * Get total stat value
     */
    public int getTotalStats() {
        return health + defense + strength + intelligence + speed;
    }
    
    /**
     * Get effective health (health * (1 + defense/100))
     */
    public double getEffectiveHealth() {
        return health * (1.0 + defense / 100.0);
    }
    
    /**
     * Get damage multiplier from strength
     */
    public double getDamageMultiplier() {
        return 1.0 + strength / 100.0;
    }
    
    /**
     * Get mana capacity from intelligence
     */
    public double getManaCapacity() {
        return intelligence * 2.0; // 2 mana per intelligence
    }
    
    /**
     * Get movement speed from speed stat
     */
    public double getMovementSpeed() {
        return 1.0 + speed / 100.0; // 1% movement speed per speed stat
    }
    
    /**
     * Check if stats are balanced
     */
    public boolean isBalanced() {
        int max = Math.max(health, Math.max(defense, Math.max(strength, Math.max(intelligence, speed))));
        int min = Math.min(health, Math.min(defense, Math.min(strength, Math.min(intelligence, speed))));
        
        return (max - min) <= max * 0.5; // Max 50% difference between highest and lowest stat
    }
    
    /**
     * Get dominant stat type
     */
    public String getDominantStat() {
        int max = Math.max(health, Math.max(defense, Math.max(strength, Math.max(intelligence, speed))));
        
        if (max == health) return "Health";
        if (max == defense) return "Defense";
        if (max == strength) return "Strength";
        if (max == intelligence) return "Intelligence";
        if (max == speed) return "Speed";
        
        return "Balanced";
    }
    
    /**
     * Get stat distribution as percentages
     */
    public StatDistribution getStatDistribution() {
        int total = getTotalStats();
        if (total == 0) {
            return new StatDistribution(0, 0, 0, 0, 0);
        }
        
        return new StatDistribution(
            (double) health / total * 100,
            (double) defense / total * 100,
            (double) strength / total * 100,
            (double) intelligence / total * 100,
            (double) speed / total * 100
        );
    }
    
    // Getters
    public int getHealth() {
        return health;
    }
    
    public int getDefense() {
        return defense;
    }
    
    public int getStrength() {
        return strength;
    }
    
    public int getIntelligence() {
        return intelligence;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    @Override
    public String toString() {
        return String.format("ArmorStats{HP=%d, DEF=%d, STR=%d, INT=%d, SPD=%d}", 
            health, defense, strength, intelligence, speed);
    }
    
    /**
     * Stat distribution percentages
     */
    public static class StatDistribution {
        private final double healthPercent;
        private final double defensePercent;
        private final double strengthPercent;
        private final double intelligencePercent;
        private final double speedPercent;
        
        public StatDistribution(double healthPercent, double defensePercent, double strengthPercent, 
                              double intelligencePercent, double speedPercent) {
            this.healthPercent = healthPercent;
            this.defensePercent = defensePercent;
            this.strengthPercent = strengthPercent;
            this.intelligencePercent = intelligencePercent;
            this.speedPercent = speedPercent;
        }
        
        public double getHealthPercent() {
            return healthPercent;
        }
        
        public double getDefensePercent() {
            return defensePercent;
        }
        
        public double getStrengthPercent() {
            return strengthPercent;
        }
        
        public double getIntelligencePercent() {
            return intelligencePercent;
        }
        
        public double getSpeedPercent() {
            return speedPercent;
        }
        
        @Override
        public String toString() {
            return String.format("Distribution{HP=%.1f%%, DEF=%.1f%%, STR=%.1f%%, INT=%.1f%%, SPD=%.1f%%}", 
                healthPercent, defensePercent, strengthPercent, intelligencePercent, speedPercent);
        }
    }
}
