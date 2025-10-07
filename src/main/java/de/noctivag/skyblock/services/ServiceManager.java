package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.cache.PlayerProfileCache;
import de.noctivag.skyblock.config.DatabaseConfig;
import de.noctivag.skyblock.config.SettingsConfig;
import de.noctivag.skyblock.data.DatabaseManager;
import de.noctivag.skyblock.worlds.RollingRestartWorldManager;

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

    public void registerAllServices(SettingsConfig settingsConfig, DatabaseConfig databaseConfig, DatabaseManager databaseManager, RollingRestartWorldManager rollingRestartWorldManager) {
        // Core Services
        registerService(SettingsConfig.class, settingsConfig);
        registerService(DatabaseConfig.class, databaseConfig);
        registerService(DatabaseManager.class, databaseManager);
        registerService(RollingRestartWorldManager.class, rollingRestartWorldManager);

        // Caching Service
        PlayerProfileCache playerProfileCache = new PlayerProfileCache(plugin);
        registerService(PlayerProfileCache.class, playerProfileCache);

        // Player Profile Service (depends on cache and db)
        PlayerProfileService playerProfileService = new PlayerProfileService(plugin);
        registerService(PlayerProfileService.class, playerProfileService);

        // World Reset Service (depends on RollingRestartWorldManager and SettingsConfig)
        if (plugin.getSettingsConfig().isRollingRestartEnabled()) {
            WorldResetService worldResetService = new WorldResetService(plugin, rollingRestartWorldManager);
            registerService(WorldResetService.class, worldResetService);
            plugin.getLogger().info("WorldResetService registered.");
        } else {
            plugin.getLogger().info("Rolling-Restart system is disabled in settings. WorldResetService not registered.");
        }

        // World Management Service (depends on RollingRestartWorldManager)
        WorldManagementService worldManagementService = new WorldManagementService(plugin);
        registerService(WorldManagementService.class, worldManagementService);

        // Teleport Service (depends on WorldManagementService)
        TeleportService teleportService = new TeleportService(plugin);
        registerService(TeleportService.class, teleportService);

        // Magical Power Service (depends on SettingsConfig)
        if (plugin.getSettingsConfig().isMagicalPowerEnabled()) {
            MagicalPowerService magicalPowerService = new MagicalPowerService(plugin);
            registerService(MagicalPowerService.class, magicalPowerService);
            plugin.getLogger().info("MagicalPowerService registered.");
        } else {
            plugin.getLogger().info("Magical Power system is disabled in settings. MagicalPowerService not registered.");
        }

        // Bazaar Service (depends on DatabaseConfig and SettingsConfig)
        if (plugin.getSettingsConfig().isBazaarEnabled()) {
            BazaarService bazaarService = new BazaarService(plugin, databaseConfig);
            registerService(BazaarService.class, bazaarService);
            plugin.getLogger().info("BazaarService registered.");
        } else {
            plugin.getLogger().info("Bazaar system is disabled in settings. BazaarService not registered.");
        }

        // Advanced Slayer Manager (depends on PlayerProfileService and SettingsConfig)
        if (plugin.getSettingsConfig().isSlayerEnabled()) {
            AdvancedSlayerManager advancedSlayerManager = new AdvancedSlayerManager(plugin, playerProfileService);
            registerService(AdvancedSlayerManager.class, advancedSlayerManager);
            plugin.getLogger().info("AdvancedSlayerManager registered.");
        } else {
            plugin.getLogger().info("Slayer system is disabled in settings. AdvancedSlayerManager not registered.");
        }

        // Class Manager (for Dungeons)
        ClassManager classManager = new ClassManager(plugin);
        registerService(ClassManager.class, classManager);

        // Dungeon Manager (depends on ClassManager and SettingsConfig)
        if (plugin.getSettingsConfig().isDungeonsEnabled()) {
            de.noctivag.skyblock.dungeons.DungeonManager dungeonManager = new de.noctivag.skyblock.dungeons.DungeonManager(plugin);
            registerService(de.noctivag.skyblock.dungeons.DungeonManager.class, dungeonManager);
            plugin.getLogger().info("DungeonManager registered.");
        } else {
            plugin.getLogger().info("Dungeons system is disabled in settings. DungeonManager not registered.");
        }

                // Loot Service (depends on SettingsConfig)
                if (plugin.getSettingsConfig().isCustomMobsEnabled()) {
                    LootService lootService = new LootService(plugin);
                    registerService(LootService.class, lootService);
                    plugin.getLogger().info("LootService registered.");
                } else {
                    plugin.getLogger().info("Custom Mobs system is disabled in settings. LootService not registered.");
                }

                // Custom Item Service
                CustomItemService customItemService = new CustomItemService(plugin);
                registerService(CustomItemService.class, customItemService);
                plugin.getLogger().info("CustomItemService registered.");

                // Zone Mob Service
                ZoneMobService zoneMobService = new ZoneMobService(plugin);
                registerService(ZoneMobService.class, zoneMobService);
                plugin.getLogger().info("ZoneMobService registered.");

                // Minion Manager
                MinionManager minionManager = new MinionManager(plugin);
                registerService(MinionManager.class, minionManager);
                plugin.getLogger().info("MinionManager registered.");

                plugin.getLogger().info("All services registered.");
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
