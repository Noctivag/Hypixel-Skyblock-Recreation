package de.noctivag.plugin.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.core.CorePlatform;
import de.noctivag.plugin.core.PlayerProfile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Custom Item System - Einzigartige Items mit Stats, Effekten, Reforging, Enchantments
 * 
 * Verantwortlich für:
 * - Custom Item-Erstellung
 * - Item-Stats und -Effekte
 * - Reforging-System
 * - Custom Enchantments
 * - Item-Rarities
 * - Item-Upgrades
 */
public class CustomItemSystem {
    private final Plugin plugin;
    private final CorePlatform corePlatform;
    private final Map<String, CustomItem> customItems = new ConcurrentHashMap<>();
    private final Map<String, ItemRarity> itemRarities = new HashMap<>();
    private final Map<String, CustomEnchantment> customEnchantments = new HashMap<>();
    
    public CustomItemSystem(Plugin plugin, CorePlatform corePlatform) {
        this.plugin = plugin;
        this.corePlatform = corePlatform;
        initializeItemRarities();
        initializeCustomEnchantments();
        initializeCustomItems();
    }
    
    private void initializeItemRarities() {
        itemRarities.put("COMMON", new ItemRarity("Common", "§f", 1.0));
        itemRarities.put("UNCOMMON", new ItemRarity("Uncommon", "§a", 1.5));
        itemRarities.put("RARE", new ItemRarity("Rare", "§9", 2.0));
        itemRarities.put("EPIC", new ItemRarity("Epic", "§5", 2.5));
        itemRarities.put("LEGENDARY", new ItemRarity("Legendary", "§6", 3.0));
        itemRarities.put("MYTHIC", new ItemRarity("Mythic", "§d", 3.5));
        itemRarities.put("DIVINE", new ItemRarity("Divine", "§b", 4.0));
        itemRarities.put("SPECIAL", new ItemRarity("Special", "§c", 5.0));
    }
    
    private void initializeCustomEnchantments() {
        // Weapon Enchantments
        customEnchantments.put("SHARPNESS", new CustomEnchantment("Sharpness", "Increases damage", 1, 10, 100.0));
        customEnchantments.put("CRITICAL", new CustomEnchantment("Critical", "Increases critical hit chance", 1, 5, 200.0));
        customEnchantments.put("LIFESTEAL", new CustomEnchantment("Lifesteal", "Heals on hit", 1, 3, 300.0));
        customEnchantments.put("EXECUTE", new CustomEnchantment("Execute", "Extra damage to low health enemies", 1, 5, 250.0));
        customEnchantments.put("VENOMOUS", new CustomEnchantment("Venomous", "Poisons enemies", 1, 3, 150.0));
        
        // Armor Enchantments
        customEnchantments.put("PROTECTION", new CustomEnchantment("Protection", "Reduces damage taken", 1, 10, 100.0));
        customEnchantments.put("THORNS", new CustomEnchantment("Thorns", "Reflects damage", 1, 5, 200.0));
        customEnchantments.put("REGENERATION", new CustomEnchantment("Regeneration", "Regenerates health", 1, 3, 300.0));
        customEnchantments.put("MAGIC_FIND", new CustomEnchantment("Magic Find", "Increases rare item chance", 1, 5, 400.0));
        customEnchantments.put("LUCK", new CustomEnchantment("Luck", "Increases luck", 1, 5, 350.0));
        
        // Tool Enchantments
        customEnchantments.put("EFFICIENCY", new CustomEnchantment("Efficiency", "Increases mining speed", 1, 10, 100.0));
        customEnchantments.put("FORTUNE", new CustomEnchantment("Fortune", "Increases drop rates", 1, 5, 200.0));
        customEnchantments.put("SILK_TOUCH", new CustomEnchantment("Silk Touch", "Drops blocks instead of items", 1, 1, 300.0));
        customEnchantments.put("SMELTING", new CustomEnchantment("Smelting", "Auto-smelts blocks", 1, 1, 250.0));
        customEnchantments.put("EXPERIENCE", new CustomEnchantment("Experience", "Gains extra XP", 1, 5, 150.0));
    }
    
