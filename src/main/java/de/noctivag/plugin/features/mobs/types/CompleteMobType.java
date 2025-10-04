package de.noctivag.plugin.features.mobs.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All 100+ mob types from the comprehensive Hypixel SkyBlock programming guide
 */
public enum CompleteMobType {
    // ===== PRIVATE ISLAND MOBS (5+) =====
    ZOMBIE("Zombie", "🧟", 1, 20, 5, "Rotten Flesh, XP", MobCategory.PRIVATE_ISLAND, MobRarity.COMMON),
    SKELETON("Skeleton", "💀", 1, 15, 10, "Bones, XP", MobCategory.PRIVATE_ISLAND, MobRarity.COMMON),
    CREEPER("Creeper", "💣", 1, 25, 15, "Gunpowder, XP", MobCategory.PRIVATE_ISLAND, MobRarity.COMMON),
    SPIDER("Spider", "🕷️", 1, 20, 8, "String, XP", MobCategory.PRIVATE_ISLAND, MobRarity.COMMON),
    ENDERMAN("Enderman", "🌑", 5, 50, 20, "Ender Pearls, XP", MobCategory.PRIVATE_ISLAND, MobRarity.UNCOMMON),

    // ===== HUB MOBS (10+) =====
    GRAVEYARD_ZOMBIE("Graveyard Zombie", "🧟", 5, 100, 25, "Rotten Flesh, XP", MobCategory.HUB, MobRarity.COMMON),
    ZOMBIE_VILLAGER("Zombie Villager", "🧟", 8, 150, 35, "Rotten Flesh, XP", MobCategory.HUB, MobRarity.COMMON),
    CRYPT_GHOUL("Crypt Ghoul", "👻", 15, 300, 50, "Rotten Flesh, XP", MobCategory.HUB, MobRarity.UNCOMMON),
    CRYPT_LURKER("Crypt Lurker", "👻", 20, 400, 65, "Rotten Flesh, XP", MobCategory.HUB, MobRarity.UNCOMMON),
    WOLF("Wolf", "🐺", 10, 200, 40, "Bones, XP", MobCategory.HUB, MobRarity.COMMON),
    RABBIT("Rabbit", "🐰", 5, 50, 10, "Rabbit Hide, XP", MobCategory.HUB, MobRarity.COMMON),
    CHICKEN("Chicken", "🐔", 3, 30, 5, "Feathers, XP", MobCategory.HUB, MobRarity.COMMON),
    COW("Cow", "🐄", 5, 60, 15, "Beef, XP", MobCategory.HUB, MobRarity.COMMON),
    PIG("Pig", "🐷", 4, 40, 12, "Pork, XP", MobCategory.HUB, MobRarity.COMMON),
    SHEEP("Sheep", "🐑", 4, 35, 10, "Wool, XP", MobCategory.HUB, MobRarity.COMMON),

    // ===== DEEP CAVERNS MOBS (8+) =====
    LAPIS_ZOMBIE("Lapis Zombie", "🧟", 15, 200, 50, "Lapis, Enchanted Lapis", MobCategory.DEEP_CAVERNS, MobRarity.UNCOMMON),
    REDSTONE_PIGMAN("Redstone Pigman", "🐷", 20, 300, 75, "Redstone, Enchanted Redstone", MobCategory.DEEP_CAVERNS, MobRarity.UNCOMMON),
    DIAMOND_SKELETON("Diamond Skeleton", "💀", 25, 400, 100, "Diamonds, Enchanted Diamonds", MobCategory.DEEP_CAVERNS, MobRarity.RARE),
    OBSIDIAN_DEFENDER("Obsidian Defender", "🛡️", 30, 500, 125, "Obsidian, Enchanted Obsidian", MobCategory.DEEP_CAVERNS, MobRarity.RARE),
    LAPIS_LAZULI_ZOMBIE("Lapis Lazuli Zombie", "🧟", 18, 250, 60, "Lapis, Enchanted Lapis", MobCategory.DEEP_CAVERNS, MobRarity.UNCOMMON),
    REDSTONE_PIGMAN_BRUTE("Redstone Pigman Brute", "🐷", 22, 350, 85, "Redstone, Enchanted Redstone", MobCategory.DEEP_CAVERNS, MobRarity.RARE),
    DIAMOND_SKELETON_ARCHER("Diamond Skeleton Archer", "🏹", 27, 450, 110, "Diamonds, Enchanted Diamonds", MobCategory.DEEP_CAVERNS, MobRarity.RARE),
    OBSIDIAN_DEFENDER_ELITE("Obsidian Defender Elite", "🛡️", 35, 600, 150, "Obsidian, Enchanted Obsidian", MobCategory.DEEP_CAVERNS, MobRarity.EPIC),

