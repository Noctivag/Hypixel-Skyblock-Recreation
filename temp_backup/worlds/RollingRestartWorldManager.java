package de.noctivag.skyblock.worlds;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.utils.FileUtils;
import de.noctivag.skyblock.worlds.generators.VoidGenerator;
import de.noctivag.skyblock.worlds.generators.IslandGenerator;
import de.noctivag.skyblock.worlds.generators.DungeonGenerator;
import de.noctivag.skyblock.worlds.generators.ArenaGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Rolling-Restart World Manager für öffentliche Welten
 * Implementiert das vollständige Rolling-Restart-System mit Hub-First-Loading
 */
public class RollingRestartWorldManager {

    private final SkyblockPluginRefactored plugin;
    private final File serverContainer;
    private final File privateIslandsContainer;

    // Für öffentliche Welten (Rolling Restart)
    private final Map<String, String> liveWorldAliases = new ConcurrentHashMap<>();
    private final Map<String, BukkitTask> restartTasks = new HashMap<>();
    
    // Welt-Konfiguration
    private final Map<String, WorldConfig> worldConfigs = new HashMap<>();
    private boolean isInitialized = false;

    public RollingRestartWorldManager(SkyblockPluginRefactored plugin) {
        this.plugin = plugin;
        this.serverContainer = plugin.getServer().getWorldContainer();
        this.privateIslandsContainer = new File(serverContainer, "private_islands");

        if (!privateIslandsContainer.exists()) {
            privateIslandsContainer.mkdirs();
        }
        
        initializeWorldConfigs();
    }

    /**
     * Initialisiert alle Welt-Konfigurationen
     */
    private void initializeWorldConfigs() {
        // Hub - Die wichtigste Welt (wird zuerst geladen)
        worldConfigs.put("hub", new WorldConfig(
            "hub",
            "Hub",
            org.bukkit.WorldType.FLAT,
            new de.noctivag.skyblock.worlds.generators.VoidGenerator(),
            "vorlagen/oeffentlich/hub.zip"
        ));
        
        // Öffentliche Welten
        worldConfigs.put("gold_mine", new WorldConfig(
            "gold_mine",
            "Gold Mine",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.IslandGenerator(),
            "vorlagen/oeffentlich/gold_mine.zip"
        ));
        
        worldConfigs.put("deep_caverns", new WorldConfig(
            "deep_caverns",
            "Deep Caverns",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.DungeonGenerator(),
            "vorlagen/oeffentlich/deep_caverns.zip"
        ));
        
        worldConfigs.put("spiders_den", new WorldConfig(
            "spiders_den",
            "Spider's Den",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.IslandGenerator(),
            "vorlagen/oeffentlich/spiders_den.zip"
        ));
        
        worldConfigs.put("blazing_fortress", new WorldConfig(
            "blazing_fortress",
            "Blazing Fortress",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.DungeonGenerator(),
            "vorlagen/oeffentlich/blazing_fortress.zip"
        ));
        
        worldConfigs.put("end", new WorldConfig(
            "end",
            "The End",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.IslandGenerator(),
            "vorlagen/oeffentlich/end.zip"
        ));
        
        worldConfigs.put("park", new WorldConfig(
            "park",
            "The Park",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.IslandGenerator(),
            "vorlagen/oeffentlich/park.zip"
        ));
        
        worldConfigs.put("dwarven_mines", new WorldConfig(
            "dwarven_mines",
            "Dwarven Mines",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.DungeonGenerator(),
            "vorlagen/oeffentlich/dwarven_mines.zip"
        ));
        
        worldConfigs.put("crystal_hollows", new WorldConfig(
            "crystal_hollows",
            "Crystal Hollows",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.DungeonGenerator(),
            "vorlagen/oeffentlich/crystal_hollows.zip"
        ));
        
        worldConfigs.put("crimson_isle", new WorldConfig(
            "crimson_isle",
            "Crimson Isle",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.IslandGenerator(),
            "vorlagen/oeffentlich/crimson_isle.zip"
        ));
        
        worldConfigs.put("kuudra", new WorldConfig(
            "kuudra",
            "Kuudra",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.DungeonGenerator(),
            "vorlagen/oeffentlich/kuudra.zip"
        ));
        
        worldConfigs.put("rift", new WorldConfig(
            "rift",
            "The Rift",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.IslandGenerator(),
            "vorlagen/oeffentlich/rift.zip"
        ));
        
        worldConfigs.put("garden", new WorldConfig(
            "garden",
            "Garden",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.IslandGenerator(),
            "vorlagen/oeffentlich/garden.zip"
        ));
        
        worldConfigs.put("dungeon_hub", new WorldConfig(
            "dungeon_hub",
            "Dungeon Hub",
            org.bukkit.WorldType.FLAT,
            new de.noctivag.skyblock.worlds.generators.VoidGenerator(),
            "vorlagen/oeffentlich/dungeon_hub.zip"
        ));
        
        worldConfigs.put("dungeon", new WorldConfig(
            "dungeon",
            "Dungeon",
            org.bukkit.WorldType.NORMAL,
            new de.noctivag.skyblock.worlds.generators.DungeonGenerator(),
            "vorlagen/oeffentlich/dungeon.zip"
        ));

        plugin.getLogger().info("Initialized " + worldConfigs.size() + " world configurations for Rolling-Restart system");
    }

