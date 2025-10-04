package de.noctivag.skyblock.events;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.core.CorePlatform;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Events System - Hypixel Skyblock Style
 * 
 * Features:
 * - Spooky Festival
 * - New Year Celebration
 * - Jacob's Farming Contest
 * - Mayor Elections
 * - Dark Auction
 * - Community Shop
 * - Event rewards and achievements
 */
public class EventsSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final CorePlatform corePlatform;
    private final Map<String, SkyBlockEvent> events = new HashMap<>();
    private final Map<UUID, PlayerEventData> playerEventData = new ConcurrentHashMap<>();
    private final Map<String, EventReward> eventRewards = new HashMap<>();
    
    public EventsSystem(SkyblockPlugin plugin, CorePlatform corePlatform) {
        this.plugin = plugin;
        this.corePlatform = corePlatform;
        initializeEvents();
        initializeEventRewards();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
        startEventScheduler();
    }
    
    private void initializeEvents() {
        // Spooky Festival
        events.put("SPOOKY_FESTIVAL", new SkyBlockEvent(
            "SPOOKY_FESTIVAL", "Spooky Festival", "§6§lSpooky Festival",
            "§7A spooky event with special rewards", EventType.SEASONAL,
            Arrays.asList("§7• Increased candy drops", "§7• Special spooky items", "§7• Halloween-themed rewards"),
            Arrays.asList("CANDY_DROPS", "SPOOKY_ITEMS", "HALLOWEEN_REWARDS")
        ));
        
        // New Year Celebration
        events.put("NEW_YEAR", new SkyBlockEvent(
            "NEW_YEAR", "New Year Celebration", "§e§lNew Year Celebration",
            "§7Celebrate the new year with special rewards", EventType.SEASONAL,
            Arrays.asList("§7• Firework displays", "§7• New Year items", "§7• Celebration rewards"),
            Arrays.asList("FIREWORKS", "NEW_YEAR_ITEMS", "CELEBRATION_REWARDS")
        ));
        
        // Jacob's Farming Contest
        events.put("JACOBS_CONTEST", new SkyBlockEvent(
            "JACOBS_CONTEST", "Jacob's Farming Contest", "§a§lJacob's Farming Contest",
            "§7Compete in farming contests for rewards", EventType.CONTEST,
            Arrays.asList("§7• Farming competitions", "§7• Leaderboards", "§7• Contest rewards"),
            Arrays.asList("FARMING_CONTEST", "LEADERBOARDS", "CONTEST_REWARDS")
        ));
        
        // Mayor Elections
        events.put("MAYOR_ELECTIONS", new SkyBlockEvent(
            "MAYOR_ELECTIONS", "Mayor Elections", "§b§lMayor Elections",
            "§7Vote for your favorite mayor", EventType.POLITICAL,
            Arrays.asList("§7• Vote for mayors", "§7• Mayor perks", "§7• Election rewards"),
            Arrays.asList("VOTING", "MAYOR_PERKS", "ELECTION_REWARDS")
        ));
        
        // Dark Auction
        events.put("DARK_AUCTION", new SkyBlockEvent(
            "DARK_AUCTION", "Dark Auction", "§8§lDark Auction",
            "§7A mysterious auction with rare items", EventType.AUCTION,
            Arrays.asList("§7• Rare items", "§7• High-value auctions", "§7• Dark rewards"),
            Arrays.asList("RARE_ITEMS", "HIGH_VALUE_AUCTIONS", "DARK_REWARDS")
        ));
        
        // Community Shop
        events.put("COMMUNITY_SHOP", new SkyBlockEvent(
            "COMMUNITY_SHOP", "Community Shop", "§6§lCommunity Shop",
            "§7A shop run by the community", EventType.SHOP,
            Arrays.asList("§7• Community items", "§7• Special deals", "§7• Community rewards"),
            Arrays.asList("COMMUNITY_ITEMS", "SPECIAL_DEALS", "COMMUNITY_REWARDS")
        ));
    }
    
    private void initializeEventRewards() {
        // Spooky Festival Rewards
        eventRewards.put("SPOOKY_CANDY", new EventReward(
            "SPOOKY_CANDY", "Spooky Candy", "§6Spooky Candy", Material.PUMPKIN_PIE,
            "§7Special candy from the Spooky Festival", EventRewardType.ITEM,
            Arrays.asList("§7• +50% XP for 1 hour", "§7• Spooky effects")
        ));
        
        eventRewards.put("HALLOWEEN_MASK", new EventReward(
            "HALLOWEEN_MASK", "Halloween Mask", "§6Halloween Mask", Material.PLAYER_HEAD,
            "§7A spooky mask for Halloween", EventRewardType.COSMETIC,
            Arrays.asList("§7• Spooky appearance", "§7• Halloween effects")
        ));
        
        // New Year Rewards
        eventRewards.put("NEW_YEAR_FIREWORK", new EventReward(
            "NEW_YEAR_FIREWORK", "New Year Firework", "§eNew Year Firework", Material.FIREWORK_ROCKET,
            "§7A special firework for New Year", EventRewardType.ITEM,
            Arrays.asList("§7• Beautiful firework display", "§7• New Year effects")
        ));
        
        // Jacob's Contest Rewards
        eventRewards.put("FARMING_TROPHY", new EventReward(
            "FARMING_TROPHY", "Farming Trophy", "§aFarming Trophy", Material.GOLD_INGOT,
            "§7A trophy for farming excellence", EventRewardType.TROPHY,
            Arrays.asList("§7• +10% Farming XP", "§7• Trophy display")
        ));
        
        // Mayor Election Rewards
        eventRewards.put("VOTER_BADGE", new EventReward(
            "VOTER_BADGE", "Voter Badge", "§bVoter Badge", Material.NAME_TAG,
            "§7A badge for participating in elections", EventRewardType.BADGE,
            Arrays.asList("§7• Voting privileges", "§7• Badge display")
        ));
        
        // Dark Auction Rewards
        eventRewards.put("DARK_ORB", new EventReward(
            "DARK_ORB", "Dark Orb", "§8Dark Orb", Material.ENDER_PEARL,
            "§7A mysterious orb from the Dark Auction", EventRewardType.ITEM,
            Arrays.asList("§7• Dark powers", "§7• Mysterious effects")
        ));
    }
    
    private void startEventScheduler() {
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    // Check for active events and update them
                    for (SkyBlockEvent event : events.values()) {
                        if (event.isActive()) {
                            event.update();
                        }
                    }
                    Thread.sleep(20L * 60L * 50); // Check every minute = 60,000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.contains("Events")) {
            event.setCancelled(true);
            handleEventsGUIClick(player, event.getSlot());
        }
    }
    
    public void openEventsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lEvents");
        
        // Active events
        List<SkyBlockEvent> activeEvents = events.values().stream()
            .filter(SkyBlockEvent::isActive)
            .toList();
        
        int slot = 10;
        for (SkyBlockEvent event : activeEvents) {
            if (slot >= 44) break;
            
            ItemStack eventItem = createEventItemStack(event);
            gui.setItem(slot, eventItem);
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        // Event categories
        addGUIItem(gui, 19, Material.PUMPKIN, "§6§lSeasonal Events", 
            Arrays.asList("§7View seasonal events", "§7• Spooky Festival", "§7• New Year Celebration", "", "§eClick to view"));
        
        addGUIItem(gui, 20, Material.WHEAT, "§a§lContest Events", 
            Arrays.asList("§7View contest events", "§7• Jacob's Farming Contest", "§7• Other competitions", "", "§eClick to view"));
        
        addGUIItem(gui, 21, Material.EMERALD, "§b§lSpecial Events", 
            Arrays.asList("§7View special events", "§7• Mayor Elections", "§7• Dark Auction", "", "§eClick to view"));
        
        // My event data
        PlayerEventData eventData = getPlayerEventData(player.getUniqueId());
        addGUIItem(gui, 28, Material.GOLD_INGOT, "§e§lMy Event Progress", 
            Arrays.asList("§7View your event progress", "§7• Participated: §e" + eventData.getParticipatedEvents().size(),
                         "§7• Rewards earned: §e" + eventData.getEarnedRewards().size(), "", "§eClick to view"));
        
        // Event rewards
        addGUIItem(gui, 29, Material.CHEST, "§6§lEvent Rewards", 
            Arrays.asList("§7View available event rewards", "§7• Items", "§7• Cosmetics", "§7• Trophies", "", "§eClick to view"));
        
        // Close button
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", 
            Arrays.asList("§7Close events", "", "§eClick to close"));
        
        player.openInventory(gui);
    }
    
    public void openEventDetailsGUI(Player player, String eventId) {
        SkyBlockEvent event = events.get(eventId);
        if (event == null) return;
        
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lEvent - " + event.getName());
        
        // Event display
        ItemStack eventItem = createEventItemStack(event);
        gui.setItem(4, eventItem);
        
        // Event actions
        if (event.isActive()) {
            addGUIItem(gui, 10, Material.EMERALD, "§a§lParticipate", 
                Arrays.asList("§7Join this event", "§7• Earn rewards", "§7• Compete with others", "", "§eClick to participate"));
            
            addGUIItem(gui, 11, Material.CHEST, "§6§lView Rewards", 
                Arrays.asList("§7View event rewards", "§7• Available items", "§7• Requirements", "", "§eClick to view"));
        } else {
            addGUIItem(gui, 10, Material.GRAY_DYE, "§7§lEvent Inactive", 
                Arrays.asList("§7This event is not currently active", "§7• Check back later", "§7• View event history", "", "§cEvent not available"));
        }
        
        // Navigation
        addGUIItem(gui, 22, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to events", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public void openEventRewardsGUI(Player player, String eventId) {
        SkyBlockEvent event = events.get(eventId);
        if (event == null) return;
        
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lEvent Rewards - " + event.getName());
        
        // Get rewards for this event
        List<EventReward> eventRewardsList = eventRewards.values().stream()
            .filter(reward -> event.getRewardTypes().contains(reward.getType().name()))
            .toList();
        
        int slot = 10;
        for (EventReward reward : eventRewardsList) {
            if (slot >= 44) break;
            
            ItemStack rewardItem = createEventRewardItemStack(reward);
            gui.setItem(slot, rewardItem);
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to event details", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public boolean participateInEvent(Player player, String eventId) {
        SkyBlockEvent event = events.get(eventId);
        if (event == null || !event.isActive()) return false;
        
        PlayerEventData eventData = getPlayerEventData(player.getUniqueId());
        eventData.addParticipatedEvent(eventId);
        
        player.sendMessage("§a§lEVENT PARTICIPATION!");
        player.sendMessage("§7Event: §e" + event.getName());
        player.sendMessage("§7You are now participating in this event!");
        
        return true;
    }
    
    public boolean claimEventReward(Player player, String rewardId) {
        EventReward reward = eventRewards.get(rewardId);
        if (reward == null) return false;
        
        PlayerEventData eventData = getPlayerEventData(player.getUniqueId());
        if (eventData.hasEarnedReward(rewardId)) {
            player.sendMessage("§cYou have already claimed this reward!");
            return false;
        }
        
        // Check if player meets requirements
        if (!meetsRewardRequirements(player, reward)) {
            player.sendMessage("§cYou don't meet the requirements for this reward!");
            return false;
        }
        
        // Give reward
        giveEventReward(player, reward);
        eventData.addEarnedReward(rewardId);
        
        player.sendMessage("§a§lEVENT REWARD CLAIMED!");
        player.sendMessage("§7Reward: " + reward.getDisplayName());
        
        return true;
    }
    
    private boolean meetsRewardRequirements(Player player, EventReward reward) {
        // Check if player meets the requirements for the reward
        // This would need to be implemented based on the specific reward
        return true; // Simplified for now
    }
    
    private void giveEventReward(Player player, EventReward reward) {
        // Give the reward to the player
        ItemStack item = new ItemStack(reward.getMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(reward.getDisplayName()));
            meta.lore(reward.getEffects().stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        player.getInventory().addItem(item);
    }
    
    private ItemStack createEventItemStack(SkyBlockEvent event) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(event.getDisplayName()));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Type: " + event.getType().getDisplayName());
            lore.add("§7Status: " + (event.isActive() ? "§aActive" : "§cInactive"));
            lore.add("");
            lore.add("§7Description:");
            lore.add(event.getDescription());
            lore.add("");
            lore.add("§7Features:");
            lore.addAll(event.getFeatures());
            lore.add("");
            lore.add("§eClick to view details");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private ItemStack createEventRewardItemStack(EventReward reward) {
        ItemStack item = new ItemStack(reward.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(reward.getDisplayName()));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Type: " + reward.getType().getDisplayName());
            lore.add("");
            lore.add("§7Description:");
            lore.add(reward.getDescription());
            lore.add("");
            lore.add("§7Effects:");
            lore.addAll(reward.getEffects());
            lore.add("");
            lore.add("§eClick to claim");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private void handleEventsGUIClick(Player player, int slot) {
        switch (slot) {
            case 19: // Seasonal Events
                player.sendMessage("§eSeasonal Events coming soon!");
                break;
            case 20: // Contest Events
                player.sendMessage("§eContest Events coming soon!");
                break;
            case 21: // Special Events
                player.sendMessage("§eSpecial Events coming soon!");
                break;
            case 28: // My Event Progress
                player.sendMessage("§eMy Event Progress coming soon!");
                break;
            case 29: // Event Rewards
                player.sendMessage("§eEvent Rewards coming soon!");
                break;
            case 49: // Close
                player.closeInventory();
                break;
        }
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, List<String> description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(description.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerEventData getPlayerEventData(UUID playerId) {
        return playerEventData.computeIfAbsent(playerId, k -> new PlayerEventData(playerId));
    }
    
    public SkyBlockEvent getEvent(String eventId) {
        return events.get(eventId);
    }
    
    public Map<String, SkyBlockEvent> getAllEvents() {
        return new HashMap<>(events);
    }
    
    // Event Classes
    public static class SkyBlockEvent {
        private final String id;
        private final String name;
        private final String displayName;
        private final String description;
        private final EventType type;
        private final List<String> features;
        private final List<String> rewardTypes;
        private boolean active;
        private long startTime;
        private long endTime;
        
        public SkyBlockEvent(String id, String name, String displayName, String description, EventType type,
                           List<String> features, List<String> rewardTypes) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.type = type;
            this.features = features;
            this.rewardTypes = rewardTypes;
            this.active = false;
            this.startTime = 0;
            this.endTime = 0;
        }
        
        public void update() {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= startTime && currentTime <= endTime) {
                active = true;
            } else {
                active = false;
            }
        }
        
        public void startEvent(long duration) {
            this.startTime = System.currentTimeMillis();
            this.endTime = startTime + duration;
            this.active = true;
        }
        
        public void stopEvent() {
            this.active = false;
            this.endTime = System.currentTimeMillis();
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public EventType getType() { return type; }
        public List<String> getFeatures() { return features; }
        public List<String> getRewardTypes() { return rewardTypes; }
        public boolean isActive() { return active; }
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
    }
    
    public enum EventType {
        SEASONAL("§6Seasonal", "Events that occur during specific seasons"),
        CONTEST("§aContest", "Competitive events with leaderboards"),
        POLITICAL("§bPolitical", "Events related to voting and politics"),
        AUCTION("§8Auction", "Events involving auctions and trading"),
        SHOP("§6Shop", "Events involving shops and commerce");
        
        private final String displayName;
        private final String description;
        
        EventType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class EventReward {
        private final String id;
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final EventRewardType type;
        private final List<String> effects;
        
        public EventReward(String id, String name, String displayName, Material material, String description,
                          EventRewardType type, List<String> effects) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.type = type;
            this.effects = effects;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public EventRewardType getType() { return type; }
        public List<String> getEffects() { return effects; }
    }
    
    public enum EventRewardType {
        ITEM("§eItem", "Physical items that can be used"),
        COSMETIC("§dCosmetic", "Items that change appearance"),
        TROPHY("§6Trophy", "Items that display achievements"),
        BADGE("§bBadge", "Items that show status");
        
        private final String displayName;
        private final String description;
        
        EventRewardType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class PlayerEventData {
        private final UUID playerId;
        private final Set<String> participatedEvents = new HashSet<>();
        private final Set<String> earnedRewards = new HashSet<>();
        
        public PlayerEventData(UUID playerId) {
            this.playerId = playerId;
        }
        
        public void addParticipatedEvent(String eventId) {
            participatedEvents.add(eventId);
        }
        
        public void addEarnedReward(String rewardId) {
            earnedRewards.add(rewardId);
        }
        
        public boolean hasEarnedReward(String rewardId) {
            return earnedRewards.contains(rewardId);
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public Set<String> getParticipatedEvents() { return participatedEvents; }
        public Set<String> getEarnedRewards() { return earnedRewards; }
    }
}