    // ===== SPIDER'S DEN MOBS (8+) =====
    CAVE_SPIDER("Cave Spider", "🕷️", 10, 100, 30, "String, Spider Eyes", MobCategory.SPIDERS_DEN, MobRarity.COMMON),
    TARANTULA("Tarantula", "🕷️", 20, 300, 80, "String, Tarantula Silk", MobCategory.SPIDERS_DEN, MobRarity.UNCOMMON),
    SPIDER_JOCKEY("Spider Jockey", "🕷️", 25, 400, 100, "String, Bones", MobCategory.SPIDERS_DEN, MobRarity.RARE),
    ARACHNE("Arachne", "🕷️", 50, 10000, 500, "Arachne's Sword, Arachne Fragments", MobCategory.SPIDERS_DEN, MobRarity.LEGENDARY),
    CAVE_SPIDER_SWARM("Cave Spider Swarm", "🕷️", 12, 150, 40, "String, Spider Eyes", MobCategory.SPIDERS_DEN, MobRarity.UNCOMMON),
    TARANTULA_BEAST("Tarantula Beast", "🕷️", 25, 500, 120, "String, Tarantula Silk", MobCategory.SPIDERS_DEN, MobRarity.RARE),
    SPIDER_QUEEN("Spider Queen", "🕷️", 30, 800, 150, "String, Spider Eyes", MobCategory.SPIDERS_DEN, MobRarity.EPIC),
    WEBWEAVER("Webweaver", "🕷️", 35, 1000, 180, "String, Spider Silk", MobCategory.SPIDERS_DEN, MobRarity.EPIC),

    // ===== THE END MOBS (10+) =====
    ENDERMAN_END("Enderman", "🌑", 25, 500, 100, "Ender Pearls, Ender Armor", MobCategory.THE_END, MobRarity.UNCOMMON),
    ENDERMITE("Endermite", "🌑", 30, 300, 150, "Ender Pearls, Ender Mite", MobCategory.THE_END, MobRarity.UNCOMMON),
    ZEALOT("Zealot", "🌑", 40, 1000, 200, "Summoning Eyes, Ender Pearls", MobCategory.THE_END, MobRarity.RARE),
    SPECIAL_ZEALOT("Special Zealot", "🌟", 60, 5000, 600, "Summoning Eyes (100% Chance)", MobCategory.THE_END, MobRarity.LEGENDARY),
    ENDER_DRAGON("Ender Dragon", "🐉", 100, 50000, 1000, "Dragon Fragments, Dragon Armor", MobCategory.THE_END, MobRarity.MYTHIC),
    VOIDLING("Voidling", "🌑", 35, 400, 120, "Ender Pearls, XP", MobCategory.THE_END, MobRarity.UNCOMMON),
    ENDERMAN_ZEALOT("Enderman Zealot", "🌑", 45, 800, 180, "Ender Pearls, XP", MobCategory.THE_END, MobRarity.RARE),
    VOIDLING_EXTREMIST("Voidling Extremist", "🌑", 50, 1200, 250, "Ender Pearls, XP", MobCategory.THE_END, MobRarity.EPIC),
    ENDERMAN_PROTECTOR("Enderman Protector", "🌑", 55, 1500, 300, "Ender Pearls, XP", MobCategory.THE_END, MobRarity.EPIC),
    WATCHER("Watcher", "👁️", 65, 2000, 400, "Ender Pearls, XP", MobCategory.THE_END, MobRarity.LEGENDARY),

