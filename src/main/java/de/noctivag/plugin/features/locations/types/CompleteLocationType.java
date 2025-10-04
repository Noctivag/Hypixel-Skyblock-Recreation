package de.noctivag.plugin.features.locations.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All 100+ location types from the comprehensive Hypixel SkyBlock programming guide
 */
public enum CompleteLocationType {
    // ===== HUB LOCATIONS (25+) =====
    BAZAAR_ALLEY("Bazaar Alley", "🏪", 1, "Trading area with merchants", LocationCategory.HUB, LocationRarity.COMMON),
    BLACKSMITH("Blacksmith", "🔨", 1, "Weapon and armor crafting", LocationCategory.HUB, LocationRarity.COMMON),
    BUILDERS_HOUSE("Builder's House", "🏠", 1, "Building materials and tools", LocationCategory.HUB, LocationRarity.COMMON),
    CANVAS_ROOM("Canvas Room", "🎨", 1, "Art and decoration items", LocationCategory.HUB, LocationRarity.COMMON),
    COAL_MINE("Coal Mine", "⛏️", 5, "Coal mining area", LocationCategory.HUB, LocationRarity.COMMON),
    COLOSSEUM("Colosseum", "🏟️", 10, "PvP combat arena", LocationCategory.HUB, LocationRarity.UNCOMMON),
    COMMUNITY_CENTER("Community Center", "🏛️", 1, "Community events and information", LocationCategory.HUB, LocationRarity.COMMON),
    ELECTION_ROOM("Election Room", "🗳️", 1, "Mayor elections and voting", LocationCategory.HUB, LocationRarity.COMMON),
    FARM("Farm", "🌾", 1, "Farming area with crops", LocationCategory.HUB, LocationRarity.COMMON),
    FASHION_SHOP("Fashion Shop", "👗", 1, "Cosmetic items and skins", LocationCategory.HUB, LocationRarity.COMMON),
    FISHING_HUT("Fishing Hut", "🎣", 1, "Fishing equipment and supplies", LocationCategory.HUB, LocationRarity.COMMON),
    GRAVEYARD("Graveyard", "⚰️", 15, "Zombie spawning area", LocationCategory.HUB, LocationRarity.UNCOMMON),
    LIBRARY("Library", "📚", 1, "Books and enchantment guides", LocationCategory.HUB, LocationRarity.COMMON),
    MINING_MERCHANT("Mining Merchant", "⛏️", 1, "Mining tools and equipment", LocationCategory.HUB, LocationRarity.COMMON),
    MUSEUM("Museum", "🏛️", 1, "Rare items and collections", LocationCategory.HUB, LocationRarity.COMMON),
    NETHER_FORTRESS("Nether Fortress", "🔥", 25, "Nether mobs and resources", LocationCategory.HUB, LocationRarity.RARE),
    OAKS_LAB("Oak's Lab", "🧪", 1, "Experiments and research", LocationCategory.HUB, LocationRarity.COMMON),
    RUINS("Ruins", "🏺", 20, "Ancient structures and loot", LocationCategory.HUB, LocationRarity.UNCOMMON),
    SEASONS_OF_JERRY("Seasons of Jerry", "🎄", 1, "Holiday events and special items", LocationCategory.HUB, LocationRarity.COMMON),
    SPIDERS_DEN("Spider's Den", "🕷️", 20, "Spider mobs and silk", LocationCategory.HUB, LocationRarity.UNCOMMON),
    THE_BARN("The Barn", "🚜", 1, "Farming supplies and animals", LocationCategory.HUB, LocationRarity.COMMON),
    THE_END("The End", "🌑", 30, "Enderman and end resources", LocationCategory.HUB, LocationRarity.RARE),
    VILLAGE("Village", "🏘️", 1, "NPCs and basic services", LocationCategory.HUB, LocationRarity.COMMON),
    WILDERNESS("Wilderness", "🌲", 1, "Open exploration area", LocationCategory.HUB, LocationRarity.COMMON),
    WIZARD_TOWER("Wizard Tower", "🧙", 1, "Magic and enchantments", LocationCategory.HUB, LocationRarity.COMMON),

