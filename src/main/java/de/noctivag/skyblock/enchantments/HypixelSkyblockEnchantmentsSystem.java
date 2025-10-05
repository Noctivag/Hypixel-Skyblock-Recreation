package de.noctivag.skyblock.enchantments;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
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
import java.util.stream.Collectors;

/**
 * Hypixel Skyblock Enchantments System - All Hypixel Skyblock Enchantments
 */
public class HypixelSkyblockEnchantmentsSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerEnchantmentData> playerEnchantmentData = new ConcurrentHashMap<>();
    private final Map<EnchantmentCategory, List<HypixelEnchantment>> enchantmentsByCategory = new HashMap<>();
    private final Map<UUID, BukkitTask> enchantmentTasks = new ConcurrentHashMap<>();
    
    public HypixelSkyblockEnchantmentsSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeAllEnchantments();
        startEnchantmentUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeAllEnchantments() {
        // COMBAT ENCHANTMENTS
        List<HypixelEnchantment> combatEnchantments = new ArrayList<>();
        combatEnchantments.add(new HypixelEnchantment("SHARPNESS", "Sharpness", "§a§lSharpness", Enchantment.getByKey(NamespacedKey.minecraft("sharpness")),
                EnchantmentRarity.COMMON, EnchantmentCategory.COMBAT, "§7Increases melee damage.",
                Arrays.asList("§7Level 1: +5% damage", "§7Level 2: +10% damage", "§7Level 3: +15% damage", "§7Level 4: +20% damage", "§7Level 5: +25% damage"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Diamond")));
        combatEnchantments.add(new HypixelEnchantment("SMITE", "Smite", "§f§lSmite", Enchantment.getByKey(NamespacedKey.minecraft("smite")),
                EnchantmentRarity.COMMON, EnchantmentCategory.COMBAT, "§7Increases damage to undead mobs.",
                Arrays.asList("§7Level 1: +8% damage to undead", "§7Level 2: +16% damage to undead", "§7Level 3: +24% damage to undead", "§7Level 4: +32% damage to undead", "§7Level 5: +40% damage to undead"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Bone", "§7- 1x Enchanted Rotten Flesh")));
        combatEnchantments.add(new HypixelEnchantment("BANE_OF_ARTHROPODS", "Bane of Arthropods", "§8§lBane of Arthropods", Enchantment.getByKey(NamespacedKey.minecraft("bane_of_arthropods")),
                EnchantmentRarity.COMMON, EnchantmentCategory.COMBAT, "§7Increases damage to arthropod mobs.",
                Arrays.asList("§7Level 1: +8% damage to arthropods", "§7Level 2: +16% damage to arthropods", "§7Level 3: +24% damage to arthropods", "§7Level 4: +32% damage to arthropods", "§7Level 5: +40% damage to arthropods"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted String", "§7- 1x Enchanted Spider Eye")));
        combatEnchantments.add(new HypixelEnchantment("KNOCKBACK", "Knockback", "§e§lKnockback", Enchantment.KNOCKBACK,
                EnchantmentRarity.UNCOMMON, EnchantmentCategory.COMBAT, "§7Knocks back enemies when hit.",
                Arrays.asList("§7Level 1: +1 block knockback", "§7Level 2: +2 block knockback", "§7Level 3: +3 block knockback", "§7Level 4: +4 block knockback", "§7Level 5: +5 block knockback"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Piston", "§7- 1x Enchanted Slime Ball")));
        combatEnchantments.add(new HypixelEnchantment("FIRE_ASPECT", "Fire Aspect", "§c§lFire Aspect", Enchantment.FIRE_ASPECT,
                EnchantmentRarity.UNCOMMON, EnchantmentCategory.COMBAT, "§7Sets enemies on fire when hit.",
                Arrays.asList("§7Level 1: 3 second burn", "§7Level 2: 6 second burn", "§7Level 3: 9 second burn", "§7Level 4: 12 second burn", "§7Level 5: 15 second burn"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Blaze Rod", "§7- 1x Enchanted Coal")));
        combatEnchantments.add(new HypixelEnchantment("LOOTING", "Looting", "§6§lLooting", Enchantment.getByKey(NamespacedKey.minecraft("looting")),
                EnchantmentRarity.RARE, EnchantmentCategory.COMBAT, "§7Increases loot drops from mobs.",
                Arrays.asList("§7Level 1: +25% loot chance", "§7Level 2: +50% loot chance", "§7Level 3: +75% loot chance", "§7Level 4: +100% loot chance", "§7Level 5: +125% loot chance"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Gold", "§7- 1x Enchanted Emerald")));
        combatEnchantments.add(new HypixelEnchantment("SWEEPING_EDGE", "Sweeping Edge", "§a§lSweeping Edge", Enchantment.SWEEPING_EDGE,
                EnchantmentRarity.RARE, EnchantmentCategory.COMBAT, "§7Increases sweeping attack damage.",
                Arrays.asList("§7Level 1: +25% sweep damage", "§7Level 2: +50% sweep damage", "§7Level 3: +75% sweep damage", "§7Level 4: +100% sweep damage", "§7Level 5: +125% sweep damage"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Diamond")));
        combatEnchantments.add(new HypixelEnchantment("CRITICAL", "Critical", "§c§lCritical", Enchantment.SHARPNESS,
                EnchantmentRarity.EPIC, EnchantmentCategory.COMBAT, "§7Increases critical hit chance and damage.",
                Arrays.asList("§7Level 1: +5% crit chance, +10% crit damage", "§7Level 2: +10% crit chance, +20% crit damage", "§7Level 3: +15% crit chance, +30% crit damage", "§7Level 4: +20% crit chance, +40% crit damage", "§7Level 5: +25% crit chance, +50% crit damage"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")));
        combatEnchantments.add(new HypixelEnchantment("EXECUTE", "Execute", "§4§lExecute", Enchantment.SHARPNESS,
                EnchantmentRarity.EPIC, EnchantmentCategory.COMBAT, "§7Increases damage to low health enemies.",
                Arrays.asList("§7Level 1: +10% damage to enemies below 50% health", "§7Level 2: +20% damage to enemies below 50% health", "§7Level 3: +30% damage to enemies below 50% health", "§7Level 4: +40% damage to enemies below 50% health", "§7Level 5: +50% damage to enemies below 50% health"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Redstone")));
        combatEnchantments.add(new HypixelEnchantment("GIANT_KILLER", "Giant Killer", "§5§lGiant Killer", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.COMBAT, "§7Increases damage to high health enemies.",
                Arrays.asList("§7Level 1: +10% damage to enemies above 50% health", "§7Level 2: +20% damage to enemies above 50% health", "§7Level 3: +30% damage to enemies above 50% health", "§7Level 4: +40% damage to enemies above 50% health", "§7Level 5: +50% damage to enemies above 50% health"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Gold Block")));
        enchantmentsByCategory.computeIfAbsent(EnchantmentCategory.COMBAT, k -> new ArrayList<>()).addAll(combatEnchantments);
        
        // PROTECTION ENCHANTMENTS
        enchantmentsByCategory.computeIfAbsent(EnchantmentCategory.PROTECTION, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelEnchantment("PROTECTION", "Protection", "§b§lProtection", Enchantment.getByKey(NamespacedKey.minecraft("protection")),
                EnchantmentRarity.COMMON, EnchantmentCategory.PROTECTION, "§7Reduces damage from all sources.",
                Arrays.asList("§7Level 1: +5% damage reduction", "§7Level 2: +10% damage reduction", "§7Level 3: +15% damage reduction", "§7Level 4: +20% damage reduction", "§7Level 5: +25% damage reduction"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Leather", "§7- 1x Enchanted Iron")),
                
            new HypixelEnchantment("FIRE_PROTECTION", "Fire Protection", "§c§lFire Protection", Enchantment.getByKey(NamespacedKey.minecraft("fire_protection")),
                EnchantmentRarity.COMMON, EnchantmentCategory.PROTECTION, "§7Reduces damage from fire and lava.",
                Arrays.asList("§7Level 1: +10% fire damage reduction", "§7Level 2: +20% fire damage reduction", "§7Level 3: +30% fire damage reduction", "§7Level 4: +40% fire damage reduction", "§7Level 5: +50% fire damage reduction"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Blaze Rod", "§7- 1x Enchanted Coal")),
                
            new HypixelEnchantment("BLAST_PROTECTION", "Blast Protection", "§e§lBlast Protection", Enchantment.getByKey(NamespacedKey.minecraft("blast_protection")),
                EnchantmentRarity.COMMON, EnchantmentCategory.PROTECTION, "§7Reduces damage from explosions.",
                Arrays.asList("§7Level 1: +10% explosion damage reduction", "§7Level 2: +20% explosion damage reduction", "§7Level 3: +30% explosion damage reduction", "§7Level 4: +40% explosion damage reduction", "§7Level 5: +50% explosion damage reduction"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted TNT", "§7- 1x Enchanted Gunpowder")),
                
            new HypixelEnchantment("PROJECTILE_PROTECTION", "Projectile Protection", "§a§lProjectile Protection", Enchantment.getByKey(NamespacedKey.minecraft("projectile_protection")),
                EnchantmentRarity.COMMON, EnchantmentCategory.PROTECTION, "§7Reduces damage from projectiles.",
                Arrays.asList("§7Level 1: +10% projectile damage reduction", "§7Level 2: +20% projectile damage reduction", "§7Level 3: +30% projectile damage reduction", "§7Level 4: +40% projectile damage reduction", "§7Level 5: +50% projectile damage reduction"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Arrow", "§7- 1x Enchanted String")),
                
            new HypixelEnchantment("FEATHER_FALLING", "Feather Falling", "§f§lFeather Falling", Enchantment.getByKey(NamespacedKey.minecraft("feather_falling")),
                EnchantmentRarity.UNCOMMON, EnchantmentCategory.PROTECTION, "§7Reduces fall damage.",
                Arrays.asList("§7Level 1: +15% fall damage reduction", "§7Level 2: +30% fall damage reduction", "§7Level 3: +45% fall damage reduction", "§7Level 4: +60% fall damage reduction", "§7Level 5: +75% fall damage reduction"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Feather", "§7- 1x Enchanted String")),
                
            new HypixelEnchantment("RESPIRATION", "Respiration", "§b§lRespiration", Enchantment.getByKey(NamespacedKey.minecraft("respiration")),
                EnchantmentRarity.UNCOMMON, EnchantmentCategory.PROTECTION, "§7Increases underwater breathing time.",
                Arrays.asList("§7Level 1: +15 seconds underwater", "§7Level 2: +30 seconds underwater", "§7Level 3: +45 seconds underwater", "§7Level 4: +60 seconds underwater", "§7Level 5: +75 seconds underwater"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Pufferfish", "§7- 1x Enchanted Water Bucket")),
                
            new HypixelEnchantment("AQUA_AFFINITY", "Aqua Affinity", "§9§lAqua Affinity", Enchantment.getByKey(NamespacedKey.minecraft("aqua_affinity")),
                EnchantmentRarity.RARE, EnchantmentCategory.PROTECTION, "§7Increases mining speed underwater.",
                Arrays.asList("§7Level 1: +25% underwater mining speed", "§7Level 2: +50% underwater mining speed", "§7Level 3: +75% underwater mining speed", "§7Level 4: +100% underwater mining speed", "§7Level 5: +125% underwater mining speed"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Prismarine", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("THORNS", "Thorns", "§2§lThorns", Enchantment.THORNS,
                EnchantmentRarity.RARE, EnchantmentCategory.PROTECTION, "§7Reflects damage back to attackers.",
                Arrays.asList("§7Level 1: +10% damage reflection", "§7Level 2: +20% damage reflection", "§7Level 3: +30% damage reflection", "§7Level 4: +40% damage reflection", "§7Level 5: +50% damage reflection"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Cactus", "§7- 1x Enchanted Iron")),
                
            new HypixelEnchantment("DEPTH_STRIDER", "Depth Strider", "§3§lDepth Strider", Enchantment.DEPTH_STRIDER,
                EnchantmentRarity.EPIC, EnchantmentCategory.PROTECTION, "§7Increases movement speed underwater.",
                Arrays.asList("§7Level 1: +25% underwater speed", "§7Level 2: +50% underwater speed", "§7Level 3: +75% underwater speed", "§7Level 4: +100% underwater speed", "§7Level 5: +125% underwater speed"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Prismarine Shard", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("FROST_WALKER", "Frost Walker", "§b§lFrost Walker", Enchantment.FROST_WALKER,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.PROTECTION, "§7Creates ice blocks when walking on water.",
                Arrays.asList("§7Level 1: 2 block radius", "§7Level 2: 4 block radius", "§7Level 3: 6 block radius", "§7Level 4: 8 block radius", "§7Level 5: 10 block radius"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Ice", "§7- 1x Enchanted Diamond Block"))
        ));
        
        // TOOL ENCHANTMENTS
        enchantmentsByCategory.computeIfAbsent(EnchantmentCategory.TOOL, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelEnchantment("EFFICIENCY", "Efficiency", "§a§lEfficiency", Enchantment.getByKey(NamespacedKey.minecraft("efficiency")),
                EnchantmentRarity.COMMON, EnchantmentCategory.TOOL, "§7Increases mining and chopping speed.",
                Arrays.asList("§7Level 1: +25% mining speed", "§7Level 2: +50% mining speed", "§7Level 3: +75% mining speed", "§7Level 4: +100% mining speed", "§7Level 5: +125% mining speed"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Redstone", "§7- 1x Enchanted Iron")),
                
            new HypixelEnchantment("FORTUNE", "Fortune", "§6§lFortune", Enchantment.getByKey(NamespacedKey.minecraft("fortune")),
                EnchantmentRarity.UNCOMMON, EnchantmentCategory.TOOL, "§7Increases block drop rates.",
                Arrays.asList("§7Level 1: +25% drop chance", "§7Level 2: +50% drop chance", "§7Level 3: +75% drop chance", "§7Level 4: +100% drop chance", "§7Level 5: +125% drop chance"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Gold", "§7- 1x Enchanted Emerald")),
                
            new HypixelEnchantment("SILK_TOUCH", "Silk Touch", "§f§lSilk Touch", Enchantment.SILK_TOUCH,
                EnchantmentRarity.RARE, EnchantmentCategory.TOOL, "§7Mines blocks in their original form.",
                Arrays.asList("§7Level 1: 50% chance to drop original block", "§7Level 2: 75% chance to drop original block", "§7Level 3: 100% chance to drop original block", "§7Level 4: 125% chance to drop original block", "§7Level 5: 150% chance to drop original block"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted String", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("UNBREAKING", "Unbreaking", "§7§lUnbreaking", Enchantment.getByKey(NamespacedKey.minecraft("unbreaking")),
                EnchantmentRarity.COMMON, EnchantmentCategory.TOOL, "§7Increases tool durability.",
                Arrays.asList("§7Level 1: +25% durability", "§7Level 2: +50% durability", "§7Level 3: +75% durability", "§7Level 4: +100% durability", "§7Level 5: +125% durability"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Leather")),
                
            new HypixelEnchantment("MENDING", "Mending", "§a§lMending", Enchantment.MENDING,
                EnchantmentRarity.EPIC, EnchantmentCategory.TOOL, "§7Repairs tools with experience.",
                Arrays.asList("§7Level 1: +25% XP repair rate", "§7Level 2: +50% XP repair rate", "§7Level 3: +75% XP repair rate", "§7Level 4: +100% XP repair rate", "§7Level 5: +125% XP repair rate"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Emerald", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("CURSE_OF_VANISHING", "Curse of Vanishing", "§c§lCurse of Vanishing", Enchantment.VANISHING_CURSE,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.TOOL, "§7Item disappears when player dies.",
                Arrays.asList("§7Level 1: 25% chance to vanish", "§7Level 2: 50% chance to vanish", "§7Level 3: 75% chance to vanish", "§7Level 4: 100% chance to vanish", "§7Level 5: 125% chance to vanish"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Diamond Block"))
        ));
        
        // BOW ENCHANTMENTS
        enchantmentsByCategory.computeIfAbsent(EnchantmentCategory.BOW, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelEnchantment("POWER", "Power", "§c§lPower", Enchantment.getByKey(NamespacedKey.minecraft("power")),
                EnchantmentRarity.COMMON, EnchantmentCategory.BOW, "§7Increases bow damage.",
                Arrays.asList("§7Level 1: +25% bow damage", "§7Level 2: +50% bow damage", "§7Level 3: +75% bow damage", "§7Level 4: +100% bow damage", "§7Level 5: +125% bow damage"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Arrow", "§7- 1x Enchanted String")),
                
            new HypixelEnchantment("PUNCH", "Punch", "§e§lPunch", Enchantment.getByKey(NamespacedKey.minecraft("punch")),
                EnchantmentRarity.UNCOMMON, EnchantmentCategory.BOW, "§7Increases arrow knockback.",
                Arrays.asList("§7Level 1: +1 block knockback", "§7Level 2: +2 block knockback", "§7Level 3: +3 block knockback", "§7Level 4: +4 block knockback", "§7Level 5: +5 block knockback"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Piston", "§7- 1x Enchanted Slime Ball")),
                
            new HypixelEnchantment("FLAME", "Flame", "§c§lFlame", Enchantment.getByKey(NamespacedKey.minecraft("flame")),
                EnchantmentRarity.UNCOMMON, EnchantmentCategory.BOW, "§7Sets arrows on fire.",
                Arrays.asList("§7Level 1: 3 second burn", "§7Level 2: 6 second burn", "§7Level 3: 9 second burn", "§7Level 4: 12 second burn", "§7Level 5: 15 second burn"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Blaze Rod", "§7- 1x Enchanted Coal")),
                
            new HypixelEnchantment("INFINITY", "Infinity", "§b§lInfinity", Enchantment.getByKey(NamespacedKey.minecraft("infinity")),
                EnchantmentRarity.RARE, EnchantmentCategory.BOW, "§7Arrows are not consumed when shooting.",
                Arrays.asList("§7Level 1: 25% chance to not consume arrow", "§7Level 2: 50% chance to not consume arrow", "§7Level 3: 75% chance to not consume arrow", "§7Level 4: 100% chance to not consume arrow", "§7Level 5: 125% chance to not consume arrow"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("MULTISHOT", "Multishot", "§d§lMultishot", Enchantment.MULTISHOT,
                EnchantmentRarity.EPIC, EnchantmentCategory.BOW, "§7Shoots multiple arrows at once.",
                Arrays.asList("§7Level 1: 2 arrows", "§7Level 2: 3 arrows", "§7Level 3: 4 arrows", "§7Level 4: 5 arrows", "§7Level 5: 6 arrows"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Arrow", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("PIERCING", "Piercing", "§5§lPiercing", Enchantment.PIERCING,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.BOW, "§7Arrows pierce through enemies.",
                Arrays.asList("§7Level 1: Pierce 1 enemy", "§7Level 2: Pierce 2 enemies", "§7Level 3: Pierce 3 enemies", "§7Level 4: Pierce 4 enemies", "§7Level 5: Pierce 5 enemies"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Arrow", "§7- 1x Enchanted Diamond Block"))
        ));
        
        // SPECIAL ENCHANTMENTS
        enchantmentsByCategory.computeIfAbsent(EnchantmentCategory.SPECIAL, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelEnchantment("LUCK", "Luck", "§6§lLuck", Enchantment.LUCK_OF_THE_SEA,
                EnchantmentRarity.RARE, EnchantmentCategory.SPECIAL, "§7Increases luck and rare drop chances.",
                Arrays.asList("§7Level 1: +10% luck", "§7Level 2: +20% luck", "§7Level 3: +30% luck", "§7Level 4: +40% luck", "§7Level 5: +50% luck"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Gold", "§7- 1x Enchanted Emerald")),
                
            new HypixelEnchantment("LURE", "Lure", "§b§lLure", Enchantment.LURE,
                EnchantmentRarity.UNCOMMON, EnchantmentCategory.SPECIAL, "§7Increases fishing speed and catch rate.",
                Arrays.asList("§7Level 1: +25% fishing speed", "§7Level 2: +50% fishing speed", "§7Level 3: +75% fishing speed", "§7Level 4: +100% fishing speed", "§7Level 5: +125% fishing speed"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Fish", "§7- 1x Enchanted String")),
                
            new HypixelEnchantment("CHANNELING", "Channeling", "§9§lChanneling", Enchantment.CHANNELING,
                EnchantmentRarity.EPIC, EnchantmentCategory.SPECIAL, "§7Summons lightning when hitting enemies.",
                Arrays.asList("§7Level 1: 25% chance to summon lightning", "§7Level 2: 50% chance to summon lightning", "§7Level 3: 75% chance to summon lightning", "§7Level 4: 100% chance to summon lightning", "§7Level 5: 125% chance to summon lightning"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Lightning Rod", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("IMPALING", "Impaling", "§3§lImpaling", Enchantment.IMPALING,
                EnchantmentRarity.RARE, EnchantmentCategory.SPECIAL, "§7Increases damage to sea creatures.",
                Arrays.asList("§7Level 1: +25% damage to sea creatures", "§7Level 2: +50% damage to sea creatures", "§7Level 3: +75% damage to sea creatures", "§7Level 4: +100% damage to sea creatures", "§7Level 5: +125% damage to sea creatures"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Prismarine", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("LOYALTY", "Loyalty", "§a§lLoyalty", Enchantment.LOYALTY,
                EnchantmentRarity.UNCOMMON, EnchantmentCategory.SPECIAL, "§7Trident returns to player after throwing.",
                Arrays.asList("§7Level 1: 25% faster return", "§7Level 2: 50% faster return", "§7Level 3: 75% faster return", "§7Level 4: 100% faster return", "§7Level 5: 125% faster return"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Prismarine Shard", "§7- 1x Enchanted Iron")),
                
            new HypixelEnchantment("RIPTIDE", "Riptide", "§b§lRiptide", Enchantment.RIPTIDE,
                EnchantmentRarity.EPIC, EnchantmentCategory.SPECIAL, "§7Launches player forward when throwing trident.",
                Arrays.asList("§7Level 1: 2 block launch", "§7Level 2: 4 block launch", "§7Level 3: 6 block launch", "§7Level 4: 8 block launch", "§7Level 5: 10 block launch"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Prismarine", "§7- 1x Enchanted Diamond Block")),
                
            // Additional Combat Enchantments
            new HypixelEnchantment("CLEAVE", "Cleave", "§c§lCleave", Enchantment.SHARPNESS,
                EnchantmentRarity.RARE, EnchantmentCategory.COMBAT, "§7Attacks multiple enemies at once.",
                Arrays.asList("§7Level 1: 2 enemies", "§7Level 2: 3 enemies", "§7Level 3: 4 enemies", "§7Level 4: 5 enemies", "§7Level 5: 6 enemies"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("FIRST_STRIKE", "First Strike", "§a§lFirst Strike", Enchantment.SHARPNESS,
                EnchantmentRarity.RARE, EnchantmentCategory.COMBAT, "§7Increases damage on first hit.",
                Arrays.asList("§7Level 1: +25% first hit damage", "§7Level 2: +50% first hit damage", "§7Level 3: +75% first hit damage", "§7Level 4: +100% first hit damage", "§7Level 5: +125% first hit damage"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("TRIPLE_STRIKE", "Triple Strike", "§d§lTriple Strike", Enchantment.SHARPNESS,
                EnchantmentRarity.EPIC, EnchantmentCategory.COMBAT, "§7Chance to hit three times.",
                Arrays.asList("§7Level 1: 10% chance", "§7Level 2: 20% chance", "§7Level 3: 30% chance", "§7Level 4: 40% chance", "§7Level 5: 50% chance"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelEnchantment("LIFE_STEAL", "Life Steal", "§c§lLife Steal", Enchantment.SHARPNESS,
                EnchantmentRarity.EPIC, EnchantmentCategory.COMBAT, "§7Heals player when dealing damage.",
                Arrays.asList("§7Level 1: 1% life steal", "§7Level 2: 2% life steal", "§7Level 3: 3% life steal", "§7Level 4: 4% life steal", "§7Level 5: 5% life steal"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelEnchantment("SYPHON", "Syphon", "§5§lSyphon", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.COMBAT, "§7Heals player based on crit damage.",
                Arrays.asList("§7Level 1: 0.1% per crit damage", "§7Level 2: 0.2% per crit damage", "§7Level 3: 0.3% per crit damage", "§7Level 4: 0.4% per crit damage", "§7Level 5: 0.5% per crit damage"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Gold Block")),
                
            new HypixelEnchantment("VAMPIRISM", "Vampirism", "§4§lVampirism", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.COMBAT, "§7Heals player when killing enemies.",
                Arrays.asList("§7Level 1: 5% heal on kill", "§7Level 2: 10% heal on kill", "§7Level 3: 15% heal on kill", "§7Level 4: 20% heal on kill", "§7Level 5: 25% heal on kill"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Gold Block")),
                
            new HypixelEnchantment("THUNDERLORD", "Thunderlord", "§e§lThunderlord", Enchantment.SHARPNESS,
                EnchantmentRarity.EPIC, EnchantmentCategory.COMBAT, "§7Strikes lightning on enemies.",
                Arrays.asList("§7Level 1: 5% lightning chance", "§7Level 2: 10% lightning chance", "§7Level 3: 15% lightning chance", "§7Level 4: 20% lightning chance", "§7Level 5: 25% lightning chance"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelEnchantment("VENOMOUS", "Venomous", "§2§lVenomous", Enchantment.SHARPNESS,
                EnchantmentRarity.RARE, EnchantmentCategory.COMBAT, "§7Poisons enemies on hit.",
                Arrays.asList("§7Level 1: 10% poison chance", "§7Level 2: 20% poison chance", "§7Level 3: 30% poison chance", "§7Level 4: 40% poison chance", "§7Level 5: 50% poison chance"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Spider Eye", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("CUBISM", "Cubism", "§b§lCubism", Enchantment.SHARPNESS,
                EnchantmentRarity.UNCOMMON, EnchantmentCategory.COMBAT, "§7Increases damage to slimes and magma cubes.",
                Arrays.asList("§7Level 1: +20% damage to cubes", "§7Level 2: +40% damage to cubes", "§7Level 3: +60% damage to cubes", "§7Level 4: +80% damage to cubes", "§7Level 5: +100% damage to cubes"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Slime Ball", "§7- 1x Enchanted Iron")),
                
            new HypixelEnchantment("ENDER_SLAYER", "Ender Slayer", "§5§lEnder Slayer", Enchantment.SHARPNESS,
                EnchantmentRarity.RARE, EnchantmentCategory.COMBAT, "§7Increases damage to endermen.",
                Arrays.asList("§7Level 1: +30% damage to endermen", "§7Level 2: +60% damage to endermen", "§7Level 3: +90% damage to endermen", "§7Level 4: +120% damage to endermen", "§7Level 5: +150% damage to endermen"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("DRAGON_HUNTER", "Dragon Hunter", "§6§lDragon Hunter", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.COMBAT, "§7Increases damage to dragons.",
                Arrays.asList("§7Level 1: +50% damage to dragons", "§7Level 2: +100% damage to dragons", "§7Level 3: +150% damage to dragons", "§7Level 4: +200% damage to dragons", "§7Level 5: +250% damage to dragons"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Dragon Egg", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("MAGIC_FIND", "Magic Find", "§d§lMagic Find", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.SPECIAL, "§7Increases magic find chance.",
                Arrays.asList("§7Level 1: +5% magic find", "§7Level 2: +10% magic find", "§7Level 3: +15% magic find", "§7Level 4: +20% magic find", "§7Level 5: +25% magic find"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Emerald", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("PET_LUCK", "Pet Luck", "§a§lPet Luck", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.SPECIAL, "§7Increases pet drop chance.",
                Arrays.asList("§7Level 1: +5% pet luck", "§7Level 2: +10% pet luck", "§7Level 3: +15% pet luck", "§7Level 4: +20% pet luck", "§7Level 5: +25% pet luck"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Emerald", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("SCAVENGER", "Scavenger", "§6§lScavenger", Enchantment.SHARPNESS,
                EnchantmentRarity.RARE, EnchantmentCategory.SPECIAL, "§7Increases coin drops from mobs.",
                Arrays.asList("§7Level 1: +10% coin drops", "§7Level 2: +20% coin drops", "§7Level 3: +30% coin drops", "§7Level 4: +40% coin drops", "§7Level 5: +50% coin drops"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Gold", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("EXPERIENCE", "Experience", "§a§lExperience", Enchantment.SHARPNESS,
                EnchantmentRarity.RARE, EnchantmentCategory.SPECIAL, "§7Increases experience gain.",
                Arrays.asList("§7Level 1: +10% experience", "§7Level 2: +20% experience", "§7Level 3: +30% experience", "§7Level 4: +40% experience", "§7Level 5: +50% experience"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Lapis", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("TELEKENESIS", "Telekenesis", "§5§lTelekenesis", Enchantment.SHARPNESS,
                EnchantmentRarity.EPIC, EnchantmentCategory.SPECIAL, "§7Automatically picks up drops.",
                Arrays.asList("§7Level 1: 25% telekenesis chance", "§7Level 2: 50% telekenesis chance", "§7Level 3: 75% telekenesis chance", "§7Level 4: 100% telekenesis chance", "§7Level 5: 125% telekenesis chance"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Diamond")),
                
            new HypixelEnchantment("PROSECUTE", "Prosecute", "§4§lProsecute", Enchantment.SHARPNESS,
                EnchantmentRarity.EPIC, EnchantmentCategory.COMBAT, "§7Increases damage to full health enemies.",
                Arrays.asList("§7Level 1: +20% damage to full health", "§7Level 2: +40% damage to full health", "§7Level 3: +60% damage to full health", "§7Level 4: +80% damage to full health", "§7Level 5: +100% damage to full health"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Gold")),
                
            new HypixelEnchantment("ULTIMATE_WISE", "Ultimate Wise", "§b§lUltimate Wise", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.SPECIAL, "§7Reduces ability mana cost.",
                Arrays.asList("§7Level 1: -10% mana cost", "§7Level 2: -20% mana cost", "§7Level 3: -30% mana cost", "§7Level 4: -40% mana cost", "§7Level 5: -50% mana cost"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Lapis", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("ULTIMATE_JERRY", "Ultimate Jerry", "§e§lUltimate Jerry", Enchantment.SHARPNESS,
                EnchantmentRarity.MYTHIC, EnchantmentCategory.SPECIAL, "§7Jerry's ultimate enchantment.",
                Arrays.asList("§7Level 1: +1% to all stats", "§7Level 2: +2% to all stats", "§7Level 3: +3% to all stats", "§7Level 4: +4% to all stats", "§7Level 5: +5% to all stats"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Jerry's Egg", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("ULTIMATE_WISDOM", "Ultimate Wisdom", "§b§lUltimate Wisdom", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.SPECIAL, "§7Increases experience gain significantly.",
                Arrays.asList("§7Level 1: +25% experience", "§7Level 2: +50% experience", "§7Level 3: +75% experience", "§7Level 4: +100% experience", "§7Level 5: +125% experience"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Lapis", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("ULTIMATE_ONE_FOR_ALL", "Ultimate One For All", "§d§lUltimate One For All", Enchantment.SHARPNESS,
                EnchantmentRarity.MYTHIC, EnchantmentCategory.SPECIAL, "§7Increases damage but removes other enchantments.",
                Arrays.asList("§7Level 1: +50% damage", "§7Level 2: +100% damage", "§7Level 3: +150% damage", "§7Level 4: +200% damage", "§7Level 5: +250% damage"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Diamond Block", "§7- 1x Enchanted Gold Block")),
                
            new HypixelEnchantment("ULTIMATE_SOUL_EATER", "Ultimate Soul Eater", "§5§lUltimate Soul Eater", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.SPECIAL, "§7Increases damage based on souls collected.",
                Arrays.asList("§7Level 1: +1% damage per soul", "§7Level 2: +2% damage per soul", "§7Level 3: +3% damage per soul", "§7Level 4: +4% damage per soul", "§7Level 5: +5% damage per soul"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("ULTIMATE_COMBO", "Ultimate Combo", "§c§lUltimate Combo", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.SPECIAL, "§7Increases damage with consecutive hits.",
                Arrays.asList("§7Level 1: +5% damage per hit", "§7Level 2: +10% damage per hit", "§7Level 3: +15% damage per hit", "§7Level 4: +20% damage per hit", "§7Level 5: +25% damage per hit"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Redstone", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("ULTIMATE_FATAL_TEMPO", "Ultimate Fatal Tempo", "§4§lUltimate Fatal Tempo", Enchantment.SHARPNESS,
                EnchantmentRarity.MYTHIC, EnchantmentCategory.SPECIAL, "§7Increases attack speed with consecutive hits.",
                Arrays.asList("§7Level 1: +10% attack speed per hit", "§7Level 2: +20% attack speed per hit", "§7Level 3: +30% attack speed per hit", "§7Level 4: +40% attack speed per hit", "§7Level 5: +50% attack speed per hit"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Redstone", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("ULTIMATE_DUPLICATE", "Ultimate Duplicate", "§b§lUltimate Duplicate", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.SPECIAL, "§7Chance to duplicate drops.",
                Arrays.asList("§7Level 1: 5% duplicate chance", "§7Level 2: 10% duplicate chance", "§7Level 3: 15% duplicate chance", "§7Level 4: 20% duplicate chance", "§7Level 5: 25% duplicate chance"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Emerald", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("ULTIMATE_CHIMERA", "Ultimate Chimera", "§d§lUltimate Chimera", Enchantment.SHARPNESS,
                EnchantmentRarity.MYTHIC, EnchantmentCategory.SPECIAL, "§7Increases pet stats.",
                Arrays.asList("§7Level 1: +10% pet stats", "§7Level 2: +20% pet stats", "§7Level 3: +30% pet stats", "§7Level 4: +40% pet stats", "§7Level 5: +50% pet stats"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Emerald", "§7- 1x Enchanted Diamond Block")),
                
            new HypixelEnchantment("ULTIMATE_LEGION", "Ultimate Legion", "§6§lUltimate Legion", Enchantment.SHARPNESS,
                EnchantmentRarity.LEGENDARY, EnchantmentCategory.SPECIAL, "§7Increases stats based on nearby players.",
                Arrays.asList("§7Level 1: +1% stats per nearby player", "§7Level 2: +2% stats per nearby player", "§7Level 3: +3% stats per nearby player", "§7Level 4: +4% stats per nearby player", "§7Level 5: +5% stats per nearby player"),
                Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Enchanted Gold", "§7- 1x Enchanted Diamond Block"))
        ));
    }
    
    private void startEnchantmentUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerEnchantmentData> entry : playerEnchantmentData.entrySet()) {
                    PlayerEnchantmentData enchantmentData = entry.getValue();
                    enchantmentData.update();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Enchantment") || displayName.contains("Enchant")) {
            openEnchantmentsGUI(player);
        }
    }
    
    public void openEnchantmentsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§d§lHypixel Skyblock Enchantments"));
        
        addGUIItem(gui, 10, Material.DIAMOND_SWORD, "§c§lCombat Enchantments", "§7View all combat enchantments");
        addGUIItem(gui, 11, Material.DIAMOND_CHESTPLATE, "§b§lProtection Enchantments", "§7View all protection enchantments");
        addGUIItem(gui, 12, Material.DIAMOND_PICKAXE, "§7§lTool Enchantments", "§7View all tool enchantments");
        addGUIItem(gui, 13, Material.BOW, "§a§lBow Enchantments", "§7View all bow enchantments");
        addGUIItem(gui, 14, Material.ENCHANTED_BOOK, "§d§lSpecial Enchantments", "§7View all special enchantments");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aHypixel Skyblock Enchantments GUI geöffnet!"));
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
    
    public PlayerEnchantmentData getPlayerEnchantmentData(UUID playerId) {
        return playerEnchantmentData.computeIfAbsent(playerId, k -> new PlayerEnchantmentData(playerId));
    }
    
    public enum EnchantmentCategory {
        COMBAT("§cCombat", "§7Combat-focused enchantments"),
        PROTECTION("§bProtection", "§7Protection-focused enchantments"),
        TOOL("§7Tool", "§7Tool-focused enchantments"),
        BOW("§aBow", "§7Bow-focused enchantments"),
        SPECIAL("§dSpecial", "§7Special and unique enchantments");
        
        private final String displayName;
        private final String description;
        
        EnchantmentCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum EnchantmentRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        EnchantmentRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class HypixelEnchantment {
        private final String id;
        private final String name;
        private final String displayName;
        private final Enchantment enchantment;
        private final EnchantmentRarity rarity;
        private final EnchantmentCategory category;
        private final String description;
        private final List<String> levels;
        private final List<String> craftingMaterials;
        
        public HypixelEnchantment(String id, String name, String displayName, Enchantment enchantment,
                                 EnchantmentRarity rarity, EnchantmentCategory category, String description,
                                 List<String> levels, List<String> craftingMaterials) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.enchantment = enchantment;
            this.rarity = rarity;
            this.category = category;
            this.description = description;
            this.levels = levels;
            this.craftingMaterials = craftingMaterials;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Enchantment getEnchantment() { return enchantment; }
        public EnchantmentRarity getRarity() { return rarity; }
        public EnchantmentCategory getCategory() { return category; }
        public String getDescription() { return description; }
        public List<String> getLevels() { return levels; }
        public List<String> getCraftingMaterials() { return craftingMaterials; }
    }
    
    public static class PlayerEnchantmentData {
        private final UUID playerId;
        private final Map<String, Integer> enchantmentLevels = new HashMap<>();
        private final Map<String, Boolean> enchantmentUnlocked = new HashMap<>();
        private long lastUpdate;
        
        public PlayerEnchantmentData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void setEnchantmentLevel(String enchantmentId, int level) {
            enchantmentLevels.put(enchantmentId, level);
        }
        
        public void unlockEnchantment(String enchantmentId) {
            enchantmentUnlocked.put(enchantmentId, true);
        }
        
        public int getEnchantmentLevel(String enchantmentId) {
            return enchantmentLevels.getOrDefault(enchantmentId, 0);
        }
        
        public boolean isEnchantmentUnlocked(String enchantmentId) {
            return enchantmentUnlocked.getOrDefault(enchantmentId, false);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
