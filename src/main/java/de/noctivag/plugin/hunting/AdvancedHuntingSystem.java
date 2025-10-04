package de.noctivag.plugin.hunting;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedHuntingSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerHuntingData> playerHuntingData = new ConcurrentHashMap<>();
    private final Map<CreatureType, CreatureConfig> creatureConfigs = new HashMap<>();
    private final Map<ShardType, ShardConfig> shardConfigs = new HashMap<>();
    
    public AdvancedHuntingSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeCreatureConfigs();
        initializeShardConfigs();
        startHuntingUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeCreatureConfigs() {
        // Basic Creatures
        creatureConfigs.put(CreatureType.ZOMBIE, new CreatureConfig(
            "Zombie", "§2Zombie", Material.ZOMBIE_HEAD,
            "§7A basic undead creature.",
            CreatureRarity.COMMON, 10, Arrays.asList("§7- Basic Shards", "§7- Low Health"),
            Arrays.asList("§7- 1x Zombie Shard", "§7- 1x Rotten Flesh")
        ));
        
        creatureConfigs.put(CreatureType.SKELETON, new CreatureConfig(
            "Skeleton", "§fSkeleton", Material.SKELETON_SKULL,
            "§7A ranged undead creature.",
            CreatureRarity.COMMON, 12, Arrays.asList("§7- Ranged Attack", "§7- Bone Shards"),
            Arrays.asList("§7- 1x Skeleton Shard", "§7- 1x Bone")
        ));
        
        creatureConfigs.put(CreatureType.SPIDER, new CreatureConfig(
            "Spider", "§8Spider", Material.SPIDER_EYE,
            "§7A climbing arachnid creature.",
            CreatureRarity.COMMON, 8, Arrays.asList("§7- Climbing", "§7- Poison"),
            Arrays.asList("§7- 1x Spider Shard", "§7- 1x Spider Eye")
        ));
        
        creatureConfigs.put(CreatureType.CREEPER, new CreatureConfig(
            "Creeper", "§aCreeper", Material.CREEPER_HEAD,
            "§7An explosive creature.",
            CreatureRarity.UNCOMMON, 15, Arrays.asList("§7- Explosion", "§7- Gunpowder"),
            Arrays.asList("§7- 1x Creeper Shard", "§7- 1x Gunpowder")
        ));
        
        creatureConfigs.put(CreatureType.ENDERMAN, new CreatureConfig(
            "Enderman", "§5Enderman", Material.ENDERMAN_SPAWN_EGG,
            "§7A teleporting end creature.",
            CreatureRarity.RARE, 25, Arrays.asList("§7- Teleportation", "§7- Ender Pearls"),
            Arrays.asList("§7- 1x Enderman Shard", "§7- 1x Ender Pearl")
        ));
        
        creatureConfigs.put(CreatureType.WITHER_SKELETON, new CreatureConfig(
            "Wither Skeleton", "§8Wither Skeleton", Material.WITHER_SKELETON_SKULL,
            "§7A powerful undead creature.",
            CreatureRarity.EPIC, 35, Arrays.asList("§7- Wither Effect", "§7- Wither Bones"),
            Arrays.asList("§7- 1x Wither Skeleton Shard", "§7- 1x Wither Skeleton Skull")
        ));
        
        creatureConfigs.put(CreatureType.DRAGON, new CreatureConfig(
            "Dragon", "§6Dragon", Material.DRAGON_HEAD,
            "§7A legendary dragon creature.",
            CreatureRarity.LEGENDARY, 100, Arrays.asList("§7- Dragon Breath", "§7- Dragon Scales"),
            Arrays.asList("§7- 1x Dragon Shard", "§7- 1x Dragon Scale")
        ));
        
        creatureConfigs.put(CreatureType.WITHER, new CreatureConfig(
            "Wither", "§8Wither", Material.WITHER_SKELETON_SKULL,
            "§7A powerful boss creature.",
            CreatureRarity.MYTHIC, 150, Arrays.asList("§7- Wither Effect", "§7- Nether Star"),
            Arrays.asList("§7- 1x Wither Shard", "§7- 1x Nether Star")
        ));
    }
    
    private void initializeShardConfigs() {
        // Basic Shards
        shardConfigs.put(ShardType.ZOMBIE_SHARD, new ShardConfig(
            "Zombie Shard", "§2Zombie Shard", Material.ROTTEN_FLESH,
            "§7A shard from a zombie creature.",
            ShardRarity.COMMON, 1, Arrays.asList("§7- +1 Health", "§7- +0.5 Defense"),
            Arrays.asList("§7- 1x Zombie Shard")
        ));
        
        shardConfigs.put(ShardType.SKELETON_SHARD, new ShardConfig(
            "Skeleton Shard", "§fSkeleton Shard", Material.BONE,
            "§7A shard from a skeleton creature.",
            ShardRarity.COMMON, 1, Arrays.asList("§7- +1 Damage", "§7- +0.5 Crit Chance"),
            Arrays.asList("§7- 1x Skeleton Shard")
        ));
        
        shardConfigs.put(ShardType.SPIDER_SHARD, new ShardConfig(
            "Spider Shard", "§8Spider Shard", Material.SPIDER_EYE,
            "§7A shard from a spider creature.",
            ShardRarity.COMMON, 1, Arrays.asList("§7- +1 Speed", "§7- +0.5 Agility"),
            Arrays.asList("§7- 1x Spider Shard")
        ));
        
        shardConfigs.put(ShardType.CREEPER_SHARD, new ShardConfig(
            "Creeper Shard", "§aCreeper Shard", Material.GUNPOWDER,
            "§7A shard from a creeper creature.",
            ShardRarity.UNCOMMON, 2, Arrays.asList("§7- +2 Explosion Damage", "§7- +1 Blast Resistance"),
            Arrays.asList("§7- 1x Creeper Shard")
        ));
        
        shardConfigs.put(ShardType.ENDERMAN_SHARD, new ShardConfig(
            "Enderman Shard", "§5Enderman Shard", Material.ENDER_PEARL,
            "§7A shard from an enderman creature.",
            ShardRarity.RARE, 3, Arrays.asList("§7- +3 Teleportation", "§7- +1.5 Ender Resistance"),
            Arrays.asList("§7- 1x Enderman Shard")
        ));
        
        shardConfigs.put(ShardType.WITHER_SKELETON_SHARD, new ShardConfig(
            "Wither Skeleton Shard", "§8Wither Skeleton Shard", Material.WITHER_SKELETON_SKULL,
            "§7A shard from a wither skeleton creature.",
            ShardRarity.EPIC, 5, Arrays.asList("§7- +5 Wither Damage", "§7- +2.5 Wither Resistance"),
            Arrays.asList("§7- 1x Wither Skeleton Shard")
        ));
        
        shardConfigs.put(ShardType.DRAGON_SHARD, new ShardConfig(
            "Dragon Shard", "§6Dragon Shard", Material.DRAGON_EGG,
            "§7A shard from a dragon creature.",
            ShardRarity.LEGENDARY, 10, Arrays.asList("§7- +10 Dragon Power", "§7- +5 Dragon Resistance"),
            Arrays.asList("§7- 1x Dragon Shard")
        ));
        
        shardConfigs.put(ShardType.WITHER_SHARD, new ShardConfig(
            "Wither Shard", "§8Wither Shard", Material.NETHER_STAR,
            "§7A shard from a wither creature.",
            ShardRarity.MYTHIC, 15, Arrays.asList("§7- +15 Wither Power", "§7- +7.5 Wither Mastery"),
            Arrays.asList("§7- 1x Wither Shard")
        ));
    }
    
    private void startHuntingUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateAllPlayerHuntingData();
            }
        }.runTaskTimer(plugin, 0L, 20L * 60L); // Update every minute
    }
    
    private void updateAllPlayerHuntingData() {
        for (PlayerHuntingData data : playerHuntingData.values()) {
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
        
        if (displayName.contains("Hunting Rod")) {
            openHuntingGUI(player);
        }
    }
    
    public void openHuntingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§a§lHunting System");
        
        // Add creature categories
        addGUIItem(gui, 10, Material.ZOMBIE_HEAD, "§2§lBasic Creatures", "§7Hunt basic creatures.");
        addGUIItem(gui, 11, Material.SKELETON_SKULL, "§f§lUndead Creatures", "§7Hunt undead creatures.");
        addGUIItem(gui, 12, Material.SPIDER_EYE, "§8§lArachnid Creatures", "§7Hunt arachnid creatures.");
        addGUIItem(gui, 13, Material.CREEPER_HEAD, "§a§lExplosive Creatures", "§7Hunt explosive creatures.");
        addGUIItem(gui, 14, Material.ENDERMAN_SPAWN_EGG, "§5§lEnd Creatures", "§7Hunt end creatures.");
        addGUIItem(gui, 15, Material.WITHER_SKELETON_SKULL, "§8§lWither Creatures", "§7Hunt wither creatures.");
        addGUIItem(gui, 16, Material.DRAGON_HEAD, "§6§lDragon Creatures", "§7Hunt dragon creatures.");
        addGUIItem(gui, 17, Material.NETHER_STAR, "§8§lBoss Creatures", "§7Hunt boss creatures.");
        
        // Add shard management
        addGUIItem(gui, 19, Material.ROTTEN_FLESH, "§2§lZombie Shards", "§7Manage zombie shards.");
        addGUIItem(gui, 20, Material.BONE, "§f§lSkeleton Shards", "§7Manage skeleton shards.");
        addGUIItem(gui, 21, Material.SPIDER_EYE, "§8§lSpider Shards", "§7Manage spider shards.");
        addGUIItem(gui, 22, Material.GUNPOWDER, "§a§lCreeper Shards", "§7Manage creeper shards.");
        addGUIItem(gui, 23, Material.ENDER_PEARL, "§5§lEnderman Shards", "§7Manage enderman shards.");
        addGUIItem(gui, 24, Material.WITHER_SKELETON_SKULL, "§8§lWither Skeleton Shards", "§7Manage wither skeleton shards.");
        addGUIItem(gui, 25, Material.DRAGON_EGG, "§6§lDragon Shards", "§7Manage dragon shards.");
        addGUIItem(gui, 26, Material.NETHER_STAR, "§8§lWither Shards", "§7Manage wither shards.");
        
        // Add fusion system
        addGUIItem(gui, 28, Material.ANVIL, "§e§lFusion System", "§7Fuse shards to create new ones.");
        addGUIItem(gui, 29, Material.ENCHANTING_TABLE, "§b§lShard Enhancement", "§7Enhance your shards.");
        addGUIItem(gui, 30, Material.BEACON, "§6§lShard Mastery", "§7Master your shard collection.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the GUI.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aHunting GUI geöffnet!");
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
    
    public PlayerHuntingData getPlayerHuntingData(UUID playerId) {
        return playerHuntingData.computeIfAbsent(playerId, k -> new PlayerHuntingData(playerId));
    }
    
    public CreatureConfig getCreatureConfig(CreatureType type) {
        return creatureConfigs.get(type);
    }
    
    public ShardConfig getShardConfig(ShardType type) {
        return shardConfigs.get(type);
    }
    
    public List<CreatureType> getAllCreatureTypes() {
        return new ArrayList<>(creatureConfigs.keySet());
    }
    
    public List<ShardType> getAllShardTypes() {
        return new ArrayList<>(shardConfigs.keySet());
    }
    
    public enum CreatureType {
        ZOMBIE, SKELETON, SPIDER, CREEPER, ENDERMAN, WITHER_SKELETON, DRAGON, WITHER
    }
    
    public enum ShardType {
        ZOMBIE_SHARD, SKELETON_SHARD, SPIDER_SHARD, CREEPER_SHARD, ENDERMAN_SHARD, 
        WITHER_SKELETON_SHARD, DRAGON_SHARD, WITHER_SHARD
    }
    
    public enum CreatureRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        CreatureRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public enum ShardRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        ShardRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class CreatureConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final CreatureRarity rarity;
        private final int health;
        private final List<String> abilities;
        private final List<String> drops;
        
        public CreatureConfig(String name, String displayName, Material material, String description,
                            CreatureRarity rarity, int health, List<String> abilities, List<String> drops) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.rarity = rarity;
            this.health = health;
            this.abilities = abilities;
            this.drops = drops;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public CreatureRarity getRarity() { return rarity; }
        public int getHealth() { return health; }
        public List<String> getAbilities() { return abilities; }
        public List<String> getDrops() { return drops; }
    }
    
    public static class ShardConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final ShardRarity rarity;
        private final int power;
        private final List<String> effects;
        private final List<String> requirements;
        
        public ShardConfig(String name, String displayName, Material material, String description,
                          ShardRarity rarity, int power, List<String> effects, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.rarity = rarity;
            this.power = power;
            this.effects = effects;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public ShardRarity getRarity() { return rarity; }
        public int getPower() { return power; }
        public List<String> getEffects() { return effects; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerHuntingData {
        private final UUID playerId;
        private final Map<CreatureType, Integer> creaturesKilled = new HashMap<>();
        private final Map<ShardType, Integer> shardsCollected = new HashMap<>();
        private final Map<ShardType, Integer> shardLevels = new HashMap<>();
        private long lastUpdate;
        
        public PlayerHuntingData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addCreatureKill(CreatureType type) {
            creaturesKilled.put(type, creaturesKilled.getOrDefault(type, 0) + 1);
        }
        
        public void addShard(ShardType type, int amount) {
            shardsCollected.put(type, shardsCollected.getOrDefault(type, 0) + amount);
        }
        
        public void levelUpShard(ShardType type) {
            shardLevels.put(type, shardLevels.getOrDefault(type, 0) + 1);
        }
        
        public int getCreaturesKilled(CreatureType type) {
            return creaturesKilled.getOrDefault(type, 0);
        }
        
        public int getShardsCollected(ShardType type) {
            return shardsCollected.getOrDefault(type, 0);
        }
        
        public int getShardLevel(ShardType type) {
            return shardLevels.getOrDefault(type, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
