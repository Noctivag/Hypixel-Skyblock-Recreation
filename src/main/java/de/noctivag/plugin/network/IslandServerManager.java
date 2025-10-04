package de.noctivag.plugin.network;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Island Server Manager - Verwaltet Inseln auf verschiedenen Servern
 *
 * Verantwortlich für:
 * - Insel-Erstellung und -Verwaltung
 * - Insel-Zuordnung zu Servern
 * - Insel-Load-Balancing
 * - Insel-Daten-Synchronisation
 * - Insel-Zugriffskontrolle
 */
public class IslandServerManager {

    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final ServerManager serverManager;
    private final JedisPool jedisPool;

    private final Map<UUID, IslandInfo> playerIslands = new ConcurrentHashMap<>();
    private final Map<String, List<IslandInfo>> islandsByServer = new ConcurrentHashMap<>();
    private final Map<NetworkArchitecture.IslandType, List<IslandInfo>> islandsByType = new ConcurrentHashMap<>();

    private final int maxIslandsPerServer;
    private final int islandSize;
    private final int islandSpacing;

    public IslandServerManager(Plugin plugin, MultiServerDatabaseManager databaseManager,
                              ServerManager serverManager, JedisPool jedisPool,
                              int maxIslandsPerServer, int islandSize, int islandSpacing) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.serverManager = serverManager;
        this.jedisPool = jedisPool;
        this.maxIslandsPerServer = maxIslandsPerServer;
        this.islandSize = islandSize;
        this.islandSpacing = islandSpacing;

