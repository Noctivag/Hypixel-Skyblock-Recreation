package de.noctivag.skyblock.bazaar;

/**
 * Bazaar Order Type - Represents the type of bazaar order
 */
public enum BazaarOrderType {
    
    BUY("Buy", "§a"),
    SELL("Sell", "§c");
    
    private final String displayName;
    private final String colorCode;
    
    BazaarOrderType(String displayName, String colorCode) {
        this.displayName = displayName;
        this.colorCode = colorCode;
    }
    
    /**
     * Get the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get the color code
     */
    public String getColorCode() {
        return colorCode;
    }
}
