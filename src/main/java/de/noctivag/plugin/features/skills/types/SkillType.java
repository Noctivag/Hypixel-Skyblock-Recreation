package de.noctivag.plugin.features.skills.types;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.core.api.Service;
import de.noctivag.plugin.core.api.SystemStatus;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.Arrays;

/**
 * All skill types in Hypixel Skyblock
 */
public enum SkillType implements Service {
    COMBAT("Combat", "⚔️", "Increases damage dealt to mobs and combat effectiveness"),
    MINING("Mining", "⛏️", "Increases mining speed, fortune, and mining-related bonuses"),
    FORAGING("Foraging", "🪓", "Increases tree chopping speed and foraging efficiency"),
    FISHING("Fishing", "🎣", "Increases fishing speed and rare catch chances"),
    FARMING("Farming", "🌾", "Increases crop growth speed and farming yields"),
    ENCHANTING("Enchanting", "✨", "Increases enchantment power and enchanting success"),
    ALCHEMY("Alchemy", "🧪", "Increases potion duration and alchemy effectiveness"),
    TAMING("Taming", "🐾", "Increases pet stats and taming bonuses");
    
    private final String displayName;
    private final String icon;
    private final String description;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    SkillType(String displayName, String icon, String description) {
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
     * Get skill weight for skill power calculation
     */
    public double getWeight() {
        return switch (this) {
            case COMBAT -> 1.5; // Combat is most important
            case MINING -> 1.2;
            case FORAGING -> 1.0;
            case FISHING -> 1.1;
            case FARMING -> 0.9;
            case ENCHANTING -> 1.3;
            case ALCHEMY -> 0.8;
            case TAMING -> 1.0;
        };
    }
    
    /**
     * Get maximum level for this skill
     */
    public int getMaxLevel() {
        return switch (this) {
            case ENCHANTING -> 60; // Enchanting has higher max level
            default -> 50;
        };
    }
    
    /**
     * Get base XP requirement for level 1
     */
    public double getBaseXPRequirement() {
        return 100.0;
    }
    
    /**
     * Get XP multiplier for this skill
     */
    public double getXPMultiplier() {
        return switch (this) {
            case COMBAT -> 1.0; // Standard XP gain
            case MINING -> 1.0;
            case FORAGING -> 1.0;
            case FISHING -> 0.8; // Fishing gives less XP
            case FARMING -> 1.2; // Farming gives more XP
            case ENCHANTING -> 0.5; // Enchanting gives much less XP
            case ALCHEMY -> 1.5; // Alchemy gives more XP
            case TAMING -> 0.3; // Taming gives very little XP
        };
    }
    
    /**
     * Get skill category
     */
    public SkillCategory getCategory() {
        return switch (this) {
            case COMBAT, TAMING -> SkillCategory.COMBAT;
            case MINING, FORAGING -> SkillCategory.GATHERING;
            case FARMING, FISHING -> SkillCategory.FOOD;
            case ENCHANTING, ALCHEMY -> SkillCategory.MAGIC;
        };
    }
    
    /**
     * Get skills by category
     */
    public static List<SkillType> getByCategory(SkillCategory category) {
        return Arrays.stream(values())
            .filter(skill -> skill.getCategory() == category)
            .toList();
    }
    
    /**
     * Get all combat-related skills
     */
    public static List<SkillType> getCombatSkills() {
        return getByCategory(SkillCategory.COMBAT);
    }
    
    /**
     * Get all gathering skills
     */
    public static List<SkillType> getGatheringSkills() {
        return getByCategory(SkillCategory.GATHERING);
    }
    
    /**
     * Get all food-related skills
     */
    public static List<SkillType> getFoodSkills() {
        return getByCategory(SkillCategory.FOOD);
    }
    
    /**
     * Get all magic skills
     */
    public static List<SkillType> getMagicSkills() {
        return getByCategory(SkillCategory.MAGIC);
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }

    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            // Initialize skill type
            status = SystemStatus.ENABLED;
        });
    }

    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            // Shutdown skill type
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
        return "SkillType";
    }
}
