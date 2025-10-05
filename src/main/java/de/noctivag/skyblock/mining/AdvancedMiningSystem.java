package de.noctivag.skyblock.mining;

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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Advanced Mining System - Hypixel Skyblock Style
 * Implements Crystal Hollows, Gemstone Mining, Mining Islands, and Mining Commissions
 */
public class AdvancedMiningSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerMiningData> playerMiningData = new ConcurrentHashMap<>();
    private final Map<MiningLocation, MiningConfig> miningConfigs = new HashMap<>();
    private final Map<GemstoneType, GemstoneConfig> gemstoneConfigs = new HashMap<>();
    
    public AdvancedMiningSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        initializeMiningConfigs();
        initializeGemstoneConfigs();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeMiningConfigs() {
        // Spawn Island
        miningConfigs.put(MiningLocation.SPAWN, new MiningConfig(
            MiningLocation.SPAWN,
            "§aSpawn Island",
            "§7A peaceful mining spot",
            Material.COBBLESTONE,
            1,
            Arrays.asList(
                new MiningReward("Cobblestone", Material.COBBLESTONE, 100, "§fCobblestone"),
                new MiningReward("Coal", Material.COAL, 150, "§8Coal"),
                new MiningReward("Iron", Material.IRON_INGOT, 200, "§fIron")
            )
        ));
        
        // Deep Caverns
        miningConfigs.put(MiningLocation.DEEP_CAVERNS, new MiningConfig(
            MiningLocation.DEEP_CAVERNS,
            "§7Deep Caverns",
            "§7A deep mining spot",
            Material.STONE,
            2,
            Arrays.asList(
                new MiningReward("Cobblestone", Material.COBBLESTONE, 200, "§fCobblestone"),
                new MiningReward("Coal", Material.COAL, 300, "§8Coal"),
                new MiningReward("Iron", Material.IRON_INGOT, 400, "§fIron"),
                new MiningReward("Gold", Material.GOLD_INGOT, 500, "§6Gold")
            )
        ));
        
        // Dwarven Mines
        miningConfigs.put(MiningLocation.DWARVEN_MINES, new MiningConfig(
            MiningLocation.DWARVEN_MINES,
            "§6Dwarven Mines",
            "§7A dwarven mining spot",
            Material.STONE,
            3,
            Arrays.asList(
                new MiningReward("Cobblestone", Material.COBBLESTONE, 300, "§fCobblestone"),
                new MiningReward("Coal", Material.COAL, 450, "§8Coal"),
                new MiningReward("Iron", Material.IRON_INGOT, 600, "§fIron"),
                new MiningReward("Gold", Material.GOLD_INGOT, 750, "§6Gold"),
                new MiningReward("Diamond", Material.DIAMOND, 1000, "§bDiamond")
            )
        ));
        
        // Crystal Hollows
        miningConfigs.put(MiningLocation.CRYSTAL_HOLLOWS, new MiningConfig(
            MiningLocation.CRYSTAL_HOLLOWS,
            "§dCrystal Hollows",
            "§7A crystal mining spot",
            Material.AMETHYST_BLOCK,
            4,
            Arrays.asList(
                new MiningReward("Cobblestone", Material.COBBLESTONE, 400, "§fCobblestone"),
                new MiningReward("Coal", Material.COAL, 600, "§8Coal"),
                new MiningReward("Iron", Material.IRON_INGOT, 800, "§fIron"),
                new MiningReward("Gold", Material.GOLD_INGOT, 1000, "§6Gold"),
                new MiningReward("Diamond", Material.DIAMOND, 1500, "§bDiamond"),
                new MiningReward("Emerald", Material.EMERALD, 2000, "§aEmerald")
            )
        ));
        
        // The End
        miningConfigs.put(MiningLocation.THE_END, new MiningConfig(
            MiningLocation.THE_END,
            "§5The End",
            "§7An end mining spot",
            Material.END_STONE,
            5,
            Arrays.asList(
                new MiningReward("End Stone", Material.END_STONE, 500, "§fEnd Stone"),
                new MiningReward("Ender Pearl", Material.ENDER_PEARL, 750, "§5Ender Pearl"),
                new MiningReward("Obsidian", Material.OBSIDIAN, 1000, "§5Obsidian")
            )
        ));
    }
    
    private void initializeGemstoneConfigs() {
        // Ruby
        gemstoneConfigs.put(GemstoneType.RUBY, new GemstoneConfig(
            GemstoneType.RUBY,
            "§cRuby",
            "§7A red gemstone",
            Material.REDSTONE,
            1,
            Arrays.asList("Ruby", "Ruby Shard", "Ruby Crystal")
        ));
        
        // Amethyst
        gemstoneConfigs.put(GemstoneType.AMETHYST, new GemstoneConfig(
            GemstoneType.AMETHYST,
            "§dAmethyst",
            "§7A purple gemstone",
            Material.AMETHYST_SHARD,
            2,
            Arrays.asList("Amethyst", "Amethyst Shard", "Amethyst Crystal")
        ));
        
        // Sapphire
        gemstoneConfigs.put(GemstoneType.SAPPHIRE, new GemstoneConfig(
            GemstoneType.SAPPHIRE,
            "§bSapphire",
            "§7A blue gemstone",
            Material.LAPIS_LAZULI,
            3,
            Arrays.asList("Sapphire", "Sapphire Shard", "Sapphire Crystal")
        ));
        
        // Jade
        gemstoneConfigs.put(GemstoneType.JADE, new GemstoneConfig(
            GemstoneType.JADE,
            "§aJade",
            "§7A green gemstone",
            Material.EMERALD,
            4,
            Arrays.asList("Jade", "Jade Shard", "Jade Crystal")
        ));
        
        // Amber
        gemstoneConfigs.put(GemstoneType.AMBER, new GemstoneConfig(
            GemstoneType.AMBER,
            "§6Amber",
            "§7A yellow gemstone",
            Material.GOLD_INGOT,
            5,
            Arrays.asList("Amber", "Amber Shard", "Amber Crystal")
        ));
        
        // Topaz
        gemstoneConfigs.put(GemstoneType.TOPAZ, new GemstoneConfig(
            GemstoneType.TOPAZ,
            "§eTopaz",
            "§7A yellow gemstone",
            Material.GOLD_INGOT,
            6,
            Arrays.asList("Topaz", "Topaz Shard", "Topaz Crystal")
        ));
        
        // Jasper
        gemstoneConfigs.put(GemstoneType.JASPER, new GemstoneConfig(
            GemstoneType.JASPER,
            "§cJasper",
            "§7A red gemstone",
            Material.REDSTONE,
            7,
            Arrays.asList("Jasper", "Jasper Shard", "Jasper Crystal")
        ));
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        
        if (isMiningBlock(blockType)) {
            PlayerMiningData data = getPlayerMiningData(player.getUniqueId());
            MiningLocation location = getMiningLocation(player);
            
            if (location != null) {
                int xp = calculateMiningXP(player, data, location);
                data.addMiningXP(xp);
                
                // Check for gemstone spawn
                if (shouldSpawnGemstone(data)) {
                    spawnGemstone(player, location);
                }
                
                player.sendMessage("§aMined " + blockType.name() + "! +" + xp + " Mining XP");
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());
        
        if (displayName.contains("Mining") || displayName.contains("mining")) {
            openMiningGUI(player);
        }
    }
    
    public void openMiningGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§7§lMining System"));
        
        // Add mining locations
        addGUIItem(gui, 10, Material.COBBLESTONE, "§a§lSpawn Island", "§7A peaceful mining spot");
        addGUIItem(gui, 11, Material.STONE, "§7§lDeep Caverns", "§7A deep mining spot");
        addGUIItem(gui, 12, Material.STONE, "§6§lDwarven Mines", "§7A dwarven mining spot");
        addGUIItem(gui, 13, Material.AMETHYST_BLOCK, "§d§lCrystal Hollows", "§7A crystal mining spot");
        addGUIItem(gui, 14, Material.END_STONE, "§5§lThe End", "§7An end mining spot");
        
        // Add mining management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Mining Progress", "§7View your mining progress.");
        addGUIItem(gui, 19, Material.DIAMOND_PICKAXE, "§c§lMining Tools", "§7View available mining tools.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lMining Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lMining Shop", "§7Buy mining items.");
        addGUIItem(gui, 22, Material.GOLD_INGOT, "§6§lMining Commissions", "§7Join mining commissions.");
        
        // Add gemstones
        addGUIItem(gui, 27, Material.REDSTONE, "§c§lRuby", "§7A red gemstone");
        addGUIItem(gui, 28, Material.AMETHYST_SHARD, "§d§lAmethyst", "§7A purple gemstone");
        addGUIItem(gui, 29, Material.LAPIS_LAZULI, "§b§lSapphire", "§7A blue gemstone");
        addGUIItem(gui, 30, Material.EMERALD, "§a§lJade", "§7A green gemstone");
        addGUIItem(gui, 31, Material.GOLD_INGOT, "§6§lAmber", "§7A yellow gemstone");
        addGUIItem(gui, 32, Material.GOLD_INGOT, "§e§lTopaz", "§7A yellow gemstone");
        addGUIItem(gui, 33, Material.REDSTONE, "§c§lJasper", "§7A red gemstone");
        
        // Add special items
        addGUIItem(gui, 36, Material.DIAMOND_PICKAXE, "§6§lDiamond Pickaxe", "§7A diamond pickaxe");
        addGUIItem(gui, 37, Material.GOLDEN_PICKAXE, "§e§lGolden Pickaxe", "§7A golden pickaxe");
        addGUIItem(gui, 38, Material.IRON_PICKAXE, "§7§lIron Pickaxe", "§7An iron pickaxe");
        addGUIItem(gui, 39, Material.STONE_PICKAXE, "§8§lStone Pickaxe", "§7A stone pickaxe");
        addGUIItem(gui, 40, Material.WOODEN_PICKAXE, "§6§lWooden Pickaxe", "§7A wooden pickaxe");
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the mining menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aMining GUI opened!"));
    }
    
    private boolean isMiningBlock(Material material) {
        return material == Material.COBBLESTONE || material == Material.STONE || 
               material == Material.COAL_ORE || material == Material.IRON_ORE || 
               material == Material.GOLD_ORE || material == Material.DIAMOND_ORE || 
               material == Material.EMERALD_ORE || material == Material.END_STONE || 
               material == Material.OBSIDIAN;
    }
    
    private MiningLocation getMiningLocation(Player player) {
        // This would determine the mining location based on player's world/position
        return MiningLocation.SPAWN; // Default for now
    }
    
    private int calculateMiningXP(Player player, PlayerMiningData data, MiningLocation location) {
        int baseXP = 10;
        int level = data.getMiningLevel();
        int xpMultiplier = 1 + (level / 10);
        int locationMultiplier = location.getRequiredLevel();
        
        return baseXP * xpMultiplier * locationMultiplier;
    }
    
    private boolean shouldSpawnGemstone(PlayerMiningData data) {
        int level = data.getMiningLevel();
        double chance = 0.01 + (level * 0.001); // 1% base chance + 0.1% per level
        
        return Math.random() < chance;
    }
    
    private void spawnGemstone(Player player, MiningLocation location) {
        // Get random gemstone based on mining level
        PlayerMiningData data = getPlayerMiningData(player.getUniqueId());
        int level = data.getMiningLevel();
        
        GemstoneType gemstoneType = getRandomGemstone(level);
        GemstoneConfig config = gemstoneConfigs.get(gemstoneType);
        
        if (config != null) {
            // Spawn gemstone
            // This would spawn the actual gemstone entity
            
            player.sendMessage("§dA " + config.getDisplayName() + " has appeared!");
        }
    }
    
    private GemstoneType getRandomGemstone(int miningLevel) {
        List<GemstoneType> availableGemstones = new ArrayList<>();
        
        for (Map.Entry<GemstoneType, GemstoneConfig> entry : gemstoneConfigs.entrySet()) {
            if (entry.getValue().getRequiredLevel() <= miningLevel) {
                availableGemstones.add(entry.getKey());
            }
        }
        
        if (availableGemstones.isEmpty()) {
            return GemstoneType.RUBY; // Default
        }
        
        return availableGemstones.get((int) (Math.random() * availableGemstones.size()));
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
    
    public PlayerMiningData getPlayerMiningData(UUID playerId) {
        return playerMiningData.computeIfAbsent(playerId, k -> new PlayerMiningData(playerId));
    }
    
    public enum MiningLocation {
        SPAWN("§aSpawn Island", "§7A peaceful mining spot", 1),
        DEEP_CAVERNS("§7Deep Caverns", "§7A deep mining spot", 2),
        DWARVEN_MINES("§6Dwarven Mines", "§7A dwarven mining spot", 3),
        CRYSTAL_HOLLOWS("§dCrystal Hollows", "§7A crystal mining spot", 4),
        THE_END("§5The End", "§7An end mining spot", 5);
        
        private final String displayName;
        private final String description;
        private final int requiredLevel;
        
        MiningLocation(String displayName, String description, int requiredLevel) {
            this.displayName = displayName;
            this.description = description;
            this.requiredLevel = requiredLevel;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public int getRequiredLevel() { return requiredLevel; }
    }
    
    public enum GemstoneType {
        RUBY("§cRuby", "§7A red gemstone"),
        AMETHYST("§dAmethyst", "§7A purple gemstone"),
        SAPPHIRE("§bSapphire", "§7A blue gemstone"),
        JADE("§aJade", "§7A green gemstone"),
        AMBER("§6Amber", "§7A yellow gemstone"),
        TOPAZ("§eTopaz", "§7A yellow gemstone"),
        JASPER("§cJasper", "§7A red gemstone");
        
        private final String displayName;
        private final String description;
        
        GemstoneType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class MiningConfig {
        private final MiningLocation location;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<MiningReward> rewards;
        
        public MiningConfig(MiningLocation location, String displayName, String description, Material icon,
                          int requiredLevel, List<MiningReward> rewards) {
            this.location = location;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.rewards = rewards;
        }
        
        public MiningLocation getLocation() { return location; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<MiningReward> getRewards() { return rewards; }
    }
    
    public static class MiningReward {
        private final String name;
        private final Material material;
        private final int cost;
        private final String displayName;
        
        public MiningReward(String name, Material material, int cost, String displayName) {
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
    
    public static class GemstoneConfig {
        private final GemstoneType type;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<String> drops;
        
        public GemstoneConfig(GemstoneType type, String displayName, String description, Material icon,
                            int requiredLevel, List<String> drops) {
            this.type = type;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.drops = drops;
        }
        
        public GemstoneType getType() { return type; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<String> getDrops() { return drops; }
    }
    
    public static class PlayerMiningData {
        private final UUID playerId;
        private int miningLevel;
        private int miningXP;
        private final Map<MiningLocation, Integer> locationStats = new HashMap<>();
        private final Map<GemstoneType, Integer> gemstoneStats = new HashMap<>();
        
        public PlayerMiningData(UUID playerId) {
            this.playerId = playerId;
            this.miningLevel = 1;
            this.miningXP = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getMiningLevel() { return miningLevel; }
        public int getMiningXP() { return miningXP; }
        public int getLocationStats(MiningLocation location) { return locationStats.getOrDefault(location, 0); }
        public int getGemstoneStats(GemstoneType type) { return gemstoneStats.getOrDefault(type, 0); }
        
        public void addMiningXP(int xp) {
            this.miningXP += xp;
            checkLevelUp();
        }
        
        public void addLocationStat(MiningLocation location) {
            locationStats.put(location, locationStats.getOrDefault(location, 0) + 1);
        }
        
        public void addGemstoneStat(GemstoneType type) {
            gemstoneStats.put(type, gemstoneStats.getOrDefault(type, 0) + 1);
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(miningLevel + 1);
            if (miningXP >= requiredXP) {
                miningLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
}
