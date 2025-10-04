package de.noctivag.plugin.weapons;

/**
 * Weapon types
 */
public enum WeaponType {
    SWORD("Sword", "§c"),
    BOW("Bow", "§9"),
    STAFF("Staff", "§d"),
    SPECIAL("Special", "§6");
    
    private final String displayName;
    private final String color;
    
    WeaponType(String displayName, String color) {
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
