package de.noctivag.skyblock.skyblock;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Health and Mana System - Manages player health and mana
 */
public class HealthManaSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerHealthMana> playerData = new ConcurrentHashMap<>();
    
    public HealthManaSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    /**
     * Initialize player health and mana
     */
    public void initializePlayer(Player player) {
        UUID playerId = player.getUniqueId();
        
        PlayerHealthMana data = playerData.computeIfAbsent(playerId, k -> 
            new PlayerHealthMana(playerId));
        
        // Set initial health and mana
        player.setHealth(20.0);
        player.setFoodLevel(20);
    }
    
    /**
     * Get player health and mana data
     */
    public PlayerHealthMana getPlayerData(UUID playerId) {
        return playerData.get(playerId);
    }
    
    /**
     * Save player data
     */
    public void savePlayerData(UUID playerId) {
        PlayerHealthMana data = playerData.get(playerId);
        if (data != null) {
            // Save to database
            plugin.getLogger().info("Saved health/mana data for: " + playerId);
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        initializePlayer(player);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        savePlayerData(player.getUniqueId());
    }
    
    /**
     * Player Health and Mana data
     */
    public static class PlayerHealthMana {
        private final UUID playerId;
        private double maxHealth;
        private double currentHealth;
        private double maxMana;
        private double currentMana;
        private double manaRegenRate;
        
        public PlayerHealthMana(UUID playerId) {
            this.playerId = playerId;
            this.maxHealth = 100.0;
            this.currentHealth = 100.0;
            this.maxMana = 100.0;
            this.currentMana = 100.0;
            this.manaRegenRate = 1.0;
        }
        
        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        
        public double getMaxHealth() { return maxHealth; }
        public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }
        
        public double getCurrentHealth() { return currentHealth; }
        public void setCurrentHealth(double currentHealth) { this.currentHealth = currentHealth; }
        
        public double getMaxMana() { return maxMana; }
        public void setMaxMana(double maxMana) { this.maxMana = maxMana; }
        
        public double getCurrentMana() { return currentMana; }
        public void setCurrentMana(double currentMana) { this.currentMana = currentMana; }
        
        public double getManaRegenRate() { return manaRegenRate; }
        public void setManaRegenRate(double manaRegenRate) { this.manaRegenRate = manaRegenRate; }
    }
}