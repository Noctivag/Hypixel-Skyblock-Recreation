package de.noctivag.skyblock.network;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import net.kyori.adventure.text.Component;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Network Manager - Haupt-Manager für das Multi-Server-Netzwerk
 *
 * Verantwortlich für:
 * - Initialisierung aller Netzwerk-Komponenten
 * - Koordination zwischen verschiedenen Systemen
 * - Event-Handling
 * - Server-Lifecycle-Management
 */
public class NetworkManager implements Listener {

    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final NetworkArchitecture.NetworkConfig config;
    private final NetworkArchitecture.ServerType serverType;

    private ServerManager serverManager;
    private NetworkCommunication networkCommunication;
    private IslandServerManager islandServerManager;
    private PlayerTransferSystem playerTransferSystem;
    private DataSynchronization dataSynchronization;

    private JedisPool jedisPool;
    private boolean isInitialized = false;

    public NetworkManager(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager,
                         NetworkArchitecture.NetworkConfig config, NetworkArchitecture.ServerType serverType) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.config = config;
        this.serverType = serverType;

        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void initialize() {
        try {
            plugin.getLogger().info("Initializing Network Manager...");

            // Initialize Redis connection
            initializeRedis();

            // Initialize core components
            initializeServerManager();
            initializeNetworkCommunication();
            initializeIslandServerManager();
            initializePlayerTransferSystem();
            initializeDataSynchronization();

            // Connect to network
            connectToNetwork();

            isInitialized = true;
            plugin.getLogger().info("Network Manager initialized successfully!");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize Network Manager: " + e.getMessage());
            plugin.getLogger().log(Level.SEVERE, "Exception during NetworkManager.initialize", e);
        }
    }

    public void shutdown() {
        try {
            plugin.getLogger().info("Shutting down Network Manager...");

            if (isInitialized) {
                // Disconnect from network
                disconnectFromNetwork();

                // Shutdown components
                shutdownDataSynchronization();
                shutdownPlayerTransferSystem();
                shutdownIslandServerManager();
                shutdownNetworkCommunication();
                shutdownServerManager();

                // Close Redis connection
                closeRedis();
            }

            isInitialized = false;
            plugin.getLogger().info("Network Manager shutdown complete!");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to shutdown Network Manager: " + e.getMessage());
            plugin.getLogger().log(Level.SEVERE, "Exception during NetworkManager.shutdown", e);
        }
    }

    private void initializeRedis() {
        try {
            // Redis connection is already initialized in ServerManager
            // We'll get the pool from there
            plugin.getLogger().info("Redis connection initialized");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize Redis: " + e.getMessage());
            throw e;
        }
    }

    private void initializeServerManager() {
        try {
            serverManager = new ServerManager(plugin, databaseManager, config, serverType);
            jedisPool = serverManager.getJedisPool(); // Get the pool for other components
            plugin.getLogger().info("Server Manager initialized");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize Server Manager: " + e.getMessage());
            throw e;
        }
    }

    private void initializeNetworkCommunication() {
        try {
            networkCommunication = new NetworkCommunication(plugin, jedisPool, serverManager.getServerId());
            plugin.getLogger().info("Network Communication initialized");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize Network Communication: " + e.getMessage());
            throw e;
        }
    }

    private void initializeIslandServerManager() {
        try {
            islandServerManager = new IslandServerManager(
                plugin, databaseManager, serverManager, jedisPool,
                config.getMaxIslandsPerServer(), 100, 200 // island size and spacing
            );
            plugin.getLogger().info("Island Server Manager initialized");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize Island Server Manager: " + e.getMessage());
            throw e;
        }
    }

    private void initializePlayerTransferSystem() {
        try {
            playerTransferSystem = new PlayerTransferSystem(
                plugin, databaseManager, serverManager, networkCommunication, jedisPool,
                10, 30000 // max concurrent transfers, transfer timeout
            );
            plugin.getLogger().info("Player Transfer System initialized");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize Player Transfer System: " + e.getMessage());
            throw e;
        }
    }

    private void initializeDataSynchronization() {
        try {
            dataSynchronization = new DataSynchronization(
                plugin, databaseManager, networkCommunication, jedisPool,
                10000, 30000 // sync interval, conflict resolution timeout
            );
            plugin.getLogger().info("Data Synchronization initialized");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize Data Synchronization: " + e.getMessage());
            throw e;
        }
    }

    private void connectToNetwork() {
        try {
            serverManager.connect();
            plugin.getLogger().info("Connected to network");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to connect to network: " + e.getMessage());
            throw e;
        }
    }

    private void disconnectFromNetwork() {
        try {
            serverManager.disconnect();
            plugin.getLogger().info("Disconnected from network");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to disconnect from network: " + e.getMessage());
        }
    }

    private void shutdownDataSynchronization() {
        try {
            // DataSynchronization doesn't need explicit shutdown
            plugin.getLogger().info("Data Synchronization shutdown");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to shutdown Data Synchronization: " + e.getMessage());
        }
    }

