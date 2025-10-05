package de.noctivag.skyblock.events;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventScheduler {
    private final SkyblockPlugin SkyblockPlugin;
    private final EventManager eventManager;
    private final Map<String, Thread> scheduledEvents = new ConcurrentHashMap<>();
    private final Map<String, Long> eventCooldowns = new ConcurrentHashMap<>();
    private final Map<String, Integer> eventCounts = new ConcurrentHashMap<>();
    
    // Event schedules (in minutes)
    private static final Map<String, Integer> EVENT_SCHEDULES = Map.of(
        "ender_dragon", 30,
        "wither", 45,
        "custom_boss", 60,
        "elder_guardian", 40,
        "ravager", 35,
        "phantom_king", 50,
        "blaze_king", 55,
        "enderman_lord", 70
    );
    
    public EventScheduler(SkyblockPlugin SkyblockPlugin, EventManager eventManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.eventManager = eventManager;
        startScheduler();
    }
    
    private void startScheduler() {
        // Schedule all events
        for (Map.Entry<String, Integer> entry : EVENT_SCHEDULES.entrySet()) {
            scheduleEvent(entry.getKey(), entry.getValue());
        }
        
        // Start announcement system
        startAnnouncementSystem();
    }
    
    private void scheduleEvent(String eventId, int intervalMinutes) {
        Thread eventThread = Thread.ofVirtual().start(() -> {
            try {
                while (SkyblockPlugin.isEnabled()) {
                    // Check if event is already active
                    if (eventManager.getEvent(eventId).isActive()) {
                        Thread.sleep(intervalMinutes * 60 * 1000L); // Wait for interval
                        continue;
                    }
                    
                    // Announce upcoming event
                    announceUpcomingEvent(eventId, 5); // 5 minutes warning
                    
                    // Wait 5 minutes then start the event
                    Thread.sleep(5 * 60 * 1000L); // 5 minutes = 300,000 ms
                    if (SkyblockPlugin.isEnabled()) {
                        startScheduledEvent(eventId);
                    }
                    
                    // Wait for the rest of the interval
                    Thread.sleep((intervalMinutes - 5) * 60 * 1000L);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        scheduledEvents.put(eventId, eventThread);
    }
    
    private void announceUpcomingEvent(String eventId, int minutes) {
        String eventName = getEventDisplayName(eventId);
        
        // Global announcement
        Bukkit.broadcastMessage("§6§l[EVENT] §e" + eventName + " §7startet in §c" + minutes + " Minuten§7!");
        Bukkit.broadcastMessage("§7Nutze §e/event join " + eventId + " §7um teilzunehmen!");
        
        // Play sound for all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }
        
        // Countdown announcements - use virtual thread for Folia compatibility
        if (minutes > 1) {
            Thread.ofVirtual().start(() -> {
                try {
                    Thread.sleep(60 * 1000L); // 1 minute = 60,000 ms
                    if (SkyblockPlugin.isEnabled()) {
                        announceUpcomingEvent(eventId, minutes - 1);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
    
    private void startScheduledEvent(String eventId) {
        // Create new event instance
        Event event = eventManager.getEvent(eventId);
        if (event != null && !event.isActive()) {
            // Announce event start
            String eventName = getEventDisplayName(eventId);
            Bukkit.broadcastMessage("§a§l[EVENT] §e" + eventName + " §7hat begonnen!");
            Bukkit.broadcastMessage("§7Nutze §e/event join " + eventId + " §7um teilzunehmen!");
            
            // Play sound for all players
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.0f);
            }
            
            // Update event count
            eventCounts.put(eventId, eventCounts.getOrDefault(eventId, 0) + 1);
        }
    }
    
    private void startAnnouncementSystem() {
        // Daily event summary - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                while (SkyblockPlugin.isEnabled()) {
                    announceDailySummary();
                    Thread.sleep(24 * 60 * 60 * 1000L); // Every 24 hours = 86,400,000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Hourly event reminders - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                while (SkyblockPlugin.isEnabled()) {
                    announceNextEvents();
                    Thread.sleep(60 * 60 * 1000L); // Every hour = 3,600,000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    private void announceDailySummary() {
        Bukkit.broadcastMessage("§6§l[EVENT SUMMARY] §7Gestern durchgeführte Events:");
        
        for (Map.Entry<String, Integer> entry : eventCounts.entrySet()) {
            String eventName = getEventDisplayName(entry.getKey());
            int count = entry.getValue();
            Bukkit.broadcastMessage("§7- §e" + eventName + ": §f" + count + " Events");
        }
        
        // Reset daily counts
        eventCounts.clear();
    }
    
    private void announceNextEvents() {
        Bukkit.broadcastMessage("§6§l[NEXT EVENTS] §7Kommende Events:");
        
        for (Map.Entry<String, Integer> entry : EVENT_SCHEDULES.entrySet()) {
            String eventId = entry.getKey();
            int interval = entry.getValue();
            String eventName = getEventDisplayName(eventId);
            
            // Calculate time until next event
            long currentTime = java.lang.System.currentTimeMillis();
            long lastEvent = eventCooldowns.getOrDefault(eventId, 0L);
            long timeUntilNext = (lastEvent + (interval * 60 * 1000L)) - currentTime;
            
            if (timeUntilNext > 0) {
                int minutes = (int) (timeUntilNext / (60 * 1000));
                Bukkit.broadcastMessage("§7- §e" + eventName + ": §f" + minutes + " Minuten");
            }
        }
    }
    
    private String getEventDisplayName(String eventId) {
        return switch (eventId) {
            case "ender_dragon" -> "Ender Dragon Event";
            case "wither" -> "Wither Boss Event";
            case "custom_boss" -> "Titan Event";
            case "elder_guardian" -> "Elder Guardian Event";
            case "ravager" -> "Ravager Event";
            case "phantom_king" -> "Phantom King Event";
            case "blaze_king" -> "Blaze King Event";
            case "enderman_lord" -> "Enderman Lord Event";
            default -> eventId;
        };
    }
    
    public void updateEventCooldown(String eventId) {
        eventCooldowns.put(eventId, java.lang.System.currentTimeMillis());
    }
    
    public long getTimeUntilNextEvent(String eventId) {
        int interval = EVENT_SCHEDULES.getOrDefault(eventId, 60);
        long lastEvent = eventCooldowns.getOrDefault(eventId, 0L);
        long timeUntilNext = (lastEvent + (interval * 60 * 1000L)) - java.lang.System.currentTimeMillis();
        return Math.max(0, timeUntilNext);
    }
    
    public Map<String, Integer> getEventCounts() {
        return new HashMap<>(eventCounts);
    }
    
    public void shutdown() {
        // Cancel all scheduled events
        for (Thread thread : scheduledEvents.values()) {
            thread.interrupt();
        }
        scheduledEvents.clear();
    }
}
