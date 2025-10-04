package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnchantingSystem {
    private final SkyblockPlugin plugin;
    private final Map<UUID, Map<CustomEnchantment, Integer>> playerEnchantments = new HashMap<>();
    private final Map<UUID, Integer> playerEnchantingLevel = new HashMap<>();
    
    public EnchantingSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
        initializeEnchantingData();
    }
    
    private void initializeEnchantingData() {
        // Initialize enchanting data for all players
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            initializePlayerEnchantingData(player.getUniqueId());
        }
    }
    
    private void initializePlayerEnchantingData(UUID playerId) {
        playerEnchantments.computeIfAbsent(playerId, k -> new HashMap<>());
        playerEnchantingLevel.computeIfAbsent(playerId, k -> 0);
        
        // Initialize all custom enchantments at level 0
        for (CustomEnchantment enchantment : CustomEnchantment.values()) {
            playerEnchantments.get(playerId).put(enchantment, 0);
        }
    }
    
    public void addEnchantingXP(Player player, int xp) {
        UUID playerId = player.getUniqueId();
        initializePlayerEnchantingData(playerId);
        
        int currentLevel = playerEnchantingLevel.get(playerId);
        int newLevel = calculateEnchantingLevel(currentLevel, xp);
        
        if (newLevel > currentLevel) {
            playerEnchantingLevel.put(playerId, newLevel);
            player.sendMessage("§a§lENCHANTING LEVEL UP!");
            player.sendMessage("§7Enchanting Level: §e" + currentLevel + " §7→ §a" + newLevel);
            
            // Give level up rewards
            giveEnchantingLevelRewards(player, newLevel);
        }
    }
    
    private int calculateEnchantingLevel(int currentLevel, int xp) {
        // Hypixel-like enchanting level calculation
        int totalXP = currentLevel * 100 + xp;
        return totalXP / 100;
    }
    
    private void giveEnchantingLevelRewards(Player player, int level) {
        // Give rewards based on enchanting level
        if (level % 5 == 0) {
            plugin.getEconomyManager().giveMoney(player, level * 50);
            player.getInventory().addItem(new ItemStack(Material.EXPERIENCE_BOTTLE, level));
        }
    }
    
    public void enchantItem(Player player, ItemStack item, CustomEnchantment enchantment, int level) {
        UUID playerId = player.getUniqueId();
        initializePlayerEnchantingData(playerId);
        
        // Check if player has required enchanting level
        int requiredLevel = enchantment.getRequiredLevel();
        int playerLevel = playerEnchantingLevel.get(playerId);
        
        if (playerLevel < requiredLevel) {
            player.sendMessage("§cYou need Enchanting Level " + requiredLevel + " to use this enchantment!");
            return;
        }
        
        // Check if player has enough coins
        double cost = enchantment.getCost() * level;
        if (!plugin.getEconomyManager().hasBalance(player, cost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + plugin.getEconomyManager().formatMoney(cost));
            return;
        }
        
        // Apply enchantment
        if (!plugin.getEconomyManager().withdrawMoney(player, cost)) {
            return;
        }
        applyCustomEnchantment(item, enchantment, level);
        
        player.sendMessage("§a§lITEM ENCHANTED!");
        player.sendMessage("§7Enchantment: §e" + enchantment.getName() + " " + level);
        player.sendMessage("§7Cost: §6" + plugin.getEconomyManager().formatMoney(cost));
    }
    
    private void applyCustomEnchantment(ItemStack item, CustomEnchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        // Apply custom enchantment logic
        switch (enchantment) {
            case SHARPNESS -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("sharpness")), level, true);
            }
            case PROTECTION -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("protection")), level, true);
            }
            case EFFICIENCY -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("efficiency")), level, true);
            }
            case UNBREAKING -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("unbreaking")), level, true);
            }
            case FORTUNE -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("fortune")), level, true);
            }
            case FORTUNE_OF_THE_SEA -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("fortune")), level, true);
            }
            case LURE -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("lure")), level, true);
            }
            case POWER -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("power")), level, true);
            }
            case PUNCH -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("punch")), level, true);
            }
            case FLAME -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("flame")), level, true);
            }
            case INFINITY -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("infinity")), level, true);
            }
            case FIRE_ASPECT -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("fire_aspect")), level, true);
            }
            case KNOCKBACK -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("knockback")), level, true);
            }
            case LOOTING -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("looting")), level, true);
            }
            case SWEEPING_EDGE -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("sweeping")), level, true);
            }
            case BANE_OF_ARTHROPODS -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("bane_of_arthropods")), level, true);
            }
            case SMITE -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("smite")), level, true);
            }
            case BLAST_PROTECTION -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("blast_protection")), level, true);
            }
            case FIRE_PROTECTION -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("fire_protection")), level, true);
            }
            case PROJECTILE_PROTECTION -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("projectile_protection")), level, true);
            }
            case FEATHER_FALLING -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("feather_falling")), level, true);
            }
            case RESPIRATION -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("respiration")), level, true);
            }
            case AQUA_AFFINITY -> {
                meta.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft("aqua_affinity")), level, true);
            }
            case THORNS -> {
                meta.addEnchant(Enchantment.THORNS, level, true);
            }
            case DEPTH_STRIDER -> {
                meta.addEnchant(Enchantment.DEPTH_STRIDER, level, true);
            }
            case FROST_WALKER -> {
                meta.addEnchant(Enchantment.FROST_WALKER, level, true);
            }
            case BINDING_CURSE -> {
                meta.addEnchant(Enchantment.BINDING_CURSE, level, true);
            }
            case VANISHING_CURSE -> {
                meta.addEnchant(Enchantment.VANISHING_CURSE, level, true);
            }
            case IMPALING -> {
                meta.addEnchant(Enchantment.IMPALING, level, true);
            }
            case RIPTIDE -> {
                meta.addEnchant(Enchantment.RIPTIDE, level, true);
            }
            case LOYALTY -> {
                meta.addEnchant(Enchantment.LOYALTY, level, true);
            }
            case CHANNELING -> {
                meta.addEnchant(Enchantment.CHANNELING, level, true);
            }
            case MENDING -> {
                meta.addEnchant(Enchantment.MENDING, level, true);
            }
            case SILK_TOUCH -> {
                meta.addEnchant(Enchantment.SILK_TOUCH, level, true);
            }
            case MULTISHOT -> {
                meta.addEnchant(Enchantment.MULTISHOT, level, true);
            }
            case QUICK_CHARGE -> {
                meta.addEnchant(Enchantment.QUICK_CHARGE, level, true);
            }
            case PIERCING -> {
                meta.addEnchant(Enchantment.PIERCING, level, true);
            }
            case SOUL_SPEED -> {
                meta.addEnchant(Enchantment.SOUL_SPEED, level, true);
            }
            case SWIFT_SNEAK -> {
                meta.addEnchant(Enchantment.SWIFT_SNEAK, level, true);
            }
        }
        
        item.setItemMeta(meta);
    }
    
    public int getEnchantingLevel(UUID playerId) {
        return playerEnchantingLevel.getOrDefault(playerId, 0);
    }
    
    public Map<CustomEnchantment, Integer> getPlayerEnchantments(UUID playerId) {
        return playerEnchantments.getOrDefault(playerId, new HashMap<>());
    }
    
    public void getPlayerEnchanting(UUID playerId) {
        // Initialize player enchanting data if not exists
        initializePlayerEnchantingData(playerId);
    }
    
    public enum CustomEnchantment {
        SHARPNESS("Sharpness", 1, 100.0),
        PROTECTION("Protection", 1, 100.0),
        EFFICIENCY("Efficiency", 1, 100.0),
        UNBREAKING("Unbreaking", 1, 100.0),
        FORTUNE("Fortune", 1, 100.0),
        FORTUNE_OF_THE_SEA("Luck of the Sea", 1, 100.0),
        LURE("Lure", 1, 100.0),
        POWER("Power", 1, 100.0),
        PUNCH("Punch", 1, 100.0),
        FLAME("Flame", 1, 100.0),
        INFINITY("Infinity", 1, 100.0),
        FIRE_ASPECT("Fire Aspect", 1, 100.0),
        KNOCKBACK("Knockback", 1, 100.0),
        LOOTING("Looting", 1, 100.0),
        SWEEPING_EDGE("Sweeping Edge", 1, 100.0),
        BANE_OF_ARTHROPODS("Bane of Arthropods", 1, 100.0),
        SMITE("Smite", 1, 100.0),
        BLAST_PROTECTION("Blast Protection", 1, 100.0),
        FIRE_PROTECTION("Fire Protection", 1, 100.0),
        PROJECTILE_PROTECTION("Projectile Protection", 1, 100.0),
        FEATHER_FALLING("Feather Falling", 1, 100.0),
        RESPIRATION("Respiration", 1, 100.0),
        AQUA_AFFINITY("Aqua Affinity", 1, 100.0),
        THORNS("Thorns", 1, 100.0),
        DEPTH_STRIDER("Depth Strider", 1, 100.0),
        FROST_WALKER("Frost Walker", 1, 100.0),
        BINDING_CURSE("Binding Curse", 1, 100.0),
        VANISHING_CURSE("Vanishing Curse", 1, 100.0),
        IMPALING("Impaling", 1, 100.0),
        RIPTIDE("Riptide", 1, 100.0),
        LOYALTY("Loyalty", 1, 100.0),
        CHANNELING("Channeling", 1, 100.0),
        MENDING("Mending", 1, 100.0),
        SILK_TOUCH("Silk Touch", 1, 100.0),
        MULTISHOT("Multishot", 1, 100.0),
        QUICK_CHARGE("Quick Charge", 1, 100.0),
        PIERCING("Piercing", 1, 100.0),
        SOUL_SPEED("Soul Speed", 1, 100.0),
        SWIFT_SNEAK("Swift Sneak", 1, 100.0);
        
        private final String name;
        private final int requiredLevel;
        private final double cost;
        
        CustomEnchantment(String name, int requiredLevel, double cost) {
            this.name = name;
            this.requiredLevel = requiredLevel;
            this.cost = cost;
        }
        
        public String getName() { return name; }
        public int getRequiredLevel() { return requiredLevel; }
        public double getCost() { return cost; }
    }
}
