package de.noctivag.plugin.network;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Player Transfer System - Verwaltet Player-Transfers zwischen Servern
 * 
 * Verantwortlich für:
 * - Player-Transfer zwischen Servern
 * - Player-Daten-Synchronisation
 * - Transfer-Status-Tracking
 * - Transfer-Sicherheit
 * - Transfer-Queue-Management
 */
public class PlayerTransferSystem {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final ServerManager serverManager;
    private final NetworkCommunication networkCommunication;
    private final JedisPool jedisPool;
    
    private final Map<UUID, TransferInfo> activeTransfers = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerData> playerDataCache = new ConcurrentHashMap<>();
    private final Queue<TransferRequest> transferQueue = new LinkedList<>();
    
    private final int maxConcurrentTransfers;
    private final long transferTimeout;
    
    public PlayerTransferSystem(Plugin plugin, MultiServerDatabaseManager databaseManager,
                               ServerManager serverManager, NetworkCommunication networkCommunication,
                               JedisPool jedisPool, int maxConcurrentTransfers, long transferTimeout) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.serverManager = serverManager;
        this.networkCommunication = networkCommunication;
        this.jedisPool = jedisPool;
        this.maxConcurrentTransfers = maxConcurrentTransfers;
        this.transferTimeout = transferTimeout;
        
