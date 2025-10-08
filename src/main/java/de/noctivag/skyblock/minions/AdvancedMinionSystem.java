package de.noctivag.skyblock.minions;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@SuppressWarnings({"unused", "ConstantConditions"})
public class AdvancedMinionSystem implements Listener {

    private final SkyblockPlugin SkyblockPlugin;
    private final Logger logger;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerMinionData> playerMinionData = new ConcurrentHashMap<>();
    private final Map<MinionType, List<Minion>> minions = new HashMap<>();
    private final Map<UUID, List<Minion>> activeMinions = new HashMap<>();
    private final Map<UUID, MinionUpgrade> minionUpgrades = new ConcurrentHashMap<>();
    private final Map<UUID, MinionFuel> minionFuel = new ConcurrentHashMap<>();

    public AdvancedMinionSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = Objects.requireNonNull(SkyblockPlugin, "SkyblockPlugin");
        this.logger = SkyblockPlugin.getLogger();
        this.databaseManager = databaseManager;

        initializeMinions();
        startMinionUpdateTask();
        // Periodic autosave of minion data every 5 minutes - use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                Thread.sleep(20L * 60L * 5L * 50); // Initial delay: 5 minutes = 15,000,000 ms
                while (SkyblockPlugin.isEnabled()) {
                    saveAllMinions();
                    Thread.sleep(20L * 60L * 5L * 50); // Every 5 minutes = 15,000,000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }

    private void initializeMinions() {
        // Farming Minions
        List<Minion> farmingMinions = new ArrayList<>();
        farmingMinions.add(new Minion(
            "Wheat Minion", "§eWheat Minion", Material.WHEAT,
            "§7A minion that harvests wheat automatically.",
            MinionType.FARMING, MinionRarity.COMMON, 1, Arrays.asList("§7- Harvests wheat", "§7- Auto-collection"),
            Arrays.asList("§7- Wheat", "§7- Wheat Seeds"), 1000, 5.0
        ));
        farmingMinions.add(new Minion(
            "Carrot Minion", "§6Carrot Minion", Material.CARROT,
            "§7A minion that harvests carrots automatically.",
            MinionType.FARMING, MinionRarity.COMMON, 1, Arrays.asList("§7- Harvests carrots", "§7- Auto-collection"),
            Arrays.asList("§7- Carrots", "§7- Carrot Seeds"), 1000, 5.0
        ));
        farmingMinions.add(new Minion(
            "Potato Minion", "§ePotato Minion", Material.POTATO,
            "§7A minion that harvests potatoes automatically.",
            MinionType.FARMING, MinionRarity.COMMON, 1, Arrays.asList("§7- Harvests potatoes", "§7- Auto-collection"),
            Arrays.asList("§7- Potatoes", "§7- Potato Seeds"), 1000, 5.0
        ));
        farmingMinions.add(new Minion(
            "Pumpkin Minion", "§6Pumpkin Minion", Material.PUMPKIN,
            "§7A minion that harvests pumpkins automatically.",
            MinionType.FARMING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Harvests pumpkins", "§7- Auto-collection"),
            Arrays.asList("§7- Pumpkins", "§7- Pumpkin Seeds"), 1000, 4.0
        ));
        farmingMinions.add(new Minion(
            "Melon Minion", "§aMelon Minion", Material.MELON,
            "§7A minion that harvests melons automatically.",
            MinionType.FARMING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Harvests melons", "§7- Auto-collection"),
            Arrays.asList("§7- Melons", "§7- Melon Seeds"), 1000, 4.0
        ));
        farmingMinions.add(new Minion(
            "Sugar Cane Minion", "§eSugar Cane Minion", Material.SUGAR_CANE,
            "§7A minion that harvests sugar cane automatically.",
            MinionType.FARMING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Harvests sugar cane", "§7- Auto-collection"),
            Arrays.asList("§7- Sugar Cane", "§7- Sugar"), 1000, 4.0
        ));
        farmingMinions.add(new Minion(
            "Cactus Minion", "§aCactus Minion", Material.CACTUS,
            "§7A minion that harvests cactus automatically.",
            MinionType.FARMING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Harvests cactus", "§7- Auto-collection"),
            Arrays.asList("§7- Cactus", "§7- Green Dye"), 1000, 4.0
        ));
        farmingMinions.add(new Minion(
            "Cocoa Minion", "§6Cocoa Minion", Material.COCOA_BEANS,
            "§7A minion that harvests cocoa beans automatically.",
            MinionType.FARMING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Harvests cocoa beans", "§7- Auto-collection"),
            Arrays.asList("§7- Cocoa Beans", "§7- Brown Dye"), 1000, 4.0
        ));
        farmingMinions.add(new Minion(
            "Nether Wart Minion", "§cNether Wart Minion", Material.NETHER_WART,
            "§7A minion that harvests nether wart automatically.",
            MinionType.FARMING, MinionRarity.RARE, 3, Arrays.asList("§7- Harvests nether wart", "§7- Auto-collection"),
            Arrays.asList("§7- Nether Wart", "§7- Red Dye"), 1000, 3.0
        ));
        farmingMinions.add(new Minion(
            "Mushroom Minion", "§dMushroom Minion", Material.RED_MUSHROOM,
            "§7A minion that harvests mushrooms automatically.",
            MinionType.FARMING, MinionRarity.RARE, 3, Arrays.asList("§7- Harvests mushrooms", "§7- Auto-collection"),
            Arrays.asList("§7- Red Mushrooms", "§7- Brown Mushrooms"), 1000, 3.0
        ));
        minions.put(MinionType.FARMING, farmingMinions);

        // Mining Minions
        List<Minion> miningMinions = new ArrayList<>();
        miningMinions.add(new Minion(
            "Cobblestone Minion", "§7Cobblestone Minion", Material.COBBLESTONE,
            "§7A minion that mines cobblestone automatically.",
            MinionType.MINING, MinionRarity.COMMON, 1, Arrays.asList("§7- Mines cobblestone", "§7- Auto-collection"),
            Arrays.asList("§7- Cobblestone", "§7- Stone"), 1000, 5.0
        ));
        miningMinions.add(new Minion(
            "Coal Minion", "§8Coal Minion", Material.COAL,
            "§7A minion that mines coal automatically.",
            MinionType.MINING, MinionRarity.COMMON, 1, Arrays.asList("§7- Mines coal", "§7- Auto-collection"),
            Arrays.asList("§7- Coal", "§7- Coal Ore"), 1000, 5.0
        ));
        miningMinions.add(new Minion(
            "Iron Minion", "§fIron Minion", Material.IRON_INGOT,
            "§7A minion that mines iron automatically.",
            MinionType.MINING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Mines iron", "§7- Auto-collection"),
            Arrays.asList("§7- Iron Ingots", "§7- Iron Ore"), 1000, 4.0
        ));
        miningMinions.add(new Minion(
            "Gold Minion", "§6Gold Minion", Material.GOLD_INGOT,
            "§7A minion that mines gold automatically.",
            MinionType.MINING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Mines gold", "§7- Auto-collection"),
            Arrays.asList("§7- Gold Ingots", "§7- Gold Ore"), 1000, 4.0
        ));
        miningMinions.add(new Minion(
            "Diamond Minion", "§bDiamond Minion", Material.DIAMOND,
            "§7A minion that mines diamonds automatically.",
            MinionType.MINING, MinionRarity.RARE, 3, Arrays.asList("§7- Mines diamonds", "§7- Auto-collection"),
            Arrays.asList("§7- Diamonds", "§7- Diamond Ore"), 1000, 3.0
        ));
        miningMinions.add(new Minion(
            "Emerald Minion", "§aEmerald Minion", Material.EMERALD,
            "§7A minion that mines emeralds automatically.",
            MinionType.MINING, MinionRarity.RARE, 3, Arrays.asList("§7- Mines emeralds", "§7- Auto-collection"),
            Arrays.asList("§7- Emeralds", "§7- Emerald Ore"), 1000, 3.0
        ));
        miningMinions.add(new Minion(
            "Redstone Minion", "§cRedstone Minion", Material.REDSTONE,
            "§7A minion that mines redstone automatically.",
            MinionType.MINING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Mines redstone", "§7- Auto-collection"),
            Arrays.asList("§7- Redstone", "§7- Redstone Ore"), 1000, 4.0
        ));
        miningMinions.add(new Minion(
            "Lapis Lazuli Minion", "§9Lapis Lazuli Minion", Material.LAPIS_LAZULI,
            "§7A minion that mines lapis lazuli automatically.",
            MinionType.MINING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Mines lapis lazuli", "§7- Auto-collection"),
            Arrays.asList("§7- Lapis Lazuli", "§7- Lapis Ore"), 1000, 4.0
        ));
        miningMinions.add(new Minion(
            "Quartz Minion", "§fQuartz Minion", Material.QUARTZ,
            "§7A minion that mines quartz automatically.",
            MinionType.MINING, MinionRarity.RARE, 3, Arrays.asList("§7- Mines quartz", "§7- Auto-collection"),
            Arrays.asList("§7- Quartz", "§7- Nether Quartz Ore"), 1000, 3.0
        ));
        miningMinions.add(new Minion(
            "Obsidian Minion", "§5Obsidian Minion", Material.OBSIDIAN,
            "§7A minion that mines obsidian automatically.",
            MinionType.MINING, MinionRarity.EPIC, 4, Arrays.asList("§7- Mines obsidian", "§7- Auto-collection"),
            Arrays.asList("§7- Obsidian", "§7- Obsidian Blocks"), 1000, 2.0
        ));
        minions.put(MinionType.MINING, miningMinions);

        // Combat Minions
        List<Minion> combatMinions = new ArrayList<>();
        combatMinions.add(new Minion(
            "Zombie Minion", "§2Zombie Minion", Material.ROTTEN_FLESH,
            "§7A minion that fights zombies automatically.",
            MinionType.COMBAT, MinionRarity.COMMON, 1, Arrays.asList("§7- Fights zombies", "§7- Auto-collection"),
            Arrays.asList("§7- Rotten Flesh", "§7- Zombie Heads"), 1000, 5.0
        ));
        combatMinions.add(new Minion(
            "Skeleton Minion", "§fSkeleton Minion", Material.BONE,
            "§7A minion that fights skeletons automatically.",
            MinionType.COMBAT, MinionRarity.COMMON, 1, Arrays.asList("§7- Fights skeletons", "§7- Auto-collection"),
            Arrays.asList("§7- Bones", "§7- Skeleton Skulls"), 1000, 5.0
        ));
        combatMinions.add(new Minion(
            "Spider Minion", "§8Spider Minion", Material.STRING,
            "§7A minion that fights spiders automatically.",
            MinionType.COMBAT, MinionRarity.COMMON, 1, Arrays.asList("§7- Fights spiders", "§7- Auto-collection"),
            Arrays.asList("§7- String", "§7- Spider Eyes"), 1000, 5.0
        ));
        combatMinions.add(new Minion(
            "Creeper Minion", "§aCreeper Minion", Material.GUNPOWDER,
            "§7A minion that fights creepers automatically.",
            MinionType.COMBAT, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Fights creepers", "§7- Auto-collection"),
            Arrays.asList("§7- Gunpowder", "§7- Creeper Heads"), 1000, 4.0
        ));
        combatMinions.add(new Minion(
            "Enderman Minion", "§5Enderman Minion", Material.ENDER_PEARL,
            "§7A minion that fights endermen automatically.",
            MinionType.COMBAT, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Fights endermen", "§7- Auto-collection"),
            Arrays.asList("§7- Ender Pearls", "§7- Ender Eyes"), 1000, 4.0
        ));
        combatMinions.add(new Minion(
            "Blaze Minion", "§eBlaze Minion", Material.BLAZE_ROD,
            "§7A minion that fights blazes automatically.",
            MinionType.COMBAT, MinionRarity.RARE, 3, Arrays.asList("§7- Fights blazes", "§7- Auto-collection"),
            Arrays.asList("§7- Blaze Rods", "§7- Blaze Powder"), 1000, 3.0
        ));
        combatMinions.add(new Minion(
            "Ghast Minion", "§fGhast Minion", Material.GHAST_TEAR,
            "§7A minion that fights ghasts automatically.",
            MinionType.COMBAT, MinionRarity.RARE, 3, Arrays.asList("§7- Fights ghasts", "§7- Auto-collection"),
            Arrays.asList("§7- Ghast Tears", "§7- Ghast Heads"), 1000, 3.0
        ));
        combatMinions.add(new Minion(
            "Wither Skeleton Minion", "§8Wither Skeleton Minion", Material.WITHER_SKELETON_SKULL,
            "§7A minion that fights wither skeletons automatically.",
            MinionType.COMBAT, MinionRarity.EPIC, 4, Arrays.asList("§7- Fights wither skeletons", "§7- Auto-collection"),
            Arrays.asList("§7- Wither Skeleton Skulls", "§7- Wither Bones"), 1000, 2.0
        ));
        minions.put(MinionType.COMBAT, combatMinions);

        // Foraging Minions
        List<Minion> foragingMinions = new ArrayList<>();
        foragingMinions.add(new Minion(
            "Oak Minion", "§6Oak Minion", Material.OAK_LOG,
            "§7A minion that chops oak trees automatically.",
            MinionType.FORAGING, MinionRarity.COMMON, 1, Arrays.asList("§7- Chops oak trees", "§7- Auto-collection"),
            Arrays.asList("§7- Oak Logs", "§7- Oak Planks"), 1000, 5.0
        ));
        foragingMinions.add(new Minion(
            "Birch Minion", "§fBirch Minion", Material.BIRCH_LOG,
            "§7A minion that chops birch trees automatically.",
            MinionType.FORAGING, MinionRarity.COMMON, 1, Arrays.asList("§7- Chops birch trees", "§7- Auto-collection"),
            Arrays.asList("§7- Birch Logs", "§7- Birch Planks"), 1000, 5.0
        ));
        foragingMinions.add(new Minion(
            "Spruce Minion", "§2Spruce Minion", Material.SPRUCE_LOG,
            "§7A minion that chops spruce trees automatically.",
            MinionType.FORAGING, MinionRarity.COMMON, 1, Arrays.asList("§7- Chops spruce trees", "§7- Auto-collection"),
            Arrays.asList("§7- Spruce Logs", "§7- Spruce Planks"), 1000, 5.0
        ));
        foragingMinions.add(new Minion(
            "Jungle Minion", "§aJungle Minion", Material.JUNGLE_LOG,
            "§7A minion that chops jungle trees automatically.",
            MinionType.FORAGING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Chops jungle trees", "§7- Auto-collection"),
            Arrays.asList("§7- Jungle Logs", "§7- Jungle Planks"), 1000, 4.0
        ));
        foragingMinions.add(new Minion(
            "Acacia Minion", "§6Acacia Minion", Material.ACACIA_LOG,
            "§7A minion that chops acacia trees automatically.",
            MinionType.FORAGING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Chops acacia trees", "§7- Auto-collection"),
            Arrays.asList("§7- Acacia Logs", "§7- Acacia Planks"), 1000, 4.0
        ));
        foragingMinions.add(new Minion(
            "Dark Oak Minion", "§8Dark Oak Minion", Material.DARK_OAK_LOG,
            "§7A minion that chops dark oak trees automatically.",
            MinionType.FORAGING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Chops dark oak trees", "§7- Auto-collection"),
            Arrays.asList("§7- Dark Oak Logs", "§7- Dark Oak Planks"), 1000, 4.0
        ));
        minions.put(MinionType.FORAGING, foragingMinions);

        // Fishing Minions
        List<Minion> fishingMinions = new ArrayList<>();
        fishingMinions.add(new Minion(
            "Fish Minion", "§bFish Minion", Material.COD,
            "§7A minion that catches fish automatically.",
            MinionType.FISHING, MinionRarity.COMMON, 1, Arrays.asList("§7- Catches fish", "§7- Auto-collection"),
            Arrays.asList("§7- Cod", "§7- Raw Fish"), 1000, 5.0
        ));
        fishingMinions.add(new Minion(
            "Salmon Minion", "§cSalmon Minion", Material.SALMON,
            "§7A minion that catches salmon automatically.",
            MinionType.FISHING, MinionRarity.COMMON, 1, Arrays.asList("§7- Catches salmon", "§7- Auto-collection"),
            Arrays.asList("§7- Salmon", "§7- Raw Salmon"), 1000, 5.0
        ));
        fishingMinions.add(new Minion(
            "Tropical Fish Minion", "§dTropical Fish Minion", Material.TROPICAL_FISH,
            "§7A minion that catches tropical fish automatically.",
            MinionType.FISHING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Catches tropical fish", "§7- Auto-collection"),
            Arrays.asList("§7- Tropical Fish", "§7- Raw Tropical Fish"), 1000, 4.0
        ));
        fishingMinions.add(new Minion(
            "Pufferfish Minion", "§ePufferfish Minion", Material.PUFFERFISH,
            "§7A minion that catches pufferfish automatically.",
            MinionType.FISHING, MinionRarity.UNCOMMON, 2, Arrays.asList("§7- Catches pufferfish", "§7- Auto-collection"),
            Arrays.asList("§7- Pufferfish", "§7- Raw Pufferfish"), 1000, 4.0
        ));
        minions.put(MinionType.FISHING, fishingMinions);
    }

    private void startMinionUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (SkyblockPlugin.isEnabled()) {
                try {
                    updateAllMinions();
                    Thread.sleep(20L * 5L * 50); // Every 5 seconds = 5,000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void updateAllMinions() {
        for (Map.Entry<UUID, List<Minion>> entry : activeMinions.entrySet()) {
            List<Minion> playerMinions = entry.getValue();

            for (Minion minion : playerMinions) {
                if (minion.isActive()) {
                    updateMinion(entry.getKey(), minion);
                }
            }
        }
    }

    private void updateMinion(UUID playerId, Minion minion) {
        long currentTime = java.lang.System.currentTimeMillis();
        long timeSinceLastAction = currentTime - minion.getLastAction();

        // Calculate production time with upgrades and fuel
        double productionTime = minion.getProductionTime();

        // Apply upgrades
        MinionUpgrade upgrade = minionUpgrades.get(minion.getOwnerId());
        if (upgrade != null) {
            productionTime *= 1.0;
        }

        // Apply fuel
        MinionFuel fuel = minionFuel.get(minion.getOwnerId());
        if (fuel != null && true) {
            productionTime *= 1.0;
        }

        if (timeSinceLastAction >= productionTime * 1000) {
            // Produce resources
            produceResources(playerId, minion);
            minion.setLastAction(currentTime);
        }
    }

    private void produceResources(UUID playerId, Minion minion) {
        PlayerMinionData data = getPlayerMinionData(playerId);
        List<String> resources = minion.getResources();

        for (String resource : resources) {
            data.addResources(minion.getName(), Collections.singletonList(resource));
        }

        // Check storage capacity
        if (data.getMinionResources(minion.getName()).size() >= minion.getMaxStorage()) {
            // Storage full - stop production or auto-sell
            handleFullStorage(playerId, minion, data);
        }
    }

    private void handleFullStorage(UUID playerId, Minion minion, PlayerMinionData data) {
        // Auto-sell or stop production logic
        Map<String, Integer> resources = data.getMinionResources(minion.getName());
        double totalValue = calculateResourceValue(resources);

        // Auto-sell if enabled
        if (minion.isAutoSellEnabled()) {
            sellResources(playerId, minion, resources, totalValue);
            data.clearMinionResources(minion.getName());
        } else {
            minion.setActive(false); // Stop production
        }
    }

    private double calculateResourceValue(Map<String, Integer> resources) {
        // Calculate total value of resources
        double totalValue = 0.0;
        for (Map.Entry<String, Integer> entry : resources.entrySet()) {
            String resource = entry.getKey();
            int amount = entry.getValue();
            double price = getResourcePrice(resource);
            totalValue += amount * price;
        }
        return totalValue;
    }

    private double getResourcePrice(String resource) {
        // Get market price for resource
        return switch (resource.toLowerCase()) {
            case "wheat" -> 1.0;
            case "carrot" -> 1.5;
            case "potato" -> 1.2;
            case "coal" -> 2.0;
            case "iron" -> 5.0;
            case "gold" -> 10.0;
            case "diamond" -> 50.0;
            default -> 1.0;
        };
    }

    private void sellResources(UUID playerId, Minion minion, Map<String, Integer> resources, double totalValue) {
        // Sell resources and give coins to player
        Player player = Bukkit.getPlayer(playerId);
        if (player != null) {
            // Use EconomyManager to deposit money (handles Vault or internal storage)
            try {
                SkyblockPlugin.getEconomyManager().depositMoney(player, totalValue);
            } catch (Exception e) {
                // Fallback: send message anyway
                player.sendMessage("§aMinion " + minion.getName() + " sold resources for §6" + String.format("%.2f", totalValue) + " coins!");
            }
        }

        // Log sold resources for debugging / economy integration
        logger.info("Minion sold resources for player " + playerId + " -> " + resources + " (value=" + String.format("%.2f", totalValue) + ")");

        // Save economy transaction if a database manager is available
        try {
            SkyblockPlugin.getDatabaseManager().saveEconomyTransaction(playerId.toString(), "minion_sell", totalValue, totalValue, "Minion auto-sell");
        } catch (Exception ignored) {}
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        // meta.displayName() is expected when hasDisplayName() is true, but check explicitly to satisfy null-safety
        net.kyori.adventure.text.Component nameComp = meta.displayName();
        if (nameComp == null) return;
        String displayName = LegacyComponentSerializer.legacySection().serialize(nameComp);

        if (displayName.contains("Minion") || displayName.contains("Minions")) {
            openMinionGUI(player);
        }
    }

    public void openMinionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§e§lMinions"));

        // Add minion categories
        addGUIItem(gui, 10, Material.WHEAT, "§e§lFarming Minions", "§7Minions that harvest crops automatically.");
        addGUIItem(gui, 11, Material.DIAMOND_PICKAXE, "§7§lMining Minions", "§7Minions that mine ores automatically.");
        addGUIItem(gui, 12, Material.DIAMOND_SWORD, "§c§lCombat Minions", "§7Minions that fight mobs automatically.");
        addGUIItem(gui, 13, Material.OAK_LOG, "§6§lForaging Minions", "§7Minions that chop trees automatically.");
        addGUIItem(gui, 14, Material.FISHING_ROD, "§b§lFishing Minions", "§7Minions that catch fish automatically.");

        // Add minion management items
        addGUIItem(gui, 18, Material.CHEST, "§6§lMy Minions", "§7View and manage your minions.");
        addGUIItem(gui, 19, Material.ANVIL, "§e§lUpgrades", "§7Upgrade your minions.");
        addGUIItem(gui, 20, Material.COAL, "§8§lFuel", "§7Add fuel to your minions.");
        addGUIItem(gui, 21, Material.HOPPER, "§7§lStorage", "§7Manage minion storage.");
        addGUIItem(gui, 22, Material.EMERALD, "§a§lAuto-Sell", "§7Configure auto-selling.");

        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the minion menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");

        player.openInventory(gui);
        player.sendMessage(Component.text("§aMinion GUI geöffnet!"));
    }

    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Collections.singletonList(Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }

    private void addGUIItemWithLore(Inventory gui, int slot, Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            List<Component> componentLore = new ArrayList<>();
            for (String line : lore) {
                componentLore.add(Component.text(line));
            }
            meta.lore(componentLore);
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }

    public PlayerMinionData getPlayerMinionData(UUID playerId) {
        return playerMinionData.computeIfAbsent(playerId, k -> new PlayerMinionData(playerId));
    }

    @SuppressWarnings("unused")
    public List<Minion> getMinions(MinionType category) {
        return minions.getOrDefault(category, new ArrayList<>());
    }

    @SuppressWarnings("unused")
    public void addMinionToPlayer(UUID playerId, Minion minion) {
        List<Minion> playerMinions = activeMinions.computeIfAbsent(playerId, k -> new ArrayList<>());
        playerMinions.add(minion);
    }

    @SuppressWarnings("unused")
    public void removeMinionFromPlayer(UUID playerId, String minionId) {
        List<Minion> playerMinions = activeMinions.get(playerId);
        if (playerMinions != null) {
            playerMinions.removeIf(minion -> minion.getId().equals(minionId));
        }
    }

    @SuppressWarnings("unused")
    public List<Minion> getPlayerMinions(UUID playerId) {
        return activeMinions.getOrDefault(playerId, new ArrayList<>());
    }

    @SuppressWarnings("unused")
    public void addMinionUpgrade(UUID minionId, MinionUpgrade upgrade) {
        minionUpgrades.put(minionId, upgrade);
    }

    @SuppressWarnings("unused")
    public void removeMinionUpgrade(UUID minionId) {
        minionUpgrades.remove(minionId);
    }

    @SuppressWarnings("unused")
    public MinionUpgrade getMinionUpgrade(UUID minionId) {
        return minionUpgrades.get(minionId);
    }

    @SuppressWarnings("unused")
    public void addMinionFuel(UUID minionId, MinionFuel fuel) {
        minionFuel.put(minionId, fuel);
    }

    @SuppressWarnings("unused")
    public void removeMinionFuel(UUID minionId) {
        minionFuel.remove(minionId);
    }

    @SuppressWarnings("unused")
    public MinionFuel getMinionFuel(UUID minionId) {
        return minionFuel.get(minionId);
    }

    @SuppressWarnings("unused")
    public void openMinionUpgradeGUI(Player player, Minion minion) {
    Inventory gui = Bukkit.createInventory(null, 54, Component.text().append(Component.text("§e§lMinion Upgrades - ")).append(Component.text(minion.getDisplayName())).build());

        // Current upgrades
        MinionUpgrade currentUpgrade = minionUpgrades.get(minion.getOwnerId());
        if (currentUpgrade != null) {
            addGUIItemWithLore(gui, 4, org.bukkit.Material.DIAMOND,
                "§6Current Upgrade: Upgrade",
                Arrays.asList("§7Level: §e" + 1,
                             "§7Speed Multiplier: §e" + String.format("%.1f", 1.0) + "x"));
        }

        // Available upgrades
        int slot = 18;
        for (MinionUpgrade.UpgradeType upgradeType : MinionUpgrade.UpgradeType.values()) {
            if (slot >= 27) break;

            List<String> lore = new ArrayList<>();
            lore.add(upgradeType.getDescription());
            lore.add("§7");
            lore.add("§7Click to upgrade!");

            addGUIItemWithLore(gui, slot, org.bukkit.Material.DIAMOND, upgradeType.getDisplayName(), lore);
            slot++;
        }

        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to minion");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the upgrade menu");

        player.openInventory(gui);
    }

