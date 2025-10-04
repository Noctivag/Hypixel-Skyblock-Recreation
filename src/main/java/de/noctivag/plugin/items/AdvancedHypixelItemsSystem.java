package de.noctivag.plugin.items;
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
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Hypixel Skyblock Items System - More Specialized Items
 */
public class AdvancedHypixelItemsSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerAdvancedItemData> playerAdvancedItemData = new ConcurrentHashMap<>();
    private final Map<AdvancedItemCategory, List<AdvancedHypixelItem>> advancedItemsByCategory = new HashMap<>();
    private final Map<UUID, BukkitTask> advancedItemTasks = new ConcurrentHashMap<>();
    
    public AdvancedHypixelItemsSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeAdvancedItems();
        startAdvancedItemUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeAdvancedItems() {
        // DUNGEON ITEMS
        advancedItemsByCategory.computeIfAbsent(AdvancedItemCategory.DUNGEON, k -> new ArrayList<>()).addAll(Arrays.asList(
            new AdvancedHypixelItem("BONZO_STAFF", "Bonzo's Staff", "§a§lBonzo's Staff", Material.STICK,
                AdvancedItemRarity.RARE, AdvancedItemType.DUNGEON_WEAPON, "§7A staff that summons balloons to attack enemies.",
                Arrays.asList("§7Damage: §c+120", "§7Intelligence: §a+100", "§7Mana: §b+50"),
                Arrays.asList("§7Ability: Balloon Barrage", "§7Mana Cost: §b80", "§7Summons balloons to attack"),
                Arrays.asList("§7- 1x Bonzo's Staff", "§7- 1x Enchanted Stick", "§7- 1x Enchanted String")),
                
            new AdvancedHypixelItem("SCARF_STUDIES", "Scarf's Studies", "§9§lScarf's Studies", Material.BOOK,
                AdvancedItemRarity.RARE, AdvancedItemType.DUNGEON_WEAPON, "§7A book that provides intelligence and mana.",
                Arrays.asList("§7Intelligence: §a+150", "§7Mana: §b+100", "§7Health: §a+50"),
                Arrays.asList("§7Ability: Knowledge Boost", "§7+25% intelligence"),
                Arrays.asList("§7- 1x Scarf's Studies", "§7- 1x Enchanted Book", "§7- 1x Enchanted Paper")),
                
            new AdvancedHypixelItem("THORN_LEGGINGS", "Thorn's Leggings", "§2§lThorn's Leggings", Material.LEATHER_LEGGINGS,
                AdvancedItemRarity.RARE, AdvancedItemType.DUNGEON_ARMOR, "§7Leggings that provide thorns damage.",
                Arrays.asList("§7Health: §a+100", "§7Defense: §a+80", "§7Thorns: §c+25"),
                Arrays.asList("§7Ability: Thorn Damage", "§7+25% thorns damage"),
                Arrays.asList("§7- 1x Thorn's Leggings", "§7- 1x Enchanted Leather", "§7- 1x Enchanted String")),
                
            new AdvancedHypixelItem("LIVID_DAGGER", "Livid Dagger", "§5§lLivid Dagger", Material.IRON_SWORD,
                AdvancedItemRarity.EPIC, AdvancedItemType.DUNGEON_WEAPON, "§7A dagger that provides critical damage.",
                Arrays.asList("§7Damage: §c+180", "§7Strength: §c+100", "§7Crit Damage: §c+100%"),
                Arrays.asList("§7Ability: Backstab", "§7+50% damage from behind"),
                Arrays.asList("§7- 1x Livid Dagger", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Diamond")),
                
            new AdvancedHypixelItem("SHADOW_FURY", "Shadow Fury", "§8§lShadow Fury", Material.DIAMOND_SWORD,
                AdvancedItemRarity.LEGENDARY, AdvancedItemType.DUNGEON_WEAPON, "§7A sword that teleports and damages enemies.",
                Arrays.asList("§7Damage: §c+250", "§7Strength: §c+150", "§7Intelligence: §a+100"),
                Arrays.asList("§7Ability: Shadow Teleport", "§7Mana Cost: §b150", "§7Teleports and damages"),
                Arrays.asList("§7- 1x Shadow Fury", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Ender Pearl"))
        ));
        
        // SLAYER ITEMS
        advancedItemsByCategory.computeIfAbsent(AdvancedItemCategory.SLAYER, k -> new ArrayList<>()).addAll(Arrays.asList(
            new AdvancedHypixelItem("REVENANT_FALCHION", "Revenant Falchion", "§2§lRevenant Falchion", Material.IRON_SWORD,
                AdvancedItemRarity.RARE, AdvancedItemType.SLAYER_WEAPON, "§7A sword that deals extra damage to zombies.",
                Arrays.asList("§7Damage: §c+150", "§7Strength: §c+100", "§7Zombie Damage: §c+50%"),
                Arrays.asList("§7Ability: Zombie Slayer", "§7+50% damage to zombies"),
                Arrays.asList("§7- 1x Revenant Falchion", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Rotten Flesh")),
                
            new AdvancedHypixelItem("TARANTULA_HELMET", "Tarantula Helmet", "§8§lTarantula Helmet", Material.LEATHER_HELMET,
                AdvancedItemRarity.RARE, AdvancedItemType.SLAYER_ARMOR, "§7A helmet that provides spider damage.",
                Arrays.asList("§7Health: §a+80", "§7Defense: §a+60", "§7Spider Damage: §c+40%"),
                Arrays.asList("§7Ability: Spider Slayer", "§7+40% damage to spiders"),
                Arrays.asList("§7- 1x Tarantula Helmet", "§7- 1x Enchanted Leather", "§7- 1x Enchanted String")),
                
            new AdvancedHypixelItem("SCORPION_FOIL", "Scorpion Foil", "§6§lScorpion Foil", Material.IRON_SWORD,
                AdvancedItemRarity.EPIC, AdvancedItemType.SLAYER_WEAPON, "§7A sword that deals extra damage to spiders.",
                Arrays.asList("§7Damage: §c+200", "§7Strength: §c+120", "§7Spider Damage: §c+60%"),
                Arrays.asList("§7Ability: Spider Slayer", "§7+60% damage to spiders"),
                Arrays.asList("§7- 1x Scorpion Foil", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Spider Eye")),
                
            new AdvancedHypixelItem("VOIDEDGE_KATANA", "Voidedge Katana", "§5§lVoidedge Katana", Material.DIAMOND_SWORD,
                AdvancedItemRarity.EPIC, AdvancedItemType.SLAYER_WEAPON, "§7A katana that deals extra damage to endermen.",
                Arrays.asList("§7Damage: §c+220", "§7Strength: §c+130", "§7Enderman Damage: §c+70%"),
                Arrays.asList("§7Ability: Enderman Slayer", "§7+70% damage to endermen"),
                Arrays.asList("§7- 1x Voidedge Katana", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Ender Pearl")),
                
            new AdvancedHypixelItem("ATOMSPLIT_KATANA", "Atomsplit Katana", "§d§lAtomsplit Katana", Material.DIAMOND_SWORD,
                AdvancedItemRarity.LEGENDARY, AdvancedItemType.SLAYER_WEAPON, "§7The ultimate katana for enderman slaying.",
                Arrays.asList("§7Damage: §c+300", "§7Strength: §c+180", "§7Enderman Damage: §c+100%"),
                Arrays.asList("§7Ability: Enderman Slayer", "§7+100% damage to endermen"),
                Arrays.asList("§7- 1x Atomsplit Katana", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Ender Pearl"))
        ));
        
        // FISHING ITEMS
        advancedItemsByCategory.computeIfAbsent(AdvancedItemCategory.FISHING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new AdvancedHypixelItem("ROD_OF_LEGENDS", "Rod of Legends", "§6§lRod of Legends", Material.FISHING_ROD,
                AdvancedItemRarity.LEGENDARY, AdvancedItemType.FISHING_ROD, "§7A legendary fishing rod with special abilities.",
                Arrays.asList("§7Fishing Speed: §a+100", "§7Sea Creature Chance: §a+25%", "§7Fishing XP: §a+50%"),
                Arrays.asList("§7Ability: Legendary Fishing", "§7+25% sea creature chance"),
                Arrays.asList("§7- 1x Rod of Legends", "§7- 1x Enchanted String", "§7- 1x Enchanted Gold")),
                
            new AdvancedHypixelItem("SHARK_SCALE_ARMOR", "Shark Scale Armor", "§b§lShark Scale Armor", Material.LEATHER_CHESTPLATE,
                AdvancedItemRarity.EPIC, AdvancedItemType.FISHING_ARMOR, "§7Armor made from shark scales.",
                Arrays.asList("§7Health: §a+150", "§7Defense: §a+120", "§7Fishing Speed: §a+30%"),
                Arrays.asList("§7Ability: Shark's Fury", "§7+30% fishing speed"),
                Arrays.asList("§7- 1x Shark Scale Armor", "§7- 1x Enchanted Leather", "§7- 1x Enchanted Prismarine")),
                
            new AdvancedHypixelItem("FISHING_HOOK", "Fishing Hook", "§7§lFishing Hook", Material.TRIPWIRE_HOOK,
                AdvancedItemRarity.UNCOMMON, AdvancedItemType.FISHING_ACCESSORY, "§7A hook that improves fishing.",
                Arrays.asList("§7Fishing Speed: §a+20", "§7Sea Creature Chance: §a+10%"),
                Arrays.asList("§7Ability: Improved Fishing", "§7+10% sea creature chance"),
                Arrays.asList("§7- 1x Fishing Hook", "§7- 1x Enchanted Iron", "§7- 1x Enchanted String")),
                
            new AdvancedHypixelItem("WHALE_BAIT", "Whale Bait", "§b§lWhale Bait", Material.SALMON,
                AdvancedItemRarity.RARE, AdvancedItemType.FISHING_BAIT, "§7Bait that attracts sea creatures.",
                Arrays.asList("§7Sea Creature Chance: §a+15%", "§7Fishing XP: §a+25%"),
                Arrays.asList("§7Ability: Sea Creature Attraction", "§7+15% sea creature chance"),
                Arrays.asList("§7- 1x Whale Bait", "§7- 1x Enchanted Salmon", "§7- 1x Enchanted String"))
        ));
        
        // MINING ITEMS
        advancedItemsByCategory.computeIfAbsent(AdvancedItemCategory.MINING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new AdvancedHypixelItem("GEMSTONE_GAUNTLET", "Gemstone Gauntlet", "§d§lGemstone Gauntlet", Material.GOLDEN_PICKAXE,
                AdvancedItemRarity.LEGENDARY, AdvancedItemType.MINING_TOOL, "§7A gauntlet that mines gemstones efficiently.",
                Arrays.asList("§7Mining Speed: §a+200", "§7Mining Fortune: §a+100", "§7Gemstone Chance: §a+50%"),
                Arrays.asList("§7Ability: Gemstone Mining", "§7+50% gemstone chance"),
                Arrays.asList("§7- 1x Gemstone Gauntlet", "§7- 1x Enchanted Gold", "§7- 1x Enchanted Diamond")),
                
            new AdvancedHypixelItem("DIVAN_ARMOR", "Divan's Armor", "§6§lDivan's Armor", Material.DIAMOND_CHESTPLATE,
                AdvancedItemRarity.LEGENDARY, AdvancedItemType.MINING_ARMOR, "§7Armor that provides mining bonuses.",
                Arrays.asList("§7Health: §a+200", "§7Defense: §a+150", "§7Mining Speed: §a+40%"),
                Arrays.asList("§7Ability: Mining Mastery", "§7+40% mining speed"),
                Arrays.asList("§7- 1x Divan's Armor", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new AdvancedHypixelItem("MINING_FUEL_TANK", "Mining Fuel Tank", "§8§lMining Fuel Tank", Material.CAULDRON,
                AdvancedItemRarity.EPIC, AdvancedItemType.MINING_ACCESSORY, "§7A tank that provides mining fuel.",
                Arrays.asList("§7Mining Speed: §a+30", "§7Mining Fortune: §a+20"),
                Arrays.asList("§7Ability: Fuel Boost", "§7+30 mining speed"),
                Arrays.asList("§7- 1x Mining Fuel Tank", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Coal")),
                
            new AdvancedHypixelItem("GEMSTONE_DRILL", "Gemstone Drill", "§5§lGemstone Drill", Material.DIAMOND_PICKAXE,
                AdvancedItemRarity.LEGENDARY, AdvancedItemType.MINING_TOOL, "§7A drill that mines gemstones.",
                Arrays.asList("§7Mining Speed: §a+250", "§7Mining Fortune: §a+150", "§7Gemstone Chance: §a+75%"),
                Arrays.asList("§7Ability: Gemstone Drilling", "§7+75% gemstone chance"),
                Arrays.asList("§7- 1x Gemstone Drill", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Redstone"))
        ));
        
        // FARMING ITEMS
        advancedItemsByCategory.computeIfAbsent(AdvancedItemCategory.FARMING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new AdvancedHypixelItem("FARMING_HOE", "Farming Hoe", "§a§lFarming Hoe", Material.DIAMOND_HOE,
                AdvancedItemRarity.RARE, AdvancedItemType.FARMING_TOOL, "§7A hoe that provides farming bonuses.",
                Arrays.asList("§7Farming Speed: §a+100", "§7Farming Fortune: §a+50", "§7Farming XP: §a+25%"),
                Arrays.asList("§7Ability: Farming Mastery", "§7+25% farming XP"),
                Arrays.asList("§7- 1x Farming Hoe", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Stick")),
                
            new AdvancedHypixelItem("CROP_ARMOR", "Crop Armor", "§a§lCrop Armor", Material.LEATHER_CHESTPLATE,
                AdvancedItemRarity.UNCOMMON, AdvancedItemType.FARMING_ARMOR, "§7Armor that provides farming bonuses.",
                Arrays.asList("§7Health: §a+80", "§7Defense: §a+60", "§7Farming Speed: §a+20%"),
                Arrays.asList("§7Ability: Crop Growth", "§7+20% farming speed"),
                Arrays.asList("§7- 1x Crop Armor", "§7- 1x Enchanted Leather", "§7- 1x Enchanted Wheat")),
                
            new AdvancedHypixelItem("FARMING_BOOTS", "Farming Boots", "§a§lFarming Boots", Material.LEATHER_BOOTS,
                AdvancedItemRarity.UNCOMMON, AdvancedItemType.FARMING_ARMOR, "§7Boots that provide farming bonuses.",
                Arrays.asList("§7Health: §a+40", "§7Defense: §a+30", "§7Farming Fortune: §a+15%"),
                Arrays.asList("§7Ability: Crop Fortune", "§7+15% farming fortune"),
                Arrays.asList("§7- 1x Farming Boots", "§7- 1x Enchanted Leather", "§7- 1x Enchanted Carrot"))
        ));
        
        // FORAGING ITEMS
        advancedItemsByCategory.computeIfAbsent(AdvancedItemCategory.FORAGING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new AdvancedHypixelItem("FORAGING_AXE", "Foraging Axe", "§2§lForaging Axe", Material.DIAMOND_AXE,
                AdvancedItemRarity.RARE, AdvancedItemType.FORAGING_TOOL, "§7An axe that provides foraging bonuses.",
                Arrays.asList("§7Foraging Speed: §a+100", "§7Foraging Fortune: §a+50", "§7Foraging XP: §a+25%"),
                Arrays.asList("§7Ability: Foraging Mastery", "§7+25% foraging XP"),
                Arrays.asList("§7- 1x Foraging Axe", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Stick")),
                
            new AdvancedHypixelItem("TREE_ARMOR", "Tree Armor", "§2§lTree Armor", Material.LEATHER_CHESTPLATE,
                AdvancedItemRarity.UNCOMMON, AdvancedItemType.FORAGING_ARMOR, "§7Armor that provides foraging bonuses.",
                Arrays.asList("§7Health: §a+80", "§7Defense: §a+60", "§7Foraging Speed: §a+20%"),
                Arrays.asList("§7Ability: Tree Growth", "§7+20% foraging speed"),
                Arrays.asList("§7- 1x Tree Armor", "§7- 1x Enchanted Leather", "§7- 1x Enchanted Oak Log"))
        ));
    }
    
    private void startAdvancedItemUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerAdvancedItemData> entry : playerAdvancedItemData.entrySet()) {
                    PlayerAdvancedItemData itemData = entry.getValue();
                    itemData.update();
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
        
        if (displayName.contains("Advanced") || displayName.contains("Dungeon") || displayName.contains("Slayer")) {
            openAdvancedItemsGUI(player);
        }
    }
    
    public void openAdvancedItemsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§5§lAdvanced Hypixel Items");
        
        addGUIItem(gui, 10, Material.STICK, "§9§lDungeon Items", "§7View all dungeon items");
        addGUIItem(gui, 11, Material.IRON_SWORD, "§c§lSlayer Items", "§7View all slayer items");
        addGUIItem(gui, 12, Material.FISHING_ROD, "§b§lFishing Items", "§7View all fishing items");
        addGUIItem(gui, 13, Material.GOLDEN_PICKAXE, "§7§lMining Items", "§7View all mining items");
        addGUIItem(gui, 14, Material.DIAMOND_HOE, "§a§lFarming Items", "§7View all farming items");
        addGUIItem(gui, 15, Material.DIAMOND_AXE, "§2§lForaging Items", "§7View all foraging items");
        
        player.openInventory(gui);
        player.sendMessage("§aAdvanced Hypixel Items GUI geöffnet!");
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
    
    public PlayerAdvancedItemData getPlayerAdvancedItemData(UUID playerId) {
        return playerAdvancedItemData.computeIfAbsent(playerId, k -> new PlayerAdvancedItemData(playerId));
    }
    
    public enum AdvancedItemCategory {
        DUNGEON("§9Dungeon", "§7Dungeon-specific items"),
        SLAYER("§cSlayer", "§7Slayer-specific items"),
        FISHING("§bFishing", "§7Fishing-specific items"),
        MINING("§7Mining", "§7Mining-specific items"),
        FARMING("§aFarming", "§7Farming-specific items"),
        FORAGING("§2Foraging", "§7Foraging-specific items");
        
        private final String displayName;
        private final String description;
        
        AdvancedItemCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum AdvancedItemRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        AdvancedItemRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public enum AdvancedItemType {
        DUNGEON_WEAPON, DUNGEON_ARMOR, SLAYER_WEAPON, SLAYER_ARMOR, FISHING_ROD, FISHING_ARMOR, FISHING_ACCESSORY, FISHING_BAIT,
        MINING_TOOL, MINING_ARMOR, MINING_ACCESSORY, FARMING_TOOL, FARMING_ARMOR, FORAGING_TOOL, FORAGING_ARMOR
    }
    
    public static class AdvancedHypixelItem {
        private final String id;
        private final String name;
        private final String displayName;
        private final Material material;
        private final AdvancedItemRarity rarity;
        private final AdvancedItemType type;
        private final String description;
        private final List<String> stats;
        private final List<String> abilities;
        private final List<String> craftingMaterials;
        
        public AdvancedHypixelItem(String id, String name, String displayName, Material material,
                                  AdvancedItemRarity rarity, AdvancedItemType type, String description,
                                  List<String> stats, List<String> abilities, List<String> craftingMaterials) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.rarity = rarity;
            this.type = type;
            this.description = description;
            this.stats = stats;
            this.abilities = abilities;
            this.craftingMaterials = craftingMaterials;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public AdvancedItemRarity getRarity() { return rarity; }
        public AdvancedItemType getType() { return type; }
        public String getDescription() { return description; }
        public List<String> getStats() { return stats; }
        public List<String> getAbilities() { return abilities; }
        public List<String> getCraftingMaterials() { return craftingMaterials; }
    }
    
    public static class PlayerAdvancedItemData {
        private final UUID playerId;
        private final Map<String, Integer> advancedItemCounts = new HashMap<>();
        private final Map<String, Boolean> advancedItemUnlocked = new HashMap<>();
        private long lastUpdate;
        
        public PlayerAdvancedItemData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addAdvancedItem(String itemId, int amount) {
            advancedItemCounts.put(itemId, advancedItemCounts.getOrDefault(itemId, 0) + amount);
        }
        
        public void unlockAdvancedItem(String itemId) {
            advancedItemUnlocked.put(itemId, true);
        }
        
        public int getAdvancedItemCount(String itemId) {
            return advancedItemCounts.getOrDefault(itemId, 0);
        }
        
        public boolean isAdvancedItemUnlocked(String itemId) {
            return advancedItemUnlocked.getOrDefault(itemId, false);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
