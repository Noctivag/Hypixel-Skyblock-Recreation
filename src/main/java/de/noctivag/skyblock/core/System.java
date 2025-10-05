package de.noctivag.skyblock.core;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;

/**
 * System - Base class for all systems
 */
public abstract class System implements Service {
    
    protected SystemStatus status = SystemStatus.DISABLED;
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    /**
     * Check if the system is running
     */
    public boolean isRunning() {
        return status == SystemStatus.RUNNING;
    }
    
    /**
     * Check if the system is disabled
     */
    public boolean isDisabled() {
        return status == SystemStatus.DISABLED;
    }
    
    /**
     * Check if the system is in error state
     */
    public boolean isError() {
        return status == SystemStatus.ERROR;
    }
}

