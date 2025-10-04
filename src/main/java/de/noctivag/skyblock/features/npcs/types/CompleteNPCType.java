package de.noctivag.skyblock.features.npcs.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All 100+ NPC types from the comprehensive Hypixel SkyBlock programming guide
 */
public enum CompleteNPCType {
    // ===== HUB NPCs (25+) =====
    AUCTION_MASTER_HUB("Auction Master", "🏪", 1, "Manages the auction house", NPCCategory.HUB, NPCRarity.COMMON),
    BAZAAR("Bazaar", "🏪", 1, "Trading marketplace", NPCCategory.HUB, NPCRarity.COMMON),
    BANKER("Banker", "🏦", 1, "Banking services", NPCCategory.HUB, NPCRarity.COMMON),
    BLACKSMITH("Blacksmith", "🔨", 1, "Weapon and armor crafting", NPCCategory.HUB, NPCRarity.COMMON),
    BUILDER("Builder", "🏗️", 1, "Building materials and tools", NPCCategory.HUB, NPCRarity.COMMON),
    FARMER("Farmer", "🌾", 1, "Farming supplies and seeds", NPCCategory.HUB, NPCRarity.COMMON),
    FISHERMAN("Fisherman", "🎣", 1, "Fishing equipment and bait", NPCCategory.HUB, NPCRarity.COMMON),
    LIBRARIAN("Librarian", "📚", 1, "Books and enchantment guides", NPCCategory.HUB, NPCRarity.COMMON),
    MINER("Miner", "⛏️", 1, "Mining tools and equipment", NPCCategory.HUB, NPCRarity.COMMON),
    MERCHANT("Merchant", "🛒", 1, "General goods and supplies", NPCCategory.HUB, NPCRarity.COMMON),
    VILLAGER("Villager", "👤", 1, "Basic villager services", NPCCategory.HUB, NPCRarity.COMMON),
    PRIEST("Priest", "⛪", 1, "Spiritual services and blessings", NPCCategory.HUB, NPCRarity.COMMON),
    GUARD("Guard", "🛡️", 1, "Security and protection", NPCCategory.HUB, NPCRarity.COMMON),
    MAYOR("Mayor", "👑", 1, "City administration", NPCCategory.HUB, NPCRarity.UNCOMMON),
    JERRY("Jerry", "🧀", 1, "Special event coordinator", NPCCategory.HUB, NPCRarity.SPECIAL),
    OAK("Oak", "🧪", 1, "Experiments and research", NPCCategory.HUB, NPCRarity.COMMON),
    DANTE("Dante", "🎭", 1, "Mysterious figure", NPCCategory.HUB, NPCRarity.LEGENDARY),
    MARINA("Marina", "🎣", 1, "Fishing contest organizer", NPCCategory.HUB, NPCRarity.EPIC),
    DIANA("Diana", "🏹", 1, "Hunt organizer", NPCCategory.HUB, NPCRarity.EPIC),
    PAUL("Paul", "💎", 1, "Dungeon master", NPCCategory.HUB, NPCRarity.EPIC),
    DERRY("Derry", "🤪", 1, "Special event coordinator", NPCCategory.HUB, NPCRarity.SPECIAL),
    TECHNOCLADE("Technoblade", "👑", 1, "Legendary warrior", NPCCategory.HUB, NPCRarity.MYTHIC),
    SCORPIOUS("Scorpious", "🦂", 1, "Dark auctioneer", NPCCategory.HUB, NPCRarity.LEGENDARY),
    SIRIUS("Sirius", "⭐", 1, "Stellar merchant", NPCCategory.HUB, NPCRarity.LEGENDARY),
    MAXWELL("Maxwell", "🧙", 1, "Wizard and scholar", NPCCategory.HUB, NPCRarity.EPIC),

