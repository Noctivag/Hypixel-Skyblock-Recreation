package de.noctivag.skyblock.furniture;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Advanced Furniture System for Hypixel Skyblock-style furniture
 * Includes furniture types, placement, and island decoration
 */
public class AdvancedFurnitureSystem {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerFurnitureData> playerFurnitureData = new ConcurrentHashMap<>();
    private final Map<String, FurnitureType> furnitureTypes = new HashMap<>();
    private final Map<UUID, List<FurnitureItem>> placedFurniture = new ConcurrentHashMap<>();
    private final Map<UUID, FurnitureMode> playerFurnitureMode = new ConcurrentHashMap<>();
    
    public AdvancedFurnitureSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeFurnitureTypes();
        startFurnitureTask();
    }
    
    /**
     * Initialize all furniture types
     */
    private void initializeFurnitureTypes() {
        // Chairs
        furnitureTypes.put("wooden_chair", new FurnitureType(
            "Wooden Chair", 
            Material.OAK_STAIRS, 
            FurnitureCategory.CHAIR,
            Arrays.asList("§7A simple wooden chair", "§7Perfect for sitting"),
            100
        ));
        
        furnitureTypes.put("stone_chair", new FurnitureType(
            "Stone Chair", 
            Material.STONE_STAIRS, 
            FurnitureCategory.CHAIR,
            Arrays.asList("§7A sturdy stone chair", "§7More durable than wood"),
            250
        ));
        
        furnitureTypes.put("iron_chair", new FurnitureType(
            "Iron Chair", 
            Material.IRON_BLOCK, 
            FurnitureCategory.CHAIR,
            Arrays.asList("§7A heavy iron chair", "§7Very durable and comfortable"),
            500
        ));
        
        furnitureTypes.put("gold_chair", new FurnitureType(
            "Gold Chair", 
            Material.GOLD_BLOCK, 
            FurnitureCategory.CHAIR,
            Arrays.asList("§7A luxurious gold chair", "§7Shows wealth and status"),
            1000
        ));
        
        furnitureTypes.put("diamond_chair", new FurnitureType(
            "Diamond Chair", 
            Material.DIAMOND_BLOCK, 
            FurnitureCategory.CHAIR,
            Arrays.asList("§7An exquisite diamond chair", "§7The ultimate in luxury"),
            2500
        ));
        
        // Tables
        furnitureTypes.put("wooden_table", new FurnitureType(
            "Wooden Table", 
            Material.OAK_PLANKS, 
            FurnitureCategory.TABLE,
            Arrays.asList("§7A simple wooden table", "§7Great for dining"),
            200
        ));
        
        furnitureTypes.put("stone_table", new FurnitureType(
            "Stone Table", 
            Material.STONE, 
            FurnitureCategory.TABLE,
            Arrays.asList("§7A sturdy stone table", "§7Perfect for outdoor use"),
            400
        ));
        
        furnitureTypes.put("iron_table", new FurnitureType(
            "Iron Table", 
            Material.IRON_BLOCK, 
            FurnitureCategory.TABLE,
            Arrays.asList("§7A heavy iron table", "§7Very durable"),
            800
        ));
        
        furnitureTypes.put("gold_table", new FurnitureType(
            "Gold Table", 
            Material.GOLD_BLOCK, 
            FurnitureCategory.TABLE,
            Arrays.asList("§7A luxurious gold table", "§7Shows wealth and status"),
            1500
        ));
        
        furnitureTypes.put("diamond_table", new FurnitureType(
            "Diamond Table", 
            Material.DIAMOND_BLOCK, 
            FurnitureCategory.TABLE,
            Arrays.asList("§7An exquisite diamond table", "§7The ultimate in luxury"),
            3000
        ));
        
        // Beds
        furnitureTypes.put("wooden_bed", new FurnitureType(
            "Wooden Bed", 
            Material.RED_BED, 
            FurnitureCategory.BED,
            Arrays.asList("§7A simple wooden bed", "§7Comfortable for sleeping"),
            300
        ));
        
        furnitureTypes.put("stone_bed", new FurnitureType(
            "Stone Bed", 
            Material.STONE_BRICKS, 
            FurnitureCategory.BED,
            Arrays.asList("§7A sturdy stone bed", "§7More durable than wood"),
            600
        ));
        
        furnitureTypes.put("iron_bed", new FurnitureType(
            "Iron Bed", 
            Material.IRON_BLOCK, 
            FurnitureCategory.BED,
            Arrays.asList("§7A heavy iron bed", "§7Very durable and comfortable"),
            1200
        ));
        
        furnitureTypes.put("gold_bed", new FurnitureType(
            "Gold Bed", 
            Material.GOLD_BLOCK, 
            FurnitureCategory.BED,
            Arrays.asList("§7A luxurious gold bed", "§7Shows wealth and status"),
            2500
        ));
        
        furnitureTypes.put("diamond_bed", new FurnitureType(
            "Diamond Bed", 
            Material.DIAMOND_BLOCK, 
            FurnitureCategory.BED,
            Arrays.asList("§7An exquisite diamond bed", "§7The ultimate in luxury"),
            5000
        ));
        
        // Storage
        furnitureTypes.put("wooden_chest", new FurnitureType(
            "Wooden Chest", 
            Material.CHEST, 
            FurnitureCategory.STORAGE,
            Arrays.asList("§7A simple wooden chest", "§7Great for storage"),
            150
        ));
        
        furnitureTypes.put("iron_chest", new FurnitureType(
            "Iron Chest", 
            Material.IRON_BLOCK, 
            FurnitureCategory.STORAGE,
            Arrays.asList("§7A heavy iron chest", "§7More secure than wood"),
            400
        ));
        
        furnitureTypes.put("gold_chest", new FurnitureType(
            "Gold Chest", 
            Material.GOLD_BLOCK, 
            FurnitureCategory.STORAGE,
            Arrays.asList("§7A luxurious gold chest", "§7Shows wealth and security"),
            1000
        ));
        
        furnitureTypes.put("diamond_chest", new FurnitureType(
            "Diamond Chest", 
            Material.DIAMOND_BLOCK, 
            FurnitureCategory.STORAGE,
            Arrays.asList("§7An exquisite diamond chest", "§7The ultimate in security"),
            2500
        ));
        
        // Decorative
        furnitureTypes.put("wooden_lamp", new FurnitureType(
            "Wooden Lamp", 
            Material.TORCH, 
            FurnitureCategory.DECORATIVE,
            Arrays.asList("§7A simple wooden lamp", "§7Provides light and decoration"),
            50
        ));
        
        furnitureTypes.put("iron_lamp", new FurnitureType(
            "Iron Lamp", 
            Material.LANTERN, 
            FurnitureCategory.DECORATIVE,
            Arrays.asList("§7A sturdy iron lamp", "§7Better light than wood"),
            150
        ));
        
        furnitureTypes.put("gold_lamp", new FurnitureType(
            "Gold Lamp", 
            Material.GOLD_BLOCK, 
            FurnitureCategory.DECORATIVE,
            Arrays.asList("§7A luxurious gold lamp", "§7Shows wealth and style"),
            400
        ));
        
        furnitureTypes.put("diamond_lamp", new FurnitureType(
            "Diamond Lamp", 
            Material.DIAMOND_BLOCK, 
            FurnitureCategory.DECORATIVE,
            Arrays.asList("§7An exquisite diamond lamp", "§7The ultimate in lighting"),
            1000
        ));
        
        // Special
        furnitureTypes.put("magic_mirror", new FurnitureType(
            "Magic Mirror", 
            Material.ENDER_EYE, 
            FurnitureCategory.SPECIAL,
            Arrays.asList("§7A magical mirror", "§7Allows teleportation"),
            5000
        ));
        
        furnitureTypes.put("enchanting_table", new FurnitureType(
            "Enchanting Table", 
            Material.ENCHANTING_TABLE, 
            FurnitureCategory.SPECIAL,
            Arrays.asList("§7An enchanting table", "§7Allows item enchanting"),
            3000
        ));
        
        furnitureTypes.put("anvil", new FurnitureType(
            "Anvil", 
            Material.ANVIL, 
            FurnitureCategory.SPECIAL,
            Arrays.asList("§7An anvil", "§7Allows item repair and naming"),
            2000
        ));
        
        furnitureTypes.put("brewing_stand", new FurnitureType(
            "Brewing Stand", 
            Material.BREWING_STAND, 
            FurnitureCategory.SPECIAL,
            Arrays.asList("§7A brewing stand", "§7Allows potion brewing"),
            1500
        ));
    }
    
    /**
     * Start the furniture task
     */
    private void startFurnitureTask() {
        // Use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                while (plugin.isEnabled()) {
                    updatePlacedFurniture();
                    Thread.sleep(1000); // Every second = 1000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    /**
     * Update all placed furniture
     */
    private void updatePlacedFurniture() {
        for (Map.Entry<UUID, List<FurnitureItem>> entry : placedFurniture.entrySet()) {
            List<FurnitureItem> furniture = entry.getValue();
            
            // Check if furniture is still valid
            furniture.removeIf(item -> !item.isValid());
        }
    }
    
    /**
     * Get player furniture data
     */
    public PlayerFurnitureData getPlayerFurnitureData(UUID playerId) {
        return playerFurnitureData.computeIfAbsent(playerId, k -> new PlayerFurnitureData());
    }
    
    /**
     * Get furniture type by ID
     */
    public FurnitureType getFurnitureType(String furnitureId) {
        return furnitureTypes.get(furnitureId);
    }
    
    /**
     * Get all furniture types
     */
    public Map<String, FurnitureType> getAllFurnitureTypes() {
        return new HashMap<>(furnitureTypes);
    }
    
    /**
     * Get furniture types by category
     */
    public Map<String, FurnitureType> getFurnitureTypesByCategory(FurnitureCategory category) {
        Map<String, FurnitureType> categoryFurniture = new HashMap<>();
        for (Map.Entry<String, FurnitureType> entry : furnitureTypes.entrySet()) {
            if (entry.getValue().getCategory() == category) {
                categoryFurniture.put(entry.getKey(), entry.getValue());
            }
        }
        return categoryFurniture;
    }
    
    /**
     * Check if player can place furniture
     */
    public boolean canPlaceFurniture(Player player, String furnitureId) {
        PlayerFurnitureData data = getPlayerFurnitureData(player.getUniqueId());
        FurnitureType furniture = getFurnitureType(furnitureId);
        
        if (furniture == null) return false;
        
        // Check if player has enough coins
        if (data.getCoins() < furniture.getCost()) {
            return false;
        }
        
        // Check if player has required level
        if (data.getLevel() < getRequiredLevel(furnitureId)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Place furniture
     */
    public boolean placeFurniture(Player player, String furnitureId, Location location) {
        if (!canPlaceFurniture(player, furnitureId)) {
            return false;
        }
        
        FurnitureType furniture = getFurnitureType(furnitureId);
        PlayerFurnitureData data = getPlayerFurnitureData(player.getUniqueId());
        
        // Remove coins
        data.removeCoins(furniture.getCost());
        
        // Create furniture item
        FurnitureItem furnitureItem = new FurnitureItem(furniture, location, player.getUniqueId());
        List<FurnitureItem> playerFurniture = placedFurniture.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        playerFurniture.add(furnitureItem);
        
        // Place the furniture in the world
        location.getBlock().setType(furniture.getMaterial());
        
        // Update statistics
        data.incrementPlacedFurniture();
        data.addExperience(furniture.getCost() / 10);
        
        return true;
    }
    
    /**
     * Remove furniture
     */
    public boolean removeFurniture(Player player, Location location) {
        List<FurnitureItem> playerFurniture = placedFurniture.get(player.getUniqueId());
        if (playerFurniture == null) return false;
        
        for (FurnitureItem item : playerFurniture) {
            if (item.getLocation().equals(location)) {
                playerFurniture.remove(item);
                location.getBlock().setType(Material.AIR);
                
                // Give back some coins
                PlayerFurnitureData data = getPlayerFurnitureData(player.getUniqueId());
                data.addCoins(item.getFurnitureType().getCost() / 2);
                
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Get required level for furniture
     */
    private int getRequiredLevel(String furnitureId) {
        FurnitureType furniture = getFurnitureType(furnitureId);
        if (furniture == null) return 1;
        
        switch (furniture.getCategory()) {
            case CHAIR:
            case TABLE:
            case BED:
            case STORAGE:
            case DECORATIVE:
                return 1;
            case SPECIAL:
                return 5;
            default:
                return 1;
        }
    }
    
    /**
     * Create a furniture item
     */
    public ItemStack createFurnitureItem(String furnitureId) {
        FurnitureType furniture = getFurnitureType(furnitureId);
        if (furniture == null) return null;
        
        ItemStack item = new ItemStack(furniture.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text("§6" + furniture.getName()));
            List<String> lore = new ArrayList<>(furniture.getDescription());
            lore.add("");
            lore.add("§7Category: " + furniture.getCategory().getDisplayName());
            lore.add("§7Cost: §a" + furniture.getCost() + " coins");
            lore.add("");
            lore.add("§7Right-click to place this furniture");
            lore.add("§7on your island!");
            lore.add("");
            lore.add("§8A piece of furniture");
            meta.lore(lore.stream().map(net.kyori.adventure.text.Component::text).collect(java.util.stream.Collectors.toList()));
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Get player's furniture level
     */
    public int getFurnitureLevel(UUID playerId) {
        PlayerFurnitureData data = getPlayerFurnitureData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's furniture experience
     */
    public int getFurnitureExperience(UUID playerId) {
        PlayerFurnitureData data = getPlayerFurnitureData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add furniture experience to player
     */
    public void addFurnitureExperience(UUID playerId, int experience) {
        PlayerFurnitureData data = getPlayerFurnitureData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's furniture coins
     */
    public int getFurnitureCoins(UUID playerId) {
        PlayerFurnitureData data = getPlayerFurnitureData(playerId);
        return data.getCoins();
    }
    
    /**
     * Add furniture coins to player
     */
    public void addFurnitureCoins(UUID playerId, int coins) {
        PlayerFurnitureData data = getPlayerFurnitureData(playerId);
        data.addCoins(coins);
    }
    
    /**
     * Remove furniture coins from player
     */
    public void removeFurnitureCoins(UUID playerId, int coins) {
        PlayerFurnitureData data = getPlayerFurnitureData(playerId);
        data.removeCoins(coins);
    }
    
    /**
     * Get player's furniture statistics
     */
    public Map<String, Integer> getFurnitureStatistics(UUID playerId) {
        PlayerFurnitureData data = getPlayerFurnitureData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("coins", data.getCoins());
        stats.put("placed_furniture", data.getPlacedFurniture());
        stats.put("total_experience", data.getTotalExperience());
        
        return stats;
    }
    
    /**
     * Get placed furniture for a player
     */
    public List<FurnitureItem> getPlacedFurniture(UUID playerId) {
        return placedFurniture.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Set player's furniture mode
     */
    public void setFurnitureMode(UUID playerId, FurnitureMode mode) {
        playerFurnitureMode.put(playerId, mode);
    }
    
    /**
     * Get player's furniture mode
     */
    public FurnitureMode getFurnitureMode(UUID playerId) {
        return playerFurnitureMode.getOrDefault(playerId, FurnitureMode.NORMAL);
    }
    
    /**
     * Reset player's furniture data
     */
    public void resetFurnitureData(UUID playerId) {
        playerFurnitureData.remove(playerId);
        placedFurniture.remove(playerId);
        playerFurnitureMode.remove(playerId);
    }
    
    /**
     * Save player's furniture data
     */
    public void saveFurnitureData(UUID playerId) {
        PlayerFurnitureData data = getPlayerFurnitureData(playerId);
        // Save to database
        databaseManager.savePlayerFurnitureData(playerId, data);
    }
    
    /**
     * Load player's furniture data
     */
    public void loadFurnitureData(UUID playerId) {
        try {
            CompletableFuture<Object> future = databaseManager.loadPlayerFurnitureData(playerId);
            PlayerFurnitureData data = (PlayerFurnitureData) future.get();
            if (data != null) {
                playerFurnitureData.put(playerId, data);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load furniture data for player " + playerId + ": " + e.getMessage());
        }
    }
    
    /**
     * Shutdown the furniture system
     */
    public void shutdown() {
        // Save all player data
        for (UUID playerId : playerFurnitureData.keySet()) {
            saveFurnitureData(playerId);
        }
        
        // Clear data
        playerFurnitureData.clear();
        placedFurniture.clear();
        playerFurnitureMode.clear();
    }
}
