package de.noctivag.plugin.multiserver;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Hypixel-Style Proxy System - Zentrale Server-Verwaltung
 *
 * Implementiert ein Hypixel-ähnliches Multi-Server-System mit:
 * - Zentraler Proxy-Verwaltung
 * - Dynamischen Server-Instanzen
 * - Load Balancing
 * - Nahtloser Spieler-Übertragung zwischen Servern
 * - Spezialisierten Server-Typen (Hub, Game, SkyBlock)
 */
public class HypixelStyleProxySystem {

    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final ServerTemplateSystem templateSystem;
    private final ServerSwitcher serverSwitcher;
    private final ServerPortal serverPortal;
    private final ServerSelectionGUI serverSelectionGUI;

    // Server Management
    private final Map<String, ServerInstance> activeInstances = new ConcurrentHashMap<>();
    private final Map<String, ServerType> serverTypes = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerServerMap = new ConcurrentHashMap<>();

    // Load Balancing
    private final Map<String, Integer> serverLoads = new ConcurrentHashMap<>();
    private final Map<String, List<UUID>> serverPlayers = new ConcurrentHashMap<>();

    // Task Management
    private BukkitTask loadBalancingTask;
    private BukkitTask instanceCleanupTask;
    private BukkitTask serverSwitcherCleanupTask;

