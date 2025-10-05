package de.noctivag.skyblock.loot;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

/**
 * Loot Service - Service for managing loot distribution
 */
public class LootService implements Service {
    
    private final SkyblockPlugin plugin;
    private final Map<String, LootTable> lootTables = new HashMap<>();
    private final Random random = new Random();
    private SystemStatus status = SystemStatus.DISABLED;
    
    public LootService(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing LootService...");
        
        // Initialize default loot tables
        initializeDefaultLootTables();
        
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("LootService initialized.");
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down LootService...");
        
        lootTables.clear();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("LootService shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    /**
     * Initialize default loot tables
     */
    private void initializeDefaultLootTables() {
        // Lapis Zombie loot table
        LootTable lapisZombieTable = new LootTable("lapis_zombie");
        lapisZombieTable.addLootEntry(new LootEntry(org.bukkit.Material.LAPIS_LAZULI, 1, 3, 0.8));
        lapisZombieTable.addLootEntry(new LootEntry(org.bukkit.Material.ROTTEN_FLESH, 1, 2, 0.6));
        lootTables.put("lapis_zombie", lapisZombieTable);
        
        // Crypt Ghoul loot table
        LootTable cryptGhoulTable = new LootTable("crypt_ghoul");
        cryptGhoulTable.addLootEntry(new LootEntry(org.bukkit.Material.ROTTEN_FLESH, 2, 4, 0.9));
        cryptGhoulTable.addLootEntry(new LootEntry(org.bukkit.Material.BONE, 1, 3, 0.7));
        lootTables.put("crypt_ghoul", cryptGhoulTable);
        
        // Zombie Villager loot table
        LootTable zombieVillagerTable = new LootTable("zombie_villager");
        zombieVillagerTable.addLootEntry(new LootEntry(org.bukkit.Material.ROTTEN_FLESH, 1, 2, 0.8));
        zombieVillagerTable.addLootEntry(new LootEntry(org.bukkit.Material.IRON_INGOT, 1, 1, 0.3));
        lootTables.put("zombie_villager", zombieVillagerTable);
        
        plugin.getLogger().log(Level.INFO, "Initialized " + lootTables.size() + " loot tables.");
    }
    
    /**
     * Distribute loot for a given loot table
     */
    public void distributeLoot(String lootTableId, Location location, Player killer) {
        LootTable lootTable = lootTables.get(lootTableId);
        if (lootTable == null) {
            plugin.getLogger().log(Level.WARNING, "Unknown loot table: " + lootTableId);
            return;
        }
        
        // Calculate magic find bonus
        double magicFind = 0.0; // Placeholder - would be calculated from player stats
        if (killer != null) {
            magicFind = getPlayerMagicFind(killer);
        }
        
        // Distribute loot
        for (LootEntry entry : lootTable.getLootEntries()) {
            if (random.nextDouble() < entry.getDropChance()) {
                int amount = entry.getMinAmount() + random.nextInt(entry.getMaxAmount() - entry.getMinAmount() + 1);
                
                // Apply magic find bonus
                if (magicFind > 0) {
                    double bonusChance = magicFind / 100.0;
                    if (random.nextDouble() < bonusChance) {
                        amount *= 2; // Double the amount
                    }
                }
                
                // Drop the item
                ItemStack item = new ItemStack(entry.getMaterial(), amount);
                location.getWorld().dropItemNaturally(location, item);
                
                if (killer != null) {
                    killer.sendMessage("§a+§e" + amount + " §a" + entry.getMaterial().name().toLowerCase().replace("_", " "));
                }
            }
        }
    }
    
    /**
     * Get a player's magic find stat
     */
    private double getPlayerMagicFind(Player player) {
        // Placeholder - would be calculated from player stats
        return 0.0;
    }
    
    /**
     * Add a loot table
     */
    public void addLootTable(LootTable lootTable) {
        lootTables.put(lootTable.getId(), lootTable);
    }
    
    /**
     * Get a loot table
     */
    public LootTable getLootTable(String id) {
        return lootTables.get(id);
    }
    
    /**
     * Get all loot tables
     */
    public Map<String, LootTable> getLootTables() {
        return new HashMap<>(lootTables);
    }
}