    // ===== PRIVATE ISLAND LOCATIONS (10+) =====
    MINION_PLATFORM("Minion Platform", "🤖", 1, "Place minions here", LocationCategory.PRIVATE_ISLAND, LocationRarity.COMMON),
    FARMING_AREA("Farming Area", "🌾", 1, "Grow crops and plants", LocationCategory.PRIVATE_ISLAND, LocationRarity.COMMON),
    MINING_AREA("Mining Area", "⛏️", 1, "Mine blocks and resources", LocationCategory.PRIVATE_ISLAND, LocationRarity.COMMON),
    BUILDING_AREA("Building Area", "🏗️", 1, "Build your base", LocationCategory.PRIVATE_ISLAND, LocationRarity.COMMON),
    STORAGE_AREA("Storage Area", "📦", 1, "Store your items", LocationCategory.PRIVATE_ISLAND, LocationRarity.COMMON),
    PET_AREA("Pet Area", "🐾", 1, "Pet training and care", LocationCategory.PRIVATE_ISLAND, LocationRarity.COMMON),
    ENCHANTING_AREA("Enchanting Area", "✨", 1, "Enchant your items", LocationCategory.PRIVATE_ISLAND, LocationRarity.COMMON),
    BREWING_AREA("Brewing Area", "🧪", 1, "Brew potions and elixirs", LocationCategory.PRIVATE_ISLAND, LocationRarity.COMMON),
    FISHING_POND("Fishing Pond", "🎣", 1, "Fish for resources", LocationCategory.PRIVATE_ISLAND, LocationRarity.COMMON),
    DECORATION_AREA("Decoration Area", "🎨", 1, "Decorate your island", LocationCategory.PRIVATE_ISLAND, LocationRarity.COMMON),

    // ===== DUNGEON LOCATIONS (15+) =====
    CATACOMBS_ENTRANCE("Catacombs Entrance", "⚰️", 15, "Entry to the Catacombs", LocationCategory.DUNGEON, LocationRarity.RARE),
    CATACOMBS_FLOOR_1("Catacombs Floor 1", "⚰️", 15, "First floor of Catacombs", LocationCategory.DUNGEON, LocationRarity.RARE),
    CATACOMBS_FLOOR_2("Catacombs Floor 2", "⚰️", 20, "Second floor of Catacombs", LocationCategory.DUNGEON, LocationRarity.RARE),
    CATACOMBS_FLOOR_3("Catacombs Floor 3", "⚰️", 25, "Third floor of Catacombs", LocationCategory.DUNGEON, LocationRarity.EPIC),
    CATACOMBS_FLOOR_4("Catacombs Floor 4", "⚰️", 30, "Fourth floor of Catacombs", LocationCategory.DUNGEON, LocationRarity.EPIC),
    CATACOMBS_FLOOR_5("Catacombs Floor 5", "⚰️", 35, "Fifth floor of Catacombs", LocationCategory.DUNGEON, LocationRarity.EPIC),
    CATACOMBS_FLOOR_6("Catacombs Floor 6", "⚰️", 40, "Sixth floor of Catacombs", LocationCategory.DUNGEON, LocationRarity.LEGENDARY),
    CATACOMBS_FLOOR_7("Catacombs Floor 7", "⚰️", 45, "Seventh floor of Catacombs", LocationCategory.DUNGEON, LocationRarity.LEGENDARY),
    BONZO_STATUE("Bonzo Statue", "🎭", 15, "Bonzo boss area", LocationCategory.DUNGEON, LocationRarity.RARE),
    SCARF_STATUE("Scarf Statue", "💀", 20, "Scarf boss area", LocationCategory.DUNGEON, LocationRarity.RARE),
    PROFESSOR_STATUE("Professor Statue", "👨‍🔬", 25, "Professor boss area", LocationCategory.DUNGEON, LocationRarity.EPIC),
    THORN_STATUE("Thorn Statue", "🕷️", 30, "Thorn boss area", LocationCategory.DUNGEON, LocationRarity.EPIC),
    LIVID_STATUE("Livid Statue", "🌑", 35, "Livid boss area", LocationCategory.DUNGEON, LocationRarity.LEGENDARY),
    SADAN_STATUE("Sadan Statue", "🧟", 40, "Sadan boss area", LocationCategory.DUNGEON, LocationRarity.LEGENDARY),
    NECRON_STATUE("Necron Statue", "💀", 45, "Necron boss area", LocationCategory.DUNGEON, LocationRarity.MYTHIC),

