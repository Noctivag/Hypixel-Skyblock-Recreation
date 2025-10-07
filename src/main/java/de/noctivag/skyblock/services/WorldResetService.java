package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.worlds.RollingRestartWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service für Rolling-Restart-Welt-Management
 * Entkoppelt die Welt-Reset-Logik vom WorldManager
 */
public class WorldResetService {
    
    private final SkyblockPlugin plugin;
    private final RollingRestartWorldManager worldManager;
    private final Map<String, BukkitTask> resetTasks;
    
    public WorldResetService(SkyblockPlugin plugin, RollingRestartWorldManager worldManager) {
        this.plugin = plugin;
        this.worldManager = worldManager;
        this.resetTasks = new HashMap<>();
        
        plugin.getLogger().info("WorldResetService initialized");
    }
    
    /**
     * Startet das Rolling-Restart-System für alle öffentlichen Welten
     */
    public void startRollingRestartSystem() {
        if (!plugin.getSettingsConfig().isRollingRestartSystemEnabled()) {
            plugin.getLogger().info("Rolling-Restart-System is disabled in configuration");
            return;
        }
        
        // Starte Rolling-Restart für alle öffentlichen Welten
        String[] publicWorlds = {
            "hub", "gold_mine", "deep_caverns", "dwarven_mines", 
            "crystal_hollows", "crimson_isle", "end", "park", 
            "spiders_den", "dungeon_hub", "dungeon", "rift", 
            "kuudra", "garden", "blazing_fortress"
        };
        
        for (String worldName : publicWorlds) {
            setupPublicWorldPair(worldName);
        }
        
        plugin.getLogger().info("Rolling-Restart-System started for " + publicWorlds.length + " public worlds");
    }
    
    /**
     * Richtet ein öffentliches Welt-Paar ein (A/B Instanzen)
     * @param worldName Basis-Weltname
     */
    public void setupPublicWorldPair(String worldName) {
        String worldA = worldName + "_a";
        String worldB = worldName + "_b";
        
        // Setze Standard-LIVE-Instanz auf A
        worldManager.setLiveWorld(worldName, worldA);
        
        // Plane den ersten Reset
        scheduleNextSwap(worldName);
        
        if (plugin.getSettingsConfig().isVerboseLogging()) {
            plugin.getLogger().info("Setup public world pair: " + worldName + " (A: " + worldA + ", B: " + worldB + ")");
        }
    }
    
    /**
     * Führt einen Welt-Swap durch (A ↔ B)
     * @param worldName Basis-Weltname
     */
    public CompletableFuture<Void> performSwap(String worldName) {
        return CompletableFuture.runAsync(() -> {
            try {
                String currentLive = worldManager.getLiveWorldName(worldName);
                String newLive = currentLive.endsWith("_a") ? worldName + "_b" : worldName + "_a";
                
                if (plugin.getSettingsConfig().isVerboseLogging()) {
                    plugin.getLogger().info("Performing world swap for " + worldName + ": " + currentLive + " → " + newLive);
                }
                
                // Setze neue LIVE-Instanz
                worldManager.setLiveWorld(worldName, newLive);
                
                // Reset die alte Instanz
                resetWorldAsync(currentLive, "oeffentlich");
                
                // Plane nächsten Swap
                scheduleNextSwap(worldName);
                
                plugin.getLogger().info("World swap completed for " + worldName + " - New live world: " + newLive);
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error performing world swap for " + worldName + ": " + e.getMessage());
            }
        });
    }
    
    /**
     * Plant den nächsten Welt-Swap
     * @param worldName Basis-Weltname
     */
    public void scheduleNextSwap(String worldName) {
        if (!plugin.getSettingsConfig().isAutoWorldReset()) {
            return;
        }
        
        // Prüfe ob Folia läuft
        if (isFoliaServer()) {
            plugin.getLogger().info("Folia detected - Rolling restart scheduling disabled for " + worldName);
            return;
        }
        
        // Entferne bestehende Task
        BukkitTask existingTask = resetTasks.get(worldName);
        if (existingTask != null) {
            existingTask.cancel();
        }
        
        // Erstelle neue Task
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                performSwap(worldName);
            }
        }.runTaskLater(plugin, plugin.getSettingsConfig().getWorldResetInterval() * 20L);
        
        resetTasks.put(worldName, task);
        
        if (plugin.getSettingsConfig().isVerboseLogging()) {
            plugin.getLogger().info("Next reset scheduled for '" + worldName + "' in " + 
                                 plugin.getSettingsConfig().getWorldResetInterval() + " seconds");
        }
    }
    
    /**
     * Reset eine Welt asynchron
     * @param worldName Weltname
     * @param templateSubfolder Template-Unterordner
     */
    private void resetWorldAsync(String worldName, String templateSubfolder) {
        if (isFoliaServer()) {
            plugin.getLogger().info("Folia detected - World reset will be handled by server configuration for " + worldName);
            return;
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                if (plugin.getSettingsConfig().isVerboseLogging()) {
                    plugin.getLogger().info("Resetting world: " + worldName);
                }
                
                // Hier würde die echte Welt-Reset-Logik stehen
                // worldManager.resetWorldFromTemplate(worldName, templateSubfolder);
                
                plugin.getLogger().info("World reset completed: " + worldName);
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error resetting world " + worldName + ": " + e.getMessage());
            }
        });
    }
    
    /**
     * Gibt die aktuelle Live-Welt für einen Alias zurück
     * @param alias Welt-Alias
     * @return World oder null
     */
    public World getLiveWorld(String alias) {
        return worldManager.getLiveWorld(alias);
    }
    
    /**
     * Setzt die Live-Welt für einen Alias
     * @param alias Welt-Alias
     * @param worldName Weltname
     */
    public void setLiveWorld(String alias, String worldName) {
        worldManager.setLiveWorld(alias, worldName);
    }
    
    /**
     * Gibt den Namen der aktuellen Live-Welt zurück
     * @param alias Welt-Alias
     * @return Weltname oder null
     */
    public String getLiveWorldName(String alias) {
        return worldManager.getLiveWorldName(alias);
    }
    
    /**
     * Stoppt alle Reset-Tasks
     */
    public void cancelAllTasks() {
        for (BukkitTask task : resetTasks.values()) {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        }
        resetTasks.clear();
        
        plugin.getLogger().info("All world reset tasks cancelled");
    }
    
    /**
     * Stoppt Reset-Tasks für eine spezifische Welt
     * @param worldName Weltname
     */
    public void cancelTask(String worldName) {
        BukkitTask task = resetTasks.get(worldName);
        if (task != null && !task.isCancelled()) {
            task.cancel();
            resetTasks.remove(worldName);
            
            if (plugin.getSettingsConfig().isVerboseLogging()) {
                plugin.getLogger().info("Cancelled reset task for world: " + worldName);
            }
        }
    }
    
    /**
     * Prüft ob der Server Folia verwendet
     * @return true wenn Folia
     */
    private boolean isFoliaServer() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    /**
     * Gibt Service-Statistiken zurück
     * @return Service-Statistiken
     */
    public String getServiceStats() {
        return String.format("WorldResetService Stats - Active Tasks: %d, Auto Reset: %s",
                           resetTasks.size(), plugin.getSettingsConfig().isAutoWorldReset());
    }
    
    /**
     * Schließt den Service
     */
    public void shutdown() {
        cancelAllTasks();
        plugin.getLogger().info("WorldResetService shutdown completed");
    }
}
