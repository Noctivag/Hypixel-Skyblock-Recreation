package de.noctivag.skyblock.worlds.generators;

import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Island Generator - Erstellt Insel-Welten für Skyblock
 */
public class IslandGenerator extends ChunkGenerator {
    
    @Override
    public void generateSurface(@NotNull WorldInfo worldInfo, @NotNull Random random, int chunkX, int chunkZ, @NotNull ChunkData chunkData) {
        // Generate bedrock layer
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
            }
        }
        
        // Generate stone layers (1-3)
        for (int y = 1; y <= 3; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
            }
        }
        
        // Generate grass layer at y=4
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunkData.setBlock(x, 4, z, Material.GRASS_BLOCK);
            }
        }
    }
    
    @Override
    public boolean shouldGenerateNoise() {
        return false;
    }
    
    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }
    
    @Override
    public boolean shouldGenerateBedrock() {
        return true;
    }
    
    @Override
    public boolean shouldGenerateCaves() {
        return false;
    }
    
    @Override
    public boolean shouldGenerateDecorations() {
        return false;
    }
    
    @Override
    public boolean shouldGenerateMobs() {
        return false;
    }
    
    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }
}