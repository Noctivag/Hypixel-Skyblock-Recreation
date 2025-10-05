package de.noctivag.skyblock.skyblock;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
// import de.noctivag.skyblock.skills.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
// import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SkyBlock Location System - Vollständige Hypixel SkyBlock Location-Integration
 * 
 * Features:
 * - Alle Hypixel SkyBlock Locations
 * - Location-spezifische Requirements
 * - Location-spezifische Features
 * - Location-spezifische NPCs und Shops
 * - Location-spezifische Events
 * - Location Navigation System
 */
public class SkyBlockLocationSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, SkyBlockLocation> locations = new HashMap<>();
    private final Map<UUID, String> playerCurrentLocation = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> discoveredLocations = new ConcurrentHashMap<>();
    
    public SkyBlockLocationSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        initializeLocations();
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeLocations() {
        // Private Island - Starting location
        locations.put("private_island", new SkyBlockLocation(
            "private_island", "Private Island", "§a§lPrivate Island",
            "§7Your personal island where you can build and farm.",
            Material.GRASS_BLOCK, LocationType.PRIVATE,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.SKYBLOCK_LEVEL, 0, "§7No requirements")
            },
            new String[]{"farming", "mining", "building", "minions"},
            new LocationFeature[]{
                new LocationFeature("farming", "§aFarming", "§7Grow crops and raise animals"),
                new LocationFeature("mining", "§6Mining", "§7Mine resources from your island"),
                new LocationFeature("building", "§eBuilding", "§7Build and customize your island"),
                new LocationFeature("minions", "§bMinions", "§7Place and manage minions")
            }
        ));
        
        // The Hub - Central location
        locations.put("hub", new SkyBlockLocation(
            "hub", "The Hub", "§6§lThe Hub",
            "§7The central area with NPCs, shops, and portals to other islands.",
            Material.NETHER_STAR, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.SKYBLOCK_LEVEL, 0, "§7No requirements")
            },
            new String[]{"shops", "npcs", "portals", "auction_house", "bazaar"},
            new LocationFeature[]{
                new LocationFeature("shops", "§6Shops", "§7Buy and sell items"),
                new LocationFeature("npcs", "§bNPCs", "§7Interact with various NPCs"),
                new LocationFeature("portals", "§dPortals", "§7Travel to other locations"),
                new LocationFeature("auction_house", "§eAuction House", "§7Buy and sell items at auction"),
                new LocationFeature("bazaar", "§aBazaar", "§7Instant buy and sell marketplace")
            }
        ));
        
        // The Barn - Farming location
        locations.put("the_barn", new SkyBlockLocation(
            "the_barn", "The Barn", "§a§lThe Barn",
            "§7A farming area with crops and animals. Perfect for beginners!",
            Material.WHEAT, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.FARMING_LEVEL, 1, "§7Requires Farming Level 1")
            },
            new String[]{"farming", "animals", "crops", "farming_merchant"},
            new LocationFeature[]{
                new LocationFeature("farming", "§aFarming", "§7Grow wheat, carrot, and potato"),
                new LocationFeature("animals", "§6Animals", "§7Raise cows, pigs, and chickens"),
                new LocationFeature("crops", "§eCrops", "§7Harvest various crops"),
                new LocationFeature("farming_merchant", "§bFarming Merchant", "§7Buy farming tools and seeds")
            }
        ));
        
        // Mushroom Desert - Advanced farming
        locations.put("mushroom_desert", new SkyBlockLocation(
            "mushroom_desert", "Mushroom Desert", "§d§lMushroom Desert",
            "§7A desert area with mushrooms, cactus, and special crops.",
            Material.RED_MUSHROOM, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.FARMING_LEVEL, 5, "§7Requires Farming Level 5")
            },
            new String[]{"farming", "mushrooms", "cactus", "desert_merchant"},
            new LocationFeature[]{
                new LocationFeature("farming", "§aFarming", "§7Grow mushrooms and cactus"),
                new LocationFeature("mushrooms", "§dMushrooms", "§7Harvest red and brown mushrooms"),
                new LocationFeature("cactus", "§aCactus", "§7Grow and harvest cactus"),
                new LocationFeature("desert_merchant", "§6Desert Merchant", "§7Buy desert-specific items")
            }
        ));
        
        // The Park - Foraging location
        locations.put("the_park", new SkyBlockLocation(
            "the_park", "The Park", "§2§lThe Park",
            "§7A peaceful park with trees and fishing spots.",
            Material.OAK_LOG, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.FORAGING_LEVEL, 1, "§7Requires Foraging Level 1")
            },
            new String[]{"foraging", "fishing", "trees", "park_merchant"},
            new LocationFeature[]{
                new LocationFeature("foraging", "§2Foraging", "§7Chop oak, birch, and spruce trees"),
                new LocationFeature("fishing", "§bFishing", "§7Fish in the park's ponds"),
                new LocationFeature("trees", "§aTrees", "§7Various tree types to chop"),
                new LocationFeature("park_merchant", "§6Park Merchant", "§7Buy foraging tools and fishing gear")
            }
        ));
        
        // Spider's Den - Combat location
        locations.put("spiders_den", new SkyBlockLocation(
            "spiders_den", "Spider's Den", "§8§lSpider's Den",
            "§7A dark cave filled with spiders and other dangerous creatures.",
            Material.SPIDER_EYE, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.COMBAT_LEVEL, 1, "§7Requires Combat Level 1")
            },
            new String[]{"combat", "spiders", "cave_spiders", "tarantula_broodfather", "spider_merchant"},
            new LocationFeature[]{
                new LocationFeature("combat", "§cCombat", "§7Fight spiders and cave spiders"),
                new LocationFeature("spiders", "§8Spiders", "§7Regular spiders for combat XP"),
                new LocationFeature("cave_spiders", "§5Cave Spiders", "§7Poisonous cave spiders"),
                new LocationFeature("tarantula_broodfather", "§4Tarantula Broodfather", "§7Boss spider with special drops"),
                new LocationFeature("spider_merchant", "§6Spider Merchant", "§7Buy combat gear and potions")
            }
        ));
        
        // The End - Advanced combat
        locations.put("the_end", new SkyBlockLocation(
            "the_end", "The End", "§5§lThe End",
            "§7A mysterious dimension with endermen and the Ender Dragon.",
            Material.ENDER_PEARL, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.COMBAT_LEVEL, 12, "§7Requires Combat Level 12")
            },
            new String[]{"combat", "endermen", "ender_dragon", "end_city", "end_merchant"},
            new LocationFeature[]{
                new LocationFeature("combat", "§cCombat", "§7Fight endermen and other end creatures"),
                new LocationFeature("endermen", "§5Endermen", "§7Strong end creatures"),
                new LocationFeature("ender_dragon", "§4Ender Dragon", "§7The ultimate boss fight"),
                new LocationFeature("end_city", "§bEnd City", "§7Explore end cities for loot"),
                new LocationFeature("end_merchant", "§6End Merchant", "§7Buy end-specific items")
            }
        ));
        
        // Crimson Isle - High-level combat
        locations.put("crimson_isle", new SkyBlockLocation(
            "crimson_isle", "Crimson Isle", "§c§lCrimson Isle",
            "§7A dangerous volcanic island with powerful creatures and factions.",
            Material.CRIMSON_NYLIUM, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.COMBAT_LEVEL, 22, "§7Requires Combat Level 22")
            },
            new String[]{"combat", "crimson_mobs", "kuudra", "blaze_slayer", "crimson_merchant"},
            new LocationFeature[]{
                new LocationFeature("combat", "§cCombat", "§7Fight powerful crimson creatures"),
                new LocationFeature("crimson_mobs", "§cCrimson Mobs", "§7Various crimson-themed creatures"),
                new LocationFeature("kuudra", "§4Kuudra", "§7Boss fight with special mechanics"),
                new LocationFeature("blaze_slayer", "§6Blaze Slayer", "§7Slayer quest for blaze creatures"),
                new LocationFeature("crimson_merchant", "§6Crimson Merchant", "§7Buy high-tier combat gear")
            }
        ));
        
        // Gold Mine - Basic mining
        locations.put("gold_mine", new SkyBlockLocation(
            "gold_mine", "Gold Mine", "§6§lGold Mine",
            "§7A basic mining area with common ores and resources.",
            Material.GOLD_ORE, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.MINING_LEVEL, 1, "§7Requires Mining Level 1")
            },
            new String[]{"mining", "basic_ores", "gold_ore", "mine_merchant"},
            new LocationFeature[]{
                new LocationFeature("mining", "§6Mining", "§7Mine basic ores and resources"),
                new LocationFeature("basic_ores", "§7Basic Ores", "§7Coal, iron, and gold ores"),
                new LocationFeature("gold_ore", "§6Gold Ore", "§7Special gold ore deposits"),
                new LocationFeature("mine_merchant", "§6Mine Merchant", "§7Buy mining tools and equipment")
            }
        ));
        
        // Deep Caverns - Intermediate mining
        locations.put("deep_caverns", new SkyBlockLocation(
            "deep_caverns", "Deep Caverns", "§8§lDeep Caverns",
            "§7A deep underground mining area with various ore levels.",
            Material.DIAMOND_ORE, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.MINING_LEVEL, 5, "§7Requires Mining Level 5")
            },
            new String[]{"mining", "deep_ores", "diamond_ore", "cavern_merchant"},
            new LocationFeature[]{
                new LocationFeature("mining", "§6Mining", "§7Mine deeper and rarer ores"),
                new LocationFeature("deep_ores", "§8Deep Ores", "§7Diamond, emerald, and lapis ores"),
                new LocationFeature("diamond_ore", "§bDiamond Ore", "§7Valuable diamond deposits"),
                new LocationFeature("cavern_merchant", "§6Cavern Merchant", "§7Buy advanced mining equipment")
            }
        ));
        
        // Dwarven Mines - Advanced mining
        locations.put("dwarven_mines", new SkyBlockLocation(
            "dwarven_mines", "Dwarven Mines", "§7§lDwarven Mines",
            "§7An ancient dwarven mining complex with unique ores and mechanics.",
            Material.ANCIENT_DEBRIS, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.MINING_LEVEL, 12, "§7Requires Mining Level 12")
            },
            new String[]{"mining", "dwarven_ores", "mithril", "dwarven_merchant"},
            new LocationFeature[]{
                new LocationFeature("mining", "§6Mining", "§7Mine dwarven ores and materials"),
                new LocationFeature("dwarven_ores", "§7Dwarven Ores", "§7Mithril, titanium, and gemstones"),
                new LocationFeature("mithril", "§bMithril", "§7Rare mithril deposits"),
                new LocationFeature("dwarven_merchant", "§6Dwarven Merchant", "§7Buy dwarven equipment and tools")
            }
        ));
        
        // Crystal Hollows - Expert mining
        locations.put("crystal_hollows", new SkyBlockLocation(
            "crystal_hollows", "Crystal Hollows", "§d§lCrystal Hollows",
            "§7A crystal-filled underground realm with unique mining mechanics.",
            Material.AMETHYST_CLUSTER, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.MINING_LEVEL, 12, "§7Requires Mining Level 12"),
                new LocationRequirement(RequirementType.HEART_OF_THE_MOUNTAIN, 4, "§7Requires Heart of the Mountain Tier 4")
            },
            new String[]{"mining", "crystals", "gemstones", "crystal_merchant"},
            new LocationFeature[]{
                new LocationFeature("mining", "§6Mining", "§7Mine crystals and gemstones"),
                new LocationFeature("crystals", "§dCrystals", "§7Various crystal types"),
                new LocationFeature("gemstones", "§bGemstones", "§7Rare gemstone deposits"),
                new LocationFeature("crystal_merchant", "§6Crystal Merchant", "§7Buy crystal equipment")
            }
        ));
        
        // Jerry's Workshop - Special event
        locations.put("jerrys_workshop", new SkyBlockLocation(
            "jerrys_workshop", "Jerry's Workshop", "§f§lJerry's Workshop",
            "§7A special winter workshop with unique events and rewards.",
            Material.SNOW_BLOCK, LocationType.EVENT,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.SKYBLOCK_LEVEL, 5, "§7Requires SkyBlock Level 5")
            },
            new String[]{"events", "winter_items", "jerry", "workshop_merchant"},
            new LocationFeature[]{
                new LocationFeature("events", "§fEvents", "§7Special winter events"),
                new LocationFeature("winter_items", "§bWinter Items", "§7Unique winter-themed items"),
                new LocationFeature("jerry", "§eJerry", "§7Meet Jerry and participate in events"),
                new LocationFeature("workshop_merchant", "§6Workshop Merchant", "§7Buy winter items and decorations")
            }
        ));
        
        // Dungeon Hub - Dungeon access
        locations.put("dungeon_hub", new SkyBlockLocation(
            "dungeon_hub", "Dungeon Hub", "§4§lDungeon Hub",
            "§7The central hub for accessing dungeons and dungeon-related content.",
            Material.NETHER_STAR, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.SKYBLOCK_LEVEL, 8, "§7Requires SkyBlock Level 8")
            },
            new String[]{"dungeons", "catacombs", "dungeon_classes", "dungeon_merchant"},
            new LocationFeature[]{
                new LocationFeature("dungeons", "§4Dungeons", "§7Access to various dungeons"),
                new LocationFeature("catacombs", "§8Catacombs", "§7The main dungeon system"),
                new LocationFeature("dungeon_classes", "§6Dungeon Classes", "§7Choose your dungeon class"),
                new LocationFeature("dungeon_merchant", "§6Dungeon Merchant", "§7Buy dungeon equipment and items")
            }
        ));
        
        // Rift Dimension - Unique mechanics
        locations.put("rift_dimension", new SkyBlockLocation(
            "rift_dimension", "Rift Dimension", "§5§lRift Dimension",
            "§7A mysterious dimension with unique mechanics and quests.",
            Material.END_PORTAL_FRAME, LocationType.SPECIAL,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.SKYBLOCK_LEVEL, 12, "§7Requires SkyBlock Level 12")
            },
            new String[]{"rift_mechanics", "rift_quests", "rift_items", "rift_merchant"},
            new LocationFeature[]{
                new LocationFeature("rift_mechanics", "§5Rift Mechanics", "§7Unique dimension mechanics"),
                new LocationFeature("rift_quests", "§bRift Quests", "§7Special rift-themed quests"),
                new LocationFeature("rift_items", "§dRift Items", "§7Items unique to the rift"),
                new LocationFeature("rift_merchant", "§6Rift Merchant", "§7Buy rift-specific items")
            }
        ));
        
        // Backwater Bayou - Fishing location
        locations.put("backwater_bayou", new SkyBlockLocation(
            "backwater_bayou", "Backwater Bayou", "§2§lBackwater Bayou",
            "§7A swampy fishing area with unique fish and fishing mechanics.",
            Material.LILY_PAD, LocationType.PUBLIC,
            new LocationRequirement[]{
                new LocationRequirement(RequirementType.FISHING_LEVEL, 5, "§7Requires Fishing Level 5")
            },
            new String[]{"fishing", "swamp_fish", "fishing_merchant"},
            new LocationFeature[]{
                new LocationFeature("fishing", "§bFishing", "§7Fish in the swampy waters"),
                new LocationFeature("swamp_fish", "§2Swamp Fish", "§7Unique swamp fish species"),
                new LocationFeature("fishing_merchant", "§6Fishing Merchant", "§7Buy fishing gear and bait")
            }
        ));
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        
        if (to == null) return;
        
        // Check if player entered a new location
        for (SkyBlockLocation location : locations.values()) {
            if (location.isWithinBounds(to) && !location.getId().equals(playerCurrentLocation.get(player.getUniqueId()))) {
                enterLocation(player, location);
                break;
            }
        }
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        
        if (to == null) return;
        
        // Check if player teleported to a new location
        for (SkyBlockLocation location : locations.values()) {
            if (location.isWithinBounds(to) && !location.getId().equals(playerCurrentLocation.get(player.getUniqueId()))) {
                enterLocation(player, location);
                break;
            }
        }
    }
    
    private void enterLocation(Player player, SkyBlockLocation location) {
        // Check requirements
        if (!canAccessLocation(player, location)) {
            player.sendMessage("§cYou don't meet the requirements to enter " + location.getDisplayName() + "!");
            return;
        }
        
        // Update player location
        playerCurrentLocation.put(player.getUniqueId(), location.getId());
        
        // Discover location
        discoveredLocations.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>()).add(location.getId());
        
        // Send welcome message
        player.sendMessage("§aWelcome to " + location.getDisplayName() + "!");
        player.sendMessage(location.getDescription());
        
        // Show available features
        if (location.getFeatureDetails().length > 0) {
            player.sendMessage(Component.text("§7Available features:"));
            for (LocationFeature feature : location.getFeatureDetails()) {
                player.sendMessage("  " + feature.getDisplayName() + " §7- " + feature.getDescription());
            }
        }
    }
    
    public boolean canAccessLocation(Player player, SkyBlockLocation location) {
        for (LocationRequirement requirement : location.getRequirements()) {
            if (!requirement.isMet(player)) {
                return false;
            }
        }
        return true;
    }
    
    public SkyBlockLocation getLocation(String id) {
        return locations.get(id);
    }
    
    public SkyBlockLocation getPlayerLocation(UUID playerId) {
        String locationId = playerCurrentLocation.get(playerId);
        return locationId != null ? locations.get(locationId) : null;
    }
    
    public Set<String> getDiscoveredLocations(UUID playerId) {
        return discoveredLocations.getOrDefault(playerId, new HashSet<>());
    }
    
    public Collection<SkyBlockLocation> getAllLocations() {
        return locations.values();
    }
    
    public Collection<SkyBlockLocation> getAccessibleLocations(Player player) {
        return locations.values().stream()
                .filter(location -> canAccessLocation(player, location))
                .toList();
    }
    
    // Location Classes
    public static class SkyBlockLocation {
        private final String id;
        private final String name;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final LocationType type;
        private final LocationRequirement[] requirements;
        private final String[] features;
        private final LocationFeature[] featureDetails;
        private Location minBound;
        private Location maxBound;
        
        public SkyBlockLocation(String id, String name, String displayName, String description,
                              Material icon, LocationType type, LocationRequirement[] requirements,
                              String[] features, LocationFeature[] featureDetails) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.type = type;
            this.requirements = requirements;
            this.features = features;
            this.featureDetails = featureDetails;
        }
        
        public void setBounds(Location minBound, Location maxBound) {
            this.minBound = minBound;
            this.maxBound = maxBound;
        }
        
        public boolean isWithinBounds(Location location) {
            if (minBound == null || maxBound == null) return false;
            
            return location.getX() >= minBound.getX() && location.getX() <= maxBound.getX() &&
                   location.getY() >= minBound.getY() && location.getY() <= maxBound.getY() &&
                   location.getZ() >= minBound.getZ() && location.getZ() <= maxBound.getZ();
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public LocationType getType() { return type; }
        public LocationRequirement[] getRequirements() { return requirements; }
        public String[] getFeatures() { return features; }
        public LocationFeature[] getFeatureDetails() { return featureDetails; }
    }
    
    public static class LocationRequirement {
        private final RequirementType type;
        private final int level;
        private final String description;
        
        public LocationRequirement(RequirementType type, int level, String description) {
            this.type = type;
            this.level = level;
            this.description = description;
        }
        
        public boolean isMet(Player player) {
            // This would check the actual player stats
            // For now, return true as placeholder
            return true;
        }
        
        public RequirementType getType() { return type; }
        public int getLevel() { return level; }
        public String getDescription() { return description; }
    }
    
    public static class LocationFeature {
        private final String id;
        private final String displayName;
        private final String description;
        
        public LocationFeature(String id, String displayName, String description) {
            this.id = id;
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum LocationType {
        PRIVATE("§aPrivate", "§7Your personal area"),
        PUBLIC("§6Public", "§7Shared with other players"),
        EVENT("§eEvent", "§7Special event location"),
        SPECIAL("§dSpecial", "§7Unique mechanics");
        
        private final String displayName;
        private final String description;
        
        LocationType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum RequirementType {
        SKYBLOCK_LEVEL("§6SkyBlock Level", "§7Your overall SkyBlock level"),
        FARMING_LEVEL("§aFarming Level", "§7Your farming skill level"),
        MINING_LEVEL("§6Mining Level", "§7Your mining skill level"),
        COMBAT_LEVEL("§cCombat Level", "§7Your combat skill level"),
        FORAGING_LEVEL("§2Foraging Level", "§7Your foraging skill level"),
        FISHING_LEVEL("§bFishing Level", "§7Your fishing skill level"),
        HEART_OF_THE_MOUNTAIN("§bHeart of the Mountain", "§7Your Heart of the Mountain tier");
        
        private final String displayName;
        private final String description;
        
        RequirementType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
}
