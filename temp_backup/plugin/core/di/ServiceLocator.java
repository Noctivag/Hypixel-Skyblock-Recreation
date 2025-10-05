package de.noctivag.SkyblockPlugin.core.di;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service locator for dependency injection
 */
public class ServiceLocator {
    private final Map<Class<?>, Object> services;
    private static ServiceLocator instance;

    private ServiceLocator() {
        this.services = new HashMap<>();
    }

    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }

    public <T> void register(Class<T> serviceClass, T service) {
        services.put(serviceClass, service);
    }

    public <T> Optional<T> get(Class<T> serviceClass) {
        Object service = services.get(serviceClass);
        if (service != null && serviceClass.isInstance(service)) {
            return Optional.of(serviceClass.cast(service));
        }
        return Optional.empty();
    }

    public <T> T getRequired(Class<T> serviceClass) {
        return get(serviceClass).orElseThrow(() -> 
            new IllegalStateException("Required service not found: " + serviceClass.getName()));
    }

    public boolean isRegistered(Class<?> serviceClass) {
        return services.containsKey(serviceClass);
    }

    public void unregister(Class<?> serviceClass) {
        services.remove(serviceClass);
    }

    public void clear() {
        services.clear();
    }

    public int getServiceCount() {
        return services.size();
    }
}
