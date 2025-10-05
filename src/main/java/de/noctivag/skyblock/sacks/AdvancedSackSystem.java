package de.noctivag.skyblock.sacks;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AdvancedSackSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerSackData> playerSackData = new ConcurrentHashMap<>();
    private final Map<SackType, SackConfig> sackConfigs = new HashMap<>();
    
    public AdvancedSackSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        initializeSackConfigs();
        startSackUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeSackConfigs() {
        // Farming Sacks
        sackConfigs.put(SackType.WHEAT_SACK, new SackConfig(
            "Wheat Sack", "§eWheat Sack", Material.WHEAT,
            "§7Stores large amounts of wheat.",
            SackCategory.FARMING, SackRarity.COMMON, 1000, Arrays.asList("§7- Stores Wheat", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Wheat Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.CARROT_SACK, new SackConfig(
            "Carrot Sack", "§6Carrot Sack", Material.CARROT,
            "§7Stores large amounts of carrots.",
            SackCategory.FARMING, SackRarity.COMMON, 1000, Arrays.asList("§7- Stores Carrots", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Carrot Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.POTATO_SACK, new SackConfig(
            "Potato Sack", "§fPotato Sack", Material.POTATO,
            "§7Stores large amounts of potatoes.",
            SackCategory.FARMING, SackRarity.COMMON, 1000, Arrays.asList("§7- Stores Potatoes", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Potato Sack", "§7- 1x String")
        ));
        
        // Mining Sacks
        sackConfigs.put(SackType.COBBLESTONE_SACK, new SackConfig(
            "Cobblestone Sack", "§7Cobblestone Sack", Material.COBBLESTONE,
            "§7Stores large amounts of cobblestone.",
            SackCategory.MINING, SackRarity.COMMON, 2000, Arrays.asList("§7- Stores Cobblestone", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Cobblestone Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.COAL_SACK, new SackConfig(
            "Coal Sack", "§8Coal Sack", Material.COAL,
            "§7Stores large amounts of coal.",
            SackCategory.MINING, SackRarity.UNCOMMON, 1500, Arrays.asList("§7- Stores Coal", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Coal Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.IRON_SACK, new SackConfig(
            "Iron Sack", "§fIron Sack", Material.IRON_INGOT,
            "§7Stores large amounts of iron.",
            SackCategory.MINING, SackRarity.RARE, 1000, Arrays.asList("§7- Stores Iron", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Iron Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.GOLD_SACK, new SackConfig(
            "Gold Sack", "§6Gold Sack", Material.GOLD_INGOT,
            "§7Stores large amounts of gold.",
            SackCategory.MINING, SackRarity.RARE, 800, Arrays.asList("§7- Stores Gold", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Gold Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.DIAMOND_SACK, new SackConfig(
            "Diamond Sack", "§bDiamond Sack", Material.DIAMOND,
            "§7Stores large amounts of diamonds.",
            SackCategory.MINING, SackRarity.EPIC, 500, Arrays.asList("§7- Stores Diamonds", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Diamond Sack", "§7- 1x String")
        ));
        
        // Combat Sacks
        sackConfigs.put(SackType.ROTTEN_FLESH_SACK, new SackConfig(
            "Rotten Flesh Sack", "§2Rotten Flesh Sack", Material.ROTTEN_FLESH,
            "§7Stores large amounts of rotten flesh.",
            SackCategory.COMBAT, SackRarity.COMMON, 2000, Arrays.asList("§7- Stores Rotten Flesh", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Rotten Flesh Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.BONE_SACK, new SackConfig(
            "Bone Sack", "§fBone Sack", Material.BONE,
            "§7Stores large amounts of bones.",
            SackCategory.COMBAT, SackRarity.COMMON, 1500, Arrays.asList("§7- Stores Bones", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Bone Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.STRING_SACK, new SackConfig(
            "String Sack", "§fString Sack", Material.STRING,
            "§7Stores large amounts of string.",
            SackCategory.COMBAT, SackRarity.COMMON, 1000, Arrays.asList("§7- Stores String", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x String Sack", "§7- 1x String")
        ));
        
        // Foraging Sacks
        sackConfigs.put(SackType.OAK_LOG_SACK, new SackConfig(
            "Oak Log Sack", "§6Oak Log Sack", Material.OAK_LOG,
            "§7Stores large amounts of oak logs.",
            SackCategory.FORAGING, SackRarity.COMMON, 2000, Arrays.asList("§7- Stores Oak Logs", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Oak Log Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.BIRCH_LOG_SACK, new SackConfig(
            "Birch Log Sack", "§fBirch Log Sack", Material.BIRCH_LOG,
            "§7Stores large amounts of birch logs.",
            SackCategory.FORAGING, SackRarity.COMMON, 2000, Arrays.asList("§7- Stores Birch Logs", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Birch Log Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.SPRUCE_LOG_SACK, new SackConfig(
            "Spruce Log Sack", "§2Spruce Log Sack", Material.SPRUCE_LOG,
            "§7Stores large amounts of spruce logs.",
            SackCategory.FORAGING, SackRarity.COMMON, 2000, Arrays.asList("§7- Stores Spruce Logs", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Spruce Log Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.JUNGLE_LOG_SACK, new SackConfig(
            "Jungle Log Sack", "§aJungle Log Sack", Material.JUNGLE_LOG,
            "§7Stores large amounts of jungle logs.",
            SackCategory.FORAGING, SackRarity.UNCOMMON, 1500, Arrays.asList("§7- Stores Jungle Logs", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Jungle Log Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.ACACIA_LOG_SACK, new SackConfig(
            "Acacia Log Sack", "§6Acacia Log Sack", Material.ACACIA_LOG,
            "§7Stores large amounts of acacia logs.",
            SackCategory.FORAGING, SackRarity.UNCOMMON, 1500, Arrays.asList("§7- Stores Acacia Logs", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Acacia Log Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.DARK_OAK_LOG_SACK, new SackConfig(
            "Dark Oak Log Sack", "§8Dark Oak Log Sack", Material.DARK_OAK_LOG,
            "§7Stores large amounts of dark oak logs.",
            SackCategory.FORAGING, SackRarity.UNCOMMON, 1500, Arrays.asList("§7- Stores Dark Oak Logs", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Dark Oak Log Sack", "§7- 1x String")
        ));
        
        // Fishing Sacks
        sackConfigs.put(SackType.FISH_SACK, new SackConfig(
            "Fish Sack", "§bFish Sack", Material.COD,
            "§7Stores large amounts of fish.",
            SackCategory.FISHING, SackRarity.COMMON, 1000, Arrays.asList("§7- Stores Fish", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Fish Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.SALMON_SACK, new SackConfig(
            "Salmon Sack", "§cSalmon Sack", Material.SALMON,
            "§7Stores large amounts of salmon.",
            SackCategory.FISHING, SackRarity.UNCOMMON, 800, Arrays.asList("§7- Stores Salmon", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Salmon Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.TROPICAL_FISH_SACK, new SackConfig(
            "Tropical Fish Sack", "§dTropical Fish Sack", Material.TROPICAL_FISH,
            "§7Stores large amounts of tropical fish.",
            SackCategory.FISHING, SackRarity.RARE, 500, Arrays.asList("§7- Stores Tropical Fish", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Tropical Fish Sack", "§7- 1x String")
        ));
        
        sackConfigs.put(SackType.PUFFERFISH_SACK, new SackConfig(
            "Pufferfish Sack", "§ePufferfish Sack", Material.PUFFERFISH,
            "§7Stores large amounts of pufferfish.",
            SackCategory.FISHING, SackRarity.RARE, 500, Arrays.asList("§7- Stores Pufferfish", "§7- Auto-Collection"),
            Arrays.asList("§7- 1x Pufferfish Sack", "§7- 1x String")
        ));
    }
    
    private void startSackUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateAllPlayerSackData();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L); // Update every minute
    }
    
    private void updateAllPlayerSackData() {
        for (PlayerSackData data : playerSackData.values()) {
            data.update();
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Sack")) {
            openSackGUI(player);
        }
    }
    
    public void openSackGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§e§lSack System"));
        
        // Add sack categories
        addGUIItem(gui, 10, Material.WHEAT, "§e§lFarming Sacks", "§7View farming sacks.");
        addGUIItem(gui, 11, Material.COBBLESTONE, "§7§lMining Sacks", "§7View mining sacks.");
        addGUIItem(gui, 12, Material.ROTTEN_FLESH, "§2§lCombat Sacks", "§7View combat sacks.");
        addGUIItem(gui, 13, Material.OAK_LOG, "§6§lForaging Sacks", "§7View foraging sacks.");
        addGUIItem(gui, 14, Material.COD, "§b§lFishing Sacks", "§7View fishing sacks.");
        
        // Add specific sacks
        addGUIItem(gui, 19, Material.WHEAT, "§e§lWheat Sack", "§7Stores wheat.");
        addGUIItem(gui, 20, Material.CARROT, "§6§lCarrot Sack", "§7Stores carrots.");
        addGUIItem(gui, 21, Material.POTATO, "§f§lPotato Sack", "§7Stores potatoes.");
        addGUIItem(gui, 22, Material.COBBLESTONE, "§7§lCobblestone Sack", "§7Stores cobblestone.");
        addGUIItem(gui, 23, Material.COAL, "§8§lCoal Sack", "§7Stores coal.");
        addGUIItem(gui, 24, Material.IRON_INGOT, "§f§lIron Sack", "§7Stores iron.");
        addGUIItem(gui, 25, Material.GOLD_INGOT, "§6§lGold Sack", "§7Stores gold.");
        addGUIItem(gui, 26, Material.DIAMOND, "§b§lDiamond Sack", "§7Stores diamonds.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the GUI.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aSack GUI geöffnet!"));
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(description).stream()
                .map(desc -> Component.text(desc))
                .collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerSackData getPlayerSackData(UUID playerId) {
        return playerSackData.computeIfAbsent(playerId, k -> new PlayerSackData(playerId));
    }
    
    public SackConfig getSackConfig(SackType type) {
        return sackConfigs.get(type);
    }
    
    public List<SackType> getAllSackTypes() {
        return new ArrayList<>(sackConfigs.keySet());
    }
    
    public enum SackType {
        // Farming
        WHEAT_SACK, CARROT_SACK, POTATO_SACK,
        // Mining
        COBBLESTONE_SACK, COAL_SACK, IRON_SACK, GOLD_SACK, DIAMOND_SACK,
        // Combat
        ROTTEN_FLESH_SACK, BONE_SACK, STRING_SACK,
        // Foraging
        OAK_LOG_SACK, BIRCH_LOG_SACK, SPRUCE_LOG_SACK, JUNGLE_LOG_SACK, ACACIA_LOG_SACK, DARK_OAK_LOG_SACK,
        // Fishing
        FISH_SACK, SALMON_SACK, TROPICAL_FISH_SACK, PUFFERFISH_SACK
    }
    
    public enum SackCategory {
        FARMING("§eFarming", "§7Farming-related sacks"),
        MINING("§7Mining", "§7Mining-related sacks"),
        COMBAT("§2Combat", "§7Combat-related sacks"),
        FORAGING("§6Foraging", "§7Foraging-related sacks"),
        FISHING("§bFishing", "§7Fishing-related sacks");
        
        private final String displayName;
        private final String description;
        
        SackCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum SackRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        SackRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class SackConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final SackCategory category;
        private final SackRarity rarity;
        private final int capacity;
        private final List<String> features;
        private final List<String> requirements;
        
        public SackConfig(String name, String displayName, Material material, String description,
                         SackCategory category, SackRarity rarity, int capacity,
                         List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.capacity = capacity;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public SackCategory getCategory() { return category; }
        public SackRarity getRarity() { return rarity; }
        public int getCapacity() { return capacity; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerSackData {
        private final UUID playerId;
        private final Map<SackType, Integer> sackContents = new HashMap<>();
        private final Map<SackType, Integer> sackLevels = new HashMap<>();
        private long lastUpdate;
        
        public PlayerSackData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void addSackContent(SackType type, int amount) {
            sackContents.put(type, sackContents.getOrDefault(type, 0) + amount);
        }
        
        public void levelUpSack(SackType type) {
            sackLevels.put(type, sackLevels.getOrDefault(type, 0) + 1);
        }
        
        public int getSackContent(SackType type) {
            return sackContents.getOrDefault(type, 0);
        }
        
        public int getSackLevel(SackType type) {
            return sackLevels.getOrDefault(type, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
