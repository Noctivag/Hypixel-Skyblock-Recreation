package de.noctivag.skyblock.worlds;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.utils.FileUtils;
import de.noctivag.skyblock.worlds.generators.VoidGenerator;
import de.noctivag.skyblock.worlds.generators.IslandGenerator;
import de.noctivag.skyblock.worlds.generators.DungeonGenerator;
import de.noctivag.skyblock.worlds.generators.ArenaGenerator;
import de.noctivag.skyblock.worlds.generators.MinigameGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Consumer;
// import org.bukkit.util.TriState; // Not available in this Bukkit version

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * World Manager - Zentrale Verwaltung aller Welten im Multiwelt-System
 *
 * Verantwortlich für:
 * - Automatische Welt-Erstellung
 * - Welt-Validierung
 * - Welt-Konfiguration
 * - Backup und Restore
 * - Performance-Optimierung
 * - Rolling-Restart-System für öffentliche Welten
 * - On-Demand-Loading für private Inseln
 */
public class WorldManager {

    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, WorldConfig> worldConfigs = new HashMap<>();
    private final Map<String, World> managedWorlds = new HashMap<>();
    private boolean isInitialized = false;
    
    // Rolling-Restart-System für öffentliche Welten
    private final Map<String, String> liveWorldAliases = new ConcurrentHashMap<>();
    private final Map<String, BukkitTask> restartTasks = new HashMap<>();
    private final File privateIslandsContainer;

    public WorldManager(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.privateIslandsContainer = new File(SkyblockPlugin.getServer().getWorldContainer(), "private_islands");
        
        if (!privateIslandsContainer.exists()) {
            privateIslandsContainer.mkdirs();
        }
        
        initializeWorldConfigs();
    }

    /**
     * Initialisiert alle Welt-Konfigurationen
     */
    private void initializeWorldConfigs() {
        // Skyblock Welten
        worldConfigs.put("skyblock_hub", new WorldConfig(
            "skyblock_hub",
            "Skyblock Hub",
            WorldType.FLAT,
            new VoidGenerator(),
            true,
            true,
            "Hauptlobby für Skyblock"
        ));

        worldConfigs.put("skyblock_private", new WorldConfig(
            "skyblock_private",
            "Private Islands",
            WorldType.NORMAL,
            new IslandGenerator(),
            true,
            false,
            "Private Spieler-Inseln"
        ));

        worldConfigs.put("skyblock_public", new WorldConfig(
            "skyblock_public",
            "Public Islands",
            WorldType.NORMAL,
            new IslandGenerator(),
            true,
            false,
            "Öffentliche Inseln"
        ));

        worldConfigs.put("skyblock_dungeons", new WorldConfig(
            "skyblock_dungeons",
            "Dungeon Instances",
            WorldType.NORMAL,
            new DungeonGenerator(),
            false,
            false,
            "Dungeon-Instanzen"
        ));

        // Event Welten
        worldConfigs.put("event_arenas", new WorldConfig(
            "event_arenas",
            "Event Arenas",
            WorldType.FLAT,
            new ArenaGenerator(),
            true,
            false,
            "Event-Arenen"
        ));

        // PvP Welten
        worldConfigs.put("pvp_arenas", new WorldConfig(
            "pvp_arenas",
            "PvP Arenas",
            WorldType.FLAT,
            new ArenaGenerator(),
            true,
            false,
            "PvP-Arenen"
        ));

        // Minigame Welten
        worldConfigs.put("minigame_worlds", new WorldConfig(
            "minigame_worlds",
            "Minigame Worlds",
            WorldType.NORMAL,
            new MinigameGenerator(),
            true,
            false,
            "Minigame-Welten"
        ));

        SkyblockPlugin.getLogger().info("Initialized " + worldConfigs.size() + " world configurations");
    }

    /**
     * Initialisiert alle Welten
     */
    public CompletableFuture<Boolean> initializeAllWorlds() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        SkyblockPlugin.getLogger().info("Initializing all worlds...");

