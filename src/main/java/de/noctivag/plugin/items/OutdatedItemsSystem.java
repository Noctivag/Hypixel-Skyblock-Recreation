package de.noctivag.plugin.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Outdated Items System - Verwaltet veraltete Items aus Hypixel Skyblock Updates
 * 
 * Features:
 * - Veraltete Items identifizieren
 * - Item-Cooldown-System
 * - Legacy Item Support
 * - Update-Warnungen
 */
public class OutdatedItemsSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<String, OutdatedItem> outdatedItems = new HashMap<>();
    private final Map<UUID, Map<String, Long>> playerCooldowns = new ConcurrentHashMap<>();
    
    public OutdatedItemsSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeOutdatedItems();
    }
    
    /**
     * Initialisiert alle veralteten Items
     */
    private void initializeOutdatedItems() {
        // Veraltete Items aus Hypixel Skyblock Updates
        outdatedItems.put("LEATHER_SNOW_HELMET", new OutdatedItem(
            "Leather Snow Helmet",
            "§c§lOUTDATED",
            "This item is outdated and cannot be used outside of your island.",
            Material.LEATHER_HELMET,
            5000L // 5 Sekunden Cooldown
        ));
        
        outdatedItems.put("ENCHANTED_ARROW", new OutdatedItem(
            "Enchanted Arrow",
            "§c§lOUTDATED",
            "This item is outdated and cannot be used outside of your island.",
            Material.ARROW,
            3000L // 3 Sekunden Cooldown
        ));
        
        outdatedItems.put("OLD_BANNER", new OutdatedItem(
            "Old Banner",
            "§c§lOUTDATED",
            "This item is outdated and cannot be used outside of your island.",
            Material.WHITE_BANNER,
            2000L // 2 Sekunden Cooldown
        ));
        
        outdatedItems.put("LEGACY_SWORD", new OutdatedItem(
            "Legacy Sword",
            "§c§lOUTDATED",
            "This item is outdated and cannot be used outside of your island.",
            Material.IRON_SWORD,
            10000L // 10 Sekunden Cooldown
        ));
        
        outdatedItems.put("OLD_ARMOR", new OutdatedItem(
            "Old Armor",
            "§c§lOUTDATED",
            "This item is outdated and cannot be used outside of your island.",
            Material.IRON_CHESTPLATE,
            8000L // 8 Sekunden Cooldown
        ));
        
        plugin.getLogger().info("§a[OutdatedItems] Initialized " + outdatedItems.size() + " outdated items");
    }
    
    /**
     * Prüft ob ein Item veraltet ist
     */
    public boolean isOutdatedItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
            String displayName = meta.displayName().toString();
            return outdatedItems.values().stream()
                .anyMatch(outdatedItem -> displayName.contains(outdatedItem.getName()));
        }
        
        return false;
    }
    
    /**
     * Gibt ein veraltetes Item zurück
     */
    public OutdatedItem getOutdatedItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
            String displayName = meta.displayName().toString();
            return outdatedItems.values().stream()
                .filter(outdatedItem -> displayName.contains(outdatedItem.getName()))
                .findFirst()
                .orElse(null);
        }
        
        return null;
    }
    
    /**
     * Prüft Cooldown für einen Spieler
     */
    public boolean isOnCooldown(Player player, String itemName) {
        Map<String, Long> cooldowns = playerCooldowns.get(player.getUniqueId());
        if (cooldowns == null) {
            return false;
        }
        
        Long lastUsed = cooldowns.get(itemName);
        if (lastUsed == null) {
            return false;
        }
        
        return System.currentTimeMillis() - lastUsed < getCooldownTime(itemName);
    }
    
    /**
     * Setzt Cooldown für einen Spieler
     */
    public void setCooldown(Player player, String itemName) {
        playerCooldowns.computeIfAbsent(player.getUniqueId(), k -> new ConcurrentHashMap<>())
            .put(itemName, System.currentTimeMillis());
    }
    
    /**
     * Gibt Cooldown-Zeit zurück
     */
    public long getCooldownTime(String itemName) {
        OutdatedItem item = outdatedItems.get(itemName);
        return item != null ? item.getCooldown() : 0L;
    }
    
    /**
     * Gibt verbleibende Cooldown-Zeit zurück
     */
    public long getRemainingCooldown(Player player, String itemName) {
        Map<String, Long> cooldowns = playerCooldowns.get(player.getUniqueId());
        if (cooldowns == null) {
            return 0L;
        }
        
        Long lastUsed = cooldowns.get(itemName);
        if (lastUsed == null) {
            return 0L;
        }
        
        long remaining = getCooldownTime(itemName) - (System.currentTimeMillis() - lastUsed);
        return Math.max(0L, remaining);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !isOutdatedItem(item)) {
            return;
        }
        
        OutdatedItem outdatedItem = getOutdatedItem(item);
        if (outdatedItem == null) {
            return;
        }
        
        // Prüfe Cooldown
        if (isOnCooldown(player, outdatedItem.getName())) {
            long remaining = getRemainingCooldown(player, outdatedItem.getName());
            player.sendMessage(Component.text("§cThis item is on cooldown for " + (remaining / 1000) + " more seconds!")
                .color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }
        
        // Setze Cooldown
        setCooldown(player, outdatedItem.getName());
        
        // Zeige Warnung
        player.sendMessage(Component.text("§c§lWARNING: " + outdatedItem.getWarning())
            .color(NamedTextColor.RED));
        
        // Spiele Sound
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
    }
    
    /**
     * Erstellt ein veraltetes Item
     */
    public ItemStack createOutdatedItem(String itemName) {
        OutdatedItem outdatedItem = outdatedItems.get(itemName);
        if (outdatedItem == null) {
            return null;
        }
        
        ItemStack item = new ItemStack(outdatedItem.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(Component.text(outdatedItem.getDisplayName())
            .color(NamedTextColor.RED));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(outdatedItem.getWarning())
            .color(NamedTextColor.GRAY));
        lore.add(Component.text(""));
        lore.add(Component.text("§7This item cannot be used")
            .color(NamedTextColor.GRAY));
        lore.add(Component.text("§7outside of your island.")
            .color(NamedTextColor.GRAY));
        
        meta.lore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Gibt alle veralteten Items zurück
     */
    public Map<String, OutdatedItem> getOutdatedItems() {
        return new HashMap<>(outdatedItems);
    }
    
    /**
     * Veraltetes Item Datenklasse
     */
    public static class OutdatedItem {
        private final String name;
        private final String displayName;
        private final String warning;
        private final Material material;
        private final long cooldown;
        
        public OutdatedItem(String name, String displayName, String warning, Material material, long cooldown) {
            this.name = name;
            this.displayName = displayName;
            this.warning = warning;
            this.material = material;
            this.cooldown = cooldown;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getWarning() { return warning; }
        public Material getMaterial() { return material; }
        public long getCooldown() { return cooldown; }
    }
}
