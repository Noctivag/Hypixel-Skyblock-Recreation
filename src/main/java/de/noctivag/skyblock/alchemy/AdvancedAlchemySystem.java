package de.noctivag.skyblock.alchemy;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Alchemy System - Hypixel Skyblock Style
 * Implements Brewing Stand upgrades, Alchemy Collections, and special potions
 */
public class AdvancedAlchemySystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerAlchemyData> playerAlchemyData = new ConcurrentHashMap<>();
    private final Map<PotionType, PotionConfig> potionConfigs = new HashMap<>();
    private final Map<AlchemyLocation, AlchemyConfig> alchemyConfigs = new HashMap<>();
    
    public AdvancedAlchemySystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializePotionConfigs();
        initializeAlchemyConfigs();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializePotionConfigs() {
        // Health Potion
        potionConfigs.put(PotionType.HEALTH, new PotionConfig(
            PotionType.HEALTH,
            "§cHealth Potion",
            "§7Restores health",
            Material.POTION,
            1,
            Arrays.asList("Health Potion I", "Health Potion II", "Health Potion III")
        ));
        
        // Speed Potion
        potionConfigs.put(PotionType.SPEED, new PotionConfig(
            PotionType.SPEED,
            "§aSpeed Potion",
            "§7Increases movement speed",
            Material.POTION,
            1,
            Arrays.asList("Speed Potion I", "Speed Potion II", "Speed Potion III")
        ));
        
        // Strength Potion
        potionConfigs.put(PotionType.STRENGTH, new PotionConfig(
            PotionType.STRENGTH,
            "§cStrength Potion",
            "§7Increases melee damage",
            Material.POTION,
            2,
            Arrays.asList("Strength Potion I", "Strength Potion II", "Strength Potion III")
        ));
        
        // Regeneration Potion
        potionConfigs.put(PotionType.REGENERATION, new PotionConfig(
            PotionType.REGENERATION,
            "§dRegeneration Potion",
            "§7Regenerates health over time",
            Material.POTION,
            2,
            Arrays.asList("Regeneration Potion I", "Regeneration Potion II", "Regeneration Potion III")
        ));
        
        // Fire Resistance Potion
        potionConfigs.put(PotionType.FIRE_RESISTANCE, new PotionConfig(
            PotionType.FIRE_RESISTANCE,
            "§6Fire Resistance Potion",
            "§7Protects from fire damage",
            Material.POTION,
            2,
            Arrays.asList("Fire Resistance Potion I", "Fire Resistance Potion II", "Fire Resistance Potion III")
        ));
        
        // Water Breathing Potion
        potionConfigs.put(PotionType.WATER_BREATHING, new PotionConfig(
            PotionType.WATER_BREATHING,
            "§bWater Breathing Potion",
            "§7Allows breathing underwater",
            Material.POTION,
            2,
            Arrays.asList("Water Breathing Potion I", "Water Breathing Potion II", "Water Breathing Potion III")
        ));
        
        // Night Vision Potion
        potionConfigs.put(PotionType.NIGHT_VISION, new PotionConfig(
            PotionType.NIGHT_VISION,
            "§5Night Vision Potion",
            "§7Allows seeing in the dark",
            Material.POTION,
            2,
            Arrays.asList("Night Vision Potion I", "Night Vision Potion II", "Night Vision Potion III")
        ));
        
        // Invisibility Potion
        potionConfigs.put(PotionType.INVISIBILITY, new PotionConfig(
            PotionType.INVISIBILITY,
            "§7Invisibility Potion",
            "§7Makes the player invisible",
            Material.POTION,
            3,
            Arrays.asList("Invisibility Potion I", "Invisibility Potion II", "Invisibility Potion III")
        ));
        
        // Jump Boost Potion
        potionConfigs.put(PotionType.JUMP_BOOST, new PotionConfig(
            PotionType.JUMP_BOOST,
            "§aJump Boost Potion",
            "§7Increases jump height",
            Material.POTION,
            2,
            Arrays.asList("Jump Boost Potion I", "Jump Boost Potion II", "Jump Boost Potion III")
        ));
        
        // Poison Potion
        potionConfigs.put(PotionType.POISON, new PotionConfig(
            PotionType.POISON,
            "§2Poison Potion",
            "§7Poisons the target",
            Material.POTION,
            2,
            Arrays.asList("Poison Potion I", "Poison Potion II", "Poison Potion III")
        ));
        
        // Weakness Potion
        potionConfigs.put(PotionType.WEAKNESS, new PotionConfig(
            PotionType.WEAKNESS,
            "§8Weakness Potion",
            "§7Reduces melee damage",
            Material.POTION,
            2,
            Arrays.asList("Weakness Potion I", "Weakness Potion II", "Weakness Potion III")
        ));
        
        // Slowness Potion
        potionConfigs.put(PotionType.SLOWNESS, new PotionConfig(
            PotionType.SLOWNESS,
            "§7Slowness Potion",
            "§7Reduces movement speed",
            Material.POTION,
            2,
            Arrays.asList("Slowness Potion I", "Slowness Potion II", "Slowness Potion III")
        ));
    }
    
    private void initializeAlchemyConfigs() {
        // Spawn Island
        alchemyConfigs.put(AlchemyLocation.SPAWN, new AlchemyConfig(
            AlchemyLocation.SPAWN,
            "§aSpawn Island",
            "§7A peaceful alchemy spot",
            Material.BREWING_STAND,
            1,
            Arrays.asList(
                new AlchemyReward("Brewing Stand", Material.BREWING_STAND, 1000, "§dBrewing Stand"),
                new AlchemyReward("Blaze Powder", Material.BLAZE_POWDER, 500, "§eBlaze Powder"),
                new AlchemyReward("Nether Wart", Material.NETHER_WART, 200, "§cNether Wart")
            )
        ));
        
        // Deep Caverns
        alchemyConfigs.put(AlchemyLocation.DEEP_CAVERNS, new AlchemyConfig(
            AlchemyLocation.DEEP_CAVERNS,
            "§7Deep Caverns",
            "§7A deep alchemy spot",
            Material.BREWING_STAND,
            2,
            Arrays.asList(
                new AlchemyReward("Brewing Stand", Material.BREWING_STAND, 2000, "§dBrewing Stand"),
                new AlchemyReward("Blaze Powder", Material.BLAZE_POWDER, 1000, "§eBlaze Powder"),
                new AlchemyReward("Nether Wart", Material.NETHER_WART, 400, "§cNether Wart"),
                new AlchemyReward("Glowstone Dust", Material.GLOWSTONE_DUST, 300, "§eGlowstone Dust")
            )
        ));
        
        // The Nether
        alchemyConfigs.put(AlchemyLocation.THE_NETHER, new AlchemyConfig(
            AlchemyLocation.THE_NETHER,
            "§cThe Nether",
            "§7A nether alchemy spot",
            Material.BREWING_STAND,
            3,
            Arrays.asList(
                new AlchemyReward("Brewing Stand", Material.BREWING_STAND, 3000, "§dBrewing Stand"),
                new AlchemyReward("Blaze Powder", Material.BLAZE_POWDER, 1500, "§eBlaze Powder"),
                new AlchemyReward("Nether Wart", Material.NETHER_WART, 600, "§cNether Wart"),
                new AlchemyReward("Glowstone Dust", Material.GLOWSTONE_DUST, 500, "§eGlowstone Dust"),
                new AlchemyReward("Redstone", Material.REDSTONE, 400, "§cRedstone")
            )
        ));
    }
    
    @EventHandler
    public void onBrew(BrewEvent event) {
        // This would be called when a potion is brewed
        // Calculate alchemy XP and give rewards
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());
        
        if (displayName.contains("Alchemy") || displayName.contains("alchemy")) {
            openAlchemyGUI(player);
        }
    }
    
    public void openAlchemyGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§d§lAlchemy System"));
        
        // Add alchemy locations
        addGUIItem(gui, 10, Material.BREWING_STAND, "§a§lSpawn Island", "§7A peaceful alchemy spot");
        addGUIItem(gui, 11, Material.BREWING_STAND, "§7§lDeep Caverns", "§7A deep alchemy spot");
        addGUIItem(gui, 12, Material.BREWING_STAND, "§c§lThe Nether", "§7A nether alchemy spot");
        
        // Add alchemy management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Alchemy Progress", "§7View your alchemy progress.");
        addGUIItem(gui, 19, Material.BREWING_STAND, "§d§lBrewing Stand", "§7Use the brewing stand.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lAlchemy Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lAlchemy Shop", "§7Buy alchemy items.");
        addGUIItem(gui, 22, Material.GOLD_INGOT, "§6§lAlchemy Contests", "§7Join alchemy contests.");
        
        // Add potion types
        addGUIItem(gui, 27, Material.POTION, "§c§lHealth Potion", "§7Restores health");
        addGUIItem(gui, 28, Material.POTION, "§a§lSpeed Potion", "§7Increases movement speed");
        addGUIItem(gui, 29, Material.POTION, "§c§lStrength Potion", "§7Increases melee damage");
        addGUIItem(gui, 30, Material.POTION, "§d§lRegeneration Potion", "§7Regenerates health over time");
        addGUIItem(gui, 31, Material.POTION, "§6§lFire Resistance Potion", "§7Protects from fire damage");
        addGUIItem(gui, 32, Material.POTION, "§b§lWater Breathing Potion", "§7Allows breathing underwater");
        addGUIItem(gui, 33, Material.POTION, "§5§lNight Vision Potion", "§7Allows seeing in the dark");
        addGUIItem(gui, 34, Material.POTION, "§7§lInvisibility Potion", "§7Makes the player invisible");
        addGUIItem(gui, 35, Material.POTION, "§a§lJump Boost Potion", "§7Increases jump height");
        
        // Add negative potions
        addGUIItem(gui, 36, Material.POTION, "§2§lPoison Potion", "§7Poisons the target");
        addGUIItem(gui, 37, Material.POTION, "§8§lWeakness Potion", "§7Reduces melee damage");
        addGUIItem(gui, 38, Material.POTION, "§7§lSlowness Potion", "§7Reduces movement speed");
        
        // Add special items
        addGUIItem(gui, 45, Material.BREWING_STAND, "§d§lBrewing Stand", "§7A basic brewing stand");
        addGUIItem(gui, 46, Material.BLAZE_POWDER, "§e§lBlaze Powder", "§7Required for brewing");
        addGUIItem(gui, 47, Material.NETHER_WART, "§c§lNether Wart", "§7Base ingredient for potions");
        addGUIItem(gui, 48, Material.GLOWSTONE_DUST, "§e§lGlowstone Dust", "§7Increases potion duration");
        addGUIItem(gui, 49, Material.REDSTONE, "§c§lRedstone", "§7Increases potion duration");
        
        // Navigation
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aAlchemy GUI opened!");
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
    
    public PlayerAlchemyData getPlayerAlchemyData(UUID playerId) {
        return playerAlchemyData.computeIfAbsent(playerId, k -> new PlayerAlchemyData(playerId));
    }
    
    public enum PotionType {
        HEALTH("§cHealth Potion", "§7Restores health"),
        SPEED("§aSpeed Potion", "§7Increases movement speed"),
        STRENGTH("§cStrength Potion", "§7Increases melee damage"),
        REGENERATION("§dRegeneration Potion", "§7Regenerates health over time"),
        FIRE_RESISTANCE("§6Fire Resistance Potion", "§7Protects from fire damage"),
        WATER_BREATHING("§bWater Breathing Potion", "§7Allows breathing underwater"),
        NIGHT_VISION("§5Night Vision Potion", "§7Allows seeing in the dark"),
        INVISIBILITY("§7Invisibility Potion", "§7Makes the player invisible"),
        JUMP_BOOST("§aJump Boost Potion", "§7Increases jump height"),
        POISON("§2Poison Potion", "§7Poisons the target"),
        WEAKNESS("§8Weakness Potion", "§7Reduces melee damage"),
        SLOWNESS("§7Slowness Potion", "§7Reduces movement speed");
        
        private final String displayName;
        private final String description;
        
        PotionType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum AlchemyLocation {
        SPAWN("§aSpawn Island", "§7A peaceful alchemy spot"),
        DEEP_CAVERNS("§7Deep Caverns", "§7A deep alchemy spot"),
        THE_NETHER("§cThe Nether", "§7A nether alchemy spot");
        
        private final String displayName;
        private final String description;
        
        AlchemyLocation(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class PotionConfig {
        private final PotionType type;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<String> levels;
        
        public PotionConfig(PotionType type, String displayName, String description, Material icon,
                          int requiredLevel, List<String> levels) {
            this.type = type;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.levels = levels;
        }
        
        public PotionType getType() { return type; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<String> getLevels() { return levels; }
    }
    
    public static class AlchemyConfig {
        private final AlchemyLocation location;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<AlchemyReward> rewards;
        
        public AlchemyConfig(AlchemyLocation location, String displayName, String description, Material icon,
                           int requiredLevel, List<AlchemyReward> rewards) {
            this.location = location;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.rewards = rewards;
        }
        
        public AlchemyLocation getLocation() { return location; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<AlchemyReward> getRewards() { return rewards; }
    }
    
    public static class AlchemyReward {
        private final String name;
        private final Material material;
        private final int cost;
        private final String displayName;
        
        public AlchemyReward(String name, Material material, int cost, String displayName) {
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
    
    public static class PlayerAlchemyData {
        private final UUID playerId;
        private int alchemyLevel;
        private int alchemyXP;
        private final Map<AlchemyLocation, Integer> locationStats = new HashMap<>();
        private final Map<PotionType, Integer> potionStats = new HashMap<>();
        
        public PlayerAlchemyData(UUID playerId) {
            this.playerId = playerId;
            this.alchemyLevel = 1;
            this.alchemyXP = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getAlchemyLevel() { return alchemyLevel; }
        public int getAlchemyXP() { return alchemyXP; }
        public int getLocationStats(AlchemyLocation location) { return locationStats.getOrDefault(location, 0); }
        public int getPotionStats(PotionType type) { return potionStats.getOrDefault(type, 0); }
        
        public void addAlchemyXP(int xp) {
            this.alchemyXP += xp;
            checkLevelUp();
        }
        
        public void addLocationStat(AlchemyLocation location) {
            locationStats.put(location, locationStats.getOrDefault(location, 0) + 1);
        }
        
        public void addPotionStat(PotionType type) {
            potionStats.put(type, potionStats.getOrDefault(type, 0) + 1);
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(alchemyLevel + 1);
            if (alchemyXP >= requiredXP) {
                alchemyLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
}
