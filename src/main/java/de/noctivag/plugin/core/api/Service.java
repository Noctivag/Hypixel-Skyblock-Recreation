package de.noctivag.plugin.core.api;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

/**
 * Core service interface that all services must implement.
 * Provides lifecycle management and status tracking.
 */
public interface Service {
    
    /**
     * Initialize the service
     * @return CompletableFuture that completes when initialization is done
     */
    CompletableFuture<Void> initialize();
    
    /**
     * Shutdown the service and clean up resources
     * @return CompletableFuture that completes when shutdown is done
     */
    CompletableFuture<Void> shutdown();
    
    /**
     * Check if the service is initialized
     * @return true if initialized, false otherwise
     */
    boolean isInitialized();
    
    /**
     * Get the service name for logging and debugging
     * @return service name
     */
    String getName();
    
    /**
     * Get the service priority for initialization order
     * Lower numbers initialize first
     * @return priority value
     */
    default int getPriority() {
        return 100;
    }
    
    /**
     * Check if the service is required for plugin startup
     * @return true if required, false if optional
     */
    default boolean isRequired() {
        return true;
    }
}
