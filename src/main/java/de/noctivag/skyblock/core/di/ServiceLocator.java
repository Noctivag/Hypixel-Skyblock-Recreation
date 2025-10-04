package de.noctivag.skyblock.core.di;

import de.noctivag.skyblock.core.api.Service;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Service Locator for dependency injection.
 * Manages service lifecycle and provides dependency resolution.
 */
public class ServiceLocator {
    
    private static ServiceLocator instance;
    private final Map<Class<?>, Service> services = new ConcurrentHashMap<>();
    private final Map<Class<?>, Class<?>> implementations = new ConcurrentHashMap<>();
    private final Set<Service> initializedServices = ConcurrentHashMap.newKeySet();
    private final Logger logger;
    private final JavaSkyblockPlugin plugin;
    
    private ServiceLocator(JavaSkyblockPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    /**
     * Get the singleton instance
     * @param plugin the plugin instance
     * @return service locator instance
     */
    public static synchronized ServiceLocator getInstance(JavaSkyblockPlugin plugin) {
        if (instance == null) {
            instance = new ServiceLocator(plugin);
        }
        return instance;
    }
    
    /**
     * Register a service implementation
     * @param interfaceClass the interface class
     * @param implementationClass the implementation class
     */
    public <T extends Service> void register(Class<T> interfaceClass, Class<? extends T> implementationClass) {
        implementations.put(interfaceClass, implementationClass);
        logger.info("Registered service: " + interfaceClass.getSimpleName() + " -> " + implementationClass.getSimpleName());
    }
    
    /**
     * Register a service instance
     * @param interfaceClass the interface class
     * @param instance the service instance
     */
    public <T extends Service> void register(Class<T> interfaceClass, T instance) {
        services.put(interfaceClass, instance);
        logger.info("Registered service instance: " + interfaceClass.getSimpleName());
    }
    
    /**
     * Get a service by its interface class
     * @param serviceClass the service interface class
     * @return the service instance
     */
    @SuppressWarnings("unchecked")
    public <T extends Service> T get(Class<T> serviceClass) {
        Service service = services.get(serviceClass);
        
        if (service == null) {
            // Try to create from registered implementation
            Class<?> implementationClass = implementations.get(serviceClass);
            if (implementationClass != null) {
                try {
                    service = (Service) implementationClass.getDeclaredConstructor().newInstance();
                    services.put(serviceClass, service);
                    logger.info("Created service instance: " + serviceClass.getSimpleName());
                } catch (Exception e) {
                    logger.severe("Failed to create service: " + serviceClass.getSimpleName() + " - " + e.getMessage());
                    return null;
                }
            }
        }
        
        return (T) service;
    }
    
    /**
     * Check if a service is registered
     * @param serviceClass the service interface class
     * @return true if registered, false otherwise
     */
    public boolean isRegistered(Class<? extends Service> serviceClass) {
        return services.containsKey(serviceClass) || implementations.containsKey(serviceClass);
    }
    
    /**
     * Initialize all registered services
     * @return CompletableFuture that completes when all services are initialized
     */
    public CompletableFuture<Void> initializeAll() {
        List<Service> allServices = new ArrayList<>(services.values());
        
        // Sort by priority
        allServices.sort(Comparator.comparingInt(Service::getPriority));
        
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        for (Service service : allServices) {
            if (!initializedServices.contains(service)) {
                CompletableFuture<Void> future = service.initialize()
                    .thenRun(() -> {
                        initializedServices.add(service);
                        logger.info("Initialized service: " + service.getName());
                    })
                    .exceptionally(throwable -> {
                        logger.severe("Failed to initialize service: " + service.getName() + " - " + throwable.getMessage());
                        if (service.isRequired()) {
                            logger.severe("Required service failed to initialize, plugin may not work correctly");
                        }
                        return null;
                    });
                futures.add(future);
            }
        }
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]));
    }
    
    /**
     * Shutdown all services
     * @return CompletableFuture that completes when all services are shut down
     */
    public CompletableFuture<Void> shutdownAll() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        
        // Shutdown in reverse priority order
        List<Service> servicesToShutdown = new ArrayList<>(initializedServices);
        servicesToShutdown.sort(Comparator.comparingInt(Service::getPriority).reversed());
        
        for (Service service : servicesToShutdown) {
            CompletableFuture<Void> future = service.shutdown()
                .thenRun(() -> {
                    initializedServices.remove(service);
                    logger.info("Shutdown service: " + service.getName());
                })
                .exceptionally(throwable -> {
                    logger.warning("Failed to shutdown service: " + service.getName() + " - " + throwable.getMessage());
                    return null;
                });
            futures.add(future);
        }
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]));
    }
    
    /**
     * Get all registered services
     * @return set of all service classes
     */
    public Set<Class<?>> getRegisteredServices() {
        Set<Class<?>> allServices = new HashSet<>();
        allServices.addAll(services.keySet());
        allServices.addAll(implementations.keySet());
        return allServices;
    }
    
    /**
     * Get initialization status
     * @return true if all services are initialized, false otherwise
     */
    public boolean isAllInitialized() {
        return initializedServices.size() == services.size();
    }
    
    /**
     * Clear all registrations (for testing)
     */
    public void clear() {
        services.clear();
        implementations.clear();
        initializedServices.clear();
    }
}
