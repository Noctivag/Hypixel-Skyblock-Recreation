package de.noctivag.plugin.features.economy.data;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

/**
 * Economy Statistics class for economy analytics
 */
public class EconomyStatistics {
    private long totalCoinsInCirculation;
    private long totalTransactions;
    private double averageTransactionValue;
    private Map<UUID, Double> topPlayers;
    private long lastUpdated;

    public EconomyStatistics() {
        this.totalCoinsInCirculation = 0;
        this.totalTransactions = 0;
        this.averageTransactionValue = 0.0;
        this.topPlayers = new java.util.HashMap<>();
        this.lastUpdated = System.currentTimeMillis();
    }

    public long getTotalCoinsInCirculation() {
        return totalCoinsInCirculation;
    }

    public void setTotalCoinsInCirculation(long totalCoinsInCirculation) {
        this.totalCoinsInCirculation = totalCoinsInCirculation;
    }

    public long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public double getAverageTransactionValue() {
        return averageTransactionValue;
    }

    public void setAverageTransactionValue(double averageTransactionValue) {
        this.averageTransactionValue = averageTransactionValue;
    }

    public Map<UUID, Double> getTopPlayers() {
        return topPlayers;
    }

    public void setTopPlayers(Map<UUID, Double> topPlayers) {
        this.topPlayers = topPlayers;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void addTransaction(double value) {
        this.totalTransactions++;
        this.totalCoinsInCirculation += (long) value;
        this.averageTransactionValue = (double) this.totalCoinsInCirculation / this.totalTransactions;
    }

    public void updateTopPlayer(UUID playerId, double balance) {
        this.topPlayers.put(playerId, balance);
    }
}
