package de.noctivag.plugin.dungeons;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

/**
 * Procedural Dungeon Generator - Generates consistent and fair, yet variable dungeon layouts
 * 
 * Features:
 * - Set of predefined room blueprints
 * - Fixed rules for dungeon generation
 * - Consistent and fair layouts
 * - Variable but controlled generation
 * - Boss room placement logic
 * - Loot room distribution
 * - Secret room integration
 */
public class ProceduralDungeonGenerator {
    
    private static final Logger logger = Logger.getLogger(ProceduralDungeonGenerator.class.getName());
    
    // Dungeon generation constants
    private static final int MIN_ROOM_COUNT = 8;
    private static final int MAX_ROOM_COUNT = 15;
    private static final int ROOM_SIZE = 16; // 16x16 rooms
    private static final int CORRIDOR_WIDTH = 3;
    private static final int MIN_BOSS_DISTANCE = 4; // Minimum distance from spawn to boss
    
    // Room type definitions
    public enum RoomType {
        SPAWN("Spawn Room", 1, true, false, false),
        CORRIDOR("Corridor", 0, false, false, false),
        COMBAT("Combat Room", 2, false, true, false),
        LOOT("Loot Room", 3, false, false, true),
        SECRET("Secret Room", 4, false, false, true),
        BOSS("Boss Room", 5, false, true, false),
        PUZZLE("Puzzle Room", 6, false, false, true),
        TRAP("Trap Room", 7, false, true, false);
        
        private final String displayName;
        private final int priority;
        private final boolean isSpawn;
        private final boolean hasMobs;
        private final boolean hasLoot;
        
        RoomType(String displayName, int priority, boolean isSpawn, boolean hasMobs, boolean hasLoot) {
            this.displayName = displayName;
            this.priority = priority;
            this.isSpawn = isSpawn;
            this.hasMobs = hasMobs;
            this.hasLoot = hasLoot;
        }
        
        // Getters
        public String getDisplayName() { return displayName; }
        public int getPriority() { return priority; }
        public boolean isSpawn() { return isSpawn; }
        public boolean hasMobs() { return hasMobs; }
        public boolean hasLoot() { return hasLoot; }
    }
    
    /**
     * Room Blueprint - Predefined room template
     */
    public static class RoomBlueprint {
        private final String blueprintId;
        private final RoomType roomType;
        private final int width;
        private final int height;
        private final int length;
        private final List<BlockPattern> blockPatterns;
        private final List<SpawnPoint> spawnPoints;
        private final List<LootChest> lootChests;
        private final List<Door> doors;
        
        public RoomBlueprint(String blueprintId, RoomType roomType, int width, int height, int length) {
            this.blueprintId = blueprintId;
            this.roomType = roomType;
            this.width = width;
            this.height = height;
            this.length = length;
            this.blockPatterns = new ArrayList<>();
            this.spawnPoints = new ArrayList<>();
            this.lootChests = new ArrayList<>();
            this.doors = new ArrayList<>();
        }
        
        // Getters
        public String getBlueprintId() { return blueprintId; }
        public RoomType getRoomType() { return roomType; }
        public int getWidth() { return width; }
        public int getHeight() { return height; }
        public int getLength() { return length; }
        public List<BlockPattern> getBlockPatterns() { return blockPatterns; }
        public List<SpawnPoint> getSpawnPoints() { return spawnPoints; }
        public List<LootChest> getLootChests() { return lootChests; }
        public List<Door> getDoors() { return doors; }
    }
    
    /**
     * Block Pattern - Defines block placement in rooms
     */
    public static class BlockPattern {
        private final Material material;
        private final int x;
        private final int y;
        private final int z;
        private final double probability; // 0.0 to 1.0
        
        public BlockPattern(Material material, int x, int y, int z, double probability) {
            this.material = material;
            this.x = x;
            this.y = y;
            this.z = z;
            this.probability = probability;
        }
        
        // Getters
        public Material getMaterial() { return material; }
        public int getX() { return x; }
        public int getY() { return y; }
        public int getZ() { return z; }
        public double getProbability() { return probability; }
    }
    
    /**
     * Spawn Point - Defines where mobs can spawn
     */
    public static class SpawnPoint {
        private final int x;
        private final int y;
        private final int z;
        private final String mobType;
        private final int maxCount;
        private final double spawnChance;
        