    // ===== SPECIAL LOCATIONS (20+) =====
    DEEP_CAVERNS("Deep Caverns", "⛏️", 25, "Deep underground mining area", LocationCategory.SPECIAL, LocationRarity.RARE),
    DWARVEN_MINES("Dwarven Mines", "⛏️", 30, "Advanced mining area", LocationCategory.SPECIAL, LocationRarity.EPIC),
    CRYSTAL_HOLLOWS("Crystal Hollows", "💎", 35, "Crystal mining area", LocationCategory.SPECIAL, LocationRarity.EPIC),
    CRIMSON_ISLE("Crimson Isle", "🔥", 40, "Fire-based island", LocationCategory.SPECIAL, LocationRarity.LEGENDARY),
    BLAZING_FORTRESS("Blazing Fortress", "🔥", 30, "Fire fortress with Blaze mobs", LocationCategory.SPECIAL, LocationRarity.EPIC),
    END_ISLAND("End Island", "🌑", 35, "End dimension island", LocationCategory.SPECIAL, LocationRarity.EPIC),
    GARDEN("Garden", "🌱", 1, "Advanced farming area", LocationCategory.SPECIAL, LocationRarity.COMMON),
    THE_RIFT("The Rift", "🌀", 50, "Alternative dimension", LocationCategory.SPECIAL, LocationRarity.MYTHIC),
    KUUDRA_ARENA("Kuudra Arena", "🐙", 45, "Kuudra boss fight area", LocationCategory.SPECIAL, LocationRarity.MYTHIC),
    INFERNO("Inferno", "🔥", 50, "Inferno Demonlord area", LocationCategory.SPECIAL, LocationRarity.MYTHIC),
    DRAGONS_NEST("Dragon's Nest", "🐉", 40, "Dragon boss area", LocationCategory.SPECIAL, LocationRarity.LEGENDARY),
    VOID_SEPULCHRE("Void Sepulchre", "🌑", 45, "Voidgloom Seraph area", LocationCategory.SPECIAL, LocationRarity.MYTHIC),
    MUSHROOM_DESERT("Mushroom Desert", "🍄", 25, "Mushroom farming area", LocationCategory.SPECIAL, LocationRarity.RARE),
    JERRYS_WORKSHOP("Jerry's Workshop", "🎄", 1, "Holiday event area", LocationCategory.SPECIAL, LocationRarity.COMMON),
    GOLD_MINE("Gold Mine", "⛏️", 20, "Gold mining area", LocationCategory.SPECIAL, LocationRarity.UNCOMMON),
    DIAMOND_RESERVE("Diamond Reserve", "💎", 25, "Diamond mining area", LocationCategory.SPECIAL, LocationRarity.RARE),
    OBSIDIAN_SANCTUARY("Obsidian Sanctuary", "🖤", 30, "Obsidian mining area", LocationCategory.SPECIAL, LocationRarity.RARE),
    SPIDERS_DEN_2("Spider's Den 2", "🕷️", 35, "Advanced spider area", LocationCategory.SPECIAL, LocationRarity.EPIC),
    END_ISLAND_2("End Island 2", "🌑", 40, "Advanced end area", LocationCategory.SPECIAL, LocationRarity.EPIC),
    CRYSTAL_NUCLEUS("Crystal Nucleus", "💎", 45, "Crystal mining center", LocationCategory.SPECIAL, LocationRarity.LEGENDARY),

