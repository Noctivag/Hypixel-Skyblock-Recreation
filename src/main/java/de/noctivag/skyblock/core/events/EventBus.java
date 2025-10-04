package de.noctivag.skyblock.core.events;
import org.bukkit.inventory.ItemStack;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.function.Consumer;

/**
 * Event bus for decoupled system communication.
 * Implements pub/sub pattern for plugin-wide events.
 */
public class EventBus {
    
    private final JavaSkyblockPlugin plugin;
    private final Logger logger;
    private final Map<Class<?>, List<Consumer<?>>> subscribers = new ConcurrentHashMap<>();
    private final Map<String, Object> eventData = new ConcurrentHashMap<>();
    
    public EventBus(JavaSkyblockPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    /**
     * Subscribe to an event type
     * @param eventType the event type to subscribe to
     * @param handler the event handler
     * @param <T> the event type
     */
    public <T> void subscribe(Class<T> eventType, Consumer<T> handler) {
        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(handler);
        logger.fine("Subscribed to event: " + eventType.getSimpleName());
    }
    
    /**
     * Unsubscribe from an event type
     * @param eventType the event type to unsubscribe from
     * @param handler the event handler to remove
     * @param <T> the event type
     */
    public <T> void unsubscribe(Class<T> eventType, Consumer<T> handler) {
        List<Consumer<?>> handlers = subscribers.get(eventType);
        if (handlers != null) {
            handlers.remove(handler);
            logger.fine("Unsubscribed from event: " + eventType.getSimpleName());
        }
    }
    
    /**
     * Publish an event
     * @param event the event to publish
     * @param <T> the event type
     */
    @SuppressWarnings("unchecked")
    public <T> void publish(T event) {
        Class<?> eventType = event.getClass();
        List<Consumer<?>> handlers = subscribers.get(eventType);
        
        if (handlers != null && !handlers.isEmpty()) {
            logger.fine("Publishing event: " + eventType.getSimpleName() + " to " + handlers.size() + " subscribers");
            
            for (Consumer<?> handler : handlers) {
                try {
                    ((Consumer<T>) handler).accept(event);
                } catch (Exception e) {
                    logger.warning("Error handling event " + eventType.getSimpleName() + ": " + e.getMessage());
                }
            }
        } else {
            logger.fine("No subscribers for event: " + eventType.getSimpleName());
        }
    }
    
    /**
     * Publish an event asynchronously
     * @param event the event to publish
     * @param <T> the event type
     */
    public <T> void publishAsync(T event) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> publish(event));
    }
    
    /**
     * Store event data
     * @param key the data key
     * @param value the data value
     */
    public void storeData(String key, Object value) {
        eventData.put(key, value);
    }
    
    /**
     * Retrieve event data
     * @param key the data key
     * @return the data value, or null if not found
     */
    public Object getData(String key) {
        return eventData.get(key);
    }
    
    /**
     * Retrieve typed event data
     * @param key the data key
     * @param type the expected type
     * @return the data value, or null if not found or wrong type
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key, Class<T> type) {
        Object value = eventData.get(key);
        if (value != null && type.isAssignableFrom(value.getClass())) {
            return (T) value;
        }
        return null;
    }
    
    /**
     * Remove event data
     * @param key the data key
     * @return the removed value, or null if not found
     */
    public Object removeData(String key) {
        return eventData.remove(key);
    }
    
    /**
     * Clear all event data
     */
    public void clearData() {
        eventData.clear();
    }
    
    /**
     * Get all subscribers for an event type
     * @param eventType the event type
     * @return list of subscribers
     */
    public List<Consumer<?>> getSubscribers(Class<?> eventType) {
        return subscribers.getOrDefault(eventType, Collections.emptyList());
    }
    
    /**
     * Get all registered event types
     * @return set of event types
     */
    public Set<Class<?>> getRegisteredEventTypes() {
        return subscribers.keySet();
    }
    
    /**
     * Get total number of subscribers
     * @return total subscriber count
     */
    public int getTotalSubscribers() {
        return subscribers.values().stream().mapToInt(List::size).sum();
    }
}