    private void initializeCustomItems() {
        // Weapons
        createCustomItem("ASPECT_OF_THE_END", "Aspect of the End", Material.DIAMOND_SWORD, "LEGENDARY", 
            Arrays.asList("§7Teleports you 8 blocks ahead", "§7and deals §a+100% §7damage", "§7for the next hit."),
            Arrays.asList("DAMAGE: 100", "STRENGTH: 100", "CRITICAL_CHANCE: 5", "CRITICAL_DAMAGE: 50"));
        
        createCustomItem("ASPECT_OF_THE_DRAGONS", "Aspect of the Dragons", Material.DIAMOND_SWORD, "LEGENDARY",
            Arrays.asList("§7Deals §a+250% §7damage to", "§7Ender Dragons and deals", "§7§a+25% §7damage to End", "§7related mobs."),
            Arrays.asList("DAMAGE: 225", "STRENGTH: 100", "CRITICAL_CHANCE: 5", "CRITICAL_DAMAGE: 50"));
        
        createCustomItem("HYPERION", "Hyperion", Material.DIAMOND_SWORD, "MYTHIC",
            Arrays.asList("§7Teleports you 10 blocks", "§7ahead and implodes dealing", "§7§a+500% §7damage to", "§7nearby enemies."),
            Arrays.asList("DAMAGE: 260", "STRENGTH: 150", "CRITICAL_CHANCE: 5", "CRITICAL_DAMAGE: 50"));
        
        // Armor
        createCustomItem("DRAGON_ARMOR", "Dragon Armor", Material.DIAMOND_CHESTPLATE, "LEGENDARY",
            Arrays.asList("§7Reduces damage taken by", "§7§a25% §7and increases", "§7damage dealt by §a15%."),
            Arrays.asList("DEFENSE: 140", "HEALTH: 100", "STRENGTH: 25", "CRITICAL_CHANCE: 2"));
        
        createCustomItem("NECRON_ARMOR", "Necron's Armor", Material.NETHERITE_CHESTPLATE, "MYTHIC",
            Arrays.asList("§7Increases damage dealt by", "§7§a30% §7and reduces", "§7damage taken by §a35%."),
            Arrays.asList("DEFENSE: 180", "HEALTH: 150", "STRENGTH: 40", "CRITICAL_CHANCE: 3"));
        
        // Tools
        createCustomItem("DRILL", "Drill", Material.DIAMOND_PICKAXE, "RARE",
            Arrays.asList("§7Mines blocks §a50% §7faster", "§7and has a chance to", "§7drop extra resources."),
            Arrays.asList("MINING_SPEED: 200", "FORTUNE: 3", "EFFICIENCY: 5"));
        
        createCustomItem("TREE_CAPITATOR", "Tree Capitator", Material.DIAMOND_AXE, "EPIC",
            Arrays.asList("§7Chops down entire trees", "§7at once and gains", "§7§a+100% §7foraging XP."),
            Arrays.asList("MINING_SPEED: 150", "FORTUNE: 2", "EFFICIENCY: 3"));
        
        // Accessories
        createCustomItem("TALISMAN_OF_POWER", "Talisman of Power", Material.GOLD_INGOT, "UNCOMMON",
            Arrays.asList("§7Increases all stats by", "§7§a+5% §7and grants", "§7§a+10% §7XP gain."),
            Arrays.asList("ALL_STATS: 5", "XP_BOOST: 10"));
        
        createCustomItem("RING_OF_LOVE", "Ring of Love", Material.GOLD_INGOT, "RARE",
            Arrays.asList("§7Increases health by", "§7§a+50 §7and grants", "§7§a+5% §7damage reduction."),
            Arrays.asList("HEALTH: 50", "DEFENSE: 5"));
    }
    