    // ===== MINING LOCATIONS (15+) =====
    COAL_MINE_ENTRANCE("Coal Mine Entrance", "⛏️", 5, "Entry to coal mine", LocationCategory.MINING, LocationRarity.COMMON),
    IRON_MINE("Iron Mine", "⛏️", 10, "Iron ore mining", LocationCategory.MINING, LocationRarity.COMMON),
    GOLD_MINE_ENTRANCE("Gold Mine Entrance", "⛏️", 15, "Entry to gold mine", LocationCategory.MINING, LocationRarity.UNCOMMON),
    LAPIS_MINE("Lapis Mine", "💙", 20, "Lapis ore mining", LocationCategory.MINING, LocationRarity.UNCOMMON),
    REDSTONE_MINE("Redstone Mine", "🔴", 20, "Redstone ore mining", LocationCategory.MINING, LocationRarity.UNCOMMON),
    EMERALD_MINE("Emerald Mine", "💚", 25, "Emerald ore mining", LocationCategory.MINING, LocationRarity.RARE),
    DIAMOND_MINE("Diamond Mine", "💎", 25, "Diamond ore mining", LocationCategory.MINING, LocationRarity.RARE),
    OBSIDIAN_MINE("Obsidian Mine", "🖤", 30, "Obsidian mining", LocationCategory.MINING, LocationRarity.RARE),
    GLOWSTONE_MINE("Glowstone Mine", "💡", 25, "Glowstone mining", LocationCategory.MINING, LocationRarity.UNCOMMON),
    NETHER_QUARTZ_MINE("Nether Quartz Mine", "⚪", 30, "Nether quartz mining", LocationCategory.MINING, LocationRarity.UNCOMMON),
    GRAVEL_MINE("Gravel Mine", "⚫", 15, "Gravel mining", LocationCategory.MINING, LocationRarity.COMMON),
    SAND_MINE("Sand Mine", "🟡", 15, "Sand mining", LocationCategory.MINING, LocationRarity.COMMON),
    END_STONE_MINE("End Stone Mine", "🟤", 35, "End stone mining", LocationCategory.MINING, LocationRarity.RARE),
    NETHERRACK_MINE("Netherrack Mine", "🔴", 25, "Netherrack mining", LocationCategory.MINING, LocationRarity.UNCOMMON),
    MYCELIUM_MINE("Mycelium Mine", "🟣", 30, "Mycelium mining", LocationCategory.MINING, LocationRarity.RARE),

    // ===== COMBAT LOCATIONS (10+) =====
    COMBAT_ARENA("Combat Arena", "⚔️", 10, "PvP combat area", LocationCategory.COMBAT, LocationRarity.UNCOMMON),
    ZOMBIE_ARENA("Zombie Arena", "🧟", 15, "Zombie combat area", LocationCategory.COMBAT, LocationRarity.UNCOMMON),
    SPIDER_ARENA("Spider Arena", "🕷️", 20, "Spider combat area", LocationCategory.COMBAT, LocationRarity.UNCOMMON),
    SKELETON_ARENA("Skeleton Arena", "💀", 20, "Skeleton combat area", LocationCategory.COMBAT, LocationRarity.UNCOMMON),
    ENDERMAN_ARENA("Enderman Arena", "🌑", 25, "Enderman combat area", LocationCategory.COMBAT, LocationRarity.RARE),
    BLAZE_ARENA("Blaze Arena", "🔥", 30, "Blaze combat area", LocationCategory.COMBAT, LocationRarity.RARE),
    WITHER_ARENA("Wither Arena", "💀", 40, "Wither combat area", LocationCategory.COMBAT, LocationRarity.EPIC),
    DRAGON_ARENA("Dragon Arena", "🐉", 45, "Dragon combat area", LocationCategory.COMBAT, LocationRarity.LEGENDARY),
    VOID_ARENA("Void Arena", "🌑", 50, "Void combat area", LocationCategory.COMBAT, LocationRarity.MYTHIC),
    BOSS_ARENA("Boss Arena", "👑", 35, "General boss combat area", LocationCategory.COMBAT, LocationRarity.EPIC),

