package de.noctivag.skyblock.core.api;

/**
 * Enum representing the status of a system or service
 */
public enum SystemStatus {
    
    /**
     * System is not initialized
     */
    UNINITIALIZED,
    
    /**
     * System is initializing
     */
    INITIALIZING,
    
    /**
     * System is running normally
     */
    RUNNING,
    
    /**
     * System is paused
     */
    PAUSED,
    
    /**
     * System is shutting down
     */
    SHUTTING_DOWN,
    
    /**
     * System has been shut down
     */
    SHUTDOWN,
    
    /**
     * System encountered an error
     */
    ERROR,
    
    /**
     * System is disabled
     */
    DISABLED
}
