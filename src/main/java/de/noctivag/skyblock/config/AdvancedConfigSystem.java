package de.noctivag.skyblock.config;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Configuration System - Hypixel Skyblock Style
 * 
 * Features:
 * - Dynamic Configuration
 * - Configuration Categories
 * - Configuration Timers
 * - Configuration Rewards
 * - Configuration Participation
 * - Configuration Leaderboards
 * - Configuration Notifications
 * - Configuration Scheduling
 * - Configuration Analytics
 * - Configuration Customization
 */
public class AdvancedConfigSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerConfig> playerConfigs = new ConcurrentHashMap<>();
    private final Map<ConfigType, ConfigConfig> configConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> configTasks = new ConcurrentHashMap<>();
    
    public AdvancedConfigSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeConfigConfigs();
        startConfigUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeConfigConfigs() {
        configConfigs.put(ConfigType.SKILL_CONFIG, new ConfigConfig(
            "Skill Configuration", "§aSkill Configuration", Material.EXPERIENCE_BOTTLE,
            "§7Configure your skill settings.",
            ConfigCategory.SKILLS, 1, Arrays.asList("§7- Skill configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Experience Bottle", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.COLLECTION_CONFIG, new ConfigConfig(
            "Collection Configuration", "§bCollection Configuration", Material.BOOK,
            "§7Configure your collection settings.",
            ConfigCategory.COLLECTIONS, 1, Arrays.asList("§7- Collection configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Book", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.MINION_CONFIG, new ConfigConfig(
            "Minion Configuration", "§eMinion Configuration", Material.IRON_GOLEM_SPAWN_EGG,
            "§7Configure your minion settings.",
            ConfigCategory.MINIONS, 1, Arrays.asList("§7- Minion configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Iron Golem Spawn Egg", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.PET_CONFIG, new ConfigConfig(
            "Pet Configuration", "§dPet Configuration", Material.WOLF_SPAWN_EGG,
            "§7Configure your pet settings.",
            ConfigCategory.PETS, 1, Arrays.asList("§7- Pet configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Wolf Spawn Egg", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.DUNGEON_CONFIG, new ConfigConfig(
            "Dungeon Configuration", "§cDungeon Configuration", Material.NETHER_STAR,
            "§7Configure your dungeon settings.",
            ConfigCategory.DUNGEONS, 1, Arrays.asList("§7- Dungeon configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Nether Star", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.SLAYER_CONFIG, new ConfigConfig(
            "Slayer Configuration", "§4Slayer Configuration", Material.DIAMOND_SWORD,
            "§7Configure your slayer settings.",
            ConfigCategory.SLAYERS, 1, Arrays.asList("§7- Slayer configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Diamond Sword", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.GUILD_CONFIG, new ConfigConfig(
            "Guild Configuration", "§6Guild Configuration", Material.WHITE_BANNER,
            "§7Configure your guild settings.",
            ConfigCategory.GUILDS, 1, Arrays.asList("§7- Guild configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Banner", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.AUCTION_CONFIG, new ConfigConfig(
            "Auction Configuration", "§eAuction Configuration", Material.GOLD_INGOT,
            "§7Configure your auction settings.",
            ConfigCategory.AUCTION, 1, Arrays.asList("§7- Auction configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.BAZAAR_CONFIG, new ConfigConfig(
            "Bazaar Configuration", "§aBazaar Configuration", Material.EMERALD,
            "§7Configure your bazaar settings.",
            ConfigCategory.BAZAAR, 1, Arrays.asList("§7- Bazaar configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Emerald", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.ISLAND_CONFIG, new ConfigConfig(
            "Island Configuration", "§bIsland Configuration", Material.GRASS_BLOCK,
            "§7Configure your island settings.",
            ConfigCategory.ISLANDS, 1, Arrays.asList("§7- Island configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Grass Block", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.ENCHANTING_CONFIG, new ConfigConfig(
            "Enchanting Configuration", "§dEnchanting Configuration", Material.ENCHANTING_TABLE,
            "§7Configure your enchanting settings.",
            ConfigCategory.ENCHANTING, 1, Arrays.asList("§7- Enchanting configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Enchanting Table", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.ALCHEMY_CONFIG, new ConfigConfig(
            "Alchemy Configuration", "§eAlchemy Configuration", Material.BREWING_STAND,
            "§7Configure your alchemy settings.",
            ConfigCategory.ALCHEMY, 1, Arrays.asList("§7- Alchemy configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Brewing Stand", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.CARPENTRY_CONFIG, new ConfigConfig(
            "Carpentry Configuration", "§6Carpentry Configuration", Material.CRAFTING_TABLE,
            "§7Configure your carpentry settings.",
            ConfigCategory.CARPENTRY, 1, Arrays.asList("§7- Carpentry configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Crafting Table", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.RUNECRAFTING_CONFIG, new ConfigConfig(
            "Runecrafting Configuration", "§5Runecrafting Configuration", Material.END_CRYSTAL,
            "§7Configure your runecrafting settings.",
            ConfigCategory.RUNECRAFTING, 1, Arrays.asList("§7- Runecrafting configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x End Crystal", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.BANKING_CONFIG, new ConfigConfig(
            "Banking Configuration", "§bBanking Configuration", Material.CHEST,
            "§7Configure your banking settings.",
            ConfigCategory.BANKING, 1, Arrays.asList("§7- Banking configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Chest", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.QUEST_CONFIG, new ConfigConfig(
            "Quest Configuration", "§aQuest Configuration", Material.BOOKSHELF,
            "§7Configure your quest settings.",
            ConfigCategory.QUESTS, 1, Arrays.asList("§7- Quest configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Bookshelf", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.EVENT_CONFIG, new ConfigConfig(
            "Event Configuration", "§eEvent Configuration", Material.BEACON,
            "§7Configure your event settings.",
            ConfigCategory.EVENTS, 1, Arrays.asList("§7- Event configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Beacon", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.COSMETIC_CONFIG, new ConfigConfig(
            "Cosmetic Configuration", "§dCosmetic Configuration", Material.ANVIL,
            "§7Configure your cosmetic settings.",
            ConfigCategory.COSMETICS, 1, Arrays.asList("§7- Cosmetic configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Anvil", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.ACHIEVEMENT_CONFIG, new ConfigConfig(
            "Achievement Configuration", "§6Achievement Configuration", Material.GOLD_INGOT,
            "§7Configure your achievement settings.",
            ConfigCategory.ACHIEVEMENTS, 1, Arrays.asList("§7- Achievement configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Trophy", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.LEADERBOARD_CONFIG, new ConfigConfig(
            "Leaderboard Configuration", "§cLeaderboard Configuration", Material.ITEM_FRAME,
            "§7Configure your leaderboard settings.",
            ConfigCategory.LEADERBOARDS, 1, Arrays.asList("§7- Leaderboard configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Item Frame", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.API_CONFIG, new ConfigConfig(
            "API Configuration", "§5API Configuration", Material.COMMAND_BLOCK,
            "§7Configure your API settings.",
            ConfigCategory.API, 1, Arrays.asList("§7- API configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Command Block", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.WEB_CONFIG, new ConfigConfig(
            "Web Configuration", "§bWeb Configuration", Material.REDSTONE_LAMP,
            "§7Configure your web settings.",
            ConfigCategory.WEB, 1, Arrays.asList("§7- Web configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Redstone Lamp", "§7- 1x Config Token")
        ));
        
        configConfigs.put(ConfigType.SOCIAL_CONFIG, new ConfigConfig(
            "Social Configuration", "§aSocial Configuration", Material.PLAYER_HEAD,
            "§7Configure your social settings.",
            ConfigCategory.SOCIAL, 1, Arrays.asList("§7- Social configuration", "§7- Settings management"),
            Arrays.asList("§7- 1x Player Head", "§7- 1x Config Token")
        ));
    }
    
    private void startConfigUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerConfig> entry : playerConfigs.entrySet()) {
                    PlayerConfig config = entry.getValue();
                    config.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.REDSTONE) {
            openConfigGUI(player);
        }
    }
    
    private void openConfigGUI(Player player) {
        player.sendMessage("§aConfiguration GUI geöffnet!");
    }
    
    public PlayerConfig getPlayerConfig(UUID playerId) {
        return playerConfigs.computeIfAbsent(playerId, k -> new PlayerConfig(playerId));
    }
    
    public ConfigConfig getConfigConfig(ConfigType type) {
        return configConfigs.get(type);
    }
    
    public List<ConfigType> getAllConfigTypes() {
        return new ArrayList<>(configConfigs.keySet());
    }
    
    public enum ConfigType {
        SKILL_CONFIG, COLLECTION_CONFIG, MINION_CONFIG, PET_CONFIG, DUNGEON_CONFIG, SLAYER_CONFIG,
        GUILD_CONFIG, AUCTION_CONFIG, BAZAAR_CONFIG, ISLAND_CONFIG, ENCHANTING_CONFIG, ALCHEMY_CONFIG,
        CARPENTRY_CONFIG, RUNECRAFTING_CONFIG, BANKING_CONFIG, QUEST_CONFIG, EVENT_CONFIG, COSMETIC_CONFIG,
        ACHIEVEMENT_CONFIG, LEADERBOARD_CONFIG, API_CONFIG, WEB_CONFIG, SOCIAL_CONFIG
    }
    
    public enum ConfigCategory {
        SKILLS("§aSkills", 1.0),
        COLLECTIONS("§bCollections", 1.2),
        MINIONS("§eMinions", 1.1),
        PETS("§dPets", 1.3),
        DUNGEONS("§cDungeons", 1.4),
        SLAYERS("§4Slayers", 1.5),
        GUILDS("§6Guilds", 1.6),
        AUCTION("§eAuction", 1.7),
        BAZAAR("§aBazaar", 1.8),
        ISLANDS("§bIslands", 1.9),
        ENCHANTING("§dEnchanting", 2.0),
        ALCHEMY("§eAlchemy", 2.1),
        CARPENTRY("§6Carpentry", 2.2),
        RUNECRAFTING("§5Runecrafting", 2.3),
        BANKING("§bBanking", 2.4),
        QUESTS("§aQuests", 2.5),
        EVENTS("§eEvents", 2.6),
        COSMETICS("§dCosmetics", 2.7),
        ACHIEVEMENTS("§6Achievements", 2.8),
        LEADERBOARDS("§cLeaderboards", 2.9),
        API("§5API", 3.0),
        WEB("§bWeb", 3.1),
        SOCIAL("§aSocial", 3.2);
        
        private final String displayName;
        private final double multiplier;
        
        ConfigCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class ConfigConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final ConfigCategory category;
        private final int priority;
        private final List<String> features;
        private final List<String> requirements;
        
        public ConfigConfig(String name, String displayName, Material icon, String description,
                           ConfigCategory category, int priority, List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.priority = priority;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public ConfigCategory getCategory() { return category; }
        public int getPriority() { return priority; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerConfig {
        private final UUID playerId;
        private final Map<ConfigType, Integer> configLevels = new ConcurrentHashMap<>();
        private int totalConfigs = 0;
        private long totalConfigTime = 0;
        private long lastUpdate;
        
        public PlayerConfig(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save config data to database
        }
        
        public void addConfig(ConfigType type, int level) {
            configLevels.put(type, level);
            totalConfigs++;
        }
        
        public int getConfigLevel(ConfigType type) {
            return configLevels.getOrDefault(type, 0);
        }
        
        public int getTotalConfigs() { return totalConfigs; }
        public long getTotalConfigTime() { return totalConfigTime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<ConfigType, Integer> getConfigLevels() { return configLevels; }
    }
}
