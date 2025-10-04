package de.noctivag.plugin.features.economy.auction.types;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Auction Statistics class for auction analytics
 */
public class AuctionStatistics {
    private long totalAuctions;
    private long successfulAuctions;
    private long failedAuctions;
    private double totalVolume;
    private double averagePrice;
    private Map<AuctionCategory, Long> categoryStats;
    private long lastUpdated;

    public AuctionStatistics() {
        this.totalAuctions = 0;
        this.successfulAuctions = 0;
        this.failedAuctions = 0;
        this.totalVolume = 0.0;
        this.averagePrice = 0.0;
        this.categoryStats = new java.util.HashMap<>();
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getTotalAuctions() {
        return totalAuctions;
    }

    public void setTotalAuctions(long totalAuctions) {
        this.totalAuctions = totalAuctions;
    }

    public long getSuccessfulAuctions() {
        return successfulAuctions;
    }

    public void setSuccessfulAuctions(long successfulAuctions) {
        this.successfulAuctions = successfulAuctions;
    }

    public long getFailedAuctions() {
        return failedAuctions;
    }

    public void setFailedAuctions(long failedAuctions) {
        this.failedAuctions = failedAuctions;
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

    public Map<AuctionCategory, Long> getCategoryStats() {
        return categoryStats;
    }

    public void setCategoryStats(Map<AuctionCategory, Long> categoryStats) {
        this.categoryStats = categoryStats;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void addAuction(AuctionCategory category, double price, boolean successful) {
        this.totalAuctions++;
        if (successful) {
            this.successfulAuctions++;
            this.totalVolume += price;
        } else {
            this.failedAuctions++;
        }
        this.averagePrice = this.totalVolume / this.successfulAuctions;
        this.categoryStats.merge(category, 1L, Long::sum);
    }
}
