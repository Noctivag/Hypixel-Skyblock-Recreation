package de.noctivag.skyblock.performance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * GUIOptimizer - Optimiert GUI-Performance
 * 
 * Features:
 * - GUI-Template-Caching
 * - Lazy Loading
 * - Batch-Updates
 * - Memory-Optimierung
 */
public class GUIOptimizer {
    private final SkyblockPlugin plugin;
    
    // GUI-Template-Cache
    private final Map<String, Inventory> guiTemplates = new ConcurrentHashMap<>();
    private final Map<String, Map<Integer, ItemStack>> itemTemplates = new ConcurrentHashMap<>();
    
    // Performance-Konfiguration
    private final int MAX_CACHED_GUIS = 50;
    private final long CACHE_EXPIRY = 300000; // 5 Minuten
    private final Map<String, Long> cacheTimestamps = new ConcurrentHashMap<>();
    
    public GUIOptimizer(SkyblockPlugin plugin) {
        this.plugin = plugin;
        startCacheCleanup();
    }
    
    /**
     * Erstellt optimiertes GUI mit Caching
     */
    public Inventory createOptimizedGUI(String guiId, String title, int size, Map<Integer, ItemStack> items) {
        // Prüfe Cache zuerst
        String cacheKey = guiId + "_" + size;
        if (isCacheValid(cacheKey)) {
            Inventory cached = guiTemplates.get(cacheKey);
            if (cached != null) {
                return cloneInventory(cached, title);
            }
        }
        
        // Erstelle neues GUI
        Inventory gui = org.bukkit.Bukkit.createInventory(null, size, title);
        
        // Fülle GUI mit Items
        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            gui.setItem(entry.getKey(), entry.getValue());
        }
        
        // Cache das Template
        cacheGUITemplate(cacheKey, gui);
        
        return gui;
    }
    
    /**
     * Erstellt optimiertes Item mit Caching
     */
    public ItemStack createOptimizedItem(String itemId, Material material, String name, List<String> lore) {
        String cacheKey = itemId + "_" + material.name() + "_" + name.hashCode();
        
        if (isCacheValid(cacheKey)) {
            Map<Integer, ItemStack> template = itemTemplates.get(cacheKey);
            if (template != null && !template.isEmpty()) {
                return template.values().iterator().next().clone();
            }
        }
        
        // Erstelle neues Item
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            meta.lore(lore.stream().map(line -> net.kyori.adventure.text.Component.text(line)).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        // Cache das Item
        Map<Integer, ItemStack> itemCache = new HashMap<>();
        itemCache.put(0, item.clone());
        itemTemplates.put(cacheKey, itemCache);
        cacheTimestamps.put(cacheKey, System.currentTimeMillis());
        
        return item;
    }
    
    /**
     * Batch-Update für mehrere GUIs
     */
    public void batchUpdateGUIs(Map<Player, Inventory> guiUpdates) {
        // Gruppiere Updates nach GUI-Typ
        Map<String, List<Player>> groupedUpdates = new HashMap<>();
        
        for (Map.Entry<Player, Inventory> entry : guiUpdates.entrySet()) {
            String guiType = entry.getValue().getType().name();
            groupedUpdates.computeIfAbsent(guiType, k -> new ArrayList<>()).add(entry.getKey());
        }
        
        // Verarbeite Updates in Batches
        for (Map.Entry<String, List<Player>> group : groupedUpdates.entrySet()) {
            List<Player> players = group.getValue();
            
            // Teile große Gruppen auf
            for (int i = 0; i < players.size(); i += 10) {
                List<Player> batch = players.subList(i, Math.min(i + 10, players.size()));
                updateGUIBatch(batch, guiUpdates);
            }
        }
    }
    
    /**
     * Update-GUI-Batch
     */
    private void updateGUIBatch(List<Player> players, Map<Player, Inventory> guiUpdates) {
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : players) {
                    if (player.isOnline()) {
                        Inventory gui = guiUpdates.get(player);
                        if (gui != null) {
                            player.openInventory(gui);
                        }
                    }
                }
            }
        }.runTask(plugin);
    }
    
    /**
     * Cache-GUI-Template
     */
    private void cacheGUITemplate(String cacheKey, Inventory gui) {
        // Prüfe Cache-Limit
        if (guiTemplates.size() >= MAX_CACHED_GUIS) {
            evictOldestTemplate();
        }
        
        guiTemplates.put(cacheKey, cloneInventory(gui, "GUI"));
        cacheTimestamps.put(cacheKey, System.currentTimeMillis());
    }
    
    /**
     * Entfernt ältestes Template
     */
    private void evictOldestTemplate() {
        String oldestKey = null;
        long oldestTime = Long.MAX_VALUE;
        
        for (Map.Entry<String, Long> entry : cacheTimestamps.entrySet()) {
            if (entry.getValue() < oldestTime) {
                oldestTime = entry.getValue();
                oldestKey = entry.getKey();
            }
        }
        
        if (oldestKey != null) {
            guiTemplates.remove(oldestKey);
            cacheTimestamps.remove(oldestKey);
        }
    }
    
    /**
     * Klont Inventory
     */
    private Inventory cloneInventory(Inventory original, String newTitle) {
        Inventory clone = Bukkit.createInventory(null, original.getSize(), newTitle);
        clone.setContents(original.getContents());
        return clone;
    }
    
    /**
     * Prüft Cache-Gültigkeit
     */
    private boolean isCacheValid(String cacheKey) {
        Long timestamp = cacheTimestamps.get(cacheKey);
        if (timestamp == null) return false;
        
        return System.currentTimeMillis() - timestamp < CACHE_EXPIRY;
    }
    
    /**
     * Startet Cache-Cleanup
     */
    private void startCacheCleanup() {
        new org.bukkit.scheduler.BukkitRunnable() {
            @Override
            public void run() {
                // Entferne abgelaufene Caches
                cacheTimestamps.entrySet().removeIf(entry -> 
                    System.currentTimeMillis() - entry.getValue() > CACHE_EXPIRY);
                
                guiTemplates.keySet().removeIf(key -> !cacheTimestamps.containsKey(key));
                itemTemplates.keySet().removeIf(key -> !cacheTimestamps.containsKey(key));
            }
        }.runTaskTimerAsynchronously(plugin, 6000L, 6000L); // Alle 5 Minuten
    }
    
    /**
     * Gibt Performance-Statistiken zurück
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("cachedGUIs", guiTemplates.size());
        stats.put("cachedItems", itemTemplates.size());
        stats.put("cacheHits", "N/A"); // Könnte implementiert werden
        return stats;
    }
}
