package de.noctivag.skyblock.levels;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SkyBlock Level System - Hypixel Skyblock Style
 * 
 * Features:
 * - Overall SkyBlock level based on all activities
 * - Level-based rewards and bonuses
 * - Level milestones and achievements
 * - Level-based item requirements
 * - Level GUI interface
 */
public class SkyBlockLevelSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final Map<UUID, PlayerSkyBlockLevel> playerLevels = new ConcurrentHashMap<>();
    
    public SkyBlockLevelSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
        startLevelUpdateTask();
    }
    
    private void startLevelUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerSkyBlockLevel> entry : playerLevels.entrySet()) {
                    PlayerSkyBlockLevel level = entry.getValue();
                    level.update();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L); // Every minute
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerSkyBlockLevel level = getPlayerLevel(player.getUniqueId());
        
        // Send level information
        player.sendMessage("§e§lSkyBlock Level: §6" + level.getLevel());
        player.sendMessage("§7XP: §e" + String.format("%.1f", level.getXP()) + "/" + String.format("%.1f", level.getXPRequiredForNextLevel()));
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material material = event.getBlock().getType();
        
        double xp = getBlockBreakXP(material);
        if (xp > 0) {
            addXP(player, xp, "Block Break");
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            double xp = getCombatXP(event.getEntity().getType().name());
            if (xp > 0) {
                addXP(player, xp, "Combat");
            }
        }
    }
    
    public void addXP(Player player, double xp, String source) {
        PlayerSkyBlockLevel level = getPlayerLevel(player.getUniqueId());
        int oldLevel = level.getLevel();
        
        level.addXP(xp);
        
        int newLevel = level.getLevel();
        if (newLevel > oldLevel) {
            player.sendMessage(Component.text("§a§lSKYBLOCK LEVEL UP!"));
            player.sendMessage("§7Level: §e" + oldLevel + " §7→ §e" + newLevel);
            player.sendMessage("§7Source: §e" + source);
            
            // Apply level rewards
            applyLevelRewards(player, newLevel);
        }
    }
    
    private void applyLevelRewards(Player player, int level) {
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return;
        
        // Level-based coin rewards
        double coinReward = level * 100;
        profile.addCoins(coinReward);
        
        player.sendMessage("§aReward: §6" + coinReward + " coins");
        
        // Special level milestones
        if (level == 10) {
            player.sendMessage(Component.text("§e§lMILESTONE REACHED!"));
            player.sendMessage(Component.text("§7You can now access the Auction House!"));
        } else if (level == 20) {
            player.sendMessage(Component.text("§e§lMILESTONE REACHED!"));
            player.sendMessage(Component.text("§7You can now access Dungeons!"));
        } else if (level == 30) {
            player.sendMessage(Component.text("§e§lMILESTONE REACHED!"));
            player.sendMessage(Component.text("§7You can now access the End!"));
        }
    }
    
    private double getBlockBreakXP(Material material) {
        return switch (material) {
            case WHEAT, CARROT, POTATO -> 2.0;
            case COAL_ORE -> 5.0;
            case IRON_ORE -> 8.0;
            case GOLD_ORE -> 12.0;
            case DIAMOND_ORE -> 20.0;
            case EMERALD_ORE -> 25.0;
            case OAK_LOG, BIRCH_LOG, SPRUCE_LOG -> 3.0;
            case JUNGLE_LOG, ACACIA_LOG, DARK_OAK_LOG -> 5.0;
            default -> 1.0;
        };
    }
    
    private double getCombatXP(String entityType) {
        return switch (entityType) {
            case "ZOMBIE", "SKELETON", "SPIDER" -> 5.0;
            case "CREEPER", "ENDERMAN" -> 10.0;
            case "BLAZE", "GHAST" -> 15.0;
            case "WITHER_SKELETON" -> 25.0;
            case "ENDER_DRAGON" -> 100.0;
            default -> 2.0;
        };
    }
    
    public PlayerSkyBlockLevel getPlayerLevel(UUID playerId) {
        return playerLevels.computeIfAbsent(playerId, k -> new PlayerSkyBlockLevel(playerId));
    }
    
    public int getPlayerLevel(Player player) {
        return getPlayerLevel(player.getUniqueId()).getLevel();
    }
    
    public double getPlayerXP(Player player) {
        return getPlayerLevel(player.getUniqueId()).getXP();
    }
    
    public double getXPRequiredForNextLevel(Player player) {
        return getPlayerLevel(player.getUniqueId()).getXPRequiredForNextLevel();
    }
    
    // Player Level Class
    public static class PlayerSkyBlockLevel {
        private final UUID playerId;
        private double xp;
        private int level;
        private long lastUpdate;
        
        public PlayerSkyBlockLevel(UUID playerId) {
            this.playerId = playerId;
            this.xp = 0.0;
            this.level = 1;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void addXP(double amount) {
            this.xp += amount;
            checkLevelUp();
        }
        
        private void checkLevelUp() {
            double xpRequired = getXPRequiredForLevel(level + 1);
            if (xp >= xpRequired) {
                level++;
                xp -= xpRequired;
                checkLevelUp(); // Check for further level ups
            }
        }
        
        private double getXPRequiredForLevel(int targetLevel) {
            if (targetLevel <= 1) return 0;
            if (targetLevel <= 10) return targetLevel * 100;
            if (targetLevel <= 20) return 1000 + (targetLevel - 10) * 200;
            if (targetLevel <= 30) return 3000 + (targetLevel - 20) * 300;
            if (targetLevel <= 40) return 6000 + (targetLevel - 30) * 400;
            if (targetLevel <= 50) return 10000 + (targetLevel - 40) * 500;
            return 15000 + (targetLevel - 50) * 1000;
        }
        
        public double getXPRequiredForNextLevel() {
            return getXPRequiredForLevel(level + 1);
        }
        
        public void update() {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public double getXP() { return xp; }
        public int getLevel() { return level; }
        public long getLastUpdate() { return lastUpdate; }
    }
}
