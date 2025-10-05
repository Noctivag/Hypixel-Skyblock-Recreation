package de.noctivag.skyblock.core.api;

/**
 * System - Alternative to Service for systems that need different naming
 */
public interface System {
    
    /**
     * Initialize the system
     */
    void initialize();
    
    /**
     * Shutdown the system
     */
    void shutdown();
    
    /**
     * Get the system name
     */
    String getName();
    
    /**
     * Get the system status
     */
    SystemStatus getStatus();
    
    /**
     * Check if the system is enabled
     */
    boolean isEnabled();
    
    /**
     * Enable or disable the system
     */
    void setEnabled(boolean enabled);
}

