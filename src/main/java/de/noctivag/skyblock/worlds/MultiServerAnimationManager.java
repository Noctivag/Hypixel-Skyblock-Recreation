package de.noctivag.skyblock.worlds;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.islands.IslandAnimationSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.List;
// import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multi-Server Animation Manager - Coordinates animations across multiple servers
 * Ensures compatibility with the Multi-Server System
 */
public class MultiServerAnimationManager {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final WorldAnimationSystem worldAnimationSystem;
    private final IslandAnimationSystem islandAnimationSystem;
    
    private final Map<String, ServerAnimationData> serverAnimations = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerAnimationData> playerAnimations = new ConcurrentHashMap<>();
    private final Map<String, BukkitTask> globalAnimationTasks = new ConcurrentHashMap<>();
    
    public MultiServerAnimationManager(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.worldAnimationSystem = new WorldAnimationSystem(plugin, databaseManager);
        this.islandAnimationSystem = new IslandAnimationSystem(plugin, databaseManager);
        
        initializeMultiServerAnimations();
        startGlobalAnimationSync();
    }
    
    private void initializeMultiServerAnimations() {
        // Initialize server-specific animation data
        String serverId = plugin.getConfig().getString("server.id", "default");
        
        ServerAnimationData serverData = new ServerAnimationData(
            serverId,
            plugin.getServer().getName(),
            Bukkit.getOnlinePlayers().size(),
            System.currentTimeMillis()
        );
        
        serverAnimations.put(serverId, serverData);
        
        // Load animation data from database
        loadAnimationDataFromDatabase();
    }
    
    private void startGlobalAnimationSync() {
        // Sync animations across servers every 5 seconds
        BukkitTask syncTask = new BukkitRunnable() {
            @Override
            public void run() {
                syncAnimationsAcrossServers();
                updateServerAnimationData();
            }
        }.runTaskTimer(plugin, 0L, 100L); // Every 5 seconds
        
        globalAnimationTasks.put("sync", syncTask);
        
        // Cleanup old animations every minute
        BukkitTask cleanupTask = new BukkitRunnable() {
            @Override
            public void run() {
                cleanupOldAnimations();
            }
        }.runTaskTimer(plugin, 0L, 1200L); // Every minute
        
        globalAnimationTasks.put("cleanup", cleanupTask);
    }
    
