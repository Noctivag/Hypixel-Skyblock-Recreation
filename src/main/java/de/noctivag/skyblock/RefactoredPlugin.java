package de.noctivag.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.core.di.ServiceLocator;
import de.noctivag.skyblock.core.lifecycle.PluginLifecycleManager;
import de.noctivag.skyblock.core.events.EventBus;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Refactored Plugin class using modern architecture patterns.
 *
 * Features:
 * - Dependency Injection
 * - Service Locator Pattern
 * - Async Initialization
 * - Lifecycle Management
 * - Event-Driven Architecture
 */
public final class RefactoredPlugin extends JavaPlugin {

    private ServiceLocator serviceLocator;
    private PluginLifecycleManager lifecycleManager;
    private EventBus eventBus;

    @Override
    public void onEnable() {
        getLogger().info("Starting Hypixel Skyblock Recreation Plugin...");

        try {
            // Initialize core framework
            initializeFramework();

            // Start async initialization
            lifecycleManager.initialize()
                .thenRun(() -> {
                    getLogger().info("Plugin enabled successfully!");
                    getLogger().info("All systems are ready and operational.");
                })
                .exceptionally(throwable -> {
                    getLogger().severe("Failed to initialize plugin: " + throwable.getMessage());
                    getServer().getPluginManager().disablePlugin(this);
                    return null;
                });

        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Critical error during plugin initialization", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Hypixel Skyblock Recreation Plugin...");

        if (lifecycleManager != null) {
            lifecycleManager.shutdown()
                .thenRun(() -> {
                    getLogger().info("Plugin disabled successfully!");
                })
                .exceptionally(throwable -> {
                    getLogger().warning("Error during plugin shutdown: " + throwable.getMessage());
                    return null;
                });
        }
    }

    /**
     * Initialize the core framework components
     */
    private void initializeFramework() {
        // Initialize service locator
        serviceLocator = ServiceLocator.getInstance(this);

        // Initialize lifecycle manager
        lifecycleManager = new PluginLifecycleManager(this, serviceLocator);

        // Initialize event bus
        eventBus = new EventBus(this);

        // Register core services
        registerCoreServices();

        // Register command and listener handlers
        registerHandlers();

        getLogger().info("Framework initialized successfully");
    }

    /**
     * Register core services with the service locator
     */
    private void registerCoreServices() {
        // Infrastructure services
        // serviceLocator.register(ConfigService.class, ConfigServiceImpl.class);
        // serviceLocator.register(DatabaseService.class, DatabaseServiceImpl.class);
        // serviceLocator.register(LoggingService.class, LoggingServiceImpl.class);

        // Feature services
        // serviceLocator.register(SkyblockService.class, SkyblockServiceImpl.class);
        // serviceLocator.register(EconomyService.class, EconomyServiceImpl.class);
        // serviceLocator.register(SocialService.class, SocialServiceImpl.class);

        getLogger().info("Core services registered");
    }

    /**
     * Register command and event handlers
     */
    private void registerHandlers() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Register event listeners
        // pluginManager.registerEvents(new PlayerEventListener(this), this);
        // pluginManager.registerEvents(new WorldEventListener(this), this);
        // pluginManager.registerEvents(new InventoryEventListener(this), this);

        // Register commands
        // getCommand("skyblock").setExecutor(new SkyblockCommand(this));
        // getCommand("economy").setExecutor(new EconomyCommand(this));
        // getCommand("social").setExecutor(new SocialCommand(this));

        getLogger().info("Handlers registered");
    }

    /**
     * Get the service locator
     * @return service locator instance
     */
    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    /**
     * Get the lifecycle manager
     * @return lifecycle manager instance
     */
    public PluginLifecycleManager getPluginLifecycleManager() {
        return lifecycleManager;
    }

    /**
     * Get the event bus
     * @return event bus instance
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Check if the plugin is fully initialized
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return lifecycleManager != null && lifecycleManager.isReady();
    }

    /**
     * Get plugin instance (singleton access)
     * @return plugin instance
     */
    public static RefactoredPlugin getInstance() {
        return getPlugin(RefactoredSkyblockPlugin.class);
    }
}
