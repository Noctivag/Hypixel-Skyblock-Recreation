package de.noctivag.skyblock.features.stats.types;
import org.bukkit.inventory.ItemStack;

/**
 * All primary stats from Hypixel Skyblock
 */
public enum PrimaryStat {
    HEALTH("Health", "❤️", "Lebenspunkte"),
    DEFENSE("Defense", "🛡️", "Schadensreduktion"),
    STRENGTH("Strength", "💪", "Nahkampfschaden"),
    INTELLIGENCE("Intelligence", "🧠", "Mana-Kapazität"),
    SPEED("Speed", "🏃", "Bewegungsgeschwindigkeit"),
    CRIT_CHANCE("Crit Chance", "⚡", "Kritische Trefferchance"),
    CRIT_DAMAGE("Crit Damage", "💥", "Kritischer Schadensmultiplikator"),
    ATTACK_SPEED("Attack Speed", "⚔️", "Angriffsgeschwindigkeit"),
    FEROCITY("Ferocity", "🔥", "Mehrfachangriffe");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    PrimaryStat(String displayName, String icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get stat color code
     */
    public String getColorCode() {
        return switch (this) {
            case HEALTH -> "§c"; // Red
            case DEFENSE -> "§a"; // Green
            case STRENGTH -> "§c"; // Red
            case INTELLIGENCE -> "§b"; // Aqua
            case SPEED -> "§f"; // White
            case CRIT_CHANCE -> "§9"; // Blue
            case CRIT_DAMAGE -> "§6"; // Gold
            case ATTACK_SPEED -> "§e"; // Yellow
            case FEROCITY -> "§4"; // Dark Red
        };
    }
    
    /**
     * Get stat category
     */
    public StatCategory getCategory() {
        return switch (this) {
            case HEALTH, DEFENSE, STRENGTH, CRIT_CHANCE, CRIT_DAMAGE, ATTACK_SPEED, FEROCITY -> StatCategory.COMBAT;
            case INTELLIGENCE -> StatCategory.MAGIC;
            case SPEED -> StatCategory.UTILITY;
        };
    }
    
    /**
     * Get base value
     */
    public double getBaseValue() {
        return switch (this) {
            case HEALTH -> 100.0;
            case DEFENSE -> 0.0;
            case STRENGTH -> 0.0;
            case INTELLIGENCE -> 100.0;
            case SPEED -> 100.0;
            case CRIT_CHANCE -> 30.0;
            case CRIT_DAMAGE -> 50.0;
            case ATTACK_SPEED -> 0.0;
            case FEROCITY -> 0.0;
        };
    }
    
    /**
     * Get max value
     */
    public double getMaxValue() {
        return switch (this) {
            case HEALTH -> 10000.0;
            case DEFENSE -> 1000.0;
            case STRENGTH -> 2000.0;
            case INTELLIGENCE -> 5000.0;
            case SPEED -> 500.0;
            case CRIT_CHANCE -> 100.0;
            case CRIT_DAMAGE -> 1000.0;
            case ATTACK_SPEED -> 100.0;
            case FEROCITY -> 200.0;
        };
    }
    
    /**
     * Get stat calculation formula
     */
    public String getCalculationFormula() {
        return switch (this) {
            case HEALTH -> "Base + Equipment + Skills";
            case DEFENSE -> "Base + Armor + Accessories";
            case STRENGTH -> "Base + Weapons + Accessories";
            case INTELLIGENCE -> "Base + Equipment + Skills";
            case SPEED -> "Base + Equipment + Potions";
            case CRIT_CHANCE -> "Base + Equipment + Pets";
            case CRIT_DAMAGE -> "Base + Equipment + Pets";
            case ATTACK_SPEED -> "Base + Equipment";
            case FEROCITY -> "Base + Equipment + Pets";
        };
    }
    
    /**
     * Get stat description
     */
    public String getStatDescription() {
        return switch (this) {
            case HEALTH -> "Increases maximum health";
            case DEFENSE -> "Reduces incoming damage";
            case STRENGTH -> "Increases melee damage";
            case INTELLIGENCE -> "Increases maximum mana";
            case SPEED -> "Increases movement speed";
            case CRIT_CHANCE -> "Chance for critical hits";
            case CRIT_DAMAGE -> "Critical hit damage multiplier";
            case ATTACK_SPEED -> "Increases attack speed";
            case FEROCITY -> "Chance for multiple attacks";
        };
    }
    
    @Override
    public String toString() {
        return getColorCode() + icon + " " + displayName;
    }
}