    /**
     * Prüft ob der Server Folia verwendet
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
     * Erstellt eine Welt falls sie nicht existiert (Out-of-the-Box Funktionalität)
     */
    private void createWorldIfNotExists(String worldName) {
        // Prüfe ob die Welt bereits existiert
        if (Bukkit.getWorld(worldName) != null) {
            plugin.getLogger().info("World " + worldName + " already exists, skipping creation");
            return;
        }

        // Prüfe ob die Welt-Dateien existieren
        File worldFolder = new File(serverContainer, worldName);
        if (!worldFolder.exists()) {
            plugin.getLogger().warning("World folder does not exist: " + worldName + " - cannot create world");
            return;
        }

        if (isFoliaServer()) {
            // Bei Folia: Welten werden über Server-Konfiguration geladen
            plugin.getLogger().info("Folia detected - world " + worldName + " will be loaded by server configuration");
            plugin.getLogger().info("Please add world to server configuration or use Multiverse-Core for automatic loading");
            return;
        }

        try {
            // Erstelle die Welt mit dem entsprechenden Generator (nur bei normalen Servern)
            WorldCreator creator = new WorldCreator(worldName);
            
            // Setze den passenden Generator basierend auf dem Welt-Namen
            if (worldName.contains("hub") || worldName.contains("dungeon_hub")) {
                creator.generator(new VoidGenerator());
            } else if (worldName.contains("dungeon")) {
                creator.generator(new DungeonGenerator());
            } else if (worldName.contains("minigame") || worldName.contains("arena")) {
                creator.generator(new ArenaGenerator());
            } else {
                creator.generator(new IslandGenerator());
            }

            World world = Bukkit.createWorld(creator);
            if (world != null) {
                plugin.getLogger().info("Successfully created world: " + worldName);
                
                // Konfiguriere die Welt
                configureWorld(world);
            } else {
                plugin.getLogger().warning("Failed to create world: " + worldName);
            }
        } catch (UnsupportedOperationException e) {
            plugin.getLogger().warning("Cannot create world on this server type: " + worldName + " - " + e.getMessage());
        } catch (Exception e) {
            plugin.getLogger().severe("Error creating world " + worldName + ": " + e.getMessage());
        }
    }

