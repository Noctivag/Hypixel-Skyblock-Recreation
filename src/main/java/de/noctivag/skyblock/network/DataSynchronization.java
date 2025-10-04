package de.noctivag.skyblock.network;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Data Synchronization - Daten-Synchronisation zwischen Servern
 * 
 * Verantwortlich für:
 * - Player-Daten-Synchronisation
 * - Island-Daten-Synchronisation
 * - Collection-Daten-Synchronisation
 * - Minion-Daten-Synchronisation
 * - Echtzeit-Daten-Updates
 * - Konflikt-Lösung
 */
public class DataSynchronization {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final NetworkCommunication networkCommunication;
    private final JedisPool jedisPool;
    
    private final Map<String, DataVersion> dataVersions = new ConcurrentHashMap<>();
    private final Map<String, PendingUpdate> pendingUpdates = new ConcurrentHashMap<>();
    private final Map<String, Long> lastSyncTimes = new ConcurrentHashMap<>();
    
    private final long syncInterval;
    private final long conflictResolutionTimeout;
    
    public DataSynchronization(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager,
                              NetworkCommunication networkCommunication, JedisPool jedisPool,
                              long syncInterval, long conflictResolutionTimeout) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.networkCommunication = networkCommunication;
        this.jedisPool = jedisPool;
        this.syncInterval = syncInterval;
        this.conflictResolutionTimeout = conflictResolutionTimeout;
        
