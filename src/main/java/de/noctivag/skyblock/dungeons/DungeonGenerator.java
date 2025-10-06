package de.noctivag.skyblock.dungeons;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

/**
 * Dungeon Generator - Generates dungeon worlds
 */
public class DungeonGenerator extends ChunkGenerator {
    
    private final SkyblockPlugin plugin;
    
    public DungeonGenerator(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Generate basic dungeon structure
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Set bedrock at bottom
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                
                // Set stone floor
                for (int y = 1; y <= 5; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
                
                // Set air above
                for (int y = 6; y <= 20; y++) {
                    chunkData.setBlock(x, y, z, Material.AIR);
                }
            }
        }
    }
}