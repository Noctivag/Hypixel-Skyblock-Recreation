package de.noctivag.skyblock.events;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.logging.Level;

/**
 * Event scheduler for managing event timing
 */
public class EventScheduler {
    
    private final UltimateEventSystem eventSystem;
    private final SkyblockPlugin plugin;
    private BukkitTask schedulerTask;
    private boolean running = false;
    
    public EventScheduler(UltimateEventSystem eventSystem) {
        this.eventSystem = eventSystem;
        this.plugin = eventSystem.getPlugin();
    }
    
    /**
     * Start the event scheduler
     */
    public void start() {
        if (running) return;
        
        running = true;
        schedulerTask = new BukkitRunnable() {
            @Override
            public void run() {
                checkScheduledEvents();
            }
        }.runTaskTimer(plugin, 0L, 20L * 60L); // Check every minute
        
        plugin.getLogger().info("Event scheduler started");
    }
    
    /**
     * Stop the event scheduler
     */
    public void stop() {
        if (!running) return;
        
        running = false;
        if (schedulerTask != null) {
            schedulerTask.cancel();
            schedulerTask = null;
        }
        
        plugin.getLogger().info("Event scheduler stopped");
    }
    
    /**
     * Check for scheduled events
     */
    private void checkScheduledEvents() {
        for (SkyblockEvent event : eventSystem.getEvents().values()) {
            if (event.isAutoStart() && !event.isActive()) {
                // Check if it's time to start the event based on schedule
                if (shouldStartEvent(event)) {
                    eventSystem.startEvent(event.getId());
                }
            }
        }
    }
    
    /**
     * Check if event should start based on schedule
     */
    private boolean shouldStartEvent(SkyblockEvent event) {
        List<String> schedule = event.getSchedule();
        if (schedule == null || schedule.isEmpty()) {
            return false; // No schedule defined
        }
        
        // Simple time-based scheduling
        // In a real implementation, this would parse cron expressions or time patterns
        long currentTime = System.currentTimeMillis();
        long currentHour = (currentTime / (1000 * 60 * 60)) % 24;
        
        for (String scheduleEntry : schedule) {
            try {
                // Parse schedule entry (e.g., "14:00" for 2 PM)
                String[] timeParts = scheduleEntry.split(":");
                if (timeParts.length == 2) {
                    int scheduledHour = Integer.parseInt(timeParts[0]);
                    int scheduledMinute = Integer.parseInt(timeParts[1]);
                    
                    // Check if current time matches scheduled time (within 1 minute)
                    if (currentHour == scheduledHour) {
                        long currentMinute = (currentTime / (1000 * 60)) % 60;
                        if (Math.abs(currentMinute - scheduledMinute) <= 1) {
                            return true;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                plugin.getLogger().log(Level.WARNING, "Invalid schedule entry: " + scheduleEntry, e);
            }
        }
        
        return false;
    }
    
    /**
     * Check if scheduler is running
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Get scheduler status
     */
    public String getStatus() {
        return running ? "Running" : "Stopped";
    }
    
    /**
     * Get plugin instance
     */
    public SkyblockPlugin getPlugin() {
        return plugin;
    }
}