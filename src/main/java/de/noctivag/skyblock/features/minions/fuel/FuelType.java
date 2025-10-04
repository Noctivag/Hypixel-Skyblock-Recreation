package de.noctivag.skyblock.features.minions.fuel;
import org.bukkit.inventory.ItemStack;

/**
 * Types of minion fuels
 */
public enum FuelType {
    BASIC("Basic", "⚫", "Basic fuel for early game"),
    ADVANCED("Advanced", "🔴", "Enhanced fuel for mid game"),
    SPECIAL("Special", "🟡", "Special fuel with unique properties"),
    SOLAR("Solar", "☀️", "Solar-powered fuel"),
    CATALYST("Catalyst", "🧪", "Catalyst that boosts output");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    FuelType(String displayName, String icon, String description) {
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
