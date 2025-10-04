package de.noctivag.skyblock.worlds;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.worlds.generators.VoidGenerator;
import de.noctivag.skyblock.worlds.generators.IslandGenerator;
import de.noctivag.skyblock.worlds.generators.DungeonGenerator;
import de.noctivag.skyblock.worlds.generators.ArenaGenerator;
import de.noctivag.skyblock.worlds.generators.MinigameGenerator;
import de.noctivag.skyblock.worlds.generators.CustomWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Thread-Safe World Manager - Multithreaded Welt-Verwaltung
 *
 * Verantwortlich für:
 * - Thread-sichere Welt-Operationen
 * - Asynchrone Welt-Erstellung und -Laden
 * - Concurrent World Access Management
 * - Performance-Optimierung durch Parallelisierung
 * - Custom World Loading System
 * - Advanced Server Management
 */
public class ThreadSafeWorldManager {

    private final SkyblockPlugin plugin;
    private final Map<String, WorldConfig> worldConfigs = new ConcurrentHashMap<>();
    private final Map<String, World> managedWorlds = new ConcurrentHashMap<>();
    private final Map<String, WorldLock> worldLocks = new ConcurrentHashMap<>();
    private final Map<String, CustomWorldInfo> customWorlds = new ConcurrentHashMap<>();

