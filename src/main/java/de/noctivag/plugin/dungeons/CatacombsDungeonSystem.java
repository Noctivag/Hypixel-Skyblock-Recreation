package de.noctivag.plugin.dungeons;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Catacombs Dungeon System - Implements F0-F7 dungeon instances
 * 
 * Features:
 * - Temporary, isolated dungeon instances
 * - Multi-floor progression (F0-F7)
 * - Party system integration
 * - Instance management and cleanup
 * - Floor-specific mechanics and rewards
 */
public class CatacombsDungeonSystem {
    
    private final org.bukkit.plugin.Plugin plugin;
    private final Map<UUID, DungeonInstance> activeInstances = new ConcurrentHashMap<>();
    private final Map<UUID, DungeonParty> activeParties = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerDungeonData> playerData = new ConcurrentHashMap<>();
    
    // Dungeon configuration
    private static final int MAX_PARTY_SIZE = 5;
    private static final int DUNGEON_TIMEOUT = 1800000L; // 30 minutes
    private static final String DUNGEON_WORLD_PREFIX = "catacombs_";
    
    public CatacombsDungeonSystem(org.bukkit.plugin.Plugin plugin) {
        this.plugin = plugin;
        startInstanceCleanupTask();
    }
    
    /**
     * Create a new dungeon party
     */
    public DungeonParty createParty(Player leader) {
        UUID partyId = UUID.randomUUID();
        DungeonParty party = new DungeonParty(partyId, leader);
        activeParties.put(partyId, party);
        
        leader.sendMessage("§a§lDungeon party created! Use §e/party invite <player> §ato invite members.");
        return party;
    }
    
    /**
     * Start a dungeon instance
     */
    public boolean startDungeon(DungeonParty party, DungeonFloor floor) {
        if (party.getMembers().size() < 1) {
            party.getLeader().sendMessage("§c§lYou need at least 1 player to start a dungeon!");
            return false;
        }
        
        if (party.getMembers().size() > MAX_PARTY_SIZE) {
            party.getLeader().sendMessage("§c§lParty is too large! Maximum size is " + MAX_PARTY_SIZE + " players.");
            return false;
        }
        
        // Check if all members are ready
        if (!party.areAllMembersReady()) {
            party.getLeader().sendMessage("§c§lNot all party members are ready!");
            return false;
        }
        
        // Create dungeon instance
        UUID instanceId = UUID.randomUUID();
        DungeonInstance instance = new DungeonInstance(instanceId, party, floor);
        
        // Create dungeon world
        World dungeonWorld = createDungeonWorld(instanceId, floor);
        if (dungeonWorld == null) {
            party.getLeader().sendMessage("§c§lFailed to create dungeon world!");
            return false;
        }
        
        instance.setWorld(dungeonWorld);
        activeInstances.put(instanceId, instance);
        
        // Teleport all party members to dungeon
        teleportPartyToDungeon(party, dungeonWorld, floor);
        
        // Start dungeon mechanics
        startDungeonMechanics(instance);
        
        // Announce dungeon start
        announceDungeonStart(party, floor);
        
        return true;
    }
    
