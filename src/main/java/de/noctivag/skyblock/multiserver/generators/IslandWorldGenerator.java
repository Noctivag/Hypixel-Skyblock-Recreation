package de.noctivag.skyblock.multiserver.generators;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

/**
 * World Generator für Island Server (Private/Public Islands)
 */
public class IslandWorldGenerator extends ChunkGenerator {
    
    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Erstelle eine natürliche Island-Oberfläche
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                // Setze Bedrock als Basis
                chunkData.setBlock(x, 0, z, Material.BEDROCK);
                
                // Setze Stone als Hauptmaterial
                for (int y = 1; y < 60; y++) {
                    chunkData.setBlock(x, y, z, Material.STONE);
                }
                
                // Setze Grass als Oberfläche
                chunkData.setBlock(x, 60, z, Material.GRASS_BLOCK);
                
                // Füge gelegentlich Bäume hinzu
                if (random.nextInt(100) < 5) {
                    generateTree(chunkData, x, 61, z, random);
                }
            }
        }
    }
    
    private void generateTree(ChunkData chunkData, int x, int y, int z, Random random) {
        if (x < 14 && z < 14 && x > 1 && z > 1) {
            // Erstelle einen einfachen Baum
            Material logType = random.nextBoolean() ? Material.OAK_LOG : Material.BIRCH_LOG;
            Material leavesType = random.nextBoolean() ? Material.OAK_LEAVES : Material.BIRCH_LEAVES;
            
            // Stamm
            for (int i = 0; i < 4; i++) {
                chunkData.setBlock(x, y + i, z, logType);
            }
            
            // Blätter
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dy = 3; dy <= 5; dy++) {
                        if (x + dx >= 0 && x + dx < 16 && z + dz >= 0 && z + dz < 16) {
                            chunkData.setBlock(x + dx, y + dy, z + dz, leavesType);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Bedrock wird bereits in generateSurface gesetzt
    }
    
    @Override
    public void generateCaves(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Keine Höhlen für Island Server
    }
    
    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // Keine Noise-Generierung für Island Server
    }
}
