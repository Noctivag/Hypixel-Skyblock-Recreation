package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.calendar.CalendarEvent;
import de.noctivag.skyblock.calendar.CalendarSystem;
import de.noctivag.skyblock.calendar.EventType;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class CalendarGUI extends Menu {
    
    private CalendarSystem calendarSystem;
    private EventType currentFilter = null; // null = all events
    private int currentPage = 0;
    private static final int ITEMS_PER_PAGE = 28; // 4 rows of 7 items
    
    public CalendarGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lCalendar & Events", 54);
        this.calendarSystem = plugin.getCalendarSystem();
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Current time display
        setupTimeDisplay();
        
        // Filter buttons
        setupFilterButtons();
        
        // Event items
        setupEventItems();
        
        // Navigation buttons
        setupNavigationButtons();
        
        // Close button
        setCloseButton(49);
    }
    
    private void setupTimeDisplay() {
        if (calendarSystem != null) {
            setItem(4, Material.CLOCK, "§eCurrent Time", "special",
                "&7Date: &f" + calendarSystem.getCurrentDate(),
                "&7Time: &f" + calendarSystem.getCurrentTime(),
                "",
                "&7Game time is synchronized with",
                "&7real-world time for events.");
        }
    }
    
    private void setupFilterButtons() {
        EventType[] types = EventType.values();
        int startSlot = 1;
        
        // All events button
        setItem(startSlot, Material.BOOK, "All Events", 
            currentFilter == null ? "special" : "uncommon",
            "&7Show all events",
            "",
            currentFilter == null ? "&aCurrently selected" : "&eClick to view");
        
        // Event type buttons
        for (int i = 0; i < Math.min(types.length, 6); i++) {
            EventType type = types[i];
            boolean isSelected = type == currentFilter;
            
            setItem(startSlot + 1 + i, type.getIcon(), type.getDisplayName(), 
                isSelected ? "special" : "uncommon",
                type.getDescription(),
                "",
                isSelected ? "&aCurrently selected" : "&eClick to filter");
        }
    }
    
    private void setupEventItems() {
        if (calendarSystem == null) {
            player.sendMessage("§cCalendar system not available!");
            return;
        }
        
        List<CalendarEvent> events = getFilteredEvents();
        int startSlot = 10; // Start after the filter row
        int endSlot = 43; // End before navigation row
        
        // Clear event area
        for (int i = startSlot; i <= endSlot; i++) {
            if (i % 9 != 0 && i % 9 != 8) { // Not border slots
                inventory.setItem(i, null);
            }
        }
        
        // Calculate pagination
        int totalPages = (int) Math.ceil((double) events.size() / ITEMS_PER_PAGE);
        if (currentPage >= totalPages) {
            currentPage = Math.max(0, totalPages - 1);
        }
        
        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, events.size());
        
        int slot = startSlot;
        for (int i = startIndex; i < endIndex; i++) {
            CalendarEvent event = events.get(i);
            boolean isActive = calendarSystem.isEventActive(event);
            boolean isUpcoming = calendarSystem.isUpcomingEvents();
            
            String rarity = isActive ? "mythic" : (isUpcoming ? "epic" : "uncommon");
            String[] lore = {
                event.getDescription(),
                "",
                "&7Type: " + event.getType().getDisplayName(),
                "&7Duration: " + event.getFormattedDuration(),
                "",
                isActive ? "&aCurrently Active" : (isUpcoming ? "&eUpcoming" : "&7Scheduled"),
                "",
                "&eClick for details"
            };
            
            setItem(slot, event.getType().getIcon(), event.getName(), rarity, lore);
            
            slot++;
            if (slot % 9 == 8) slot += 2; // Skip border slots
        }
    }
    
    private void setupNavigationButtons() {
        if (calendarSystem == null) return;
        
        List<CalendarEvent> events = getFilteredEvents();
        int totalPages = (int) Math.ceil((double) events.size() / ITEMS_PER_PAGE);
        
        // Previous page button
        if (currentPage > 0) {
            setBackButton(45);
        }
        
        // Next page button
        if (currentPage < totalPages - 1) {
            setNextButton(53);
        }
        
        // Page info
        if (totalPages > 1) {
            setItem(49, Material.BOOK, "§ePage " + (currentPage + 1) + "/" + totalPages, "uncommon",
                "&7Showing " + (currentFilter != null ? currentFilter.getDisplayName() : "All") + " events",
                "&7Total events: " + events.size());
        }
    }
    
    private List<CalendarEvent> getFilteredEvents() {
        if (calendarSystem == null) return List.of();
        
        if (currentFilter == null) {
            return calendarSystem.getAllEvents().stream().toList();
        } else {
            return calendarSystem.getEventsByType(currentFilter);
        }
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        // Filter buttons (slots 1-7)
        if (slot >= 1 && slot <= 7) {
            if (slot == 1) {
                currentFilter = null; // All events
            } else {
                EventType[] types = EventType.values();
                int typeIndex = slot - 2;
                
                if (typeIndex < types.length) {
                    currentFilter = types[typeIndex];
                }
            }
            currentPage = 0;
            setupItems();
            return;
        }
        
        // Navigation buttons
        if (slot == 45) { // Previous page
            if (currentPage > 0) {
                currentPage--;
                setupItems();
            }
            return;
        }
        
        if (slot == 53) { // Next page
            List<CalendarEvent> events = getFilteredEvents();
            int totalPages = (int) Math.ceil((double) events.size() / ITEMS_PER_PAGE);
            
            if (currentPage < totalPages - 1) {
                currentPage++;
                setupItems();
            }
            return;
        }
        
        // Event items (slots 10-43)
        if (slot >= 10 && slot <= 43 && slot % 9 != 0 && slot % 9 != 8) {
            if (calendarSystem != null) {
                List<CalendarEvent> events = getFilteredEvents();
                int startIndex = currentPage * ITEMS_PER_PAGE;
                int eventIndex = startIndex + (slot - 10);
                
                if (eventIndex < events.size()) {
                    CalendarEvent selectedEvent = events.get(eventIndex);
                    
                    // Show event details
                    player.sendMessage("§aEvent: " + selectedEvent.getDisplayName());
                    player.sendMessage("§7Type: " + selectedEvent.getType().getDisplayName());
                    player.sendMessage("§7Description: " + selectedEvent.getDescription());
                    player.sendMessage("§7Duration: " + selectedEvent.getFormattedDuration());
                    
                    if (!selectedEvent.getRewards().isEmpty()) {
                        player.sendMessage("§7Rewards:");
                        for (String reward : selectedEvent.getRewards()) {
                            player.sendMessage("  " + reward);
                        }
                    }
                    
                    boolean isActive = calendarSystem.isEventActive(selectedEvent);
                    if (isActive) {
                        player.sendMessage("§aThis event is currently active!");
                    } else {
                        player.sendMessage("§7This event is scheduled for later.");
                    }
                }
            }
            return;
        }
        
        // Close button
        if (slot == 49) {
            close();
        }
    }
}
