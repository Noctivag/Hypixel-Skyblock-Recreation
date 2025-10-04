package de.noctivag.plugin.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
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
 * Item Cooldown System - Verwaltet Cooldowns für Items mit Fähigkeiten
 * 
 * Features:
 * - Item-Fähigkeits-Cooldowns
 * - Visuelle Cooldown-Anzeige
 * - Cooldown-Reduktion durch Items
 * - Cooldown-Reset-Funktionen
 */
public class ItemCooldownSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, Map<String, Long>> playerCooldowns = new ConcurrentHashMap<>();
    private final Map<String, ItemCooldown> itemCooldowns = new HashMap<>();
    
    public ItemCooldownSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeItemCooldowns();
    }
    
    /**
     * Initialisiert alle Item-Cooldowns
     */
    private void initializeItemCooldowns() {
        // Waffen-Cooldowns
        itemCooldowns.put("ASPECT_OF_THE_END", new ItemCooldown(
            "Aspect of the End",
            "Teleport",
            3000L, // 3 Sekunden
            "§7Teleports you 8 blocks ahead"
        ));
        
        itemCooldowns.put("ASPECT_OF_THE_DRAGONS", new ItemCooldown(
            "Aspect of the Dragons",
            "Dragon Rage",
            5000L, // 5 Sekunden
            "§7Deals damage to nearby enemies"
        ));
        
        itemCooldowns.put("HYPERION", new ItemCooldown(
            "Hyperion",
            "Wither Impact",
            2000L, // 2 Sekunden
            "§7Teleports and deals massive damage"
        ));
        
        itemCooldowns.put("VALKYRIE", new ItemCooldown(
            "Valkyrie",
            "Wither Impact",
            2000L, // 2 Sekunden
            "§7Teleports and deals massive damage"
        ));
        
        itemCooldowns.put("SCYLLA", new ItemCooldown(
            "Scylla",
            "Wither Impact",
            2000L, // 2 Sekunden
            "§7Teleports and deals massive damage"
        ));
        
        itemCooldowns.put("ASTRAEA", new ItemCooldown(
            "Astraea",
            "Wither Impact",
            2000L, // 2 Sekunden
            "§7Teleports and deals massive damage"
        ));
        
        // Bogen-Cooldowns
        itemCooldowns.put("JUJU_SHORTBOW", new ItemCooldown(
            "Juju Shortbow",
            "Rapid Fire",
            1000L, // 1 Sekunde
            "§7Fires arrows rapidly"
        ));
        
        itemCooldowns.put("TERMINATOR", new ItemCooldown(
            "Terminator",
            "Snipe",
            1500L, // 1.5 Sekunden
            "§7Fires a powerful arrow"
        ));
        
        // Stab-Cooldowns
        itemCooldowns.put("BONZO_STAFF", new ItemCooldown(
            "Bonzo Staff",
            "Balloon",
            4000L, // 4 Sekunden
            "§7Creates explosive balloons"
        ));
        
        itemCooldowns.put("SPIRIT_SCEPTER", new ItemCooldown(
            "Spirit Scepter",
            "Spirit Beam",
            3000L, // 3 Sekunden
            "§7Fires a spirit beam"
        ));
        
        itemCooldowns.put("FROZEN_SCYTHE", new ItemCooldown(
            "Frozen Scythe",
            "Ice Bolt",
            2500L, // 2.5 Sekunden
            "§7Fires an ice bolt"
        ));
        
        plugin.getLogger().info("§a[ItemCooldown] Initialized " + itemCooldowns.size() + " item cooldowns");
    }
    
    /**
     * Prüft ob ein Item einen Cooldown hat
     */
    public boolean hasCooldown(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
            String displayName = meta.displayName().toString();
            return itemCooldowns.values().stream()
                .anyMatch(cooldown -> displayName.contains(cooldown.getItemName()));
        }
        
        return false;
    }
    
    /**
     * Gibt Item-Cooldown zurück
     */
    public ItemCooldown getItemCooldown(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
            String displayName = meta.displayName().toString();
            return itemCooldowns.values().stream()
                .filter(cooldown -> displayName.contains(cooldown.getItemName()))
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
        
        ItemCooldown itemCooldown = itemCooldowns.get(itemName);
        if (itemCooldown == null) {
            return false;
        }
        
        return System.currentTimeMillis() - lastUsed < itemCooldown.getCooldown();
    }
    
    /**
     * Setzt Cooldown für einen Spieler
     */
    public void setCooldown(Player player, String itemName) {
        playerCooldowns.computeIfAbsent(player.getUniqueId(), k -> new ConcurrentHashMap<>())
            .put(itemName, System.currentTimeMillis());
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
        
        ItemCooldown itemCooldown = itemCooldowns.get(itemName);
        if (itemCooldown == null) {
            return 0L;
        }
        
        long remaining = itemCooldown.getCooldown() - (System.currentTimeMillis() - lastUsed);
        return Math.max(0L, remaining);
    }
    
    /**
     * Reduziert Cooldown für einen Spieler
     */
    public void reduceCooldown(Player player, String itemName, long reduction) {
        Map<String, Long> cooldowns = playerCooldowns.get(player.getUniqueId());
        if (cooldowns == null) {
            return;
        }
        
        Long lastUsed = cooldowns.get(itemName);
        if (lastUsed == null) {
            return;
        }
        
        cooldowns.put(itemName, lastUsed + reduction);
    }
    
    /**
     * Setzt Cooldown zurück
     */
    public void resetCooldown(Player player, String itemName) {
        Map<String, Long> cooldowns = playerCooldowns.get(player.getUniqueId());
        if (cooldowns != null) {
            cooldowns.remove(itemName);
        }
    }
    
    /**
     * Setzt alle Cooldowns zurück
     */
    public void resetAllCooldowns(Player player) {
        playerCooldowns.remove(player.getUniqueId());
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !hasCooldown(item)) {
            return;
        }
        
        ItemCooldown itemCooldown = getItemCooldown(item);
        if (itemCooldown == null) {
            return;
        }
        
        // Prüfe Cooldown
        if (isOnCooldown(player, itemCooldown.getItemName())) {
            long remaining = getRemainingCooldown(player, itemCooldown.getItemName());
            player.sendMessage(Component.text("§c" + itemCooldown.getAbilityName() + " is on cooldown for " + 
                (remaining / 1000) + " more seconds!")
                .color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }
        
        // Setze Cooldown
        setCooldown(player, itemCooldown.getItemName());
        
        // Zeige Fähigkeits-Nachricht
        player.sendMessage(Component.text("§aUsed " + itemCooldown.getAbilityName() + "!")
            .color(NamedTextColor.GREEN));
        
        // Spiele Sound
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }
    
    /**
     * Gibt alle Item-Cooldowns zurück
     */
    public Map<String, ItemCooldown> getItemCooldowns() {
        return new HashMap<>(itemCooldowns);
    }
    
    /**
     * Item-Cooldown Datenklasse
     */
    public static class ItemCooldown {
        private final String itemName;
        private final String abilityName;
        private final long cooldown;
        private final String description;
        
        public ItemCooldown(String itemName, String abilityName, long cooldown, String description) {
            this.itemName = itemName;
            this.abilityName = abilityName;
            this.cooldown = cooldown;
            this.description = description;
        }
        
        public String getItemName() { return itemName; }
        public String getAbilityName() { return abilityName; }
        public long getCooldown() { return cooldown; }
        public String getDescription() { return description; }
    }
}
