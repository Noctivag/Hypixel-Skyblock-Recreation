package de.noctivag.skyblock.core.architecture;
import org.bukkit.inventory.ItemStack;

/**
 * Service Interface - Base interface for all services
 * 
 * Features:
 * - Lifecycle management
 * - Service discovery
 * - Dependency injection support
 */
public interface Service {
    
    /**
     * Initialize the service
     */
    void initialize();
    
    /**
     * Shutdown the service
     */
    void shutdown();
    
    /**
     * Check if service is initialized
     */
    boolean isInitialized();
    
    /**
     * Get service name
     */
    String getName();
    
    /**
     * Get service version
     */
    String getVersion();
    
    /**
     * Get service description
     */
    String getDescription();
}
