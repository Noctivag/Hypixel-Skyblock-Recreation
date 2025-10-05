package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Manager for brewing modifiers
 */
public class BrewingModifierManager implements Service {
    
    private final SkyblockPlugin plugin;
    private SystemStatus status = SystemStatus.DISABLED;
    private final Map<UUID, Map<BrewingModifierType, BrewingModifier>> playerModifiers = new ConcurrentHashMap<>();
    
    public BrewingModifierManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing BrewingModifierManager...");
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("BrewingModifierManager initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down BrewingModifierManager...");
        
        playerModifiers.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("BrewingModifierManager shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "BrewingModifierManager";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Add modifier to player
     */
    public void addModifier(UUID playerUuid, BrewingModifierType type, BrewingModifier modifier) {
        playerModifiers.computeIfAbsent(playerUuid, k -> new ConcurrentHashMap<>()).put(type, modifier);
        plugin.getLogger().log(Level.INFO, "Added brewing modifier " + modifier.getName() + " to player " + playerUuid);
    }
    
    /**
     * Remove modifier from player
     */
    public void removeModifier(UUID playerUuid, BrewingModifierType type) {
        Map<BrewingModifierType, BrewingModifier> modifiers = playerModifiers.get(playerUuid);
        if (modifiers != null) {
            BrewingModifier removed = modifiers.remove(type);
            if (removed != null) {
                plugin.getLogger().log(Level.INFO, "Removed brewing modifier " + removed.getName() + " from player " + playerUuid);
            }
        }
    }
    
    /**
     * Get modifier for player
     */
    public BrewingModifier getModifier(UUID playerUuid, BrewingModifierType type) {
        Map<BrewingModifierType, BrewingModifier> modifiers = playerModifiers.get(playerUuid);
        return modifiers != null ? modifiers.get(type) : null;
    }
    
    /**
     * Get all modifiers for player
     */
    public Map<BrewingModifierType, BrewingModifier> getPlayerModifiers(UUID playerUuid) {
        return playerModifiers.getOrDefault(playerUuid, new ConcurrentHashMap<>());
    }
    
    /**
     * Check if player has modifier
     */
    public boolean hasModifier(UUID playerUuid, BrewingModifierType type) {
        Map<BrewingModifierType, BrewingModifier> modifiers = playerModifiers.get(playerUuid);
        return modifiers != null && modifiers.containsKey(type);
    }
    
    /**
     * Clear all modifiers for player
     */
    public void clearPlayerModifiers(UUID playerUuid) {
        playerModifiers.remove(playerUuid);
        plugin.getLogger().log(Level.INFO, "Cleared all brewing modifiers for player " + playerUuid);
    }
    
    /**
     * Clear all modifiers
     */
    public void clearAllModifiers() {
        playerModifiers.clear();
        plugin.getLogger().info("Cleared all brewing modifiers");
    }
}