        initializeIslandTypes();
        startIslandSync();
    }

    private void initializeIslandTypes() {
        for (NetworkArchitecture.IslandType type : NetworkArchitecture.IslandType.values()) {
            islandsByType.put(type, new ArrayList<>());
        }
    }

    public boolean createIsland(Player player, NetworkArchitecture.IslandType islandType) {
        try {
            // Check if player already has an island
            if (playerIslands.containsKey(player.getUniqueId())) {
                player.sendMessage(Component.text("§cYou already have an island!"));
                return false;
            }

            // Find best server for new island
            String targetServer = findBestServerForIsland(islandType);
            if (targetServer == null) {
                player.sendMessage(Component.text("§cNo available server found for island creation"));
                return false;
            }

            // Create island
            IslandInfo island = createIslandInfo(player.getUniqueId(), islandType, targetServer);

            // Store island data
            storeIslandData(island);

            // Update local maps
            playerIslands.put(player.getUniqueId(), island);
            islandsByServer.computeIfAbsent(targetServer, k -> new ArrayList<>()).add(island);
            islandsByType.computeIfAbsent(islandType, k -> new ArrayList<>()).add(island);

            // Notify other servers
            notifyIslandCreation(island);

            player.sendMessage(Component.text("§aIsland created successfully on server " + targetServer));
            return true;

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to create island: " + e.getMessage());
            plugin.getLogger().log(Level.SEVERE, "Exception while creating island for player " + player.getUniqueId(), e);
            return false;
        }
    }

    private String findBestServerForIsland(NetworkArchitecture.IslandType islandType) {
        List<ServerManager.ServerInfo> islandServers = serverManager.getServersByType(NetworkArchitecture.ServerType.ISLAND);

        if (islandServers.isEmpty()) {
            return null;
        }

        // Find server with lowest island count
        return islandServers.stream()
                .filter(ServerManager.ServerInfo::isHealthy)
                .min(Comparator.comparingInt(server -> getIslandCountOnServer(server.getId())))
                .map(ServerManager.ServerInfo::getId)
                .orElse(null);
    }

    private int getIslandCountOnServer(String serverId) {
        return islandsByServer.getOrDefault(serverId, new ArrayList<>()).size();
    }

    private IslandInfo createIslandInfo(UUID ownerId, NetworkArchitecture.IslandType islandType, String serverId) {
        String islandId = generateIslandId();
        Location spawnLocation = calculateIslandSpawn(serverId);

        return new IslandInfo(
            islandId,
            ownerId,
            islandType,
            serverId,
            spawnLocation,
            islandSize,
            System.currentTimeMillis(),
            new ArrayList<>(),
            new HashMap<>(),
            true
        );
    }

    private String generateIslandId() {
        return "island_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
    }

    private Location calculateIslandSpawn(String serverId) {
        // Calculate spawn location based on server and island count
        int islandCount = getIslandCountOnServer(serverId);
        int x = (islandCount % 100) * (islandSize + islandSpacing);
        int z = (islandCount / 100) * (islandSize + islandSpacing);

        World world = Bukkit.getWorld("islands_" + serverId);
        if (world == null) {
            world = Bukkit.getWorlds().get(0); // Fallback to first world
        }

        return new Location(world, x, 100, z);
    }

    private void storeIslandData(IslandInfo island) {
        try (Jedis jedis = jedisPool.getResource()) {
            String islandKey = "island:" + island.getId();

            jedis.hset(islandKey, "id", island.getId());
            jedis.hset(islandKey, "ownerId", island.getOwnerId().toString());
            jedis.hset(islandKey, "type", island.getType().name());
            jedis.hset(islandKey, "serverId", island.getServerId());
            jedis.hset(islandKey, "spawnX", String.valueOf(island.getSpawnLocation().getX()));
            jedis.hset(islandKey, "spawnY", String.valueOf(island.getSpawnLocation().getY()));
            jedis.hset(islandKey, "spawnZ", String.valueOf(island.getSpawnLocation().getZ()));
            jedis.hset(islandKey, "spawnWorld", island.getSpawnLocation().getWorld().getName());
            jedis.hset(islandKey, "size", String.valueOf(island.getSize()));
            jedis.hset(islandKey, "createdAt", String.valueOf(island.getCreatedAt()));
            jedis.hset(islandKey, "isActive", String.valueOf(island.isActive()));

            // Set expiration (island data expires after 7 days of inactivity)
            jedis.expire(islandKey, 604800); // 7 days

            // Add to island lists
            jedis.sadd("islands", island.getId());
            jedis.sadd("islands:" + island.getType().name().toLowerCase(), island.getId());
            jedis.sadd("islands:server:" + island.getServerId(), island.getId());
            jedis.sadd("islands:owner:" + island.getOwnerId(), island.getId());

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to store island data: " + e.getMessage());
        }
    }

    private void notifyIslandCreation(IslandInfo island) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> notification = new HashMap<>();
            notification.put("islandId", island.getId());
            notification.put("ownerId", island.getOwnerId().toString());
            notification.put("type", island.getType().name());
            notification.put("serverId", island.getServerId());
            notification.put("timestamp", String.valueOf(System.currentTimeMillis()));

            jedis.hset("island_created:" + island.getId(), notification);
            jedis.expire("island_created:" + island.getId(), 300); // 5 minutes

            // Notify all servers
            jedis.publish("island_events", "created:" + island.getId());

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to notify island creation: " + e.getMessage());
        }
    }

    public boolean transferPlayerToIsland(Player player, UUID islandOwnerId) {
        try {
            IslandInfo island = playerIslands.get(islandOwnerId);
            if (island == null) {
                player.sendMessage(Component.text("§cIsland not found!"));
                return false;
            }

            // Check if player has access to island
            if (!hasAccessToIsland(player.getUniqueId(), island)) {
                player.sendMessage(Component.text("§cYou don't have access to this island!"));
                return false;
            }

            // Transfer player to island server
            return serverManager.transferPlayer(player, NetworkArchitecture.ServerType.ISLAND,
                                               NetworkArchitecture.TransferReason.ISLAND_ACCESS);

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to transfer player to island: " + e.getMessage());
            return false;
        }
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
        try {
            IslandInfo island = playerIslands.get(islandOwnerId);
            if (island == null) {
                return false;
            }

            if (!island.getMembers().contains(memberId)) {
                island.getMembers().add(memberId);
                updateIslandData(island);
                return true;
            }

            return false;

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to add island member: " + e.getMessage());
            return false;
        }
    }

    public boolean removeIslandMember(UUID islandOwnerId, UUID memberId) {
        try {
            IslandInfo island = playerIslands.get(islandOwnerId);
            if (island == null) {
                return false;
            }

            if (island.getMembers().contains(memberId)) {
                island.getMembers().remove(memberId);
                updateIslandData(island);
                return true;
            }

            return false;

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to remove island member: " + e.getMessage());
            return false;
        }
    }

    private void updateIslandData(IslandInfo island) {
        try (Jedis jedis = jedisPool.getResource()) {
            String islandKey = "island:" + island.getId();

            // Update members list
            jedis.del(islandKey + ":members");
            for (UUID memberId : island.getMembers()) {
                jedis.sadd(islandKey + ":members", memberId.toString());
            }

            // Update settings
            for (Map.Entry<String, Object> entry : island.getSettings().entrySet()) {
                jedis.hset(islandKey + ":settings", entry.getKey(), entry.getValue().toString());
            }

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to update island data: " + e.getMessage());
        }
    }

    private void startIslandSync() {
        new BukkitRunnable() {
            @Override
            public void run() {
                syncIslandsFromNetwork();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 200L); // Every 10 seconds
    }

    private void syncIslandsFromNetwork() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> islandIds = jedis.smembers("islands");

            for (String islandId : islandIds) {
                String islandKey = "island:" + islandId;
                Map<String, String> islandData = jedis.hgetAll(islandKey);

                if (!islandData.isEmpty()) {
                    IslandInfo island = parseIslandInfo(islandData);
                    if (island != null) {
                        // Update local island data
                        playerIslands.put(island.getOwnerId(), island);
                        islandsByServer.computeIfAbsent(island.getServerId(), k -> new ArrayList<>()).add(island);
                        islandsByType.get(island.getType()).add(island);
                    }
                }
            }

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to sync islands from network: " + e.getMessage());
        }
    }

    private IslandInfo parseIslandInfo(Map<String, String> data) {
        try {
            String islandId = data.get("id");
            UUID ownerId = UUID.fromString(data.get("ownerId"));
            NetworkArchitecture.IslandType type = NetworkArchitecture.IslandType.valueOf(data.get("type"));
            String serverId = data.get("serverId");

            double x = Double.parseDouble(data.get("spawnX"));
            double y = Double.parseDouble(data.get("spawnY"));
            double z = Double.parseDouble(data.get("spawnZ"));
            String worldName = data.get("spawnWorld");

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                world = Bukkit.getWorlds().get(0);
            }

            Location spawnLocation = new Location(world, x, y, z);
            int size = Integer.parseInt(data.get("size"));
            long createdAt = Long.parseLong(data.get("createdAt"));
            boolean isActive = Boolean.parseBoolean(data.get("isActive"));

            return new IslandInfo(islandId, ownerId, type, serverId, spawnLocation, size, createdAt,
                                new ArrayList<>(), new HashMap<>(), isActive);

        } catch (Exception e) {
            return null;
        }
    }

    public IslandInfo getPlayerIsland(UUID playerId) {
        return playerIslands.get(playerId);
    }

    public List<IslandInfo> getIslandsByType(NetworkArchitecture.IslandType islandType) {
        return new ArrayList<>(islandsByType.getOrDefault(islandType, new ArrayList<>()));
    }

    public List<IslandInfo> getIslandsByServer(String serverId) {
        return new ArrayList<>(islandsByServer.getOrDefault(serverId, new ArrayList<>()));
    }

    // Island Info Class
    public static class IslandInfo {
        private final String id;
        private final UUID ownerId;
        private final NetworkArchitecture.IslandType type;
        private final String serverId;
        private final Location spawnLocation;
        private final int size;
        private final long createdAt;
        private final List<UUID> members;
        private final Map<String, Object> settings;
        private final boolean isActive;

        public IslandInfo(String id, UUID ownerId, NetworkArchitecture.IslandType type,
                         String serverId, Location spawnLocation, int size, long createdAt,
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
        public Location getSpawnLocation() { return spawnLocation; }
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
