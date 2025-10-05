package de.noctivag.skyblock.dungeons;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class DungeonGenerator extends ChunkGenerator {

    private final SkyblockPluginRefactored plugin;
    private final String dungeonType;
    private final int floor;

    public DungeonGenerator(SkyblockPluginRefactored plugin, String dungeonType, int floor) {
        this.plugin = plugin;
        this.dungeonType = dungeonType;
        this.floor = floor;
    }

    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Generate a basic void world for dungeons
        // The actual dungeon structure will be loaded from schematics
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                for (int y = 1; y < 64; y++) {
                    chunkData.setBlock(x, y, z, Material.AIR);
                }
            }
        }
    }

    public void generateDungeon(World world, Location spawnLocation) {
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Generating dungeon: " + dungeonType + " Floor " + floor + " at " + spawnLocation);
        }

        // Load dungeon schematic asynchronously
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    loadDungeonSchematic(world, spawnLocation);
                } catch (Exception e) {
                    plugin.getLogger().severe("Failed to generate dungeon: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    private void loadDungeonSchematic(World world, Location spawnLocation) {
        try {
            // Look for dungeon schematic files
            String schematicName = dungeonType.toLowerCase() + "_floor_" + floor + ".schematic";
            Path schematicPath = Paths.get(plugin.getDataFolder().getAbsolutePath(), "schematics", "dungeons", schematicName);
            
            if (!Files.exists(schematicPath)) {
                plugin.getLogger().warning("Dungeon schematic not found: " + schematicPath);
                generateBasicDungeon(world, spawnLocation);
                return;
            }

            // In a real implementation, you would use WorldEdit or similar to load the schematic
            // For now, we'll generate a basic dungeon structure
            generateBasicDungeon(world, spawnLocation);

        } catch (Exception e) {
            plugin.getLogger().severe("Error loading dungeon schematic: " + e.getMessage());
            generateBasicDungeon(world, spawnLocation);
        }
    }

    private void generateBasicDungeon(World world, Location spawnLocation) {
        // Generate a basic dungeon structure
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // Create a simple room structure
                    createDungeonRoom(world, spawnLocation);
                    
                    // Spawn dungeon mobs
                    spawnDungeonMobs(world, spawnLocation);
                    
                    // Set up dungeon objectives
                    setupDungeonObjectives(world, spawnLocation);
                    
                    if (plugin.getSettingsConfig().isDebugMode()) {
                        plugin.getLogger().info("Basic dungeon generated at " + spawnLocation);
                    }
                } catch (Exception e) {
                    plugin.getLogger().severe("Error generating basic dungeon: " + e.getMessage());
                }
            }
        }.runTask(plugin);
    }

    private void createDungeonRoom(World world, Location center) {
        // Create a simple 20x20x10 room
        int roomSize = 20;
        int roomHeight = 10;
        
        for (int x = -roomSize/2; x <= roomSize/2; x++) {
            for (int z = -roomSize/2; z <= roomSize/2; z++) {
                for (int y = 0; y <= roomHeight; y++) {
                    Location blockLoc = center.clone().add(x, y, z);
                    
                    // Floor
                    if (y == 0) {
                        blockLoc.getBlock().setType(Material.STONE_BRICKS);
                    }
                    // Ceiling
                    else if (y == roomHeight) {
                        blockLoc.getBlock().setType(Material.STONE_BRICKS);
                    }
                    // Walls
                    else if (x == -roomSize/2 || x == roomSize/2 || z == -roomSize/2 || z == roomSize/2) {
                        blockLoc.getBlock().setType(Material.STONE_BRICKS);
                    }
                    // Interior
                    else {
                        blockLoc.getBlock().setType(Material.AIR);
                    }
                }
            }
        }
        
        // Add some decorative elements
        addDungeonDecorations(world, center);
    }

    private void addDungeonDecorations(World world, Location center) {
        // Add torches for lighting
        center.clone().add(8, 1, 8).getBlock().setType(Material.TORCH);
        center.clone().add(-8, 1, 8).getBlock().setType(Material.TORCH);
        center.clone().add(8, 1, -8).getBlock().setType(Material.TORCH);
        center.clone().add(-8, 1, -8).getBlock().setType(Material.TORCH);
        
        // Add some chests with loot
        center.clone().add(5, 1, 5).getBlock().setType(Material.CHEST);
        center.clone().add(-5, 1, -5).getBlock().setType(Material.CHEST);
        
        // Add spawner for mobs
        center.clone().add(0, 1, 0).getBlock().setType(Material.SPAWNER);
    }

    private void spawnDungeonMobs(World world, Location center) {
        // This would spawn appropriate dungeon mobs based on floor and type
        // For now, we'll just log that mobs should be spawned
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Dungeon mobs should be spawned at " + center);
        }
    }

    private void setupDungeonObjectives(World world, Location center) {
        // Set up dungeon objectives like:
        // - Kill all mobs
        // - Find the exit
        // - Collect items
        // - Defeat the boss
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Dungeon objectives set up at " + center);
        }
    }

    public String getDungeonType() {
        return dungeonType;
    }

    public int getFloor() {
        return floor;
    }
}
