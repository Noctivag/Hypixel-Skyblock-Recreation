package de.noctivag.skyblock.features.dungeons;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dungeon Solutions System - Rätsel-Lösungen und Wegpunkte für Dungeons
 * 
 * Features:
 * - Rätsel-Lösungen für alle Dungeon-Floors
 * - Wegpunkte für wichtige Bereiche
 * - Gewinnrechner für Dungeon-Runs
 * - Automatische Rätsel-Erkennung
 */
public class DungeonSolutionsSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<String, DungeonSolution> solutions = new HashMap<>();
    private final Map<String, DungeonWaypoint> waypoints = new HashMap<>();
    private final Map<UUID, Set<String>> playerDiscoveredSolutions = new ConcurrentHashMap<>();
    
    public DungeonSolutionsSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeDungeonSolutions();
        initializeDungeonWaypoints();
    }
    
    /**
     * Initialisiert alle Dungeon-Rätsel-Lösungen
     */
    private void initializeDungeonSolutions() {
        // F1 - Bonzo's Circus
        solutions.put("F1_CRYPT_PUZZLE", new DungeonSolution(
            "F1_CRYPT_PUZZLE",
            "F1 Crypt Puzzle",
            "F1",
            "Crypt",
            "Step on the pressure plates in the correct order: Red, Blue, Green, Yellow",
            Arrays.asList("RED_WOOL", "BLUE_WOOL", "GREEN_WOOL", "YELLOW_WOOL"),
            "Solve the crypt puzzle by stepping on pressure plates"
        ));
        
        solutions.put("F1_LEVER_PUZZLE", new DungeonSolution(
            "F1_LEVER_PUZZLE",
            "F1 Lever Puzzle",
            "F1",
            "Lever Room",
            "Pull levers in this order: Left, Right, Middle, Left, Right",
            Arrays.asList("LEVER"),
            "Pull levers in the correct sequence"
        ));
        
        // F2 - Scarf's Studies
        solutions.put("F2_BOOK_PUZZLE", new DungeonSolution(
            "F2_BOOK_PUZZLE",
            "F2 Book Puzzle",
            "F2",
            "Library",
            "Place books in chronological order",
            Arrays.asList("BOOK", "BOOKSHELF"),
            "Arrange books by their written dates"
        ));
        
        // F3 - The Professor
        solutions.put("F3_TNT_PUZZLE", new DungeonSolution(
            "F3_TNT_PUZZLE",
            "F3 TNT Puzzle",
            "F3",
            "TNT Room",
            "Defuse TNT by cutting correct wires",
            Arrays.asList("TNT", "SHEARS"),
            "Cut wires in the sequence: Red, Blue, Green"
        ));
        
        // F4 - Thorn
        solutions.put("F4_SPIRIT_PUZZLE", new DungeonSolution(
            "F4_SPIRIT_PUZZLE",
            "F4 Spirit Puzzle",
            "F4",
            "Spirit Room",
            "Guide spirits to their correct altars",
            Arrays.asList("SOUL_SAND", "GHAST_TEAR"),
            "Lead each spirit to the matching colored altar"
        ));
        
        // F5 - Livid
        solutions.put("F5_LIVID_FINDER", new DungeonSolution(
            "F5_LIVID_FINDER",
            "F5 Livid Finder",
            "F5",
            "Boss Room",
            "Identify the real Livid among clones",
            Arrays.asList("BOW", "ARROW"),
            "Real Livid takes damage, clones don't"
        ));
        
        // F6 - Sadan
        solutions.put("F6_TERRACOTTA_PUZZLE", new DungeonSolution(
            "F6_TERRACOTTA_PUZZLE",
            "F6 Terracotta Puzzle",
            "F6",
            "Terracotta Room",
            "Activate terracotta in specific pattern",
            Arrays.asList("TERRACOTTA"),
            "Follow the pattern shown on the walls"
        ));
        
        // F7 - Necron
        solutions.put("F7_CRYSTAL_PUZZLE", new DungeonSolution(
            "F7_CRYSTAL_PUZZLE",
            "F7 Crystal Puzzle",
            "F7",
            "Crystal Room",
            "Align crystals to unlock Necron's chamber",
            Arrays.asList("PRISMARINE_CRYSTALS"),
            "Rotate crystals until all beams connect"
        ));
    }
    
    /**
     * Initialisiert alle Dungeon-Wegpunkte
     */
    private void initializeDungeonWaypoints() {
        // F1 Waypoints
        waypoints.put("F1_ENTRANCE", new DungeonWaypoint(
            "F1_ENTRANCE",
            "F1 Entrance",
            "F1",
            "Entrance Hall",
            "Main entrance to Bonzo's domain",
            Material.OAK_DOOR
        ));
        
        waypoints.put("F1_SECRET_1", new DungeonWaypoint(
            "F1_SECRET_1",
            "F1 Secret #1",
            "F1",
            "Hidden Chamber",
            "First secret room behind bookshelf",
            Material.BOOKSHELF
        ));
        
        // F2 Waypoints
        waypoints.put("F2_LIBRARY", new DungeonWaypoint(
            "F2_LIBRARY",
            "F2 Library",
            "F2",
            "Main Library",
            "Scarf's study with puzzle books",
            Material.BOOKSHELF
        ));
        
        // F3 Waypoints
        waypoints.put("F3_TNT_ROOM", new DungeonWaypoint(
            "F3_TNT_ROOM",
            "F3 TNT Room",
            "F3",
            "Explosives Lab",
            "Professor's dangerous experiments",
            Material.TNT
        ));
        
        // F4 Waypoints
        waypoints.put("F4_SPIRIT_ROOM", new DungeonWaypoint(
            "F4_SPIRIT_ROOM",
            "F4 Spirit Room",
            "F4",
            "Haunted Chamber",
            "Where lost spirits roam",
            Material.SOUL_SAND
        ));
        
        // F5 Waypoints
        waypoints.put("F5_BOSS_ROOM", new DungeonWaypoint(
            "F5_BOSS_ROOM",
            "F5 Boss Room",
            "F5",
            "Livid's Arena",
            "Face Livid and his clones",
            Material.NETHERITE_SWORD
        ));
        
        // F6 Waypoints
        waypoints.put("F6_TERRACOTTA", new DungeonWaypoint(
            "F6_TERRACOTTA",
            "F6 Terracotta Room",
            "F6",
            "Ancient Statues",
            "Solve the terracotta warrior puzzle",
            Material.TERRACOTTA
        ));
        
        // F7 Waypoints
        waypoints.put("F7_NECRON", new DungeonWaypoint(
            "F7_NECRON",
            "F7 Necron's Lair",
            "F7",
            "Final Chamber",
            "The ultimate challenge awaits",
            Material.NETHER_STAR
        ));
    }
    
    /**
     * Gibt eine Dungeon-Lösung zurück
     */
    public DungeonSolution getSolution(String solutionId) {
        return solutions.get(solutionId);
    }
    
    /**
     * Gibt einen Dungeon-Wegpunkt zurück
     */
    public DungeonWaypoint getWaypoint(String waypointId) {
        return waypoints.get(waypointId);
    }
    
    /**
     * Gibt alle Lösungen zurück
     */
    public Collection<DungeonSolution> getAllSolutions() {
        return solutions.values();
    }
    
    /**
     * Gibt alle Wegpunkte zurück
     */
    public Collection<DungeonWaypoint> getAllWaypoints() {
        return waypoints.values();
    }
    
    /**
     * Markiert eine Lösung als entdeckt
     */
    public void markSolutionDiscovered(Player player, String solutionId) {
        playerDiscoveredSolutions.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>())
            .add(solutionId);
    }
    
    /**
     * Prüft ob eine Lösung entdeckt wurde
     */
    public boolean isSolutionDiscovered(Player player, String solutionId) {
        Set<String> discovered = playerDiscoveredSolutions.get(player.getUniqueId());
        return discovered != null && discovered.contains(solutionId);
    }
    
    /**
     * Gibt alle entdeckten Lösungen zurück
     */
    public Set<String> getDiscoveredSolutions(Player player) {
        return playerDiscoveredSolutions.getOrDefault(player.getUniqueId(), new HashSet<>());
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null) {
            return;
        }
        
        // Prüfe ob es sich um ein Rätsel-Item handelt
        if (isPuzzleItem(item)) {
            handlePuzzleInteraction(player, item, event);
        }
    }
    
    /**
     * Prüft ob es sich um ein Rätsel-Item handelt
     */
    private boolean isPuzzleItem(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) {
            return false;
        }
        
        String displayName = meta.displayName().toString();
        return solutions.values().stream()
            .anyMatch(solution -> solution.getRequiredItems().stream()
                .anyMatch(requiredItem -> displayName.contains(requiredItem)));
    }
    
    /**
     * Behandelt Rätsel-Interaktionen
     */
    private void handlePuzzleInteraction(Player player, ItemStack item, PlayerInteractEvent event) {
        // Finde passende Lösung
        for (DungeonSolution solution : solutions.values()) {
            if (solution.getRequiredItems().contains(item.getType().name())) {
                // Zeige Lösung an
                player.sendMessage(Component.text("§6§lDUNGEON LÖSUNG: §e" + solution.getName())
                    .color(NamedTextColor.GOLD));
                player.sendMessage(Component.text("§7§l» §f" + solution.getSolution())
                    .color(NamedTextColor.WHITE));
                player.sendMessage(Component.text("§7§l» §8" + solution.getDescription())
                    .color(NamedTextColor.DARK_GRAY));
                
                // Markiere als entdeckt
                markSolutionDiscovered(player, solution.getId());
                break;
            }
        }
    }
    
    /**
     * Dungeon-Lösung Datenklasse
     */
    public static class DungeonSolution {
        private final String id;
        private final String name;
        private final String floor;
        private final String room;
        private final String solution;
        private final List<String> requiredItems;
        private final String description;
        
        public DungeonSolution(String id, String name, String floor, String room, String solution, 
                              List<String> requiredItems, String description) {
            this.id = id;
            this.name = name;
            this.floor = floor;
            this.room = room;
            this.solution = solution;
            this.requiredItems = requiredItems;
            this.description = description;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getFloor() { return floor; }
        public String getRoom() { return room; }
        public String getSolution() { return solution; }
        public List<String> getRequiredItems() { return requiredItems; }
        public String getDescription() { return description; }
    }
    
    /**
     * Dungeon-Wegpunkt Datenklasse
     */
    public static class DungeonWaypoint {
        private final String id;
        private final String name;
        private final String floor;
        private final String location;
        private final String description;
        private final Material icon;
        
        public DungeonWaypoint(String id, String name, String floor, String location, 
                              String description, Material icon) {
            this.id = id;
            this.name = name;
            this.floor = floor;
            this.location = location;
            this.description = description;
            this.icon = icon;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getFloor() { return floor; }
        public String getLocation() { return location; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
    }
}
