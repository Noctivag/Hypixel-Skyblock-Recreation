package de.noctivag.plugin.core.architecture;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service Locator - Dependency Injection Container
 * 
 * Features:
 * - Type-safe service registration
 * - Singleton pattern enforcement
 * - Lazy initialization support
 * - Thread-safe operations
 */
public class ServiceLocator {
    
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> singletons = new ConcurrentHashMap<>();
    
    /**
     * Register a service instance
     */
    public <T> void register(Class<T> serviceType, T instance) {
        if (instance == null) {
            throw new IllegalArgumentException("Service instance cannot be null");
        }
        
        services.put(serviceType, instance);
    }
    
    /**
     * Register a singleton service
     */
    public <T> void registerSingleton(Class<T> serviceType, T instance) {
        if (instance == null) {
            throw new IllegalArgumentException("Singleton instance cannot be null");
        }
        
        singletons.put(serviceType, instance);
    }
    
    /**
     * Get a service instance
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> serviceType) {
        // Check singletons first
        T singleton = (T) singletons.get(serviceType);
        if (singleton != null) {
            return singleton;
        }
        
        // Check regular services
        return (T) services.get(serviceType);
    }
    
    /**
     * Check if a service is registered
     */
    public boolean isRegistered(Class<?> serviceType) {
        return services.containsKey(serviceType) || singletons.containsKey(serviceType);
    }
    
    /**
     * Remove a service
     */
    public void remove(Class<?> serviceType) {
        services.remove(serviceType);
        singletons.remove(serviceType);
    }
    
    /**
     * Clear all services
     */
    public void clear() {
        services.clear();
        singletons.clear();
    }
    
    /**
     * Get all registered service types
     */
    public Class<?>[] getRegisteredTypes() {
        return services.keySet().toArray(new Class[0]);
    }
    
    /**
     * Get service count
     */
    public int getServiceCount() {
        return services.size() + singletons.size();
    }
}
