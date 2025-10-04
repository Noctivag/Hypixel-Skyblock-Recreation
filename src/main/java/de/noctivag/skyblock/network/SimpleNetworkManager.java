package de.noctivag.skyblock.network;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.worlds.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Simple Network Manager - Vereinfachte Version ohne Redis
 *
 * Verantwortlich für:
 * - Grundlegende Server-Verwaltung
 * - Player-Transfer Simulation
 * - Island-Management
 * - Daten-Synchronisation (vereinfacht)
 */
public class SimpleNetworkManager implements Listener {

    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final WorldManager worldManager;
    private final NetworkArchitecture.ServerType serverType;

    private final Map<UUID, String> playerServerMap = new ConcurrentHashMap<>();
    private final Map<UUID, IslandInfo> playerIslands = new ConcurrentHashMap<>();
    private final Map<String, List<IslandInfo>> islandsByServer = new ConcurrentHashMap<>();

    private final String serverId;
    private boolean isInitialized = false;

    public SimpleNetworkManager(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager,
                               NetworkArchitecture.ServerType serverType) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.worldManager = (de.noctivag.plugin.worlds.WorldManager) plugin.getSimpleWorldManager();
        this.serverType = serverType;
        this.serverId = generateServerId();

        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void initialize() {
        try {
            plugin.getLogger().info("Initializing Simple Network Manager...");

            // Initialize server
            initializeServer();

            isInitialized = true;
            plugin.getLogger().info("Simple Network Manager initialized successfully!");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initialize Simple Network Manager: " + e.getMessage());
            plugin.getLogger().log(Level.SEVERE, "Exception during SimpleNetworkManager.initialize", e);
        }
    }

    public void shutdown() {
        try {
            plugin.getLogger().info("Shutting down Simple Network Manager...");

            if (isInitialized) {
                // Cleanup
                playerServerMap.clear();
                playerIslands.clear();
                islandsByServer.clear();
            }

            isInitialized = false;
            plugin.getLogger().info("Simple Network Manager shutdown complete!");

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to shutdown Simple Network Manager: " + e.getMessage());
            plugin.getLogger().log(Level.SEVERE, "Exception during SimpleNetworkManager.shutdown", e);
        }
    }

    private void initializeServer() {
        plugin.getLogger().info("Server initialized as " + serverId + " (" + serverType.getName() + ")");
    }

    private String generateServerId() {
        return serverType.getName().toLowerCase() + "_" + System.currentTimeMillis() + "_" +
               (int)(Math.random() * 1000);
    }

    // Event Handlers
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (isInitialized) {
            Player player = event.getPlayer();
            playerServerMap.put(player.getUniqueId(), serverId);
            plugin.getLogger().info("Player " + player.getName() + " joined server " + serverId);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (isInitialized) {
            Player player = event.getPlayer();
            playerServerMap.remove(player.getUniqueId());
            plugin.getLogger().info("Player " + player.getName() + " left server " + serverId);
        }
    }

    // Public API Methods
    public CompletableFuture<Boolean> transferPlayer(Player player, NetworkArchitecture.ServerType targetServerType,
                                                    NetworkArchitecture.TransferReason reason) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        if (!isInitialized) {
            future.complete(false);
            return future;
        }

        // Simulate transfer
        player.sendMessage(Component.text("§aTransferring to " + targetServerType.getName() + "..."));