    // ===== DWARVEN MINES MOBS (8+) =====
    GOBLIN("Goblin", "👹", 30, 800, 200, "Mithril, Goblin Eggs", MobCategory.DWARVEN_MINES, MobRarity.UNCOMMON),
    ICE_WALKER("Ice Walker", "🧊", 35, 1000, 250, "Ice, Frozen Blaze Rods", MobCategory.DWARVEN_MINES, MobRarity.UNCOMMON),
    GOLDEN_GOBLIN("Golden Goblin", "👹", 40, 1500, 300, "Gold, Enchanted Gold", MobCategory.DWARVEN_MINES, MobRarity.RARE),
    TREASURE_HOARDER("Treasure Hoarder", "💰", 45, 2000, 350, "Coins, Treasure", MobCategory.DWARVEN_MINES, MobRarity.RARE),
    GOBLIN_THIEF("Goblin Thief", "👹", 32, 900, 220, "Mithril, Goblin Eggs", MobCategory.DWARVEN_MINES, MobRarity.UNCOMMON),
    ICE_WALKER_GUARDIAN("Ice Walker Guardian", "🧊", 38, 1200, 280, "Ice, Frozen Blaze Rods", MobCategory.DWARVEN_MINES, MobRarity.RARE),
    GOLDEN_GOBLIN_KING("Golden Goblin King", "👹", 42, 1800, 350, "Gold, Enchanted Gold", MobCategory.DWARVEN_MINES, MobRarity.EPIC),
    TREASURE_HOARDER_ELITE("Treasure Hoarder Elite", "💰", 48, 2500, 400, "Coins, Treasure", MobCategory.DWARVEN_MINES, MobRarity.EPIC),

    // ===== CRYSTAL HOLLOWS MOBS (8+) =====
    AUTOMATON("Automaton", "🤖", 50, 2000, 400, "Gemstones, Automaton Parts", MobCategory.CRYSTAL_HOLLOWS, MobRarity.RARE),
    GOBLIN_CRYSTAL("Goblin", "👹", 55, 2500, 450, "Gemstones, Goblin Eggs", MobCategory.CRYSTAL_HOLLOWS, MobRarity.RARE),
    CRYSTAL_WALKER("Crystal Walker", "💎", 60, 3000, 500, "Gemstones, Crystal Fragments", MobCategory.CRYSTAL_HOLLOWS, MobRarity.EPIC),
    CRYSTAL_GUARDIAN("Crystal Guardian", "💎", 70, 5000, 700, "Gemstones, Crystal Armor", MobCategory.CRYSTAL_HOLLOWS, MobRarity.LEGENDARY),
    AUTOMATON_MARK_II("Automaton Mark II", "🤖", 52, 2200, 420, "Gemstones, Automaton Parts", MobCategory.CRYSTAL_HOLLOWS, MobRarity.RARE),
    GOBLIN_CRYSTAL_KING("Goblin King", "👹", 58, 2800, 480, "Gemstones, Goblin Eggs", MobCategory.CRYSTAL_HOLLOWS, MobRarity.EPIC),
    CRYSTAL_WALKER_ELITE("Crystal Walker Elite", "💎", 65, 3500, 550, "Gemstones, Crystal Fragments", MobCategory.CRYSTAL_HOLLOWS, MobRarity.LEGENDARY),
    CRYSTAL_GUARDIAN_ANCIENT("Crystal Guardian Ancient", "💎", 75, 6000, 800, "Gemstones, Crystal Armor", MobCategory.CRYSTAL_HOLLOWS, MobRarity.MYTHIC),

