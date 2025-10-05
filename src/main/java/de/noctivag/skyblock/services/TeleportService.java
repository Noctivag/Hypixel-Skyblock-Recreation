package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

/**
 * Service für alle Teleportations-Anfragen
 * Behandelt Hub-Teleportation, Insel-Teleportation und andere Welt-Wechsel
 */
public class TeleportService {

    private final SkyblockPluginRefactored plugin;
    private final WorldManagementService worldManagementService;

    public TeleportService(SkyblockPluginRefactored plugin, WorldManagementService worldManagementService) {
        this.plugin = plugin;
        this.worldManagementService = worldManagementService;
    }

    /**
     * Teleportiert einen Spieler zum Hub
     * @param player Spieler
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> teleportToHub(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Prüfe ob Spieler bereits im Hub ist
                World currentWorld = player.getWorld();
                if (currentWorld.getName().startsWith("hub")) {
                    player.sendMessage("§eDu bist bereits im Hub!");
                    return true;
                }

                // Teleportiere zum Hub
                return worldManagementService.teleportToHub(player).join();
            } catch (Exception e) {
                plugin.getLogger().severe("Error teleporting player to hub: " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Teleportiert einen Spieler zu seiner privaten Insel
     * @param player Spieler
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> teleportToPrivateIsland(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Prüfe ob Spieler bereits auf seiner Insel ist
                World currentWorld = player.getWorld();
                String islandName = "island_" + player.getUniqueId().toString();
                if (currentWorld.getName().equals(islandName)) {
                    player.sendMessage("§eDu bist bereits auf deiner Insel!");
                    return true;
                }

                // Teleportiere zur privaten Insel
                return worldManagementService.teleportToPrivateIsland(player).join();
            } catch (Exception e) {
                plugin.getLogger().severe("Error teleporting player to private island: " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Teleportiert einen Spieler zu einer öffentlichen Welt
     * @param player Spieler
     * @param worldName Name der Welt
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> teleportToPublicWorld(Player player, String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Hole die aktuelle live-Welt
                World world = worldManagementService.getLiveWorld(worldName).join();
                if (world == null) {
                    player.sendMessage("§cDie Welt " + worldName + " ist nicht verfügbar!");
                    return false;
                }

                // Teleportiere zur Welt
                player.teleport(world.getSpawnLocation());
                player.sendMessage("§aDu wurdest zu " + worldName + " teleportiert!");
                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error teleporting player to public world " + worldName + ": " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Teleportiert einen Spieler zu einem anderen Spieler
     * @param player Spieler der teleportiert wird
     * @param targetPlayer Ziel-Spieler
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> teleportToPlayer(Player player, Player targetPlayer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Prüfe ob Ziel-Spieler online ist
                if (targetPlayer == null || !targetPlayer.isOnline()) {
                    player.sendMessage("§cDer Spieler ist nicht online!");
                    return false;
                }

                // Teleportiere zum Spieler
                player.teleport(targetPlayer.getLocation());
                player.sendMessage("§aDu wurdest zu " + targetPlayer.getName() + " teleportiert!");
                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error teleporting player to another player: " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Teleportiert einen Spieler zu einer spezifischen Location
     * @param player Spieler
     * @param location Ziel-Location
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> teleportToLocation(Player player, Location location) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (location == null || location.getWorld() == null) {
                    player.sendMessage("§cUngültige Ziel-Location!");
                    return false;
                }

                // Teleportiere zur Location
                player.teleport(location);
                return true;
            } catch (Exception e) {
                plugin.getLogger().severe("Error teleporting player to location: " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Teleportiert einen Spieler mit Verzögerung (für /hub, /island etc.)
     * @param player Spieler
     * @param destination Ziel (hub, island, world)
     * @param delay Verzögerung in Sekunden
     * @return CompletableFuture mit Erfolg
     */
    public CompletableFuture<Boolean> teleportWithDelay(Player player, String destination, int delay) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Prüfe ob Spieler bereits teleportiert wird
                if (isPlayerTeleporting(player)) {
                    player.sendMessage("§cDu wirst bereits teleportiert!");
                    return false;
                }

                // Markiere Spieler als teleportierend
                markPlayerTeleporting(player, true);

                // Zeige Countdown
                player.sendMessage("§eTeleportation in " + delay + " Sekunden...");
                
                // Warte die Verzögerung ab
                Thread.sleep(delay * 1000);

                // Prüfe ob Spieler noch online ist
                if (!player.isOnline()) {
                    markPlayerTeleporting(player, false);
                    return false;
                }

                // Führe Teleportation durch
                boolean success = false;
                switch (destination.toLowerCase()) {
                    case "hub":
                        success = teleportToHub(player).join();
                        break;
                    case "island":
                        success = teleportToPrivateIsland(player).join();
                        break;
                    default:
                        success = teleportToPublicWorld(player, destination).join();
                        break;
                }

                // Markiere Spieler als nicht mehr teleportierend
                markPlayerTeleporting(player, false);

                if (success) {
                    player.sendMessage("§aTeleportation erfolgreich!");
                } else {
                    player.sendMessage("§cTeleportation fehlgeschlagen!");
                }

                return success;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                markPlayerTeleporting(player, false);
                return false;
            } catch (Exception e) {
                plugin.getLogger().severe("Error in delayed teleportation: " + e.getMessage());
                markPlayerTeleporting(player, false);
                return false;
            }
        });
    }

    /**
     * Prüft ob ein Spieler gerade teleportiert wird
     * @param player Spieler
     * @return true wenn teleportierend
     */
    private boolean isPlayerTeleporting(Player player) {
        // Einfache Implementierung - in einer echten Anwendung würde man das in einem Cache speichern
        return false;
    }

    /**
     * Markiert einen Spieler als teleportierend oder nicht
     * @param player Spieler
     * @param teleporting true wenn teleportierend
     */
    private void markPlayerTeleporting(Player player, boolean teleporting) {
        // Einfache Implementierung - in einer echten Anwendung würde man das in einem Cache speichern
    }

    /**
     * Teleportiert alle Spieler aus einer Welt zum Hub
     * @param world Welt
     * @return CompletableFuture mit Anzahl der teleportierten Spieler
     */
    public CompletableFuture<Integer> teleportAllPlayersToHub(World world) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int teleportedCount = 0;
                World hubWorld = worldManagementService.getHubWorld().join();
                
                if (hubWorld == null) {
                    plugin.getLogger().warning("Hub world not found, cannot teleport players");
                    return 0;
                }

                for (Player player : world.getPlayers()) {
                    try {
                        player.teleport(hubWorld.getSpawnLocation());
                        player.sendMessage("§eDu wurdest zum Hub teleportiert!");
                        teleportedCount++;
                    } catch (Exception e) {
                        plugin.getLogger().warning("Error teleporting player " + player.getName() + ": " + e.getMessage());
                    }
                }

                return teleportedCount;
            } catch (Exception e) {
                plugin.getLogger().severe("Error teleporting all players to hub: " + e.getMessage());
                return 0;
            }
        });
    }

    /**
     * Gibt Statistiken über Teleportationen zurück
     * @return Statistiken als String
     */
    public String getServiceStats() {
        return "§6Teleport Service Statistics:\n" +
               "§7Service Status: §aActive\n" +
               "§7World Management Service: §a" + (worldManagementService != null ? "Connected" : "Disconnected");
    }
}