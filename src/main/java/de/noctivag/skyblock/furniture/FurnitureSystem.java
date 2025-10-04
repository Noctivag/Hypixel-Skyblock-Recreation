package de.noctivag.skyblock.furniture;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Furniture System - Hypixel Skyblock Style
 * 
 * Features:
 * - Decorative Furniture Items
 * - Furniture Categories (Chairs, Tables, Beds, Storage, Decorative)
 * - Furniture Placement System
 * - Furniture Interaction
 * - Furniture Storage
 * - Furniture Crafting
 * - Furniture Rarities
 * - Furniture Effects
 * - Furniture Skins
 * - Furniture Upgrades
 */
public class FurnitureSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerFurnitureData> playerFurnitureData = new ConcurrentHashMap<>();
    private final Map<FurnitureType, FurnitureConfig> furnitureConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> furnitureTasks = new ConcurrentHashMap<>();
    
    public FurnitureSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeFurnitureConfigs();
        startFurnitureUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeFurnitureConfigs() {
        // Chairs
        furnitureConfigs.put(FurnitureType.OAK_CHAIR, new FurnitureConfig(
            "Oak Chair", "§6Oak Chair", Material.OAK_STAIRS,
            "§7A comfortable oak chair.",
            FurnitureCategory.CHAIR, FurnitureRarity.COMMON, 1, 
            Arrays.asList("§7- Sit functionality", "§7- Comfort bonus"),
            Arrays.asList("§7- 6x Oak Planks", "§7- 1x String")
        ));
        
        furnitureConfigs.put(FurnitureType.SPRUCE_CHAIR, new FurnitureConfig(
            "Spruce Chair", "§6Spruce Chair", Material.SPRUCE_STAIRS,
            "§7A comfortable spruce chair.",
            FurnitureCategory.CHAIR, FurnitureRarity.COMMON, 1,
            Arrays.asList("§7- Sit functionality", "§7- Comfort bonus"),
            Arrays.asList("§7- 6x Spruce Planks", "§7- 1x String")
        ));
        
        // Tables
        furnitureConfigs.put(FurnitureType.OAK_TABLE, new FurnitureConfig(
            "Oak Table", "§6Oak Table", Material.OAK_PRESSURE_PLATE,
            "§7A sturdy oak table.",
            FurnitureCategory.TABLE, FurnitureRarity.COMMON, 1,
            Arrays.asList("§7- Storage functionality", "§7- Crafting surface"),
            Arrays.asList("§7- 8x Oak Planks", "§7- 4x Oak Stairs")
        ));
        
        furnitureConfigs.put(FurnitureType.SPRUCE_TABLE, new FurnitureConfig(
            "Spruce Table", "§6Spruce Table", Material.SPRUCE_PRESSURE_PLATE,
            "§7A sturdy spruce table.",
            FurnitureCategory.TABLE, FurnitureRarity.COMMON, 1,
            Arrays.asList("§7- Storage functionality", "§7- Crafting surface"),
            Arrays.asList("§7- 8x Spruce Planks", "§7- 4x Spruce Stairs")
        ));
        
        // Beds
        furnitureConfigs.put(FurnitureType.OAK_BED, new FurnitureConfig(
            "Oak Bed", "§6Oak Bed", Material.OAK_SLAB,
            "§7A comfortable oak bed.",
            FurnitureCategory.BED, FurnitureRarity.COMMON, 1,
            Arrays.asList("§7- Sleep functionality", "§7- Health regeneration"),
            Arrays.asList("§7- 3x Oak Planks", "§7- 3x Wool")
        ));
        
        // Storage
        furnitureConfigs.put(FurnitureType.OAK_CHEST, new FurnitureConfig(
            "Oak Chest", "§6Oak Chest", Material.CHEST,
            "§7A storage chest.",
            FurnitureCategory.STORAGE, FurnitureRarity.COMMON, 1,
            Arrays.asList("§7- Storage functionality", "§7- 27 slots"),
            Arrays.asList("§7- 8x Oak Planks")
        ));
        
        furnitureConfigs.put(FurnitureType.OAK_BARREL, new FurnitureConfig(
            "Oak Barrel", "§6Oak Barrel", Material.BARREL,
            "§7A storage barrel.",
            FurnitureCategory.STORAGE, FurnitureRarity.COMMON, 1,
            Arrays.asList("§7- Storage functionality", "§7- 27 slots"),
            Arrays.asList("§7- 6x Oak Planks", "§7- 2x Oak Slabs")
        ));
        
        // Decorative
        furnitureConfigs.put(FurnitureType.OAK_LAMP, new FurnitureConfig(
            "Oak Lamp", "§6Oak Lamp", Material.TORCH,
            "§7A decorative oak lamp.",
            FurnitureCategory.DECORATIVE, FurnitureRarity.COMMON, 1,
            Arrays.asList("§7- Light source", "§7- Decorative"),
            Arrays.asList("§7- 4x Oak Planks", "§7- 1x Glowstone")
        ));
        
        furnitureConfigs.put(FurnitureType.OAK_PLANT, new FurnitureConfig(
            "Oak Plant", "§6Oak Plant", Material.OAK_SAPLING,
            "§7A decorative oak plant.",
            FurnitureCategory.DECORATIVE, FurnitureRarity.COMMON, 1,
            Arrays.asList("§7- Decorative", "§7- Nature bonus"),
            Arrays.asList("§7- 1x Oak Sapling", "§7- 1x Flower Pot")
        ));
        
        // Rare Furniture
        furnitureConfigs.put(FurnitureType.GOLDEN_CHAIR, new FurnitureConfig(
            "Golden Chair", "§6Golden Chair", Material.GOLD_BLOCK,
            "§7A luxurious golden chair.",
            FurnitureCategory.CHAIR, FurnitureRarity.RARE, 1,
            Arrays.asList("§7- Sit functionality", "§7- Luxury bonus", "§7- Gold effect"),
            Arrays.asList("§7- 6x Gold Ingots", "§7- 1x Golden String")
        ));
        
        furnitureConfigs.put(FurnitureType.DIAMOND_TABLE, new FurnitureConfig(
            "Diamond Table", "§bDiamond Table", Material.DIAMOND_BLOCK,
            "§7A luxurious diamond table.",
            FurnitureCategory.TABLE, FurnitureRarity.EPIC, 1,
            Arrays.asList("§7- Storage functionality", "§7- Luxury bonus", "§7- Diamond effect"),
            Arrays.asList("§7- 8x Diamonds", "§7- 4x Diamond Blocks")
        ));
    }
    
    private void startFurnitureUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerFurnitureData> entry : playerFurnitureData.entrySet()) {
                    PlayerFurnitureData furnitureData = entry.getValue();
                    furnitureData.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 60L); // Update every minute
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Chair") || displayName.contains("Table") || 
            displayName.contains("Bed") || displayName.contains("Chest") || 
            displayName.contains("Lamp") || displayName.contains("Plant")) {
            openFurnitureGUI(player);
        }
    }
    
    public void openFurnitureGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lFurniture System");
        
        // Add furniture categories
        addGUIItem(gui, 10, Material.OAK_STAIRS, "§6§lChairs", "§7View chairs.");
        addGUIItem(gui, 11, Material.OAK_PRESSURE_PLATE, "§6§lTables", "§7View tables.");
        addGUIItem(gui, 12, Material.OAK_SLAB, "§6§lBeds", "§7View beds.");
        addGUIItem(gui, 13, Material.CHEST, "§6§lStorage", "§7View storage furniture.");
        addGUIItem(gui, 14, Material.TORCH, "§6§lDecorative", "§7View decorative furniture.");
        
        // Add specific furniture
        addGUIItem(gui, 19, Material.OAK_STAIRS, "§6§lOak Chair", "§7A comfortable oak chair.");
        addGUIItem(gui, 20, Material.SPRUCE_STAIRS, "§6§lSpruce Chair", "§7A comfortable spruce chair.");
        addGUIItem(gui, 21, Material.OAK_PRESSURE_PLATE, "§6§lOak Table", "§7A sturdy oak table.");
        addGUIItem(gui, 22, Material.SPRUCE_PRESSURE_PLATE, "§6§lSpruce Table", "§7A sturdy spruce table.");
        
        addGUIItem(gui, 24, Material.OAK_SLAB, "§6§lOak Bed", "§7A comfortable oak bed.");
        addGUIItem(gui, 25, Material.CHEST, "§6§lOak Chest", "§7A storage chest.");
        addGUIItem(gui, 26, Material.BARREL, "§6§lOak Barrel", "§7A storage barrel.");
        
        addGUIItem(gui, 28, Material.TORCH, "§6§lOak Lamp", "§7A decorative oak lamp.");
        addGUIItem(gui, 29, Material.OAK_SAPLING, "§6§lOak Plant", "§7A decorative oak plant.");
        
        // Add rare furniture
        addGUIItem(gui, 31, Material.GOLD_BLOCK, "§6§lGolden Chair", "§7A luxurious golden chair.");
        addGUIItem(gui, 32, Material.DIAMOND_BLOCK, "§b§lDiamond Table", "§7A luxurious diamond table.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the furniture menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aFurniture GUI geöffnet!");
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(description));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public void placeFurniture(Player player, FurnitureType type, Location location) {
        FurnitureConfig config = furnitureConfigs.get(type);
        if (config == null) return;
        
        PlayerFurnitureData furnitureData = getPlayerFurnitureData(player.getUniqueId());
        if (!furnitureData.hasFurniture(type)) {
            player.sendMessage("§cDu besitzt dieses Möbelstück nicht!");
            return;
        }
        
        // Place furniture at location
        location.getBlock().setType(config.getIcon());
        
        player.sendMessage("§a" + config.getDisplayName() + " platziert!");
    }
    
    public void removeFurniture(Player player, Location location) {
        // Remove furniture from location
        location.getBlock().setType(Material.AIR);
        
        player.sendMessage("§aMöbelstück entfernt!");
    }
    
    public void craftFurniture(Player player, FurnitureType type) {
        FurnitureConfig config = furnitureConfigs.get(type);
        if (config == null) return;
        
        // Check if player has required materials
        // This would be implemented with actual material checking
        
        PlayerFurnitureData furnitureData = getPlayerFurnitureData(player.getUniqueId());
        furnitureData.addFurniture(type, 1);
        
        player.sendMessage("§a" + config.getDisplayName() + " hergestellt!");
    }
    
    public PlayerFurnitureData getPlayerFurnitureData(UUID playerId) {
        return playerFurnitureData.computeIfAbsent(playerId, k -> new PlayerFurnitureData(playerId));
    }
    
    public FurnitureConfig getFurnitureConfig(FurnitureType type) {
        return furnitureConfigs.get(type);
    }
    
    public List<FurnitureType> getAllFurnitureTypes() {
        return new ArrayList<>(furnitureConfigs.keySet());
    }
    
    public enum FurnitureType {
        // Chairs
        OAK_CHAIR, SPRUCE_CHAIR, BIRCH_CHAIR, JUNGLE_CHAIR, ACACIA_CHAIR, DARK_OAK_CHAIR,
        // Tables
        OAK_TABLE, SPRUCE_TABLE, BIRCH_TABLE, JUNGLE_TABLE, ACACIA_TABLE, DARK_OAK_TABLE,
        // Beds
        OAK_BED, SPRUCE_BED, BIRCH_BED, JUNGLE_BED, ACACIA_BED, DARK_OAK_BED,
        // Storage
        OAK_CHEST, SPRUCE_CHEST, BIRCH_CHEST, JUNGLE_CHEST, ACACIA_CHEST, DARK_OAK_CHEST,
        OAK_BARREL, SPRUCE_BARREL, BIRCH_BARREL, JUNGLE_BARREL, ACACIA_BARREL, DARK_OAK_BARREL,
        // Decorative
        OAK_LAMP, SPRUCE_LAMP, BIRCH_LAMP, JUNGLE_LAMP, ACACIA_LAMP, DARK_OAK_LAMP,
        OAK_PLANT, SPRUCE_PLANT, BIRCH_PLANT, JUNGLE_PLANT, ACACIA_PLANT, DARK_OAK_PLANT,
        // Rare Furniture
        GOLDEN_CHAIR, DIAMOND_TABLE, EMERALD_BED, NETHERITE_CHEST
    }
    
    public enum FurnitureCategory {
        CHAIR("§6Chair", "§7Chairs for sitting"),
        TABLE("§6Table", "§7Tables for storage and crafting"),
        BED("§6Bed", "§7Beds for sleeping"),
        STORAGE("§6Storage", "§7Storage furniture"),
        DECORATIVE("§6Decorative", "§7Decorative furniture");
        
        private final String displayName;
        private final String description;
        
        FurnitureCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum FurnitureRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        FurnitureRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class FurnitureConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final FurnitureCategory category;
        private final FurnitureRarity rarity;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public FurnitureConfig(String name, String displayName, Material icon, String description,
                             FurnitureCategory category, FurnitureRarity rarity, int maxLevel, 
                             List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.maxLevel = maxLevel;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public FurnitureCategory getCategory() { return category; }
        public FurnitureRarity getRarity() { return rarity; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerFurnitureData {
        private final UUID playerId;
        private final Map<FurnitureType, Integer> furnitureCounts = new HashMap<>();
        private final Map<FurnitureType, Integer> furnitureLevels = new HashMap<>();
        private long lastUpdate;
        
        public PlayerFurnitureData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addFurniture(FurnitureType type, int amount) {
            furnitureCounts.put(type, furnitureCounts.getOrDefault(type, 0) + amount);
        }
        
        public void levelUpFurniture(FurnitureType type) {
            furnitureLevels.put(type, furnitureLevels.getOrDefault(type, 0) + 1);
        }
        
        public boolean hasFurniture(FurnitureType type) {
            return furnitureCounts.getOrDefault(type, 0) > 0;
        }
        
        public int getFurnitureCount(FurnitureType type) {
            return furnitureCounts.getOrDefault(type, 0);
        }
        
        public int getFurnitureLevel(FurnitureType type) {
            return furnitureLevels.getOrDefault(type, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
