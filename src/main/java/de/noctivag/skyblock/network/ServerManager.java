package de.noctivag.skyblock.network;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
// Redis imports
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Server Manager - Verwaltet alle Server im Netzwerk
 *
 * Verantwortlich für:
 * - Server-Registrierung und -Verwaltung
 * - Server-Status Überwachung
 * - Player-Transfer zwischen Servern
 * - Server-Load-Balancing
 * - Server-Kommunikation
 */
public class ServerManager {

    private final SkyblockPlugin SkyblockPlugin;
    private final NetworkArchitecture.NetworkConfig config;
    private final JedisPool jedisPool;

    private final Map<String, ServerInfo> registeredServers = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerServerMap = new ConcurrentHashMap<>();
    private final Map<NetworkArchitecture.ServerType, List<ServerInfo>> serversByType = new ConcurrentHashMap<>();

    private final String serverId;
    private final NetworkArchitecture.ServerType serverType;
    private boolean isConnected = false;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void updatePlayerServer(UUID playerId, String serverId) {
        playerServerMap.put(playerId, serverId);
    }

    public void removePlayerServer(UUID playerId) {
        playerServerMap.remove(playerId);
    }

    public ServerManager(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager,
                        NetworkArchitecture.NetworkConfig config, NetworkArchitecture.ServerType serverType) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.config = config;
        this.serverType = serverType;
        this.serverId = generateServerId();

