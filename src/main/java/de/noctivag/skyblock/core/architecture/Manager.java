package de.noctivag.skyblock.core.architecture;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap; // Unused import

/**
 * Manager Interface - Base interface for all managers
 * 
 * Features:
 * - Data management
 * - CRUD operations
 * - Thread-safe operations
 * - Lifecycle management
 */
public interface Manager<T> extends System {
    
    /**
     * Get an item by key
     */
    T get(String key);
    
    /**
     * Set an item with key
     */
    void set(String key, T value);
    
    /**
     * Remove an item by key
     */
    void remove(String key);
    
    /**
     * Check if key exists
     */
    boolean contains(String key);
    
    /**
     * Get all items
     */
    Map<String, T> getAll();
    
    /**
     * Clear all items
     */
    void clear();
    
    /**
     * Get item count
     */
    int size();
    
    /**
     * Check if manager is empty
     */
    boolean isEmpty();
}
