package de.noctivag.plugin.armor;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Armor System - Hypixel Skyblock Style
 * 
 * Features:
 * - Dynamic Armor Sets
 * - Armor Abilities
 * - Armor Stats
 * - Armor Rarities
 * - Armor Enchantments
 * - Armor Reforges
 * - Armor Gemstones
 * - Armor Customization
 * - Armor Progression
 * - Set Bonuses
 */
public class AdvancedArmorSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerArmor> playerArmor = new ConcurrentHashMap<>();
    private final Map<ArmorType, ArmorConfig> armorConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> armorTasks = new ConcurrentHashMap<>();
    
    public AdvancedArmorSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeArmorConfigs();
        startArmorUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeArmorConfigs() {
        // Dragon Armor Sets
        armorConfigs.put(ArmorType.SUPERIOR_DRAGON, new ArmorConfig(
            "Superior Dragon Armor", "§6Superior Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7The ultimate dragon armor with superior stats.",
            ArmorCategory.DRAGON, ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- Superior Stats", "§7- All-around Bonus"),
            Arrays.asList("§7- 1x Superior Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        armorConfigs.put(ArmorType.UNSTABLE_DRAGON, new ArmorConfig(
            "Unstable Dragon Armor", "§5Unstable Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Unstable dragon armor with random effects.",
            ArmorCategory.DRAGON, ArmorRarity.LEGENDARY, 350, Arrays.asList("§7- Random Effects", "§7- Unstable Power"),
            Arrays.asList("§7- 1x Unstable Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        armorConfigs.put(ArmorType.STRONG_DRAGON, new ArmorConfig(
            "Strong Dragon Armor", "§cStrong Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Strong dragon armor focused on damage.",
            ArmorCategory.DRAGON, ArmorRarity.LEGENDARY, 380, Arrays.asList("§7- High Damage", "§7- Strength Bonus"),
            Arrays.asList("§7- 1x Strong Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        armorConfigs.put(ArmorType.YOUNG_DRAGON, new ArmorConfig(
            "Young Dragon Armor", "§fYoung Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Young dragon armor focused on speed.",
            ArmorCategory.DRAGON, ArmorRarity.LEGENDARY, 320, Arrays.asList("§7- Speed Bonus", "§7- Agility"),
            Arrays.asList("§7- 1x Young Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        armorConfigs.put(ArmorType.OLD_DRAGON, new ArmorConfig(
            "Old Dragon Armor", "§7Old Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Old dragon armor focused on defense.",
            ArmorCategory.DRAGON, ArmorRarity.LEGENDARY, 360, Arrays.asList("§7- High Defense", "§7- Protection"),
            Arrays.asList("§7- 1x Old Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        armorConfigs.put(ArmorType.PROTECTOR_DRAGON, new ArmorConfig(
            "Protector Dragon Armor", "§aProtector Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Protector dragon armor with maximum defense.",
            ArmorCategory.DRAGON, ArmorRarity.LEGENDARY, 420, Arrays.asList("§7- Maximum Defense", "§7- Protection"),
            Arrays.asList("§7- 1x Protector Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        armorConfigs.put(ArmorType.WISE_DRAGON, new ArmorConfig(
            "Wise Dragon Armor", "§bWise Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Wise dragon armor focused on intelligence.",
            ArmorCategory.DRAGON, ArmorRarity.LEGENDARY, 340, Arrays.asList("§7- Intelligence Bonus", "§7- Mana Boost"),
            Arrays.asList("§7- 1x Wise Dragon Fragment", "§7- 1x Diamond Armor")
        ));
        
        // Dungeon Armor Sets
        armorConfigs.put(ArmorType.SHADOW_ASSASSIN, new ArmorConfig(
            "Shadow Assassin Armor", "§5Shadow Assassin Armor", Material.LEATHER_CHESTPLATE,
            "§7Shadow assassin armor for stealth and speed.",
            ArmorCategory.DUNGEON, ArmorRarity.LEGENDARY, 450, Arrays.asList("§7- Stealth", "§7- Speed Bonus"),
            Arrays.asList("§7- 1x Shadow Assassin Fragment", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.ADAPTIVE_ARMOR, new ArmorConfig(
            "Adaptive Armor", "§eAdaptive Armor", Material.IRON_CHESTPLATE,
            "§7Adaptive armor that changes based on your needs.",
            ArmorCategory.DUNGEON, ArmorRarity.LEGENDARY, 400, Arrays.asList("§7- Adaptive Stats", "§7- Versatile"),
            Arrays.asList("§7- 1x Adaptive Fragment", "§7- 1x Iron Armor")
        ));
        
        armorConfigs.put(ArmorType.ZOMBIE_SOLDIER, new ArmorConfig(
            "Zombie Soldier Armor", "§2Zombie Soldier Armor", Material.CHAINMAIL_CHESTPLATE,
            "§7Zombie soldier armor for tanking damage.",
            ArmorCategory.DUNGEON, ArmorRarity.EPIC, 300, Arrays.asList("§7- High Defense", "§7- Tanking"),
            Arrays.asList("§7- 1x Zombie Soldier Fragment", "§7- 1x Chainmail Armor")
        ));
        
        armorConfigs.put(ArmorType.SKELETON_MASTER, new ArmorConfig(
            "Skeleton Master Armor", "§fSkeleton Master Armor", Material.CHAINMAIL_CHESTPLATE,
            "§7Skeleton master armor for ranged combat.",
            ArmorCategory.DUNGEON, ArmorRarity.EPIC, 320, Arrays.asList("§7- Ranged Combat", "§7- Archery Bonus"),
            Arrays.asList("§7- 1x Skeleton Master Fragment", "§7- 1x Chainmail Armor")
        ));
        
        armorConfigs.put(ArmorType.BONZO_ARMOR, new ArmorConfig(
            "Bonzo Armor", "§eBonzo Armor", Material.LEATHER_CHESTPLATE,
            "§7Bonzo armor for magic and intelligence.",
            ArmorCategory.DUNGEON, ArmorRarity.EPIC, 280, Arrays.asList("§7- Magic Bonus", "§7- Intelligence"),
            Arrays.asList("§7- 1x Bonzo Fragment", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.SCARF_ARMOR, new ArmorConfig(
            "Scarf Armor", "§cScarf Armor", Material.LEATHER_CHESTPLATE,
            "§7Scarf armor for healing and support.",
            ArmorCategory.DUNGEON, ArmorRarity.EPIC, 260, Arrays.asList("§7- Healing Bonus", "§7- Support"),
            Arrays.asList("§7- 1x Scarf Fragment", "§7- 1x Leather Armor")
        ));
        
        // Special Armor Sets
        armorConfigs.put(ArmorType.GOBLIN_ARMOR, new ArmorConfig(
            "Goblin Armor", "§aGoblin Armor", Material.GOLDEN_CHESTPLATE,
            "§7Goblin armor for mining and wealth.",
            ArmorCategory.SPECIAL, ArmorRarity.RARE, 200, Arrays.asList("§7- Mining Bonus", "§7- Wealth"),
            Arrays.asList("§7- 1x Goblin Fragment", "§7- 1x Golden Armor")
        ));
        
        armorConfigs.put(ArmorType.SPIDER_ARMOR, new ArmorConfig(
            "Spider Armor", "§8Spider Armor", Material.LEATHER_CHESTPLATE,
            "§7Spider armor for climbing and agility.",
            ArmorCategory.SPECIAL, ArmorRarity.RARE, 180, Arrays.asList("§7- Climbing", "§7- Agility"),
            Arrays.asList("§7- 1x Spider Fragment", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.ENDER_ARMOR, new ArmorConfig(
            "Ender Armor", "§5Ender Armor", Material.LEATHER_CHESTPLATE,
            "§7Ender armor for teleportation and ender abilities.",
            ArmorCategory.SPECIAL, ArmorRarity.EPIC, 250, Arrays.asList("§7- Teleportation", "§7- Ender Abilities"),
            Arrays.asList("§7- 1x Ender Fragment", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.FARMER_ARMOR, new ArmorConfig(
            "Farmer Armor", "§aFarmer Armor", Material.LEATHER_CHESTPLATE,
            "§7Farmer armor for farming and agriculture.",
            ArmorCategory.SPECIAL, ArmorRarity.RARE, 160, Arrays.asList("§7- Farming Bonus", "§7- Agriculture"),
            Arrays.asList("§7- 1x Farmer Fragment", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.MINER_ARMOR, new ArmorConfig(
            "Miner Armor", "§6Miner Armor", Material.IRON_CHESTPLATE,
            "§7Miner armor for mining and excavation.",
            ArmorCategory.SPECIAL, ArmorRarity.RARE, 220, Arrays.asList("§7- Mining Bonus", "§7- Excavation"),
            Arrays.asList("§7- 1x Miner Fragment", "§7- 1x Iron Armor")
        ));
        
        armorConfigs.put(ArmorType.FISHER_ARMOR, new ArmorConfig(
            "Fisher Armor", "§bFisher Armor", Material.LEATHER_CHESTPLATE,
            "§7Fisher armor for fishing and water activities.",
            ArmorCategory.SPECIAL, ArmorRarity.RARE, 140, Arrays.asList("§7- Fishing Bonus", "§7- Water Activities"),
            Arrays.asList("§7- 1x Fisher Fragment", "§7- 1x Leather Armor")
        ));
        
        // Additional Missing Armor Sets
        armorConfigs.put(ArmorType.FROZEN_BLAZE_ARMOR, new ArmorConfig(
            "Frozen Blaze Armor", "§bFrozen Blaze Armor", Material.BLUE_ICE,
            "§7Armor that combines ice and fire powers.",
            ArmorCategory.SPECIAL, ArmorRarity.LEGENDARY, 180, Arrays.asList("§7- Ice Fire", "§7- Temperature Immunity"),
            Arrays.asList("§7- 1x Blue Ice", "§7- 1x Blaze Rod")
        ));
        
        armorConfigs.put(ArmorType.SHADOW_ASSASSIN_ARMOR, new ArmorConfig(
            "Shadow Assassin Armor", "§5Shadow Assassin Armor", Material.BLACK_DYE,
            "§7Armor that enhances stealth and assassination.",
            ArmorCategory.DUNGEON, ArmorRarity.LEGENDARY, 220, Arrays.asList("§7- Stealth", "§7- Assassination"),
            Arrays.asList("§7- 1x Black Dye", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.SPIRIT_ARMOR, new ArmorConfig(
            "Spirit Armor", "§bSpirit Armor", Material.GHAST_TEAR,
            "§7Armor that channels spirit energy.",
            ArmorCategory.DUNGEON, ArmorRarity.LEGENDARY, 190, Arrays.asList("§7- Spirit Energy", "§7- Ghost Immunity"),
            Arrays.asList("§7- 1x Ghast Tear", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.YETI_ARMOR, new ArmorConfig(
            "Yeti Armor", "§fYeti Armor", Material.SNOW_BLOCK,
            "§7Armor that provides yeti-like protection.",
            ArmorCategory.SPECIAL, ArmorRarity.LEGENDARY, 230, Arrays.asList("§7- Yeti Protection", "§7- Cold Immunity"),
            Arrays.asList("§7- 1x Snow Block", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.REVENANT_ARMOR, new ArmorConfig(
            "Revenant Armor", "§2Revenant Armor", Material.ROTTEN_FLESH,
            "§7Armor that provides revenant-like protection.",
            ArmorCategory.SLAYER, ArmorRarity.LEGENDARY, 120, Arrays.asList("§7- Revenant Protection", "§7- Undead Immunity"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.TARANTULA_ARMOR, new ArmorConfig(
            "Tarantula Armor", "§8Tarantula Armor", Material.STRING,
            "§7Armor that provides tarantula-like protection.",
            ArmorCategory.SLAYER, ArmorRarity.LEGENDARY, 130, Arrays.asList("§7- Tarantula Protection", "§7- Spider Immunity"),
            Arrays.asList("§7- 1x String", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.SVEN_ARMOR, new ArmorConfig(
            "Sven Armor", "§fSven Armor", Material.BONE,
            "§7Armor that provides sven-like protection.",
            ArmorCategory.SLAYER, ArmorRarity.LEGENDARY, 140, Arrays.asList("§7- Sven Protection", "§7- Wolf Immunity"),
            Arrays.asList("§7- 1x Bone", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.VOIDGLOOM_ARMOR, new ArmorConfig(
            "Voidgloom Armor", "§5Voidgloom Armor", Material.ENDER_PEARL,
            "§7Armor that provides voidgloom-like protection.",
            ArmorCategory.SLAYER, ArmorRarity.LEGENDARY, 150, Arrays.asList("§7- Voidgloom Protection", "§7- Enderman Immunity"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 1x Leather Armor")
        ));
        
        armorConfigs.put(ArmorType.INFERNO_ARMOR, new ArmorConfig(
            "Inferno Armor", "§6Inferno Armor", Material.BLAZE_ROD,
            "§7Armor that provides inferno-like protection.",
            ArmorCategory.SLAYER, ArmorRarity.LEGENDARY, 160, Arrays.asList("§7- Inferno Protection", "§7- Fire Immunity"),
            Arrays.asList("§7- 1x Blaze Rod", "§7- 1x Leather Armor")
        ));
    }
    
    private void startArmorUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerArmor> entry : playerArmor.entrySet()) {
                    PlayerArmor armor = entry.getValue();
                    armor.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
            openArmorGUI(player);
        }
    }
    
    private void openArmorGUI(Player player) {
        player.sendMessage("§aArmor GUI geöffnet!");
    }
    
    public ItemStack createArmorPiece(ArmorType type, ArmorPiece piece, int level) {
        ArmorConfig config = armorConfigs.get(type);
        if (config == null) return null;
        
        Material material = getArmorMaterial(piece);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(config.getDisplayName() + " " + piece.getDisplayName() + " §7Lv." + level);
        List<String> lore = new ArrayList<>();
        lore.add(config.getDescription());
        lore.add("");
        lore.add("§7Level: §a" + level);
        lore.add("§7Rarity: " + config.getRarity().getColor() + config.getRarity().getName());
        lore.add("§7Category: " + config.getCategory().getColor() + config.getCategory().getName());
        lore.add("§7Piece: " + piece.getColor() + piece.getDisplayName());
        lore.add("");
        lore.addAll(config.getFeatures());
        lore.add("");
        lore.add("§7Click to use ability!");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    private Material getArmorMaterial(ArmorPiece piece) {
        switch (piece) {
            case HELMET: return Material.DIAMOND_HELMET;
            case CHESTPLATE: return Material.DIAMOND_CHESTPLATE;
            case LEGGINGS: return Material.DIAMOND_LEGGINGS;
            case BOOTS: return Material.DIAMOND_BOOTS;
            default: return Material.DIAMOND_CHESTPLATE;
        }
    }
    
    public PlayerArmor getPlayerArmor(UUID playerId) {
        return playerArmor.computeIfAbsent(playerId, k -> new PlayerArmor(playerId));
    }
    
    public ArmorConfig getArmorConfig(ArmorType type) {
        return armorConfigs.get(type);
    }
    
    public List<ArmorType> getAllArmorTypes() {
        return new ArrayList<>(armorConfigs.keySet());
    }
    
    public enum ArmorType {
        // Dragon Armor Sets
        SUPERIOR_DRAGON, UNSTABLE_DRAGON, STRONG_DRAGON, YOUNG_DRAGON, OLD_DRAGON, PROTECTOR_DRAGON, WISE_DRAGON,
        // Dungeon Armor Sets
        SHADOW_ASSASSIN, ADAPTIVE_ARMOR, ZOMBIE_SOLDIER, SKELETON_MASTER, BONZO_ARMOR, SCARF_ARMOR,
        // Special Armor Sets
        GOBLIN_ARMOR, SPIDER_ARMOR, ENDER_ARMOR, FARMER_ARMOR, MINER_ARMOR, FISHER_ARMOR,
        // Additional Armor Sets
        FROZEN_BLAZE_ARMOR, SHADOW_ASSASSIN_ARMOR, SPIRIT_ARMOR, YETI_ARMOR,
        // Slayer Armor Sets
        REVENANT_ARMOR, TARANTULA_ARMOR, SVEN_ARMOR, VOIDGLOOM_ARMOR, INFERNO_ARMOR,
        // Additional Armor Sets
        NECRON_ARMOR, STORM_ARMOR, GOLDOR_ARMOR, MAXOR_ARMOR, WITHER_ARMOR, SHADOW_ARMOR, FROZEN_ARMOR,
        BURNING_ARMOR, CRYSTAL_ARMOR, DIAMOND_ARMOR, EMERALD_ARMOR, RUBY_ARMOR, SAPPHIRE_ARMOR, AMETHYST_ARMOR,
        TOPAZ_ARMOR, JASPER_ARMOR, OPAL_ARMOR, JADE_ARMOR, AMBER_ARMOR, PEARL_ARMOR, CORAL_ARMOR, QUARTZ_ARMOR,
        LAPIS_ARMOR, REDSTONE_ARMOR, COAL_ARMOR, IRON_ARMOR, GOLD_ARMOR, COPPER_ARMOR, TIN_ARMOR, BRONZE_ARMOR,
        STEEL_ARMOR, SILVER_ARMOR, PLATINUM_ARMOR, TITANIUM_ARMOR, URANIUM_ARMOR, THORIUM_ARMOR, RADIUM_ARMOR,
        PLUTONIUM_ARMOR, NEPTUNIUM_ARMOR, AMERICIUM_ARMOR, CURIUM_ARMOR, BERKELIUM_ARMOR, CALIFORNIUM_ARMOR,
        EINSTEINIUM_ARMOR, FERMIUM_ARMOR, MENDELEVIUM_ARMOR, NOBELIUM_ARMOR, LAWRENCIUM_ARMOR, RUTHERFORDIUM_ARMOR,
        DUBNIUM_ARMOR, SEABORGIUM_ARMOR, BOHRIUM_ARMOR, HASSIUM_ARMOR, MEITNERIUM_ARMOR, DARMSTADTIUM_ARMOR,
        ROENTGENIUM_ARMOR, COPERNICIUM_ARMOR, NIHONIUM_ARMOR, FLEROVIUM_ARMOR, MOSCOVIUM_ARMOR, LIVERMORIUM_ARMOR,
        TENNESSINE_ARMOR, OGANESSON_ARMOR
    }
    
    public enum ArmorPiece {
        HELMET("§6Helmet", "§7Head protection"),
        CHESTPLATE("§6Chestplate", "§7Body protection"),
        LEGGINGS("§6Leggings", "§7Leg protection"),
        BOOTS("§6Boots", "§7Foot protection");
        
        private final String displayName;
        private final String description;
        
        ArmorPiece(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public String getColor() { return displayName.substring(0, 2); }
    }
    
    public enum ArmorCategory {
        DRAGON("§6Dragon", "§7Armor forged from dragon scales"),
        DUNGEON("§5Dungeon", "§7Armor found in dungeons"),
        SPECIAL("§eSpecial", "§7Special armor with unique effects"),
        SLAYER("§4Slayer", "§7Armor from slayer bosses");
        
        private final String name;
        private final String description;
        
        ArmorCategory(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getColor() { return name.substring(0, 2); }
    }
    
    public enum ArmorRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.2),
        RARE("§9Rare", 1.5),
        EPIC("§5Epic", 2.0),
        LEGENDARY("§6Legendary", 3.0),
        MYTHIC("§dMythic", 5.0),
        DIVINE("§bDivine", 10.0);
        
        private final String name;
        private final double multiplier;
        
        ArmorRarity(String name, double multiplier) {
            this.name = name;
            this.multiplier = multiplier;
        }
        
        public String getName() { return name; }
        public double getMultiplier() { return multiplier; }
        public String getColor() { return name.substring(0, 2); }
    }
    
    public static class ArmorConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final ArmorCategory category;
        private final ArmorRarity rarity;
        private final int baseDefense;
        private final List<String> features;
        private final List<String> requirements;
        
        public ArmorConfig(String name, String displayName, Material material, String description,
                          ArmorCategory category, ArmorRarity rarity, int baseDefense, List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.baseDefense = baseDefense;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public ArmorCategory getCategory() { return category; }
        public ArmorRarity getRarity() { return rarity; }
        public int getBaseDefense() { return baseDefense; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerArmor {
        private final UUID playerId;
        private final Map<ArmorType, Integer> armorLevels = new ConcurrentHashMap<>();
        private int totalArmor = 0;
        private long totalArmorTime = 0;
        private long lastUpdate;
        
        public PlayerArmor(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save armor data to database
        }
        
        public void addArmor(ArmorType type, int level) {
            armorLevels.put(type, level);
            totalArmor++;
        }
        
        public int getArmorLevel(ArmorType type) {
            return armorLevels.getOrDefault(type, 0);
        }
        
        public int getTotalArmor() { return totalArmor; }
        public long getTotalArmorTime() { return totalArmorTime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<ArmorType, Integer> getArmorLevels() { return armorLevels; }
    }
}