    // ===== SHOP NPCs (15+) =====
    WEAPON_SHOP("Weapon Shop", "⚔️", 1, "Sells weapons and combat items", NPCCategory.SHOP, NPCRarity.COMMON),
    ARMOR_SHOP("Armor Shop", "🛡️", 1, "Sells armor and protection items", NPCCategory.SHOP, NPCRarity.COMMON),
    TOOL_SHOP("Tool Shop", "🔧", 1, "Sells tools and equipment", NPCCategory.SHOP, NPCRarity.COMMON),
    FOOD_SHOP("Food Shop", "🍎", 1, "Sells food and consumables", NPCCategory.SHOP, NPCRarity.COMMON),
    POTION_SHOP("Potion Shop", "🧪", 1, "Sells potions and elixirs", NPCCategory.SHOP, NPCRarity.UNCOMMON),
    ENCHANT_SHOP("Enchant Shop", "✨", 1, "Sells enchantment books", NPCCategory.SHOP, NPCRarity.UNCOMMON),
    MATERIAL_SHOP("Material Shop", "📦", 1, "Sells building materials", NPCCategory.SHOP, NPCRarity.COMMON),
    SEED_SHOP("Seed Shop", "🌱", 1, "Sells seeds and farming supplies", NPCCategory.SHOP, NPCRarity.COMMON),
    FISHING_SHOP("Fishing Shop", "🎣", 1, "Sells fishing equipment", NPCCategory.SHOP, NPCRarity.COMMON),
    MINING_SHOP("Mining Shop", "⛏️", 1, "Sells mining equipment", NPCCategory.SHOP, NPCRarity.COMMON),
    SPECIAL_SHOP("Special Shop", "💎", 1, "Sells rare and special items", NPCCategory.SHOP, NPCRarity.RARE),
    COSMETIC_SHOP("Cosmetic Shop", "🎨", 1, "Sells cosmetic items", NPCCategory.SHOP, NPCRarity.UNCOMMON),
    PET_SHOP("Pet Shop", "🐾", 1, "Sells pets and pet supplies", NPCCategory.SHOP, NPCRarity.UNCOMMON),
    ACCESSORY_SHOP("Accessory Shop", "💍", 1, "Sells accessories and talismans", NPCCategory.SHOP, NPCRarity.RARE),
    RARE_SHOP("Rare Shop", "🌟", 1, "Sells extremely rare items", NPCCategory.SHOP, NPCRarity.EPIC),

    // ===== QUEST NPCs (15+) =====
    QUEST_GIVER("Quest Giver", "📜", 1, "Gives various quests", NPCCategory.QUEST, NPCRarity.COMMON),
    DAILY_QUEST("Daily Quest", "📅", 1, "Gives daily quests", NPCCategory.QUEST, NPCRarity.COMMON),
    WEEKLY_QUEST("Weekly Quest", "📆", 1, "Gives weekly quests", NPCCategory.QUEST, NPCRarity.UNCOMMON),
    SPECIAL_QUEST("Special Quest", "⭐", 1, "Gives special event quests", NPCCategory.QUEST, NPCRarity.RARE),
    ACHIEVEMENT_QUEST("Achievement Quest", "🏆", 1, "Gives achievement quests", NPCCategory.QUEST, NPCRarity.UNCOMMON),
    COMBAT_QUEST("Combat Quest", "⚔️", 1, "Gives combat-related quests", NPCCategory.QUEST, NPCRarity.COMMON),
    MINING_QUEST("Mining Quest", "⛏️", 1, "Gives mining-related quests", NPCCategory.QUEST, NPCRarity.COMMON),
    FARMING_QUEST("Farming Quest", "🌾", 1, "Gives farming-related quests", NPCCategory.QUEST, NPCRarity.COMMON),
    FISHING_QUEST("Fishing Quest", "🎣", 1, "Gives fishing-related quests", NPCCategory.QUEST, NPCRarity.COMMON),
    FORAGING_QUEST("Foraging Quest", "🌳", 1, "Gives foraging-related quests", NPCCategory.QUEST, NPCRarity.COMMON),
    BOSS_QUEST("Boss Quest", "👑", 1, "Gives boss-fighting quests", NPCCategory.QUEST, NPCRarity.EPIC),
    DUNGEON_QUEST("Dungeon Quest", "🏰", 1, "Gives dungeon-related quests", NPCCategory.QUEST, NPCRarity.RARE),
    COLLECTION_QUEST("Collection Quest", "📦", 1, "Gives collection quests", NPCCategory.QUEST, NPCRarity.UNCOMMON),
    EXPLORATION_QUEST("Exploration Quest", "🗺️", 1, "Gives exploration quests", NPCCategory.QUEST, NPCRarity.UNCOMMON),
    LEGENDARY_QUEST("Legendary Quest", "🌟", 1, "Gives legendary quests", NPCCategory.QUEST, NPCRarity.LEGENDARY),

