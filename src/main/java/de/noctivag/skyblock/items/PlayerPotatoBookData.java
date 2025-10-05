package de.noctivag.skyblock.items;
import java.util.UUID;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player potato book data
 */
public class PlayerPotatoBookData {
    
    private final UUID playerId;
    private final ConcurrentHashMap<String, Integer> potatoBooksUsed = new ConcurrentHashMap<>();
    private int totalPotatoBooksUsed = 0;
    
    public PlayerPotatoBookData(UUID playerId) {
        this.playerId = playerId;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public void addPotatoBookUsed(String potatoBookId) {
        potatoBooksUsed.merge(potatoBookId, 1, Integer::sum);
        totalPotatoBooksUsed++;
    }
    
    public int getPotatoBookUsed(String potatoBookId) {
        return potatoBooksUsed.getOrDefault(potatoBookId, 0);
    }
    
    public int getTotalPotatoBooksUsed() {
        return totalPotatoBooksUsed;
    }
    
    public ConcurrentHashMap<String, Integer> getPotatoBooksUsed() {
        return new ConcurrentHashMap<>(potatoBooksUsed);
    }
}
