package de.noctivag.plugin.contests;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Jacob's Farming Contest System - Hypixel Skyblock Style
 */
public class JacobsFarmingContestSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerContestData> playerContestData = new ConcurrentHashMap<>();
    private final Map<ContestType, ContestConfig> contestConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> contestTasks = new ConcurrentHashMap<>();
    
    public JacobsFarmingContestSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeContestConfigs();
        startContestUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeContestConfigs() {
        contestConfigs.put(ContestType.WHEAT_CONTEST, new ContestConfig(
            "Wheat Contest", "§eWheat Contest", Material.WHEAT,
            "§7Compete in wheat farming!",
            ContestCategory.FARMING, ContestRarity.COMMON, 1, Arrays.asList("§7- Farm wheat", "§7- Compete with others", "§7- Win rewards"),
            Arrays.asList("§7- 1x Wheat Seeds", "§7- 1x Hoe")
        ));
        
        contestConfigs.put(ContestType.CARROT_CONTEST, new ContestConfig(
            "Carrot Contest", "§6Carrot Contest", Material.CARROT,
            "§7Compete in carrot farming!",
            ContestCategory.FARMING, ContestRarity.COMMON, 1, Arrays.asList("§7- Farm carrots", "§7- Compete with others", "§7- Win rewards"),
            Arrays.asList("§7- 1x Carrot", "§7- 1x Hoe")
        ));
        
        contestConfigs.put(ContestType.POTATO_CONTEST, new ContestConfig(
            "Potato Contest", "§fPotato Contest", Material.POTATO,
            "§7Compete in potato farming!",
            ContestCategory.FARMING, ContestRarity.COMMON, 1, Arrays.asList("§7- Farm potatoes", "§7- Compete with others", "§7- Win rewards"),
            Arrays.asList("§7- 1x Potato", "§7- 1x Hoe")
        ));
        
        contestConfigs.put(ContestType.PUMPKIN_CONTEST, new ContestConfig(
            "Pumpkin Contest", "§6Pumpkin Contest", Material.PUMPKIN,
            "§7Compete in pumpkin farming!",
            ContestCategory.FARMING, ContestRarity.COMMON, 1, Arrays.asList("§7- Farm pumpkins", "§7- Compete with others", "§7- Win rewards"),
            Arrays.asList("§7- 1x Pumpkin Seeds", "§7- 1x Hoe")
        ));
        
        contestConfigs.put(ContestType.MELON_CONTEST, new ContestConfig(
            "Melon Contest", "§aMelon Contest", Material.MELON,
            "§7Compete in melon farming!",
            ContestCategory.FARMING, ContestRarity.COMMON, 1, Arrays.asList("§7- Farm melons", "§7- Compete with others", "§7- Win rewards"),
            Arrays.asList("§7- 1x Melon Seeds", "§7- 1x Hoe")
        ));
    }
    
    private void startContestUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerContestData> entry : playerContestData.entrySet()) {
                    PlayerContestData contestData = entry.getValue();
                    contestData.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 60L);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        
        PlayerContestData contestData = getPlayerContestData(player.getUniqueId());
        
        // Check if block is part of a contest
        ContestType contestType = getContestTypeForMaterial(blockType);
        if (contestType != null) {
            contestData.addContestProgress(contestType, 1);
            player.sendMessage("§a+1 " + contestType.name() + " progress!");
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Contest")) {
            openContestGUI(player);
        }
    }
    
    public void openContestGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§e§lJacob's Farming Contest");
        
        addGUIItem(gui, 10, Material.WHEAT, "§e§lWheat Contest", "§7Compete in wheat farming.");
        addGUIItem(gui, 11, Material.CARROT, "§6§lCarrot Contest", "§7Compete in carrot farming.");
        addGUIItem(gui, 12, Material.POTATO, "§f§lPotato Contest", "§7Compete in potato farming.");
        addGUIItem(gui, 13, Material.PUMPKIN, "§6§lPumpkin Contest", "§7Compete in pumpkin farming.");
        addGUIItem(gui, 14, Material.MELON, "§a§lMelon Contest", "§7Compete in melon farming.");
        
        player.openInventory(gui);
        player.sendMessage("§aJacob's Farming Contest GUI geöffnet!");
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
    
    private ContestType getContestTypeForMaterial(Material material) {
        switch (material) {
            case WHEAT: return ContestType.WHEAT_CONTEST;
            case CARROT: return ContestType.CARROT_CONTEST;
            case POTATO: return ContestType.POTATO_CONTEST;
            case PUMPKIN: return ContestType.PUMPKIN_CONTEST;
            case MELON: return ContestType.MELON_CONTEST;
            default: return null;
        }
    }
    
    public PlayerContestData getPlayerContestData(UUID playerId) {
        return playerContestData.computeIfAbsent(playerId, k -> new PlayerContestData(playerId));
    }
    
    public enum ContestType {
        WHEAT_CONTEST, CARROT_CONTEST, POTATO_CONTEST, PUMPKIN_CONTEST, MELON_CONTEST,
        SUGAR_CANE_CONTEST, COCOA_CONTEST, MUSHROOM_CONTEST, CACTUS_CONTEST, NETHER_WART_CONTEST
    }
    
    public enum ContestCategory {
        FARMING("§aFarming", "§7Farming contests"),
        MINING("§7Mining", "§7Mining contests"),
        FORAGING("§2Foraging", "§7Foraging contests"),
        FISHING("§bFishing", "§7Fishing contests"),
        COMBAT("§cCombat", "§7Combat contests");
        
        private final String displayName;
        private final String description;
        
        ContestCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum ContestRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0);
        
        private final String displayName;
        private final double multiplier;
        
        ContestRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class ContestConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final ContestCategory category;
        private final ContestRarity rarity;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public ContestConfig(String name, String displayName, Material icon, String description,
                           ContestCategory category, ContestRarity rarity, int maxLevel,
                           List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.maxLevel = maxLevel;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public ContestCategory getCategory() { return category; }
        public ContestRarity getRarity() { return rarity; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerContestData {
        private final UUID playerId;
        private final Map<ContestType, Integer> contestProgress = new HashMap<>();
        private final Map<ContestType, Integer> contestWins = new HashMap<>();
        private long lastUpdate;
        
        public PlayerContestData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addContestProgress(ContestType type, int amount) {
            contestProgress.put(type, contestProgress.getOrDefault(type, 0) + amount);
        }
        
        public void addContestWin(ContestType type) {
            contestWins.put(type, contestWins.getOrDefault(type, 0) + 1);
        }
        
        public int getContestProgress(ContestType type) {
            return contestProgress.getOrDefault(type, 0);
        }
        
        public int getContestWins(ContestType type) {
            return contestWins.getOrDefault(type, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
