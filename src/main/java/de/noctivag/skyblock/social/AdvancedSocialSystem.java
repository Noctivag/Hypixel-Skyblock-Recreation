package de.noctivag.skyblock.social;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Social System - Hypixel Skyblock Style
 * Implements Social Interactions, Social Collections, and Social Rewards
 */
public class AdvancedSocialSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerSocialData> playerSocialData = new ConcurrentHashMap<>();
    private final Map<SocialActivity, SocialConfig> socialConfigs = new HashMap<>();
    private final Map<SocialLocation, SocialLocationConfig> socialLocationConfigs = new HashMap<>();
    
    public AdvancedSocialSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        initializeSocialConfigs();
        initializeSocialLocationConfigs();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeSocialConfigs() {
        // Chat Activities
        socialConfigs.put(SocialActivity.CHAT, new SocialConfig(
            SocialActivity.CHAT,
            "§bChat",
            "§7Communicate with other players",
            Material.PAPER,
            SocialCategory.COMMUNICATION,
            1,
            Arrays.asList("Increases communication", "Increases social interaction", "Increases community building")
        ));
        
        socialConfigs.put(SocialActivity.PARTY, new SocialConfig(
            SocialActivity.PARTY,
            "§dParty",
            "§7Join parties with other players",
            Material.PLAYER_HEAD,
            SocialCategory.COMMUNICATION,
            2,
            Arrays.asList("Increases party interaction", "Increases teamwork", "Increases social bonding")
        ));
        
        socialConfigs.put(SocialActivity.GUILD, new SocialConfig(
            SocialActivity.GUILD,
            "§6Guild",
            "§7Join guilds with other players",
            Material.GOLD_BLOCK,
            SocialCategory.COMMUNICATION,
            3,
            Arrays.asList("Increases guild interaction", "Increases community building", "Increases social leadership")
        ));
        
        // Trading Activities
        socialConfigs.put(SocialActivity.TRADE, new SocialConfig(
            SocialActivity.TRADE,
            "§eTrade",
            "§7Trade items with other players",
            Material.EMERALD,
            SocialCategory.TRADING,
            1,
            Arrays.asList("Increases trading", "Increases economic interaction", "Increases social commerce")
        ));
        
        socialConfigs.put(SocialActivity.AUCTION, new SocialConfig(
            SocialActivity.AUCTION,
            "§6Auction",
            "§7Auction items to other players",
            Material.GOLD_INGOT,
            SocialCategory.TRADING,
            2,
            Arrays.asList("Increases auction participation", "Increases economic interaction", "Increases social commerce")
        ));
        
        socialConfigs.put(SocialActivity.BAZAAR, new SocialConfig(
            SocialActivity.BAZAAR,
            "§aBazaar",
            "§7Buy and sell items at the bazaar",
            Material.EMERALD_BLOCK,
            SocialCategory.TRADING,
            3,
            Arrays.asList("Increases bazaar participation", "Increases economic interaction", "Increases social commerce")
        ));
        
        // Event Activities
        socialConfigs.put(SocialActivity.EVENT, new SocialConfig(
            SocialActivity.EVENT,
            "§cEvent",
            "§7Participate in server events",
            Material.FIREWORK_ROCKET,
            SocialCategory.EVENTS,
            1,
            Arrays.asList("Increases event participation", "Increases social interaction", "Increases community building")
        ));
        
        socialConfigs.put(SocialActivity.CONTEST, new SocialConfig(
            SocialActivity.CONTEST,
            "§6Contest",
            "§7Participate in contests",
            Material.GOLD_INGOT,
            SocialCategory.EVENTS,
            2,
            Arrays.asList("Increases contest participation", "Increases competition", "Increases social achievement")
        ));
        
        socialConfigs.put(SocialActivity.TOURNAMENT, new SocialConfig(
            SocialActivity.TOURNAMENT,
            "§dTournament",
            "§7Participate in tournaments",
            Material.DIAMOND_SWORD,
            SocialCategory.EVENTS,
            3,
            Arrays.asList("Increases tournament participation", "Increases competition", "Increases social achievement")
        ));
        
        // Help Activities
        socialConfigs.put(SocialActivity.HELP, new SocialConfig(
            SocialActivity.HELP,
            "§aHelp",
            "§7Help other players",
            Material.BOOK,
            SocialCategory.HELP,
            1,
            Arrays.asList("Increases helping behavior", "Increases social support", "Increases community building")
        ));
        
        socialConfigs.put(SocialActivity.MENTOR, new SocialConfig(
            SocialActivity.MENTOR,
            "§eMentor",
            "§7Mentor new players",
            Material.EXPERIENCE_BOTTLE,
            SocialCategory.HELP,
            2,
            Arrays.asList("Increases mentoring", "Increases social support", "Increases community building")
        ));
        
        socialConfigs.put(SocialActivity.MODERATE, new SocialConfig(
            SocialActivity.MODERATE,
            "§cModerate",
            "§7Moderate the server",
            Material.BARRIER,
            SocialCategory.HELP,
            3,
            Arrays.asList("Increases moderation", "Increases social responsibility", "Increases community building")
        ));
    }
    
    private void initializeSocialLocationConfigs() {
        // Spawn Island
        socialLocationConfigs.put(SocialLocation.SPAWN, new SocialLocationConfig(
            SocialLocation.SPAWN,
            "§aSpawn Island",
            "§7A peaceful social spot",
            Material.GRASS_BLOCK,
            1,
            Arrays.asList(
                new SocialReward("Social Token", Material.EMERALD, 100, "§aSocial Token"),
                new SocialReward("Chat Badge", Material.PAPER, 150, "§bChat Badge"),
                new SocialReward("Friend Badge", Material.PLAYER_HEAD, 200, "§dFriend Badge")
            )
        ));
        
        // Deep Caverns
        socialLocationConfigs.put(SocialLocation.DEEP_CAVERNS, new SocialLocationConfig(
            SocialLocation.DEEP_CAVERNS,
            "§7Deep Caverns",
            "§7A deep social spot",
            Material.STONE,
            2,
            Arrays.asList(
                new SocialReward("Social Token", Material.EMERALD, 200, "§aSocial Token"),
                new SocialReward("Chat Badge", Material.PAPER, 300, "§bChat Badge"),
                new SocialReward("Friend Badge", Material.PLAYER_HEAD, 400, "§dFriend Badge"),
                new SocialReward("Party Badge", Material.PLAYER_HEAD, 500, "§dParty Badge")
            )
        ));
        
        // The End
        socialLocationConfigs.put(SocialLocation.THE_END, new SocialLocationConfig(
            SocialLocation.THE_END,
            "§5The End",
            "§7An end social spot",
            Material.END_STONE,
            3,
            Arrays.asList(
                new SocialReward("Social Token", Material.EMERALD, 300, "§aSocial Token"),
                new SocialReward("Chat Badge", Material.PAPER, 450, "§bChat Badge"),
                new SocialReward("Friend Badge", Material.PLAYER_HEAD, 600, "§dFriend Badge"),
                new SocialReward("Party Badge", Material.PLAYER_HEAD, 750, "§dParty Badge"),
                new SocialReward("Guild Badge", Material.GOLD_BLOCK, 1000, "§6Guild Badge")
            )
        ));
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());
        
        if (displayName.contains("Social") || displayName.contains("social")) {
            openSocialGUI(player);
        }
    }
    
    public void openSocialGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lSocial System"));
        
        // Add social locations
        addGUIItem(gui, 10, Material.GRASS_BLOCK, "§a§lSpawn Island", "§7A peaceful social spot");
        addGUIItem(gui, 11, Material.STONE, "§7§lDeep Caverns", "§7A deep social spot");
        addGUIItem(gui, 12, Material.END_STONE, "§5§lThe End", "§7An end social spot");
        
        // Add social management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Social Progress", "§7View your social progress.");
        addGUIItem(gui, 19, Material.PLAYER_HEAD, "§d§lFriends", "§7Manage your friends.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lSocial Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lSocial Shop", "§7Buy social items.");
        addGUIItem(gui, 22, Material.GOLD_INGOT, "§6§lSocial Contests", "§7Join social contests.");
        
        // Add social categories
        addGUIItem(gui, 27, Material.PAPER, "§b§lCommunication", "§7Chat and party activities");
        addGUIItem(gui, 28, Material.EMERALD, "§e§lTrading", "§7Trade and auction activities");
        addGUIItem(gui, 29, Material.FIREWORK_ROCKET, "§c§lEvents", "§7Event and contest activities");
        addGUIItem(gui, 30, Material.BOOK, "§a§lHelp", "§7Help and mentor activities");
        
        // Add communication activities
        addGUIItem(gui, 36, Material.PAPER, "§b§lChat", "§7Communicate with other players");
        addGUIItem(gui, 37, Material.PLAYER_HEAD, "§d§lParty", "§7Join parties with other players");
        addGUIItem(gui, 38, Material.GOLD_BLOCK, "§6§lGuild", "§7Join guilds with other players");
        
        // Add trading activities
        addGUIItem(gui, 45, Material.EMERALD, "§e§lTrade", "§7Trade items with other players");
        addGUIItem(gui, 46, Material.GOLD_INGOT, "§6§lAuction", "§7Auction items to other players");
        addGUIItem(gui, 47, Material.EMERALD_BLOCK, "§a§lBazaar", "§7Buy and sell items at the bazaar");
        
        // Add event activities
        addGUIItem(gui, 48, Material.FIREWORK_ROCKET, "§c§lEvent", "§7Participate in server events");
        addGUIItem(gui, 49, Material.GOLD_INGOT, "§6§lContest", "§7Participate in contests");
        addGUIItem(gui, 50, Material.DIAMOND_SWORD, "§d§lTournament", "§7Participate in tournaments");
        
        // Add help activities
        addGUIItem(gui, 51, Material.BOOK, "§a§lHelp", "§7Help other players");
        addGUIItem(gui, 52, Material.EXPERIENCE_BOTTLE, "§e§lMentor", "§7Mentor new players");
        addGUIItem(gui, 53, Material.BARRIER, "§c§lModerate", "§7Moderate the server");
        
        player.openInventory(gui);
        player.sendMessage("§aSocial GUI opened!");
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
    
    public PlayerSocialData getPlayerSocialData(UUID playerId) {
        return playerSocialData.computeIfAbsent(playerId, k -> new PlayerSocialData(playerId));
    }
    
    public enum SocialActivity {
        CHAT("§bChat", "§7Communicate with other players"),
        PARTY("§dParty", "§7Join parties with other players"),
        GUILD("§6Guild", "§7Join guilds with other players"),
        TRADE("§eTrade", "§7Trade items with other players"),
        AUCTION("§6Auction", "§7Auction items to other players"),
        BAZAAR("§aBazaar", "§7Buy and sell items at the bazaar"),
        EVENT("§cEvent", "§7Participate in server events"),
        CONTEST("§6Contest", "§7Participate in contests"),
        TOURNAMENT("§dTournament", "§7Participate in tournaments"),
        HELP("§aHelp", "§7Help other players"),
        MENTOR("§eMentor", "§7Mentor new players"),
        MODERATE("§cModerate", "§7Moderate the server");
        
        private final String displayName;
        private final String description;
        
        SocialActivity(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum SocialCategory {
        COMMUNICATION("§bCommunication", "§7Chat and party activities"),
        TRADING("§eTrading", "§7Trade and auction activities"),
        EVENTS("§cEvents", "§7Event and contest activities"),
        HELP("§aHelp", "§7Help and mentor activities");
        
        private final String displayName;
        private final String description;
        
        SocialCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum SocialLocation {
        SPAWN("§aSpawn Island", "§7A peaceful social spot"),
        DEEP_CAVERNS("§7Deep Caverns", "§7A deep social spot"),
        THE_END("§5The End", "§7An end social spot");
        
        private final String displayName;
        private final String description;
        
        SocialLocation(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class SocialConfig {
        private final SocialActivity activity;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final SocialCategory category;
        private final int requiredLevel;
        private final List<String> benefits;
        
        public SocialConfig(SocialActivity activity, String displayName, String description, Material icon,
                          SocialCategory category, int requiredLevel, List<String> benefits) {
            this.activity = activity;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.category = category;
            this.requiredLevel = requiredLevel;
            this.benefits = benefits;
        }
        
        public SocialActivity getActivity() { return activity; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public SocialCategory getCategory() { return category; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<String> getBenefits() { return benefits; }
    }
    
    public static class SocialLocationConfig {
        private final SocialLocation location;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final int requiredLevel;
        private final List<SocialReward> rewards;
        
        public SocialLocationConfig(SocialLocation location, String displayName, String description, Material icon,
                                 int requiredLevel, List<SocialReward> rewards) {
            this.location = location;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.requiredLevel = requiredLevel;
            this.rewards = rewards;
        }
        
        public SocialLocation getLocation() { return location; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public int getRequiredLevel() { return requiredLevel; }
        public List<SocialReward> getRewards() { return rewards; }
    }
    
    public static class SocialReward {
        private final String name;
        private final Material material;
        private final int cost;
        private final String displayName;
        
        public SocialReward(String name, Material material, int cost, String displayName) {
            this.name = name;
            this.material = material;
            this.cost = cost;
            this.displayName = displayName;
        }
        
        public String getName() { return name; }
        public Material getMaterial() { return material; }
        public int getCost() { return cost; }
        public String getDisplayName() { return displayName; }
    }
    
    public static class PlayerSocialData {
        private final UUID playerId;
        private int socialLevel;
        private int socialXP;
        private final Map<SocialLocation, Integer> locationStats = new HashMap<>();
        private final Map<SocialActivity, Integer> activityStats = new HashMap<>();
        
        public PlayerSocialData(UUID playerId) {
            this.playerId = playerId;
            this.socialLevel = 1;
            this.socialXP = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getSocialLevel() { return socialLevel; }
        public int getSocialXP() { return socialXP; }
        public int getLocationStats(SocialLocation location) { return locationStats.getOrDefault(location, 0); }
        public int getActivityStats(SocialActivity activity) { return activityStats.getOrDefault(activity, 0); }
        
        public void addSocialXP(int xp) {
            this.socialXP += xp;
            checkLevelUp();
        }
        
        public void addLocationStat(SocialLocation location) {
            locationStats.put(location, locationStats.getOrDefault(location, 0) + 1);
        }
        
        public void addActivityStat(SocialActivity activity) {
            activityStats.put(activity, activityStats.getOrDefault(activity, 0) + 1);
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(socialLevel + 1);
            if (socialXP >= requiredXP) {
                socialLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
}
