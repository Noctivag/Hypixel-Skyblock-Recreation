package de.noctivag.plugin.garden;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Garden System - Complete Hypixel SkyBlock Garden Implementation
 * 
 * Features:
 * - Garden plots and farming areas
 * - Crop types and growth mechanics
 * - Garden visitors and quests
 * - Garden upgrades and equipment
 * - Garden XP and leveling
 * - Garden events and competitions
 */
public class GardenSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerGardenData> playerGardenData = new ConcurrentHashMap<>();
    private final Map<UUID, GardenSession> activeSessions = new ConcurrentHashMap<>();
    private final Map<CropType, CropConfig> cropConfigs = new HashMap<>();
    private final Map<GardenVisitor, VisitorConfig> visitorConfigs = new HashMap<>();
    private final Map<GardenUpgrade, UpgradeConfig> upgradeConfigs = new HashMap<>();
    
    public GardenSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeCropConfigs();
        initializeVisitorConfigs();
        initializeUpgradeConfigs();
        startGardenUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeCropConfigs() {
        // Wheat
        cropConfigs.put(CropType.WHEAT, new CropConfig(
            "Wheat", "§eWheat", "§7A basic crop",
            Material.WHEAT, 60, 1, 5, 0.1,
            Arrays.asList("§7Basic crop", "§7Fast growth", "§7Low value"),
            Arrays.asList("§7Wheat seeds", "§7Wheat")
        ));
        
        // Carrot
        cropConfigs.put(CropType.CARROT, new CropConfig(
            "Carrot", "§6Carrot", "§7A nutritious crop",
            Material.CARROT, 90, 2, 8, 0.15,
            Arrays.asList("§7Nutritious crop", "§7Medium growth", "§7Medium value"),
            Arrays.asList("§7Carrot seeds", "§7Carrot")
        ));
        
        // Potato
        cropConfigs.put(CropType.POTATO, new CropConfig(
            "Potato", "§fPotato", "§7A versatile crop",
            Material.POTATO, 90, 2, 8, 0.15,
            Arrays.asList("§7Versatile crop", "§7Medium growth", "§7Medium value"),
            Arrays.asList("§7Potato seeds", "§7Potato")
        ));
        
        // Pumpkin
        cropConfigs.put(CropType.PUMPKIN, new CropConfig(
            "Pumpkin", "§6Pumpkin", "§7A large crop",
            Material.PUMPKIN, 120, 3, 12, 0.2,
            Arrays.asList("§7Large crop", "§7Slow growth", "§7High value"),
            Arrays.asList("§7Pumpkin seeds", "§7Pumpkin")
        ));
        
        // Melon
        cropConfigs.put(CropType.MELON, new CropConfig(
            "Melon", "§aMelon", "§7A refreshing crop",
            Material.MELON, 120, 3, 12, 0.2,
            Arrays.asList("§7Refreshing crop", "§7Slow growth", "§7High value"),
            Arrays.asList("§7Melon seeds", "§7Melon")
        ));
        
        // Sugar Cane
        cropConfigs.put(CropType.SUGAR_CANE, new CropConfig(
            "Sugar Cane", "§aSugar Cane", "§7A sweet crop",
            Material.SUGAR_CANE, 150, 4, 15, 0.25,
            Arrays.asList("§7Sweet crop", "§7Very slow growth", "§7Very high value"),
            Arrays.asList("§7Sugar Cane seeds", "§7Sugar Cane")
        ));
        
        // Nether Wart
        cropConfigs.put(CropType.NETHER_WART, new CropConfig(
            "Nether Wart", "§cNether Wart", "§7A magical crop",
            Material.NETHER_WART, 180, 5, 20, 0.3,
            Arrays.asList("§7Magical crop", "§7Extremely slow growth", "§7Extremely high value"),
            Arrays.asList("§7Nether Wart seeds", "§7Nether Wart")
        ));
        
        // Cocoa
        cropConfigs.put(CropType.COCOA, new CropConfig(
            "Cocoa", "§6Cocoa", "§7A chocolate crop",
            Material.COCOA_BEANS, 200, 6, 25, 0.35,
            Arrays.asList("§7Chocolate crop", "§7Ultra slow growth", "§7Ultra high value"),
            Arrays.asList("§7Cocoa seeds", "§7Cocoa Beans")
        ));
    }
    
    private void initializeVisitorConfigs() {
        // Jacob
        visitorConfigs.put(GardenVisitor.JACOB, new VisitorConfig(
            "Jacob", "§6Jacob", "§7The farming contest organizer",
            Material.GOLDEN_HOE, Arrays.asList("§7Farming contests", "§7Special rewards", "§7Competition prizes"),
            Arrays.asList("§7Contest participation", "§7Farming challenges", "§7Reward collection")
        ));
        
        // Anita
        visitorConfigs.put(GardenVisitor.ANITA, new VisitorConfig(
            "Anita", "§bAnita", "§7The garden upgrade specialist",
            Material.EMERALD, Arrays.asList("§7Garden upgrades", "§7Plot expansions", "§7Equipment improvements"),
            Arrays.asList("§7Upgrade purchases", "§7Plot management", "§7Equipment upgrades")
        ));
        
        // Garden Shop
        visitorConfigs.put(GardenVisitor.GARDEN_SHOP, new VisitorConfig(
            "Garden Shop", "§aGarden Shop", "§7The garden equipment store",
            Material.CHEST, Arrays.asList("§7Garden equipment", "§7Seeds and tools", "§7Farming supplies"),
            Arrays.asList("§7Equipment purchases", "§7Seed buying", "§7Tool upgrades")
        ));
        
        // Garden Quest
        visitorConfigs.put(GardenVisitor.GARDEN_QUEST, new VisitorConfig(
            "Garden Quest", "§eGarden Quest", "§7The garden quest giver",
            Material.BOOK, Arrays.asList("§7Garden quests", "§7Farming challenges", "§7Quest rewards"),
            Arrays.asList("§7Quest acceptance", "§7Quest completion", "§7Reward collection")
        ));
    }
    
    private void initializeUpgradeConfigs() {
        // Plot Expansion
        upgradeConfigs.put(GardenUpgrade.PLOT_EXPANSION, new UpgradeConfig(
            "Plot Expansion", "§6Plot Expansion", "§7Expand your garden plots",
            Material.GRASS_BLOCK, 1000, Arrays.asList("§7+1 Garden Plot", "§7More farming space", "§7Increased capacity"),
            Arrays.asList("§7Unlock new plot", "§7Expand farming area", "§7Increase production")
        ));
        
        // Watering Can
        upgradeConfigs.put(GardenUpgrade.WATERING_CAN, new UpgradeConfig(
            "Watering Can", "§bWatering Can", "§7Speed up crop growth",
            Material.WATER_BUCKET, 500, Arrays.asList("§7+25% Growth Speed", "§7Faster crop growth", "§7Increased efficiency"),
            Arrays.asList("§7Speed up growth", "§7Increase efficiency", "§7Faster harvests")
        ));
        
        // Compost
        upgradeConfigs.put(GardenUpgrade.COMPOST, new UpgradeConfig(
            "Compost", "§2Compost", "§7Improve crop quality",
            Material.DIRT, 750, Arrays.asList("§7+50% Crop Quality", "§7Better crop yields", "§7Improved harvests"),
            Arrays.asList("§7Improve quality", "§7Better yields", "§7Enhanced harvests")
        ));
        
        // Fertilizer
        upgradeConfigs.put(GardenUpgrade.FERTILIZER, new UpgradeConfig(
            "Fertilizer", "§eFertilizer", "§7Boost crop production",
            Material.BONE_MEAL, 1000, Arrays.asList("§7+100% Production", "§7Double crop yields", "§7Maximum efficiency"),
            Arrays.asList("§7Boost production", "§7Double yields", "§7Maximum efficiency")
        ));
    }
    
    private void startGardenUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (GardenSession session : activeSessions.values()) {
                updateGardenSession(session);
            }
        }, 0L, 20L); // Every second
    }
    
    private void updateGardenSession(GardenSession session) {
        if (session.isActive()) {
            session.setTimeElapsed(session.getTimeElapsed() + 1);
            
            // Update crop growth
            updateCropGrowth(session);
            
            // Check for visitor spawns
            if (Math.random() < getVisitorSpawnChance()) {
                spawnGardenVisitor(session);
            }
        }
    }
    
    private void updateCropGrowth(GardenSession session) {
        for (GardenPlot plot : session.getPlots()) {
            for (CropPlot cropPlot : plot.getCropPlots()) {
                if (cropPlot.getCropType() != null && !cropPlot.isFullyGrown()) {
                    cropPlot.setGrowthTime(cropPlot.getGrowthTime() + 1);
                    
                    if (cropPlot.getGrowthTime() >= cropPlot.getCropConfig().getGrowthTime()) {
                        cropPlot.setFullyGrown(true);
                        
                        // Notify player
                        Player player = Bukkit.getPlayer(session.getPlayerId());
                        if (player != null) {
                            player.sendMessage("§a" + cropPlot.getCropConfig().getDisplayName() + " is ready for harvest!");
                        }
                    }
                }
            }
        }
    }
    
    public void startGardenSession(Player player) {
        if (activeSessions.containsKey(player.getUniqueId())) {
            player.sendMessage("§cYou already have an active garden session!");
            return;
        }
        
        PlayerGardenData data = getPlayerGardenData(player);
        GardenSession session = new GardenSession(
            player.getUniqueId(),
            data.getGardenLevel(),
            System.currentTimeMillis()
        );
        
        activeSessions.put(player.getUniqueId(), session);
        
        player.sendMessage("§aGarden session started!");
        player.sendMessage("§7Garden Level: " + data.getGardenLevel());
        player.sendMessage("§7Plots Available: " + data.getPlotCount());
    }
    
    public void stopGardenSession(Player player) {
        GardenSession session = activeSessions.remove(player.getUniqueId());
        if (session != null) {
            session.setActive(false);
            player.sendMessage("§aGarden session ended!");
            player.sendMessage("§7Time in garden: " + session.getTimeElapsed() + " seconds");
        }
    }
    
    public void plantCrop(Player player, Location location, CropType cropType) {
        GardenSession session = activeSessions.get(player.getUniqueId());
        if (session == null || !session.isActive()) {
            player.sendMessage("§cYou must have an active garden session to plant crops!");
            return;
        }
        
        CropConfig cropConfig = cropConfigs.get(cropType);
        if (cropConfig == null) {
            player.sendMessage("§cInvalid crop type!");
            return;
        }
        
        // Find the plot for this location
        GardenPlot plot = findPlotForLocation(session, location);
        if (plot == null) {
            player.sendMessage("§cThis location is not in your garden!");
            return;
        }
        
        // Find the crop plot for this location
        CropPlot cropPlot = findCropPlotForLocation(plot, location);
        if (cropPlot == null) {
            player.sendMessage("§cThis location is not a valid crop plot!");
            return;
        }
        
        if (cropPlot.getCropType() != null) {
            player.sendMessage("§cThis plot already has a crop!");
            return;
        }
        
        // Plant the crop
        cropPlot.setCropType(cropType);
        cropPlot.setCropConfig(cropConfig);
        cropPlot.setGrowthTime(0);
        cropPlot.setFullyGrown(false);
        
        // Set the block
        location.getBlock().setType(cropConfig.getMaterial());
        
        player.sendMessage("§aPlanted " + cropConfig.getDisplayName() + "!");
    }
    
    public void harvestCrop(Player player, Location location) {
        GardenSession session = activeSessions.get(player.getUniqueId());
        if (session == null || !session.isActive()) {
            player.sendMessage("§cYou must have an active garden session to harvest crops!");
            return;
        }
        
        // Find the plot for this location
        GardenPlot plot = findPlotForLocation(session, location);
        if (plot == null) {
            player.sendMessage("§cThis location is not in your garden!");
            return;
        }
        
        // Find the crop plot for this location
        CropPlot cropPlot = findCropPlotForLocation(plot, location);
        if (cropPlot == null) {
            player.sendMessage("§cThis location is not a valid crop plot!");
            return;
        }
        
        if (cropPlot.getCropType() == null) {
            player.sendMessage("§cThis plot doesn't have a crop!");
            return;
        }
        
        if (!cropPlot.isFullyGrown()) {
            player.sendMessage("§cThis crop is not ready for harvest!");
            return;
        }
        
        // Harvest the crop
        CropConfig cropConfig = cropPlot.getCropConfig();
        ItemStack harvest = new ItemStack(cropConfig.getMaterial());
        player.getInventory().addItem(harvest);
        
        // Give garden XP
        giveGardenXP(player, cropConfig.getXpReward());
        
        // Reset the plot
        cropPlot.setCropType(null);
        cropPlot.setCropConfig(null);
        cropPlot.setGrowthTime(0);
        cropPlot.setFullyGrown(false);
        
        // Clear the block
        location.getBlock().setType(Material.AIR);
        
        player.sendMessage("§aHarvested " + cropConfig.getDisplayName() + "!");
        player.sendMessage("§7+" + cropConfig.getXpReward() + " Garden XP");
    }
    
    private GardenPlot findPlotForLocation(GardenSession session, Location location) {
        for (GardenPlot plot : session.getPlots()) {
            if (isLocationInPlot(plot, location)) {
                return plot;
            }
        }
        return null;
    }
    
    private CropPlot findCropPlotForLocation(GardenPlot plot, Location location) {
        for (CropPlot cropPlot : plot.getCropPlots()) {
            if (cropPlot.getLocation().equals(location)) {
                return cropPlot;
            }
        }
        return null;
    }
    
    private boolean isLocationInPlot(GardenPlot plot, Location location) {
        // Check if location is within plot bounds
        Location plotCenter = plot.getCenter();
        int radius = plot.getRadius();
        
        return location.distance(plotCenter) <= radius;
    }
    
    private double getVisitorSpawnChance() {
        return 0.01; // 1% chance per second
    }
    
    private void spawnGardenVisitor(GardenSession session) {
        Player player = Bukkit.getPlayer(session.getPlayerId());
        if (player == null) return;
        
        // Select random visitor
        List<GardenVisitor> visitors = Arrays.asList(GardenVisitor.values());
        GardenVisitor visitorType = visitors.get((int) (Math.random() * visitors.size()));
        VisitorConfig visitorConfig = visitorConfigs.get(visitorType);
        
        // Spawn visitor
        Location spawnLocation = player.getLocation().add(5, 0, 0);
        GardenVisitorEntity visitor = new GardenVisitorEntity(
            visitorType,
            visitorConfig,
            spawnLocation,
            player
        );
        
        session.addVisitor(visitor);
        
        // Spawn effects
        player.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, spawnLocation, 10);
        player.getWorld().playSound(spawnLocation, Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
        
        player.sendMessage("§a" + visitorConfig.getDisplayName() + " has visited your garden!");
    }
    
    private void giveGardenXP(Player player, int xp) {
        PlayerGardenData data = getPlayerGardenData(player);
        data.addXP(xp);
        player.sendMessage("§a+" + xp + " Garden XP");
    }
    
    private PlayerGardenData getPlayerGardenData(Player player) {
        return playerGardenData.computeIfAbsent(player.getUniqueId(), k -> new PlayerGardenData(k));
    }
    
    // Enums and Classes
    public enum CropType {
        WHEAT, CARROT, POTATO, PUMPKIN, MELON, SUGAR_CANE, NETHER_WART, COCOA
    }
    
    public enum GardenVisitor {
        JACOB, ANITA, GARDEN_SHOP, GARDEN_QUEST
    }
    
    public enum GardenUpgrade {
        PLOT_EXPANSION, WATERING_CAN, COMPOST, FERTILIZER
    }
    
    // Data Classes
    public static class CropConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final int growthTime;
        private final int xpReward;
        private final int coinReward;
        private final double rarity;
        private final List<String> characteristics;
        private final List<String> drops;
        
        public CropConfig(String name, String displayName, String description, Material material,
                         int growthTime, int xpReward, int coinReward, double rarity,
                         List<String> characteristics, List<String> drops) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.material = material;
            this.growthTime = growthTime;
            this.xpReward = xpReward;
            this.coinReward = coinReward;
            this.rarity = rarity;
            this.characteristics = characteristics;
            this.drops = drops;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
        public int getGrowthTime() { return growthTime; }
        public int getXpReward() { return xpReward; }
        public int getCoinReward() { return coinReward; }
        public double getRarity() { return rarity; }
        public List<String> getCharacteristics() { return characteristics; }
        public List<String> getDrops() { return drops; }
    }
    
    public static class VisitorConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final List<String> services;
        private final List<String> activities;
        
        public VisitorConfig(String name, String displayName, String description, Material material,
                           List<String> services, List<String> activities) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.material = material;
            this.services = services;
            this.activities = activities;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
        public List<String> getServices() { return services; }
        public List<String> getActivities() { return activities; }
    }
    
    public static class UpgradeConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final int cost;
        private final List<String> benefits;
        private final List<String> effects;
        
        public UpgradeConfig(String name, String displayName, String description, Material material,
                           int cost, List<String> benefits, List<String> effects) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.material = material;
            this.cost = cost;
            this.benefits = benefits;
            this.effects = effects;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
        public int getCost() { return cost; }
        public List<String> getBenefits() { return benefits; }
        public List<String> getEffects() { return effects; }
    }
    
    public static class GardenSession {
        private final UUID playerId;
        private final int gardenLevel;
        private final long startTime;
        private boolean active;
        private int timeElapsed;
        private final List<GardenPlot> plots;
        private final List<GardenVisitorEntity> visitors;
        
        public GardenSession(UUID playerId, int gardenLevel, long startTime) {
            this.playerId = playerId;
            this.gardenLevel = gardenLevel;
            this.startTime = startTime;
            this.active = true;
            this.timeElapsed = 0;
            this.plots = new ArrayList<>();
            this.visitors = new ArrayList<>();
            
            // Initialize plots based on garden level
            initializePlots();
        }
        
        private void initializePlots() {
            // Create plots based on garden level
            for (int i = 0; i < Math.min(gardenLevel, 10); i++) {
                Location plotCenter = new Location(
                    Bukkit.getWorld("garden_" + playerId),
                    i * 20, 100, 0
                );
                
                GardenPlot plot = new GardenPlot(plotCenter, 10);
                plots.add(plot);
            }
        }
        
        public void addVisitor(GardenVisitorEntity visitor) {
            visitors.add(visitor);
        }
        
        // Getters and Setters
        public UUID getPlayerId() { return playerId; }
        public int getGardenLevel() { return gardenLevel; }
        public long getStartTime() { return startTime; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        public int getTimeElapsed() { return timeElapsed; }
        public void setTimeElapsed(int timeElapsed) { this.timeElapsed = timeElapsed; }
        public List<GardenPlot> getPlots() { return plots; }
        public List<GardenVisitorEntity> getVisitors() { return visitors; }
    }
    
    public static class GardenPlot {
        private final Location center;
        private final int radius;
        private final List<CropPlot> cropPlots;
        
        public GardenPlot(Location center, int radius) {
            this.center = center;
            this.radius = radius;
            this.cropPlots = new ArrayList<>();
            
            // Initialize crop plots
            initializeCropPlots();
        }
        
        private void initializeCropPlots() {
            // Create crop plots in a grid pattern
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z <= radius * radius) {
                        Location cropLocation = center.clone().add(x, 0, z);
                        CropPlot cropPlot = new CropPlot(cropLocation);
                        cropPlots.add(cropPlot);
                    }
                }
            }
        }
        
        // Getters
        public Location getCenter() { return center; }
        public int getRadius() { return radius; }
        public List<CropPlot> getCropPlots() { return cropPlots; }
    }
    
    public static class CropPlot {
        private final Location location;
        private CropType cropType;
        private CropConfig cropConfig;
        private int growthTime;
        private boolean fullyGrown;
        
        public CropPlot(Location location) {
            this.location = location;
            this.cropType = null;
            this.cropConfig = null;
            this.growthTime = 0;
            this.fullyGrown = false;
        }
        
        // Getters and Setters
        public Location getLocation() { return location; }
        public CropType getCropType() { return cropType; }
        public void setCropType(CropType cropType) { this.cropType = cropType; }
        public CropConfig getCropConfig() { return cropConfig; }
        public void setCropConfig(CropConfig cropConfig) { this.cropConfig = cropConfig; }
        public int getGrowthTime() { return growthTime; }
        public void setGrowthTime(int growthTime) { this.growthTime = growthTime; }
        public boolean isFullyGrown() { return fullyGrown; }
        public void setFullyGrown(boolean fullyGrown) { this.fullyGrown = fullyGrown; }
    }
    
    public static class GardenVisitorEntity {
        private final GardenVisitor visitorType;
        private final VisitorConfig config;
        private final Location location;
        private final Player target;
        
        public GardenVisitorEntity(GardenVisitor visitorType, VisitorConfig config, Location location, Player target) {
            this.visitorType = visitorType;
            this.config = config;
            this.location = location;
            this.target = target;
        }
        
        // Getters
        public GardenVisitor getVisitorType() { return visitorType; }
        public VisitorConfig getConfig() { return config; }
        public Location getLocation() { return location; }
        public Player getTarget() { return target; }
    }
    
    public static class PlayerGardenData {
        private final UUID playerId;
        private int totalXP;
        private int gardenLevel;
        private int plotCount;
        private final Map<CropType, Integer> cropStats;
        private final Map<GardenUpgrade, Boolean> upgrades;
        
        public PlayerGardenData(UUID playerId) {
            this.playerId = playerId;
            this.totalXP = 0;
            this.gardenLevel = 1;
            this.plotCount = 1;
            this.cropStats = new HashMap<>();
            this.upgrades = new HashMap<>();
        }
        
        public void addXP(int xp) {
            this.totalXP += xp;
            this.gardenLevel = calculateLevel(totalXP);
            this.plotCount = Math.min(10, gardenLevel);
        }
        
        private int calculateLevel(int xp) {
            return Math.min(50, (int) Math.floor(Math.sqrt(xp / 100.0)) + 1);
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public int getTotalXP() { return totalXP; }
        public int getGardenLevel() { return gardenLevel; }
        public int getPlotCount() { return plotCount; }
        public Map<CropType, Integer> getCropStats() { return cropStats; }
        public Map<GardenUpgrade, Boolean> getUpgrades() { return upgrades; }
    }
}
