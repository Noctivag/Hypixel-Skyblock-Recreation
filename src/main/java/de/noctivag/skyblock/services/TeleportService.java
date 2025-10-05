package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

/**
 * Service für Teleportation-Management
 * Zentralisiert alle Teleportations-Logik
 */
public class TeleportService {
    
    private final SkyblockPlugin plugin;
    private final ServiceManager serviceManager;
    
    public TeleportService(SkyblockPlugin plugin, ServiceManager serviceManager) {
        this.plugin = plugin;
        this.serviceManager = serviceManager;
        
        plugin.getLogger().info("TeleportService initialized");
    }
    
    /**
     * Teleportiert einen Spieler zum Hub
     * @param player Spieler
     * @return CompletableFuture mit Erfolg/Fehler
     */
    public CompletableFuture<Boolean> teleportToHub(Player player) {
        if (player == null) {
            return CompletableFuture.completedFuture(false);
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (plugin.getSettingsConfig().isVerboseLogging()) {
                    plugin.getLogger().info("Attempting to teleport player " + player.getName() + " to Hub...");
                }
                
                // Hole WorldResetService
                WorldResetService worldResetService = serviceManager.getService(WorldResetService.class);
                if (worldResetService == null) {
                    plugin.getLogger().warning("WorldResetService not available for teleportation");
                    return teleportToFallbackHub(player);
                }
                
                // Hole aktuelle Live-Hub-Welt
                World hub = worldResetService.getLiveWorld("hub");
                if (hub != null) {
                    return teleportToWorld(player, hub, "Hub");
                }
                
                // Fallback: Standard-Hub
                return teleportToFallbackHub(player);
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error teleporting player " + player.getName() + " to Hub: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Teleportiert einen Spieler zu einer spezifischen Welt
     * @param player Spieler
     * @param worldName Weltname
     * @return CompletableFuture mit Erfolg/Fehler
     */
    public CompletableFuture<Boolean> teleportToWorld(Player player, String worldName) {
        if (player == null || worldName == null) {
            return CompletableFuture.completedFuture(false);
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    plugin.getLogger().warning("World not found: " + worldName);
                    return false;
                }
                
                return teleportToWorld(player, world, worldName);
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error teleporting player " + player.getName() + " to world " + worldName + ": " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Teleportiert einen Spieler zu einer Welt-Instanz
     * @param player Spieler
     * @param world Welt
     * @param worldDisplayName Anzeigename der Welt
     * @return true wenn erfolgreich
     */
    private boolean teleportToWorld(Player player, World world, String worldDisplayName) {
        try {
            Location spawnLocation = world.getSpawnLocation();
            if (spawnLocation == null) {
                plugin.getLogger().warning("Spawn location not found for world: " + world.getName());
                return false;
            }
            
            // Folia-kompatible Teleportation
            if (isFoliaServer()) {
                return teleportAsync(player, spawnLocation, worldDisplayName);
            } else {
                return teleportSync(player, spawnLocation, worldDisplayName);
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error teleporting to world " + world.getName() + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Asynchrone Teleportation (Folia-kompatibel)
     * @param player Spieler
     * @param location Ziel-Location
     * @param worldDisplayName Anzeigename
     * @return true wenn erfolgreich
     */
    private boolean teleportAsync(Player player, Location location, String worldDisplayName) {
        try {
            player.teleportAsync(location).thenAccept(success -> {
                if (success) {
                    player.sendMessage(Component.text("§aWillkommen in " + worldDisplayName + "!")
                        .color(NamedTextColor.GREEN));
                    
                    if (plugin.getSettingsConfig().isVerboseLogging()) {
                        plugin.getLogger().info("Successfully teleported " + player.getName() + " to " + worldDisplayName);
                    }
                } else {
                    player.sendMessage(Component.text("§cTeleportation fehlgeschlagen!")
                        .color(NamedTextColor.RED));
                    
                    plugin.getLogger().warning("Failed to teleport " + player.getName() + " to " + worldDisplayName);
                }
            });
            
            return true;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error in async teleportation: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Synchrone Teleportation (normale Server)
     * @param player Spieler
     * @param location Ziel-Location
     * @param worldDisplayName Anzeigename
     * @return true wenn erfolgreich
     */
    private boolean teleportSync(Player player, Location location, String worldDisplayName) {
        try {
            boolean success = player.teleport(location);
            
            if (success) {
                player.sendMessage(Component.text("§aWillkommen in " + worldDisplayName + "!")
                    .color(NamedTextColor.GREEN));
                
                if (plugin.getSettingsConfig().isVerboseLogging()) {
                    plugin.getLogger().info("Successfully teleported " + player.getName() + " to " + worldDisplayName);
                }
            } else {
                player.sendMessage(Component.text("§cTeleportation fehlgeschlagen!")
                    .color(NamedTextColor.RED));
                
                plugin.getLogger().warning("Failed to teleport " + player.getName() + " to " + worldDisplayName);
            }
            
            return success;
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error in sync teleportation: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Fallback-Teleportation zum Hub
     * @param player Spieler
     * @return true wenn erfolgreich
     */
    private boolean teleportToFallbackHub(Player player) {
        // Fallback: Hub-A oder Hub-B direkt
        World hubA = Bukkit.getWorld("hub_a");
        if (hubA != null) {
            return teleportToWorld(player, hubA, "Hub (A)");
        }
        
        World hubB = Bukkit.getWorld("hub_b");
        if (hubB != null) {
            return teleportToWorld(player, hubB, "Hub (B)");
        }
        
        // Letzter Fallback: Standard-Welt
        World world = Bukkit.getWorld("world");
        if (world != null) {
            player.sendMessage(Component.text("§eHub nicht verfügbar - Spawn in Standard-Welt")
                .color(NamedTextColor.YELLOW));
            return teleportToWorld(player, world, "Standard-Welt");
        }
        
        player.sendMessage(Component.text("§cKeine Welt verfügbar!")
            .color(NamedTextColor.RED));
        plugin.getLogger().severe("No worlds available for teleportation!");
        
        return false;
    }
    
    /**
     * Teleportiert einen Spieler zu seiner privaten Insel
     * @param player Spieler
     * @return CompletableFuture mit Erfolg/Fehler
     */
    public CompletableFuture<Boolean> teleportToPrivateIsland(Player player) {
        if (player == null) {
            return CompletableFuture.completedFuture(false);
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                String islandWorldName = "island_" + player.getUniqueId().toString().replace("-", "");
                return teleportToWorld(player, islandWorldName).join();
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error teleporting player " + player.getName() + " to private island: " + e.getMessage());
                return false;
            }
        });
    }
    
    /**
     * Teleportiert einen Spieler zu einer öffentlichen Welt
     * @param player Spieler
     * @param worldAlias Welt-Alias
     * @return CompletableFuture mit Erfolg/Fehler
     */
    public CompletableFuture<Boolean> teleportToPublicWorld(Player player, String worldAlias) {
        if (player == null || worldAlias == null) {
            return CompletableFuture.completedFuture(false);
        }
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                WorldResetService worldResetService = serviceManager.getService(WorldResetService.class);
                if (worldResetService == null) {
                    plugin.getLogger().warning("WorldResetService not available for public world teleportation");
                    return false;
                }
                
                World world = worldResetService.getLiveWorld(worldAlias);
                if (world == null) {
                    plugin.getLogger().warning("Public world not found: " + worldAlias);
                    return false;
                }
                
                return teleportToWorld(player, world, worldAlias);
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error teleporting player " + player.getName() + " to public world " + worldAlias + ": " + e.getMessage());
                return false;
            }
        });
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
        return String.format("TeleportService Stats - Folia Compatible: %s, ServiceManager Available: %s",
                           isFoliaServer(), serviceManager != null);
    }
    
    /**
     * Schließt den Service
     */
    public void shutdown() {
        plugin.getLogger().info("TeleportService shutdown completed");
    }
}
