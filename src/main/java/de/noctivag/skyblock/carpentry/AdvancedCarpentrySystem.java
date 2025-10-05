package de.noctivag.skyblock.carpentry;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Advanced Carpentry System - Hypixel Skyblock Style
 * Implements Custom Furniture, Building Tools, and Carpentry Collections
 */
public class AdvancedCarpentrySystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCarpentryData> playerCarpentryData = new ConcurrentHashMap<>();
    private final Map<FurnitureType, FurnitureConfig> furnitureConfigs = new HashMap<>();
    private final Map<CarpentryLocation, CarpentryConfig> carpentryConfigs = new HashMap<>();
    
    public AdvancedCarpentrySystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        initializeFurnitureConfigs();
        initializeCarpentryConfigs();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeFurnitureConfigs() {
        // Chairs
        furnitureConfigs.put(FurnitureType.CHAIR, new FurnitureConfig(
            FurnitureType.CHAIR,
            "§6Chair",
            "§7A basic chair",
            Material.OAK_STAIRS,
            FurnitureCategory.SEATING,
            1,
            Arrays.asList("Basic seating", "Comfortable", "Decorative")
        ));
        
        furnitureConfigs.put(FurnitureType.THONE, new FurnitureConfig(
            FurnitureType.THONE,
            "§6Throne",
            "§7A royal throne",
            Material.GOLD_BLOCK,
            FurnitureCategory.SEATING,
            3,
            Arrays.asList("Royal seating", "Luxurious", "Decorative", "Expensive")
        ));
        
        // Tables
        furnitureConfigs.put(FurnitureType.TABLE, new FurnitureConfig(
            FurnitureType.TABLE,
            "§6Table",
            "§7A basic table",
            Material.OAK_PRESSURE_PLATE,
            FurnitureCategory.TABLE,
            1,
            Arrays.asList("Basic table", "Functional", "Decorative")
        ));
        
        furnitureConfigs.put(FurnitureType.ROYAL_TABLE, new FurnitureConfig(
            FurnitureType.ROYAL_TABLE,
            "§6Royal Table",
            "§7A royal table",
            Material.GOLD_BLOCK,
            FurnitureCategory.TABLE,
            3,
            Arrays.asList("Royal table", "Luxurious", "Decorative", "Expensive")
        ));
        
        // Beds
        furnitureConfigs.put(FurnitureType.BED, new FurnitureConfig(
            FurnitureType.BED,
            "§6Bed",
            "§7A basic bed",
            Material.RED_BED,
            FurnitureCategory.SLEEPING,
            1,
            Arrays.asList("Basic bed", "Comfortable", "Decorative")
        ));
        
        furnitureConfigs.put(FurnitureType.ROYAL_BED, new FurnitureConfig(
            FurnitureType.ROYAL_BED,
            "§6Royal Bed",
            "§7A royal bed",
            Material.GOLD_BLOCK,
            FurnitureCategory.SLEEPING,
            3,
            Arrays.asList("Royal bed", "Luxurious", "Decorative", "Expensive")
        ));
        
        // Storage
        furnitureConfigs.put(FurnitureType.CHEST, new FurnitureConfig(
            FurnitureType.CHEST,
            "§6Chest",
            "§7A basic chest",
            Material.CHEST,
            FurnitureCategory.STORAGE,
            1,
            Arrays.asList("Basic storage", "Functional", "Decorative")
        ));
        
        furnitureConfigs.put(FurnitureType.ROYAL_CHEST, new FurnitureConfig(
            FurnitureType.ROYAL_CHEST,
            "§6Royal Chest",
            "§7A royal chest",
            Material.GOLD_BLOCK,
            FurnitureCategory.STORAGE,
            3,
            Arrays.asList("Royal storage", "Luxurious", "Decorative", "Expensive")
        ));
        
        // Decorative
        furnitureConfigs.put(FurnitureType.PAINTING, new FurnitureConfig(
            FurnitureType.PAINTING,
            "§6Painting",
            "§7A decorative painting",
            Material.PAINTING,
            FurnitureCategory.DECORATIVE,
            1,
            Arrays.asList("Decorative", "Artistic", "Beautiful")
        ));
        
        furnitureConfigs.put(FurnitureType.SCULPTURE, new FurnitureConfig(
            FurnitureType.SCULPTURE,
            "§6Sculpture",
            "§7A decorative sculpture",
            Material.STONE,
            FurnitureCategory.DECORATIVE,
            2,
            Arrays.asList("Decorative", "Artistic", "Beautiful", "Expensive")
        ));
        
        furnitureConfigs.put(FurnitureType.FOUNTAIN, new FurnitureConfig(
            FurnitureType.FOUNTAIN,
            "§6Fountain",
            "§7A decorative fountain",
            Material.WATER_BUCKET,
            FurnitureCategory.DECORATIVE,
            3,
            Arrays.asList("Decorative", "Artistic", "Beautiful", "Expensive", "Water feature")
        ));
        
        // Lighting
        furnitureConfigs.put(FurnitureType.TORCH, new FurnitureConfig(
            FurnitureType.TORCH,
            "§6Torch",
            "§7A basic torch",
            Material.TORCH,
            FurnitureCategory.LIGHTING,
            1,
            Arrays.asList("Basic lighting", "Functional", "Decorative")
        ));
        
        furnitureConfigs.put(FurnitureType.CHANDELIER, new FurnitureConfig(
            FurnitureType.CHANDELIER,
            "§6Chandelier",
            "§7A decorative chandelier",
            Material.GOLD_BLOCK,
            FurnitureCategory.LIGHTING,
            3,
            Arrays.asList("Decorative lighting", "Luxurious", "Beautiful", "Expensive")
        ));
    }
    
    private void initializeCarpentryConfigs() {
        // Spawn Island
        carpentryConfigs.put(CarpentryLocation.SPAWN, new CarpentryConfig(
            CarpentryLocation.SPAWN,
            "§aSpawn Island",
            "§7A peaceful carpentry spot",
            Material.OAK_LOG,
            1,
            Arrays.asList(
                new CarpentryReward("Oak Log", Material.OAK_LOG, 100, "§6Oak Log"),
                new CarpentryReward("Oak Planks", Material.OAK_PLANKS, 150, "§6Oak Planks"),
                new CarpentryReward("Oak Stairs", Material.OAK_STAIRS, 200, "§6Oak Stairs")
            )
        ));
        
        // Deep Caverns
        carpentryConfigs.put(CarpentryLocation.DEEP_CAVERNS, new CarpentryConfig(
            CarpentryLocation.DEEP_CAVERNS,
            "§7Deep Caverns",
            "§7A deep carpentry spot",
            Material.STONE,
            2,
            Arrays.asList(
                new CarpentryReward("Oak Log", Material.OAK_LOG, 200, "§6Oak Log"),
                new CarpentryReward("Oak Planks", Material.OAK_PLANKS, 300, "§6Oak Planks"),
                new CarpentryReward("Oak Stairs", Material.OAK_STAIRS, 400, "§6Oak Stairs"),
                new CarpentryReward("Stone", Material.STONE, 250, "§7Stone")
            )
        ));
        
        // The End
        carpentryConfigs.put(CarpentryLocation.THE_END, new CarpentryConfig(
            CarpentryLocation.THE_END,
            "§5The End",
            "§7An end carpentry spot",
            Material.END_STONE,
            3,
            Arrays.asList(
                new CarpentryReward("Oak Log", Material.OAK_LOG, 300, "§6Oak Log"),
                new CarpentryReward("Oak Planks", Material.OAK_PLANKS, 450, "§6Oak Planks"),
                new CarpentryReward("Oak Stairs", Material.OAK_STAIRS, 600, "§6Oak Stairs"),
                new CarpentryReward("Stone", Material.STONE, 400, "§7Stone"),
                new CarpentryReward("End Stone", Material.END_STONE, 500, "§fEnd Stone")
            )
        ));
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());
        
        if (displayName.contains("Carpentry") || displayName.contains("carpentry")) {
            openCarpentryGUI(player);
        }
    }
    
    public void openCarpentryGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lCarpentry System"));
        
        // Add carpentry locations
        addGUIItem(gui, 10, Material.OAK_LOG, "§a§lSpawn Island", "§7A peaceful carpentry spot");
        addGUIItem(gui, 11, Material.STONE, "§7§lDeep Caverns", "§7A deep carpentry spot");
        addGUIItem(gui, 12, Material.END_STONE, "§5§lThe End", "§7An end carpentry spot");
        
        // Add carpentry management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Carpentry Progress", "§7View your carpentry progress.");
        addGUIItem(gui, 19, Material.CRAFTING_TABLE, "§6§lCrafting Table", "§7Use the crafting table.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lCarpentry Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lCarpentry Shop", "§7Buy carpentry items.");
        addGUIItem(gui, 22, Material.GOLD_INGOT, "§6§lCarpentry Contests", "§7Join carpentry contests.");
        
        // Add furniture categories
        addGUIItem(gui, 27, Material.OAK_STAIRS, "§6§lSeating", "§7Chairs and thrones");
        addGUIItem(gui, 28, Material.OAK_PRESSURE_PLATE, "§6§lTables", "§7Tables and surfaces");
        addGUIItem(gui, 29, Material.RED_BED, "§6§lSleeping", "§7Beds and sleeping furniture");
        addGUIItem(gui, 30, Material.CHEST, "§6§lStorage", "§7Chests and storage furniture");
        addGUIItem(gui, 31, Material.PAINTING, "§6§lDecorative", "§7Paintings and sculptures");
        addGUIItem(gui, 32, Material.TORCH, "§6§lLighting", "§7Torches and chandeliers");
        
        // Add furniture types
        addGUIItem(gui, 36, Material.OAK_STAIRS, "§6§lChair", "§7A basic chair");
        addGUIItem(gui, 37, Material.GOLD_BLOCK, "§6§lThrone", "§7A royal throne");
        addGUIItem(gui, 38, Material.OAK_PRESSURE_PLATE, "§6§lTable", "§7A basic table");
        addGUIItem(gui, 39, Material.GOLD_BLOCK, "§6§lRoyal Table", "§7A royal table");
        addGUIItem(gui, 40, Material.RED_BED, "§6§lBed", "§7A basic bed");
        addGUIItem(gui, 41, Material.GOLD_BLOCK, "§6§lRoyal Bed", "§7A royal bed");
        addGUIItem(gui, 42, Material.CHEST, "§6§lChest", "§7A basic chest");
        addGUIItem(gui, 43, Material.GOLD_BLOCK, "§6§lRoyal Chest", "§7A royal chest");
        addGUIItem(gui, 44, Material.PAINTING, "§6§lPainting", "§7A decorative painting");
        addGUIItem(gui, 45, Material.STONE, "§6§lSculpture", "§7A decorative sculpture");
        addGUIItem(gui, 46, Material.WATER_BUCKET, "§6§lFountain", "§7A decorative fountain");
        addGUIItem(gui, 47, Material.TORCH, "§6§lTorch", "§7A basic torch");
        addGUIItem(gui, 48, Material.GOLD_BLOCK, "§6§lChandelier", "§7A decorative chandelier");
        
        // Add building tools
        addGUIItem(gui, 49, Material.DIAMOND_AXE, "§6§lBuilding Axe", "§7A tool for building");
        addGUIItem(gui, 50, Material.DIAMOND_PICKAXE, "§6§lBuilding Pickaxe", "§7A tool for building");
        addGUIItem(gui, 51, Material.DIAMOND_SHOVEL, "§6§lBuilding Shovel", "§7A tool for building");
        addGUIItem(gui, 52, Material.DIAMOND_HOE, "§6§lBuilding Hoe", "§7A tool for building");
        addGUIItem(gui, 53, Material.DIAMOND_SWORD, "§6§lBuilding Sword", "§7A tool for building");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aCarpentry GUI opened!"));
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerCarpentryData getPlayerCarpentryData(UUID playerId) {
        return playerCarpentryData.computeIfAbsent(playerId, k -> new PlayerCarpentryData(playerId));
    }
    
    public enum FurnitureType {
        CHAIR("§6Chair", "§7A basic chair"),
        THONE("§6Throne", "§7A royal throne"),
        TABLE("§6Table", "§7A basic table"),
        ROYAL_TABLE("§6Royal Table", "§7A royal table"),
        BED("§6Bed", "§7A basic bed"),
        ROYAL_BED("§6Royal Bed", "§7A royal bed"),
        CHEST("§6Chest", "§7A basic chest"),
        ROYAL_CHEST("§6Royal Chest", "§7A royal chest"),
        PAINTING("§6Painting", "§7A decorative painting"),
        SCULPTURE("§6Sculpture", "§7A decorative sculpture"),
        FOUNTAIN("§6Fountain", "§7A decorative fountain"),
        TORCH("§6Torch", "§7A basic torch"),
        CHANDELIER("§6Chandelier", "§7A decorative chandelier");
        
        private final String displayName;
        private final String description;
        
        FurnitureType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum FurnitureCategory {
        SEATING("§6Seating", "§7Chairs and thrones"),
        TABLE("§6Table", "§7Tables and surfaces"),
        SLEEPING("§6Sleeping", "§7Beds and sleeping furniture"),
        STORAGE("§6Storage", "§7Chests and storage furniture"),
        DECORATIVE("§6Decorative", "§7Paintings and sculptures"),
        LIGHTING("§6Lighting", "§7Torches and chandeliers");
        
        private final String displayName;
        private final String description;
        
        FurnitureCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum CarpentryLocation {
        SPAWN("§aSpawn Island", "§7A peaceful carpentry spot"),
        DEEP_CAVERNS("§7Deep Caverns", "§7A deep carpentry spot"),
        THE_END("§5The End", "§7An end carpentry spot");
        
        private final String displayName;
        private final String description;
        
        CarpentryLocation(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class FurnitureConfig {
        private final FurnitureType type;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final FurnitureCategory category;
        private final int requiredLevel;
        private final List<String> features;
        
        public FurnitureConfig(FurnitureType type, String displayName, String description, Material icon,
                             FurnitureCategory category, int requiredLevel, List<String> features) {
            this.type = type;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.category = category;
            this.requiredLevel = requiredLevel;
            this.features = features;
        }
        
        public FurnitureType getType() { return type; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public FurnitureCategory getCategory() { return category; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<String> getFeatures() { return features; }
    }
    
    public static class CarpentryConfig {
        private final CarpentryLocation location;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<CarpentryReward> rewards;
        
        public CarpentryConfig(CarpentryLocation location, String displayName, String description, Material icon,
                             int requiredLevel, List<CarpentryReward> rewards) {
            this.location = location;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.rewards = rewards;
        }
        
        public CarpentryLocation getLocation() { return location; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<CarpentryReward> getRewards() { return rewards; }
    }
    
    public static class CarpentryReward {
        private final String name;
        private final Material material;
        private final int cost;
        private final String displayName;
        
        public CarpentryReward(String name, Material material, int cost, String displayName) {
            this.name = name;
            this.material = material;
            this.cost = cost;
            this.displayName = displayName;
        }
        
        public String getName() { return name; }
        public Material getMaterial() { return material; }
        public int getCost() { return cost; }
        public String getDisplayName() { return displayName; }
    }
    
    public static class PlayerCarpentryData {
        private final UUID playerId;
        private int carpentryLevel;
        private int carpentryXP;
        private final Map<CarpentryLocation, Integer> locationStats = new HashMap<>();
        private final Map<FurnitureType, Integer> furnitureStats = new HashMap<>();
        
        public PlayerCarpentryData(UUID playerId) {
            this.playerId = playerId;
            this.carpentryLevel = 1;
            this.carpentryXP = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getCarpentryLevel() { return carpentryLevel; }
        public int getCarpentryXP() { return carpentryXP; }
        public int getLocationStats(CarpentryLocation location) { return locationStats.getOrDefault(location, 0); }
        public int getFurnitureStats(FurnitureType type) { return furnitureStats.getOrDefault(type, 0); }
        
        public void addCarpentryXP(int xp) {
            this.carpentryXP += xp;
            checkLevelUp();
        }
        
        public void addLocationStat(CarpentryLocation location) {
            locationStats.put(location, locationStats.getOrDefault(location, 0) + 1);
        }
        
        public void addFurnitureStat(FurnitureType type) {
            furnitureStats.put(type, furnitureStats.getOrDefault(type, 0) + 1);
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(carpentryLevel + 1);
            if (carpentryXP >= requiredXP) {
                carpentryLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
}
