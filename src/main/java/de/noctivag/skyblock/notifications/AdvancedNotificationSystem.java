package de.noctivag.skyblock.notifications;
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
 * Advanced Notification System - Hypixel Skyblock Style
 * 
 * Features:
 * - Dynamic Notifications
 * - Notification Categories
 * - Notification Timers
 * - Notification Rewards
 * - Notification Participation
 * - Notification Leaderboards
 * - Notification Notifications
 * - Notification Scheduling
 * - Notification Analytics
 * - Notification Customization
 */
public class AdvancedNotificationSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerNotifications> playerNotifications = new ConcurrentHashMap<>();
    private final Map<NotificationType, NotificationConfig> notificationConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> notificationTasks = new ConcurrentHashMap<>();
    
    public AdvancedNotificationSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeNotificationConfigs();
        startNotificationUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeNotificationConfigs() {
        notificationConfigs.put(NotificationType.SKILL_LEVEL_UP, new NotificationConfig(
            "Skill Level Up", "§aSkill Level Up", Material.EXPERIENCE_BOTTLE,
            "§7You leveled up a skill!",
            NotificationCategory.SKILLS, 1, Arrays.asList("§7- Skill level up", "§7- XP gained"),
            Arrays.asList("§7- 1x Experience Bottle", "§7- 1x Skill Book")
        ));
        
        notificationConfigs.put(NotificationType.COLLECTION_MILESTONE, new NotificationConfig(
            "Collection Milestone", "§bCollection Milestone", Material.BOOK,
            "§7You reached a collection milestone!",
            NotificationCategory.COLLECTIONS, 1, Arrays.asList("§7- Collection milestone", "§7- Rewards unlocked"),
            Arrays.asList("§7- 1x Book", "§7- 1x Collection Token")
        ));
        
        notificationConfigs.put(NotificationType.MINION_COLLECTION, new NotificationConfig(
            "Minion Collection", "§eMinion Collection", Material.IRON_GOLEM_SPAWN_EGG,
            "§7Your minion collected resources!",
            NotificationCategory.MINIONS, 1, Arrays.asList("§7- Minion collection", "§7- Resources gained"),
            Arrays.asList("§7- 1x Iron Golem Spawn Egg", "§7- 1x Minion Token")
        ));
        
        notificationConfigs.put(NotificationType.PET_LEVEL_UP, new NotificationConfig(
            "Pet Level Up", "§dPet Level Up", Material.WOLF_SPAWN_EGG,
            "§7Your pet leveled up!",
            NotificationCategory.PETS, 1, Arrays.asList("§7- Pet level up", "§7- Abilities improved"),
            Arrays.asList("§7- 1x Wolf Spawn Egg", "§7- 1x Pet Token")
        ));
        
        notificationConfigs.put(NotificationType.DUNGEON_COMPLETION, new NotificationConfig(
            "Dungeon Completion", "§cDungeon Completion", Material.NETHER_STAR,
            "§7You completed a dungeon!",
            NotificationCategory.DUNGEONS, 1, Arrays.asList("§7- Dungeon completion", "§7- Rewards gained"),
            Arrays.asList("§7- 1x Nether Star", "§7- 1x Dungeon Token")
        ));
        
        notificationConfigs.put(NotificationType.SLAYER_BOSS_DEFEAT, new NotificationConfig(
            "Slayer Boss Defeat", "§4Slayer Boss Defeat", Material.DIAMOND_SWORD,
            "§7You defeated a slayer boss!",
            NotificationCategory.SLAYERS, 1, Arrays.asList("§7- Slayer boss defeat", "§7- Rewards gained"),
            Arrays.asList("§7- 1x Diamond Sword", "§7- 1x Slayer Token")
        ));
        
        notificationConfigs.put(NotificationType.GUILD_ACTIVITY, new NotificationConfig(
            "Guild Activity", "§6Guild Activity", Material.WHITE_BANNER,
            "§7Your guild has new activity!",
            NotificationCategory.GUILDS, 1, Arrays.asList("§7- Guild activity", "§7- Member updates"),
            Arrays.asList("§7- 1x Banner", "§7- 1x Guild Token")
        ));
        
        notificationConfigs.put(NotificationType.AUCTION_SOLD, new NotificationConfig(
            "Auction Sold", "§eAuction Sold", Material.GOLD_INGOT,
            "§7Your auction was sold!",
            NotificationCategory.AUCTION, 1, Arrays.asList("§7- Auction sold", "§7- Coins received"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Auction Token")
        ));
        
        notificationConfigs.put(NotificationType.BAZAAR_ORDER_FILLED, new NotificationConfig(
            "Bazaar Order Filled", "§aBazaar Order Filled", Material.EMERALD,
            "§7Your bazaar order was filled!",
            NotificationCategory.BAZAAR, 1, Arrays.asList("§7- Bazaar order filled", "§7- Items received"),
            Arrays.asList("§7- 1x Emerald", "§7- 1x Bazaar Token")
        ));
        
        notificationConfigs.put(NotificationType.ISLAND_UPGRADE, new NotificationConfig(
            "Island Upgrade", "§bIsland Upgrade", Material.GRASS_BLOCK,
            "§7Your island was upgraded!",
            NotificationCategory.ISLANDS, 1, Arrays.asList("§7- Island upgrade", "§7- Features unlocked"),
            Arrays.asList("§7- 1x Grass Block", "§7- 1x Island Token")
        ));
        
        notificationConfigs.put(NotificationType.ENCHANTING_SUCCESS, new NotificationConfig(
            "Enchanting Success", "§dEnchanting Success", Material.ENCHANTING_TABLE,
            "§7Your enchanting was successful!",
            NotificationCategory.ENCHANTING, 1, Arrays.asList("§7- Enchanting success", "§7- Enchantment applied"),
            Arrays.asList("§7- 1x Enchanting Table", "§7- 1x Enchanting Token")
        ));
        
        notificationConfigs.put(NotificationType.ALCHEMY_SUCCESS, new NotificationConfig(
            "Alchemy Success", "§eAlchemy Success", Material.BREWING_STAND,
            "§7Your alchemy was successful!",
            NotificationCategory.ALCHEMY, 1, Arrays.asList("§7- Alchemy success", "§7- Potion created"),
            Arrays.asList("§7- 1x Brewing Stand", "§7- 1x Alchemy Token")
        ));
        
        notificationConfigs.put(NotificationType.CARPENTRY_SUCCESS, new NotificationConfig(
            "Carpentry Success", "§6Carpentry Success", Material.CRAFTING_TABLE,
            "§7Your carpentry was successful!",
            NotificationCategory.CARPENTRY, 1, Arrays.asList("§7- Carpentry success", "§7- Item crafted"),
            Arrays.asList("§7- 1x Crafting Table", "§7- 1x Carpentry Token")
        ));
        
        notificationConfigs.put(NotificationType.RUNECRAFTING_SUCCESS, new NotificationConfig(
            "Runecrafting Success", "§5Runecrafting Success", Material.END_CRYSTAL,
            "§7Your runecrafting was successful!",
            NotificationCategory.RUNECRAFTING, 1, Arrays.asList("§7- Runecrafting success", "§7- Rune created"),
            Arrays.asList("§7- 1x End Crystal", "§7- 1x Runecrafting Token")
        ));
        
        notificationConfigs.put(NotificationType.BANKING_INTEREST, new NotificationConfig(
            "Banking Interest", "§bBanking Interest", Material.CHEST,
            "§7You received banking interest!",
            NotificationCategory.BANKING, 1, Arrays.asList("§7- Banking interest", "§7- Coins received"),
            Arrays.asList("§7- 1x Chest", "§7- 1x Banking Token")
        ));
        
        notificationConfigs.put(NotificationType.QUEST_COMPLETION, new NotificationConfig(
            "Quest Completion", "§aQuest Completion", Material.BOOKSHELF,
            "§7You completed a quest!",
            NotificationCategory.QUESTS, 1, Arrays.asList("§7- Quest completion", "§7- Rewards received"),
            Arrays.asList("§7- 1x Bookshelf", "§7- 1x Quest Token")
        ));
        
        notificationConfigs.put(NotificationType.EVENT_START, new NotificationConfig(
            "Event Start", "§eEvent Start", Material.BEACON,
            "§7A new event has started!",
            NotificationCategory.EVENTS, 1, Arrays.asList("§7- Event start", "§7- Bonuses active"),
            Arrays.asList("§7- 1x Beacon", "§7- 1x Event Token")
        ));
        
        notificationConfigs.put(NotificationType.COSMETIC_UNLOCK, new NotificationConfig(
            "Cosmetic Unlock", "§dCosmetic Unlock", Material.ANVIL,
            "§7You unlocked a new cosmetic!",
            NotificationCategory.COSMETICS, 1, Arrays.asList("§7- Cosmetic unlock", "§7- New appearance"),
            Arrays.asList("§7- 1x Anvil", "§7- 1x Cosmetic Token")
        ));
        
        notificationConfigs.put(NotificationType.ACHIEVEMENT_UNLOCK, new NotificationConfig(
            "Achievement Unlock", "§6Achievement Unlock", Material.GOLD_INGOT,
            "§7You unlocked a new achievement!",
            NotificationCategory.ACHIEVEMENTS, 1, Arrays.asList("§7- Achievement unlock", "§7- Rewards received"),
            Arrays.asList("§7- 1x Trophy", "§7- 1x Achievement Token")
        ));
        
        notificationConfigs.put(NotificationType.LEADERBOARD_UPDATE, new NotificationConfig(
            "Leaderboard Update", "§cLeaderboard Update", Material.ITEM_FRAME,
            "§7Your leaderboard position was updated!",
            NotificationCategory.LEADERBOARDS, 1, Arrays.asList("§7- Leaderboard update", "§7- Position changed"),
            Arrays.asList("§7- 1x Item Frame", "§7- 1x Leaderboard Token")
        ));
        
        notificationConfigs.put(NotificationType.API_ACCESS, new NotificationConfig(
            "API Access", "§5API Access", Material.COMMAND_BLOCK,
            "§7You accessed the API!",
            NotificationCategory.API, 1, Arrays.asList("§7- API access", "§7- Data retrieved"),
            Arrays.asList("§7- 1x Command Block", "§7- 1x API Token")
        ));
        
        notificationConfigs.put(NotificationType.WEB_ACCESS, new NotificationConfig(
            "Web Access", "§bWeb Access", Material.REDSTONE_LAMP,
            "§7You accessed the web interface!",
            NotificationCategory.WEB, 1, Arrays.asList("§7- Web access", "§7- Interface opened"),
            Arrays.asList("§7- 1x Redstone Lamp", "§7- 1x Web Token")
        ));
        
        notificationConfigs.put(NotificationType.SOCIAL_INTERACTION, new NotificationConfig(
            "Social Interaction", "§aSocial Interaction", Material.PLAYER_HEAD,
            "§7You had a social interaction!",
            NotificationCategory.SOCIAL, 1, Arrays.asList("§7- Social interaction", "§7- Relationship improved"),
            Arrays.asList("§7- 1x Player Head", "§7- 1x Social Token")
        ));
    }
    
    private void startNotificationUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerNotifications> entry : playerNotifications.entrySet()) {
                    PlayerNotifications notifications = entry.getValue();
                    notifications.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BELL) {
            openNotificationGUI(player);
        }
    }
    
    private void openNotificationGUI(Player player) {
        player.sendMessage("§aNotification GUI geöffnet!");
    }
    
    public PlayerNotifications getPlayerNotifications(UUID playerId) {
        return playerNotifications.computeIfAbsent(playerId, k -> new PlayerNotifications(playerId));
    }
    
    public NotificationConfig getNotificationConfig(NotificationType type) {
        return notificationConfigs.get(type);
    }
    
    public List<NotificationType> getAllNotificationTypes() {
        return new ArrayList<>(notificationConfigs.keySet());
    }
    
    public enum NotificationType {
        SKILL_LEVEL_UP, COLLECTION_MILESTONE, MINION_COLLECTION, PET_LEVEL_UP, DUNGEON_COMPLETION,
        SLAYER_BOSS_DEFEAT, GUILD_ACTIVITY, AUCTION_SOLD, BAZAAR_ORDER_FILLED, ISLAND_UPGRADE,
        ENCHANTING_SUCCESS, ALCHEMY_SUCCESS, CARPENTRY_SUCCESS, RUNECRAFTING_SUCCESS, BANKING_INTEREST,
        QUEST_COMPLETION, EVENT_START, COSMETIC_UNLOCK, ACHIEVEMENT_UNLOCK, LEADERBOARD_UPDATE,
        API_ACCESS, WEB_ACCESS, SOCIAL_INTERACTION
    }
    
    public enum NotificationCategory {
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
        
        NotificationCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class NotificationConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final NotificationCategory category;
        private final int priority;
        private final List<String> features;
        private final List<String> requirements;
        
        public NotificationConfig(String name, String displayName, Material icon, String description,
                                NotificationCategory category, int priority, List<String> features, List<String> requirements) {
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
        public NotificationCategory getCategory() { return category; }
        public int getPriority() { return priority; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerNotifications {
        private final UUID playerId;
        private final Map<NotificationType, Integer> notificationLevels = new ConcurrentHashMap<>();
        private int totalNotifications = 0;
        private long totalNotificationTime = 0;
        private long lastUpdate;
        
        public PlayerNotifications(UUID playerId) {
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
            // Save notification data to database
        }
        
        public void addNotification(NotificationType type, int level) {
            notificationLevels.put(type, level);
            totalNotifications++;
        }
        
        public int getNotificationLevel(NotificationType type) {
            return notificationLevels.getOrDefault(type, 0);
        }
        
        public int getTotalNotifications() { return totalNotifications; }
        public long getTotalNotificationTime() { return totalNotificationTime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<NotificationType, Integer> getNotificationLevels() { return notificationLevels; }
    }
}
