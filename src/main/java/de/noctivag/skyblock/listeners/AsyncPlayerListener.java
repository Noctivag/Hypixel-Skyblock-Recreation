package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.services.PlayerProfileService;
import de.noctivag.skyblock.services.TeleportService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Asynchroner Player Listener für optimierte Login/Logout-Prozesse
 * Eliminiert Server-Lag durch Datenbankzugriffe
 */
public class AsyncPlayerListener implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final PlayerProfileService playerProfileService;
    private final TeleportService teleportService;

    public AsyncPlayerListener(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        this.teleportService = plugin.getServiceManager().getService(TeleportService.class);
    }

    /**
     * Asynchrones Pre-Login Event
     * Lädt das Spielerprofil bereits vor dem Join
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }

        UUID playerUUID = event.getUniqueId();
        
        try {
            // Lade Profil asynchron
            CompletableFuture<PlayerProfile> profileFuture = playerProfileService.loadProfile(playerUUID);
            
            // Warte auf das Laden (mit Timeout)
            PlayerProfile profile = profileFuture.get(5, java.util.concurrent.TimeUnit.SECONDS);
            
            if (profile == null) {
                plugin.getLogger().warning("Failed to load profile for player: " + event.getName());
                // Erstelle neues Profil
                profile = new PlayerProfile(playerUUID);
                playerProfileService.saveProfile(profile);
            }
            
            if (plugin.getSettingsConfig().isVerboseLogging()) {
                plugin.getLogger().info("Pre-loaded profile for player: " + event.getName());
            }
            
        } catch (Exception e) {
            plugin.getLogger().severe("Error in async pre-login for player " + event.getName() + ": " + e.getMessage());
            // Erlaube Login trotz Fehler
        }
    }

    /**
     * Player Join Event
     * Teleportiert den Spieler zum Hub und zeigt Willkommensnachricht
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Verzögerte Ausführung um sicherzustellen, dass der Spieler vollständig geladen ist
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            try {
                // Teleportiere zum Hub
                if (teleportService != null) {
                    teleportService.teleportToHub(player).thenAccept(success -> {
                        if (success) {
                            player.sendMessage("§aWillkommen auf dem Skyblock Server!");
                            player.sendMessage("§eVerwende /hub, /island oder /help für weitere Befehle.");
                        } else {
                            player.sendMessage("§cFehler beim Teleportieren zum Hub!");
                        }
                    });
                }
                
                // Zeige Spielerstatistiken
                showPlayerStats(player);
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error in player join for " + player.getName() + ": " + e.getMessage());
            }
        }, 20L); // 1 Sekunde Verzögerung
    }

    /**
     * Player Quit Event
     * Speichert das Spielerprofil asynchron
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        
        // Speichere Profil asynchron
        CompletableFuture.runAsync(() -> {
            try {
                // Lade aktuelles Profil aus dem Cache
                PlayerProfile profile = playerProfileService.loadProfile(playerUUID).join();
                
                if (profile != null) {
                    // Aktualisiere letzte Aktivität
                    profile.setLastLogout(System.currentTimeMillis());
                    
                    // Speichere Profil
                    playerProfileService.saveProfile(profile).join();
                    
                    if (plugin.getSettingsConfig().isVerboseLogging()) {
                        plugin.getLogger().info("Saved profile for player: " + player.getName());
                    }
                }
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error saving profile for player " + player.getName() + ": " + e.getMessage());
            }
        });
    }

    /**
     * Zeigt Spielerstatistiken an
     */
    private void showPlayerStats(Player player) {
        try {
            PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
            if (profile != null) {
                // Zeige Coins
                player.sendMessage("§6Coins: §e" + String.format("%,.0f", profile.getCoins()));
                
                // Zeige Skill-Level
                player.sendMessage("§aSkills: §7Farming " + profile.getSkillLevel("farming") + 
                                 " §7Mining " + profile.getSkillLevel("mining") + 
                                 " §7Combat " + profile.getSkillLevel("combat"));
                
                // Zeige Collection-Fortschritt
                int totalCollections = profile.getCollections().values().stream()
                    .mapToInt(collection -> collection.getItems().size())
                    .sum();
                player.sendMessage("§bCollections: §7" + totalCollections + " Items gesammelt");
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Error showing player stats for " + player.getName() + ": " + e.getMessage());
        }
    }
}
