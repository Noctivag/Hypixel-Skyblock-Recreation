package de.noctivag.skyblock.items;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Booster Cookie System - Hypixel SkyBlock Style
 * 
 * Features:
 * - Booster Cookie consumption
 * - Temporary bonuses
 * - Cookie effects management
 * - Cookie shop integration
 */
public class BoosterCookieSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCookieData> playerCookieData = new ConcurrentHashMap<>();
    private final Map<CookieEffect, CookieEffectConfig> cookieEffects = new HashMap<>();
    
    public BoosterCookieSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeCookieEffects();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeCookieEffects() {
        // Mining Speed Boost
        cookieEffects.put(CookieEffect.MINING_SPEED_BOOST, new CookieEffectConfig(
            "Mining Speed Boost", "§aMining Speed Boost", 
            "§7+25% Mining Speed", 3600, // 1 hour
            Arrays.asList("§7• +25% Mining Speed", "§7• Faster block breaking", "§7• Increased efficiency")
        ));
        
        // Combat Boost
        cookieEffects.put(CookieEffect.COMBAT_BOOST, new CookieEffectConfig(
            "Combat Boost", "§cCombat Boost",
            "§7+20% Combat Damage", 3600,
            Arrays.asList("§7• +20% Combat Damage", "§7• Increased critical hit chance", "§7• Better loot drops")
        ));
        
        // Farming Boost
        cookieEffects.put(CookieEffect.FARMING_BOOST, new CookieEffectConfig(
            "Farming Boost", "§aFarming Boost",
            "§7+30% Farming XP", 3600,
            Arrays.asList("§7• +30% Farming XP", "§7• Double crop drops", "§7• Faster crop growth")
        ));
        
        // Foraging Boost
        cookieEffects.put(CookieEffect.FORAGING_BOOST, new CookieEffectConfig(
            "Foraging Boost", "§2Foraging Boost",
            "§7+25% Foraging XP", 3600,
            Arrays.asList("§7• +25% Foraging XP", "§7• Increased wood drops", "§7• Faster tree chopping")
        ));
        
        // Fishing Boost
        cookieEffects.put(CookieEffect.FISHING_BOOST, new CookieEffectConfig(
            "Fishing Boost", "§bFishing Boost",
            "§7+35% Fishing XP", 3600,
            Arrays.asList("§7• +35% Fishing XP", "§7• Better catch rates", "§7• Rare fish attraction")
        ));
        
        // Magic Find Boost
        cookieEffects.put(CookieEffect.MAGIC_FIND_BOOST, new CookieEffectConfig(
            "Magic Find Boost", "§dMagic Find Boost",
            "§7+50% Magic Find", 3600,
            Arrays.asList("§7• +50% Magic Find", "§7• Better rare drops", "§7• Increased luck")
        ));
        
        // Pet Luck Boost
        cookieEffects.put(CookieEffect.PET_LUCK_BOOST, new CookieEffectConfig(
            "Pet Luck Boost", "§6Pet Luck Boost",
            "§7+25% Pet Luck", 3600,
            Arrays.asList("§7• +25% Pet Luck", "§7• Better pet drops", "§7• Rare pet attraction")
        ));
        
        // Island Size Boost
        cookieEffects.put(CookieEffect.ISLAND_SIZE_BOOST, new CookieEffectConfig(
            "Island Size Boost", "§eIsland Size Boost",
            "§7+50% Island Size", 3600,
            Arrays.asList("§7• +50% Island Size", "§7• More building space", "§7• Expanded borders")
        ));
        
        // Minion Speed Boost
        cookieEffects.put(CookieEffect.MINION_SPEED_BOOST, new CookieEffectConfig(
            "Minion Speed Boost", "§9Minion Speed Boost",
            "§7+20% Minion Speed", 3600,
            Arrays.asList("§7• +20% Minion Speed", "§7• Faster resource collection", "§7• Increased efficiency")
        ));
        
        // Collection Boost
        cookieEffects.put(CookieEffect.COLLECTION_BOOST, new CookieEffectConfig(
            "Collection Boost", "§fCollection Boost",
            "§7+15% Collection XP", 3600,
            Arrays.asList("§7• +15% Collection XP", "§7• Faster collection progress", "§7• Bonus milestones")
        ));
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        Component displayNameComponent = meta.displayName();
        if (displayNameComponent == null) return;
        String displayName = displayNameComponent.toString();
        
        if (displayName.contains("Booster Cookie")) {
            event.setCancelled(true);
            consumeBoosterCookie(player);
        }
    }
    
    public void consumeBoosterCookie(Player player) {
        PlayerCookieData cookieData = getPlayerCookieData(player.getUniqueId());
        
        // Check if player already has active cookie
        if (cookieData.hasActiveCookie()) {
            player.sendMessage(Component.text("§cDu hast bereits einen aktiven Booster Cookie!"));
            return;
        }
        
        // Remove cookie from inventory
        ItemStack cookie = player.getInventory().getItemInMainHand();
        if (cookie.getAmount() > 1) {
            cookie.setAmount(cookie.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
        
        // Activate all cookie effects
        activateCookieEffects(player);
        
        // Set cookie active for 4 days (Hypixel SkyBlock standard)
        cookieData.setCookieActive(true);
        cookieData.setCookieExpiry(java.lang.System.currentTimeMillis() + (4 * 24 * 60 * 60 * 1000L));
        
        // Save to database
        savePlayerCookieData(player.getUniqueId(), cookieData);
        
        // Send success message
        player.sendMessage(Component.text("§a§lBooster Cookie aktiviert!"));
        player.sendMessage(Component.text("§7Du erhältst für 4 Tage:"));
        for (CookieEffect effect : CookieEffect.values()) {
            CookieEffectConfig config = cookieEffects.get(effect);
            player.sendMessage("§7• " + config.getDescription());
        }
        
        // Start effect management
        startEffectManagement(player);
    }
    
    private void activateCookieEffects(Player player) {
        for (CookieEffect effect : CookieEffect.values()) {
            CookieEffectConfig config = cookieEffects.get(effect);
            applyCookieEffect(player, effect, config);
        }
    }
    
    private void applyCookieEffect(Player player, CookieEffect effect, CookieEffectConfig config) {
        // Apply cookie effects - simplified implementation
        // In a full implementation, these would integrate with the respective systems
        switch (effect) {
            case MINING_SPEED_BOOST:
                // Apply mining speed boost - placeholder
                player.sendMessage(Component.text("§aMining Speed Boost activated!"));
                break;
            case COMBAT_BOOST:
                // Apply combat damage boost - placeholder
                player.sendMessage(Component.text("§cCombat Boost activated!"));
                break;
            case FARMING_BOOST:
                // Apply farming XP boost - placeholder
                player.sendMessage(Component.text("§aFarming Boost activated!"));
                break;
            case FORAGING_BOOST:
                // Apply foraging XP boost - placeholder
                player.sendMessage(Component.text("§2Foraging Boost activated!"));
                break;
            case FISHING_BOOST:
                // Apply fishing XP boost - placeholder
                player.sendMessage(Component.text("§bFishing Boost activated!"));
                break;
            case MAGIC_FIND_BOOST:
                // Apply magic find boost - placeholder
                player.sendMessage(Component.text("§dMagic Find Boost activated!"));
                break;
            case PET_LUCK_BOOST:
                // Apply pet luck boost - placeholder
                player.sendMessage(Component.text("§6Pet Luck Boost activated!"));
                break;
            case ISLAND_SIZE_BOOST:
                // Apply island size boost - placeholder
                player.sendMessage(Component.text("§eIsland Size Boost activated!"));
                break;
            case MINION_SPEED_BOOST:
                // Apply minion speed boost - placeholder
                player.sendMessage(Component.text("§9Minion Speed Boost activated!"));
                break;
            case COLLECTION_BOOST:
                // Apply collection XP boost - placeholder
                player.sendMessage(Component.text("§fCollection Boost activated!"));
                break;
        }
    }
    
    private void startEffectManagement(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                
                PlayerCookieData cookieData = getPlayerCookieData(player.getUniqueId());
                
                // Check if cookie expired
                if (!cookieData.hasActiveCookie() || cookieData.isCookieExpired()) {
                    removeCookieEffects(player);
                    cookieData.setCookieActive(false);
                    savePlayerCookieData(player.getUniqueId(), cookieData);
                    
                    player.sendMessage(Component.text("§c§lDein Booster Cookie ist abgelaufen!"));
                    cancel();
                    return;
                }
                
                // Send reminder every hour
                if (cookieData.shouldSendReminder()) {
                    long remainingTime = cookieData.getCookieExpiry() - java.lang.System.currentTimeMillis();
                    long hours = remainingTime / (60 * 60 * 1000);
                    long minutes = (remainingTime % (60 * 60 * 1000)) / (60 * 1000);
                    
                    player.sendMessage("§e§lBooster Cookie: §7Noch " + hours + "h " + minutes + "m aktiv");
                    cookieData.setLastReminder(java.lang.System.currentTimeMillis());
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L); // Check every minute
    }
    
    private void removeCookieEffects(Player player) {
        for (CookieEffect effect : CookieEffect.values()) {
            removeCookieEffect(player, effect);
        }
    }
    
    private void removeCookieEffect(Player player, CookieEffect effect) {
        // Remove cookie effects - simplified implementation
        // In a full implementation, these would integrate with the respective systems
        switch (effect) {
            case MINING_SPEED_BOOST:
                player.sendMessage(Component.text("§7Mining Speed Boost expired!"));
                break;
            case COMBAT_BOOST:
                player.sendMessage(Component.text("§7Combat Boost expired!"));
                break;
            case FARMING_BOOST:
                player.sendMessage(Component.text("§7Farming Boost expired!"));
                break;
            case FORAGING_BOOST:
                player.sendMessage(Component.text("§7Foraging Boost expired!"));
                break;
            case FISHING_BOOST:
                player.sendMessage(Component.text("§7Fishing Boost expired!"));
                break;
            case MAGIC_FIND_BOOST:
                player.sendMessage(Component.text("§7Magic Find Boost expired!"));
                break;
            case PET_LUCK_BOOST:
                player.sendMessage(Component.text("§7Pet Luck Boost expired!"));
                break;
            case ISLAND_SIZE_BOOST:
                player.sendMessage(Component.text("§7Island Size Boost expired!"));
                break;
            case MINION_SPEED_BOOST:
                player.sendMessage(Component.text("§7Minion Speed Boost expired!"));
                break;
            case COLLECTION_BOOST:
                player.sendMessage(Component.text("§7Collection Boost expired!"));
                break;
        }
    }
    
    public ItemStack createBoosterCookie() {
        ItemStack cookie = new ItemStack(Material.COOKIE);
        ItemMeta meta = cookie.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§lBooster Cookie"));
            meta.lore(Arrays.asList(
                Component.text("§7A special cookie that provides"),
                Component.text("§7various temporary bonuses!"),
                Component.text(""),
                Component.text("§7Effects for 4 days:"),
                Component.text("§a• +25% Mining Speed"),
                Component.text("§c• +20% Combat Damage"), 
                Component.text("§a• +30% Farming XP"),
                Component.text("§2• +25% Foraging XP"),
                Component.text("§b• +35% Fishing XP"),
                Component.text("§d• +50% Magic Find"),
                Component.text("§6• +25% Pet Luck"),
                Component.text("§e• +50% Island Size"),
                Component.text("§9• +20% Minion Speed"),
                Component.text("§f• +15% Collection XP"),
                Component.text(""),
                Component.text("§eRight-click to consume!")
            ));
            cookie.setItemMeta(meta);
        }
        
        return cookie;
    }
    
    public PlayerCookieData getPlayerCookieData(UUID playerId) {
        return playerCookieData.computeIfAbsent(playerId, k -> new PlayerCookieData(playerId));
    }
    
    private void savePlayerCookieData(UUID playerId, PlayerCookieData data) {
        // Save to database
        databaseManager.executeUpdate(
            "INSERT INTO player_cookie_data (player_uuid, cookie_active, cookie_expiry, last_reminder) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE cookie_active = VALUES(cookie_active), cookie_expiry = VALUES(cookie_expiry), last_reminder = VALUES(last_reminder)",
            playerId.toString(), data.isCookieActive(), data.getCookieExpiry(), data.getLastReminder()
        );
    }
    
    public enum CookieEffect {
        MINING_SPEED_BOOST, COMBAT_BOOST, FARMING_BOOST, FORAGING_BOOST, 
        FISHING_BOOST, MAGIC_FIND_BOOST, PET_LUCK_BOOST, ISLAND_SIZE_BOOST,
        MINION_SPEED_BOOST, COLLECTION_BOOST
    }
    
    public static class CookieEffectConfig {
        private final String name;
        private final String displayName;
        private final String description;
        private final int duration;
        private final List<String> effects;
        
        public CookieEffectConfig(String name, String displayName, String description, int duration, List<String> effects) {
            this.name = name;
            this.displayName = displayName;
            this.description = description;
            this.duration = duration;
            this.effects = effects;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public int getDuration() { return duration; }
        public List<String> getEffects() { return effects; }
    }
    
    public static class PlayerCookieData {
        private final UUID playerId;
        private boolean cookieActive;
        private long cookieExpiry;
        private long lastReminder;
        
        public PlayerCookieData(UUID playerId) {
            this.playerId = playerId;
            this.cookieActive = false;
            this.cookieExpiry = 0;
            this.lastReminder = 0;
        }
        
        public boolean hasActiveCookie() { return cookieActive && !isCookieExpired(); }
        public boolean isCookieActive() { return cookieActive; }
        public boolean isCookieExpired() { return java.lang.System.currentTimeMillis() > cookieExpiry; }
        public boolean shouldSendReminder() { return java.lang.System.currentTimeMillis() - lastReminder > 3600000; } // 1 hour
        
        public void setCookieActive(boolean active) { this.cookieActive = active; }
        public void setCookieExpiry(long expiry) { this.cookieExpiry = expiry; }
        public void setLastReminder(long reminder) { this.lastReminder = reminder; }
        
        public long getCookieExpiry() { return cookieExpiry; }
        public long getLastReminder() { return lastReminder; }
        
        public UUID getPlayerId() { return playerId; }
    }
}
