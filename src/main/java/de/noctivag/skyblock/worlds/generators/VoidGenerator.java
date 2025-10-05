package de.noctivag.skyblock.worlds.generators;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

/**
 * Void generator for empty worlds
 */
public class VoidGenerator extends ChunkGenerator {
    
    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int x, int z, ChunkData chunkData) {
        // Generate empty chunks (void)
    }
    
    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int x, int z, ChunkData chunkData) {
        // Generate empty chunks (void)
    }
    
    @Override
    public void generateCaves(WorldInfo worldInfo, Random random, int x, int z, ChunkData chunkData) {
        // Generate empty chunks (void)
    }
}