    // ===== INFO NPCs (10+) =====
    INFO_DESK("Info Desk", "ℹ️", 1, "Provides general information", NPCCategory.INFO, NPCRarity.COMMON),
    HELP_DESK("Help Desk", "❓", 1, "Provides help and support", NPCCategory.INFO, NPCRarity.COMMON),
    GUIDE("Guide", "🗺️", 1, "Provides location guides", NPCCategory.INFO, NPCRarity.COMMON),
    TUTORIAL("Tutorial", "📚", 1, "Provides tutorials", NPCCategory.INFO, NPCRarity.COMMON),
    NEWS("News", "📰", 1, "Provides news and updates", NPCCategory.INFO, NPCRarity.COMMON),
    RULES("Rules", "📋", 1, "Provides server rules", NPCCategory.INFO, NPCRarity.COMMON),
    EVENTS("Events", "🎪", 1, "Provides event information", NPCCategory.INFO, NPCRarity.UNCOMMON),
    UPDATES("Updates", "🔄", 1, "Provides update information", NPCCategory.INFO, NPCRarity.UNCOMMON),
    FEATURES("Features", "✨", 1, "Provides feature information", NPCCategory.INFO, NPCRarity.UNCOMMON),
    COMMUNITY("Community", "👥", 1, "Provides community information", NPCCategory.INFO, NPCRarity.COMMON),

    // ===== WARP NPCs (10+) =====
    WARP_MASTER("Warp Master", "🌍", 1, "Teleports to various locations", NPCCategory.WARP, NPCRarity.COMMON),
    HUB_WARP("Hub Warp", "🏠", 1, "Teleports to Hub", NPCCategory.WARP, NPCRarity.COMMON),
    ISLAND_WARP("Island Warp", "🏝️", 1, "Teleports to Private Island", NPCCategory.WARP, NPCRarity.COMMON),
    DUNGEON_WARP("Dungeon Warp", "🏰", 1, "Teleports to Dungeons", NPCCategory.WARP, NPCRarity.RARE),
    MINING_WARP("Mining Warp", "⛏️", 1, "Teleports to Mining Areas", NPCCategory.WARP, NPCRarity.UNCOMMON),
    COMBAT_WARP("Combat Warp", "⚔️", 1, "Teleports to Combat Areas", NPCCategory.WARP, NPCRarity.UNCOMMON),
    FARMING_WARP("Farming Warp", "🌾", 1, "Teleports to Farming Areas", NPCCategory.WARP, NPCRarity.UNCOMMON),
    FISHING_WARP("Fishing Warp", "🎣", 1, "Teleports to Fishing Areas", NPCCategory.WARP, NPCRarity.UNCOMMON),
    SPECIAL_WARP("Special Warp", "✨", 1, "Teleports to Special Areas", NPCCategory.WARP, NPCRarity.RARE),
    EVENT_WARP("Event Warp", "🎪", 1, "Teleports to Event Areas", NPCCategory.WARP, NPCRarity.EPIC),

    // ===== BANK NPCs (5+) =====
    BANK_CLERK("Bank Clerk", "🏦", 1, "Basic banking services", NPCCategory.BANK, NPCRarity.COMMON),
    BANK_MANAGER("Bank Manager", "👔", 1, "Advanced banking services", NPCCategory.BANK, NPCRarity.UNCOMMON),
    LOAN_OFFICER("Loan Officer", "💰", 1, "Loan and credit services", NPCCategory.BANK, NPCRarity.RARE),
    INVESTMENT_ADVISOR("Investment Advisor", "📈", 1, "Investment services", NPCCategory.BANK, NPCRarity.EPIC),
    VAULT_KEEPER("Vault Keeper", "🔒", 1, "Vault and storage services", NPCCategory.BANK, NPCRarity.LEGENDARY),

