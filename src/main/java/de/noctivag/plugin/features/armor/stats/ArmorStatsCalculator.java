package de.noctivag.plugin.features.armor.stats;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.armor.config.ArmorConfig;
import org.bukkit.entity.Player;
import java.util.Map;

/**
 * Armor Stats Calculator
 */
public class ArmorStatsCalculator {
    
    public static class ArmorStats {
        private final int defense;
        private final int health;
        private final int strength;
        private final int critChance;
        private final int critDamage;
        private final int attackSpeed;
        private final int intelligence;
        private final int speed;
        private final int ferocity;
        private final int abilityDamage;
        
        public ArmorStats(int defense, int health, int strength, int critChance, int critDamage,
                         int attackSpeed, int intelligence, int speed, int ferocity, int abilityDamage) {
            this.defense = defense;
            this.health = health;
            this.strength = strength;
            this.critChance = critChance;
            this.critDamage = critDamage;
            this.attackSpeed = attackSpeed;
            this.intelligence = intelligence;
            this.speed = speed;
            this.ferocity = ferocity;
            this.abilityDamage = abilityDamage;
        }
        
        public int getDefense() { return defense; }
        public int getHealth() { return health; }
        public int getStrength() { return strength; }
        public int getCritChance() { return critChance; }
        public int getCritDamage() { return critDamage; }
        public int getAttackSpeed() { return attackSpeed; }
        public int getIntelligence() { return intelligence; }
        public int getSpeed() { return speed; }
        public int getFerocity() { return ferocity; }
        public int getAbilityDamage() { return abilityDamage; }
    }
    
    public ArmorStats calculatePlayerStats(Player player, Map<String, ArmorConfig> equippedArmor) {
        int totalDefense = 0;
        int totalHealth = 0;
        int totalStrength = 0;
        int totalCritChance = 0;
        int totalCritDamage = 0;
        int totalAttackSpeed = 0;
        int totalIntelligence = 0;
        int totalSpeed = 0;
        int totalFerocity = 0;
        int totalAbilityDamage = 0;
        
        // Calculate stats from equipped armor
        for (ArmorConfig armor : equippedArmor.values()) {
            totalDefense += armor.getBaseDefense();
            // Add other stat calculations based on armor type
        }
        
        return new ArmorStats(totalDefense, totalHealth, totalStrength, totalCritChance, 
                             totalCritDamage, totalAttackSpeed, totalIntelligence, 
                             totalSpeed, totalFerocity, totalAbilityDamage);
    }
}
