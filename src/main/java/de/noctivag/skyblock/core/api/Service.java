package de.noctivag.skyblock.core.api;

/**
 * Base interface for all services in the Skyblock plugin
 * Provides common functionality for service management
 */
public interface Service {
    
    /**
     * Initialize the service
     * Called when the plugin starts
     */
    void initialize();
    
    /**
     * Shutdown the service
     * Called when the plugin stops
     */
    void shutdown();
    
    /**
     * Get the service name
     */
    String getName();
    
    /**
     * Get the service status
     */
    SystemStatus getStatus();
    
    /**
     * Check if the service is enabled
     */
    boolean isEnabled();
    
    /**
     * Enable or disable the service
     */
    void setEnabled(boolean enabled);
}
