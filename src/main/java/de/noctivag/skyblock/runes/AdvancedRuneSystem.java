package de.noctivag.skyblock.runes;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Advanced Rune System for Hypixel Skyblock-style runes
 * Includes rune types, upgrades, and enchanting effects
 */
public class AdvancedRuneSystem {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerRuneData> playerRuneData = new ConcurrentHashMap<>();
    private final Map<String, RuneType> runeTypes = new HashMap<>();
    private final Map<String, RuneUpgrade> runeUpgrades = new HashMap<>();
    private final Map<UUID, List<RuneEffect>> activeRuneEffects = new ConcurrentHashMap<>();
    
    public AdvancedRuneSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeRuneTypes();
        initializeRuneUpgrades();
        startRuneTask();
    }
    
    /**
     * Initialize all rune types
     */
    private void initializeRuneTypes() {
        // Combat Runes
        runeTypes.put("blood_rune", new RuneType(
            "Blood Rune", 
            Material.REDSTONE, 
            RuneCategory.COMBAT,
            Arrays.asList("§7+5% Damage", "§7+3% Critical Chance"),
            1000
        ));
        
        runeTypes.put("fire_rune", new RuneType(
            "Fire Rune", 
            Material.BLAZE_POWDER, 
            RuneCategory.COMBAT,
            Arrays.asList("§7+10% Fire Damage", "§7+5% Ignite Chance"),
            1500
        ));
        
        runeTypes.put("ice_rune", new RuneType(
            "Ice Rune", 
            Material.SNOWBALL, 
            RuneCategory.COMBAT,
            Arrays.asList("§7+10% Ice Damage", "§7+5% Freeze Chance"),
            1500
        ));
        
        runeTypes.put("lightning_rune", new RuneType(
            "Lightning Rune", 
            Material.GLOWSTONE_DUST, 
            RuneCategory.COMBAT,
            Arrays.asList("§7+10% Lightning Damage", "§7+5% Stun Chance"),
            1500
        ));
        
        // Mining Runes
        runeTypes.put("mining_rune", new RuneType(
            "Mining Rune", 
            Material.IRON_PICKAXE, 
            RuneCategory.MINING,
            Arrays.asList("§7+20% Mining Speed", "§7+10% Fortune"),
            2000
        ));
        
        runeTypes.put("gemstone_rune", new RuneType(
            "Gemstone Rune", 
            Material.EMERALD, 
            RuneCategory.MINING,
            Arrays.asList("§7+15% Gemstone Drop Rate", "§7+10% Mining Fortune"),
            3000
        ));
        
        runeTypes.put("mithril_rune", new RuneType(
            "Mithril Rune", 
            Material.IRON_INGOT, 
            RuneCategory.MINING,
            Arrays.asList("§7+25% Mithril Drop Rate", "§7+15% Mining Speed"),
            2500
        ));
        
        // Farming Runes
        runeTypes.put("farming_rune", new RuneType(
            "Farming Rune", 
            Material.WHEAT, 
            RuneCategory.FARMING,
            Arrays.asList("§7+25% Farming Fortune", "§7+10% Crop Growth Speed"),
            1800
        ));
        
        runeTypes.put("harvest_rune", new RuneType(
            "Harvest Rune", 
            Material.GOLDEN_HOE, 
            RuneCategory.FARMING,
            Arrays.asList("§7+30% Harvest Speed", "§7+15% Double Drop Chance"),
            2200
        ));
        
        // Fishing Runes
        runeTypes.put("fishing_rune", new RuneType(
            "Fishing Rune", 
            Material.FISHING_ROD, 
            RuneCategory.FISHING,
            Arrays.asList("§7+20% Fishing Speed", "§7+15% Treasure Drop Rate"),
            2000
        ));
        
        runeTypes.put("sea_creature_rune", new RuneType(
            "Sea Creature Rune", 
            Material.PRISMARINE_SHARD, 
            RuneCategory.FISHING,
            Arrays.asList("§7+25% Sea Creature Chance", "§7+10% Fishing XP"),
            2500
        ));
        
        // Foraging Runes
        runeTypes.put("foraging_rune", new RuneType(
            "Foraging Rune", 
            Material.OAK_LOG, 
            RuneCategory.FORAGING,
            Arrays.asList("§7+20% Foraging Speed", "§7+15% Wood Drop Rate"),
            1800
        ));
        
        runeTypes.put("tree_capitan_rune", new RuneType(
            "Tree Captain Rune", 
            Material.JUNGLE_LOG, 
            RuneCategory.FORAGING,
            Arrays.asList("§7+30% Tree Felling Speed", "§7+20% Sapling Drop Rate"),
            2200
        ));
        
        // Special Runes
        runeTypes.put("experience_rune", new RuneType(
            "Experience Rune", 
            Material.EXPERIENCE_BOTTLE, 
            RuneCategory.SPECIAL,
            Arrays.asList("§7+50% XP Gain", "§7+25% Skill XP"),
            5000
        ));
        
        runeTypes.put("coin_rune", new RuneType(
            "Coin Rune", 
            Material.GOLD_INGOT, 
            RuneCategory.SPECIAL,
            Arrays.asList("§7+30% Coin Gain", "§7+15% Treasure Drop Rate"),
            4000
        ));
        
        runeTypes.put("luck_rune", new RuneType(
            "Luck Rune", 
            Material.RABBIT_FOOT, 
            RuneCategory.SPECIAL,
            Arrays.asList("§7+20% Magic Find", "§7+10% Pet Luck"),
            3500
        ));
    }
    
    /**
     * Initialize all rune upgrades
     */
    private void initializeRuneUpgrades() {
        // Level 1 upgrades
        runeUpgrades.put("blood_rune_1", new RuneUpgrade(
            "Blood Rune I", 
            "blood_rune", 
            1, 
            Arrays.asList("§7+5% Damage", "§7+3% Critical Chance"),
            1000
        ));
        
        runeUpgrades.put("blood_rune_2", new RuneUpgrade(
            "Blood Rune II", 
            "blood_rune", 
            2, 
            Arrays.asList("§7+10% Damage", "§7+6% Critical Chance"),
            2500
        ));
        
        runeUpgrades.put("blood_rune_3", new RuneUpgrade(
            "Blood Rune III", 
            "blood_rune", 
            3, 
            Arrays.asList("§7+15% Damage", "§7+9% Critical Chance"),
            5000
        ));
        
        // Fire rune upgrades
        runeUpgrades.put("fire_rune_1", new RuneUpgrade(
            "Fire Rune I", 
            "fire_rune", 
            1, 
            Arrays.asList("§7+10% Fire Damage", "§7+5% Ignite Chance"),
            1500
        ));
        
        runeUpgrades.put("fire_rune_2", new RuneUpgrade(
            "Fire Rune II", 
            "fire_rune", 
            2, 
            Arrays.asList("§7+20% Fire Damage", "§7+10% Ignite Chance"),
            3750
        ));
        
        runeUpgrades.put("fire_rune_3", new RuneUpgrade(
            "Fire Rune III", 
            "fire_rune", 
            3, 
            Arrays.asList("§7+30% Fire Damage", "§7+15% Ignite Chance"),
            7500
        ));
        
        // Mining rune upgrades
        runeUpgrades.put("mining_rune_1", new RuneUpgrade(
            "Mining Rune I", 
            "mining_rune", 
            1, 
            Arrays.asList("§7+20% Mining Speed", "§7+10% Fortune"),
            2000
        ));
        
        runeUpgrades.put("mining_rune_2", new RuneUpgrade(
            "Mining Rune II", 
            "mining_rune", 
            2, 
            Arrays.asList("§7+40% Mining Speed", "§7+20% Fortune"),
            5000
        ));
        
        runeUpgrades.put("mining_rune_3", new RuneUpgrade(
            "Mining Rune III", 
            "mining_rune", 
            3, 
            Arrays.asList("§7+60% Mining Speed", "§7+30% Fortune"),
            10000
        ));
        
        // Experience rune upgrades
        runeUpgrades.put("experience_rune_1", new RuneUpgrade(
            "Experience Rune I", 
            "experience_rune", 
            1, 
            Arrays.asList("§7+50% XP Gain", "§7+25% Skill XP"),
            5000
        ));
        
        runeUpgrades.put("experience_rune_2", new RuneUpgrade(
            "Experience Rune II", 
            "experience_rune", 
            2, 
            Arrays.asList("§7+100% XP Gain", "§7+50% Skill XP"),
            12500
        ));
        
        runeUpgrades.put("experience_rune_3", new RuneUpgrade(
            "Experience Rune III", 
            "experience_rune", 
            3, 
            Arrays.asList("§7+150% XP Gain", "§7+75% Skill XP"),
            25000
        ));
    }
    
    /**
     * Start the rune task
     */
    private void startRuneTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveRuneEffects();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    /**
     * Update all active rune effects
     */
    private void updateActiveRuneEffects() {
        for (Map.Entry<UUID, List<RuneEffect>> entry : activeRuneEffects.entrySet()) {
            UUID playerId = entry.getKey();
            List<RuneEffect> effects = entry.getValue();
            
            // Remove expired effects
            effects.removeIf(RuneEffect::isExpired);
            
            // Apply active effects
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                applyRuneEffects(player, effects);
            }
        }
    }
    
    /**
     * Apply rune effects to a player
     */
    private void applyRuneEffects(Player player, List<RuneEffect> effects) {
        for (RuneEffect effect : effects) {
            effect.apply(player);
        }
    }
    
    /**
     * Get player rune data
     */
    public PlayerRuneData getPlayerRuneData(UUID playerId) {
        return playerRuneData.computeIfAbsent(playerId, k -> new PlayerRuneData());
    }
    
    /**
     * Get rune type by ID
     */
    public RuneType getRuneType(String runeId) {
        return runeTypes.get(runeId);
    }
    
    /**
     * Get rune upgrade by ID
     */
    public RuneUpgrade getRuneUpgrade(String upgradeId) {
        return runeUpgrades.get(upgradeId);
    }
    
    /**
     * Get all rune types
     */
    public Map<String, RuneType> getAllRuneTypes() {
        return new HashMap<>(runeTypes);
    }
    
    /**
     * Get all rune upgrades
     */
    public Map<String, RuneUpgrade> getAllRuneUpgrades() {
        return new HashMap<>(runeUpgrades);
    }
    
    /**
     * Get rune types by category
     */
    public Map<String, RuneType> getRuneTypesByCategory(RuneCategory category) {
        Map<String, RuneType> categoryRunes = new HashMap<>();
        for (Map.Entry<String, RuneType> entry : runeTypes.entrySet()) {
            if (entry.getValue().getCategory() == category) {
                categoryRunes.put(entry.getKey(), entry.getValue());
            }
        }
        return categoryRunes;
    }
    
    /**
     * Check if player can craft a rune
     */
    public boolean canCraftRune(Player player, String runeId) {
        PlayerRuneData data = getPlayerRuneData(player.getUniqueId());
        RuneType runeType = getRuneType(runeId);
        
        if (runeType == null) return false;
        
        // Check if player has enough coins
        if (data.getCoins() < runeType.getCost()) {
            return false;
        }
        
        // Check if player has required level
        if (data.getLevel() < getRequiredLevel(runeId)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Craft a rune
     */
    public boolean craftRune(Player player, String runeId) {
        if (!canCraftRune(player, runeId)) {
            return false;
        }
        
        RuneType runeType = getRuneType(runeId);
        PlayerRuneData data = getPlayerRuneData(player.getUniqueId());
        
        // Remove coins
        data.removeCoins(runeType.getCost());
        
        // Create rune item
        ItemStack rune = createRuneItem(runeType);
        player.getInventory().addItem(rune);
        
        // Update statistics
        data.incrementCraftedRunes();
        data.addExperience(runeType.getCost() / 20);
        
        return true;
    }
    
    /**
     * Create a rune item
     */
    private ItemStack createRuneItem(RuneType runeType) {
        ItemStack rune = new ItemStack(runeType.getMaterial());
        ItemMeta meta = rune.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§6" + runeType.getName());
            List<String> lore = new ArrayList<>(runeType.getEffects());
            lore.add("");
            lore.add("§7Right-click to apply this rune");
            lore.add("§7to a weapon, tool, or armor piece.");
            lore.add("");
            lore.add("§8Crafted with ancient magic");
            meta.setLore(lore);
            
            rune.setItemMeta(meta);
        }
        
        return rune;
    }
    
    /**
     * Apply a rune to an item
     */
    public boolean applyRune(Player player, ItemStack item, String runeId) {
        RuneType runeType = getRuneType(runeId);
        if (runeType == null) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        
        // Add rune effect to lore
        lore.add("§6" + runeType.getName() + ":");
        lore.addAll(runeType.getEffects());
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return true;
    }
    
    /**
     * Get required level for a rune
     */
    private int getRequiredLevel(String runeId) {
        // Base level requirements
        switch (runeId) {
            case "blood_rune", "fire_rune", "ice_rune", "lightning_rune":
                return 1;
            case "mining_rune", "farming_rune", "fishing_rune", "foraging_rune":
                return 5;
            case "gemstone_rune", "mithril_rune", "harvest_rune", "sea_creature_rune", "tree_capitan_rune":
                return 10;
            case "experience_rune", "coin_rune", "luck_rune":
                return 15;
            default:
                return 1;
        }
    }
    
    /**
     * Activate a rune effect
     */
    public void activateRuneEffect(UUID playerId, String runeId, int level, int duration) {
        RuneType runeType = getRuneType(runeId);
        if (runeType == null) return;
        
        List<RuneEffect> effects = activeRuneEffects.computeIfAbsent(playerId, k -> new ArrayList<>());
        
        RuneEffect effect = new RuneEffect(runeType, level, duration);
        effects.add(effect);
    }
    
    /**
     * Get active rune effects for a player
     */
    public List<RuneEffect> getActiveRuneEffects(UUID playerId) {
        return activeRuneEffects.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Get player's rune level
     */
    public int getRuneLevel(UUID playerId) {
        PlayerRuneData data = getPlayerRuneData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's rune experience
     */
    public int getRuneExperience(UUID playerId) {
        PlayerRuneData data = getPlayerRuneData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add rune experience to player
     */
    public void addRuneExperience(UUID playerId, int experience) {
        PlayerRuneData data = getPlayerRuneData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's rune coins
     */
    public int getRuneCoins(UUID playerId) {
        PlayerRuneData data = getPlayerRuneData(playerId);
        return data.getCoins();
    }
    
    /**
     * Add rune coins to player
     */
    public void addRuneCoins(UUID playerId, int coins) {
        PlayerRuneData data = getPlayerRuneData(playerId);
        data.addCoins(coins);
    }
    
    /**
     * Remove rune coins from player
     */
    public void removeRuneCoins(UUID playerId, int coins) {
        PlayerRuneData data = getPlayerRuneData(playerId);
        data.removeCoins(coins);
    }
    
    /**
     * Get player's rune statistics
     */
    public Map<String, Integer> getRuneStatistics(UUID playerId) {
        PlayerRuneData data = getPlayerRuneData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("coins", data.getCoins());
        stats.put("crafted_runes", data.getCraftedRunes());
        stats.put("total_experience", data.getTotalExperience());
        
        return stats;
    }
    
    /**
     * Reset player's rune data
     */
    public void resetRuneData(UUID playerId) {
        playerRuneData.remove(playerId);
        activeRuneEffects.remove(playerId);
    }
    
    /**
     * Save player's rune data
     */
    public void saveRuneData(UUID playerId) {
        PlayerRuneData data = getPlayerRuneData(playerId);
        // Save to database
        databaseManager.savePlayerRuneData(playerId, data);
    }
    
    /**
     * Load player's rune data
     */
    public void loadRuneData(UUID playerId) {
        try {
            CompletableFuture<Object> future = databaseManager.loadPlayerRuneData(playerId);
            PlayerRuneData data = (PlayerRuneData) future.get();
            if (data != null) {
                playerRuneData.put(playerId, data);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load rune data for player " + playerId + ": " + e.getMessage());
        }
    }
    
    /**
     * Shutdown the rune system
     */
    public void shutdown() {
        // Save all player data
        for (UUID playerId : playerRuneData.keySet()) {
            saveRuneData(playerId);
        }
        
        // Clear data
        playerRuneData.clear();
        activeRuneEffects.clear();
    }
}
