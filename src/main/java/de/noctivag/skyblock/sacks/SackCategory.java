package de.noctivag.skyblock.sacks;

/**
 * Sack Category enum for sack categories
 */
public enum SackCategory {
    FARMING("Farming"),
    MINING("Mining"),
    FORAGING("Foraging"),
    FISHING("Fishing"),
    COMBAT("Combat"),
    MISC("Miscellaneous"),
    SPECIAL("Special");

    private final String displayName;

    SackCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