        Thread.ofVirtual().start(() -> {
            try {
                final int[] successCount = {0};
                int totalCount = worldConfigs.size();

                for (Map.Entry<String, WorldConfig> entry : worldConfigs.entrySet()) {
                    String worldName = entry.getKey();
                    WorldConfig config = entry.getValue();

                    try {
                        // World creation/load - execute directly on current thread for Folia compatibility
                        World world = initializeWorld(worldName, config);

                        if (world != null) {
                            managedWorlds.put(worldName, world);
                            successCount[0]++;
                            SkyblockPlugin.getLogger().info("Successfully initialized world: " + worldName);
                        } else {
                            SkyblockPlugin.getLogger().warning("Failed to initialize world: " + worldName);
                        }

                    } catch (Exception e) {
                        SkyblockPlugin.getLogger().log(java.util.logging.Level.SEVERE, "Error initializing world: " + worldName, e);
                    }
                }

                // Folia compatibility: Mark as initialized even if no worlds were created
                // since world creation is not supported on Folia servers
                isInitialized = true;

                // Complete the future directly for Folia compatibility
                if (successCount[0] > 0) {
                    SkyblockPlugin.getLogger().info("World initialization completed: " + successCount[0] + "/" + totalCount + " worlds ready");
                } else {
                    SkyblockPlugin.getLogger().warning("World initialization completed: 0/" + totalCount + " worlds ready (Folia compatibility - worlds must be created manually)");
                }
                future.complete(true);

            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(java.util.logging.Level.SEVERE, "Critical error during world initialization", e);
                // Folia compatibility: Mark as initialized even if there was an error
                // to prevent infinite loops
                isInitialized = true;
                future.complete(true);
            }
        });

