package de.noctivag.plugin.minions;
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
 * Hypixel Skyblock Minions System - All Hypixel Skyblock Minions
 */
public class HypixelSkyblockMinionsSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerMinionData> playerMinionData = new ConcurrentHashMap<>();
    private final Map<MinionCategory, List<HypixelMinion>> minionsByCategory = new HashMap<>();
    private final Map<UUID, BukkitTask> minionTasks = new ConcurrentHashMap<>();
    
    public HypixelSkyblockMinionsSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeAllMinions();
        startMinionUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeAllMinions() {
        // MINING MINIONS
        minionsByCategory.computeIfAbsent(MinionCategory.MINING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelMinion("COBBLESTONE_MINION", "Cobblestone Minion", "§7§lCobblestone Minion", Material.COBBLESTONE,
                MinionRarity.COMMON, MinionCategory.MINING, "§7Mines cobblestone automatically.",
                Arrays.asList("§7Level 1: 1 cobblestone per 12 seconds", "§7Level 2: 1 cobblestone per 11 seconds", "§7Level 3: 1 cobblestone per 10 seconds", "§7Level 4: 1 cobblestone per 9 seconds", "§7Level 5: 1 cobblestone per 8 seconds"),
                Arrays.asList("§7- 1x Cobblestone Minion", "§7- 1x Enchanted Cobblestone", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("COAL_MINION", "Coal Minion", "§8§lCoal Minion", Material.COAL,
                MinionRarity.COMMON, MinionCategory.MINING, "§7Mines coal automatically.",
                Arrays.asList("§7Level 1: 1 coal per 12 seconds", "§7Level 2: 1 coal per 11 seconds", "§7Level 3: 1 coal per 10 seconds", "§7Level 4: 1 coal per 9 seconds", "§7Level 5: 1 coal per 8 seconds"),
                Arrays.asList("§7- 1x Coal Minion", "§7- 1x Enchanted Coal", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("IRON_MINION", "Iron Minion", "§f§lIron Minion", Material.IRON_INGOT,
                MinionRarity.UNCOMMON, MinionCategory.MINING, "§7Mines iron automatically.",
                Arrays.asList("§7Level 1: 1 iron per 14 seconds", "§7Level 2: 1 iron per 13 seconds", "§7Level 3: 1 iron per 12 seconds", "§7Level 4: 1 iron per 11 seconds", "§7Level 5: 1 iron per 10 seconds"),
                Arrays.asList("§7- 1x Iron Minion", "§7- 1x Enchanted Iron", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("GOLD_MINION", "Gold Minion", "§6§lGold Minion", Material.GOLD_INGOT,
                MinionRarity.UNCOMMON, MinionCategory.MINING, "§7Mines gold automatically.",
                Arrays.asList("§7Level 1: 1 gold per 16 seconds", "§7Level 2: 1 gold per 15 seconds", "§7Level 3: 1 gold per 14 seconds", "§7Level 4: 1 gold per 13 seconds", "§7Level 5: 1 gold per 12 seconds"),
                Arrays.asList("§7- 1x Gold Minion", "§7- 1x Enchanted Gold", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("DIAMOND_MINION", "Diamond Minion", "§b§lDiamond Minion", Material.DIAMOND,
                MinionRarity.RARE, MinionCategory.MINING, "§7Mines diamonds automatically.",
                Arrays.asList("§7Level 1: 1 diamond per 20 seconds", "§7Level 2: 1 diamond per 19 seconds", "§7Level 3: 1 diamond per 18 seconds", "§7Level 4: 1 diamond per 17 seconds", "§7Level 5: 1 diamond per 16 seconds"),
                Arrays.asList("§7- 1x Diamond Minion", "§7- 1x Enchanted Diamond", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("EMERALD_MINION", "Emerald Minion", "§a§lEmerald Minion", Material.EMERALD,
                MinionRarity.RARE, MinionCategory.MINING, "§7Mines emeralds automatically.",
                Arrays.asList("§7Level 1: 1 emerald per 22 seconds", "§7Level 2: 1 emerald per 21 seconds", "§7Level 3: 1 emerald per 20 seconds", "§7Level 4: 1 emerald per 19 seconds", "§7Level 5: 1 emerald per 18 seconds"),
                Arrays.asList("§7- 1x Emerald Minion", "§7- 1x Enchanted Emerald", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("LAPIS_MINION", "Lapis Minion", "§9§lLapis Minion", Material.LAPIS_LAZULI,
                MinionRarity.UNCOMMON, MinionCategory.MINING, "§7Mines lapis lazuli automatically.",
                Arrays.asList("§7Level 1: 1 lapis per 18 seconds", "§7Level 2: 1 lapis per 17 seconds", "§7Level 3: 1 lapis per 16 seconds", "§7Level 4: 1 lapis per 15 seconds", "§7Level 5: 1 lapis per 14 seconds"),
                Arrays.asList("§7- 1x Lapis Minion", "§7- 1x Enchanted Lapis", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("REDSTONE_MINION", "Redstone Minion", "§c§lRedstone Minion", Material.REDSTONE,
                MinionRarity.UNCOMMON, MinionCategory.MINING, "§7Mines redstone automatically.",
                Arrays.asList("§7Level 1: 1 redstone per 16 seconds", "§7Level 2: 1 redstone per 15 seconds", "§7Level 3: 1 redstone per 14 seconds", "§7Level 4: 1 redstone per 13 seconds", "§7Level 5: 1 redstone per 12 seconds"),
                Arrays.asList("§7- 1x Redstone Minion", "§7- 1x Enchanted Redstone", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("QUARTZ_MINION", "Quartz Minion", "§f§lQuartz Minion", Material.QUARTZ,
                MinionRarity.RARE, MinionCategory.MINING, "§7Mines quartz automatically.",
                Arrays.asList("§7Level 1: 1 quartz per 20 seconds", "§7Level 2: 1 quartz per 19 seconds", "§7Level 3: 1 quartz per 18 seconds", "§7Level 4: 1 quartz per 17 seconds", "§7Level 5: 1 quartz per 16 seconds"),
                Arrays.asList("§7- 1x Quartz Minion", "§7- 1x Enchanted Quartz", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("OBSIDIAN_MINION", "Obsidian Minion", "§5§lObsidian Minion", Material.OBSIDIAN,
                MinionRarity.EPIC, MinionCategory.MINING, "§7Mines obsidian automatically.",
                Arrays.asList("§7Level 1: 1 obsidian per 30 seconds", "§7Level 2: 1 obsidian per 28 seconds", "§7Level 3: 1 obsidian per 26 seconds", "§7Level 4: 1 obsidian per 24 seconds", "§7Level 5: 1 obsidian per 22 seconds"),
                Arrays.asList("§7- 1x Obsidian Minion", "§7- 1x Enchanted Obsidian", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("GLOWSTONE_MINION", "Glowstone Minion", "§e§lGlowstone Minion", Material.GLOWSTONE,
                MinionRarity.UNCOMMON, MinionCategory.MINING, "§7Mines glowstone automatically.",
                Arrays.asList("§7Level 1: 1 glowstone per 18 seconds", "§7Level 2: 1 glowstone per 17 seconds", "§7Level 3: 1 glowstone per 16 seconds", "§7Level 4: 1 glowstone per 15 seconds", "§7Level 5: 1 glowstone per 14 seconds"),
                Arrays.asList("§7- 1x Glowstone Minion", "§7- 1x Enchanted Glowstone", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("GRAVEL_MINION", "Gravel Minion", "§7§lGravel Minion", Material.GRAVEL,
                MinionRarity.COMMON, MinionCategory.MINING, "§7Mines gravel automatically.",
                Arrays.asList("§7Level 1: 1 gravel per 12 seconds", "§7Level 2: 1 gravel per 11 seconds", "§7Level 3: 1 gravel per 10 seconds", "§7Level 4: 1 gravel per 9 seconds", "§7Level 5: 1 gravel per 8 seconds"),
                Arrays.asList("§7- 1x Gravel Minion", "§7- 1x Enchanted Gravel", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("SAND_MINION", "Sand Minion", "§e§lSand Minion", Material.SAND,
                MinionRarity.COMMON, MinionCategory.MINING, "§7Mines sand automatically.",
                Arrays.asList("§7Level 1: 1 sand per 12 seconds", "§7Level 2: 1 sand per 11 seconds", "§7Level 3: 1 sand per 10 seconds", "§7Level 4: 1 sand per 9 seconds", "§7Level 5: 1 sand per 8 seconds"),
                Arrays.asList("§7- 1x Sand Minion", "§7- 1x Enchanted Sand", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("END_STONE_MINION", "End Stone Minion", "§f§lEnd Stone Minion", Material.END_STONE,
                MinionRarity.RARE, MinionCategory.MINING, "§7Mines end stone automatically.",
                Arrays.asList("§7Level 1: 1 end stone per 20 seconds", "§7Level 2: 1 end stone per 19 seconds", "§7Level 3: 1 end stone per 18 seconds", "§7Level 4: 1 end stone per 17 seconds", "§7Level 5: 1 end stone per 16 seconds"),
                Arrays.asList("§7- 1x End Stone Minion", "§7- 1x Enchanted End Stone", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("SNOW_MINION", "Snow Minion", "§f§lSnow Minion", Material.SNOWBALL,
                MinionRarity.COMMON, MinionCategory.MINING, "§7Mines snow automatically.",
                Arrays.asList("§7Level 1: 1 snow per 10 seconds", "§7Level 2: 1 snow per 9 seconds", "§7Level 3: 1 snow per 8 seconds", "§7Level 4: 1 snow per 7 seconds", "§7Level 5: 1 snow per 6 seconds"),
                Arrays.asList("§7- 1x Snow Minion", "§7- 1x Enchanted Snow", "§7- 1x Enchanted Wood"))
        ));
        
        // FARMING MINIONS
        minionsByCategory.computeIfAbsent(MinionCategory.FARMING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelMinion("WHEAT_MINION", "Wheat Minion", "§e§lWheat Minion", Material.WHEAT,
                MinionRarity.COMMON, MinionCategory.FARMING, "§7Farms wheat automatically.",
                Arrays.asList("§7Level 1: 1 wheat per 12 seconds", "§7Level 2: 1 wheat per 11 seconds", "§7Level 3: 1 wheat per 10 seconds", "§7Level 4: 1 wheat per 9 seconds", "§7Level 5: 1 wheat per 8 seconds"),
                Arrays.asList("§7- 1x Wheat Minion", "§7- 1x Enchanted Wheat", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("CARROT_MINION", "Carrot Minion", "§6§lCarrot Minion", Material.CARROT,
                MinionRarity.COMMON, MinionCategory.FARMING, "§7Farms carrots automatically.",
                Arrays.asList("§7Level 1: 1 carrot per 12 seconds", "§7Level 2: 1 carrot per 11 seconds", "§7Level 3: 1 carrot per 10 seconds", "§7Level 4: 1 carrot per 9 seconds", "§7Level 5: 1 carrot per 8 seconds"),
                Arrays.asList("§7- 1x Carrot Minion", "§7- 1x Enchanted Carrot", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("POTATO_MINION", "Potato Minion", "§e§lPotato Minion", Material.POTATO,
                MinionRarity.COMMON, MinionCategory.FARMING, "§7Farms potatoes automatically.",
                Arrays.asList("§7Level 1: 1 potato per 12 seconds", "§7Level 2: 1 potato per 11 seconds", "§7Level 3: 1 potato per 10 seconds", "§7Level 4: 1 potato per 9 seconds", "§7Level 5: 1 potato per 8 seconds"),
                Arrays.asList("§7- 1x Potato Minion", "§7- 1x Enchanted Potato", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("PUMPKIN_MINION", "Pumpkin Minion", "§6§lPumpkin Minion", Material.PUMPKIN,
                MinionRarity.UNCOMMON, MinionCategory.FARMING, "§7Farms pumpkins automatically.",
                Arrays.asList("§7Level 1: 1 pumpkin per 14 seconds", "§7Level 2: 1 pumpkin per 13 seconds", "§7Level 3: 1 pumpkin per 12 seconds", "§7Level 4: 1 pumpkin per 11 seconds", "§7Level 5: 1 pumpkin per 10 seconds"),
                Arrays.asList("§7- 1x Pumpkin Minion", "§7- 1x Enchanted Pumpkin", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("MELON_MINION", "Melon Minion", "§a§lMelon Minion", Material.MELON,
                MinionRarity.UNCOMMON, MinionCategory.FARMING, "§7Farms melons automatically.",
                Arrays.asList("§7Level 1: 1 melon per 14 seconds", "§7Level 2: 1 melon per 13 seconds", "§7Level 3: 1 melon per 12 seconds", "§7Level 4: 1 melon per 11 seconds", "§7Level 5: 1 melon per 10 seconds"),
                Arrays.asList("§7- 1x Melon Minion", "§7- 1x Enchanted Melon", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("MUSHROOM_MINION", "Mushroom Minion", "§c§lMushroom Minion", Material.RED_MUSHROOM,
                MinionRarity.UNCOMMON, MinionCategory.FARMING, "§7Farms mushrooms automatically.",
                Arrays.asList("§7Level 1: 1 mushroom per 16 seconds", "§7Level 2: 1 mushroom per 15 seconds", "§7Level 3: 1 mushroom per 14 seconds", "§7Level 4: 1 mushroom per 13 seconds", "§7Level 5: 1 mushroom per 12 seconds"),
                Arrays.asList("§7- 1x Mushroom Minion", "§7- 1x Enchanted Mushroom", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("COCOA_MINION", "Cocoa Minion", "§6§lCocoa Minion", Material.COCOA_BEANS,
                MinionRarity.UNCOMMON, MinionCategory.FARMING, "§7Farms cocoa beans automatically.",
                Arrays.asList("§7Level 1: 1 cocoa per 16 seconds", "§7Level 2: 1 cocoa per 15 seconds", "§7Level 3: 1 cocoa per 14 seconds", "§7Level 4: 1 cocoa per 13 seconds", "§7Level 5: 1 cocoa per 12 seconds"),
                Arrays.asList("§7- 1x Cocoa Minion", "§7- 1x Enchanted Cocoa", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("CACTUS_MINION", "Cactus Minion", "§a§lCactus Minion", Material.CACTUS,
                MinionRarity.UNCOMMON, MinionCategory.FARMING, "§7Farms cactus automatically.",
                Arrays.asList("§7Level 1: 1 cactus per 18 seconds", "§7Level 2: 1 cactus per 17 seconds", "§7Level 3: 1 cactus per 16 seconds", "§7Level 4: 1 cactus per 15 seconds", "§7Level 5: 1 cactus per 14 seconds"),
                Arrays.asList("§7- 1x Cactus Minion", "§7- 1x Enchanted Cactus", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("SUGAR_CANE_MINION", "Sugar Cane Minion", "§a§lSugar Cane Minion", Material.SUGAR_CANE,
                MinionRarity.UNCOMMON, MinionCategory.FARMING, "§7Farms sugar cane automatically.",
                Arrays.asList("§7Level 1: 1 sugar cane per 16 seconds", "§7Level 2: 1 sugar cane per 15 seconds", "§7Level 3: 1 sugar cane per 14 seconds", "§7Level 4: 1 sugar cane per 13 seconds", "§7Level 5: 1 sugar cane per 12 seconds"),
                Arrays.asList("§7- 1x Sugar Cane Minion", "§7- 1x Enchanted Sugar Cane", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("NETHER_WART_MINION", "Nether Wart Minion", "§c§lNether Wart Minion", Material.NETHER_WART,
                MinionRarity.RARE, MinionCategory.FARMING, "§7Farms nether wart automatically.",
                Arrays.asList("§7Level 1: 1 nether wart per 20 seconds", "§7Level 2: 1 nether wart per 19 seconds", "§7Level 3: 1 nether wart per 18 seconds", "§7Level 4: 1 nether wart per 17 seconds", "§7Level 5: 1 nether wart per 16 seconds"),
                Arrays.asList("§7- 1x Nether Wart Minion", "§7- 1x Enchanted Nether Wart", "§7- 1x Enchanted Wood"))
        ));
        
        // FORAGING MINIONS
        minionsByCategory.computeIfAbsent(MinionCategory.FORAGING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelMinion("OAK_MINION", "Oak Minion", "§6§lOak Minion", Material.OAK_LOG,
                MinionRarity.COMMON, MinionCategory.FORAGING, "§7Chops oak logs automatically.",
                Arrays.asList("§7Level 1: 1 oak log per 12 seconds", "§7Level 2: 1 oak log per 11 seconds", "§7Level 3: 1 oak log per 10 seconds", "§7Level 4: 1 oak log per 9 seconds", "§7Level 5: 1 oak log per 8 seconds"),
                Arrays.asList("§7- 1x Oak Minion", "§7- 1x Enchanted Oak Log", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("SPRUCE_MINION", "Spruce Minion", "§8§lSpruce Minion", Material.SPRUCE_LOG,
                MinionRarity.COMMON, MinionCategory.FORAGING, "§7Chops spruce logs automatically.",
                Arrays.asList("§7Level 1: 1 spruce log per 12 seconds", "§7Level 2: 1 spruce log per 11 seconds", "§7Level 3: 1 spruce log per 10 seconds", "§7Level 4: 1 spruce log per 9 seconds", "§7Level 5: 1 spruce log per 8 seconds"),
                Arrays.asList("§7- 1x Spruce Minion", "§7- 1x Enchanted Spruce Log", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("BIRCH_MINION", "Birch Minion", "§f§lBirch Minion", Material.BIRCH_LOG,
                MinionRarity.COMMON, MinionCategory.FORAGING, "§7Chops birch logs automatically.",
                Arrays.asList("§7Level 1: 1 birch log per 12 seconds", "§7Level 2: 1 birch log per 11 seconds", "§7Level 3: 1 birch log per 10 seconds", "§7Level 4: 1 birch log per 9 seconds", "§7Level 5: 1 birch log per 8 seconds"),
                Arrays.asList("§7- 1x Birch Minion", "§7- 1x Enchanted Birch Log", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("JUNGLE_MINION", "Jungle Minion", "§a§lJungle Minion", Material.JUNGLE_LOG,
                MinionRarity.UNCOMMON, MinionCategory.FORAGING, "§7Chops jungle logs automatically.",
                Arrays.asList("§7Level 1: 1 jungle log per 14 seconds", "§7Level 2: 1 jungle log per 13 seconds", "§7Level 3: 1 jungle log per 12 seconds", "§7Level 4: 1 jungle log per 11 seconds", "§7Level 5: 1 jungle log per 10 seconds"),
                Arrays.asList("§7- 1x Jungle Minion", "§7- 1x Enchanted Jungle Log", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("ACACIA_MINION", "Acacia Minion", "§6§lAcacia Minion", Material.ACACIA_LOG,
                MinionRarity.UNCOMMON, MinionCategory.FORAGING, "§7Chops acacia logs automatically.",
                Arrays.asList("§7Level 1: 1 acacia log per 14 seconds", "§7Level 2: 1 acacia log per 13 seconds", "§7Level 3: 1 acacia log per 12 seconds", "§7Level 4: 1 acacia log per 11 seconds", "§7Level 5: 1 acacia log per 10 seconds"),
                Arrays.asList("§7- 1x Acacia Minion", "§7- 1x Enchanted Acacia Log", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("DARK_OAK_MINION", "Dark Oak Minion", "§8§lDark Oak Minion", Material.DARK_OAK_LOG,
                MinionRarity.UNCOMMON, MinionCategory.FORAGING, "§7Chops dark oak logs automatically.",
                Arrays.asList("§7Level 1: 1 dark oak log per 14 seconds", "§7Level 2: 1 dark oak log per 13 seconds", "§7Level 3: 1 dark oak log per 12 seconds", "§7Level 4: 1 dark oak log per 11 seconds", "§7Level 5: 1 dark oak log per 10 seconds"),
                Arrays.asList("§7- 1x Dark Oak Minion", "§7- 1x Enchanted Dark Oak Log", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("CRIMSON_MINION", "Crimson Minion", "§c§lCrimson Minion", Material.CRIMSON_STEM,
                MinionRarity.RARE, MinionCategory.FORAGING, "§7Chops crimson stems automatically.",
                Arrays.asList("§7Level 1: 1 crimson stem per 16 seconds", "§7Level 2: 1 crimson stem per 15 seconds", "§7Level 3: 1 crimson stem per 14 seconds", "§7Level 4: 1 crimson stem per 13 seconds", "§7Level 5: 1 crimson stem per 12 seconds"),
                Arrays.asList("§7- 1x Crimson Minion", "§7- 1x Enchanted Crimson Stem", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("WARPED_MINION", "Warped Minion", "§b§lWarped Minion", Material.WARPED_STEM,
                MinionRarity.RARE, MinionCategory.FORAGING, "§7Chops warped stems automatically.",
                Arrays.asList("§7Level 1: 1 warped stem per 16 seconds", "§7Level 2: 1 warped stem per 15 seconds", "§7Level 3: 1 warped stem per 14 seconds", "§7Level 4: 1 warped stem per 13 seconds", "§7Level 5: 1 warped stem per 12 seconds"),
                Arrays.asList("§7- 1x Warped Minion", "§7- 1x Enchanted Warped Stem", "§7- 1x Enchanted Wood"))
        ));
        
        // FISHING MINIONS
        minionsByCategory.computeIfAbsent(MinionCategory.FISHING, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelMinion("FISHING_MINION", "Fishing Minion", "§b§lFishing Minion", Material.FISHING_ROD,
                MinionRarity.UNCOMMON, MinionCategory.FISHING, "§7Fishes automatically.",
                Arrays.asList("§7Level 1: 1 fish per 20 seconds", "§7Level 2: 1 fish per 19 seconds", "§7Level 3: 1 fish per 18 seconds", "§7Level 4: 1 fish per 17 seconds", "§7Level 5: 1 fish per 16 seconds"),
                Arrays.asList("§7- 1x Fishing Minion", "§7- 1x Enchanted Fish", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("CLAY_MINION", "Clay Minion", "§7§lClay Minion", Material.CLAY,
                MinionRarity.COMMON, MinionCategory.FISHING, "§7Mines clay automatically.",
                Arrays.asList("§7Level 1: 1 clay per 12 seconds", "§7Level 2: 1 clay per 11 seconds", "§7Level 3: 1 clay per 10 seconds", "§7Level 4: 1 clay per 9 seconds", "§7Level 5: 1 clay per 8 seconds"),
                Arrays.asList("§7- 1x Clay Minion", "§7- 1x Enchanted Clay", "§7- 1x Enchanted Wood"))
        ));
        
        // COMBAT MINIONS
        minionsByCategory.computeIfAbsent(MinionCategory.COMBAT, k -> new ArrayList<>()).addAll(Arrays.asList(
            new HypixelMinion("ZOMBIE_MINION", "Zombie Minion", "§2§lZombie Minion", Material.ZOMBIE_HEAD,
                MinionRarity.UNCOMMON, MinionCategory.COMBAT, "§7Kills zombies automatically.",
                Arrays.asList("§7Level 1: 1 zombie per 20 seconds", "§7Level 2: 1 zombie per 19 seconds", "§7Level 3: 1 zombie per 18 seconds", "§7Level 4: 1 zombie per 17 seconds", "§7Level 5: 1 zombie per 16 seconds"),
                Arrays.asList("§7- 1x Zombie Minion", "§7- 1x Enchanted Rotten Flesh", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("SKELETON_MINION", "Skeleton Minion", "§f§lSkeleton Minion", Material.SKELETON_SKULL,
                MinionRarity.UNCOMMON, MinionCategory.COMBAT, "§7Kills skeletons automatically.",
                Arrays.asList("§7Level 1: 1 skeleton per 20 seconds", "§7Level 2: 1 skeleton per 19 seconds", "§7Level 3: 1 skeleton per 18 seconds", "§7Level 4: 1 skeleton per 17 seconds", "§7Level 5: 1 skeleton per 16 seconds"),
                Arrays.asList("§7- 1x Skeleton Minion", "§7- 1x Enchanted Bone", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("SPIDER_MINION", "Spider Minion", "§8§lSpider Minion", Material.SPIDER_EYE,
                MinionRarity.UNCOMMON, MinionCategory.COMBAT, "§7Kills spiders automatically.",
                Arrays.asList("§7Level 1: 1 spider per 20 seconds", "§7Level 2: 1 spider per 19 seconds", "§7Level 3: 1 spider per 18 seconds", "§7Level 4: 1 spider per 17 seconds", "§7Level 5: 1 spider per 16 seconds"),
                Arrays.asList("§7- 1x Spider Minion", "§7- 1x Enchanted String", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("CREEPER_MINION", "Creeper Minion", "§a§lCreeper Minion", Material.CREEPER_HEAD,
                MinionRarity.UNCOMMON, MinionCategory.COMBAT, "§7Kills creepers automatically.",
                Arrays.asList("§7Level 1: 1 creeper per 20 seconds", "§7Level 2: 1 creeper per 19 seconds", "§7Level 3: 1 creeper per 18 seconds", "§7Level 4: 1 creeper per 17 seconds", "§7Level 5: 1 creeper per 16 seconds"),
                Arrays.asList("§7- 1x Creeper Minion", "§7- 1x Enchanted Gunpowder", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("ENDERMAN_MINION", "Enderman Minion", "§5§lEnderman Minion", Material.ENDERMAN_SPAWN_EGG,
                MinionRarity.RARE, MinionCategory.COMBAT, "§7Kills endermen automatically.",
                Arrays.asList("§7Level 1: 1 enderman per 25 seconds", "§7Level 2: 1 enderman per 24 seconds", "§7Level 3: 1 enderman per 23 seconds", "§7Level 4: 1 enderman per 22 seconds", "§7Level 5: 1 enderman per 21 seconds"),
                Arrays.asList("§7- 1x Enderman Minion", "§7- 1x Enchanted Ender Pearl", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("BLAZE_MINION", "Blaze Minion", "§c§lBlaze Minion", Material.BLAZE_ROD,
                MinionRarity.RARE, MinionCategory.COMBAT, "§7Kills blazes automatically.",
                Arrays.asList("§7Level 1: 1 blaze per 25 seconds", "§7Level 2: 1 blaze per 24 seconds", "§7Level 3: 1 blaze per 23 seconds", "§7Level 4: 1 blaze per 22 seconds", "§7Level 5: 1 blaze per 21 seconds"),
                Arrays.asList("§7- 1x Blaze Minion", "§7- 1x Enchanted Blaze Rod", "§7- 1x Enchanted Wood")),
                
            new HypixelMinion("WITHER_SKELETON_MINION", "Wither Skeleton Minion", "§8§lWither Skeleton Minion", Material.WITHER_SKELETON_SKULL,
                MinionRarity.EPIC, MinionCategory.COMBAT, "§7Kills wither skeletons automatically.",
                Arrays.asList("§7Level 1: 1 wither skeleton per 30 seconds", "§7Level 2: 1 wither skeleton per 28 seconds", "§7Level 3: 1 wither skeleton per 26 seconds", "§7Level 4: 1 wither skeleton per 24 seconds", "§7Level 5: 1 wither skeleton per 22 seconds"),
                Arrays.asList("§7- 1x Wither Skeleton Minion", "§7- 1x Wither Skeleton Skull", "§7- 1x Enchanted Wood"))
        ));
    }
    
    private void startMinionUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerMinionData> entry : playerMinionData.entrySet()) {
                    PlayerMinionData minionData = entry.getValue();
                    minionData.update();
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
        
        if (displayName.contains("Minion") || displayName.contains("Hypixel")) {
            openMinionsGUI(player);
        }
    }
    
    public void openMinionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lHypixel Skyblock Minions");
        
        addGUIItem(gui, 10, Material.STONE, "§7§lMining Minions", "§7View all mining minions");
        addGUIItem(gui, 11, Material.WHEAT, "§a§lFarming Minions", "§7View all farming minions");
        addGUIItem(gui, 12, Material.OAK_LOG, "§2§lForaging Minions", "§7View all foraging minions");
        addGUIItem(gui, 13, Material.FISHING_ROD, "§b§lFishing Minions", "§7View all fishing minions");
        addGUIItem(gui, 14, Material.IRON_SWORD, "§c§lCombat Minions", "§7View all combat minions");
        
        player.openInventory(gui);
        player.sendMessage("§aHypixel Skyblock Minions GUI geöffnet!");
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
    
    public PlayerMinionData getPlayerMinionData(UUID playerId) {
        return playerMinionData.computeIfAbsent(playerId, k -> new PlayerMinionData(playerId));
    }
    
    public enum MinionCategory {
        MINING("§7Mining", "§7Mining-focused minions"),
        FARMING("§aFarming", "§7Farming-focused minions"),
        FORAGING("§2Foraging", "§7Foraging-focused minions"),
        FISHING("§bFishing", "§7Fishing-focused minions"),
        COMBAT("§cCombat", "§7Combat-focused minions");
        
        private final String displayName;
        private final String description;
        
        MinionCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum MinionRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        MinionRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class HypixelMinion {
        private final String id;
        private final String name;
        private final String displayName;
        private final Material material;
        private final MinionRarity rarity;
        private final MinionCategory category;
        private final String description;
        private final List<String> levels;
        private final List<String> craftingMaterials;
        
        public HypixelMinion(String id, String name, String displayName, Material material,
                            MinionRarity rarity, MinionCategory category, String description,
                            List<String> levels, List<String> craftingMaterials) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.rarity = rarity;
            this.category = category;
            this.description = description;
            this.levels = levels;
            this.craftingMaterials = craftingMaterials;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public MinionRarity getRarity() { return rarity; }
        public MinionCategory getCategory() { return category; }
        public String getDescription() { return description; }
        public List<String> getLevels() { return levels; }
        public List<String> getCraftingMaterials() { return craftingMaterials; }
    }
    
    public static class PlayerMinionData {
        private final UUID playerId;
        private final Map<String, Integer> minionLevels = new HashMap<>();
        private final Map<String, Boolean> minionUnlocked = new HashMap<>();
        private long lastUpdate;
        
        public PlayerMinionData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void setMinionLevel(String minionId, int level) {
            minionLevels.put(minionId, level);
        }
        
        public void unlockMinion(String minionId) {
            minionUnlocked.put(minionId, true);
        }
        
        public int getMinionLevel(String minionId) {
            return minionLevels.getOrDefault(minionId, 0);
        }
        
        public boolean isMinionUnlocked(String minionId) {
            return minionUnlocked.getOrDefault(minionId, false);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
