package de.noctivag.skyblock;

import de.noctivag.skyblock.config.SettingsConfig;
import de.noctivag.skyblock.config.DatabaseConfig;
import de.noctivag.skyblock.services.ServiceManager;
import de.noctivag.skyblock.services.PlayerProfileService;
import de.noctivag.skyblock.services.WorldResetService;
import de.noctivag.skyblock.services.WorldManagementService;
import de.noctivag.skyblock.services.TeleportService;
import de.noctivag.skyblock.worlds.RollingRestartWorldManager;
import de.noctivag.skyblock.systems.HubSpawnSystem;
import de.noctivag.skyblock.commands.HubCommand;
import de.noctivag.skyblock.commands.MiningCommand;
import de.noctivag.skyblock.commands.AdvancedCommandSystem;
import de.noctivag.skyblock.performance.AdvancedPerformanceManager;
import de.noctivag.skyblock.data.DatabaseManager;
import de.noctivag.skyblock.listeners.PlayerListener;
import de.noctivag.skyblock.events.SeaCreatureEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Refaktorierte Haupt-Plugin-Klasse mit Service-orientierter Architektur
 * Zentrale Konfiguration und Dependency Injection
 */
public class SkyblockPluginRefactored extends JavaPlugin {
    
    // Statische Instanz für einfachen Zugriff
    private static SkyblockPluginRefactored instance;
    
    // Konfigurations-Manager
    private SettingsConfig settingsConfig;
    private DatabaseConfig databaseConfig;
    
    // Service-Manager
    private ServiceManager serviceManager;
    
    // Services
    private PlayerProfileService playerProfileService;
    private WorldResetService worldResetService;
    private WorldManagementService worldManagementService;
    private TeleportService teleportService;
    
    // Legacy-Systeme (werden schrittweise durch Services ersetzt)
    private RollingRestartWorldManager rollingRestartWorldManager;
    private HubSpawnSystem hubSpawnSystem;
    private DatabaseManager databaseManager;
    private AdvancedPerformanceManager performanceManager;
    
    // Commands
    private HubCommand hubCommand;
    private MiningCommand miningCommand;
    private AdvancedCommandSystem commandSystem;
    
    // Listeners
    private PlayerListener playerListener;
    private SeaCreatureEvent seaCreatureEvent;
    
    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Enabling Skyblock v" + getDescription().getVersion());
        
