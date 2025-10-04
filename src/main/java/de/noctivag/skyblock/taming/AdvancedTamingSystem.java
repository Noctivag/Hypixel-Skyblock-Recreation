package de.noctivag.skyblock.taming;
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Taming System - Hypixel Skyblock Style
 * Implements Pet Evolution, Pet Abilities, and Pet Collections
 */
public class AdvancedTamingSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerTamingData> playerTamingData = new ConcurrentHashMap<>();
    private final Map<PetType, PetConfig> petConfigs = new HashMap<>();
    private final Map<TamingLocation, TamingConfig> tamingConfigs = new HashMap<>();
    
    public AdvancedTamingSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializePetConfigs();
        initializeTamingConfigs();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializePetConfigs() {
        // Combat Pets
        petConfigs.put(PetType.ZOMBIE, new PetConfig(
            PetType.ZOMBIE,
            "§2Zombie Pet",
            "§7A combat pet that increases damage",
            Material.ZOMBIE_HEAD,
            PetCategory.COMBAT,
            1,
            Arrays.asList("Increases melee damage", "Increases health", "Increases defense")
        ));
        
        petConfigs.put(PetType.SKELETON, new PetConfig(
            PetType.SKELETON,
            "§fSkeleton Pet",
            "§7A combat pet that increases ranged damage",
            Material.SKELETON_SKULL,
            PetCategory.COMBAT,
            1,
            Arrays.asList("Increases ranged damage", "Increases accuracy", "Increases critical chance")
        ));
        
        petConfigs.put(PetType.SPIDER, new PetConfig(
            PetType.SPIDER,
            "§8Spider Pet",
            "§7A combat pet that increases speed",
            Material.SPIDER_EYE,
            PetCategory.COMBAT,
            1,
            Arrays.asList("Increases movement speed", "Increases attack speed", "Increases dodge chance")
        ));
        
        petConfigs.put(PetType.ENDERMAN, new PetConfig(
            PetType.ENDERMAN,
            "§5Enderman Pet",
            "§7A combat pet that increases teleportation",
            Material.ENDER_PEARL,
            PetCategory.COMBAT,
            2,
            Arrays.asList("Increases teleportation range", "Increases ender pearl damage", "Increases end resistance")
        ));
        
        petConfigs.put(PetType.BLAZE, new PetConfig(
            PetType.BLAZE,
            "§eBlaze Pet",
            "§7A combat pet that increases fire damage",
            Material.BLAZE_ROD,
            PetCategory.COMBAT,
            2,
            Arrays.asList("Increases fire damage", "Increases fire resistance", "Increases blaze rod damage")
        ));
        
        // Mining Pets
        petConfigs.put(PetType.SILVERFISH, new PetConfig(
            PetType.SILVERFISH,
            "§7Silverfish Pet",
            "§7A mining pet that increases mining speed",
            Material.SILVERFISH_SPAWN_EGG,
            PetCategory.MINING,
            1,
            Arrays.asList("Increases mining speed", "Increases mining XP", "Increases ore drops")
        ));
        
        petConfigs.put(PetType.ROCK, new PetConfig(
            PetType.ROCK,
            "§8Rock Pet",
            "§7A mining pet that increases mining damage",
            Material.STONE,
            PetCategory.MINING,
            1,
            Arrays.asList("Increases mining damage", "Increases stone drops", "Increases cobblestone drops")
        ));
        
        petConfigs.put(PetType.GOLEM, new PetConfig(
            PetType.GOLEM,
            "§7Golem Pet",
            "§7A mining pet that increases mining defense",
            Material.IRON_INGOT,
            PetCategory.MINING,
            2,
            Arrays.asList("Increases mining defense", "Increases iron drops", "Increases golem damage")
        ));
        
        // Farming Pets
        petConfigs.put(PetType.RABBIT, new PetConfig(
            PetType.RABBIT,
            "§fRabbit Pet",
            "§7A farming pet that increases farming speed",
            Material.RABBIT_FOOT,
            PetCategory.FARMING,
            1,
            Arrays.asList("Increases farming speed", "Increases farming XP", "Increases crop drops")
        ));
        
        petConfigs.put(PetType.ELEPHANT, new PetConfig(
            PetType.ELEPHANT,
            "§7Elephant Pet",
            "§7A farming pet that increases farming damage",
            Material.LEATHER,
            PetCategory.FARMING,
            1,
            Arrays.asList("Increases farming damage", "Increases leather drops", "Increases elephant damage")
        ));
        
        petConfigs.put(PetType.MOOSHROOM, new PetConfig(
            PetType.MOOSHROOM,
            "§dMooshroom Pet",
            "§7A farming pet that increases mushroom drops",
            Material.RED_MUSHROOM,
            PetCategory.FARMING,
            2,
            Arrays.asList("Increases mushroom drops", "Increases mushroom farming", "Increases mooshroom damage")
        ));
        
        petConfigs.put(PetType.BEE, new PetConfig(
            PetType.BEE,
            "§eBee Pet",
            "§7A farming pet that increases honey drops",
            Material.HONEYCOMB,
            PetCategory.FARMING,
            2,
            Arrays.asList("Increases honey drops", "Increases bee farming", "Increases bee damage")
        ));
        
        // Foraging Pets
        petConfigs.put(PetType.OCELOT, new PetConfig(
            PetType.OCELOT,
            "§6Ocelot Pet",
            "§7A foraging pet that increases foraging speed",
            Material.OCELOT_SPAWN_EGG,
            PetCategory.FORAGING,
            1,
            Arrays.asList("Increases foraging speed", "Increases foraging XP", "Increases wood drops")
        ));
        
        petConfigs.put(PetType.MONKEY, new PetConfig(
            PetType.MONKEY,
            "§6Monkey Pet",
            "§7A foraging pet that increases foraging damage",
            Material.COCOA_BEANS,
            PetCategory.FORAGING,
            1,
            Arrays.asList("Increases foraging damage", "Increases cocoa drops", "Increases monkey damage")
        ));
        
        petConfigs.put(PetType.LION, new PetConfig(
            PetType.LION,
            "§eLion Pet",
            "§7A foraging pet that increases foraging defense",
            Material.GOLDEN_APPLE,
            PetCategory.FORAGING,
            2,
            Arrays.asList("Increases foraging defense", "Increases golden apple drops", "Increases lion damage")
        ));
        
        petConfigs.put(PetType.GIRAFFE, new PetConfig(
            PetType.GIRAFFE,
            "§6Giraffe Pet",
            "§7A foraging pet that increases foraging range",
            Material.ACACIA_LOG,
            PetCategory.FORAGING,
            2,
            Arrays.asList("Increases foraging range", "Increases acacia drops", "Increases giraffe damage")
        ));
        
        // Fishing Pets
        petConfigs.put(PetType.SQUID, new PetConfig(
            PetType.SQUID,
            "§bSquid Pet",
            "§7A fishing pet that increases fishing speed",
            Material.SQUID_SPAWN_EGG,
            PetCategory.FISHING,
            1,
            Arrays.asList("Increases fishing speed", "Increases fishing XP", "Increases fish drops")
        ));
        
        petConfigs.put(PetType.DOLPHIN, new PetConfig(
            PetType.DOLPHIN,
            "§bDolphin Pet",
            "§7A fishing pet that increases fishing damage",
            Material.DOLPHIN_SPAWN_EGG,
            PetCategory.FISHING,
            1,
            Arrays.asList("Increases fishing damage", "Increases dolphin drops", "Increases dolphin damage")
        ));
        
        petConfigs.put(PetType.GUARDIAN, new PetConfig(
            PetType.GUARDIAN,
            "§bGuardian Pet",
            "§7A fishing pet that increases fishing defense",
            Material.GUARDIAN_SPAWN_EGG,
            PetCategory.FISHING,
            2,
            Arrays.asList("Increases fishing defense", "Increases guardian drops", "Increases guardian damage")
        ));
        
        petConfigs.put(PetType.MEGALODON, new PetConfig(
            PetType.MEGALODON,
            "§8Megalodon Pet",
            "§7A fishing pet that increases fishing range",
            Material.PRISMARINE_SHARD,
            PetCategory.FISHING,
            2,
            Arrays.asList("Increases fishing range", "Increases prismarine drops", "Increases megalodon damage")
        ));
    }
    
    private void initializeTamingConfigs() {
        // Spawn Island
        tamingConfigs.put(TamingLocation.SPAWN, new TamingConfig(
            TamingLocation.SPAWN,
            "§aSpawn Island",
            "§7A peaceful taming spot",
            Material.GRASS_BLOCK,
            1,
            Arrays.asList(
                new TamingReward("Pet Food", Material.BREAD, 100, "§6Pet Food"),
                new TamingReward("Pet Treat", Material.COOKIE, 200, "§ePet Treat"),
                new TamingReward("Pet Toy", Material.STICK, 300, "§aPet Toy")
            )
        ));
        
        // Deep Caverns
        tamingConfigs.put(TamingLocation.DEEP_CAVERNS, new TamingConfig(
            TamingLocation.DEEP_CAVERNS,
            "§7Deep Caverns",
            "§7A deep taming spot",
            Material.STONE,
            2,
            Arrays.asList(
                new TamingReward("Pet Food", Material.BREAD, 200, "§6Pet Food"),
                new TamingReward("Pet Treat", Material.COOKIE, 400, "§ePet Treat"),
                new TamingReward("Pet Toy", Material.STICK, 600, "§aPet Toy"),
                new TamingReward("Pet Armor", Material.LEATHER_CHESTPLATE, 800, "§7Pet Armor")
            )
        ));
        
        // The End
        tamingConfigs.put(TamingLocation.THE_END, new TamingConfig(
            TamingLocation.THE_END,
            "§5The End",
            "§7An end taming spot",
            Material.END_STONE,
            3,
            Arrays.asList(
                new TamingReward("Pet Food", Material.BREAD, 300, "§6Pet Food"),
                new TamingReward("Pet Treat", Material.COOKIE, 600, "§ePet Treat"),
                new TamingReward("Pet Toy", Material.STICK, 900, "§aPet Toy"),
                new TamingReward("Pet Armor", Material.LEATHER_CHESTPLATE, 1200, "§7Pet Armor"),
                new TamingReward("Pet Upgrade", Material.NETHER_STAR, 1500, "§dPet Upgrade")
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
        
        if (displayName.contains("Taming") || displayName.contains("taming")) {
            openTamingGUI(player);
        }
    }
    
    public void openTamingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lTaming System"));
        
        // Add taming locations
        addGUIItem(gui, 10, Material.GRASS_BLOCK, "§a§lSpawn Island", "§7A peaceful taming spot");
        addGUIItem(gui, 11, Material.STONE, "§7§lDeep Caverns", "§7A deep taming spot");
        addGUIItem(gui, 12, Material.END_STONE, "§5§lThe End", "§7An end taming spot");
        
        // Add taming management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Taming Progress", "§7View your taming progress.");
        addGUIItem(gui, 19, Material.LEAD, "§6§lMy Pets", "§7View your pets.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lTaming Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lTaming Shop", "§7Buy taming items.");
        addGUIItem(gui, 22, Material.GOLD_INGOT, "§6§lTaming Contests", "§7Join taming contests.");
        
        // Add combat pets
        addGUIItem(gui, 27, Material.ZOMBIE_HEAD, "§2§lZombie Pet", "§7A combat pet that increases damage");
        addGUIItem(gui, 28, Material.SKELETON_SKULL, "§f§lSkeleton Pet", "§7A combat pet that increases ranged damage");
        addGUIItem(gui, 29, Material.SPIDER_EYE, "§8§lSpider Pet", "§7A combat pet that increases speed");
        addGUIItem(gui, 30, Material.ENDER_PEARL, "§5§lEnderman Pet", "§7A combat pet that increases teleportation");
        addGUIItem(gui, 31, Material.BLAZE_ROD, "§e§lBlaze Pet", "§7A combat pet that increases fire damage");
        
        // Add mining pets
        addGUIItem(gui, 36, Material.SILVERFISH_SPAWN_EGG, "§7§lSilverfish Pet", "§7A mining pet that increases mining speed");
        addGUIItem(gui, 37, Material.STONE, "§8§lRock Pet", "§7A mining pet that increases mining damage");
        addGUIItem(gui, 38, Material.IRON_INGOT, "§7§lGolem Pet", "§7A mining pet that increases mining defense");
        
        // Add farming pets
        addGUIItem(gui, 45, Material.RABBIT_FOOT, "§f§lRabbit Pet", "§7A farming pet that increases farming speed");
        addGUIItem(gui, 46, Material.LEATHER, "§7§lElephant Pet", "§7A farming pet that increases farming damage");
        addGUIItem(gui, 47, Material.RED_MUSHROOM, "§d§lMooshroom Pet", "§7A farming pet that increases mushroom drops");
        addGUIItem(gui, 48, Material.HONEYCOMB, "§e§lBee Pet", "§7A farming pet that increases honey drops");
        
        // Add foraging pets
        addGUIItem(gui, 49, Material.OCELOT_SPAWN_EGG, "§6§lOcelot Pet", "§7A foraging pet that increases foraging speed");
        addGUIItem(gui, 50, Material.COCOA_BEANS, "§6§lMonkey Pet", "§7A foraging pet that increases foraging damage");
        addGUIItem(gui, 51, Material.GOLDEN_APPLE, "§e§lLion Pet", "§7A foraging pet that increases foraging defense");
        addGUIItem(gui, 52, Material.ACACIA_LOG, "§6§lGiraffe Pet", "§7A foraging pet that increases foraging range");
        
        // Add fishing pets
        addGUIItem(gui, 53, Material.SQUID_SPAWN_EGG, "§b§lSquid Pet", "§7A fishing pet that increases fishing speed");
        
        player.openInventory(gui);
        player.sendMessage("§aTaming GUI opened!");
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
    
    public PlayerTamingData getPlayerTamingData(UUID playerId) {
        return playerTamingData.computeIfAbsent(playerId, k -> new PlayerTamingData(playerId));
    }
    
    public enum PetType {
        ZOMBIE("§2Zombie Pet", "§7A combat pet that increases damage"),
        SKELETON("§fSkeleton Pet", "§7A combat pet that increases ranged damage"),
        SPIDER("§8Spider Pet", "§7A combat pet that increases speed"),
        ENDERMAN("§5Enderman Pet", "§7A combat pet that increases teleportation"),
        BLAZE("§eBlaze Pet", "§7A combat pet that increases fire damage"),
        SILVERFISH("§7Silverfish Pet", "§7A mining pet that increases mining speed"),
        ROCK("§8Rock Pet", "§7A mining pet that increases mining damage"),
        GOLEM("§7Golem Pet", "§7A mining pet that increases mining defense"),
        RABBIT("§fRabbit Pet", "§7A farming pet that increases farming speed"),
        ELEPHANT("§7Elephant Pet", "§7A farming pet that increases farming damage"),
        MOOSHROOM("§dMooshroom Pet", "§7A farming pet that increases mushroom drops"),
        BEE("§eBee Pet", "§7A farming pet that increases honey drops"),
        OCELOT("§6Ocelot Pet", "§7A foraging pet that increases foraging speed"),
        MONKEY("§6Monkey Pet", "§7A foraging pet that increases foraging damage"),
        LION("§eLion Pet", "§7A foraging pet that increases foraging defense"),
        GIRAFFE("§6Giraffe Pet", "§7A foraging pet that increases foraging range"),
        SQUID("§bSquid Pet", "§7A fishing pet that increases fishing speed"),
        DOLPHIN("§bDolphin Pet", "§7A fishing pet that increases fishing damage"),
        GUARDIAN("§bGuardian Pet", "§7A fishing pet that increases fishing defense"),
        MEGALODON("§8Megalodon Pet", "§7A fishing pet that increases fishing range");
        
        private final String displayName;
        private final String description;
        
        PetType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum PetCategory {
        COMBAT("§cCombat", "§7Combat pets"),
        MINING("§7Mining", "§7Mining pets"),
        FARMING("§aFarming", "§7Farming pets"),
        FORAGING("§6Foraging", "§7Foraging pets"),
        FISHING("§bFishing", "§7Fishing pets");
        
        private final String displayName;
        private final String description;
        
        PetCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum TamingLocation {
        SPAWN("§aSpawn Island", "§7A peaceful taming spot"),
        DEEP_CAVERNS("§7Deep Caverns", "§7A deep taming spot"),
        THE_END("§5The End", "§7An end taming spot");
        
        private final String displayName;
        private final String description;
        
        TamingLocation(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class PetConfig {
        private final PetType type;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final PetCategory category;
        private final int requiredLevel;
        private final List<String> abilities;
        
        public PetConfig(PetType type, String displayName, String description, Material icon,
                        PetCategory category, int requiredLevel, List<String> abilities) {
            this.type = type;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.category = category;
            this.requiredLevel = requiredLevel;
            this.abilities = abilities;
        }
        
        public PetType getType() { return type; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public PetCategory getCategory() { return category; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<String> getAbilities() { return abilities; }
    }
    
    public static class TamingConfig {
        private final TamingLocation location;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<TamingReward> rewards;
        
        public TamingConfig(TamingLocation location, String displayName, String description, Material icon,
                           int requiredLevel, List<TamingReward> rewards) {
            this.location = location;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.rewards = rewards;
        }
        
        public TamingLocation getLocation() { return location; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<TamingReward> getRewards() { return rewards; }
    }
    
    public static class TamingReward {
        private final String name;
        private final Material material;
        private final int cost;
        private final String displayName;
        
        public TamingReward(String name, Material material, int cost, String displayName) {
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
    
    public static class PlayerTamingData {
        private final UUID playerId;
        private int tamingLevel;
        private int tamingXP;
        private final Map<TamingLocation, Integer> locationStats = new HashMap<>();
        private final Map<PetType, Integer> petStats = new HashMap<>();
        
        public PlayerTamingData(UUID playerId) {
            this.playerId = playerId;
            this.tamingLevel = 1;
            this.tamingXP = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getTamingLevel() { return tamingLevel; }
        public int getTamingXP() { return tamingXP; }
        public int getLocationStats(TamingLocation location) { return locationStats.getOrDefault(location, 0); }
        public int getPetStats(PetType type) { return petStats.getOrDefault(type, 0); }
        
        public void addTamingXP(int xp) {
            this.tamingXP += xp;
            checkLevelUp();
        }
        
        public void addLocationStat(TamingLocation location) {
            locationStats.put(location, locationStats.getOrDefault(location, 0) + 1);
        }
        
        public void addPetStat(PetType type) {
            petStats.put(type, petStats.getOrDefault(type, 0) + 1);
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(tamingLevel + 1);
            if (tamingXP >= requiredXP) {
                tamingLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
}
