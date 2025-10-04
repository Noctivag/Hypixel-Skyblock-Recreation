package de.noctivag.skyblock.features.events.types;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * All event types from Hypixel Skyblock
 */
public enum EventType {
    // Regular Events
    SPOOKY_FESTIVAL("Spooky Festival", "🎃", "Halloween-themed event with spooky mobs"),
    SEASON_OF_JERRY("Season of Jerry", "🎭", "Jerry-themed event with gift-giving"),
    NEW_YEAR("New Year", "🎊", "New Year celebration with fireworks"),
    MINING_FIESTA("Mining Fiesta", "⛏️", "Mining-focused event with bonus rewards"),
    FISHING_FESTIVAL("Fishing Festival", "🎣", "Fishing-focused event with rare creatures"),
    FARMING_CONTEST("Farming Contest", "🌾", "Farming competition with crop rewards"),
    
    // Special Events
    DIANA("Diana", "🌙", "Mythological event with Diana and creatures"),
    MARINA("Marina", "🌊", "Ocean-themed event with Marina and adventures"),
    DERPY("Derpy", "🤪", "Chaotic event with Derpy and random effects"),
    TECHNOBLADE("Technoblade", "🐷", "Special event honoring Technoblade"),
    
    // Seasonal Events
    SUMMER_FESTIVAL("Summer Festival", "☀️", "Summer celebration with beach activities"),
    WINTER_FESTIVAL("Winter Festival", "❄️", "Winter celebration with snow activities"),
    SPRING_FESTIVAL("Spring Festival", "🌸", "Spring celebration with flower activities"),
    AUTUMN_FESTIVAL("Autumn Festival", "🍂", "Autumn celebration with harvest activities"),
    
    // Community Events
    COMMUNITY_CHALLENGE("Community Challenge", "👥", "Community-wide challenge event"),
    DEVELOPER_EVENT("Developer Event", "👨‍💻", "Special event hosted by developers"),
    
    // Dungeon Events
    DUNGEON_RUSH("Dungeon Rush", "🏰", "Dungeon-focused event with bonus rewards"),
    BOSS_RUSH("Boss Rush", "👹", "Boss-fighting event with special rewards"),
    
    // Skill Events
    SKILL_BOOST("Skill Boost", "📈", "Event that boosts all skill XP gains"),
    COMBAT_TRAINING("Combat Training", "⚔️", "Combat-focused training event"),
    MINING_MARATHON("Mining Marathon", "⛏️", "Extended mining event"),
    FISHING_TOURNAMENT("Fishing Tournament", "🎣", "Fishing competition event"),
    
    // Economy Events
    BAZAAR_BONANZA("Bazaar Bonanza", "💰", "Special bazaar event with discounts"),
    AUCTION_FRENZY("Auction Frenzy", "🔨", "Auction house event with special items"),
    COIN_MULTIPLIER("Coin Multiplier", "💎", "Event that multiplies coin gains"),
    
    // Pet Events
    PET_CARE("Pet Care", "🐾", "Pet-focused event with bonus XP"),
    PET_SHOW("Pet Show", "🎪", "Pet competition event"),
    
    // Collection Events
    COLLECTION_BOOST("Collection Boost", "📚", "Event that boosts collection gains"),
    RARE_DROP("Rare Drop", "✨", "Event with increased rare drop rates");
    
    private final String displayName;
    private final String icon;
    private final String description;
    
