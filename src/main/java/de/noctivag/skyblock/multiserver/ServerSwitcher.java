package de.noctivag.skyblock.multiserver;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * System für flüssige Server-Wechsel zwischen Hypixel SkyBlock Servern
 */
public class ServerSwitcher {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final HypixelStyleProxySystem proxySystem;
    private final Map<UUID, ServerSwitchRequest> pendingSwitches = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastSwitchTime = new ConcurrentHashMap<>();
    
    // Cooldown für Server-Wechsel (in Millisekunden)
    private static final long SWITCH_COOLDOWN = 3000; // 3 Sekunden
    
    public ServerSwitcher(SkyblockPlugin SkyblockPlugin, HypixelStyleProxySystem proxySystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.proxySystem = proxySystem;
    }
    
    /**
     * Wechselt einen Spieler zu einem anderen Server
     */
    public CompletableFuture<Boolean> switchPlayerToServer(Player player, String serverType) {
        UUID playerId = player.getUniqueId();
        
        // Prüfe Cooldown
        if (isOnCooldown(playerId)) {
            long remainingTime = getRemainingCooldown(playerId);
            player.sendMessage(Component.text("§cBitte warte noch " + (remainingTime / 1000) + " Sekunden bevor du den Server wechselst!"));
            return CompletableFuture.completedFuture(false);
        }
        
        // Prüfe ob bereits ein Wechsel läuft
        if (pendingSwitches.containsKey(playerId)) {
            player.sendMessage(Component.text("§cDu wechselst bereits den Server! Bitte warte..."));
            return CompletableFuture.completedFuture(false);
        }
        
        // Erstelle Switch-Request
        ServerSwitchRequest request = new ServerSwitchRequest(playerId, serverType, java.lang.System.currentTimeMillis());
        pendingSwitches.put(playerId, request);
        
        // Starte den Wechsel-Prozess
        return performServerSwitch(player, serverType, request);
    }
    
    /**
     * Führt den eigentlichen Server-Wechsel durch
     */
    private CompletableFuture<Boolean> performServerSwitch(Player player, String serverType, ServerSwitchRequest request) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        try {
            // 1. Speichere Spieler-Daten
            savePlayerData(player);
            
            // 2. Zeige Wechsel-Animation
            showSwitchAnimation(player, serverType);
            
            // 3. Finde oder erstelle Server-Instanz
            ServerInstance targetInstance = findOrCreateServerInstance(serverType);
            if (targetInstance == null) {
                player.sendMessage(Component.text("§cServer '" + serverType + "' ist nicht verfügbar!"));
                pendingSwitches.remove(player.getUniqueId());
                future.complete(false);
                return future;
            }
            
            // 4. Teleportiere Spieler
            teleportPlayerToServer(player, targetInstance, future);
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Error during server switch: " + e.getMessage());
            pendingSwitches.remove(player.getUniqueId());
            future.complete(false);
        }
        
