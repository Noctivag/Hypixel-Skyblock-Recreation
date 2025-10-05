package de.noctivag.skyblock.engine;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.features.stats.types.PrimaryStat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

/**
 * PreciseDamageCalculator - Implements exact Hypixel Skyblock damage formulas
 * 
 * This calculator implements the precise numerical formulas used in Hypixel Skyblock
 * to maintain exact game balance and meta-game integrity. Any deviation from these
 * formulas would break the carefully balanced progression system.
 * 
 * Core Melee Damage Formula:
 * Final Damage = Weapon Damage × (1 + Strength/100) × (1 + Critical Damage/100)
 * 
 * Additional Multipliers:
 * - Ferocity: Chance for multiple attacks
 * - Enchantments: Various damage bonuses
 * - Armor Reduction: Target defense calculation
 */
public class PreciseDamageCalculator {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final StatCalculationService statCalculationService;
    
    public PreciseDamageCalculator(SkyblockPlugin SkyblockPlugin, StatCalculationService statCalculationService) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.statCalculationService = statCalculationService;
    }
    
    /**
     * Calculate precise melee damage using exact Hypixel Skyblock formula
     * 
     * Formula: Final Damage = Weapon Damage × (1 + Strength/100) × (1 + Critical Damage/100)
     * 
     * @param attacker The attacking player
     * @param target The target entity
     * @return CompletableFuture containing the damage calculation result
     */
    public CompletableFuture<DamageResult> calculateMeleeDamage(Player attacker, LivingEntity target) {
        return statCalculationService.calculatePlayerStatsAsync(attacker).thenApply(stats -> {
            DamageResult result = new DamageResult();
            
            // Get weapon damage
            double weaponDamage = getWeaponDamage(attacker);
            result.setWeaponDamage(weaponDamage);
            
            // Get player stats
            double strength = stats.getFinalStat(PrimaryStat.STRENGTH);
            double criticalDamage = stats.getFinalStat(PrimaryStat.CRIT_DAMAGE);
            double criticalChance = stats.getFinalStat(PrimaryStat.CRIT_CHANCE);
            double ferocity = stats.getFinalStat(PrimaryStat.FEROCITY);
            
            // Apply strength multiplier: (1 + Strength/100)
            double strengthMultiplier = 1.0 + (strength / 100.0);
            result.setStrengthMultiplier(strengthMultiplier);
            
            // Calculate critical hit
            boolean isCritical = calculateCriticalHit(criticalChance);
            result.setCritical(isCritical);
            
            // Apply critical damage multiplier: (1 + Critical Damage/100)
            double criticalMultiplier = 1.0 + (criticalDamage / 100.0);
            result.setCriticalMultiplier(criticalMultiplier);
            
            // Calculate base damage with strength
            double baseDamage = weaponDamage * strengthMultiplier;
            result.setBaseDamage(baseDamage);
            
            // Apply critical damage if critical hit
            double damageAfterCritical = baseDamage;
            if (isCritical) {
                damageAfterCritical *= criticalMultiplier;
            }
            result.setDamageAfterCritical(damageAfterCritical);
            
            // Apply ferocity (multiple attacks)
            double ferocityMultiplier = 1.0 + (ferocity / 100.0);
            result.setFerocityMultiplier(ferocityMultiplier);
            
            // Calculate final damage before armor reduction
            double finalDamageBeforeArmor = damageAfterCritical * ferocityMultiplier;
            result.setFinalDamageBeforeArmor(finalDamageBeforeArmor);
            
            // Apply armor reduction
            double armorReduction = calculateArmorReduction(target);
            result.setArmorReduction(armorReduction);
            
            // Calculate final damage after armor reduction
            double finalDamage = finalDamageBeforeArmor * (1.0 - armorReduction);
            result.setFinalDamage(Math.max(1.0, finalDamage));
            
            // Calculate ferocity attacks
            int ferocityAttacks = calculateFerocityAttacks(ferocity);
            result.setFerocityAttacks(ferocityAttacks);
            
            return result;
        });
    }
    
    /**
     * Calculate ranged damage (bow/crossbow)
     * 
     * Formula: Final Damage = Weapon Damage × (1 + Strength/100) × (1 + Critical Damage/100)
     * Note: Ranged weapons use the same base formula as melee
     */
    public CompletableFuture<DamageResult> calculateRangedDamage(Player attacker, LivingEntity target) {
        return statCalculationService.calculatePlayerStatsAsync(attacker).thenApply(stats -> {
            DamageResult result = new DamageResult();
            
            // Get weapon damage (bow/crossbow)
            double weaponDamage = getRangedWeaponDamage(attacker);
            result.setWeaponDamage(weaponDamage);
            
            // Get player stats
            double strength = stats.getFinalStat(PrimaryStat.STRENGTH);
            double criticalDamage = stats.getFinalStat(PrimaryStat.CRIT_DAMAGE);
            double criticalChance = stats.getFinalStat(PrimaryStat.CRIT_CHANCE);
            
            // Apply strength multiplier: (1 + Strength/100)
            double strengthMultiplier = 1.0 + (strength / 100.0);
            result.setStrengthMultiplier(strengthMultiplier);
            
            // Calculate critical hit
            boolean isCritical = calculateCriticalHit(criticalChance);
            result.setCritical(isCritical);
            
            // Apply critical damage multiplier: (1 + Critical Damage/100)
            double criticalMultiplier = 1.0 + (criticalDamage / 100.0);
            result.setCriticalMultiplier(criticalMultiplier);
            
            // Calculate base damage with strength
            double baseDamage = weaponDamage * strengthMultiplier;
            result.setBaseDamage(baseDamage);
            
            // Apply critical damage if critical hit
            double damageAfterCritical = baseDamage;
            if (isCritical) {
                damageAfterCritical *= criticalMultiplier;
            }
            result.setDamageAfterCritical(damageAfterCritical);
            
            // Ranged weapons don't use ferocity
            result.setFerocityMultiplier(1.0);
            result.setFerocityAttacks(0);
            
            // Calculate final damage before armor reduction
            double finalDamageBeforeArmor = damageAfterCritical;
            result.setFinalDamageBeforeArmor(finalDamageBeforeArmor);
            
            // Apply armor reduction
            double armorReduction = calculateArmorReduction(target);
            result.setArmorReduction(armorReduction);
            
            // Calculate final damage after armor reduction
            double finalDamage = finalDamageBeforeArmor * (1.0 - armorReduction);
            result.setFinalDamage(Math.max(1.0, finalDamage));
            
            return result;
        });
    }
    
    /**
     * Calculate magic damage (abilities, spells)
     * 
     * Formula: Final Damage = Base Ability Damage × (1 + Intelligence/100) × (1 + Ability Damage/100)
     */
    public CompletableFuture<DamageResult> calculateMagicDamage(Player attacker, LivingEntity target, double baseAbilityDamage) {
        return statCalculationService.calculatePlayerStatsAsync(attacker).thenApply(stats -> {
            DamageResult result = new DamageResult();
            
            result.setWeaponDamage(baseAbilityDamage);
            
            // Get player stats
            double intelligence = stats.getFinalStat(PrimaryStat.INTELLIGENCE);
            double abilityDamage = 0.0; // This would need proper secondary stat access
            
            // Apply intelligence multiplier: (1 + Intelligence/100)
            double intelligenceMultiplier = 1.0 + (intelligence / 100.0);
            result.setStrengthMultiplier(intelligenceMultiplier); // Reusing field for intelligence
            
            // Magic damage doesn't use critical hits in the same way
            result.setCritical(false);
            result.setCriticalMultiplier(1.0);
            
            // Calculate base damage with intelligence
            double baseDamage = baseAbilityDamage * intelligenceMultiplier;
            result.setBaseDamage(baseDamage);
            
            // Apply ability damage bonus
            double abilityDamageMultiplier = 1.0 + (abilityDamage / 100.0);
            double damageAfterAbility = baseDamage * abilityDamageMultiplier;
            result.setDamageAfterCritical(damageAfterAbility);
            
            // Magic damage doesn't use ferocity
            result.setFerocityMultiplier(1.0);
            result.setFerocityAttacks(0);
            
            // Calculate final damage before armor reduction
            double finalDamageBeforeArmor = damageAfterAbility;
            result.setFinalDamageBeforeArmor(finalDamageBeforeArmor);
            
            // Apply armor reduction (magic damage may have different armor interaction)
            double armorReduction = calculateMagicArmorReduction(target);
            result.setArmorReduction(armorReduction);
            
            // Calculate final damage after armor reduction
            double finalDamage = finalDamageBeforeArmor * (1.0 - armorReduction);
            result.setFinalDamage(Math.max(1.0, finalDamage));
            
            return result;
        });
    }
    
    /**
     * Get weapon damage from player's main hand
     */
    private double getWeaponDamage(Player player) {
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta()) {
            return 1.0; // Default damage
        }
        
        // Parse weapon damage from item lore or NBT
        // This would need to be implemented with the item system
        return parseWeaponDamage(weapon);
    }
    
    /**
     * Get ranged weapon damage
     */
    private double getRangedWeaponDamage(Player player) {
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta()) {
            return 1.0; // Default damage
        }
        
        // Parse ranged weapon damage from item lore or NBT
        return parseRangedWeaponDamage(weapon);
    }
    
    /**
     * Parse weapon damage from item
     */
    private double parseWeaponDamage(ItemStack weapon) {
        // This would parse the actual weapon damage from the item's lore or NBT
        // For now, return a placeholder value
        return 100.0; // Placeholder
    }
    
    /**
     * Parse ranged weapon damage from item
     */
    private double parseRangedWeaponDamage(ItemStack weapon) {
        // This would parse the actual ranged weapon damage from the item's lore or NBT
        // For now, return a placeholder value
        return 80.0; // Placeholder
    }
    
    /**
     * Calculate critical hit chance
     */
    private boolean calculateCriticalHit(double criticalChance) {
        return Math.random() * 100.0 < criticalChance;
    }
    
    /**
     * Calculate ferocity attacks
     */
    private int calculateFerocityAttacks(double ferocity) {
        int attacks = 0;
        double ferocityChance = ferocity / 100.0;
        
        // Each point of ferocity gives a chance for an additional attack
        while (Math.random() < ferocityChance) {
            attacks++;
            ferocityChance *= 0.5; // Diminishing returns
        }
        
        return attacks;
    }
    
    /**
     * Calculate armor reduction for physical damage
     */
    private double calculateArmorReduction(LivingEntity target) {
        // This would calculate armor reduction based on target's armor
        // For now, return a placeholder value
        return 0.1; // 10% reduction
    }
    
    /**
     * Calculate armor reduction for magic damage
     */
    private double calculateMagicArmorReduction(LivingEntity target) {
        // Magic damage may have different armor interaction
        // For now, return a placeholder value
        return 0.05; // 5% reduction
    }
    
    /**
     * Damage Result - contains all damage calculation details
     */
    public static class DamageResult {
        private double weaponDamage;
        private double strengthMultiplier;
        private boolean critical;
        private double criticalMultiplier;
        private double baseDamage;
        private double damageAfterCritical;
        private double ferocityMultiplier;
        private double finalDamageBeforeArmor;
        private double armorReduction;
        private double finalDamage;
        private int ferocityAttacks;
        
        // Getters and setters
        public double getWeaponDamage() { return weaponDamage; }
        public void setWeaponDamage(double weaponDamage) { this.weaponDamage = weaponDamage; }
        
        public double getStrengthMultiplier() { return strengthMultiplier; }
        public void setStrengthMultiplier(double strengthMultiplier) { this.strengthMultiplier = strengthMultiplier; }
        
        public boolean isCritical() { return critical; }
        public void setCritical(boolean critical) { this.critical = critical; }
        
        public double getCriticalMultiplier() { return criticalMultiplier; }
        public void setCriticalMultiplier(double criticalMultiplier) { this.criticalMultiplier = criticalMultiplier; }
        
        public double getBaseDamage() { return baseDamage; }
        public void setBaseDamage(double baseDamage) { this.baseDamage = baseDamage; }
        
        public double getDamageAfterCritical() { return damageAfterCritical; }
        public void setDamageAfterCritical(double damageAfterCritical) { this.damageAfterCritical = damageAfterCritical; }
        
        public double getFerocityMultiplier() { return ferocityMultiplier; }
        public void setFerocityMultiplier(double ferocityMultiplier) { this.ferocityMultiplier = ferocityMultiplier; }
        
        public double getFinalDamageBeforeArmor() { return finalDamageBeforeArmor; }
        public void setFinalDamageBeforeArmor(double finalDamageBeforeArmor) { this.finalDamageBeforeArmor = finalDamageBeforeArmor; }
        
        public double getArmorReduction() { return armorReduction; }
        public void setArmorReduction(double armorReduction) { this.armorReduction = armorReduction; }
        
        public double getFinalDamage() { return finalDamage; }
        public void setFinalDamage(double finalDamage) { this.finalDamage = finalDamage; }
        
        public int getFerocityAttacks() { return ferocityAttacks; }
        public void setFerocityAttacks(int ferocityAttacks) { this.ferocityAttacks = ferocityAttacks; }
        
        @Override
        public String toString() {
            return String.format("DamageResult{weapon=%.1f, strength=%.2f, critical=%s, critMult=%.2f, " +
                               "base=%.1f, afterCrit=%.1f, ferocity=%.2f, beforeArmor=%.1f, " +
                               "armorRed=%.2f, final=%.1f, ferocityAttacks=%d}",
                               weaponDamage, strengthMultiplier, critical, criticalMultiplier,
                               baseDamage, damageAfterCritical, ferocityMultiplier, finalDamageBeforeArmor,
                               armorReduction, finalDamage, ferocityAttacks);
        }
    }
}