    private void createCustomItem(String id, String name, Material material, String rarity, List<String> description, List<String> stats) {
        CustomItem item = new CustomItem(id, name, material, itemRarities.get(rarity), description, stats);
        customItems.put(id, item);
    }
    
    public ItemStack createCustomItem(String itemId, int level) {
        CustomItem customItem = customItems.get(itemId);
        if (customItem == null) return null;
        
        ItemStack item = new ItemStack(customItem.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            // Set display name with rarity color
            String displayName = customItem.getRarity().getColor() + "§l" + customItem.getName();
            if (level > 1) {
                displayName += " §7(Level " + level + ")";
            }
            meta.displayName(Component.text(displayName));
            
            // Set lore
            List<String> lore = new ArrayList<>();
            lore.add(customItem.getRarity().getColor() + "§l" + customItem.getRarity().getName() + " ITEM");
            lore.add("");
            
            // Add description
            lore.addAll(customItem.getDescription());
            lore.add("");
            
            // Add stats
            lore.add("§7Stats:");
            for (String stat : customItem.getStats()) {
                lore.add("§7• " + stat);
            }
            lore.add("");
            
            // Add enchantments
            lore.add("§7Enchantments:");
            for (Map.Entry<String, Integer> entry : customItem.getEnchantments().entrySet()) {
                CustomEnchantment enchantment = customEnchantments.get(entry.getKey());
                if (enchantment != null) {
                    lore.add("§7• " + enchantment.getName() + " " + entry.getValue());
                }
            }
            lore.add("");
            
            // Add reforge info
            if (customItem.getReforge() != null) {
                lore.add("§7Reforge: §e" + customItem.getReforge());
            }
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public void reforgeItem(Player player, ItemStack item, String reforgeType) {
        // Check if player has enough coins
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return;
        
        double cost = getReforgeCost(reforgeType);
        if (!profile.tryRemoveCoins(cost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + cost);
            return;
        }
        
        // Apply reforge
        applyReforge(item, reforgeType);
        
        player.sendMessage("§a§lITEM REFORGED!");
        player.sendMessage("§7Reforge: §e" + reforgeType);
        player.sendMessage("§7Cost: §6" + cost + " coins");
    }
    
    private double getReforgeCost(String reforgeType) {
        return switch (reforgeType) {
            case "COMMON" -> 100.0;
            case "UNCOMMON" -> 250.0;
            case "RARE" -> 500.0;
            case "EPIC" -> 1000.0;
            case "LEGENDARY" -> 2500.0;
            case "MYTHIC" -> 5000.0;
            default -> 100.0;
        };
    }
    
    private void applyReforge(ItemStack item, String reforgeType) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        // Apply reforge stats
        Map<String, Integer> reforgeStats = getReforgeStats(reforgeType);
        
        // Update item lore with reforge stats
        List<Component> lore = meta.lore();
        if (lore != null) {
            // Find and update reforge line
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).toString().contains("Reforge:")) {
                    lore.set(i, Component.text("§7Reforge: §e" + reforgeType));
                    break;
                }
            }
            
