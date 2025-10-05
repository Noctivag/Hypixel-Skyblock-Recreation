package de.noctivag.skyblock;

import de.noctivag.skyblock.config.SettingsConfig;
import de.noctivag.skyblock.config.DatabaseConfig;
import de.noctivag.skyblock.services.ServiceManager;
import de.noctivag.skyblock.services.PlayerProfileService;
import de.noctivag.skyblock.services.WorldManagementService;
import de.noctivag.skyblock.services.TeleportService;
import de.noctivag.skyblock.worlds.RollingRestartWorldManager;
import de.noctivag.skyblock.systems.HubSpawnSystem;
import de.noctivag.skyblock.commands.HubCommand;
import de.noctivag.skyblock.data.DatabaseManager;
import de.noctivag.skyblock.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Minimale, funktionsfähige Version des Skyblock-Plugins
 * Fokussiert auf Kern-Features ohne Legacy-Systeme
 */
public class SkyblockPluginMinimal extends JavaPlugin {

    // Statische Instanz für einfachen Zugriff
    private static SkyblockPluginMinimal instance;

    // Konfigurations-Manager
    private SettingsConfig settingsConfig;
    private DatabaseConfig databaseConfig;

    // Service-Manager
    private ServiceManager serviceManager;

    // Services
    private PlayerProfileService playerProfileService;
    private WorldManagementService worldManagementService;
    private TeleportService teleportService;

    // Kern-Systeme
    private RollingRestartWorldManager rollingRestartWorldManager;
    private HubSpawnSystem hubSpawnSystem;
    private DatabaseManager databaseManager;

    // Commands
    private HubCommand hubCommand;

    // Listeners
    private PlayerListener playerListener;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Enabling Skyblock Minimal v" + getDescription().getVersion());

        try {
            // 1. Konfiguration laden
            loadConfigurations();

            // 2. Datenbank initialisieren
            databaseManager = new DatabaseManager(this, databaseConfig);
            databaseManager.initializeConnection();

            // 3. Rolling Restart World Manager initialisieren
            rollingRestartWorldManager = new RollingRestartWorldManager(this);
            rollingRestartWorldManager.initialize();

            // 4. Service Manager initialisieren und Services registrieren
            serviceManager = new ServiceManager(this, rollingRestartWorldManager, databaseManager);
            serviceManager.registerAllServices();

            // 5. Services holen
            playerProfileService = serviceManager.getService(PlayerProfileService.class);
            worldManagementService = serviceManager.getService(WorldManagementService.class);
            teleportService = serviceManager.getService(TeleportService.class);

            // 6. Hub-Spawn-System initialisieren
            if (settingsConfig.isHubSpawnSystemEnabled()) {
                hubSpawnSystem = new HubSpawnSystem(this);
                getLogger().info("Hub-Spawn-System initialized - All players will spawn in Hub");
            }

            // 7. Commands registrieren
            registerCommands();

            // 8. Event Listener registrieren
            registerListeners();

            getLogger().info("SkyblockPlugin Minimal successfully enabled!");

        } catch (Exception e) {
            getLogger().severe("Error during plugin startup: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        try {
            getLogger().info("Disabling SkyblockPlugin Minimal...");

            // Services herunterfahren
            if (serviceManager != null) {
                serviceManager.shutdownAllServices();
            }

            // Legacy-Systeme herunterfahren
            if (rollingRestartWorldManager != null) {
                rollingRestartWorldManager.cancelAllTasks();
            }

            if (databaseManager != null) {
                databaseManager.closeConnection();
            }

            getLogger().info("SkyblockPlugin Minimal successfully disabled!");

        } catch (Exception e) {
            getLogger().severe("Error during plugin shutdown: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static SkyblockPluginMinimal getInstance() {
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

        // Datenbank-Konfiguration
        databaseConfig = new DatabaseConfig(this);
        databaseConfig.load();
    }

    /**
     * Registriert alle Commands
     */
    private void registerCommands() {
        getLogger().info("Registering commands...");
        
        // Hub Command
        hubCommand = new HubCommand(this);
        getCommand("hub").setExecutor(hubCommand);
    }

    /**
     * Registriert alle Event Listener
     */
    private void registerListeners() {
        getLogger().info("Registering event listeners...");
        
        // Player Listener
        playerListener = new PlayerListener(this);
        getServer().getPluginManager().registerEvents(playerListener, this);
    }

    // Getter-Methoden
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public RollingRestartWorldManager getRollingRestartWorldManager() {
        return rollingRestartWorldManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public PlayerProfileService getPlayerProfileService() {
        return playerProfileService;
    }

    public WorldManagementService getWorldManagementService() {
        return worldManagementService;
    }

    public TeleportService getTeleportService() {
        return teleportService;
    }
}
