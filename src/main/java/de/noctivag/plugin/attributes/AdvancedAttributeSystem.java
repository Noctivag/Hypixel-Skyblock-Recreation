package de.noctivag.plugin.attributes;
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

public class AdvancedAttributeSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerAttributeData> playerAttributeData = new ConcurrentHashMap<>();
    private final Map<AttributeType, AttributeConfig> attributeConfigs = new HashMap<>();
    private final Map<AttributeCategory, CategoryConfig> categoryConfigs = new HashMap<>();
    
    public AdvancedAttributeSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeAttributeConfigs();
        initializeCategoryConfigs();
        startAttributeUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeAttributeConfigs() {
        // Combat Attributes
        attributeConfigs.put(AttributeType.STRENGTH, new AttributeConfig(
            "Strength", "§cStrength", Material.DIAMOND_SWORD,
            "§7Increases melee damage.",
            AttributeCategory.COMBAT, AttributeRarity.COMMON, 1, Arrays.asList("§7- +1 Melee Damage", "§7- +0.5 Crit Damage"),
            Arrays.asList("§7- 1x Strength Shard", "§7- 1x Combat Experience")
        ));
        
        attributeConfigs.put(AttributeType.CRITICAL_DAMAGE, new AttributeConfig(
            "Critical Damage", "§6Critical Damage", Material.GOLDEN_SWORD,
            "§7Increases critical hit damage.",
            AttributeCategory.COMBAT, AttributeRarity.UNCOMMON, 2, Arrays.asList("§7- +2 Crit Damage", "§7- +1 Crit Chance"),
            Arrays.asList("§7- 1x Critical Shard", "§7- 1x Combat Experience")
        ));
        
        attributeConfigs.put(AttributeType.CRITICAL_CHANCE, new AttributeConfig(
            "Critical Chance", "§eCritical Chance", Material.ARROW,
            "§7Increases critical hit chance.",
            AttributeCategory.COMBAT, AttributeRarity.UNCOMMON, 2, Arrays.asList("§7- +1 Crit Chance", "§7- +0.5 Crit Damage"),
            Arrays.asList("§7- 1x Critical Shard", "§7- 1x Combat Experience")
        ));
        
        attributeConfigs.put(AttributeType.ATTACK_SPEED, new AttributeConfig(
            "Attack Speed", "§aAttack Speed", Material.STICK,
            "§7Increases attack speed.",
            AttributeCategory.COMBAT, AttributeRarity.RARE, 3, Arrays.asList("§7- +1 Attack Speed", "§7- +0.5 Strength"),
            Arrays.asList("§7- 1x Speed Shard", "§7- 1x Combat Experience")
        ));
        
        // Defense Attributes
        attributeConfigs.put(AttributeType.HEALTH, new AttributeConfig(
            "Health", "§cHealth", Material.APPLE,
            "§7Increases maximum health.",
            AttributeCategory.DEFENSE, AttributeRarity.COMMON, 1, Arrays.asList("§7- +2 Health", "§7- +0.5 Health Regen"),
            Arrays.asList("§7- 1x Health Shard", "§7- 1x Defense Experience")
        ));
        
        attributeConfigs.put(AttributeType.DEFENSE, new AttributeConfig(
            "Defense", "§bDefense", Material.SHIELD,
            "§7Reduces incoming damage.",
            AttributeCategory.DEFENSE, AttributeRarity.COMMON, 1, Arrays.asList("§7- +1 Defense", "§7- +0.5 Damage Reduction"),
            Arrays.asList("§7- 1x Defense Shard", "§7- 1x Defense Experience")
        ));
        
        attributeConfigs.put(AttributeType.HEALTH_REGENERATION, new AttributeConfig(
            "Health Regeneration", "§aHealth Regeneration", Material.GOLDEN_APPLE,
            "§7Increases health regeneration rate.",
            AttributeCategory.DEFENSE, AttributeRarity.UNCOMMON, 2, Arrays.asList("§7- +1 Health Regen", "§7- +0.5 Health"),
            Arrays.asList("§7- 1x Regen Shard", "§7- 1x Defense Experience")
        ));
        
        attributeConfigs.put(AttributeType.DAMAGE_REDUCTION, new AttributeConfig(
            "Damage Reduction", "§6Damage Reduction", Material.IRON_CHESTPLATE,
            "§7Reduces all incoming damage.",
            AttributeCategory.DEFENSE, AttributeRarity.RARE, 3, Arrays.asList("§7- +1 Damage Reduction", "§7- +0.5 Defense"),
            Arrays.asList("§7- 1x Reduction Shard", "§7- 1x Defense Experience")
        ));
        
        // Magic Attributes
        attributeConfigs.put(AttributeType.INTELLIGENCE, new AttributeConfig(
            "Intelligence", "§bIntelligence", Material.ENCHANTED_BOOK,
            "§7Increases mana and magic damage.",
            AttributeCategory.MAGIC, AttributeRarity.COMMON, 1, Arrays.asList("§7- +2 Mana", "§7- +1 Magic Damage"),
            Arrays.asList("§7- 1x Intelligence Shard", "§7- 1x Magic Experience")
        ));
        
        attributeConfigs.put(AttributeType.MANA, new AttributeConfig(
            "Mana", "§9Mana", Material.LAPIS_LAZULI,
            "§7Increases maximum mana.",
            AttributeCategory.MAGIC, AttributeRarity.COMMON, 1, Arrays.asList("§7- +3 Mana", "§7- +0.5 Mana Regen"),
            Arrays.asList("§7- 1x Mana Shard", "§7- 1x Magic Experience")
        ));
        
        attributeConfigs.put(AttributeType.MANA_REGENERATION, new AttributeConfig(
            "Mana Regeneration", "§dMana Regeneration", Material.END_CRYSTAL,
            "§7Increases mana regeneration rate.",
            AttributeCategory.MAGIC, AttributeRarity.UNCOMMON, 2, Arrays.asList("§7- +1 Mana Regen", "§7- +0.5 Mana"),
            Arrays.asList("§7- 1x Regen Shard", "§7- 1x Magic Experience")
        ));
        
        attributeConfigs.put(AttributeType.MAGIC_DAMAGE, new AttributeConfig(
            "Magic Damage", "§5Magic Damage", Material.BLAZE_ROD,
            "§7Increases magic damage.",
            AttributeCategory.MAGIC, AttributeRarity.RARE, 3, Arrays.asList("§7- +2 Magic Damage", "§7- +0.5 Intelligence"),
            Arrays.asList("§7- 1x Magic Shard", "§7- 1x Magic Experience")
        ));
        
        // Utility Attributes
        attributeConfigs.put(AttributeType.SPEED, new AttributeConfig(
            "Speed", "§fSpeed", Material.SUGAR,
            "§7Increases movement speed.",
            AttributeCategory.UTILITY, AttributeRarity.COMMON, 1, Arrays.asList("§7- +1 Speed", "§7- +0.5 Agility"),
            Arrays.asList("§7- 1x Speed Shard", "§7- 1x Utility Experience")
        ));
        
        attributeConfigs.put(AttributeType.AGILITY, new AttributeConfig(
            "Agility", "§aAgility", Material.FEATHER,
            "§7Increases agility and jumping.",
            AttributeCategory.UTILITY, AttributeRarity.UNCOMMON, 2, Arrays.asList("§7- +1 Agility", "§7- +0.5 Speed"),
            Arrays.asList("§7- 1x Agility Shard", "§7- 1x Utility Experience")
        ));
        
        attributeConfigs.put(AttributeType.LUCK, new AttributeConfig(
            "Luck", "§6Luck", Material.EMERALD,
            "§7Increases luck and drop rates.",
            AttributeCategory.UTILITY, AttributeRarity.RARE, 3, Arrays.asList("§7- +1 Luck", "§7- +0.5 Drop Rate"),
            Arrays.asList("§7- 1x Luck Shard", "§7- 1x Utility Experience")
        ));
        
        attributeConfigs.put(AttributeType.FORTUNE, new AttributeConfig(
            "Fortune", "§eFortune", Material.EMERALD,
            "§7Increases fortune and wealth.",
            AttributeCategory.UTILITY, AttributeRarity.EPIC, 4, Arrays.asList("§7- +1 Fortune", "§7- +0.5 Luck"),
            Arrays.asList("§7- 1x Fortune Shard", "§7- 1x Utility Experience")
        ));
    }
    
    private void initializeCategoryConfigs() {
        categoryConfigs.put(AttributeCategory.COMBAT, new CategoryConfig(
            "Combat", "§cCombat", Material.DIAMOND_SWORD,
            "§7Combat-related attributes.",
            Arrays.asList("§7- Strength", "§7- Critical Damage", "§7- Critical Chance", "§7- Attack Speed")
        ));
        
        categoryConfigs.put(AttributeCategory.DEFENSE, new CategoryConfig(
            "Defense", "§bDefense", Material.SHIELD,
            "§7Defense-related attributes.",
            Arrays.asList("§7- Health", "§7- Defense", "§7- Health Regeneration", "§7- Damage Reduction")
        ));
        
        categoryConfigs.put(AttributeCategory.MAGIC, new CategoryConfig(
            "Magic", "§dMagic", Material.ENCHANTED_BOOK,
            "§7Magic-related attributes.",
            Arrays.asList("§7- Intelligence", "§7- Mana", "§7- Mana Regeneration", "§7- Magic Damage")
        ));
        
        categoryConfigs.put(AttributeCategory.UTILITY, new CategoryConfig(
            "Utility", "§aUtility", Material.SUGAR,
            "§7Utility-related attributes.",
            Arrays.asList("§7- Speed", "§7- Agility", "§7- Luck", "§7- Fortune")
        ));
    }
    
    private void startAttributeUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateAllPlayerAttributeData();
            }
        }.runTaskTimer(plugin, 0L, 20L * 60L); // Update every minute
    }
    
    private void updateAllPlayerAttributeData() {
        for (PlayerAttributeData data : playerAttributeData.values()) {
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
        
        if (displayName.contains("Attribute Book")) {
            openAttributeGUI(player);
        }
    }
    
    public void openAttributeGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§d§lAttribute System");
        
        // Add attribute categories
        addGUIItem(gui, 10, Material.DIAMOND_SWORD, "§c§lCombat", "§7View combat attributes.");
        addGUIItem(gui, 11, Material.SHIELD, "§b§lDefense", "§7View defense attributes.");
        addGUIItem(gui, 12, Material.ENCHANTED_BOOK, "§d§lMagic", "§7View magic attributes.");
        addGUIItem(gui, 13, Material.SUGAR, "§a§lUtility", "§7View utility attributes.");
        
        // Add specific attributes
        addGUIItem(gui, 19, Material.DIAMOND_SWORD, "§c§lStrength", "§7View strength attribute.");
        addGUIItem(gui, 20, Material.GOLDEN_SWORD, "§6§lCritical Damage", "§7View critical damage attribute.");
        addGUIItem(gui, 21, Material.ARROW, "§e§lCritical Chance", "§7View critical chance attribute.");
        addGUIItem(gui, 22, Material.STICK, "§a§lAttack Speed", "§7View attack speed attribute.");
        addGUIItem(gui, 23, Material.APPLE, "§c§lHealth", "§7View health attribute.");
        addGUIItem(gui, 24, Material.SHIELD, "§b§lDefense", "§7View defense attribute.");
        addGUIItem(gui, 25, Material.GOLDEN_APPLE, "§a§lHealth Regen", "§7View health regeneration attribute.");
        addGUIItem(gui, 26, Material.IRON_CHESTPLATE, "§6§lDamage Reduction", "§7View damage reduction attribute.");
        
        // Add magic attributes
        addGUIItem(gui, 28, Material.ENCHANTED_BOOK, "§b§lIntelligence", "§7View intelligence attribute.");
        addGUIItem(gui, 29, Material.LAPIS_LAZULI, "§9§lMana", "§7View mana attribute.");
        addGUIItem(gui, 30, Material.END_CRYSTAL, "§d§lMana Regen", "§7View mana regeneration attribute.");
        addGUIItem(gui, 31, Material.BLAZE_ROD, "§5§lMagic Damage", "§7View magic damage attribute.");
        
        // Add utility attributes
        addGUIItem(gui, 33, Material.SUGAR, "§f§lSpeed", "§7View speed attribute.");
        addGUIItem(gui, 34, Material.FEATHER, "§a§lAgility", "§7View agility attribute.");
        addGUIItem(gui, 35, Material.EMERALD, "§6§lLuck", "§7View luck attribute.");
        addGUIItem(gui, 36, Material.EMERALD, "§e§lFortune", "§7View fortune attribute.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the GUI.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aAttribute GUI geöffnet!");
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
    
    public PlayerAttributeData getPlayerAttributeData(UUID playerId) {
        return playerAttributeData.computeIfAbsent(playerId, k -> new PlayerAttributeData(playerId));
    }
    
    public AttributeConfig getAttributeConfig(AttributeType type) {
        return attributeConfigs.get(type);
    }
    
    public CategoryConfig getCategoryConfig(AttributeCategory category) {
        return categoryConfigs.get(category);
    }
    
    public List<AttributeType> getAllAttributeTypes() {
        return new ArrayList<>(attributeConfigs.keySet());
    }
    
    public List<AttributeCategory> getAllAttributeCategories() {
        return new ArrayList<>(categoryConfigs.keySet());
    }
    
    public enum AttributeType {
        // Combat
        STRENGTH, CRITICAL_DAMAGE, CRITICAL_CHANCE, ATTACK_SPEED,
        // Defense
        HEALTH, DEFENSE, HEALTH_REGENERATION, DAMAGE_REDUCTION,
        // Magic
        INTELLIGENCE, MANA, MANA_REGENERATION, MAGIC_DAMAGE,
        // Utility
        SPEED, AGILITY, LUCK, FORTUNE
    }
    
    public enum AttributeCategory {
        COMBAT("§cCombat", "§7Combat-related attributes"),
        DEFENSE("§bDefense", "§7Defense-related attributes"),
        MAGIC("§dMagic", "§7Magic-related attributes"),
        UTILITY("§aUtility", "§7Utility-related attributes");
        
        private final String displayName;
        private final String description;
        
        AttributeCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum AttributeRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        AttributeRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class AttributeConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final AttributeCategory category;
        private final AttributeRarity rarity;
        private final int baseValue;
        private final List<String> effects;
        private final List<String> requirements;
        
        public AttributeConfig(String name, String displayName, Material material, String description,
                              AttributeCategory category, AttributeRarity rarity, int baseValue,
                              List<String> effects, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.baseValue = baseValue;
            this.effects = effects;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public AttributeCategory getCategory() { return category; }
        public AttributeRarity getRarity() { return rarity; }
        public int getBaseValue() { return baseValue; }
        public List<String> getEffects() { return effects; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class CategoryConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final List<String> attributes;
        
        public CategoryConfig(String name, String displayName, Material material, String description,
                             List<String> attributes) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.attributes = attributes;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public List<String> getAttributes() { return attributes; }
    }
    
    public static class PlayerAttributeData {
        private final UUID playerId;
        private final Map<AttributeType, Integer> attributeLevels = new HashMap<>();
        private final Map<AttributeType, Integer> attributeValues = new HashMap<>();
        private long lastUpdate;
        
        public PlayerAttributeData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addAttributeLevel(AttributeType type, int amount) {
            attributeLevels.put(type, attributeLevels.getOrDefault(type, 0) + amount);
        }
        
        public void addAttributeValue(AttributeType type, int amount) {
            attributeValues.put(type, attributeValues.getOrDefault(type, 0) + amount);
        }
        
        public int getAttributeLevel(AttributeType type) {
            return attributeLevels.getOrDefault(type, 0);
        }
        
        public int getAttributeValue(AttributeType type) {
            return attributeValues.getOrDefault(type, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
