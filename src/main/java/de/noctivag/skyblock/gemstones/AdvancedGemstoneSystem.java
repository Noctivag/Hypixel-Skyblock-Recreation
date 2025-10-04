package de.noctivag.skyblock.gemstones;
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
 * Advanced Gemstone System for Hypixel Skyblock-style gemstones
 * Includes gemstone types, mining, crafting, and enchanting
 */
public class AdvancedGemstoneSystem {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerGemstoneData> playerGemstoneData = new ConcurrentHashMap<>();
    private final Map<String, GemstoneType> gemstoneTypes = new HashMap<>();
    private final Map<String, GemstoneSlot> gemstoneSlots = new HashMap<>();
    private final Map<UUID, List<GemstoneEffect>> activeGemstoneEffects = new ConcurrentHashMap<>();
    
    public AdvancedGemstoneSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeGemstoneTypes();
        initializeGemstoneSlots();
        startGemstoneTask();
    }
    
    /**
     * Initialize all gemstone types
     */
    private void initializeGemstoneTypes() {
        // Combat Gemstones
        gemstoneTypes.put("ruby", new GemstoneType(
            "Ruby", 
            Material.REDSTONE, 
            GemstoneCategory.COMBAT,
            GemstoneRarity.COMMON,
            Arrays.asList("§7+2 Strength", "§7+1% Critical Chance"),
            1000,
            500
        ));
        
        gemstoneTypes.put("sapphire", new GemstoneType(
            "Sapphire", 
            Material.LAPIS_LAZULI, 
            GemstoneCategory.COMBAT,
            GemstoneRarity.COMMON,
            Arrays.asList("§7+2 Strength", "§7+1% Critical Chance"),
            1000,
            500
        ));
        
        gemstoneTypes.put("amber", new GemstoneType(
            "Amber", 
            Material.GOLD_NUGGET, 
            GemstoneCategory.COMBAT,
            GemstoneRarity.COMMON,
            Arrays.asList("§7+2 Strength", "§7+1% Critical Chance"),
            1000,
            500
        ));
        
        gemstoneTypes.put("topaz", new GemstoneType(
            "Topaz", 
            Material.GLOWSTONE_DUST, 
            GemstoneCategory.COMBAT,
            GemstoneRarity.COMMON,
            Arrays.asList("§7+2 Strength", "§7+1% Critical Chance"),
            1000,
            500
        ));
        
        // Mining Gemstones
        gemstoneTypes.put("jasper", new GemstoneType(
            "Jasper", 
            Material.EMERALD, 
            GemstoneCategory.MINING,
            GemstoneRarity.UNCOMMON,
            Arrays.asList("§7+4 Mining Speed", "§7+2% Mining Fortune"),
            2500,
            1250
        ));
        
        gemstoneTypes.put("amethyst", new GemstoneType(
            "Amethyst", 
            Material.AMETHYST_SHARD, 
            GemstoneCategory.MINING,
            GemstoneRarity.UNCOMMON,
            Arrays.asList("§7+4 Mining Speed", "§7+2% Mining Fortune"),
            2500,
            1250
        ));
        
        gemstoneTypes.put("jade", new GemstoneType(
            "Jade", 
            Material.GREEN_DYE, 
            GemstoneCategory.MINING,
            GemstoneRarity.RARE,
            Arrays.asList("§7+6 Mining Speed", "§7+3% Mining Fortune"),
            5000,
            2500
        ));
        
        gemstoneTypes.put("opal", new GemstoneType(
            "Opal", 
            Material.QUARTZ, 
            GemstoneCategory.MINING,
            GemstoneRarity.RARE,
            Arrays.asList("§7+6 Mining Speed", "§7+3% Mining Fortune"),
            5000,
            2500
        ));
        
        // Farming Gemstones
        gemstoneTypes.put("diamond", new GemstoneType(
            "Diamond", 
            Material.DIAMOND, 
            GemstoneCategory.FARMING,
            GemstoneRarity.EPIC,
            Arrays.asList("§7+8 Farming Fortune", "§7+4% Crop Growth Speed"),
            10000,
            5000
        ));
        
        gemstoneTypes.put("peridot", new GemstoneType(
            "Peridot", 
            Material.LIME_DYE, 
            GemstoneCategory.FARMING,
            GemstoneRarity.EPIC,
            Arrays.asList("§7+8 Farming Fortune", "§7+4% Crop Growth Speed"),
            10000,
            5000
        ));
        
        // Fishing Gemstones
        gemstoneTypes.put("aquamarine", new GemstoneType(
            "Aquamarine", 
            Material.PRISMARINE_CRYSTALS, 
            GemstoneCategory.FISHING,
            GemstoneRarity.LEGENDARY,
            Arrays.asList("§7+10 Fishing Speed", "§7+5% Sea Creature Chance"),
            25000,
            12500
        ));
        
        gemstoneTypes.put("citrine", new GemstoneType(
            "Citrine", 
            Material.YELLOW_DYE, 
            GemstoneCategory.FISHING,
            GemstoneRarity.LEGENDARY,
            Arrays.asList("§7+10 Fishing Speed", "§7+5% Sea Creature Chance"),
            25000,
            12500
        ));
        
        // Foraging Gemstones
        gemstoneTypes.put("onyx", new GemstoneType(
            "Onyx", 
            Material.BLACK_DYE, 
            GemstoneCategory.FORAGING,
            GemstoneRarity.MYTHIC,
            Arrays.asList("§7+12 Foraging Speed", "§7+6% Wood Drop Rate"),
            50000,
            25000
        ));
        
        gemstoneTypes.put("moonstone", new GemstoneType(
            "Moonstone", 
            Material.WHITE_DYE, 
            GemstoneCategory.FORAGING,
            GemstoneRarity.MYTHIC,
            Arrays.asList("§7+12 Foraging Speed", "§7+6% Wood Drop Rate"),
            50000,
            25000
        ));
        
        // Special Gemstones
        gemstoneTypes.put("garnet", new GemstoneType(
            "Garnet", 
            Material.RED_DYE, 
            GemstoneCategory.SPECIAL,
            GemstoneRarity.LEGENDARY,
            Arrays.asList("§7+15% Magic Find", "§7+10% Pet Luck"),
            30000,
            15000
        ));
        
        gemstoneTypes.put("rubellite", new GemstoneType(
            "Rubellite", 
            Material.PINK_DYE, 
            GemstoneCategory.SPECIAL,
            GemstoneRarity.MYTHIC,
            Arrays.asList("§7+20% Magic Find", "§7+15% Pet Luck"),
            75000,
            37500
        ));
    }
    
    /**
     * Initialize all gemstone slots
     */
    private void initializeGemstoneSlots() {
        // Combat Slots
        gemstoneSlots.put("combat_1", new GemstoneSlot("Combat Slot 1", GemstoneCategory.COMBAT, 1));
        gemstoneSlots.put("combat_2", new GemstoneSlot("Combat Slot 2", GemstoneCategory.COMBAT, 2));
        gemstoneSlots.put("combat_3", new GemstoneSlot("Combat Slot 3", GemstoneCategory.COMBAT, 3));
        
        // Mining Slots
        gemstoneSlots.put("mining_1", new GemstoneSlot("Mining Slot 1", GemstoneCategory.MINING, 1));
        gemstoneSlots.put("mining_2", new GemstoneSlot("Mining Slot 2", GemstoneCategory.MINING, 2));
        gemstoneSlots.put("mining_3", new GemstoneSlot("Mining Slot 3", GemstoneCategory.MINING, 3));
        
        // Farming Slots
        gemstoneSlots.put("farming_1", new GemstoneSlot("Farming Slot 1", GemstoneCategory.FARMING, 1));
        gemstoneSlots.put("farming_2", new GemstoneSlot("Farming Slot 2", GemstoneCategory.FARMING, 2));
        gemstoneSlots.put("farming_3", new GemstoneSlot("Farming Slot 3", GemstoneCategory.FARMING, 3));
        
        // Fishing Slots
        gemstoneSlots.put("fishing_1", new GemstoneSlot("Fishing Slot 1", GemstoneCategory.FISHING, 1));
        gemstoneSlots.put("fishing_2", new GemstoneSlot("Fishing Slot 2", GemstoneCategory.FISHING, 2));
        gemstoneSlots.put("fishing_3", new GemstoneSlot("Fishing Slot 3", GemstoneCategory.FISHING, 3));
        
        // Foraging Slots
        gemstoneSlots.put("foraging_1", new GemstoneSlot("Foraging Slot 1", GemstoneCategory.FORAGING, 1));
        gemstoneSlots.put("foraging_2", new GemstoneSlot("Foraging Slot 2", GemstoneCategory.FORAGING, 2));
        gemstoneSlots.put("foraging_3", new GemstoneSlot("Foraging Slot 3", GemstoneCategory.FORAGING, 3));
        
        // Special Slots
        gemstoneSlots.put("special_1", new GemstoneSlot("Special Slot 1", GemstoneCategory.SPECIAL, 1));
        gemstoneSlots.put("special_2", new GemstoneSlot("Special Slot 2", GemstoneCategory.SPECIAL, 2));
        gemstoneSlots.put("special_3", new GemstoneSlot("Special Slot 3", GemstoneCategory.SPECIAL, 3));
    }
    
    /**
     * Start the gemstone task
     */
    private void startGemstoneTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveGemstoneEffects();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    /**
     * Update all active gemstone effects
     */
    private void updateActiveGemstoneEffects() {
        for (Map.Entry<UUID, List<GemstoneEffect>> entry : activeGemstoneEffects.entrySet()) {
            UUID playerId = entry.getKey();
            List<GemstoneEffect> effects = entry.getValue();
            
            // Remove expired effects
            effects.removeIf(GemstoneEffect::isExpired);
            
            // Apply active effects
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                applyGemstoneEffects(player, effects);
            }
        }
    }
    
    /**
     * Apply gemstone effects to a player
     */
    private void applyGemstoneEffects(Player player, List<GemstoneEffect> effects) {
        for (GemstoneEffect effect : effects) {
            effect.apply(player);
        }
    }
    
    /**
     * Get player gemstone data
     */
    public PlayerGemstoneData getPlayerGemstoneData(UUID playerId) {
        return playerGemstoneData.computeIfAbsent(playerId, k -> new PlayerGemstoneData());
    }
    
    /**
     * Get gemstone type by ID
     */
    public GemstoneType getGemstoneType(String gemstoneId) {
        return gemstoneTypes.get(gemstoneId);
    }
    
    /**
     * Get gemstone slot by ID
     */
    public GemstoneSlot getGemstoneSlot(String slotId) {
        return gemstoneSlots.get(slotId);
    }
    
    /**
     * Get all gemstone types
     */
    public Map<String, GemstoneType> getAllGemstoneTypes() {
        return new HashMap<>(gemstoneTypes);
    }
    
    /**
     * Get all gemstone slots
     */
    public Map<String, GemstoneSlot> getAllGemstoneSlots() {
        return new HashMap<>(gemstoneSlots);
    }
    
    /**
     * Get gemstone types by category
     */
    public Map<String, GemstoneType> getGemstoneTypesByCategory(GemstoneCategory category) {
        Map<String, GemstoneType> categoryGemstones = new HashMap<>();
        for (Map.Entry<String, GemstoneType> entry : gemstoneTypes.entrySet()) {
            if (entry.getValue().getCategory() == category) {
                categoryGemstones.put(entry.getKey(), entry.getValue());
            }
        }
        return categoryGemstones;
    }
    
    /**
     * Get gemstone types by rarity
     */
    public Map<String, GemstoneType> getGemstoneTypesByRarity(GemstoneRarity rarity) {
        Map<String, GemstoneType> rarityGemstones = new HashMap<>();
        for (Map.Entry<String, GemstoneType> entry : gemstoneTypes.entrySet()) {
            if (entry.getValue().getRarity() == rarity) {
                rarityGemstones.put(entry.getKey(), entry.getValue());
            }
        }
        return rarityGemstones;
    }
    
    /**
     * Check if player can mine a gemstone
     */
    public boolean canMineGemstone(Player player, String gemstoneId) {
        PlayerGemstoneData data = getPlayerGemstoneData(player.getUniqueId());
        GemstoneType gemstone = getGemstoneType(gemstoneId);
        
        if (gemstone == null) return false;
        
        // Check if player has required level
        if (data.getLevel() < getRequiredLevel(gemstoneId)) {
            return false;
        }
        
        // Check if player has required mining level
        if (data.getMiningLevel() < getRequiredMiningLevel(gemstoneId)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Mine a gemstone
     */
    public boolean mineGemstone(Player player, String gemstoneId) {
        if (!canMineGemstone(player, gemstoneId)) {
            return false;
        }
        
        GemstoneType gemstone = getGemstoneType(gemstoneId);
        PlayerGemstoneData data = getPlayerGemstoneData(player.getUniqueId());
        
        // Create gemstone item
        ItemStack gemstoneItem = createGemstoneItem(gemstone);
        player.getInventory().addItem(gemstoneItem);
        
        // Update statistics
        data.incrementMinedGemstones();
        data.addExperience(gemstone.getExperienceValue());
        
        return true;
    }
    
    /**
     * Get required level for a gemstone
     */
    private int getRequiredLevel(String gemstoneId) {
        GemstoneType gemstone = getGemstoneType(gemstoneId);
        if (gemstone == null) return 1;
        
        switch (gemstone.getRarity()) {
            case COMMON: return 1;
            case UNCOMMON: return 5;
            case RARE: return 10;
            case EPIC: return 15;
            case LEGENDARY: return 20;
            case MYTHIC: return 25;
            default: return 1;
        }
    }
    
    /**
     * Get required mining level for a gemstone
     */
    private int getRequiredMiningLevel(String gemstoneId) {
        GemstoneType gemstone = getGemstoneType(gemstoneId);
        if (gemstone == null) return 1;
        
        switch (gemstone.getRarity()) {
            case COMMON: return 1;
            case UNCOMMON: return 10;
            case RARE: return 20;
            case EPIC: return 30;
            case LEGENDARY: return 40;
            case MYTHIC: return 50;
            default: return 1;
        }
    }
    
    /**
     * Create a gemstone item
     */
    private ItemStack createGemstoneItem(GemstoneType gemstone) {
        ItemStack item = new ItemStack(gemstone.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(gemstone.getRarity().getColor() + gemstone.getName() + " Gemstone");
            List<String> lore = new ArrayList<>(gemstone.getEffects());
            lore.add("");
            lore.add("§7Category: " + gemstone.getCategory().getDisplayName());
            lore.add("§7Rarity: " + gemstone.getRarity().getDisplayName());
            lore.add("§7Value: §a" + gemstone.getValue() + " coins");
            lore.add("§7Experience: §a" + gemstone.getExperienceValue() + " XP");
            lore.add("");
            lore.add("§7Can be applied to items");
            lore.add("§7to enhance their abilities.");
            lore.add("");
            lore.add("§8A precious gemstone");
            meta.setLore(lore);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Apply a gemstone to an item
     */
    public boolean applyGemstone(Player player, ItemStack item, String gemstoneId, String slotId) {
        GemstoneType gemstone = getGemstoneType(gemstoneId);
        GemstoneSlot slot = getGemstoneSlot(slotId);
        
        if (gemstone == null || slot == null) return false;
        
        // Check if gemstone category matches slot category
        if (gemstone.getCategory() != slot.getCategory()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        
        // Add gemstone effect to lore
        lore.add(gemstone.getRarity().getColor() + gemstone.getName() + " Gemstone (" + slot.getName() + "):");
        lore.addAll(gemstone.getEffects());
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return true;
    }
    
    /**
     * Activate a gemstone effect
     */
    public void activateGemstoneEffect(UUID playerId, String gemstoneId, int level, int duration) {
        GemstoneType gemstone = getGemstoneType(gemstoneId);
        if (gemstone == null) return;
        
        List<GemstoneEffect> effects = activeGemstoneEffects.computeIfAbsent(playerId, k -> new ArrayList<>());
        
        GemstoneEffect effect = new GemstoneEffect(gemstone, level, duration);
        effects.add(effect);
    }
    
    /**
     * Get active gemstone effects for a player
     */
    public List<GemstoneEffect> getActiveGemstoneEffects(UUID playerId) {
        return activeGemstoneEffects.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Get player's gemstone level
     */
    public int getGemstoneLevel(UUID playerId) {
        PlayerGemstoneData data = getPlayerGemstoneData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's gemstone experience
     */
    public int getGemstoneExperience(UUID playerId) {
        PlayerGemstoneData data = getPlayerGemstoneData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add gemstone experience to player
     */
    public void addGemstoneExperience(UUID playerId, int experience) {
        PlayerGemstoneData data = getPlayerGemstoneData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's gemstone statistics
     */
    public Map<String, Integer> getGemstoneStatistics(UUID playerId) {
        PlayerGemstoneData data = getPlayerGemstoneData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("mining_level", data.getMiningLevel());
        stats.put("mined_gemstones", data.getMinedGemstones());
        stats.put("total_experience", data.getTotalExperience());
        
        return stats;
    }
    
    /**
     * Reset player's gemstone data
     */
    public void resetGemstoneData(UUID playerId) {
        playerGemstoneData.remove(playerId);
        activeGemstoneEffects.remove(playerId);
    }
    
    /**
     * Save player's gemstone data
     */
    public void saveGemstoneData(UUID playerId) {
        PlayerGemstoneData data = getPlayerGemstoneData(playerId);
        // Save to database
        databaseManager.savePlayerGemstoneData(playerId, data);
    }
    
    /**
     * Load player's gemstone data
     */
    public void loadGemstoneData(UUID playerId) {
        try {
            CompletableFuture<Object> future = databaseManager.loadPlayerGemstoneData(playerId);
            PlayerGemstoneData data = (PlayerGemstoneData) future.get();
            if (data != null) {
                playerGemstoneData.put(playerId, data);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load gemstone data for player " + playerId + ": " + e.getMessage());
        }
    }
    
    /**
     * Shutdown the gemstone system
     */
    public void shutdown() {
        // Save all player data
        for (UUID playerId : playerGemstoneData.keySet()) {
            saveGemstoneData(playerId);
        }
        
        // Clear data
        playerGemstoneData.clear();
        activeGemstoneEffects.clear();
    }
}
