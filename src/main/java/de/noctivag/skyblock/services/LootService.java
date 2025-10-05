package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.Rarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;

public class LootService {

    private final SkyblockPluginRefactored plugin;
    private final Map<String, List<LootEntry>> lootTables = new HashMap<>();
    private final Random random = new Random();

    public LootService(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
        loadLootTables();
    }

    private void loadLootTables() {
        File configFile = new File(plugin.getDataFolder(), "loot_tables.yml");
        if (!configFile.exists()) {
            plugin.getLogger().info("Creating default loot_tables.yml...");
            try (InputStream in = plugin.getResource("loot_tables.yml")) {
                if (in != null) {
                    Files.copy(in, configFile.toPath());
                } else {
                    plugin.getLogger().warning("Default loot_tables.yml not found in plugin resources!");
                    createDefaultLootTables(configFile);
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create loot_tables.yml: " + e.getMessage());
                createDefaultLootTables(configFile);
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Load loot tables for different sources
        loadLootTable(config, "fishing", "fishing");
        loadLootTable(config, "mining", "mining");
        loadLootTable(config, "farming", "farming");
        loadLootTable(config, "combat", "combat");
        loadLootTable(config, "dungeons", "dungeons");
        loadLootTable(config, "slayer", "slayer");

        plugin.getLogger().info("LootService loaded " + lootTables.size() + " loot tables.");
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("  Loot tables: " + lootTables.keySet());
        }
    }

    private void loadLootTable(FileConfiguration config, String tableName, String configPath) {
        List<LootEntry> entries = new ArrayList<>();
        
        if (config.contains(configPath)) {
            for (String itemKey : config.getConfigurationSection(configPath).getKeys(false)) {
                String itemPath = configPath + "." + itemKey;
                
                Material material = Material.valueOf(config.getString(itemPath + ".material", "STONE"));
                int minQuantity = config.getInt(itemPath + ".min-quantity", 1);
                int maxQuantity = config.getInt(itemPath + ".max-quantity", 1);
                double weight = config.getDouble(itemPath + ".weight", 1.0);
                Rarity rarity = Rarity.valueOf(config.getString(itemPath + ".rarity", "COMMON"));
                
                entries.add(new LootEntry(material, minQuantity, maxQuantity, weight, rarity));
            }
        }
        
        lootTables.put(tableName, entries);
    }

    private void createDefaultLootTables(File configFile) {
        try {
            configFile.createNewFile();
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            
            // Fishing loot table
            config.set("fishing.wheat.material", "WHEAT");
            config.set("fishing.wheat.min-quantity", 1);
            config.set("fishing.wheat.max-quantity", 3);
            config.set("fishing.wheat.weight", 0.3);
            config.set("fishing.wheat.rarity", "COMMON");
            
            config.set("fishing.cod.material", "COD");
            config.set("fishing.cod.min-quantity", 1);
            config.set("fishing.cod.max-quantity", 2);
            config.set("fishing.cod.weight", 0.4);
            config.set("fishing.cod.rarity", "COMMON");
            
            config.set("fishing.salmon.material", "SALMON");
            config.set("fishing.salmon.min-quantity", 1);
            config.set("fishing.salmon.max-quantity", 2);
            config.set("fishing.salmon.weight", 0.2);
            config.set("fishing.salmon.rarity", "UNCOMMON");
            
            config.set("fishing.prismarine_shard.material", "PRISMARINE_SHARD");
            config.set("fishing.prismarine_shard.min-quantity", 1);
            config.set("fishing.prismarine_shard.max-quantity", 2);
            config.set("fishing.prismarine_shard.weight", 0.05);
            config.set("fishing.prismarine_shard.rarity", "RARE");
            
            // Mining loot table
            config.set("mining.coal.material", "COAL");
            config.set("mining.coal.min-quantity", 1);
            config.set("mining.coal.max-quantity", 5);
            config.set("mining.coal.weight", 0.4);
            config.set("mining.coal.rarity", "COMMON");
            
            config.set("mining.iron_ingot.material", "IRON_INGOT");
            config.set("mining.iron_ingot.min-quantity", 1);
            config.set("mining.iron_ingot.max-quantity", 3);
            config.set("mining.iron_ingot.weight", 0.3);
            config.set("mining.iron_ingot.rarity", "UNCOMMON");
            
            config.set("mining.gold_ingot.material", "GOLD_INGOT");
            config.set("mining.gold_ingot.min-quantity", 1);
            config.set("mining.gold_ingot.max-quantity", 2);
            config.set("mining.gold_ingot.weight", 0.2);
            config.set("mining.gold_ingot.rarity", "RARE");
            
            config.set("mining.diamond.material", "DIAMOND");
            config.set("mining.diamond.min-quantity", 1);
            config.set("mining.diamond.max-quantity", 1);
            config.set("mining.diamond.weight", 0.1);
            config.set("mining.diamond.rarity", "EPIC");
            
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not create default loot_tables.yml: " + e.getMessage());
        }
    }

    public List<ItemStack> generateLoot(String lootTableName, Player player, double magicFindMultiplier) {
        List<LootEntry> entries = lootTables.get(lootTableName);
        if (entries == null || entries.isEmpty()) {
            return new ArrayList<>();
        }

        List<ItemStack> loot = new ArrayList<>();
        
        // Calculate total weight
        double totalWeight = entries.stream().mapToDouble(LootEntry::getWeight).sum();
        
        // Generate loot based on weights
        for (LootEntry entry : entries) {
            double adjustedWeight = entry.getWeight() * magicFindMultiplier;
            if (random.nextDouble() < (adjustedWeight / totalWeight)) {
                int quantity = random.nextInt(entry.getMaxQuantity() - entry.getMinQuantity() + 1) + entry.getMinQuantity();
                ItemStack item = new ItemStack(entry.getMaterial(), quantity);
                loot.add(item);
            }
        }
        
        if (plugin.getSettingsConfig().isDebugMode()) {
            plugin.getLogger().info("Generated " + loot.size() + " items from loot table " + lootTableName + 
                " for player " + player.getName() + " (Magic Find: " + magicFindMultiplier + ")");
        }
        
        return loot;
    }

    public void dropLoot(Location location, String lootTableName, Player player, double magicFindMultiplier) {
        List<ItemStack> loot = generateLoot(lootTableName, player, magicFindMultiplier);
        
        for (ItemStack item : loot) {
            location.getWorld().dropItem(location, item);
        }
        
        if (!loot.isEmpty()) {
            player.sendMessage("§aDu erhältst " + loot.size() + " Items!");
        }
    }

    public double calculateMagicFind(Player player) {
        // In a real implementation, you would calculate magic find based on:
        // - Player's magic find stat
        // - Active potions
        // - Equipment bonuses
        // - Pet bonuses
        // For now, return a base multiplier
        return 1.0;
    }

    public static class LootEntry {
        private final Material material;
        private final int minQuantity;
        private final int maxQuantity;
        private final double weight;
        private final Rarity rarity;

        public LootEntry(Material material, int minQuantity, int maxQuantity, double weight, Rarity rarity) {
            this.material = material;
            this.minQuantity = minQuantity;
            this.maxQuantity = maxQuantity;
            this.weight = weight;
            this.rarity = rarity;
        }

        public Material getMaterial() {
            return material;
        }

        public int getMinQuantity() {
            return minQuantity;
        }

        public int getMaxQuantity() {
            return maxQuantity;
        }

        public double getWeight() {
            return weight;
        }

        public Rarity getRarity() {
            return rarity;
        }
    }
}