        return future;
    }
    
    /**
     * Speichert Spieler-Daten vor dem Wechsel
     */
    private void savePlayerData(Player player) {
        try {
            // Hier würde normalerweise die Spieler-Daten gespeichert werden
            // z.B. Inventar, Position, Statistiken, etc.
            SkyblockPlugin.getLogger().info("Saving player data for: " + player.getName());
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Error saving player data: " + e.getMessage());
        }
    }
    
    /**
     * Zeigt Wechsel-Animation
     */
    private void showSwitchAnimation(Player player, String serverType) {
        // Zeige Wechsel-Nachricht
        player.sendMessage(Component.text("§6§l=== Server-Wechsel ==="));
        player.sendMessage(Component.text("§eWechsle zu: §f" + getServerDisplayName(serverType)));
        player.sendMessage(Component.text("§7Bitte warte einen Moment..."));
        
        // Zeige Fortschrittsbalken
        new BukkitRunnable() {
            int progress = 0;
            final int maxProgress = 20;
            
            @Override
            public void run() {
                if (progress >= maxProgress) {
                    this.cancel();
                    return;
                }
                
                // Erstelle Fortschrittsbalken
                StringBuilder bar = new StringBuilder("§a");
                for (int i = 0; i < progress; i++) {
                    bar.append("█");
                }
                bar.append("§7");
                for (int i = progress; i < maxProgress; i++) {
                    bar.append("█");
                }
                
                player.sendActionBar(Component.text(bar.toString() + " §e" + (progress * 5) + "%"));
                progress++;
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 2L); // Alle 2 Ticks (0.1 Sekunden)
    }
    
    /**
     * Teleportiert Spieler zum Ziel-Server
     */
    private void teleportPlayerToServer(Player player, ServerInstance instance, CompletableFuture<Boolean> future) {
        // Simuliere Teleportation (in echter Implementierung würde hier der Spieler zu einem anderen Server transferiert)
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // Erstelle oder lade Welt
                    World targetWorld = getOrCreateWorld(instance);
                    if (targetWorld == null) {
                        player.sendMessage(Component.text("§cFehler beim Laden der Ziel-Welt!"));
                        pendingSwitches.remove(player.getUniqueId());
                        future.complete(false);
                        return;
                    }
                    
                    // Finde sichere Spawn-Location
                    Location spawnLocation = findSafeSpawnLocation(targetWorld, instance);
                    if (spawnLocation == null) {
                        spawnLocation = targetWorld.getSpawnLocation();
                    }
                    
                    // Teleportiere Spieler
                    player.teleport(spawnLocation);
                    
                    // Lade Spieler-Daten
                    loadPlayerData(player, instance);
                    
                    // Zeige Erfolgs-Nachricht
                    player.sendMessage(Component.text("§a§lErfolgreich gewechselt!"));
                    player.sendMessage(Component.text("§eWillkommen auf: §f" + instance.getType().getDisplayName()));
                    
                    // Aktualisiere Cooldown
                    lastSwitchTime.put(player.getUniqueId(), java.lang.System.currentTimeMillis());
                    
                    // Entferne aus pending switches
                    pendingSwitches.remove(player.getUniqueId());
                    
                    future.complete(true);
                    
                } catch (Exception e) {
                    SkyblockPlugin.getLogger().severe("Error during teleportation: " + e.getMessage());
                    player.sendMessage(Component.text("§cFehler beim Server-Wechsel!"));
                    pendingSwitches.remove(player.getUniqueId());
                    future.complete(false);
                }
            }
        }.runTaskLater(SkyblockPlugin, 20L); // 1 Sekunde Verzögerung
    }
    
    /**
     * Findet oder erstellt eine Server-Instanz
     */
    private ServerInstance findOrCreateServerInstance(String serverType) {
        // Suche nach verfügbarer Instanz
        ServerInstance instance = proxySystem.findAvailableInstance(serverType);
        
        if (instance == null) {
            // Erstelle neue Instanz
            instance = proxySystem.createNewInstance(serverType);
        }
        
        return instance;
    }
    
    /**
     * Holt oder erstellt die Welt für eine Server-Instanz
     */
    private World getOrCreateWorld(ServerInstance instance) {
        try {
            // Prüfe ob Welt bereits existiert
            World world = Bukkit.getWorld(instance.getInstanceId());
            if (world != null) {
                return world;
            }
            
            // Erstelle Welt aus Template
            return proxySystem.getTemplateSystem().createWorldFromTemplate(
                instance.getType().getTypeId(), 
                instance.getInstanceId()
            ).get();
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().severe("Error getting/creating world: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Findet eine sichere Spawn-Location
     */
    private Location findSafeSpawnLocation(World world, ServerInstance instance) {
        try {
            // Verwende WorldManager für sichere Spawn-Location
            // TODO: Implement proper WorldManager interface
            // if (SkyblockPlugin.getWorldManager() != null) {
            //     return ((WorldManager) SkyblockPlugin.getWorldManager()).getSafeSpawnLocation(world.getName());
            // }
            
            // Fallback zu Welt-Spawn
            return world.getSpawnLocation();
            
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Error finding safe spawn location: " + e.getMessage());
            return world.getSpawnLocation();
        }
    }
    
    /**
     * Lädt Spieler-Daten für den neuen Server
     */
    private void loadPlayerData(Player player, ServerInstance instance) {
        try {
            // Hier würde normalerweise die Spieler-Daten für den neuen Server geladen werden
            SkyblockPlugin.getLogger().info("Loading player data for: " + player.getName() + " on server: " + instance.getInstanceId());
        } catch (Exception e) {
            SkyblockPlugin.getLogger().warning("Error loading player data: " + e.getMessage());
        }
    }
    
    /**
     * Gibt den Anzeigenamen für einen Server-Typ zurück
     */
    private String getServerDisplayName(String serverType) {
        return proxySystem.getServerTypes().get(serverType) != null ? 
            proxySystem.getServerTypes().get(serverType).getDisplayName() : serverType;
    }
    
    /**
     * Prüft ob ein Spieler auf Cooldown ist
     */
    private boolean isOnCooldown(UUID playerId) {
        Long lastSwitch = lastSwitchTime.get(playerId);
        if (lastSwitch == null) {
            return false;
        }
        
        return (java.lang.System.currentTimeMillis() - lastSwitch) < SWITCH_COOLDOWN;
    }
    
    /**
     * Gibt die verbleibende Cooldown-Zeit zurück
     */
    private long getRemainingCooldown(UUID playerId) {
        Long lastSwitch = lastSwitchTime.get(playerId);
        if (lastSwitch == null) {
            return 0;
        }
        
        long elapsed = java.lang.System.currentTimeMillis() - lastSwitch;
        return Math.max(0, SWITCH_COOLDOWN - elapsed);
    }
    
    /**
     * Bricht einen laufenden Server-Wechsel ab
     */
    public boolean cancelServerSwitch(UUID playerId) {
        ServerSwitchRequest request = pendingSwitches.remove(playerId);
        if (request != null) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage(Component.text("§cServer-Wechsel abgebrochen!"));
            }
            return true;
        }
        return false;
    }
    
    /**
     * Gibt alle laufenden Server-Wechsel zurück
     */
    public Map<UUID, ServerSwitchRequest> getPendingSwitches() {
        return new HashMap<>(pendingSwitches);
    }
    
    /**
     * Bereinigt abgelaufene Switch-Requests
     */
    public void cleanupExpiredSwitches() {
        long currentTime = java.lang.System.currentTimeMillis();
        long timeout = 30000; // 30 Sekunden Timeout
        
        pendingSwitches.entrySet().removeIf(entry -> {
            if (currentTime - entry.getValue().getRequestTime() > timeout) {
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player != null) {
                    player.sendMessage(Component.text("§cServer-Wechsel ist abgelaufen!"));
                }
                return true;
            }
            return false;
        });
    }
    
    /**
     * Repräsentiert einen Server-Wechsel-Request
     */
    public static class ServerSwitchRequest {
        private final UUID playerId;
        private final String targetServer;
        private final long requestTime;
        
        public ServerSwitchRequest(UUID playerId, String targetServer, long requestTime) {
            this.playerId = playerId;
            this.targetServer = targetServer;
            this.requestTime = requestTime;
        }
        
        public UUID getPlayerId() {
            return playerId;
        }
        
        public String getTargetServer() {
            return targetServer;
        }
        
        public long getRequestTime() {
            return requestTime;
        }
    }
    
    // Missing method for ServerSwitcher
    public Location getSafeSpawnLocation(String serverName) {
        // Placeholder implementation
        World world = Bukkit.getWorld("world");
        if (world != null) {
            return world.getSpawnLocation();
        }
        return null; // TODO: Implement actual safe spawn location logic
    }
}
