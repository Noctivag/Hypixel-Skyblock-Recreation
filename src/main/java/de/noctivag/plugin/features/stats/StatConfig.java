package de.noctivag.plugin.features.stats;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.stats.types.PrimaryStat;
import de.noctivag.plugin.features.stats.types.SecondaryStat;
import de.noctivag.plugin.features.stats.types.StatCategory;
import de.noctivag.plugin.core.api.Service;
import de.noctivag.plugin.core.api.SystemStatus;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Configuration for a stat
 */
public class StatConfig implements Service {
    
    private final String statId;
    private final String displayName;
    private final String icon;
    private final String description;
    private final StatCategory category;
    private final double baseValue;
    private final double maxValue;
    private final String calculationFormula;
    private final String statDescription;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public StatConfig(PrimaryStat stat, String displayName, String icon, String description,
                     StatCategory category, double baseValue, double maxValue, 
                     String calculationFormula, String statDescription) {
        this.statId = stat.name();
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.category = category;
        this.baseValue = baseValue;
        this.maxValue = maxValue;
        this.calculationFormula = calculationFormula;
        this.statDescription = statDescription;
    }
    
    public StatConfig(SecondaryStat stat, String displayName, String icon, String description,
                     StatCategory category, double baseValue, double maxValue, 
                     String calculationFormula, String statDescription) {
        this.statId = stat.name();
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
        this.category = category;
        this.baseValue = baseValue;
        this.maxValue = maxValue;
        this.calculationFormula = calculationFormula;
        this.statDescription = statDescription;
    }
    
    /**
     * Get formatted stat display name
     */
    public String getFormattedName() {
        return category.getColorCode() + icon + " " + displayName;
    }
    
    /**
     * Get stat lore for display
     */
    public List<String> getLore() {
        return List.of(
            "§7" + description,
            "",
            "§7Category: " + category.getColorCode() + category.getDisplayName(),
            "§7Base Value: §e" + String.format("%.1f", baseValue),
            "§7Max Value: §c" + String.format("%.1f", maxValue),
            "",
            "§7Formula: §f" + calculationFormula,
            "",
            "§7" + statDescription
        );
    }
    
    /**
     * Get stat value with suffix
     */
    public String getFormattedValue(double value) {
        String suffix = getValueSuffix();
        return String.format("%.1f", value) + suffix;
    }
    
    /**
     * Get value suffix
     */
    private String getValueSuffix() {
        return switch (statId) {
            case "HEALTH" -> " HP";
            case "INTELLIGENCE" -> " MP";
            case "SPEED", "CRIT_CHANCE", "CRIT_DAMAGE", "ATTACK_SPEED", "FEROCITY" -> "%";
            case "MINING_WISDOM", "FARMING_WISDOM", "FISHING_WISDOM", "COMBAT_WISDOM", 
                 "TAMING_WISDOM", "FORAGING_WISDOM", "ENCHANTING_WISDOM", "ALCHEMY_WISDOM", 
                 "RUNECRAFTING_WISDOM", "DUNGEONEERING_WISDOM", "SOCIAL_WISDOM", "CARPENTRY_WISDOM" -> "%";
            case "SEA_CREATURE_CHANCE", "PET_LUCK" -> "%";
            default -> "";
        };
    }
    
    /**
     * Check if stat is at maximum value
     */
    public boolean isAtMaxValue(double value) {
        return value >= maxValue;
    }
    
    /**
     * Get stat percentage of max value
     */
    public double getPercentageOfMax(double value) {
        if (maxValue == 0) return 0;
        return (value / maxValue) * 100;
    }
    
    /**
     * Get formatted percentage
     */
    public String getFormattedPercentage(double value) {
        double percentage = getPercentageOfMax(value);
        String color = percentage >= 90 ? "§a" : percentage >= 70 ? "§e" : percentage >= 50 ? "§6" : "§c";
        return color + String.format("%.1f", percentage) + "%";
    }
    
    // Getters
    public String getStatId() {
        return statId;
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
    
    public StatCategory getCategory() {
        return category;
    }
    
    public double getBaseValue() {
        return baseValue;
    }
    
    public double getMaxValue() {
        return maxValue;
    }
    
    public String getCalculationFormula() {
        return calculationFormula;
    }
    
    public String getStatDescription() {
        return statDescription;
    }
    
    @Override
    public String toString() {
        return getFormattedName();
    }

    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            // Initialize stat config
            status = SystemStatus.ENABLED;
        });
    }

    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            // Shutdown stat config
            status = SystemStatus.UNINITIALIZED;
        });
    }

    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String getName() {
        return "StatConfig";
    }
}
