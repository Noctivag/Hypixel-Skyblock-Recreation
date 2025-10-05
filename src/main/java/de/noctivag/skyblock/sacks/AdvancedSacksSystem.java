package de.noctivag.skyblock.sacks;
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

/**
 * Advanced Sacks System for Hypixel Skyblock-style sacks
 * Includes auto collection, storage, and item management
 */
public class AdvancedSacksSystem {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerSackData> playerSackData = new ConcurrentHashMap<>();
    private final Map<String, SackType> sackTypes = new HashMap<>();
    private final Map<UUID, List<SackItem>> playerSacks = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> autoCollectionEnabled = new ConcurrentHashMap<>();
    
    public AdvancedSacksSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeSackTypes();
        startSackTask();
    }
    
    /**
     * Initialize all sack types
     */
    private void initializeSackTypes() {
        // Farming Sacks
        sackTypes.put("wheat_sack", new SackType(
            "Wheat Sack", 
            Material.WHEAT, 
            SackCategory.FARMING,
            Arrays.asList("§7Automatically collects wheat", "§7Increases wheat storage capacity"),
            1000,
            10000
        ));
        
        sackTypes.put("carrot_sack", new SackType(
            "Carrot Sack", 
            Material.CARROT, 
            SackCategory.FARMING,
            Arrays.asList("§7Automatically collects carrots", "§7Increases carrot storage capacity"),
            1000,
            10000
        ));
        
        sackTypes.put("potato_sack", new SackType(
            "Potato Sack", 
            Material.POTATO, 
            SackCategory.FARMING,
            Arrays.asList("§7Automatically collects potatoes", "§7Increases potato storage capacity"),
            1000,
            10000
        ));
        
        sackTypes.put("pumpkin_sack", new SackType(
            "Pumpkin Sack", 
            Material.PUMPKIN, 
            SackCategory.FARMING,
            Arrays.asList("§7Automatically collects pumpkins", "§7Increases pumpkin storage capacity"),
            1500,
            5000
        ));
        
        sackTypes.put("melons_sack", new SackType(
            "Melon Sack", 
            Material.MELON, 
            SackCategory.FARMING,
            Arrays.asList("§7Automatically collects melons", "§7Increases melon storage capacity"),
            1500,
            5000
        ));
        
        sackTypes.put("sugar_cane_sack", new SackType(
            "Sugar Cane Sack", 
            Material.SUGAR_CANE, 
            SackCategory.FARMING,
            Arrays.asList("§7Automatically collects sugar cane", "§7Increases sugar cane storage capacity"),
            1200,
            8000
        ));
        
        sackTypes.put("cocoa_sack", new SackType(
            "Cocoa Sack", 
            Material.COCOA_BEANS, 
            SackCategory.FARMING,
            Arrays.asList("§7Automatically collects cocoa beans", "§7Increases cocoa storage capacity"),
            1200,
            8000
        ));
        
        sackTypes.put("nether_wart_sack", new SackType(
            "Nether Wart Sack", 
            Material.NETHER_WART, 
            SackCategory.FARMING,
            Arrays.asList("§7Automatically collects nether wart", "§7Increases nether wart storage capacity"),
            2000,
            5000
        ));
        
        // Mining Sacks
        sackTypes.put("cobblestone_sack", new SackType(
            "Cobblestone Sack", 
            Material.COBBLESTONE, 
            SackCategory.MINING,
            Arrays.asList("§7Automatically collects cobblestone", "§7Increases cobblestone storage capacity"),
            500,
            50000
        ));
        
        sackTypes.put("coal_sack", new SackType(
            "Coal Sack", 
            Material.COAL, 
            SackCategory.MINING,
            Arrays.asList("§7Automatically collects coal", "§7Increases coal storage capacity"),
            800,
            25000
        ));
        
        sackTypes.put("iron_sack", new SackType(
            "Iron Sack", 
            Material.IRON_INGOT, 
            SackCategory.MINING,
            Arrays.asList("§7Automatically collects iron", "§7Increases iron storage capacity"),
            1000,
            15000
        ));
        
        sackTypes.put("gold_sack", new SackType(
            "Gold Sack", 
            Material.GOLD_INGOT, 
            SackCategory.MINING,
            Arrays.asList("§7Automatically collects gold", "§7Increases gold storage capacity"),
            1500,
            10000
        ));
        
        sackTypes.put("diamond_sack", new SackType(
            "Diamond Sack", 
            Material.DIAMOND, 
            SackCategory.MINING,
            Arrays.asList("§7Automatically collects diamonds", "§7Increases diamond storage capacity"),
            3000,
            5000
        ));
        
        sackTypes.put("emerald_sack", new SackType(
            "Emerald Sack", 
            Material.EMERALD, 
            SackCategory.MINING,
            Arrays.asList("§7Automatically collects emeralds", "§7Increases emerald storage capacity"),
            3000,
            5000
        ));
        
        sackTypes.put("lapis_sack", new SackType(
            "Lapis Sack", 
            Material.LAPIS_LAZULI, 
            SackCategory.MINING,
            Arrays.asList("§7Automatically collects lapis lazuli", "§7Increases lapis storage capacity"),
            1200,
            15000
        ));
        
        sackTypes.put("redstone_sack", new SackType(
            "Redstone Sack", 
            Material.REDSTONE, 
            SackCategory.MINING,
            Arrays.asList("§7Automatically collects redstone", "§7Increases redstone storage capacity"),
            1000,
            20000
        ));
        
        // Foraging Sacks
        sackTypes.put("oak_log_sack", new SackType(
            "Oak Log Sack", 
            Material.OAK_LOG, 
            SackCategory.FORAGING,
            Arrays.asList("§7Automatically collects oak logs", "§7Increases oak log storage capacity"),
            800,
            20000
        ));
        
        sackTypes.put("birch_log_sack", new SackType(
            "Birch Log Sack", 
            Material.BIRCH_LOG, 
            SackCategory.FORAGING,
            Arrays.asList("§7Automatically collects birch logs", "§7Increases birch log storage capacity"),
            800,
            20000
        ));
        
        sackTypes.put("spruce_log_sack", new SackType(
            "Spruce Log Sack", 
            Material.SPRUCE_LOG, 
            SackCategory.FORAGING,
            Arrays.asList("§7Automatically collects spruce logs", "§7Increases spruce log storage capacity"),
            800,
            20000
        ));
        
        sackTypes.put("jungle_log_sack", new SackType(
            "Jungle Log Sack", 
            Material.JUNGLE_LOG, 
            SackCategory.FORAGING,
            Arrays.asList("§7Automatically collects jungle logs", "§7Increases jungle log storage capacity"),
            800,
            20000
        ));
        
        sackTypes.put("acacia_log_sack", new SackType(
            "Acacia Log Sack", 
            Material.ACACIA_LOG, 
            SackCategory.FORAGING,
            Arrays.asList("§7Automatically collects acacia logs", "§7Increases acacia log storage capacity"),
            800,
            20000
        ));
        
        sackTypes.put("dark_oak_log_sack", new SackType(
            "Dark Oak Log Sack", 
            Material.DARK_OAK_LOG, 
            SackCategory.FORAGING,
            Arrays.asList("§7Automatically collects dark oak logs", "§7Increases dark oak log storage capacity"),
            800,
            20000
        ));
        
        // Combat Sacks
        sackTypes.put("rotten_flesh_sack", new SackType(
            "Rotten Flesh Sack", 
            Material.ROTTEN_FLESH, 
            SackCategory.COMBAT,
            Arrays.asList("§7Automatically collects rotten flesh", "§7Increases rotten flesh storage capacity"),
            600,
            25000
        ));
        
        sackTypes.put("bone_sack", new SackType(
            "Bone Sack", 
            Material.BONE, 
            SackCategory.COMBAT,
            Arrays.asList("§7Automatically collects bones", "§7Increases bone storage capacity"),
            800,
            20000
        ));
        
        sackTypes.put("string_sack", new SackType(
            "String Sack", 
            Material.STRING, 
            SackCategory.COMBAT,
            Arrays.asList("§7Automatically collects string", "§7Increases string storage capacity"),
            700,
            20000
        ));
        
        sackTypes.put("spider_eye_sack", new SackType(
            "Spider Eye Sack", 
            Material.SPIDER_EYE, 
            SackCategory.COMBAT,
            Arrays.asList("§7Automatically collects spider eyes", "§7Increases spider eye storage capacity"),
            1000,
            15000
        ));
        
        sackTypes.put("gunpowder_sack", new SackType(
            "Gunpowder Sack", 
            Material.GUNPOWDER, 
            SackCategory.COMBAT,
            Arrays.asList("§7Automatically collects gunpowder", "§7Increases gunpowder storage capacity"),
            1200,
            15000
        ));
        
        sackTypes.put("ender_pearl_sack", new SackType(
            "Ender Pearl Sack", 
            Material.ENDER_PEARL, 
            SackCategory.COMBAT,
            Arrays.asList("§7Automatically collects ender pearls", "§7Increases ender pearl storage capacity"),
            2000,
            5000
        ));
        
        sackTypes.put("blaze_rod_sack", new SackType(
            "Blaze Rod Sack", 
            Material.BLAZE_ROD, 
            SackCategory.COMBAT,
            Arrays.asList("§7Automatically collects blaze rods", "§7Increases blaze rod storage capacity"),
            2500,
            3000
        ));
        
        sackTypes.put("ghast_tear_sack", new SackType(
            "Ghast Tear Sack", 
            Material.GHAST_TEAR, 
            SackCategory.COMBAT,
            Arrays.asList("§7Automatically collects ghast tears", "§7Increases ghast tear storage capacity"),
            3000,
            2000
        ));
        
        // Special Sacks
        sackTypes.put("universal_sack", new SackType(
            "Universal Sack", 
            Material.ENDER_CHEST, 
            SackCategory.SPECIAL,
            Arrays.asList("§7Automatically collects all items", "§7Universal storage capacity"),
            10000,
            100000
        ));
        
        sackTypes.put("coin_sack", new SackType(
            "Coin Sack", 
            Material.GOLD_INGOT, 
            SackCategory.SPECIAL,
            Arrays.asList("§7Automatically collects coins", "§7Increases coin storage capacity"),
            5000,
            50000
        ));
        
        sackTypes.put("experience_sack", new SackType(
            "Experience Sack", 
            Material.EXPERIENCE_BOTTLE, 
            SackCategory.SPECIAL,
            Arrays.asList("§7Automatically collects experience", "§7Increases experience storage capacity"),
            5000,
            25000
        ));
    }
    
    /**
     * Start the sack task
     */
    private void startSackTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateAutoCollection();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L); // Every second
    }
    
    /**
     * Update auto collection for all players
     */
    private void updateAutoCollection() {
        for (Map.Entry<UUID, Boolean> entry : autoCollectionEnabled.entrySet()) {
            UUID playerId = entry.getKey();
            boolean enabled = entry.getValue();
            
            if (enabled) {
                Player player = Bukkit.getPlayer(playerId);
                if (player != null && player.isOnline()) {
                    processAutoCollection(player);
                }
            }
        }
    }
    
    /**
     * Process auto collection for a player
     */
    private void processAutoCollection(Player player) {
        List<SackItem> playerSacks = this.playerSacks.get(player.getUniqueId());
        if (playerSacks == null) return;
        
        for (SackItem sack : playerSacks) {
            if (sack.isAutoCollectionEnabled()) {
                sack.processAutoCollection(player);
            }
        }
    }
    
    /**
     * Get player sack data
     */
    public PlayerSackData getPlayerSackData(UUID playerId) {
        return playerSackData.computeIfAbsent(playerId, k -> new PlayerSackData());
    }
    
    /**
     * Get sack type by ID
     */
    public SackType getSackType(String sackId) {
        return sackTypes.get(sackId);
    }
    
    /**
     * Get all sack types
     */
    public Map<String, SackType> getAllSackTypes() {
        return new HashMap<>(sackTypes);
    }
    
    /**
     * Get sack types by category
     */
    public Map<String, SackType> getSackTypesByCategory(SackCategory category) {
        Map<String, SackType> categorySacks = new HashMap<>();
        for (Map.Entry<String, SackType> entry : sackTypes.entrySet()) {
            if (entry.getValue().getCategory() == category) {
                categorySacks.put(entry.getKey(), entry.getValue());
            }
        }
        return categorySacks;
    }
    
    /**
     * Check if player can purchase a sack
     */
    public boolean canPurchaseSack(Player player, String sackId) {
        PlayerSackData data = getPlayerSackData(player.getUniqueId());
        SackType sack = getSackType(sackId);
        
        if (sack == null) return false;
        
        // Check if player has enough coins
        if (data.getCoins() < sack.getCost()) {
            return false;
        }
        
        // Check if player has required level
        if (data.getLevel() < getRequiredLevel(sackId)) {
            return false;
        }
        
        // Check if player already has this sack
        List<SackItem> playerSacks = this.playerSacks.getOrDefault(player.getUniqueId(), new ArrayList<>());
        for (SackItem sackItem : playerSacks) {
            if (sackItem.getSackType().equals(sackId)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Purchase a sack
     */
    public boolean purchaseSack(Player player, String sackId) {
        if (!canPurchaseSack(player, sackId)) {
            return false;
        }
        
        SackType sack = getSackType(sackId);
        PlayerSackData data = getPlayerSackData(player.getUniqueId());
        
        // Remove coins
        data.removeCoins(sack.getCost());
        
        // Create sack item
        SackItem sackItem = new SackItem(sackId, player.getUniqueId(), sack.getCapacity());
        List<SackItem> playerSacks = this.playerSacks.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        playerSacks.add(sackItem);
        
        // Update statistics
        data.incrementPurchasedSacks();
        data.addExperience(sack.getCost() / 20);
        
        return true;
    }
    
    /**
     * Get required level for a sack
     */
    private int getRequiredLevel(String sackId) {
        SackType sack = getSackType(sackId);
        if (sack == null) return 1;
        
        switch (sack.getCategory()) {
            case FARMING:
            case MINING:
            case FORAGING:
            case COMBAT:
                return 1;
            case SPECIAL:
                return 10;
            default:
                return 1;
        }
    }
    
    /**
     * Create a sack item
     */
    public ItemStack createSackItem(String sackId) {
        SackType sack = getSackType(sackId);
        if (sack == null) return null;
        
        ItemStack item = new ItemStack(sack.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6" + sack.getName()));
            List<String> lore = new ArrayList<>(sack.getDescription());
            lore.add("");
            lore.add("§7Category: " + sack.getCategory().getDisplayName());
            lore.add("§7Capacity: §a" + sack.getCapacity() + " items");
            lore.add("§7Cost: §a" + sack.getCost() + " coins");
            lore.add("");
            lore.add("§7Right-click to open this sack");
            lore.add("§7and manage your items!");
            lore.add("");
            lore.add("§8An automatic collection sack");
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Toggle auto collection for a player
     */
    public void toggleAutoCollection(UUID playerId) {
        boolean enabled = autoCollectionEnabled.getOrDefault(playerId, false);
        autoCollectionEnabled.put(playerId, !enabled);
    }
    
    /**
     * Check if auto collection is enabled for a player
     */
    public boolean isAutoCollectionEnabled(UUID playerId) {
        return autoCollectionEnabled.getOrDefault(playerId, false);
    }
    
    /**
     * Get player's sacks
     */
    public List<SackItem> getPlayerSacks(UUID playerId) {
        return playerSacks.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Get player's sack level
     */
    public int getSackLevel(UUID playerId) {
        PlayerSackData data = getPlayerSackData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's sack experience
     */
    public int getSackExperience(UUID playerId) {
        PlayerSackData data = getPlayerSackData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add sack experience to player
     */
    public void addSackExperience(UUID playerId, int experience) {
        PlayerSackData data = getPlayerSackData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's sack coins
     */
    public int getSackCoins(UUID playerId) {
        PlayerSackData data = getPlayerSackData(playerId);
        return data.getCoins();
    }
    
    /**
     * Add sack coins to player
     */
    public void addSackCoins(UUID playerId, int coins) {
        PlayerSackData data = getPlayerSackData(playerId);
        data.addCoins(coins);
    }
    
    /**
     * Remove sack coins from player
     */
    public void removeSackCoins(UUID playerId, int coins) {
        PlayerSackData data = getPlayerSackData(playerId);
        data.removeCoins(coins);
    }
    
    /**
     * Get player's sack statistics
     */
    public Map<String, Integer> getSackStatistics(UUID playerId) {
        PlayerSackData data = getPlayerSackData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("coins", data.getCoins());
        stats.put("purchased_sacks", data.getPurchasedSacks());
        stats.put("total_experience", data.getTotalExperience());
        
        return stats;
    }
    
    /**
     * Reset player's sack data
     */
    public void resetSackData(UUID playerId) {
        playerSackData.remove(playerId);
        playerSacks.remove(playerId);
        autoCollectionEnabled.remove(playerId);
    }
    
    /**
     * Save player's sack data
     */
    public void saveSackData(UUID playerId) {
        PlayerSackData data = getPlayerSackData(playerId);
        // Save to database
        databaseManager.savePlayerSackData(playerId, data);
    }
    
    /**
     * Load player's sack data
     */
    public void loadSackData(UUID playerId) {
        databaseManager.loadPlayerSackData(playerId).thenAccept(data -> {
            if (data instanceof PlayerSackData sackData) {
                playerSackData.put(playerId, sackData);
            }
        });
    }
    
    /**
     * Shutdown the sack system
     */
    public void shutdown() {
        // Save all player data
        for (UUID playerId : playerSackData.keySet()) {
            saveSackData(playerId);
        }
        
        // Clear data
        playerSackData.clear();
        playerSacks.clear();
        autoCollectionEnabled.clear();
    }
}
