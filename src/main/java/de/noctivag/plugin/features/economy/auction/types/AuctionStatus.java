package de.noctivag.plugin.features.economy.auction.types;
import org.bukkit.inventory.ItemStack;

/**
 * Auction Status enum for auction states
 */
public enum AuctionStatus {
    ACTIVE("Active", "Auction is currently running"),
    ENDED("Ended", "Auction has ended"),
    CANCELLED("Cancelled", "Auction was cancelled"),
    EXPIRED("Expired", "Auction expired without bids"),
    SOLD("Sold", "Auction was successful");

    private final String name;
    private final String description;

    AuctionStatus(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public String getColorCode() {
        switch (this) {
            case ACTIVE: return "§a";
            case ENDED: return "§7";
            case CANCELLED: return "§c";
            case EXPIRED: return "§e";
            case SOLD: return "§6";
            default: return "§f";
        }
    }
    
    public String getDisplayName() {
        return getColorCode() + name;
    }
}
