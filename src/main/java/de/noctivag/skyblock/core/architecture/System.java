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
