package de.noctivag.skyblock.core.api;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

/**
 * System interface for game systems that can be enabled/disabled.
 * Extends Service to provide lifecycle management.
 */
public interface System extends Service {
    
    /**
     * Enable the system
     * @return CompletableFuture that completes when enabled
     */
    CompletableFuture<Void> enable();
    
    /**
     * Disable the system
     * @return CompletableFuture that completes when disabled
     */
    CompletableFuture<Void> disable();
    
    /**
     * Check if the system is enabled
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();
    
    /**
     * Get the system status
     * @return current system status
     */
    SystemStatus getStatus();
    
    /**
     * Set the system status
     * @param status the new status
     */
    void setStatus(SystemStatus status);
    
    /**
     * Get the system version
     * @return version string
     */
    default String getVersion() {
        return "1.0.0";
    }
    
    /**
     * Get the system description
     * @return description string
     */
    default String getDescription() {
        return "No description available";
    }
    
    /**
     * Check if the system can be disabled
     * @return true if can be disabled, false otherwise
     */
    default boolean canDisable() {
        return true;
    }
    
    /**
     * Get dependencies that must be initialized before this system
     * @return array of dependency class names
     */
    default String[] getDependencies() {
        return new String[0];
    }
}
