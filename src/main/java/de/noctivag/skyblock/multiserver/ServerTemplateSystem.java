package de.noctivag.skyblock.multiserver;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Server Template System - Verwaltet Vorlagen für temporäre Server
 *
 * Ermöglicht es, Vorlagen-Welten für verschiedene Server-Typen zu definieren
 * und diese bei Bedarf zu kopieren/erstellen.
 */
public class ServerTemplateSystem {

    private final SkyblockPlugin SkyblockPlugin;
    private final Map<String, ServerTemplate> templates = new ConcurrentHashMap<>();
    private final File templatesDirectory;

    public ServerTemplateSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.templatesDirectory = new File(SkyblockPlugin.getDataFolder(), "templates");
    }

    public void init() {
        initializeTemplatesDirectory();
        loadTemplates();
    }

    /**
     * Initialisiert das Templates-Verzeichnis
     */
    private void initializeTemplatesDirectory() {
        if (!templatesDirectory.exists()) {
            templatesDirectory.mkdirs();
            SkyblockPlugin.getLogger().info("Created templates directory: " + templatesDirectory.getAbsolutePath());
        }
    }

    /**
     * Lädt alle verfügbaren Templates
     */
    private void loadTemplates() {
        // Erstelle Standard-Templates falls sie nicht existieren
        createDefaultTemplates();

        // Lade existierende Templates
        File[] templateFiles = templatesDirectory.listFiles((dir, name) -> name.endsWith(".yml"));
        if (templateFiles != null) {
            for (File templateFile : templateFiles) {
                try {
                    ServerTemplate template = loadTemplateFromFile(templateFile);
                    if (template != null) {
                        templates.put(template.getName(), template);
                        SkyblockPlugin.getLogger().info("Loaded template: " + template.getName());
                    }
                } catch (Exception e) {
                    SkyblockPlugin.getLogger().log(Level.WARNING, "Failed to load template: " + templateFile.getName(), e);
                }
            }
        }

        SkyblockPlugin.getLogger().info("Loaded " + templates.size() + " server templates");
    }

    /**
     * Erstellt Standard-Templates
     */
    private void createDefaultTemplates() {
        // Haupt-Server Template (SkyBlock Hub)
        createTemplate("skyblock_hub", "SkyBlock Hub Template", "Haupt-Server für SkyBlock", WorldType.FLAT, true, true);

        // Private Island Template
        createTemplate("private_island", "Private Island Template", "Standard Private Island", WorldType.NORMAL, false, true);

        // Garden Template
        createTemplate("garden", "Garden Template", "Standard Garden Welt", WorldType.NORMAL, false, true);

        // Public Island Templates
        createTemplate("spiders_den", "Spider's Den Template", "Spider's Den Welt", WorldType.NORMAL, false, true);
        createTemplate("the_end", "The End Template", "The End Welt", WorldType.NORMAL, false, true);
        createTemplate("the_park", "The Park Template", "The Park Welt", WorldType.NORMAL, false, true);
        createTemplate("gold_mine", "Gold Mine Template", "Gold Mine Welt", WorldType.NORMAL, false, true);
        createTemplate("deep_caverns", "Deep Caverns Template", "Deep Caverns Welt", WorldType.NORMAL, false, true);
        createTemplate("dwarven_mines", "Dwarven Mines Template", "Dwarven Mines Welt", WorldType.NORMAL, false, true);
        createTemplate("crystal_hollows", "Crystal Hollows Template", "Crystal Hollows Welt", WorldType.NORMAL, false, true);
        createTemplate("the_barn", "The Barn Template", "The Barn Welt", WorldType.NORMAL, false, true);
        createTemplate("mushroom_desert", "Mushroom Desert Template", "Mushroom Desert Welt", WorldType.NORMAL, false, true);
        createTemplate("blazing_fortress", "Blazing Fortress Template", "Blazing Fortress Welt", WorldType.NORMAL, false, true);
        createTemplate("the_nether", "The Nether Template", "The Nether Welt", WorldType.NORMAL, false, true);
        createTemplate("crimson_isle", "Crimson Isle Template", "Crimson Isle Welt", WorldType.NORMAL, false, true);
        createTemplate("rift", "The Rift Template", "The Rift Welt", WorldType.NORMAL, false, true);
        createTemplate("garden", "The Garden Template", "The Garden Welt", WorldType.NORMAL, false, true);
        createTemplate("kuudra", "Kuudra's End Template", "Kuudra's End Welt", WorldType.NORMAL, false, true);

        // Dungeon Templates (Temporäre Instanzen)
        createTemplate("catacombs_entrance", "Catacombs Entrance Template", "Catacombs Entrance Welt", WorldType.NORMAL, false, true);
        createTemplate("catacombs_floor_1", "Catacombs Floor 1 Template", "Catacombs Floor 1 Welt", WorldType.NORMAL, false, true);
        createTemplate("catacombs_floor_2", "Catacombs Floor 2 Template", "Catacombs Floor 2 Welt", WorldType.NORMAL, false, true);
        createTemplate("catacombs_floor_3", "Catacombs Floor 3 Template", "Catacombs Floor 3 Welt", WorldType.NORMAL, false, true);
        createTemplate("catacombs_floor_4", "Catacombs Floor 4 Template", "Catacombs Floor 4 Welt", WorldType.NORMAL, false, true);
        createTemplate("catacombs_floor_5", "Catacombs Floor 5 Template", "Catacombs Floor 5 Welt", WorldType.NORMAL, false, true);
        createTemplate("catacombs_floor_6", "Catacombs Floor 6 Template", "Catacombs Floor 6 Welt", WorldType.NORMAL, false, true);
        createTemplate("catacombs_floor_7", "Catacombs Floor 7 Template", "Catacombs Floor 7 Welt", WorldType.NORMAL, false, true);
        createTemplate("master_mode_floor_1", "Master Mode Floor 1 Template", "Master Mode Floor 1 Welt", WorldType.NORMAL, false, true);
        createTemplate("master_mode_floor_2", "Master Mode Floor 2 Template", "Master Mode Floor 2 Welt", WorldType.NORMAL, false, true);
        createTemplate("master_mode_floor_3", "Master Mode Floor 3 Template", "Master Mode Floor 3 Welt", WorldType.NORMAL, false, true);
        createTemplate("master_mode_floor_4", "Master Mode Floor 4 Template", "Master Mode Floor 4 Welt", WorldType.NORMAL, false, true);
        createTemplate("master_mode_floor_5", "Master Mode Floor 5 Template", "Master Mode Floor 5 Welt", WorldType.NORMAL, false, true);
        createTemplate("master_mode_floor_6", "Master Mode Floor 6 Template", "Master Mode Floor 6 Welt", WorldType.NORMAL, false, true);
        createTemplate("master_mode_floor_7", "Master Mode Floor 7 Template", "Master Mode Floor 7 Welt", WorldType.NORMAL, false, true);
    }

    /**
     * Erstellt ein neues Template
     */
    public void createTemplate(String name, String displayName, String description, WorldType worldType, boolean persistent, boolean gameServer) {
        ServerTemplate template = new ServerTemplate(name, displayName, description, worldType, persistent, gameServer);
        templates.put(name, template);
        saveTemplateToFile(template);
        SkyblockPlugin.getLogger().info("Created template: " + name);
    }

    /**
     * Lädt ein Template aus einer Datei
     */
    private ServerTemplate loadTemplateFromFile(File file) {
        try {
            // Hier würde normalerweise YAML-Parsing stattfinden
            // Für die Simulation erstellen wir ein Standard-Template
            String name = file.getName().replace(".yml", "");
            return new ServerTemplate(name, name + " Template", "Template loaded from file", WorldType.NORMAL, false, true);
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.WARNING, "Failed to load template from file: " + file.getName(), e);
            return null;
        }
    }

    /**
     * Speichert ein Template in eine Datei
     */
    private void saveTemplateToFile(ServerTemplate template) {
        try {
            File templateFile = new File(templatesDirectory, template.getName() + ".yml");
            // Hier würde normalerweise YAML-Schreiben stattfinden
            // Für die Simulation erstellen wir eine einfache Datei
            if (!templateFile.exists()) {
                templateFile.createNewFile();
            }
        } catch (Exception e) {
            SkyblockPlugin.getLogger().log(Level.WARNING, "Failed to save template to file: " + template.getName(), e);
        }
    }

    /**
     * Erstellt eine Welt basierend auf einem Template
     */
    public CompletableFuture<World> createWorldFromTemplate(String templateName, String worldName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ServerTemplate template = templates.get(templateName);
                if (template == null) {
                    SkyblockPlugin.getLogger().warning("Template not found: " + templateName);
                    return null;
                }

                // Prüfe ob Welt bereits existiert
                World existingWorld = Bukkit.getWorld(worldName);
                if (existingWorld != null) {
                    SkyblockPlugin.getLogger().info("World " + worldName + " already exists, using existing world");
                    return existingWorld;
                }

                // Erstelle neue Welt basierend auf Template
                WorldCreator creator = new WorldCreator(worldName);
                creator.type(template.getWorldType());
                creator.generateStructures(template.isGenerateStructures());

                // Setze Template-spezifische Einstellungen
                if (template.isGameServer()) {
                    creator.generator(new de.noctivag.skyblock.multiserver.generators.GameWorldGenerator());
                } else if (template.isPersistent()) {
                    creator.generator(new de.noctivag.skyblock.multiserver.generators.HubWorldGenerator());
                } else {
                    creator.generator(new de.noctivag.skyblock.multiserver.generators.IslandWorldGenerator());
                }

                // Erstelle die Welt
                World world = creator.createWorld();
                if (world != null) {
                    // Setze Template-spezifische GameRules
                    applyTemplateSettings(world, template);
                    SkyblockPlugin.getLogger().info("Created world " + worldName + " from template " + templateName);
                }

                return world;

            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Error creating world from template", e);
                return null;
            }
        });
    }

    /**
     * Wendet Template-Einstellungen auf eine Welt an
     */
    private void applyTemplateSettings(World world, ServerTemplate template) {
        // Setze GameRules basierend auf Template-Typ
        if (template.isGameServer()) {
            // Game Server Einstellungen
            world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
            world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(org.bukkit.GameRule.KEEP_INVENTORY, true);
        } else if (template.isPersistent()) {
            // Hub/Lobby Einstellungen
            world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, true);
            world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, true);
            world.setGameRule(org.bukkit.GameRule.KEEP_INVENTORY, false);
        } else {
            // Island Einstellungen
            world.setGameRule(org.bukkit.GameRule.DO_MOB_SPAWNING, true);
            world.setGameRule(org.bukkit.GameRule.DO_DAYLIGHT_CYCLE, true);
            world.setGameRule(org.bukkit.GameRule.DO_WEATHER_CYCLE, true);
            world.setGameRule(org.bukkit.GameRule.KEEP_INVENTORY, false);
        }
    }

    /**
     * Kopiert eine Vorlagen-Welt zu einer neuen Welt
     */
    public CompletableFuture<Boolean> copyTemplateWorld(String templateWorldName, String newWorldName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                World templateWorld = Bukkit.getWorld(templateWorldName);
                if (templateWorld == null) {
                    SkyblockPlugin.getLogger().warning("Template world not found: " + templateWorldName);
                    return false;
                }

                // Hier würde normalerweise das Kopieren der Welt-Dateien stattfinden
                // Für die Simulation erstellen wir eine neue Welt
                WorldCreator creator = new WorldCreator(newWorldName);
                creator.copy(templateWorld);

                World newWorld = creator.createWorld();
                if (newWorld != null) {
                    SkyblockPlugin.getLogger().info("Copied template world " + templateWorldName + " to " + newWorldName);
                    return true;
                }

                return false;

            } catch (Exception e) {
                SkyblockPlugin.getLogger().log(Level.SEVERE, "Error copying template world", e);
                return false;
            }
        });
    }

    /**
     * Gibt alle verfügbaren Templates zurück
     */
    public Map<String, ServerTemplate> getTemplates() {
        return new HashMap<>(templates);
    }

    /**
     * Gibt ein spezifisches Template zurück
     */
    public ServerTemplate getTemplate(String name) {
        return templates.get(name);
    }

    /**
     * Löscht ein Template
     */
    public boolean deleteTemplate(String name) {
        ServerTemplate template = templates.remove(name);
        if (template != null) {
            File templateFile = new File(templatesDirectory, name + ".yml");
            if (templateFile.exists()) {
                templateFile.delete();
            }
            SkyblockPlugin.getLogger().info("Deleted template: " + name);
            return true;
        }
        return false;
    }

    /**
     * Aktualisiert ein Template
     */
    public void updateTemplate(ServerTemplate template) {
        templates.put(template.getName(), template);
        saveTemplateToFile(template);
        SkyblockPlugin.getLogger().info("Updated template: " + template.getName());
    }
}
