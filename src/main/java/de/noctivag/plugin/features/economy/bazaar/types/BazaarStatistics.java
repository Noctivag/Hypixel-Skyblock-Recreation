package de.noctivag.plugin.features.economy.bazaar.types;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Bazaar Statistics class for bazaar analytics
 */
public class BazaarStatistics {
    private long totalOrders;
    private long buyOrders;
    private long sellOrders;
    private double totalVolume;
    private double averagePrice;
    private Map<BazaarCategory, Long> categoryStats;
    private long lastUpdated;

    public BazaarStatistics() {
        this.totalOrders = 0;
        this.buyOrders = 0;
        this.sellOrders = 0;
        this.totalVolume = 0.0;
        this.averagePrice = 0.0;
        this.categoryStats = new java.util.HashMap<>();
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getBuyOrders() {
        return buyOrders;
    }

    public void setBuyOrders(long buyOrders) {
        this.buyOrders = buyOrders;
    }

    public long getSellOrders() {
        return sellOrders;
    }

    public void setSellOrders(long sellOrders) {
        this.sellOrders = sellOrders;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Map<BazaarCategory, Long> getCategoryStats() {
        return categoryStats;
    }

    public void setCategoryStats(Map<BazaarCategory, Long> categoryStats) {
        this.categoryStats = categoryStats;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void addOrder(BazaarCategory category, BazaarOrder.BazaarOrderType type, double price, int amount) {
        this.totalOrders++;
        if (type == BazaarOrder.BazaarOrderType.BUY) {
            this.buyOrders++;
        } else {
            this.sellOrders++;
        }
        this.totalVolume += price * amount;
        this.averagePrice = this.totalVolume / this.totalOrders;
        this.categoryStats.merge(category, 1L, Long::sum);
    }
}
