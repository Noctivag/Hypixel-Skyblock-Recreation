package de.noctivag.plugin.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MiningSystem implements Listener {
    private final Plugin plugin;
    private final Map<Location, BlockData> blockRegeneration = new ConcurrentHashMap<>();
    private final Map<Location, Thread> regenerationTasks = new ConcurrentHashMap<>();
    private final Map<String, MiningArea> miningAreas = new HashMap<>();
    
    public MiningSystem(Plugin plugin) {
        this.plugin = plugin;
        initializeMiningAreas();
        startBlockRegenerationTimer();
    }
    
    private void initializeMiningAreas() {
        // Create different mining areas like in Hypixel Skyblock
        
        // Deep Caverns
        MiningArea deepCaverns = new MiningArea("deep_caverns", "Deep Caverns", 
            new Location(Bukkit.getWorlds().get(0), 0, 50, 0),
            new Location(Bukkit.getWorlds().get(0), 100, 100, 100));
        deepCaverns.addAllowedBlock(Material.COBBLESTONE, 1, 0);
        deepCaverns.addAllowedBlock(Material.COAL_ORE, 2, 1);
        deepCaverns.addAllowedBlock(Material.IRON_ORE, 3, 2);
        deepCaverns.addAllowedBlock(Material.GOLD_ORE, 4, 3);
        deepCaverns.addAllowedBlock(Material.DIAMOND_ORE, 5, 4);
        deepCaverns.addAllowedBlock(Material.EMERALD_ORE, 6, 5);
        deepCaverns.addAllowedBlock(Material.REDSTONE_ORE, 2, 1);
        deepCaverns.addAllowedBlock(Material.LAPIS_ORE, 3, 2);
        deepCaverns.addAllowedBlock(Material.NETHER_QUARTZ_ORE, 4, 3);
        deepCaverns.addAllowedBlock(Material.OBSIDIAN, 7, 6);
        miningAreas.put("deep_caverns", deepCaverns);
        
        // Dwarven Mines
        MiningArea dwarvenMines = new MiningArea("dwarven_mines", "Dwarven Mines",
            new Location(Bukkit.getWorlds().get(0), 200, 50, 200),
            new Location(Bukkit.getWorlds().get(0), 300, 100, 300));
        dwarvenMines.addAllowedBlock(Material.STONE, 1, 0);
        dwarvenMines.addAllowedBlock(Material.COAL_ORE, 2, 1);
        dwarvenMines.addAllowedBlock(Material.IRON_ORE, 3, 2);
        dwarvenMines.addAllowedBlock(Material.GOLD_ORE, 4, 3);
        dwarvenMines.addAllowedBlock(Material.DIAMOND_ORE, 5, 4);
        dwarvenMines.addAllowedBlock(Material.EMERALD_ORE, 6, 5);
        dwarvenMines.addAllowedBlock(Material.REDSTONE_ORE, 2, 1);
        dwarvenMines.addAllowedBlock(Material.LAPIS_ORE, 3, 2);
        dwarvenMines.addAllowedBlock(Material.NETHER_QUARTZ_ORE, 4, 3);
        dwarvenMines.addAllowedBlock(Material.OBSIDIAN, 7, 6);
        dwarvenMines.addAllowedBlock(Material.ANCIENT_DEBRIS, 8, 7);
        dwarvenMines.addAllowedBlock(Material.ANCIENT_DEBRIS, 9, 8);
        miningAreas.put("dwarven_mines", dwarvenMines);
        
        // Crystal Hollows
        MiningArea crystalHollows = new MiningArea("crystal_hollows", "Crystal Hollows",
            new Location(Bukkit.getWorlds().get(0), 400, 50, 400),
            new Location(Bukkit.getWorlds().get(0), 500, 100, 500));
        crystalHollows.addAllowedBlock(Material.END_STONE, 1, 0);
        crystalHollows.addAllowedBlock(Material.END_STONE_BRICKS, 2, 1);
        crystalHollows.addAllowedBlock(Material.PURPUR_BLOCK, 3, 2);
        crystalHollows.addAllowedBlock(Material.PURPUR_PILLAR, 4, 3);
        crystalHollows.addAllowedBlock(Material.END_ROD, 5, 4);
        crystalHollows.addAllowedBlock(Material.END_CRYSTAL, 6, 5);
        crystalHollows.addAllowedBlock(Material.DRAGON_EGG, 7, 6);
        crystalHollows.addAllowedBlock(Material.ELYTRA, 8, 7);
        crystalHollows.addAllowedBlock(Material.SHULKER_BOX, 9, 8);
        miningAreas.put("crystal_hollows", crystalHollows);
        
        // The End
        MiningArea theEnd = new MiningArea("the_end", "The End",
            new Location(Bukkit.getWorlds().get(0), 600, 50, 600),
            new Location(Bukkit.getWorlds().get(0), 700, 100, 700));
        theEnd.addAllowedBlock(Material.END_STONE, 1, 0);
        theEnd.addAllowedBlock(Material.END_STONE_BRICKS, 2, 1);
        theEnd.addAllowedBlock(Material.PURPUR_BLOCK, 3, 2);
        theEnd.addAllowedBlock(Material.PURPUR_PILLAR, 4, 3);
        theEnd.addAllowedBlock(Material.END_ROD, 5, 4);
        theEnd.addAllowedBlock(Material.END_CRYSTAL, 6, 5);
        theEnd.addAllowedBlock(Material.DRAGON_EGG, 7, 6);
        theEnd.addAllowedBlock(Material.ELYTRA, 8, 7);
        theEnd.addAllowedBlock(Material.SHULKER_BOX, 9, 8);
        theEnd.addAllowedBlock(Material.ENDER_CHEST, 10, 9);
        miningAreas.put("the_end", theEnd);
        
        // The Nether
        MiningArea theNether = new MiningArea("the_nether", "The Nether",
            new Location(Bukkit.getWorlds().get(0), 800, 50, 800),
            new Location(Bukkit.getWorlds().get(0), 900, 100, 900));
        theNether.addAllowedBlock(Material.NETHERRACK, 1, 0);
        theNether.addAllowedBlock(Material.NETHER_BRICK, 2, 1);
        theNether.addAllowedBlock(Material.NETHER_BRICK_FENCE, 3, 2);
        theNether.addAllowedBlock(Material.NETHER_BRICK_STAIRS, 4, 3);
        theNether.addAllowedBlock(Material.NETHER_BRICK_SLAB, 5, 4);
        theNether.addAllowedBlock(Material.NETHER_BRICK_WALL, 6, 5);
        theNether.addAllowedBlock(Material.NETHER_WART_BLOCK, 7, 6);
        theNether.addAllowedBlock(Material.WARPED_WART_BLOCK, 8, 7);
        theNether.addAllowedBlock(Material.CRIMSON_NYLIUM, 9, 8);
        theNether.addAllowedBlock(Material.WARPED_NYLIUM, 10, 9);
        theNether.addAllowedBlock(Material.ANCIENT_DEBRIS, 11, 10);
        theNether.addAllowedBlock(Material.ANCIENT_DEBRIS, 12, 11);
        miningAreas.put("the_nether", theNether);
        
        // Overworld
        MiningArea overworld = new MiningArea("overworld", "Overworld",
            new Location(Bukkit.getWorlds().get(0), 1000, 50, 1000),
            new Location(Bukkit.getWorlds().get(0), 1100, 100, 1100));
        overworld.addAllowedBlock(Material.STONE, 1, 0);
        overworld.addAllowedBlock(Material.COBBLESTONE, 1, 0);
        overworld.addAllowedBlock(Material.COAL_ORE, 2, 1);
        overworld.addAllowedBlock(Material.IRON_ORE, 3, 2);
        overworld.addAllowedBlock(Material.GOLD_ORE, 4, 3);
        overworld.addAllowedBlock(Material.DIAMOND_ORE, 5, 4);
        overworld.addAllowedBlock(Material.EMERALD_ORE, 6, 5);
        overworld.addAllowedBlock(Material.REDSTONE_ORE, 2, 1);
        overworld.addAllowedBlock(Material.LAPIS_ORE, 3, 2);
        overworld.addAllowedBlock(Material.NETHER_QUARTZ_ORE, 4, 3);
        overworld.addAllowedBlock(Material.OBSIDIAN, 7, 6);
        overworld.addAllowedBlock(Material.ANCIENT_DEBRIS, 8, 7);
        overworld.addAllowedBlock(Material.ANCIENT_DEBRIS, 9, 8);
        miningAreas.put("overworld", overworld);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();
        
        // Check if player is in creative mode - allow them to break any block
        if (player.getGameMode() == GameMode.CREATIVE) {
            return; // Allow creative mode players to break any block
        }
        
        // Check if block is in a mining area
        MiningArea area = getMiningArea(location);
        if (area == null) {
            // Block is not in a mining area, allow breaking
            return;
        }
        
        // Check if player has required level to break this block
        if (!canPlayerBreakBlock(player, block, area)) {
            event.setCancelled(true);
            player.sendMessage("§cYou need a higher mining level to break this block!");
            return;
        }
        
        // Store original block data for regeneration
        BlockData blockData = new BlockData(block.getType(), block.getBlockData());
        blockRegeneration.put(location, blockData);
        
        // Start regeneration timer
        startBlockRegeneration(location, blockData, area);
        
        // Give mining XP
        giveMiningXP(player, block, area);
        
        // Add to collection
        plugin.getSkyblockManager().addCollection(player, block.getType(), 1);
        
        // Give block drops
        giveBlockDrops(player, block, area);
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();
        
        // Check if player is in creative mode - allow them to place any block
        if (player.getGameMode() == GameMode.CREATIVE) {
            return; // Allow creative mode players to place any block
        }
        
        // Check if block is in a mining area
        MiningArea area = getMiningArea(location);
        if (area == null) {
            // Block is not in a mining area, allow placing
            return;
        }
        
        // Check if player has required level to place this block
        if (!canPlayerPlaceBlock(player, block, area)) {
            event.setCancelled(true);
            player.sendMessage("§cYou need a higher mining level to place this block!");
            return;
        }
        
        // Store original block data for regeneration
        BlockData blockData = new BlockData(block.getType(), block.getBlockData());
        blockRegeneration.put(location, blockData);
        
        // Start regeneration timer
        startBlockRegeneration(location, blockData, area);
    }
    
    private boolean canPlayerBreakBlock(Player player, Block block, MiningArea area) {
        // Get player's mining level
        int playerLevel = plugin.getSkyblockManager().getSkills(player.getUniqueId())
            .getLevel(SkyblockManager.SkyblockSkill.MINING);
        
        // Get required level for this block
        int requiredLevel = area.getRequiredLevel(block.getType());
        
        return playerLevel >= requiredLevel;
    }
    
    private boolean canPlayerPlaceBlock(Player player, Block block, MiningArea area) {
        // Get player's mining level
        int playerLevel = plugin.getSkyblockManager().getSkills(player.getUniqueId())
            .getLevel(SkyblockManager.SkyblockSkill.MINING);
        
        // Get required level for this block
        int requiredLevel = area.getRequiredLevel(block.getType());
        
        return playerLevel >= requiredLevel;
    }
    
    private void giveMiningXP(Player player, Block block, MiningArea area) {
        // Get XP amount for this block
        double xp = area.getXPAmount(block.getType());
        
        // Add mining XP
        plugin.getSkyblockManager().addSkillXP(player, SkyblockManager.SkyblockSkill.MINING, xp);
        
        // Add foraging XP for certain blocks
        if (block.getType().name().contains("LOG") || block.getType().name().contains("LEAVES")) {
            plugin.getSkyblockManager().addSkillXP(player, SkyblockManager.SkyblockSkill.FORAGING, xp);
        }
    }
    
    private void giveBlockDrops(Player player, Block block, MiningArea area) {
        // Get drop amount for this block
        int dropAmount = area.getDropAmount(block.getType());
        
        // Give drops
        player.getInventory().addItem(new ItemStack(block.getType(), dropAmount));
        
        // Give bonus drops based on area
        giveBonusDrops(player, block, area);
    }
    
    private void giveBonusDrops(Player player, Block block, MiningArea area) {
        // Give bonus drops based on area and block type
        switch (area.getId()) {
            case "deep_caverns" -> {
                if (block.getType() == Material.DIAMOND_ORE) {
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                }
                if (block.getType() == Material.EMERALD_ORE) {
                    player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
                }
            }
            case "dwarven_mines" -> {
                if (block.getType() == Material.ANCIENT_DEBRIS) {
                    player.getInventory().addItem(new ItemStack(Material.NETHERITE_SCRAP, 1));
                }
                if (block.getType() == Material.ANCIENT_DEBRIS) {
                    player.getInventory().addItem(new ItemStack(Material.NETHERITE_INGOT, 1));
                }
            }
            case "crystal_hollows" -> {
                if (block.getType() == Material.END_CRYSTAL) {
                    player.getInventory().addItem(new ItemStack(Material.END_CRYSTAL, 1));
                }
                if (block.getType() == Material.DRAGON_EGG) {
                    player.getInventory().addItem(new ItemStack(Material.DRAGON_EGG, 1));
                }
            }
            case "the_end" -> {
                if (block.getType() == Material.ELYTRA) {
                    player.getInventory().addItem(new ItemStack(Material.ELYTRA, 1));
                }
                if (block.getType() == Material.SHULKER_BOX) {
                    player.getInventory().addItem(new ItemStack(Material.SHULKER_BOX, 1));
                }
            }
            case "the_nether" -> {
                if (block.getType() == Material.ANCIENT_DEBRIS) {
                    player.getInventory().addItem(new ItemStack(Material.NETHERITE_SCRAP, 1));
                }
                if (block.getType() == Material.ANCIENT_DEBRIS) {
                    player.getInventory().addItem(new ItemStack(Material.NETHERITE_INGOT, 1));
                }
            }
        }
    }
    
    private void startBlockRegeneration(Location location, BlockData blockData, MiningArea area) {
        // Cancel existing regeneration task if any
        Thread existingTask = regenerationTasks.get(location);
        if (existingTask != null) {
            existingTask.interrupt();
        }
        
        // Get regeneration time based on area
        long regenerationTime = area.getRegenerationTime();
        
        // Start regeneration task - use virtual thread for Folia compatibility
        Thread regenerationThread = Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(regenerationTime * 50); // Convert ticks to ms
                // Regenerate block
                Block block = location.getBlock();
                block.setType(blockData.getType());
                block.setBlockData(blockData.getBlockData());
                
                // Remove from tracking
                blockRegeneration.remove(location);
                regenerationTasks.remove(location);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        regenerationTasks.put(location, regenerationThread);
    }
    
    private void startBlockRegenerationTimer() {
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    // Check for expired regeneration tasks
                    List<Location> expiredLocations = new ArrayList<>();
                    for (Map.Entry<Location, Thread> entry : regenerationTasks.entrySet()) {
                        if (!entry.getValue().isAlive()) {
                            expiredLocations.add(entry.getKey());
                        }
                    }
                    
                    // Clean up expired tasks
                    for (Location location : expiredLocations) {
                        regenerationTasks.remove(location);
                        blockRegeneration.remove(location);
                    }
                    
                    Thread.sleep(1000); // Check every second = 1000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    private MiningArea getMiningArea(Location location) {
        for (MiningArea area : miningAreas.values()) {
            if (area.isWithinBounds(location)) {
                return area;
            }
        }
        return null;
    }
    
    public MiningArea getMiningArea(String id) {
        return miningAreas.get(id);
    }
    
    public Map<String, MiningArea> getAllMiningAreas() {
        return new HashMap<>(miningAreas);
    }
    
    public static class BlockData {
        private final Material type;
        private final org.bukkit.block.data.BlockData blockData;
        
        public BlockData(Material type, org.bukkit.block.data.BlockData blockData) {
            this.type = type;
            this.blockData = blockData;
        }
        
        public Material getType() { return type; }
        public org.bukkit.block.data.BlockData getBlockData() { return blockData; }
    }
    
    public static class MiningArea {
        private final String id;
        private final String name;
        private final Location minBound;
        private final Location maxBound;
        private final Map<Material, Integer> requiredLevels = new HashMap<>();
        private final Map<Material, Double> xpAmounts = new HashMap<>();
        private final Map<Material, Integer> dropAmounts = new HashMap<>();
        private long regenerationTime = 20 * 60; // 1 minute default
        
        public MiningArea(String id, String name, Location minBound, Location maxBound) {
            this.id = id;
            this.name = name;
            this.minBound = minBound;
            this.maxBound = maxBound;
        }
        
        public void addAllowedBlock(Material material, int requiredLevel, double xpAmount) {
            requiredLevels.put(material, requiredLevel);
            xpAmounts.put(material, xpAmount);
            dropAmounts.put(material, 1); // Default drop amount
        }
        
        public void addAllowedBlock(Material material, int requiredLevel, double xpAmount, int dropAmount) {
            requiredLevels.put(material, requiredLevel);
            xpAmounts.put(material, xpAmount);
            dropAmounts.put(material, dropAmount);
        }
        
        public boolean isWithinBounds(Location location) {
            return location.getX() >= minBound.getX() && location.getX() <= maxBound.getX() &&
                   location.getY() >= minBound.getY() && location.getY() <= maxBound.getY() &&
                   location.getZ() >= minBound.getZ() && location.getZ() <= maxBound.getZ();
        }
        
        public int getRequiredLevel(Material material) {
            return requiredLevels.getOrDefault(material, 0);
        }
        
        public double getXPAmount(Material material) {
            return xpAmounts.getOrDefault(material, 0.0);
        }
        
        public int getDropAmount(Material material) {
            return dropAmounts.getOrDefault(material, 1);
        }
        
        public void setRegenerationTime(long regenerationTime) {
            this.regenerationTime = regenerationTime;
        }
        
        public long getRegenerationTime() {
            return regenerationTime;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public Location getMinBound() { return minBound; }
        public Location getMaxBound() { return maxBound; }
        public Map<Material, Integer> getRequiredLevels() { return new HashMap<>(requiredLevels); }
        public Map<Material, Double> getXPAmounts() { return new HashMap<>(xpAmounts); }
        public Map<Material, Integer> getDropAmounts() { return new HashMap<>(dropAmounts); }
    }
}
