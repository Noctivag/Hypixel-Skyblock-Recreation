package de.noctivag.skyblock.runecrafting;

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
 * Advanced Runecrafting System - Hypixel Skyblock Style
 * Implements Rune Creation, Rune Upgrades, and Runecrafting Collections
 */
public class AdvancedRunecraftingSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerRunecraftingData> playerRunecraftingData = new ConcurrentHashMap<>();
    private final Map<RuneType, RuneConfig> runeConfigs = new HashMap<>();
    private final Map<RunecraftingLocation, RunecraftingConfig> runecraftingConfigs = new HashMap<>();
    
    public AdvancedRunecraftingSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        initializeRuneConfigs();
        initializeRunecraftingConfigs();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeRuneConfigs() {
        // Combat Runes
        runeConfigs.put(RuneType.SHARPNESS, new RuneConfig(
            RuneType.SHARPNESS,
            "§cSharpness Rune",
            "§7Increases melee damage",
            Material.REDSTONE,
            RuneCategory.COMBAT,
            1,
            Arrays.asList("Increases melee damage", "Increases critical chance", "Increases attack speed")
        ));
        
        runeConfigs.put(RuneType.PROTECTION, new RuneConfig(
            RuneType.PROTECTION,
            "§aProtection Rune",
            "§7Reduces damage from all sources",
            Material.EMERALD,
            RuneCategory.COMBAT,
            1,
            Arrays.asList("Reduces damage", "Increases defense", "Increases health")
        ));
        
        runeConfigs.put(RuneType.FIRE, new RuneConfig(
            RuneType.FIRE,
            "§cFire Rune",
            "§7Sets targets on fire",
            Material.FIRE_CHARGE,
            RuneCategory.COMBAT,
            2,
            Arrays.asList("Sets targets on fire", "Increases fire damage", "Increases fire resistance")
        ));
        
        runeConfigs.put(RuneType.ICE, new RuneConfig(
            RuneType.ICE,
            "§bIce Rune",
            "§7Slows down targets",
            Material.ICE,
            RuneCategory.COMBAT,
            2,
            Arrays.asList("Slows down targets", "Increases ice damage", "Increases ice resistance")
        ));
        
        runeConfigs.put(RuneType.LIGHTNING, new RuneConfig(
            RuneType.LIGHTNING,
            "§eLightning Rune",
            "§7Strikes targets with lightning",
            Material.LIGHTNING_ROD,
            RuneCategory.COMBAT,
            3,
            Arrays.asList("Strikes targets with lightning", "Increases lightning damage", "Increases lightning resistance")
        ));
        
        // Mining Runes
        runeConfigs.put(RuneType.EFFICIENCY, new RuneConfig(
            RuneType.EFFICIENCY,
            "§eEfficiency Rune",
            "§7Increases mining speed",
            Material.GOLD_INGOT,
            RuneCategory.MINING,
            1,
            Arrays.asList("Increases mining speed", "Increases mining XP", "Increases ore drops")
        ));
        
        runeConfigs.put(RuneType.FORTUNE, new RuneConfig(
            RuneType.FORTUNE,
            "§6Fortune Rune",
            "§7Increases block drops",
            Material.DIAMOND,
            RuneCategory.MINING,
            2,
            Arrays.asList("Increases block drops", "Increases rare drops", "Increases mining luck")
        ));
        
        runeConfigs.put(RuneType.SILK_TOUCH, new RuneConfig(
            RuneType.SILK_TOUCH,
            "§fSilk Touch Rune",
            "§7Mines blocks without breaking them",
            Material.SLIME_BALL,
            RuneCategory.MINING,
            3,
            Arrays.asList("Mines blocks without breaking them", "Increases block preservation", "Increases mining precision")
        ));
        
        // Farming Runes
        runeConfigs.put(RuneType.GROWTH, new RuneConfig(
            RuneType.GROWTH,
            "§aGrowth Rune",
            "§7Increases crop growth speed",
            Material.WHEAT,
            RuneCategory.FARMING,
            1,
            Arrays.asList("Increases crop growth speed", "Increases farming XP", "Increases crop yields")
        ));
        
        runeConfigs.put(RuneType.HARVEST, new RuneConfig(
            RuneType.HARVEST,
            "§6Harvest Rune",
            "§7Increases crop harvest",
            Material.GOLDEN_HOE,
            RuneCategory.FARMING,
            2,
            Arrays.asList("Increases crop harvest", "Increases farming efficiency", "Increases crop quality")
        ));
        
        runeConfigs.put(RuneType.FERTILIZER, new RuneConfig(
            RuneType.FERTILIZER,
            "§2Fertilizer Rune",
            "§7Increases soil fertility",
            Material.BONE_MEAL,
            RuneCategory.FARMING,
            3,
            Arrays.asList("Increases soil fertility", "Increases crop growth", "Increases farming success")
        ));
        
        // Foraging Runes
        runeConfigs.put(RuneType.TREECAPITATOR, new RuneConfig(
            RuneType.TREECAPITATOR,
            "§6Treecapitator Rune",
            "§7Chops entire trees",
            Material.DIAMOND_AXE,
            RuneCategory.FORAGING,
            1,
            Arrays.asList("Chops entire trees", "Increases foraging speed", "Increases wood drops")
        ));
        
        runeConfigs.put(RuneType.LUMBERJACK, new RuneConfig(
            RuneType.LUMBERJACK,
            "§6Lumberjack Rune",
            "§7Increases wood cutting efficiency",
            Material.IRON_AXE,
            RuneCategory.FORAGING,
            2,
            Arrays.asList("Increases wood cutting efficiency", "Increases foraging XP", "Increases wood quality")
        ));
        
        runeConfigs.put(RuneType.FOREST, new RuneConfig(
            RuneType.FOREST,
            "§2Forest Rune",
            "§7Increases forest growth",
            Material.OAK_SAPLING,
            RuneCategory.FORAGING,
            3,
            Arrays.asList("Increases forest growth", "Increases tree regeneration", "Increases forest health")
        ));
        
        // Fishing Runes
        runeConfigs.put(RuneType.LURE, new RuneConfig(
            RuneType.LURE,
            "§bLure Rune",
            "§7Attracts fish",
            Material.FISHING_ROD,
            RuneCategory.FISHING,
            1,
            Arrays.asList("Attracts fish", "Increases fishing speed", "Increases fish catches")
        ));
        
        runeConfigs.put(RuneType.LUCK_OF_THE_SEA, new RuneConfig(
            RuneType.LUCK_OF_THE_SEA,
            "§bLuck of the Sea Rune",
            "§7Increases fishing luck",
            Material.NAUTILUS_SHELL,
            RuneCategory.FISHING,
            2,
            Arrays.asList("Increases fishing luck", "Increases rare catches", "Increases fishing success")
        ));
        
        runeConfigs.put(RuneType.SEA_CREATURE, new RuneConfig(
            RuneType.SEA_CREATURE,
            "§dSea Creature Rune",
            "§7Attracts sea creatures",
            Material.PRISMARINE_SHARD,
            RuneCategory.FISHING,
            3,
            Arrays.asList("Attracts sea creatures", "Increases sea creature spawns", "Increases sea creature drops")
        ));
    }
    
    private void initializeRunecraftingConfigs() {
        // Spawn Island
        runecraftingConfigs.put(RunecraftingLocation.SPAWN, new RunecraftingConfig(
            RunecraftingLocation.SPAWN,
            "§aSpawn Island",
            "§7A peaceful runecrafting spot",
            Material.ENCHANTING_TABLE,
            1,
            Arrays.asList(
                new RunecraftingReward("Redstone", Material.REDSTONE, 100, "§cRedstone"),
                new RunecraftingReward("Emerald", Material.EMERALD, 150, "§aEmerald"),
                new RunecraftingReward("Gold Ingot", Material.GOLD_INGOT, 200, "§6Gold Ingot")
            )
        ));
        
        // Deep Caverns
        runecraftingConfigs.put(RunecraftingLocation.DEEP_CAVERNS, new RunecraftingConfig(
            RunecraftingLocation.DEEP_CAVERNS,
            "§7Deep Caverns",
            "§7A deep runecrafting spot",
            Material.ENCHANTING_TABLE,
            2,
            Arrays.asList(
                new RunecraftingReward("Redstone", Material.REDSTONE, 200, "§cRedstone"),
                new RunecraftingReward("Emerald", Material.EMERALD, 300, "§aEmerald"),
                new RunecraftingReward("Gold Ingot", Material.GOLD_INGOT, 400, "§6Gold Ingot"),
                new RunecraftingReward("Diamond", Material.DIAMOND, 500, "§bDiamond")
            )
        ));
        
        // The End
        runecraftingConfigs.put(RunecraftingLocation.THE_END, new RunecraftingConfig(
            RunecraftingLocation.THE_END,
            "§5The End",
            "§7An end runecrafting spot",
            Material.ENCHANTING_TABLE,
            3,
            Arrays.asList(
                new RunecraftingReward("Redstone", Material.REDSTONE, 300, "§cRedstone"),
                new RunecraftingReward("Emerald", Material.EMERALD, 450, "§aEmerald"),
                new RunecraftingReward("Gold Ingot", Material.GOLD_INGOT, 600, "§6Gold Ingot"),
                new RunecraftingReward("Diamond", Material.DIAMOND, 750, "§bDiamond"),
                new RunecraftingReward("Ender Pearl", Material.ENDER_PEARL, 1000, "§5Ender Pearl")
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
        
        if (displayName.contains("Runecrafting") || displayName.contains("runecrafting")) {
            openRunecraftingGUI(player);
        }
    }
    
    public void openRunecraftingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§d§lRunecrafting System"));
        
        // Add runecrafting locations
        addGUIItem(gui, 10, Material.ENCHANTING_TABLE, "§a§lSpawn Island", "§7A peaceful runecrafting spot");
        addGUIItem(gui, 11, Material.ENCHANTING_TABLE, "§7§lDeep Caverns", "§7A deep runecrafting spot");
        addGUIItem(gui, 12, Material.ENCHANTING_TABLE, "§5§lThe End", "§7An end runecrafting spot");
        
        // Add runecrafting management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Runecrafting Progress", "§7View your runecrafting progress.");
        addGUIItem(gui, 19, Material.ENCHANTING_TABLE, "§d§lRune Table", "§7Use the rune table.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lRunecrafting Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lRunecrafting Shop", "§7Buy runecrafting items.");
        addGUIItem(gui, 22, Material.GOLD_INGOT, "§6§lRunecrafting Contests", "§7Join runecrafting contests.");
        
        // Add rune categories
        addGUIItem(gui, 27, Material.DIAMOND_SWORD, "§c§lCombat Runes", "§7Combat-related runes");
        addGUIItem(gui, 28, Material.DIAMOND_PICKAXE, "§7§lMining Runes", "§7Mining-related runes");
        addGUIItem(gui, 29, Material.DIAMOND_HOE, "§a§lFarming Runes", "§7Farming-related runes");
        addGUIItem(gui, 30, Material.DIAMOND_AXE, "§6§lForaging Runes", "§7Foraging-related runes");
        addGUIItem(gui, 31, Material.FISHING_ROD, "§b§lFishing Runes", "§7Fishing-related runes");
        
        // Add combat runes
        addGUIItem(gui, 36, Material.REDSTONE, "§c§lSharpness Rune", "§7Increases melee damage");
        addGUIItem(gui, 37, Material.EMERALD, "§a§lProtection Rune", "§7Reduces damage from all sources");
        addGUIItem(gui, 38, Material.FIRE_CHARGE, "§c§lFire Rune", "§7Sets targets on fire");
        addGUIItem(gui, 39, Material.ICE, "§b§lIce Rune", "§7Slows down targets");
        addGUIItem(gui, 40, Material.LIGHTNING_ROD, "§e§lLightning Rune", "§7Strikes targets with lightning");
        
        // Add mining runes
        addGUIItem(gui, 45, Material.GOLD_INGOT, "§e§lEfficiency Rune", "§7Increases mining speed");
        addGUIItem(gui, 46, Material.DIAMOND, "§6§lFortune Rune", "§7Increases block drops");
        addGUIItem(gui, 47, Material.SLIME_BALL, "§f§lSilk Touch Rune", "§7Mines blocks without breaking them");
        
        // Add farming runes
        addGUIItem(gui, 48, Material.WHEAT, "§a§lGrowth Rune", "§7Increases crop growth speed");
        addGUIItem(gui, 49, Material.GOLDEN_HOE, "§6§lHarvest Rune", "§7Increases crop harvest");
        addGUIItem(gui, 50, Material.BONE_MEAL, "§2§lFertilizer Rune", "§7Increases soil fertility");
        
        // Add foraging runes
        addGUIItem(gui, 51, Material.DIAMOND_AXE, "§6§lTreecapitator Rune", "§7Chops entire trees");
        addGUIItem(gui, 52, Material.IRON_AXE, "§6§lLumberjack Rune", "§7Increases wood cutting efficiency");
        addGUIItem(gui, 53, Material.OAK_SAPLING, "§2§lForest Rune", "§7Increases forest growth");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aRunecrafting GUI opened!"));
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
    
    public PlayerRunecraftingData getPlayerRunecraftingData(UUID playerId) {
        return playerRunecraftingData.computeIfAbsent(playerId, k -> new PlayerRunecraftingData(playerId));
    }
    
    public enum RuneType {
        SHARPNESS("§cSharpness Rune", "§7Increases melee damage"),
        PROTECTION("§aProtection Rune", "§7Reduces damage from all sources"),
        FIRE("§cFire Rune", "§7Sets targets on fire"),
        ICE("§bIce Rune", "§7Slows down targets"),
        LIGHTNING("§eLightning Rune", "§7Strikes targets with lightning"),
        EFFICIENCY("§eEfficiency Rune", "§7Increases mining speed"),
        FORTUNE("§6Fortune Rune", "§7Increases block drops"),
        SILK_TOUCH("§fSilk Touch Rune", "§7Mines blocks without breaking them"),
        GROWTH("§aGrowth Rune", "§7Increases crop growth speed"),
        HARVEST("§6Harvest Rune", "§7Increases crop harvest"),
        FERTILIZER("§2Fertilizer Rune", "§7Increases soil fertility"),
        TREECAPITATOR("§6Treecapitator Rune", "§7Chops entire trees"),
        LUMBERJACK("§6Lumberjack Rune", "§7Increases wood cutting efficiency"),
        FOREST("§2Forest Rune", "§7Increases forest growth"),
        LURE("§bLure Rune", "§7Attracts fish"),
        LUCK_OF_THE_SEA("§bLuck of the Sea Rune", "§7Increases fishing luck"),
        SEA_CREATURE("§dSea Creature Rune", "§7Attracts sea creatures");
        
        private final String displayName;
        private final String description;
        
        RuneType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum RuneCategory {
        COMBAT("§cCombat", "§7Combat-related runes"),
        MINING("§7Mining", "§7Mining-related runes"),
        FARMING("§aFarming", "§7Farming-related runes"),
        FORAGING("§6Foraging", "§7Foraging-related runes"),
        FISHING("§bFishing", "§7Fishing-related runes");
        
        private final String displayName;
        private final String description;
        
        RuneCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum RunecraftingLocation {
        SPAWN("§aSpawn Island", "§7A peaceful runecrafting spot"),
        DEEP_CAVERNS("§7Deep Caverns", "§7A deep runecrafting spot"),
        THE_END("§5The End", "§7An end runecrafting spot");
        
        private final String displayName;
        private final String description;
        
        RunecraftingLocation(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class RuneConfig {
        private final RuneType type;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final RuneCategory category;
        private final int requiredLevel;
        private final List<String> effects;
        
        public RuneConfig(RuneType type, String displayName, String description, Material icon,
                         RuneCategory category, int requiredLevel, List<String> effects) {
            this.type = type;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.category = category;
            this.requiredLevel = requiredLevel;
            this.effects = effects;
        }
        
        public RuneType getType() { return type; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public RuneCategory getCategory() { return category; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<String> getEffects() { return effects; }
    }
    
    public static class RunecraftingConfig {
        private final RunecraftingLocation location;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<RunecraftingReward> rewards;
        
        public RunecraftingConfig(RunecraftingLocation location, String displayName, String description, Material icon,
                                int requiredLevel, List<RunecraftingReward> rewards) {
            this.location = location;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.rewards = rewards;
        }
        
        public RunecraftingLocation getLocation() { return location; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<RunecraftingReward> getRewards() { return rewards; }
    }
    
    public static class RunecraftingReward {
        private final String name;
        private final Material material;
        private final int cost;
        private final String displayName;
        
        public RunecraftingReward(String name, Material material, int cost, String displayName) {
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
    
    public static class PlayerRunecraftingData {
        private final UUID playerId;
        private int runecraftingLevel;
        private int runecraftingXP;
        private final Map<RunecraftingLocation, Integer> locationStats = new HashMap<>();
        private final Map<RuneType, Integer> runeStats = new HashMap<>();
        
        public PlayerRunecraftingData(UUID playerId) {
            this.playerId = playerId;
            this.runecraftingLevel = 1;
            this.runecraftingXP = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getRunecraftingLevel() { return runecraftingLevel; }
        public int getRunecraftingXP() { return runecraftingXP; }
        public int getLocationStats(RunecraftingLocation location) { return locationStats.getOrDefault(location, 0); }
        public int getRuneStats(RuneType type) { return runeStats.getOrDefault(type, 0); }
        
        public void addRunecraftingXP(int xp) {
            this.runecraftingXP += xp;
            checkLevelUp();
        }
        
        public void addLocationStat(RunecraftingLocation location) {
            locationStats.put(location, locationStats.getOrDefault(location, 0) + 1);
        }
        
        public void addRuneStat(RuneType type) {
            runeStats.put(type, runeStats.getOrDefault(type, 0) + 1);
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(runecraftingLevel + 1);
            if (runecraftingXP >= requiredXP) {
                runecraftingLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
}
