
package de.noctivag.skyblock.accessories;

/**
 * Categories for accessories
 */
public enum AccessoryCategory {
    RING("Ring"),
    NECKLACE("Necklace"),
    BRACELET("Bracelet"),
    SPECIAL("Special"),
    COMBAT("Combat"),
    FARMING("Farming"),
    FISHING("Fishing"),
    UTILITY("Utility"),
    MISC("Misc"),
    FORAGING("Foraging"),
    MINING("Mining"),
    DUNGEON("Dungeon"),
    SLAYER("Slayer"),
    ACCESSORY("Accessory");

    private final String displayName;

    AccessoryCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
