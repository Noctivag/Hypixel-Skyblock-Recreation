package de.noctivag.plugin.features.economy.bazaar;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

import de.noctivag.plugin.features.economy.bazaar.types.BazaarCategory;

/**
 * Represents a bazaar item
 */
public class BazaarItem {
    
    private final String itemId;
    private final String displayName;
    private final String icon;
    private final BazaarCategory category;
    private final double buyPrice;
    private final double sellPrice;
    private final int totalSupply;
    private final int totalDemand;
    
    public BazaarItem(String displayName, String icon, BazaarCategory category, double buyPrice, double sellPrice) {
        this.itemId = displayName.toUpperCase().replace(" ", "_");
        this.displayName = displayName;
        this.icon = icon;
        this.category = category;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.totalSupply = 0;
        this.totalDemand = 0;
    }
    
    public BazaarItem(String itemId, String displayName, String icon, BazaarCategory category, 
                     double buyPrice, double sellPrice, int totalSupply, int totalDemand) {
        this.itemId = itemId;
        this.displayName = displayName;
        this.icon = icon;
        this.category = category;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.totalSupply = totalSupply;
        this.totalDemand = totalDemand;
    }
    
    /**
     * Get formatted display name
     */
    public String getFormattedName() {
        return icon + " " + displayName;
    }
    
    /**
     * Get formatted buy price
     */
    public String getFormattedBuyPrice() {
        return "§c" + String.format("%.2f", buyPrice) + " coins";
    }
    
    /**
     * Get formatted sell price
     */
    public String getFormattedSellPrice() {
        return "§a" + String.format("%.2f", sellPrice) + " coins";
    }
    
    /**
     * Get price difference percentage
     */
    public double getPriceDifferencePercentage() {
        if (sellPrice == 0) return 0;
        return ((buyPrice - sellPrice) / sellPrice) * 100;
    }
    
    /**
     * Get formatted price difference
     */
    public String getFormattedPriceDifference() {
        double difference = getPriceDifferencePercentage();
        String color = difference > 0 ? "§c" : "§a";
        return color + String.format("%.1f%%", difference);
    }
    
    /**
     * Get item lore for display
     */
    public List<String> getLore() {
        return List.of(
            "§7Category: " + category.getColorCode() + category.getDisplayName(),
            "",
            "§7Buy Price: " + getFormattedBuyPrice(),
            "§7Sell Price: " + getFormattedSellPrice(),
            "§7Price Difference: " + getFormattedPriceDifference(),
            "",
            "§7Total Supply: §e" + totalSupply,
            "§7Total Demand: §e" + totalDemand,
            "",
            "§eClick to buy/sell this item"
        );
    }
    
    /**
     * Check if item is profitable to buy and sell
     */
    public boolean isProfitable() {
        return sellPrice > buyPrice;
    }
    
    /**
     * Get profit per item
     */
    public double getProfitPerItem() {
        return sellPrice - buyPrice;
    }
    
    /**
     * Get formatted profit
     */
    public String getFormattedProfit() {
        double profit = getProfitPerItem();
        String color = profit > 0 ? "§a" : "§c";
        return color + String.format("%.2f", profit) + " coins";
    }
    
    // Getters
    public String getItemId() {
        return itemId;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public BazaarCategory getCategory() {
        return category;
    }
    
    public double getBuyPrice() {
        return buyPrice;
    }
    
    public double getSellPrice() {
        return sellPrice;
    }
    
    public int getTotalSupply() {
        return totalSupply;
    }
    
    public int getTotalDemand() {
        return totalDemand;
    }
    
    @Override
    public String toString() {
        return getFormattedName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BazaarItem that = (BazaarItem) obj;
        return Objects.equals(itemId, that.itemId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
