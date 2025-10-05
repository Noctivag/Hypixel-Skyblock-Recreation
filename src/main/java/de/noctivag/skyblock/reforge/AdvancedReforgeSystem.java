package de.noctivag.skyblock.reforge;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Advanced Reforge System for Hypixel Skyblock-style reforging
 * Includes reforge stones, item enhancement, and stat bonuses
 */
public class AdvancedReforgeSystem {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerReforgeData> playerReforgeData = new ConcurrentHashMap<>();
    private final Map<String, ReforgeStone> reforgeStones = new HashMap<>();
    private final Map<String, ReforgeType> reforgeTypes = new HashMap<>();
    private final Map<UUID, List<ReforgeEffect>> activeReforgeEffects = new ConcurrentHashMap<>();
    
    public AdvancedReforgeSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeReforgeStones();
        initializeReforgeTypes();
        startReforgeTask();
    }
    
    /**
     * Initialize all reforge stones
     */
    private void initializeReforgeStones() {
        // Common Reforge Stones
        reforgeStones.put("rough_stone", new ReforgeStone(
            "Rough Stone", 
            Material.COBBLESTONE, 
            ReforgeRarity.COMMON,
            Arrays.asList("§7A rough reforge stone", "§7Basic item enhancement"),
            100,
            "rough"
        ));
        
        reforgeStones.put("smooth_stone", new ReforgeStone(
            "Smooth Stone", 
            Material.STONE, 
            ReforgeRarity.COMMON,
            Arrays.asList("§7A smooth reforge stone", "§7Better item enhancement"),
            200,
            "smooth"
        ));
        
        reforgeStones.put("polished_stone", new ReforgeStone(
            "Polished Stone", 
            Material.STONE_BRICKS, 
            ReforgeRarity.COMMON,
            Arrays.asList("§7A polished reforge stone", "§7Good item enhancement"),
            300,
            "polished"
        ));
        
        // Uncommon Reforge Stones
        reforgeStones.put("iron_stone", new ReforgeStone(
            "Iron Stone", 
            Material.IRON_INGOT, 
            ReforgeRarity.UNCOMMON,
            Arrays.asList("§7An iron reforge stone", "§7Strong item enhancement"),
            500,
            "iron"
        ));
        
        reforgeStones.put("gold_stone", new ReforgeStone(
            "Gold Stone", 
            Material.GOLD_INGOT, 
            ReforgeRarity.UNCOMMON,
            Arrays.asList("§7A gold reforge stone", "§7Valuable item enhancement"),
            750,
            "gold"
        ));
        
        reforgeStones.put("copper_stone", new ReforgeStone(
            "Copper Stone", 
            Material.COPPER_INGOT, 
            ReforgeRarity.UNCOMMON,
            Arrays.asList("§7A copper reforge stone", "§7Durable item enhancement"),
            600,
            "copper"
        ));
        
        // Rare Reforge Stones
        reforgeStones.put("diamond_stone", new ReforgeStone(
            "Diamond Stone", 
            Material.DIAMOND, 
            ReforgeRarity.RARE,
            Arrays.asList("§7A diamond reforge stone", "§7Excellent item enhancement"),
            1500,
            "diamond"
        ));
        
        reforgeStones.put("emerald_stone", new ReforgeStone(
            "Emerald Stone", 
            Material.EMERALD, 
            ReforgeRarity.RARE,
            Arrays.asList("§7An emerald reforge stone", "§7Lucky item enhancement"),
            1200,
            "emerald"
        ));
        
        reforgeStones.put("lapis_stone", new ReforgeStone(
            "Lapis Stone", 
            Material.LAPIS_LAZULI, 
            ReforgeRarity.RARE,
            Arrays.asList("§7A lapis reforge stone", "§7Magical item enhancement"),
            1000,
            "lapis"
        ));
        
        // Epic Reforge Stones
        reforgeStones.put("netherite_stone", new ReforgeStone(
            "Netherite Stone", 
            Material.NETHERITE_INGOT, 
            ReforgeRarity.EPIC,
            Arrays.asList("§7A netherite reforge stone", "§7Powerful item enhancement"),
            3000,
            "netherite"
        ));
        
        reforgeStones.put("end_stone", new ReforgeStone(
            "End Stone", 
            Material.END_STONE, 
            ReforgeRarity.EPIC,
            Arrays.asList("§7An end reforge stone", "§7Mystical item enhancement"),
            2500,
            "end"
        ));
        
        reforgeStones.put("obsidian_stone", new ReforgeStone(
            "Obsidian Stone", 
            Material.OBSIDIAN, 
            ReforgeRarity.EPIC,
            Arrays.asList("§7An obsidian reforge stone", "§7Dark item enhancement"),
            2000,
            "obsidian"
        ));
        
        // Legendary Reforge Stones
        reforgeStones.put("dragon_stone", new ReforgeStone(
            "Dragon Stone", 
            Material.DRAGON_EGG, 
            ReforgeRarity.LEGENDARY,
            Arrays.asList("§7A dragon reforge stone", "§7Legendary item enhancement"),
            5000,
            "dragon"
        ));
        
        reforgeStones.put("phoenix_stone", new ReforgeStone(
            "Phoenix Stone", 
            Material.FIRE_CHARGE, 
            ReforgeRarity.LEGENDARY,
            Arrays.asList("§7A phoenix reforge stone", "§7Rebirth item enhancement"),
            4500,
            "phoenix"
        ));
        
        reforgeStones.put("void_stone", new ReforgeStone(
            "Void Stone", 
            Material.ENDER_EYE, 
            ReforgeRarity.LEGENDARY,
            Arrays.asList("§7A void reforge stone", "§7Void item enhancement"),
            4000,
            "void"
        ));
        
        // Mythic Reforge Stones
        reforgeStones.put("cosmic_stone", new ReforgeStone(
            "Cosmic Stone", 
            Material.NETHER_STAR, 
            ReforgeRarity.MYTHIC,
            Arrays.asList("§7A cosmic reforge stone", "§7Cosmic item enhancement"),
            10000,
            "cosmic"
        ));
        
        reforgeStones.put("divine_stone", new ReforgeStone(
            "Divine Stone", 
            Material.ENCHANTING_TABLE, 
            ReforgeRarity.MYTHIC,
            Arrays.asList("§7A divine reforge stone", "§7Divine item enhancement"),
            15000,
            "divine"
        ));
        
        reforgeStones.put("primal_stone", new ReforgeStone(
            "Primal Stone", 
            Material.ANCIENT_DEBRIS, 
            ReforgeRarity.MYTHIC,
            Arrays.asList("§7A primal reforge stone", "§7Primal item enhancement"),
            20000,
            "primal"
        ));
    }
    
    /**
     * Initialize all reforge types
     */
    private void initializeReforgeTypes() {
        // Rough Reforge
        reforgeTypes.put("rough", new ReforgeType(
            "Rough", 
            Arrays.asList("§7+1 Strength", "§7+1 Defense"),
            ReforgeRarity.COMMON
        ));
        
        // Smooth Reforge
        reforgeTypes.put("smooth", new ReforgeType(
            "Smooth", 
            Arrays.asList("§7+2 Strength", "§7+2 Defense"),
            ReforgeRarity.COMMON
        ));
        
        // Polished Reforge
        reforgeTypes.put("polished", new ReforgeType(
            "Polished", 
            Arrays.asList("§7+3 Strength", "§7+3 Defense"),
            ReforgeRarity.COMMON
        ));
        
        // Iron Reforge
        reforgeTypes.put("iron", new ReforgeType(
            "Iron", 
            Arrays.asList("§7+5 Strength", "§7+5 Defense"),
            ReforgeRarity.UNCOMMON
        ));
        
        // Gold Reforge
        reforgeTypes.put("gold", new ReforgeType(
            "Gold", 
            Arrays.asList("§7+3 Strength", "§7+7 Defense"),
            ReforgeRarity.UNCOMMON
        ));
        
        // Copper Reforge
        reforgeTypes.put("copper", new ReforgeType(
            "Copper", 
            Arrays.asList("§7+4 Strength", "§7+4 Defense", "§7+2 Speed"),
            ReforgeRarity.UNCOMMON
        ));
        
        // Diamond Reforge
        reforgeTypes.put("diamond", new ReforgeType(
            "Diamond", 
            Arrays.asList("§7+8 Strength", "§7+8 Defense"),
            ReforgeRarity.RARE
        ));
        
        // Emerald Reforge
        reforgeTypes.put("emerald", new ReforgeType(
            "Emerald", 
            Arrays.asList("§7+6 Strength", "§7+6 Defense", "§7+5% Critical Chance"),
            ReforgeRarity.RARE
        ));
        
        // Lapis Reforge
        reforgeTypes.put("lapis", new ReforgeType(
            "Lapis", 
            Arrays.asList("§7+4 Strength", "§7+4 Defense", "§7+10% Mana"),
            ReforgeRarity.RARE
        ));
        
        // Netherite Reforge
        reforgeTypes.put("netherite", new ReforgeType(
            "Netherite", 
            Arrays.asList("§7+12 Strength", "§7+12 Defense", "§7+5% Critical Damage"),
            ReforgeRarity.EPIC
        ));
        
        // End Reforge
        reforgeTypes.put("end", new ReforgeType(
            "End", 
            Arrays.asList("§7+10 Strength", "§7+10 Defense", "§7+8% Critical Chance"),
            ReforgeRarity.EPIC
        ));
        
        // Obsidian Reforge
        reforgeTypes.put("obsidian", new ReforgeType(
            "Obsidian", 
            Arrays.asList("§7+15 Defense", "§7+5 Strength", "§7+10% Health"),
            ReforgeRarity.EPIC
        ));
        
        // Dragon Reforge
        reforgeTypes.put("dragon", new ReforgeType(
            "Dragon", 
            Arrays.asList("§7+20 Strength", "§7+20 Defense", "§7+10% Critical Damage"),
            ReforgeRarity.LEGENDARY
        ));
        
        // Phoenix Reforge
        reforgeTypes.put("phoenix", new ReforgeType(
            "Phoenix", 
            Arrays.asList("§7+15 Strength", "§7+15 Defense", "§7+15% Health", "§7+5% Critical Chance"),
            ReforgeRarity.LEGENDARY
        ));
        
        // Void Reforge
        reforgeTypes.put("void", new ReforgeType(
            "Void", 
            Arrays.asList("§7+18 Strength", "§7+18 Defense", "§7+12% Critical Damage", "§7+8% Critical Chance"),
            ReforgeRarity.LEGENDARY
        ));
        
        // Cosmic Reforge
        reforgeTypes.put("cosmic", new ReforgeType(
            "Cosmic", 
            Arrays.asList("§7+25 Strength", "§7+25 Defense", "§7+15% Critical Damage", "§7+10% Critical Chance"),
            ReforgeRarity.MYTHIC
        ));
        
        // Divine Reforge
        reforgeTypes.put("divine", new ReforgeType(
            "Divine", 
            Arrays.asList("§7+30 Strength", "§7+30 Defense", "§7+20% Critical Damage", "§7+15% Critical Chance"),
            ReforgeRarity.MYTHIC
        ));
        
        // Primal Reforge
        reforgeTypes.put("primal", new ReforgeType(
            "Primal", 
            Arrays.asList("§7+35 Strength", "§7+35 Defense", "§7+25% Critical Damage", "§7+20% Critical Chance"),
            ReforgeRarity.MYTHIC
        ));
    }
    
    /**
     * Start the reforge task
     */
    private void startReforgeTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveReforgeEffects();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L); // Every second
    }
    
    /**
     * Update all active reforge effects
     */
    private void updateActiveReforgeEffects() {
        for (Map.Entry<UUID, List<ReforgeEffect>> entry : activeReforgeEffects.entrySet()) {
            UUID playerId = entry.getKey();
            List<ReforgeEffect> effects = entry.getValue();
            
            // Remove expired effects
            effects.removeIf(ReforgeEffect::isExpired);
            
            // Apply active effects
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                applyReforgeEffects(player, effects);
            }
        }
    }
    
    /**
     * Apply reforge effects to a player
     */
    private void applyReforgeEffects(Player player, List<ReforgeEffect> effects) {
        for (ReforgeEffect effect : effects) {
            effect.apply(player);
        }
    }
    
    /**
     * Get player reforge data
     */
    public PlayerReforgeData getPlayerReforgeData(UUID playerId) {
        return playerReforgeData.computeIfAbsent(playerId, k -> new PlayerReforgeData());
    }
    
    /**
     * Get reforge stone by ID
     */
    public ReforgeStone getReforgeStone(String stoneId) {
        return reforgeStones.get(stoneId);
    }
    
    /**
     * Get reforge type by ID
     */
    public ReforgeType getReforgeType(String typeId) {
        return reforgeTypes.get(typeId);
    }
    
    /**
     * Get all reforge stones
     */
    public Map<String, ReforgeStone> getAllReforgeStones() {
        return new HashMap<>(reforgeStones);
    }
    
    /**
     * Get all reforge types
     */
    public Map<String, ReforgeType> getAllReforgeTypes() {
        return new HashMap<>(reforgeTypes);
    }
    
    /**
     * Get reforge stones by rarity
     */
    public Map<String, ReforgeStone> getReforgeStonesByRarity(ReforgeRarity rarity) {
        Map<String, ReforgeStone> rarityStones = new HashMap<>();
        for (Map.Entry<String, ReforgeStone> entry : reforgeStones.entrySet()) {
            if (entry.getValue().getRarity() == rarity) {
                rarityStones.put(entry.getKey(), entry.getValue());
            }
        }
        return rarityStones;
    }
    
    /**
     * Check if player can reforge an item
     */
    public boolean canReforgeItem(Player player, ItemStack item, String stoneId) {
        ReforgeStone stone = getReforgeStone(stoneId);
        if (stone == null) return false;
        
        // Check if item can be reforged
        if (!canItemBeReforged(item)) {
            return false;
        }
        
        // Check if player has enough coins
        PlayerReforgeData data = getPlayerReforgeData(player.getUniqueId());
        if (data.getCoins() < stone.getCost()) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Check if an item can be reforged
     */
    private boolean canItemBeReforged(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        
        // Check if item is a weapon, tool, or armor
        Material type = item.getType();
        return type.name().contains("SWORD") || type.name().contains("AXE") || 
               type.name().contains("PICKAXE") || type.name().contains("SHOVEL") || 
               type.name().contains("HOE") || type.name().contains("HELMET") || 
               type.name().contains("CHESTPLATE") || type.name().contains("LEGGINGS") || 
               type.name().contains("BOOTS") || type.name().contains("BOW") || 
               type.name().contains("CROSSBOW") || type.name().contains("TRIDENT");
    }
    
    /**
     * Reforge an item
     */
    public boolean reforgeItem(Player player, ItemStack item, String stoneId) {
        if (!canReforgeItem(player, item, stoneId)) {
            return false;
        }
        
        ReforgeStone stone = getReforgeStone(stoneId);
        ReforgeType reforgeType = getReforgeType(stone.getReforgeType());
        PlayerReforgeData data = getPlayerReforgeData(player.getUniqueId());
        
        // Remove coins
        data.removeCoins(stone.getCost());
        
        // Apply reforge to item
        applyReforgeToItem(item, reforgeType);
        
        // Update statistics
        data.incrementReforgedItems();
        data.addExperience(stone.getCost() / 10);
        
        return true;
    }
    
    /**
     * Apply reforge to an item
     */
    private void applyReforgeToItem(ItemStack item, ReforgeType reforgeType) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        
        // Remove existing reforge
        lore.removeIf(line -> line.startsWith("§6Reforge:"));
        
        // Add new reforge
        lore.add("§6Reforge: " + reforgeType.getName());
        lore.addAll(reforgeType.getEffects());
        
        meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
        item.setItemMeta(meta);
    }
    
    /**
     * Create a reforge stone item
     */
    public ItemStack createReforgeStoneItem(String stoneId) {
        ReforgeStone stone = getReforgeStone(stoneId);
        if (stone == null) return null;
        
        ItemStack item = new ItemStack(stone.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(stone.getRarity().getColor() + stone.getName()));
            List<String> lore = new ArrayList<>(stone.getDescription());
            lore.add("");
            lore.add("§7Rarity: " + stone.getRarity().getDisplayName());
            lore.add("§7Cost: §a" + stone.getCost() + " coins");
            lore.add("§7Reforge Type: §a" + stone.getReforgeType());
            lore.add("");
            lore.add("§7Right-click on a weapon, tool,");
            lore.add("§7or armor piece to reforge it!");
            lore.add("");
            lore.add("§8A reforge stone");
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Activate a reforge effect
     */
    public void activateReforgeEffect(UUID playerId, String reforgeType, int level, int duration) {
        ReforgeType type = getReforgeType(reforgeType);
        if (type == null) return;
        
        List<ReforgeEffect> effects = activeReforgeEffects.computeIfAbsent(playerId, k -> new ArrayList<>());
        
        ReforgeEffect effect = new ReforgeEffect(type, level, duration);
        effects.add(effect);
    }
    
    /**
     * Get active reforge effects for a player
     */
    public List<ReforgeEffect> getActiveReforgeEffects(UUID playerId) {
        return activeReforgeEffects.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Get player's reforge level
     */
    public int getReforgeLevel(UUID playerId) {
        PlayerReforgeData data = getPlayerReforgeData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's reforge experience
     */
    public int getReforgeExperience(UUID playerId) {
        PlayerReforgeData data = getPlayerReforgeData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add reforge experience to player
     */
    public void addReforgeExperience(UUID playerId, int experience) {
        PlayerReforgeData data = getPlayerReforgeData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's reforge coins
     */
    public int getReforgeCoins(UUID playerId) {
        PlayerReforgeData data = getPlayerReforgeData(playerId);
        return data.getCoins();
    }
    
    /**
     * Add reforge coins to player
     */
    public void addReforgeCoins(UUID playerId, int coins) {
        PlayerReforgeData data = getPlayerReforgeData(playerId);
        data.addCoins(coins);
    }
    
    /**
     * Remove reforge coins from player
     */
    public void removeReforgeCoins(UUID playerId, int coins) {
        PlayerReforgeData data = getPlayerReforgeData(playerId);
        data.removeCoins(coins);
    }
    
    /**
     * Get player's reforge statistics
     */
    public Map<String, Integer> getReforgeStatistics(UUID playerId) {
        PlayerReforgeData data = getPlayerReforgeData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("coins", data.getCoins());
        stats.put("reforged_items", data.getReforgedItems());
        stats.put("total_experience", data.getTotalExperience());
        
        return stats;
    }
    
    /**
     * Reset player's reforge data
     */
    public void resetReforgeData(UUID playerId) {
        playerReforgeData.remove(playerId);
        activeReforgeEffects.remove(playerId);
    }
    
    /**
     * Save player's reforge data
     */
    public void saveReforgeData(UUID playerId) {
        PlayerReforgeData data = getPlayerReforgeData(playerId);
        // Save to database
        databaseManager.savePlayerReforgeData(playerId, data);
    }
    
    /**
     * Load player's reforge data
     */
    public void loadReforgeData(UUID playerId) {
        try {
            CompletableFuture<Object> future = databaseManager.loadPlayerReforgeData(playerId);
            PlayerReforgeData data = (PlayerReforgeData) future.get();
            if (data != null) {
                playerReforgeData.put(playerId, data);
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Failed to load reforge data for player " + playerId + ": " + e.getMessage());
        }
    }
    
    /**
     * Shutdown the reforge system
     */
    public void shutdown() {
        // Save all player data
        for (UUID playerId : playerReforgeData.keySet()) {
            saveReforgeData(playerId);
        }
        
        // Clear data
        playerReforgeData.clear();
        activeReforgeEffects.clear();
    }
}