        // Simulate delay - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(20L * 50); // 1 second = 1,000 ms
                player.sendMessage(Component.text("§aTransfer successful! (Simulated)"));
                future.complete(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.complete(false);
            }
        });

        return future;
    }

    public boolean createIsland(Player player, NetworkArchitecture.IslandType islandType) {
        if (!isInitialized) {
            return false;
        }

        // Check if player already has an island
        if (playerIslands.containsKey(player.getUniqueId())) {
            player.sendMessage(Component.text("§cYou already have an island!"));
            return false;
        }

        // Get appropriate world for island type
        String worldName = getWorldNameForIslandType(islandType);
        org.bukkit.World world = worldManager.getWorld(worldName);
        if (world == null) {
            plugin.getLogger().warning("World " + worldName + " not available, using fallback");
            world = Bukkit.getWorlds().isEmpty() ? null : Bukkit.getWorlds().get(0);
        }

        if (world == null) {
            plugin.getLogger().severe("No worlds available for island creation!");
            return false;
        }

        // Create island
        IslandInfo island = new IslandInfo(
            generateIslandId(),
            player.getUniqueId(),
            islandType,
            serverId,
            worldManager.getSafeSpawnLocation(worldName),
            100,
            System.currentTimeMillis(),
            new ArrayList<>(),
            new HashMap<>(),
            true
        );

        // Store island
        playerIslands.put(player.getUniqueId(), island);
        islandsByServer.computeIfAbsent(serverId, k -> new ArrayList<>()).add(island);

        player.sendMessage(Component.text("§aIsland created successfully!"));
        return true;
    }

    public boolean transferPlayerToIsland(Player player, UUID islandOwnerId) {
        if (!isInitialized) {
            return false;
        }

        IslandInfo island = playerIslands.get(islandOwnerId);
        if (island == null) {
            player.sendMessage(Component.text("§cIsland not found!"));
            return false;
        }

        // Check access
        if (!hasAccessToIsland(player.getUniqueId(), island)) {
            player.sendMessage(Component.text("§cYou don't have access to this island!"));
            return false;
        }

        // Transfer player
        return transferPlayer(player, NetworkArchitecture.ServerType.ISLAND,
                             NetworkArchitecture.TransferReason.ISLAND_ACCESS).join();
    }

    private boolean hasAccessToIsland(UUID playerId, IslandInfo island) {
        // Check if player is owner
        if (island.getOwnerId().equals(playerId)) {
            return true;
        }

        // Check if player is in members list
        if (island.getMembers().contains(playerId)) {
            return true;
        }

        // Check if island is public
        if (island.getType() == NetworkArchitecture.IslandType.PUBLIC) {
            return true;
        }

        return false;
    }

    public boolean addIslandMember(UUID islandOwnerId, UUID memberId) {
        IslandInfo island = playerIslands.get(islandOwnerId);
        if (island == null) {
            return false;
        }

        if (!island.getMembers().contains(memberId)) {
            island.getMembers().add(memberId);
            return true;
        }

        return false;
    }

    public boolean removeIslandMember(UUID islandOwnerId, UUID memberId) {
        IslandInfo island = playerIslands.get(islandOwnerId);
        if (island == null) {
            return false;
        }

        if (island.getMembers().contains(memberId)) {
            island.getMembers().remove(memberId);
            return true;
        }

        return false;
    }

    public void syncPlayerData(UUID playerId) {
        if (isInitialized) {
            plugin.getLogger().info("Syncing player data for: " + playerId);
        }
    }

    public void syncIslandData(String islandId) {
        if (isInitialized) {
            plugin.getLogger().info("Syncing island data for: " + islandId);
        }
    }

    public void syncCollectionData(String collectionId) {
        if (isInitialized) {
            plugin.getLogger().info("Syncing collection data for: " + collectionId);
        }
    }

    public void syncMinionData(String minionId) {
        if (isInitialized) {
            plugin.getLogger().info("Syncing minion data for: " + minionId);
        }
    }

    // Getters
    public boolean isInitialized() {
        return isInitialized;
    }

    public NetworkArchitecture.ServerType getServerType() {
        return serverType;
    }

    public String getServerId() {
        return serverId;
    }

    public IslandInfo getPlayerIsland(UUID playerId) {
        return playerIslands.get(playerId);
    }

    public List<IslandInfo> getIslandsByServer(String serverId) {
        return new ArrayList<>(islandsByServer.getOrDefault(serverId, new ArrayList<>()));
    }

    public Map<UUID, String> getPlayerServerMap() {
        return new HashMap<>(playerServerMap);
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

    private String generateIslandId() {
        return "island_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
    }

    /**
     * Gibt den passenden Welt-Namen für einen Insel-Typ zurück
     */
    private String getWorldNameForIslandType(NetworkArchitecture.IslandType islandType) {
        switch (islandType) {
            case PRIVATE:
                return "skyblock_private";
            case PUBLIC:
                return "skyblock_public";
            case GUILD:
                return "skyblock_private"; // Verwende private Welt für Gilden
            case COOP:
                return "skyblock_private"; // Verwende private Welt für Coop
            case EVENT:
                return "event_arenas";
            case DUNGEON:
                return "skyblock_dungeons";
            default:
                return "skyblock_private";
        }
    }

    // Island Info Class
    public static class IslandInfo {
        private final String id;
        private final UUID ownerId;
        private final NetworkArchitecture.IslandType type;
        private final String serverId;
        private final org.bukkit.Location spawnLocation;
        private final int size;
        private final long createdAt;
        private final List<UUID> members;
        private final Map<String, Object> settings;
        private final boolean isActive;

        public IslandInfo(String id, UUID ownerId, NetworkArchitecture.IslandType type,
                         String serverId, org.bukkit.Location spawnLocation, int size, long createdAt,
                         List<UUID> members, Map<String, Object> settings, boolean isActive) {
            this.id = id;
            this.ownerId = ownerId;
            this.type = type;
            this.serverId = serverId;
            this.spawnLocation = spawnLocation;
            this.size = size;
            this.createdAt = createdAt;
            this.members = members;
            this.settings = settings;
            this.isActive = isActive;
        }

        // Getters
        public String getId() { return id; }
        public UUID getOwnerId() { return ownerId; }
        public NetworkArchitecture.IslandType getType() { return type; }
        public String getServerId() { return serverId; }
        public org.bukkit.Location getSpawnLocation() { return spawnLocation; }
        public int getSize() { return size; }
        public long getCreatedAt() { return createdAt; }
        public List<UUID> getMembers() { return members; }
        public Map<String, Object> getSettings() { return settings; }
        public boolean isActive() { return isActive; }

        public void setSetting(String key, Object value) {
            settings.put(key, value);
        }

        public Object getSetting(String key) {
            return settings.get(key);
        }
    }
}
