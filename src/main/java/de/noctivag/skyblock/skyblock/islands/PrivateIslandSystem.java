package de.noctivag.skyblock.skyblock.islands;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import de.noctivag.skyblock.SkyblockPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Comprehensive Private Island System inspired by Hypixel Skyblock
 * Features:
 * - Private islands for each player
 * - Island customization and building
 * - Island visitors and permissions
 * - Island upgrades and expansions
 * - Island challenges and objectives
 * - Island teleportation and management
 * - Island reset and backup system
 */
public class PrivateIslandSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerIslandData> playerIslandData = new ConcurrentHashMap<>();
    private final Map<UUID, PrivateIsland> playerIslands = new ConcurrentHashMap<>();
    private final Map<String, PrivateIsland> islandWorlds = new ConcurrentHashMap<>();
    private final Map<UUID, Set<UUID>> islandVisitors = new ConcurrentHashMap<>();
    
    public PrivateIslandSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Load player island data
        PlayerIslandData data = loadPlayerIslandData(playerId);
        playerIslandData.put(playerId, data);
        
        // Create island if it doesn't exist
        if (!hasIsland(playerId)) {
            createIsland(player);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Save player island data
        savePlayerIslandData(playerId);
        
        // Remove from memory
        playerIslandData.remove(playerId);
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        
        if (to == null) return;
        
        // Check if teleporting to an island
        PrivateIsland island = getIslandByWorld(to.getWorld());
        if (island != null) {
            handleIslandTeleport(player, island, event);
        }
    }
    
    public void createIsland(Player player) {
        UUID playerId = player.getUniqueId();
        
        // Create island world
        String worldName = "island_" + playerId.toString().replace("-", "");
        World islandWorld = createIslandWorld(worldName);
        
        // Create island data
        PrivateIsland island = new PrivateIsland(playerId, islandWorld);
        playerIslands.put(playerId, island);
        islandWorlds.put(worldName, island);
        
        // Generate island
        generateIsland(island);
        
        // Set spawn location
        Location spawnLocation = new Location(islandWorld, 0, 100, 0);
        island.setSpawnLocation(spawnLocation);
        
        // Teleport player to island
        player.teleport(spawnLocation);
        
        player.sendMessage(Component.text("§aWelcome to your private island!"));
        player.sendMessage(Component.text("§7Use §e/island §7to manage your island."));
    }
    
    public void teleportToIsland(Player player) {
        UUID playerId = player.getUniqueId();
        PrivateIsland island = playerIslands.get(playerId);
        
        if (island == null) {
            player.sendMessage(Component.text("§cYou don't have an island! Creating one now..."));
            createIsland(player);
            return;
        }
        
        Location spawnLocation = island.getSpawnLocation();
        if (spawnLocation == null) {
            spawnLocation = new Location(island.getWorld(), 0, 100, 0);
            island.setSpawnLocation(spawnLocation);
        }
        
        player.teleport(spawnLocation);
        player.sendMessage(Component.text("§aTeleported to your private island!"));
    }
    
    public void visitIsland(Player visitor, String targetPlayerName) {
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            visitor.sendMessage("§cPlayer not found!");
            return;
        }
        
        UUID targetId = targetPlayer.getUniqueId();
        PrivateIsland island = playerIslands.get(targetId);
        
        if (island == null) {
            visitor.sendMessage("§cThis player doesn't have an island!");
            return;
        }
        
        // Check if island allows visitors
        if (!island.isPublic()) {
            visitor.sendMessage("§cThis island is private!");
            return;
        }
        
        // Add visitor
        islandVisitors.computeIfAbsent(targetId, k -> new HashSet<>()).add(visitor.getUniqueId());
        
        // Teleport visitor
        Location spawnLocation = island.getSpawnLocation();
        if (spawnLocation == null) {
            spawnLocation = new Location(island.getWorld(), 0, 100, 0);
        }
        
        visitor.teleport(spawnLocation);
        visitor.sendMessage("§aVisiting " + targetPlayerName + "'s island!");
        
        // Notify island owner
        if (targetPlayer.isOnline()) {
            targetPlayer.sendMessage("§e" + visitor.getName() + " is visiting your island!");
        }
    }
    
    public void expandIsland(Player player, IslandExpansion expansion) {
        UUID playerId = player.getUniqueId();
        PrivateIsland island = playerIslands.get(playerId);
        
        if (island == null) {
            player.sendMessage(Component.text("§cYou don't have an island!"));
            return;
        }
        
        // Check if player can afford the expansion
        double cost = expansion.getCost();
        // This would check player's coin balance
        
        // Check if expansion is already purchased
        if (island.hasExpansion(expansion)) {
            player.sendMessage(Component.text("§cYou already have this expansion!"));
            return;
        }
        
        // Purchase expansion
        island.addExpansion(expansion);
        
        // Apply expansion
        applyIslandExpansion(island, expansion);
        
        player.sendMessage("§aPurchased " + expansion.getDisplayName() + " for §6" + String.format("%.0f", cost) + " coins!");
    }
    
    public void resetIsland(Player player) {
        UUID playerId = player.getUniqueId();
        PrivateIsland island = playerIslands.get(playerId);
        
        if (island == null) {
            player.sendMessage(Component.text("§cYou don't have an island!"));
            return;
        }
        
        // Confirm reset
        player.sendMessage(Component.text("§cAre you sure you want to reset your island?"));
        player.sendMessage(Component.text("§7This will delete all your builds and items!"));
        player.sendMessage(Component.text("§7Type §e/island confirm §7to confirm the reset."));
        
        // This would implement a confirmation system
    }
    
    public void confirmIslandReset(Player player) {
        UUID playerId = player.getUniqueId();
        PrivateIsland island = playerIslands.get(playerId);
        
        if (island == null) {
            player.sendMessage(Component.text("§cYou don't have an island!"));
            return;
        }
        
        // Backup island data
        backupIsland(island);
        
        // Reset island
        resetIslandWorld(island);
        
        // Regenerate island
        generateIsland(island);
        
        player.sendMessage(Component.text("§aYour island has been reset!"));
    }
    
    private World createIslandWorld(String worldName) {
        WorldCreator creator = new WorldCreator(worldName);
        creator.type(org.bukkit.WorldType.FLAT);
        creator.generatorSettings("3;minecraft:air;2;minecraft:air");
        
        World world = creator.createWorld();
        if (world != null) {
            world.setSpawnFlags(false, false); // Disable mob spawning
            world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, false);
        }
        
        return world;
    }
    
    private void generateIsland(PrivateIsland island) {
        World world = island.getWorld();
        
        // Generate basic island structure
        generateBasicIsland(world);
        
        // Add starter items
        addStarterItems(island);
        
        // Apply expansions
        for (IslandExpansion expansion : island.getExpansions()) {
            applyIslandExpansion(island, expansion);
        }
    }
    
    private void generateBasicIsland(World world) {
        // Generate a basic island structure
        Location center = new Location(world, 0, 100, 0);
        
        // Create island base
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                Location loc = center.clone().add(x, 0, z);
                
                // Create island base
                if (x * x + z * z <= 100) { // Circular island
                    loc.getBlock().setType(Material.GRASS_BLOCK);
                    
                    // Add some dirt layers
                    for (int y = -1; y >= -3; y--) {
                        Location dirtLoc = loc.clone().add(0, y, 0);
                        dirtLoc.getBlock().setType(Material.DIRT);
                    }
                }
            }
        }
        
        // Add some starter blocks
        center.clone().add(0, 1, 0).getBlock().setType(Material.CHEST);
        center.clone().add(1, 1, 0).getBlock().setType(Material.CRAFTING_TABLE);
        center.clone().add(-1, 1, 0).getBlock().setType(Material.FURNACE);
    }
    
    private void addStarterItems(PrivateIsland island) {
        // Add starter items to island chest
        Location chestLocation = island.getSpawnLocation().clone().add(0, 1, 0);
        
        if (chestLocation.getBlock().getType() == Material.CHEST) {
            org.bukkit.block.Chest chest = (org.bukkit.block.Chest) chestLocation.getBlock().getState();
            
            // Add starter items
            chest.getInventory().addItem(new ItemStack(Material.WHEAT_SEEDS, 16));
            chest.getInventory().addItem(new ItemStack(Material.CARROT, 8));
            chest.getInventory().addItem(new ItemStack(Material.POTATO, 8));
            chest.getInventory().addItem(new ItemStack(Material.OAK_SAPLING, 4));
            chest.getInventory().addItem(new ItemStack(Material.BREAD, 8));
            chest.getInventory().addItem(new ItemStack(Material.WOODEN_PICKAXE, 1));
            chest.getInventory().addItem(new ItemStack(Material.WOODEN_AXE, 1));
            chest.getInventory().addItem(new ItemStack(Material.WOODEN_SHOVEL, 1));
        }
    }
    
    private void applyIslandExpansion(PrivateIsland island, IslandExpansion expansion) {
        World world = island.getWorld();
        
        switch (expansion) {
            case SIZE_UPGRADE_1:
                expandIslandSize(world, 20); // Expand to 20x20
                break;
            case SIZE_UPGRADE_2:
                expandIslandSize(world, 30); // Expand to 30x30
                break;
            case SIZE_UPGRADE_3:
                expandIslandSize(world, 40); // Expand to 40x40
                break;
            case MINION_SLOTS_1:
                // Add minion slots
                break;
            case MINION_SLOTS_2:
                // Add more minion slots
                break;
            case COAL_MINE:
                generateCoalMine(world);
                break;
            case IRON_MINE:
                generateIronMine(world);
                break;
            case GOLD_MINE:
                generateGoldMine(world);
                break;
            case DIAMOND_MINE:
                generateDiamondMine(world);
                break;
        }
    }
    
    private void expandIslandSize(World world, int newRadius) {
        Location center = new Location(world, 0, 100, 0);
        
        // Expand island
        for (int x = -newRadius; x <= newRadius; x++) {
            for (int z = -newRadius; z <= newRadius; z++) {
                Location loc = center.clone().add(x, 0, z);
                
                // Only add blocks if they don't exist
                if (loc.getBlock().getType() == Material.AIR && x * x + z * z <= newRadius * newRadius) {
                    loc.getBlock().setType(Material.GRASS_BLOCK);
                    
                    // Add dirt layers
                    for (int y = -1; y >= -3; y--) {
                        Location dirtLoc = loc.clone().add(0, y, 0);
                        if (dirtLoc.getBlock().getType() == Material.AIR) {
                            dirtLoc.getBlock().setType(Material.DIRT);
                        }
                    }
                }
            }
        }
    }
    
    private void generateCoalMine(World world) {
        Location mineLocation = new Location(world, 15, 95, 0);
        generateMine(mineLocation, Material.COAL_ORE, 10);
    }
    
    private void generateIronMine(World world) {
        Location mineLocation = new Location(world, -15, 95, 0);
        generateMine(mineLocation, Material.IRON_ORE, 8);
    }
    
    private void generateGoldMine(World world) {
        Location mineLocation = new Location(world, 0, 95, 15);
        generateMine(mineLocation, Material.GOLD_ORE, 6);
    }
    
    private void generateDiamondMine(World world) {
        Location mineLocation = new Location(world, 0, 95, -15);
        generateMine(mineLocation, Material.DIAMOND_ORE, 4);
    }
    
    private void generateMine(Location center, Material oreType, int oreCount) {
        Random random = new Random();
        
        for (int i = 0; i < oreCount; i++) {
            int x = random.nextInt(10) - 5;
            int y = random.nextInt(10) - 5;
            int z = random.nextInt(10) - 5;
            
            Location oreLocation = center.clone().add(x, y, z);
            oreLocation.getBlock().setType(oreType);
        }
    }
    
    private void handleIslandTeleport(Player player, PrivateIsland island, PlayerTeleportEvent event) {
        // Handle island-specific teleport logic
        if (player.getUniqueId().equals(island.getOwnerId())) {
            // Owner teleporting to their island
            return;
        }
        
        // Check if player is allowed to visit
        Set<UUID> visitors = islandVisitors.get(island.getOwnerId());
        if (visitors == null || !visitors.contains(player.getUniqueId())) {
            if (!island.isPublic()) {
                event.setCancelled(true);
                player.sendMessage(Component.text("§cYou don't have permission to visit this island!"));
            }
        }
    }
    
    private void backupIsland(PrivateIsland island) {
        // Backup island data to database
        // This would implement island backup functionality
    }
    
    private void resetIslandWorld(PrivateIsland island) {
        World world = island.getWorld();
        
        // Clear all blocks in the world
        for (int x = -100; x <= 100; x++) {
            for (int z = -100; z <= 100; z++) {
                for (int y = 0; y <= 256; y++) {
                    Location loc = new Location(world, x, y, z);
                    loc.getBlock().setType(Material.AIR);
                }
            }
        }
    }
    
    private boolean hasIsland(UUID playerId) {
        return playerIslands.containsKey(playerId);
    }
    
    private PrivateIsland getIslandByWorld(World world) {
        return islandWorlds.get(world.getName());
    }
    
    private PlayerIslandData loadPlayerIslandData(UUID playerId) {
        // Load from database or create new
        return new PlayerIslandData(playerId);
    }
    
    private void savePlayerIslandData(UUID playerId) {
        // Save to database
    }
    
    public enum IslandExpansion {
        SIZE_UPGRADE_1("§aIsland Size Upgrade I", "§7Increases island size to 20x20", 10000.0),
        SIZE_UPGRADE_2("§aIsland Size Upgrade II", "§7Increases island size to 30x30", 50000.0),
        SIZE_UPGRADE_3("§aIsland Size Upgrade III", "§7Increases island size to 40x40", 250000.0),
        MINION_SLOTS_1("§eMinion Slots I", "§7Adds 2 minion slots", 5000.0),
        MINION_SLOTS_2("§eMinion Slots II", "§7Adds 2 more minion slots", 25000.0),
        COAL_MINE("§8Coal Mine", "§7Adds a coal mine to your island", 15000.0),
        IRON_MINE("§fIron Mine", "§7Adds an iron mine to your island", 50000.0),
        GOLD_MINE("§6Gold Mine", "§7Adds a gold mine to your island", 150000.0),
        DIAMOND_MINE("§bDiamond Mine", "§7Adds a diamond mine to your island", 500000.0);
        
        private final String displayName;
        private final String description;
        private final double cost;
        
        IslandExpansion(String displayName, String description, double cost) {
            this.displayName = displayName;
            this.description = description;
            this.cost = cost;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public double getCost() { return cost; }
    }
    
    public static class PrivateIsland {
        private final UUID ownerId;
        private final World world;
        private Location spawnLocation;
        private final Set<IslandExpansion> expansions;
        private boolean isPublic;
        private final long creationTime;
        
        public PrivateIsland(UUID ownerId, World world) {
            this.ownerId = ownerId;
            this.world = world;
            this.expansions = new HashSet<>();
            this.isPublic = false;
            this.creationTime = java.lang.System.currentTimeMillis();
        }
        
        public void addExpansion(IslandExpansion expansion) {
            expansions.add(expansion);
        }
        
        public boolean hasExpansion(IslandExpansion expansion) {
            return expansions.contains(expansion);
        }
        
        // Getters and setters
        public UUID getOwnerId() { return ownerId; }
        public World getWorld() { return world; }
        public Location getSpawnLocation() { return spawnLocation; }
        public void setSpawnLocation(Location spawnLocation) { this.spawnLocation = spawnLocation; }
        public Set<IslandExpansion> getExpansions() { return expansions; }
        public boolean isPublic() { return isPublic; }
        public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
        public long getCreationTime() { return creationTime; }
    }
    
    public static class PlayerIslandData {
        private final UUID playerId;
        private int islandLevel;
        private double islandXP;
        private int visitorsCount;
        private long totalPlayTime;
        private final Map<String, Object> customData;
        
        public PlayerIslandData(UUID playerId) {
            this.playerId = playerId;
            this.islandLevel = 1;
            this.islandXP = 0.0;
            this.visitorsCount = 0;
            this.totalPlayTime = 0;
            this.customData = new HashMap<>();
        }
        
        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        public int getIslandLevel() { return islandLevel; }
        public void setIslandLevel(int islandLevel) { this.islandLevel = islandLevel; }
        public double getIslandXP() { return islandXP; }
        public void setIslandXP(double islandXP) { this.islandXP = islandXP; }
        public int getVisitorsCount() { return visitorsCount; }
        public void setVisitorsCount(int visitorsCount) { this.visitorsCount = visitorsCount; }
        public long getTotalPlayTime() { return totalPlayTime; }
        public void setTotalPlayTime(long totalPlayTime) { this.totalPlayTime = totalPlayTime; }
        public Map<String, Object> getCustomData() { return customData; }
    }
}
