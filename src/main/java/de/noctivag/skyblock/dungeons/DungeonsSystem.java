package de.noctivag.skyblock.dungeons;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Main Dungeons System - Manages dungeon sessions, classes, and progression
 */
public class DungeonsSystem implements Service, Listener {
    
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<String, DungeonSession> activeSessions;
    private final Map<UUID, String> playerSessions;
    private final Map<UUID, DungeonClass> playerClasses;
    private final BukkitTask dungeonTask;
    private SystemStatus status = SystemStatus.UNINITIALIZED;

    public DungeonsSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.activeSessions = new ConcurrentHashMap<>();
        this.playerSessions = new ConcurrentHashMap<>();
        this.playerClasses = new ConcurrentHashMap<>();
        
        // Start dungeon management task
        this.dungeonTask = new BukkitRunnable() {
            @Override
            public void run() {
                processDungeonSessions();
            }
        }.runTaskTimer(plugin, 20L, 20L); // Run every second
    }

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing DungeonsSystem...");

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Load all online player data
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayerDungeonData(player.getUniqueId());
        }

        status = SystemStatus.RUNNING;
        plugin.getLogger().info("DungeonsSystem initialized successfully!");
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down DungeonsSystem...");

        // Cancel dungeon task
        if (dungeonTask != null && !dungeonTask.isCancelled()) {
            dungeonTask.cancel();
        }

        // Save all player data
        saveAllPlayerDungeonData();

        // End all active sessions
        for (DungeonSession session : activeSessions.values()) {
            endSession(session.getSessionId());
        }

        activeSessions.clear();
        playerSessions.clear();
        playerClasses.clear();
        status = SystemStatus.SHUTDOWN;
        plugin.getLogger().info("DungeonsSystem shutdown complete!");
    }

    @Override
    public String getName() {
        return "DungeonsSystem";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        // Dungeons system is always enabled when running
    }

    /**
     * Load player dungeon data from database
     */
    public void loadPlayerDungeonData(UUID playerId) {
        try {
            // Load from database (placeholder - implement actual database loading)
            // For now, set default class
            playerClasses.put(playerId, DungeonClass.ARCHER);
            plugin.getLogger().info("Loaded dungeon data for player: " + playerId);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load dungeon data for player " + playerId + ": " + e.getMessage());
            // Set default class if loading fails
            playerClasses.put(playerId, DungeonClass.ARCHER);
        }
    }

    /**
     * Save player dungeon data to database
     */
    public void savePlayerDungeonData(UUID playerId) {
        try {
            // Save to database (placeholder - implement actual database saving)
            plugin.getLogger().info("Saved dungeon data for player: " + playerId);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save dungeon data for player " + playerId + ": " + e.getMessage());
        }
    }

    /**
     * Save all player dungeon data
     */
    public void saveAllPlayerDungeonData() {
        for (UUID playerId : playerClasses.keySet()) {
            savePlayerDungeonData(playerId);
        }
    }

    /**
     * Create a new dungeon session
     */
    public DungeonSession createSession(DungeonFloor floor, Location startLocation) {
        String sessionId = "dungeon_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
        DungeonSession session = new DungeonSession(sessionId, floor, startLocation);
        activeSessions.put(sessionId, session);
        return session;
    }

    /**
     * Join a dungeon session
     */
    public boolean joinSession(Player player, String sessionId) {
        DungeonSession session = activeSessions.get(sessionId);
        if (session == null) {
            return false; // Session not found
        }
        
        if (session.isFull()) {
            return false; // Session is full
        }
        
        DungeonClass playerClass = playerClasses.get(player.getUniqueId());
        if (playerClass == null) {
            playerClass = DungeonClass.ARCHER; // Default class
        }
        
        if (session.addPlayer(player, playerClass)) {
            playerSessions.put(player.getUniqueId(), sessionId);
            return true;
        }
        
        return false;
    }

    /**
     * Leave a dungeon session
     */
    public boolean leaveSession(Player player) {
        String sessionId = playerSessions.get(player.getUniqueId());
        if (sessionId == null) {
            return false; // Player not in a session
        }
        
        DungeonSession session = activeSessions.get(sessionId);
        if (session == null) {
            playerSessions.remove(player.getUniqueId());
            return false; // Session not found
        }
        
        if (session.removePlayer(player.getUniqueId())) {
            playerSessions.remove(player.getUniqueId());
            
            // End session if no players left
            if (session.getPlayerCount() == 0) {
                endSession(sessionId);
            }
            
            return true;
        }
        
        return false;
    }

    /**
     * End a dungeon session
     */
    public boolean endSession(String sessionId) {
        DungeonSession session = activeSessions.remove(sessionId);
        if (session == null) {
            return false; // Session not found
        }
        
        // Remove all players from session
        for (UUID playerId : session.getPlayers().keySet()) {
            playerSessions.remove(playerId);
        }
        
        return true;
    }

    /**
     * Get dungeon session by ID
     */
    public DungeonSession getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }

    /**
     * Get player's current session
     */
    public DungeonSession getPlayerSession(UUID playerId) {
        String sessionId = playerSessions.get(playerId);
        if (sessionId == null) return null;
        return activeSessions.get(sessionId);
    }

    /**
     * Get all active sessions
     */
    public Collection<DungeonSession> getAllActiveSessions() {
        return new ArrayList<>(activeSessions.values());
    }

    /**
     * Get sessions by floor
     */
    public List<DungeonSession> getSessionsByFloor(DungeonFloor floor) {
        return activeSessions.values().stream()
                .filter(session -> session.getFloor() == floor)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Set player's dungeon class
     */
    public boolean setPlayerClass(Player player, DungeonClass dungeonClass) {
        if (playerSessions.containsKey(player.getUniqueId())) {
            return false; // Player is in a dungeon session
        }
        
        playerClasses.put(player.getUniqueId(), dungeonClass);
        return true;
    }

    /**
     * Get player's dungeon class
     */
    public DungeonClass getPlayerClass(UUID playerId) {
        return playerClasses.getOrDefault(playerId, DungeonClass.ARCHER);
    }

    /**
     * Get player's dungeon class (by Player object)
     */
    public DungeonClass getPlayerClass(Player player) {
        return getPlayerClass(player.getUniqueId());
    }

    /**
     * Check if player is in a dungeon session
     */
    public boolean isPlayerInSession(UUID playerId) {
        return playerSessions.containsKey(playerId);
    }

    /**
     * Check if player is in a dungeon session (by Player object)
     */
    public boolean isPlayerInSession(Player player) {
        return isPlayerInSession(player.getUniqueId());
    }

    /**
     * Get available sessions for a floor
     */
    public List<DungeonSession> getAvailableSessions(DungeonFloor floor) {
        return activeSessions.values().stream()
                .filter(session -> session.getFloor() == floor && !session.isFull())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get sessions waiting for players
     */
    public List<DungeonSession> getWaitingSessions() {
        return activeSessions.values().stream()
                .filter(session -> !session.hasMinimumPlayers())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get sessions in progress
     */
    public List<DungeonSession> getInProgressSessions() {
        return activeSessions.values().stream()
                .filter(session -> session.hasMinimumPlayers())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Get dungeon statistics
     */
    public DungeonStatistics getDungeonStatistics() {
        return new DungeonStatistics(this);
    }

    /**
     * Process dungeon sessions
     */
    private void processDungeonSessions() {
        // Process session logic here
        // This would include dungeon progression, mob spawning, etc.
        // For now, just iterate through sessions to keep them active
        for (DungeonSession session : activeSessions.values()) {
            // Session processing logic would go here
            if (session.getPlayerCount() == 0) {
                // Remove empty sessions
                endSession(session.getSessionId());
            }
        }
    }

    /**
     * Get dungeon class by name
     */
    public DungeonClass getDungeonClassByName(String name) {
        for (DungeonClass dungeonClass : DungeonClass.values()) {
            if (dungeonClass.getDisplayName().equalsIgnoreCase(name)) {
                return dungeonClass;
            }
        }
        return null;
    }

    /**
     * Get dungeon floor by number
     */
    public DungeonFloor getDungeonFloorByNumber(int floorNumber) {
        return DungeonFloor.getByNumber(floorNumber);
    }

    /**
     * Get all dungeon classes
     */
    public DungeonClass[] getAllDungeonClasses() {
        return DungeonClass.values();
    }

    /**
     * Get all dungeon floors
     */
    public DungeonFloor[] getAllDungeonFloors() {
        return DungeonFloor.values();
    }

    /**
     * Get normal dungeon floors
     */
    public DungeonFloor[] getNormalDungeonFloors() {
        return DungeonFloor.getNormalFloors();
    }

    /**
     * Get master mode dungeon floors
     */
    public DungeonFloor[] getMasterModeDungeonFloors() {
        return DungeonFloor.getMasterModeFloors();
    }

    // Event Handlers

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadPlayerDungeonData(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        leaveSession(player);
        savePlayerDungeonData(player.getUniqueId());
    }

    /**
     * Dungeon statistics inner class
     */
    public static class DungeonStatistics {
        private final DungeonsSystem dungeonsSystem;
        private final int totalSessions;
        private final int activeSessions;
        private final int waitingSessions;
        private final int inProgressSessions;
        private final int totalPlayers;

        public DungeonStatistics(DungeonsSystem dungeonsSystem) {
            this.dungeonsSystem = dungeonsSystem;
            this.totalSessions = dungeonsSystem.getAllActiveSessions().size();
            this.activeSessions = dungeonsSystem.getAllActiveSessions().size();
            this.waitingSessions = dungeonsSystem.getWaitingSessions().size();
            this.inProgressSessions = dungeonsSystem.getInProgressSessions().size();
            this.totalPlayers = dungeonsSystem.playerSessions.size();
        }

        // Getters
        public DungeonsSystem getDungeonsSystem() { return dungeonsSystem; }
        public int getTotalSessions() { return totalSessions; }
        public int getActiveSessions() { return activeSessions; }
        public int getWaitingSessions() { return waitingSessions; }
        public int getInProgressSessions() { return inProgressSessions; }
        public int getTotalPlayers() { return totalPlayers; }

        /**
         * Get statistics summary
         */
        public String[] getStatisticsSummary() {
            return new String[]{
                "&7Dungeon Statistics:",
                "",
                "&7Total Sessions: &a" + totalSessions,
                "&7Active Sessions: &a" + activeSessions,
                "&7Waiting Sessions: &e" + waitingSessions,
                "&7In Progress: &b" + inProgressSessions,
                "&7Total Players: &a" + totalPlayers
            };
        }
    }
}
