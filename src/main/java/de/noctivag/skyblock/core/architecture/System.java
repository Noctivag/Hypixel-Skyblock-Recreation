package de.noctivag.skyblock.core.architecture;
import org.bukkit.inventory.ItemStack;

/**
 * System Interface - Base interface for all game systems
 * 
 * Features:
 * - Lifecycle management
 * - State tracking
 * - Dependency injection support
 */
public interface System {
    
    /**
     * Initialize the system
     */
    void initialize();
    
    /**
     * Enable the system
     */
    void enable();
    
    /**
     * Disable the system
     */
    void disable();
    
    /**
     * Shutdown the system
     */
    void shutdown();
    
    /**
     * Check if system is initialized
     */
    boolean isInitialized();
    
    /**
     * Check if system is enabled
     */
    boolean isEnabled();
    
    /**
     * Get system name
     */
    String getName();
    
    /**
     * Get system status
     */
    SystemStatus getStatus();
    
    /**
     * Get current time in milliseconds
     */
    default long currentTimeMillis() {
        return java.lang.System.currentTimeMillis();
    }
    
    /**
     * Get current time in nanoseconds
     */
    default long nanoTime() {
        return java.lang.System.nanoTime();
    }
    
    /**
     * Get system property
     */
    default String getProperty(String key, String defaultValue) {
        return java.lang.System.getProperty(key, defaultValue);
    }
    
    /**
     * System status enum
     */
    enum SystemStatus {
        UNINITIALIZED,
        INITIALIZING,
        INITIALIZED,
        ENABLING,
        ENABLED,
        DISABLING,
        DISABLED,
        SHUTTING_DOWN,
        SHUTDOWN,
        ERROR
    }
}
