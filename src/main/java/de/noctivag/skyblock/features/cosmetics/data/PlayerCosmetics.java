package de.noctivag.skyblock.features.cosmetics.data;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Player Cosmetics Data
 */
public class PlayerCosmetics {
    private final UUID playerId;
    private final Set<String> unlockedCosmetics;
    private final Map<String, String> activeCosmetics;
    private final Map<String, Object> preferences;
    
    public PlayerCosmetics(UUID playerId) {
        this.playerId = playerId;
        this.unlockedCosmetics = Set.of();
        this.activeCosmetics = Map.of();
        this.preferences = Map.of();
    }
    
    public UUID getPlayerId() { return playerId; }
    public Set<String> getUnlockedCosmetics() { return unlockedCosmetics; }
    public Map<String, String> getActiveCosmetics() { return activeCosmetics; }
    public Map<String, Object> getPreferences() { return preferences; }
    
    public boolean hasCosmetic(String cosmeticId) {
        return unlockedCosmetics.contains(cosmeticId);
    }
    
    public void unlockCosmetic(String cosmeticId) {
        unlockedCosmetics.add(cosmeticId);
    }
    
    public void setActiveCosmetic(String slot, String cosmeticId) {
        activeCosmetics.put(slot, cosmeticId);
    }
}
