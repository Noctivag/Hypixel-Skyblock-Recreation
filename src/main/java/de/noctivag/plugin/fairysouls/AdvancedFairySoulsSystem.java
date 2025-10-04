package de.noctivag.plugin.fairysouls;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Advanced Fairy Souls System for Hypixel Skyblock-style fairy souls
 * Includes collection, rewards, and magical effects
 */
public class AdvancedFairySoulsSystem {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerFairySoulData> playerFairySoulData = new ConcurrentHashMap<>();
    private final Map<String, FairySoul> fairySouls = new HashMap<>();
    private final Map<UUID, Set<String>> collectedFairySouls = new ConcurrentHashMap<>();
    private final Map<UUID, List<FairySoulEffect>> activeFairySoulEffects = new ConcurrentHashMap<>();
    
    public AdvancedFairySoulsSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeFairySouls();
        startFairySoulTask();
    }
    
    /**
     * Initialize all fairy souls
     */
    private void initializeFairySouls() {
        // Hub Fairy Souls
        fairySouls.put("hub_soul_1", new FairySoul(
            "Hub Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.HUB,
            new Location(Bukkit.getWorld("hub_world"), 100, 70, 100),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            1
        ));
        
        fairySouls.put("hub_soul_2", new FairySoul(
            "Hub Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.HUB,
            new Location(Bukkit.getWorld("hub_world"), 200, 80, 200),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            1
        ));
        
        fairySouls.put("hub_soul_3", new FairySoul(
            "Hub Fairy Soul #3", 
            Material.END_ROD, 
            FairySoulCategory.HUB,
            new Location(Bukkit.getWorld("hub_world"), 300, 90, 300),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            1
        ));
        
        fairySouls.put("hub_soul_4", new FairySoul(
            "Hub Fairy Soul #4", 
            Material.END_ROD, 
            FairySoulCategory.HUB,
            new Location(Bukkit.getWorld("hub_world"), 400, 100, 400),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            1
        ));
        
        fairySouls.put("hub_soul_5", new FairySoul(
            "Hub Fairy Soul #5", 
            Material.END_ROD, 
            FairySoulCategory.HUB,
            new Location(Bukkit.getWorld("hub_world"), 500, 110, 500),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            1
        ));
        
        // Mining Islands Fairy Souls
        fairySouls.put("gold_mine_soul_1", new FairySoul(
            "Gold Mine Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.MINING,
            new Location(Bukkit.getWorld("mining_world"), 150, 60, 150),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            2
        ));
        
        fairySouls.put("gold_mine_soul_2", new FairySoul(
            "Gold Mine Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.MINING,
            new Location(Bukkit.getWorld("mining_world"), 250, 70, 250),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            2
        ));
        
        fairySouls.put("deep_caverns_soul_1", new FairySoul(
            "Deep Caverns Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.MINING,
            new Location(Bukkit.getWorld("mining_world"), 350, 40, 350),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            3
        ));
        
        fairySouls.put("deep_caverns_soul_2", new FairySoul(
            "Deep Caverns Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.MINING,
            new Location(Bukkit.getWorld("mining_world"), 450, 30, 450),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            3
        ));
        
        fairySouls.put("dwarven_mines_soul_1", new FairySoul(
            "Dwarven Mines Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.MINING,
            new Location(Bukkit.getWorld("mining_world"), 550, 20, 550),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            4
        ));
        
        fairySouls.put("dwarven_mines_soul_2", new FairySoul(
            "Dwarven Mines Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.MINING,
            new Location(Bukkit.getWorld("mining_world"), 650, 10, 650),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            4
        ));
        
        fairySouls.put("crystal_hollows_soul_1", new FairySoul(
            "Crystal Hollows Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.MINING,
            new Location(Bukkit.getWorld("mining_world"), 750, 5, 750),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            5
        ));
        
        fairySouls.put("crystal_hollows_soul_2", new FairySoul(
            "Crystal Hollows Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.MINING,
            new Location(Bukkit.getWorld("mining_world"), 850, 5, 850),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            5
        ));
        
        // Farming Islands Fairy Souls
        fairySouls.put("barn_soul_1", new FairySoul(
            "Barn Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.FARMING,
            new Location(Bukkit.getWorld("farming_world"), 100, 80, 100),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            2
        ));
        
        fairySouls.put("barn_soul_2", new FairySoul(
            "Barn Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.FARMING,
            new Location(Bukkit.getWorld("farming_world"), 200, 90, 200),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            2
        ));
        
        fairySouls.put("mushroom_desert_soul_1", new FairySoul(
            "Mushroom Desert Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.FARMING,
            new Location(Bukkit.getWorld("farming_world"), 300, 70, 300),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            3
        ));
        
        fairySouls.put("mushroom_desert_soul_2", new FairySoul(
            "Mushroom Desert Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.FARMING,
            new Location(Bukkit.getWorld("farming_world"), 400, 80, 400),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            3
        ));
        
        fairySouls.put("spiders_den_soul_1", new FairySoul(
            "Spider's Den Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.FARMING,
            new Location(Bukkit.getWorld("farming_world"), 500, 60, 500),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            4
        ));
        
        fairySouls.put("spiders_den_soul_2", new FairySoul(
            "Spider's Den Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.FARMING,
            new Location(Bukkit.getWorld("farming_world"), 600, 70, 600),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            4
        ));
        
        fairySouls.put("blazing_fortress_soul_1", new FairySoul(
            "Blazing Fortress Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.FARMING,
            new Location(Bukkit.getWorld("nether_world"), 100, 50, 100),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            5
        ));
        
        fairySouls.put("blazing_fortress_soul_2", new FairySoul(
            "Blazing Fortress Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.FARMING,
            new Location(Bukkit.getWorld("nether_world"), 200, 60, 200),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            5
        ));
        
        fairySouls.put("end_soul_1", new FairySoul(
            "End Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.FARMING,
            new Location(Bukkit.getWorld("end_world"), 0, 70, 0),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            6
        ));
        
        fairySouls.put("end_soul_2", new FairySoul(
            "End Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.FARMING,
            new Location(Bukkit.getWorld("end_world"), 100, 80, 100),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            6
        ));
        
        // Fishing Islands Fairy Souls
        fairySouls.put("fishing_soul_1", new FairySoul(
            "Fishing Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.FISHING,
            new Location(Bukkit.getWorld("fishing_world"), 50, 60, 50),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            2
        ));
        
        fairySouls.put("fishing_soul_2", new FairySoul(
            "Fishing Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.FISHING,
            new Location(Bukkit.getWorld("fishing_world"), 150, 70, 150),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            2
        ));
        
        fairySouls.put("fishing_harbor_soul_1", new FairySoul(
            "Fishing Harbor Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.FISHING,
            new Location(Bukkit.getWorld("fishing_world"), 250, 80, 250),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            3
        ));
        
        fairySouls.put("fishing_harbor_soul_2", new FairySoul(
            "Fishing Harbor Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.FISHING,
            new Location(Bukkit.getWorld("fishing_world"), 350, 90, 350),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            3
        ));
        
        // Dungeon Fairy Souls
        fairySouls.put("catacombs_soul_1", new FairySoul(
            "Catacombs Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.DUNGEON,
            new Location(Bukkit.getWorld("dungeon_world"), 0, 50, 0),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            7
        ));
        
        fairySouls.put("catacombs_soul_2", new FairySoul(
            "Catacombs Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.DUNGEON,
            new Location(Bukkit.getWorld("dungeon_world"), 100, 60, 100),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            7
        ));
        
        // Special Fairy Souls
        fairySouls.put("special_soul_1", new FairySoul(
            "Special Fairy Soul #1", 
            Material.END_ROD, 
            FairySoulCategory.SPECIAL,
            new Location(Bukkit.getWorld("hub_world"), 0, 100, 0),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            10
        ));
        
        fairySouls.put("special_soul_2", new FairySoul(
            "Special Fairy Soul #2", 
            Material.END_ROD, 
            FairySoulCategory.SPECIAL,
            new Location(Bukkit.getWorld("hub_world"), 1000, 100, 1000),
            Arrays.asList("§7A magical fairy soul", "§7Provides permanent stat bonuses"),
            10
        ));
    }
    
    /**
     * Start the fairy soul task
     */
    private void startFairySoulTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveFairySoulEffects();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    /**
     * Update all active fairy soul effects
     */
    private void updateActiveFairySoulEffects() {
        for (Map.Entry<UUID, List<FairySoulEffect>> entry : activeFairySoulEffects.entrySet()) {
            UUID playerId = entry.getKey();
            List<FairySoulEffect> effects = entry.getValue();
            
            // Remove expired effects
            effects.removeIf(FairySoulEffect::isExpired);
            
            // Apply active effects
            Player player = Bukkit.getPlayer(playerId);
            if (player != null && player.isOnline()) {
                applyFairySoulEffects(player, effects);
            }
        }
    }
    
    /**
     * Apply fairy soul effects to a player
     */
    private void applyFairySoulEffects(Player player, List<FairySoulEffect> effects) {
        for (FairySoulEffect effect : effects) {
            effect.apply(player);
        }
    }
    
    /**
     * Get player fairy soul data
     */
    public PlayerFairySoulData getPlayerFairySoulData(UUID playerId) {
        return playerFairySoulData.computeIfAbsent(playerId, k -> new PlayerFairySoulData());
    }
    
    /**
     * Get fairy soul by ID
     */
    public FairySoul getFairySoul(String soulId) {
        return fairySouls.get(soulId);
    }
    
    /**
     * Get all fairy souls
     */
    public Map<String, FairySoul> getAllFairySouls() {
        return new HashMap<>(fairySouls);
    }
    
    /**
     * Get fairy souls by category
     */
    public Map<String, FairySoul> getFairySoulsByCategory(FairySoulCategory category) {
        Map<String, FairySoul> categorySouls = new HashMap<>();
        for (Map.Entry<String, FairySoul> entry : fairySouls.entrySet()) {
            if (entry.getValue().getCategory() == category) {
                categorySouls.put(entry.getKey(), entry.getValue());
            }
        }
        return categorySouls;
    }
    
    /**
     * Check if player can collect a fairy soul
     */
    public boolean canCollectFairySoul(Player player, String soulId) {
        Set<String> collected = collectedFairySouls.getOrDefault(player.getUniqueId(), new HashSet<>());
        return !collected.contains(soulId);
    }
    
    /**
     * Collect a fairy soul
     */
    public boolean collectFairySoul(Player player, String soulId) {
        if (!canCollectFairySoul(player, soulId)) {
            return false;
        }
        
        FairySoul soul = getFairySoul(soulId);
        if (soul == null) return false;
        
        // Check if player is close enough to the fairy soul
        if (player.getLocation().distance(soul.getLocation()) > 5.0) {
            return false;
        }
        
        // Add to collected souls
        Set<String> collected = collectedFairySouls.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
        collected.add(soulId);
        
        // Update statistics
        PlayerFairySoulData data = getPlayerFairySoulData(player.getUniqueId());
        data.incrementCollectedSouls();
        data.addExperience(soul.getExperienceValue());
        
        // Apply fairy soul effect
        applyFairySoulEffect(player, soul);
        
        return true;
    }
    
    /**
     * Apply fairy soul effect to player
     */
    private void applyFairySoulEffect(Player player, FairySoul soul) {
        FairySoulEffect effect = new FairySoulEffect(soul, 1, 3600); // 1 hour
        List<FairySoulEffect> effects = activeFairySoulEffects.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        effects.add(effect);
    }
    
    /**
     * Get collected fairy souls for a player
     */
    public Set<String> getCollectedFairySouls(UUID playerId) {
        return collectedFairySouls.getOrDefault(playerId, new HashSet<>());
    }
    
    /**
     * Get total collected fairy souls for a player
     */
    public int getTotalCollectedFairySouls(UUID playerId) {
        return collectedFairySouls.getOrDefault(playerId, new HashSet<>()).size();
    }
    
    /**
     * Get collected fairy souls by category for a player
     */
    public int getCollectedFairySoulsByCategory(UUID playerId, FairySoulCategory category) {
        Set<String> collected = collectedFairySouls.getOrDefault(playerId, new HashSet<>());
        int count = 0;
        
        for (String soulId : collected) {
            FairySoul soul = getFairySoul(soulId);
            if (soul != null && soul.getCategory() == category) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Create a fairy soul item
     */
    public ItemStack createFairySoulItem(String soulId) {
        FairySoul soul = getFairySoul(soulId);
        if (soul == null) return null;
        
        ItemStack item = new ItemStack(soul.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§6" + soul.getName());
            List<String> lore = new ArrayList<>(soul.getDescription());
            lore.add("");
            lore.add("§7Category: " + soul.getCategory().getDisplayName());
            lore.add("§7Experience: §a" + soul.getExperienceValue() + " XP");
            lore.add("§7Location: §a" + soul.getLocation().getWorld().getName());
            lore.add("");
            lore.add("§7Right-click to collect this fairy soul");
            lore.add("§7and gain permanent stat bonuses!");
            lore.add("");
            lore.add("§8A magical fairy soul");
            meta.setLore(lore);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Get player's fairy soul level
     */
    public int getFairySoulLevel(UUID playerId) {
        PlayerFairySoulData data = getPlayerFairySoulData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's fairy soul experience
     */
    public int getFairySoulExperience(UUID playerId) {
        PlayerFairySoulData data = getPlayerFairySoulData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add fairy soul experience to player
     */
    public void addFairySoulExperience(UUID playerId, int experience) {
        PlayerFairySoulData data = getPlayerFairySoulData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's fairy soul statistics
     */
    public Map<String, Integer> getFairySoulStatistics(UUID playerId) {
        PlayerFairySoulData data = getPlayerFairySoulData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("collected_souls", data.getCollectedSouls());
        stats.put("total_experience", data.getTotalExperience());
        
        return stats;
    }
    
    /**
     * Reset player's fairy soul data
     */
    public void resetFairySoulData(UUID playerId) {
        playerFairySoulData.remove(playerId);
        collectedFairySouls.remove(playerId);
        activeFairySoulEffects.remove(playerId);
    }
    
    /**
     * Save player's fairy soul data
     */
    public void saveFairySoulData(UUID playerId) {
        PlayerFairySoulData data = getPlayerFairySoulData(playerId);
        // Save to database
        databaseManager.savePlayerFairySoulData(playerId, data);
    }
    
    /**
     * Load player's fairy soul data
     */
    public void loadFairySoulData(UUID playerId) {
        try {
            CompletableFuture<Object> future = databaseManager.loadPlayerFairySoulData(playerId);
            PlayerFairySoulData data = (PlayerFairySoulData) future.get();
            if (data != null) {
                playerFairySoulData.put(playerId, data);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load fairy soul data for player " + playerId + ": " + e.getMessage());
        }
    }
    
    /**
     * Shutdown the fairy soul system
     */
    public void shutdown() {
        // Save all player data
        for (UUID playerId : playerFairySoulData.keySet()) {
            saveFairySoulData(playerId);
        }
        
        // Clear data
        playerFairySoulData.clear();
        collectedFairySouls.clear();
        activeFairySoulEffects.clear();
    }
}
