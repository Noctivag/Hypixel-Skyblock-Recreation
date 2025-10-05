package de.noctivag.skyblock.accessories;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Accessory System for Hypixel Skyblock-style accessories
 * Includes accessory bag, collection, and magical effects
 */
public class AdvancedAccessorySystem {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerAccessoryData> playerAccessoryData = new ConcurrentHashMap<>();
    private final Map<String, AccessoryType> accessoryTypes = new HashMap<>();
    private final Map<UUID, List<AccessoryItem>> playerAccessories = new ConcurrentHashMap<>();
    private final Map<UUID, List<AccessoryEffect>> activeAccessoryEffects = new ConcurrentHashMap<>();
    
    public AdvancedAccessorySystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeAccessoryTypes();
        startAccessoryTask();
    }
    
    /**
     * Initialize all accessory types
     */
    private void initializeAccessoryTypes() {
        // Common Accessories
        accessoryTypes.put("wooden_ring", new AccessoryType(
            "Wooden Ring", 
            Material.OAK_LOG, 
            AccessoryRarity.COMMON,
            AccessoryCategory.RING,
            Arrays.asList("§7+1 Strength", "§7+1 Defense"),
            100,
            1
        ));
        
        accessoryTypes.put("stone_ring", new AccessoryType(
            "Stone Ring", 
            Material.STONE, 
            AccessoryRarity.COMMON,
            AccessoryCategory.RING,
            Arrays.asList("§7+2 Strength", "§7+2 Defense"),
            200,
            1
        ));
        
        accessoryTypes.put("iron_ring", new AccessoryType(
            "Iron Ring", 
            Material.IRON_INGOT, 
            AccessoryRarity.COMMON,
            AccessoryCategory.RING,
            Arrays.asList("§7+3 Strength", "§7+3 Defense"),
            300,
            1
        ));
        
        accessoryTypes.put("gold_ring", new AccessoryType(
            "Gold Ring", 
            Material.GOLD_INGOT, 
            AccessoryRarity.COMMON,
            AccessoryCategory.RING,
            Arrays.asList("§7+4 Strength", "§7+4 Defense"),
            400,
            1
        ));
        
        // Uncommon Accessories
        accessoryTypes.put("diamond_ring", new AccessoryType(
            "Diamond Ring", 
            Material.DIAMOND, 
            AccessoryRarity.UNCOMMON,
            AccessoryCategory.RING,
            Arrays.asList("§7+5 Strength", "§7+5 Defense", "§7+2% Critical Chance"),
            500,
            2
        ));
        
        accessoryTypes.put("emerald_ring", new AccessoryType(
            "Emerald Ring", 
            Material.EMERALD, 
            AccessoryRarity.UNCOMMON,
            AccessoryCategory.RING,
            Arrays.asList("§7+6 Strength", "§7+6 Defense", "§7+3% Critical Chance"),
            600,
            2
        ));
        
        accessoryTypes.put("lapis_ring", new AccessoryType(
            "Lapis Ring", 
            Material.LAPIS_LAZULI, 
            AccessoryRarity.UNCOMMON,
            AccessoryCategory.RING,
            Arrays.asList("§7+7 Strength", "§7+7 Defense", "§7+4% Critical Chance"),
            700,
            2
        ));
        
        accessoryTypes.put("redstone_ring", new AccessoryType(
            "Redstone Ring", 
            Material.REDSTONE, 
            AccessoryRarity.UNCOMMON,
            AccessoryCategory.RING,
            Arrays.asList("§7+8 Strength", "§7+8 Defense", "§7+5% Critical Chance"),
            800,
            2
        ));
        
        // Rare Accessories
        accessoryTypes.put("netherite_ring", new AccessoryType(
            "Netherite Ring", 
            Material.NETHERITE_INGOT, 
            AccessoryRarity.RARE,
            AccessoryCategory.RING,
            Arrays.asList("§7+10 Strength", "§7+10 Defense", "§7+8% Critical Chance", "§7+5% Critical Damage"),
            1000,
            3
        ));
        
        accessoryTypes.put("end_stone_ring", new AccessoryType(
            "End Stone Ring", 
            Material.END_STONE, 
            AccessoryRarity.RARE,
            AccessoryCategory.RING,
            Arrays.asList("§7+12 Strength", "§7+12 Defense", "§7+10% Critical Chance", "§7+7% Critical Damage"),
            1200,
            3
        ));
        
        accessoryTypes.put("obsidian_ring", new AccessoryType(
            "Obsidian Ring", 
            Material.OBSIDIAN, 
            AccessoryRarity.RARE,
            AccessoryCategory.RING,
            Arrays.asList("§7+15 Defense", "§7+8 Strength", "§7+12% Critical Chance", "§7+10% Critical Damage"),
            1500,
            3
        ));
        
        accessoryTypes.put("crystal_ring", new AccessoryType(
            "Crystal Ring", 
            Material.AMETHYST_SHARD, 
            AccessoryRarity.RARE,
            AccessoryCategory.RING,
            Arrays.asList("§7+14 Strength", "§7+14 Defense", "§7+15% Critical Chance", "§7+12% Critical Damage"),
            1800,
            3
        ));
        
        // Epic Accessories
        accessoryTypes.put("dragon_ring", new AccessoryType(
            "Dragon Ring", 
            Material.DRAGON_EGG, 
            AccessoryRarity.EPIC,
            AccessoryCategory.RING,
            Arrays.asList("§7+20 Strength", "§7+20 Defense", "§7+20% Critical Chance", "§7+15% Critical Damage"),
            2500,
            4
        ));
        
        accessoryTypes.put("phoenix_ring", new AccessoryType(
            "Phoenix Ring", 
            Material.FIRE_CHARGE, 
            AccessoryRarity.EPIC,
            AccessoryCategory.RING,
            Arrays.asList("§7+18 Strength", "§7+18 Defense", "§7+18% Critical Chance", "§7+20% Health"),
            2200,
            4
        ));
        
        accessoryTypes.put("void_ring", new AccessoryType(
            "Void Ring", 
            Material.ENDER_EYE, 
            AccessoryRarity.EPIC,
            AccessoryCategory.RING,
            Arrays.asList("§7+22 Strength", "§7+22 Defense", "§7+25% Critical Chance", "§7+18% Critical Damage"),
            2800,
            4
        ));
        
        accessoryTypes.put("cosmic_ring", new AccessoryType(
            "Cosmic Ring", 
            Material.NETHER_STAR, 
            AccessoryRarity.EPIC,
            AccessoryCategory.RING,
            Arrays.asList("§7+25 Strength", "§7+25 Defense", "§7+30% Critical Chance", "§7+25% Critical Damage"),
            3000,
            4
        ));
        
        // Legendary Accessories
        accessoryTypes.put("divine_ring", new AccessoryType(
            "Divine Ring", 
            Material.ENCHANTING_TABLE, 
            AccessoryRarity.LEGENDARY,
            AccessoryCategory.RING,
            Arrays.asList("§7+30 Strength", "§7+30 Defense", "§7+35% Critical Chance", "§7+30% Critical Damage"),
            5000,
            5
        ));
        
        accessoryTypes.put("primal_ring", new AccessoryType(
            "Primal Ring", 
            Material.ANCIENT_DEBRIS, 
            AccessoryRarity.LEGENDARY,
            AccessoryCategory.RING,
            Arrays.asList("§7+35 Strength", "§7+35 Defense", "§7+40% Critical Chance", "§7+35% Critical Damage"),
            6000,
            5
        ));
        
        accessoryTypes.put("eternal_ring", new AccessoryType(
            "Eternal Ring", 
            Material.BEACON, 
            AccessoryRarity.LEGENDARY,
            AccessoryCategory.RING,
            Arrays.asList("§7+40 Strength", "§7+40 Defense", "§7+45% Critical Chance", "§7+40% Critical Damage"),
            7500,
            5
        ));
        
        accessoryTypes.put("transcendent_ring", new AccessoryType(
            "Transcendent Ring", 
            Material.END_CRYSTAL, 
            AccessoryRarity.LEGENDARY,
            AccessoryCategory.RING,
            Arrays.asList("§7+45 Strength", "§7+45 Defense", "§7+50% Critical Chance", "§7+45% Critical Damage"),
            10000,
            5
        ));
        
        // Mythic Accessories
        accessoryTypes.put("mythic_ring", new AccessoryType(
            "Mythic Ring", 
            Material.END_PORTAL_FRAME, 
            AccessoryRarity.MYTHIC,
            AccessoryCategory.RING,
            Arrays.asList("§7+50 Strength", "§7+50 Defense", "§7+55% Critical Chance", "§7+50% Critical Damage"),
            15000,
            6
        ));
        
        accessoryTypes.put("legendary_ring", new AccessoryType(
            "Legendary Ring", 
            Material.DRAGON_HEAD, 
            AccessoryRarity.MYTHIC,
            AccessoryCategory.RING,
            Arrays.asList("§7+60 Strength", "§7+60 Defense", "§7+65% Critical Chance", "§7+60% Critical Damage"),
            20000,
            6
        ));
        
        accessoryTypes.put("ultimate_ring", new AccessoryType(
            "Ultimate Ring", 
            Material.WITHER_SKELETON_SKULL, 
            AccessoryRarity.MYTHIC,
            AccessoryCategory.RING,
            Arrays.asList("§7+75 Strength", "§7+75 Defense", "§7+80% Critical Chance", "§7+75% Critical Damage"),
            30000,
            6
        ));
        
        accessoryTypes.put("supreme_ring", new AccessoryType(
            "Supreme Ring", 
            Material.ELDER_GUARDIAN_SPAWN_EGG, 
            AccessoryRarity.MYTHIC,
            AccessoryCategory.RING,
            Arrays.asList("§7+100 Strength", "§7+100 Defense", "§7+100% Critical Chance", "§7+100% Critical Damage"),
            50000,
            6
        ));
        
        // Special Accessories
        accessoryTypes.put("luck_ring", new AccessoryType(
            "Luck Ring", 
            Material.RABBIT_FOOT, 
            AccessoryRarity.LEGENDARY,
            AccessoryCategory.RING,
            Arrays.asList("§7+25% Magic Find", "§7+20% Pet Luck", "§7+15% Coin Gain"),
            8000,
            5
        ));
        
        accessoryTypes.put("speed_ring", new AccessoryType(
            "Speed Ring", 
            Material.SUGAR, 
            AccessoryRarity.EPIC,
            AccessoryCategory.RING,
            Arrays.asList("§7+20% Movement Speed", "§7+15% Attack Speed", "§7+10% Mining Speed"),
            3000,
            4
        ));
        
        accessoryTypes.put("wisdom_ring", new AccessoryType(
            "Wisdom Ring", 
            Material.EXPERIENCE_BOTTLE, 
            AccessoryRarity.EPIC,
            AccessoryCategory.RING,
            Arrays.asList("§7+30% XP Gain", "§7+25% Skill XP", "§7+20% Enchanting XP"),
            3500,
            4
        ));
        
        accessoryTypes.put("fortune_ring", new AccessoryType(
            "Fortune Ring", 
            Material.EMERALD_BLOCK, 
            AccessoryRarity.LEGENDARY,
            AccessoryCategory.RING,
            Arrays.asList("§7+40% Fortune", "§7+30% Mining Fortune", "§7+25% Farming Fortune"),
            6000,
            5
        ));
    }
    
    /**
     * Start the accessory task
     */
    private void startAccessoryTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveAccessoryEffects();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L); // Every second
    }
    
    /**
     * Update all active accessory effects
     */
    private void updateActiveAccessoryEffects() {
        for (Map.Entry<UUID, List<AccessoryEffect>> entry : activeAccessoryEffects.entrySet()) {
            UUID playerId = entry.getKey();
            List<AccessoryEffect> effects = entry.getValue();
            
            // Remove expired effects
            effects.removeIf(AccessoryEffect::isExpired);
            
            // Apply active effects
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                applyAccessoryEffects(player, effects);
            }
        }
    }
    
    /**
     * Apply accessory effects to a player
     */
    private void applyAccessoryEffects(Player player, List<AccessoryEffect> effects) {
        for (AccessoryEffect effect : effects) {
            effect.apply(player);
        }
    }
    
    /**
     * Get player accessory data
     */
    public PlayerAccessoryData getPlayerAccessoryData(UUID playerId) {
        return playerAccessoryData.computeIfAbsent(playerId, k -> new PlayerAccessoryData(playerId));
    }
    
    /**
     * Get accessory type by ID
     */
    public AccessoryType getAccessoryType(String accessoryId) {
        return accessoryTypes.get(accessoryId);
    }
    
    /**
     * Get all accessory types
     */
    public Map<String, AccessoryType> getAllAccessoryTypes() {
        return new HashMap<>(accessoryTypes);
    }
    
    /**
     * Get accessory types by rarity
     */
    public Map<String, AccessoryType> getAccessoryTypesByRarity(AccessoryRarity rarity) {
        Map<String, AccessoryType> rarityAccessories = new HashMap<>();
        for (Map.Entry<String, AccessoryType> entry : accessoryTypes.entrySet()) {
            if (entry.getValue().getRarity() == rarity) {
                rarityAccessories.put(entry.getKey(), entry.getValue());
            }
        }
        return rarityAccessories;
    }
    
    /**
     * Get accessory types by category
     */
    public Map<String, AccessoryType> getAccessoryTypesByCategory(AccessoryCategory category) {
        Map<String, AccessoryType> categoryAccessories = new HashMap<>();
        for (Map.Entry<String, AccessoryType> entry : accessoryTypes.entrySet()) {
            if (entry.getValue().getCategory() == category) {
                categoryAccessories.put(entry.getKey(), entry.getValue());
            }
        }
        return categoryAccessories;
    }
    
    /**
     * Check if player can purchase an accessory
     */
    public boolean canPurchaseAccessory(Player player, String accessoryId) {
        PlayerAccessoryData data = getPlayerAccessoryData(player.getUniqueId());
        AccessoryType accessory = getAccessoryType(accessoryId);
        
        if (accessory == null) return false;
        
        // Check if player has enough coins
        if (data.getCoins() < accessory.getCost()) {
            return false;
        }
        
        // Check if player has required level
        if (data.getLevel() < getRequiredLevel(accessoryId)) {
            return false;
        }
        
        // Check if player already has this accessory
        List<AccessoryItem> playerAccessories = this.playerAccessories.getOrDefault(player.getUniqueId(), new ArrayList<>());
        for (AccessoryItem accessoryItem : playerAccessories) {
            if (accessoryItem.getAccessoryType().equals(accessoryId)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Purchase an accessory
     */
    public boolean purchaseAccessory(Player player, String accessoryId) {
        if (!canPurchaseAccessory(player, accessoryId)) {
            return false;
        }
        
        AccessoryType accessory = getAccessoryType(accessoryId);
        PlayerAccessoryData data = getPlayerAccessoryData(player.getUniqueId());
        
        // Remove coins
        data.removeCoins(accessory.getCost());
        
        // Create accessory item
        AccessoryItem accessoryItem = new AccessoryItem(accessoryId, player.getUniqueId(), accessory.getLevel());
        List<AccessoryItem> playerAccessories = this.playerAccessories.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        playerAccessories.add(accessoryItem);
        
        // Update statistics
        data.incrementPurchasedAccessories();
        data.addExperience(accessory.getCost() / 20);
        
        return true;
    }
    
    /**
     * Get required level for an accessory
     */
    private int getRequiredLevel(String accessoryId) {
        AccessoryType accessory = getAccessoryType(accessoryId);
        if (accessory == null) return 1;
        
        return accessory.getLevel();
    }
    
    /**
     * Create an accessory item
     */
    public ItemStack createAccessoryItem(String accessoryId) {
        AccessoryType accessory = getAccessoryType(accessoryId);
        if (accessory == null) return null;
        
        ItemStack item = new ItemStack(accessory.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(accessory.getRarity().getColor() + accessory.getName()));
            List<String> lore = new ArrayList<>(accessory.getEffects());
            lore.add("");
            lore.add("§7Rarity: " + accessory.getRarity().getDisplayName());
            lore.add("§7Category: " + accessory.getCategory().getDisplayName());
            lore.add("§7Level: §a" + accessory.getLevel());
            lore.add("§7Cost: §a" + accessory.getCost() + " coins");
            lore.add("");
            lore.add("§7Right-click to equip this accessory");
            lore.add("§7and gain its magical effects!");
            lore.add("");
            lore.add("§8A magical accessory");
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Equip an accessory
     */
    public boolean equipAccessory(Player player, String accessoryId) {
        List<AccessoryItem> playerAccessories = this.playerAccessories.get(player.getUniqueId());
        if (playerAccessories == null) return false;
        
        for (AccessoryItem accessoryItem : playerAccessories) {
            if (accessoryItem.getAccessoryType().equals(accessoryId)) {
                if (!accessoryItem.isEquipped()) {
                    accessoryItem.setEquipped(true);
                    activateAccessoryEffect(player.getUniqueId(), accessoryId);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Unequip an accessory
     */
    public boolean unequipAccessory(Player player, String accessoryId) {
        List<AccessoryItem> playerAccessories = this.playerAccessories.get(player.getUniqueId());
        if (playerAccessories == null) return false;
        
        for (AccessoryItem accessoryItem : playerAccessories) {
            if (accessoryItem.getAccessoryType().equals(accessoryId)) {
                if (accessoryItem.isEquipped()) {
                    accessoryItem.setEquipped(false);
                    deactivateAccessoryEffect(player.getUniqueId(), accessoryId);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Activate an accessory effect
     */
    private void activateAccessoryEffect(UUID playerId, String accessoryId) {
        AccessoryType accessory = getAccessoryType(accessoryId);
        if (accessory == null) return;
        
        AccessoryEffect effect = new AccessoryEffect(accessory, 1, 3600); // 1 hour
        List<AccessoryEffect> effects = activeAccessoryEffects.computeIfAbsent(playerId, k -> new ArrayList<>());
        effects.add(effect);
    }
    
    /**
     * Deactivate an accessory effect
     */
    private void deactivateAccessoryEffect(UUID playerId, String accessoryId) {
        List<AccessoryEffect> effects = activeAccessoryEffects.get(playerId);
        if (effects == null) return;
        
        effects.removeIf(effect -> effect.getAccessoryType().getName().equals(accessoryId));
    }
    
    /**
     * Get player's accessories
     */
    public List<AccessoryItem> getPlayerAccessories(UUID playerId) {
        return playerAccessories.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Get equipped accessories for a player
     */
    public List<AccessoryItem> getEquippedAccessories(UUID playerId) {
        List<AccessoryItem> playerAccessories = this.playerAccessories.get(playerId);
        if (playerAccessories == null) return new ArrayList<>();
        
        List<AccessoryItem> equipped = new ArrayList<>();
        for (AccessoryItem accessory : playerAccessories) {
            if (accessory.isEquipped()) {
                equipped.add(accessory);
            }
        }
        
        return equipped;
    }
    
    /**
     * Get player's accessory level
     */
    public int getAccessoryLevel(UUID playerId) {
        PlayerAccessoryData data = getPlayerAccessoryData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's accessory experience
     */
    public int getAccessoryExperience(UUID playerId) {
        PlayerAccessoryData data = getPlayerAccessoryData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add accessory experience to player
     */
    public void addAccessoryExperience(UUID playerId, int experience) {
        PlayerAccessoryData data = getPlayerAccessoryData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's accessory coins
     */
    public int getAccessoryCoins(UUID playerId) {
        PlayerAccessoryData data = getPlayerAccessoryData(playerId);
        return data.getCoins();
    }
    
    /**
     * Add accessory coins to player
     */
    public void addAccessoryCoins(UUID playerId, int coins) {
        PlayerAccessoryData data = getPlayerAccessoryData(playerId);
        data.addCoins(coins);
    }
    
    /**
     * Remove accessory coins from player
     */
    public void removeAccessoryCoins(UUID playerId, int coins) {
        PlayerAccessoryData data = getPlayerAccessoryData(playerId);
        data.removeCoins(coins);
    }
    
    /**
     * Get player's accessory statistics
     */
    public Map<String, Integer> getAccessoryStatistics(UUID playerId) {
        PlayerAccessoryData data = getPlayerAccessoryData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("coins", data.getCoins());
        stats.put("purchased_accessories", data.getPurchasedAccessories().size());
        stats.put("total_experience", data.getTotalExperience());
        
        return stats;
    }
    
    /**
     * Reset player's accessory data
     */
    public void resetAccessoryData(UUID playerId) {
        playerAccessoryData.remove(playerId);
        playerAccessories.remove(playerId);
        activeAccessoryEffects.remove(playerId);
    }
    
    /**
     * Save player's accessory data
     */
    public void saveAccessoryData(UUID playerId) {
        PlayerAccessoryData data = getPlayerAccessoryData(playerId);
        // Save to database
        databaseManager.savePlayerAccessoryData(playerId, data);
    }
    
    /**
     * Load player's accessory data
     */
    public void loadAccessoryData(UUID playerId) {
        try {
            CompletableFuture<PlayerAccessoryData> future = databaseManager.loadPlayerAccessoryData(playerId);
            PlayerAccessoryData data = future.get();
            if (data != null) {
                playerAccessoryData.put(playerId, data);
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Failed to load accessory data for player " + playerId + ": " + e.getMessage());
        }
    }
    
    /**
     * Shutdown the accessory system
     */
    public void shutdown() {
        // Save all player data
        for (UUID playerId : playerAccessoryData.keySet()) {
            saveAccessoryData(playerId);
        }
        
        // Clear data
        playerAccessoryData.clear();
        playerAccessories.clear();
        activeAccessoryEffects.clear();
    }
}