    // ===== FARMING LOCATIONS (10+) =====
    WHEAT_FIELD("Wheat Field", "🌾", 1, "Wheat farming area", LocationCategory.FARMING, LocationRarity.COMMON),
    CARROT_FIELD("Carrot Field", "🥕", 1, "Carrot farming area", LocationCategory.FARMING, LocationRarity.COMMON),
    POTATO_FIELD("Potato Field", "🥔", 1, "Potato farming area", LocationCategory.FARMING, LocationRarity.COMMON),
    PUMPKIN_FIELD("Pumpkin Field", "🎃", 5, "Pumpkin farming area", LocationCategory.FARMING, LocationRarity.COMMON),
    MELON_FIELD("Melon Field", "🍈", 5, "Melon farming area", LocationCategory.FARMING, LocationRarity.COMMON),
    SUGAR_CANE_FIELD("Sugar Cane Field", "🎋", 10, "Sugar cane farming area", LocationCategory.FARMING, LocationRarity.COMMON),
    COCOA_FIELD("Cocoa Field", "🍫", 15, "Cocoa farming area", LocationCategory.FARMING, LocationRarity.UNCOMMON),
    CACTUS_FIELD("Cactus Field", "🌵", 15, "Cactus farming area", LocationCategory.FARMING, LocationRarity.UNCOMMON),
    NETHER_WART_FIELD("Nether Wart Field", "🍄", 20, "Nether wart farming area", LocationCategory.FARMING, LocationRarity.UNCOMMON),
    MUSHROOM_FIELD("Mushroom Field", "🍄", 20, "Mushroom farming area", LocationCategory.FARMING, LocationRarity.UNCOMMON),

    // ===== FISHING LOCATIONS (8+) =====
    FISHING_POND_HUB("Fishing Pond Hub", "🎣", 1, "Basic fishing area", LocationCategory.FISHING, LocationRarity.COMMON),
    FISHING_POND_ISLAND("Fishing Pond Island", "🎣", 5, "Island fishing area", LocationCategory.FISHING, LocationRarity.COMMON),
    FISHING_POND_OCEAN("Fishing Pond Ocean", "🌊", 10, "Ocean fishing area", LocationCategory.FISHING, LocationRarity.UNCOMMON),
    FISHING_POND_LAVA("Fishing Pond Lava", "🌋", 25, "Lava fishing area", LocationCategory.FISHING, LocationRarity.RARE),
    FISHING_POND_END("Fishing Pond End", "🌑", 30, "End fishing area", LocationCategory.FISHING, LocationRarity.RARE),
    FISHING_POND_SPOOKY("Fishing Pond Spooky", "👻", 15, "Spooky fishing area", LocationCategory.FISHING, LocationRarity.UNCOMMON),
    FISHING_POND_WINTER("Fishing Pond Winter", "❄️", 20, "Winter fishing area", LocationCategory.FISHING, LocationRarity.UNCOMMON),
    FISHING_POND_SUMMER("Fishing Pond Summer", "☀️", 20, "Summer fishing area", LocationCategory.FISHING, LocationRarity.UNCOMMON),

