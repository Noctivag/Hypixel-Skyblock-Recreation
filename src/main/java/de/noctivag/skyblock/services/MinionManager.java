package de.noctivag.skyblock.services;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.enums.MinionType;
import de.noctivag.skyblock.models.Minion;
import de.noctivag.skyblock.core.PlayerProfile;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manager für Minion-Verwaltung und Offline-Fortschritt
 */
public class MinionManager implements Service {

    private final SkyblockPlugin plugin;
    private final PlayerProfileService playerProfileService;
    private final Map<UUID, List<Minion>> playerMinions;
    private final Map<UUID, Long> lastOfflineCalculation;

    public MinionManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        // TODO: Implement proper service manager integration
        // this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        this.playerProfileService = null; // Placeholder
        this.playerMinions = new ConcurrentHashMap<>();
        this.lastOfflineCalculation = new ConcurrentHashMap<>();
        
        // Starte Minion-Task
        startMinionTask();

        // Initialize service
        status = SystemStatus.RUNNING;
    }

    private SystemStatus status = SystemStatus.DISABLED;

    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        // Service initialization logic here
        status = SystemStatus.RUNNING;
        plugin.getLogger().info("MinionManager initialized.");
    }

    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        // Cleanup resources
        playerMinions.clear();
        lastOfflineCalculation.clear();
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("MinionManager shut down.");
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public String getName() {
        return "MinionManager";
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status != SystemStatus.RUNNING) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }

    /**
     * Startet den Minion-Task für regelmäßige Updates
     */
    private void startMinionTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Map.Entry<UUID, List<Minion>> entry : playerMinions.entrySet()) {
                UUID playerUUID = entry.getKey();
                List<Minion> minions = entry.getValue();
                
                // Prüfe ob Spieler online ist
                Player player = Bukkit.getPlayer(playerUUID);
                if (player != null && player.isOnline()) {
                    // Spieler ist online, aktualisiere Minions
                    updateMinions(minions);
                } else {
                    // Spieler ist offline, berechne Offline-Fortschritt
                    calculateOfflineProgress(playerUUID, minions);
                }
            }
        }, 20L, 20L); // Jede Sekunde
    }

    /**
     * Aktualisiert aktive Minions
     */
    private void updateMinions(List<Minion> minions) {
        for (Minion minion : minions) {
            if (!minion.isActive()) continue;
            
            long timeSinceLastAction = System.currentTimeMillis() - minion.getLastAction();
            if (timeSinceLastAction >= minion.getTimeUntilNextAction()) {
                minion.performAction();
            }
        }
    }

    /**
     * Berechnet Offline-Fortschritt für einen Spieler
     */
    private void calculateOfflineProgress(UUID playerUUID, List<Minion> minions) {
        Long lastCalculation = lastOfflineCalculation.get(playerUUID);
        if (lastCalculation == null) {
            lastCalculation = System.currentTimeMillis();
            lastOfflineCalculation.put(playerUUID, lastCalculation);
        }
        
        long offlineTime = System.currentTimeMillis() - lastCalculation;
        if (offlineTime < 60000) return; // Mindestens 1 Minute offline
        
        for (Minion minion : minions) {
            if (minion.isActive()) {
                minion.calculateOfflineProgress(offlineTime);
            }
        }
        
        lastOfflineCalculation.put(playerUUID, System.currentTimeMillis());
    }

    /**
     * Platziert einen Minion
     */
    public CompletableFuture<Boolean> placeMinion(Player player, MinionType minionType, Location location) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                UUID playerUUID = player.getUniqueId();
                
                // Prüfe ob Spieler bereits zu viele Minions hat
                List<Minion> minions = playerMinions.getOrDefault(playerUUID, new ArrayList<>());
                if (minions.size() >= getMaxMinions(player)) {
                    player.sendMessage("§cDu hast bereits die maximale Anzahl an Minions!");
                    return false;
                }
                
                // Erstelle neuen Minion
                Minion minion = new Minion(minionType, location);
                minions.add(minion);
                playerMinions.put(playerUUID, minions);
                
                // Speichere in PlayerProfile
                // TODO: Fix playerProfileService null check
                PlayerProfile profile = playerProfileService.getCachedProfile(playerUUID);
                if (profile != null) {
                    profile.addMinion(minion);
                    playerProfileService.saveProfile(profile);
                }
                
                player.sendMessage("§aMinion erfolgreich platziert!");
                return true;
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error placing minion: " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Entfernt einen Minion
     */
    public CompletableFuture<Boolean> removeMinion(Player player, UUID minionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                UUID playerUUID = player.getUniqueId();
                List<Minion> minions = playerMinions.get(playerUUID);
                
                if (minions == null) {
                    return false;
                }
                
                Minion minion = minions.stream()
                    .filter(m -> m.getMinionId().equals(minionId))
                    .findFirst()
                    .orElse(null);
                
                if (minion == null) {
                    return false;
                }
                
                // Gib Items aus dem Inventar zurück
                List<ItemStack> items = minion.emptyInventory();
                for (ItemStack item : items) {
                    player.getInventory().addItem(item);
                }
                
                // Entferne Minion
                minions.remove(minion);
                
                // Aktualisiere PlayerProfile
                // TODO: Fix playerProfileService null check
                PlayerProfile profile = playerProfileService.getCachedProfile(playerUUID);
                if (profile != null) {
                    profile.removeMinion(minionId);
                    playerProfileService.saveProfile(profile);
                }
                
                player.sendMessage("§aMinion erfolgreich entfernt!");
                return true;
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error removing minion: " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Sammelt Items von einem Minion
     */
    public CompletableFuture<Boolean> collectMinion(Player player, UUID minionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                UUID playerUUID = player.getUniqueId();
                List<Minion> minions = playerMinions.get(playerUUID);
                
                if (minions == null) {
                    return false;
                }
                
                Minion minion = minions.stream()
                    .filter(m -> m.getMinionId().equals(minionId))
                    .findFirst()
                    .orElse(null);
                
                if (minion == null) {
                    return false;
                }
                
                // Sammle Items
                List<ItemStack> items = minion.emptyInventory();
                if (items.isEmpty()) {
                    player.sendMessage("§eDer Minion hat keine Items zu sammeln!");
                    return false;
                }
                
                // Füge Items zum Spieler-Inventar hinzu
                for (ItemStack item : items) {
                    player.getInventory().addItem(item);
                }
                
                player.sendMessage("§a" + items.size() + " Items gesammelt!");
                return true;
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error collecting minion: " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Upgraded einen Minion
     */
    public CompletableFuture<Boolean> upgradeMinion(Player player, UUID minionId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                UUID playerUUID = player.getUniqueId();
                List<Minion> minions = playerMinions.get(playerUUID);
                
                if (minions == null) {
                    return false;
                }
                
                Minion minion = minions.stream()
                    .filter(m -> m.getMinionId().equals(minionId))
                    .findFirst()
                    .orElse(null);
                
                if (minion == null) {
                    return false;
                }
                
                if (minion.getLevel() >= 11) {
                    player.sendMessage("§cDer Minion hat bereits das maximale Level!");
                    return false;
                }
                
                // Prüfe ob Spieler genug Coins hat
                // TODO: Fix playerProfileService null check
                PlayerProfile profile = playerProfileService.getCachedProfile(playerUUID);
                if (profile == null) {
                    return false;
                }
                
                double upgradeCost = getUpgradeCost(minion.getLevel());
                if (profile.getCoins() < upgradeCost) {
                    player.sendMessage("§cDu hast nicht genug Coins für das Upgrade!");
                    return false;
                }
                
                // Führe Upgrade durch
                profile.removeCoins(upgradeCost);
                minion.setLevel(minion.getLevel() + 1);
                
                // Speichere Änderungen
                playerProfileService.saveProfile(profile);
                
                player.sendMessage("§aMinion auf Level " + minion.getLevel() + " upgegradet!");
                return true;
                
            } catch (Exception e) {
                plugin.getLogger().severe("Error upgrading minion: " + e.getMessage());
                return false;
            }
        });
    }

    /**
     * Gibt die maximale Anzahl an Minions für einen Spieler zurück
     */
    private int getMaxMinions(Player player) {
        // Basis: 5 Minions
        // Jedes 10. Level gibt +1 Minion
        int baseMinions = 5;
        int levelBonus = player.getLevel() / 10;
        return baseMinions + levelBonus;
    }

    /**
     * Berechnet die Upgrade-Kosten für ein Minion-Level
     */
    private double getUpgradeCost(int currentLevel) {
        return Math.pow(2, currentLevel) * 1000; // Exponentiell steigende Kosten
    }

    /**
     * Lädt Minions für einen Spieler
     */
    public void loadPlayerMinions(UUID playerUUID) {
        try {
            // TODO: Fix playerProfileService null check
            PlayerProfile profile = playerProfileService.getCachedProfile(playerUUID);
            if (profile != null) {
                List<Minion> minions = profile.getMinions();
                playerMinions.put(playerUUID, minions);
                lastOfflineCalculation.put(playerUUID, System.currentTimeMillis());
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Error loading player minions: " + e.getMessage());
        }
    }

    /**
     * Entlädt Minions für einen Spieler
     */
    public void unloadPlayerMinions(UUID playerUUID) {
        playerMinions.remove(playerUUID);
        lastOfflineCalculation.remove(playerUUID);
    }

    /**
     * Gibt alle Minions eines Spielers zurück
     */
    public List<Minion> getPlayerMinions(UUID playerUUID) {
        return playerMinions.getOrDefault(playerUUID, new ArrayList<>());
    }

    /**
     * Gibt Statistiken über Minions zurück
     */
    public String getMinionStats() {
        int totalMinions = playerMinions.values().stream()
            .mapToInt(List::size)
            .sum();
        
        return "§6Minion Statistics:\n" +
               "§7Total Minions: §a" + totalMinions + "\n" +
               "§7Active Players: §e" + playerMinions.size();
    }
}
