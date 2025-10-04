package de.noctivag.plugin.events;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Calendar System - Hypixel SkyBlock Style
 * 
 * Features:
 * - Event calendar with daily, weekly, and monthly events
 * - Event scheduling and reminders
 * - Special event tracking
 * - Calendar GUI with navigation
 * - Event participation tracking
 */
public class CalendarSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCalendarData> playerCalendarData = new ConcurrentHashMap<>();
    private final Map<String, CalendarEvent> events = new HashMap<>();
    private final Map<LocalDate, List<CalendarEvent>> eventsByDate = new HashMap<>();
    
    public CalendarSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeEvents();
        startCalendarUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeEvents() {
        // Daily Events
        addDailyEvent("mining_fiesta", "Mining Fiesta", 
            "\u00A77Double mining XP and rare ore spawns",
            EventType.DAILY, EventRarity.COMMON,
            Arrays.asList("\u00A7a• +100% Mining XP", "\u00A7a• 2x Ore Drops", "\u00A7a• Rare Ore Spawns"));

        addDailyEvent("farming_contest", "Farming Contest",
            "\u00A77Competition for the best farmer",
            EventType.DAILY, EventRarity.UNCOMMON,
            Arrays.asList("\u00A7a• +50% Farming XP", "\u00A7a• Contest Rewards", "\u00A7a• Special Crops"));

        addDailyEvent("fishing_tournament", "Fishing Tournament",
            "\u00A77Catch the rarest fish to win",
            EventType.DAILY, EventRarity.RARE,
            Arrays.asList("\u00A7a• +75% Fishing XP", "\u00A7a• Rare Fish Spawns", "\u00A7a• Tournament Prizes"));

        addDailyEvent("combat_arena", "Combat Arena",
            "\u00A77Special combat challenges",
            EventType.DAILY, EventRarity.UNCOMMON,
            Arrays.asList("\u00A7a• +60% Combat XP", "\u00A7a• Special Mobs", "\u00A7a• Arena Rewards"));

        // Weekly Events
        addWeeklyEvent("dungeon_week", "Dungeon Week",
            "\u00A77Enhanced dungeon rewards",
            EventType.WEEKLY, EventRarity.EPIC,
            Arrays.asList("\u00A7a• +200% Dungeon XP", "\u00A7a• Rare Drops", "\u00A7a• Special Bosses"));

        addWeeklyEvent("slayer_week", "Slayer Week",
            "\u00A77Enhanced slayer rewards",
            EventType.WEEKLY, EventRarity.EPIC,
            Arrays.asList("\u00A7a• +150% Slayer XP", "\u00A7a• Rare Drops", "\u00A7a• Special Quests"));

        addWeeklyEvent("auction_week", "Auction Week",
            "\u00A77Reduced auction fees",
            EventType.WEEKLY, EventRarity.UNCOMMON,
            Arrays.asList("\u00A7a• -50% Auction Fees", "\u00A7a• Special Items", "\u00A7a• Bonus Coins"));

        // Monthly Events
        addMonthlyEvent("anniversary_event", "Server Anniversary",
            "\u00A77Celebrate the server's anniversary",
            EventType.MONTHLY, EventRarity.LEGENDARY,
            Arrays.asList("\u00A7a• Special Rewards", "\u00A7a• Anniversary Items", "\u00A7a• Bonus Everything"));

        addMonthlyEvent("seasonal_event", "Seasonal Event",
            "\u00A77Special seasonal content",
            EventType.MONTHLY, EventRarity.RARE,
            Arrays.asList("\u00A7a• Seasonal Items", "\u00A7a• Special Activities", "\u00A7a• Limited Rewards"));

        // Special Events
        addSpecialEvent("double_coins", "Double Coins Weekend",
            "\u00A77Double coin rewards from all activities",
            EventType.SPECIAL, EventRarity.EPIC,
            Arrays.asList("\u00A7a• 2x All Coin Rewards", "\u00A7a• Enhanced Economy", "\u00A7a• Special Bonuses"));

        addSpecialEvent("xp_boost", "XP Boost Event",
            "\u00A77Massive XP bonuses for all skills",
            EventType.SPECIAL, EventRarity.LEGENDARY,
            Arrays.asList("\u00A7a• +300% All Skill XP", "\u00A7a• Faster Leveling", "\u00A7a• Bonus Rewards"));
    }
    
    private void addDailyEvent(String id, String name, String description, EventType type, EventRarity rarity, List<String> rewards) {
        CalendarEvent event = new CalendarEvent(id, name, description, type, rarity, rewards, 
            EventFrequency.DAILY, getRandomDailyTime());
        events.put(id, event);
        scheduleEvent(event);
    }
    
    private void addWeeklyEvent(String id, String name, String description, EventType type, EventRarity rarity, List<String> rewards) {
        CalendarEvent event = new CalendarEvent(id, name, description, type, rarity, rewards, 
            EventFrequency.WEEKLY, getRandomWeeklyTime());
        events.put(id, event);
        scheduleEvent(event);
    }
    
    private void addMonthlyEvent(String id, String name, String description, EventType type, EventRarity rarity, List<String> rewards) {
        CalendarEvent event = new CalendarEvent(id, name, description, type, rarity, rewards, 
            EventFrequency.MONTHLY, getRandomMonthlyTime());
        events.put(id, event);
        scheduleEvent(event);
    }
    
    private void addSpecialEvent(String id, String name, String description, EventType type, EventRarity rarity, List<String> rewards) {
        CalendarEvent event = new CalendarEvent(id, name, description, type, rarity, rewards, 
            EventFrequency.SPECIAL, getRandomSpecialTime());
        events.put(id, event);
        scheduleEvent(event);
    }
    
    private LocalDateTime getRandomDailyTime() {
        return LocalDateTime.now().plusDays(1).withHour(12).withMinute(0);
    }
    
    private LocalDateTime getRandomWeeklyTime() {
        return LocalDateTime.now().plusWeeks(1).withHour(18).withMinute(0);
    }
    
    private LocalDateTime getRandomMonthlyTime() {
        return LocalDateTime.now().plusMonths(1).withHour(20).withMinute(0);
    }
    
    private LocalDateTime getRandomSpecialTime() {
        return LocalDateTime.now().plusDays(3).withHour(15).withMinute(30);
    }
    
    private void scheduleEvent(CalendarEvent event) {
        LocalDate eventDate = event.getStartTime().toLocalDate();
        eventsByDate.computeIfAbsent(eventDate, k -> new ArrayList<>()).add(event);
    }
    
    private void startCalendarUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateCalendar();
                checkUpcomingEvents();
                cleanupOldEvents();
            }
        }.runTaskTimer(plugin, 0L, 20L * 60L); // Every minute
    }
    
    private void updateCalendar() {
        LocalDate today = LocalDate.now();
        
        // Add today's events to calendar
        if (!eventsByDate.containsKey(today)) {
            eventsByDate.put(today, new ArrayList<>());
        }
        
        // Check for active events
        for (CalendarEvent event : events.values()) {
            if (isEventActive(event)) {
                announceActiveEvent(event);
            }
        }
    }
    
    private void checkUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        
        for (CalendarEvent event : events.values()) {
            if (event.getStartTime().isAfter(now) && event.getStartTime().isBefore(now.plusHours(1))) {
                announceUpcomingEvent(event);
            }
        }
    }
    
    private void cleanupOldEvents() {
        LocalDate cutoffDate = LocalDate.now().minusDays(7);
        eventsByDate.entrySet().removeIf(entry -> entry.getKey().isBefore(cutoffDate));
    }
    
    private boolean isEventActive(CalendarEvent event) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = event.getStartTime();
        LocalDateTime endTime = startTime.plusHours(event.getDuration());
        
        return now.isAfter(startTime) && now.isBefore(endTime);
    }
    
    private void announceActiveEvent(CalendarEvent event) {
        String message = "§6§l[CALENDAR] §e" + event.getName() + " §7is now active!";
        Bukkit.broadcastMessage(message);
        
        for (String reward : event.getRewards()) {
            Bukkit.broadcastMessage("§7  " + reward);
        }
    }
    
    private void announceUpcomingEvent(CalendarEvent event) {
        String message = "§6§l[CALENDAR] §e" + event.getName() + " §7starts in 1 hour!";
        Bukkit.broadcastMessage(message);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Calendar")) {
            event.setCancelled(true);
            openCalendar(player);
        }
    }
    
    public void openCalendar(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§l📅 Event Calendar");
        
        // Current date header
        setupDateHeader(gui);
        
        // Today's events
        setupTodaysEvents(gui);
        
        // Weekly view
        setupWeeklyView(gui);
        
        // Monthly overview
        setupMonthlyOverview(gui);
        
        // Navigation
        setupNavigation(gui);
        
        player.openInventory(gui);
    }
    
    public void openDateView(Player player, LocalDate date) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§l📅 " + date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        
        List<CalendarEvent> dayEvents = eventsByDate.getOrDefault(date, new ArrayList<>());
        
        int slot = 10;
        for (CalendarEvent event : dayEvents) {
            if (slot >= 45) break;
            
            ItemStack eventItem = createEventItem(event);
            gui.setItem(slot, eventItem);
            slot++;
        }
        
        // Navigation
        setupDateNavigation(gui, date);
        
        player.openInventory(gui);
    }
    
    private void setupDateHeader(Inventory gui) {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy"));
        
        ItemStack dateItem = new ItemStack(Material.CLOCK);
        ItemMeta meta = dateItem.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§6§l" + dateStr);
            meta.setLore(Arrays.asList(
                "§7Today's events and activities",
                "",
                "§eClick to view today's events"
            ));
            dateItem.setItemMeta(meta);
        }
        
        gui.setItem(4, dateItem);
    }
    
    private void setupTodaysEvents(Inventory gui) {
        LocalDate today = LocalDate.now();
        List<CalendarEvent> todaysEvents = eventsByDate.getOrDefault(today, new ArrayList<>());
        
        ItemStack todayItem = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = todayItem.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§e§lToday's Events");
            meta.setLore(Arrays.asList(
                "§7Active events for today:",
                "",
                "§7Events: §e" + todaysEvents.size(),
                "",
                "§eClick to view details"
            ));
            todayItem.setItemMeta(meta);
        }
        
        gui.setItem(10, todayItem);
    }
    
    private void setupWeeklyView(Inventory gui) {
        ItemStack weeklyItem = new ItemStack(Material.CLOCK);
        ItemMeta meta = weeklyItem.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§b§lWeekly View");
            meta.setLore(Arrays.asList(
                "§7View events for the entire week",
                "",
                "§eClick to view weekly calendar"
            ));
            weeklyItem.setItemMeta(meta);
        }
        
        gui.setItem(12, weeklyItem);
    }
    
    private void setupMonthlyOverview(Inventory gui) {
        ItemStack monthlyItem = new ItemStack(Material.PAPER);
        ItemMeta meta = monthlyItem.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§d§lMonthly Overview");
            meta.setLore(Arrays.asList(
                "§7View all monthly events",
                "",
                "§eClick to view monthly calendar"
            ));
            monthlyItem.setItemMeta(meta);
        }
        
        gui.setItem(14, monthlyItem);
    }
    
    private void setupNavigation(Inventory gui) {
        // Previous day
        ItemStack prevItem = new ItemStack(Material.ARROW);
        ItemMeta prevMeta = prevItem.getItemMeta();
        
        if (prevMeta != null) {
            prevMeta.setDisplayName("§7§l← Previous Day");
            prevMeta.setLore(Arrays.asList("§7Go to previous day"));
            prevItem.setItemMeta(prevMeta);
        }
        
        gui.setItem(45, prevItem);
        
        // Next day
        ItemStack nextItem = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = nextItem.getItemMeta();
        
        if (nextMeta != null) {
            nextMeta.setDisplayName("§7§lNext Day →");
            nextMeta.setLore(Arrays.asList("§7Go to next day"));
            nextItem.setItemMeta(nextMeta);
        }
        
        gui.setItem(53, nextItem);
        
        // Close
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        
        if (closeMeta != null) {
            closeMeta.setDisplayName("§c§lClose");
            closeMeta.setLore(Arrays.asList("§7Close calendar"));
            closeItem.setItemMeta(closeMeta);
        }
        
        gui.setItem(49, closeItem);
    }
    
    private void setupDateNavigation(Inventory gui, LocalDate date) {
        // Back to calendar
        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta meta = backItem.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§7§l← Back to Calendar");
            meta.setLore(Arrays.asList("§7Return to main calendar"));
            backItem.setItemMeta(meta);
        }
        
        gui.setItem(45, backItem);
        
        // Close
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        
        if (closeMeta != null) {
            closeMeta.setDisplayName("§c§lClose");
            closeMeta.setLore(Arrays.asList("§7Close calendar"));
            closeItem.setItemMeta(closeMeta);
        }
        
        gui.setItem(49, closeItem);
    }
    
    private ItemStack createEventItem(CalendarEvent event) {
        ItemStack item = new ItemStack(event.getRarity().getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            String status = isEventActive(event) ? "§a§lACTIVE" : "§7§lUPCOMING";
            meta.setDisplayName(event.getRarity().getColor() + event.getName() + " " + status);
            
            List<String> lore = new ArrayList<>();
            lore.add(event.getDescription());
            lore.add("");
            lore.add("§7Type: " + event.getType().getDisplayName());
            lore.add("§7Rarity: " + event.getRarity().getDisplayName());
            lore.add("§7Start: §e" + event.getStartTime().format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")));
            lore.add("§7Duration: §e" + event.getDuration() + " hours");
            lore.add("");
            lore.add("§7Rewards:");
            for (String reward : event.getRewards()) {
                lore.add("§7  " + reward);
            }
            
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    public PlayerCalendarData getPlayerCalendarData(UUID playerId) {
        return playerCalendarData.computeIfAbsent(playerId, k -> new PlayerCalendarData(playerId));
    }
    
    public enum EventType {
        DAILY("Daily", "§a"),
        WEEKLY("Weekly", "§b"),
        MONTHLY("Monthly", "§d"),
        SPECIAL("Special", "§6");
        
        private final String displayName;
        private final String color;
        
        EventType(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }
        
        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
    }
    
    public enum EventRarity {
        COMMON("Common", "§f", Material.WHITE_DYE),
        UNCOMMON("Uncommon", "§a", Material.GREEN_DYE),
        RARE("Rare", "§9", Material.BLUE_DYE),
        EPIC("Epic", "§5", Material.PURPLE_DYE),
        LEGENDARY("Legendary", "§6", Material.ORANGE_DYE);
        
        private final String displayName;
        private final String color;
        private final Material material;
        
        EventRarity(String displayName, String color, Material material) {
            this.displayName = displayName;
            this.color = color;
            this.material = material;
        }
        
        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
        public Material getMaterial() { return material; }
    }
    
    public enum EventFrequency {
        DAILY, WEEKLY, MONTHLY, SPECIAL
    }
    
    public static class CalendarEvent {
        private final String id;
        private final String name;
        private final String description;
        private final EventType type;
        private final EventRarity rarity;
        private final List<String> rewards;
        private final EventFrequency frequency;
        private final LocalDateTime startTime;
        private final int duration;
        
        public CalendarEvent(String id, String name, String description, EventType type, EventRarity rarity,
                           List<String> rewards, EventFrequency frequency, LocalDateTime startTime) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.type = type;
            this.rarity = rarity;
            this.rewards = rewards;
            this.frequency = frequency;
            this.startTime = startTime;
            this.duration = calculateDuration(frequency);
        }
        
        private int calculateDuration(EventFrequency frequency) {
            switch (frequency) {
                case DAILY: return 6; // 6 hours
                case WEEKLY: return 48; // 48 hours
                case MONTHLY: return 168; // 1 week
                case SPECIAL: return 72; // 3 days
                default: return 24;
            }
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public EventType getType() { return type; }
        public EventRarity getRarity() { return rarity; }
        public List<String> getRewards() { return rewards; }
        public EventFrequency getFrequency() { return frequency; }
        public LocalDateTime getStartTime() { return startTime; }
        public int getDuration() { return duration; }
    }
    
    public static class PlayerCalendarData {
        private final UUID playerId;
        private final Map<String, Long> eventParticipations;
        private final Set<String> completedEvents;
        
        public PlayerCalendarData(UUID playerId) {
            this.playerId = playerId;
            this.eventParticipations = new HashMap<>();
            this.completedEvents = new HashSet<>();
        }
        
        public void participateInEvent(String eventId) {
            eventParticipations.put(eventId, System.currentTimeMillis());
        }
        
        public void completeEvent(String eventId) {
            completedEvents.add(eventId);
        }
        
        public boolean hasParticipatedInEvent(String eventId) {
            return eventParticipations.containsKey(eventId);
        }
        
        public boolean hasCompletedEvent(String eventId) {
            return completedEvents.contains(eventId);
        }
        
        public Map<String, Long> getEventParticipations() { return eventParticipations; }
        public Set<String> getCompletedEvents() { return completedEvents; }
    }
}