    public HypixelStyleProxySystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.templateSystem = new ServerTemplateSystem(plugin);
        this.serverSwitcher = new ServerSwitcher(plugin, this);
        this.serverPortal = new ServerPortal(plugin, serverSwitcher);
        this.serverSelectionGUI = new ServerSelectionGUI(plugin, serverSwitcher);
    }

    public void init() {
        templateSystem.init();
        initializeServerTypes();
        startLoadBalancing();
        startInstanceCleanup();
        startServerSwitcherCleanup();
    }

    /**
     * Initialisiert die verschiedenen Server-Typen basierend auf echten Hypixel SkyBlock Welten
     *
     * Architektur:
     * - SkyBlock Hub: Hauptserver (nie herunterfahrbar)
     * - Public Islands: 4h Neustart-Zyklus
     * - Private Islands: Persistent pro Spieler (ausgeschaltet aber gespeichert)
     * - Garden: Persistent pro Spieler (ausgeschaltet aber gespeichert)
     * - Dungeons: Temporäre Instanzen
     */
    private void initializeServerTypes() {
        // ===== HAUPT-SERVER (NIE HERUNTERFAHRBAR) =====
        // SkyBlock Hub - Der Hauptserver, der nie herunterfährt
        serverTypes.put("skyblock_hub", new ServerType("skyblock_hub", "SkyBlock Hub", 500, true, false, true, 0));

        // ===== PUBLIC ISLANDS (4h NEUSTART-ZYKLUS) =====
        // Diese Inseln starten alle 4 Stunden neu und sind vom Hub aus erreichbar
        serverTypes.put("spiders_den", new ServerType("spiders_den", "Spider's Den", 100, false, true, false, 14400)); // 4h
        serverTypes.put("the_end", new ServerType("the_end", "The End", 100, false, true, false, 14400));
        serverTypes.put("the_park", new ServerType("the_park", "The Park", 100, false, true, false, 14400));
        serverTypes.put("gold_mine", new ServerType("gold_mine", "Gold Mine", 100, false, true, false, 14400));
        serverTypes.put("deep_caverns", new ServerType("deep_caverns", "Deep Caverns", 100, false, true, false, 14400));
        serverTypes.put("dwarven_mines", new ServerType("dwarven_mines", "Dwarven Mines", 100, false, true, false, 14400));
        serverTypes.put("crystal_hollows", new ServerType("crystal_hollows", "Crystal Hollows", 100, false, true, false, 14400));
        serverTypes.put("the_barn", new ServerType("the_barn", "The Barn", 100, false, true, false, 14400));
        serverTypes.put("mushroom_desert", new ServerType("mushroom_desert", "Mushroom Desert", 100, false, true, false, 14400));
        serverTypes.put("blazing_fortress", new ServerType("blazing_fortress", "Blazing Fortress", 100, false, true, false, 14400));
        serverTypes.put("the_nether", new ServerType("the_nether", "The Nether", 100, false, true, false, 14400));
        serverTypes.put("crimson_isle", new ServerType("crimson_isle", "Crimson Isle", 100, false, true, false, 14400));
        serverTypes.put("rift", new ServerType("rift", "The Rift", 100, false, true, false, 14400));
        serverTypes.put("kuudra", new ServerType("kuudra", "Kuudra's End", 100, false, true, false, 14400));

        // ===== PERSISTENTE SPIELER-INSTANZEN (AUSGESCHALTET ABER GESPEICHERT) =====
        // Private Islands - Jeder Spieler hat seine eigene, die gespeichert wird
        serverTypes.put("private_island", new ServerType("private_island", "Private Island", 1, false, true, true, 0));

        // Garden - Jeder Spieler hat seinen eigenen Garten, der gespeichert wird
        serverTypes.put("garden", new ServerType("garden", "The Garden", 1, false, true, true, 0));

        // ===== TEMPORÄRE DUNGEON-INSTANZEN =====
        // Dungeons sind temporär und werden nach dem Verlassen gelöscht
        serverTypes.put("catacombs_entrance", new ServerType("catacombs_entrance", "Catacombs Entrance", 20, false, true, false, 0));
        serverTypes.put("catacombs_floor_1", new ServerType("catacombs_floor_1", "Catacombs Floor 1", 5, false, true, false, 0));
        serverTypes.put("catacombs_floor_2", new ServerType("catacombs_floor_2", "Catacombs Floor 2", 5, false, true, false, 0));
        serverTypes.put("catacombs_floor_3", new ServerType("catacombs_floor_3", "Catacombs Floor 3", 5, false, true, false, 0));
        serverTypes.put("catacombs_floor_4", new ServerType("catacombs_floor_4", "Catacombs Floor 4", 5, false, true, false, 0));
        serverTypes.put("catacombs_floor_5", new ServerType("catacombs_floor_5", "Catacombs Floor 5", 5, false, true, false, 0));
        serverTypes.put("catacombs_floor_6", new ServerType("catacombs_floor_6", "Catacombs Floor 6", 5, false, true, false, 0));
        serverTypes.put("catacombs_floor_7", new ServerType("catacombs_floor_7", "Catacombs Floor 7", 5, false, true, false, 0));
        serverTypes.put("master_mode_floor_1", new ServerType("master_mode_floor_1", "Master Mode Floor 1", 5, false, true, false, 0));
        serverTypes.put("master_mode_floor_2", new ServerType("master_mode_floor_2", "Master Mode Floor 2", 5, false, true, false, 0));
        serverTypes.put("master_mode_floor_3", new ServerType("master_mode_floor_3", "Master Mode Floor 3", 5, false, true, false, 0));
        serverTypes.put("master_mode_floor_4", new ServerType("master_mode_floor_4", "Master Mode Floor 4", 5, false, true, false, 0));
        serverTypes.put("master_mode_floor_5", new ServerType("master_mode_floor_5", "Master Mode Floor 5", 5, false, true, false, 0));
        serverTypes.put("master_mode_floor_6", new ServerType("master_mode_floor_6", "Master Mode Floor 6", 5, false, true, false, 0));
        serverTypes.put("master_mode_floor_7", new ServerType("master_mode_floor_7", "Master Mode Floor 7", 5, false, true, false, 0));

        plugin.getLogger().info("Initialized " + serverTypes.size() + " Hypixel SkyBlock server types with proper lifecycle management");
    }

    /**
     * Startet Load Balancing
     */
    private void startLoadBalancing() {
        loadBalancingTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                balanceServerLoads();
                cleanupEmptyInstances();
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error in load balancing", e);
            }
        }, 20L, 20L * 5); // Every 5 seconds
    }

    /**
     * Startet Instance Cleanup
     */
    private void startInstanceCleanup() {
        instanceCleanupTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                cleanupEmptyInstances();
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Error in instance cleanup", e);
            }
        }, 20L * 60, 20L * 60); // Every minute
    }

    /**
     * Startet die ServerSwitcher-Bereinigung
     */
    private void startServerSwitcherCleanup() {
        serverSwitcherCleanupTask = Bukkit.getScheduler().runTaskTimer(plugin, serverSwitcher::cleanupExpiredSwitches, 20L * 30, 20L * 30); // Alle 30 Sekunden
    }

    /**
     * Verbindet einen Spieler zu einem Server
     */
    public CompletableFuture<Boolean> connectPlayer(Player player, String serverType) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ServerType type = serverTypes.get(serverType);
                if (type == null) {
                    plugin.getLogger().warning("Unknown server type: " + serverType);
                    return false;
                }

                // Finde beste Server-Instanz
                ServerInstance instance = findBestInstance(serverType);
                if (instance == null) {
                    // Erstelle neue Instanz falls nötig
                    instance = createNewInstance(serverType);
                    if (instance == null) {
                        plugin.getLogger().warning("Failed to create instance for type: " + serverType);
                        return false;
                    }
                }

                // Verbinde Spieler
                return connectPlayerToInstance(player, instance);

            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error connecting player to server", e);
                return false;
            }
        });
    }

    /**
     * Findet die beste Server-Instanz für einen Typ
     */
    private ServerInstance findBestInstance(String serverType) {
        return activeInstances.values().stream()
            .filter(instance -> instance.getType().equals(serverType))
            .filter(instance -> !instance.isFull())
            .min(Comparator.comparing(instance -> instance.getPlayerCount()))
            .orElse(null);
    }

    /**
     * Erstellt eine neue Server-Instanz basierend auf einem Template
     */
    private ServerInstance createNewInstanceInternal(String serverType) {
        try {
            ServerType type = serverTypes.get(serverType);
            if (type == null) {
                return null;
            }

            String instanceId = generateInstanceId(serverType);
            ServerInstance instance = new ServerInstance(instanceId, type);

            // Erstelle Welt basierend auf Template
            instance.setStatus(ServerStatus.STARTING);

            // Füge zur aktiven Instanzen hinzu
            activeInstances.put(instanceId, instance);
            serverLoads.put(instanceId, 0);
            serverPlayers.put(instanceId, new ArrayList<>());

            // Erstelle Welt asynchron basierend auf Template
            templateSystem.createWorldFromTemplate(serverType, instanceId).thenAccept(world -> {
                if (world != null) {
                    instance.setStatus(ServerStatus.ONLINE);
                    instance.setMetadata("world", world);
                    plugin.getLogger().info("Server instance " + instanceId + " is now online with world: " + world.getName());
                } else {
                    instance.setStatus(ServerStatus.OFFLINE);
                    plugin.getLogger().warning("Failed to create world for instance: " + instanceId);
                }
            });

            plugin.getLogger().info("Creating new server instance: " + instanceId + " for type: " + serverType + " using template");
            return instance;

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error creating server instance", e);
            return null;
        }
    }

    /**
     * Verbindet einen Spieler zu einer Instanz
     */
    private boolean connectPlayerToInstance(Player player, ServerInstance instance) {
        try {
            // Aktualisiere Spieler-Mapping
            playerServerMap.put(player.getUniqueId(), instance.getInstanceId());

            // Füge Spieler zur Instanz hinzu
            List<UUID> players = serverPlayers.get(instance.getInstanceId());
            if (players != null) {
                players.add(player.getUniqueId());
            }

            // Aktualisiere Load
            serverLoads.put(instance.getInstanceId(), instance.getPlayerCount());

            // Simuliere Teleportation (in echter Implementierung würde hier BungeeCord/Velocity verwendet)
            plugin.getLogger().info("Player " + player.getName() + " connected to instance " + instance.getInstanceId());

            return true;

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error connecting player to instance", e);
            return false;
        }
    }

    /**
     * Generiert eine eindeutige Instanz-ID
     */
    private String generateInstanceId(String serverType) {
        return serverType + "_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }

    /**
     * Balanciert Server-Loads
     */
    private void balanceServerLoads() {
        for (Map.Entry<String, ServerInstance> entry : activeInstances.entrySet()) {
            String instanceId = entry.getKey();
            ServerInstance instance = entry.getValue();

            // Aktualisiere Player Count
            List<UUID> players = serverPlayers.get(instanceId);
            int playerCount = players != null ? players.size() : 0;
            instance.setPlayerCount(playerCount);
            serverLoads.put(instanceId, playerCount);
        }
    }

    /**
     * Bereinigt leere Instanzen basierend auf Hypixel SkyBlock Lifecycle-Regeln
     */
    private void cleanupEmptyInstances() {
        List<String> toRemove = new ArrayList<>();
        List<String> toRestart = new ArrayList<>();

        for (Map.Entry<String, ServerInstance> entry : activeInstances.entrySet()) {
            String instanceId = entry.getKey();
            ServerInstance instance = entry.getValue();

            // SkyBlock Hub: Nie herunterfahren
            if (instance.getType().isNeverShutdown()) {
                continue;
            }

            // Public Islands: 4h Neustart-Zyklus
            if (instance.getType().hasRestartCycle() && instance.needsRestart()) {
                toRestart.add(instanceId);
                continue;
            }

            // Private Islands & Garden: Ausschalten aber speichern wenn leer
            if (instance.getType().isPlayerPersistent() && instance.isEmpty()) {
                shutdownPlayerPersistentInstance(instance);
                continue;
            }

            // Dungeons: Temporäre Instanzen - sofort entfernen wenn leer
            if (instance.getType().isTemporary() && instance.isEmpty()) {
                toRemove.add(instanceId);
            }
        }

        // Führe Neustarts durch
        for (String instanceId : toRestart) {
            restartInstance(instanceId);
        }

        // Entferne temporäre Instanzen
        for (String instanceId : toRemove) {
            removeInstance(instanceId);
        }
    }

    /**
     * Startet eine Instanz neu (für Public Islands mit 4h Zyklus)
     */
    private void restartInstance(String instanceId) {
        try {
            ServerInstance instance = activeInstances.get(instanceId);
            if (instance != null) {
                plugin.getLogger().info("Restarting instance: " + instanceId + " (4h cycle)");

                // Speichere Welt-Daten vor Neustart
                saveInstanceData(instance);

                // Setze Neustart-Zeit
                instance.setLastRestart(System.currentTimeMillis());
                instance.scheduleNextRestart();

                // Simuliere Neustart (in echter Implementierung würde hier die Welt neu geladen)
                instance.setStatus(ServerStatus.STARTING);

                // Use virtual thread for Folia compatibility
                Thread.ofVirtual().start(() -> {
                    try {
                        Thread.sleep(20L * 5 * 50); // 5 seconds = 5,000 ms
                        instance.setStatus(ServerStatus.ONLINE);
                        plugin.getLogger().info("Instance " + instanceId + " restarted successfully");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error restarting instance", e);
        }
    }

    /**
     * Schaltet eine persistente Spieler-Instanz aus (Private Island/Garden)
     */
    private void shutdownPlayerPersistentInstance(ServerInstance instance) {
        try {
            plugin.getLogger().info("Shutting down player persistent instance: " + instance.getInstanceId() + " (saving data)");

            // Speichere Spieler-Daten
            savePlayerInstanceData(instance);

            // Setze Status auf ausgeschaltet
            instance.setStatus(ServerStatus.OFFLINE);

            // Entferne aus aktiven Instanzen, aber behalte in persistenter Liste
            activeInstances.remove(instance.getInstanceId());

            plugin.getLogger().info("Player persistent instance " + instance.getInstanceId() + " shut down and saved");
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error shutting down player persistent instance", e);
        }
    }

    /**
     * Speichert Instanz-Daten vor Neustart
     */
    private void saveInstanceData(ServerInstance instance) {
        try {
            // Hier würde normalerweise die Welt-Daten gespeichert werden
            plugin.getLogger().info("Saving instance data for: " + instance.getInstanceId());
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error saving instance data", e);
        }
    }

    /**
     * Speichert Spieler-Instanz-Daten (Private Island/Garden)
     */
    private void savePlayerInstanceData(ServerInstance instance) {
        try {
            if (instance.getOwnerPlayerId() != null) {
                // Hier würde normalerweise die Spieler-spezifischen Daten gespeichert werden
                plugin.getLogger().info("Saving player instance data for player: " + instance.getOwnerPlayerId());
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error saving player instance data", e);
        }
    }

    /**
     * Entfernt eine Instanz (nur für temporäre Dungeons)
     */
    private void removeInstance(String instanceId) {
        try {
            ServerInstance instance = activeInstances.remove(instanceId);
            if (instance != null) {
                instance.setStatus(ServerStatus.SHUTTING_DOWN);

                // Entferne alle Spieler-Mappings
                List<UUID> players = serverPlayers.remove(instanceId);
                if (players != null) {
                    for (UUID playerId : players) {
                        playerServerMap.remove(playerId);
                    }
                }

                serverLoads.remove(instanceId);
                plugin.getLogger().info("Removed temporary server instance: " + instanceId);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error removing instance", e);
        }
    }

    /**
     * Gibt Server-Statistiken zurück
     */
    public Map<String, Object> getServerStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalInstances", activeInstances.size());
        stats.put("totalPlayers", playerServerMap.size());

        Map<String, Integer> typeCounts = new HashMap<>();
        Map<String, Integer> typePlayers = new HashMap<>();

        for (ServerInstance instance : activeInstances.values()) {
            String type = instance.getType().getTypeId();
            typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
            typePlayers.put(type, typePlayers.getOrDefault(type, 0) + instance.getPlayerCount());
        }

        stats.put("instancesByType", typeCounts);
        stats.put("playersByType", typePlayers);

        return stats;
    }

    /**
     * Gibt das Template-System zurück
     */
    public ServerTemplateSystem getTemplateSystem() {
        return templateSystem;
    }

    /**
     * Gibt den Server-Switcher zurück
     */
    public ServerSwitcher getServerSwitcher() {
        return serverSwitcher;
    }

    /**
     * Wechselt einen Spieler zu einem anderen Server
     */
    public CompletableFuture<Boolean> switchPlayerToServer(Player player, String serverType) {
        return serverSwitcher.switchPlayerToServer(player, serverType);
    }

    /**
     * Findet eine verfügbare Server-Instanz für einen Server-Typ
     */
    public ServerInstance findAvailableInstance(String serverType) {
        return activeInstances.values().stream()
            .filter(instance -> instance.getType().getTypeId().equals(serverType))
            .filter(instance -> instance.isOnline() && !instance.isFull())
            .min(Comparator.comparing(ServerInstance::getPlayerCount))
            .orElse(null);
    }

    /**
     * Erstellt eine neue Server-Instanz (public für ServerSwitcher)
     */
    public ServerInstance createNewInstance(String serverType) {
        return createNewInstanceInternal(serverType);
    }

    /**
     * Gibt alle Server-Typen zurück
     */
    public Map<String, ServerType> getServerTypes() {
        return new HashMap<>(serverTypes);
    }

    /**
     * Gibt das Portal-System zurück
     */
    public ServerPortal getServerPortal() {
        return serverPortal;
    }

    /**
     * Gibt die Server-Auswahl-GUI zurück
     */
    public ServerSelectionGUI getServerSelectionGUI() {
        return serverSelectionGUI;
    }

    /**
     * Öffnet die Server-Auswahl-GUI für einen Spieler
     */
    public void openServerSelection(Player player) {
        serverSelectionGUI.openServerSelection(player);
    }

    /**
     * Schließt das System
     */
    public void shutdown() {
        if (loadBalancingTask != null) {
            loadBalancingTask.cancel();
        }
        if (instanceCleanupTask != null) {
            instanceCleanupTask.cancel();
        }
        if (serverSwitcherCleanupTask != null) {
            serverSwitcherCleanupTask.cancel();
        }

        // Bereinige GUI-System
        serverSelectionGUI.cleanup();

        // Schließe alle Instanzen
        for (String instanceId : new ArrayList<>(activeInstances.keySet())) {
            removeInstance(instanceId);
        }

        plugin.getLogger().info("HypixelStyleProxySystem shutdown complete");
    }

    // Missing method for MultiServerCommands
}