    // ===== AUCTION NPCs (5+) =====
    AUCTION_CLERK("Auction Clerk", "🏪", 1, "Basic auction services", NPCCategory.AUCTION, NPCRarity.COMMON),
    AUCTION_MASTER_ADVANCED("Auction Master", "👑", 1, "Advanced auction services", NPCCategory.AUCTION, NPCRarity.UNCOMMON),
    BIDDING_AGENT("Bidding Agent", "📊", 1, "Bidding assistance", NPCCategory.AUCTION, NPCRarity.RARE),
    VALUATION_EXPERT("Valuation Expert", "💎", 1, "Item valuation services", NPCCategory.AUCTION, NPCRarity.EPIC),
    COLLECTOR("Collector", "📦", 1, "Rare item collector", NPCCategory.AUCTION, NPCRarity.LEGENDARY),

    // ===== GUILD NPCs (5+) =====
    GUILD_MASTER("Guild Master", "🏰", 1, "Guild management", NPCCategory.GUILD, NPCRarity.COMMON),
    GUILD_OFFICER("Guild Officer", "👮", 1, "Guild administration", NPCCategory.GUILD, NPCRarity.UNCOMMON),
    GUILD_RECRUITER("Guild Recruiter", "📢", 1, "Guild recruitment", NPCCategory.GUILD, NPCRarity.COMMON),
    GUILD_BANKER("Guild Banker", "🏦", 1, "Guild banking", NPCCategory.GUILD, NPCRarity.RARE),
    GUILD_WAR_MASTER("Guild War Master", "⚔️", 1, "Guild war management", NPCCategory.GUILD, NPCRarity.EPIC),

    // ===== PET NPCs (5+) =====
    PET_KEEPER("Pet Keeper", "🐾", 1, "Pet management", NPCCategory.PET, NPCRarity.COMMON),
    PET_TRAINER("Pet Trainer", "🎓", 1, "Pet training", NPCCategory.PET, NPCRarity.UNCOMMON),
    PET_BREEDER("Pet Breeder", "💕", 1, "Pet breeding", NPCCategory.PET, NPCRarity.RARE),
    PET_VETERINARIAN("Pet Veterinarian", "🏥", 1, "Pet health care", NPCCategory.PET, NPCRarity.EPIC),
    PET_PSYCHOLOGIST("Pet Psychologist", "🧠", 1, "Pet behavior", NPCCategory.PET, NPCRarity.LEGENDARY),

    // ===== COSMETIC NPCs (5+) =====
    COSMETIC_SHOP_ADVANCED("Cosmetic Shop", "✨", 1, "Cosmetic items", NPCCategory.COSMETIC, NPCRarity.COMMON),
    SKIN_DESIGNER("Skin Designer", "🎨", 1, "Custom skins", NPCCategory.COSMETIC, NPCRarity.UNCOMMON),
    PARTICLE_ARTIST("Particle Artist", "🌟", 1, "Particle effects", NPCCategory.COSMETIC, NPCRarity.RARE),
    EMOTE_TEACHER("Emote Teacher", "🎭", 1, "Emotes and animations", NPCCategory.COSMETIC, NPCRarity.EPIC),
    FASHION_DESIGNER("Fashion Designer", "👗", 1, "Fashion and style", NPCCategory.COSMETIC, NPCRarity.LEGENDARY),

    // ===== ADMIN NPCs (5+) =====
    ADMIN_ASSISTANT("Admin Assistant", "👑", 1, "Admin support", NPCCategory.ADMIN, NPCRarity.SPECIAL),
    MODERATOR("Moderator", "👮", 1, "Moderation services", NPCCategory.ADMIN, NPCRarity.SPECIAL),
    HELPER("Helper", "🤝", 1, "Player assistance", NPCCategory.ADMIN, NPCRarity.SPECIAL),
    DEVELOPER("Developer", "💻", 1, "Development support", NPCCategory.ADMIN, NPCRarity.SPECIAL),
    OWNER("Owner", "👑", 1, "Server ownership", NPCCategory.ADMIN, NPCRarity.SPECIAL),

