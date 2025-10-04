package de.noctivag.plugin.core.api;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Generic manager interface for managing collections of objects.
 * Extends Service to provide lifecycle management.
 * 
 * @param <K> Key type
 * @param <V> Value type
 */
public interface Manager<K, V> extends Service {
    
    /**
     * Get a value by key
     * @param key the key
     * @return the value, or null if not found
     */
    V get(K key);
    
    /**
     * Set a value for a key
     * @param key the key
     * @param value the value
     */
    void set(K key, V value);
    
    /**
     * Remove a value by key
     * @param key the key
     * @return the removed value, or null if not found
     */
    V remove(K key);
    
    /**
     * Check if a key exists
     * @param key the key
     * @return true if exists, false otherwise
     */
    boolean contains(K key);
    
    /**
     * Get all keys
     * @return set of all keys
     */
    Set<K> getKeys();
    
    /**
     * Get all values
     * @return collection of all values
     */
    Iterable<V> getValues();
    
    /**
     * Get all key-value pairs
     * @return map of all entries
     */
    Map<K, V> getAll();
    
    /**
     * Clear all entries
     */
    void clear();
    
    /**
     * Get the number of entries
     * @return entry count
     */
    int size();
    
    /**
     * Check if the manager is empty
     * @return true if empty, false otherwise
     */
    boolean isEmpty();
    
    /**
     * Save all data to persistent storage
     * @return CompletableFuture that completes when save is done
     */
    CompletableFuture<Void> save();
    
    /**
     * Load all data from persistent storage
     * @return CompletableFuture that completes when load is done
     */
    CompletableFuture<Void> load();
}
