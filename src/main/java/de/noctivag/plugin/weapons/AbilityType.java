package de.noctivag.plugin.weapons;

/**
 * Ability activation types
 */
public enum AbilityType {
    RIGHT_CLICK("Right Click", "§a"),
    SHIFT_RIGHT_CLICK("Shift + Right Click", "§e"),
    LEFT_CLICK("Left Click", "§c"),
    SHIFT_LEFT_CLICK("Shift + Left Click", "§6");
    
    private final String displayName;
    private final String color;
    
    AbilityType(String displayName, String color) {
        this.displayName = displayName;
        this.color = color;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColor() {
        return color;
    }
}