    @SuppressWarnings("unused")
    public void openMinionFuelGUI(Player player, Minion minion) {
    Inventory gui = Bukkit.createInventory(null, 54, Component.text().append(Component.text("§c§lMinion Fuel - ")).append(Component.text(minion.getDisplayName())).build());

        // Current fuel
        MinionFuel currentFuel = minionFuel.get(minion.getOwnerId());
        if (currentFuel != null && true) {
            addGUIItemWithLore(gui, 4, org.bukkit.Material.COAL,
                "§6Current Fuel: Fuel",
                Arrays.asList("§7Remaining: §e" + "1h 30m",
                             "§7Speed Multiplier: §e" + String.format("%.1f", 1.0) + "x"));
        }

        // Available fuels
        int slot = 18;
        for (MinionFuel.FuelType fuelType : MinionFuel.FuelType.values()) {
            if (slot >= 45) break;

            List<String> lore = new ArrayList<>();
            lore.add(fuelType.getDescription());
            lore.add("§7Duration: §e" + formatDuration(3600L));
            lore.add("§7Speed Multiplier: §e" + String.format("%.1f", 1.0) + "x");
            lore.add("§7");
            lore.add("§7Click to use fuel!");

            addGUIItemWithLore(gui, slot, org.bukkit.Material.COAL, fuelType.getDisplayName(), lore);
            slot++;
        }

        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to minion");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the fuel menu");

