package de.noctivag.skyblock.elections;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * Mayor Elections System - Hypixel Skyblock Style
 */
public class MayorElectionsSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerElectionData> playerElectionData = new ConcurrentHashMap<>();
    private final Map<MayorType, MayorConfig> mayorConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> electionTasks = new ConcurrentHashMap<>();
    
    public MayorElectionsSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeMayorConfigs();
        startElectionUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeMayorConfigs() {
        mayorConfigs.put(MayorType.DIAZ, new MayorConfig(
            "Diaz", "§6Diaz", Material.GOLD_INGOT,
            "§7The economic mayor focused on money and trading.",
            MayorCategory.ECONOMY, MayorRarity.COMMON, 1,
            Arrays.asList("§7- +20% Coin gain", "§7- -10% Shop prices", "§7- +50% Auction tax"),
            Arrays.asList("§7- Economic focus", "§7- Trading benefits")
        ));
        
        mayorConfigs.put(MayorType.FINN, new MayorConfig(
            "Finn", "§bFinn", Material.FISHING_ROD,
            "§7The fishing mayor focused on sea activities.",
            MayorCategory.FISHING, MayorRarity.COMMON, 1,
            Arrays.asList("§7- +25% Fishing speed", "§7- +30% Sea creature chance", "§7- +15% Fishing XP"),
            Arrays.asList("§7- Fishing focus", "§7- Sea creature benefits")
        ));
        
        mayorConfigs.put(MayorType.MARINA, new MayorConfig(
            "Marina", "§9Marina", Material.PRISMARINE_SHARD,
            "§7The ocean mayor focused on water activities.",
            MayorCategory.FISHING, MayorRarity.UNCOMMON, 1,
            Arrays.asList("§7- +40% Fishing speed", "§7- +50% Sea creature chance", "§7- +25% Fishing XP"),
            Arrays.asList("§7- Ocean focus", "§7- Advanced fishing benefits")
        ));
        
        mayorConfigs.put(MayorType.PAUL, new MayorConfig(
            "Paul", "§ePaul", Material.DIAMOND_SWORD,
            "§7The dungeon mayor focused on dungeon activities.",
            MayorCategory.DUNGEONS, MayorRarity.RARE, 1,
            Arrays.asList("§7- +25% Dungeon XP", "§7- +20% Dungeon loot", "§7- +15% Dungeon speed"),
            Arrays.asList("§7- Dungeon focus", "§7- Advanced dungeon benefits")
        ));
        
        mayorConfigs.put(MayorType.SCORPIOUS, new MayorConfig(
            "Scorpious", "§5Scorpious", Material.NETHER_STAR,
            "§7The dark auction mayor focused on rare items.",
            MayorCategory.AUCTION, MayorRarity.EPIC, 1,
            Arrays.asList("§7- +30% Dark auction items", "§7- -20% Dark auction prices", "§7- +25% Rare item chance"),
            Arrays.asList("§7- Dark auction focus", "§7- Rare item benefits")
        ));
        
        mayorConfigs.put(MayorType.DERRY, new MayorConfig(
            "Derry", "§aDerry", Material.WHEAT,
            "§7The farming mayor focused on agricultural activities.",
            MayorCategory.FARMING, MayorRarity.COMMON, 1,
            Arrays.asList("§7- +30% Farming XP", "§7- +20% Crop growth", "§7- +25% Farming fortune"),
            Arrays.asList("§7- Farming focus", "§7- Agricultural benefits")
        ));
    }
    
    private void startElectionUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerElectionData> entry : playerElectionData.entrySet()) {
                    PlayerElectionData electionData = entry.getValue();
                    electionData.update();
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
        
        if (displayName.contains("Mayor") || displayName.contains("Election")) {
            openElectionGUI(player);
        }
    }
    
    public void openElectionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lMayor Elections"));
        
        addGUIItem(gui, 10, Material.GOLD_INGOT, "§6§lDiaz", "§7The economic mayor.");
        addGUIItem(gui, 11, Material.FISHING_ROD, "§b§lFinn", "§7The fishing mayor.");
        addGUIItem(gui, 12, Material.PRISMARINE_SHARD, "§9§lMarina", "§7The ocean mayor.");
        addGUIItem(gui, 13, Material.DIAMOND_SWORD, "§e§lPaul", "§7The dungeon mayor.");
        addGUIItem(gui, 14, Material.NETHER_STAR, "§5§lScorpious", "§7The dark auction mayor.");
        addGUIItem(gui, 15, Material.WHEAT, "§a§lDerry", "§7The farming mayor.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aMayor Elections GUI geöffnet!"));
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
    
    public void voteForMayor(Player player, MayorType mayorType) {
        PlayerElectionData electionData = getPlayerElectionData(player.getUniqueId());
        
        if (electionData.hasVoted()) {
            player.sendMessage(Component.text("§cYou have already voted in this election!"));
            return;
        }
        
        electionData.setVotedMayor(mayorType);
        electionData.setHasVoted(true);
        
        player.sendMessage("§aYou voted for " + mayorType.name() + "!");
    }
    
    public PlayerElectionData getPlayerElectionData(UUID playerId) {
        return playerElectionData.computeIfAbsent(playerId, k -> new PlayerElectionData(playerId));
    }
    
    public enum MayorType {
        DIAZ, FINN, MARINA, PAUL, SCORPIOUS, DERRY, AATROX, COLE, MINIKLOON, 
        JERRY, TECHNOBLADE, DANTE, SIRIUS, MAXOR, STORM, GOLDOR, NECRON
    }
    
    public enum MayorCategory {
        ECONOMY("§6Economy", "§7Economic benefits"),
        FISHING("§bFishing", "§7Fishing benefits"),
        DUNGEONS("§eDungeons", "§7Dungeon benefits"),
        AUCTION("§5Auction", "§7Auction benefits"),
        FARMING("§aFarming", "§7Farming benefits"),
        MINING("§7Mining", "§7Mining benefits"),
        COMBAT("§cCombat", "§7Combat benefits"),
        FORAGING("§2Foraging", "§7Foraging benefits");
        
        private final String displayName;
        private final String description;
        
        MayorCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum MayorRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0);
        
        private final String displayName;
        private final double multiplier;
        
        MayorRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class MayorConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final MayorCategory category;
        private final MayorRarity rarity;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public MayorConfig(String name, String displayName, Material icon, String description,
                          MayorCategory category, MayorRarity rarity, int maxLevel,
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
        public MayorCategory getCategory() { return category; }
        public MayorRarity getRarity() { return rarity; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerElectionData {
        private final UUID playerId;
        private MayorType votedMayor;
        private boolean hasVoted;
        private long lastUpdate;
        
        public PlayerElectionData(UUID playerId) {
            this.playerId = playerId;
            this.hasVoted = false;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void setVotedMayor(MayorType mayorType) {
            this.votedMayor = mayorType;
        }
        
        public void setHasVoted(boolean hasVoted) {
            this.hasVoted = hasVoted;
        }
        
        public MayorType getVotedMayor() {
            return votedMayor;
        }
        
        public boolean hasVoted() {
            return hasVoted;
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
