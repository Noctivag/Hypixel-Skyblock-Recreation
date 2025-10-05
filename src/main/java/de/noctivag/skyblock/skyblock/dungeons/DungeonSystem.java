package de.noctivag.skyblock.skyblock.dungeons;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Comprehensive Dungeon System inspired by Hypixel Skyblock
 * Features:
 * - All dungeon floors (F1-F7, M1-M7)
 * - Dungeon classes (Berserker, Archer, Healer, Mage, Tank)
 * - Dungeon items and equipment
 * - Dungeon score and rating system
 * - Dungeon rewards and loot
 * - Dungeon parties and matchmaking
 */
public class DungeonSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerDungeonData> playerDungeonData = new ConcurrentHashMap<>();
    private final Map<String, DungeonInstance> activeDungeons = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonParty> playerParties = new ConcurrentHashMap<>();
    
    public DungeonSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
        initializeDungeonFloors();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Load player dungeon data
        PlayerDungeonData data = loadPlayerDungeonData(playerId);
        playerDungeonData.put(playerId, data);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Save player dungeon data
        savePlayerDungeonData(playerId);
        
        // Remove from active dungeons
        removePlayerFromDungeon(player);
        
        // Remove from memory
        playerDungeonData.remove(playerId);
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        
        Player killer = event.getEntity().getKiller();
        DungeonInstance dungeon = getPlayerDungeon(killer);
        
        if (dungeon != null) {
            // Handle dungeon mob death
            handleDungeonMobDeath(dungeon, event.getEntity(), killer);
        }
    }
    
    private void initializeDungeonFloors() {
        // Initialize all dungeon floors with their configurations
        // This would load from config files
    }
    
    public void startDungeon(Player player, DungeonFloor floor, DungeonClass dungeonClass) {
        UUID playerId = player.getUniqueId();
        PlayerDungeonData data = playerDungeonData.get(playerId);
        
        if (data == null) return;
        
        // Check requirements
        if (!canEnterDungeon(player, floor)) {
            player.sendMessage(Component.text("§cYou don't meet the requirements for this dungeon floor!"));
            return;
        }
        
        // Create dungeon instance
        DungeonInstance dungeon = new DungeonInstance(floor, dungeonClass);
        dungeon.addPlayer(player);
        
        // Set player dungeon class
        data.setDungeonClass(dungeonClass);
        
        // Teleport player to dungeon
        teleportToDungeon(player, dungeon);
        
        // Start dungeon
        startDungeonInstance(dungeon);
        
        player.sendMessage("§aEntered " + floor.getDisplayName() + " as " + dungeonClass.getDisplayName() + "!");
    }
    
    public void joinDungeonParty(Player player, String partyId) {
        DungeonParty party = getDungeonParty(partyId);
        if (party == null) {
            player.sendMessage(Component.text("§cParty not found!"));
            return;
        }
        
        if (party.isFull()) {
            player.sendMessage(Component.text("§cParty is full!"));
            return;
        }
        
        party.addPlayer(player);
        playerParties.put(player.getUniqueId(), party);
        
        player.sendMessage(Component.text("§aJoined dungeon party!"));
    }
    
    public void createDungeonParty(Player leader) {
        DungeonParty party = new DungeonParty(leader);
        playerParties.put(leader.getUniqueId(), party);
        
        leader.sendMessage("§aCreated dungeon party! Use /dungeon invite <player> to invite others.");
    }
    
    private boolean canEnterDungeon(Player player, DungeonFloor floor) {
        PlayerDungeonData data = playerDungeonData.get(player.getUniqueId());
        if (data == null) return false;
        
        // Check catacombs level requirement
        if (data.getCatacombsLevel() < floor.getRequiredLevel()) {
            return false;
        }
        
        // Check other requirements (gear, etc.)
        return true;
    }
    
    private void teleportToDungeon(Player player, DungeonInstance dungeon) {
        // Create or get dungeon world
        World dungeonWorld = getDungeonWorld(dungeon.getFloor());
        
        // Teleport to dungeon spawn
        Location spawnLocation = getDungeonSpawn(dungeon.getFloor());
        player.teleport(spawnLocation);
    }
    
    private World getDungeonWorld(DungeonFloor floor) {
        // Get or create dungeon world for the floor
        String worldName = "dungeon_" + floor.getName().toLowerCase();
        World world = Bukkit.getWorld(worldName);
        
        if (world == null) {
            // Create new dungeon world
            // This would use world generation
        }
        
        return world;
    }
    
    private Location getDungeonSpawn(DungeonFloor floor) {
        // Get spawn location for the dungeon floor
        World world = getDungeonWorld(floor);
        return new Location(world, 0, 100, 0); // Default spawn
    }
    
    private void startDungeonInstance(DungeonInstance dungeon) {
        // Start dungeon timer and mechanics
        new BukkitRunnable() {
            @Override
            public void run() {
                if (dungeon.isCompleted() || dungeon.isFailed()) {
                    this.cancel();
                    return;
                }
                
                // Update dungeon progress
                updateDungeonProgress(dungeon);
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L); // Every second
    }
    
    private void updateDungeonProgress(DungeonInstance dungeon) {
        // Update dungeon progress, spawn mobs, etc.
        dungeon.updateProgress();
    }
    
    private void handleDungeonMobDeath(DungeonInstance dungeon, org.bukkit.entity.Entity mob, Player killer) {
        // Handle mob death in dungeon
        dungeon.addScore(DungeonScoreType.MOB_KILL, 1);
        
        // Give rewards
        giveDungeonRewards(killer, DungeonRewardType.MOB_KILL);
    }
    
    private void giveDungeonRewards(Player player, DungeonRewardType rewardType) {
        // Give dungeon rewards based on type
        switch (rewardType) {
            case MOB_KILL:
                // Give XP and coins
                break;
            case BOSS_KILL:
                // Give better rewards
                break;
            case DUNGEON_COMPLETE:
                // Give completion rewards
                break;
        }
    }
    
    private DungeonInstance getPlayerDungeon(Player player) {
        for (DungeonInstance dungeon : activeDungeons.values()) {
            if (dungeon.hasPlayer(player)) {
                return dungeon;
            }
        }
        return null;
    }
    
    private void removePlayerFromDungeon(Player player) {
        DungeonInstance dungeon = getPlayerDungeon(player);
        if (dungeon != null) {
            dungeon.removePlayer(player);
            
            if (dungeon.isEmpty()) {
                activeDungeons.remove(dungeon.getId());
            }
        }
    }
    
    private DungeonParty getDungeonParty(String partyId) {
        // Get dungeon party by ID
        return null; // Implementation needed
    }
    
    private PlayerDungeonData loadPlayerDungeonData(UUID playerId) {
        // Load from database or create new
        return new PlayerDungeonData(playerId);
    }
    
    private void savePlayerDungeonData(UUID playerId) {
        // Save to database
    }
    
    public enum DungeonFloor {
        F1("§aEntrance", "§7The entrance to the catacombs", 1, 0),
        F2("§eFloor 2", "§7Deeper into the catacombs", 2, 5),
        F3("§6Floor 3", "§7The catacombs grow darker", 3, 10),
        F4("§cFloor 4", "§7Ancient dangers await", 4, 15),
        F5("§5Floor 5", "§7The heart of the catacombs", 5, 20),
        F6("§dFloor 6", "§7Master level challenges", 6, 25),
        F7("§4Floor 7", "§7The ultimate challenge", 7, 30),
        M1("§8Master Floor 1", "§7Master mode begins", 8, 35),
        M2("§8Master Floor 2", "§7Master mode continues", 9, 40),
        M3("§8Master Floor 3", "§7Master mode deepens", 10, 45),
        M4("§8Master Floor 4", "§7Master mode intensifies", 11, 50),
        M5("§8Master Floor 5", "§7Master mode peaks", 12, 55),
        M6("§8Master Floor 6", "§7Master mode mastery", 13, 60),
        M7("§8Master Floor 7", "§7The ultimate master challenge", 14, 65);
        
        private final String displayName;
        private final String description;
        private final int floorNumber;
        private final int requiredLevel;
        
        DungeonFloor(String displayName, String description, int floorNumber, int requiredLevel) {
            this.displayName = displayName;
            this.description = description;
            this.floorNumber = floorNumber;
            this.requiredLevel = requiredLevel;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public int getFloorNumber() { return floorNumber; }
        public int getRequiredLevel() { return requiredLevel; }
        public String getName() { return name(); }
    }
    
    public enum DungeonClass {
        BERSERKER("§cBerserker", "§7High damage melee class", Material.DIAMOND_SWORD),
        ARCHER("§aArcher", "§7Ranged damage class", Material.BOW),
        HEALER("§dHealer", "§7Support and healing class", Material.GOLDEN_APPLE),
        MAGE("§bMage", "§7Magic damage class", Material.BLAZE_ROD),
        TANK("§eTank", "§7Defensive class", Material.SHIELD);
        
        private final String displayName;
        private final String description;
        private final Material icon;
        
        DungeonClass(String displayName, String description, Material icon) {
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
    }
    
    public enum DungeonScoreType {
        MOB_KILL, BOSS_KILL, SECRET_FOUND, PUZZLE_SOLVED, TIME_BONUS
    }
    
    public enum DungeonRewardType {
        MOB_KILL, BOSS_KILL, DUNGEON_COMPLETE, SECRET, PUZZLE
    }
    
    public static class DungeonInstance {
        private final String id;
        private final DungeonFloor floor;
        private final DungeonClass dungeonClass;
        private final List<Player> players;
        private final Map<DungeonScoreType, Integer> scores;
        private boolean completed;
        private boolean failed;
        private long startTime;
        private long endTime;
        
        public DungeonInstance(DungeonFloor floor, DungeonClass dungeonClass) {
            this.id = UUID.randomUUID().toString();
            this.floor = floor;
            this.dungeonClass = dungeonClass;
            this.players = new ArrayList<>();
            this.scores = new HashMap<>();
            this.completed = false;
            this.failed = false;
            this.startTime = java.lang.System.currentTimeMillis();
        }
        
        public void addPlayer(Player player) {
            players.add(player);
        }
        
        public void removePlayer(Player player) {
            players.remove(player);
        }
        
        public boolean hasPlayer(Player player) {
            return players.contains(player);
        }
        
        public boolean isEmpty() {
            return players.isEmpty();
        }
        
        public void addScore(DungeonScoreType type, int amount) {
            scores.put(type, scores.getOrDefault(type, 0) + amount);
        }
        
        public void updateProgress() {
            // Update dungeon progress
        }
        
        // Getters
        public String getId() { return id; }
        public DungeonFloor getFloor() { return floor; }
        public DungeonClass getDungeonClass() { return dungeonClass; }
        public List<Player> getPlayers() { return players; }
        public Map<DungeonScoreType, Integer> getScores() { return scores; }
        public boolean isCompleted() { return completed; }
        public boolean isFailed() { return failed; }
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
    }
    
    public static class DungeonParty {
        private final String id;
        private final Player leader;
        private final List<Player> members;
        private final int maxSize = 5;
        
        public DungeonParty(Player leader) {
            this.id = UUID.randomUUID().toString();
            this.leader = leader;
            this.members = new ArrayList<>();
            this.members.add(leader);
        }
        
        public void addPlayer(Player player) {
            if (!isFull()) {
                members.add(player);
            }
        }
        
        public void removePlayer(Player player) {
            members.remove(player);
        }
        
        public boolean isFull() {
            return members.size() >= maxSize;
        }
        
        // Getters
        public String getId() { return id; }
        public Player getLeader() { return leader; }
        public List<Player> getMembers() { return members; }
    }
    
    public static class PlayerDungeonData {
        private final UUID playerId;
        private int catacombsLevel;
        private int catacombsXP;
        private DungeonClass dungeonClass;
        private Map<DungeonFloor, Integer> floorCompletions;
        private Map<DungeonFloor, Integer> bestScores;
        private Map<DungeonFloor, Long> bestTimes;
        
        public PlayerDungeonData(UUID playerId) {
            this.playerId = playerId;
            this.catacombsLevel = 0;
            this.catacombsXP = 0;
            this.dungeonClass = DungeonClass.BERSERKER;
            this.floorCompletions = new HashMap<>();
            this.bestScores = new HashMap<>();
            this.bestTimes = new HashMap<>();
        }
        
        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        public int getCatacombsLevel() { return catacombsLevel; }
        public void setCatacombsLevel(int catacombsLevel) { this.catacombsLevel = catacombsLevel; }
        public int getCatacombsXP() { return catacombsXP; }
        public void setCatacombsXP(int catacombsXP) { this.catacombsXP = catacombsXP; }
        public DungeonClass getDungeonClass() { return dungeonClass; }
        public void setDungeonClass(DungeonClass dungeonClass) { this.dungeonClass = dungeonClass; }
        public Map<DungeonFloor, Integer> getFloorCompletions() { return floorCompletions; }
        public Map<DungeonFloor, Integer> getBestScores() { return bestScores; }
        public Map<DungeonFloor, Long> getBestTimes() { return bestTimes; }
    }
}
