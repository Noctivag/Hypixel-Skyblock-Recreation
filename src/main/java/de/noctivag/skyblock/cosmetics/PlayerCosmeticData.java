package de.noctivag.skyblock.cosmetics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player cosmetic data storage
 */
public class PlayerCosmeticData {
    
    private final UUID playerUuid;
    private final Set<String> unlockedCosmetics = ConcurrentHashMap.newKeySet();
    private final Map<CosmeticType, String> activeCosmetics = new ConcurrentHashMap<>();
    private final Map<String, Long> cosmeticExpirations = new ConcurrentHashMap<>();
    
    public PlayerCosmeticData(UUID playerUuid) {
        this.playerUuid = playerUuid;
    }
    
    /**
     * Get player UUID
     */
    public UUID getPlayerUuid() {
        return playerUuid;
    }
    
    /**
     * Check if cosmetic is unlocked
     */
    public boolean isCosmeticUnlocked(String cosmeticId) {
        return unlockedCosmetics.contains(cosmeticId);
    }
    
    /**
     * Unlock cosmetic
     */
    public void unlockCosmetic(String cosmeticId) {
        unlockedCosmetics.add(cosmeticId);
    }
    
    /**
     * Get all unlocked cosmetics
     */
    public Set<String> getUnlockedCosmetics() {
        return new HashSet<>(unlockedCosmetics);
    }
    
    /**
     * Get active cosmetic for type
     */
    public String getActiveCosmetic(CosmeticType type) {
        return activeCosmetics.get(type);
    }
    
    /**
     * Activate cosmetic
     */
    public void activateCosmetic(String cosmeticId, CosmeticType type) {
        activeCosmetics.put(type, cosmeticId);
    }
    
    /**
     * Deactivate cosmetic for type
     */
    public void deactivateCosmetic(CosmeticType type) {
        activeCosmetics.remove(type);
    }
    
    /**
     * Get all active cosmetics
     */
    public Set<String> getAllActiveCosmetics() {
        return new HashSet<>(activeCosmetics.values());
    }
    
    /**
     * Set cosmetic expiration
     */
    public void setCosmeticExpiration(String cosmeticId, long expirationTime) {
        if (expirationTime > 0) {
            cosmeticExpirations.put(cosmeticId, expirationTime);
        } else {
            cosmeticExpirations.remove(cosmeticId);
        }
    }
    
    /**
     * Get cosmetic expiration
     */
    public long getCosmeticExpiration(String cosmeticId) {
        return cosmeticExpirations.getOrDefault(cosmeticId, -1L);
    }
    
    /**
     * Check if cosmetic is expired
     */
    public boolean isCosmeticExpired(String cosmeticId) {
        long expiration = getCosmeticExpiration(cosmeticId);
        return expiration > 0 && System.currentTimeMillis() > expiration;
    }
    
    /**
     * Remove expired cosmetics
     */
    public void removeExpiredCosmetics() {
        cosmeticExpirations.entrySet().removeIf(entry -> 
            entry.getValue() > 0 && System.currentTimeMillis() > entry.getValue());
    }
    
    /**
     * Get cosmetics by type
     */
    public List<String> getCosmeticsByType(CosmeticType type) {
        List<String> result = new ArrayList<>();
        for (String cosmeticId : unlockedCosmetics) {
            // This would need access to the cosmetic system to check type
            // For now, return all unlocked cosmetics
            result.add(cosmeticId);
        }
        return result;
    }
    
    /**
     * Get active cosmetics count
     */
    public int getActiveCosmeticsCount() {
        return activeCosmetics.size();
    }
    
    /**
     * Get unlocked cosmetics count
     */
    public int getUnlockedCosmeticsCount() {
        return unlockedCosmetics.size();
    }
    
    /**
     * Check if player has any active cosmetics
     */
    public boolean hasActiveCosmetics() {
        return !activeCosmetics.isEmpty();
    }
    
    /**
     * Check if player has any unlocked cosmetics
     */
    public boolean hasUnlockedCosmetics() {
        return !unlockedCosmetics.isEmpty();
    }
    
    /**
     * Clear all active cosmetics
     */
    public void clearActiveCosmetics() {
        activeCosmetics.clear();
    }
    
    /**
     * Clear all unlocked cosmetics
     */
    public void clearUnlockedCosmetics() {
        unlockedCosmetics.clear();
    }
    
    /**
     * Clear all cosmetic data
     */
    public void clearAllData() {
        unlockedCosmetics.clear();
        activeCosmetics.clear();
        cosmeticExpirations.clear();
    }
}