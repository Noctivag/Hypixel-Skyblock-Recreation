package de.noctivag.plugin.weapons;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Player ability data
 */
public class PlayerAbilityData {
    
    private final UUID playerId;
    private final ConcurrentHashMap<String, Long> cooldowns = new ConcurrentHashMap<>();
    
    public PlayerAbilityData(UUID playerId) {
        this.playerId = playerId;
    }
    
    public UUID getPlayerId() {
        return playerId;
    }
    
    public boolean isOnCooldown(String abilityId) {
        Long cooldownEnd = cooldowns.get(abilityId);
        if (cooldownEnd == null) return false;
        
        if (System.currentTimeMillis() >= cooldownEnd) {
            cooldowns.remove(abilityId);
            return false;
        }
        
        return true;
    }
    
    public long getCooldownRemaining(String abilityId) {
        Long cooldownEnd = cooldowns.get(abilityId);
        if (cooldownEnd == null) return 0;
        
        long remaining = (cooldownEnd - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }
    
    public void setCooldown(String abilityId, int seconds) {
        long cooldownEnd = System.currentTimeMillis() + (seconds * 1000L);
        cooldowns.put(abilityId, cooldownEnd);
    }
    
    public void clearCooldown(String abilityId) {
        cooldowns.remove(abilityId);
    }
    
    public void clearAllCooldowns() {
        cooldowns.clear();
    }
}