        return future;
    }

    /**
     * Initialisiert eine einzelne Welt
     */
    private World initializeWorld(String worldName, WorldConfig config) {
        try {
            // Prüfe ob Welt bereits existiert
            World existingWorld = Bukkit.getWorld(worldName);
            if (existingWorld != null) {
                SkyblockPlugin.getLogger().info("World " + worldName + " already exists, loading...");
                return existingWorld;
            }

            // Prüfe ob Welt-Ordner existiert
            File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
            if (worldFolder.exists()) {
                SkyblockPlugin.getLogger().info("World folder for " + worldName + " exists, loading...");
                return loadExistingWorld(worldName, config);
            }

            // Erstelle neue Welt
            SkyblockPlugin.getLogger().info("Creating new world: " + worldName);
            return createNewWorld(worldName, config);

        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to initialize world: " + worldName, e);
            return null;
        }
    }

    /**
     * Lädt eine existierende Welt
     */
    private World loadExistingWorld(String worldName, WorldConfig config) {
        try {
            WorldCreator creator = new WorldCreator(worldName);
            creator.type(config.getWorldType());
            creator.generator(config.getGenerator());

            World world = creator.createWorld();
            if (world != null) {
                configureWorld(world, config);
            }
            return world;
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to load existing world: " + worldName, e);
            return null;
        }
    }

    /**
     * Erstellt eine neue Welt
     */
    private World createNewWorld(String worldName, WorldConfig config) {
        try {
            // Folia compatibility: World creation is not supported on Folia servers
            // Log a warning and return null - worlds will need to be created manually
            SkyblockPlugin.getLogger().warning("World creation not supported on Folia: " + worldName + 
                " - Please create this world manually if needed");
            return null;

        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to create new world: " + worldName, e);
            return null;
        }
    }

    /**
     * Konfiguriert eine Welt nach der Erstellung
     */
    private void configureWorld(World world, WorldConfig config) {
        try {
            // Grundlegende Welt-Einstellungen
            world.setAutoSave(config.isAutoSave());

            // Spawn-Punkt setzen
            if (config.getSpawnLocation() != null) {
                world.setSpawnLocation(config.getSpawnLocation());
            } else {
                world.setSpawnLocation(0, 100, 0);
            }

            // Welt-spezifische Konfiguration
            switch (world.getName()) {
                case "skyblock_hub":
                    configureHubWorld(world);
                    break;
                case "skyblock_private":
                case "skyblock_public":
                    configureIslandWorld(world);
                    break;
                case "skyblock_dungeons":
                    configureDungeonWorld(world);
                    break;
                case "event_arenas":
                case "pvp_arenas":
                    configureArenaWorld(world);
                    break;
                case "minigame_worlds":
                    configureMinigameWorld(world);
                    break;
            }

        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.WARNING, "Failed to configure world: " + world.getName(), e);
        }
    }

    /**
     * Konfiguriert die Hub-Welt
     */
    private void configureHubWorld(World world) {
        world.setSpawnLocation(0, 100, 0);
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, false);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, false);
        world.setTime(6000); // Mittag
    }

    /**
     * Konfiguriert Insel-Welten
     */
    private void configureIslandWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, true);
    }

    /**
     * Konfiguriert Dungeon-Welten
     */
    private void configureDungeonWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, true);
        world.setTime(18000); // Nacht
    }

    /**
     * Konfiguriert Arena-Welten
     */
    private void configureArenaWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, false);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, false);
        world.setTime(6000); // Mittag
    }

    /**
     * Konfiguriert Minigame-Welten
     */
    private void configureMinigameWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, true);
    }

    /**
     * Gibt eine Welt zurück oder erstellt sie falls nötig
     */
    public World getWorld(String worldName) {
        if (!isInitialized) {
            SkyblockPlugin.getLogger().warning("WorldManager not initialized, attempting to get world: " + worldName);
            // Try to get world directly from Bukkit
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                return world;
            }
            // If world doesn't exist, try to create it
            WorldConfig config = worldConfigs.get(worldName);
            if (config != null) {
                SkyblockPlugin.getLogger().info("Creating world " + worldName + " on demand...");
                return initializeWorld(worldName, config);
            }
            return null;
        }

        World world = managedWorlds.get(worldName);
        if (world != null) {
            return world;
        }

        // Versuche Welt zu laden falls sie existiert
        world = Bukkit.getWorld(worldName);
        if (world != null) {
            managedWorlds.put(worldName, world);
            return world;
        }

        // Try to create world if config exists
        WorldConfig config = worldConfigs.get(worldName);
        if (config != null) {
            SkyblockPlugin.getLogger().info("Creating world " + worldName + " on demand...");
            world = initializeWorld(worldName, config);
            if (world != null) {
                managedWorlds.put(worldName, world);
                return world;
            }
        }

        // No fallback - return null if world doesn't exist
        SkyblockPlugin.getLogger().warning("World " + worldName + " not found and cannot be created");
        return null;
    }

    /**
     * Prüft ob eine Welt existiert oder erstellt werden kann
     */
    public boolean worldExists(String worldName) {
        // Check if world is already managed
        if (managedWorlds.containsKey(worldName)) {
            return true;
        }
        
        // Check if world exists in Bukkit
        if (Bukkit.getWorld(worldName) != null) {
            return true;
        }
        
        // Check if we have a configuration for this world (can be created)
        if (worldConfigs.containsKey(worldName)) {
            return true;
        }
        
        return false;
    }

    /**
     * Lädt eine Welt
     */
    public CompletableFuture<World> loadWorld(String worldName) {
        CompletableFuture<World> future = new CompletableFuture<>();

        Thread.ofVirtual().start(() -> {
            try {
                // Perform world lookup/creation directly for Folia compatibility
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
                    // Versuche Welt zu erstellen falls Konfiguration existiert
                    WorldConfig config = worldConfigs.get(worldName);
                    if (config != null) {
                        world = createNewWorld(worldName, config);
                    }
                }

                if (world != null) {
                    managedWorlds.put(worldName, world);
                }

                future.complete(world);

            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to load world: " + worldName, e);
                future.complete(null);
            }
        });

        return future;
    }

    /**
     * Entlädt eine Welt
     */
    public CompletableFuture<Boolean> unloadWorld(String worldName) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Thread.ofVirtual().start(() -> {
            try {
                // Run unload logic directly for Folia compatibility
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    // Prüfe ob Spieler in der Welt sind
                    if (!world.getPlayers().isEmpty()) {
                        SkyblockPlugin.getLogger().warning("Cannot unload world " + worldName + ": Players are still in the world");
                        future.complete(false);
                        return;
                    }

                    // Entlade Welt
                    boolean success = Bukkit.unloadWorld(world, true);
                    if (success) {
                        managedWorlds.remove(worldName);
                        SkyblockPlugin.getLogger().info("Successfully unloaded world: " + worldName);
                    }

                    future.complete(success);
                } else {
                    future.complete(true);
                }

            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Failed to unload world: " + worldName, e);
                future.complete(false);
            }
        });

        return future;
    }

    /**
     * Unloads all managed worlds and cleans up resources.
     */
    public void unloadAll() {
        for (World world : managedWorlds.values()) {
            SkyblockPlugin.getServer().unloadWorld(world, true);
            SkyblockPlugin.getLogger().info("World unloaded: " + world.getName());
        }
        managedWorlds.clear();
        SkyblockPlugin.getLogger().info("All managed worlds have been unloaded.");
    }

    /**
     * Erstellt eine sichere Spawn-Location
     */
    public Location getSafeSpawnLocation(String worldName) {
        World world = getWorld(worldName);
        if (world == null) {
            SkyblockPlugin.getLogger().warning("Cannot get safe spawn location for world: " + worldName + " (world not found)");
            return null;
        }

        Location spawn = world.getSpawnLocation();
        if (spawn == null) {
            SkyblockPlugin.getLogger().warning("Spawn location is null for world: " + worldName);
            return null;
        }

        // Ensure spawn location is safe
        if (spawn.getBlock().getType().isSolid()) {
            // Finde sichere Y-Position
            for (int y = spawn.getBlockY(); y < world.getMaxHeight(); y++) {
                Location testLoc = spawn.clone();
                testLoc.setY(y);
                if (!testLoc.getBlock().getType().isSolid() &&
                    !testLoc.clone().add(0, 1, 0).getBlock().getType().isSolid()) {
                    SkyblockPlugin.getLogger().info("Found safe spawn location for " + worldName + " at Y=" + y);
                    return testLoc;
                }
            }
        }

        SkyblockPlugin.getLogger().info("Using default spawn location for " + worldName + " at " + spawn);
        return spawn;
    }

    /**
     * Gibt alle verwalteten Welten zurück
     */
    public Map<String, World> getManagedWorlds() {
        return new HashMap<>(managedWorlds);
    }

    /**
     * Gibt alle Welt-Konfigurationen zurück
     */
    public Map<String, WorldConfig> getWorldConfigs() {
        return new HashMap<>(worldConfigs);
    }

    /**
     * Prüft ob der WorldManager initialisiert ist
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Schließt den WorldManager
     */
    public void shutdown() {
        SkyblockPlugin.getLogger().info("Shutting down WorldManager...");

        // Speichere alle Welten
        for (World world : managedWorlds.values()) {
            if (world != null) {
                world.save();
            }
        }

        managedWorlds.clear();
        isInitialized = false;

        SkyblockPlugin.getLogger().info("WorldManager shutdown complete");
    }

    // ==================== ROLLING-RESTART-SYSTEM ====================

    /**
     * Richtet ein öffentliches Welt-Paar mit Rolling-Restart ein
     */
    public void setupPublicWorldPair(String alias) {
        String worldNameA = alias + "_a";
        String worldNameB = alias + "_b";

        loadOrCopyWorld(worldNameA, "oeffentlich");
        loadOrCopyWorld(worldNameB, "oeffentlich");

        liveWorldAliases.put(alias, worldNameA);
        SkyblockPlugin.getLogger().info("'" + worldNameA + "' ist jetzt die LIVE-Instanz für den Alias '" + alias + "'.");
        
        scheduleNextSwap(alias);
    }

    /**
     * Führt einen Welt-Swap durch
     */
    private void performSwap(String alias) {
        String currentLiveName = liveWorldAliases.get(alias);
        String nextLiveName = currentLiveName.endsWith("_a") ? alias + "_b" : alias + "_a";
        
        World currentLiveWorld = Bukkit.getWorld(currentLiveName);
        World nextLiveWorld = Bukkit.getWorld(nextLiveName);

        if (currentLiveWorld == null || nextLiveWorld == null) {
            SkyblockPlugin.getLogger().severe("Konnte den Welt-Swap für '" + alias + "' nicht durchführen: Eine der Welten ist nicht geladen!");
            scheduleNextSwap(alias);
            return;
        }

        SkyblockPlugin.getLogger().info("Starte Welt-Swap für '" + alias + "'. Neue LIVE-Instanz: '" + nextLiveName + "'.");

        for (Player player : currentLiveWorld.getPlayers()) {
            player.sendMessage(Component.text("§e[Skyblock] §7Diese Welt wird zurückgesetzt. Du wirst zur neuen Instanz teleportiert."));
            player.teleport(nextLiveWorld.getSpawnLocation());
        }

        liveWorldAliases.put(alias, nextLiveName);
        resetWorldAsync(currentLiveName, "oeffentlich");
        scheduleNextSwap(alias);
    }

    /**
     * Plant den nächsten Welt-Swap
     */
    private void scheduleNextSwap(String alias) {
        if (restartTasks.containsKey(alias)) restartTasks.get(alias).cancel();

        long fourHoursInTicks = 4 * 60 * 60 * 20;
        
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                performSwap(alias);
            }
        }.runTaskLater(SkyblockPlugin, fourHoursInTicks);

        restartTasks.put(alias, task);
        SkyblockPlugin.getLogger().info("Nächster Reset für '" + alias + "' geplant in 4 Stunden.");
    }

    /**
     * Gibt die aktuelle Live-Welt für einen Alias zurück
     */
    public World getLiveWorld(String alias) {
        String liveWorldName = liveWorldAliases.get(alias);
        return liveWorldName != null ? Bukkit.getWorld(liveWorldName) : null;
    }
    
    /**
     * Beendet alle Rolling-Restart-Tasks
     */
    public void cancelAllTasks() {
        restartTasks.values().forEach(BukkitTask::cancel);
        restartTasks.clear();
    }

    // ==================== ON-DEMAND-LOADING FÜR PRIVATE INSELN ====================

    /**
     * Lädt eine private Insel on-demand
     */
    public World loadPrivateIsland(UUID playerUUID) {
        File islandFolder = new File(privateIslandsContainer, playerUUID.toString());
        
        if (!islandFolder.exists()) {
            SkyblockPlugin.getLogger().info("Erstelle neue private Insel für " + playerUUID);
            loadOrCopyWorld(playerUUID.toString(), "privat", "standard_insel", privateIslandsContainer);
        }
        
        return Bukkit.createWorld(new WorldCreator(islandFolder.getName()).generator(new IslandGenerator()));
    }
    
    /**
     * Entlädt eine private Insel
     */
    public void unloadPrivateIsland(UUID playerUUID) {
        String worldName = playerUUID.toString();
        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            if (!world.getPlayers().isEmpty()) {
                SkyblockPlugin.getLogger().warning("Versuch, eine bewohnte Insel zu entladen: " + worldName);
                return;
            }
            world.save();
            Bukkit.unloadWorld(world, true);
            SkyblockPlugin.getLogger().info("Private Insel für " + playerUUID + " gespeichert und entladen.");
        }
    }

    // ==================== HILFSMETHODEN ====================

    /**
     * Lädt oder kopiert eine Welt aus einer Vorlage
     */
    private void loadOrCopyWorld(String worldName, String templateSubfolder, String templateName, File parentContainer) {
        File worldFolder = new File(parentContainer, worldName);
        String templatePath = "vorlagen/" + templateSubfolder + "/" + templateName + ".zip";

        if (!worldFolder.exists()) {
            try (InputStream templateStream = SkyblockPlugin.getResource(templatePath)) {
                if (templateStream == null) {
                    SkyblockPlugin.getLogger().severe("Vorlage nicht gefunden: " + templatePath);
                    return;
                }
                FileUtils.unzip(templateStream, worldFolder);
                SkyblockPlugin.getLogger().info("Welt '" + worldName + "' erfolgreich aus Vorlage erstellt.");
            } catch (IOException e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Fehler beim Erstellen der Welt '" + worldName + "'", e);
            }
        }
    }
    
    /**
     * Überladene Methode für öffentliche Welten
     */
    private void loadOrCopyWorld(String worldName, String templateSubfolder) {
        loadOrCopyWorld(worldName, templateSubfolder, worldName, SkyblockPlugin.getServer().getWorldContainer());
        Bukkit.createWorld(new WorldCreator(worldName));
    }
    
    /**
     * Setzt eine Welt asynchron zurück
     */
    private void resetWorldAsync(String worldName, String templateSubfolder) {
        new BukkitRunnable() {
            @Override
            public void run() {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        World world = Bukkit.getWorld(worldName);
                        if (world != null) Bukkit.unloadWorld(world, false);
                        
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                File worldFolder = new File(SkyblockPlugin.getServer().getWorldContainer(), worldName);
                                FileUtils.deleteDirectory(worldFolder);
                                loadOrCopyWorld(worldName, templateSubfolder);
                                
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Bukkit.createWorld(new WorldCreator(worldName));
                                        SkyblockPlugin.getLogger().info("'" + worldName + "' wurde zurückgesetzt und ist als STANDBY bereit.");
                                    }
                                }.runTask(SkyblockPlugin);
                            }
                        }.runTaskAsynchronously(SkyblockPlugin);
                    }
                }.runTask(SkyblockPlugin);
            }
        }.runTask(SkyblockPlugin);
    }

    /**
     * Welt-Konfiguration Klasse
     */
    public static class WorldConfig {
        private final String name;
        private final String displayName;
        private final WorldType worldType;
        private final ChunkGenerator generator;
        private final boolean autoSave;
        private final boolean keepSpawnInMemory;
        private final String description;
        private Location spawnLocation;

        public WorldConfig(String name, String displayName, WorldType worldType,
                          ChunkGenerator generator, boolean autoSave,
                          boolean keepSpawnInMemory, String description) {
            this.name = name;
            this.displayName = displayName;
            this.worldType = worldType;
            this.generator = generator;
            this.autoSave = autoSave;
            this.keepSpawnInMemory = keepSpawnInMemory;
            this.description = description;
        }

        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public WorldType getWorldType() { return worldType; }
        public ChunkGenerator getGenerator() { return generator; }
        public boolean isAutoSave() { return autoSave; }
        public boolean isKeepSpawnInMemory() { return keepSpawnInMemory; }
        public String getDescription() { return description; }
        public Location getSpawnLocation() { return spawnLocation; }

        public void setSpawnLocation(Location spawnLocation) {
            this.spawnLocation = spawnLocation;
        }
    }
}