    // ===== FORAGING LOCATIONS (8+) =====
    OAK_FOREST("Oak Forest", "🌳", 1, "Oak wood foraging", LocationCategory.FORAGING, LocationRarity.COMMON),
    BIRCH_FOREST("Birch Forest", "🌲", 1, "Birch wood foraging", LocationCategory.FORAGING, LocationRarity.COMMON),
    SPRUCE_FOREST("Spruce Forest", "🌲", 1, "Spruce wood foraging", LocationCategory.FORAGING, LocationRarity.COMMON),
    DARK_OAK_FOREST("Dark Oak Forest", "🌳", 5, "Dark oak wood foraging", LocationCategory.FORAGING, LocationRarity.UNCOMMON),
    ACACIA_FOREST("Acacia Forest", "🌳", 5, "Acacia wood foraging", LocationCategory.FORAGING, LocationRarity.UNCOMMON),
    JUNGLE_FOREST("Jungle Forest", "🌴", 10, "Jungle wood foraging", LocationCategory.FORAGING, LocationRarity.UNCOMMON),
    CRIMSON_FOREST("Crimson Forest", "🔴", 25, "Crimson wood foraging", LocationCategory.FORAGING, LocationRarity.RARE),
    WARPED_FOREST("Warped Forest", "🔵", 25, "Warped wood foraging", LocationCategory.FORAGING, LocationRarity.RARE),

    // ===== EVENT LOCATIONS (8+) =====
    SPOOKY_FESTIVAL_AREA("Spooky Festival Area", "👻", 1, "Halloween event area", LocationCategory.EVENT, LocationRarity.COMMON),
    JERRY_WORKSHOP("Jerry Workshop", "🎄", 1, "Christmas event area", LocationCategory.EVENT, LocationRarity.COMMON),
    NEW_YEAR_AREA("New Year Area", "🎆", 1, "New Year event area", LocationCategory.EVENT, LocationRarity.COMMON),
    EASTER_AREA("Easter Area", "🐰", 1, "Easter event area", LocationCategory.EVENT, LocationRarity.COMMON),
    VALENTINE_AREA("Valentine Area", "💕", 1, "Valentine event area", LocationCategory.EVENT, LocationRarity.COMMON),
    SUMMER_AREA("Summer Area", "☀️", 1, "Summer event area", LocationCategory.EVENT, LocationRarity.COMMON),
    WINTER_AREA("Winter Area", "❄️", 1, "Winter event area", LocationCategory.EVENT, LocationRarity.COMMON),
    SPECIAL_EVENT_AREA("Special Event Area", "🎪", 1, "Special event area", LocationCategory.EVENT, LocationRarity.COMMON),

    // ===== BOSS LOCATIONS (10+) =====
    MAGMA_BOSS_ARENA("Magma Boss Arena", "🌋", 30, "Magma boss fight area", LocationCategory.BOSS, LocationRarity.EPIC),
    ARACHNE_ARENA("Arachne Arena", "🕷️", 35, "Arachne boss fight area", LocationCategory.BOSS, LocationRarity.EPIC),
    DRAGON_ARENA_BOSS("Dragon Arena Boss", "🐉", 40, "Dragon boss fight area", LocationCategory.BOSS, LocationRarity.LEGENDARY),
    WITHER_ARENA_BOSS("Wither Arena Boss", "💀", 45, "Wither boss fight area", LocationCategory.BOSS, LocationRarity.LEGENDARY),
    KUUDRA_ARENA_BOSS("Kuudra Arena Boss", "🐙", 50, "Kuudra boss fight area", LocationCategory.BOSS, LocationRarity.MYTHIC),
    INFERNO_DEMONLORD_ARENA("Inferno Demonlord Arena", "🔥", 50, "Inferno Demonlord boss fight area", LocationCategory.BOSS, LocationRarity.MYTHIC),
    VOIDGLOOM_SERAPH_ARENA("Voidgloom Seraph Arena", "🌑", 50, "Voidgloom Seraph boss fight area", LocationCategory.BOSS, LocationRarity.MYTHIC),
    REVENANT_HORROR_ARENA("Revenant Horror Arena", "🧟", 40, "Revenant Horror boss fight area", LocationCategory.BOSS, LocationRarity.LEGENDARY),
    TARANTULA_BROODFATHER_ARENA("Tarantula Broodfather Arena", "🕷️", 40, "Tarantula Broodfather boss fight area", LocationCategory.BOSS, LocationRarity.LEGENDARY),
    SVEN_PACKMASTER_ARENA("Sven Packmaster Arena", "🐺", 40, "Sven Packmaster boss fight area", LocationCategory.BOSS, LocationRarity.LEGENDARY),