        startTransferProcessor();
        registerMessageHandlers();
    }
    
    private void startTransferProcessor() {
        new BukkitRunnable() {
            @Override
            public void run() {
                processTransferQueue();
                cleanupExpiredTransfers();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    private void processTransferQueue() {
        while (!transferQueue.isEmpty() && activeTransfers.size() < maxConcurrentTransfers) {
            TransferRequest request = transferQueue.poll();
            if (request != null) {
                processTransfer(request);
            }
        }
    }
    
    private void cleanupExpiredTransfers() {
        long currentTime = System.currentTimeMillis();
        activeTransfers.entrySet().removeIf(entry -> {
            TransferInfo transfer = entry.getValue();
            if (currentTime - transfer.getStartTime() > transferTimeout) {
                plugin.getLogger().warning("Transfer expired for player: " + entry.getKey());
                return true;
            }
            return false;
        });
    }
    
    private void registerMessageHandlers() {
        networkCommunication.registerMessageHandler("player_transfer", new NetworkCommunication.MessageHandler() {
            @Override
            public void handleMessage(Map<String, String> messageData) {
                handlePlayerTransferMessage(messageData);
            }
            
            @Override
            public String handleRequest(Map<String, String> requestData) {
                return handlePlayerTransferRequest(requestData);
            }
        });
        
        networkCommunication.registerEventListener(new NetworkCommunication.NetworkEventListener() {
            @Override
            public void onEvent(String eventType, Map<String, String> eventData) {
                if (eventType.equals("player_join")) {
                    handlePlayerJoinEvent(eventData);
                } else if (eventType.equals("player_leave")) {
                    handlePlayerLeaveEvent(eventData);
                }
            }
        });
    }
    
    public CompletableFuture<Boolean> transferPlayer(Player player, NetworkArchitecture.ServerType targetServerType,
                                                    NetworkArchitecture.TransferReason reason) {
        return transferPlayer(player, targetServerType, reason, null);
    }
    
    public CompletableFuture<Boolean> transferPlayer(Player player, NetworkArchitecture.ServerType targetServerType,
                                                    NetworkArchitecture.TransferReason reason, Map<String, String> customData) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            // Check if player is already being transferred
            if (activeTransfers.containsKey(player.getUniqueId())) {
                future.complete(false);
                return future;
            }
            
            // Find target server
            ServerManager.ServerInfo targetServer = findBestTargetServer(targetServerType);
            if (targetServer == null) {
                player.sendMessage(Component.text("§cNo available server found for transfer"));
                future.complete(false);
                return future;
            }
            
            // Create transfer request
            TransferRequest request = new TransferRequest(
                player.getUniqueId(),
                player.getName(),
                serverManager.getServerId(),
                targetServer.getId(),
                targetServerType,
                reason,
                customData,
                future
            );
            
            // Add to queue
            transferQueue.offer(request);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to initiate player transfer: " + e.getMessage());
            future.complete(false);
        }
        
        return future;
    }
    
    private ServerManager.ServerInfo findBestTargetServer(NetworkArchitecture.ServerType serverType) {
        List<ServerManager.ServerInfo> servers = serverManager.getServersByType(serverType);
        
        if (servers.isEmpty()) {
            return null;
        }
        
        // Find server with lowest load
        return servers.stream()
                .filter(ServerManager.ServerInfo::isHealthy)
                .min(Comparator.comparingDouble(ServerManager.ServerInfo::getLoadPercentage))
                .orElse(null);
    }
    
    private void processTransfer(TransferRequest request) {
        try {
            // Create transfer info
            TransferInfo transferInfo = new TransferInfo(
                request.getPlayerId(),
                request.getPlayerName(),
                request.getSourceServerId(),
                request.getTargetServerId(),
                request.getTargetServerType(),
                request.getReason(),
                System.currentTimeMillis(),
                TransferInfo.TransferStatus.PREPARING
            );
            
            activeTransfers.put(request.getPlayerId(), transferInfo);
            
            // Save player data
            savePlayerData(request.getPlayerId());
            
            // Send transfer request to target server
            Map<String, String> transferData = new HashMap<>();
            transferData.put("playerId", request.getPlayerId().toString());
            transferData.put("playerName", request.getPlayerName());
            transferData.put("sourceServerId", request.getSourceServerId());
            transferData.put("targetServerId", request.getTargetServerId());
            transferData.put("targetServerType", request.getTargetServerType().name());
            transferData.put("reason", request.getReason().name());
            transferData.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            if (request.getCustomData() != null) {
                transferData.putAll(request.getCustomData());
            }
            
            boolean sent = networkCommunication.sendMessage(request.getTargetServerId(), "player_transfer", transferData);
            
            if (sent) {
                transferInfo.setStatus(TransferInfo.TransferStatus.TRANSFERRING);
                
                // Start transfer timeout
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (activeTransfers.containsKey(request.getPlayerId())) {
                            TransferInfo transfer = activeTransfers.get(request.getPlayerId());
                            if (transfer.getStatus() == TransferInfo.TransferStatus.TRANSFERRING) {
                                plugin.getLogger().warning("Transfer timeout for player: " + request.getPlayerId());
                                transfer.setStatus(TransferInfo.TransferStatus.FAILED);
                                request.getFuture().complete(false);
                                activeTransfers.remove(request.getPlayerId());
                            }
                        }
                    }
                }.runTaskLater(plugin, transferTimeout / 50); // Convert to ticks
                
            } else {
                transferInfo.setStatus(TransferInfo.TransferStatus.FAILED);
                request.getFuture().complete(false);
                activeTransfers.remove(request.getPlayerId());
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to process transfer: " + e.getMessage());
            request.getFuture().complete(false);
        }
    }
    
    private void savePlayerData(UUID playerId) {
        try {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                PlayerData playerData = new PlayerData(
                    playerId,
                    player.getName(),
                    player.getLocation(),
                    player.getInventory().getContents(),
                    player.getEnderChest().getContents(),
                    player.getHealth(),
                    player.getFoodLevel(),
                    player.getLevel(),
                    player.getExp(),
                    player.getGameMode(),
                    System.currentTimeMillis()
                );
                
                playerDataCache.put(playerId, playerData);
                
                // Save to Redis for cross-server access
                savePlayerDataToRedis(playerData);
                
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to save player data: " + e.getMessage());
        }
    }
    
    private void savePlayerDataToRedis(PlayerData playerData) {
        try (Jedis jedis = jedisPool.getResource()) {
            String playerKey = "player_data:" + playerData.getPlayerId();
            
            jedis.hset(playerKey, "playerId", playerData.getPlayerId().toString());
            jedis.hset(playerKey, "playerName", playerData.getPlayerName());
            jedis.hset(playerKey, "world", playerData.getLocation().getWorld().getName());
            jedis.hset(playerKey, "x", String.valueOf(playerData.getLocation().getX()));
            jedis.hset(playerKey, "y", String.valueOf(playerData.getLocation().getY()));
            jedis.hset(playerKey, "z", String.valueOf(playerData.getLocation().getZ()));
            jedis.hset(playerKey, "yaw", String.valueOf(playerData.getLocation().getYaw()));
            jedis.hset(playerKey, "pitch", String.valueOf(playerData.getLocation().getPitch()));
            jedis.hset(playerKey, "health", String.valueOf(playerData.getHealth()));
            jedis.hset(playerKey, "foodLevel", String.valueOf(playerData.getFoodLevel()));
            jedis.hset(playerKey, "level", String.valueOf(playerData.getLevel()));
            jedis.hset(playerKey, "exp", String.valueOf(playerData.getExp()));
            jedis.hset(playerKey, "gameMode", playerData.getGameMode().name());
            jedis.hset(playerKey, "timestamp", String.valueOf(playerData.getTimestamp()));
            
            // Set expiration (player data expires after 1 hour)
            jedis.expire(playerKey, 3600);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to save player data to Redis: " + e.getMessage());
        }
    }
    
    private void handlePlayerTransferMessage(Map<String, String> messageData) {
        try {
            UUID playerId = UUID.fromString(messageData.get("playerId"));
            String playerName = messageData.get("playerName");
            String sourceServerId = messageData.get("sourceServerId");
            String targetServerId = messageData.get("targetServerId");
            
            // Check if this server is the target
            if (serverManager.getServerId().equals(targetServerId)) {
                // Prepare for player arrival
                prepareForPlayerArrival(playerId, playerName, sourceServerId, messageData);
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to handle player transfer message: " + e.getMessage());
        }
    }
    
    private String handlePlayerTransferRequest(Map<String, String> requestData) {
        try {
            UUID playerId = UUID.fromString(requestData.get("playerId"));
            String playerName = requestData.get("playerName");
            String sourceServerId = requestData.get("sourceServerId");
            
            // Check if server can accept player
            if (canAcceptPlayer()) {
                // Load player data
                PlayerData playerData = loadPlayerData(playerId);
                if (playerData != null) {
                    return "ACCEPT:" + playerData.getPlayerId();
                } else {
                    return "REJECT:Player data not found";
                }
            } else {
                return "REJECT:Server full";
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to handle player transfer request: " + e.getMessage());
            return "REJECT:Internal error";
        }
    }
    
    private void prepareForPlayerArrival(UUID playerId, String playerName, String sourceServerId, Map<String, String> data) {
        try {
            // Load player data
            PlayerData playerData = loadPlayerData(playerId);
            if (playerData != null) {
                // Prepare spawn location
                org.bukkit.Location spawnLocation = calculateSpawnLocation(playerData, data);
                
                // Store arrival data
                storeArrivalData(playerId, spawnLocation, data);
                
                // Notify source server
                Map<String, String> responseData = new HashMap<>();
                responseData.put("playerId", playerId.toString());
                responseData.put("status", "READY");
                responseData.put("spawnX", String.valueOf(spawnLocation.getX()));
                responseData.put("spawnY", String.valueOf(spawnLocation.getY()));
                responseData.put("spawnZ", String.valueOf(spawnLocation.getZ()));
                responseData.put("spawnWorld", spawnLocation.getWorld().getName());
                
                networkCommunication.sendMessage(sourceServerId, "transfer_ready", responseData);
                
            } else {
                // Reject transfer
                Map<String, String> responseData = new HashMap<>();
                responseData.put("playerId", playerId.toString());
                responseData.put("status", "REJECTED");
                responseData.put("reason", "Player data not found");
                
                networkCommunication.sendMessage(sourceServerId, "transfer_rejected", responseData);
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to prepare for player arrival: " + e.getMessage());
        }
    }
    
    private PlayerData loadPlayerData(UUID playerId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String playerKey = "player_data:" + playerId;
            Map<String, String> data = jedis.hgetAll(playerKey);
            
            if (!data.isEmpty()) {
                return parsePlayerData(data);
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load player data: " + e.getMessage());
        }
        
        return null;
    }
    
    private PlayerData parsePlayerData(Map<String, String> data) {
        try {
            UUID playerId = UUID.fromString(data.get("playerId"));
            String playerName = data.get("playerName");
            
            String worldName = data.get("world");
            org.bukkit.World world = Bukkit.getWorld(worldName);
            if (world == null) {
                world = Bukkit.getWorlds().get(0);
            }
            
            double x = Double.parseDouble(data.get("x"));
            double y = Double.parseDouble(data.get("y"));
            double z = Double.parseDouble(data.get("z"));
            float yaw = Float.parseFloat(data.get("yaw"));
            float pitch = Float.parseFloat(data.get("pitch"));
            
            org.bukkit.Location location = new org.bukkit.Location(world, x, y, z, yaw, pitch);
            
            double health = Double.parseDouble(data.get("health"));
            int foodLevel = Integer.parseInt(data.get("foodLevel"));
            int level = Integer.parseInt(data.get("level"));
            float exp = Float.parseFloat(data.get("exp"));
            org.bukkit.GameMode gameMode = org.bukkit.GameMode.valueOf(data.get("gameMode"));
            long timestamp = Long.parseLong(data.get("timestamp"));
            
            return new PlayerData(playerId, playerName, location, null, null, health, foodLevel, level, exp, gameMode, timestamp);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    private org.bukkit.Location calculateSpawnLocation(PlayerData playerData, Map<String, String> data) {
        // Check if custom spawn location is provided
        if (data.containsKey("spawnX") && data.containsKey("spawnY") && data.containsKey("spawnZ")) {
            double x = Double.parseDouble(data.get("spawnX"));
            double y = Double.parseDouble(data.get("spawnY"));
            double z = Double.parseDouble(data.get("spawnZ"));
            String worldName = data.getOrDefault("spawnWorld", "world");
            
            org.bukkit.World world = Bukkit.getWorld(worldName);
            if (world == null) {
                world = Bukkit.getWorlds().get(0);
            }
            
            return new org.bukkit.Location(world, x, y, z);
        }
        
        // Use default spawn location
        return Bukkit.getWorlds().get(0).getSpawnLocation();
    }
    
    private void storeArrivalData(UUID playerId, org.bukkit.Location spawnLocation, Map<String, String> data) {
        try (Jedis jedis = jedisPool.getResource()) {
            String arrivalKey = "arrival:" + playerId;
            
            jedis.hset(arrivalKey, "playerId", playerId.toString());
            jedis.hset(arrivalKey, "spawnX", String.valueOf(spawnLocation.getX()));
            jedis.hset(arrivalKey, "spawnY", String.valueOf(spawnLocation.getY()));
            jedis.hset(arrivalKey, "spawnZ", String.valueOf(spawnLocation.getZ()));
            jedis.hset(arrivalKey, "spawnWorld", spawnLocation.getWorld().getName());
            jedis.hset(arrivalKey, "timestamp", String.valueOf(System.currentTimeMillis()));
            
            // Set expiration (arrival data expires after 5 minutes)
            jedis.expire(arrivalKey, 300);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to store arrival data: " + e.getMessage());
        }
    }
    
    private boolean canAcceptPlayer() {
        return Bukkit.getOnlinePlayers().size() < Bukkit.getMaxPlayers();
    }
    
    private void handlePlayerJoinEvent(Map<String, String> eventData) {
        try {
            UUID playerId = UUID.fromString(eventData.get("playerId"));
            String serverId = eventData.get("serverId");
            
            // Update player server map
            serverManager.updatePlayerServer(playerId, serverId);
            
            // Clean up transfer if it exists
            TransferInfo transfer = activeTransfers.remove(playerId);
            if (transfer != null) {
                transfer.setStatus(TransferInfo.TransferStatus.COMPLETED);
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to handle player join event: " + e.getMessage());
        }
    }
    
    private void handlePlayerLeaveEvent(Map<String, String> eventData) {
        try {
            UUID playerId = UUID.fromString(eventData.get("playerId"));
            String serverId = eventData.get("serverId");
            
            // Remove from player server map
            serverManager.removePlayerServer(playerId);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to handle player leave event: " + e.getMessage());
        }
    }
    
    public TransferInfo getTransferInfo(UUID playerId) {
        return activeTransfers.get(playerId);
    }
    
    public List<TransferInfo> getActiveTransfers() {
        return new ArrayList<>(activeTransfers.values());
    }
    
    public int getTransferQueueSize() {
        return transferQueue.size();
    }
    
    // Transfer Request Class
    public static class TransferRequest {
        private final UUID playerId;
        private final String playerName;
        private final String sourceServerId;
        private final String targetServerId;
        private final NetworkArchitecture.ServerType targetServerType;
        private final NetworkArchitecture.TransferReason reason;
        private final Map<String, String> customData;
        private final CompletableFuture<Boolean> future;
        
        public TransferRequest(UUID playerId, String playerName, String sourceServerId,
                              String targetServerId, NetworkArchitecture.ServerType targetServerType,
                              NetworkArchitecture.TransferReason reason, Map<String, String> customData,
                              CompletableFuture<Boolean> future) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.sourceServerId = sourceServerId;
            this.targetServerId = targetServerId;
            this.targetServerType = targetServerType;
            this.reason = reason;
            this.customData = customData;
            this.future = future;
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public String getPlayerName() { return playerName; }
        public String getSourceServerId() { return sourceServerId; }
        public String getTargetServerId() { return targetServerId; }
        public NetworkArchitecture.ServerType getTargetServerType() { return targetServerType; }
        public NetworkArchitecture.TransferReason getReason() { return reason; }
        public Map<String, String> getCustomData() { return customData; }
        public CompletableFuture<Boolean> getFuture() { return future; }
    }
    
    // Transfer Info Class
    public static class TransferInfo {
        public enum TransferStatus {
            PREPARING, TRANSFERRING, COMPLETED, FAILED
        }
        
        private final UUID playerId;
        private final String playerName;
        private final String sourceServerId;
        private final String targetServerId;
        private final NetworkArchitecture.ServerType targetServerType;
        private final NetworkArchitecture.TransferReason reason;
        private final long startTime;
        private TransferStatus status;
        
        public TransferInfo(UUID playerId, String playerName, String sourceServerId,
                           String targetServerId, NetworkArchitecture.ServerType targetServerType,
                           NetworkArchitecture.TransferReason reason, long startTime, TransferStatus status) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.sourceServerId = sourceServerId;
            this.targetServerId = targetServerId;
            this.targetServerType = targetServerType;
            this.reason = reason;
            this.startTime = startTime;
            this.status = status;
        }
        
        // Getters and Setters
        public UUID getPlayerId() { return playerId; }
        public String getPlayerName() { return playerName; }
        public String getSourceServerId() { return sourceServerId; }
        public String getTargetServerId() { return targetServerId; }
        public NetworkArchitecture.ServerType getTargetServerType() { return targetServerType; }
        public NetworkArchitecture.TransferReason getReason() { return reason; }
        public long getStartTime() { return startTime; }
        public TransferStatus getStatus() { return status; }
        public void setStatus(TransferStatus status) { this.status = status; }
    }
    
    // Player Data Class
    public static class PlayerData {
        private final UUID playerId;
        private final String playerName;
        private final org.bukkit.Location location;
        private final org.bukkit.inventory.ItemStack[] inventory;
        private final org.bukkit.inventory.ItemStack[] enderChest;
        private final double health;
        private final int foodLevel;
        private final int level;
        private final float exp;
        private final org.bukkit.GameMode gameMode;
        private final long timestamp;
        
        public PlayerData(UUID playerId, String playerName, org.bukkit.Location location,
                         org.bukkit.inventory.ItemStack[] inventory, org.bukkit.inventory.ItemStack[] enderChest,
                         double health, int foodLevel, int level, float exp, org.bukkit.GameMode gameMode, long timestamp) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.location = location;
            this.inventory = inventory;
            this.enderChest = enderChest;
            this.health = health;
            this.foodLevel = foodLevel;
            this.level = level;
            this.exp = exp;
            this.gameMode = gameMode;
            this.timestamp = timestamp;
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public String getPlayerName() { return playerName; }
        public org.bukkit.Location getLocation() { return location; }
        public org.bukkit.inventory.ItemStack[] getInventory() { return inventory; }
        public org.bukkit.inventory.ItemStack[] getEnderChest() { return enderChest; }
        public double getHealth() { return health; }
        public int getFoodLevel() { return foodLevel; }
        public int getLevel() { return level; }
        public float getExp() { return exp; }
        public org.bukkit.GameMode getGameMode() { return gameMode; }
        public long getTimestamp() { return timestamp; }
    }
}
