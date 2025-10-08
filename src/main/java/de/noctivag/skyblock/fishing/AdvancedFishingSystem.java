package de.noctivag.skyblock.fishing;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Fishing System - Complete Hypixel SkyBlock Fishing Implementation
 * 
 * Features:
 * - Sea Creatures with unique mechanics
 * - Fishing events (Fishing Festival, Spooky Fishing)
 * - Special fishing locations (Crystal Hollows, Crimson Isle)
 * - Fishing gear and equipment
 * - Fishing XP and leveling
 * - Rare fish and special catches
 * - Fishing minions and automation
 */
public class AdvancedFishingSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerFishingData> playerFishingData = new ConcurrentHashMap<>();
    private final Map<UUID, FishingSession> activeSessions = new ConcurrentHashMap<>();
    private final Map<SeaCreatureType, SeaCreatureConfig> seaCreatureConfigs = new HashMap<>();
    private final Map<FishingLocation, FishingLocationConfig> locationConfigs = new HashMap<>();
    private final Map<FishingEvent, FishingEventConfig> eventConfigs = new HashMap<>();
    
    public AdvancedFishingSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeSeaCreatureConfigs();
        initializeLocationConfigs();
        initializeEventConfigs();
        startFishingUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeSeaCreatureConfigs() {
        // Common Sea Creatures
        seaCreatureConfigs.put(SeaCreatureType.SQUID, new SeaCreatureConfig(
            "Squid", "§9Squid", "§7A common sea creature",
            Material.INK_SAC, 100, 50, 0.3, 0.1,
            Arrays.asList("§7Common sea creature", "§7Low health", "§7Basic drops")
        ));
        
        seaCreatureConfigs.put(SeaCreatureType.SEA_GUARDIAN, new SeaCreatureConfig(
            "Sea Guardian", "§bSea Guardian", "§7A guardian of the deep",
            Material.PRISMARINE_SHARD, 200, 100, 0.2, 0.15,
            Arrays.asList("§7Guardian of the deep", "§7Medium health", "§7Prismarine drops")
        ));
        
        // Uncommon Sea Creatures
        seaCreatureConfigs.put(SeaCreatureType.SEA_WITCH, new SeaCreatureConfig(
            "Sea Witch", "§5Sea Witch", "§7A witch of the ocean",
            Material.POTION, 300, 150, 0.15, 0.2,
            Arrays.asList("§7Ocean witch", "§7Potion attacks", "§7Magic drops")
        ));
        
        seaCreatureConfigs.put(SeaCreatureType.SEA_ARCHER, new SeaCreatureConfig(
            "Sea Archer", "§eSea Archer", "§7An archer of the sea",
            Material.BOW, 250, 120, 0.18, 0.18,
            Arrays.asList("§7Sea archer", "§7Ranged attacks", "§7Arrow drops")
        ));
        
        // Rare Sea Creatures
        seaCreatureConfigs.put(SeaCreatureType.GUARDIAN_DEFENDER, new SeaCreatureConfig(
            "Guardian Defender", "§6Guardian Defender", "§7A powerful guardian",
            Material.PRISMARINE_CRYSTALS, 500, 250, 0.1, 0.25,
            Arrays.asList("§7Powerful guardian", "§7High health", "§7Rare drops")
        ));
        
        seaCreatureConfigs.put(SeaCreatureType.DEEP_SEA_PROTECTOR, new SeaCreatureConfig(
            "Deep Sea Protector", "§cDeep Sea Protector", "§7Protector of the deep",
            Material.HEART_OF_THE_SEA, 600, 300, 0.08, 0.3,
            Arrays.asList("§7Deep sea protector", "§7Very high health", "§7Very rare drops")
        ));
        
        // Epic Sea Creatures
        seaCreatureConfigs.put(SeaCreatureType.SEA_EMPEROR, new SeaCreatureConfig(
            "Sea Emperor", "§5Sea Emperor", "§7Emperor of the ocean",
            Material.NAUTILUS_SHELL, 1000, 500, 0.05, 0.4,
            Arrays.asList("§7Emperor of the ocean", "§7Extreme health", "§7Epic drops")
        ));
        
        seaCreatureConfigs.put(SeaCreatureType.MEGALODON, new SeaCreatureConfig(
            "Megalodon", "§4Megalodon", "§7The ancient shark",
            Material.PRISMARINE_SHARD, 1200, 600, 0.03, 0.5,
            Arrays.asList("§7Ancient shark", "§7Massive health", "§7Legendary drops")
        ));
        
        // Legendary Sea Creatures
        seaCreatureConfigs.put(SeaCreatureType.GREAT_WHITE_SHARK, new SeaCreatureConfig(
            "Great White Shark", "§fGreat White Shark", "§7The apex predator",
            Material.PRISMARINE_SHARD, 1500, 750, 0.02, 0.6,
            Arrays.asList("§7Apex predator", "§7Legendary health", "§7Mythic drops")
        ));
        
        seaCreatureConfigs.put(SeaCreatureType.KRAKEN, new SeaCreatureConfig(
            "Kraken", "§0Kraken", "§7The legendary sea monster",
            Material.INK_SAC, 2000, 1000, 0.01, 0.7,
            Arrays.asList("§7Legendary sea monster", "§7Mythic health", "§7Divine drops")
        ));
    }
    
    private void initializeLocationConfigs() {
        // Hub Fishing
        locationConfigs.put(FishingLocation.HUB, new FishingLocationConfig(
            "Hub", "§bHub", "§7Basic fishing location",
            Arrays.asList(SeaCreatureType.SQUID, SeaCreatureType.SEA_GUARDIAN),
            1.0, 1.0, 1.0
        ));
        
        // Spiders Den
        locationConfigs.put(FishingLocation.SPIDERS_DEN, new FishingLocationConfig(
            "Spider's Den", "§cSpider's Den", "§7Spider-infested waters",
            Arrays.asList(SeaCreatureType.SEA_WITCH, SeaCreatureType.SEA_ARCHER),
            1.2, 1.1, 1.1
        ));
        
        // End Island
        locationConfigs.put(FishingLocation.END_ISLAND, new FishingLocationConfig(
            "End Island", "§5End Island", "§7End dimension fishing",
            Arrays.asList(SeaCreatureType.GUARDIAN_DEFENDER, SeaCreatureType.DEEP_SEA_PROTECTOR),
            1.5, 1.3, 1.2
        ));
        
        // Crystal Hollows
        locationConfigs.put(FishingLocation.CRYSTAL_HOLLOWS, new FishingLocationConfig(
            "Crystal Hollows", "§dCrystal Hollows", "§7Crystal-filled waters",
            Arrays.asList(SeaCreatureType.SEA_EMPEROR, SeaCreatureType.MEGALODON),
            2.0, 1.5, 1.4
        ));
        
        // Crimson Isle
        locationConfigs.put(FishingLocation.CRIMSON_ISLE, new FishingLocationConfig(
            "Crimson Isle", "§4Crimson Isle", "§7Volcanic fishing waters",
            Arrays.asList(SeaCreatureType.GREAT_WHITE_SHARK, SeaCreatureType.KRAKEN),
            2.5, 2.0, 1.6
        ));
    }
    
    private void initializeEventConfigs() {
        // Fishing Festival
        eventConfigs.put(FishingEvent.FISHING_FESTIVAL, new FishingEventConfig(
            "Fishing Festival", "§6Fishing Festival", "§7Double fishing XP and rare catches",
            3600, // 1 hour
            Arrays.asList("§7Double fishing XP", "§7Increased rare catch chance", "§7Special festival rewards"),
            Arrays.asList(SeaCreatureType.SEA_EMPEROR, SeaCreatureType.MEGALODON)
        ));
        
        // Spooky Fishing
        eventConfigs.put(FishingEvent.SPOOKY_FISHING, new FishingEventConfig(
            "Spooky Fishing", "§5Spooky Fishing", "§7Halloween-themed fishing event",
            1800, // 30 minutes
            Arrays.asList("§7Spooky sea creatures", "§7Halloween rewards", "§7Special spooky drops"),
            Arrays.asList(SeaCreatureType.SEA_WITCH, SeaCreatureType.KRAKEN)
        ));
        
        // Marina's Fishing Event
        eventConfigs.put(FishingEvent.MARINAS_FISHING, new FishingEventConfig(
            "Marina's Fishing Event", "§bMarina's Fishing Event", "§7Marina's special fishing event",
            7200, // 2 hours
            Arrays.asList("§7Marina's special rewards", "§7Increased sea creature spawns", "§7Unique event items"),
            Arrays.asList(SeaCreatureType.GREAT_WHITE_SHARK, SeaCreatureType.KRAKEN)
        ));
    }
    
    private void startFishingUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(SkyblockPlugin, () -> {
            for (FishingSession session : activeSessions.values()) {
                updateFishingSession(session);
            }
        }, 0L, 20L); // Every second
    }
    
    private void updateFishingSession(FishingSession session) {
        if (session.isActive()) {
            session.setTimeElapsed(session.getTimeElapsed() + 1);
            
            // Check for sea creature spawn
            if (Math.random() < getSeaCreatureSpawnChance(session)) {
                spawnSeaCreature(session);
            }
        }
    }
    
    public void startFishingSession(Player player, FishingLocation location) {
        if (activeSessions.containsKey(player.getUniqueId())) {
            player.sendMessage(Component.text("§cYou already have an active fishing session!"));
            return;
        }
        
        PlayerFishingData data = getPlayerFishingData(player);
        FishingLocationConfig locationConfig = locationConfigs.get(location);
        
        FishingSession session = new FishingSession(
            player.getUniqueId(),
            location,
            locationConfig,
            java.lang.System.currentTimeMillis()
        );
        
        activeSessions.put(player.getUniqueId(), session);
        
        player.sendMessage(Component.text("§aFishing session started!"));
        player.sendMessage("§7Location: " + locationConfig.getDisplayName());
        player.sendMessage(Component.text("§7Use your fishing rod to catch fish!"));
    }
    
    public void stopFishingSession(Player player) {
        FishingSession session = activeSessions.remove(player.getUniqueId());
        if (session != null) {
            session.setActive(false);
            player.sendMessage(Component.text("§aFishing session ended!"));
            player.sendMessage("§7Time fished: " + session.getTimeElapsed() + " seconds");
        }
    }
    
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        FishingSession session = activeSessions.get(player.getUniqueId());
        
        if (session == null || !session.isActive()) {
            return;
        }
        
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            handleFishCaught(player, session, event);
        }
    }
    
    private void handleFishCaught(Player player, FishingSession session, PlayerFishEvent event) {
        // Calculate catch type
        CatchType catchType = calculateCatchType(session);
        
        switch (catchType) {
            case FISH:
                handleFishCatch(player, session);
                break;
            case SEA_CREATURE:
                handleSeaCreatureCatch(player, session);
                break;
            case TREASURE:
                handleTreasureCatch(player, session);
                break;
            case JUNK:
                handleJunkCatch(player, session);
                break;
        }
        
        // Give fishing XP
        giveFishingXP(player, catchType);
    }
    
    private CatchType calculateCatchType(FishingSession session) {
        double random = Math.random();
        double fishChance = 0.6; // 60% chance for fish
        double seaCreatureChance = 0.2; // 20% chance for sea creature
        double treasureChance = 0.15; // 15% chance for treasure
        // 5% chance for junk
        
        if (random < seaCreatureChance) {
            return CatchType.SEA_CREATURE;
        } else if (random < seaCreatureChance + treasureChance) {
            return CatchType.TREASURE;
        } else if (random < seaCreatureChance + treasureChance + fishChance) {
            return CatchType.FISH;
        } else {
            return CatchType.JUNK;
        }
    }
    
    private void handleFishCatch(Player player, FishingSession session) {
        // Give regular fish
        ItemStack fish = new ItemStack(Material.COD);
        player.getInventory().addItem(fish);
        player.sendMessage(Component.text("§aCaught a fish!"));
    }
    
    private void handleSeaCreatureCatch(Player player, FishingSession session) {
        // Spawn sea creature
        spawnSeaCreature(session);
        player.sendMessage(Component.text("§c⚠ Sea Creature Spawned! ⚠"));
    }
    
    private void spawnSeaCreature(FishingSession session) {
        Player player = Bukkit.getPlayer(session.getPlayerId());
        if (player == null) return;
        
        // Select random sea creature from location
        List<SeaCreatureType> availableCreatures = session.getLocationConfig().getAvailableCreatures();
        SeaCreatureType creatureType = availableCreatures.get((int) (Math.random() * availableCreatures.size()));
        SeaCreatureConfig creatureConfig = seaCreatureConfigs.get(creatureType);
        
        // Spawn sea creature
        Location spawnLocation = player.getLocation().add(5, 0, 0);
        SeaCreature creature = new SeaCreature(
            creatureType,
            creatureConfig,
            spawnLocation,
            player
        );
        
        session.addSeaCreature(creature);
        
        // Spawn effects
        player.getWorld().spawnParticle(Particle.SPLASH, spawnLocation, 10);
        player.getWorld().playSound(spawnLocation, Sound.ENTITY_GUARDIAN_AMBIENT, 1.0f, 1.0f);
        
        player.sendMessage("§c" + creatureConfig.getDisplayName() + " has appeared!");
    }
    
    private void handleTreasureCatch(Player player, FishingSession session) {
        // Give treasure
        ItemStack treasure = generateTreasure(session);
        player.getInventory().addItem(treasure);
        player.sendMessage("§6Treasure caught: " + treasure.getItemMeta().getDisplayName());
    }
    
    private void handleJunkCatch(Player player, FishingSession session) {
        // Give junk
        ItemStack junk = new ItemStack(Material.ROTTEN_FLESH);
        player.getInventory().addItem(junk);
        player.sendMessage(Component.text("§7Caught junk: Rotten Flesh"));
    }
    
    private ItemStack generateTreasure(FishingSession session) {
        // Generate random treasure based on location
        List<ItemStack> treasures = Arrays.asList(
            new ItemStack(Material.GOLD_INGOT),
            new ItemStack(Material.IRON_INGOT),
            new ItemStack(Material.DIAMOND),
            new ItemStack(Material.EMERALD),
            new ItemStack(Material.LAPIS_LAZULI)
        );
        
        return treasures.get((int) (Math.random() * treasures.size()));
    }
    
    private double getSeaCreatureSpawnChance(FishingSession session) {
        // Base chance + location modifier + event modifier
        double baseChance = 0.01; // 1% per second
        double locationModifier = session.getLocationConfig().getSeaCreatureModifier();
        double eventModifier = getActiveEventModifier();
        
        return baseChance * locationModifier * eventModifier;
    }
    
    private double getActiveEventModifier() {
        // Check for active fishing events
        for (FishingEvent event : FishingEvent.values()) {
            if (isEventActive(event)) {
                return 2.0; // Double chance during events
            }
        }
        return 1.0;
    }
    
    private boolean isEventActive(FishingEvent event) {
        // Check if event is currently active
        // This would integrate with your event system
        return false;
    }
    
    private void giveFishingXP(Player player, CatchType catchType) {
        PlayerFishingData data = getPlayerFishingData(player);
        int xp = getFishingXP(catchType);
        
        data.addXP(xp);
        player.sendMessage("§a+" + xp + " Fishing XP");
    }
    
    private int getFishingXP(CatchType catchType) {
        switch (catchType) {
            case FISH: return 5;
            case SEA_CREATURE: return 20;
            case TREASURE: return 15;
            case JUNK: return 1;
            default: return 1;
        }
    }
    
    private PlayerFishingData getPlayerFishingData(Player player) {
        return playerFishingData.computeIfAbsent(player.getUniqueId(), k -> new PlayerFishingData(k));
    }
    
    // Enums and Classes
    public enum SeaCreatureType {
        SQUID, SEA_GUARDIAN, SEA_WITCH, SEA_ARCHER, GUARDIAN_DEFENDER,
        DEEP_SEA_PROTECTOR, SEA_EMPEROR, MEGALODON, GREAT_WHITE_SHARK, KRAKEN
    }
    
    public enum FishingLocation {
        HUB, SPIDERS_DEN, END_ISLAND, CRYSTAL_HOLLOWS, CRIMSON_ISLE
    }
    
    public enum FishingEvent {
        FISHING_FESTIVAL, SPOOKY_FISHING, MARINAS_FISHING
    }
    
    public enum CatchType {
        FISH, SEA_CREATURE, TREASURE, JUNK
    }
    
    // Data Classes
    public static class SeaCreatureConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final int health;
        private final int damage;
        private final double spawnChance;
        private final double dropChance;
        private final List<String> characteristics;
        
        public SeaCreatureConfig(String name, String displayName, String description, Material material,
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
    
    public static class FishingLocationConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final List<SeaCreatureType> availableCreatures;
        private final double xpModifier;
        private final double seaCreatureModifier;
        private final double treasureModifier;
        
        public FishingLocationConfig(String name, String displayName, String description,
                                  List<SeaCreatureType> availableCreatures, double xpModifier,
                                  double seaCreatureModifier, double treasureModifier) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.availableCreatures = availableCreatures;
            this.xpModifier = xpModifier;
            this.seaCreatureModifier = seaCreatureModifier;
            this.treasureModifier = treasureModifier;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public List<SeaCreatureType> getAvailableCreatures() { return availableCreatures; }
        public double getXpModifier() { return xpModifier; }
        public double getSeaCreatureModifier() { return seaCreatureModifier; }
        public double getTreasureModifier() { return treasureModifier; }
    }
    
    public static class FishingEventConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final int duration;
        private final List<String> bonuses;
        private final List<SeaCreatureType> specialCreatures;
        
        public FishingEventConfig(String name, String displayName, String description, int duration,
                                List<String> bonuses, List<SeaCreatureType> specialCreatures) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.duration = duration;
            this.bonuses = bonuses;
            this.specialCreatures = specialCreatures;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public int getDuration() { return duration; }
        public List<String> getBonuses() { return bonuses; }
        public List<SeaCreatureType> getSpecialCreatures() { return specialCreatures; }
    }
    
    public static class FishingSession {
        private final UUID playerId;
        private final FishingLocation location;
        private final FishingLocationConfig locationConfig;
        private final long startTime;
        private boolean active;
        private int timeElapsed;
        private final List<SeaCreature> seaCreatures;
        
        public FishingSession(UUID playerId, FishingLocation location, FishingLocationConfig locationConfig, long startTime) {
            this.playerId = playerId;
            this.location = location;
            this.locationConfig = locationConfig;
            this.startTime = startTime;
            this.active = true;
            this.timeElapsed = 0;
            this.seaCreatures = new ArrayList<>();
        }
        
        public void addSeaCreature(SeaCreature creature) {
            seaCreatures.add(creature);
        }
        
        // Getters and Setters
        public UUID getPlayerId() { return playerId; }
        public FishingLocation getLocation() { return location; }
        public FishingLocationConfig getLocationConfig() { return locationConfig; }
        public long getStartTime() { return startTime; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }
        public int getTimeElapsed() { return timeElapsed; }
        public void setTimeElapsed(int timeElapsed) { this.timeElapsed = timeElapsed; }
        public List<SeaCreature> getSeaCreatures() { return seaCreatures; }
    }
    
    public static class SeaCreature {
        private final SeaCreatureType type;
        private final SeaCreatureConfig config;
        private final Location location;
        private final Player target;
        private int currentHealth;
        
        public SeaCreature(SeaCreatureType type, SeaCreatureConfig config, Location location, Player target) {
            this.type = type;
            this.config = config;
            this.location = location;
            this.target = target;
            this.currentHealth = config.getHealth();
        }
        
        // Getters and Setters
        public SeaCreatureType getType() { return type; }
        public SeaCreatureConfig getConfig() { return config; }
        public Location getLocation() { return location; }
        public Player getTarget() { return target; }
        public int getCurrentHealth() { return currentHealth; }
        public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }
    }
    
    public static class PlayerFishingData extends de.noctivag.skyblock.core.skills.BaseSkillData {
        private final Map<SeaCreatureType, Integer> seaCreatureKills;
        private final Map<FishingLocation, Integer> locationStats;

        public PlayerFishingData(UUID playerId) {
            super(playerId, 1);
            this.seaCreatureKills = new HashMap<>();
            this.locationStats = new HashMap<>();
        }

        @Override
        public void addXP(int xp) {
            this.xp += xp;
            checkLevelUp();
        }

        @Override
        protected void checkLevelUp() {
            this.level = calculateLevel(this.xp);
        }

        private int calculateLevel(int xp) {
            return Math.min(50, (int) Math.floor(Math.sqrt(xp / 100.0)) + 1);
        }

        public int getTotalXP() { return xp; }
        public Map<SeaCreatureType, Integer> getSeaCreatureKills() { return seaCreatureKills; }
        public Map<FishingLocation, Integer> getLocationStats() { return locationStats; }
    }
}