        try {
            // 1. Konfiguration laden
            loadConfigurations();
            
            // 2. Service-Manager initialisieren
            initializeServiceManager();
            
            // 3. Services registrieren
            registerServices();
            
            // 4. Legacy-Systeme initialisieren
            initializeLegacySystems();
            
            // 5. Commands registrieren
            registerCommands();
            
            // 6. Listeners registrieren
            registerListeners();
            
            // 7. Finale Initialisierung
            finalizeInitialization();
            
            getLogger().info("SkyblockPlugin successfully enabled with refactored architecture!");
            
        } catch (Exception e) {
            getLogger().severe("Failed to enable SkyblockPlugin: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }
    
    @Override
    public void onDisable() {
        getLogger().info("Disabling SkyblockPlugin...");
        
        try {
            // Services herunterfahren
            if (serviceManager != null) {
                serviceManager.shutdown();
            }
            
            // Legacy-Systeme herunterfahren
            if (rollingRestartWorldManager != null) {
                rollingRestartWorldManager.cancelAllTasks();
            }
            
            if (databaseManager != null) {
                databaseManager.closeConnection();
            }
            
            getLogger().info("SkyblockPlugin successfully disabled!");
            
        } catch (Exception e) {
            getLogger().severe("Error during plugin shutdown: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static SkyblockPluginRefactored getInstance() {
        return instance;
    }
    
    public SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }
    
    public boolean isFoliaServer() {
        return getServer().getName().contains("Folia");
    }
    
    /**
     * Lädt alle Konfigurationen
     */
    private void loadConfigurations() {
        getLogger().info("Loading configurations...");
        
        // Standard-Konfiguration laden
        saveDefaultConfig();
        
        // Settings-Konfiguration
        settingsConfig = new SettingsConfig(this);
        settingsConfig.load();
        
        // Database-Konfiguration
        databaseConfig = new DatabaseConfig(this);
        databaseConfig.load();
        
        getLogger().info("Configurations loaded successfully");
    }
    
    /**
     * Initialisiert den Service-Manager
     */
    private void initializeServiceManager() {
        getLogger().info("Initializing ServiceManager...");
        
        serviceManager = new ServiceManager(this);
        
        getLogger().info("ServiceManager initialized");
    }
    
    /**
     * Registriert alle Services
     */
    private void registerServices() {
        getLogger().info("Registering services...");
        
        // PlayerProfileService
        playerProfileService = new PlayerProfileService(this);
        serviceManager.registerService(PlayerProfileService.class, playerProfileService);
        
        // WorldResetService (wird nach RollingRestartWorldManager initialisiert)
        // TeleportService (wird nach WorldResetService initialisiert)
        
        getLogger().info("Services registered successfully");
    }
    
    /**
     * Initialisiert Legacy-Systeme
     */
    private void initializeLegacySystems() {
        getLogger().info("Initializing legacy systems...");
        
        // DatabaseManager
        databaseManager = new DatabaseManager(this);
        if (databaseConfig.isEnabled()) {
            databaseManager.initializeConnection();
        } else {
            getLogger().info("Database is disabled in configuration - Using file-based storage");
        }
        
        // PerformanceManager
        performanceManager = new AdvancedPerformanceManager(this);
        performanceManager.initialize();
        
        // RollingRestartWorldManager
        rollingRestartWorldManager = new RollingRestartWorldManager(this);
        rollingRestartWorldManager.initializeRollingRestartSystem();
        
        // WorldResetService (nach RollingRestartWorldManager)
        worldResetService = new WorldResetService(this, rollingRestartWorldManager);
        serviceManager.registerService(WorldResetService.class, worldResetService);
        
        // WorldManagementService (nach RollingRestartWorldManager)
        worldManagementService = new WorldManagementService(this, rollingRestartWorldManager);
        serviceManager.registerService(WorldManagementService.class, worldManagementService);
        
        // TeleportService (nach WorldManagementService)
        teleportService = new TeleportService(this, worldManagementService);
        serviceManager.registerService(TeleportService.class, teleportService);
        
        // HubSpawnSystem
        if (settingsConfig.isHubSpawnSystemEnabled()) {
            hubSpawnSystem = new HubSpawnSystem(this);
            getLogger().info("Hub-Spawn-System initialized - All players will spawn in Hub");
        }
        
        getLogger().info("Legacy systems initialized successfully");
    }
    
    /**
     * Registriert alle Commands
     */
    private void registerCommands() {
        getLogger().info("Registering commands...");
        
        // HubCommand
        hubCommand = new HubCommand(this);
        getCommand("hub").setExecutor(hubCommand);
        
        // MiningCommand
        miningCommand = new MiningCommand(this);
        getCommand("mining").setExecutor(miningCommand);
        
        // AdvancedCommandSystem
        commandSystem = new AdvancedCommandSystem(this, databaseManager);
        getCommand("vault").setExecutor(commandSystem);
        getCommand("enchant").setExecutor(commandSystem);
        getCommand("reforge").setExecutor(commandSystem);
        getCommand("boss").setExecutor(commandSystem);
        getCommand("island").setExecutor(commandSystem);
        
        getLogger().info("Commands registered successfully");
    }
    
    /**
     * Registriert alle Listeners
     */
    private void registerListeners() {
        getLogger().info("Registering listeners...");
        
        // PlayerListener
        playerListener = new PlayerListener(this);
        getServer().getPluginManager().registerEvents(playerListener, this);
        
        // SeaCreatureEvent (if custom mobs are enabled)
        if (settingsConfig.isCustomMobsEnabled()) {
            seaCreatureEvent = new SeaCreatureEvent(this);
            getLogger().info("SeaCreatureEvent registered for custom mob spawning");
        }
        
        getLogger().info("Listeners registered successfully");
    }
    
    /**
     * Finalisiert die Initialisierung
     */
    private void finalizeInitialization() {
        getLogger().info("Finalizing initialization...");
        
        // Starte Rolling-Restart-System
        if (settingsConfig.isRollingRestartSystemEnabled()) {
            worldResetService.startRollingRestartSystem();
        }
        
        // Logge Service-Statistiken
        if (settingsConfig.isVerboseLogging()) {
            getLogger().info("=== Service Statistics ===");
            getLogger().info(serviceManager.getServiceStats());
            getLogger().info(playerProfileService.getCacheStats());
            getLogger().info(worldResetService.getServiceStats());
            getLogger().info(teleportService.getServiceStats());
            getLogger().info("=========================");
        }
        
        getLogger().info("Initialization completed successfully!");
    }
    
    // Getter-Methoden für Dependency Injection
    
    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }
    
    public ServiceManager getServiceManager() {
        return serviceManager;
    }
    
    public PlayerProfileService getPlayerProfileService() {
        return playerProfileService;
    }
    
    public WorldResetService getWorldResetService() {
        return worldResetService;
    }
    
    public TeleportService getTeleportService() {
        return teleportService;
    }
    
    public RollingRestartWorldManager getRollingRestartWorldManager() {
        return rollingRestartWorldManager;
    }
    
    public HubSpawnSystem getHubSpawnSystem() {
        return hubSpawnSystem;
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public AdvancedPerformanceManager getPerformanceManager() {
        return performanceManager;
    }
    
    // Utility-Methoden
    
    /**
     * Gibt Plugin-Statistiken zurück
     * @return Plugin-Statistiken als String
     */
    public String getPluginStats() {
        return String.format("SkyblockPlugin Stats - Services: %d, Config Loaded: %s, Database: %s",
                           serviceManager.getServiceCount(),
                           settingsConfig != null,
                           databaseConfig != null && databaseConfig.isEnabled());
    }
    
    /**
     * Prüft ob das Plugin vollständig initialisiert ist
     * @return true wenn initialisiert
     */
    public boolean isFullyInitialized() {
        return settingsConfig != null && 
               serviceManager != null && 
               playerProfileService != null && 
               worldResetService != null && 
               teleportService != null;
    }
}