    // ===== SPECIAL NPCs (10+) =====
    MYSTERY_MERCHANT("Mystery Merchant", "🎭", 1, "Mysterious items", NPCCategory.SPECIAL, NPCRarity.LEGENDARY),
    TIME_TRAVELER("Time Traveler", "⏰", 1, "Time-related services", NPCCategory.SPECIAL, NPCRarity.MYTHIC),
    DIMENSION_GUARDIAN("Dimension Guardian", "🌌", 1, "Dimension protection", NPCCategory.SPECIAL, NPCRarity.MYTHIC),
    SOUL_COLLECTOR("Soul Collector", "👻", 1, "Soul-related services", NPCCategory.SPECIAL, NPCRarity.LEGENDARY),
    DREAM_WALKER("Dream Walker", "💭", 1, "Dream-related services", NPCCategory.SPECIAL, NPCRarity.MYTHIC),
    VOID_MASTER("Void Master", "🌑", 1, "Void-related services", NPCCategory.SPECIAL, NPCRarity.MYTHIC),
    ELEMENTAL_MASTER("Elemental Master", "⚡", 1, "Elemental powers", NPCCategory.SPECIAL, NPCRarity.LEGENDARY),
    CELESTIAL_GUARDIAN("Celestial Guardian", "⭐", 1, "Celestial protection", NPCCategory.SPECIAL, NPCRarity.MYTHIC),
    INFINITY_KEEPER("Infinity Keeper", "♾️", 1, "Infinite possibilities", NPCCategory.SPECIAL, NPCRarity.MYTHIC),
    COSMIC_ENTITY("Cosmic Entity", "🌌", 1, "Cosmic powers", NPCCategory.SPECIAL, NPCRarity.MYTHIC),

    // ===== EVENT NPCs (8+) =====
    EVENT_COORDINATOR("Event Coordinator", "🎪", 1, "Event management", NPCCategory.EVENT, NPCRarity.EPIC),
    SPOOKY_MASTER("Spooky Master", "👻", 1, "Halloween events", NPCCategory.EVENT, NPCRarity.EPIC),
    JERRY_MASTER("Jerry Master", "🎄", 1, "Christmas events", NPCCategory.EVENT, NPCRarity.EPIC),
    NEW_YEAR_MASTER("New Year Master", "🎆", 1, "New Year events", NPCCategory.EVENT, NPCRarity.EPIC),
    EASTER_MASTER("Easter Master", "🐰", 1, "Easter events", NPCCategory.EVENT, NPCRarity.EPIC),
    VALENTINE_MASTER("Valentine Master", "💕", 1, "Valentine events", NPCCategory.EVENT, NPCRarity.EPIC),
    SUMMER_MASTER("Summer Master", "☀️", 1, "Summer events", NPCCategory.EVENT, NPCRarity.EPIC),
    WINTER_MASTER("Winter Master", "❄️", 1, "Winter events", NPCCategory.EVENT, NPCRarity.EPIC);

    private final String displayName;
    private final String icon;
    private final int level;
    private final String description;
    private final NPCCategory category;
    private final NPCRarity rarity;