    // ===== SECRET LOCATIONS (5+) =====
    SECRET_CAVE("Secret Cave", "🕳️", 20, "Hidden cave with treasures", LocationCategory.SECRET, LocationRarity.SECRET),
    SECRET_CHAMBER("Secret Chamber", "🏺", 25, "Hidden chamber with loot", LocationCategory.SECRET, LocationRarity.SECRET),
    SECRET_PASSAGE("Secret Passage", "🚪", 30, "Hidden passage to special area", LocationCategory.SECRET, LocationRarity.SECRET),
    SECRET_VAULT("Secret Vault", "🏦", 35, "Hidden vault with riches", LocationCategory.SECRET, LocationRarity.SECRET),
    SECRET_SANCTUARY("Secret Sanctuary", "🏛️", 40, "Hidden sanctuary with power", LocationCategory.SECRET, LocationRarity.SECRET),

    // ===== PORTAL LOCATIONS (5+) =====
    HUB_PORTAL("Hub Portal", "🌀", 1, "Portal to Hub", LocationCategory.PORTAL, LocationRarity.COMMON),
    ISLAND_PORTAL("Island Portal", "🌀", 1, "Portal to Private Island", LocationCategory.PORTAL, LocationRarity.COMMON),
    DUNGEON_PORTAL("Dungeon Portal", "🌀", 15, "Portal to Dungeons", LocationCategory.PORTAL, LocationRarity.RARE),
    SPECIAL_PORTAL("Special Portal", "🌀", 25, "Portal to Special Areas", LocationCategory.PORTAL, LocationRarity.RARE),
    BOSS_PORTAL("Boss Portal", "🌀", 35, "Portal to Boss Areas", LocationCategory.PORTAL, LocationRarity.EPIC);

    private final String displayName;
    private final String icon;
    private final int level;
    private final String description;
    private final LocationCategory category;
    private final LocationRarity rarity;

