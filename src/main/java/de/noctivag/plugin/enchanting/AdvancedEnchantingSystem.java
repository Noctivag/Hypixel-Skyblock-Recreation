package de.noctivag.plugin.enchanting;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Enchanting System - Hypixel Skyblock Style
 * Implements Enchanting Table upgrades, Enchanting Collections, and special enchantments
 */
public class AdvancedEnchantingSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerEnchantingData> playerEnchantingData = new ConcurrentHashMap<>();
    private final Map<EnchantmentType, EnchantmentConfig> enchantmentConfigs = new HashMap<>();
    private final Map<EnchantingLocation, EnchantingConfig> enchantingConfigs = new HashMap<>();
    
    public AdvancedEnchantingSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeEnchantmentConfigs();
        initializeEnchantingConfigs();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeEnchantmentConfigs() {
        // Sharpness
        enchantmentConfigs.put(EnchantmentType.SHARPNESS, new EnchantmentConfig(
            EnchantmentType.SHARPNESS,
            "§cSharpness",
            "§7Increases melee damage",
            Material.DIAMOND_SWORD,
            1,
            Arrays.asList("Sharpness I", "Sharpness II", "Sharpness III", "Sharpness IV", "Sharpness V")
        ));
        
        // Protection
        enchantmentConfigs.put(EnchantmentType.PROTECTION, new EnchantmentConfig(
            EnchantmentType.PROTECTION,
            "§aProtection",
            "§7Reduces damage from all sources",
            Material.DIAMOND_CHESTPLATE,
            1,
            Arrays.asList("Protection I", "Protection II", "Protection III", "Protection IV")
        ));
        
        // Efficiency
        enchantmentConfigs.put(EnchantmentType.EFFICIENCY, new EnchantmentConfig(
            EnchantmentType.EFFICIENCY,
            "§eEfficiency",
            "§7Increases mining speed",
            Material.DIAMOND_PICKAXE,
            1,
            Arrays.asList("Efficiency I", "Efficiency II", "Efficiency III", "Efficiency IV", "Efficiency V")
        ));
        
        // Fortune
        enchantmentConfigs.put(EnchantmentType.FORTUNE, new EnchantmentConfig(
            EnchantmentType.FORTUNE,
            "§6Fortune",
            "§7Increases block drops",
            Material.DIAMOND_PICKAXE,
            2,
            Arrays.asList("Fortune I", "Fortune II", "Fortune III")
        ));
        
        // Looting
        enchantmentConfigs.put(EnchantmentType.LOOTING, new EnchantmentConfig(
            EnchantmentType.LOOTING,
            "§dLooting",
            "§7Increases mob drops",
            Material.DIAMOND_SWORD,
            2,
            Arrays.asList("Looting I", "Looting II", "Looting III")
        ));
        
        // Unbreaking
        enchantmentConfigs.put(EnchantmentType.UNBREAKING, new EnchantmentConfig(
            EnchantmentType.UNBREAKING,
            "§7Unbreaking",
            "§7Increases item durability",
            Material.DIAMOND_SWORD,
            1,
            Arrays.asList("Unbreaking I", "Unbreaking II", "Unbreaking III")
        ));
        
        // Mending
        enchantmentConfigs.put(EnchantmentType.MENDING, new EnchantmentConfig(
            EnchantmentType.MENDING,
            "§aMending",
            "§7Repairs items with XP",
            Material.DIAMOND_SWORD,
            3,
            Arrays.asList("Mending")
        ));
        
        // Fire Aspect
        enchantmentConfigs.put(EnchantmentType.FIRE_ASPECT, new EnchantmentConfig(
            EnchantmentType.FIRE_ASPECT,
            "§cFire Aspect",
            "§7Sets targets on fire",
            Material.DIAMOND_SWORD,
            2,
            Arrays.asList("Fire Aspect I", "Fire Aspect II")
        ));
        
        // Knockback
        enchantmentConfigs.put(EnchantmentType.KNOCKBACK, new EnchantmentConfig(
            EnchantmentType.KNOCKBACK,
            "§eKnockback",
            "§7Knocks back targets",
            Material.DIAMOND_SWORD,
            1,
            Arrays.asList("Knockback I", "Knockback II")
        ));
        
        // Power
        enchantmentConfigs.put(EnchantmentType.POWER, new EnchantmentConfig(
            EnchantmentType.POWER,
            "§cPower",
            "§7Increases bow damage",
            Material.BOW,
            1,
            Arrays.asList("Power I", "Power II", "Power III", "Power IV", "Power V")
        ));
        
        // Punch
        enchantmentConfigs.put(EnchantmentType.PUNCH, new EnchantmentConfig(
            EnchantmentType.PUNCH,
            "§ePunch",
            "§7Knocks back targets",
            Material.BOW,
            2,
            Arrays.asList("Punch I", "Punch II")
        ));
        
        // Flame
        enchantmentConfigs.put(EnchantmentType.FLAME, new EnchantmentConfig(
            EnchantmentType.FLAME,
            "§cFlame",
            "§7Sets arrows on fire",
            Material.BOW,
            2,
            Arrays.asList("Flame")
        ));
        
        // Infinity
        enchantmentConfigs.put(EnchantmentType.INFINITY, new EnchantmentConfig(
            EnchantmentType.INFINITY,
            "§bInfinity",
            "§7Arrows are not consumed",
            Material.BOW,
            3,
            Arrays.asList("Infinity")
        ));
    }
    
    private void initializeEnchantingConfigs() {
        // Spawn Island
        enchantingConfigs.put(EnchantingLocation.SPAWN, new EnchantingConfig(
            EnchantingLocation.SPAWN,
            "§aSpawn Island",
            "§7A peaceful enchanting spot",
            Material.ENCHANTING_TABLE,
            1,
            Arrays.asList(
                new EnchantingReward("Enchanting Table", Material.ENCHANTING_TABLE, 1000, "§dEnchanting Table"),
                new EnchantingReward("Bookshelf", Material.BOOKSHELF, 500, "§6Bookshelf"),
                new EnchantingReward("Lapis Lazuli", Material.LAPIS_LAZULI, 100, "§9Lapis Lazuli")
            )
        ));
        
        // Deep Caverns
        enchantingConfigs.put(EnchantingLocation.DEEP_CAVERNS, new EnchantingConfig(
            EnchantingLocation.DEEP_CAVERNS,
            "§7Deep Caverns",
            "§7A deep enchanting spot",
            Material.ENCHANTING_TABLE,
            2,
            Arrays.asList(
                new EnchantingReward("Enchanting Table", Material.ENCHANTING_TABLE, 2000, "§dEnchanting Table"),
                new EnchantingReward("Bookshelf", Material.BOOKSHELF, 1000, "§6Bookshelf"),
                new EnchantingReward("Lapis Lazuli", Material.LAPIS_LAZULI, 200, "§9Lapis Lazuli"),
                new EnchantingReward("Enchanted Book", Material.ENCHANTED_BOOK, 500, "§dEnchanted Book")
            )
        ));
        
        // The End
        enchantingConfigs.put(EnchantingLocation.THE_END, new EnchantingConfig(
            EnchantingLocation.THE_END,
            "§5The End",
            "§7An end enchanting spot",
            Material.ENCHANTING_TABLE,
            3,
            Arrays.asList(
                new EnchantingReward("Enchanting Table", Material.ENCHANTING_TABLE, 3000, "§dEnchanting Table"),
                new EnchantingReward("Bookshelf", Material.BOOKSHELF, 1500, "§6Bookshelf"),
                new EnchantingReward("Lapis Lazuli", Material.LAPIS_LAZULI, 300, "§9Lapis Lazuli"),
                new EnchantingReward("Enchanted Book", Material.ENCHANTED_BOOK, 1000, "§dEnchanted Book"),
                new EnchantingReward("Ender Pearl", Material.ENDER_PEARL, 500, "§5Ender Pearl")
            )
        ));
    }
    
    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        PlayerEnchantingData data = getPlayerEnchantingData(player.getUniqueId());
        
        // Calculate enchanting XP
        int xp = calculateEnchantingXP(player, data);
        data.addEnchantingXP(xp);
        
        player.sendMessage("§aEnchanted item! +" + xp + " Enchanting XP");
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());
        
        if (displayName.contains("Enchanting") || displayName.contains("enchanting")) {
            openEnchantingGUI(player);
        }
    }
    
    public void openEnchantingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§d§lEnchanting System"));
        
        // Add enchanting locations
        addGUIItem(gui, 10, Material.ENCHANTING_TABLE, "§a§lSpawn Island", "§7A peaceful enchanting spot");
        addGUIItem(gui, 11, Material.ENCHANTING_TABLE, "§7§lDeep Caverns", "§7A deep enchanting spot");
        addGUIItem(gui, 12, Material.ENCHANTING_TABLE, "§5§lThe End", "§7An end enchanting spot");
        
        // Add enchanting management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Enchanting Progress", "§7View your enchanting progress.");
        addGUIItem(gui, 19, Material.ENCHANTING_TABLE, "§d§lEnchanting Table", "§7Use the enchanting table.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lEnchanting Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lEnchanting Shop", "§7Buy enchanting items.");
        addGUIItem(gui, 22, Material.GOLD_INGOT, "§6§lEnchanting Contests", "§7Join enchanting contests.");
        
        // Add enchantment types
        addGUIItem(gui, 27, Material.DIAMOND_SWORD, "§c§lSharpness", "§7Increases melee damage");
        addGUIItem(gui, 28, Material.DIAMOND_CHESTPLATE, "§a§lProtection", "§7Reduces damage from all sources");
        addGUIItem(gui, 29, Material.DIAMOND_PICKAXE, "§e§lEfficiency", "§7Increases mining speed");
        addGUIItem(gui, 30, Material.DIAMOND_PICKAXE, "§6§lFortune", "§7Increases block drops");
        addGUIItem(gui, 31, Material.DIAMOND_SWORD, "§d§lLooting", "§7Increases mob drops");
        addGUIItem(gui, 32, Material.DIAMOND_SWORD, "§7§lUnbreaking", "§7Increases item durability");
        addGUIItem(gui, 33, Material.DIAMOND_SWORD, "§a§lMending", "§7Repairs items with XP");
        addGUIItem(gui, 34, Material.DIAMOND_SWORD, "§c§lFire Aspect", "§7Sets targets on fire");
        addGUIItem(gui, 35, Material.DIAMOND_SWORD, "§e§lKnockback", "§7Knocks back targets");
        
        // Add bow enchantments
        addGUIItem(gui, 36, Material.BOW, "§c§lPower", "§7Increases bow damage");
        addGUIItem(gui, 37, Material.BOW, "§e§lPunch", "§7Knocks back targets");
        addGUIItem(gui, 38, Material.BOW, "§c§lFlame", "§7Sets arrows on fire");
        addGUIItem(gui, 39, Material.BOW, "§b§lInfinity", "§7Arrows are not consumed");
        
        // Add special items
        addGUIItem(gui, 45, Material.ENCHANTING_TABLE, "§d§lEnchanting Table", "§7A basic enchanting table");
        addGUIItem(gui, 46, Material.BOOKSHELF, "§6§lBookshelf", "§7Increases enchanting power");
        addGUIItem(gui, 47, Material.LAPIS_LAZULI, "§9§lLapis Lazuli", "§7Required for enchanting");
        addGUIItem(gui, 48, Material.ENCHANTED_BOOK, "§d§lEnchanted Book", "§7Contains enchantments");
        
        // Navigation
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the enchanting menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aEnchanting GUI opened!");
    }
    
    private int calculateEnchantingXP(Player player, PlayerEnchantingData data) {
        int baseXP = 10;
        int level = data.getEnchantingLevel();
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
    
    public PlayerEnchantingData getPlayerEnchantingData(UUID playerId) {
        return playerEnchantingData.computeIfAbsent(playerId, k -> new PlayerEnchantingData(playerId));
    }
    
    public enum EnchantmentType {
        SHARPNESS("§cSharpness", "§7Increases melee damage"),
        PROTECTION("§aProtection", "§7Reduces damage from all sources"),
        EFFICIENCY("§eEfficiency", "§7Increases mining speed"),
        FORTUNE("§6Fortune", "§7Increases block drops"),
        LOOTING("§dLooting", "§7Increases mob drops"),
        UNBREAKING("§7Unbreaking", "§7Increases item durability"),
        MENDING("§aMending", "§7Repairs items with XP"),
        FIRE_ASPECT("§cFire Aspect", "§7Sets targets on fire"),
        KNOCKBACK("§eKnockback", "§7Knocks back targets"),
        POWER("§cPower", "§7Increases bow damage"),
        PUNCH("§ePunch", "§7Knocks back targets"),
        FLAME("§cFlame", "§7Sets arrows on fire"),
        INFINITY("§bInfinity", "§7Arrows are not consumed");
        
        private final String displayName;
        private final String description;
        
        EnchantmentType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum EnchantingLocation {
        SPAWN("§aSpawn Island", "§7A peaceful enchanting spot"),
        DEEP_CAVERNS("§7Deep Caverns", "§7A deep enchanting spot"),
        THE_END("§5The End", "§7An end enchanting spot");
        
        private final String displayName;
        private final String description;
        
        EnchantingLocation(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class EnchantmentConfig {
        private final EnchantmentType type;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<String> levels;
        
        public EnchantmentConfig(EnchantmentType type, String displayName, String description, Material icon,
                               int requiredLevel, List<String> levels) {
            this.type = type;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.levels = levels;
        }
        
        public EnchantmentType getType() { return type; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<String> getLevels() { return levels; }
    }
    
    public static class EnchantingConfig {
        private final EnchantingLocation location;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<EnchantingReward> rewards;
        
        public EnchantingConfig(EnchantingLocation location, String displayName, String description, Material icon,
                              int requiredLevel, List<EnchantingReward> rewards) {
            this.location = location;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.rewards = rewards;
        }
        
        public EnchantingLocation getLocation() { return location; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<EnchantingReward> getRewards() { return rewards; }
    }
    
    public static class EnchantingReward {
        private final String name;
        private final Material material;
        private final int cost;
        private final String displayName;
        
        public EnchantingReward(String name, Material material, int cost, String displayName) {
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
    
    public static class PlayerEnchantingData {
        private final UUID playerId;
        private int enchantingLevel;
        private int enchantingXP;
        private final Map<EnchantingLocation, Integer> locationStats = new HashMap<>();
        private final Map<EnchantmentType, Integer> enchantmentStats = new HashMap<>();
        
        public PlayerEnchantingData(UUID playerId) {
            this.playerId = playerId;
            this.enchantingLevel = 1;
            this.enchantingXP = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getEnchantingLevel() { return enchantingLevel; }
        public int getEnchantingXP() { return enchantingXP; }
        public int getLocationStats(EnchantingLocation location) { return locationStats.getOrDefault(location, 0); }
        public int getEnchantmentStats(EnchantmentType type) { return enchantmentStats.getOrDefault(type, 0); }
        
        public void addEnchantingXP(int xp) {
            this.enchantingXP += xp;
            checkLevelUp();
        }
        
        public void addLocationStat(EnchantingLocation location) {
            locationStats.put(location, locationStats.getOrDefault(location, 0) + 1);
        }
        
        public void addEnchantmentStat(EnchantmentType type) {
            enchantmentStats.put(type, enchantmentStats.getOrDefault(type, 0) + 1);
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(enchantingLevel + 1);
            if (enchantingXP >= requiredXP) {
                enchantingLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
}