    // Threading
    private final ExecutorService worldExecutor = Executors.newFixedThreadPool(4, r -> {
        Thread thread = new Thread(r, "WorldManager-Thread");
        thread.setDaemon(true);
        return thread;
    });

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread thread = new Thread(r, "WorldManager-Scheduler");
        thread.setDaemon(true);
        return thread;
    });

    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private final AtomicInteger activeOperations = new AtomicInteger(0);

    // Performance Monitoring
    private final Map<String, WorldMetrics> worldMetrics = new ConcurrentHashMap<>();
    private BukkitTask metricsTask;

    public ThreadSafeWorldManager(SkyblockPlugin plugin) {
        this.plugin = plugin;
        initializeWorldConfigs();
    }

    /**
     * Initialisiert alle Welt-Konfigurationen mit erweiterten Server-Typen
     */
    private void initializeWorldConfigs() {
        // Skyblock Welten
        worldConfigs.put("skyblock_hub", new WorldConfig(
            "skyblock_hub", "Skyblock Hub", WorldType.FLAT, new VoidGenerator(),
            true, true, "Hauptlobby für Skyblock"
        ));

        worldConfigs.put("skyblock_private", new WorldConfig(
            "skyblock_private", "Private Islands", WorldType.NORMAL, new IslandGenerator(),
            true, false, "Private Spieler-Inseln"
        ));

        worldConfigs.put("skyblock_public", new WorldConfig(
            "skyblock_public", "Public Islands", WorldType.NORMAL, new IslandGenerator(),
            true, false, "Öffentliche Inseln"
        ));

        worldConfigs.put("skyblock_dungeons", new WorldConfig(
            "skyblock_dungeons", "Dungeon Instances", WorldType.NORMAL, new DungeonGenerator(),
            false, false, "Dungeon-Instanzen"
        ));

        // Event Welten
        worldConfigs.put("event_arenas", new WorldConfig(
            "event_arenas", "Event Arenas", WorldType.FLAT, new ArenaGenerator(),
            true, false, "Event-Arenen"
        ));

        // PvP Welten
        worldConfigs.put("pvp_arenas", new WorldConfig(
            "pvp_arenas", "PvP Arenas", WorldType.FLAT, new ArenaGenerator(),
            true, false, "PvP-Arenen"
        ));

        // Minigame Welten
        worldConfigs.put("minigame_worlds", new WorldConfig(
            "minigame_worlds", "Minigame Worlds", WorldType.NORMAL, new MinigameGenerator(),
            true, false, "Minigame-Welten"
        ));

        // Neue Server-Typen
        worldConfigs.put("creative_worlds", new WorldConfig(
            "creative_worlds", "Creative Worlds", WorldType.NORMAL, new CustomWorldGenerator(),
            true, false, "Creative-Modus Welten"
        ));

        worldConfigs.put("survival_worlds", new WorldConfig(
            "survival_worlds", "Survival Worlds", WorldType.NORMAL, new CustomWorldGenerator(),
            true, false, "Survival-Modus Welten"
        ));

        worldConfigs.put("hardcore_worlds", new WorldConfig(
            "hardcore_worlds", "Hardcore Worlds", WorldType.NORMAL, new CustomWorldGenerator(),
            true, false, "Hardcore-Modus Welten"
        ));

        worldConfigs.put("adventure_worlds", new WorldConfig(
            "adventure_worlds", "Adventure Worlds", WorldType.NORMAL, new CustomWorldGenerator(),
            true, false, "Adventure-Modus Welten"
        ));

        worldConfigs.put("spectator_worlds", new WorldConfig(
            "spectator_worlds", "Spectator Worlds", WorldType.FLAT, new VoidGenerator(),
            true, false, "Spectator-Modus Welten"
        ));

        worldConfigs.put("test_worlds", new WorldConfig(
            "test_worlds", "Test Worlds", WorldType.FLAT, new VoidGenerator(),
            true, false, "Test- und Entwicklungs-Welten"
        ));

        worldConfigs.put("resource_worlds", new WorldConfig(
            "resource_worlds", "Resource Worlds", WorldType.NORMAL, new CustomWorldGenerator(),
            true, false, "Resource-Sammel-Welten"
        ));

        worldConfigs.put("nether_worlds", new WorldConfig(
            "nether_worlds", "Nether Worlds", WorldType.NORMAL, new CustomWorldGenerator(),
            true, false, "Nether-Dimension Welten"
        ));

        worldConfigs.put("end_worlds", new WorldConfig(
            "end_worlds", "End Worlds", WorldType.NORMAL, new CustomWorldGenerator(),
            true, false, "End-Dimension Welten"
        ));

        plugin.getLogger().info("Initialized " + worldConfigs.size() + " world configurations with multithreading support");
    }

    /**
     * Initialisiert alle Welten asynchron
     */
    public CompletableFuture<Boolean> initializeAllWorlds() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        plugin.getLogger().info("Initializing all worlds with multithreading...");

        worldExecutor.submit(() -> {
            try {
                List<CompletableFuture<World>> worldFutures = new ArrayList<>();

                // Erstelle alle Welten parallel
                for (Map.Entry<String, WorldConfig> entry : worldConfigs.entrySet()) {
                    String worldName = entry.getKey();
                    WorldConfig config = entry.getValue();

                    CompletableFuture<World> worldFuture = initializeWorldAsync(worldName, config);
                    worldFutures.add(worldFuture);
                }

                // Warte auf alle Welten
                CompletableFuture<Void> allWorlds = CompletableFuture.allOf(
                    worldFutures.toArray(new CompletableFuture<?>[0])
                );

                allWorlds.thenAccept(v -> {
                    final int[] successCount = {0};
                    for (CompletableFuture<World> worldFuture : worldFutures) {
                        try {
                            World world = worldFuture.get();
                            if (world != null) {
                                successCount[0]++;
                            }
                        } catch (Exception e) {
                            plugin.getLogger().log(Level.WARNING, "Failed to get world result", e);
                        }
                    }

                    isInitialized.set(successCount[0] > 0);

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        if (isInitialized.get()) {
                            plugin.getLogger().info("Multithreaded world initialization completed: " +
                                successCount[0] + "/" + worldConfigs.size() + " worlds ready");
                            future.complete(true);
                        } else {
                            plugin.getLogger().severe("Multithreaded world initialization failed");
                            future.complete(false);
                        }
                    });
                });

            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Critical error during multithreaded world initialization", e);
                Bukkit.getScheduler().runTask(plugin, () -> future.complete(false));
            }
        });

        return future;
    }

    /**
     * Initialisiert eine einzelne Welt asynchron
     */
    private CompletableFuture<World> initializeWorldAsync(String worldName, WorldConfig config) {
        CompletableFuture<World> future = new CompletableFuture<>();

        worldExecutor.submit(() -> {
            try {
                activeOperations.incrementAndGet();

                // Prüfe ob Welt bereits existiert
                World existingWorld = Bukkit.getWorld(worldName);
                if (existingWorld != null) {
                    managedWorlds.put(worldName, existingWorld);
                    worldMetrics.put(worldName, new WorldMetrics(worldName));
                    future.complete(existingWorld);
                    return;
                }

                // Prüfe ob Welt-Ordner existiert
                File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
                if (worldFolder.exists()) {
                    World world = loadExistingWorld(worldName, config);
                    if (world != null) {
                        managedWorlds.put(worldName, world);
                        worldMetrics.put(worldName, new WorldMetrics(worldName));
                    }
                    future.complete(world);
                    return;
                }

                // Erstelle neue Welt
                World world = createNewWorld(worldName, config);
                if (world != null) {
                    managedWorlds.put(worldName, world);
                    worldMetrics.put(worldName, new WorldMetrics(worldName));
                }

                future.complete(world);

            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to initialize world: " + worldName, e);
                future.complete(null);
            } finally {
                activeOperations.decrementAndGet();
            }
        });

        return future;
    }

    /**
     * Lädt eine existierende Welt
     */
    private World loadExistingWorld(String worldName, WorldConfig config) {
        try {
            WorldCreator creator = new WorldCreator(worldName);
            creator.type(config.getWorldType());
            creator.generator(config.getGenerator());

            // Ensure world creation runs on main thread
            java.util.concurrent.Future<World> syncFuture = Bukkit.getScheduler().callSyncMethod(plugin, creator::createWorld);

            World world = null;
            try {
                world = syncFuture.get();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                plugin.getLogger().log(Level.WARNING, "Interrupted while creating world: " + worldName, ie);
            } catch (java.util.concurrent.ExecutionException ee) {
                plugin.getLogger().log(Level.SEVERE, "Exception while creating world (sync): " + worldName, ee.getCause());
            }

            if (world != null) {
                configureWorld(world, config);
            }
            return world;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load existing world: " + worldName, e);
            return null;
        }
    }

    /**
     * Erstellt eine neue Welt
     */
    private World createNewWorld(String worldName, WorldConfig config) {
        try {
            WorldCreator creator = new WorldCreator(worldName);
            creator.type(config.getWorldType());
            creator.generator(config.getGenerator());

            // Try to call keepSpawnLoaded(boolean) reflectively if available (some API versions provide it)
            if (config.isKeepSpawnInMemory()) {
                try {
                    java.lang.reflect.Method keepMethod = WorldCreator.class.getMethod("keepSpawnLoaded", boolean.class);
                    if (keepMethod != null) {
                        keepMethod.invoke(creator, true);
                    }
                } catch (NoSuchMethodException nsme) {
                    // Method not available on this server's API - ignore silently
                } catch (Exception ex) {
                    plugin.getLogger().log(Level.FINE, "Failed to call keepSpawnLoaded reflectively", ex);
                }
            }

            // Ensure world creation runs on main thread
            java.util.concurrent.Future<World> syncFuture = Bukkit.getScheduler().callSyncMethod(plugin, creator::createWorld);

            World world = null;
            try {
                world = syncFuture.get();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                plugin.getLogger().log(Level.WARNING, "Interrupted while creating world: " + worldName, ie);
            } catch (java.util.concurrent.ExecutionException ee) {
                plugin.getLogger().log(Level.SEVERE, "Exception while creating world (sync): " + worldName, ee.getCause());
            }

            if (world != null) {
                configureWorld(world, config);
                plugin.getLogger().info("Successfully created world: " + worldName);
            }
            return world;

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create new world: " + worldName, e);
            return null;
        }
    }

    /**
     * Konfiguriert eine Welt nach der Erstellung
     */
    private void configureWorld(World world, WorldConfig config) {
        try {
            world.setAutoSave(config.isAutoSave());

            if (config.getSpawnLocation() != null) {
                world.setSpawnLocation(config.getSpawnLocation());
            } else {
                world.setSpawnLocation(0, 100, 0);
            }

            // Welt-spezifische Konfiguration
            configureWorldSpecific(world);

        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to configure world: " + world.getName(), e);
        }
    }

    /**
     * Konfiguriert welt-spezifische Einstellungen
     */
    private void configureWorldSpecific(World world) {
        String worldName = world.getName();

        if (worldName.contains("hub") || worldName.contains("lobby")) {
            configureHubWorld(world);
        } else if (worldName.contains("island") || worldName.contains("private") || worldName.contains("public")) {
            configureIslandWorld(world);
        } else if (worldName.contains("dungeon")) {
            configureDungeonWorld(world);
        } else if (worldName.contains("arena") || worldName.contains("pvp") || worldName.contains("event")) {
            configureArenaWorld(world);
        } else if (worldName.contains("minigame")) {
            configureMinigameWorld(world);
        } else if (worldName.contains("creative")) {
            configureCreativeWorld(world);
        } else if (worldName.contains("survival")) {
            configureSurvivalWorld(world);
        } else if (worldName.contains("hardcore")) {
            configureHardcoreWorld(world);
        } else if (worldName.contains("adventure")) {
            configureAdventureWorld(world);
        } else if (worldName.contains("spectator")) {
            configureSpectatorWorld(world);
        } else if (worldName.contains("test")) {
            configureTestWorld(world);
        } else if (worldName.contains("resource")) {
            configureResourceWorld(world);
        } else if (worldName.contains("nether")) {
            configureNetherWorld(world);
        } else if (worldName.contains("end")) {
            configureEndWorld(world);
        }
    }

    /**
     * Konfiguriert Hub-Welten
     */
    private void configureHubWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, false);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, false);
        world.setTime(6000);
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
        world.setTime(18000);
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
        world.setTime(6000);
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
     * Konfiguriert Creative-Welten
     */
    private void configureCreativeWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, true);
        world.setGameRule(org.bukkit.GameRule.DO_IMMEDIATE_RESPAWN, true);
    }

    /**
     * Konfiguriert Survival-Welten
     */
    private void configureSurvivalWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, true);
        world.setGameRule(org.bukkit.GameRule.DO_IMMEDIATE_RESPAWN, false);
    }

    /**
     * Konfiguriert Hardcore-Welten
     */
    private void configureHardcoreWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, true);
        world.setGameRule(org.bukkit.GameRule.DO_IMMEDIATE_RESPAWN, false);
        world.setDifficulty(org.bukkit.Difficulty.HARD);
    }

    /**
     * Konfiguriert Adventure-Welten
     */
    private void configureAdventureWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, false);
        world.setGameRule(org.bukkit.GameRule.DO_IMMEDIATE_RESPAWN, false);
    }

    /**
     * Konfiguriert Spectator-Welten
     */
    private void configureSpectatorWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, false);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, false);
    }

    /**
     * Konfiguriert Test-Welten
     */
    private void configureTestWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, false);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, false);
        world.setGameRule(org.bukkit.GameRule.DO_IMMEDIATE_RESPAWN, true);
    }

    /**
     * Konfiguriert Resource-Welten
     */
    private void configureResourceWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, true);
    }

    /**
     * Konfiguriert Nether-Welten
     */
    private void configureNetherWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, true);
    }

    /**
     * Konfiguriert End-Welten
     */
    private void configureEndWorld(World world) {
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, true);
    }

    /**
     * Lädt eine eigene Welt
     */
    public CompletableFuture<Boolean> loadCustomWorld(String worldName, String worldPath, WorldType worldType,
                                                     ChunkGenerator generator, Map<String, Object> customConfig) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        worldExecutor.submit(() -> {
            try {
                activeOperations.incrementAndGet();

                // Prüfe ob Welt bereits geladen ist
                if (managedWorlds.containsKey(worldName)) {
                    future.complete(true);
                    return;
                }

                // Erstelle Custom World Info
                CustomWorldInfo customInfo = new CustomWorldInfo(worldName, worldPath, worldType, generator, customConfig);
                customWorlds.put(worldName, customInfo);

                // Erstelle World Config
                WorldConfig config = new WorldConfig(
                    worldName,
                    "Custom: " + worldName,
                    worldType,
                    generator != null ? generator : new CustomWorldGenerator(),
                    true,
                    false,
                    "Custom loaded world"
                );

                // Setze Custom-Konfiguration
                if (customConfig != null) {
                    config.setCustomConfig(customConfig);
                }

                worldConfigs.put(worldName, config);

                // Lade Welt
                World world = null;
                File worldFolder = new File(worldPath);

                if (worldFolder.exists()) {
                    // Kopiere Welt-Ordner
                    File targetFolder = new File(Bukkit.getWorldContainer(), worldName);
                    copyWorldFolder(worldFolder, targetFolder);

                    // Lade Welt
                    world = loadExistingWorld(worldName, config);
                } else {
                    // Erstelle neue Welt
                    world = createNewWorld(worldName, config);
                }

                if (world != null) {
                    managedWorlds.put(worldName, world);
                    worldMetrics.put(worldName, new WorldMetrics(worldName));
                    plugin.getLogger().info("Successfully loaded custom world: " + worldName);
                    future.complete(true);
                } else {
                    plugin.getLogger().warning("Failed to load custom world: " + worldName);
                    future.complete(false);
                }

            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error loading custom world: " + worldName, e);
                future.complete(false);
            } finally {
                activeOperations.decrementAndGet();
            }
        });

        return future;
    }

    /**
     * Kopiert einen Welt-Ordner
     */
    private void copyWorldFolder(File source, File target) {
        try {
            Path srcPath = source.toPath();
            Path destPath = target.toPath();

            Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws java.io.IOException {
                    Path relative = srcPath.relativize(dir);
                    Path resolved = destPath.resolve(relative);
                    if (!Files.exists(resolved)) {
                        try {
                            Files.createDirectories(resolved);
                        } catch (Exception e) {
                            plugin.getLogger().log(Level.WARNING, "Failed to create directory while copying world: " + resolved, e);
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws java.io.IOException {
                    Path relative = srcPath.relativize(file);
                    Path resolved = destPath.resolve(relative);
                    try {
                        Files.copy(file, resolved, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    } catch (Exception e) {
                        plugin.getLogger().log(Level.WARNING, "Failed to copy file while copying world: " + file + " -> " + resolved, e);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Error copying world folder", e);
        }
    }

    /**
     * Gibt eine Welt zurück (thread-safe)
     */
    public World getWorld(String worldName) {
        if (!isInitialized.get()) {
            plugin.getLogger().warning("ThreadSafeWorldManager not initialized, attempting to get world: " + worldName);
            return Bukkit.getWorld(worldName);
        }

        return managedWorlds.get(worldName);
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
     * Lädt eine Welt asynchron
     */
    public CompletableFuture<World> loadWorld(String worldName) {
        CompletableFuture<World> future = new CompletableFuture<>();

        worldExecutor.submit(() -> {
            try {
                World world = Bukkit.getWorld(worldName);
                if (world == null) {
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
                plugin.getLogger().log(Level.SEVERE, "Failed to load world: " + worldName, e);
                future.complete(null);
            }
        });

        return future;
    }

    /**
     * Entlädt eine Welt asynchron
     */
    public CompletableFuture<Boolean> unloadWorld(String worldName) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        worldExecutor.submit(() -> {
            try {
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    if (!world.getPlayers().isEmpty()) {
                        plugin.getLogger().warning("Cannot unload world " + worldName + ": Players are still in the world");
                        future.complete(false);
                        return;
                    }

                    boolean success = Bukkit.unloadWorld(world, true);
                    if (success) {
                        managedWorlds.remove(worldName);
                        worldMetrics.remove(worldName);
                        plugin.getLogger().info("Successfully unloaded world: " + worldName);
                    }

                    future.complete(success);
                } else {
                    future.complete(true);
                }

            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to unload world: " + worldName, e);
                future.complete(false);
            }
        });

        return future;
    }

    /**
     * Erstellt eine sichere Spawn-Location
     */
    public Location getSafeSpawnLocation(String worldName) {
        World world = getWorld(worldName);
        if (world == null) {
            return null;
        }

        Location spawn = world.getSpawnLocation();
        if (spawn.getBlock().getType().isSolid()) {
            for (int y = spawn.getBlockY(); y < world.getMaxHeight(); y++) {
                Location testLoc = spawn.clone();
                testLoc.setY(y);
                if (!testLoc.getBlock().getType().isSolid() &&
                    !testLoc.clone().add(0, 1, 0).getBlock().getType().isSolid()) {
                    return testLoc;
                }
            }
        }

        return spawn;
    }

    /**
     * Startet Performance-Monitoring
     */
    public void startPerformanceMonitoring() {
        metricsTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Map.Entry<String, WorldMetrics> entry : worldMetrics.entrySet()) {
                String worldName = entry.getKey();
                WorldMetrics metrics = entry.getValue();

                World world = managedWorlds.get(worldName);
                if (world != null) {
                    metrics.updateMetrics(world.getPlayers().size(), world.getLoadedChunks().length);
                }
            }
        }, 0L, 20L * 5L); // Alle 5 Sekunden
    }

    /**
     * Stoppt Performance-Monitoring
     */
    public void stopPerformanceMonitoring() {
        if (metricsTask != null) {
            metricsTask.cancel();
        }
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
     * Gibt alle Custom-Welten zurück
     */
    public Map<String, CustomWorldInfo> getCustomWorlds() {
        return new HashMap<>(customWorlds);
    }

    /**
     * Gibt Welt-Metriken zurück
     */
    public Map<String, WorldMetrics> getWorldMetrics() {
        return new HashMap<>(worldMetrics);
    }

    /**
     * Prüft ob der WorldManager initialisiert ist
     */
    public boolean isInitialized() {
        return isInitialized.get();
    }

    /**
     * Gibt aktive Operationen zurück
     */
    public int getActiveOperations() {
        return activeOperations.get();
    }

    /**
     * Schließt den WorldManager
     */
    public void shutdown() {
        plugin.getLogger().info("Shutting down ThreadSafeWorldManager...");

        // Stoppe Performance-Monitoring
        stopPerformanceMonitoring();

        // Speichere alle Welten
        for (World world : managedWorlds.values()) {
            if (world != null) {
                world.save();
            }
        }

        // Schließe Executor Services
        worldExecutor.shutdown();
        scheduler.shutdown();

        try {
            if (!worldExecutor.awaitTermination(30, TimeUnit.SECONDS)) {
                worldExecutor.shutdownNow();
            }
            if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            worldExecutor.shutdownNow();
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        managedWorlds.clear();
        worldConfigs.clear();
        customWorlds.clear();
        worldMetrics.clear();
        worldLocks.clear();

        isInitialized.set(false);

        plugin.getLogger().info("ThreadSafeWorldManager shutdown complete");
    }

    /**
     * World Lock Klasse für Thread-Safety
     */
    public static class WorldLock {
        private final String worldName;
        private final String lockOwner;
        private final long lockTime;
        private final LockType lockType;

        public WorldLock(String worldName, String lockOwner, LockType lockType) {
            this.worldName = worldName;
            this.lockOwner = lockOwner;
            this.lockTime = System.currentTimeMillis();
            this.lockType = lockType;
        }

        // Getters
        public String getWorldName() { return worldName; }
        public String getLockOwner() { return lockOwner; }
        public long getLockTime() { return lockTime; }
        public LockType getLockType() { return lockType; }

        public enum LockType {
            READ, WRITE, EXCLUSIVE
        }
    }

    /**
     * Custom World Info Klasse
     */
    public static class CustomWorldInfo {
        private final String worldName;
        private final String worldPath;
        private final WorldType worldType;
        private final ChunkGenerator generator;
        private final Map<String, Object> customConfig;
        private final long loadTime;

        public CustomWorldInfo(String worldName, String worldPath, WorldType worldType,
                              ChunkGenerator generator, Map<String, Object> customConfig) {
            this.worldName = worldName;
            this.worldPath = worldPath;
            this.worldType = worldType;
            this.generator = generator;
            this.customConfig = customConfig != null ? new HashMap<>(customConfig) : new HashMap<>();
            this.loadTime = System.currentTimeMillis();
        }

        // Getters
        public String getWorldName() { return worldName; }
        public String getWorldPath() { return worldPath; }
        public WorldType getWorldType() { return worldType; }
        public ChunkGenerator getGenerator() { return generator; }
        public Map<String, Object> getCustomConfig() { return new HashMap<>(customConfig); }
        public long getLoadTime() { return loadTime; }
    }

    /**
     * World Metrics Klasse
     */
    public static class WorldMetrics {
        private final String worldName;
        private int playerCount;
        private int loadedChunks;
        private long lastUpdate;
        private final List<Integer> playerCountHistory = new ArrayList<>();
        private final List<Integer> chunkCountHistory = new ArrayList<>();

        public WorldMetrics(String worldName) {
            this.worldName = worldName;
            this.lastUpdate = System.currentTimeMillis();
        }

        public void updateMetrics(int playerCount, int loadedChunks) {
            this.playerCount = playerCount;
            this.loadedChunks = loadedChunks;
            this.lastUpdate = System.currentTimeMillis();

            // Behalte nur die letzten 100 Einträge
            playerCountHistory.add(playerCount);
            chunkCountHistory.add(loadedChunks);

            if (playerCountHistory.size() > 100) {
                playerCountHistory.remove(0);
                chunkCountHistory.remove(0);
            }
        }

        // Getters
        public String getWorldName() { return worldName; }
        public int getPlayerCount() { return playerCount; }
        public int getLoadedChunks() { return loadedChunks; }
        public long getLastUpdate() { return lastUpdate; }
        public List<Integer> getPlayerCountHistory() { return new ArrayList<>(playerCountHistory); }
        public List<Integer> getChunkCountHistory() { return new ArrayList<>(chunkCountHistory); }

        public double getAveragePlayerCount() {
            return playerCountHistory.isEmpty() ? 0.0 :
                playerCountHistory.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        }

        public double getAverageChunkCount() {
            return chunkCountHistory.isEmpty() ? 0.0 :
                chunkCountHistory.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        }
    }

    /**
     * Erweiterte World Config Klasse
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
        private Map<String, Object> customConfig;

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
            this.customConfig = new HashMap<>();
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
        public Map<String, Object> getCustomConfig() { return new HashMap<>(customConfig); }

        public void setSpawnLocation(Location spawnLocation) {
            this.spawnLocation = spawnLocation;
        }

        public void setCustomConfig(Map<String, Object> customConfig) {
            this.customConfig = customConfig != null ? new HashMap<>(customConfig) : new HashMap<>();
        }
    }
}
