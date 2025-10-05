package de.noctivag.skyblock.combat;

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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Advanced Combat System - Hypixel Skyblock Style
 * Implements Critical Damage, Ferocity, Attack Speed, and Combat Collections
 */
public class AdvancedCombatSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCombatData> playerCombatData = new ConcurrentHashMap<>();
    private final Map<CombatLocation, CombatConfig> combatConfigs = new HashMap<>();
    private final Map<MobType, MobConfig> mobConfigs = new HashMap<>();
    
    public AdvancedCombatSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        initializeCombatConfigs();
        initializeMobConfigs();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeCombatConfigs() {
        // Spawn Island
        combatConfigs.put(CombatLocation.SPAWN, new CombatConfig(
            CombatLocation.SPAWN,
            "§aSpawn Island",
            "§7A peaceful combat spot",
            Material.GRASS_BLOCK,
            1,
            Arrays.asList(
                new CombatReward("Rotten Flesh", Material.ROTTEN_FLESH, 100, "§fRotten Flesh"),
                new CombatReward("Bone", Material.BONE, 150, "§fBone"),
                new CombatReward("String", Material.STRING, 200, "§fString")
            )
        ));
        
        // Spider's Den
        combatConfigs.put(CombatLocation.SPIDERS_DEN, new CombatConfig(
            CombatLocation.SPIDERS_DEN,
            "§8Spider's Den",
            "§7A dark combat spot",
            Material.COBWEB,
            2,
            Arrays.asList(
                new CombatReward("Rotten Flesh", Material.ROTTEN_FLESH, 200, "§fRotten Flesh"),
                new CombatReward("Bone", Material.BONE, 300, "§fBone"),
                new CombatReward("String", Material.STRING, 400, "§fString"),
                new CombatReward("Spider Eye", Material.SPIDER_EYE, 500, "§fSpider Eye")
            )
        ));
        
        // Deep Caverns
        combatConfigs.put(CombatLocation.DEEP_CAVERNS, new CombatConfig(
            CombatLocation.DEEP_CAVERNS,
            "§7Deep Caverns",
            "§7A deep combat spot",
            Material.STONE,
            3,
            Arrays.asList(
                new CombatReward("Rotten Flesh", Material.ROTTEN_FLESH, 300, "§fRotten Flesh"),
                new CombatReward("Bone", Material.BONE, 450, "§fBone"),
                new CombatReward("String", Material.STRING, 600, "§fString"),
                new CombatReward("Spider Eye", Material.SPIDER_EYE, 750, "§fSpider Eye"),
                new CombatReward("Gunpowder", Material.GUNPOWDER, 1000, "§fGunpowder")
            )
        ));
        
        // The End
        combatConfigs.put(CombatLocation.THE_END, new CombatConfig(
            CombatLocation.THE_END,
            "§5The End",
            "§7An end combat spot",
            Material.END_STONE,
            4,
            Arrays.asList(
                new CombatReward("Ender Pearl", Material.ENDER_PEARL, 500, "§5Ender Pearl"),
                new CombatReward("Ender Eye", Material.ENDER_EYE, 750, "§5Ender Eye"),
                new CombatReward("Obsidian", Material.OBSIDIAN, 1000, "§5Obsidian")
            )
        ));
        
        // The Nether
        combatConfigs.put(CombatLocation.THE_NETHER, new CombatConfig(
            CombatLocation.THE_NETHER,
            "§cThe Nether",
            "§7A nether combat spot",
            Material.NETHERRACK,
            5,
            Arrays.asList(
                new CombatReward("Blaze Rod", Material.BLAZE_ROD, 600, "§eBlaze Rod"),
                new CombatReward("Ghast Tear", Material.GHAST_TEAR, 800, "§fGhast Tear"),
                new CombatReward("Nether Star", Material.NETHER_STAR, 1500, "§dNether Star")
            )
        ));
    }
    
    private void initializeMobConfigs() {
        // Zombie
        mobConfigs.put(MobType.ZOMBIE, new MobConfig(
            MobType.ZOMBIE,
            "§2Zombie",
            "§7A common undead mob",
            Material.ZOMBIE_HEAD,
            100,
            10,
            Arrays.asList("Rotten Flesh", "Zombie Head")
        ));
        
        // Skeleton
        mobConfigs.put(MobType.SKELETON, new MobConfig(
            MobType.SKELETON,
            "§fSkeleton",
            "§7A common undead mob",
            Material.SKELETON_SKULL,
            100,
            10,
            Arrays.asList("Bone", "Skeleton Skull")
        ));
        
        // Spider
        mobConfigs.put(MobType.SPIDER, new MobConfig(
            MobType.SPIDER,
            "§8Spider",
            "§7A common arachnid mob",
            Material.SPIDER_EYE,
            100,
            10,
            Arrays.asList("String", "Spider Eye")
        ));
        
        // Creeper
        mobConfigs.put(MobType.CREEPER, new MobConfig(
            MobType.CREEPER,
            "§aCreeper",
            "§7A common explosive mob",
            Material.GUNPOWDER,
            100,
            10,
            Arrays.asList("Gunpowder", "Creeper Head")
        ));
        
        // Enderman
        mobConfigs.put(MobType.ENDERMAN, new MobConfig(
            MobType.ENDERMAN,
            "§5Enderman",
            "§7A common end mob",
            Material.ENDER_PEARL,
            200,
            20,
            Arrays.asList("Ender Pearl", "Ender Eye")
        ));
        
        // Blaze
        mobConfigs.put(MobType.BLAZE, new MobConfig(
            MobType.BLAZE,
            "§eBlaze",
            "§7A common nether mob",
            Material.BLAZE_ROD,
            200,
            20,
            Arrays.asList("Blaze Rod", "Blaze Powder")
        ));
        
        // Ghast
        mobConfigs.put(MobType.GHAST, new MobConfig(
            MobType.GHAST,
            "§fGhast",
            "§7A common nether mob",
            Material.GHAST_TEAR,
            200,
            20,
            Arrays.asList("Ghast Tear", "Ghast Head")
        ));
        
        // Wither Skeleton
        mobConfigs.put(MobType.WITHER_SKELETON, new MobConfig(
            MobType.WITHER_SKELETON,
            "§8Wither Skeleton",
            "§7A rare nether mob",
            Material.WITHER_SKELETON_SKULL,
            300,
            30,
            Arrays.asList("Wither Skeleton Skull", "Wither Bone")
        ));
        
        // Wither
        mobConfigs.put(MobType.WITHER, new MobConfig(
            MobType.WITHER,
            "§4Wither",
            "§7A boss nether mob",
            Material.NETHER_STAR,
            500,
            50,
            Arrays.asList("Nether Star", "Wither Head")
        ));
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            PlayerCombatData data = getPlayerCombatData(player.getUniqueId());
            
            // Calculate critical damage
            double criticalChance = calculateCriticalChance(data);
            double criticalMultiplier = calculateCriticalMultiplier(data);
            
            if (Math.random() < criticalChance) {
                double damage = event.getDamage();
                double criticalDamage = damage * criticalMultiplier;
                event.setDamage(criticalDamage);
                
                player.sendMessage("§cCritical Hit! " + String.format("%.1f", criticalDamage) + " damage!");
            }
            
            // Calculate ferocity
            double ferocityChance = calculateFerocityChance(data);
            if (Math.random() < ferocityChance) {
                // Ferocity hit - deal additional damage
                double ferocityDamage = event.getDamage() * 0.5;
                event.setDamage(event.getDamage() + ferocityDamage);
                
                player.sendMessage("§dFerocity Hit! " + String.format("%.1f", ferocityDamage) + " additional damage!");
            }
            
            // Add combat XP
            int xp = calculateCombatXP(player, data);
            data.addCombatXP(xp);
            
            player.sendMessage("§aDealt " + String.format("%.1f", event.getDamage()) + " damage! +" + xp + " Combat XP");
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
        
        if (displayName.contains("Combat") || displayName.contains("combat")) {
            openCombatGUI(player);
        }
    }
    
    public void openCombatGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§c§lCombat System"));
        
        // Add combat locations
        addGUIItem(gui, 10, Material.GRASS_BLOCK, "§a§lSpawn Island", "§7A peaceful combat spot");
        addGUIItem(gui, 11, Material.COBWEB, "§8§lSpider's Den", "§7A dark combat spot");
        addGUIItem(gui, 12, Material.STONE, "§7§lDeep Caverns", "§7A deep combat spot");
        addGUIItem(gui, 13, Material.END_STONE, "§5§lThe End", "§7An end combat spot");
        addGUIItem(gui, 14, Material.NETHERRACK, "§c§lThe Nether", "§7A nether combat spot");
        
        // Add combat management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Combat Progress", "§7View your combat progress.");
        addGUIItem(gui, 19, Material.DIAMOND_SWORD, "§c§lCombat Weapons", "§7View available combat weapons.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lCombat Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lCombat Shop", "§7Buy combat items.");
        addGUIItem(gui, 22, Material.GOLD_INGOT, "§6§lCombat Contests", "§7Join combat contests.");
        
        // Add mob types
        addGUIItem(gui, 27, Material.ZOMBIE_HEAD, "§2§lZombie", "§7A common undead mob");
        addGUIItem(gui, 28, Material.SKELETON_SKULL, "§f§lSkeleton", "§7A common undead mob");
        addGUIItem(gui, 29, Material.SPIDER_EYE, "§8§lSpider", "§7A common arachnid mob");
        addGUIItem(gui, 30, Material.GUNPOWDER, "§a§lCreeper", "§7A common explosive mob");
        addGUIItem(gui, 31, Material.ENDER_PEARL, "§5§lEnderman", "§7A common end mob");
        addGUIItem(gui, 32, Material.BLAZE_ROD, "§e§lBlaze", "§7A common nether mob");
        addGUIItem(gui, 33, Material.GHAST_TEAR, "§f§lGhast", "§7A common nether mob");
        addGUIItem(gui, 34, Material.WITHER_SKELETON_SKULL, "§8§lWither Skeleton", "§7A rare nether mob");
        addGUIItem(gui, 35, Material.NETHER_STAR, "§4§lWither", "§7A boss nether mob");
        
        // Add special items
        addGUIItem(gui, 36, Material.DIAMOND_SWORD, "§6§lDiamond Sword", "§7A diamond sword");
        addGUIItem(gui, 37, Material.GOLDEN_SWORD, "§e§lGolden Sword", "§7A golden sword");
        addGUIItem(gui, 38, Material.IRON_SWORD, "§7§lIron Sword", "§7An iron sword");
        addGUIItem(gui, 39, Material.STONE_SWORD, "§8§lStone Sword", "§7A stone sword");
        addGUIItem(gui, 40, Material.WOODEN_SWORD, "§6§lWooden Sword", "§7A wooden sword");
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the combat menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aCombat GUI opened!"));
    }
    
    private double calculateCriticalChance(PlayerCombatData data) {
        int level = data.getCombatLevel();
        double baseChance = 0.1; // 10% base critical chance
        double levelBonus = level * 0.01; // 1% per level
        
        return Math.min(baseChance + levelBonus, 0.5); // Max 50% critical chance
    }
    
    private double calculateCriticalMultiplier(PlayerCombatData data) {
        int level = data.getCombatLevel();
        double baseMultiplier = 1.5; // 150% base critical damage
        double levelBonus = level * 0.05; // 5% per level
        
        return baseMultiplier + levelBonus;
    }
    
    private double calculateFerocityChance(PlayerCombatData data) {
        int level = data.getCombatLevel();
        double baseChance = 0.05; // 5% base ferocity chance
        double levelBonus = level * 0.005; // 0.5% per level
        
        return Math.min(baseChance + levelBonus, 0.25); // Max 25% ferocity chance
    }
    
    private int calculateCombatXP(Player player, PlayerCombatData data) {
        int baseXP = 10;
        int level = data.getCombatLevel();
        int xpMultiplier = 1 + (level / 10);
        
        return baseXP * xpMultiplier;
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
    
    public PlayerCombatData getPlayerCombatData(UUID playerId) {
        return playerCombatData.computeIfAbsent(playerId, k -> new PlayerCombatData(playerId));
    }
    
    public enum CombatLocation {
        SPAWN("§aSpawn Island", "§7A peaceful combat spot", 1),
        SPIDERS_DEN("§8Spider's Den", "§7A dark combat spot", 2),
        DEEP_CAVERNS("§7Deep Caverns", "§7A deep combat spot", 3),
        THE_END("§5The End", "§7An end combat spot", 4),
        THE_NETHER("§cThe Nether", "§7A nether combat spot", 5);
        
        private final String displayName;
        private final String description;
        private final int requiredLevel;
        
        CombatLocation(String displayName, String description, int requiredLevel) {
            this.displayName = displayName;
            this.description = description;
            this.requiredLevel = requiredLevel;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public int getRequiredLevel() { return requiredLevel; }
    }
    
    public enum MobType {
        ZOMBIE("§2Zombie", "§7A common undead mob"),
        SKELETON("§fSkeleton", "§7A common undead mob"),
        SPIDER("§8Spider", "§7A common arachnid mob"),
        CREEPER("§aCreeper", "§7A common explosive mob"),
        ENDERMAN("§5Enderman", "§7A common end mob"),
        BLAZE("§eBlaze", "§7A common nether mob"),
        GHAST("§fGhast", "§7A common nether mob"),
        WITHER_SKELETON("§8Wither Skeleton", "§7A rare nether mob"),
        WITHER("§4Wither", "§7A boss nether mob");
        
        private final String displayName;
        private final String description;
        
        MobType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class CombatConfig {
        private final CombatLocation location;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<CombatReward> rewards;
        
        public CombatConfig(CombatLocation location, String displayName, String description, Material icon,
                          int requiredLevel, List<CombatReward> rewards) {
            this.location = location;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.rewards = rewards;
        }
        
        public CombatLocation getLocation() { return location; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<CombatReward> getRewards() { return rewards; }
    }
    
    public static class CombatReward {
        private final String name;
        private final Material material;
        private final int cost;
        private final String displayName;
        
        public CombatReward(String name, Material material, int cost, String displayName) {
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
    
    public static class MobConfig {
        private final MobType type;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int health;
        private final int requiredLevel;
        private final List<String> drops;
        
        public MobConfig(MobType type, String displayName, String description, Material icon,
                        int health, int requiredLevel, List<String> drops) {
            this.type = type;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.health = health;
            this.requiredLevel = requiredLevel;
            this.drops = drops;
        }
        
        public MobType getType() { return type; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getHealth() { return health; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<String> getDrops() { return drops; }
    }
    
    public static class PlayerCombatData {
        private final UUID playerId;
        private int combatLevel;
        private int combatXP;
        private final Map<CombatLocation, Integer> locationStats = new HashMap<>();
        private final Map<MobType, Integer> mobStats = new HashMap<>();
        
        public PlayerCombatData(UUID playerId) {
            this.playerId = playerId;
            this.combatLevel = 1;
            this.combatXP = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getCombatLevel() { return combatLevel; }
        public int getCombatXP() { return combatXP; }
        public int getLocationStats(CombatLocation location) { return locationStats.getOrDefault(location, 0); }
        public int getMobStats(MobType type) { return mobStats.getOrDefault(type, 0); }
        
        public void addCombatXP(int xp) {
            this.combatXP += xp;
            checkLevelUp();
        }
        
        public void addLocationStat(CombatLocation location) {
            locationStats.put(location, locationStats.getOrDefault(location, 0) + 1);
        }
        
        public void addMobStat(MobType type) {
            mobStats.put(type, mobStats.getOrDefault(type, 0) + 1);
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(combatLevel + 1);
            if (combatXP >= requiredXP) {
                combatLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
}
