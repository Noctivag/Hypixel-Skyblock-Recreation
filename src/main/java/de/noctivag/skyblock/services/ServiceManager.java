package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Zentraler Service-Manager für alle Plugin-Services
 * Entkoppelt die Logik und ermöglicht Dependency Injection
 */
public class ServiceManager {
    
    private final SkyblockPlugin plugin;
    private final Map<Class<?>, Object> services;
    private final Map<String, Object> namedServices;
    
    public ServiceManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.services = new ConcurrentHashMap<>();
        this.namedServices = new ConcurrentHashMap<>();
        
        plugin.getLogger().info("ServiceManager initialized");
    }
    
    /**
     * Registriert einen Service mit seiner Klasse
     * @param serviceClass Service-Klasse
     * @param service Service-Instanz
     * @param <T> Service-Typ
     */
    public <T> void registerService(Class<T> serviceClass, T service) {
        if (serviceClass == null || service == null) {
            throw new IllegalArgumentException("Service class and instance cannot be null");
        }
        
        services.put(serviceClass, service);
        
        if (plugin.getSettingsConfig().isVerboseLogging()) {
            plugin.getLogger().info("Registered service: " + serviceClass.getSimpleName());
        }
    }
    
    /**
     * Registriert einen Service mit einem Namen
     * @param name Service-Name
     * @param service Service-Instanz
     */
    public void registerService(String name, Object service) {
        if (name == null || name.trim().isEmpty() || service == null) {
            throw new IllegalArgumentException("Service name and instance cannot be null or empty");
        }
        
        namedServices.put(name, service);
        
        if (plugin.getSettingsConfig().isVerboseLogging()) {
            plugin.getLogger().info("Registered named service: " + name);
        }
    }
    
    /**
     * Gibt einen Service anhand seiner Klasse zurück
     * @param serviceClass Service-Klasse
     * @param <T> Service-Typ
     * @return Service-Instanz oder null
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        if (serviceClass == null) {
            return null;
        }
        
        Object service = services.get(serviceClass);
        if (service == null) {
            plugin.getLogger().warning("Service not found: " + serviceClass.getSimpleName());
            return null;
        }
        
        return (T) service;
    }
    
    /**
     * Gibt einen Service anhand seines Namens zurück
     * @param name Service-Name
     * @return Service-Instanz oder null
     */
    public Object getService(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        
        Object service = namedServices.get(name);
        if (service == null) {
            plugin.getLogger().warning("Named service not found: " + name);
            return null;
        }
        
        return service;
    }
    
    /**
     * Prüft ob ein Service registriert ist
     * @param serviceClass Service-Klasse
     * @return true wenn registriert
     */
    public boolean hasService(Class<?> serviceClass) {
        return serviceClass != null && services.containsKey(serviceClass);
    }
    
    /**
     * Prüft ob ein Service mit dem Namen registriert ist
     * @param name Service-Name
     * @return true wenn registriert
     */
    public boolean hasService(String name) {
        return name != null && !name.trim().isEmpty() && namedServices.containsKey(name);
    }
    
    /**
     * Entfernt einen Service
     * @param serviceClass Service-Klasse
     * @param <T> Service-Typ
     * @return Entfernte Service-Instanz oder null
     */
    @SuppressWarnings("unchecked")
    public <T> T unregisterService(Class<T> serviceClass) {
        if (serviceClass == null) {
            return null;
        }
        
        Object service = services.remove(serviceClass);
        if (service != null && plugin.getSettingsConfig().isVerboseLogging()) {
            plugin.getLogger().info("Unregistered service: " + serviceClass.getSimpleName());
        }
        
        return (T) service;
    }
    
    /**
     * Entfernt einen Service anhand seines Namens
     * @param name Service-Name
     * @return Entfernte Service-Instanz oder null
     */
    public Object unregisterService(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        
        Object service = namedServices.remove(name);
        if (service != null && plugin.getSettingsConfig().isVerboseLogging()) {
            plugin.getLogger().info("Unregistered named service: " + name);
        }
        
        return service;
    }
    
    /**
     * Gibt alle registrierten Services zurück
     * @return Map mit allen Services
     */
    public Map<Class<?>, Object> getAllServices() {
        return new HashMap<>(services);
    }
    
    /**
     * Gibt alle registrierten benannten Services zurück
     * @return Map mit allen benannten Services
     */
    public Map<String, Object> getAllNamedServices() {
        return new HashMap<>(namedServices);
    }
    
    /**
     * Gibt die Anzahl der registrierten Services zurück
     * @return Anzahl der Services
     */
    public int getServiceCount() {
        return services.size() + namedServices.size();
    }
    
    /**
     * Gibt Service-Statistiken zurück
     * @return Service-Statistiken als String
     */
    public String getServiceStats() {
        return String.format("ServiceManager Stats - Class Services: %d, Named Services: %d, Total: %d",
                           services.size(), namedServices.size(), getServiceCount());
    }
    
    /**
     * Schließt alle Services und räumt Ressourcen auf
     */
    public void shutdown() {
        plugin.getLogger().info("Shutting down ServiceManager...");
        
        // Schließe alle Services die ein shutdown() haben
        for (Object service : services.values()) {
            if (service instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) service).close();
                } catch (Exception e) {
                    plugin.getLogger().warning("Error shutting down service " + 
                                             service.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }
        
        for (Object service : namedServices.values()) {
            if (service instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) service).close();
                } catch (Exception e) {
                    plugin.getLogger().warning("Error shutting down named service: " + e.getMessage());
                }
            }
        }
        
        services.clear();
        namedServices.clear();
        
        plugin.getLogger().info("ServiceManager shutdown completed");
    }
}
