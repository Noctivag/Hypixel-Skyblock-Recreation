package de.noctivag.skyblock.items;

import java.util.UUID;

/**
 * Player recombobulator data
 */
public class PlayerRecombobulatorData {
    
    private final UUID playerId;
    private int successfulRecombobulations = 0;
    private int failedRecombobulations = 0;
    private int totalRecombobulations = 0;
    
    public PlayerRecombobulatorData(UUID playerId) {
        this.playerId = playerId;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public int getSuccessfulRecombobulations() {
        return successfulRecombobulations;
    }
    
    public void addSuccessfulRecombobulation() {
        this.successfulRecombobulations++;
        this.totalRecombobulations++;
    }
    
    public int getFailedRecombobulations() {
        return failedRecombobulations;
    }
    
    public void addFailedRecombobulation() {
        this.failedRecombobulations++;
        this.totalRecombobulations++;
    }
    
    public int getTotalRecombobulations() {
        return totalRecombobulations;
    }
    
    public double getSuccessRate() {
        if (totalRecombobulations == 0) return 0.0;
        return (double) successfulRecombobulations / totalRecombobulations * 100.0;
    }
}