    // ===== CRIMSON ISLE MOBS (8+) =====
    BLAZE("Blaze", "🔥", 60, 3000, 600, "Blaze Rods, Enchanted Blaze Rods", MobCategory.CRIMSON_ISLE, MobRarity.RARE),
    MAGMA_CUBE("Magma Cube", "🌋", 65, 3500, 650, "Magma Cream, Enchanted Magma Cream", MobCategory.CRIMSON_ISLE, MobRarity.RARE),
    PIGMAN("Pigman", "🐷", 70, 4000, 700, "Gold, Enchanted Gold", MobCategory.CRIMSON_ISLE, MobRarity.RARE),
    KUUDRA("Kuudra", "🐙", 100, 100000, 2000, "Kuudra Armor, Kuudra Weapons", MobCategory.CRIMSON_ISLE, MobRarity.MYTHIC),
    BLAZE_ELITE("Blaze Elite", "🔥", 62, 3200, 620, "Blaze Rods, Enchanted Blaze Rods", MobCategory.CRIMSON_ISLE, MobRarity.EPIC),
    MAGMA_CUBE_GUARDIAN("Magma Cube Guardian", "🌋", 68, 3800, 680, "Magma Cream, Enchanted Magma Cream", MobCategory.CRIMSON_ISLE, MobRarity.EPIC),
    PIGMAN_BRUTE("Pigman Brute", "🐷", 72, 4200, 720, "Gold, Enchanted Gold", MobCategory.CRIMSON_ISLE, MobRarity.EPIC),
    KUUDRA_FOLLOWER("Kuudra Follower", "🐙", 80, 50000, 1500, "Kuudra Fragments, XP", MobCategory.CRIMSON_ISLE, MobRarity.LEGENDARY),

    // ===== SLAYER BOSSES (16+) =====
    // Zombie Slayer
    REVENANT_HORROR_I("Revenant Horror I", "🧟", 100, 10000, 500, "Revenant Armor, Revenant Falchion", MobCategory.SLAYER_BOSS, MobRarity.LEGENDARY),
    REVENANT_HORROR_II("Revenant Horror II", "🧟", 200, 25000, 750, "Revenant Armor, Revenant Falchion", MobCategory.SLAYER_BOSS, MobRarity.LEGENDARY),
    REVENANT_HORROR_III("Revenant Horror III", "🧟", 300, 50000, 1000, "Revenant Armor, Revenant Falchion", MobCategory.SLAYER_BOSS, MobRarity.LEGENDARY),
    REVENANT_HORROR_IV("Revenant Horror IV", "🧟", 500, 100000, 1500, "Revenant Armor, Revenant Falchion", MobCategory.SLAYER_BOSS, MobRarity.MYTHIC),
    
    // Spider Slayer
    TARANTULA_BROODFATHER_I("Tarantula Broodfather I", "🕷️", 100, 15000, 600, "Tarantula Armor, Tarantula Sword", MobCategory.SLAYER_BOSS, MobRarity.LEGENDARY),
    TARANTULA_BROODFATHER_II("Tarantula Broodfather II", "🕷️", 200, 37500, 900, "Tarantula Armor, Tarantula Sword", MobCategory.SLAYER_BOSS, MobRarity.LEGENDARY),
    TARANTULA_BROODFATHER_III("Tarantula Broodfather III", "🕷️", 300, 75000, 1200, "Tarantula Armor, Tarantula Sword", MobCategory.SLAYER_BOSS, MobRarity.LEGENDARY),
    TARANTULA_BROODFATHER_IV("Tarantula Broodfather IV", "🕷️", 500, 150000, 1800, "Tarantula Armor, Tarantula Sword", MobCategory.SLAYER_BOSS, MobRarity.MYTHIC),
    