        // Initialize Redis connection
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);

        this.jedisPool = new JedisPool(poolConfig, config.getRedisHost(), config.getRedisPort(),
                                     2000, config.getRedisPassword());

        initializeServerTypes();
        startHeartbeat();
        startServerDiscovery();
    }

    private void initializeServerTypes() {
        for (NetworkArchitecture.ServerType type : NetworkArchitecture.ServerType.values()) {
            serversByType.put(type, new ArrayList<>());
        }
    }

    private String generateServerId() {
        return serverType.getName().toLowerCase() + "_" + java.lang.System.currentTimeMillis() + "_" +
               (int)(Math.random() * 1000);
    }

    public void connect() {
        try {
            // Register this server (registerServer will manage its own Jedis resource)
            registerServer();

            // Subscribe to server events
            subscribeToServerEvents();

            isConnected = true;
            SkyblockPlugin.getLogger().info("Connected to network as " + serverId);

        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Failed to connect to network: " + e.getMessage());
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Exception during ServerManager.connect", e);
        }
    }

    public void disconnect() {
        try {
            // Unregister this server (unregisterServer will manage its own Jedis resource)
            unregisterServer();

            isConnected = false;
            SkyblockPlugin.getLogger().info("Disconnected from network");

        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Failed to disconnect from network: " + e.getMessage());
        }
    }

    private void registerServer() {
        try (Jedis jedis = jedisPool.getResource()) {
            ServerInfo serverInfo = new ServerInfo(
                serverId,
                serverType,
                "Server-" + Bukkit.getPort(),
                Bukkit.getIp(),
                Bukkit.getPort(),
                Bukkit.getOnlinePlayers().size(),
                config.getMaxPlayersPerServer(),
                java.lang.System.currentTimeMillis(),
                true
            );

            // Store server info in Redis
            String serverKey = "server:" + serverId;
            jedis.hset(serverKey, "id", serverInfo.getId());
            jedis.hset(serverKey, "type", serverInfo.getType().name());
            jedis.hset(serverKey, "name", serverInfo.getName());
            jedis.hset(serverKey, "address", serverInfo.getAddress());
            jedis.hset(serverKey, "port", String.valueOf(serverInfo.getPort()));
            jedis.hset(serverKey, "onlinePlayers", String.valueOf(serverInfo.getOnlinePlayers()));
            jedis.hset(serverKey, "maxPlayers", String.valueOf(serverInfo.getMaxPlayers()));
            jedis.hset(serverKey, "lastHeartbeat", String.valueOf(serverInfo.getLastHeartbeat()));
            jedis.hset(serverKey, "isOnline", String.valueOf(serverInfo.isOnline()));

            // Set expiration (server will be removed if no heartbeat for 30 seconds)
            jedis.expire(serverKey, 30);

            // Add to server list
            jedis.sadd("servers", serverId);
            jedis.sadd("servers:" + serverType.name().toLowerCase(), serverId);

            registeredServers.put(serverId, serverInfo);
            serversByType.get(serverType).add(serverInfo);

        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Failed to register server: " + e.getMessage());
        }
    }

    private void unregisterServer() {
        try (Jedis jedis = jedisPool.getResource()) {
            String serverKey = "server:" + serverId;
            jedis.del(serverKey);
            jedis.srem("servers", serverId);
            jedis.srem("servers:" + serverType.name().toLowerCase(), serverId);

            registeredServers.remove(serverId);
            serversByType.get(serverType).removeIf(server -> server.getId().equals(serverId));

        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Failed to unregister server: " + e.getMessage());
        }
    }

    private void startHeartbeat() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isConnected) {
                    sendHeartbeat();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L); // Every second
    }

    private void sendHeartbeat() {
        try (Jedis jedis = jedisPool.getResource()) {
            String serverKey = "server:" + serverId;
            jedis.hset(serverKey, "onlinePlayers", String.valueOf(Bukkit.getOnlinePlayers().size()));
            jedis.hset(serverKey, "lastHeartbeat", String.valueOf(java.lang.System.currentTimeMillis()));
            jedis.expire(serverKey, 30);

        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Failed to send heartbeat: " + e.getMessage());
        }
    }

    private void startServerDiscovery() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isConnected) {
                    discoverServers();
                }
            }
        }.runTaskTimerAsynchronously(SkyblockPlugin, 0L, 100L); // Every 5 seconds
    }

    private void discoverServers() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> serverIds = jedis.smembers("servers");

            for (String serverId : serverIds) {
                if (!this.serverId.equals(serverId)) {
                    String serverKey = "server:" + serverId;
                    Map<String, String> serverData = jedis.hgetAll(serverKey);

                    if (!serverData.isEmpty()) {
                        ServerInfo serverInfo = parseServerInfo(serverData);
                        if (serverInfo != null) {
                            registeredServers.put(serverId, serverInfo);

                            // Update servers by type
                            serversByType.get(serverInfo.getType()).removeIf(server -> server.getId().equals(serverId));
                            serversByType.get(serverInfo.getType()).add(serverInfo);
                        }
                    }
                }
            }

        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Failed to discover servers: " + e.getMessage());
        }
    }

    private ServerInfo parseServerInfo(Map<String, String> data) {
        try {
            return new ServerInfo(
                data.get("id"),
                NetworkArchitecture.ServerType.valueOf(data.get("type")),
                data.get("name"),
                data.get("address"),
                Integer.parseInt(data.get("port")),
                Integer.parseInt(data.get("onlinePlayers")),
                Integer.parseInt(data.get("maxPlayers")),
                Long.parseLong(data.get("lastHeartbeat")),
                Boolean.parseBoolean(data.get("isOnline"))
            );
        } catch (Exception e) {
            return null;
        }
    }

    private void subscribeToServerEvents() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try (Jedis jedis = jedisPool.getResource()) {
                    // Subscribe to player transfer events
                    jedis.subscribe(new PlayerTransferListener(), "player_transfer");

                } catch (Exception e) {
                    SkyblockPlugin.getLogger().severe("Failed to subscribe to server events: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(SkyblockPlugin);
    }

    public boolean transferPlayer(Player player, NetworkArchitecture.ServerType targetServerType,
                                 NetworkArchitecture.TransferReason reason) {
        try {
            // Find best server of target type
            ServerInfo targetServer = findBestServer(targetServerType);
            if (targetServer == null) {
                player.sendMessage(Component.text("§cNo available server found for " + targetServerType.getName()));
                return false;
            }

            // Check if server has capacity
            if (targetServer.getOnlinePlayers() >= targetServer.getMaxPlayers()) {
                player.sendMessage(Component.text("§cTarget server is full"));
                return false;
            }

            // Send transfer request
            sendTransferRequest(player, targetServer, reason);

            // Update player server map
            playerServerMap.put(player.getUniqueId(), targetServer.getId());

            return true;

        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Failed to transfer player: " + e.getMessage());
            return false;
        }
    }

    private ServerInfo findBestServer(NetworkArchitecture.ServerType serverType) {
        List<ServerInfo> servers = serversByType.get(serverType);
        if (servers.isEmpty()) {
            return null;
        }

        // Find server with lowest player count
        return servers.stream()
                .filter(ServerInfo::isOnline)
                .min(Comparator.comparingInt(ServerInfo::getOnlinePlayers))
                .orElse(null);
    }

    private void sendTransferRequest(Player player, ServerInfo targetServer,
                                   NetworkArchitecture.TransferReason reason) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> transferData = new HashMap<>();
            transferData.put("playerId", player.getUniqueId().toString());
            transferData.put("playerName", player.getName());
            transferData.put("sourceServer", serverId);
            transferData.put("targetServer", targetServer.getId());
            transferData.put("reason", reason.name());
            transferData.put("timestamp", String.valueOf(java.lang.System.currentTimeMillis()));

            jedis.hset("transfer_request:" + player.getUniqueId(), transferData);
            jedis.expire("transfer_request:" + player.getUniqueId(), 60); // 1 minute expiration

            // Notify target server
            jedis.publish("player_transfer", player.getUniqueId().toString());

        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Failed to send transfer request: " + e.getMessage());
        }
    }

    public List<ServerInfo> getServersByType(NetworkArchitecture.ServerType serverType) {
        return new ArrayList<>(serversByType.get(serverType));
    }

    public ServerInfo getServerInfo(String serverId) {
        return registeredServers.get(serverId);
    }

    public String getPlayerServer(UUID playerId) {
        return playerServerMap.get(playerId);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getServerId() {
        return serverId;
    }

    public NetworkArchitecture.ServerType getServerType() {
        return serverType;
    }

    // Player Transfer Listener
    private class PlayerTransferListener extends redis.clients.jedis.JedisPubSub {
        @Override
        public void onMessage(String channel, String message) {
            if (channel.equals("player_transfer")) {
                UUID playerId = UUID.fromString(message);
                handlePlayerTransfer(playerId);
            }
        }

        private void handlePlayerTransfer(UUID playerId) {
            try (Jedis jedis = jedisPool.getResource()) {
                String transferKey = "transfer_request:" + playerId;
                Map<String, String> transferData = jedis.hgetAll(transferKey);

                if (!transferData.isEmpty() && serverId.equals(transferData.get("targetServer"))) {
                    // This server is the target, prepare for player arrival
                    prepareForPlayerArrival(playerId, transferData);
                }

            } catch (Exception e) {
                SkyblockPlugin.getLogger().severe("Failed to handle player transfer: " + e.getMessage());
            }
        }

        private void prepareForPlayerArrival(UUID playerId, Map<String, String> transferData) {
            // Prepare server for player arrival
            // This could include loading player data, setting up spawn location, etc.
            SkyblockPlugin.getLogger().info("Preparing for player arrival: " + playerId);
        }
    }

    // Server Info Class
    public static class ServerInfo {
        private final String id;
        private final NetworkArchitecture.ServerType type;
        private final String name;
        private final String address;
        private final int port;
        private final int onlinePlayers;
        private final int maxPlayers;
        private final long lastHeartbeat;
        private final boolean isOnline;

        public ServerInfo(String id, NetworkArchitecture.ServerType type, String name,
                         String address, int port, int onlinePlayers, int maxPlayers,
                         long lastHeartbeat, boolean isOnline) {
            this.id = id;
            this.type = type;
            this.name = name;
            this.address = address;
            this.port = port;
            this.onlinePlayers = onlinePlayers;
            this.maxPlayers = maxPlayers;
            this.lastHeartbeat = lastHeartbeat;
            this.isOnline = isOnline;
        }

        // Getters
        public String getId() { return id; }
        public NetworkArchitecture.ServerType getType() { return type; }
        public String getName() { return name; }
        public String getAddress() { return address; }
        public int getPort() { return port; }
        public int getOnlinePlayers() { return onlinePlayers; }
        public int getMaxPlayers() { return maxPlayers; }
        public long getLastHeartbeat() { return lastHeartbeat; }
        public boolean isOnline() { return isOnline; }

        public double getLoadPercentage() {
            return (double) onlinePlayers / maxPlayers * 100;
        }

        public boolean isHealthy() {
            return isOnline && (java.lang.System.currentTimeMillis() - lastHeartbeat) < 30000; // 30 seconds
        }
    }
}
