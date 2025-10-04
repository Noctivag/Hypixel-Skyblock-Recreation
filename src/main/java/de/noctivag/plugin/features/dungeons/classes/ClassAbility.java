package de.noctivag.plugin.features.dungeons.classes;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a class ability with cooldown and effects
 */
public class ClassAbility {
    
    private final String name;
    private final String description;
    private final int cooldownSeconds;
    
    private final Map<UUID, Long> lastUsed = new ConcurrentHashMap<>();
    
    public ClassAbility(String name, String description, int cooldownSeconds) {
        this.name = name;
        this.description = description;
        this.cooldownSeconds = cooldownSeconds;
    }
    
    /**
     * Check if ability can be used
     */
    public boolean canUse(UUID playerId) {
        long lastUsedTime = lastUsed.getOrDefault(playerId, 0L);
        long currentTime = System.currentTimeMillis();
        long cooldownMs = cooldownSeconds * 1000L;
        
        return (currentTime - lastUsedTime) >= cooldownMs;
    }
    
    /**
     * Use the ability
     */
    public boolean use(UUID playerId) {
        if (!canUse(playerId)) {
            return false;
        }
        
        lastUsed.put(playerId, System.currentTimeMillis());
        
        // Execute ability effect
        executeAbility(playerId);
        
        return true;
    }
    
    /**
     * Execute the ability effect (override in subclasses)
     */
    protected void executeAbility(UUID playerId) {
        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            // Default implementation - send message
            player.sendMessage("§6Used ability: §e" + name);
        }
    }
    
    /**
     * Get remaining cooldown in seconds
     */
    public int getRemainingCooldown(UUID playerId) {
        long lastUsedTime = lastUsed.getOrDefault(playerId, 0L);
        long currentTime = System.currentTimeMillis();
        long cooldownMs = cooldownSeconds * 1000L;
        
        long remaining = cooldownMs - (currentTime - lastUsedTime);
        return Math.max(0, (int) (remaining / 1000L));
    }
    
    /**
     * Get ability name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get ability description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get cooldown in seconds
     */
    public int getCooldownSeconds() {
        return cooldownSeconds;
    }
}
