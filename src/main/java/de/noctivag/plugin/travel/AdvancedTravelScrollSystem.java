package de.noctivag.plugin.travel;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Advanced Travel Scroll System for Hypixel Skyblock-style fast travel
 * Includes scroll types, locations, cooldowns, and teleportation mechanics
 */
public class AdvancedTravelScrollSystem {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerTravelData> playerTravelData = new ConcurrentHashMap<>();
    private final Map<String, TravelScroll> travelScrolls = new HashMap<>();
    private final Map<String, TravelLocation> travelLocations = new HashMap<>();
    private final Map<UUID, BukkitTask> activeTeleports = new ConcurrentHashMap<>();
    private final Map<UUID, Long> teleportCooldowns = new ConcurrentHashMap<>();
    
    public AdvancedTravelScrollSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeTravelScrolls();
        initializeTravelLocations();
        startTravelTask();
    }
    
    /**
     * Initialize all travel scrolls
     */
    private void initializeTravelScrolls() {
        // Hub Scrolls
        travelScrolls.put("hub_scroll", new TravelScroll(
            "Hub Scroll", 
            Material.PAPER, 
            "hub",
            Arrays.asList("§7Teleport to the Hub", "§7Cooldown: 5 seconds"),
            5000
        ));
        
        travelScrolls.put("spawn_scroll", new TravelScroll(
            "Spawn Scroll", 
            Material.PAPER, 
            "spawn",
            Arrays.asList("§7Teleport to Spawn", "§7Cooldown: 5 seconds"),
            5000
        ));
        
        // Island Scrolls
        travelScrolls.put("island_scroll", new TravelScroll(
            "Island Scroll", 
            Material.PAPER, 
            "island",
            Arrays.asList("§7Teleport to your Island", "§7Cooldown: 10 seconds"),
            10000
        ));
        
        travelScrolls.put("coop_island_scroll", new TravelScroll(
            "Co-op Island Scroll", 
            Material.PAPER, 
            "coop_island",
            Arrays.asList("§7Teleport to Co-op Island", "§7Cooldown: 15 seconds"),
            15000
        ));
        
        // Mining Islands
        travelScrolls.put("gold_mine_scroll", new TravelScroll(
            "Gold Mine Scroll", 
            Material.GOLD_INGOT, 
            "gold_mine",
            Arrays.asList("§7Teleport to Gold Mine", "§7Cooldown: 30 seconds"),
            30000
        ));
        
        travelScrolls.put("deep_caverns_scroll", new TravelScroll(
            "Deep Caverns Scroll", 
            Material.STONE, 
            "deep_caverns",
            Arrays.asList("§7Teleport to Deep Caverns", "§7Cooldown: 30 seconds"),
            30000
        ));
        
        travelScrolls.put("dwarven_mines_scroll", new TravelScroll(
            "Dwarven Mines Scroll", 
            Material.IRON_INGOT, 
            "dwarven_mines",
            Arrays.asList("§7Teleport to Dwarven Mines", "§7Cooldown: 45 seconds"),
            45000
        ));
        
        travelScrolls.put("crystal_hollows_scroll", new TravelScroll(
            "Crystal Hollows Scroll", 
            Material.AMETHYST_SHARD, 
            "crystal_hollows",
            Arrays.asList("§7Teleport to Crystal Hollows", "§7Cooldown: 60 seconds"),
            60000
        ));
        
        // Farming Islands
        travelScrolls.put("barn_scroll", new TravelScroll(
            "Barn Scroll", 
            Material.WHEAT, 
            "barn",
            Arrays.asList("§7Teleport to Barn", "§7Cooldown: 30 seconds"),
            30000
        ));
        
        travelScrolls.put("mushroom_desert_scroll", new TravelScroll(
            "Mushroom Desert Scroll", 
            Material.RED_MUSHROOM, 
            "mushroom_desert",
            Arrays.asList("§7Teleport to Mushroom Desert", "§7Cooldown: 30 seconds"),
            30000
        ));
        
        travelScrolls.put("spiders_den_scroll", new TravelScroll(
            "Spider's Den Scroll", 
            Material.COBWEB, 
            "spiders_den",
            Arrays.asList("§7Teleport to Spider's Den", "§7Cooldown: 30 seconds"),
            30000
        ));
        
        travelScrolls.put("blazing_fortress_scroll", new TravelScroll(
            "Blazing Fortress Scroll", 
            Material.NETHERRACK, 
            "blazing_fortress",
            Arrays.asList("§7Teleport to Blazing Fortress", "§7Cooldown: 45 seconds"),
            45000
        ));
        
        travelScrolls.put("end_scroll", new TravelScroll(
            "End Scroll", 
            Material.END_STONE, 
            "end",
            Arrays.asList("§7Teleport to The End", "§7Cooldown: 45 seconds"),
            45000
        ));
        
        // Fishing Islands
        travelScrolls.put("fishing_scroll", new TravelScroll(
            "Fishing Scroll", 
            Material.FISHING_ROD, 
            "fishing",
            Arrays.asList("§7Teleport to Fishing Area", "§7Cooldown: 30 seconds"),
            30000
        ));
        
        travelScrolls.put("fishing_harbor_scroll", new TravelScroll(
            "Fishing Harbor Scroll", 
            Material.PRISMARINE, 
            "fishing_harbor",
            Arrays.asList("§7Teleport to Fishing Harbor", "§7Cooldown: 30 seconds"),
            30000
        ));
        
        // Dungeon Scrolls
        travelScrolls.put("catacombs_scroll", new TravelScroll(
            "Catacombs Scroll", 
            Material.BONE, 
            "catacombs",
            Arrays.asList("§7Teleport to Catacombs", "§7Cooldown: 60 seconds"),
            60000
        ));
        
        // Special Scrolls
        travelScrolls.put("auction_house_scroll", new TravelScroll(
            "Auction House Scroll", 
            Material.GOLD_BLOCK, 
            "auction_house",
            Arrays.asList("§7Teleport to Auction House", "§7Cooldown: 10 seconds"),
            10000
        ));
        
        travelScrolls.put("bank_scroll", new TravelScroll(
            "Bank Scroll", 
            Material.EMERALD_BLOCK, 
            "bank",
            Arrays.asList("§7Teleport to Bank", "§7Cooldown: 10 seconds"),
            10000
        ));
        
        travelScrolls.put("bazaar_scroll", new TravelScroll(
            "Bazaar Scroll", 
            Material.HOPPER, 
            "bazaar",
            Arrays.asList("§7Teleport to Bazaar", "§7Cooldown: 10 seconds"),
            10000
        ));
        
        travelScrolls.put("community_center_scroll", new TravelScroll(
            "Community Center Scroll", 
            Material.CRAFTING_TABLE, 
            "community_center",
            Arrays.asList("§7Teleport to Community Center", "§7Cooldown: 15 seconds"),
            15000
        ));
    }
    
    /**
     * Initialize all travel locations
     */
    private void initializeTravelLocations() {
        // Hub Locations
        travelLocations.put("hub", new TravelLocation(
            "Hub", 
            "hub_world", 
            0.5, 100.0, 0.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("spawn", new TravelLocation(
            "Spawn", 
            "hub_world", 
            0.5, 100.0, 0.5, 
            0.0f, 0.0f
        ));
        
        // Mining Locations
        travelLocations.put("gold_mine", new TravelLocation(
            "Gold Mine", 
            "mining_world", 
            100.5, 70.0, 100.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("deep_caverns", new TravelLocation(
            "Deep Caverns", 
            "mining_world", 
            200.5, 30.0, 200.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("dwarven_mines", new TravelLocation(
            "Dwarven Mines", 
            "mining_world", 
            300.5, 20.0, 300.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("crystal_hollows", new TravelLocation(
            "Crystal Hollows", 
            "mining_world", 
            400.5, 10.0, 400.5, 
            0.0f, 0.0f
        ));
        
        // Farming Locations
        travelLocations.put("barn", new TravelLocation(
            "Barn", 
            "farming_world", 
            50.5, 80.0, 50.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("mushroom_desert", new TravelLocation(
            "Mushroom Desert", 
            "farming_world", 
            150.5, 70.0, 150.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("spiders_den", new TravelLocation(
            "Spider's Den", 
            "farming_world", 
            250.5, 60.0, 250.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("blazing_fortress", new TravelLocation(
            "Blazing Fortress", 
            "nether_world", 
            100.5, 50.0, 100.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("end", new TravelLocation(
            "The End", 
            "end_world", 
            0.5, 70.0, 0.5, 
            0.0f, 0.0f
        ));
        
        // Fishing Locations
        travelLocations.put("fishing", new TravelLocation(
            "Fishing Area", 
            "fishing_world", 
            50.5, 60.0, 50.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("fishing_harbor", new TravelLocation(
            "Fishing Harbor", 
            "fishing_world", 
            100.5, 60.0, 100.5, 
            0.0f, 0.0f
        ));
        
        // Dungeon Locations
        travelLocations.put("catacombs", new TravelLocation(
            "Catacombs", 
            "dungeon_world", 
            0.5, 50.0, 0.5, 
            0.0f, 0.0f
        ));
        
        // Special Locations
        travelLocations.put("auction_house", new TravelLocation(
            "Auction House", 
            "hub_world", 
            10.5, 100.0, 10.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("bank", new TravelLocation(
            "Bank", 
            "hub_world", 
            20.5, 100.0, 20.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("bazaar", new TravelLocation(
            "Bazaar", 
            "hub_world", 
            30.5, 100.0, 30.5, 
            0.0f, 0.0f
        ));
        
        travelLocations.put("community_center", new TravelLocation(
            "Community Center", 
            "hub_world", 
            40.5, 100.0, 40.5, 
            0.0f, 0.0f
        ));
    }
    
    /**
     * Start the travel task
     */
    private void startTravelTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateActiveTeleports();
                updateCooldowns();
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    /**
     * Update all active teleports
     */
    private void updateActiveTeleports() {
        for (Map.Entry<UUID, BukkitTask> entry : activeTeleports.entrySet()) {
            UUID playerId = entry.getKey();
            BukkitTask task = entry.getValue();
            
            if (task.isCancelled()) {
                activeTeleports.remove(playerId);
            }
        }
    }
    
    /**
     * Update teleport cooldowns
     */
    private void updateCooldowns() {
        long currentTime = System.currentTimeMillis();
        teleportCooldowns.entrySet().removeIf(entry -> currentTime >= entry.getValue());
    }
    
    /**
     * Get player travel data
     */
    public PlayerTravelData getPlayerTravelData(UUID playerId) {
        return playerTravelData.computeIfAbsent(playerId, k -> new PlayerTravelData());
    }
    
    /**
     * Get travel scroll by ID
     */
    public TravelScroll getTravelScroll(String scrollId) {
        return travelScrolls.get(scrollId);
    }
    
    /**
     * Get travel location by ID
     */
    public TravelLocation getTravelLocation(String locationId) {
        return travelLocations.get(locationId);
    }
    
    /**
     * Get all travel scrolls
     */
    public Map<String, TravelScroll> getAllTravelScrolls() {
        return new HashMap<>(travelScrolls);
    }
    
    /**
     * Get all travel locations
     */
    public Map<String, TravelLocation> getAllTravelLocations() {
        return new HashMap<>(travelLocations);
    }
    
    /**
     * Check if player can use a travel scroll
     */
    public boolean canUseTravelScroll(Player player, String scrollId) {
        TravelScroll scroll = getTravelScroll(scrollId);
        if (scroll == null) return false;
        
        // Check if player has the scroll
        if (!player.getInventory().contains(scroll.getMaterial())) {
            return false;
        }
        
        // Check cooldown
        if (isOnCooldown(player.getUniqueId(), scrollId)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Use a travel scroll
     */
    public boolean useTravelScroll(Player player, String scrollId) {
        if (!canUseTravelScroll(player, scrollId)) {
            return false;
        }
        
        TravelScroll scroll = getTravelScroll(scrollId);
        TravelLocation location = getTravelLocation(scroll.getLocationId());
        
        if (location == null) {
            player.sendMessage("§cTravel location not found!");
            return false;
        }
        
        // Remove scroll from inventory
        player.getInventory().removeItem(new ItemStack(scroll.getMaterial(), 1));
        
        // Set cooldown
        setCooldown(player.getUniqueId(), scrollId, scroll.getCooldown());
        
        // Start teleportation
        startTeleportation(player, location, scroll);
        
        return true;
    }
    
    /**
     * Start teleportation process
     */
    private void startTeleportation(Player player, TravelLocation location, TravelScroll scroll) {
        player.sendMessage("§aTeleporting to " + location.getName() + " in 3 seconds...");
        player.sendMessage("§7Don't move!");
        
        Location startLocation = player.getLocation();
        BukkitTask teleportTask = new BukkitRunnable() {
            int countdown = 3;
            
            @Override
            public void run() {
                if (countdown <= 0) {
                    // Teleport player
                    teleportPlayer(player, location);
                    activeTeleports.remove(player.getUniqueId());
                    cancel();
                    return;
                }
                
                // Check if player moved
                if (player.getLocation().distance(startLocation) > 1.0) {
                    player.sendMessage("§cTeleportation cancelled! You moved.");
                    activeTeleports.remove(player.getUniqueId());
                    cancel();
                    return;
                }
                
                player.sendMessage("§eTeleporting in " + countdown + "...");
                countdown--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
        
        activeTeleports.put(player.getUniqueId(), teleportTask);
    }
    
    /**
     * Teleport player to location
     */
    private void teleportPlayer(Player player, TravelLocation location) {
        try {
            // Get or create world
            org.bukkit.World world = Bukkit.getWorld(location.getWorldName());
            if (world == null) {
                player.sendMessage("§cWorld not found: " + location.getWorldName());
                return;
            }
            
            Location teleportLocation = new Location(
                world,
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
            );
            
            player.teleport(teleportLocation);
            player.sendMessage("§aTeleported to " + location.getName() + "!");
            
            // Update statistics
            PlayerTravelData data = getPlayerTravelData(player.getUniqueId());
            data.incrementTeleports();
            data.addExperience(100);
            
        } catch (Exception e) {
            player.sendMessage("§cTeleportation failed!");
            plugin.getLogger().warning("Failed to teleport player: " + e.getMessage());
        }
    }
    
    /**
     * Check if player is on cooldown for a scroll
     */
    public boolean isOnCooldown(UUID playerId, String scrollId) {
        Long cooldownEnd = teleportCooldowns.get(playerId + ":" + scrollId);
        return cooldownEnd != null && System.currentTimeMillis() < cooldownEnd;
    }
    
    /**
     * Set cooldown for a scroll
     */
    public void setCooldown(UUID playerId, String scrollId, long cooldownMs) {
        // Use a different approach since we need to store per-scroll cooldowns
        // For now, just use player ID as key
        teleportCooldowns.put(playerId, System.currentTimeMillis() + cooldownMs);
    }
    
    /**
     * Get remaining cooldown time
     */
    public long getRemainingCooldown(UUID playerId, String scrollId) {
        Long cooldownEnd = teleportCooldowns.get(playerId + ":" + scrollId);
        if (cooldownEnd == null) return 0;
        return Math.max(0, cooldownEnd - System.currentTimeMillis());
    }
    
    /**
     * Create a travel scroll item
     */
    public ItemStack createTravelScroll(String scrollId) {
        TravelScroll scroll = getTravelScroll(scrollId);
        if (scroll == null) return null;
        
        ItemStack item = new ItemStack(scroll.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§6" + scroll.getName());
            List<String> lore = new ArrayList<>(scroll.getDescription());
            lore.add("");
            lore.add("§7Right-click to use this scroll");
            lore.add("§7and teleport instantly!");
            lore.add("");
            lore.add("§8A magical travel scroll");
            meta.setLore(lore);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Get player's travel level
     */
    public int getTravelLevel(UUID playerId) {
        PlayerTravelData data = getPlayerTravelData(playerId);
        return data.getLevel();
    }
    
    /**
     * Get player's travel experience
     */
    public int getTravelExperience(UUID playerId) {
        PlayerTravelData data = getPlayerTravelData(playerId);
        return data.getExperience();
    }
    
    /**
     * Add travel experience to player
     */
    public void addTravelExperience(UUID playerId, int experience) {
        PlayerTravelData data = getPlayerTravelData(playerId);
        data.addExperience(experience);
    }
    
    /**
     * Get player's travel statistics
     */
    public Map<String, Integer> getTravelStatistics(UUID playerId) {
        PlayerTravelData data = getPlayerTravelData(playerId);
        Map<String, Integer> stats = new HashMap<>();
        
        stats.put("level", data.getLevel());
        stats.put("experience", data.getExperience());
        stats.put("teleports", data.getTeleports());
        stats.put("total_experience", data.getTotalExperience());
        
        return stats;
    }
    
    /**
     * Reset player's travel data
     */
    public void resetTravelData(UUID playerId) {
        playerTravelData.remove(playerId);
        teleportCooldowns.remove(playerId);
    }
    
    /**
     * Save player's travel data
     */
    public void saveTravelData(UUID playerId) {
        PlayerTravelData data = getPlayerTravelData(playerId);
        // Save to database
        databaseManager.savePlayerTravelData(playerId, data);
    }
    
    /**
     * Load player's travel data
     */
    public void loadTravelData(UUID playerId) {
        try {
            CompletableFuture<Object> future = databaseManager.loadPlayerTravelData(playerId);
            PlayerTravelData data = (PlayerTravelData) future.get();
            if (data != null) {
                playerTravelData.put(playerId, data);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load travel data for player " + playerId + ": " + e.getMessage());
        }
    }
    
    /**
     * Shutdown the travel system
     */
    public void shutdown() {
        // Cancel all active teleports
        for (BukkitTask task : activeTeleports.values()) {
            if (!task.isCancelled()) {
                task.cancel();
            }
        }
        
        // Save all player data
        for (UUID playerId : playerTravelData.keySet()) {
            saveTravelData(playerId);
        }
        
        // Clear data
        playerTravelData.clear();
        activeTeleports.clear();
        teleportCooldowns.clear();
    }
}