    CompleteNPCType(String displayName, String icon, int level, String description, NPCCategory category, NPCRarity rarity) {
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

    public NPCCategory getCategory() {
        return category;
    }

    public NPCRarity getRarity() {
        return rarity;
    }

    /**
     * Get NPCs by category
     */
    public static List<CompleteNPCType> getNPCsByCategory(NPCCategory category) {
        return Arrays.stream(values())
                .filter(npc -> npc.getCategory() == category)
                .toList();
    }

    /**
     * Get NPCs by rarity
     */
    public static List<CompleteNPCType> getNPCsByRarity(NPCRarity rarity) {
        return Arrays.stream(values())
                .filter(npc -> npc.getRarity() == rarity)
                .toList();
    }

    /**
     * Get NPCs by level range
     */
    public static List<CompleteNPCType> getNPCsByLevelRange(int minLevel, int maxLevel) {
        return Arrays.stream(values())
                .filter(npc -> npc.getLevel() >= minLevel && npc.getLevel() <= maxLevel)
                .toList();
    }

    /**
     * Get total NPC count
     */
    public static int getTotalNPCCount() {
        return values().length;
    }

    /**
     * Get NPC count by category
     */
    public static int getNPCCountByCategory(NPCCategory category) {
        return (int) Arrays.stream(values())
                .filter(npc -> npc.getCategory() == category)
                .count();
    }

    /**
     * Get NPC count by rarity
     */
    public static int getNPCCountByRarity(NPCRarity rarity) {
        return (int) Arrays.stream(values())
                .filter(npc -> npc.getRarity() == rarity)
                .count();
    }

    /**
     * Get hub NPCs
     */
    public static List<CompleteNPCType> getHubNPCs() {
        return getNPCsByCategory(NPCCategory.HUB);
    }

    /**
     * Get shop NPCs
     */
    public static List<CompleteNPCType> getShopNPCs() {
        return getNPCsByCategory(NPCCategory.SHOP);
    }

    /**
     * Get quest NPCs
     */
    public static List<CompleteNPCType> getQuestNPCs() {
        return getNPCsByCategory(NPCCategory.QUEST);
    }

    /**
     * Get info NPCs
     */
    public static List<CompleteNPCType> getInfoNPCs() {
        return getNPCsByCategory(NPCCategory.INFO);
    }

    /**
     * Get warp NPCs
     */
    public static List<CompleteNPCType> getWarpNPCs() {
        return getNPCsByCategory(NPCCategory.WARP);
    }

    /**
     * Get bank NPCs
     */
    public static List<CompleteNPCType> getBankNPCs() {
        return getNPCsByCategory(NPCCategory.BANK);
    }

    /**
     * Get auction NPCs
     */
    public static List<CompleteNPCType> getAuctionNPCs() {
        return getNPCsByCategory(NPCCategory.AUCTION);
    }

    /**
     * Get guild NPCs
     */
    public static List<CompleteNPCType> getGuildNPCs() {
        return getNPCsByCategory(NPCCategory.GUILD);
    }

    /**
     * Get pet NPCs
     */
    public static List<CompleteNPCType> getPetNPCs() {
        return getNPCsByCategory(NPCCategory.PET);
    }

    /**
     * Get cosmetic NPCs
     */
    public static List<CompleteNPCType> getCosmeticNPCs() {
        return getNPCsByCategory(NPCCategory.COSMETIC);
    }

    /**
     * Get admin NPCs
     */
    public static List<CompleteNPCType> getAdminNPCs() {
        return getNPCsByCategory(NPCCategory.ADMIN);
    }

    /**
     * Get special NPCs
     */
    public static List<CompleteNPCType> getSpecialNPCs() {
        return getNPCsByCategory(NPCCategory.SPECIAL);
    }

    /**
     * Get event NPCs
     */
    public static List<CompleteNPCType> getEventNPCs() {
        return getNPCsByCategory(NPCCategory.EVENT);
    }

    /**
     * Get NPCs suitable for specific activities
     */
    public static List<CompleteNPCType> getNPCsForActivity(String activity) {
        return Arrays.stream(values())
                .filter(npc -> isSuitableForActivity(npc, activity))
                .toList();
    }

    /**
     * Check if NPC is suitable for activity
     */
    private static boolean isSuitableForActivity(CompleteNPCType npc, String activity) {
        String act = activity.toLowerCase();
        NPCCategory category = npc.getCategory();
        
        return switch (category) {
            case SHOP -> act.contains("shop") || act.contains("buy") || act.contains("sell");
            case QUEST -> act.contains("quest") || act.contains("mission") || act.contains("task");
            case INFO -> act.contains("info") || act.contains("help") || act.contains("guide");
            case WARP -> act.contains("warp") || act.contains("teleport") || act.contains("travel");
            case BANK -> act.contains("bank") || act.contains("money") || act.contains("finance");
            case AUCTION -> act.contains("auction") || act.contains("bid") || act.contains("sell");
            case GUILD -> act.contains("guild") || act.contains("clan") || act.contains("group");
            case PET -> act.contains("pet") || act.contains("animal") || act.contains("companion");
            case COSMETIC -> act.contains("cosmetic") || act.contains("skin") || act.contains("style");
            case ADMIN -> act.contains("admin") || act.contains("mod") || act.contains("staff");
            case SPECIAL -> act.contains("special") || act.contains("rare") || act.contains("unique");
            case EVENT -> act.contains("event") || act.contains("festival") || act.contains("celebration");
            default -> true;
        };
    }

    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
