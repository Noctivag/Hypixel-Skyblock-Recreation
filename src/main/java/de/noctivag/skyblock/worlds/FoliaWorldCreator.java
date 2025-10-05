package de.noctivag.skyblock.worlds;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.utils.FileUtils;
import de.noctivag.skyblock.worlds.generators.VoidGenerator;
import de.noctivag.skyblock.worlds.generators.IslandGenerator;
import de.noctivag.skyblock.worlds.generators.DungeonGenerator;
import de.noctivag.skyblock.worlds.generators.ArenaGenerator;
import de.noctivag.skyblock.worlds.generators.MinigameGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Folia-kompatible Welt-Erstellung
 * Erstellt Welten vor dem Server-Start oder lädt existierende Welten
 */
public class FoliaWorldCreator {
    
    private final SkyblockPlugin plugin;
    private final Map<String, WorldTemplate> worldTemplates = new HashMap<>();
    
    public FoliaWorldCreator(SkyblockPlugin plugin) {
        this.plugin = plugin;
        initializeTemplates();
    }
    
    /**
     * Initialisiert alle Welt-Vorlagen
     */
    private void initializeTemplates() {
        // Skyblock Hub - Void World
        worldTemplates.put("skyblock_hub", new WorldTemplate(
            "skyblock_hub",
            "Skyblock Hub",
            WorldType.FLAT,
            new VoidGenerator(),
            "vorlagen/hub/hub_template.zip"
        ));
        
        // Private Islands - Island World
        worldTemplates.put("skyblock_private", new WorldTemplate(
            "skyblock_private",
            "Private Islands",
            WorldType.NORMAL,
            new IslandGenerator(),
            "vorlagen/islands/private_template.zip"
        ));
        
        // Public Islands - Island World
        worldTemplates.put("skyblock_public", new WorldTemplate(
            "skyblock_public",
            "Public Islands",
            WorldType.NORMAL,
            new IslandGenerator(),
            "vorlagen/islands/public_template.zip"
        ));
        
        // Dungeons - Dungeon World
        worldTemplates.put("skyblock_dungeons", new WorldTemplate(
            "skyblock_dungeons",
            "Dungeon Instances",
            WorldType.NORMAL,
            new DungeonGenerator(),
            "vorlagen/dungeons/dungeon_template.zip"
        ));
        
        // Event Arenas - Flat World
        worldTemplates.put("event_arenas", new WorldTemplate(
            "event_arenas",
            "Event Arenas",
            WorldType.FLAT,
            new ArenaGenerator(),
            "vorlagen/arenas/event_template.zip"
        ));
        
        // PvP Arenas - Flat World
        worldTemplates.put("pvp_arenas", new WorldTemplate(
            "pvp_arenas",
            "PvP Arenas",
            WorldType.FLAT,
            new ArenaGenerator(),
            "vorlagen/arenas/pvp_template.zip"
        ));
        
        // Minigame Worlds - Normal World
        worldTemplates.put("minigame_worlds", new WorldTemplate(
            "minigame_worlds",
            "Minigame Worlds",
            WorldType.NORMAL,
            new MinigameGenerator(),
            "vorlagen/minigames/minigame_template.zip"
        ));
        
        plugin.getLogger().info("Initialized " + worldTemplates.size() + " world templates");
    }
    
