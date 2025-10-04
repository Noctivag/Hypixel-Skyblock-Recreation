package de.noctivag.skyblock.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
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
 * Hypixel Skyblock Items System - Comprehensive Item Collection
 */
public class HypixelSkyblockItemsSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerItemData> playerItemData = new ConcurrentHashMap<>();
    private final Map<ItemCategory, List<HypixelItem>> itemsByCategory = new HashMap<>();
    private final Map<UUID, BukkitTask> itemTasks = new ConcurrentHashMap<>();
    
    public HypixelSkyblockItemsSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeAllItems();
        startItemUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeAllItems() {
        // SWORDS
        itemsByCategory.computeIfAbsent(ItemCategory.SWORDS, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelItem("ASPECT_OF_THE_END", "Aspect of the End", "§a§lAspect of the End", Material.DIAMOND_SWORD,
                ItemRarity.RARE, ItemType.SWORD, "§7Teleports you 8 blocks ahead of you and gives a speed boost for 3 seconds.",
                Arrays.asList("§7Damage: §c+100", "§7Strength: §c+100", "§7Intelligence: §a+50"),
                Arrays.asList("§7Ability: Instant Transmission", "§7Mana Cost: §b50", "§7Teleports §a8 blocks §7ahead"),
                Arrays.asList("§7- 1x Aspect of the End", "§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Diamond")),
                
            new HypixelItem("ASPECT_OF_THE_VOID", "Aspect of the Void", "§5§lAspect of the Void", Material.DIAMOND_SWORD,
                ItemRarity.EPIC, ItemType.SWORD, "§7Teleports you 8 blocks ahead of you and gives a speed boost for 5 seconds.",
                Arrays.asList("§7Damage: §c+150", "§7Strength: §c+150", "§7Intelligence: §a+100"),
                Arrays.asList("§7Ability: Void Transmission", "§7Mana Cost: §b75", "§7Teleports §a8 blocks §7ahead"),
                Arrays.asList("§7- 1x Aspect of the Void", "§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Diamond")),
                
            new HypixelItem("HYPERION", "Hyperion", "§d§lHyperion", Material.DIAMOND_SWORD,
                ItemRarity.LEGENDARY, ItemType.SWORD, "§7The legendary sword of the Wither King.",
                Arrays.asList("§7Damage: §c+270", "§7Strength: §c+120", "§7Intelligence: §a+350"),
                Arrays.asList("§7Ability: Wither Impact", "§7Mana Cost: §b200", "§7Explodes nearby enemies"),
                Arrays.asList("§7- 1x Hyperion", "§7- 1x Wither Catalyst", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelItem("VALKYRIE", "Valkyrie", "§6§lValkyrie", Material.DIAMOND_SWORD,
                ItemRarity.LEGENDARY, ItemType.SWORD, "§7The legendary sword of the Valkyrie.",
                Arrays.asList("§7Damage: §c+270", "§7Strength: §c+120", "§7Intelligence: §a+350"),
                Arrays.asList("§7Ability: Valkyrie Impact", "§7Mana Cost: §b200", "§7Explodes nearby enemies"),
                Arrays.asList("§7- 1x Valkyrie", "§7- 1x Wither Catalyst", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelItem("SCYLLA", "Scylla", "§9§lScylla", Material.DIAMOND_SWORD,
                ItemRarity.LEGENDARY, ItemType.SWORD, "§7The legendary sword of the Scylla.",
                Arrays.asList("§7Damage: §c+270", "§7Strength: §c+120", "§7Intelligence: §a+350"),
                Arrays.asList("§7Ability: Scylla Impact", "§7Mana Cost: §b200", "§7Explodes nearby enemies"),
                Arrays.asList("§7- 1x Scylla", "§7- 1x Wither Catalyst", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelItem("ASTRAEA", "Astraea", "§b§lAstraea", Material.DIAMOND_SWORD,
                ItemRarity.LEGENDARY, ItemType.SWORD, "§7The legendary sword of the Astraea.",
                Arrays.asList("§7Damage: §c+270", "§7Strength: §c+120", "§7Intelligence: §a+350"),
                Arrays.asList("§7Ability: Astraea Impact", "§7Mana Cost: §b200", "§7Explodes nearby enemies"),
                Arrays.asList("§7- 1x Astraea", "§7- 1x Wither Catalyst", "§7- 1x Enchanted Diamond Block"))
        ));
        
        // BOWS
        itemsByCategory.computeIfAbsent(ItemCategory.BOWS, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelItem("MOSQUITO_BOW", "Mosquito Bow", "§c§lMosquito Bow", Material.BOW,
                ItemRarity.LEGENDARY, ItemType.BOW, "§7A powerful bow that drains life from enemies.",
                Arrays.asList("§7Damage: §c+180", "§7Strength: §c+50", "§7Crit Damage: §c+50%"),
                Arrays.asList("§7Ability: Life Drain", "§7Mana Cost: §b100", "§7Drains life from enemies"),
                Arrays.asList("§7- 1x Mosquito Bow", "§7- 1x Enchanted String", "§7- 1x Enchanted Iron")),
                
            new HypixelItem("SPIRIT_BOW", "Spirit Bow", "§d§lSpirit Bow", Material.BOW,
                ItemRarity.LEGENDARY, ItemType.BOW, "§7A mystical bow that fires spirit arrows.",
                Arrays.asList("§7Damage: §c+200", "§7Strength: §c+60", "§7Crit Damage: §c+60%"),
                Arrays.asList("§7Ability: Spirit Arrow", "§7Mana Cost: §b120", "§7Fires spirit arrows"),
                Arrays.asList("§7- 1x Spirit Bow", "§7- 1x Enchanted String", "§7- 1x Enchanted Diamond")),
                
            new HypixelItem("THORN_BOW", "Thorn Bow", "§2§lThorn Bow", Material.BOW,
                ItemRarity.LEGENDARY, ItemType.BOW, "§7A bow that fires thorn arrows.",
                Arrays.asList("§7Damage: §c+190", "§7Strength: §c+55", "§7Crit Damage: §c+55%"),
                Arrays.asList("§7Ability: Thorn Arrow", "§7Mana Cost: §b110", "§7Fires thorn arrows"),
                Arrays.asList("§7- 1x Thorn Bow", "§7- 1x Enchanted String", "§7- 1x Enchanted Emerald"))
        ));
        
        // ARMOR SETS
        itemsByCategory.computeIfAbsent(ItemCategory.ARMOR, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelItem("DRAGON_ARMOR", "Dragon Armor", "§6§lDragon Armor", Material.DIAMOND_CHESTPLATE,
                ItemRarity.LEGENDARY, ItemType.ARMOR, "§7Armor forged from dragon scales.",
                Arrays.asList("§7Health: §a+200", "§7Defense: §a+150", "§7Strength: §c+100"),
                Arrays.asList("§7Set Bonus: Dragon's Fury", "§7+25% damage to dragons"),
                Arrays.asList("§7- 1x Dragon Scale", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelItem("NECRON_ARMOR", "Necron's Armor", "§5§lNecron's Armor", Material.NETHERITE_CHESTPLATE,
                ItemRarity.LEGENDARY, ItemType.ARMOR, "§7Armor of the Wither King.",
                Arrays.asList("§7Health: §a+300", "§7Defense: §a+200", "§7Strength: §c+150"),
                Arrays.asList("§7Set Bonus: Wither's Wrath", "§7+50% damage to withers"),
                Arrays.asList("§7- 1x Wither Catalyst", "§7- 1x Enchanted Netherite", "§7- 1x Enchanted Diamond")),
                
            new HypixelItem("STORM_ARMOR", "Storm's Armor", "§b§lStorm's Armor", Material.DIAMOND_CHESTPLATE,
                ItemRarity.LEGENDARY, ItemType.ARMOR, "§7Armor of the Storm.",
                Arrays.asList("§7Health: §a+250", "§7Defense: §a+180", "§7Intelligence: §a+200"),
                Arrays.asList("§7Set Bonus: Storm's Fury", "§7+30% magic damage"),
                Arrays.asList("§7- 1x Storm Fragment", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Lapis")),
                
            new HypixelItem("GOLDOR_ARMOR", "Goldor's Armor", "§6§lGoldor's Armor", Material.DIAMOND_CHESTPLATE,
                ItemRarity.LEGENDARY, ItemType.ARMOR, "§7Armor of the Goldor.",
                Arrays.asList("§7Health: §a+400", "§7Defense: §a+300", "§7Strength: §c+100"),
                Arrays.asList("§7Set Bonus: Goldor's Shield", "§7+40% damage reduction"),
                Arrays.asList("§7- 1x Goldor Fragment", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelItem("MAXOR_ARMOR", "Maxor's Armor", "§c§lMaxor's Armor", Material.DIAMOND_CHESTPLATE,
                ItemRarity.LEGENDARY, ItemType.ARMOR, "§7Armor of the Maxor.",
                Arrays.asList("§7Health: §a+200", "§7Defense: §a+150", "§7Speed: §f+50"),
                Arrays.asList("§7Set Bonus: Maxor's Speed", "§7+25% movement speed"),
                Arrays.asList("§7- 1x Maxor Fragment", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Redstone"))
        ));
        
        // ACCESSORIES
        itemsByCategory.computeIfAbsent(ItemCategory.ACCESSORIES, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelItem("ZOMBIE_TALISMAN", "Zombie Talisman", "§2§lZombie Talisman", Material.ROTTEN_FLESH,
                ItemRarity.COMMON, ItemType.ACCESSORY, "§7A talisman that increases damage to zombies.",
                Arrays.asList("§7Damage: §c+5", "§7Strength: §c+5"),
                Arrays.asList("§7Bonus: +10% damage to zombies"),
                Arrays.asList("§7- 1x Rotten Flesh", "§7- 1x String")),
                
            new HypixelItem("SKELETON_TALISMAN", "Skeleton Talisman", "§f§lSkeleton Talisman", Material.BONE,
                ItemRarity.COMMON, ItemType.ACCESSORY, "§7A talisman that increases damage to skeletons.",
                Arrays.asList("§7Damage: §c+5", "§7Strength: §c+5"),
                Arrays.asList("§7Bonus: +10% damage to skeletons"),
                Arrays.asList("§7- 1x Bone", "§7- 1x String")),
                
            new HypixelItem("SPIDER_TALISMAN", "Spider Talisman", "§8§lSpider Talisman", Material.SPIDER_EYE,
                ItemRarity.COMMON, ItemType.ACCESSORY, "§7A talisman that increases damage to spiders.",
                Arrays.asList("§7Damage: §c+5", "§7Strength: §c+5"),
                Arrays.asList("§7Bonus: +10% damage to spiders"),
                Arrays.asList("§7- 1x Spider Eye", "§7- 1x String")),
                
            new HypixelItem("CREEPER_TALISMAN", "Creeper Talisman", "§a§lCreeper Talisman", Material.GUNPOWDER,
                ItemRarity.COMMON, ItemType.ACCESSORY, "§7A talisman that increases damage to creepers.",
                Arrays.asList("§7Damage: §c+5", "§7Strength: §c+5"),
                Arrays.asList("§7Bonus: +10% damage to creepers"),
                Arrays.asList("§7- 1x Gunpowder", "§7- 1x String")),
                
            new HypixelItem("END_TALISMAN", "End Talisman", "§5§lEnd Talisman", Material.ENDER_PEARL,
                ItemRarity.UNCOMMON, ItemType.ACCESSORY, "§7A talisman that increases damage to endermen.",
                Arrays.asList("§7Damage: §c+10", "§7Strength: §c+10"),
                Arrays.asList("§7Bonus: +15% damage to endermen"),
                Arrays.asList("§7- 1x Ender Pearl", "§7- 1x String")),
                
            new HypixelItem("BLAZE_TALISMAN", "Blaze Talisman", "§c§lBlaze Talisman", Material.BLAZE_ROD,
                ItemRarity.UNCOMMON, ItemType.ACCESSORY, "§7A talisman that increases damage to blazes.",
                Arrays.asList("§7Damage: §c+10", "§7Strength: §c+10"),
                Arrays.asList("§7Bonus: +15% damage to blazes"),
                Arrays.asList("§7- 1x Blaze Rod", "§7- 1x String")),
                
            new HypixelItem("WITHER_TALISMAN", "Wither Talisman", "§8§lWither Talisman", Material.WITHER_SKELETON_SKULL,
                ItemRarity.RARE, ItemType.ACCESSORY, "§7A talisman that increases damage to withers.",
                Arrays.asList("§7Damage: §c+20", "§7Strength: §c+20"),
                Arrays.asList("§7Bonus: +25% damage to withers"),
                Arrays.asList("§7- 1x Wither Skeleton Skull", "§7- 1x String")),
                
            new HypixelItem("DRAGON_TALISMAN", "Dragon Talisman", "§6§lDragon Talisman", Material.DRAGON_EGG,
                ItemRarity.EPIC, ItemType.ACCESSORY, "§7A talisman that increases damage to dragons.",
                Arrays.asList("§7Damage: §c+30", "§7Strength: §c+30"),
                Arrays.asList("§7Bonus: +35% damage to dragons"),
                Arrays.asList("§7- 1x Dragon Egg", "§7- 1x String"))
        ));
        
        // TOOLS
        itemsByCategory.computeIfAbsent(ItemCategory.TOOLS, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelItem("DIAMOND_PICKAXE", "Diamond Pickaxe", "§b§lDiamond Pickaxe", Material.DIAMOND_PICKAXE,
                ItemRarity.UNCOMMON, ItemType.TOOL, "§7A powerful pickaxe for mining.",
                Arrays.asList("§7Mining Speed: §a+50", "§7Mining Fortune: §a+25"),
                Arrays.asList("§7Ability: Efficient Mining", "§7+25% mining speed"),
                Arrays.asList("§7- 1x Diamond Pickaxe", "§7- 1x Enchanted Diamond")),
                
            new HypixelItem("GOLDEN_PICKAXE", "Golden Pickaxe", "§6§lGolden Pickaxe", Material.GOLDEN_PICKAXE,
                ItemRarity.RARE, ItemType.TOOL, "§7A golden pickaxe with special abilities.",
                Arrays.asList("§7Mining Speed: §a+75", "§7Mining Fortune: §a+50"),
                Arrays.asList("§7Ability: Golden Touch", "§7+50% mining speed"),
                Arrays.asList("§7- 1x Golden Pickaxe", "§7- 1x Enchanted Gold")),
                
            new HypixelItem("NETHERITE_PICKAXE", "Netherite Pickaxe", "§8§lNetherite Pickaxe", Material.NETHERITE_PICKAXE,
                ItemRarity.EPIC, ItemType.TOOL, "§7The ultimate pickaxe for mining.",
                Arrays.asList("§7Mining Speed: §a+100", "§7Mining Fortune: §a+75"),
                Arrays.asList("§7Ability: Netherite Mining", "§7+75% mining speed"),
                Arrays.asList("§7- 1x Netherite Pickaxe", "§7- 1x Enchanted Netherite")),
                
            new HypixelItem("DIAMOND_AXE", "Diamond Axe", "§b§lDiamond Axe", Material.DIAMOND_AXE,
                ItemRarity.UNCOMMON, ItemType.TOOL, "§7A powerful axe for chopping.",
                Arrays.asList("§7Chopping Speed: §a+50", "§7Chopping Fortune: §a+25"),
                Arrays.asList("§7Ability: Efficient Chopping", "§7+25% chopping speed"),
                Arrays.asList("§7- 1x Diamond Axe", "§7- 1x Enchanted Diamond")),
                
            new HypixelItem("GOLDEN_AXE", "Golden Axe", "§6§lGolden Axe", Material.GOLDEN_AXE,
                ItemRarity.RARE, ItemType.TOOL, "§7A golden axe with special abilities.",
                Arrays.asList("§7Chopping Speed: §a+75", "§7Chopping Fortune: §a+50"),
                Arrays.asList("§7Ability: Golden Touch", "§7+50% chopping speed"),
                Arrays.asList("§7- 1x Golden Axe", "§7- 1x Enchanted Gold")),
                
            new HypixelItem("NETHERITE_AXE", "Netherite Axe", "§8§lNetherite Axe", Material.NETHERITE_AXE,
                ItemRarity.EPIC, ItemType.TOOL, "§7The ultimate axe for chopping.",
                Arrays.asList("§7Chopping Speed: §a+100", "§7Chopping Fortune: §a+75"),
                Arrays.asList("§7Ability: Netherite Chopping", "§7+75% chopping speed"),
                Arrays.asList("§7- 1x Netherite Axe", "§7- 1x Enchanted Netherite"))
        ));
        
        // SPECIAL ITEMS
        itemsByCategory.computeIfAbsent(ItemCategory.SPECIAL, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelItem("BOOSTER_COOKIE", "Booster Cookie", "§6§lBooster Cookie", Material.COOKIE,
                ItemRarity.LEGENDARY, ItemType.SPECIAL, "§7A special cookie that provides various boosts.",
                Arrays.asList("§7Duration: §a4 days", "§7Magic Find: §a+15%", "§7Pet Luck: §a+15%"),
                Arrays.asList("§7Ability: Cookie Buff", "§7Provides various boosts"),
                Arrays.asList("§7- 1x Booster Cookie", "§7- 1x Enchanted Sugar", "§7- 1x Enchanted Wheat")),
                
            new HypixelItem("GOD_POTION", "God Potion", "§d§lGod Potion", Material.POTION,
                ItemRarity.LEGENDARY, ItemType.SPECIAL, "§7A potion that provides all positive effects.",
                Arrays.asList("§7Duration: §a24 hours", "§7All Effects: §a+100%", "§7Magic Find: §a+20%"),
                Arrays.asList("§7Ability: God Buff", "§7Provides all positive effects"),
                Arrays.asList("§7- 1x God Potion", "§7- 1x Enchanted Blaze Rod", "§7- 1x Enchanted Nether Wart")),
                
            new HypixelItem("RECOMBOBULATOR_3000", "Recombobulator 3000", "§5§lRecombobulator 3000", Material.ANVIL,
                ItemRarity.LEGENDARY, ItemType.SPECIAL, "§7Upgrades the rarity of any item by one tier.",
                Arrays.asList("§7Rarity Upgrade: §a+1 tier", "§7Success Rate: §a100%"),
                Arrays.asList("§7Ability: Rarity Upgrade", "§7Upgrades item rarity"),
                Arrays.asList("§7- 1x Recombobulator 3000", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Gold Block")),
                
            new HypixelItem("DUNGEON_POTION", "Dungeon Potion", "§9§lDungeon Potion", Material.POTION,
                ItemRarity.EPIC, ItemType.SPECIAL, "§7A potion that provides dungeon-specific effects.",
                Arrays.asList("§7Duration: §a2 hours", "§7Dungeon Damage: §a+25%", "§7Dungeon Defense: §a+25%"),
                Arrays.asList("§7Ability: Dungeon Buff", "§7Provides dungeon effects"),
                Arrays.asList("§7- 1x Dungeon Potion", "§7- 1x Enchanted Blaze Rod", "§7- 1x Enchanted Nether Wart")),
                
            new HypixelItem("SPEED_POTION", "Speed Potion", "§a§lSpeed Potion", Material.POTION,
                ItemRarity.UNCOMMON, ItemType.SPECIAL, "§7A potion that increases movement speed.",
                Arrays.asList("§7Duration: §a1 hour", "§7Speed: §a+50%", "§7Jump Boost: §a+25%"),
                Arrays.asList("§7Ability: Speed Boost", "§7Increases movement speed"),
                Arrays.asList("§7- 1x Speed Potion", "§7- 1x Enchanted Sugar", "§7- 1x Enchanted Nether Wart")),
                
            new HypixelItem("STRENGTH_POTION", "Strength Potion", "§c§lStrength Potion", Material.POTION,
                ItemRarity.UNCOMMON, ItemType.SPECIAL, "§7A potion that increases damage.",
                Arrays.asList("§7Duration: §a1 hour", "§7Damage: §a+50%", "§7Strength: §a+25%"),
                Arrays.asList("§7Ability: Strength Boost", "§7Increases damage"),
                Arrays.asList("§7- 1x Strength Potion", "§7- 1x Enchanted Blaze Rod", "§7- 1x Enchanted Nether Wart"))
        ));
    }
    
    private void startItemUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerItemData> entry : playerItemData.entrySet()) {
                    PlayerItemData itemData = entry.getValue();
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
        
        if (displayName.contains("Hypixel") || displayName.contains("Skyblock")) {
            openItemsGUI(player);
        }
    }
    
    public void openItemsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lHypixel Skyblock Items");
        
        addGUIItem(gui, 10, Material.DIAMOND_SWORD, "§c§lSwords", "§7View all swords");
        addGUIItem(gui, 11, Material.BOW, "§a§lBows", "§7View all bows");
        addGUIItem(gui, 12, Material.DIAMOND_CHESTPLATE, "§b§lArmor", "§7View all armor");
        addGUIItem(gui, 13, Material.ROTTEN_FLESH, "§e§lAccessories", "§7View all accessories");
        addGUIItem(gui, 14, Material.DIAMOND_PICKAXE, "§7§lTools", "§7View all tools");
        addGUIItem(gui, 15, Material.COOKIE, "§d§lSpecial Items", "§7View all special items");
        
        player.openInventory(gui);
        player.sendMessage("§aHypixel Skyblock Items GUI geöffnet!");
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
    
    public PlayerItemData getPlayerItemData(UUID playerId) {
        return playerItemData.computeIfAbsent(playerId, k -> new PlayerItemData(playerId));
    }
    
    public enum ItemCategory {
        SWORDS("§cSwords", "§7Swords and melee weapons"),
        BOWS("§aBows", "§7Bows and ranged weapons"),
        ARMOR("§bArmor", "§7Armor sets and pieces"),
        ACCESSORIES("§eAccessories", "§7Talismans and accessories"),
        TOOLS("§7Tools", "§7Mining and chopping tools"),
        SPECIAL("§dSpecial Items", "§7Special and unique items");
        
        private final String displayName;
        private final String description;
        
        ItemCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum ItemRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        ItemRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public enum ItemType {
        SWORD, BOW, ARMOR, ACCESSORY, TOOL, SPECIAL, PET, MINION, FURNITURE
    }
    
    public static class HypixelItem {
        private final String id;
        private final String name;
        private final String displayName;
        private final Material material;
        private final ItemRarity rarity;
        private final ItemType type;
        private final String description;
        private final List<String> stats;
        private final List<String> abilities;
        private final List<String> craftingMaterials;
        
        public HypixelItem(String id, String name, String displayName, Material material,
                          ItemRarity rarity, ItemType type, String description,
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
        public ItemRarity getRarity() { return rarity; }
        public ItemType getType() { return type; }
        public String getDescription() { return description; }
        public List<String> getStats() { return stats; }
        public List<String> getAbilities() { return abilities; }
        public List<String> getCraftingMaterials() { return craftingMaterials; }
    }
    
    public static class PlayerItemData {
        private final UUID playerId;
        private final Map<String, Integer> itemCounts = new HashMap<>();
        private final Map<String, Boolean> itemUnlocked = new HashMap<>();
        private long lastUpdate;
        
        public PlayerItemData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addItem(String itemId, int amount) {
            itemCounts.put(itemId, itemCounts.getOrDefault(itemId, 0) + amount);
        }
        
        public void unlockItem(String itemId) {
            itemUnlocked.put(itemId, true);
        }
        
        public int getItemCount(String itemId) {
            return itemCounts.getOrDefault(itemId, 0);
        }
        
        public boolean isItemUnlocked(String itemId) {
            return itemUnlocked.getOrDefault(itemId, false);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