    EventType(String displayName, String icon, String description) {
        this.displayName = displayName;
        this.icon = icon;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Get events by category
     */
    public static List<EventType> getRegularEvents() {
        return Arrays.asList(
            SPOOKY_FESTIVAL, SEASON_OF_JERRY, NEW_YEAR, MINING_FIESTA, 
            FISHING_FESTIVAL, FARMING_CONTEST
        );
    }
    
    /**
     * Get special events
     */
    public static List<EventType> getSpecialEvents() {
        return Arrays.asList(DIANA, MARINA, DERPY, TECHNOBLADE);
    }
    
    /**
     * Get seasonal events
     */
    public static List<EventType> getSeasonalEvents() {
        return Arrays.asList(
            SUMMER_FESTIVAL, WINTER_FESTIVAL, SPRING_FESTIVAL, AUTUMN_FESTIVAL
        );
    }
    
    /**
     * Get community events
     */
    public static List<EventType> getCommunityEvents() {
        return Arrays.asList(COMMUNITY_CHALLENGE, DEVELOPER_EVENT);
    }
    
    /**
     * Get dungeon events
     */
    public static List<EventType> getDungeonEvents() {
        return Arrays.asList(DUNGEON_RUSH, BOSS_RUSH);
    }
    
    /**
     * Get skill events
     */
    public static List<EventType> getSkillEvents() {
        return Arrays.asList(
            SKILL_BOOST, COMBAT_TRAINING, MINING_MARATHON, FISHING_TOURNAMENT
        );
    }
    
    /**
     * Get economy events
     */
    public static List<EventType> getEconomyEvents() {
        return Arrays.asList(BAZAAR_BONANZA, AUCTION_FRENZY, COIN_MULTIPLIER);
    }
    
    /**
     * Get pet events
     */
    public static List<EventType> getPetEvents() {
        return Arrays.asList(PET_CARE, PET_SHOW);
    }
    
    /**
     * Get collection events
     */
    public static List<EventType> getCollectionEvents() {
        return Arrays.asList(COLLECTION_BOOST, RARE_DROP);
    }
    
    /**
     * Get events by category
     */
    public static List<EventType> getByCategory(EventCategory category) {
        return switch (category) {
            case GENERAL -> getRegularEvents();
            case SPECIAL -> getSpecialEvents();
            case SOCIAL -> getCommunityEvents();
            case DUNGEON -> getDungeonEvents();
            case COMBAT -> getSkillEvents();
            case MINING -> getEconomyEvents();
            case TAMING -> getPetEvents();
            case ENCHANTING -> getCollectionEvents();
            default -> getRegularEvents();
        };
    }
    
    /**
     * Get events by rarity
     */
    public static List<EventType> getByRarity(EventRarity rarity) {
        return Arrays.stream(values())
            .filter(event -> {
                // Simple rarity mapping - would be more sophisticated in real implementation
                return switch (rarity) {
                    case COMMON -> getRegularEvents().contains(event) || getSeasonalEvents().contains(event);
                    case UNCOMMON -> getSkillEvents().contains(event) || getEconomyEvents().contains(event);
                    case RARE -> getPetEvents().contains(event) || getCollectionEvents().contains(event);
                    case EPIC -> getSpecialEvents().contains(event) || getCommunityEvents().contains(event);
                    case LEGENDARY -> getDungeonEvents().contains(event);
                    case MYTHIC -> getDungeonEvents().contains(event);
                    case SPECIAL -> getSpecialEvents().contains(event);
                };
            })
            .toList();
    }
    
    /**
     * Get event frequency
     */
    public EventFrequency getFrequency() {
        return switch (this) {
            case SPOOKY_FESTIVAL, SEASON_OF_JERRY, NEW_YEAR -> EventFrequency.WEEKLY;
            case MINING_FIESTA, FISHING_FESTIVAL, FARMING_CONTEST -> EventFrequency.MONTHLY;
            case DIANA, MARINA, DERPY, TECHNOBLADE -> EventFrequency.RARE;
            case SUMMER_FESTIVAL, WINTER_FESTIVAL, SPRING_FESTIVAL, AUTUMN_FESTIVAL -> EventFrequency.SEASONAL;
            case COMMUNITY_CHALLENGE, DEVELOPER_EVENT -> EventFrequency.SPECIAL;
            case DUNGEON_RUSH, BOSS_RUSH -> EventFrequency.MONTHLY;
            case SKILL_BOOST, COMBAT_TRAINING, MINING_MARATHON, FISHING_TOURNAMENT -> EventFrequency.WEEKLY;
            case BAZAAR_BONANZA, AUCTION_FRENZY, COIN_MULTIPLIER -> EventFrequency.BI_WEEKLY;
            case PET_CARE, PET_SHOW -> EventFrequency.MONTHLY;
            case COLLECTION_BOOST, RARE_DROP -> EventFrequency.WEEKLY;
        };
    }
    
    /**
     * Get event duration
     */
    public java.time.Duration getDuration() {
        return switch (this) {
            case SPOOKY_FESTIVAL, SEASON_OF_JERRY -> java.time.Duration.ofHours(3);
            case NEW_YEAR -> java.time.Duration.ofHours(2);
            case MINING_FIESTA -> java.time.Duration.ofHours(6);
            case FISHING_FESTIVAL -> java.time.Duration.ofHours(5);
            case FARMING_CONTEST -> java.time.Duration.ofHours(4);
            case DIANA, MARINA -> java.time.Duration.ofHours(8);
            case DERPY -> java.time.Duration.ofHours(12);
            case TECHNOBLADE -> java.time.Duration.ofHours(24);
            case SUMMER_FESTIVAL, WINTER_FESTIVAL, SPRING_FESTIVAL, AUTUMN_FESTIVAL -> java.time.Duration.ofHours(6);
            case COMMUNITY_CHALLENGE -> java.time.Duration.ofHours(48);
            case DEVELOPER_EVENT -> java.time.Duration.ofHours(24);
            case DUNGEON_RUSH, BOSS_RUSH -> java.time.Duration.ofHours(4);
            case SKILL_BOOST -> java.time.Duration.ofHours(12);
            case COMBAT_TRAINING, MINING_MARATHON, FISHING_TOURNAMENT -> java.time.Duration.ofHours(8);
            case BAZAAR_BONANZA, AUCTION_FRENZY, COIN_MULTIPLIER -> java.time.Duration.ofHours(6);
            case PET_CARE, PET_SHOW -> java.time.Duration.ofHours(4);
            case COLLECTION_BOOST, RARE_DROP -> java.time.Duration.ofHours(3);
        };
    }
    
    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