    // Wolf Slayer
    SVEN_PACKMASTER_I("Sven Packmaster I", "🐺", 100, 20000, 700, "Mastiff Armor, Shaman Sword", MobCategory.SLAYER_BOSS, MobRarity.LEGENDARY),
    SVEN_PACKMASTER_II("Sven Packmaster II", "🐺", 200, 50000, 1050, "Mastiff Armor, Shaman Sword", MobCategory.SLAYER_BOSS, MobRarity.LEGENDARY),
    SVEN_PACKMASTER_III("Sven Packmaster III", "🐺", 300, 100000, 1400, "Mastiff Armor, Shaman Sword", MobCategory.SLAYER_BOSS, MobRarity.LEGENDARY),
    SVEN_PACKMASTER_IV("Sven Packmaster IV", "🐺", 500, 200000, 2100, "Mastiff Armor, Shaman Sword", MobCategory.SLAYER_BOSS, MobRarity.MYTHIC),
    
    // Enderman Slayer
    VOIDGLOOM_SERAPH_I("Voidgloom Seraph I", "🌑", 100, 50000, 1000, "Final Destination Armor, Voidedge Katana", MobCategory.SLAYER_BOSS, MobRarity.MYTHIC),
    VOIDGLOOM_SERAPH_II("Voidgloom Seraph II", "🌑", 200, 125000, 1500, "Final Destination Armor, Voidedge Katana", MobCategory.SLAYER_BOSS, MobRarity.MYTHIC),
    VOIDGLOOM_SERAPH_III("Voidgloom Seraph III", "🌑", 300, 250000, 2000, "Final Destination Armor, Voidedge Katana", MobCategory.SLAYER_BOSS, MobRarity.MYTHIC),
    VOIDGLOOM_SERAPH_IV("Voidgloom Seraph IV", "🌑", 500, 500000, 3000, "Final Destination Armor, Voidedge Katana", MobCategory.SLAYER_BOSS, MobRarity.MYTHIC),

    // ===== WORLD BOSSES (8+) =====
    MAGMA_BOSS("Magma Boss", "🌋", 80, 50000, 1500, "Magma Armor, Magma Weapons", MobCategory.WORLD_BOSS, MobRarity.LEGENDARY),
    ARACHNE_BOSS("Arachne Boss", "🕷️", 85, 60000, 1600, "Arachne Armor, Arachne Weapons", MobCategory.WORLD_BOSS, MobRarity.LEGENDARY),
    DRAGON_BOSS("Dragon Boss", "🐉", 90, 70000, 1700, "Dragon Armor, Dragon Weapons", MobCategory.WORLD_BOSS, MobRarity.LEGENDARY),
    WITHER_BOSS("Wither Boss", "💀", 95, 80000, 1800, "Wither Armor, Wither Weapons", MobCategory.WORLD_BOSS, MobRarity.LEGENDARY),
    MAGMA_BOSS_ELITE("Magma Boss Elite", "🌋", 82, 55000, 1550, "Magma Armor, Magma Weapons", MobCategory.WORLD_BOSS, MobRarity.LEGENDARY),
    ARACHNE_BOSS_ELITE("Arachne Boss Elite", "🕷️", 87, 65000, 1650, "Arachne Armor, Arachne Weapons", MobCategory.WORLD_BOSS, MobRarity.LEGENDARY),
    DRAGON_BOSS_ELITE("Dragon Boss Elite", "🐉", 92, 75000, 1750, "Dragon Armor, Dragon Weapons", MobCategory.WORLD_BOSS, MobRarity.LEGENDARY),
    WITHER_BOSS_ELITE("Wither Boss Elite", "💀", 97, 85000, 1850, "Wither Armor, Wither Weapons", MobCategory.WORLD_BOSS, MobRarity.LEGENDARY),

