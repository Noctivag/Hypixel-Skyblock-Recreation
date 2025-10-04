package de.noctivag.plugin.mobs;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedMobSystem implements Listener {

    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerMobData> playerMobData = new ConcurrentHashMap<>();
    private final Map<MobType, MobConfig> mobConfigs = new HashMap<>();
    private final Map<UUID, SpawnArea> spawnAreas = new ConcurrentHashMap<>();
    private final Map<UUID, LivingEntity> activeMobs = new ConcurrentHashMap<>();

    public AdvancedMobSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;

        initializeMobConfigs();
        startMobUpdateTask();
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void initializeMobConfigs() {
        // Load Hypixel SkyBlock mob definitions
        mobConfigs.putAll(HypixelMobDefinitions.getHypixelMobConfigs());
        mobConfigs.putAll(SlayerMobDefinitions.getSlayerMobConfigs());
        mobConfigs.putAll(WorldBossDefinitions.getWorldBossConfigs());

        // === HUB AREA MOBS ===

        // Graveyard Zombies
        mobConfigs.put(MobType.GRAVEYARD_ZOMBIE, new MobConfig(
            "Graveyard Zombie", "§2Graveyard Zombie", EntityType.ZOMBIE,
            "§7A zombie found in the graveyard area.",
            MobCategory.HUB, MobRarity.COMMON, 25, 5, 0.2,
            Arrays.asList("§7- Basic Attack", "§7- Undead", "§7- Graveyard Spawn"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 2x XP", "§7- 5% Rotten Flesh"),
            Arrays.asList("§7- Graveyard Area", "§7- Hub World")
        ));

        mobConfigs.put(MobType.ZOMBIE_VILLAGER, new MobConfig(
            "Zombie Villager", "§2Zombie Villager", EntityType.ZOMBIE_VILLAGER,
            "§7A zombified villager from the graveyard.",
            MobCategory.HUB, MobRarity.UNCOMMON, 30, 6, 0.18,
            Arrays.asList("§7- Basic Attack", "§7- Undead", "§7- Villager Traits"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 3x XP", "§7- 10% Rotten Flesh"),
            Arrays.asList("§7- Graveyard Area", "§7- Hub World")
        ));

        // Crypt Ghouls
        mobConfigs.put(MobType.CRYPT_GHOUL, new MobConfig(
            "Crypt Ghoul", "§8Crypt Ghoul", EntityType.ZOMBIE,
            "§7A stronger zombie found in the crypts.",
            MobCategory.HUB, MobRarity.UNCOMMON, 50, 8, 0.15,
            Arrays.asList("§7- Strong Attack", "§7- Undead", "§7- Crypt Spawn"),
            Arrays.asList("§7- 2x Rotten Flesh", "§7- 5x XP", "§7- 15% Rotten Flesh"),
            Arrays.asList("§7- Crypt Area", "§7- Hub World")
        ));

        mobConfigs.put(MobType.CRYPT_LURKER, new MobConfig(
            "Crypt Lurker", "§8Crypt Lurker", EntityType.ZOMBIE,
            "§7A stealthy zombie that lurks in the crypts.",
            MobCategory.HUB, MobRarity.RARE, 75, 12, 0.12,
            Arrays.asList("§7- Stealth Attack", "§7- Undead", "§7- Crypt Spawn"),
            Arrays.asList("§7- 3x Rotten Flesh", "§7- 8x XP", "§7- 20% Rotten Flesh"),
            Arrays.asList("§7- Crypt Area", "§7- Hub World")
        ));

        // === PRIVATE ISLAND MOBS ===

        mobConfigs.put(MobType.ZOMBIE, new MobConfig(
            "Zombie", "§2Zombie", EntityType.ZOMBIE,
            "§7A basic undead creature.",
            MobCategory.BASIC, MobRarity.COMMON, 20, 4, 0.23,
            Arrays.asList("§7- Basic Attack", "§7- Undead"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 1x XP"),
            Arrays.asList("§7- Basic Spawn", "§7- Low Light")
        ));

        mobConfigs.put(MobType.SKELETON, new MobConfig(
            "Skeleton", "§fSkeleton", EntityType.SKELETON,
            "§7A ranged undead creature.",
            MobCategory.BASIC, MobRarity.COMMON, 20, 4, 0.25,
            Arrays.asList("§7- Ranged Attack", "§7- Undead"),
            Arrays.asList("§7- 1x Bone", "§7- 1x XP"),
            Arrays.asList("§7- Basic Spawn", "§7- Low Light")
        ));

        mobConfigs.put(MobType.SPIDER, new MobConfig(
            "Spider", "§8Spider", EntityType.SPIDER,
            "§7A climbing arachnid creature.",
            MobCategory.BASIC, MobRarity.COMMON, 16, 2, 0.3,
            Arrays.asList("§7- Climbing", "§7- Poison"),
            Arrays.asList("§7- 1x String", "§7- 1x XP"),
            Arrays.asList("§7- Basic Spawn", "§7- Dark Areas")
        ));

        mobConfigs.put(MobType.CREEPER, new MobConfig(
            "Creeper", "§aCreeper", EntityType.CREEPER,
            "§7An explosive creature.",
            MobCategory.BASIC, MobRarity.COMMON, 20, 0, 0.2,
            Arrays.asList("§7- Explosion", "§7- Stealth"),
            Arrays.asList("§7- 1x Gunpowder", "§7- 1x XP"),
            Arrays.asList("§7- Basic Spawn", "§7- Low Light")
        ));

        // === END DIMENSION MOBS ===

        mobConfigs.put(MobType.ENDERMAN, new MobConfig(
            "Enderman", "§5Enderman", EntityType.ENDERMAN,
            "§7A teleporting end creature.",
            MobCategory.END, MobRarity.UNCOMMON, 40, 7, 0.3,
            Arrays.asList("§7- Teleportation", "§7- End Power"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 5x XP"),
            Arrays.asList("§7- End Spawn", "§7- End Dimension")
        ));

        mobConfigs.put(MobType.ZEALOT, new MobConfig(
            "Zealot", "§5Zealot", EntityType.ENDERMAN,
            "§7A special Enderman that can drop special items.",
            MobCategory.END, MobRarity.RARE, 100, 15, 0.1,
            Arrays.asList("§7- Teleportation", "§7- End Power", "§7- Special Drops"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 10x XP", "§7- 0.1% Special Drop"),
            Arrays.asList("§7- End Spawn", "§7- End Dimension", "§7- Special Spawn")
        ));

        mobConfigs.put(MobType.SPECIAL_ZEALOT, new MobConfig(
            "Special Zealot", "§dSpecial Zealot", EntityType.ENDERMAN,
            "§7A very rare Zealot with guaranteed special drops.",
            MobCategory.END, MobRarity.EPIC, 200, 25, 0.05,
            Arrays.asList("§7- Teleportation", "§7- End Power", "§7- Guaranteed Drops"),
            Arrays.asList("§7- 2x Ender Pearl", "§7- 25x XP", "§7- 100% Special Drop"),
            Arrays.asList("§7- End Spawn", "§7- End Dimension", "§7- Very Rare Spawn")
        ));

        mobConfigs.put(MobType.ENDERMITE, new MobConfig(
            "Endermite", "§5Endermite", EntityType.ENDERMITE,
            "§7A small end creature.",
            MobCategory.END, MobRarity.COMMON, 8, 2, 0.4,
            Arrays.asList("§7- Small Size", "§7- End Power"),
            Arrays.asList("§7- 1x XP"),
            Arrays.asList("§7- End Spawn", "§7- End Dimension")
        ));

        mobConfigs.put(MobType.ENDER_DRAGON, new MobConfig(
            "Ender Dragon", "§5Ender Dragon", EntityType.ENDER_DRAGON,
            "§7The mighty dragon of the End.",
            MobCategory.END, MobRarity.LEGENDARY, 200, 50, 0.01,
            Arrays.asList("§7- Flight", "§7- Dragon Breath", "§7- End Power"),
            Arrays.asList("§7- Dragon Egg", "§7- 100x XP", "§7- Special Rewards"),
            Arrays.asList("§7- End Spawn", "§7- End Dimension", "§7- Boss Spawn")
        ));

        // === NETHER DIMENSION MOBS ===

        mobConfigs.put(MobType.BLAZE, new MobConfig(
            "Blaze", "§6Blaze", EntityType.BLAZE,
            "§7A fire-based creature.",
            MobCategory.NETHER, MobRarity.UNCOMMON, 20, 6, 0.23,
            Arrays.asList("§7- Fire Attack", "§7- Fire Immunity"),
            Arrays.asList("§7- 1x Blaze Rod", "§7- 5x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.MAGMA_CUBE, new MobConfig(
            "Magma Cube", "§6Magma Cube", EntityType.MAGMA_CUBE,
            "§7A bouncing fire creature.",
            MobCategory.NETHER, MobRarity.COMMON, 16, 3, 0.3,
            Arrays.asList("§7- Bouncing", "§7- Fire Immunity", "§7- Size Variation"),
            Arrays.asList("§7- 1x Magma Cream", "§7- 3x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.GHAST, new MobConfig(
            "Ghast", "§fGhast", EntityType.GHAST,
            "§7A floating fire creature.",
            MobCategory.NETHER, MobRarity.UNCOMMON, 10, 5, 0.2,
            Arrays.asList("§7- Flight", "§7- Fireball Attack", "§7- Fire Immunity"),
            Arrays.asList("§7- 1x Ghast Tear", "§7- 5x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.PIGLIN, new MobConfig(
            "Piglin", "§6Piglin", EntityType.PIGLIN,
            "§7A nether humanoid creature.",
            MobCategory.NETHER, MobRarity.COMMON, 16, 5, 0.25,
            Arrays.asList("§7- Gold Attraction", "§7- Nether Spawn"),
            Arrays.asList("§7- 1x Gold Nugget", "§7- 3x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.PIGLIN_BRUTE, new MobConfig(
            "Piglin Brute", "§cPiglin Brute", EntityType.PIGLIN_BRUTE,
            "§7A stronger, more aggressive Piglin.",
            MobCategory.NETHER, MobRarity.UNCOMMON, 50, 8, 0.15,
            Arrays.asList("§7- Strong Attack", "§7- Gold Attraction", "§7- Aggressive"),
            Arrays.asList("§7- 2x Gold Nugget", "§7- 8x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.HOGLIN, new MobConfig(
            "Hoglin", "§6Hoglin", EntityType.HOGLIN,
            "§7A large nether beast.",
            MobCategory.NETHER, MobRarity.UNCOMMON, 40, 9, 0.2,
            Arrays.asList("§7- Charge Attack", "§7- Nether Spawn"),
            Arrays.asList("§7- 1x Raw Porkchop", "§7- 6x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.ZOGLIN, new MobConfig(
            "Zoglin", "§cZoglin", EntityType.ZOGLIN,
            "§7An undead nether beast.",
            MobCategory.NETHER, MobRarity.RARE, 60, 12, 0.12,
            Arrays.asList("§7- Charge Attack", "§7- Undead", "§7- Aggressive"),
            Arrays.asList("§7- 2x Raw Porkchop", "§7- 10x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.STRIDER, new MobConfig(
            "Strider", "§6Strider", EntityType.STRIDER,
            "§7A lava-walking creature.",
            MobCategory.NETHER, MobRarity.COMMON, 20, 3, 0.3,
            Arrays.asList("§7- Lava Walking", "§7- Nether Spawn"),
            Arrays.asList("§7- 1x String", "§7- 2x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.WITHER_SKELETON, new MobConfig(
            "Wither Skeleton", "§8Wither Skeleton", EntityType.WITHER_SKELETON,
            "§7A dark skeleton with wither powers.",
            MobCategory.NETHER, MobRarity.UNCOMMON, 20, 8, 0.2,
            Arrays.asList("§7- Wither Effect", "§7- Undead", "§7- Nether Spawn"),
            Arrays.asList("§7- 1x Wither Skeleton Skull", "§7- 8x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));


        // Slayer Mobs
        mobConfigs.put(MobType.REVENANT_HORROR_I, new MobConfig(
            "Revenant Horror", "§2Revenant Horror", EntityType.ZOMBIE,
            "§7A powerful zombie slayer boss.",
            MobCategory.SLAYER, MobRarity.EPIC, 1000, 50, 0.2,
            Arrays.asList("§7- Zombie Slayer", "§7- Undead Power"),
            Arrays.asList("§7- 1x Revenant Flesh", "§7- 100x XP"),
            Arrays.asList("§7- Slayer Spawn", "§7- Slayer Area")
        ));

        mobConfigs.put(MobType.TARANTULA_BROODFATHER_I, new MobConfig(
            "Tarantula Broodfather", "§8Tarantula Broodfather", EntityType.SPIDER,
            "§7A powerful spider slayer boss.",
            MobCategory.SLAYER, MobRarity.EPIC, 1000, 50, 0.3,
            Arrays.asList("§7- Spider Slayer", "§7- Web Attack"),
            Arrays.asList("§7- 1x Tarantula Silk", "§7- 100x XP"),
            Arrays.asList("§7- Slayer Spawn", "§7- Slayer Area")
        ));

        mobConfigs.put(MobType.SVEN_PACKMASTER_I, new MobConfig(
            "Sven Packmaster", "§fSven Packmaster", EntityType.WOLF,
            "§7A powerful wolf slayer boss.",
            MobCategory.SLAYER, MobRarity.EPIC, 1000, 50, 0.3,
            Arrays.asList("§7- Wolf Slayer", "§7- Pack Power"),
            Arrays.asList("§7- 1x Sven Fur", "§7- 100x XP"),
            Arrays.asList("§7- Slayer Spawn", "§7- Slayer Area")
        ));

        mobConfigs.put(MobType.VOIDGLOOM_SERAPH_I, new MobConfig(
            "Voidgloom Seraph", "§5Voidgloom Seraph", EntityType.ENDERMAN,
            "§7A powerful enderman slayer boss.",
            MobCategory.SLAYER, MobRarity.EPIC, 1000, 50, 0.3,
            Arrays.asList("§7- Enderman Slayer", "§7- Void Power"),
            Arrays.asList("§7- 1x Void Fragment", "§7- 100x XP"),
            Arrays.asList("§7- Slayer Spawn", "§7- Slayer Area")
        ));

        mobConfigs.put(MobType.INFERNO_DEMONLORD, new MobConfig(
            "Inferno Demonlord", "§6Inferno Demonlord", EntityType.BLAZE,
            "§7A powerful blaze slayer boss.",
            MobCategory.SLAYER, MobRarity.EPIC, 1000, 50, 0.3,
            Arrays.asList("§7- Blaze Slayer", "§7- Inferno Power"),
            Arrays.asList("§7- 1x Inferno Fragment", "§7- 100x XP"),
            Arrays.asList("§7- Slayer Spawn", "§7- Slayer Area")
        ));

        // Dungeon Mobs
        mobConfigs.put(MobType.DUNGEON_ZOMBIE, new MobConfig(
            "Dungeon Zombie", "§2Dungeon Zombie", EntityType.ZOMBIE,
            "§7A powerful dungeon zombie.",
            MobCategory.DUNGEON, MobRarity.RARE, 100, 20, 0.23,
            Arrays.asList("§7- Dungeon Power", "§7- Undead"),
            Arrays.asList("§7- 1x Dungeon Fragment", "§7- 20x XP"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Area")
        ));

        mobConfigs.put(MobType.DUNGEON_SKELETON, new MobConfig(
            "Dungeon Skeleton", "§fDungeon Skeleton", EntityType.SKELETON,
            "§7A powerful dungeon skeleton.",
            MobCategory.DUNGEON, MobRarity.RARE, 100, 20, 0.25,
            Arrays.asList("§7- Dungeon Power", "§7- Ranged Attack"),
            Arrays.asList("§7- 1x Dungeon Fragment", "§7- 20x XP"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Area")
        ));

        mobConfigs.put(MobType.DUNGEON_SPIDER, new MobConfig(
            "Dungeon Spider", "§8Dungeon Spider", EntityType.SPIDER,
            "§7A powerful dungeon spider.",
            MobCategory.DUNGEON, MobRarity.RARE, 100, 20, 0.3,
            Arrays.asList("§7- Dungeon Power", "§7- Climbing"),
            Arrays.asList("§7- 1x Dungeon Fragment", "§7- 20x XP"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Area")
        ));

        mobConfigs.put(MobType.DUNGEON_ENDERMAN, new MobConfig(
            "Dungeon Enderman", "§5Dungeon Enderman", EntityType.ENDERMAN,
            "§7A powerful dungeon enderman.",
            MobCategory.DUNGEON, MobRarity.RARE, 100, 20, 0.3,
            Arrays.asList("§7- Dungeon Power", "§7- Teleportation"),
            Arrays.asList("§7- 1x Dungeon Fragment", "§7- 20x XP"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Area")
        ));

        mobConfigs.put(MobType.DUNGEON_BLAZE, new MobConfig(
            "Dungeon Blaze", "§6Dungeon Blaze", EntityType.BLAZE,
            "§7A powerful dungeon blaze.",
            MobCategory.DUNGEON, MobRarity.RARE, 100, 20, 0.23,
            Arrays.asList("§7- Dungeon Power", "§7- Fire Attack"),
            Arrays.asList("§7- 1x Dungeon Fragment", "§7- 20x XP"),
            Arrays.asList("§7- Dungeon Spawn", "§7- Dungeon Area")
        ));

        // Special Mobs
        mobConfigs.put(MobType.GOLEM, new MobConfig(
            "Golem", "§7Golem", EntityType.IRON_GOLEM,
            "§7A powerful construct.",
            MobCategory.SPECIAL, MobRarity.RARE, 100, 15, 0.25,
            Arrays.asList("§7- High Health", "§7- Strong Attack"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 10x XP"),
            Arrays.asList("§7- Village Spawn", "§7- Village Area")
        ));

        mobConfigs.put(MobType.WITHER, new MobConfig(
            "Wither", "§8Wither", EntityType.WITHER,
            "§7A powerful undead boss.",
            MobCategory.BOSS, MobRarity.LEGENDARY, 300, 8, 0.2,
            Arrays.asList("§7- Wither Effect", "§7- Flight"),
            Arrays.asList("§7- 1x Nether Star", "§7- 50x XP"),
            Arrays.asList("§7- Boss Spawn", "§7- Boss Area")
        ));

        mobConfigs.put(MobType.ENDER_DRAGON, new MobConfig(
            "Ender Dragon", "§5Ender Dragon", EntityType.ENDER_DRAGON,
            "§7The ultimate end boss.",
            MobCategory.BOSS, MobRarity.LEGENDARY, 200, 10, 0.3,
            Arrays.asList("§7- Dragon Breath", "§7- Flight"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 100x XP"),
            Arrays.asList("§7- End Spawn", "§7- End Dimension")
        ));

        // Additional Basic Mobs
        mobConfigs.put(MobType.PIGLIN_BRUTE, new MobConfig(
            "Piglin Brute", "§6Piglin Brute", EntityType.PIGLIN_BRUTE,
            "§7A powerful piglin warrior.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 30, 8, 0.25,
            Arrays.asList("§7- Brute Strength", "§7- Gold Love"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 8x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Bastion")
        ));

        mobConfigs.put(MobType.ZOMBIFIED_PIGLIN, new MobConfig(
            "Zombified Piglin", "§2Zombified Piglin", EntityType.ZOMBIFIED_PIGLIN,
            "§7A zombified piglin from the nether.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 25, 6, 0.23,
            Arrays.asList("§7- Undead", "§7- Nether Power"),
            Arrays.asList("§7- 1x Gold Nugget", "§7- 6x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.HOGLIN, new MobConfig(
            "Hoglin", "§cHoglin", EntityType.HOGLIN,
            "§7A large pig-like creature from the nether.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 40, 7, 0.3,
            Arrays.asList("§7- Charge Attack", "§7- Nether Power"),
            Arrays.asList("§7- 1x Leather", "§7- 7x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Crimson Forest")
        ));

        mobConfigs.put(MobType.ZOGLIN, new MobConfig(
            "Zoglin", "§4Zoglin", EntityType.ZOGLIN,
            "§7A zombified hoglin.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 35, 8, 0.28,
            Arrays.asList("§7- Undead", "§7- Charge Attack"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 8x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.STRIDER, new MobConfig(
            "Strider", "§5Strider", EntityType.STRIDER,
            "§7A creature that walks on lava.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 20, 3, 0.2,
            Arrays.asList("§7- Lava Walking", "§7- Fire Immunity"),
            Arrays.asList("§7- 1x String", "§7- 5x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Lava Lakes")
        ));

        mobConfigs.put(MobType.ENDERMITE, new MobConfig(
            "Endermite", "§5Endermite", EntityType.ENDERMITE,
            "§7A small end creature.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 8, 2, 0.3,
            Arrays.asList("§7- End Power", "§7- Small Size"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 3x XP"),
            Arrays.asList("§7- End Spawn", "§7- End Dimension")
        ));

        mobConfigs.put(MobType.SHULKER, new MobConfig(
            "Shulker", "§dShulker", EntityType.SHULKER,
            "§7A floating end creature with a shell.",
            MobCategory.ADVANCED, MobRarity.RARE, 30, 4, 0.0,
            Arrays.asList("§7- Levitation", "§7- Shell Protection"),
            Arrays.asList("§7- 1x Shulker Shell", "§7- 10x XP"),
            Arrays.asList("§7- End Spawn", "§7- End City")
        ));

        mobConfigs.put(MobType.VEX, new MobConfig(
            "Vex", "§fVex", EntityType.VEX,
            "§7A small flying creature.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 14, 4, 0.0,
            Arrays.asList("§7- Flight", "§7- Phasing"),
            Arrays.asList("§7- 1x Iron Sword", "§7- 5x XP"),
            Arrays.asList("§7- Mansion Spawn", "§7- Woodland Mansion")
        ));

        mobConfigs.put(MobType.EVOKER, new MobConfig(
            "Evoker", "§6Evoker", EntityType.EVOKER,
            "§7A powerful magic user.",
            MobCategory.ADVANCED, MobRarity.RARE, 24, 6, 0.25,
            Arrays.asList("§7- Magic Spells", "§7- Vex Summoning"),
            Arrays.asList("§7- 1x Totem of Undying", "§7- 10x XP"),
            Arrays.asList("§7- Mansion Spawn", "§7- Woodland Mansion")
        ));

        mobConfigs.put(MobType.VINDICATOR, new MobConfig(
            "Vindicator", "§7Vindicator", EntityType.VINDICATOR,
            "§7A hostile villager with an axe.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 24, 7, 0.25,
            Arrays.asList("§7- Axe Attack", "§7- Vindication"),
            Arrays.asList("§7- 1x Iron Axe", "§7- 8x XP"),
            Arrays.asList("§7- Mansion Spawn", "§7- Woodland Mansion")
        ));

        mobConfigs.put(MobType.PILLAGER, new MobConfig(
            "Pillager", "§8Pillager", EntityType.PILLAGER,
            "§7A hostile villager with a crossbow.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 24, 5, 0.25,
            Arrays.asList("§7- Crossbow Attack", "§7- Bad Omen"),
            Arrays.asList("§7- 1x Crossbow", "§7- 8x XP"),
            Arrays.asList("§7- Pillage Spawn", "§7- Pillage Outpost")
        ));

        mobConfigs.put(MobType.RAVAGER, new MobConfig(
            "Ravager", "§4Ravager", EntityType.RAVAGER,
            "§7A large beast ridden by pillagers.",
            MobCategory.ADVANCED, MobRarity.RARE, 100, 12, 0.2,
            Arrays.asList("§7- Charge Attack", "§7- Roar"),
            Arrays.asList("§7- 1x Saddle", "§7- 20x XP"),
            Arrays.asList("§7- Pillage Spawn", "§7- Pillage Outpost")
        ));

        mobConfigs.put(MobType.WITCH, new MobConfig(
            "Witch", "§5Witch", EntityType.WITCH,
            "§7A magic user that throws potions.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 26, 6, 0.25,
            Arrays.asList("§7- Potion Throwing", "§7- Magic"),
            Arrays.asList("§7- 1x Potion", "§7- 8x XP"),
            Arrays.asList("§7- Swamp Spawn", "§7- Witch Hut")
        ));

        mobConfigs.put(MobType.PHANTOM, new MobConfig(
            "Phantom", "§8Phantom", EntityType.PHANTOM,
            "§7A flying undead creature.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 20, 6, 0.0,
            Arrays.asList("§7- Flight", "§7- Undead"),
            Arrays.asList("§7- 1x Phantom Membrane", "§7- 8x XP"),
            Arrays.asList("§7- Night Spawn", "§7- Overworld")
        ));

        mobConfigs.put(MobType.DROWNED, new MobConfig(
            "Drowned", "§1Drowned", EntityType.DROWNED,
            "§7A zombie that lives underwater.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 20, 4, 0.23,
            Arrays.asList("§7- Water Breathing", "§7- Trident Attack"),
            Arrays.asList("§7- 1x Copper Ingot", "§7- 6x XP"),
            Arrays.asList("§7- Water Spawn", "§7- Ocean")
        ));

        mobConfigs.put(MobType.HUSK, new MobConfig(
            "Husk", "§eHusk", EntityType.HUSK,
            "§7A zombie that lives in deserts.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 20, 4, 0.23,
            Arrays.asList("§7- Desert Adaptation", "§7- Undead"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 6x XP"),
            Arrays.asList("§7- Desert Spawn", "§7- Desert")
        ));

        mobConfigs.put(MobType.STRAY, new MobConfig(
            "Stray", "§fStray", EntityType.STRAY,
            "§7A skeleton that lives in cold biomes.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 20, 4, 0.25,
            Arrays.asList("§7- Cold Adaptation", "§7- Slowness Arrow"),
            Arrays.asList("§7- 1x Arrow", "§7- 6x XP"),
            Arrays.asList("§7- Cold Spawn", "§7- Snow Biome")
        ));

        mobConfigs.put(MobType.SLIME, new MobConfig(
            "Slime", "§aSlime", EntityType.SLIME,
            "§7A bouncy green creature.",
            MobCategory.BASIC, MobRarity.COMMON, 16, 4, 0.2,
            Arrays.asList("§7- Bouncing", "§7- Size Variation"),
            Arrays.asList("§7- 1x Slime Ball", "§7- 4x XP"),
            Arrays.asList("§7- Swamp Spawn", "§7- Swamp")
        ));

        mobConfigs.put(MobType.CAVE_SPIDER, new MobConfig(
            "Cave Spider", "§8Cave Spider", EntityType.CAVE_SPIDER,
            "§7A smaller, poisonous spider.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 12, 2, 0.3,
            Arrays.asList("§7- Poison", "§7- Climbing"),
            Arrays.asList("§7- 1x String", "§7- 5x XP"),
            Arrays.asList("§7- Cave Spawn", "§7- Mineshaft")
        ));

        mobConfigs.put(MobType.SILVERFISH, new MobConfig(
            "Silverfish", "§7Silverfish", EntityType.SILVERFISH,
            "§7A small insect that hides in stone.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 8, 1, 0.25,
            Arrays.asList("§7- Hiding", "§7- Swarming"),
            Arrays.asList("§7- 1x Stone", "§7- 3x XP"),
            Arrays.asList("§7- Stone Spawn", "§7- Stronghold")
        ));

        // Additional Special Mobs
        mobConfigs.put(MobType.IRON_GOLEM, new MobConfig(
            "Iron Golem", "§7Iron Golem", EntityType.IRON_GOLEM,
            "§7A powerful construct made of iron.",
            MobCategory.SPECIAL, MobRarity.RARE, 100, 15, 0.25,
            Arrays.asList("§7- High Health", "§7- Strong Attack", "§7- Village Protection"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 10x XP"),
            Arrays.asList("§7- Village Spawn", "§7- Village Area")
        ));

        mobConfigs.put(MobType.SNOW_GOLEM, new MobConfig(
            "Snow Golem", "§fSnow Golem", EntityType.SNOW_GOLEM,
            "§7A friendly construct made of snow.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 4, 0, 0.2,
            Arrays.asList("§7- Snowball Attack", "§7- Friendly"),
            Arrays.asList("§7- 1x Snowball", "§7- 2x XP"),
            Arrays.asList("§7- Player Spawn", "§7- Snow Biome")
        ));

        mobConfigs.put(MobType.VILLAGER, new MobConfig(
            "Villager", "§eVillager", EntityType.VILLAGER,
            "§7A friendly villager that trades items.",
            MobCategory.SPECIAL, MobRarity.COMMON, 20, 0, 0.2,
            Arrays.asList("§7- Trading", "§7- Friendly"),
            Arrays.asList("§7- 1x Emerald", "§7- 1x XP"),
            Arrays.asList("§7- Village Spawn", "§7- Village Area")
        ));

        mobConfigs.put(MobType.WANDERING_TRADER, new MobConfig(
            "Wandering Trader", "§6Wandering Trader", EntityType.WANDERING_TRADER,
            "§7A trader that wanders the world with llamas.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 20, 0, 0.2,
            Arrays.asList("§7- Trading", "§7- Wandering", "§7- Llamas"),
            Arrays.asList("§7- 1x Emerald", "§7- 3x XP"),
            Arrays.asList("§7- Random Spawn", "§7- Overworld")
        ));

        mobConfigs.put(MobType.CAT, new MobConfig(
            "Cat", "§eCat", EntityType.CAT,
            "§7A friendly cat that can be tamed.",
            MobCategory.SPECIAL, MobRarity.COMMON, 10, 2, 0.3,
            Arrays.asList("§7- Taming", "§7- Friendly", "§7- Creeper Scaring"),
            Arrays.asList("§7- 1x String", "§7- 1x XP"),
            Arrays.asList("§7- Village Spawn", "§7- Village Area")
        ));

        mobConfigs.put(MobType.WOLF, new MobConfig(
            "Wolf", "§7Wolf", EntityType.WOLF,
            "§7A wild wolf that can be tamed.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 20, 4, 0.3,
            Arrays.asList("§7- Taming", "§7- Pack Behavior", "§7- Howling"),
            Arrays.asList("§7- 1x Bone", "§7- 2x XP"),
            Arrays.asList("§7- Forest Spawn", "§7- Taiga Biome")
        ));

        mobConfigs.put(MobType.OCELOT, new MobConfig(
            "Ocelot", "§6Ocelot", EntityType.OCELOT,
            "§7A wild ocelot that can be tamed into a cat.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 10, 2, 0.3,
            Arrays.asList("§7- Taming", "§7- Stealth", "§7- Cat Transformation"),
            Arrays.asList("§7- 1x Fish", "§7- 2x XP"),
            Arrays.asList("§7- Jungle Spawn", "§7- Jungle Biome")
        ));

        mobConfigs.put(MobType.HORSE, new MobConfig(
            "Horse", "§6Horse", EntityType.HORSE,
            "§7A wild horse that can be tamed and ridden.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 22, 0, 0.2,
            Arrays.asList("§7- Taming", "§7- Riding", "§7- Speed Variation"),
            Arrays.asList("§7- 1x Leather", "§7- 3x XP"),
            Arrays.asList("§7- Plains Spawn", "§7- Plains Biome")
        ));

        mobConfigs.put(MobType.DONKEY, new MobConfig(
            "Donkey", "§7Donkey", EntityType.DONKEY,
            "§7A donkey that can be tamed and ridden.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 22, 0, 0.175,
            Arrays.asList("§7- Taming", "§7- Riding", "§7- Chest Carrying"),
            Arrays.asList("§7- 1x Leather", "§7- 3x XP"),
            Arrays.asList("§7- Plains Spawn", "§7- Plains Biome")
        ));

        mobConfigs.put(MobType.MULE, new MobConfig(
            "Mule", "§7Mule", EntityType.MULE,
            "§7A mule that can be tamed and ridden.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 22, 0, 0.175,
            Arrays.asList("§7- Taming", "§7- Riding", "§7- Chest Carrying"),
            Arrays.asList("§7- 1x Leather", "§7- 3x XP"),
            Arrays.asList("§7- Plains Spawn", "§7- Plains Biome")
        ));

        mobConfigs.put(MobType.LLAMA, new MobConfig(
            "Llama", "§eLlama", EntityType.LLAMA,
            "§7A llama that can be tamed and ridden.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 15, 1, 0.175,
            Arrays.asList("§7- Taming", "§7- Riding", "§7- Spitting"),
            Arrays.asList("§7- 1x Leather", "§7- 3x XP"),
            Arrays.asList("§7- Mountain Spawn", "§7- Savanna Biome")
        ));

        mobConfigs.put(MobType.PARROT, new MobConfig(
            "Parrot", "§aParrot", EntityType.PARROT,
            "§7A colorful parrot that can be tamed.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 6, 0, 0.2,
            Arrays.asList("§7- Taming", "§7- Flying", "§7- Mimicking"),
            Arrays.asList("§7- 1x Seed", "§7- 1x XP"),
            Arrays.asList("§7- Jungle Spawn", "§7- Jungle Biome")
        ));

        mobConfigs.put(MobType.POLAR_BEAR, new MobConfig(
            "Polar Bear", "§fPolar Bear", EntityType.POLAR_BEAR,
            "§7A large bear that lives in cold biomes.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 30, 6, 0.25,
            Arrays.asList("§7- Strong Attack", "§7- Cold Resistance", "§7- Swimming"),
            Arrays.asList("§7- 1x Fish", "§7- 5x XP"),
            Arrays.asList("§7- Cold Spawn", "§7- Ice Spikes Biome")
        ));

        mobConfigs.put(MobType.PANDA, new MobConfig(
            "Panda", "§fPanda", EntityType.PANDA,
            "§7A cute panda that lives in bamboo forests.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 20, 4, 0.2,
            Arrays.asList("§7- Bamboo Eating", "§7- Lazy", "§7- Rolling"),
            Arrays.asList("§7- 1x Bamboo", "§7- 3x XP"),
            Arrays.asList("§7- Bamboo Spawn", "§7- Bamboo Jungle")
        ));

        mobConfigs.put(MobType.FOX, new MobConfig(
            "Fox", "§6Fox", EntityType.FOX,
            "§7A clever fox that lives in taiga biomes.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 10, 2, 0.3,
            Arrays.asList("§7- Stealth", "§7- Item Stealing", "§7- Sleeping"),
            Arrays.asList("§7- 1x Sweet Berries", "§7- 2x XP"),
            Arrays.asList("§7- Taiga Spawn", "§7- Taiga Biome")
        ));

        mobConfigs.put(MobType.BEE, new MobConfig(
            "Bee", "§eBee", EntityType.BEE,
            "§7A bee that pollinates flowers and makes honey.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 10, 2, 0.3,
            Arrays.asList("§7- Pollination", "§7- Honey Making", "§7- Stinging"),
            Arrays.asList("§7- 1x Honey", "§7- 2x XP"),
            Arrays.asList("§7- Flower Spawn", "§7- Flower Forest")
        ));

        mobConfigs.put(MobType.DOLPHIN, new MobConfig(
            "Dolphin", "§bDolphin", EntityType.DOLPHIN,
            "§7A friendly dolphin that lives in oceans.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 10, 0, 0.3,
            Arrays.asList("§7- Swimming", "§7- Friendly", "§7- Treasure Finding"),
            Arrays.asList("§7- 1x Fish", "§7- 3x XP"),
            Arrays.asList("§7- Ocean Spawn", "§7- Ocean Biome")
        ));

        mobConfigs.put(MobType.TURTLE, new MobConfig(
            "Turtle", "§aTurtle", EntityType.TURTLE,
            "§7A slow turtle that lives on beaches.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 30, 0, 0.1,
            Arrays.asList("§7- Swimming", "§7- Shell Protection", "§7- Egg Laying"),
            Arrays.asList("§7- 1x Seagrass", "§7- 2x XP"),
            Arrays.asList("§7- Beach Spawn", "§7- Beach Biome")
        ));

        mobConfigs.put(MobType.COD, new MobConfig(
            "Cod", "§7Cod", EntityType.COD,
            "§7A common fish that lives in oceans.",
            MobCategory.SPECIAL, MobRarity.COMMON, 3, 0, 0.2,
            Arrays.asList("§7- Swimming", "§7- Schooling"),
            Arrays.asList("§7- 1x Raw Cod", "§7- 1x XP"),
            Arrays.asList("§7- Ocean Spawn", "§7- Ocean Biome")
        ));

        mobConfigs.put(MobType.SALMON, new MobConfig(
            "Salmon", "§cSalmon", EntityType.SALMON,
            "§7A salmon that lives in cold waters.",
            MobCategory.SPECIAL, MobRarity.COMMON, 3, 0, 0.2,
            Arrays.asList("§7- Swimming", "§7- Migration"),
            Arrays.asList("§7- 1x Raw Salmon", "§7- 1x XP"),
            Arrays.asList("§7- Cold Ocean Spawn", "§7- Cold Ocean")
        ));

        mobConfigs.put(MobType.TROPICAL_FISH, new MobConfig(
            "Tropical Fish", "§dTropical Fish", EntityType.TROPICAL_FISH,
            "§7A colorful tropical fish that lives in warm waters.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 3, 0, 0.2,
            Arrays.asList("§7- Swimming", "§7- Colorful", "§7- Variety"),
            Arrays.asList("§7- 1x Tropical Fish", "§7- 1x XP"),
            Arrays.asList("§7- Warm Ocean Spawn", "§7- Warm Ocean")
        ));

        mobConfigs.put(MobType.PUFFERFISH, new MobConfig(
            "Pufferfish", "§ePufferfish", EntityType.PUFFERFISH,
            "§7A pufferfish that inflates when threatened.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 3, 0, 0.2,
            Arrays.asList("§7- Swimming", "§7- Inflation", "§7- Poison"),
            Arrays.asList("§7- 1x Pufferfish", "§7- 1x XP"),
            Arrays.asList("§7- Warm Ocean Spawn", "§7- Warm Ocean")
        ));

        mobConfigs.put(MobType.SQUID, new MobConfig(
            "Squid", "§8Squid", EntityType.SQUID,
            "§7A squid that lives in oceans and releases ink.",
            MobCategory.SPECIAL, MobRarity.COMMON, 10, 0, 0.2,
            Arrays.asList("§7- Swimming", "§7- Ink Release", "§7- Tentacles"),
            Arrays.asList("§7- 1x Ink Sac", "§7- 1x XP"),
            Arrays.asList("§7- Ocean Spawn", "§7- Ocean Biome")
        ));

        mobConfigs.put(MobType.GUARDIAN, new MobConfig(
            "Guardian", "§bGuardian", EntityType.GUARDIAN,
            "§7A guardian that protects ocean monuments.",
            MobCategory.ADVANCED, MobRarity.RARE, 30, 6, 0.2,
            Arrays.asList("§7- Laser Attack", "§7- Swimming", "§7- Monument Protection"),
            Arrays.asList("§7- 1x Prismarine Shard", "§7- 10x XP"),
            Arrays.asList("§7- Monument Spawn", "§7- Ocean Monument")
        ));

        mobConfigs.put(MobType.ELDER_GUARDIAN, new MobConfig(
            "Elder Guardian", "§5Elder Guardian", EntityType.ELDER_GUARDIAN,
            "§7An elder guardian that protects ocean monuments.",
            MobCategory.BOSS, MobRarity.LEGENDARY, 80, 8, 0.2,
            Arrays.asList("§7- Laser Attack", "§7- Mining Fatigue", "§7- Monument Protection"),
            Arrays.asList("§7- 1x Prismarine Crystal", "§7- 50x XP"),
            Arrays.asList("§7- Monument Spawn", "§7- Ocean Monument")
        ));

        mobConfigs.put(MobType.BAT, new MobConfig(
            "Bat", "§8Bat", EntityType.BAT,
            "§7A small bat that hangs upside down in caves.",
            MobCategory.SPECIAL, MobRarity.COMMON, 6, 0, 0.3,
            Arrays.asList("§7- Flying", "§7- Hanging", "§7- Echolocation"),
            Arrays.asList("§7- 1x Bat Wing", "§7- 1x XP"),
            Arrays.asList("§7- Cave Spawn", "§7- Dark Areas")
        ));

        mobConfigs.put(MobType.CHICKEN, new MobConfig(
            "Chicken", "§fChicken", EntityType.CHICKEN,
            "§7A chicken that lays eggs and provides feathers.",
            MobCategory.SPECIAL, MobRarity.COMMON, 4, 0, 0.2,
            Arrays.asList("§7- Egg Laying", "§7- Feather Dropping", "§7- Flying"),
            Arrays.asList("§7- 1x Feather", "§7- 1x XP"),
            Arrays.asList("§7- Grass Spawn", "§7- Overworld")
        ));

        mobConfigs.put(MobType.COW, new MobConfig(
            "Cow", "§7Cow", EntityType.COW,
            "§7A cow that provides milk and leather.",
            MobCategory.SPECIAL, MobRarity.COMMON, 10, 0, 0.2,
            Arrays.asList("§7- Milk Production", "§7- Leather Dropping", "§7- Grass Eating"),
            Arrays.asList("§7- 1x Leather", "§7- 1x XP"),
            Arrays.asList("§7- Grass Spawn", "§7- Overworld")
        ));

        mobConfigs.put(MobType.MUSHROOM_COW, new MobConfig(
            "Mushroom Cow", "§dMushroom Cow", EntityType.COW,
            "§7A mushroom cow that provides mushroom stew.",
            MobCategory.SPECIAL, MobRarity.UNCOMMON, 10, 0, 0.2,
            Arrays.asList("§7- Mushroom Stew", "§7- Mushroom Growth", "§7- Mushroom Biome"),
            Arrays.asList("§7- 1x Mushroom Stew", "§7- 2x XP"),
            Arrays.asList("§7- Mushroom Spawn", "§7- Mushroom Island")
        ));

        mobConfigs.put(MobType.PIG, new MobConfig(
            "Pig", "§dPig", EntityType.PIG,
            "§7A pig that can be ridden with a saddle.",
            MobCategory.SPECIAL, MobRarity.COMMON, 10, 0, 0.2,
            Arrays.asList("§7- Riding", "§7- Carrot Following", "§7- Oinking"),
            Arrays.asList("§7- 1x Porkchop", "§7- 1x XP"),
            Arrays.asList("§7- Grass Spawn", "§7- Overworld")
        ));

        mobConfigs.put(MobType.RABBIT, new MobConfig(
            "Rabbit", "§fRabbit", EntityType.RABBIT,
            "§7A small rabbit that hops around quickly.",
            MobCategory.SPECIAL, MobRarity.COMMON, 3, 0, 0.3,
            Arrays.asList("§7- Hopping", "§7- Carrot Eating", "§7- Fast Movement"),
            Arrays.asList("§7- 1x Rabbit Hide", "§7- 1x XP"),
            Arrays.asList("§7- Grass Spawn", "§7- Overworld")
        ));

        mobConfigs.put(MobType.SHEEP, new MobConfig(
            "Sheep", "§fSheep", EntityType.SHEEP,
            "§7A sheep that provides wool and can be dyed.",
            MobCategory.SPECIAL, MobRarity.COMMON, 8, 0, 0.2,
            Arrays.asList("§7- Wool Production", "§7- Dyeing", "§7- Grass Eating"),
            Arrays.asList("§7- 1x Wool", "§7- 1x XP"),
            Arrays.asList("§7- Grass Spawn", "§7- Overworld")
        ));

        mobConfigs.put(MobType.ZOMBIE_VILLAGER, new MobConfig(
            "Zombie Villager", "§2Zombie Villager", EntityType.ZOMBIE_VILLAGER,
            "§7A zombified villager that can be cured.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 20, 4, 0.23,
            Arrays.asList("§7- Curing", "§7- Undead", "§7- Villager Conversion"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 5x XP"),
            Arrays.asList("§7- Village Spawn", "§7- Village Area")
        ));

        mobConfigs.put(MobType.ZOMBIE_HORSE, new MobConfig(
            "Zombie Horse", "§2Zombie Horse", EntityType.ZOMBIE_HORSE,
            "§7A zombified horse that can be ridden.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 22, 0, 0.2,
            Arrays.asList("§7- Riding", "§7- Undead", "§7- Undead Speed"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 5x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.SKELETON_HORSE, new MobConfig(
            "Skeleton Horse", "§fSkeleton Horse", EntityType.SKELETON_HORSE,
            "§7A skeletal horse that can be ridden.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 22, 0, 0.2,
            Arrays.asList("§7- Riding", "§7- Undead", "§7- Skeletal Speed"),
            Arrays.asList("§7- 1x Bone", "§7- 5x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));

        mobConfigs.put(MobType.ZOMBIE_PIGMAN, new MobConfig(
            "Zombie Pigman", "§6Zombie Pigman", EntityType.ZOMBIFIED_PIGLIN,
            "§7A zombified piglin from the nether.",
            MobCategory.ADVANCED, MobRarity.UNCOMMON, 25, 6, 0.23,
            Arrays.asList("§7- Undead", "§7- Nether Power", "§7- Gold Love"),
            Arrays.asList("§7- 1x Gold Nugget", "§7- 6x XP"),
            Arrays.asList("§7- Nether Spawn", "§7- Nether Dimension")
        ));
    }

    private void startMobUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    updateAllPlayerMobData();
                    updateSpawnAreas();
                    Thread.sleep(20L * 60L * 50); // Update every minute = 60,000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void updateAllPlayerMobData() {
        for (PlayerMobData data : playerMobData.values()) {
            data.update();
        }
    }

    private void updateSpawnAreas() {
        for (SpawnArea area : spawnAreas.values()) {
            area.update();
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            UUID entityId = entity.getUniqueId();

            // Check if this is a custom mob
            for (MobType type : mobConfigs.keySet()) {
                if (entity.getType() == mobConfigs.get(type).getEntityType()) {
                    activeMobs.put(entityId, entity);
                    applyMobAbilities(entity, type);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        UUID entityId = entity.getUniqueId();

        if (activeMobs.containsKey(entityId)) {
            activeMobs.remove(entityId);

            // Give rewards to killer
            if (entity.getKiller() != null) {
                Player killer = entity.getKiller();
                giveMobRewards(killer, entity);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        String displayName = meta.displayName() != null ?
            net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(meta.displayName()) : "";

        if (displayName.contains("Mob")) {
            openMobGUI(player);
        }
    }

    public void openMobGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§c§lMob System"));

        // Add mob categories
        addGUIItem(gui, 10, Material.ZOMBIE_HEAD, "§2§lBasic Mobs", "§7View basic mobs.");
        addGUIItem(gui, 11, Material.SKELETON_SKULL, "§f§lAdvanced Mobs", "§7View advanced mobs.");
        addGUIItem(gui, 12, Material.WITHER_SKELETON_SKULL, "§8§lSlayer Mobs", "§7View slayer mobs.");
        addGUIItem(gui, 13, Material.STONE_BRICKS, "§7§lDungeon Mobs", "§7View dungeon mobs.");
        addGUIItem(gui, 14, Material.NETHER_STAR, "§6§lSpecial Mobs", "§7View special mobs.");

        // Add specific mobs
        addGUIItem(gui, 19, Material.ZOMBIE_HEAD, "§2§lZombie", "§7Basic undead creature.");
        addGUIItem(gui, 20, Material.SKELETON_SKULL, "§f§lSkeleton", "§7Ranged undead creature.");
        addGUIItem(gui, 21, Material.SPIDER_EYE, "§8§lSpider", "§7Climbing arachnid creature.");
        addGUIItem(gui, 22, Material.GUNPOWDER, "§a§lCreeper", "§7Explosive creature.");
        addGUIItem(gui, 23, Material.ENDER_PEARL, "§5§lEnderman", "§7Teleporting end creature.");
        addGUIItem(gui, 24, Material.BLAZE_ROD, "§6§lBlaze", "§7Fire-based creature.");
        addGUIItem(gui, 25, Material.WITHER_SKELETON_SKULL, "§8§lWither Skeleton", "§7Powerful undead creature.");
        addGUIItem(gui, 26, Material.GHAST_TEAR, "§f§lGhast", "§7Floating fire creature.");
        addGUIItem(gui, 27, Material.MAGMA_CREAM, "§c§lMagma Cube", "§7Fire-based slime creature.");
        addGUIItem(gui, 28, Material.GOLD_INGOT, "§6§lPiglin", "§7Gold-loving nether creature.");

        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the GUI.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");

        player.openInventory(gui);
        player.sendMessage("§aMob GUI geöffnet!");
    }

    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            meta.lore(Arrays.asList(net.kyori.adventure.text.Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }

    // Removed duplicate applyMobAbilities method

    private void giveMobRewards(Player player, LivingEntity entity) {
        // Find the mob type
        for (MobType type : mobConfigs.keySet()) {
            if (entity.getType() == mobConfigs.get(type).getEntityType()) {
                MobConfig config = mobConfigs.get(type);

                // Give XP
                player.giveExp(config.getXpReward());

                // Give items
                for (String reward : config.getRewards()) {
                    if (reward.contains("Rotten Flesh")) {
                        player.getInventory().addItem(new ItemStack(Material.ROTTEN_FLESH));
                    } else if (reward.contains("Bone")) {
                        player.getInventory().addItem(new ItemStack(Material.BONE));
                    } else if (reward.contains("String")) {
                        player.getInventory().addItem(new ItemStack(Material.STRING));
                    } else if (reward.contains("Gunpowder")) {
                        player.getInventory().addItem(new ItemStack(Material.GUNPOWDER));
                    } else if (reward.contains("Ender Pearl")) {
                        player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
                    } else if (reward.contains("Blaze Rod")) {
                        player.getInventory().addItem(new ItemStack(Material.BLAZE_ROD));
                    } else if (reward.contains("Wither Skeleton Skull")) {
                        player.getInventory().addItem(new ItemStack(Material.WITHER_SKELETON_SKULL));
                    } else if (reward.contains("Ghast Tear")) {
                        player.getInventory().addItem(new ItemStack(Material.GHAST_TEAR));
                    } else if (reward.contains("Magma Cream")) {
                        player.getInventory().addItem(new ItemStack(Material.MAGMA_CREAM));
                    } else if (reward.contains("Gold Ingot")) {
                        player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT));
                    } else if (reward.contains("Iron Ingot")) {
                        player.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
                    } else if (reward.contains("Nether Star")) {
                        player.getInventory().addItem(new ItemStack(Material.NETHER_STAR));
                    } else if (reward.contains("Dragon Egg")) {
                        player.getInventory().addItem(new ItemStack(Material.DRAGON_EGG));
                    }
                }

                // Update player data
                PlayerMobData data = getPlayerMobData(player.getUniqueId());
                data.addMobKill(type);

                player.sendMessage("§aYou killed a " + config.getDisplayName() + "!");
                break;
            }
        }
    }

    public PlayerMobData getPlayerMobData(UUID playerId) {
        return playerMobData.computeIfAbsent(playerId, k -> new PlayerMobData(playerId));
    }

    public MobConfig getMobConfig(MobType type) {
        return mobConfigs.get(type);
    }

    public List<MobType> getAllMobTypes() {
        return new ArrayList<>(mobConfigs.keySet());
    }

    public void createSpawnArea(String name, Location center, int radius, MobType mobType, int maxMobs) {
        UUID areaId = UUID.randomUUID();
        SpawnArea area = new SpawnArea(areaId, name, center, radius, mobType, maxMobs);
        spawnAreas.put(areaId, area);
    }

    public void removeSpawnArea(UUID areaId) {
        spawnAreas.remove(areaId);
    }

    public SpawnArea getSpawnArea(UUID areaId) {
        return spawnAreas.get(areaId);
    }

    public List<SpawnArea> getAllSpawnAreas() {
        return new ArrayList<>(spawnAreas.values());
    }

    public enum MobType {
        // Basic Mobs
        ZOMBIE, SKELETON, SPIDER, CREEPER, ENDERMAN, BLAZE, WITHER_SKELETON, GHAST, MAGMA_CUBE, PIGLIN,
        PIGLIN_BRUTE, ZOMBIFIED_PIGLIN, HOGLIN, ZOGLIN, STRIDER, ENDERMITE, SHULKER, VEX, EVOKER, VINDICATOR,
        PILLAGER, RAVAGER, WITCH, PHANTOM, DROWNED, HUSK, STRAY, SLIME, CAVE_SPIDER, SILVERFISH, ENDERMAN_CRYSTAL,

        // Hypixel SkyBlock Hub Mobs
        GRAVEYARD_ZOMBIE, ZOMBIE_VILLAGER, CRYPT_GHOUL, CRYPT_LURKER,

        // Hypixel SkyBlock End Mobs
        ZEALOT, SPECIAL_ZEALOT, ENDER_DRAGON,

        // Hypixel SkyBlock Slayer Mobs
        REVENANT_HORROR_I, REVENANT_HORROR_II, REVENANT_HORROR_III, REVENANT_HORROR_IV,
        TARANTULA_BROODFATHER_I, TARANTULA_BROODFATHER_II, TARANTULA_BROODFATHER_III, TARANTULA_BROODFATHER_IV,
        SVEN_PACKMASTER_I, SVEN_PACKMASTER_II, SVEN_PACKMASTER_III, SVEN_PACKMASTER_IV,
        VOIDGLOOM_SERAPH_I, VOIDGLOOM_SERAPH_II, VOIDGLOOM_SERAPH_III, VOIDGLOOM_SERAPH_IV,

        // Hypixel SkyBlock Dungeon Mobs
        DUNGEON_ZOMBIE, DUNGEON_SKELETON, DUNGEON_SPIDER, DUNGEON_ENDERMAN, DUNGEON_BLAZE, DUNGEON_CREEPER,
        DUNGEON_WITHER_SKELETON, DUNGEON_GHAST, DUNGEON_MAGMA_CUBE, DUNGEON_PIGLIN, DUNGEON_GOLEM,

        // Hypixel SkyBlock World Bosses
        MAGMA_BOSS, ARACHNE, DRAGON_BOSS,

        // Hypixel SkyBlock Special Bosses
        INFERNO_DEMONLORD, KUUDRA, NECRON, SADAN, LIVID, SCARF, THORN,
        // Special Mobs
        GOLEM, IRON_GOLEM, SNOW_GOLEM, VILLAGER, WANDERING_TRADER, CAT, WOLF, OCELOT,
        HORSE, DONKEY, MULE, LLAMA, PARROT, POLAR_BEAR, PANDA, FOX, BEE, DOLPHIN, TURTLE, COD, SALMON,
        TROPICAL_FISH, PUFFERFISH, SQUID, GUARDIAN, ELDER_GUARDIAN, BAT, CHICKEN, COW, MUSHROOM_COW, PIG,
        RABBIT, SHEEP, ZOMBIE_HORSE, SKELETON_HORSE, ZOMBIE_PIGMAN,
        // Boss Mobs
        WITHER, ENDER_DRAGON_BOSS, ELDER_GUARDIAN_BOSS, RAVAGER_BOSS, EVOKER_BOSS, VINDICATOR_BOSS,
        // Mythological Mobs
        PHOENIX, GRIFFIN, DRAGON, UNICORN, PEGASUS, KRAKEN, LEVIATHAN, BEHEMOTH, TITAN, COLOSSUS,
        // Elemental Mobs
        FIRE_ELEMENTAL, WATER_ELEMENTAL, EARTH_ELEMENTAL, AIR_ELEMENTAL, LIGHTNING_ELEMENTAL, ICE_ELEMENTAL,
        SHADOW_ELEMENTAL, LIGHT_ELEMENTAL, VOID_ELEMENTAL, CHAOS_ELEMENTAL,
        // Mechanical Mobs
        MECHANICAL_GOLEM, STEAMPUNK_GOLEM, CYBER_GOLEM, ROBOT, ANDROID, DRONE, TURRET, SENTRY,
        // Magical Mobs
        MAGE, WIZARD, SORCERER, NECROMANCER, ENCHANTER, ALCHEMIST, RUNEMASTER, SPELLCASTER,
        // Undead Mobs
        LICH, VAMPIRE, WRAITH, SPECTER, BANSHEE, REVENANT, ZOMBIE_LORD, SKELETON_LORD, GHOST,
        // Demon Mobs
        DEMON, IMP, SUCCUBUS, INCUBUS, HELLHOUND, BALROG, ASMODEUS, LUCIFER, SATAN, DEVIL,
        // Angel Mobs
        ANGEL, ARCHANGEL, SERAPHIM, CHERUBIM, THRONE, DOMINION, VIRTUE, POWER, PRINCIPALITY, ARCHANGEL_MICHAEL,
        // Dragon Mobs
        FIRE_DRAGON, ICE_DRAGON, LIGHTNING_DRAGON, EARTH_DRAGON, WATER_DRAGON, WIND_DRAGON, SHADOW_DRAGON,
        LIGHT_DRAGON, VOID_DRAGON, CHAOS_DRAGON, ANCIENT_DRAGON, ELDER_DRAGON, PRIMAL_DRAGON, COSMIC_DRAGON
    }

    public enum MobCategory {
        BASIC("§aBasic", "§7Basic mobs"),
        ADVANCED("§eAdvanced", "§7Advanced mobs"),
        HUB("§6Hub", "§7Hub area mobs"),
        END("§5End", "§7End dimension mobs"),
        NETHER("§cNether", "§7Nether dimension mobs"),
        SLAYER("§cSlayer", "§7Slayer mobs"),
        DUNGEON("§5Dungeon", "§7Dungeon mobs"),
        WORLD_BOSS("§4World Boss", "§7World boss mobs"),
        SPECIAL_BOSS("§dSpecial Boss", "§7Special boss mobs"),
        SPECIAL("§6Special", "§7Special mobs"),
        BOSS("§4Boss", "§7Boss mobs"),
        MYTHOLOGICAL("§dMythological", "§7Mythological mobs"),
        ELEMENTAL("§bElemental", "§7Elemental mobs"),
        MECHANICAL("§7Mechanical", "§7Mechanical mobs"),
        MAGICAL("§5Magical", "§7Magical mobs"),
        UNDEAD("§8Undead", "§7Undead mobs"),
        DEMON("§4Demon", "§7Demon mobs"),
        ANGEL("§fAngel", "§7Angel mobs"),
        DRAGON("§6Dragon", "§7Dragon mobs");

        private final String displayName;
        private final String description;

        MobCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }

    public enum MobRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);

        private final String displayName;
        private final double multiplier;

        MobRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    public static class MobConfig {
        private final String name;
        private final String displayName;
        private final EntityType entityType;
        private final String description;
        private final MobCategory category;
        private final MobRarity rarity;
        private final int health;
        private final int damage;
        private final double speed;
        private final List<String> abilities;
        private final List<String> rewards;
        private final List<String> spawnConditions;

        public MobConfig(String name, String displayName, EntityType entityType, String description,
                        MobCategory category, MobRarity rarity, int health, int damage, double speed,
                        List<String> abilities, List<String> rewards, List<String> spawnConditions) {
            this.name = name;
            this.displayName = displayName;
            this.entityType = entityType;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.health = health;
            this.damage = damage;
            this.speed = speed;
            this.abilities = abilities;
            this.rewards = rewards;
            this.spawnConditions = spawnConditions;
        }

        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public EntityType getEntityType() { return entityType; }
        public String getDescription() { return description; }
        public MobCategory getCategory() { return category; }
        public MobRarity getRarity() { return rarity; }
        public int getHealth() { return health; }
        public int getDamage() { return damage; }
        public double getSpeed() { return speed; }
        public List<String> getAbilities() { return abilities; }
        public List<String> getRewards() { return rewards; }
        public List<String> getSpawnConditions() { return spawnConditions; }
        public int getXpReward() { return (int) (10 * rarity.getMultiplier()); }
    }

    public static class PlayerMobData {
        private final UUID playerId;
        private final Map<MobType, Integer> mobKills = new HashMap<>();
        private final Map<MobType, Integer> mobLevels = new HashMap<>();
        private long lastUpdate;

        public PlayerMobData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }

        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }

        public void addMobKill(MobType type) {
            mobKills.put(type, mobKills.getOrDefault(type, 0) + 1);
        }

        public void levelUpMob(MobType type) {
            mobLevels.put(type, mobLevels.getOrDefault(type, 0) + 1);
        }

        public int getMobKills(MobType type) {
            return mobKills.getOrDefault(type, 0);
        }

        public int getMobLevel(MobType type) {
            return mobLevels.getOrDefault(type, 0);
        }

        public long getLastUpdate() { return lastUpdate; }
    }

    public static class SpawnArea {
        private final UUID areaId;
        private final String name;
        private final Location center;
        private final int radius;
        private final MobType mobType;
        private final int maxMobs;
        private final List<LivingEntity> spawnedMobs = new ArrayList<>();
        private long lastSpawn;

        public SpawnArea(UUID areaId, String name, Location center, int radius, MobType mobType, int maxMobs) {
            this.areaId = areaId;
            this.name = name;
            this.center = center;
            this.radius = radius;
            this.mobType = mobType;
            this.maxMobs = maxMobs;
            this.lastSpawn = System.currentTimeMillis();
        }

        public void update() {
            // Remove dead mobs
            spawnedMobs.removeIf(mob -> mob.isDead() || !mob.isValid());

            // Spawn new mobs if needed
            if (spawnedMobs.size() < maxMobs && System.currentTimeMillis() - lastSpawn > 5000) {
                spawnMob();
                lastSpawn = System.currentTimeMillis();
            }
        }

        private void spawnMob() {
            // Generate random location within radius
            double angle = Math.random() * 2 * Math.PI;
            double distance = Math.random() * radius;

            double x = center.getX() + Math.cos(angle) * distance;
            double z = center.getZ() + Math.sin(angle) * distance;
            double y = center.getY();

            Location spawnLocation = new Location(center.getWorld(), x, y, z);

            // Spawn the mob
            EntityType entityType = getEntityType(mobType);
            if (entityType != null) {
                LivingEntity mob = (LivingEntity) center.getWorld().spawnEntity(spawnLocation, entityType);
                spawnedMobs.add(mob);
            }
        }

        public UUID getAreaId() { return areaId; }
        public String getName() { return name; }
        public Location getCenter() { return center; }
        public int getRadius() { return radius; }
        public MobType getMobType() { return mobType; }
        public int getMaxMobs() { return maxMobs; }
        public List<LivingEntity> getSpawnedMobs() { return spawnedMobs; }
        public long getLastSpawn() { return lastSpawn; }

        private EntityType getEntityType(MobType mobType) {
            switch (mobType) {
                case ZOMBIE:
                case REVENANT_HORROR_I:
                case DUNGEON_ZOMBIE:
                    return EntityType.ZOMBIE;
                case SKELETON:
                case DUNGEON_SKELETON:
                    return EntityType.SKELETON;
                case SPIDER:
                case TARANTULA_BROODFATHER_I:
                case DUNGEON_SPIDER:
                    return EntityType.SPIDER;
                case CREEPER:
                    return EntityType.CREEPER;
                case ENDERMAN:
                case VOIDGLOOM_SERAPH_I:
                case DUNGEON_ENDERMAN:
                    return EntityType.ENDERMAN;
                case BLAZE:
                case INFERNO_DEMONLORD:
                case DUNGEON_BLAZE:
                    return EntityType.BLAZE;
                case WITHER_SKELETON:
                    return EntityType.WITHER_SKELETON;
                case GHAST:
                    return EntityType.GHAST;
                case MAGMA_CUBE:
                    return EntityType.MAGMA_CUBE;
                case PIGLIN:
                    return EntityType.PIGLIN;
                case SVEN_PACKMASTER_I:
                    return EntityType.WOLF;
                case GOLEM:
                case IRON_GOLEM:
                    return EntityType.IRON_GOLEM;
                case WITHER:
                    return EntityType.WITHER;
                case ENDER_DRAGON:
                case ENDER_DRAGON_BOSS:
                    return EntityType.ENDER_DRAGON;
                case SNOW_GOLEM:
                    return EntityType.SNOW_GOLEM;
                case VILLAGER:
                    return EntityType.VILLAGER;
                case WANDERING_TRADER:
                    return EntityType.WANDERING_TRADER;
                case CAT:
                    return EntityType.CAT;
                case WOLF:
                    return EntityType.WOLF;
                case OCELOT:
                    return EntityType.OCELOT;
                case HORSE:
                    return EntityType.HORSE;
                case DONKEY:
                    return EntityType.DONKEY;
                case MULE:
                    return EntityType.MULE;
                case LLAMA:
                    return EntityType.LLAMA;
                case PARROT:
                    return EntityType.PARROT;
                case POLAR_BEAR:
                    return EntityType.POLAR_BEAR;
                case PANDA:
                    return EntityType.PANDA;
                case FOX:
                    return EntityType.FOX;
                case BEE:
                    return EntityType.BEE;
                case DOLPHIN:
                    return EntityType.DOLPHIN;
                case TURTLE:
                    return EntityType.TURTLE;
                case COD:
                    return EntityType.COD;
                case SALMON:
                    return EntityType.SALMON;
                case TROPICAL_FISH:
                    return EntityType.TROPICAL_FISH;
                case PUFFERFISH:
                    return EntityType.PUFFERFISH;
                case SQUID:
                    return EntityType.SQUID;
                case GUARDIAN:
                    return EntityType.GUARDIAN;
                case ELDER_GUARDIAN:
                    return EntityType.ELDER_GUARDIAN;
                case BAT:
                    return EntityType.BAT;
                case CHICKEN:
                    return EntityType.CHICKEN;
                case COW:
                    return EntityType.COW;
                case MUSHROOM_COW:
                    return EntityType.MOOSHROOM;
                case PIG:
                    return EntityType.PIG;
                case RABBIT:
                    return EntityType.RABBIT;
                case SHEEP:
                    return EntityType.SHEEP;
                case ZOMBIE_HORSE:
                    return EntityType.ZOMBIE_HORSE;
                case SKELETON_HORSE:
                    return EntityType.SKELETON_HORSE;
                case ZOMBIE_PIGMAN:
                    return EntityType.ZOMBIFIED_PIGLIN;
                default:
                    return EntityType.ZOMBIE;
            }
        }
    }

     public void applyMobAbilities(LivingEntity entity, MobType type) {
         MobConfig config = mobConfigs.get(type);
         if (config == null) return;

        // Apply custom name and make it visible
        entity.customName(Component.text(config.getDisplayName()));
        entity.setCustomNameVisible(true);

         // Apply health and damage - cap health at 1024.0 for Folia compatibility
         if (entity instanceof LivingEntity livingEntity) {
             int health = Math.min(config.getHealth(), 1024); // Cap health at 1024.0
             livingEntity.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
             livingEntity.setHealth(health);
         }

         // Apply special abilities based on mob type
         switch (type) {
             case ZOMBIE, REVENANT_HORROR_I, DUNGEON_ZOMBIE -> {
                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 0));
                entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0));
             }
             case SKELETON, DUNGEON_SKELETON -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 0));
             }
             case SPIDER, TARANTULA_BROODFATHER_I, DUNGEON_SPIDER -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 1));
             }
             case CREEPER -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
             }
             case ENDERMAN, VOIDGLOOM_SERAPH_I, DUNGEON_ENDERMAN -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
             }
             case BLAZE, INFERNO_DEMONLORD, DUNGEON_BLAZE -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
             }
             case WITHER_SKELETON -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
             }
             case GHAST -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, Integer.MAX_VALUE, 0));
             }
             case MAGMA_CUBE -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, Integer.MAX_VALUE, 2));
             }
             case PIGLIN, PIGLIN_BRUTE -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
             }
             case SVEN_PACKMASTER_I -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
             }
             case GOLEM, IRON_GOLEM -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
             }
                case WITHER -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
             }
             case ENDER_DRAGON, ENDER_DRAGON_BOSS -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 3));
             }
             default -> {
                 // Default abilities for all other mob types
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
             }
         }

         // Apply special effects based on rarity
         switch (config.getRarity()) {
             case COMMON -> {
                 // No special effects
             }
             case UNCOMMON -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0));
             }
             case RARE -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0));
             }
             case EPIC -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 0));
             }
             case LEGENDARY -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
             }
             case MYTHIC -> {
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 2));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 2));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
                 entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
             }
         }
     }
}