    private void shutdownPlayerTransferSystem() {
        try {
            // PlayerTransferSystem doesn't need explicit shutdown
            plugin.getLogger().info("Player Transfer System shutdown");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to shutdown Player Transfer System: " + e.getMessage());
        }
    }

    private void shutdownIslandServerManager() {
        try {
            // IslandServerManager doesn't need explicit shutdown
            plugin.getLogger().info("Island Server Manager shutdown");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to shutdown Island Server Manager: " + e.getMessage());
        }
    }

    private void shutdownNetworkCommunication() {
        try {
            networkCommunication.stopListening();
            plugin.getLogger().info("Network Communication shutdown");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to shutdown Network Communication: " + e.getMessage());
        }
    }

    private void shutdownServerManager() {
        try {
            // ServerManager shutdown is handled in disconnectFromNetwork
            plugin.getLogger().info("Server Manager shutdown");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to shutdown Server Manager: " + e.getMessage());
        }
    }

    private void closeRedis() {
        try {
            if (jedisPool != null && !jedisPool.isClosed()) {
                jedisPool.close();
            }
            plugin.getLogger().info("Redis connection closed");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to close Redis connection: " + e.getMessage());
        }
    }

    // Event Handlers
    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        if (event.getType() == ServerLoadEvent.LoadType.STARTUP) {
            // Server is starting up
            plugin.getLogger().info("Server startup detected");
        }
    }

    // ServerShutdownEvent is not available in newer Bukkit versions
    // @EventHandler
    // public void onServerShutdown(ServerShutdownEvent event) {
    //     // Server is shutting down
    //     plugin.getLogger().info("Server shutdown detected");
    //     shutdown();
    // }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (isInitialized) {
            Player player = event.getPlayer();

            // Notify other servers about player join
            networkCommunication.notifyPlayerJoin(player.getUniqueId(), serverManager.getServerId());

            // Sync player data
            dataSynchronization.syncPlayerData(player.getUniqueId());

            plugin.getLogger().info("Player " + player.getName() + " joined server " + serverManager.getServerId());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isInitialized) {
            Player player = event.getPlayer();

            // Notify other servers about player leave
            networkCommunication.notifyPlayerLeave(player.getUniqueId(), serverManager.getServerId());

            plugin.getLogger().info("Player " + player.getName() + " left server " + serverManager.getServerId());
        }
    }

    // Public API Methods
    public CompletableFuture<Boolean> transferPlayer(Player player, NetworkArchitecture.ServerType targetServerType,
                                                    NetworkArchitecture.TransferReason reason) {
        if (!isInitialized) {
            return CompletableFuture.completedFuture(false);
        }

        return playerTransferSystem.transferPlayer(player, targetServerType, reason);
    }

    public boolean createIsland(Player player, NetworkArchitecture.IslandType islandType) {
        if (!isInitialized) {
            return false;
        }

        return islandServerManager.createIsland(player, islandType);
    }

    public boolean transferPlayerToIsland(Player player, java.util.UUID islandOwnerId) {
        if (!isInitialized) {
            return false;
        }

        return islandServerManager.transferPlayerToIsland(player, islandOwnerId);
    }

    public void syncPlayerData(java.util.UUID playerId) {
        if (isInitialized) {
            dataSynchronization.syncPlayerData(playerId);
        }
    }

    public void syncIslandData(String islandId) {
        if (isInitialized) {
            dataSynchronization.syncIslandData(islandId);
        }
    }

    public void syncCollectionData(String collectionId) {
        if (isInitialized) {
            dataSynchronization.syncCollectionData(collectionId);
        }
    }

    public void syncMinionData(String minionId) {
        if (isInitialized) {
            dataSynchronization.syncMinionData(minionId);
        }
    }

    // Getters
    public ServerManager getServerManager() {
        return serverManager;
    }

    public NetworkCommunication getNetworkCommunication() {
        return networkCommunication;
    }

    public IslandServerManager getIslandServerManager() {
        return islandServerManager;
    }

    public PlayerTransferSystem getPlayerTransferSystem() {
        return playerTransferSystem;
    }

    public DataSynchronization getDataSynchronization() {
        return dataSynchronization;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public NetworkArchitecture.ServerType getServerType() {
        return serverType;
    }

    public String getServerId() {
        return serverManager != null ? serverManager.getServerId() : null;
    }

    // Utility Methods
    public void sendMessageToPlayer(Player player, String message) {
        player.sendMessage(Component.text(message));
    }

    public void broadcastToServer(String message) {
        Bukkit.broadcast(Component.text(message));
    }

    public void logInfo(String message) {
        plugin.getLogger().info(message);
    }

    public void logWarning(String message) {
        plugin.getLogger().warning(message);
    }

    public void logError(String message) {
        plugin.getLogger().severe(message);
    }
}
