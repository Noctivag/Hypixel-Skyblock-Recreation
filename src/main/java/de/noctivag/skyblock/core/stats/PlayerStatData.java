package de.noctivag.skyblock.core.stats;

import java.util.UUID;

/**
 * Vereinheitlichte Stat-Daten für Health und Mana (und erweiterbar für weitere Stats)
 */
public class PlayerStatData extends BaseStatData {
    public PlayerStatData(UUID playerId) {
        super(playerId);
        // Standardwerte für Health, Mana und weitere Skyblock-Stats
        stats.put("health", 100.0);
        stats.put("max_health", 100.0);
        stats.put("mana", 100.0);
        stats.put("max_mana", 100.0);
        stats.put("mana_regen", 1.0);
        stats.put("defense", 0.0);
        stats.put("strength", 0.0);
        stats.put("speed", 100.0);
        stats.put("crit_chance", 30.0);
        stats.put("crit_damage", 50.0);
        stats.put("intelligence", 0.0);
        stats.put("ferocity", 0.0);
        stats.put("true_defense", 0.0);
        stats.put("magic_find", 0.0);
        stats.put("pristine", 0.0);
        stats.put("fortune", 0.0);
    }

    // Getter/Setter für Defense
    public double getDefense() { return stats.getOrDefault("defense", 0.0); }
    public void setDefense(double value) { stats.put("defense", value); }

    // Getter/Setter für Strength
    public double getStrength() { return stats.getOrDefault("strength", 0.0); }
    public void setStrength(double value) { stats.put("strength", value); }

    // Getter/Setter für Speed
    public double getSpeed() { return stats.getOrDefault("speed", 100.0); }
    public void setSpeed(double value) { stats.put("speed", value); }

    // Getter/Setter für Crit Chance
    public double getCritChance() { return stats.getOrDefault("crit_chance", 30.0); }
    public void setCritChance(double value) { stats.put("crit_chance", value); }

    // Getter/Setter für Crit Damage
    public double getCritDamage() { return stats.getOrDefault("crit_damage", 50.0); }
    public void setCritDamage(double value) { stats.put("crit_damage", value); }

    // Getter/Setter für Intelligence
    public double getIntelligence() { return stats.getOrDefault("intelligence", 0.0); }
    public void setIntelligence(double value) { stats.put("intelligence", value); }

    // Getter/Setter für Ferocity
    public double getFerocity() { return stats.getOrDefault("ferocity", 0.0); }
    public void setFerocity(double value) { stats.put("ferocity", value); }

    // Getter/Setter für True Defense
    public double getTrueDefense() { return stats.getOrDefault("true_defense", 0.0); }
    public void setTrueDefense(double value) { stats.put("true_defense", value); }

    // Getter/Setter für Magic Find
    public double getMagicFind() { return stats.getOrDefault("magic_find", 0.0); }
    public void setMagicFind(double value) { stats.put("magic_find", value); }

    // Getter/Setter für Pristine
    public double getPristine() { return stats.getOrDefault("pristine", 0.0); }
    public void setPristine(double value) { stats.put("pristine", value); }

    // Getter/Setter für Fortune
    public double getFortune() { return stats.getOrDefault("fortune", 0.0); }
    public void setFortune(double value) { stats.put("fortune", value); }

    // Getter/Setter für Health
    public double getHealth() { return stats.getOrDefault("health", 0.0); }
    public void setHealth(double value) { stats.put("health", value); }
    public double getMaxHealth() { return stats.getOrDefault("max_health", 100.0); }
    public void setMaxHealth(double value) { stats.put("max_health", value); }

    // Getter/Setter für Mana
    public double getMana() { return stats.getOrDefault("mana", 0.0); }
    public void setMana(double value) { stats.put("mana", value); }
    public double getMaxMana() { return stats.getOrDefault("max_mana", 100.0); }
    public void setMaxMana(double value) { stats.put("max_mana", value); }
    public double getManaRegen() { return stats.getOrDefault("mana_regen", 1.0); }
    public void setManaRegen(double value) { stats.put("mana_regen", value); }
}