    // ===== SPECIAL BOSSES (8+) =====
    INFERNO_DEMONLORD("Inferno Demonlord", "👹", 100, 200000, 2500, "Inferno Armor, Inferno Weapons", MobCategory.SPECIAL_BOSS, MobRarity.MYTHIC),
    KUUDRA_BOSS("Kuudra Boss", "🐙", 100, 300000, 3000, "Kuudra Armor, Kuudra Weapons", MobCategory.SPECIAL_BOSS, MobRarity.MYTHIC),
    NECRON("Necron", "💀", 100, 400000, 3500, "Necron Armor, Necron Weapons", MobCategory.SPECIAL_BOSS, MobRarity.MYTHIC),
    SADAN("Sadan", "🧟", 100, 500000, 4000, "Sadan Armor, Sadan Weapons", MobCategory.SPECIAL_BOSS, MobRarity.MYTHIC),
    LIVID("Livid", "🌑", 100, 600000, 4500, "Livid Armor, Livid Weapons", MobCategory.SPECIAL_BOSS, MobRarity.MYTHIC),
    SCARF("Scarf", "💀", 100, 700000, 5000, "Scarf Armor, Scarf Weapons", MobCategory.SPECIAL_BOSS, MobRarity.MYTHIC),
    THORN("Thorn", "🕷️", 100, 800000, 5500, "Thorn Armor, Thorn Weapons", MobCategory.SPECIAL_BOSS, MobRarity.MYTHIC),
    BONZO("Bonzo", "🎭", 100, 900000, 6000, "Bonzo Armor, Bonzo Weapons", MobCategory.SPECIAL_BOSS, MobRarity.MYTHIC),

    // ===== DUNGEON MOBS (10+) =====
    DUNGEON_ZOMBIE("Dungeon Zombie", "🧟", 30, 800, 200, "Rotten Flesh, XP", MobCategory.DUNGEON, MobRarity.UNCOMMON),
    DUNGEON_SKELETON("Dungeon Skeleton", "💀", 35, 900, 220, "Bones, XP", MobCategory.DUNGEON, MobRarity.UNCOMMON),
    DUNGEON_SPIDER("Dungeon Spider", "🕷️", 40, 1000, 240, "String, XP", MobCategory.DUNGEON, MobRarity.UNCOMMON),
    DUNGEON_CREEPER("Dungeon Creeper", "💣", 45, 1100, 260, "Gunpowder, XP", MobCategory.DUNGEON, MobRarity.UNCOMMON),
    DUNGEON_ENDERMAN("Dungeon Enderman", "🌑", 50, 1200, 280, "Ender Pearls, XP", MobCategory.DUNGEON, MobRarity.RARE),
    DUNGEON_BLAZE("Dungeon Blaze", "🔥", 55, 1300, 300, "Blaze Rods, XP", MobCategory.DUNGEON, MobRarity.RARE),
    DUNGEON_WITHER_SKELETON("Dungeon Wither Skeleton", "💀", 60, 1400, 320, "Wither Skeleton Skulls, XP", MobCategory.DUNGEON, MobRarity.RARE),
    DUNGEON_GUARDIAN("Dungeon Guardian", "🐟", 65, 1500, 340, "Prismarine, XP", MobCategory.DUNGEON, MobRarity.EPIC),
    DUNGEON_WITHER("Dungeon Wither", "💀", 70, 2000, 400, "Wither Skeleton Skulls, XP", MobCategory.DUNGEON, MobRarity.EPIC),
    DUNGEON_DRAGON("Dungeon Dragon", "🐉", 80, 3000, 500, "Dragon Fragments, XP", MobCategory.DUNGEON, MobRarity.LEGENDARY),

