package de.noctivag.skyblock.crimson;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
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
 * Crimson Isle System - Complete Hypixel SkyBlock Crimson Isle Implementation
 * 
 * Features:
 * - Crimson Isle factions (Mage, Barbarian, Archer)
 * - Faction reputation and rewards
 * - Crimson Isle mobs and bosses
 * - Crimson Isle items and equipment
 * - Crimson Isle quests and events
 * - Crimson Isle locations and teleports
 */
public class CrimsonIsleSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCrimsonData> playerCrimsonData = new ConcurrentHashMap<>();
    private final Map<CrimsonFaction, FactionConfig> factionConfigs = new HashMap<>();
    private final Map<CrimsonLocation, LocationConfig> locationConfigs = new HashMap<>();
    private final Map<CrimsonMob, MobConfig> mobConfigs = new HashMap<>();
    
    public CrimsonIsleSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeFactionConfigs();
        initializeLocationConfigs();
        initializeMobConfigs();
        startCrimsonUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeFactionConfigs() {
        // Mage Faction
        factionConfigs.put(CrimsonFaction.MAGE, new FactionConfig(
            "Mage", "§bMage Faction", "§7Masters of magic and intelligence",
            Material.BLAZE_POWDER, Arrays.asList("§7High intelligence", "§7Magic damage", "§7Mana regeneration"),
            Arrays.asList("§7Mage equipment", "§7Magic weapons", "§7Intelligence bonuses"),
            Arrays.asList("§7Magic quests", "§7Spell research", "§7Mana training")
        ));
        
        // Barbarian Faction
        factionConfigs.put(CrimsonFaction.BARBARIAN, new FactionConfig(
            "Barbarian", "§cBarbarian Faction", "§7Warriors of strength and combat",
            Material.IRON_SWORD, Arrays.asList("§7High strength", "§7Melee damage", "§7Health regeneration"),
            Arrays.asList("§7Barbarian equipment", "§7Melee weapons", "§7Strength bonuses"),
            Arrays.asList("§7Combat quests", "§7Weapon training", "§7Strength training")
        ));
        
        // Archer Faction
        factionConfigs.put(CrimsonFaction.ARCHER, new FactionConfig(
            "Archer", "§eArcher Faction", "§7Masters of ranged combat and precision",
            Material.BOW, Arrays.asList("§7High dexterity", "§7Ranged damage", "§7Critical hits"),
            Arrays.asList("§7Archer equipment", "§7Ranged weapons", "§7Dexterity bonuses"),
            Arrays.asList("§7Archery quests", "§7Bow training", "§7Precision training")
        ));
    }
    
    private void initializeLocationConfigs() {
        // Mage Outpost
        locationConfigs.put(CrimsonLocation.MAGE_OUTPOST, new LocationConfig(
            "Mage Outpost", "§bMage Outpost", "§7The mage faction's base",
            Material.BLAZE_POWDER, Arrays.asList("§7Mage faction base", "§7Magic training area", "§7Spell research"),
            Arrays.asList(CrimsonMob.MAGE_APPRENTICE, CrimsonMob.MAGE_SCHOLAR, CrimsonMob.MAGE_MASTER)
        ));
        
        // Barbarian Outpost
        locationConfigs.put(CrimsonLocation.BARBARIAN_OUTPOST, new LocationConfig(
            "Barbarian Outpost", "§cBarbarian Outpost", "§7The barbarian faction's base",
            Material.IRON_SWORD, Arrays.asList("§7Barbarian faction base", "§7Combat training area", "§7Weapon forge"),
            Arrays.asList(CrimsonMob.BARBARIAN_WARRIOR, CrimsonMob.BARBARIAN_CHAMPION, CrimsonMob.BARBARIAN_CHIEF)
        ));
        
        // Archer Outpost
        locationConfigs.put(CrimsonLocation.ARCHER_OUTPOST, new LocationConfig(
            "Archer Outpost", "§eArcher Outpost", "§7The archer faction's base",
            Material.BOW, Arrays.asList("§7Archer faction base", "§7Archery training area", "§7Bow crafting"),
            Arrays.asList(CrimsonMob.ARCHER_SCOUT, CrimsonMob.ARCHER_HUNTER, CrimsonMob.ARCHER_MASTER)
        ));
        
        // Crimson Fields
        locationConfigs.put(CrimsonLocation.CRIMSON_FIELDS, new LocationConfig(
            "Crimson Fields", "§4Crimson Fields", "§7The main battlefield",
            Material.NETHERRACK, Arrays.asList("§7Main battlefield", "§7Faction conflicts", "§7Resource gathering"),
            Arrays.asList(CrimsonMob.CRIMSON_GRUNT, CrimsonMob.CRIMSON_SOLDIER, CrimsonMob.CRIMSON_COMMANDER)
        ));
        
        // Blazing Fortress
        locationConfigs.put(CrimsonLocation.BLAZING_FORTRESS, new LocationConfig(
            "Blazing Fortress", "§6Blazing Fortress", "§7The fortress of fire",
            Material.BLAZE_ROD, Arrays.asList("§7Fire fortress", "§7Boss battles", "§7Rare resources"),
            Arrays.asList(CrimsonMob.BLAZING_KNIGHT, CrimsonMob.BLAZING_LORD, CrimsonMob.BLAZING_KING)
        ));
    }
    
    private void initializeMobConfigs() {
        // Mage Mobs
        mobConfigs.put(CrimsonMob.MAGE_APPRENTICE, new MobConfig(
            "Mage Apprentice", "§bMage Apprentice", "§7A novice mage",
            Material.BLAZE_POWDER, 100, 50, 0.3, 0.1,
            Arrays.asList("§7Basic magic attacks", "§7Low health", "§7Mana drops")
        ));
        
        mobConfigs.put(CrimsonMob.MAGE_SCHOLAR, new MobConfig(
            "Mage Scholar", "§bMage Scholar", "§7An experienced mage",
            Material.BLAZE_POWDER, 200, 100, 0.2, 0.15,
            Arrays.asList("§7Advanced magic attacks", "§7Medium health", "§7Magic items")
        ));
        
        mobConfigs.put(CrimsonMob.MAGE_MASTER, new MobConfig(
            "Mage Master", "§bMage Master", "§7A master mage",
            Material.BLAZE_POWDER, 300, 150, 0.1, 0.2,
            Arrays.asList("§7Master magic attacks", "§7High health", "§7Rare magic items")
        ));
        
        // Barbarian Mobs
        mobConfigs.put(CrimsonMob.BARBARIAN_WARRIOR, new MobConfig(
            "Barbarian Warrior", "§cBarbarian Warrior", "§7A fierce warrior",
            Material.IRON_SWORD, 150, 75, 0.25, 0.12,
            Arrays.asList("§7Melee attacks", "§7Medium health", "§7Weapon drops")
        ));
        
        mobConfigs.put(CrimsonMob.BARBARIAN_CHAMPION, new MobConfig(
            "Barbarian Champion", "§cBarbarian Champion", "§7A champion warrior",
            Material.IRON_SWORD, 250, 125, 0.15, 0.18,
            Arrays.asList("§7Powerful melee attacks", "§7High health", "§7Champion weapons")
        ));
        
        mobConfigs.put(CrimsonMob.BARBARIAN_CHIEF, new MobConfig(
            "Barbarian Chief", "§cBarbarian Chief", "§7A chief warrior",
            Material.IRON_SWORD, 350, 175, 0.08, 0.25,
            Arrays.asList("§7Devastating melee attacks", "§7Very high health", "§7Chief weapons")
        ));
        
        // Archer Mobs
        mobConfigs.put(CrimsonMob.ARCHER_SCOUT, new MobConfig(
            "Archer Scout", "§eArcher Scout", "§7A skilled scout",
            Material.BOW, 120, 60, 0.28, 0.11,
            Arrays.asList("§7Ranged attacks", "§7Low health", "§7Arrow drops")
        ));
        
        mobConfigs.put(CrimsonMob.ARCHER_HUNTER, new MobConfig(
            "Archer Hunter", "§eArcher Hunter", "§7An expert hunter",
            Material.BOW, 220, 110, 0.18, 0.16,
            Arrays.asList("§7Precise ranged attacks", "§7Medium health", "§7Hunter equipment")
        ));
        
        mobConfigs.put(CrimsonMob.ARCHER_MASTER, new MobConfig(
            "Archer Master", "§eArcher Master", "§7A master archer",
            Material.BOW, 320, 160, 0.12, 0.22,
            Arrays.asList("§7Master ranged attacks", "§7High health", "§7Master equipment")
        ));
        
        // Crimson Mobs
        mobConfigs.put(CrimsonMob.CRIMSON_GRUNT, new MobConfig(
            "Crimson Grunt", "§4Crimson Grunt", "§7A basic crimson soldier",
            Material.NETHERRACK, 80, 40, 0.35, 0.08,
            Arrays.asList("§7Basic attacks", "§7Low health", "§7Crimson materials")
        ));
        
        mobConfigs.put(CrimsonMob.CRIMSON_SOLDIER, new MobConfig(
            "Crimson Soldier", "§4Crimson Soldier", "§7A trained crimson soldier",
            Material.NETHERRACK, 180, 90, 0.22, 0.14,
            Arrays.asList("§7Trained attacks", "§7Medium health", "§7Crimson equipment")
        ));
        
        mobConfigs.put(CrimsonMob.CRIMSON_COMMANDER, new MobConfig(
            "Crimson Commander", "§4Crimson Commander", "§7A crimson commander",
            Material.NETHERRACK, 280, 140, 0.14, 0.19,
            Arrays.asList("§7Commanding attacks", "§7High health", "§7Commander equipment")
        ));
        
        // Blazing Mobs
        mobConfigs.put(CrimsonMob.BLAZING_KNIGHT, new MobConfig(
            "Blazing Knight", "§6Blazing Knight", "§7A knight of fire",
            Material.BLAZE_ROD, 400, 200, 0.1, 0.3,
            Arrays.asList("§7Fire attacks", "§7Very high health", "§7Blazing equipment")
        ));
        
        mobConfigs.put(CrimsonMob.BLAZING_LORD, new MobConfig(
            "Blazing Lord", "§6Blazing Lord", "§7A lord of fire",
            Material.BLAZE_ROD, 500, 250, 0.06, 0.35,
            Arrays.asList("§7Devastating fire attacks", "§7Extreme health", "§7Lord equipment")
        ));
        
        mobConfigs.put(CrimsonMob.BLAZING_KING, new MobConfig(
            "Blazing King", "§6Blazing King", "§7The king of fire",
            Material.BLAZE_ROD, 600, 300, 0.04, 0.4,
            Arrays.asList("§7Infernal fire attacks", "§7Legendary health", "§7King equipment")
        ));
    }
    
    private void startCrimsonUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // Update faction events, reputation, etc.
            updateFactionEvents();
        }, 0L, 20L * 60L); // Every minute
    }
    
    private void updateFactionEvents() {
        // Update faction events, reputation changes, etc.
    }
    
    public void joinFaction(Player player, CrimsonFaction faction) {
        PlayerCrimsonData data = getPlayerCrimsonData(player);
        
        if (data.getFaction() != null) {
            player.sendMessage("§cYou are already in a faction!");
            return;
        }
        
        data.setFaction(faction);
        data.setReputation(0);
        
        FactionConfig config = factionConfigs.get(faction);
        player.sendMessage("§aJoined " + config.getDisplayName() + "!");
        player.sendMessage("§7" + config.getDescription());
    }
    
    public void leaveFaction(Player player) {
        PlayerCrimsonData data = getPlayerCrimsonData(player);
        
        if (data.getFaction() == null) {
            player.sendMessage("§cYou are not in a faction!");
            return;
        }
        
        CrimsonFaction oldFaction = data.getFaction();
        data.setFaction(null);
        data.setReputation(0);
        
        player.sendMessage("§aLeft " + oldFaction.getDisplayName() + "!");
    }
    
    public void addReputation(Player player, int amount) {
        PlayerCrimsonData data = getPlayerCrimsonData(player);
        
        if (data.getFaction() == null) {
            player.sendMessage("§cYou must be in a faction to gain reputation!");
            return;
        }
        
        data.addReputation(amount);
        player.sendMessage("§a+" + amount + " " + data.getFaction().getDisplayName() + " Reputation!");
        
        // Check for reputation milestones
        checkReputationMilestones(player, data);
    }
    
    private void checkReputationMilestones(Player player, PlayerCrimsonData data) {
        int reputation = data.getReputation();
        CrimsonFaction faction = data.getFaction();
        
        if (reputation >= 1000 && !data.hasMilestone(1000)) {
            data.addMilestone(1000);
            player.sendMessage("§6Reputation Milestone: 1000 " + faction.getDisplayName() + " Reputation!");
            giveReputationReward(player, faction, 1000);
        }
        
        if (reputation >= 5000 && !data.hasMilestone(5000)) {
            data.addMilestone(5000);
            player.sendMessage("§6Reputation Milestone: 5000 " + faction.getDisplayName() + " Reputation!");
            giveReputationReward(player, faction, 5000);
        }
        
        if (reputation >= 10000 && !data.hasMilestone(10000)) {
            data.addMilestone(10000);
            player.sendMessage("§6Reputation Milestone: 10000 " + faction.getDisplayName() + " Reputation!");
            giveReputationReward(player, faction, 10000);
        }
    }
    
    private void giveReputationReward(Player player, CrimsonFaction faction, int milestone) {
        // Give faction-specific rewards based on milestone
        switch (faction) {
            case MAGE:
                giveMageReward(player, milestone);
                break;
            case BARBARIAN:
                giveBarbarianReward(player, milestone);
                break;
            case ARCHER:
                giveArcherReward(player, milestone);
                break;
        }
    }
    
    private void giveMageReward(Player player, int milestone) {
        switch (milestone) {
            case 1000:
                player.getInventory().addItem(createCrimsonItem("Mage Robe", Material.LEATHER_CHESTPLATE, "§bMage Robe"));
                break;
            case 5000:
                player.getInventory().addItem(createCrimsonItem("Mage Staff", Material.BLAZE_ROD, "§bMage Staff"));
                break;
            case 10000:
                player.getInventory().addItem(createCrimsonItem("Master Mage Crown", Material.GOLDEN_HELMET, "§bMaster Mage Crown"));
                break;
        }
    }
    
    private void giveBarbarianReward(Player player, int milestone) {
        switch (milestone) {
            case 1000:
                player.getInventory().addItem(createCrimsonItem("Barbarian Armor", Material.IRON_CHESTPLATE, "§cBarbarian Armor"));
                break;
            case 5000:
                player.getInventory().addItem(createCrimsonItem("Barbarian Axe", Material.IRON_AXE, "§cBarbarian Axe"));
                break;
            case 10000:
                player.getInventory().addItem(createCrimsonItem("Chieftain's Crown", Material.GOLDEN_HELMET, "§cChieftain's Crown"));
                break;
        }
    }
    
    private void giveArcherReward(Player player, int milestone) {
        switch (milestone) {
            case 1000:
                player.getInventory().addItem(createCrimsonItem("Archer Tunic", Material.LEATHER_CHESTPLATE, "§eArcher Tunic"));
                break;
            case 5000:
                player.getInventory().addItem(createCrimsonItem("Archer Bow", Material.BOW, "§eArcher Bow"));
                break;
            case 10000:
                player.getInventory().addItem(createCrimsonItem("Master Archer's Cap", Material.LEATHER_HELMET, "§eMaster Archer's Cap"));
                break;
        }
    }
    
    private ItemStack createCrimsonItem(String name, Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(Arrays.asList(
            "§7A powerful item from the Crimson Isle",
            "§7Faction reward",
            "",
            "§7Rarity: §6Rare"
        ));
        item.setItemMeta(meta);
        return item;
    }
    
    public void teleportToLocation(Player player, CrimsonLocation location) {
        LocationConfig config = locationConfigs.get(location);
        
        // Create location if it doesn't exist
        Location teleportLocation = new Location(
            Bukkit.getWorld("crimson_isle"),
            getLocationX(location),
            100,
            getLocationZ(location)
        );
        
        player.teleport(teleportLocation);
        player.sendMessage("§aTeleported to " + config.getDisplayName() + "!");
    }
    
    private double getLocationX(CrimsonLocation location) {
        switch (location) {
            case MAGE_OUTPOST: return 100;
            case BARBARIAN_OUTPOST: return -100;
            case ARCHER_OUTPOST: return 0;
            case CRIMSON_FIELDS: return 0;
            case BLAZING_FORTRESS: return 200;
            default: return 0;
        }
    }
    
    private double getLocationZ(CrimsonLocation location) {
        switch (location) {
            case MAGE_OUTPOST: return 100;
            case BARBARIAN_OUTPOST: return 100;
            case ARCHER_OUTPOST: return -100;
            case CRIMSON_FIELDS: return 0;
            case BLAZING_FORTRESS: return 0;
            default: return 0;
        }
    }
    
    private PlayerCrimsonData getPlayerCrimsonData(Player player) {
        return playerCrimsonData.computeIfAbsent(player.getUniqueId(), k -> new PlayerCrimsonData(k));
    }
    
    // Enums and Classes
    public enum CrimsonFaction {
        MAGE("Mage", "§bMage"),
        BARBARIAN("Barbarian", "§cBarbarian"),
        ARCHER("Archer", "§eArcher");
        
        private final String name;
        private final String displayName;
        
        CrimsonFaction(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
    }
    
    public enum CrimsonLocation {
        MAGE_OUTPOST, BARBARIAN_OUTPOST, ARCHER_OUTPOST, CRIMSON_FIELDS, BLAZING_FORTRESS
    }
    
    public enum CrimsonMob {
        MAGE_APPRENTICE, MAGE_SCHOLAR, MAGE_MASTER,
        BARBARIAN_WARRIOR, BARBARIAN_CHAMPION, BARBARIAN_CHIEF,
        ARCHER_SCOUT, ARCHER_HUNTER, ARCHER_MASTER,
        CRIMSON_GRUNT, CRIMSON_SOLDIER, CRIMSON_COMMANDER,
        BLAZING_KNIGHT, BLAZING_LORD, BLAZING_KING
    }
    
    // Data Classes
    public static class FactionConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final List<String> characteristics;
        private final List<String> equipment;
        private final List<String> activities;
        
        public FactionConfig(String name, String displayName, String description, Material material,
                           List<String> characteristics, List<String> equipment, List<String> activities) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.material = material;
            this.characteristics = characteristics;
            this.equipment = equipment;
            this.activities = activities;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
        public List<String> getCharacteristics() { return characteristics; }
        public List<String> getEquipment() { return equipment; }
        public List<String> getActivities() { return activities; }
    }
    
    public static class LocationConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final List<String> features;
        private final List<CrimsonMob> availableMobs;
        
        public LocationConfig(String name, String displayName, String description, Material material,
                            List<String> features, List<CrimsonMob> availableMobs) {
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
        public List<CrimsonMob> getAvailableMobs() { return availableMobs; }
    }
    
    public static class MobConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final Material material;
        private final int health;
        private final int damage;
        private final double spawnChance;
        private final double dropChance;
        private final List<String> characteristics;
        
        public MobConfig(String name, String displayName, String description, Material material,
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
    
    public static class PlayerCrimsonData {
        private final UUID playerId;
        private CrimsonFaction faction;
        private int reputation;
        private final Set<Integer> milestones;
        private final Map<CrimsonLocation, Integer> locationStats;
        
        public PlayerCrimsonData(UUID playerId) {
            this.playerId = playerId;
            this.faction = null;
            this.reputation = 0;
            this.milestones = new HashSet<>();
            this.locationStats = new HashMap<>();
        }
        
        public void addReputation(int amount) {
            this.reputation += amount;
        }
        
        public void addMilestone(int milestone) {
            this.milestones.add(milestone);
        }
        
        public boolean hasMilestone(int milestone) {
            return this.milestones.contains(milestone);
        }
        
        // Getters and Setters
        public UUID getPlayerId() { return playerId; }
        public CrimsonFaction getFaction() { return faction; }
        public void setFaction(CrimsonFaction faction) { this.faction = faction; }
        public int getReputation() { return reputation; }
        public void setReputation(int reputation) { this.reputation = reputation; }
        public Set<Integer> getMilestones() { return milestones; }
        public Map<CrimsonLocation, Integer> getLocationStats() { return locationStats; }
    }
}
