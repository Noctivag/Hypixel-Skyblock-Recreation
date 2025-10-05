package de.noctivag.skyblock.core.api;

/**
 * System Interface - Interface for all systems
 */
public interface SystemInterface {
    
    /**
     * Initialize the system
     */
    void initialize();
    
    /**
     * Shutdown the system
     */
    void shutdown();
    
    /**
     * Get the system status
     */
    SystemStatus getStatus();
    
    /**
     * Check if the system is running
     */
    default boolean isRunning() {
        return getStatus() == SystemStatus.RUNNING;
    }
    
    /**
     * Check if the system is disabled
     */
    default boolean isDisabled() {
        return getStatus() == SystemStatus.DISABLED;
    }
    
    /**
     * Check if the system is in error state
     */
    default boolean isError() {
        return getStatus() == SystemStatus.ERROR;
    }
}