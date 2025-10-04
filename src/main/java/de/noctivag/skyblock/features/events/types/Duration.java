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
        this.startTime = System.currentTimeMillis();
        this.endTime = startTime + duration;
        this.duration = duration;
    }

    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            // Initialize duration
            status = SystemStatus.ENABLED;
        });
    }

    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            // Shutdown duration
            status = SystemStatus.UNINITIALIZED;
        });
    }

    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public String getName() {
        return "Duration";
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
        return System.currentTimeMillis() < endTime;
    }

    public long getRemainingTime() {
        return Math.max(0, endTime - System.currentTimeMillis());
    }

    public double getProgress() {
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.min(1.0, (double) elapsed / duration);
    }
}
