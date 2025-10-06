package de.noctivag.skyblock.features.events.types;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import java.util.concurrent.CompletableFuture;

/**
 * Duration class for event durations
 */
public class Duration implements Service {
    private long startTime;
    private long endTime;
    private long duration;
    private SystemStatus status = SystemStatus.UNINITIALIZED;

    public Duration(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime - startTime;
    }

    public Duration(long duration) {
        this.startTime = java.lang.System.currentTimeMillis();
        this.endTime = startTime + duration;
        this.duration = duration;
    }

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        // Initialize duration
        status = SystemStatus.RUNNING;
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        // Shutdown duration
        status = SystemStatus.DISABLED;
    }

    @Override
    public String getName() {
        return "Duration";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }

    public int getPriority() {
        return 50;
    }

    public boolean isRequired() {
        return false;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isActive() {
        return java.lang.System.currentTimeMillis() < endTime;
    }

    public long getRemainingTime() {
        return Math.max(0, endTime - java.lang.System.currentTimeMillis());
    }

    public double getProgress() {
        long elapsed = java.lang.System.currentTimeMillis() - startTime;
        return Math.min(1.0, (double) elapsed / duration);
    }
}