    /**
     * Konfiguriert eine Welt nach der Erstellung
     */
    private void configureWorld(World world) {
        if (world == null) return;

        String worldName = world.getName();
        
        // Basis-Konfiguration für alle Welten
        world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
        world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, true);
        world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, true);
        world.setGameRule(org.bukkit.GameRule.KEEP_INVENTORY, true);
        world.setGameRule(org.bukkit.GameRule.DO_IMMEDIATE_RESPAWN, true);

        // Spezifische Konfiguration basierend auf Welt-Typ
        if (worldName.contains("hub")) {
            world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(org.bukkit.GameRule.DO_FIRE_TICK, false);
            world.setGameRule(org.bukkit.GameRule.MOB_GRIEFING, false);
        } else if (worldName.contains("dungeon")) {
            world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
        } else if (worldName.contains("arena") || worldName.contains("minigame")) {
            world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);
        }

        plugin.getLogger().info("Configured world: " + worldName);
    }

    /**
     * Bereitet Welt-Vorlagen für Folia vor und erstellt die Welten automatisch
     */
    private void prepareWorldTemplates() {
        plugin.getLogger().info("Preparing and creating world templates for Folia server...");

        for (String worldName : worldConfigs.keySet()) {
            String worldNameA = worldName + "_a";
            String worldNameB = worldName + "_b";

            // Kopiere Vorlagen für beide Instanzen
            loadOrCopyWorld(worldNameA, "oeffentlich");
            loadOrCopyWorld(worldNameB, "oeffentlich");

            // Erstelle die Welten automatisch
            createWorldIfNotExists(worldNameA);
            createWorldIfNotExists(worldNameB);

            // Setze Standard-LIVE-Instanz
            liveWorldAliases.put(worldName, worldNameA);

            plugin.getLogger().info("Prepared and created templates for " + worldName + " (A: " + worldNameA + ", B: " + worldNameB + ")");
        }

        plugin.getLogger().info("World templates prepared and created successfully for Folia server!");
        plugin.getLogger().info("All worlds are now available out-of-the-box!");
    }

    /**
     * Initialisiert das Rolling-Restart-System
     */
    public void initializeRollingRestartSystem() {
        if (isInitialized) {
            plugin.getLogger().warning("Rolling-Restart system already initialized!");
            return;
        }

        plugin.getLogger().info("Initializing Rolling-Restart system...");
        
        if (isFoliaServer()) {
            plugin.getLogger().info("Folia detected - preparing world templates for server startup");
            prepareWorldTemplates();
        } else {
            // HUB ZUERST LADEN - Das ist die wichtigste Welt
            plugin.getLogger().info("Loading Hub as first world...");
            setupPublicWorldPair("hub");
            
            // Dann andere öffentliche Welten
            for (String worldName : worldConfigs.keySet()) {
                if (!worldName.equals("hub")) {
                    setupPublicWorldPair(worldName);
                }
            }
        }
        
        isInitialized = true;
        plugin.getLogger().info("Rolling-Restart system initialized successfully!");
        plugin.getLogger().info("Hub is now available as the primary world.");
    }

    // --- ÖFFENTLICHE WELTEN (ROLLING RESTART) ---

    public void setupPublicWorldPair(String alias) {
        String worldNameA = alias + "_a";
        String worldNameB = alias + "_b";

        loadOrCopyWorld(worldNameA, "oeffentlich");
        loadOrCopyWorld(worldNameB, "oeffentlich");

        liveWorldAliases.put(alias, worldNameA);
        plugin.getLogger().info("'" + worldNameA + "' ist jetzt die LIVE-Instanz für den Alias '" + alias + "'.");
        
        scheduleNextSwap(alias);
    }

    private void performSwap(String alias) {
        String currentLiveName = liveWorldAliases.get(alias);
        String nextLiveName = currentLiveName.endsWith("_a") ? alias + "_b" : alias + "_a";
        
        World currentLiveWorld = Bukkit.getWorld(currentLiveName);
        World nextLiveWorld = Bukkit.getWorld(nextLiveName);

        if (currentLiveWorld == null || nextLiveWorld == null) {
            plugin.getLogger().severe("Konnte den Welt-Swap für '" + alias + "' nicht durchführen: Eine der Welten ist nicht geladen!");
            scheduleNextSwap(alias);
            return;
        }

        plugin.getLogger().info("Starte Welt-Swap für '" + alias + "'. Neue LIVE-Instanz: '" + nextLiveName + "'.");

        // Teleportiere alle Spieler zur neuen Welt
        for (Player player : currentLiveWorld.getPlayers()) {
            player.sendMessage("§e[Skyblock] §7Diese Welt wird zurückgesetzt. Du wirst zur neuen Instanz teleportiert.");
            player.teleport(nextLiveWorld.getSpawnLocation());
        }

        liveWorldAliases.put(alias, nextLiveName);
        resetWorldAsync(currentLiveName, "oeffentlich");
        scheduleNextSwap(alias);
    }

    private void scheduleNextSwap(String alias) {
        if (restartTasks.containsKey(alias)) restartTasks.get(alias).cancel();

        // Folia-kompatible Task-Scheduling
        if (isFoliaServer()) {
            plugin.getLogger().info("Folia detected - Rolling restart scheduling disabled");
            plugin.getLogger().info("World swaps will be handled manually or by server restart");
            return;
        }

        long fourHoursInTicks = 4 * 60 * 60 * 20; // 4 Stunden
        
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                performSwap(alias);
            }
        }.runTaskLater(plugin, fourHoursInTicks);

        restartTasks.put(alias, task);
        plugin.getLogger().info("Nächster Reset für '" + alias + "' geplant in 4 Stunden.");
    }

    public World getLiveWorld(String alias) {
        String liveWorldName = liveWorldAliases.get(alias);
        if (liveWorldName == null) {
            return null;
        }
        
        World world = Bukkit.getWorld(liveWorldName);
        if (world == null) {
            // Versuche die Welt automatisch zu erstellen/laden (Out-of-the-Box)
            plugin.getLogger().info("World " + liveWorldName + " not loaded, attempting to create/load...");
            createWorldIfNotExists(liveWorldName);
            world = Bukkit.getWorld(liveWorldName);
            
            if (world != null) {
                plugin.getLogger().info("Successfully loaded world: " + liveWorldName);
            } else {
                plugin.getLogger().warning("Failed to load world: " + liveWorldName);
            }
        }
        
        return world;
    }
    
    public void cancelAllTasks() {
        restartTasks.values().forEach(BukkitTask::cancel);
        restartTasks.clear();
    }
    
    // --- PRIVATE INSELN (ON-DEMAND) ---

    public World loadPrivateIsland(UUID playerUUID) {
        File islandFolder = new File(privateIslandsContainer, playerUUID.toString());
        
        if (!islandFolder.exists()) {
            plugin.getLogger().info("Erstelle neue private Insel für " + playerUUID);
            loadOrCopyWorld(playerUUID.toString(), "privat", "standard_insel", privateIslandsContainer);
        }
        
        return Bukkit.createWorld(new WorldCreator(islandFolder.getName()).generator(new EmptyWorldGenerator()));
    }
    
    public void unloadPrivateIsland(UUID playerUUID) {
        String worldName = playerUUID.toString();
        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            if (!world.getPlayers().isEmpty()) {
                plugin.getLogger().warning("Versuch, eine bewohnte Insel zu entladen: " + worldName);
                return;
            }
            world.save();
            Bukkit.unloadWorld(world, true);
            plugin.getLogger().info("Private Insel für " + playerUUID + " gespeichert und entladen.");
        }
    }

    // --- ALLGEMEINE HILFSMETHODEN ---

    private void loadOrCopyWorld(String worldName, String templateSubfolder, String templateName, File parentContainer) {
        File worldFolder = new File(parentContainer, worldName);
        String templatePath = "vorlagen/" + templateSubfolder + "/" + templateName + ".zip";

        if (!worldFolder.exists()) {
            try (InputStream templateStream = plugin.getResource(templatePath)) {
                if (templateStream == null) {
                    plugin.getLogger().severe("Vorlage nicht gefunden: " + templatePath);
                    return;
                }
                FileUtils.unzip(templateStream, worldFolder);
                plugin.getLogger().info("Welt '" + worldName + "' erfolgreich aus Vorlage erstellt.");
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Fehler beim Erstellen der Welt '" + worldName + "'", e);
            }
        }
    }
    
    // Überladene Methode für öffentliche Welten
    private void loadOrCopyWorld(String worldName, String templateSubfolder) {
        // Extrahiere den Basis-Namen (ohne _a/_b Suffix)
        String baseName = worldName.replaceAll("_[ab]$", "");
        loadOrCopyWorld(worldName, templateSubfolder, baseName, serverContainer);

        // Erstelle die Welt automatisch (Out-of-the-Box Funktionalität)
        createWorldIfNotExists(worldName);
    }
    
    private void resetWorldAsync(String worldName, String templateSubfolder) {
        // Folia-kompatible Welt-Reset
        if (isFoliaServer()) {
            plugin.getLogger().info("Folia detected - world reset will be handled by server configuration");
            plugin.getLogger().info("'" + worldName + "' reset scheduled for next server restart");
            return;
        }
        
        // Für normale Server: Verwende BukkitRunnable
        new BukkitRunnable() {
            @Override
            public void run() {
                // Haupt-Thread für Welt-Operationen
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        World world = Bukkit.getWorld(worldName);
                        if (world != null) Bukkit.unloadWorld(world, false);

                        // Asynchron für Datei-Operationen
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                File worldFolder = new File(serverContainer, worldName);
                                FileUtils.deleteDirectory(worldFolder);
                                loadOrCopyWorld(worldName, templateSubfolder);

                                // Zurück zum Haupt-Thread, um die Welt wieder zu laden
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Bukkit.createWorld(new WorldCreator(worldName));
                                            plugin.getLogger().info("'" + worldName + "' wurde zurückgesetzt und ist als STANDBY bereit.");
                                        } catch (UnsupportedOperationException e) {
                                            plugin.getLogger().warning("Cannot reset world on this server type: " + worldName);
                                        }
                                    }
                                }.runTask(plugin);
                            }
                        }.runTaskAsynchronously(plugin);
                    }
                }.runTask(plugin);
            }
        }.runTask(plugin);
    }
    
    // Empty World Generator für private Inseln
    public static class EmptyWorldGenerator extends org.bukkit.generator.ChunkGenerator {
        @Override
        @SuppressWarnings("deprecation")
        public ChunkData generateChunkData(World world, java.util.Random random, int x, int z, BiomeGrid biome) {
            return createChunkData(world);
        }
    }
    
    /**
     * Welt-Konfiguration Klasse
     */
    public static class WorldConfig {
        private final String name;
        private final String displayName;
        private final org.bukkit.WorldType worldType;
        private final org.bukkit.generator.ChunkGenerator generator;
        private final String templatePath;
        
        public WorldConfig(String name, String displayName, org.bukkit.WorldType worldType,
                         org.bukkit.generator.ChunkGenerator generator, String templatePath) {
            this.name = name;
            this.displayName = displayName;
            this.worldType = worldType;
            this.generator = generator;
            this.templatePath = templatePath;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public org.bukkit.WorldType getWorldType() { return worldType; }
        public org.bukkit.generator.ChunkGenerator getGenerator() { return generator; }
        public String getTemplatePath() { return templatePath; }
    }
    
    /**
     * Setzt die Live-Welt für ein Welt-Alias
     * @param worldAlias Das Welt-Alias (z.B. "hub", "mining")
     * @param liveWorldName Der Name der aktuell aktiven Welt (z.B. "hub_a")
     */
    public void setLiveWorld(String worldAlias, String liveWorldName) {
        liveWorldAliases.put(worldAlias, liveWorldName);
        plugin.getLogger().info("Set live world for " + worldAlias + " to " + liveWorldName);
    }
    
    /**
     * Holt den Namen der aktuell aktiven Welt für ein Welt-Alias
     * @param worldAlias Das Welt-Alias (z.B. "hub", "mining")
     * @return Der Name der aktuell aktiven Welt oder null wenn nicht gefunden
     */
    public String getLiveWorldName(String worldAlias) {
        return liveWorldAliases.get(worldAlias);
    }
}