        startSynchronization();
        registerMessageHandlers();
    }
    
    private void startSynchronization() {
        new BukkitRunnable() {
            @Override
            public void run() {
                synchronizeAllData();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, syncInterval / 50); // Convert to ticks
    }
    
    private void registerMessageHandlers() {
        networkCommunication.registerMessageHandler("data_sync", new NetworkCommunication.MessageHandler() {
            @Override
            public void handleMessage(Map<String, String> messageData) {
                handleDataSyncMessage(messageData);
            }
            
            @Override
            public String handleRequest(Map<String, String> requestData) {
                return handleDataSyncRequest(requestData);
            }
        });
        
        networkCommunication.registerEventListener(new NetworkCommunication.NetworkEventListener() {
            @Override
            public void onEvent(String eventType, Map<String, String> eventData) {
                if (eventType.equals("player_data_sync")) {
                    handlePlayerDataSync(eventData);
                } else if (eventType.equals("island_data_sync")) {
                    handleIslandDataSync(eventData);
                } else if (eventType.equals("collection_data_sync")) {
                    handleCollectionDataSync(eventData);
                } else if (eventType.equals("minion_data_sync")) {
                    handleMinionDataSync(eventData);
                }
            }
        });
    }
    
    private void synchronizeAllData() {
        try {
            // Synchronize player data
            synchronizePlayerData();
            
            // Synchronize island data
            synchronizeIslandData();
            
            // Synchronize collection data
            synchronizeCollectionData();
            
            // Synchronize minion data
            synchronizeMinionData();
            
            // Process pending updates
            processPendingUpdates();
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to synchronize data: " + e.getMessage());
        }
    }
    
    private void synchronizePlayerData() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> playerKeys = jedis.keys("player_data:*");
            
            for (String playerKey : playerKeys) {
                String playerId = playerKey.substring("player_data:".length());
                Map<String, String> playerData = jedis.hgetAll(playerKey);
                
                if (!playerData.isEmpty()) {
                    DataVersion currentVersion = dataVersions.get(playerId);
                    long remoteTimestamp = Long.parseLong(playerData.get("timestamp"));
                    
                    if (currentVersion == null || remoteTimestamp > currentVersion.getTimestamp()) {
                        // Update local data
                        updateLocalPlayerData(playerId, playerData);
                        dataVersions.put(playerId, new DataVersion(remoteTimestamp, playerData.get("serverId")));
                    }
                }
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to synchronize player data: " + e.getMessage());
        }
    }
    
    private void synchronizeIslandData() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> islandKeys = jedis.keys("island:*");
            
            for (String islandKey : islandKeys) {
                String islandId = islandKey.substring("island:".length());
                Map<String, String> islandData = jedis.hgetAll(islandKey);
                
                if (!islandData.isEmpty()) {
                    DataVersion currentVersion = dataVersions.get(islandId);
                    long remoteTimestamp = Long.parseLong(islandData.get("lastUpdate"));
                    
                    if (currentVersion == null || remoteTimestamp > currentVersion.getTimestamp()) {
                        // Update local data
                        updateLocalIslandData(islandId, islandData);
                        dataVersions.put(islandId, new DataVersion(remoteTimestamp, islandData.get("serverId")));
                    }
                }
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to synchronize island data: " + e.getMessage());
        }
    }
    
    private void synchronizeCollectionData() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> collectionKeys = jedis.keys("collection:*");
            
            for (String collectionKey : collectionKeys) {
                String collectionId = collectionKey.substring("collection:".length());
                Map<String, String> collectionData = jedis.hgetAll(collectionKey);
                
                if (!collectionData.isEmpty()) {
                    DataVersion currentVersion = dataVersions.get(collectionId);
                    long remoteTimestamp = Long.parseLong(collectionData.get("lastUpdate"));
                    
                    if (currentVersion == null || remoteTimestamp > currentVersion.getTimestamp()) {
                        // Update local data
                        updateLocalCollectionData(collectionId, collectionData);
                        dataVersions.put(collectionId, new DataVersion(remoteTimestamp, collectionData.get("serverId")));
                    }
                }
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to synchronize collection data: " + e.getMessage());
        }
    }
    
    private void synchronizeMinionData() {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> minionKeys = jedis.keys("minion:*");
            
            for (String minionKey : minionKeys) {
                String minionId = minionKey.substring("minion:".length());
                Map<String, String> minionData = jedis.hgetAll(minionKey);
                
                if (!minionData.isEmpty()) {
                    DataVersion currentVersion = dataVersions.get(minionId);
                    long remoteTimestamp = Long.parseLong(minionData.get("lastUpdate"));
                    
                    if (currentVersion == null || remoteTimestamp > currentVersion.getTimestamp()) {
                        // Update local data
                        updateLocalMinionData(minionId, minionData);
                        dataVersions.put(minionId, new DataVersion(remoteTimestamp, minionData.get("serverId")));
                    }
                }
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to synchronize minion data: " + e.getMessage());
        }
    }
    
    private void updateLocalPlayerData(String playerId, Map<String, String> playerData) {
        try {
            // Update local player data
            // This would integrate with your existing player data system
            plugin.getLogger().info("Updating local player data for: " + playerId);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to update local player data: " + e.getMessage());
        }
    }
    
    private void updateLocalIslandData(String islandId, Map<String, String> islandData) {
        try {
            // Update local island data
            // This would integrate with your existing island system
            plugin.getLogger().info("Updating local island data for: " + islandId);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to update local island data: " + e.getMessage());
        }
    }
    
    private void updateLocalCollectionData(String collectionId, Map<String, String> collectionData) {
        try {
            // Update local collection data
            // This would integrate with your existing collection system
            plugin.getLogger().info("Updating local collection data for: " + collectionId);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to update local collection data: " + e.getMessage());
        }
    }
    
    private void updateLocalMinionData(String minionId, Map<String, String> minionData) {
        try {
            // Update local minion data
            // This would integrate with your existing minion system
            plugin.getLogger().info("Updating local minion data for: " + minionId);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to update local minion data: " + e.getMessage());
        }
    }
    
    private void processPendingUpdates() {
        long currentTime = System.currentTimeMillis();
        
        pendingUpdates.entrySet().removeIf(entry -> {
            PendingUpdate update = entry.getValue();
            if (currentTime - update.getTimestamp() > conflictResolutionTimeout) {
                // Resolve conflict by using latest timestamp
                resolveConflict(update);
                return true;
            }
            return false;
        });
    }
    
    private void resolveConflict(PendingUpdate update) {
        try {
            // Resolve conflict by using the latest timestamp
            DataVersion localVersion = dataVersions.get(update.getDataId());
            DataVersion remoteVersion = update.getRemoteVersion();
            
            if (remoteVersion.getTimestamp() > localVersion.getTimestamp()) {
                // Use remote version
                dataVersions.put(update.getDataId(), remoteVersion);
                plugin.getLogger().info("Resolved conflict for " + update.getDataId() + " using remote version");
            } else {
                // Use local version
                plugin.getLogger().info("Resolved conflict for " + update.getDataId() + " using local version");
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to resolve conflict: " + e.getMessage());
        }
    }
    
    private void handleDataSyncMessage(Map<String, String> messageData) {
        try {
            String dataId = messageData.get("dataId");
            String dataType = messageData.get("dataType");
            long timestamp = Long.parseLong(messageData.get("timestamp"));
            String serverId = messageData.get("serverId");
            
            DataVersion remoteVersion = new DataVersion(timestamp, serverId);
            DataVersion localVersion = dataVersions.get(dataId);
            
            if (localVersion == null) {
                // No local version, accept remote
                dataVersions.put(dataId, remoteVersion);
            } else if (remoteVersion.getTimestamp() > localVersion.getTimestamp()) {
                // Remote is newer, accept it
                dataVersions.put(dataId, remoteVersion);
            } else if (remoteVersion.getTimestamp() < localVersion.getTimestamp()) {
                // Local is newer, send local version
                sendLocalData(dataId, dataType);
            } else {
                // Same timestamp, check for conflicts
                if (!remoteVersion.getServerId().equals(localVersion.getServerId())) {
                    // Conflict detected
                    PendingUpdate pendingUpdate = new PendingUpdate(dataId, dataType, remoteVersion, System.currentTimeMillis());
                    pendingUpdates.put(dataId, pendingUpdate);
                }
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to handle data sync message: " + e.getMessage());
        }
    }
    
    private String handleDataSyncRequest(Map<String, String> requestData) {
        try {
            String dataId = requestData.get("dataId");
            String dataType = requestData.get("dataType");
            
            // Return local data
            return getLocalData(dataId, dataType);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to handle data sync request: " + e.getMessage());
            return "ERROR:Internal error";
        }
    }
    
    private void handlePlayerDataSync(Map<String, String> eventData) {
        try {
            String playerId = eventData.get("playerId");
            String serverId = eventData.get("serverId");
            long timestamp = Long.parseLong(eventData.get("timestamp"));
            
            // Update data version
            dataVersions.put(playerId, new DataVersion(timestamp, serverId));
            
            // Broadcast to other servers
            Map<String, String> syncData = new HashMap<>();
            syncData.put("dataId", playerId);
            syncData.put("dataType", "player");
            syncData.put("timestamp", String.valueOf(timestamp));
            syncData.put("serverId", serverId);
            
            networkCommunication.broadcastEvent("data_sync", syncData);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to handle player data sync: " + e.getMessage());
        }
    }
    
    private void handleIslandDataSync(Map<String, String> eventData) {
        try {
            String islandId = eventData.get("islandId");
            String serverId = eventData.get("serverId");
            long timestamp = Long.parseLong(eventData.get("timestamp"));
            
            // Update data version
            dataVersions.put(islandId, new DataVersion(timestamp, serverId));
            
            // Broadcast to other servers
            Map<String, String> syncData = new HashMap<>();
            syncData.put("dataId", islandId);
            syncData.put("dataType", "island");
            syncData.put("timestamp", String.valueOf(timestamp));
            syncData.put("serverId", serverId);
            
            networkCommunication.broadcastEvent("data_sync", syncData);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to handle island data sync: " + e.getMessage());
        }
    }
    
    private void handleCollectionDataSync(Map<String, String> eventData) {
        try {
            String collectionId = eventData.get("collectionId");
            String serverId = eventData.get("serverId");
            long timestamp = Long.parseLong(eventData.get("timestamp"));
            
            // Update data version
            dataVersions.put(collectionId, new DataVersion(timestamp, serverId));
            
            // Broadcast to other servers
            Map<String, String> syncData = new HashMap<>();
            syncData.put("dataId", collectionId);
            syncData.put("dataType", "collection");
            syncData.put("timestamp", String.valueOf(timestamp));
            syncData.put("serverId", serverId);
            
            networkCommunication.broadcastEvent("data_sync", syncData);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to handle collection data sync: " + e.getMessage());
        }
    }
    
    private void handleMinionDataSync(Map<String, String> eventData) {
        try {
            String minionId = eventData.get("minionId");
            String serverId = eventData.get("serverId");
            long timestamp = Long.parseLong(eventData.get("timestamp"));
            
            // Update data version
            dataVersions.put(minionId, new DataVersion(timestamp, serverId));
            
            // Broadcast to other servers
            Map<String, String> syncData = new HashMap<>();
            syncData.put("dataId", minionId);
            syncData.put("dataType", "minion");
            syncData.put("timestamp", String.valueOf(timestamp));
            syncData.put("serverId", serverId);
            
            networkCommunication.broadcastEvent("data_sync", syncData);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to handle minion data sync: " + e.getMessage());
        }
    }
    
    private void sendLocalData(String dataId, String dataType) {
        try {
            // Send local data to requesting server
            Map<String, String> data = getLocalDataAsMap(dataId, dataType);
            if (data != null) {
                networkCommunication.broadcastEvent("data_sync", data);
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send local data: " + e.getMessage());
        }
    }
    
    private String getLocalData(String dataId, String dataType) {
        try {
            // Get local data based on type
            switch (dataType) {
                case "player":
                    return getLocalPlayerData(dataId);
                case "island":
                    return getLocalIslandData(dataId);
                case "collection":
                    return getLocalCollectionData(dataId);
                case "minion":
                    return getLocalMinionData(dataId);
                default:
                    return "ERROR:Unknown data type";
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to get local data: " + e.getMessage());
            return "ERROR:Internal error";
        }
    }
    
    private Map<String, String> getLocalDataAsMap(String dataId, String dataType) {
        try {
            // Get local data as map
            switch (dataType) {
                case "player":
                    return getLocalPlayerDataAsMap(dataId);
                case "island":
                    return getLocalIslandDataAsMap(dataId);
                case "collection":
                    return getLocalCollectionDataAsMap(dataId);
                case "minion":
                    return getLocalMinionDataAsMap(dataId);
                default:
                    return null;
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to get local data as map: " + e.getMessage());
            return null;
        }
    }
    
    private String getLocalPlayerData(String playerId) {
        // Implementation would depend on your player data system
        return "player_data:" + playerId;
    }
    
    private String getLocalIslandData(String islandId) {
        // Implementation would depend on your island data system
        return "island_data:" + islandId;
    }
    
    private String getLocalCollectionData(String collectionId) {
        // Implementation would depend on your collection data system
        return "collection_data:" + collectionId;
    }
    
    private String getLocalMinionData(String minionId) {
        // Implementation would depend on your minion data system
        return "minion_data:" + minionId;
    }
    
    private Map<String, String> getLocalPlayerDataAsMap(String playerId) {
        // Implementation would depend on your player data system
        Map<String, String> data = new HashMap<>();
        data.put("dataId", playerId);
        data.put("dataType", "player");
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));
        data.put("serverId", networkCommunication.getServerId());
        return data;
    }
    
    private Map<String, String> getLocalIslandDataAsMap(String islandId) {
        // Implementation would depend on your island data system
        Map<String, String> data = new HashMap<>();
        data.put("dataId", islandId);
        data.put("dataType", "island");
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));
        data.put("serverId", networkCommunication.getServerId());
        return data;
    }
    
    private Map<String, String> getLocalCollectionDataAsMap(String collectionId) {
        // Implementation would depend on your collection data system
        Map<String, String> data = new HashMap<>();
        data.put("dataId", collectionId);
        data.put("dataType", "collection");
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));
        data.put("serverId", networkCommunication.getServerId());
        return data;
    }
    
    private Map<String, String> getLocalMinionDataAsMap(String minionId) {
        // Implementation would depend on your minion data system
        Map<String, String> data = new HashMap<>();
        data.put("dataId", minionId);
        data.put("dataType", "minion");
        data.put("timestamp", String.valueOf(System.currentTimeMillis()));
        data.put("serverId", networkCommunication.getServerId());
        return data;
    }
    
    public void syncPlayerData(UUID playerId) {
        try {
            Map<String, String> syncData = new HashMap<>();
            syncData.put("playerId", playerId.toString());
            syncData.put("serverId", networkCommunication.getServerId());
            syncData.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            networkCommunication.broadcastEvent("player_data_sync", syncData);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to sync player data: " + e.getMessage());
        }
    }
    
    public void syncIslandData(String islandId) {
        try {
            Map<String, String> syncData = new HashMap<>();
            syncData.put("islandId", islandId);
            syncData.put("serverId", networkCommunication.getServerId());
            syncData.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            networkCommunication.broadcastEvent("island_data_sync", syncData);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to sync island data: " + e.getMessage());
        }
    }
    
    public void syncCollectionData(String collectionId) {
        try {
            Map<String, String> syncData = new HashMap<>();
            syncData.put("collectionId", collectionId);
            syncData.put("serverId", networkCommunication.getServerId());
            syncData.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            networkCommunication.broadcastEvent("collection_data_sync", syncData);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to sync collection data: " + e.getMessage());
        }
    }
    
    public void syncMinionData(String minionId) {
        try {
            Map<String, String> syncData = new HashMap<>();
            syncData.put("minionId", minionId);
            syncData.put("serverId", networkCommunication.getServerId());
            syncData.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            networkCommunication.broadcastEvent("minion_data_sync", syncData);
            
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to sync minion data: " + e.getMessage());
        }
    }
    
    public DataVersion getDataVersion(String dataId) {
        return dataVersions.get(dataId);
    }
    
    public Map<String, DataVersion> getAllDataVersions() {
        return new HashMap<>(dataVersions);
    }
    
    public int getPendingUpdatesCount() {
        return pendingUpdates.size();
    }
    
    // Data Version Class
    public static class DataVersion {
        private final long timestamp;
        private final String serverId;
        
        public DataVersion(long timestamp, String serverId) {
            this.timestamp = timestamp;
            this.serverId = serverId;
        }
        
        public long getTimestamp() { return timestamp; }
        public String getServerId() { return serverId; }
    }
    
    // Pending Update Class
    public static class PendingUpdate {
        private final String dataId;
        private final String dataType;
        private final DataVersion remoteVersion;
        private final long timestamp;
        
        public PendingUpdate(String dataId, String dataType, DataVersion remoteVersion, long timestamp) {
            this.dataId = dataId;
            this.dataType = dataType;
            this.remoteVersion = remoteVersion;
            this.timestamp = timestamp;
        }
        
        public String getDataId() { return dataId; }
        public String getDataType() { return dataType; }
        public DataVersion getRemoteVersion() { return remoteVersion; }
        public long getTimestamp() { return timestamp; }
    }
}