    /**
     * Erstellt oder lädt alle Welten
     */
    public void createOrLoadAllWorlds() {
        plugin.getLogger().info("Creating or loading all worlds...");
        
        int successCount = 0;
        int totalCount = worldTemplates.size();
        
        for (Map.Entry<String, WorldTemplate> entry : worldTemplates.entrySet()) {
            String worldName = entry.getKey();
            WorldTemplate template = entry.getValue();
            
            try {
                if (createOrLoadWorld(worldName, template)) {
                    successCount++;
                    plugin.getLogger().info("Successfully created/loaded world: " + worldName);
                } else {
                    plugin.getLogger().warning("Failed to create/load world: " + worldName);
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Error creating/loading world: " + worldName, e);
            }
        }
        
        if (successCount > 0) {
            plugin.getLogger().info("World creation/loading completed: " + successCount + "/" + totalCount + " worlds ready");
        } else {
            plugin.getLogger().warning("World creation/loading completed: 0/" + totalCount + " worlds ready");
        }
    }
    
    /**
     * Erstellt oder lädt eine einzelne Welt
     */
    private boolean createOrLoadWorld(String worldName, WorldTemplate template) {
        try {
            // Prüfe ob Welt bereits existiert
            World existingWorld = Bukkit.getWorld(worldName);
            if (existingWorld != null) {
                plugin.getLogger().info("World " + worldName + " already exists, loading...");
                return true;
            }
            
            // Prüfe ob Welt-Ordner existiert
            File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
            if (worldFolder.exists()) {
                plugin.getLogger().info("World folder for " + worldName + " exists, loading...");
                return loadExistingWorld(worldName, template);
            }
            
            // Erstelle neue Welt aus Vorlage
            plugin.getLogger().info("Creating new world from template: " + worldName);
            return createWorldFromTemplate(worldName, template);
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create/load world: " + worldName, e);
            return false;
        }
    }
    
    /**
     * Lädt eine existierende Welt
     */
    private boolean loadExistingWorld(String worldName, WorldTemplate template) {
        try {
            WorldCreator creator = new WorldCreator(worldName);
            creator.type(template.getWorldType());
            creator.generator(template.getGenerator());
            
            World world = creator.createWorld();
            if (world != null) {
                configureWorld(world, template);
                return true;
            }
            return false;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load existing world: " + worldName, e);
            return false;
        }
    }
    
    /**
     * Erstellt eine neue Welt aus Vorlage
     */
    private boolean createWorldFromTemplate(String worldName, WorldTemplate template) {
        try {
            File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
            
            // Erstelle Welt-Ordner
            if (!worldFolder.exists()) {
                worldFolder.mkdirs();
            }
            
            // Kopiere Vorlage falls vorhanden
            if (template.getTemplatePath() != null) {
                if (!copyTemplate(worldName, template.getTemplatePath(), worldFolder)) {
                    plugin.getLogger().info("Template not found for " + worldName + ", creating empty world");
                }
            }
            
            // Erstelle Welt direkt - funktioniert auch auf Folia beim Server-Start
            plugin.getLogger().info("Creating world: " + worldName + " with generator: " + template.getGenerator().getClass().getSimpleName());
            WorldCreator creator = new WorldCreator(worldName);
            creator.type(template.getWorldType());
            creator.generator(template.getGenerator());
            
            World world = creator.createWorld();
            if (world != null) {
                configureWorld(world, template);
                plugin.getLogger().info("Successfully created world: " + worldName);
                return true;
            } else {
                plugin.getLogger().warning("Failed to create world: " + worldName + " - creator returned null");
                return false;
            }
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to create world from template: " + worldName, e);
            return false;
        }
    }
    
    /**
     * Kopiert eine Vorlage in den Welt-Ordner
     */
    private boolean copyTemplate(String worldName, String templatePath, File worldFolder) {
        try (InputStream templateStream = plugin.getResource(templatePath)) {
            if (templateStream == null) {
                plugin.getLogger().warning("Template not found: " + templatePath);
                return false;
            }
            
            // Lösche existierenden Inhalt
            if (worldFolder.exists()) {
                FileUtils.deleteDirectory(worldFolder);
                worldFolder.mkdirs();
            }
            
            // Entpacke Vorlage
            FileUtils.unzip(templateStream, worldFolder);
            plugin.getLogger().info("Successfully copied template for world: " + worldName);
            return true;
            
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to copy template for world: " + worldName, e);
            return false;
        }
    }
    
    /**
     * Konfiguriert eine Welt nach der Erstellung
     */
    private void configureWorld(World world, WorldTemplate template) {
        try {
            // Grundlegende Welt-Einstellungen
            world.setAutoSave(true);
            
            // Spawn-Punkt setzen
            world.setSpawnLocation(0, 100, 0);
            
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
            plugin.getLogger().log(Level.WARNING, "Failed to configure world: " + world.getName(), e);
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
        // Prüfe ob Welt bereits existiert
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            return world;
        }
        
        // Versuche Welt zu erstellen falls Vorlage existiert
        WorldTemplate template = worldTemplates.get(worldName);
        if (template != null) {
            plugin.getLogger().info("Creating world " + worldName + " on demand...");
            if (createOrLoadWorld(worldName, template)) {
                return Bukkit.getWorld(worldName);
            }
        }
        
        plugin.getLogger().warning("World " + worldName + " not found and cannot be created");
        return null;
    }
    
    /**
     * Prüft ob eine Welt existiert oder erstellt werden kann
     */
    public boolean worldExists(String worldName) {
        // Prüfe ob Welt bereits existiert
        if (Bukkit.getWorld(worldName) != null) {
            return true;
        }
        
        // Prüfe ob wir eine Vorlage für diese Welt haben
        return worldTemplates.containsKey(worldName);
    }
    
    /**
     * Gibt alle Welt-Vorlagen zurück
     */
    public Map<String, WorldTemplate> getWorldTemplates() {
        return new HashMap<>(worldTemplates);
    }
    
    /**
     * Welt-Vorlage Klasse
     */
    public static class WorldTemplate {
        private final String name;
        private final String displayName;
        private final WorldType worldType;
        private final ChunkGenerator generator;
        private final String templatePath;
        
        public WorldTemplate(String name, String displayName, WorldType worldType,
                           ChunkGenerator generator, String templatePath) {
            this.name = name;
            this.displayName = displayName;
            this.worldType = worldType;
            this.generator = generator;
            this.templatePath = templatePath;
        }
        
        // Getters
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public WorldType getWorldType() { return worldType; }
        public ChunkGenerator getGenerator() { return generator; }
        public String getTemplatePath() { return templatePath; }
    }
}