        public SpawnPoint(int x, int y, int z, String mobType, int maxCount, double spawnChance) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.mobType = mobType;
            this.maxCount = maxCount;
            this.spawnChance = spawnChance;
        }
        
        // Getters
        public int getX() { return x; }
        public int getY() { return y; }
        public int getZ() { return z; }
        public String getMobType() { return mobType; }
        public int getMaxCount() { return maxCount; }
        public double getSpawnChance() { return spawnChance; }
    }
    
    /**
     * Loot Chest - Defines loot chest placement
     */
    public static class LootChest {
        private final int x;
        private final int y;
        private final int z;
        private final String lootTable;
        private final double spawnChance;
        
        public LootChest(int x, int y, int z, String lootTable, double spawnChance) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.lootTable = lootTable;
            this.spawnChance = spawnChance;
        }
        
        // Getters
        public int getX() { return x; }
        public int getY() { return y; }
        public int getZ() { return z; }
        public String getLootTable() { return lootTable; }
        public double getSpawnChance() { return spawnChance; }
    }
    
    /**
     * Door - Defines door placement and connections
     */
    public static class Door {
        private final int x;
        private final int y;
        private final int z;
        private final DoorDirection direction;
        private final boolean isLocked;
        
        public Door(int x, int y, int z, DoorDirection direction, boolean isLocked) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.direction = direction;
            this.isLocked = isLocked;
        }
        
        // Getters
        public int getX() { return x; }
        public int getY() { return y; }
        public int getZ() { return z; }
        public DoorDirection getDirection() { return direction; }
        public boolean isLocked() { return isLocked; }
    }
    
    /**
     * Door Direction
     */
    public enum DoorDirection {
        NORTH(0, -1),
        SOUTH(0, 1),
        EAST(1, 0),
        WEST(-1, 0);
        
        private final int xOffset;
        private final int zOffset;
        
        DoorDirection(int xOffset, int zOffset) {
            this.xOffset = xOffset;
            this.zOffset = zOffset;
        }
        
        public int getXOffset() { return xOffset; }
        public int getZOffset() { return zOffset; }
    }
    
    /**
     * Dungeon Room - Generated room instance
     */
    public static class DungeonRoom {
        private final String roomId;
        private final RoomBlueprint blueprint;
        private final Location location;
        private final RoomType roomType;
        private final Map<String, Object> roomData;
        
        public DungeonRoom(String roomId, RoomBlueprint blueprint, Location location, RoomType roomType) {
            this.roomId = roomId;
            this.blueprint = blueprint;
            this.location = location;
            this.roomType = roomType;
            this.roomData = new ConcurrentHashMap<>();
        }
        
        // Getters
        public String getRoomId() { return roomId; }
        public RoomBlueprint getBlueprint() { return blueprint; }
        public Location getLocation() { return location; }
        public RoomType getRoomType() { return roomType; }
        public Map<String, Object> getRoomData() { return roomData; }
    }
    
    /**
     * Generated Dungeon Layout
     */
    public static class DungeonLayout {
        private final String dungeonId;
        private final List<DungeonRoom> rooms;
        private final Map<String, List<String>> connections;
        private final Location spawnLocation;
        private final Location bossLocation;
        private final int totalRooms;
        
        public DungeonLayout(String dungeonId, List<DungeonRoom> rooms, Map<String, List<String>> connections,
                           Location spawnLocation, Location bossLocation) {
            this.dungeonId = dungeonId;
            this.rooms = new ArrayList<>(rooms);
            this.connections = new HashMap<>(connections);
            this.spawnLocation = spawnLocation;
            this.bossLocation = bossLocation;
            this.totalRooms = rooms.size();
        }
        
        // Getters
        public String getDungeonId() { return dungeonId; }
        public List<DungeonRoom> getRooms() { return rooms; }
        public Map<String, List<String>> getConnections() { return connections; }
        public Location getSpawnLocation() { return spawnLocation; }
        public Location getBossLocation() { return bossLocation; }
        public int getTotalRooms() { return totalRooms; }
    }
    
    // Blueprint registry
    private final Map<String, RoomBlueprint> blueprintRegistry = new ConcurrentHashMap<>();
    
    public ProceduralDungeonGenerator() {
        initializeBlueprints();
    }
    
    /**
     * Initialize predefined room blueprints
     */
    private void initializeBlueprints() {
        // Spawn Room Blueprint
        RoomBlueprint spawnRoom = new RoomBlueprint("spawn_room_1", RoomType.SPAWN, ROOM_SIZE, 8, ROOM_SIZE);
        spawnRoom.getBlockPatterns().add(new BlockPattern(Material.STONE_BRICKS, 0, 0, 0, 1.0));
        spawnRoom.getBlockPatterns().add(new BlockPattern(Material.SPAWNER, 8, 1, 8, 1.0));
        registerBlueprint(spawnRoom);
        
        // Combat Room Blueprint
        RoomBlueprint combatRoom = new RoomBlueprint("combat_room_1", RoomType.COMBAT, ROOM_SIZE, 8, ROOM_SIZE);
        combatRoom.getBlockPatterns().add(new BlockPattern(Material.STONE_BRICKS, 0, 0, 0, 1.0));
        combatRoom.getSpawnPoints().add(new SpawnPoint(4, 1, 4, "SKELETON", 3, 0.8));
        combatRoom.getSpawnPoints().add(new SpawnPoint(12, 1, 12, "ZOMBIE", 2, 0.6));
        combatRoom.getDoors().add(new Door(8, 1, 0, DoorDirection.NORTH, false));
        combatRoom.getDoors().add(new Door(8, 1, 15, DoorDirection.SOUTH, false));
        registerBlueprint(combatRoom);
        
        // Loot Room Blueprint
        RoomBlueprint lootRoom = new RoomBlueprint("loot_room_1", RoomType.LOOT, ROOM_SIZE, 8, ROOM_SIZE);
        lootRoom.getBlockPatterns().add(new BlockPattern(Material.STONE_BRICKS, 0, 0, 0, 1.0));
        lootRoom.getLootChests().add(new LootChest(8, 1, 8, "dungeon_common", 0.9));
        lootRoom.getLootChests().add(new LootChest(4, 1, 4, "dungeon_rare", 0.3));
        lootRoom.getDoors().add(new Door(0, 1, 8, DoorDirection.WEST, true));
        registerBlueprint(lootRoom);
        
        // Boss Room Blueprint
        RoomBlueprint bossRoom = new RoomBlueprint("boss_room_1", RoomType.BOSS, ROOM_SIZE * 2, 12, ROOM_SIZE * 2);
        bossRoom.getBlockPatterns().add(new BlockPattern(Material.OBSIDIAN, 0, 0, 0, 1.0));
        bossRoom.getSpawnPoints().add(new SpawnPoint(16, 2, 16, "BOSS_NECRON", 1, 1.0));
        bossRoom.getDoors().add(new Door(0, 2, 16, DoorDirection.WEST, false));
        registerBlueprint(bossRoom);
        
        // Secret Room Blueprint
        RoomBlueprint secretRoom = new RoomBlueprint("secret_room_1", RoomType.SECRET, ROOM_SIZE / 2, 6, ROOM_SIZE / 2);
        secretRoom.getBlockPatterns().add(new BlockPattern(Material.MOSSY_STONE_BRICKS, 0, 0, 0, 1.0));
        secretRoom.getLootChests().add(new LootChest(4, 1, 4, "dungeon_epic", 1.0));
        registerBlueprint(secretRoom);
        
        logger.info("Initialized " + blueprintRegistry.size() + " room blueprints");
    }
    
    /**
     * Register a room blueprint
     */
    public void registerBlueprint(RoomBlueprint blueprint) {
        blueprintRegistry.put(blueprint.getBlueprintId(), blueprint);
        logger.info("Registered blueprint: " + blueprint.getBlueprintId());
    }
    
    /**
     * Generate a procedural dungeon layout
     */
    public CompletableFuture<DungeonLayout> generateDungeonLayout(Location startLocation, int difficulty) {
        return CompletableFuture.supplyAsync(() -> {
            String dungeonId = UUID.randomUUID().toString();
            
            // Determine room count based on difficulty
            int roomCount = Math.min(MAX_ROOM_COUNT, MIN_ROOM_COUNT + difficulty);
            
            // Generate room layout
            List<DungeonRoom> rooms = generateRoomLayout(startLocation, roomCount);
            
            // Generate connections between rooms
            Map<String, List<String>> connections = generateRoomConnections(rooms);
            
            // Find spawn and boss locations
            Location spawnLocation = findSpawnLocation(rooms);
            Location bossLocation = findBossLocation(rooms, spawnLocation);
            
            DungeonLayout layout = new DungeonLayout(dungeonId, rooms, connections, spawnLocation, bossLocation);
            
            logger.info("Generated dungeon layout with " + rooms.size() + " rooms");
            
            return layout;
        });
    }
    
    /**
     * Generate room layout
     */
    private List<DungeonRoom> generateRoomLayout(Location startLocation, int roomCount) {
        List<DungeonRoom> rooms = new ArrayList<>();
        Set<String> usedPositions = new HashSet<>();
        
        // Create spawn room
        DungeonRoom spawnRoom = createRoom("spawn", RoomType.SPAWN, startLocation, usedPositions);
        rooms.add(spawnRoom);
        
        // Generate additional rooms
        for (int i = 1; i < roomCount; i++) {
            RoomType roomType = determineRoomType(i, roomCount);
            Location roomLocation = findValidRoomLocation(startLocation, usedPositions);
            
            if (roomLocation != null) {
                DungeonRoom room = createRoom("room_" + i, roomType, roomLocation, usedPositions);
                rooms.add(room);
            }
        }
        
        return rooms;
    }
    
    /**
     * Create a room instance
     */
    private DungeonRoom createRoom(String roomId, RoomType roomType, Location location, Set<String> usedPositions) {
        RoomBlueprint blueprint = getRandomBlueprint(roomType);
        DungeonRoom room = new DungeonRoom(roomId, blueprint, location, roomType);
        
        // Mark position as used
        String positionKey = location.getBlockX() + "," + location.getBlockZ();
        usedPositions.add(positionKey);
        
        return room;
    }
    
    /**
     * Get random blueprint for room type
     */
    private RoomBlueprint getRandomBlueprint(RoomType roomType) {
        List<RoomBlueprint> matchingBlueprints = new ArrayList<>();
        
        for (RoomBlueprint blueprint : blueprintRegistry.values()) {
            if (blueprint.getRoomType() == roomType) {
                matchingBlueprints.add(blueprint);
            }
        }
        
        if (matchingBlueprints.isEmpty()) {
            // Fallback to first available blueprint
            return blueprintRegistry.values().iterator().next();
        }
        
        return matchingBlueprints.get(new Random().nextInt(matchingBlueprints.size()));
    }
    
    /**
     * Determine room type based on position and requirements
     */
    private RoomType determineRoomType(int roomIndex, int totalRooms) {
        Random random = new Random();
        double rand = random.nextDouble();
        
        // Ensure boss room is always last
        if (roomIndex == totalRooms - 1) {
            return RoomType.BOSS;
        }
        
        // Room type distribution
        if (rand < 0.4) { // 40% combat rooms
            return RoomType.COMBAT;
        } else if (rand < 0.6) { // 20% loot rooms
            return RoomType.LOOT;
        } else if (rand < 0.8) { // 20% puzzle rooms
            return RoomType.PUZZLE;
        } else if (rand < 0.9) { // 10% trap rooms
            return RoomType.TRAP;
        } else { // 10% secret rooms
            return RoomType.SECRET;
        }
    }
    
    /**
     * Find valid room location
     */
    private Location findValidRoomLocation(Location startLocation, Set<String> usedPositions) {
        Random random = new Random();
        
        for (int attempts = 0; attempts < 100; attempts++) {
            int offsetX = random.nextInt(200) - 100; // -100 to +100
            int offsetZ = random.nextInt(200) - 100; // -100 to +100
            
            Location candidateLocation = startLocation.clone().add(offsetX, 0, offsetZ);
            String positionKey = candidateLocation.getBlockX() + "," + candidateLocation.getBlockZ();
            
            if (!usedPositions.contains(positionKey)) {
                return candidateLocation;
            }
        }
        
        return null; // No valid location found
    }
    
    /**
     * Generate room connections
     */
    private Map<String, List<String>> generateRoomConnections(List<DungeonRoom> rooms) {
        Map<String, List<String>> connections = new HashMap<>();
        
        // Initialize connections
        for (DungeonRoom room : rooms) {
            connections.put(room.getRoomId(), new ArrayList<>());
        }
        
        // Create minimum spanning tree to ensure all rooms are connected
        createMinimumSpanningTree(rooms, connections);
        
        // Add additional connections for variety
        addAdditionalConnections(rooms, connections);
        
        return connections;
    }
    
    /**
     * Create minimum spanning tree for room connections
     */
    private void createMinimumSpanningTree(List<DungeonRoom> rooms, Map<String, List<String>> connections) {
        if (rooms.isEmpty()) return;
        
        Set<String> connected = new HashSet<>();
        connected.add(rooms.get(0).getRoomId());
        
        while (connected.size() < rooms.size()) {
            String closestRoom = null;
            double closestDistance = Double.MAX_VALUE;
            
            for (String connectedRoomId : connected) {
                DungeonRoom connectedRoom = findRoomById(rooms, connectedRoomId);
                
                for (DungeonRoom room : rooms) {
                    if (!connected.contains(room.getRoomId())) {
                        double distance = connectedRoom.getLocation().distance(room.getLocation());
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestRoom = room.getRoomId();
                        }
                    }
                }
            }
            
            if (closestRoom != null) {
                connected.add(closestRoom);
                // Add bidirectional connection
                connections.get(rooms.get(0).getRoomId()).add(closestRoom);
                connections.get(closestRoom).add(rooms.get(0).getRoomId());
            }
        }
    }
    
    /**
     * Add additional connections for variety
     */
    private void addAdditionalConnections(List<DungeonRoom> rooms, Map<String, List<String>> connections) {
        Random random = new Random();
        
        for (DungeonRoom room : rooms) {
            if (random.nextDouble() < 0.3) { // 30% chance for additional connection
                DungeonRoom targetRoom = rooms.get(random.nextInt(rooms.size()));
                if (!targetRoom.equals(room) && !connections.get(room.getRoomId()).contains(targetRoom.getRoomId())) {
                    connections.get(room.getRoomId()).add(targetRoom.getRoomId());
                    connections.get(targetRoom.getRoomId()).add(room.getRoomId());
                }
            }
        }
    }
    
    /**
     * Find room by ID
     */
    private DungeonRoom findRoomById(List<DungeonRoom> rooms, String roomId) {
        for (DungeonRoom room : rooms) {
            if (room.getRoomId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }
    
    /**
     * Find spawn location
     */
    private Location findSpawnLocation(List<DungeonRoom> rooms) {
        for (DungeonRoom room : rooms) {
            if (room.getRoomType() == RoomType.SPAWN) {
                return room.getLocation().clone().add(8, 1, 8); // Center of spawn room
            }
        }
        return rooms.get(0).getLocation(); // Fallback to first room
    }
    
    /**
     * Find boss location
     */
    private Location findBossLocation(List<DungeonRoom> rooms, Location spawnLocation) {
        DungeonRoom bossRoom = null;
        double maxDistance = 0;
        
        for (DungeonRoom room : rooms) {
            if (room.getRoomType() == RoomType.BOSS) {
                double distance = spawnLocation.distance(room.getLocation());
                if (distance > maxDistance) {
                    maxDistance = distance;
                    bossRoom = room;
                }
            }
        }
        
        if (bossRoom != null) {
            return bossRoom.getLocation().clone().add(16, 2, 16); // Center of boss room
        }
        
        return rooms.get(rooms.size() - 1).getLocation(); // Fallback to last room
    }
    
    /**
     * Get all registered blueprints
     */
    public Map<String, RoomBlueprint> getAllBlueprints() {
        return new HashMap<>(blueprintRegistry);
    }
    
    /**
     * Get blueprints by room type
     */
    public List<RoomBlueprint> getBlueprintsByType(RoomType roomType) {
        List<RoomBlueprint> matchingBlueprints = new ArrayList<>();
        
        for (RoomBlueprint blueprint : blueprintRegistry.values()) {
            if (blueprint.getRoomType() == roomType) {
                matchingBlueprints.add(blueprint);
            }
        }
        
        return matchingBlueprints;
    }
}
