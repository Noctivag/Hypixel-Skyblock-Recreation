package de.noctivag.plugin.pets;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Hypixel Skyblock Pets System - All Hypixel Skyblock Pets
 */
public class HypixelSkyblockPetsSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerPetData> playerPetData = new ConcurrentHashMap<>();
    private final Map<PetCategory, List<HypixelPet>> petsByCategory = new HashMap<>();
    private final Map<UUID, BukkitTask> petTasks = new ConcurrentHashMap<>();
    
    public HypixelSkyblockPetsSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeAllPets();
        startPetUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeAllPets() {
        // COMBAT PETS
        petsByCategory.computeIfAbsent(PetCategory.COMBAT, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelPet("ENDERMAN_PET", "Enderman Pet", "§5§lEnderman Pet", Material.ENDERMAN_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.COMBAT, "§7A pet that increases damage to endermen.",
                Arrays.asList("§7Damage: §c+25%", "§7Strength: §c+20%", "§7Crit Damage: §c+15%"),
                Arrays.asList("§7Ability: Enderman Slayer", "§7+25% damage to endermen"),
                Arrays.asList("§7- 1x Enderman Pet", "§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Diamond")),
                
            new HypixelPet("ZOMBIE_PET", "Zombie Pet", "§2§lZombie Pet", Material.ZOMBIE_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A pet that increases damage to zombies.",
                Arrays.asList("§7Damage: §c+20%", "§7Strength: §c+15%", "§7Health: §a+10%"),
                Arrays.asList("§7Ability: Zombie Slayer", "§7+20% damage to zombies"),
                Arrays.asList("§7- 1x Zombie Pet", "§7- 1x Enchanted Rotten Flesh", "§7- 1x Enchanted Iron")),
                
            new HypixelPet("SKELETON_PET", "Skeleton Pet", "§f§lSkeleton Pet", Material.SKELETON_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A pet that increases damage to skeletons.",
                Arrays.asList("§7Damage: §c+20%", "§7Strength: §c+15%", "§7Crit Damage: §c+10%"),
                Arrays.asList("§7Ability: Skeleton Slayer", "§7+20% damage to skeletons"),
                Arrays.asList("§7- 1x Skeleton Pet", "§7- 1x Enchanted Bone", "§7- 1x Enchanted Iron")),
                
            new HypixelPet("SPIDER_PET", "Spider Pet", "§8§lSpider Pet", Material.SPIDER_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A pet that increases damage to spiders.",
                Arrays.asList("§7Damage: §c+20%", "§7Strength: §c+15%", "§7Speed: §f+10%"),
                Arrays.asList("§7Ability: Spider Slayer", "§7+20% damage to spiders"),
                Arrays.asList("§7- 1x Spider Pet", "§7- 1x Enchanted String", "§7- 1x Enchanted Iron")),
                
            new HypixelPet("BLAZE_PET", "Blaze Pet", "§c§lBlaze Pet", Material.BLAZE_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.COMBAT, "§7A pet that increases damage to blazes.",
                Arrays.asList("§7Damage: §c+25%", "§7Strength: §c+20%", "§7Intelligence: §a+15%"),
                Arrays.asList("§7Ability: Blaze Slayer", "§7+25% damage to blazes"),
                Arrays.asList("§7- 1x Blaze Pet", "§7- 1x Enchanted Blaze Rod", "§7- 1x Enchanted Diamond")),
                
            new HypixelPet("WITHER_PET", "Wither Pet", "§8§lWither Pet", Material.WITHER_SKELETON_SKULL,
                PetRarity.MYTHIC, PetCategory.COMBAT, "§7A pet that increases damage to withers.",
                Arrays.asList("§7Damage: §c+30%", "§7Strength: §c+25%", "§7Intelligence: §a+20%"),
                Arrays.asList("§7Ability: Wither Slayer", "§7+30% damage to withers"),
                Arrays.asList("§7- 1x Wither Pet", "§7- 1x Wither Skeleton Skull", "§7- 1x Enchanted Diamond Block"))
        ));
        
        // MINING PETS
        petsByCategory.computeIfAbsent(PetCategory.MINING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelPet("ROCK_PET", "Rock Pet", "§7§lRock Pet", Material.STONE,
                PetRarity.COMMON, PetCategory.MINING, "§7A pet that increases mining speed.",
                Arrays.asList("§7Mining Speed: §a+10%", "§7Mining Fortune: §a+5%", "§7Defense: §a+5%"),
                Arrays.asList("§7Ability: Mining Boost", "§7+10% mining speed"),
                Arrays.asList("§7- 1x Rock Pet", "§7- 1x Enchanted Stone", "§7- 1x Enchanted Coal")),
                
            new HypixelPet("SILVERFISH_PET", "Silverfish Pet", "§f§lSilverfish Pet", Material.SILVERFISH_SPAWN_EGG,
                PetRarity.UNCOMMON, PetCategory.MINING, "§7A pet that increases mining speed.",
                Arrays.asList("§7Mining Speed: §a+15%", "§7Mining Fortune: §a+10%", "§7Defense: §a+8%"),
                Arrays.asList("§7Ability: Mining Boost", "§7+15% mining speed"),
                Arrays.asList("§7- 1x Silverfish Pet", "§7- 1x Enchanted Stone", "§7- 1x Enchanted Iron")),
                
            new HypixelPet("MOLES_PET", "Mole Pet", "§6§lMole Pet", Material.RABBIT_SPAWN_EGG,
                PetRarity.RARE, PetCategory.MINING, "§7A pet that increases mining speed.",
                Arrays.asList("§7Mining Speed: §a+20%", "§7Mining Fortune: §a+15%", "§7Defense: §a+10%"),
                Arrays.asList("§7Ability: Mining Boost", "§7+20% mining speed"),
                Arrays.asList("§7- 1x Mole Pet", "§7- 1x Enchanted Stone", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("BAL_PET", "Bal Pet", "§5§lBal Pet", Material.ENDERMAN_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.MINING, "§7A pet that increases mining speed.",
                Arrays.asList("§7Mining Speed: §a+25%", "§7Mining Fortune: §a+20%", "§7Defense: §a+15%"),
                Arrays.asList("§7Ability: Mining Boost", "§7+25% mining speed"),
                Arrays.asList("§7- 1x Bal Pet", "§7- 1x Enchanted Stone", "§7- 1x Enchanted Diamond")),
                
            new HypixelPet("GOLEM_PET", "Golem Pet", "§7§lGolem Pet", Material.IRON_GOLEM_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.MINING, "§7A pet that increases mining speed.",
                Arrays.asList("§7Mining Speed: §a+22%", "§7Mining Fortune: §a+18%", "§7Defense: §a+12%"),
                Arrays.asList("§7Ability: Mining Boost", "§7+22% mining speed"),
                Arrays.asList("§7- 1x Golem Pet", "§7- 1x Enchanted Stone", "§7- 1x Enchanted Iron"))
        ));
        
        // FARMING PETS
        petsByCategory.computeIfAbsent(PetCategory.FARMING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelPet("RABBIT_PET", "Rabbit Pet", "§f§lRabbit Pet", Material.RABBIT_SPAWN_EGG,
                PetRarity.COMMON, PetCategory.FARMING, "§7A pet that increases farming speed.",
                Arrays.asList("§7Farming Speed: §a+10%", "§7Farming Fortune: §a+5%", "§7Health: §a+5%"),
                Arrays.asList("§7Ability: Farming Boost", "§7+10% farming speed"),
                Arrays.asList("§7- 1x Rabbit Pet", "§7- 1x Enchanted Carrot", "§7- 1x Enchanted Wheat")),
                
            new HypixelPet("ELEPHANT_PET", "Elephant Pet", "§7§lElephant Pet", Material.VILLAGER_SPAWN_EGG,
                PetRarity.UNCOMMON, PetCategory.FARMING, "§7A pet that increases farming speed.",
                Arrays.asList("§7Farming Speed: §a+15%", "§7Farming Fortune: §a+10%", "§7Health: §a+8%"),
                Arrays.asList("§7Ability: Farming Boost", "§7+15% farming speed"),
                Arrays.asList("§7- 1x Elephant Pet", "§7- 1x Enchanted Carrot", "§7- 1x Enchanted Potato")),
                
            new HypixelPet("MONKEY_PET", "Monkey Pet", "§6§lMonkey Pet", Material.OCELOT_SPAWN_EGG,
                PetRarity.RARE, PetCategory.FARMING, "§7A pet that increases farming speed.",
                Arrays.asList("§7Farming Speed: §a+20%", "§7Farming Fortune: §a+15%", "§7Health: §a+10%"),
                Arrays.asList("§7Ability: Farming Boost", "§7+20% farming speed"),
                Arrays.asList("§7- 1x Monkey Pet", "§7- 1x Enchanted Carrot", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("BEE_PET", "Bee Pet", "§e§lBee Pet", Material.BEE_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.FARMING, "§7A pet that increases farming speed.",
                Arrays.asList("§7Farming Speed: §a+22%", "§7Farming Fortune: §a+18%", "§7Health: §a+12%"),
                Arrays.asList("§7Ability: Farming Boost", "§7+22% farming speed"),
                Arrays.asList("§7- 1x Bee Pet", "§7- 1x Enchanted Carrot", "§7- 1x Enchanted Diamond")),
                
            new HypixelPet("BABY_YETI_PET", "Baby Yeti Pet", "§b§lBaby Yeti Pet", Material.POLAR_BEAR_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.FARMING, "§7A pet that increases farming speed.",
                Arrays.asList("§7Farming Speed: §a+25%", "§7Farming Fortune: §a+20%", "§7Health: §a+15%"),
                Arrays.asList("§7Ability: Farming Boost", "§7+25% farming speed"),
                Arrays.asList("§7- 1x Baby Yeti Pet", "§7- 1x Enchanted Carrot", "§7- 1x Enchanted Diamond Block"))
        ));
        
        // FISHING PETS
        petsByCategory.computeIfAbsent(PetCategory.FISHING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelPet("SQUID_PET", "Squid Pet", "§8§lSquid Pet", Material.SQUID_SPAWN_EGG,
                PetRarity.COMMON, PetCategory.FISHING, "§7A pet that increases fishing speed.",
                Arrays.asList("§7Fishing Speed: §a+10%", "§7Sea Creature Chance: §a+5%", "§7Health: §a+5%"),
                Arrays.asList("§7Ability: Fishing Boost", "§7+10% fishing speed"),
                Arrays.asList("§7- 1x Squid Pet", "§7- 1x Enchanted Ink Sac", "§7- 1x Enchanted String")),
                
            new HypixelPet("GUARDIAN_PET", "Guardian Pet", "§b§lGuardian Pet", Material.GUARDIAN_SPAWN_EGG,
                PetRarity.UNCOMMON, PetCategory.FISHING, "§7A pet that increases fishing speed.",
                Arrays.asList("§7Fishing Speed: §a+15%", "§7Sea Creature Chance: §a+10%", "§7Health: §a+8%"),
                Arrays.asList("§7Ability: Fishing Boost", "§7+15% fishing speed"),
                Arrays.asList("§7- 1x Guardian Pet", "§7- 1x Enchanted Prismarine", "§7- 1x Enchanted String")),
                
            new HypixelPet("DOLPHIN_PET", "Dolphin Pet", "§b§lDolphin Pet", Material.DOLPHIN_SPAWN_EGG,
                PetRarity.RARE, PetCategory.FISHING, "§7A pet that increases fishing speed.",
                Arrays.asList("§7Fishing Speed: §a+20%", "§7Sea Creature Chance: §a+15%", "§7Health: §a+10%"),
                Arrays.asList("§7Ability: Fishing Boost", "§7+20% fishing speed"),
                Arrays.asList("§7- 1x Dolphin Pet", "§7- 1x Enchanted Prismarine", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("BLUE_WHALE_PET", "Blue Whale Pet", "§9§lBlue Whale Pet", Material.COD,
                PetRarity.EPIC, PetCategory.FISHING, "§7A pet that increases fishing speed.",
                Arrays.asList("§7Fishing Speed: §a+22%", "§7Sea Creature Chance: §a+18%", "§7Health: §a+12%"),
                Arrays.asList("§7Ability: Fishing Boost", "§7+22% fishing speed"),
                Arrays.asList("§7- 1x Blue Whale Pet", "§7- 1x Enchanted Prismarine", "§7- 1x Enchanted Diamond")),
                
            new HypixelPet("MEGALODON_PET", "Megalodon Pet", "§5§lMegalodon Pet", Material.SALMON,
                PetRarity.LEGENDARY, PetCategory.FISHING, "§7A pet that increases fishing speed.",
                Arrays.asList("§7Fishing Speed: §a+25%", "§7Sea Creature Chance: §a+20%", "§7Health: §a+15%"),
                Arrays.asList("§7Ability: Fishing Boost", "§7+25% fishing speed"),
                Arrays.asList("§7- 1x Megalodon Pet", "§7- 1x Enchanted Prismarine", "§7- 1x Enchanted Diamond Block"))
        ));
        
        // FORAGING PETS
        petsByCategory.computeIfAbsent(PetCategory.FORAGING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelPet("OCELOT_PET", "Ocelot Pet", "§6§lOcelot Pet", Material.OCELOT_SPAWN_EGG,
                PetRarity.COMMON, PetCategory.FORAGING, "§7A pet that increases foraging speed.",
                Arrays.asList("§7Foraging Speed: §a+10%", "§7Foraging Fortune: §a+5%", "§7Speed: §f+5%"),
                Arrays.asList("§7Ability: Foraging Boost", "§7+10% foraging speed"),
                Arrays.asList("§7- 1x Ocelot Pet", "§7- 1x Enchanted Oak Log", "§7- 1x Enchanted String")),
                
            new HypixelPet("LION_PET", "Lion Pet", "§e§lLion Pet", Material.OCELOT_SPAWN_EGG,
                PetRarity.UNCOMMON, PetCategory.FORAGING, "§7A pet that increases foraging speed.",
                Arrays.asList("§7Foraging Speed: §a+15%", "§7Foraging Fortune: §a+10%", "§7Speed: §f+8%"),
                Arrays.asList("§7Ability: Foraging Boost", "§7+15% foraging speed"),
                Arrays.asList("§7- 1x Lion Pet", "§7- 1x Enchanted Oak Log", "§7- 1x Enchanted Iron")),
                
            new HypixelPet("TIGER_PET", "Tiger Pet", "§6§lTiger Pet", Material.OCELOT_SPAWN_EGG,
                PetRarity.RARE, PetCategory.FORAGING, "§7A pet that increases foraging speed.",
                Arrays.asList("§7Foraging Speed: §a+20%", "§7Foraging Fortune: §a+15%", "§7Speed: §f+10%"),
                Arrays.asList("§7Ability: Foraging Boost", "§7+20% foraging speed"),
                Arrays.asList("§7- 1x Tiger Pet", "§7- 1x Enchanted Oak Log", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("GRIFFIN_PET", "Griffin Pet", "§e§lGriffin Pet", Material.PHANTOM_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.FORAGING, "§7A pet that increases foraging speed.",
                Arrays.asList("§7Foraging Speed: §a+22%", "§7Foraging Fortune: §a+18%", "§7Speed: §f+12%"),
                Arrays.asList("§7Ability: Foraging Boost", "§7+22% foraging speed"),
                Arrays.asList("§7- 1x Griffin Pet", "§7- 1x Enchanted Oak Log", "§7- 1x Enchanted Diamond")),
                
            new HypixelPet("PHOENIX_PET", "Phoenix Pet", "§c§lPhoenix Pet", Material.PHANTOM_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.FORAGING, "§7A pet that increases foraging speed.",
                Arrays.asList("§7Foraging Speed: §a+25%", "§7Foraging Fortune: §a+20%", "§7Speed: §f+15%"),
                Arrays.asList("§7Ability: Foraging Boost", "§7+25% foraging speed"),
                Arrays.asList("§7- 1x Phoenix Pet", "§7- 1x Enchanted Oak Log", "§7- 1x Enchanted Diamond Block"))
        ));
        
        // SPECIAL PETS
        petsByCategory.computeIfAbsent(PetCategory.SPECIAL, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelPet("DRAGON_PET", "Dragon Pet", "§6§lDragon Pet", Material.DRAGON_EGG,
                PetRarity.MYTHIC, PetCategory.SPECIAL, "§7A legendary dragon pet.",
                Arrays.asList("§7Damage: §c+35%", "§7Strength: §c+30%", "§7Intelligence: §a+25%"),
                Arrays.asList("§7Ability: Dragon's Fury", "§7+35% damage to dragons"),
                Arrays.asList("§7- 1x Dragon Pet", "§7- 1x Dragon Egg", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelPet("GOLDEN_DRAGON_PET", "Golden Dragon Pet", "§6§lGolden Dragon Pet", Material.DRAGON_EGG,
                PetRarity.MYTHIC, PetCategory.SPECIAL, "§7A legendary golden dragon pet.",
                Arrays.asList("§7Damage: §c+40%", "§7Strength: §c+35%", "§7Intelligence: §a+30%"),
                Arrays.asList("§7Ability: Golden Dragon's Fury", "§7+40% damage to dragons"),
                Arrays.asList("§7- 1x Golden Dragon Pet", "§7- 1x Dragon Egg", "§7- 1x Enchanted Gold Block")),
                
            new HypixelPet("ENDER_DRAGON_PET", "Ender Dragon Pet", "§5§lEnder Dragon Pet", Material.DRAGON_EGG,
                PetRarity.MYTHIC, PetCategory.SPECIAL, "§7A legendary ender dragon pet.",
                Arrays.asList("§7Damage: §c+45%", "§7Strength: §c+40%", "§7Intelligence: §a+35%"),
                Arrays.asList("§7Ability: Ender Dragon's Fury", "§7+45% damage to dragons"),
                Arrays.asList("§7- 1x Ender Dragon Pet", "§7- 1x Dragon Egg", "§7- 1x Enchanted Ender Pearl")),
                
            new HypixelPet("WISE_DRAGON_PET", "Wise Dragon Pet", "§b§lWise Dragon Pet", Material.DRAGON_EGG,
                PetRarity.MYTHIC, PetCategory.SPECIAL, "§7A legendary wise dragon pet.",
                Arrays.asList("§7Damage: §c+40%", "§7Strength: §c+35%", "§7Intelligence: §a+40%"),
                Arrays.asList("§7Ability: Wise Dragon's Fury", "§7+40% damage to dragons"),
                Arrays.asList("§7- 1x Wise Dragon Pet", "§7- 1x Dragon Egg", "§7- 1x Enchanted Lapis")),
                
            new HypixelPet("UNSTABLE_DRAGON_PET", "Unstable Dragon Pet", "§d§lUnstable Dragon Pet", Material.DRAGON_EGG,
                PetRarity.MYTHIC, PetCategory.SPECIAL, "§7A legendary unstable dragon pet.",
                Arrays.asList("§7Damage: §c+42%", "§7Strength: §c+37%", "§7Intelligence: §a+32%"),
                Arrays.asList("§7Ability: Unstable Dragon's Fury", "§7+42% damage to dragons"),
                Arrays.asList("§7- 1x Unstable Dragon Pet", "§7- 1x Dragon Egg", "§7- 1x Enchanted Redstone")),
                
            new HypixelPet("YOUNG_DRAGON_PET", "Young Dragon Pet", "§a§lYoung Dragon Pet", Material.DRAGON_EGG,
                PetRarity.LEGENDARY, PetCategory.SPECIAL, "§7A young dragon pet with speed bonuses.",
                Arrays.asList("§7Speed: §f+30%", "§7Damage: §c+25%", "§7Strength: §c+20%"),
                Arrays.asList("§7Ability: Young Dragon's Speed", "§7+30% movement speed"),
                Arrays.asList("§7- 1x Young Dragon Pet", "§7- 1x Dragon Egg", "§7- 1x Enchanted Feather")),
                
            new HypixelPet("OLD_DRAGON_PET", "Old Dragon Pet", "§7§lOld Dragon Pet", Material.DRAGON_EGG,
                PetRarity.LEGENDARY, PetCategory.SPECIAL, "§7An old dragon pet with defense bonuses.",
                Arrays.asList("§7Defense: §a+40%", "§7Health: §a+30%", "§7Damage: §c+20%"),
                Arrays.asList("§7Ability: Old Dragon's Defense", "§7+40% defense"),
                Arrays.asList("§7- 1x Old Dragon Pet", "§7- 1x Dragon Egg", "§7- 1x Enchanted Iron")),
                
            new HypixelPet("PROTECTOR_DRAGON_PET", "Protector Dragon Pet", "§e§lProtector Dragon Pet", Material.DRAGON_EGG,
                PetRarity.LEGENDARY, PetCategory.SPECIAL, "§7A protector dragon pet with defense bonuses.",
                Arrays.asList("§7Defense: §a+35%", "§7Health: §a+25%", "§7Damage: §c+15%"),
                Arrays.asList("§7Ability: Protector Dragon's Shield", "§7+35% defense"),
                Arrays.asList("§7- 1x Protector Dragon Pet", "§7- 1x Dragon Egg", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("SUPERIOR_DRAGON_PET", "Superior Dragon Pet", "§6§lSuperior Dragon Pet", Material.DRAGON_EGG,
                PetRarity.MYTHIC, PetCategory.SPECIAL, "§7The most powerful dragon pet.",
                Arrays.asList("§7All Stats: §c+10%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+15%"),
                Arrays.asList("§7Ability: Superior Dragon's Power", "§7+10% to all stats"),
                Arrays.asList("§7- 1x Superior Dragon Pet", "§7- 1x Dragon Egg", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelPet("BABY_YETI_PET", "Baby Yeti Pet", "§b§lBaby Yeti Pet", Material.POLAR_BEAR_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.SPECIAL, "§7A baby yeti pet with strength bonuses.",
                Arrays.asList("§7Strength: §c+30%", "§7Defense: §a+20%", "§7Health: §a+15%"),
                Arrays.asList("§7Ability: Baby Yeti's Strength", "§7+30% strength"),
                Arrays.asList("§7- 1x Baby Yeti Pet", "§7- 1x Enchanted Ice", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelPet("SNOWMAN_PET", "Snowman Pet", "§f§lSnowman Pet", Material.SNOW_GOLEM_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.SPECIAL, "§7A snowman pet with ice abilities.",
                Arrays.asList("§7Intelligence: §a+25%", "§7Defense: §a+15%", "§7Health: §a+10%"),
                Arrays.asList("§7Ability: Snowman's Ice", "§7+25% intelligence"),
                Arrays.asList("§7- 1x Snowman Pet", "§7- 1x Enchanted Snow", "§7- 1x Enchanted Diamond")),
                
            new HypixelPet("GOLEM_PET", "Golem Pet", "§7§lGolem Pet", Material.IRON_GOLEM_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.SPECIAL, "§7A golem pet with defense bonuses.",
                Arrays.asList("§7Defense: §a+30%", "§7Health: §a+20%", "§7Strength: §c+15%"),
                Arrays.asList("§7Ability: Golem's Defense", "§7+30% defense"),
                Arrays.asList("§7- 1x Golem Pet", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Diamond")),
                
            new HypixelPet("TIGER_PET", "Tiger Pet", "§6§lTiger Pet", Material.OCELOT_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.SPECIAL, "§7A tiger pet with ferocity bonuses.",
                Arrays.asList("§7Ferocity: §c+25%", "§7Damage: §c+20%", "§7Speed: §f+15%"),
                Arrays.asList("§7Ability: Tiger's Ferocity", "§7+25% ferocity"),
                Arrays.asList("§7- 1x Tiger Pet", "§7- 1x Enchanted Raw Beef", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelPet("LION_PET", "Lion Pet", "§e§lLion Pet", Material.OCELOT_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.SPECIAL, "§7A lion pet with strength bonuses.",
                Arrays.asList("§7Strength: §c+25%", "§7Damage: §c+20%", "§7Health: §a+15%"),
                Arrays.asList("§7Ability: Lion's Strength", "§7+25% strength"),
                Arrays.asList("§7- 1x Lion Pet", "§7- 1x Enchanted Raw Beef", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelPet("WOLF_PET", "Wolf Pet", "§7§lWolf Pet", Material.WOLF_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.SPECIAL, "§7A wolf pet with combat bonuses.",
                Arrays.asList("§7Damage: §c+20%", "§7Strength: §c+15%", "§7Speed: §f+10%"),
                Arrays.asList("§7Ability: Wolf's Pack", "§7+20% damage"),
                Arrays.asList("§7- 1x Wolf Pet", "§7- 1x Enchanted Bone", "§7- 1x Enchanted Diamond")),
                
            new HypixelPet("HORSE_PET", "Horse Pet", "§6§lHorse Pet", Material.HORSE_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.SPECIAL, "§7A horse pet with speed bonuses.",
                Arrays.asList("§7Speed: §f+25%", "§7Health: §a+15%", "§7Defense: §a+10%"),
                Arrays.asList("§7Ability: Horse's Speed", "§7+25% movement speed"),
                Arrays.asList("§7- 1x Horse Pet", "§7- 1x Enchanted Hay Bale", "§7- 1x Enchanted Diamond")),
                
            new HypixelPet("PHOENIX_PET", "Phoenix Pet", "§c§lPhoenix Pet", Material.PHANTOM_SPAWN_EGG,
                PetRarity.MYTHIC, PetCategory.SPECIAL, "§7A phoenix pet with fire abilities.",
                Arrays.asList("§7Intelligence: §a+40%", "§7Magic Find: §a+20%", "§7Pet Luck: §a+20%"),
                Arrays.asList("§7Ability: Phoenix's Rebirth", "§7+40% intelligence"),
                Arrays.asList("§7- 1x Phoenix Pet", "§7- 1x Enchanted Blaze Rod", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelPet("GRIFFIN_PET", "Griffin Pet", "§e§lGriffin Pet", Material.PHANTOM_SPAWN_EGG,
                PetRarity.MYTHIC, PetCategory.SPECIAL, "§7A griffin pet with balanced bonuses.",
                Arrays.asList("§7All Stats: §c+15%", "§7Magic Find: §a+25%", "§7Pet Luck: §a+25%"),
                Arrays.asList("§7Ability: Griffin's Balance", "§7+15% to all stats"),
                Arrays.asList("§7- 1x Griffin Pet", "§7- 1x Enchanted Feather", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelPet("BABY_DRAGON_PET", "Baby Dragon Pet", "§5§lBaby Dragon Pet", Material.DRAGON_EGG,
                PetRarity.LEGENDARY, PetCategory.SPECIAL, "§7A baby dragon with magical powers.",
                Arrays.asList("§7Intelligence: §a+30%", "§7Magic Find: §a+20%", "§7Pet Luck: §a+15%"),
                Arrays.asList("§7Ability: Dragon's Breath", "§7Ability: Dragon's Fury", "§7Ability: Dragon's Wisdom"),
                Arrays.asList("§7- 1x Dragon Egg", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Emerald Block")),
                
            new HypixelPet("ENDERMAN_PET", "Enderman Pet", "§5§lEnderman Pet", Material.ENDERMAN_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A mysterious enderman companion.",
                Arrays.asList("§7Intelligence: §a+25%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+10%"),
                Arrays.asList("§7Ability: Teleport", "§7Ability: Enderman's Rage", "§7Ability: Void Walker"),
                Arrays.asList("§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("BLAZE_PET", "Blaze Pet", "§c§lBlaze Pet", Material.BLAZE_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A fiery blaze companion.",
                Arrays.asList("§7Intelligence: §a+30%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+10%"),
                Arrays.asList("§7Ability: Fireball", "§7Ability: Blaze's Fury", "§7Ability: Fire Walker"),
                Arrays.asList("§7- 1x Enchanted Blaze Rod", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("SKELETON_PET", "Skeleton Pet", "§7§lSkeleton Pet", Material.SKELETON_SPAWN_EGG,
                PetRarity.RARE, PetCategory.COMBAT, "§7A skeletal companion.",
                Arrays.asList("§7Intelligence: §a+20%", "§7Magic Find: §a+10%", "§7Pet Luck: §a+5%"),
                Arrays.asList("§7Ability: Bone Throw", "§7Ability: Skeleton's Rage", "§7Ability: Bone Walker"),
                Arrays.asList("§7- 1x Enchanted Bone", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("ZOMBIE_PET", "Zombie Pet", "§2§lZombie Pet", Material.ZOMBIE_SPAWN_EGG,
                PetRarity.RARE, PetCategory.COMBAT, "§7A zombie companion.",
                Arrays.asList("§7Intelligence: §a+20%", "§7Magic Find: §a+10%", "§7Pet Luck: §a+5%"),
                Arrays.asList("§7Ability: Zombie's Bite", "§7Ability: Zombie's Rage", "§7Ability: Undead Walker"),
                Arrays.asList("§7- 1x Enchanted Rotten Flesh", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("SPIDER_PET", "Spider Pet", "§8§lSpider Pet", Material.SPIDER_SPAWN_EGG,
                PetRarity.RARE, PetCategory.COMBAT, "§7A spider companion.",
                Arrays.asList("§7Intelligence: §a+20%", "§7Magic Find: §a+10%", "§7Pet Luck: §a+5%"),
                Arrays.asList("§7Ability: Web Shot", "§7Ability: Spider's Rage", "§7Ability: Web Walker"),
                Arrays.asList("§7- 1x Enchanted String", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("CREEPER_PET", "Creeper Pet", "§a§lCreeper Pet", Material.CREEPER_SPAWN_EGG,
                PetRarity.RARE, PetCategory.COMBAT, "§7A creeper companion.",
                Arrays.asList("§7Intelligence: §a+20%", "§7Magic Find: §a+10%", "§7Pet Luck: §a+5%"),
                Arrays.asList("§7Ability: Explosion", "§7Ability: Creeper's Rage", "§7Ability: Explosive Walker"),
                Arrays.asList("§7- 1x Enchanted Gunpowder", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("WITCH_PET", "Witch Pet", "§5§lWitch Pet", Material.WITCH_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A witch companion.",
                Arrays.asList("§7Intelligence: §a+30%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+10%"),
                Arrays.asList("§7Ability: Potion Throw", "§7Ability: Witch's Rage", "§7Ability: Magic Walker"),
                Arrays.asList("§7- 1x Enchanted Redstone", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("WITHER_SKELETON_PET", "Wither Skeleton Pet", "§8§lWither Skeleton Pet", Material.WITHER_SKELETON_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.COMBAT, "§7A wither skeleton companion.",
                Arrays.asList("§7Intelligence: §a+35%", "§7Magic Find: §a+20%", "§7Pet Luck: §a+15%"),
                Arrays.asList("§7Ability: Wither Effect", "§7Ability: Wither's Rage", "§7Ability: Wither Walker"),
                Arrays.asList("§7- 1x Enchanted Wither Skeleton Skull", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Gold Block")),
                
            new HypixelPet("GUARDIAN_PET", "Guardian Pet", "§b§lGuardian Pet", Material.GUARDIAN_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A guardian companion.",
                Arrays.asList("§7Intelligence: §a+30%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+10%"),
                Arrays.asList("§7Ability: Laser Beam", "§7Ability: Guardian's Rage", "§7Ability: Ocean Walker"),
                Arrays.asList("§7- 1x Enchanted Prismarine", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("ELDER_GUARDIAN_PET", "Elder Guardian Pet", "§b§lElder Guardian Pet", Material.ELDER_GUARDIAN_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.COMBAT, "§7An elder guardian companion.",
                Arrays.asList("§7Intelligence: §a+35%", "§7Magic Find: §a+20%", "§7Pet Luck: §a+15%"),
                Arrays.asList("§7Ability: Elder Laser", "§7Ability: Elder's Rage", "§7Ability: Ocean Master"),
                Arrays.asList("§7- 1x Enchanted Prismarine Block", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Gold Block")),
                
            new HypixelPet("SHULKER_PET", "Shulker Pet", "§d§lShulker Pet", Material.SHULKER_SPAWN_EGG,
                PetRarity.LEGENDARY, PetCategory.COMBAT, "§7A shulker companion.",
                Arrays.asList("§7Intelligence: §a+35%", "§7Magic Find: §a+20%", "§7Pet Luck: §a+15%"),
                Arrays.asList("§7Ability: Levitation", "§7Ability: Shulker's Rage", "§7Ability: End Walker"),
                Arrays.asList("§7- 1x Enchanted Shulker Shell", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Gold Block")),
                
            new HypixelPet("ENDERMITE_PET", "Endermite Pet", "§5§lEndermite Pet", Material.ENDERMITE_SPAWN_EGG,
                PetRarity.RARE, PetCategory.COMBAT, "§7An endermite companion.",
                Arrays.asList("§7Intelligence: §a+20%", "§7Magic Find: §a+10%", "§7Pet Luck: §a+5%"),
                Arrays.asList("§7Ability: Teleport", "§7Ability: Endermite's Rage", "§7Ability: End Walker"),
                Arrays.asList("§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("SILVERFISH_PET", "Silverfish Pet", "§7§lSilverfish Pet", Material.SILVERFISH_SPAWN_EGG,
                PetRarity.RARE, PetCategory.COMBAT, "§7A silverfish companion.",
                Arrays.asList("§7Intelligence: §a+20%", "§7Magic Find: §a+10%", "§7Pet Luck: §a+5%"),
                Arrays.asList("§7Ability: Burrow", "§7Ability: Silverfish's Rage", "§7Ability: Stone Walker"),
                Arrays.asList("§7- 1x Enchanted Stone", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("CAVE_SPIDER_PET", "Cave Spider Pet", "§8§lCave Spider Pet", Material.CAVE_SPIDER_SPAWN_EGG,
                PetRarity.RARE, PetCategory.COMBAT, "§7A cave spider companion.",
                Arrays.asList("§7Intelligence: §a+20%", "§7Magic Find: §a+10%", "§7Pet Luck: §a+5%"),
                Arrays.asList("§7Ability: Poison", "§7Ability: Cave Spider's Rage", "§7Ability: Cave Walker"),
                Arrays.asList("§7- 1x Enchanted String", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("MAGMA_CUBE_PET", "Magma Cube Pet", "§c§lMagma Cube Pet", Material.MAGMA_CUBE_SPAWN_EGG,
                PetRarity.RARE, PetCategory.COMBAT, "§7A magma cube companion.",
                Arrays.asList("§7Intelligence: §a+20%", "§7Magic Find: §a+10%", "§7Pet Luck: §a+5%"),
                Arrays.asList("§7Ability: Fire Jump", "§7Ability: Magma Cube's Rage", "§7Ability: Fire Walker"),
                Arrays.asList("§7- 1x Enchanted Magma Cream", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("SLIME_PET", "Slime Pet", "§a§lSlime Pet", Material.SLIME_SPAWN_EGG,
                PetRarity.RARE, PetCategory.COMBAT, "§7A slime companion.",
                Arrays.asList("§7Intelligence: §a+20%", "§7Magic Find: §a+10%", "§7Pet Luck: §a+5%"),
                Arrays.asList("§7Ability: Bounce", "§7Ability: Slime's Rage", "§7Ability: Bouncy Walker"),
                Arrays.asList("§7- 1x Enchanted Slime Ball", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("GHAST_PET", "Ghast Pet", "§f§lGhast Pet", Material.GHAST_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A ghast companion.",
                Arrays.asList("§7Intelligence: §a+30%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+10%"),
                Arrays.asList("§7Ability: Fireball", "§7Ability: Ghast's Rage", "§7Ability: Sky Walker"),
                Arrays.asList("§7- 1x Enchanted Ghast Tear", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("PIGMAN_PET", "Pigman Pet", "§6§lPigman Pet", Material.ZOMBIFIED_PIGLIN_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A pigman companion.",
                Arrays.asList("§7Intelligence: §a+30%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+10%"),
                Arrays.asList("§7Ability: Gold Sword", "§7Ability: Pigman's Rage", "§7Ability: Nether Walker"),
                Arrays.asList("§7- 1x Enchanted Gold", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("PIGLIN_PET", "Piglin Pet", "§6§lPiglin Pet", Material.PIGLIN_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A piglin companion.",
                Arrays.asList("§7Intelligence: §a+30%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+10%"),
                Arrays.asList("§7Ability: Gold Lover", "§7Ability: Piglin's Rage", "§7Ability: Nether Walker"),
                Arrays.asList("§7- 1x Enchanted Gold", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("HOGLIN_PET", "Hoglin Pet", "§c§lHoglin Pet", Material.HOGLIN_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A hoglin companion.",
                Arrays.asList("§7Intelligence: §a+30%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+10%"),
                Arrays.asList("§7Ability: Charge", "§7Ability: Hoglin's Rage", "§7Ability: Nether Walker"),
                Arrays.asList("§7- 1x Enchanted Leather", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("STRIDER_PET", "Strider Pet", "§c§lStrider Pet", Material.STRIDER_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A strider companion.",
                Arrays.asList("§7Intelligence: §a+30%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+10%"),
                Arrays.asList("§7Ability: Lava Walk", "§7Ability: Strider's Rage", "§7Ability: Lava Walker"),
                Arrays.asList("§7- 1x Enchanted Warped Fungus", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("ZOGLIN_PET", "Zoglin Pet", "§c§lZoglin Pet", Material.ZOGLIN_SPAWN_EGG,
                PetRarity.EPIC, PetCategory.COMBAT, "§7A zoglin companion.",
                Arrays.asList("§7Intelligence: §a+30%", "§7Magic Find: §a+15%", "§7Pet Luck: §a+10%"),
                Arrays.asList("§7Ability: Rage", "§7Ability: Zoglin's Rage", "§7Ability: Nether Walker"),
                Arrays.asList("§7- 1x Enchanted Leather", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelPet("WITHER_PET", "Wither Pet", "§8§lWither Pet", Material.WITHER_SKELETON_SKULL,
                PetRarity.MYTHIC, PetCategory.COMBAT, "§7A wither companion.",
                Arrays.asList("§7Intelligence: §a+40%", "§7Magic Find: §a+25%", "§7Pet Luck: §a+20%"),
                Arrays.asList("§7Ability: Wither Effect", "§7Ability: Wither's Rage", "§7Ability: Wither Walker"),
                Arrays.asList("§7- 1x Wither Skeleton Skull", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Gold Block")),
                
            new HypixelPet("ENDER_DRAGON_PET", "Ender Dragon Pet", "§5§lEnder Dragon Pet", Material.DRAGON_EGG,
                PetRarity.MYTHIC, PetCategory.COMBAT, "§7An ender dragon companion.",
                Arrays.asList("§7Intelligence: §a+40%", "§7Magic Find: §a+25%", "§7Pet Luck: §a+20%"),
                Arrays.asList("§7Ability: Dragon's Breath", "§7Ability: Dragon's Rage", "§7Ability: Dragon Walker"),
                Arrays.asList("§7- 1x Dragon Egg", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Gold Block"))
        ));
    }
    
    private void startPetUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerPetData> entry : playerPetData.entrySet()) {
                    PlayerPetData petData = entry.getValue();
                    petData.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 60L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Pet") || displayName.contains("Hypixel")) {
            openPetsGUI(player);
        }
    }
    
    public void openPetsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lHypixel Skyblock Pets");
        
        addGUIItem(gui, 10, Material.IRON_SWORD, "§c§lCombat Pets", "§7View all combat pets");
        addGUIItem(gui, 11, Material.STONE, "§7§lMining Pets", "§7View all mining pets");
        addGUIItem(gui, 12, Material.CARROT, "§a§lFarming Pets", "§7View all farming pets");
        addGUIItem(gui, 13, Material.FISHING_ROD, "§b§lFishing Pets", "§7View all fishing pets");
        addGUIItem(gui, 14, Material.OAK_LOG, "§2§lForaging Pets", "§7View all foraging pets");
        addGUIItem(gui, 15, Material.DRAGON_EGG, "§d§lSpecial Pets", "§7View all special pets");
        
        player.openInventory(gui);
        player.sendMessage("§aHypixel Skyblock Pets GUI geöffnet!");
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerPetData getPlayerPetData(UUID playerId) {
        return playerPetData.computeIfAbsent(playerId, k -> new PlayerPetData(playerId));
    }
    
    public enum PetCategory {
        COMBAT("§cCombat", "§7Combat-focused pets"),
        MINING("§7Mining", "§7Mining-focused pets"),
        FARMING("§aFarming", "§7Farming-focused pets"),
        FISHING("§bFishing", "§7Fishing-focused pets"),
        FORAGING("§2Foraging", "§7Foraging-focused pets"),
        SPECIAL("§dSpecial", "§7Special and legendary pets");
        
        private final String displayName;
        private final String description;
        
        PetCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum PetRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        PetRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class HypixelPet {
        private final String id;
        private final String name;
        private final String displayName;
        private final Material material;
        private final PetRarity rarity;
        private final PetCategory category;
        private final String description;
        private final List<String> stats;
        private final List<String> abilities;
        private final List<String> craftingMaterials;
        
        public HypixelPet(String id, String name, String displayName, Material material,
                         PetRarity rarity, PetCategory category, String description,
                         List<String> stats, List<String> abilities, List<String> craftingMaterials) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.rarity = rarity;
            this.category = category;
            this.description = description;
            this.stats = stats;
            this.abilities = abilities;
            this.craftingMaterials = craftingMaterials;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public PetRarity getRarity() { return rarity; }
        public PetCategory getCategory() { return category; }
        public String getDescription() { return description; }
        public List<String> getStats() { return stats; }
        public List<String> getAbilities() { return abilities; }
        public List<String> getCraftingMaterials() { return craftingMaterials; }
    }
    
    public static class PlayerPetData {
        private final UUID playerId;
        private final Map<String, Integer> petCounts = new HashMap<>();
        private final Map<String, Boolean> petUnlocked = new HashMap<>();
        private long lastUpdate;
        
        public PlayerPetData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addPet(String petId, int amount) {
            petCounts.put(petId, petCounts.getOrDefault(petId, 0) + amount);
        }
        
        public void unlockPet(String petId) {
            petUnlocked.put(petId, true);
        }
        
        public int getPetCount(String petId) {
            return petCounts.getOrDefault(petId, 0);
        }
        
        public boolean isPetUnlocked(String petId) {
            return petUnlocked.getOrDefault(petId, false);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
