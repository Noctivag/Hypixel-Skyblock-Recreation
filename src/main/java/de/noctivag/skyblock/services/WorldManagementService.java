package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.worlds.RollingRestartWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service für die Verwaltung von Welten und Inseln
 * Kapselt die Logik des Rolling-Restart-Systems und Insel-Ladens
 */
public class WorldManagementService {

    private final SkyblockPluginRefactored plugin;
    private final RollingRestartWorldManager rollingRestartWorldManager;

    public WorldManagementService(SkyblockPluginRefactored plugin, RollingRestartWorldManager rollingRestartWorldManager) {
        this.plugin = plugin;
        this.rollingRestartWorldManager = rollingRestartWorldManager;
    }

    /**
     * Gibt die aktuelle live-Welt für eine öffentliche Welt zurück
     * @param worldName Name der Welt
     * @return Aktuelle live-Welt oder null
     */
    public CompletableFuture<World> getLiveWorld(String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (rollingRestartWorldManager == null) {
                    return Bukkit.getWorld(worldName);
                }

                // TODO: Implement getLiveWorldName method in RollingRestartWorldManager
                // String liveWorldName = rollingRestartWorldManager.getLiveWorldName(worldName);
                // if (liveWorldName != null) {
                //     return Bukkit.getWorld(liveWorldName);
                // }

                return Bukkit.getWorld(worldName);
            } catch (Exception e) {
                plugin.getLogger().severe("Error getting live world for " + worldName + ": " + e.getMessage());
                return null;
            }
        });
    }

    /**
     * Lädt eine private Insel für einen Spieler
     * @param playerUUID Spieler-UUID
     * @return CompletableFuture mit der Insel-Welt
     */
    public CompletableFuture<World> loadPrivateIsland(java.util.UUID playerUUID) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String islandName = "island_" + playerUUID.toString();
                World world = Bukkit.getWorld(islandName);

                if (world == null) {
                    // Insel existiert nicht, erstelle sie
                    world = createPrivateIsland(playerUUID);
                }

                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Loaded private island for player: " + playerUUID);
                }

                return world;
            } catch (Exception e) {
                plugin.getLogger().severe("Error loading private island for " + playerUUID + ": " + e.getMessage());
                return null;
            }
        });
    }

    /**
     * Erstellt eine neue private Insel für einen Spieler
     * @param playerUUID Spieler-UUID
     * @return Erstellte Insel-Welt
     */
    private World createPrivateIsland(java.util.UUID playerUUID) {
        try {
            String islandName = "island_" + playerUUID.toString();
            
            // Erstelle Welt mit VoidGenerator
            org.bukkit.WorldCreator worldCreator = new org.bukkit.WorldCreator(islandName);
            worldCreator.generator(new de.noctivag.skyblock.worlds.generators.VoidGenerator());
            worldCreator.environment(org.bukkit.World.Environment.NORMAL);
            
            World world = worldCreator.createWorld();
            
            if (world != null) {
                // Setze Spawn-Punkt
                Location spawnLocation = new Location(world, 0, 100, 0);
                world.setSpawnLocation(spawnLocation);
                
                if (plugin.getSettingsConfig().isDebugMode()) {
                    plugin.getLogger().info("Created new private island: " + islandName);
                }
            }
            
            return world;
        } catch (Exception e) {
            plugin.getLogger().severe("Error creating private island for " + playerUUID + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Entlädt eine private Insel
     * @param playerUUID Spieler-UUID
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> unloadPrivateIsland(java.util.UUID playerUUID) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String islandName = "island_" + playerUUID.toString();
                World world = Bukkit.getWorld(islandName);

                if (world != null) {
                    // Teleportiere alle Spieler aus der Welt
                    List<Player> players = world.getPlayers();
                    World hubWorld = Bukkit.getWorld("hub_a");
                    
                    if (hubWorld != null) {
                        for (Player player : players) {
                            player.teleport(hubWorld.getSpawnLocation());
                        }
                    }

                    // Entlade die Welt
                    boolean success = Bukkit.unloadWorld(world, true);
                    
                    if (success && plugin.getSettingsConfig().isDebugMode()) {
                        plugin.getLogger().info("Unloaded private island: " + islandName);
                    }
                    
                    return success;
                }

                return true; // Welt existiert nicht, also erfolgreich
            } catch (Exception e) {
                plugin.getLogger().severe("Error unloading private island for " + playerUUID + ": " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Prüft ob eine Welt existiert
     * @param worldName Name der Welt
     * @return true wenn die Welt existiert
     */
    public boolean worldExists(String worldName) {
        return Bukkit.getWorld(worldName) != null;
    }

    /**
     * Gibt alle geladenen Welten zurück
     * @return Liste aller geladenen Welten
     */
    public List<World> getLoadedWorlds() {
        return Bukkit.getWorlds();
    }

    /**
     * Gibt die Hub-Welt zurück
     * @return Hub-Welt oder null
     */
    public CompletableFuture<World> getHubWorld() {
        return getLiveWorld("hub");
    }

    /**
     * Teleportiert einen Spieler zum Hub
     * @param player Spieler
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> teleportToHub(Player player) {
        return getHubWorld().thenApply(hubWorld -> {
            if (hubWorld != null) {
                try {
                    player.teleport(hubWorld.getSpawnLocation());
                    return true;
                } catch (Exception e) {
                    plugin.getLogger().severe("Error teleporting player to hub: " + e.getMessage());
                    return false;
                }
            }
            return false;
        });
    }

    /**
     * Teleportiert einen Spieler zu seiner privaten Insel
     * @param player Spieler
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> teleportToPrivateIsland(Player player) {
        return loadPrivateIsland(player.getUniqueId()).thenApply(islandWorld -> {
            if (islandWorld != null) {
                try {
                    player.teleport(islandWorld.getSpawnLocation());
                    return true;
                } catch (Exception e) {
                    plugin.getLogger().severe("Error teleporting player to private island: " + e.getMessage());
                    return false;
                }
            }
            return false;
        });
    }

    /**
     * Startet einen Rolling-Restart für eine Welt
     * @param worldName Name der Welt
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> startRollingRestart(String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (rollingRestartWorldManager != null) {
                    // TODO: Implement startRollingRestart method in RollingRestartWorldManager
                    // return rollingRestartWorldManager.startRollingRestart(worldName);
                    return true; // Placeholder
                }
                return false;
            } catch (Exception e) {
                plugin.getLogger().severe("Error starting rolling restart for " + worldName + ": " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Gibt Statistiken über die Welt-Verwaltung zurück
     * @return Statistiken als String
     */
    public String getWorldStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("§6World Management Statistics:\n");
        stats.append("§7Loaded Worlds: §a").append(getLoadedWorlds().size()).append("\n");
        
        if (rollingRestartWorldManager != null) {
            stats.append("§7Rolling Restart Manager: §aActive\n");
        } else {
            stats.append("§7Rolling Restart Manager: §cInactive\n");
        }
        
        return stats.toString();
    }
}
