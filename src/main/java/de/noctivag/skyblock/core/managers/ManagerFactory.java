package de.noctivag.skyblock.core.managers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.core.managers.impl.UnifiedManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Factory for creating and managing UnifiedManager instances.
 * Provides singleton access to managers and ensures proper lifecycle management.
 */
public class ManagerFactory {
    
    private static ManagerFactory instance;
    private final JavaSkyblockPlugin plugin;
    private final Logger logger;
    private final ConcurrentHashMap<String, UnifiedManager<?, ?>> managers;
    
    private ManagerFactory(JavaSkyblockPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.managers = new ConcurrentHashMap<>();
    }
    
    /**
     * Get the singleton instance
     * @param plugin the plugin instance
     * @return manager factory instance
     */
    public static synchronized ManagerFactory getInstance(JavaSkyblockPlugin plugin) {
        if (instance == null) {
            instance = new ManagerFactory(plugin);
        }
        return instance;
    }
    
    /**
     * Create or get a manager instance
     * @param name the manager name
     * @param keyType the key type class
     * @param valueType the value type class
     * @param managerType the manager type
     * @return manager instance
     */
    @SuppressWarnings("unchecked")
    public <K, V> UnifiedManager<K, V> getManager(String name, Class<K> keyType, Class<V> valueType, UnifiedManager.ManagerType managerType) {
        return (UnifiedManager<K, V>) managers.computeIfAbsent(name, k -> {
            UnifiedManager<K, V> manager = new UnifiedManagerImpl<>(plugin, name, managerType);
            logger.info("Created manager: " + name + " (" + managerType.getDisplayName() + ")");
            return manager;
        });
    }
    
    /**
     * Create or get a player data manager
     * @return player data manager
     */
    public UnifiedManager<String, Object> getPlayerDataManager() {
        return getManager("player_data", String.class, Object.class, UnifiedManager.ManagerType.PLAYER);
    }
    
    /**
     * Create or get an economy manager
     * @return economy manager
     */
    public UnifiedManager<String, Double> getEconomyManager() {
        return getManager("economy", String.class, Double.class, UnifiedManager.ManagerType.ECONOMY);
    }
    
    /**
     * Create or get a skyblock manager
     * @return skyblock manager
     */
    public UnifiedManager<String, Object> getSkyblockManager() {
        return getManager("skyblock", String.class, Object.class, UnifiedManager.ManagerType.SKYBLOCK);
    }
    
    /**
     * Create or get a cosmetics manager
     * @return cosmetics manager
     */
    public UnifiedManager<String, Object> getCosmeticsManager() {
        return getManager("cosmetics", String.class, Object.class, UnifiedManager.ManagerType.COSMETICS);
    }
    
    /**
     * Create or get an achievements manager
     * @return achievements manager
     */
    public UnifiedManager<String, Object> getAchievementsManager() {
        return getManager("achievements", String.class, Object.class, UnifiedManager.ManagerType.ACHIEVEMENTS);
    }
    
    /**
     * Create or get a social manager
     * @return social manager
     */
    public UnifiedManager<String, Object> getSocialManager() {
        return getManager("social", String.class, Object.class, UnifiedManager.ManagerType.SOCIAL);
    }
    
    /**
     * Create or get an events manager
     * @return events manager
     */
    public UnifiedManager<String, Object> getEventsManager() {
        return getManager("events", String.class, Object.class, UnifiedManager.ManagerType.EVENTS);
    }
    
    /**
     * Create or get a configuration manager
     * @return configuration manager
     */
    public UnifiedManager<String, Object> getConfigManager() {
        return getManager("config", String.class, Object.class, UnifiedManager.ManagerType.CONFIG);
    }
    
    /**
     * Get all managers
     * @return map of all managers
     */
    public ConcurrentHashMap<String, UnifiedManager<?, ?>> getAllManagers() {
        return new ConcurrentHashMap<>(managers);
    }
    
    /**
     * Get manager by name
     * @param name the manager name
     * @return manager instance or null if not found
     */
    public UnifiedManager<?, ?> getManager(String name) {
        return managers.get(name);
    }
    
    /**
     * Remove a manager
     * @param name the manager name
     * @return the removed manager or null if not found
     */
    public UnifiedManager<?, ?> removeManager(String name) {
        UnifiedManager<?, ?> manager = managers.remove(name);
        if (manager != null) {
            logger.info("Removed manager: " + name);
        }
        return manager;
    }
    
    /**
     * Check if a manager exists
     * @param name the manager name
     * @return true if exists, false otherwise
     */
    public boolean hasManager(String name) {
        return managers.containsKey(name);
    }
    
    /**
     * Get manager count
     * @return number of managers
     */
    public int getManagerCount() {
        return managers.size();
    }
    
    /**
     * Clear all managers
     */
    public void clearAll() {
        managers.clear();
        logger.info("Cleared all managers");
    }
}
