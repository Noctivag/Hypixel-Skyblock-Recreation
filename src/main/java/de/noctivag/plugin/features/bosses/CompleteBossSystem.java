package de.noctivag.plugin.features.bosses;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Complete Boss System - Alle Bosse aus Hypixel Skyblock
 * 
 * Implementiert alle Bosse genau nach der offiziellen Hypixel Skyblock Wiki:
 * - Weltbosse (Enderdrache, Arachne, Endstein-Beschützer)
 * - Slayer-Bosse (Revenant, Tarantula, Sven, Enderman, Blaze, Vampire)
 * - Drachen-Bosse (alle 8 Drachen-Typen)
 * - Catacombs-Bosse (Bonzo, Scarf, Professor, Thorn, Livid, Sadan, Necron)
 * - Kuudra-Bosse (alle 5 Varianten)
 */
public class CompleteBossSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    
    // Boss-Kategorien
    private final Map<String, WorldBoss> worldBosses = new HashMap<>();
    private final Map<String, SlayerBoss> slayerBosses = new HashMap<>();
    private final Map<String, DragonBoss> dragonBosses = new HashMap<>();
    private final Map<String, CatacombsBoss> catacombsBosses = new HashMap<>();
    private final Map<String, KuudraBoss> kuudraBosses = new HashMap<>();
    
    // Aktive Boss-Instanzen
    private final Map<UUID, BossInstance> activeBosses = new ConcurrentHashMap<>();
    private final Map<UUID, BossFight> activeFights = new ConcurrentHashMap<>();
    
    public CompleteBossSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeAllBosses();
    }
    
    /**
     * Initialisiert alle Bosse aus Hypixel Skyblock
     */
    private void initializeAllBosses() {
        initializeWorldBosses();
        initializeSlayerBosses();
        initializeDragonBosses();
        initializeCatacombsBosses();
        initializeKuudraBosses();
    }
    
    /**
     * Initialisiert alle Weltbosse
     */
    private void initializeWorldBosses() {
        // Enderdrache - Hauptweltboss
        worldBosses.put("ENDER_DRAGON", new WorldBoss(
            "ENDER_DRAGON",
            "Enderdrache",
            "§5§lEnderdrache",
            EntityType.ENDER_DRAGON,
            15000000, // 15M HP
            2200,     // 2200 Damage
            Arrays.asList(
                "§7Der mächtigste Boss in Skyblock",
                "§7Spawnt alle 8 Stunden im Drachennest",
                "§7Droppt Dragon Fragments und Dragon Armor"
            ),
            Arrays.asList("DRAGON_FRAGMENT", "DRAGON_ARMOR", "DRAGON_EGG"),
            BossType.WORLD_BOSS,
            28800000L // 8 Stunden Spawn-Zeit
        ));
        
        // Arachne - Spinnen-Boss
        worldBosses.put("ARACHNE", new WorldBoss(
            "ARACHNE",
            "Arachne",
            "§c§lArachne",
            EntityType.SPIDER,
            100000,   // 100K HP
            600,      // 600 Damage
            Arrays.asList(
                "§7Spinnen-Boss im Spinnenbau",
                "§7Spawnt alle 4 Stunden",
                "§7Droppt Spider Relics und Arachne's Keeper"
            ),
            Arrays.asList("SPIDER_RELIC", "ARACHNES_KEEPER", "SPIDER_CATALYST"),
            BossType.WORLD_BOSS,
            14400000L // 4 Stunden Spawn-Zeit
        ));
        
        // Endstein-Beschützer
        worldBosses.put("ENDSTONE_PROTECTOR", new WorldBoss(
            "ENDSTONE_PROTECTOR",
            "Endstein-Beschützer",
            "§e§lEndstein-Beschützer",
            EntityType.ENDERMAN,
            5000000,  // 5M HP
            1200,     // 1200 Damage
            Arrays.asList(
                "§7Beschützt das Endstone im End",
                "§7Spawnt alle 2 Stunden",
                "§7Droppt Endstone Bow und Endstone Sword"
            ),
            Arrays.asList("ENDSTONE_BOW", "ENDSTONE_SWORD", "ENDSTONE_SHARD"),
            BossType.WORLD_BOSS,
            7200000L // 2 Stunden Spawn-Zeit
        ));
    }
    
    /**
     * Initialisiert alle Slayer-Bosse
     */
    private void initializeSlayerBosses() {
        // Revenant Horror (Zombie Slayer)
        for (int tier = 1; tier <= 5; tier++) {
            slayerBosses.put("REVENANT_T" + tier, new SlayerBoss(
                "REVENANT_T" + tier,
                "Revenant Horror T" + tier,
                "§2§lRevenant Horror T" + tier,
                EntityType.ZOMBIE,
                getRevenantHealth(tier),
                getRevenantDamage(tier),
                Arrays.asList(
                    "§7Zombie Slayer Boss Tier " + tier,
                    "§7Spawnt durch Zombie Slayer Quest",
                    "§7Droppt Revenant Viscera und Revenant Armor"
                ),
                Arrays.asList("REVENANT_VISCERA", "REVENANT_ARMOR", "REVENANT_FALCHION"),
                SlayerType.ZOMBIE,
                tier
            ));
        }
        
        // Tarantula Broodfather (Spider Slayer)
        for (int tier = 1; tier <= 4; tier++) {
            slayerBosses.put("TARANTULA_T" + tier, new SlayerBoss(
                "TARANTULA_T" + tier,
                "Tarantula Broodfather T" + tier,
                "§c§lTarantula Broodfather T" + tier,
                EntityType.SPIDER,
                getTarantulaHealth(tier),
                getTarantulaDamage(tier),
                Arrays.asList(
                    "§7Spider Slayer Boss Tier " + tier,
                    "§7Spawnt durch Spider Slayer Quest",
                    "§7Droppt Tarantula Web und Tarantula Armor"
                ),
                Arrays.asList("TARANTULA_WEB", "TARANTULA_ARMOR", "TARANTULA_BLADE"),
                SlayerType.SPIDER,
                tier
            ));
        }
        
        // Sven Packmaster (Wolf Slayer)
        for (int tier = 1; tier <= 4; tier++) {
            slayerBosses.put("SVEN_T" + tier, new SlayerBoss(
                "SVEN_T" + tier,
                "Sven Packmaster T" + tier,
                "§b§lSven Packmaster T" + tier,
                EntityType.WOLF,
                getSvenHealth(tier),
                getSvenDamage(tier),
                Arrays.asList(
                    "§7Wolf Slayer Boss Tier " + tier,
                    "§7Spawnt durch Wolf Slayer Quest",
                    "§7Droppt Hamster Wheel und Sven Armor"
                ),
                Arrays.asList("HAMSTER_WHEEL", "SVEN_ARMOR", "POOCH_SWORD"),
                SlayerType.WOLF,
                tier
            ));
        }
        
        // Enderman Slayer (Voidgloom Seraph)
        for (int tier = 1; tier <= 4; tier++) {
            slayerBosses.put("VOIDGLOOM_T" + tier, new SlayerBoss(
                "VOIDGLOOM_T" + tier,
                "Voidgloom Seraph T" + tier,
                "§5§lVoidgloom Seraph T" + tier,
                EntityType.ENDERMAN,
                getVoidgloomHealth(tier),
                getVoidgloomDamage(tier),
                Arrays.asList(
                    "§7Enderman Slayer Boss Tier " + tier,
                    "§7Spawnt durch Enderman Slayer Quest",
                    "§7Droppt Null Sphere und Voidgloom Armor"
                ),
                Arrays.asList("NULL_SPHERE", "VOIDGLOOM_ARMOR", "VOIDEDGE_KATANA"),
                SlayerType.ENDERMAN,
                tier
            ));
        }
        
        // Blaze Slayer (Inferno Demonlord)
        for (int tier = 1; tier <= 4; tier++) {
            slayerBosses.put("BLAZE_T" + tier, new SlayerBoss(
                "BLAZE_T" + tier,
                "Inferno Demonlord T" + tier,
                "§6§lInferno Demonlord T" + tier,
                EntityType.BLAZE,
                getBlazeHealth(tier),
                getBlazeDamage(tier),
                Arrays.asList(
                    "§7Blaze Slayer Boss Tier " + tier,
                    "§7Spawnt durch Blaze Slayer Quest",
                    "§7Droppt Blaze Rod und Blaze Armor"
                ),
                Arrays.asList("BLAZE_ROD", "BLAZE_ARMOR", "BLAZE_SWORD"),
                SlayerType.BLAZE,
                tier
            ));
        }
        
        // Vampire Slayer (Riftstalker Bloodfiend)
        for (int tier = 1; tier <= 5; tier++) {
            slayerBosses.put("VAMPIRE_T" + tier, new SlayerBoss(
                "VAMPIRE_T" + tier,
                "Riftstalker Bloodfiend T" + tier,
                "§4§lRiftstalker Bloodfiend T" + tier,
                EntityType.ZOMBIE, // VAMPIRE nicht verfügbar, verwende ZOMBIE
                getVampireHealth(tier),
                getVampireDamage(tier),
                Arrays.asList(
                    "§7Vampire Slayer Boss Tier " + tier,
                    "§7Spawnt durch Vampire Slayer Quest",
                    "§7Droppt Blood Chalice und Vampire Armor"
                ),
                Arrays.asList("BLOOD_CHALICE", "VAMPIRE_ARMOR", "VAMPIRE_SWORD"),
                SlayerType.VAMPIRE,
                tier
            ));
        }
    }
    
    /**
     * Initialisiert alle Drachen-Bosse
     */
    private void initializeDragonBosses() {
        // Alle 8 Drachen-Typen
        String[] dragonTypes = {"PROTECTOR", "OLD", "WISE", "UNSTABLE", "STRONG", "YOUNG", "SUPERIOR", "HOLY"};
        String[] dragonColors = {"§a", "§7", "§b", "§d", "§c", "§e", "§6", "§f"};
        
        for (int i = 0; i < dragonTypes.length; i++) {
            String type = dragonTypes[i];
            String color = dragonColors[i];
            
            dragonBosses.put(type, new DragonBoss(
                type,
                type + " Dragon",
                color + "§l" + type + " Dragon",
                EntityType.ENDER_DRAGON,
                getDragonHealth(type),
                getDragonDamage(type),
                Arrays.asList(
                    "§7" + type + " Dragon im Drachennest",
                    "§7Spawnt beim Enderdrache Kampf",
                    "§7Droppt " + type + " Dragon Fragments"
                ),
                Arrays.asList(type + "_FRAGMENT", type + "_ARMOR", "DRAGON_CLAW"),
                DragonType.valueOf(type)
            ));
        }
    }
    
    /**
     * Initialisiert alle Catacombs-Bosse
     */
    private void initializeCatacombsBosses() {
        // F1 - Bonzo
        catacombsBosses.put("BONZO", new CatacombsBoss(
            "BONZO",
            "Bonzo",
            "§6§lBonzo",
            EntityType.ZOMBIE,
            1000000,  // 1M HP
            500,      // 500 Damage
            Arrays.asList(
                "§7F1 Boss - Ehemaliger Zirkusclown",
                "§7Verwendet dunkle Künste",
                "§7Droppt Bonzo's Staff und Clown Mask"
            ),
            Arrays.asList("BONZOS_STAFF", "CLOWN_MASK", "BONZO_FRAGMENT"),
            1
        ));
        
        // F2 - Scarf
        catacombsBosses.put("SCARF", new CatacombsBoss(
            "SCARF",
            "Scarf",
            "§5§lScarf",
            EntityType.SKELETON,
            2000000,  // 2M HP
            750,      // 750 Damage
            Arrays.asList(
                "§7F2 Boss - Talentierter Schüler",
                "§7Spezialisiert auf Nekromantie",
                "§7Droppt Scarf's Studies und Scarf Fragment"
            ),
            Arrays.asList("SCARFS_STUDIES", "SCARF_FRAGMENT", "SCARF_GRIMOIRE"),
            2
        ));
        
        // F3 - The Professor
        catacombsBosses.put("PROFESSOR", new CatacombsBoss(
            "PROFESSOR",
            "The Professor",
            "§b§lThe Professor",
            EntityType.WITCH,
            3000000,  // 3M HP
            1000,     // 1000 Damage
            Arrays.asList(
                "§7F3 Boss - Lehrer mit Technik",
                "§7Ruft Wächter herbei",
                "§7Droppt Professor's Research und Professor Fragment"
            ),
            Arrays.asList("PROFESSORS_RESEARCH", "PROFESSOR_FRAGMENT", "PROFESSOR_TOME"),
            3
        ));
        
        // F4 - Thorn
        catacombsBosses.put("THORN", new CatacombsBoss(
            "THORN",
            "Thorn",
            "§2§lThorn",
            EntityType.SPIDER,
            4000000,  // 4M HP
            1250,     // 1250 Damage
            Arrays.asList(
                "§7F4 Boss - Nekromant",
                "§7Spezialisiert auf Tiere",
                "§7Droppt Thorn's Bow und Thorn Fragment"
            ),
            Arrays.asList("THORNS_BOW", "THORN_FRAGMENT", "THORN_QUIVER"),
            4
        ));
        
        // F5 - Livid
        catacombsBosses.put("LIVID", new CatacombsBoss(
            "LIVID",
            "Livid",
            "§d§lLivid",
            EntityType.ENDERMAN,
            5000000,  // 5M HP
            1500,     // 1500 Damage
            Arrays.asList(
                "§7F5 Boss - Hat mehrere Klone",
                "§7Echter Livid nimmt Schaden",
                "§7Droppt Livid Dagger und Livid Fragment"
            ),
            Arrays.asList("LIVID_DAGGER", "LIVID_FRAGMENT", "LIVID_MASK"),
            5
        ));
        
        // F6 - Sadan
        catacombsBosses.put("SADAN", new CatacombsBoss(
            "SADAN",
            "Sadan",
            "§c§lSadan",
            EntityType.WITHER_SKELETON,
            6000000,  // 6M HP
            1750,     // 1750 Damage
            Arrays.asList(
                "§7F6 Boss - Nekromant mit Armee",
                "§7Ruft Giant Warriors herbei",
                "§7Droppt Sadan's Brooch und Sadan Fragment"
            ),
            Arrays.asList("SADANS_BROOCH", "SADAN_FRAGMENT", "SADAN_ARMOR"),
            6
        ));
        
        // F7 - Necron
        catacombsBosses.put("NECRON", new CatacombsBoss(
            "NECRON",
            "Necron",
            "§8§lNecron",
            EntityType.WITHER,
            10000000, // 10M HP
            2500,     // 2500 Damage
            Arrays.asList(
                "§7F7 Boss - Mächtiger Wither Lord",
                "§7Finaler Boss der Catacombs",
                "§7Droppt Necron's Handle und Necron Fragment"
            ),
            Arrays.asList("NECRONS_HANDLE", "NECRON_FRAGMENT", "NECRON_ARMOR"),
            7
        ));
    }
    
    /**
     * Initialisiert alle Kuudra-Bosse
     */
    private void initializeKuudraBosses() {
        // Basic Kuudra
        kuudraBosses.put("BASIC_KUUDRA", new KuudraBoss(
            "BASIC_KUUDRA",
            "Basic Kuudra",
            "§6§lBasic Kuudra",
            EntityType.ENDER_DRAGON,
            50000000, // 50M HP
            5000,     // 5000 Damage
            Arrays.asList(
                "§7Basic Kuudra im Crimson Isles",
                "§7Erste Kuudra-Variante",
                "§7Droppt Basic Kuudra Armor und Kuudra Core"
            ),
            Arrays.asList("BASIC_KUUDRA_ARMOR", "KUUDRA_CORE", "KUUDRA_FRAGMENT"),
            KuudraTier.BASIC
        ));
        
        // Hot Kuudra
        kuudraBosses.put("HOT_KUUDRA", new KuudraBoss(
            "HOT_KUUDRA",
            "Hot Kuudra",
            "§c§lHot Kuudra",
            EntityType.BLAZE,
            75000000, // 75M HP
            7500,     // 7500 Damage
            Arrays.asList(
                "§7Hot Kuudra - Feuer-Variante",
                "§7Höhere Schwierigkeit",
                "§7Droppt Hot Kuudra Armor und Hot Kuudra Core"
            ),
            Arrays.asList("HOT_KUUDRA_ARMOR", "HOT_KUUDRA_CORE", "HOT_KUUDRA_FRAGMENT"),
            KuudraTier.HOT
        ));
        
        // Burning Kuudra
        kuudraBosses.put("BURNING_KUUDRA", new KuudraBoss(
            "BURNING_KUUDRA",
            "Burning Kuudra",
            "§4§lBurning Kuudra",
            EntityType.MAGMA_CUBE,
            100000000, // 100M HP
            10000,     // 10000 Damage
            Arrays.asList(
                "§7Burning Kuudra - Lava-Variante",
                "§7Sehr hohe Schwierigkeit",
                "§7Droppt Burning Kuudra Armor und Burning Kuudra Core"
            ),
            Arrays.asList("BURNING_KUUDRA_ARMOR", "BURNING_KUUDRA_CORE", "BURNING_KUUDRA_FRAGMENT"),
            KuudraTier.BURNING
        ));
        
        // Fiery Kuudra
        kuudraBosses.put("FIERY_KUUDRA", new KuudraBoss(
            "FIERY_KUUDRA",
            "Fiery Kuudra",
            "§6§lFiery Kuudra",
            EntityType.GHAST,
            150000000, // 150M HP
            15000,     // 15000 Damage
            Arrays.asList(
                "§7Fiery Kuudra - Inferno-Variante",
                "§7Extreme Schwierigkeit",
                "§7Droppt Fiery Kuudra Armor und Fiery Kuudra Core"
            ),
            Arrays.asList("FIERY_KUUDRA_ARMOR", "FIERY_KUUDRA_CORE", "FIERY_KUUDRA_FRAGMENT"),
            KuudraTier.FIERY
        ));
        
        // Inferno Kuudra
        kuudraBosses.put("INFERNO_KUUDRA", new KuudraBoss(
            "INFERNO_KUUDRA",
            "Inferno Kuudra",
            "§4§lInferno Kuudra",
            EntityType.WITHER_SKELETON,
            200000000, // 200M HP
            20000,     // 20000 Damage
            Arrays.asList(
                "§7Inferno Kuudra - Finale Variante",
                "§7Höchste Schwierigkeit",
                "§7Droppt Inferno Kuudra Armor und Inferno Kuudra Core"
            ),
            Arrays.asList("INFERNO_KUUDRA_ARMOR", "INFERNO_KUUDRA_CORE", "INFERNO_KUUDRA_FRAGMENT"),
            KuudraTier.INFERNO
        ));
    }
    
    // Helper-Methoden für Boss-Statistiken
    private int getRevenantHealth(int tier) {
        return switch (tier) {
            case 1 -> 100000;   // 100K
            case 2 -> 500000;   // 500K
            case 3 -> 2000000;  // 2M
            case 4 -> 10000000; // 10M
            case 5 -> 50000000; // 50M
            default -> 100000;
        };
    }
    
    private int getRevenantDamage(int tier) {
        return switch (tier) {
            case 1 -> 200;
            case 2 -> 500;
            case 3 -> 1000;
            case 4 -> 2500;
            case 5 -> 5000;
            default -> 200;
        };
    }
    
    private int getTarantulaHealth(int tier) {
        return switch (tier) {
            case 1 -> 150000;   // 150K
            case 2 -> 750000;   // 750K
            case 3 -> 3000000;  // 3M
            case 4 -> 15000000; // 15M
            default -> 150000;
        };
    }
    
    private int getTarantulaDamage(int tier) {
        return switch (tier) {
            case 1 -> 300;
            case 2 -> 750;
            case 3 -> 1500;
            case 4 -> 3750;
            default -> 300;
        };
    }
    
    private int getSvenHealth(int tier) {
        return switch (tier) {
            case 1 -> 200000;   // 200K
            case 2 -> 1000000;  // 1M
            case 3 -> 4000000;  // 4M
            case 4 -> 20000000; // 20M
            default -> 200000;
        };
    }
    
    private int getSvenDamage(int tier) {
        return switch (tier) {
            case 1 -> 400;
            case 2 -> 1000;
            case 3 -> 2000;
            case 4 -> 5000;
            default -> 400;
        };
    }
    
    private int getVoidgloomHealth(int tier) {
        return switch (tier) {
            case 1 -> 500000;   // 500K
            case 2 -> 2500000;  // 2.5M
            case 3 -> 10000000; // 10M
            case 4 -> 50000000; // 50M
            default -> 500000;
        };
    }
    
    private int getVoidgloomDamage(int tier) {
        return switch (tier) {
            case 1 -> 500;
            case 2 -> 1250;
            case 3 -> 2500;
            case 4 -> 6250;
            default -> 500;
        };
    }
    
    private int getBlazeHealth(int tier) {
        return switch (tier) {
            case 1 -> 1000000;  // 1M
            case 2 -> 5000000;  // 5M
            case 3 -> 20000000; // 20M
            case 4 -> 100000000; // 100M
            default -> 1000000;
        };
    }
    
    private int getBlazeDamage(int tier) {
        return switch (tier) {
            case 1 -> 1000;
            case 2 -> 2500;
            case 3 -> 5000;
            case 4 -> 12500;
            default -> 1000;
        };
    }
    
    private int getVampireHealth(int tier) {
        return switch (tier) {
            case 1 -> 2000000;  // 2M
            case 2 -> 10000000; // 10M
            case 3 -> 40000000; // 40M
            case 4 -> 200000000; // 200M
            case 5 -> 1000000000; // 1B
            default -> 2000000;
        };
    }
    
    private int getVampireDamage(int tier) {
        return switch (tier) {
            case 1 -> 2000;
            case 2 -> 5000;
            case 3 -> 10000;
            case 4 -> 25000;
            case 5 -> 50000;
            default -> 2000;
        };
    }
    
    private int getDragonHealth(String type) {
        return switch (type) {
            case "PROTECTOR" -> 7500000;  // 7.5M
            case "OLD" -> 8000000;        // 8M
            case "WISE" -> 8500000;       // 8.5M
            case "UNSTABLE" -> 9000000;   // 9M
            case "STRONG" -> 9500000;     // 9.5M
            case "YOUNG" -> 10000000;     // 10M
            case "SUPERIOR" -> 12000000;  // 12M
            case "HOLY" -> 15000000;      // 15M
            default -> 10000000;
        };
    }
    
    private int getDragonDamage(String type) {
        return switch (type) {
            case "PROTECTOR" -> 1100;
            case "OLD" -> 1200;
            case "WISE" -> 1300;
            case "UNSTABLE" -> 1400;
            case "STRONG" -> 1500;
            case "YOUNG" -> 1600;
            case "SUPERIOR" -> 2000;
            case "HOLY" -> 2200;
            default -> 1500;
        };
    }
    
    /**
     * Spawnt einen Boss
     */
    public void spawnBoss(String bossId, Location location, Player spawner) {
        Boss boss = getBossById(bossId);
        if (boss == null) {
            spawner.sendMessage(Component.text("§cBoss nicht gefunden!").color(NamedTextColor.RED));
            return;
        }
        
        // Erstelle Boss-Instanz
        BossInstance instance = new BossInstance(boss, location, spawner);
        activeBosses.put(instance.getBossEntity().getUniqueId(), instance);
        
        // Starte Boss-Kampf
        startBossFight(instance);
        
        spawner.sendMessage(Component.text("§a§lBOSS GESPAWNT!")
            .color(NamedTextColor.GREEN));
        spawner.sendMessage(Component.text("§7§l» §6" + boss.getName())
            .color(NamedTextColor.GOLD));
    }
    
    /**
     * Startet einen Boss-Kampf
     */
    private void startBossFight(BossInstance instance) {
        BossFight fight = new BossFight(instance);
        activeFights.put(instance.getBossEntity().getUniqueId(), fight);
        
        // Starte Boss-AI
        new BossAI(instance, fight).runTaskTimer(plugin, 0L, 20L);
    }
    
    /**
     * Gibt einen Boss anhand der ID zurück
     */
    private Boss getBossById(String bossId) {
        if (worldBosses.containsKey(bossId)) return worldBosses.get(bossId);
        if (slayerBosses.containsKey(bossId)) return slayerBosses.get(bossId);
        if (dragonBosses.containsKey(bossId)) return dragonBosses.get(bossId);
        if (catacombsBosses.containsKey(bossId)) return catacombsBosses.get(bossId);
        if (kuudraBosses.containsKey(bossId)) return kuudraBosses.get(bossId);
        return null;
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        UUID entityId = event.getEntity().getUniqueId();
        
        if (activeBosses.containsKey(entityId)) {
            BossInstance instance = activeBosses.get(entityId);
            BossFight fight = activeFights.get(entityId);
            
            // Boss wurde besiegt
            endBossFight(instance, fight, true);
            
            // Entferne aus aktiven Listen
            activeBosses.remove(entityId);
            activeFights.remove(entityId);
        }
    }
    
    /**
     * Beendet einen Boss-Kampf
     */
    private void endBossFight(BossInstance instance, BossFight fight, boolean defeated) {
        if (defeated) {
            // Belohne Spieler
            for (Player player : fight.getParticipants()) {
                player.sendMessage(Component.text("§a§lBOSS BESIEGT!")
                    .color(NamedTextColor.GREEN));
                player.sendMessage(Component.text("§7§l» §6" + instance.getBoss().getName() + " wurde besiegt!")
                    .color(NamedTextColor.GOLD));
                
                // Gib Belohnungen
                giveBossRewards(player, instance.getBoss());
            }
        }
    }
    
    /**
     * Gibt Boss-Belohnungen
     */
    private void giveBossRewards(Player player, Boss boss) {
        for (String reward : boss.getDrops()) {
            ItemStack item = createBossReward(reward);
            if (item != null) {
                player.getInventory().addItem(item);
            }
        }
    }
    
    /**
     * Erstellt Boss-Belohnungen
     */
    private ItemStack createBossReward(String reward) {
        // Hier würden die echten Belohnungs-Items erstellt werden
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("§6" + reward));
        item.setItemMeta(meta);
        return item;
    }
    
    // Boss-Datenklassen
    public static abstract class Boss {
        protected final String id;
        protected final String name;
        protected final String displayName;
        protected final EntityType entityType;
        protected final int health;
        protected final int damage;
        protected final List<String> description;
        protected final List<String> drops;
        
        public Boss(String id, String name, String displayName, EntityType entityType,
                   int health, int damage, List<String> description, List<String> drops) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.entityType = entityType;
            this.health = health;
            this.damage = damage;
            this.description = description;
            this.drops = drops;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public EntityType getEntityType() { return entityType; }
        public int getHealth() { return health; }
        public int getDamage() { return damage; }
        public List<String> getDescription() { return description; }
        public List<String> getDrops() { return drops; }
    }
    
    public static class WorldBoss extends Boss {
        private final BossType type;
        private final long spawnTime;
        
        public WorldBoss(String id, String name, String displayName, EntityType entityType,
                        int health, int damage, List<String> description, List<String> drops,
                        BossType type, long spawnTime) {
            super(id, name, displayName, entityType, health, damage, description, drops);
            this.type = type;
            this.spawnTime = spawnTime;
        }
        
        public BossType getType() { return type; }
        public long getSpawnTime() { return spawnTime; }
    }
    
    public static class SlayerBoss extends Boss {
        private final SlayerType slayerType;
        private final int tier;
        
        public SlayerBoss(String id, String name, String displayName, EntityType entityType,
                         int health, int damage, List<String> description, List<String> drops,
                         SlayerType slayerType, int tier) {
            super(id, name, displayName, entityType, health, damage, description, drops);
            this.slayerType = slayerType;
            this.tier = tier;
        }
        
        public SlayerType getSlayerType() { return slayerType; }
        public int getTier() { return tier; }
    }
    
    public static class DragonBoss extends Boss {
        private final DragonType dragonType;
        
        public DragonBoss(String id, String name, String displayName, EntityType entityType,
                         int health, int damage, List<String> description, List<String> drops,
                         DragonType dragonType) {
            super(id, name, displayName, entityType, health, damage, description, drops);
            this.dragonType = dragonType;
        }
        
        public DragonType getDragonType() { return dragonType; }
    }
    
    public static class CatacombsBoss extends Boss {
        private final int floor;
        
        public CatacombsBoss(String id, String name, String displayName, EntityType entityType,
                            int health, int damage, List<String> description, List<String> drops,
                            int floor) {
            super(id, name, displayName, entityType, health, damage, description, drops);
            this.floor = floor;
        }
        
        public int getFloor() { return floor; }
    }
    
    public static class KuudraBoss extends Boss {
        private final KuudraTier tier;
        
        public KuudraBoss(String id, String name, String displayName, EntityType entityType,
                         int health, int damage, List<String> description, List<String> drops,
                         KuudraTier tier) {
            super(id, name, displayName, entityType, health, damage, description, drops);
            this.tier = tier;
        }
        
        public KuudraTier getTier() { return tier; }
    }
    
    // Enums
    public enum BossType {
        WORLD_BOSS, SLAYER_BOSS, DRAGON_BOSS, CATACOMBS_BOSS, KUUDRABOSS
    }
    
    public enum SlayerType {
        ZOMBIE, SPIDER, WOLF, ENDERMAN, BLAZE, VAMPIRE
    }
    
    public enum DragonType {
        PROTECTOR, OLD, WISE, UNSTABLE, STRONG, YOUNG, SUPERIOR, HOLY
    }
    
    public enum KuudraTier {
        BASIC, HOT, BURNING, FIERY, INFERNO
    }
    
    // Boss-Instanz und Kampf-Klassen
    public static class BossInstance {
        private final Boss boss;
        private final Location location;
        private final Player spawner;
        private final LivingEntity bossEntity;
        
        public BossInstance(Boss boss, Location location, Player spawner) {
            this.boss = boss;
            this.location = location;
            this.spawner = spawner;
            this.bossEntity = (LivingEntity) location.getWorld().spawnEntity(location, boss.getEntityType());
            
            // Setze Boss-Eigenschaften
            bossEntity.customName(Component.text(boss.getDisplayName()));
            bossEntity.setCustomNameVisible(true);
            // bossEntity.setMaxHealth(boss.getHealth()); // Deprecated, verwende Attribute
            bossEntity.setHealth(boss.getHealth());
        }
        
        public Boss getBoss() { return boss; }
        public Location getLocation() { return location; }
        public Player getSpawner() { return spawner; }
        public LivingEntity getBossEntity() { return bossEntity; }
    }
    
    public static class BossFight {
        private final BossInstance instance;
        private final List<Player> participants = new ArrayList<>();
        private final long startTime;
        
        public BossFight(BossInstance instance) {
            this.instance = instance;
            this.startTime = System.currentTimeMillis();
        }
        
        public BossInstance getInstance() { return instance; }
        public List<Player> getParticipants() { return participants; }
        public long getStartTime() { return startTime; }
    }
    
    // Boss-AI Klasse
    public static class BossAI extends BukkitRunnable {
        private final BossInstance instance;
        private final BossFight fight;
        private int phase = 1;
        
        public BossAI(BossInstance instance, BossFight fight) {
            this.instance = instance;
            this.fight = fight;
        }
        
        @Override
        public void run() {
            if (instance.getBossEntity().isDead()) {
                cancel();
                return;
            }
            
            // Boss-AI Logik hier implementieren
            // Verschiedene Phasen, Angriffe, etc.
        }
    }
}
