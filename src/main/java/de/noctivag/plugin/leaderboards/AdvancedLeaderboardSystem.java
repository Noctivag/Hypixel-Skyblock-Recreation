package de.noctivag.plugin.leaderboards;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedLeaderboardSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerLeaderboardData> playerLeaderboardData = new ConcurrentHashMap<>();
    private final Map<LeaderboardType, List<Leaderboard>> leaderboards = new HashMap<>();
    
    public AdvancedLeaderboardSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeLeaderboards();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeLeaderboards() {
        // Skill Leaderboards
        List<Leaderboard> skillLeaderboards = new ArrayList<>();
        skillLeaderboards.add(new Leaderboard(
            "Combat Leaderboard", "§cCombat Leaderboard", Material.DIAMOND_SWORD,
            "§7Leaderboard for combat skill levels.",
            LeaderboardType.SKILL, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Combat skill", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Skill levels"), "combat"
        ));
        skillLeaderboards.add(new Leaderboard(
            "Mining Leaderboard", "§7Mining Leaderboard", Material.DIAMOND_PICKAXE,
            "§7Leaderboard for mining skill levels.",
            LeaderboardType.SKILL, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Mining skill", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Skill levels"), "mining"
        ));
        skillLeaderboards.add(new Leaderboard(
            "Farming Leaderboard", "§eFarming Leaderboard", Material.WHEAT,
            "§7Leaderboard for farming skill levels.",
            LeaderboardType.SKILL, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Farming skill", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Skill levels"), "farming"
        ));
        skillLeaderboards.add(new Leaderboard(
            "Foraging Leaderboard", "§6Foraging Leaderboard", Material.OAK_LOG,
            "§7Leaderboard for foraging skill levels.",
            LeaderboardType.SKILL, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Foraging skill", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Skill levels"), "foraging"
        ));
        skillLeaderboards.add(new Leaderboard(
            "Fishing Leaderboard", "§bFishing Leaderboard", Material.FISHING_ROD,
            "§7Leaderboard for fishing skill levels.",
            LeaderboardType.SKILL, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Fishing skill", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Skill levels"), "fishing"
        ));
        skillLeaderboards.add(new Leaderboard(
            "Enchanting Leaderboard", "§dEnchanting Leaderboard", Material.ENCHANTED_BOOK,
            "§7Leaderboard for enchanting skill levels.",
            LeaderboardType.SKILL, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Enchanting skill", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Skill levels"), "enchanting"
        ));
        skillLeaderboards.add(new Leaderboard(
            "Alchemy Leaderboard", "§5Alchemy Leaderboard", Material.POTION,
            "§7Leaderboard for alchemy skill levels.",
            LeaderboardType.SKILL, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Alchemy skill", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Skill levels"), "alchemy"
        ));
        skillLeaderboards.add(new Leaderboard(
            "Taming Leaderboard", "§aTaming Leaderboard", Material.BONE,
            "§7Leaderboard for taming skill levels.",
            LeaderboardType.SKILL, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Taming skill", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Skill levels"), "taming"
        ));
        skillLeaderboards.add(new Leaderboard(
            "Carpentry Leaderboard", "§6Carpentry Leaderboard", Material.WOODEN_AXE,
            "§7Leaderboard for carpentry skill levels.",
            LeaderboardType.SKILL, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Carpentry skill", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Skill levels"), "carpentry"
        ));
        skillLeaderboards.add(new Leaderboard(
            "Runecrafting Leaderboard", "§5Runecrafting Leaderboard", Material.LAPIS_LAZULI,
            "§7Leaderboard for runecrafting skill levels.",
            LeaderboardType.SKILL, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Runecrafting skill", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Skill levels"), "runecrafting"
        ));
        leaderboards.put(LeaderboardType.SKILL, skillLeaderboards);
        
        // Collection Leaderboards
        List<Leaderboard> collectionLeaderboards = new ArrayList<>();
        collectionLeaderboards.add(new Leaderboard(
            "Wheat Collection Leaderboard", "§eWheat Collection Leaderboard", Material.WHEAT,
            "§7Leaderboard for wheat collection amounts.",
            LeaderboardType.COLLECTION, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Wheat collection", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Collection amounts"), "wheat"
        ));
        collectionLeaderboards.add(new Leaderboard(
            "Carrot Collection Leaderboard", "§6Carrot Collection Leaderboard", Material.CARROT,
            "§7Leaderboard for carrot collection amounts.",
            LeaderboardType.COLLECTION, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Carrot collection", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Collection amounts"), "carrot"
        ));
        collectionLeaderboards.add(new Leaderboard(
            "Potato Collection Leaderboard", "§ePotato Collection Leaderboard", Material.POTATO,
            "§7Leaderboard for potato collection amounts.",
            LeaderboardType.COLLECTION, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Potato collection", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Collection amounts"), "potato"
        ));
        collectionLeaderboards.add(new Leaderboard(
            "Pumpkin Collection Leaderboard", "§6Pumpkin Collection Leaderboard", Material.PUMPKIN,
            "§7Leaderboard for pumpkin collection amounts.",
            LeaderboardType.COLLECTION, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Pumpkin collection", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Collection amounts"), "pumpkin"
        ));
        collectionLeaderboards.add(new Leaderboard(
            "Melon Collection Leaderboard", "§aMelon Collection Leaderboard", Material.MELON,
            "§7Leaderboard for melon collection amounts.",
            LeaderboardType.COLLECTION, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Melon collection", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Collection amounts"), "melon"
        ));
        collectionLeaderboards.add(new Leaderboard(
            "Sugar Cane Collection Leaderboard", "§eSugar Cane Collection Leaderboard", Material.SUGAR_CANE,
            "§7Leaderboard for sugar cane collection amounts.",
            LeaderboardType.COLLECTION, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Sugar cane collection", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Collection amounts"), "sugar_cane"
        ));
        collectionLeaderboards.add(new Leaderboard(
            "Cactus Collection Leaderboard", "§aCactus Collection Leaderboard", Material.CACTUS,
            "§7Leaderboard for cactus collection amounts.",
            LeaderboardType.COLLECTION, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Cactus collection", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Collection amounts"), "cactus"
        ));
        collectionLeaderboards.add(new Leaderboard(
            "Cocoa Collection Leaderboard", "§6Cocoa Collection Leaderboard", Material.COCOA_BEANS,
            "§7Leaderboard for cocoa collection amounts.",
            LeaderboardType.COLLECTION, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Cocoa collection", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Collection amounts"), "cocoa"
        ));
        collectionLeaderboards.add(new Leaderboard(
            "Nether Wart Collection Leaderboard", "§cNether Wart Collection Leaderboard", Material.NETHER_WART,
            "§7Leaderboard for nether wart collection amounts.",
            LeaderboardType.COLLECTION, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Nether wart collection", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Collection amounts"), "nether_wart"
        ));
        collectionLeaderboards.add(new Leaderboard(
            "Mushroom Collection Leaderboard", "§dMushroom Collection Leaderboard", Material.RED_MUSHROOM,
            "§7Leaderboard for mushroom collection amounts.",
            LeaderboardType.COLLECTION, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Mushroom collection", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Collection amounts"), "mushroom"
        ));
        leaderboards.put(LeaderboardType.COLLECTION, collectionLeaderboards);
        
        // Economy Leaderboards
        List<Leaderboard> economyLeaderboards = new ArrayList<>();
        economyLeaderboards.add(new Leaderboard(
            "Coins Leaderboard", "§6Coins Leaderboard", Material.GOLD_INGOT,
            "§7Leaderboard for player coin amounts.",
            LeaderboardType.ECONOMY, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Coin amounts", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Coin amounts"), "coins"
        ));
        economyLeaderboards.add(new Leaderboard(
            "Bank Balance Leaderboard", "§eBank Balance Leaderboard", Material.ENDER_CHEST,
            "§7Leaderboard for player bank balances.",
            LeaderboardType.ECONOMY, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Bank balances", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Bank balances"), "bank_balance"
        ));
        economyLeaderboards.add(new Leaderboard(
            "Investment Leaderboard", "§bInvestment Leaderboard", Material.EMERALD_BLOCK,
            "§7Leaderboard for player investments.",
            LeaderboardType.ECONOMY, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Investments", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Investment amounts"), "investments"
        ));
        economyLeaderboards.add(new Leaderboard(
            "Loan Leaderboard", "§cLoan Leaderboard", Material.GOLD_INGOT,
            "§7Leaderboard for player loans.",
            LeaderboardType.ECONOMY, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Loans", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Loan amounts"), "loans"
        ));
        leaderboards.put(LeaderboardType.ECONOMY, economyLeaderboards);
        
        // Achievement Leaderboards
        List<Leaderboard> achievementLeaderboards = new ArrayList<>();
        achievementLeaderboards.add(new Leaderboard(
            "Achievement Leaderboard", "§aAchievement Leaderboard", Material.GOLD_INGOT,
            "§7Leaderboard for player achievements.",
            LeaderboardType.ACHIEVEMENT, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Achievements", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Achievement counts"), "achievements"
        ));
        achievementLeaderboards.add(new Leaderboard(
            "Quest Leaderboard", "§eQuest Leaderboard", Material.BOOK,
            "§7Leaderboard for player quest completions.",
            LeaderboardType.ACHIEVEMENT, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Quest completions", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Quest counts"), "quests"
        ));
        achievementLeaderboards.add(new Leaderboard(
            "Event Leaderboard", "§dEvent Leaderboard", Material.FIREWORK_ROCKET,
            "§7Leaderboard for player event participation.",
            LeaderboardType.ACHIEVEMENT, LeaderboardRarity.COMMON, 1, Arrays.asList("§7- Event participation", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Event counts"), "events"
        ));
        leaderboards.put(LeaderboardType.ACHIEVEMENT, achievementLeaderboards);
        
        // Special Leaderboards
        List<Leaderboard> specialLeaderboards = new ArrayList<>();
        specialLeaderboards.add(new Leaderboard(
            "Overall Leaderboard", "§6Overall Leaderboard", Material.NETHER_STAR,
            "§7Overall leaderboard combining all stats.",
            LeaderboardType.SPECIAL, LeaderboardRarity.RARE, 2, Arrays.asList("§7- Overall stats", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Overall scores"), "overall"
        ));
        specialLeaderboards.add(new Leaderboard(
            "Weekly Leaderboard", "§eWeekly Leaderboard", Material.CLOCK,
            "§7Weekly leaderboard for recent activity.",
            LeaderboardType.SPECIAL, LeaderboardRarity.UNCOMMON, 1, Arrays.asList("§7- Weekly activity", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Weekly scores"), "weekly"
        ));
        specialLeaderboards.add(new Leaderboard(
            "Monthly Leaderboard", "§bMonthly Leaderboard", Material.CLOCK,
            "§7Monthly leaderboard for monthly activity.",
            LeaderboardType.SPECIAL, LeaderboardRarity.UNCOMMON, 1, Arrays.asList("§7- Monthly activity", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- Monthly scores"), "monthly"
        ));
        specialLeaderboards.add(new Leaderboard(
            "All Time Leaderboard", "§cAll Time Leaderboard", Material.END_CRYSTAL,
            "§7All time leaderboard for total activity.",
            LeaderboardType.SPECIAL, LeaderboardRarity.EPIC, 3, Arrays.asList("§7- All time activity", "§7- Player rankings"),
            Arrays.asList("§7- Top 10 players", "§7- All time scores"), "all_time"
        ));
        leaderboards.put(LeaderboardType.SPECIAL, specialLeaderboards);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Leaderboard") || displayName.contains("Leaderboards")) {
            openLeaderboardGUI(player);
        }
    }
    
    public void openLeaderboardGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lLeaderboards");
        
        // Add leaderboard categories
        addGUIItem(gui, 10, Material.DIAMOND_SWORD, "§c§lSkill Leaderboards", "§7Leaderboards for skill levels.");
        addGUIItem(gui, 11, Material.WHEAT, "§e§lCollection Leaderboards", "§7Leaderboards for collection amounts.");
        addGUIItem(gui, 12, Material.GOLD_INGOT, "§6§lEconomy Leaderboards", "§7Leaderboards for economy stats.");
        addGUIItem(gui, 13, Material.GOLD_INGOT, "§a§lAchievement Leaderboards", "§7Leaderboards for achievements.");
        addGUIItem(gui, 14, Material.NETHER_STAR, "§d§lSpecial Leaderboards", "§7Special leaderboards.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the leaderboard menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aLeaderboard GUI geöffnet!");
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(description));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public void updateLeaderboard(LeaderboardType type, String category, UUID playerId, int value) {
        PlayerLeaderboardData data = getPlayerLeaderboardData(playerId);
        data.updateLeaderboard(type, category, value);
    }
    
    public List<LeaderboardEntry> getTopPlayers(LeaderboardType type, String category, int limit) {
        // This would typically query the database for top players
        // For now, return empty list
        return new ArrayList<>();
    }
    
    public PlayerLeaderboardData getPlayerLeaderboardData(UUID playerId) {
        return playerLeaderboardData.computeIfAbsent(playerId, k -> new PlayerLeaderboardData(playerId));
    }
    
    public List<Leaderboard> getLeaderboards(LeaderboardType category) {
        return leaderboards.getOrDefault(category, new ArrayList<>());
    }
    
    public enum LeaderboardType {
        SKILL("§cSkill", "§7Leaderboards for skill levels"),
        COLLECTION("§eCollection", "§7Leaderboards for collection amounts"),
        ECONOMY("§6Economy", "§7Leaderboards for economy stats"),
        ACHIEVEMENT("§aAchievement", "§7Leaderboards for achievements"),
        SPECIAL("§dSpecial", "§7Special leaderboards");
        
        private final String displayName;
        private final String description;
        
        LeaderboardType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum LeaderboardRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        LeaderboardRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class Leaderboard {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final LeaderboardType type;
        private final LeaderboardRarity rarity;
        private final int level;
        private final List<String> features;
        private final List<String> benefits;
        private final String category;
        
        public Leaderboard(String name, String displayName, Material material, String description,
                          LeaderboardType type, LeaderboardRarity rarity, int level, List<String> features,
                          List<String> benefits, String category) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.type = type;
            this.rarity = rarity;
            this.level = level;
            this.features = features;
            this.benefits = benefits;
            this.category = category;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public LeaderboardType getType() { return type; }
        public LeaderboardRarity getRarity() { return rarity; }
        public int getLevel() { return level; }
        public List<String> getFeatures() { return features; }
        public List<String> getBenefits() { return benefits; }
        public String getCategory() { return category; }
    }
    
    public static class LeaderboardEntry {
        private final UUID playerId;
        private final String playerName;
        private final int value;
        private final int rank;
        
        public LeaderboardEntry(UUID playerId, String playerName, int value, int rank) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.value = value;
            this.rank = rank;
        }
        
        public UUID getPlayerId() { return playerId; }
        public String getPlayerName() { return playerName; }
        public int getValue() { return value; }
        public int getRank() { return rank; }
    }
    
    public static class PlayerLeaderboardData {
        private final UUID playerId;
        private final Map<String, Integer> leaderboardValues = new HashMap<>();
        private long lastUpdate;
        
        public PlayerLeaderboardData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void updateLeaderboard(LeaderboardType type, String category, int value) {
            String key = type.name() + "_" + category;
            leaderboardValues.put(key, value);
        }
        
        public int getLeaderboardValue(LeaderboardType type, String category) {
            String key = type.name() + "_" + category;
            return leaderboardValues.getOrDefault(key, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