    /**
     * Create dungeon world
     */
    private World createDungeonWorld(UUID instanceId, DungeonFloor floor) {
        String worldName = DUNGEON_WORLD_PREFIX + instanceId.toString();
        
        try {
            // Create world with specific generator for catacombs
            WorldCreator creator = new WorldCreator(worldName);
            creator.environment(World.Environment.NORMAL);
            creator.type(WorldType.FLAT);
            creator.generator(new CatacombsWorldGenerator(floor));
            
            World world = creator.createWorld();
            if (world != null) {
                world.setAutoSave(false); // Disable auto-save for dungeon worlds
                world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                world.setTime(18000); // Set to night
            }
            
            return world;
        } catch (Exception e) {
            System.err.println("Error creating dungeon world: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Teleport party to dungeon
     */
    private void teleportPartyToDungeon(DungeonParty party, World dungeonWorld, DungeonFloor floor) {
        Location spawnLocation = getDungeonSpawnLocation(dungeonWorld, floor);
        
        for (Player member : party.getMembers()) {
            member.teleport(spawnLocation);
            member.sendMessage("§a§lWelcome to Catacombs " + floor.getDisplayName() + "!");
            
            // Set player data
            PlayerDungeonData data = new PlayerDungeonData(member.getUniqueId(), floor);
            playerData.put(member.getUniqueId(), data);
        }
    }
    
    /**
     * Get dungeon spawn location
     */
    private Location getDungeonSpawnLocation(World world, DungeonFloor floor) {
        // Floor-specific spawn locations
        return switch (floor) {
            case F0 -> new Location(world, 0, 64, 0);
            case F1 -> new Location(world, 0, 64, 0);
            case F2 -> new Location(world, 0, 64, 0);
            case F3 -> new Location(world, 0, 64, 0);
            case F4 -> new Location(world, 0, 64, 0);
            case F5 -> new Location(world, 0, 64, 0);
            case F6 -> new Location(world, 0, 64, 0);
            case F7 -> new Location(world, 0, 64, 0);
        };
    }
    
    /**
     * Start dungeon mechanics
     */
    private void startDungeonMechanics(DungeonInstance instance) {
        DungeonFloor floor = instance.getFloor();
        
        // Start floor-specific mechanics
        switch (floor) {
            case F0 -> startF0Mechanics(instance);
            case F1 -> startF1Mechanics(instance);
            case F2 -> startF2Mechanics(instance);
            case F3 -> startF3Mechanics(instance);
            case F4 -> startF4Mechanics(instance);
            case F5 -> startF5Mechanics(instance);
            case F6 -> startF6Mechanics(instance);
            case F7 -> startF7Mechanics(instance);
        }
        
        // Start general dungeon mechanics
        startGeneralMechanics(instance);
    }
    
    /**
     * Start F7 mechanics (Necron boss)
     */
    private void startF7Mechanics(DungeonInstance instance) {
        // Spawn Necron boss after a delay
        new BukkitRunnable() {
            @Override
            public void run() {
                if (instance.isActive()) {
                    spawnNecronBoss(instance);
                }
            }
        }.runTaskLater(plugin, 200L); // 10 seconds delay
    }
    
    /**
     * Spawn Necron boss
     */
    private void spawnNecronBoss(DungeonInstance instance) {
        World world = instance.getWorld();
        Location bossLocation = new Location(world, 0, 70, 0);
        
        // Import and use NecronBoss class
        try {
            Class<?> necronClass = Class.forName("de.noctivag.plugin.dungeons.bosses.NecronBoss");
            Object necronBoss = necronClass.getConstructor(org.bukkit.plugin.Plugin.class)
                .newInstance(plugin);
            
            // Call spawn method
            necronClass.getMethod("spawnNecron", Location.class).invoke(necronBoss, bossLocation);
            
            // Announce boss spawn
            for (Player member : instance.getParty().getMembers()) {
                member.sendMessage("§c§lNECRON HAS AWAKENED!");
                member.sendMessage("§7§lDefeat Necron to complete the dungeon!");
            }
            
        } catch (Exception e) {
            System.err.println("Error spawning Necron boss: " + e.getMessage());
        }
    }
    
    /**
     * Start other floor mechanics (placeholder implementations)
     */
    private void startF0Mechanics(DungeonInstance instance) {
        // F0 mechanics
    }
    
    private void startF1Mechanics(DungeonInstance instance) {
        // F1 mechanics
    }
    
    private void startF2Mechanics(DungeonInstance instance) {
        // F2 mechanics
    }
    
    private void startF3Mechanics(DungeonInstance instance) {
        // F3 mechanics
    }
    
    private void startF4Mechanics(DungeonInstance instance) {
        // F4 mechanics
    }
    
    private void startF5Mechanics(DungeonInstance instance) {
        // F5 mechanics
    }
    
    private void startF6Mechanics(DungeonInstance instance) {
        // F6 mechanics
    }
    
    /**
     * Start general dungeon mechanics
     */
    private void startGeneralMechanics(DungeonInstance instance) {
        // Start dungeon timer
        startDungeonTimer(instance);
        
        // Start mob spawning
        startMobSpawning(instance);
        
        // Start score tracking
        startScoreTracking(instance);
    }
    
    /**
     * Start dungeon timer
     */
    private void startDungeonTimer(DungeonInstance instance) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!instance.isActive()) {
                    cancel();
                    return;
                }
                
                long elapsed = System.currentTimeMillis() - instance.getStartTime();
                long remaining = DUNGEON_TIMEOUT - elapsed;
                
                if (remaining <= 0) {
                    // Timeout reached
                    endDungeon(instance, DungeonResult.TIMEOUT);
                    cancel();
                    return;
                }
                
                // Update timer display
                updateTimerDisplay(instance, remaining);
            }
        }.runTaskTimer(plugin, 0L, 20L); // Update every second
    }
    
    /**
     * Start mob spawning
     */
    private void startMobSpawning(DungeonInstance instance) {
        // TODO: Implement mob spawning based on floor
    }
    
    /**
     * Start score tracking
     */
    private void startScoreTracking(DungeonInstance instance) {
        // TODO: Implement score tracking
    }
    
    /**
     * Update timer display
     */
    private void updateTimerDisplay(DungeonInstance instance, long remaining) {
        long minutes = remaining / 60000;
        long seconds = (remaining % 60000) / 1000;
        
        for (Player member : instance.getParty().getMembers()) {
            member.sendActionBar(net.kyori.adventure.text.Component.text(
                "§6§lTime Remaining: §f" + String.format("%02d:%02d", minutes, seconds)));
        }
    }
    
    /**
     * End dungeon
     */
    public void endDungeon(DungeonInstance instance, DungeonResult result) {
        DungeonParty party = instance.getParty();
        
        // Announce result
        announceDungeonEnd(party, result);
        
        // Give rewards
        giveDungeonRewards(instance, result);
        
        // Teleport players back
        teleportPartyBack(party);
        
        // Cleanup instance
        cleanupInstance(instance);
    }
    
    /**
     * Announce dungeon end
     */
    private void announceDungeonEnd(DungeonParty party, DungeonResult result) {
        String message = switch (result) {
            case SUCCESS -> "§a§lDungeon completed successfully!";
            case FAILED -> "§c§lDungeon failed!";
            case TIMEOUT -> "§e§lDungeon timed out!";
            case DISCONNECT -> "§7§lDungeon ended due to disconnection.";
        };
        
        for (Player member : party.getMembers()) {
            member.sendMessage(message);
        }
    }
    
    /**
     * Give dungeon rewards
     */
    private void giveDungeonRewards(DungeonInstance instance, DungeonResult result) {
        // TODO: Implement reward system
    }
    
    /**
     * Teleport party back to spawn
     */
    private void teleportPartyBack(DungeonParty party) {
        Location spawnLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
        
        for (Player member : party.getMembers()) {
            member.teleport(spawnLocation);
            playerData.remove(member.getUniqueId());
        }
    }
    
    /**
     * Cleanup instance
     */
    private void cleanupInstance(DungeonInstance instance) {
        // Unload and delete dungeon world
        World world = instance.getWorld();
        if (world != null) {
            Bukkit.unloadWorld(world, false);
            // TODO: Delete world folder
        }
        
        // Remove from active instances
        activeInstances.remove(instance.getInstanceId());
        
        // Remove party
        activeParties.remove(instance.getParty().getPartyId());
    }
    
    /**
     * Announce dungeon start
     */
    private void announceDungeonStart(DungeonParty party, DungeonFloor floor) {
        for (Player member : party.getMembers()) {
            member.sendMessage("§a§l=== CATACOMBS " + floor.getDisplayName() + " ===");
            member.sendMessage("§7§lGood luck, adventurers!");
        }
    }
    
    /**
     * Start instance cleanup task
     */
    private void startInstanceCleanupTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Cleanup inactive instances
                activeInstances.entrySet().removeIf(entry -> {
                    DungeonInstance instance = entry.getValue();
                    return !instance.isActive() || 
                           (System.currentTimeMillis() - instance.getStartTime()) > DUNGEON_TIMEOUT;
                });
            }
        }.runTaskTimer(plugin, 0L, 6000L); // Check every 5 minutes
    }
    
    /**
     * Get active instance for player
     */
    public DungeonInstance getPlayerInstance(Player player) {
        return activeInstances.values().stream()
            .filter(instance -> instance.getParty().getMembers().contains(player))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Get player dungeon data
     */
    public PlayerDungeonData getPlayerData(Player player) {
        return playerData.get(player.getUniqueId());
    }
    
    /**
     * Dungeon Result enum
     */
    public enum DungeonResult {
        SUCCESS, FAILED, TIMEOUT, DISCONNECT
    }
    
    /**
     * Catacombs World Generator
     */
    private static class CatacombsWorldGenerator implements org.bukkit.generator.ChunkGenerator {
        private final DungeonFloor floor;
        
        public CatacombsWorldGenerator(DungeonFloor floor) {
            this.floor = floor;
        }
        
        @Override
        public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            ChunkData chunk = createChunkData(world);
            
            // Generate floor-specific terrain
            generateFloorTerrain(chunk, x, z, floor);
            
            return chunk;
        }
        
        private void generateFloorTerrain(ChunkData chunk, int x, int z, DungeonFloor floor) {
            // Generate basic stone platform
            for (int y = 0; y < 64; y++) {
                for (int blockX = 0; blockX < 16; blockX++) {
                    for (int blockZ = 0; blockZ < 16; blockZ++) {
                        chunk.setBlock(blockX, y, blockZ, Material.STONE);
                    }
                }
            }
            
            // Add floor-specific structures
            // TODO: Implement floor-specific terrain generation
        }
    }
}
