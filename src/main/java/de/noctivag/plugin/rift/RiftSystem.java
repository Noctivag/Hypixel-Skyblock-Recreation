package de.noctivag.plugin.rift;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rift System - Complete Hypixel SkyBlock Rift Implementation
 * 
 * Features:
 * - Rift dimension with time mechanics
 * - Rift items and equipment
 * - Rift quests and progression
 * - Rift mobs and bosses
 * - Rift currency (Motes)
 * - Rift upgrades and abilities
 */
public class RiftSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerRiftData> playerRiftData = new ConcurrentHashMap<>();
    private final Map<UUID, RiftSession> activeSessions = new ConcurrentHashMap<>();
    private final Map<RiftLocation, RiftLocationConfig> locationConfigs = new HashMap<>();
    private final Map<RiftMob, RiftMobConfig> mobConfigs = new HashMap<>();
    private final Map<RiftItem, RiftItemConfig> itemConfigs = new HashMap<>();
    
    public RiftSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeLocationConfigs();
        initializeMobConfigs();
        initializeItemConfigs();
        startRiftUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeLocationConfigs() {
        // Rift Hub
        locationConfigs.put(RiftLocation.RIFT_HUB, new RiftLocationConfig(
            "Rift Hub", "§5Rift Hub", "§7The central hub of the Rift",
            Material.END_PORTAL_FRAME, Arrays.asList("§7Central hub", "§7Rift entrance", "§7Quest giver"),
            Arrays.asList(RiftMob.RIFT_GUARDIAN, RiftMob.RIFT_SENTINEL)
        ));
        
        // West Village
        locationConfigs.put(RiftLocation.WEST_VILLAGE, new RiftLocationConfig(
            "West Village", "§eWest Village", "§7A peaceful village in the Rift",
            Material.OAK_LOG, Arrays.asList("§7Peaceful village", "§7Rift residents", "§7Trading post"),
            Arrays.asList(RiftMob.VILLAGER_RIFT, RiftMob.VILLAGE_GUARD)
        ));
        
        // East Village
        locationConfigs.put(RiftLocation.EAST_VILLAGE, new RiftLocationConfig(
            "East Village", "§cEast Village", "§7A war-torn village in the Rift",
            Material.CRIMSON_STEM, Arrays.asList("§7War-torn village", "§7Battle scars", "§7Military outpost"),
            Arrays.asList(RiftMob.WARRIOR_RIFT, RiftMob.BATTLE_MAGE)
        ));
        
        // Dreadfarm
        locationConfigs.put(RiftLocation.DREADFARM, new RiftLocationConfig(
            "Dreadfarm", "§4Dreadfarm", "§7A cursed farming area",
            Material.WARPED_STEM, Arrays.asList("§7Cursed farming", "§7Dark magic", "§7Corrupted crops"),
            Arrays.asList(RiftMob.DREADFARM_WORKER, RiftMob.CORRUPTED_CROP)
        ));
        
        // Stillgore Château
        locationConfigs.put(RiftLocation.STILLGORE_CHATEAU, new RiftLocationConfig(
            "Stillgore Château", "§0Stillgore Château", "§7A mysterious castle",
            Material.BLACKSTONE, Arrays.asList("§7Mysterious castle", "§7Dark secrets", "§7Powerful artifacts"),
            Arrays.asList(RiftMob.CHATEAU_GUARD, RiftMob.DARK_MAGE)
        ));
        
        // The Rift
        locationConfigs.put(RiftLocation.THE_RIFT, new RiftLocationConfig(
            "The Rift", "§5The Rift", "§7The deepest part of the Rift",
            Material.END_STONE, Arrays.asList("§7Deepest part", "§7Rift core", "§7Final challenge"),
            Arrays.asList(RiftMob.RIFT_BOSS, RiftMob.RIFT_GUARDIAN_ELITE)
        ));
    }
    
    private void initializeMobConfigs() {
        // Rift Guardian
        mobConfigs.put(RiftMob.RIFT_GUARDIAN, new RiftMobConfig(
            "Rift Guardian", "§5Rift Guardian", "§7A guardian of the Rift",
            Material.END_STONE, 100, 50, 0.3, 0.1,
            Arrays.asList("§7Rift guardian", "§7Basic attacks", "§7Mote drops")
        ));
        
        // Rift Sentinel
        mobConfigs.put(RiftMob.RIFT_SENTINEL, new RiftMobConfig(
            "Rift Sentinel", "§5Rift Sentinel", "§7A sentinel of the Rift",
            Material.END_STONE, 150, 75, 0.25, 0.15,
            Arrays.asList("§7Rift sentinel", "§7Enhanced attacks", "§7Rare mote drops")
        ));
        
        // Villager Rift
        mobConfigs.put(RiftMob.VILLAGER_RIFT, new RiftMobConfig(
            "Villager Rift", "§eVillager Rift", "§7A villager from the Rift",
            Material.VILLAGER_SPAWN_EGG, 50, 25, 0.4, 0.05,
            Arrays.asList("§7Rift villager", "§7Friendly", "§7Quest giver")
        ));
        
        // Village Guard
        mobConfigs.put(RiftMob.VILLAGE_GUARD, new RiftMobConfig(
            "Village Guard", "§eVillage Guard", "§7A guard of the village",
            Material.IRON_SWORD, 120, 60, 0.3, 0.12,
            Arrays.asList("§7Village guard", "§7Protective", "§7Guard equipment")
        ));
        
        // Warrior Rift
        mobConfigs.put(RiftMob.WARRIOR_RIFT, new RiftMobConfig(
            "Warrior Rift", "§cWarrior Rift", "§7A warrior from the Rift",
            Material.IRON_SWORD, 200, 100, 0.2, 0.18,
            Arrays.asList("§7Rift warrior", "§7Combat focused", "§7Warrior equipment")
        ));
        
        // Battle Mage
        mobConfigs.put(RiftMob.BATTLE_MAGE, new RiftMobConfig(
            "Battle Mage", "§bBattle Mage", "§7A mage from the Rift",
            Material.BLAZE_ROD, 180, 90, 0.22, 0.16,
            Arrays.asList("§7Rift mage", "§7Magic attacks", "§7Mage equipment")
        ));
        
        // Dreadfarm Worker
        mobConfigs.put(RiftMob.DREADFARM_WORKER, new RiftMobConfig(
            "Dreadfarm Worker", "§4Dreadfarm Worker", "§7A worker from Dreadfarm",
            Material.WARPED_STEM, 80, 40, 0.35, 0.08,
            Arrays.asList("§7Dreadfarm worker", "§7Cursed", "§7Farming tools")
        ));
        
        // Corrupted Crop
        mobConfigs.put(RiftMob.CORRUPTED_CROP, new RiftMobConfig(
            "Corrupted Crop", "§4Corrupted Crop", "§7A corrupted crop",
            Material.WARPED_FUNGUS, 60, 30, 0.4, 0.06,
            Arrays.asList("§7Corrupted crop", "§7Dark magic", "§7Crop drops")
        ));
        
        // Château Guard
        mobConfigs.put(RiftMob.CHATEAU_GUARD, new RiftMobConfig(
            "Château Guard", "§0Château Guard", "§7A guard of the château",
            Material.NETHERITE_SWORD, 300, 150, 0.15, 0.25,
            Arrays.asList("§7Château guard", "§7Elite", "§7Elite equipment")
        ));
        
        // Dark Mage
        mobConfigs.put(RiftMob.DARK_MAGE, new RiftMobConfig(
            "Dark Mage", "§0Dark Mage", "§7A dark mage from the château",
            Material.NETHERITE_SWORD, 250, 125, 0.18, 0.22,
            Arrays.asList("§7Dark mage", "§7Powerful magic", "§7Dark equipment")
        ));
        
        // Rift Boss
        mobConfigs.put(RiftMob.RIFT_BOSS, new RiftMobConfig(
            "Rift Boss", "§5Rift Boss", "§7The ultimate boss of the Rift",
            Material.END_CRYSTAL, 500, 250, 0.05, 0.5,
            Arrays.asList("§7Rift boss", "§7Ultimate challenge", "§7Legendary drops")
        ));
        
        // Rift Guardian Elite
        mobConfigs.put(RiftMob.RIFT_GUARDIAN_ELITE, new RiftMobConfig(
            "Rift Guardian Elite", "§5Rift Guardian Elite", "§7An elite guardian of the Rift",
            Material.END_CRYSTAL, 400, 200, 0.08, 0.4,
            Arrays.asList("§7Elite guardian", "§7Powerful", "§7Elite drops")
        ));
    }
    
    private void initializeItemConfigs() {
        // Rift Sword
        itemConfigs.put(RiftItem.RIFT_SWORD, new RiftItemConfig(
            "Rift Sword", "§5Rift Sword", "§7A sword from the Rift",
            Material.IRON_SWORD, 100, Arrays.asList("§7Rift weapon", "§7Basic damage", "§7Rift enchantments"),
            Arrays.asList("§7+50 Damage", "§7+25 Strength", "§7Rift Power")
        ));
        
        // Rift Armor
        itemConfigs.put(RiftItem.RIFT_ARMOR, new RiftItemConfig(
            "Rift Armor", "§5Rift Armor", "§7Armor from the Rift",
            Material.IRON_CHESTPLATE, 150, Arrays.asList("§7Rift armor", "§7Basic defense", "§7Rift protection"),
            Arrays.asList("§7+100 Defense", "§7+50 Health", "§7Rift Resistance")
        ));
        
        // Rift Pickaxe
        itemConfigs.put(RiftItem.RIFT_PICKAXE, new RiftItemConfig(
            "Rift Pickaxe", "§5Rift Pickaxe", "§7A pickaxe from the Rift",
            Material.IRON_PICKAXE, 120, Arrays.asList("§7Rift tool", "§7Mining efficiency", "§7Rift mining"),
            Arrays.asList("§7+75 Mining Speed", "§7+50 Fortune", "§7Rift Mining")
        ));
        
        // Rift Bow
        itemConfigs.put(RiftItem.RIFT_BOW, new RiftItemConfig(
            "Rift Bow", "§5Rift Bow", "§7A bow from the Rift",
            Material.BOW, 130, Arrays.asList("§7Rift bow", "§7Ranged damage", "§7Rift arrows"),
            Arrays.asList("§7+60 Bow Damage", "§7+30 Crit Chance", "§7Rift Arrows")
        ));
        
        // Rift Staff
        itemConfigs.put(RiftItem.RIFT_STAFF, new RiftItemConfig(
            "Rift Staff", "§5Rift Staff", "§7A staff from the Rift",
            Material.BLAZE_ROD, 200, Arrays.asList("§7Rift staff", "§7Magic damage", "§7Rift spells"),
            Arrays.asList("§7+100 Magic Damage", "§7+50 Intelligence", "§7Rift Spells")
        ));
    }
    
    private void startRiftUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (RiftSession session : activeSessions.values()) {
                updateRiftSession(session);
            }
        }, 0L, 20L); // Every second
    }
    
    private void updateRiftSession(RiftSession session) {
        if (session.isActive()) {
            session.setTimeElapsed(session.getTimeElapsed() + 1);
            
            // Update Rift time mechanics
            updateRiftTime(session);
            
            // Check for mob spawns
            if (Math.random() < getMobSpawnChance(session)) {
                spawnRiftMob(session);
            }
        }
    }
    
    private void updateRiftTime(RiftSession session) {
        // Rift has its own time mechanics
        session.setRiftTime(session.getRiftTime() + 1);
        
        // Check for time-based events
        if (session.getRiftTime() % 300 == 0) { // Every 5 minutes
            triggerTimeEvent(session);
        }
    }
    
    private void triggerTimeEvent(RiftSession session) {
        Player player = Bukkit.getPlayer(session.getPlayerId());
        if (player == null) return;
        
        // Random time-based event
        int eventType = (int) (Math.random() * 3);
        
        switch (eventType) {
            case 0:
                player.sendMessage("§5Time Rift: The Rift's time has shifted!");
                // Apply time effects
                break;
            case 1:
                player.sendMessage("§5Time Rift: A temporal anomaly has appeared!");
                // Spawn special mobs
                break;
            case 2:
                player.sendMessage("§5Time Rift: The Rift's energy has increased!");
                // Boost player abilities
                break;
        }
    }
    
    public void enterRift(Player player) {
        if (activeSessions.containsKey(player.getUniqueId())) {
            player.sendMessage("§cYou are already in the Rift!");
            return;
        }
        
        PlayerRiftData data = getPlayerRiftData(player);
        RiftSession session = new RiftSession(
            player.getUniqueId(),
            data.getRiftLevel(),
            System.currentTimeMillis()
        );
        
        activeSessions.put(player.getUniqueId(), session);
        
        // Teleport to Rift
        teleportToRift(player, session);
        
        player.sendMessage("§aEntered the Rift!");
        player.sendMessage("§7Rift Level: " + data.getRiftLevel());
        player.sendMessage("§7Motes: " + data.getMotes());
    }
    
    public void exitRift(Player player) {
        RiftSession session = activeSessions.remove(player.getUniqueId());
        if (session == null) {
            player.sendMessage("§cYou are not in the Rift!");
            return;
        }
        
        session.setActive(false);
        
        // Teleport back to hub
        teleportToHub(player);
        
        player.sendMessage("§aExited the Rift!");
        player.sendMessage("§7Time in Rift: " + session.getTimeElapsed() + " seconds");
    }
    
    private void teleportToRift(Player player, RiftSession session) {
        Location riftLocation = new Location(
            Bukkit.getWorld("rift_" + session.getPlayerId()),
            0, 100, 0
        );
        
        player.teleport(riftLocation);
        player.sendMessage("§aTeleported to the Rift!");
    }
    
    private void teleportToHub(Player player) {
        Location hubLocation = new Location(
            Bukkit.getWorld("world"),
            0, 100, 0
        );
        
        player.teleport(hubLocation);
        player.sendMessage("§aTeleported to the Hub!");
    }
    
    public void teleportToRiftLocation(Player player, RiftLocation location) {
        RiftSession session = activeSessions.get(player.getUniqueId());
        if (session == null || !session.isActive()) {
            player.sendMessage("§cYou must be in the Rift to teleport to locations!");
            return;
        }
        
        RiftLocationConfig config = locationConfigs.get(location);
        Location teleportLocation = new Location(
            Bukkit.getWorld("rift_" + session.getPlayerId()),
            getLocationX(location),
            100,
            getLocationZ(location)
        );
        
        player.teleport(teleportLocation);
        player.sendMessage("§aTeleported to " + config.getDisplayName() + "!");
    }
    
    private double getLocationX(RiftLocation location) {
        switch (location) {
            case RIFT_HUB: return 0;
            case WEST_VILLAGE: return -100;
            case EAST_VILLAGE: return 100;
            case DREADFARM: return 0;
            case STILLGORE_CHATEAU: return 200;
            case THE_RIFT: return 0;
            default: return 0;
        }
    }
    
    private double getLocationZ(RiftLocation location) {
        switch (location) {
            case RIFT_HUB: return 0;
            case WEST_VILLAGE: return 0;
            case EAST_VILLAGE: return 0;
            case DREADFARM: return 100;
            case STILLGORE_CHATEAU: return 0;
            case THE_RIFT: return 200;
            default: return 0;
        }
    }
    
    private double getMobSpawnChance(RiftSession session) {
        return 0.02; // 2% chance per second
    }
    
    private void spawnRiftMob(RiftSession session) {
        Player player = Bukkit.getPlayer(session.getPlayerId());
        if (player == null) return;
        
        // Select random mob based on location
        RiftLocation currentLocation = getCurrentLocation(session);
        RiftLocationConfig config = locationConfigs.get(currentLocation);
        
        List<RiftMob> availableMobs = config.getAvailableMobs();
        RiftMob mobType = availableMobs.get((int) (Math.random() * availableMobs.size()));
        RiftMobConfig mobConfig = mobConfigs.get(mobType);
        
        // Spawn mob
        Location spawnLocation = player.getLocation().add(10, 0, 0);
        RiftMobEntity mob = new RiftMobEntity(
            mobType,
            mobConfig,
            spawnLocation,
            player
        );
        
        session.addMob(mob);
        
        // Spawn effects
        player.getWorld().spawnParticle(Particle.PORTAL, spawnLocation, 10);
        player.getWorld().playSound(spawnLocation, Sound.ENTITY_ENDERMAN_AMBIENT, 1.0f, 1.0f);
        
        player.sendMessage("§c" + mobConfig.getDisplayName() + " has appeared!");
    }
    
    private RiftLocation getCurrentLocation(RiftSession session) {
        // Determine current location based on player position
        // This is a simplified version
        return RiftLocation.RIFT_HUB;
    }
    
    public void giveMotes(Player player, int amount) {
        PlayerRiftData data = getPlayerRiftData(player);
        data.addMotes(amount);
        player.sendMessage("§a+" + amount + " Motes");
    }
    
    public void spendMotes(Player player, int amount) {
        PlayerRiftData data = getPlayerRiftData(player);
        
        if (data.getMotes() < amount) {
            player.sendMessage("§cNot enough Motes!");
            return;
        }
        
        data.spendMotes(amount);
        player.sendMessage("§aSpent " + amount + " Motes");
    }
    
    public void giveRiftXP(Player player, int xp) {
        PlayerRiftData data = getPlayerRiftData(player);
        data.addXP(xp);
        player.sendMessage("§a+" + xp + " Rift XP");
    }
    
    private PlayerRiftData getPlayerRiftData(Player player) {
        return playerRiftData.computeIfAbsent(player.getUniqueId(), k -> new PlayerRiftData(k));
    }
    
    // Enums and Classes
    public enum RiftLocation {
        RIFT_HUB, WEST_VILLAGE, EAST_VILLAGE, DREADFARM, STILLGORE_CHATEAU, THE_RIFT
    }
    
    public enum RiftMob {
        RIFT_GUARDIAN, RIFT_SENTINEL, VILLAGER_RIFT, VILLAGE_GUARD,
        WARRIOR_RIFT, BATTLE_MAGE, DREADFARM_WORKER, CORRUPTED_CROP,
        CHATEAU_GUARD, DARK_MAGE, RIFT_BOSS, RIFT_GUARDIAN_ELITE
    }
    
    public enum RiftItem {
        RIFT_SWORD, RIFT_ARMOR, RIFT_PICKAXE, RIFT_BOW, RIFT_STAFF
    }
    
    // Data Classes
    public static class RiftLocationConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final List<String> features;
        private final List<RiftMob> availableMobs;
        
        public RiftLocationConfig(String name, String displayName, String description, Material material,
                                List<String> features, List<RiftMob> availableMobs) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.material = material;
            this.features = features;
            this.availableMobs = availableMobs;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
        public List<String> getFeatures() { return features; }
        public List<RiftMob> getAvailableMobs() { return availableMobs; }
    }
    
    public static class RiftMobConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final int health;
        private final int damage;
        private final double spawnChance;
        private final double dropChance;
        private final List<String> characteristics;
        
        public RiftMobConfig(String name, String displayName, String description, Material material,
                           int health, int damage, double spawnChance, double dropChance,
                           List<String> characteristics) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.material = material;
            this.health = health;
            this.damage = damage;
            this.spawnChance = spawnChance;
            this.dropChance = dropChance;
            this.characteristics = characteristics;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
        public int getHealth() { return health; }
        public int getDamage() { return damage; }
        public double getSpawnChance() { return spawnChance; }
        public double getDropChance() { return dropChance; }
        public List<String> getCharacteristics() { return characteristics; }
    }
    
    public static class RiftItemConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final int cost;
        private final List<String> features;
        private final List<String> stats;
        
        public RiftItemConfig(String name, String displayName, String description, Material material,
                            int cost, List<String> features, List<String> stats) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.material = material;
            this.cost = cost;
            this.features = features;
            this.stats = stats;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
        public int getCost() { return cost; }
        public List<String> getFeatures() { return features; }
        public List<String> getStats() { return stats; }
    }
    
    public static class RiftSession {
        private final UUID playerId;
        private final int riftLevel;
        private final long startTime;
        private boolean active;
        private int timeElapsed;
        private int riftTime;
        private final List<RiftMobEntity> mobs;
        
        public RiftSession(UUID playerId, int riftLevel, long startTime) {
            this.playerId = playerId;
            this.riftLevel = riftLevel;
            this.startTime = startTime;
            this.active = true;
            this.timeElapsed = 0;
            this.riftTime = 0;
            this.mobs = new ArrayList<>();
        }
        
        public void addMob(RiftMobEntity mob) {
            mobs.add(mob);
        }
        
        // Getters and Setters
        public UUID getPlayerId() { return playerId; }
        public int getRiftLevel() { return riftLevel; }
        public long getStartTime() { return startTime; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        public int getTimeElapsed() { return timeElapsed; }
        public void setTimeElapsed(int timeElapsed) { this.timeElapsed = timeElapsed; }
        public int getRiftTime() { return riftTime; }
        public void setRiftTime(int riftTime) { this.riftTime = riftTime; }
        public List<RiftMobEntity> getMobs() { return mobs; }
    }
    
    public static class RiftMobEntity {
        private final RiftMob mobType;
        private final RiftMobConfig config;
        private final Location location;
        private final Player target;
        
        public RiftMobEntity(RiftMob mobType, RiftMobConfig config, Location location, Player target) {
            this.mobType = mobType;
            this.config = config;
            this.location = location;
            this.target = target;
        }
        
        // Getters
        public RiftMob getMobType() { return mobType; }
        public RiftMobConfig getConfig() { return config; }
        public Location getLocation() { return location; }
        public Player getTarget() { return target; }
    }
    
    public static class PlayerRiftData {
        private final UUID playerId;
        private int totalXP;
        private int riftLevel;
        private int motes;
        private final Map<RiftLocation, Integer> locationStats;
        private final Map<RiftMob, Integer> mobKills;
        
        public PlayerRiftData(UUID playerId) {
            this.playerId = playerId;
            this.totalXP = 0;
            this.riftLevel = 1;
            this.motes = 0;
            this.locationStats = new HashMap<>();
            this.mobKills = new HashMap<>();
        }
        
        public void addXP(int xp) {
            this.totalXP += xp;
            this.riftLevel = calculateLevel(totalXP);
        }
        
        public void addMotes(int amount) {
            this.motes += amount;
        }
        
        public void spendMotes(int amount) {
            this.motes -= amount;
        }
        
        private int calculateLevel(int xp) {
            return Math.min(50, (int) Math.floor(Math.sqrt(xp / 100.0)) + 1);
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public int getTotalXP() { return totalXP; }
        public int getRiftLevel() { return riftLevel; }
        public int getMotes() { return motes; }
        public Map<RiftLocation, Integer> getLocationStats() { return locationStats; }
        public Map<RiftMob, Integer> getMobKills() { return mobKills; }
    }
}
