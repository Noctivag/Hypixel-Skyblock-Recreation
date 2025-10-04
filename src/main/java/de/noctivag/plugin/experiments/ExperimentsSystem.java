package de.noctivag.plugin.experiments;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
// import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Experiments System - Hypixel Skyblock Style
 */
public class ExperimentsSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerExperimentData> playerExperimentData = new ConcurrentHashMap<>();
    private final Map<ExperimentType, ExperimentConfig> experimentConfigs = new HashMap<>();
    
    public ExperimentsSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeExperimentConfigs();
        startExperimentUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeExperimentConfigs() {
        experimentConfigs.put(ExperimentType.ENCHANTING_EXPERIMENT, new ExperimentConfig(
            "Enchanting Experiment", "§dEnchanting Experiment", Material.ENCHANTING_TABLE,
            "§7Experiment with enchanting to gain XP and rewards.",
            ExperimentCategory.ENCHANTING, ExperimentRarity.COMMON, 1,
            Arrays.asList("§7- Gain Enchanting XP", "§7- Unlock new enchantments"),
            Arrays.asList("§7- 1x Enchanting Table", "§7- 64x Lapis Lazuli")
        ));
        
        experimentConfigs.put(ExperimentType.ALCHEMY_EXPERIMENT, new ExperimentConfig(
            "Alchemy Experiment", "§bAlchemy Experiment", Material.BREWING_STAND,
            "§7Experiment with alchemy to gain XP and rewards.",
            ExperimentCategory.ALCHEMY, ExperimentRarity.COMMON, 1,
            Arrays.asList("§7- Gain Alchemy XP", "§7- Unlock new potions"),
            Arrays.asList("§7- 1x Brewing Stand", "§7- 32x Nether Wart")
        ));
        
        experimentConfigs.put(ExperimentType.MINING_EXPERIMENT, new ExperimentConfig(
            "Mining Experiment", "§6Mining Experiment", Material.DIAMOND_PICKAXE,
            "§7Experiment with mining to gain XP and rewards.",
            ExperimentCategory.MINING, ExperimentRarity.COMMON, 1,
            Arrays.asList("§7- Gain Mining XP", "§7- Unlock new ores"),
            Arrays.asList("§7- 1x Diamond Pickaxe", "§7- 16x Iron Ore")
        ));
    }
    
    private void startExperimentUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerExperimentData> entry : playerExperimentData.entrySet()) {
                    PlayerExperimentData experimentData = entry.getValue();
                    experimentData.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 60L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Experiment")) {
            openExperimentGUI(player);
        }
    }
    
    public void openExperimentGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§d§lExperiments");
        
        addGUIItem(gui, 10, Material.ENCHANTING_TABLE, "§d§lEnchanting Experiment", "§7Experiment with enchanting.");
        addGUIItem(gui, 11, Material.BREWING_STAND, "§b§lAlchemy Experiment", "§7Experiment with alchemy.");
        addGUIItem(gui, 12, Material.DIAMOND_PICKAXE, "§6§lMining Experiment", "§7Experiment with mining.");
        
        player.openInventory(gui);
        player.sendMessage("§aExperiments GUI geöffnet!");
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
    
    public PlayerExperimentData getPlayerExperimentData(UUID playerId) {
        return playerExperimentData.computeIfAbsent(playerId, k -> new PlayerExperimentData(playerId));
    }
    
    public enum ExperimentType {
        ENCHANTING_EXPERIMENT, ALCHEMY_EXPERIMENT, MINING_EXPERIMENT,
        FARMING_EXPERIMENT, FORAGING_EXPERIMENT, FISHING_EXPERIMENT,
        COMBAT_EXPERIMENT, TAMING_EXPERIMENT
    }
    
    public enum ExperimentCategory {
        ENCHANTING("§dEnchanting", "§7Enchanting experiments"),
        ALCHEMY("§bAlchemy", "§7Alchemy experiments"),
        MINING("§6Mining", "§7Mining experiments"),
        FARMING("§aFarming", "§7Farming experiments"),
        FORAGING("§2Foraging", "§7Foraging experiments"),
        FISHING("§9Fishing", "§7Fishing experiments"),
        COMBAT("§cCombat", "§7Combat experiments"),
        TAMING("§eTaming", "§7Taming experiments");
        
        private final String displayName;
        private final String description;
        
        ExperimentCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum ExperimentRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0);
        
        private final String displayName;
        private final double multiplier;
        
        ExperimentRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class ExperimentConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final ExperimentCategory category;
        private final ExperimentRarity rarity;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public ExperimentConfig(String name, String displayName, Material icon, String description,
                              ExperimentCategory category, ExperimentRarity rarity, int maxLevel,
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
        public ExperimentCategory getCategory() { return category; }
        public ExperimentRarity getRarity() { return rarity; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerExperimentData {
        private final UUID playerId;
        private final Map<ExperimentType, Integer> experimentLevels = new HashMap<>();
        private final Map<ExperimentType, Boolean> unlockedExperiments = new HashMap<>();
        private long lastUpdate;
        
        public PlayerExperimentData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addExperiment(ExperimentType type) {
            experimentLevels.put(type, experimentLevels.getOrDefault(type, 0) + 1);
            unlockedExperiments.put(type, true);
        }
        
        public boolean hasExperiment(ExperimentType type) {
            return unlockedExperiments.getOrDefault(type, false);
        }
        
        public int getExperimentLevel(ExperimentType type) {
            return experimentLevels.getOrDefault(type, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