            meta.lore(lore);
            item.setItemMeta(meta);
        }
    }
    
    private Map<String, Integer> getReforgeStats(String reforgeType) {
        Map<String, Integer> stats = new HashMap<>();
        
        switch (reforgeType) {
            case "COMMON" -> {
                stats.put("DAMAGE", 5);
                stats.put("STRENGTH", 5);
            }
            case "UNCOMMON" -> {
                stats.put("DAMAGE", 10);
                stats.put("STRENGTH", 10);
            }
            case "RARE" -> {
                stats.put("DAMAGE", 15);
                stats.put("STRENGTH", 15);
            }
            case "EPIC" -> {
                stats.put("DAMAGE", 20);
                stats.put("STRENGTH", 20);
            }
            case "LEGENDARY" -> {
                stats.put("DAMAGE", 25);
                stats.put("STRENGTH", 25);
            }
            case "MYTHIC" -> {
                stats.put("DAMAGE", 30);
                stats.put("STRENGTH", 30);
            }
        }
        
        return stats;
    }
    
    public void enchantItem(Player player, ItemStack item, String enchantmentId, int level) {
        // Check if player has enough coins
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return;
        
        CustomEnchantment enchantment = customEnchantments.get(enchantmentId);
        if (enchantment == null) return;
        
        double cost = enchantment.getCost() * level;
        if (!profile.tryRemoveCoins(cost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + cost);
            return;
        }
        
        // Apply enchantment
        applyEnchantment(item, enchantment, level);
        
        player.sendMessage("§a§lITEM ENCHANTED!");
        player.sendMessage("§7Enchantment: §e" + enchantment.getName() + " " + level);
        player.sendMessage("§7Cost: §6" + cost + " coins");
    }
    
    private void applyEnchantment(ItemStack item, CustomEnchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        // Add enchantment to item
        // This would need to be implemented with a custom enchantment system
        // For now, we'll just update the lore
        
        List<Component> lore = meta.lore();
        if (lore != null) {
            // Find enchantments section and add new enchantment
            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).toString().contains("Enchantments:")) {
                    lore.add(i + 1, Component.text("§7• " + enchantment.getName() + " " + level));
                    break;
                }
            }
            
            meta.lore(lore);
            item.setItemMeta(meta);
        }
    }
    
    public CustomItem getCustomItem(String itemId) {
        return customItems.get(itemId);
    }
    
    public Map<String, CustomItem> getAllCustomItems() {
        return new HashMap<>(customItems);
    }
    
    public Map<String, ItemRarity> getAllRarities() {
        return new HashMap<>(itemRarities);
    }
    
    public Map<String, CustomEnchantment> getAllEnchantments() {
        return new HashMap<>(customEnchantments);
    }
    
    // Custom Item Class
    public static class CustomItem {
        private final String id;
        private final String name;
        private final Material material;
        private final ItemRarity rarity;
        private final List<String> description;
        private final List<String> stats;
        private final Map<String, Integer> enchantments = new HashMap<>();
        private String reforge;
        
        public CustomItem(String id, String name, Material material, ItemRarity rarity, List<String> description, List<String> stats) {
            this.id = id;
            this.name = name;
            this.material = material;
            this.rarity = rarity;
            this.description = description;
            this.stats = stats;
        }
        
        public void addEnchantment(String enchantmentId, int level) {
            enchantments.put(enchantmentId, level);
        }
        
        public void setReforge(String reforge) {
            this.reforge = reforge;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public Material getMaterial() { return material; }
        public ItemRarity getRarity() { return rarity; }
        public List<String> getDescription() { return description; }
        public List<String> getStats() { return stats; }
        public Map<String, Integer> getEnchantments() { return enchantments; }
        public String getReforge() { return reforge; }
    }
    
    // Item Rarity Class
    public static class ItemRarity {
        private final String name;
        private final String color;
        private final double multiplier;
        
        public ItemRarity(String name, String color, double multiplier) {
            this.name = name;
            this.color = color;
            this.multiplier = multiplier;
        }
        
        public String getName() { return name; }
        public String getColor() { return color; }
        public double getMultiplier() { return multiplier; }
    }
    
    // Custom Enchantment Class
    public static class CustomEnchantment {
        private final String name;
        private final String description;
        private final int minLevel;
        private final int maxLevel;
        private final double cost;
        
        public CustomEnchantment(String name, String description, int minLevel, int maxLevel, double cost) {
            this.name = name;
            this.description = description;
            this.minLevel = minLevel;
            this.maxLevel = maxLevel;
            this.cost = cost;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public int getMinLevel() { return minLevel; }
        public int getMaxLevel() { return maxLevel; }
        public double getCost() { return cost; }
    }
}
