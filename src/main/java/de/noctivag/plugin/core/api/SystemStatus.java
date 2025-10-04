package de.noctivag.plugin.core.api;
import org.bukkit.inventory.ItemStack;

/**
 * Enum representing the status of a system.
 */
public enum SystemStatus {
    
    /**
     * System is not initialized
     */
    UNINITIALIZED("Uninitialized", "System has not been initialized"),
    
    /**
     * System is initializing
     */
    INITIALIZING("Initializing", "System is currently initializing"),
    
    /**
     * System is initialized but not enabled
     */
    INITIALIZED("Initialized", "System is initialized but not enabled"),
    
    /**
     * System is enabled and running
     */
    ENABLED("Enabled", "System is enabled and running"),
    
    /**
     * System is disabled
     */
    DISABLED("Disabled", "System is disabled"),
    
    /**
     * System is disabling
     */
    DISABLING("Disabling", "System is currently disabling"),
    
    /**
     * System is shutting down
     */
    SHUTTING_DOWN("Shutting Down", "System is shutting down"),
    
    /**
     * System has encountered an error
     */
    ERROR("Error", "System has encountered an error"),
    
    /**
     * System is shutting down
     */
    SHUTDOWN("Shutdown", "System has been shut down");
    
    private final String displayName;
    private final String description;
    
    SystemStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * Get the display name of the status
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get the description of the status
     * @return description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if the system is active (enabled)
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return this == ENABLED;
    }
    
    /**
     * Check if the system is in a transitional state
     * @return true if transitional, false otherwise
     */
    public boolean isTransitional() {
        return this == INITIALIZING || this == DISABLING || this == SHUTTING_DOWN;
    }
    
    /**
     * Check if the system is in an error state
     * @return true if error state, false otherwise
     */
    public boolean isError() {
        return this == ERROR;
    }
}
