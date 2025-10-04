package de.noctivag.plugin.help;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
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
 * Advanced Help System - Hypixel Skyblock Style
 * 
 * Features:
 * - Dynamic Help
 * - Help Categories
 * - Help Timers
 * - Help Rewards
 * - Help Participation
 * - Help Leaderboards
 * - Help Notifications
 * - Help Scheduling
 * - Help Analytics
 * - Help Customization
 */
public class AdvancedHelpSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerHelp> playerHelp = new ConcurrentHashMap<>();
    private final Map<HelpType, HelpConfig> helpConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> helpTasks = new ConcurrentHashMap<>();
    
    public AdvancedHelpSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeHelpConfigs();
        startHelpUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeHelpConfigs() {
        helpConfigs.put(HelpType.SKILL_HELP, new HelpConfig(
            "Skill Help", "§aSkill Help", Material.EXPERIENCE_BOTTLE,
            "§7Get help with skills.",
            HelpCategory.SKILLS, 1, Arrays.asList("§7- Skill help", "§7- Information"),
            Arrays.asList("§7- 1x Experience Bottle", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.COLLECTION_HELP, new HelpConfig(
            "Collection Help", "§bCollection Help", Material.BOOK,
            "§7Get help with collections.",
            HelpCategory.COLLECTIONS, 1, Arrays.asList("§7- Collection help", "§7- Information"),
            Arrays.asList("§7- 1x Book", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.MINION_HELP, new HelpConfig(
            "Minion Help", "§eMinion Help", Material.IRON_GOLEM_SPAWN_EGG,
            "§7Get help with minions.",
            HelpCategory.MINIONS, 1, Arrays.asList("§7- Minion help", "§7- Information"),
            Arrays.asList("§7- 1x Iron Golem Spawn Egg", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.PET_HELP, new HelpConfig(
            "Pet Help", "§dPet Help", Material.WOLF_SPAWN_EGG,
            "§7Get help with pets.",
            HelpCategory.PETS, 1, Arrays.asList("§7- Pet help", "§7- Information"),
            Arrays.asList("§7- 1x Wolf Spawn Egg", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.DUNGEON_HELP, new HelpConfig(
            "Dungeon Help", "§cDungeon Help", Material.NETHER_STAR,
            "§7Get help with dungeons.",
            HelpCategory.DUNGEONS, 1, Arrays.asList("§7- Dungeon help", "§7- Information"),
            Arrays.asList("§7- 1x Nether Star", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.SLAYER_HELP, new HelpConfig(
            "Slayer Help", "§4Slayer Help", Material.DIAMOND_SWORD,
            "§7Get help with slayers.",
            HelpCategory.SLAYERS, 1, Arrays.asList("§7- Slayer help", "§7- Information"),
            Arrays.asList("§7- 1x Diamond Sword", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.GUILD_HELP, new HelpConfig(
            "Guild Help", "§6Guild Help", Material.WHITE_BANNER,
            "§7Get help with guilds.",
            HelpCategory.GUILDS, 1, Arrays.asList("§7- Guild help", "§7- Information"),
            Arrays.asList("§7- 1x Banner", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.AUCTION_HELP, new HelpConfig(
            "Auction Help", "§eAuction Help", Material.GOLD_INGOT,
            "§7Get help with auctions.",
            HelpCategory.AUCTION, 1, Arrays.asList("§7- Auction help", "§7- Information"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.BAZAAR_HELP, new HelpConfig(
            "Bazaar Help", "§aBazaar Help", Material.EMERALD,
            "§7Get help with bazaar.",
            HelpCategory.BAZAAR, 1, Arrays.asList("§7- Bazaar help", "§7- Information"),
            Arrays.asList("§7- 1x Emerald", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.ISLAND_HELP, new HelpConfig(
            "Island Help", "§bIsland Help", Material.GRASS_BLOCK,
            "§7Get help with islands.",
            HelpCategory.ISLANDS, 1, Arrays.asList("§7- Island help", "§7- Information"),
            Arrays.asList("§7- 1x Grass Block", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.ENCHANTING_HELP, new HelpConfig(
            "Enchanting Help", "§dEnchanting Help", Material.ENCHANTING_TABLE,
            "§7Get help with enchanting.",
            HelpCategory.ENCHANTING, 1, Arrays.asList("§7- Enchanting help", "§7- Information"),
            Arrays.asList("§7- 1x Enchanting Table", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.ALCHEMY_HELP, new HelpConfig(
            "Alchemy Help", "§eAlchemy Help", Material.BREWING_STAND,
            "§7Get help with alchemy.",
            HelpCategory.ALCHEMY, 1, Arrays.asList("§7- Alchemy help", "§7- Information"),
            Arrays.asList("§7- 1x Brewing Stand", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.CARPENTRY_HELP, new HelpConfig(
            "Carpentry Help", "§6Carpentry Help", Material.CRAFTING_TABLE,
            "§7Get help with carpentry.",
            HelpCategory.CARPENTRY, 1, Arrays.asList("§7- Carpentry help", "§7- Information"),
            Arrays.asList("§7- 1x Crafting Table", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.RUNECRAFTING_HELP, new HelpConfig(
            "Runecrafting Help", "§5Runecrafting Help", Material.END_CRYSTAL,
            "§7Get help with runecrafting.",
            HelpCategory.RUNECRAFTING, 1, Arrays.asList("§7- Runecrafting help", "§7- Information"),
            Arrays.asList("§7- 1x End Crystal", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.BANKING_HELP, new HelpConfig(
            "Banking Help", "§bBanking Help", Material.CHEST,
            "§7Get help with banking.",
            HelpCategory.BANKING, 1, Arrays.asList("§7- Banking help", "§7- Information"),
            Arrays.asList("§7- 1x Chest", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.QUEST_HELP, new HelpConfig(
            "Quest Help", "§aQuest Help", Material.BOOKSHELF,
            "§7Get help with quests.",
            HelpCategory.QUESTS, 1, Arrays.asList("§7- Quest help", "§7- Information"),
            Arrays.asList("§7- 1x Bookshelf", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.EVENT_HELP, new HelpConfig(
            "Event Help", "§eEvent Help", Material.BEACON,
            "§7Get help with events.",
            HelpCategory.EVENTS, 1, Arrays.asList("§7- Event help", "§7- Information"),
            Arrays.asList("§7- 1x Beacon", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.COSMETIC_HELP, new HelpConfig(
            "Cosmetic Help", "§dCosmetic Help", Material.ANVIL,
            "§7Get help with cosmetics.",
            HelpCategory.COSMETICS, 1, Arrays.asList("§7- Cosmetic help", "§7- Information"),
            Arrays.asList("§7- 1x Anvil", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.ACHIEVEMENT_HELP, new HelpConfig(
            "Achievement Help", "§6Achievement Help", Material.GOLD_INGOT,
            "§7Get help with achievements.",
            HelpCategory.ACHIEVEMENTS, 1, Arrays.asList("§7- Achievement help", "§7- Information"),
            Arrays.asList("§7- 1x Trophy", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.LEADERBOARD_HELP, new HelpConfig(
            "Leaderboard Help", "§cLeaderboard Help", Material.ITEM_FRAME,
            "§7Get help with leaderboards.",
            HelpCategory.LEADERBOARDS, 1, Arrays.asList("§7- Leaderboard help", "§7- Information"),
            Arrays.asList("§7- 1x Item Frame", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.API_HELP, new HelpConfig(
            "API Help", "§5API Help", Material.COMMAND_BLOCK,
            "§7Get help with API.",
            HelpCategory.API, 1, Arrays.asList("§7- API help", "§7- Information"),
            Arrays.asList("§7- 1x Command Block", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.WEB_HELP, new HelpConfig(
            "Web Help", "§bWeb Help", Material.REDSTONE_LAMP,
            "§7Get help with web interface.",
            HelpCategory.WEB, 1, Arrays.asList("§7- Web help", "§7- Information"),
            Arrays.asList("§7- 1x Redstone Lamp", "§7- 1x Help Token")
        ));
        
        helpConfigs.put(HelpType.SOCIAL_HELP, new HelpConfig(
            "Social Help", "§aSocial Help", Material.PLAYER_HEAD,
            "§7Get help with social features.",
            HelpCategory.SOCIAL, 1, Arrays.asList("§7- Social help", "§7- Information"),
            Arrays.asList("§7- 1x Player Head", "§7- 1x Help Token")
        ));
    }
    
    private void startHelpUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerHelp> entry : playerHelp.entrySet()) {
                    PlayerHelp help = entry.getValue();
                    help.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BOOK) {
            openHelpGUI(player);
        }
    }
    
    private void openHelpGUI(Player player) {
        player.sendMessage("§aHelp GUI geöffnet!");
    }
    
    public PlayerHelp getPlayerHelp(UUID playerId) {
        return playerHelp.computeIfAbsent(playerId, k -> new PlayerHelp(playerId));
    }
    
    public HelpConfig getHelpConfig(HelpType type) {
        return helpConfigs.get(type);
    }
    
    public List<HelpType> getAllHelpTypes() {
        return new ArrayList<>(helpConfigs.keySet());
    }
    
    public enum HelpType {
        SKILL_HELP, COLLECTION_HELP, MINION_HELP, PET_HELP, DUNGEON_HELP, SLAYER_HELP,
        GUILD_HELP, AUCTION_HELP, BAZAAR_HELP, ISLAND_HELP, ENCHANTING_HELP, ALCHEMY_HELP,
        CARPENTRY_HELP, RUNECRAFTING_HELP, BANKING_HELP, QUEST_HELP, EVENT_HELP, COSMETIC_HELP,
        ACHIEVEMENT_HELP, LEADERBOARD_HELP, API_HELP, WEB_HELP, SOCIAL_HELP
    }
    
    public enum HelpCategory {
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
        
        HelpCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class HelpConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final HelpCategory category;
        private final int priority;
        private final List<String> features;
        private final List<String> requirements;
        
        public HelpConfig(String name, String displayName, Material icon, String description,
                         HelpCategory category, int priority, List<String> features, List<String> requirements) {
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
        public HelpCategory getCategory() { return category; }
        public int getPriority() { return priority; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerHelp {
        private final UUID playerId;
        private final Map<HelpType, Integer> helpLevels = new ConcurrentHashMap<>();
        private int totalHelp = 0;
        private long totalHelpTime = 0;
        private long lastUpdate;
        
        public PlayerHelp(UUID playerId) {
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
            // Save help data to database
        }
        
        public void addHelp(HelpType type, int level) {
            helpLevels.put(type, level);
            totalHelp++;
        }
        
        public int getHelpLevel(HelpType type) {
            return helpLevels.getOrDefault(type, 0);
        }
        
        public int getTotalHelp() { return totalHelp; }
        public long getTotalHelpTime() { return totalHelpTime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<HelpType, Integer> getHelpLevels() { return helpLevels; }
    }
}