    // ===== NETHER MOBS (8+) =====
    NETHER_ZOMBIE("Nether Zombie", "🧟", 25, 600, 150, "Rotten Flesh, XP", MobCategory.NETHER, MobRarity.UNCOMMON),
    NETHER_SKELETON("Nether Skeleton", "💀", 30, 700, 170, "Bones, XP", MobCategory.NETHER, MobRarity.UNCOMMON),
    NETHER_SPIDER("Nether Spider", "🕷️", 35, 800, 190, "String, XP", MobCategory.NETHER, MobRarity.UNCOMMON),
    NETHER_CREEPER("Nether Creeper", "💣", 40, 900, 210, "Gunpowder, XP", MobCategory.NETHER, MobRarity.UNCOMMON),
    NETHER_ENDERMAN("Nether Enderman", "🌑", 45, 1000, 230, "Ender Pearls, XP", MobCategory.NETHER, MobRarity.RARE),
    NETHER_BLAZE("Nether Blaze", "🔥", 50, 1100, 250, "Blaze Rods, XP", MobCategory.NETHER, MobRarity.RARE),
    NETHER_WITHER_SKELETON("Nether Wither Skeleton", "💀", 55, 1200, 270, "Wither Skeleton Skulls, XP", MobCategory.NETHER, MobRarity.RARE),
    NETHER_WITHER("Nether Wither", "💀", 75, 5000, 800, "Wither Skeleton Skulls, XP", MobCategory.NETHER, MobRarity.LEGENDARY),

    // ===== FISHING MOBS (8+) =====
    SEA_CREATURE("Sea Creature", "🐟", 20, 400, 100, "Fish, XP", MobCategory.FISHING, MobRarity.UNCOMMON),
    SHARK("Shark", "🦈", 30, 800, 200, "Shark Fin, XP", MobCategory.FISHING, MobRarity.RARE),
    SQUID("Squid", "🦑", 25, 600, 150, "Ink Sac, XP", MobCategory.FISHING, MobRarity.UNCOMMON),
    GUARDIAN("Guardian", "🐟", 35, 1000, 250, "Prismarine, XP", MobCategory.FISHING, MobRarity.RARE),
    ELDER_GUARDIAN("Elder Guardian", "🐟", 50, 2000, 400, "Prismarine, XP", MobCategory.FISHING, MobRarity.EPIC),
    DOLPHIN("Dolphin", "🐬", 20, 500, 120, "Dolphin Fins, XP", MobCategory.FISHING, MobRarity.UNCOMMON),
    WHALE("Whale", "🐋", 40, 1500, 300, "Whale Fin, XP", MobCategory.FISHING, MobRarity.EPIC),
    SEA_EMPEROR("Sea Emperor", "🐟", 60, 3000, 600, "Sea Emperor Fins, XP", MobCategory.FISHING, MobRarity.LEGENDARY),

    // ===== EVENT MOBS (8+) =====
    SPOOKY_MOB("Spooky Mob", "👻", 30, 800, 200, "Spooky Items, XP", MobCategory.EVENT, MobRarity.UNCOMMON),
    JERRY_MOB("Jerry Mob", "🧀", 25, 600, 150, "Jerry Items, XP", MobCategory.EVENT, MobRarity.UNCOMMON),
    NEW_YEAR_MOB("New Year Mob", "🎆", 35, 1000, 250, "New Year Items, XP", MobCategory.EVENT, MobRarity.UNCOMMON),
    EASTER_MOB("Easter Mob", "🐰", 30, 800, 200, "Easter Items, XP", MobCategory.EVENT, MobRarity.UNCOMMON),
    HALLOWEEN_MOB("Halloween Mob", "🎃", 40, 1200, 300, "Halloween Items, XP", MobCategory.EVENT, MobRarity.RARE),
    CHRISTMAS_MOB("Christmas Mob", "🎄", 35, 1000, 250, "Christmas Items, XP", MobCategory.EVENT, MobRarity.UNCOMMON),
    VALENTINE_MOB("Valentine Mob", "💕", 30, 800, 200, "Valentine Items, XP", MobCategory.EVENT, MobRarity.UNCOMMON),
    SUMMER_MOB("Summer Mob", "☀️", 40, 1200, 300, "Summer Items, XP", MobCategory.EVENT, MobRarity.RARE);