    private void syncAnimationsAcrossServers() {
        // Get animation data from other servers
        try {
            List<ServerAnimationData> otherServerData = databaseManager.getAnimationDataFromOtherServers().get();
            
            for (ServerAnimationData data : otherServerData) {
                if (!data.getServerId().equals(plugin.getConfig().getString("server.id", "default"))) {
                    // Apply animations from other servers if needed
                    applyRemoteAnimations(data);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to sync animations across servers: " + e.getMessage());
        }
    }
    
    private void applyRemoteAnimations(ServerAnimationData remoteData) {
        // Apply world animations from remote server
        for (String worldName : remoteData.getActiveWorldAnimations()) {
            if (!worldAnimationSystem.getActiveAnimations().containsKey(worldName)) {
                // Start animation if not already active
                WorldAnimationSystem.WorldAnimation animation = createWorldAnimationFromData(remoteData, worldName);
                if (animation != null) {
                    worldAnimationSystem.startWorldAnimation(worldName, animation);
                }
            }
        }
        
        // Apply island animations from remote server
        for (UUID islandId : remoteData.getActiveIslandAnimations()) {
            if (!islandAnimationSystem.getActiveIslandAnimations().containsKey(islandId)) {
                // Start island animation if not already active
                IslandAnimationSystem.IslandAnimation animation = createIslandAnimationFromData(remoteData, islandId);
                if (animation != null) {
                    // Note: This would need to be implemented in IslandAnimationSystem
                    // islandAnimationSystem.startIslandAnimation(islandId, animation);
                }
            }
        }
    }
    
    private WorldAnimationSystem.WorldAnimation createWorldAnimationFromData(ServerAnimationData data, String worldName) {
        // Create world animation based on remote data
        // This is a simplified version - in a real implementation, you would
        // store more detailed animation data in the database
        
        return new WorldAnimationSystem.WorldAnimation(
            List.of(
                new WorldAnimationSystem.ParticleAnimation(Particle.HAPPY_VILLAGER, 0.5, 0.5, 0.5, 2, 20L)
            ),
            List.of(
                new WorldAnimationSystem.SoundAnimation(Sound.ENTITY_VILLAGER_YES, 0.5f, 1.0f, 200L)
            ),
            20L
        );
    }
    
    private IslandAnimationSystem.IslandAnimation createIslandAnimationFromData(ServerAnimationData data, UUID islandId) {
        // Create island animation based on remote data
        // This is a simplified version - in a real implementation, you would
        // store more detailed animation data in the database
        
        return new IslandAnimationSystem.IslandAnimation(
            "§aRemote Island Animation",
            List.of(
                new IslandAnimationSystem.ParticleEffect(Particle.HAPPY_VILLAGER, new Location(Bukkit.getWorlds().get(0), 0, 100, 0), 0.5, 0.5, 0.5, 3, 15L)
            ),
            List.of(
                new IslandAnimationSystem.SoundEffect(Sound.ENTITY_VILLAGER_YES, new Location(Bukkit.getWorlds().get(0), 0, 100, 0), 0.6f, 1.0f, 0L)
            ),
            200L
        );
    }
    
    private void updateServerAnimationData() {
        String serverId = plugin.getConfig().getString("server.id", "default");
        ServerAnimationData currentData = serverAnimations.get(serverId);
        
        if (currentData != null) {
            // Update current server data
            currentData.setPlayerCount(Bukkit.getOnlinePlayers().size());
            currentData.setLastUpdate(System.currentTimeMillis());
            currentData.setActiveWorldAnimations(new ArrayList<>(worldAnimationSystem.getActiveAnimations().keySet()));
            currentData.setActiveIslandAnimations(new ArrayList<>(islandAnimationSystem.getActiveIslandAnimations().keySet()));
            
            // Save to database
            saveServerAnimationData(currentData);
        }
    }
    
    private void saveServerAnimationData(ServerAnimationData data) {
        try {
            databaseManager.saveServerAnimationData(data);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save server animation data: " + e.getMessage());
        }
    }
    
    private void loadAnimationDataFromDatabase() {
        try {
            List<ServerAnimationData> allData = databaseManager.getAllServerAnimationData().get();
            
            for (ServerAnimationData data : allData) {
                serverAnimations.put(data.getServerId(), data);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load animation data from database: " + e.getMessage());
        }
    }
    
    private void cleanupOldAnimations() {
        long currentTime = System.currentTimeMillis();
        long cleanupThreshold = 300000; // 5 minutes
        
        // Cleanup old server data
        serverAnimations.entrySet().removeIf(entry -> 
            currentTime - entry.getValue().getLastUpdate() > cleanupThreshold
        );
        
        // Cleanup old player animations
        playerAnimations.entrySet().removeIf(entry -> 
            currentTime - entry.getValue().getLastUpdate() > cleanupThreshold
        );
    }
    
    public void playGlobalAnimation(String animationType, Location location, Player player) {
        // Play animation on all servers
        String serverId = plugin.getConfig().getString("server.id", "default");
        
        // Create global animation data
        GlobalAnimationData globalData = new GlobalAnimationData(
            UUID.randomUUID(),
            animationType,
            location,
            player != null ? player.getUniqueId() : null,
            serverId,
            System.currentTimeMillis()
        );
        
        // Save to database for other servers to pick up
        try {
            databaseManager.saveGlobalAnimationData(globalData);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save global animation data: " + e.getMessage());
        }
        
        // Play animation locally
        playLocalAnimation(animationType, location, player);
    }
    
    private void playLocalAnimation(String animationType, Location location, Player player) {
        switch (animationType.toLowerCase()) {
            case "world_spawn" -> worldAnimationSystem.startWorldAnimation(
                location.getWorld().getName(),
                new WorldAnimationSystem.WorldAnimation(
                    List.of(
                        new WorldAnimationSystem.ParticleAnimation(Particle.HAPPY_VILLAGER, 0.5, 0.5, 0.5, 3, 10L)
                    ),
                    List.of(
                        new WorldAnimationSystem.SoundAnimation(Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.2f, 0L)
                    ),
                    20L
                )
            );
            case "island_spawn" -> {
                if (player != null) {
                    islandAnimationSystem.playIslandSpawnAnimation(
                        UUID.randomUUID(), location, player
                    );
                }
            }
            case "island_level_up" -> {
                if (player != null) {
                    islandAnimationSystem.playIslandLevelUpAnimation(
                        UUID.randomUUID(), location, 1, player
                    );
                }
            }
        }
    }
    
    public void handleGlobalAnimationData(GlobalAnimationData data) {
        // Handle animation data from other servers
        if (!data.getServerId().equals(plugin.getConfig().getString("server.id", "default"))) {
            // Play animation from remote server
            Player player = data.getPlayerId() != null ? Bukkit.getPlayer(data.getPlayerId()) : null;
            playLocalAnimation(data.getAnimationType(), data.getLocation(), player);
        }
    }
    
    public void cleanup() {
        // Cancel all tasks
        for (BukkitTask task : globalAnimationTasks.values()) {
            task.cancel();
        }
        globalAnimationTasks.clear();
        
        // Cleanup subsystems
        worldAnimationSystem.cleanup();
        islandAnimationSystem.cleanup();
        
        // Clear data
        serverAnimations.clear();
        playerAnimations.clear();
    }
    
    // Getters
    public WorldAnimationSystem getWorldAnimationSystem() { return worldAnimationSystem; }
    public IslandAnimationSystem getIslandAnimationSystem() { return islandAnimationSystem; }
    public Map<String, ServerAnimationData> getServerAnimations() { return serverAnimations; }
    public Map<UUID, PlayerAnimationData> getPlayerAnimations() { return playerAnimations; }
    
    // Inner classes
    public static class ServerAnimationData {
        private final String serverId;
        private final String serverName;
        private int playerCount;
        private long lastUpdate;
        private List<String> activeWorldAnimations = new ArrayList<>();
        private List<UUID> activeIslandAnimations = new ArrayList<>();
        
        public ServerAnimationData(String serverId, String serverName, int playerCount, long lastUpdate) {
            this.serverId = serverId;
            this.serverName = serverName;
            this.playerCount = playerCount;
            this.lastUpdate = lastUpdate;
        }
        
        // Getters and setters
        public String getServerId() { return serverId; }
        public String getServerName() { return serverName; }
        public int getPlayerCount() { return playerCount; }
        public void setPlayerCount(int playerCount) { this.playerCount = playerCount; }
        public long getLastUpdate() { return lastUpdate; }
        public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }
        public List<String> getActiveWorldAnimations() { return activeWorldAnimations; }
        public void setActiveWorldAnimations(List<String> activeWorldAnimations) { this.activeWorldAnimations = activeWorldAnimations; }
        public List<UUID> getActiveIslandAnimations() { return activeIslandAnimations; }
        public void setActiveIslandAnimations(List<UUID> activeIslandAnimations) { this.activeIslandAnimations = activeIslandAnimations; }
    }
    
    public static class PlayerAnimationData {
        private final UUID playerId;
        private final String currentAnimation;
        private final long startTime;
        private long lastUpdate;
        
        public PlayerAnimationData(UUID playerId, String currentAnimation, long startTime) {
            this.playerId = playerId;
            this.currentAnimation = currentAnimation;
            this.startTime = startTime;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        public String getCurrentAnimation() { return currentAnimation; }
        public long getStartTime() { return startTime; }
        public long getLastUpdate() { return lastUpdate; }
        public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }
    }
    
    public static class GlobalAnimationData {
        private final UUID animationId;
        private final String animationType;
        private final Location location;
        private final UUID playerId;
        private final String serverId;
        private final long timestamp;
        
        public GlobalAnimationData(UUID animationId, String animationType, Location location, UUID playerId, String serverId, long timestamp) {
            this.animationId = animationId;
            this.animationType = animationType;
            this.location = location;
            this.playerId = playerId;
            this.serverId = serverId;
            this.timestamp = timestamp;
        }
        
        // Getters
        public UUID getAnimationId() { return animationId; }
        public String getAnimationType() { return animationType; }
        public Location getLocation() { return location; }
        public UUID getPlayerId() { return playerId; }
        public String getServerId() { return serverId; }
        public long getTimestamp() { return timestamp; }
    }
}
