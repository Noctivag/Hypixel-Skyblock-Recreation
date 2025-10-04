package de.noctivag.plugin.core.architecture;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Event Bus - Centralized event management system
 * 
 * Features:
 * - Type-safe event handling
 * - Async event processing
 * - Event filtering and routing
 * - Performance optimization
 */
public class EventBus {
    
    private final JavaPlugin plugin;
    private final Map<Class<?>, EventHandler<?>> handlers = new ConcurrentHashMap<>();
    
    public EventBus() {
        this.plugin = null; // Will be set by PluginLifecycleManager
    }
    
    public EventBus(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Register an event handler
     */
    public <T extends Event> void register(Class<T> eventType, Consumer<T> handler) {
        register(eventType, handler, EventPriority.NORMAL);
    }
    
    /**
     * Register an event handler with priority
     */
    public <T extends Event> void register(Class<T> eventType, Consumer<T> handler, EventPriority priority) {
        EventHandler<T> eventHandler = new EventHandler<>(handler, priority);
        handlers.put(eventType, eventHandler);
        
        // Register with Bukkit if plugin is available
        if (plugin != null) {
            Bukkit.getPluginManager().registerEvent(
                eventType,
                new Listener() {},
                priority,
                (listener, event) -> {
                    if (eventType.isInstance(event)) {
                        handler.accept(eventType.cast(event));
                    }
                },
                plugin
            );
        }
    }
    
    /**
     * Unregister an event handler
     */
    public void unregister(Class<?> eventType) {
        handlers.remove(eventType);
    }
    
    /**
     * Publish an event
     */
    public void publish(Event event) {
        if (plugin != null) {
            Bukkit.getPluginManager().callEvent(event);
        }
    }
    
    /**
     * Publish an event asynchronously
     */
    public void publishAsync(Event event) {
        if (plugin != null) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                Bukkit.getPluginManager().callEvent(event);
            });
        }
    }
    
    /**
     * Check if an event type is registered
     */
    public boolean isRegistered(Class<?> eventType) {
        return handlers.containsKey(eventType);
    }
    
    /**
     * Get handler count
     */
    public int getHandlerCount() {
        return handlers.size();
    }
    
    /**
     * Clear all handlers
     */
    public void clear() {
        handlers.clear();
    }
    
    /**
     * Event handler wrapper
     */
    private static class EventHandler<T extends Event> {
        private final Consumer<T> handler;
        private final EventPriority priority;
        
        public EventHandler(Consumer<T> handler, EventPriority priority) {
            this.handler = handler;
            this.priority = priority;
        }
        
        public Consumer<T> getHandler() {
            return handler;
        }
        
        public EventPriority getPriority() {
            return priority;
        }
    }
}
