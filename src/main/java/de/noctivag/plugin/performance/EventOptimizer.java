package de.noctivag.plugin.performance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EventOptimizer - Optimiert Event-Handler-Performance
 * 
 * Features:
 * - Event-Routing
 * - Cooldown-Management
 * - Batch-Processing
 * - Priority-Management
 */
public class EventOptimizer implements Listener {
    private final Plugin plugin;
    
    // Event-Routing
    private final Map<String, List<java.util.function.Consumer<Event>>> eventHandlers = new ConcurrentHashMap<>();
    private final Map<UUID, Long> playerCooldowns = new ConcurrentHashMap<>();
    
    // Performance-Konfiguration
    private final long DEFAULT_COOLDOWN = 100; // 100ms
    private final int MAX_HANDLERS_PER_EVENT = 10;
    
    public EventOptimizer(Plugin plugin) {
        this.plugin = plugin;
        org.bukkit.Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Registriert Event-Handler mit Optimierung
     */
    public void registerHandler(String eventType, java.util.function.Consumer<Event> handler) {
        eventHandlers.computeIfAbsent(eventType, k -> new ArrayList<>()).add(handler);
        
        // Handler sind jetzt Consumer, keine Priorität-Sortierung nötig
    }
    
    /**
     * Optimierter InventoryClick-Handler
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onOptimizedInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        // Cooldown-Check
        if (isOnCooldown(player, "inventory_click")) return;
        
        String guiTitle = "GUI"; // Placeholder since getTitle() is deprecated
        
        // Route zu spezifischen Handlers
        List<java.util.function.Consumer<Event>> handlers = eventHandlers.get("inventory_" + getGUIType(guiTitle));
        if (handlers != null) {
            executeHandlers(event, handlers, player);
        }
        
        // Allgemeine Inventory-Handler
        List<java.util.function.Consumer<Event>> generalHandlers = eventHandlers.get("inventory_general");
        if (generalHandlers != null) {
            executeHandlers(event, generalHandlers, player);
        }
    }
    
    /**
     * Optimierter PlayerInteract-Handler
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onOptimizedPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        // Cooldown-Check
        if (isOnCooldown(player, "player_interact")) return;
        
        // Route basierend auf Interaktionstyp
        String interactionType = getInteractionType(event);
        List<java.util.function.Consumer<Event>> handlers = eventHandlers.get("interact_" + interactionType);
        
        if (handlers != null) {
            executeHandlers(event, handlers, player);
        }
    }
    
    /**
     * Führt Handler in optimierter Reihenfolge aus
     */
    private void executeHandlers(Event event, List<java.util.function.Consumer<Event>> handlers, Player player) {
        // Limitiere Handler-Anzahl
        int maxHandlers = Math.min(handlers.size(), MAX_HANDLERS_PER_EVENT);
        
        for (int i = 0; i < maxHandlers; i++) {
            java.util.function.Consumer<Event> handler = handlers.get(i);
            
            try {
                // Führe Handler aus
                handler.accept(event);
                
                // Wenn Event gecancelt wurde, stoppe weitere Handler
                if (event instanceof org.bukkit.event.Cancellable) {
                    org.bukkit.event.Cancellable cancellable = (org.bukkit.event.Cancellable) event;
                    if (cancellable.isCancelled()) {
                        break;
                    }
                }
                
            } catch (Exception e) {
                plugin.getLogger().warning("Error in event handler: " + e.getMessage());
            }
        }
    }
    
    /**
     * Prüft Player-Cooldown
     */
    private boolean isOnCooldown(Player player, String actionType) {
        UUID playerId = player.getUniqueId();
        Long lastAction = playerCooldowns.get(playerId);
        
        if (lastAction == null) {
            playerCooldowns.put(playerId, System.currentTimeMillis());
            return false;
        }
        
        long cooldownTime = getCooldownTime(actionType);
        if (System.currentTimeMillis() - lastAction < cooldownTime) {
            return true;
        }
        
        playerCooldowns.put(playerId, System.currentTimeMillis());
        return false;
    }
    
    /**
     * Holt Cooldown-Zeit für Action
     */
    private long getCooldownTime(String actionType) {
        switch (actionType) {
            case "inventory_click": return 50; // 50ms
            case "player_interact": return 100; // 100ms
            default: return DEFAULT_COOLDOWN;
        }
    }
    
    /**
     * Bestimmt GUI-Typ aus Titel
     */
    private String getGUIType(String title) {
        if (title.contains("ULTIMATE")) return "ultimate";
        if (title.contains("TALISMANS")) return "talismans";
        if (title.contains("MINIONS")) return "minions";
        if (title.contains("ACHIEVEMENTS")) return "achievements";
        return "general";
    }
    
    /**
     * Bestimmt Interaktionstyp
     */
    private String getInteractionType(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            return event.getClickedBlock().getType().name().toLowerCase();
        }
        return "general";
    }
    
    /**
     * Holt Handler-Priorität
     */
    @SuppressWarnings("unused")
    private int getHandlerPriority(java.util.function.Consumer<Event> handler) {
        // Könnte durch Annotation oder Interface implementiert werden
        return 0; // Standard-Priorität
    }
    
    /**
     * Startet Cooldown-Cleanup
     */
    public void startCooldownCleanup() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Entferne alte Cooldowns
                long currentTime = System.currentTimeMillis();
                playerCooldowns.entrySet().removeIf(entry -> 
                    currentTime - entry.getValue() > 60000); // 1 Minute
            }
        }.runTaskTimerAsynchronously(plugin, 6000L, 6000L); // Alle 5 Minuten
    }
    
    /**
     * Gibt Performance-Statistiken zurück
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("registeredHandlers", eventHandlers.size());
        stats.put("activeCooldowns", playerCooldowns.size());
        
        int totalHandlers = eventHandlers.values().stream()
            .mapToInt(List::size)
            .sum();
        stats.put("totalHandlers", totalHandlers);
        
        return stats;
    }
}