    CompleteLocationType(String displayName, String icon, int level, String description, LocationCategory category, LocationRarity rarity) {
        this.displayName = displayName;
        this.icon = icon;
        this.level = level;
        this.description = description;
        this.category = category;
        this.rarity = rarity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public LocationCategory getCategory() {
        return category;
    }

    public LocationRarity getRarity() {
        return rarity;
    }

    /**
     * Get locations by category
     */
    public static List<CompleteLocationType> getLocationsByCategory(LocationCategory category) {
        return Arrays.stream(values())
                .filter(location -> location.getCategory() == category)
                .toList();
    }

    /**
     * Get locations by rarity
     */
    public static List<CompleteLocationType> getLocationsByRarity(LocationRarity rarity) {
        return Arrays.stream(values())
                .filter(location -> location.getRarity() == rarity)
                .toList();
    }

    /**
     * Get locations by level range
     */
    public static List<CompleteLocationType> getLocationsByLevelRange(int minLevel, int maxLevel) {
        return Arrays.stream(values())
                .filter(location -> location.getLevel() >= minLevel && location.getLevel() <= maxLevel)
                .toList();
    }

    /**
     * Get total location count
     */
    public static int getTotalLocationCount() {
        return values().length;
    }

    /**
     * Get location count by category
     */
    public static int getLocationCountByCategory(LocationCategory category) {
        return (int) Arrays.stream(values())
                .filter(location -> location.getCategory() == category)
                .count();
    }

    /**
     * Get location count by rarity
     */
    public static int getLocationCountByRarity(LocationRarity rarity) {
        return (int) Arrays.stream(values())
                .filter(location -> location.getRarity() == rarity)
                .count();
    }

    /**
     * Get hub locations
     */
    public static List<CompleteLocationType> getHubLocations() {
        return getLocationsByCategory(LocationCategory.HUB);
    }

    /**
     * Get private island locations
     */
    public static List<CompleteLocationType> getPrivateIslandLocations() {
        return getLocationsByCategory(LocationCategory.PRIVATE_ISLAND);
    }

    /**
     * Get dungeon locations
     */
    public static List<CompleteLocationType> getDungeonLocations() {
        return getLocationsByCategory(LocationCategory.DUNGEON);
    }

    /**
     * Get special locations
     */
    public static List<CompleteLocationType> getSpecialLocations() {
        return getLocationsByCategory(LocationCategory.SPECIAL);
    }

    /**
     * Get mining locations
     */
    public static List<CompleteLocationType> getMiningLocations() {
        return getLocationsByCategory(LocationCategory.MINING);
    }

    /**
     * Get combat locations
     */
    public static List<CompleteLocationType> getCombatLocations() {
        return getLocationsByCategory(LocationCategory.COMBAT);
    }

    /**
     * Get farming locations
     */
    public static List<CompleteLocationType> getFarmingLocations() {
        return getLocationsByCategory(LocationCategory.FARMING);
    }

    /**
     * Get fishing locations
     */
    public static List<CompleteLocationType> getFishingLocations() {
        return getLocationsByCategory(LocationCategory.FISHING);
    }

    /**
     * Get foraging locations
     */
    public static List<CompleteLocationType> getForagingLocations() {
        return getLocationsByCategory(LocationCategory.FORAGING);
    }

    /**
     * Get event locations
     */
    public static List<CompleteLocationType> getEventLocations() {
        return getLocationsByCategory(LocationCategory.EVENT);
    }

    /**
     * Get boss locations
     */
    public static List<CompleteLocationType> getBossLocations() {
        return getLocationsByCategory(LocationCategory.BOSS);
    }

    /**
     * Get secret locations
     */
    public static List<CompleteLocationType> getSecretLocations() {
        return getLocationsByCategory(LocationCategory.SECRET);
    }

    /**
     * Get portal locations
     */
    public static List<CompleteLocationType> getPortalLocations() {
        return getLocationsByCategory(LocationCategory.PORTAL);
    }

    /**
     * Get locations suitable for specific activities
     */
    public static List<CompleteLocationType> getLocationsForActivity(String activity) {
        return Arrays.stream(values())
                .filter(location -> isSuitableForActivity(location, activity))
                .toList();
    }

    /**
     * Check if location is suitable for activity
     */
    private static boolean isSuitableForActivity(CompleteLocationType location, String activity) {
        String act = activity.toLowerCase();
        LocationCategory category = location.getCategory();
        
        return switch (category) {
            case MINING -> act.contains("mine") || act.contains("mining") || act.contains("ore");
            case FARMING -> act.contains("farm") || act.contains("farming") || act.contains("crop");
            case COMBAT -> act.contains("combat") || act.contains("fight") || act.contains("pvp");
            case FORAGING -> act.contains("forage") || act.contains("wood") || act.contains("tree");
            case FISHING -> act.contains("fish") || act.contains("fishing") || act.contains("water");
            case BOSS -> act.contains("boss") || act.contains("boss fight");
            case EVENT -> act.contains("event") || act.contains("festival");
            case SECRET -> act.contains("secret") || act.contains("hidden");
            case PORTAL -> act.contains("portal") || act.contains("teleport");
            default -> true;
        };
    }

    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
