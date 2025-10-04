package de.noctivag.plugin.features.skills.types;
import org.bukkit.inventory.ItemStack;

/**
 * Categories of skills
 */
public enum SkillCategory {
    COMBAT("Combat", "⚔️", "Skills related to combat and fighting"),
    GATHERING("Gathering", "⛏️", "Skills related to gathering resources"),
    FOOD("Food", "🌾", "Skills related to food production"),
    MAGIC("Magic", "✨", "Skills related to magic and enchanting");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    SkillCategory(String displayName, String icon, String description) {
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
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
