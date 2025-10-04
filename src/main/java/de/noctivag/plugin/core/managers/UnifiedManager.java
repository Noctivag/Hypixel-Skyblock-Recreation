package de.noctivag.plugin.core.managers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.core.api.Manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Unified manager interface that consolidates common manager functionality.
 * Provides a single interface for managing different types of data.
 * 
 * @param <K> Key type
 * @param <V> Value type
 */
public interface UnifiedManager<K, V> extends Manager<K, V> {
    
    /**
     * Get multiple values by keys
     * @param keys the keys
     * @return map of key-value pairs
     */
    Map<K, V> getMultiple(List<K> keys);
    
    /**
     * Set multiple values
     * @param entries the key-value pairs
     */
    void setMultiple(Map<K, V> entries);
    
    /**
     * Remove multiple values by keys
     * @param keys the keys to remove
     * @return map of removed key-value pairs
     */
    Map<K, V> removeMultiple(List<K> keys);
    
    /**
     * Check if multiple keys exist
     * @param keys the keys to check
     * @return map of key-existence pairs
     */
    Map<K, Boolean> containsMultiple(List<K> keys);
    
    /**
     * Get values by pattern (for string keys)
     * @param pattern the pattern to match
     * @return map of matching key-value pairs
     */
    Map<K, V> getByPattern(String pattern);
    
    /**
     * Get all keys matching a pattern
     * @param pattern the pattern to match
     * @return list of matching keys
     */
    List<K> getKeysByPattern(String pattern);
    
    /**
     * Get statistics about the manager
     * @return manager statistics
     */
    ManagerStatistics getStatistics();
    
    /**
     * Backup all data
     * @return CompletableFuture that completes when backup is done
     */
    CompletableFuture<Void> backup();
    
    /**
     * Restore data from backup
     * @return CompletableFuture that completes when restore is done
     */
    CompletableFuture<Void> restore();
    
    /**
     * Get manager type
     * @return manager type
     */
    ManagerType getManagerType();
    
    /**
     * Manager type enumeration
     */
    enum ManagerType {
        CONFIG("Configuration Manager"),
        DATA("Data Manager"),
        PLAYER("Player Manager"),
        ECONOMY("Economy Manager"),
        SKYBLOCK("Skyblock Manager"),
        SOCIAL("Social Manager"),
        COSMETICS("Cosmetics Manager"),
        ACHIEVEMENTS("Achievements Manager"),
        EVENTS("Events Manager"),
        CUSTOM("Custom Manager");
        
        private final String displayName;
        
        ManagerType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Manager statistics container
     */
    class ManagerStatistics {
        private final int totalEntries;
        private final int activeEntries;
        private final long lastAccessed;
        private final long lastModified;
        private final long memoryUsage;
        
        public ManagerStatistics(int totalEntries, int activeEntries, long lastAccessed, long lastModified, long memoryUsage) {
            this.totalEntries = totalEntries;
            this.activeEntries = activeEntries;
            this.lastAccessed = lastAccessed;
            this.lastModified = lastModified;
            this.memoryUsage = memoryUsage;
        }
        
        public int getTotalEntries() { return totalEntries; }
        public int getActiveEntries() { return activeEntries; }
        public long getLastAccessed() { return lastAccessed; }
        public long getLastModified() { return lastModified; }
        public long getMemoryUsage() { return memoryUsage; }
    }
}
