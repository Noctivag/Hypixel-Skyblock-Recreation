package de.noctivag.skyblock.bazaar;

import java.util.UUID;

/**
 * Bazaar statistics for a player
 */
public class BazaarStats {
    private final UUID playerId;
    private int totalTransactions;
    private double totalVolume;
    private double totalProfit;

    public BazaarStats(UUID playerId) {
        this.playerId = playerId;
        this.totalTransactions = 0;
        this.totalVolume = 0.0;
        this.totalProfit = 0.0;
    }

    public UUID getPlayerId() { return playerId; }
    public int getTotalTransactions() { return totalTransactions; }
    public double getTotalVolume() { return totalVolume; }
    public double getTotalProfit() { return totalProfit; }

    public void incrementTransactions() { this.totalTransactions++; }
    public void addVolume(double volume) { this.totalVolume += volume; }
    public void addProfit(double profit) { this.totalProfit += profit; }
}