    private final String displayName;
    private final String icon;
    private final int level;
    private final int health;
    private final int damage;
    private final String drops;
    private final MobCategory category;
    private final MobRarity rarity;

    CompleteMobType(String displayName, String icon, int level, int health, int damage, String drops, MobCategory category, MobRarity rarity) {
        this.displayName = displayName;
        this.icon = icon;
        this.level = level;
        this.health = health;
        this.damage = damage;
        this.drops = drops;
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

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public String getDrops() {
        return drops;
    }

    public MobCategory getCategory() {
        return category;
    }

    public MobRarity getRarity() {
        return rarity;
    }

    /**
     * Get mobs by category
     */
    public static List<CompleteMobType> getMobsByCategory(MobCategory category) {
        return Arrays.stream(values())
                .filter(mob -> mob.getCategory() == category)
                .toList();
    }

    /**
     * Get mobs by rarity
     */
    public static List<CompleteMobType> getMobsByRarity(MobRarity rarity) {
        return Arrays.stream(values())
                .filter(mob -> mob.getRarity() == rarity)
                .toList();
    }

    /**
     * Get mobs by level range
     */
    public static List<CompleteMobType> getMobsByLevelRange(int minLevel, int maxLevel) {
        return Arrays.stream(values())
                .filter(mob -> mob.getLevel() >= minLevel && mob.getLevel() <= maxLevel)
                .toList();
    }

    /**
     * Get total mob count
     */
    public static int getTotalMobCount() {
        return values().length;
    }

    /**
     * Get mob count by category
     */
    public static int getMobCountByCategory(MobCategory category) {
        return (int) Arrays.stream(values())
                .filter(mob -> mob.getCategory() == category)
                .count();
    }

    /**
     * Get mob count by rarity
     */
    public static int getMobCountByRarity(MobRarity rarity) {
        return (int) Arrays.stream(values())
                .filter(mob -> mob.getRarity() == rarity)
                .count();
    }

    /**
     * Get mobs by health range
     */
    public static List<CompleteMobType> getMobsByHealthRange(int minHealth, int maxHealth) {
        return Arrays.stream(values())
                .filter(mob -> mob.getHealth() >= minHealth && mob.getHealth() <= maxHealth)
                .toList();
    }

    /**
     * Get mobs by damage range
     */
    public static List<CompleteMobType> getMobsByDamageRange(int minDamage, int maxDamage) {
        return Arrays.stream(values())
                .filter(mob -> mob.getDamage() >= minDamage && mob.getDamage() <= maxDamage)
                .toList();
    }

    /**
     * Get boss mobs (high level, high health)
     */
    public static List<CompleteMobType> getBossMobs() {
        return Arrays.stream(values())
                .filter(mob -> mob.getLevel() >= 50 && mob.getHealth() >= 5000)
                .toList();
    }

    /**
     * Get slayer mobs
     */
    public static List<CompleteMobType> getSlayerMobs() {
        return getMobsByCategory(MobCategory.SLAYER_BOSS);
    }

    /**
     * Get world boss mobs
     */
    public static List<CompleteMobType> getWorldBossMobs() {
        return getMobsByCategory(MobCategory.WORLD_BOSS);
    }

    /**
     * Get special boss mobs
     */
    public static List<CompleteMobType> getSpecialBossMobs() {
        return getMobsByCategory(MobCategory.SPECIAL_BOSS);
    }

    /**
     * Get dungeon mobs
     */
    public static List<CompleteMobType> getDungeonMobs() {
        return getMobsByCategory(MobCategory.DUNGEON);
    }

    /**
     * Get event mobs
     */
    public static List<CompleteMobType> getEventMobs() {
        return getMobsByCategory(MobCategory.EVENT);
    }

    @Override
    public String toString() {
        return icon + " " + displayName;
    }
}