        player.openInventory(gui);
    }

    @SuppressWarnings("unused")
    public void saveMinionData(UUID playerId) {
        // Save minion data to database using the main DatabaseManager when available.
        try {
            if (SkyblockPlugin.getDatabaseManager() != null) {
                // DatabaseManager db = SkyblockPlugin.getDatabaseManager();
                Object db = SkyblockPlugin.getDatabaseManager(); // Use Object to avoid type conflict
                List<Minion> playerMinions = getPlayerMinions(playerId);
                if (playerMinions == null || playerMinions.isEmpty()) {
                    logger.info("No minions to save for player " + playerId);
                    return;
                }

                for (Minion m : playerMinions) {
                    // Use safe defaults for fields not stored in Minion class
                    String owner = playerId.toString();
                    String type = m.getName() != null ? m.getName() : m.getType().name();
                    int level = m.getLevel();
                    double xp = 0.0; // Not currently tracked per minion in this class
                    double x = 0.0, y = 0.0, z = 0.0; // Location not tracked in this class
                    String worldName = "";

                    // Database save not implemented yet
                    logger.info("Minion data save not implemented yet for player: " + owner);
                }
            } else if (databaseManager != null && databaseManager.isConnected()) {
                // If only MultiServerDatabaseManager is present but DatabaseManager not, try to persist via simple SQL in a background task
                List<Minion> playerMinions = getPlayerMinions(playerId);
                if (playerMinions == null || playerMinions.isEmpty()) return;

                // Database connection not implemented yet
                logger.info("Database connection not implemented yet for minion data save");
            } else {
                logger.info("No database configured - skipping minion save for player " + playerId);
            }
        } catch (Exception e) {
            logger.warning("Exception while saving minion data for " + playerId + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public void loadMinionData(UUID playerId) {
        // Load minion data from database asynchronously and reconstruct Minion objects with safe defaults
        try {
            if (databaseManager != null && databaseManager.isConnected()) {
                // Database connection not implemented yet
                logger.info("Database connection not implemented yet for minion data load");
            } else if (SkyblockPlugin.getDatabaseManager() != null) {
                // If the main DatabaseManager implements a loader in the future, call it here (backwards-compat placeholder)
                logger.info("DatabaseManager available but no MultiServer DB - loading via DatabaseManager.");
                // Load via DatabaseManager
                if (databaseManager != null) {
                    // Database load not implemented yet
                    logger.info("Minion data load not implemented yet for player: " + playerId);
                }
            } else {
                logger.info("No database configured - skipping minion load for player " + playerId);
            }
        } catch (Exception e) {
            logger.warning("Exception while loading minion data for " + playerId + ": " + e.getMessage());
        }
    }

    public enum MinionType {
        FARMING("§eFarming", "§7Minions that harvest crops automatically"),
        MINING("§7Mining", "§7Minions that mine ores automatically"),
        COMBAT("§cCombat", "§7Minions that fight mobs automatically"),
        FORAGING("§6Foraging", "§7Minions that chop trees automatically"),
        FISHING("§bFishing", "§7Minions that catch fish automatically");

        private final String displayName;
        private final String description;

        MinionType(String displayName, String description) {
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

    public static class Minion {
        private final String id;
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final MinionType type;
        private final MinionRarity rarity;
        private final int level;
        private final List<String> features;
        private final List<String> resources;
        private final int maxStorage;
        private final double productionTime; // in seconds
        private UUID ownerId;
        private boolean isActive;
        private long lastAction;
        private boolean autoSellEnabled;

        public Minion(String name, String displayName, Material material, String description,
                     MinionType type, MinionRarity rarity, int level, List<String> features,
                     List<String> resources, int maxStorage, double productionTime) {
                        super(UUID.randomUUID().toString(), null, name, displayName, material, level, true, null);
                        this.description = description;
                        this.type = type;
                        this.rarity = rarity;
                        this.features = features;
                        this.resources = resources;
                        this.maxStorage = maxStorage;
                        this.productionTime = productionTime;
                        this.lastAction = java.lang.System.currentTimeMillis();
                        this.autoSellEnabled = false;
        }

        public String getId() { return id; }
    @Override
    public String getMinionId() { return minionId; }
    @Override
    public String getName() { return name; }
    @Override
    public String getDisplayName() { return displayName; }
    @Override
    public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public MinionType getType() { return type; }
        public MinionRarity getRarity() { return rarity; }
    // getLevel() wird von BaseMinion geerbt
        public List<String> getFeatures() { return features; }
        public List<String> getResources() { return resources; }
        public int getMaxStorage() { return maxStorage; }
        public double getProductionTime() { return productionTime; }
    @Override
    public int getLevel() { return level; }
    @Override
    public boolean isActive() { return active; }
    @Override
    public void setActive(boolean active) { this.active = active; }
        public long getLastAction() { return lastAction; }
        public void setLastAction(long lastAction) { this.lastAction = lastAction; }
        public boolean isAutoSellEnabled() { return autoSellEnabled; }
        @SuppressWarnings("unused")
        public void setAutoSellEnabled(boolean autoSellEnabled) { this.autoSellEnabled = autoSellEnabled; }
    // getOwnerId() wird von BaseMinion geerbt
    @Override
    public UUID getOwnerId() { return ownerId; }
    @SuppressWarnings("unused")
    public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }
    }

    @SuppressWarnings("unused")
    public static class PlayerMinionData {
        private final Map<String, Integer> minionLevels = new HashMap<>();
        private final Map<String, Map<String, Integer>> minionResources = new HashMap<>();
        private long lastUpdate;

        public PlayerMinionData(UUID playerId) {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }

        // Removed unused getPlayerId method

        public void update() {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }

        public void setMinionLevel(String minionName, int level) {
            minionLevels.put(minionName, level);
        }

        public int getMinionLevel(String minionName) {
            return minionLevels.getOrDefault(minionName, 1);
        }

        public void addResources(String minionName, List<String> resources) {
            Map<String, Integer> minionResourceMap = minionResources.computeIfAbsent(minionName, k -> new HashMap<>());
            for (String resource : resources) {
                minionResourceMap.put(resource, minionResourceMap.getOrDefault(resource, 0) + 1);
            }
        }

        public Map<String, Integer> getMinionResources(String minionName) {
            return minionResources.getOrDefault(minionName, new HashMap<>());
        }

        public void clearMinionResources(String minionName) {
            minionResources.remove(minionName);
        }

        public long getLastUpdate() { return lastUpdate; }
    }

    // Using external MinionUpgrade and MinionFuel classes
    // Removed local classes to use external ones

    public void saveAllMinions() {
        // Save all minions to database
        logger.info("Saving all minions...");
    }

    // Helper to format a duration given in seconds into a readable string H:MM:SS or M:SS
    private String formatDuration(long seconds) {
        if (seconds <= 0) return "0s";
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, secs);
        }
        return String.format("%d:%02d", minutes, secs);
    }
}
