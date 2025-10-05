package de.noctivag.skyblock.worlds;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

/**
 * Custom World Generator - Custom world generator
 */
public class CustomWorldGenerator extends ChunkGenerator {
    
    private final SkyblockPlugin plugin;
    
    public CustomWorldGenerator(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Generate basic surface
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Set bedrock at bottom
                chunkData.setBlock(x, 0, z, org.bukkit.Material.BEDROCK);
                
                // Set stone floor
                for (int y = 1; y <= 5; y++) {
                    chunkData.setBlock(x, y, z, org.bukkit.Material.STONE);
                }
                
                // Set air above
                for (int y = 6; y <= 20; y++) {
                    chunkData.setBlock(x, y, z, org.bukkit.Material.AIR);
                }
            }
        }
    }
    
    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Bedrock is already set in generateSurface
    }
    
    @Override
    public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // No caves in custom worlds
    }
    
    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // No noise generation needed
    }
    
    @Override
    public void generateBase(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Base generation is handled in generateSurface
    }
    
    @Override
    public void generateMobs(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // No mob generation - handled by plugin
    }
    
    @Override
    public void generateDecorations(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // No decorations - handled by plugin
    }
    
    @Override
    public void generateStructures(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // No structures - handled by plugin
    }
    
    @Override
    public void generateVegetation(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // No vegetation in custom worlds
    }
    
    @Override
    public void generateFluids(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // No fluids in custom worlds
    }
    
    @Override
    public void generatePostProcess(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Post-processing handled by plugin
    }
}

