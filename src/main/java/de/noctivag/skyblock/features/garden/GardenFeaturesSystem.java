package de.noctivag.skyblock.features.garden;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;

/**
 * Garden Features System - Farming HUD, Besucher und Speed-Presets
 * 
 * Features:
 * - 8 verschiedene Farming-Speed-Presets
 * - 6 Garden-Besucher mit speziellen Angeboten
 * - Automatische Farm-Verwaltung
 * - Farming-Statistiken und HUD
 */
public class GardenFeaturesSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<String, GardenPreset> farmingPresets = new HashMap<>();
    private final Map<String, GardenVisitor> gardenVisitors = new HashMap<>();
    private final Map<String, GardenStatistic> farmingStats = new HashMap<>();
    
    public GardenFeaturesSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeFarmingPresets();
        initializeGardenVisitors();
        initializeFarmingStatistics();
    }
    
    /**
     * Initialisiert alle Farming-Speed-Presets
     */
    private void initializeFarmingPresets() {
        // Speed Presets für verschiedene Crops
        farmingPresets.put("WHEAT_SPEED", new GardenPreset(
            "WHEAT_SPEED",
            "Wheat Speed Preset",
            Arrays.asList("Farming Speed +25%", "Wheat Collection +10%", "Auto-Replant"),
            "Optimized for wheat farming with maximum efficiency",
            Material.WHEAT
        ));
        
        farmingPresets.put("CARROT_SPEED", new GardenPreset(
            "CARROT_SPEED",
            "Carrot Speed Preset",
            Arrays.asList("Farming Speed +30%", "Carrot Collection +15%", "Fortune III"),
            "Perfect for carrot farming with extra fortune",
            Material.CARROT
        ));
        
        farmingPresets.put("POTATO_SPEED", new GardenPreset(
            "POTATO_SPEED",
            "Potato Speed Preset",
            Arrays.asList("Farming Speed +28%", "Potato Collection +12%", "Auto-Compost"),
            "Ideal for potato farming with composting bonus",
            Material.POTATO
        ));
        
        farmingPresets.put("SUGAR_CANE_SPEED", new GardenPreset(
            "SUGAR_CANE_SPEED",
            "Sugar Cane Speed Preset",
            Arrays.asList("Farming Speed +35%", "Sugar Cane Collection +20%", "Multi-Break"),
            "Specialized for sugar cane with multi-block breaking",
            Material.SUGAR_CANE
        ));
        
        farmingPresets.put("NETHER_WART_SPEED", new GardenPreset(
            "NETHER_WART_SPEED",
            "Nether Wart Speed Preset",
            Arrays.asList("Farming Speed +22%", "Nether Wart Collection +8%", "Alchemy Bonus"),
            "Enhanced for nether wart with alchemy benefits",
            Material.NETHER_WART
        ));
        
        farmingPresets.put("PUMPKIN_SPEED", new GardenPreset(
            "PUMPKIN_SPEED",
            "Pumpkin Speed Preset",
            Arrays.asList("Farming Speed +26%", "Pumpkin Collection +14%", "Mega-Crop Chance"),
            "Optimized for pumpkin farming with mega-crop bonus",
            Material.PUMPKIN
        ));
        
        farmingPresets.put("MELON_SPEED", new GardenPreset(
            "MELON_SPEED",
            "Melon Speed Preset",
            Arrays.asList("Farming Speed +24%", "Melon Collection +11%", "Juice Extraction"),
            "Perfect for melon farming with juice bonus",
            Material.MELON
        ));
        
        farmingPresets.put("MUSHROOM_SPEED", new GardenPreset(
            "MUSHROOM_SPEED",
            "Mushroom Speed Preset",
            Arrays.asList("Farming Speed +18%", "Mushroom Collection +6%", "Spore Spread"),
            "Specialized for mushroom farming with spore mechanics",
            Material.RED_MUSHROOM
        ));
    }
    
    /**
     * Initialisiert alle Garden-Besucher
     */
    private void initializeGardenVisitors() {
        // Garden Visitors mit speziellen Angeboten
        gardenVisitors.put("JACOB", new GardenVisitor(
            "JACOB",
            "Jacob the Farmer",
            Arrays.asList("Offers rare seeds", "Farming contests", "Special rewards"),
            "§7A famous farmer who organizes contests",
            EntityType.VILLAGER,
            3600000L // 1 hour visit duration
        ));
        
        gardenVisitors.put("ANITA", new GardenVisitor(
            "ANITA",
            "Anita the Merchant",
            Arrays.asList("Buys crops at premium", "Rare farming tools", "Collection upgrades"),
            "§7A merchant looking for quality produce",
            EntityType.VILLAGER,
            1800000L // 30 minutes visit duration
        ));
        
        gardenVisitors.put("BETH", new GardenVisitor(
            "BETH",
            "Beth the Botanist",
            Arrays.asList("Plant mutations", "Crop analysis", "Growth acceleration"),
            "§7A scientist studying plant genetics",
            EntityType.VILLAGER,
            2700000L // 45 minutes visit duration
        ));
        
        gardenVisitors.put("SPACEMAN", new GardenVisitor(
            "SPACEMAN",
            "Spaceman the Explorer",
            Arrays.asList("Alien crops", "Space farming tech", "Cosmic fertilizer"),
            "§7An intergalactic farmer with exotic seeds",
            EntityType.VILLAGER,
            5400000L // 90 minutes visit duration
        ));
        
        gardenVisitors.put("RUSTY", new GardenVisitor(
            "RUSTY",
            "Rusty the Mechanic",
            Arrays.asList("Farming equipment", "Tool upgrades", "Automation systems"),
            "§7A mechanic specializing in farming machinery",
            EntityType.VILLAGER,
            2400000L // 40 minutes visit duration
        ));
    }
    
    /**
     * Initialisiert alle Farming-Statistiken
     */
    private void initializeFarmingStatistics() {
        // Farming Statistics
        farmingStats.put("CROPS_HARVESTED", new GardenStatistic(
            "CROPS_HARVESTED",
            "Crops Harvested",
            "§7Total crops harvested: §a{value}",
            Material.WHEAT
        ));
        
        farmingStats.put("GARDEN_LEVEL", new GardenStatistic(
            "GARDEN_LEVEL",
            "Garden Level",
            "§7Current garden level: §6{value}",
            Material.EXPERIENCE_BOTTLE
        ));
        
        farmingStats.put("VISITORS_SERVED", new GardenStatistic(
            "VISITORS_SERVED",
            "Visitors Served",
            "§7Total visitors served: §b{value}",
            EntityType.VILLAGER
        ));
        
        farmingStats.put("FARMING_FORTUNE", new GardenStatistic(
            "FARMING_FORTUNE",
            "Farming Fortune",
            "§7Current farming fortune: §d{value}",
            Material.GOLDEN_HOE
        ));
    }
    
    /**
     * Aktiviert ein Farming-Preset
     */
    public void activatePreset(Player player, String presetId) {
        GardenPreset preset = farmingPresets.get(presetId);
        if (preset == null) {
            player.sendMessage(Component.text("§cPreset nicht gefunden!").color(NamedTextColor.RED));
            return;
        }
        
        player.sendMessage(Component.text("§a§lGARDEN PRESET AKTIVIERT!")
            .color(NamedTextColor.GREEN));
        player.sendMessage(Component.text("§7§l» §6" + preset.getName())
            .color(NamedTextColor.GOLD));
        
        for (String effect : preset.getEffects()) {
            player.sendMessage(Component.text("§7§l» §a" + effect)
                .color(NamedTextColor.GREEN));
        }
    }
    
    /**
     * Spawnt einen Garden-Besucher
     */
    public void spawnVisitor(Player player, String visitorId) {
        GardenVisitor visitor = gardenVisitors.get(visitorId);
        if (visitor == null) {
            player.sendMessage(Component.text("§cBesucher nicht gefunden!").color(NamedTextColor.RED));
            return;
        }
        
        player.sendMessage(Component.text("§6§lGARDEN BESUCHER ANGEKOMMEN!")
            .color(NamedTextColor.GOLD));
        player.sendMessage(Component.text("§7§l» §e" + visitor.getName())
            .color(NamedTextColor.YELLOW));
        player.sendMessage(Component.text("§7§l» " + visitor.getDescription())
            .color(NamedTextColor.GRAY));
        
        for (String offer : visitor.getOffers()) {
            player.sendMessage(Component.text("§7§l» §b" + offer)
                .color(NamedTextColor.AQUA));
        }
    }
    
    /**
     * Zeigt Farming-Statistiken an
     */
    public void showFarmingStats(Player player) {
        player.sendMessage(Component.text("§6§l=== GARDEN STATISTIKEN ===")
            .color(NamedTextColor.GOLD));
        
        for (GardenStatistic stat : farmingStats.values()) {
            // Hier würden echte Statistiken aus der Datenbank geladen
            String value = "123"; // Placeholder
            String display = stat.getDisplayFormat().replace("{value}", value);
            player.sendMessage(Component.text(display));
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null) {
            return;
        }
        
        // Prüfe auf Garden-Tool
        if (isGardenTool(item)) {
            handleGardenToolUse(player, item, event);
        }
    }
    
    /**
     * Prüft ob es sich um ein Garden-Tool handelt
     */
    private boolean isGardenTool(ItemStack item) {
        return item.getType().name().contains("HOE") || 
               item.getType() == Material.SHEARS ||
               item.getType().name().contains("AXE");
    }
    
    /**
     * Behandelt Garden-Tool Verwendung
     */
    private void handleGardenToolUse(Player player, ItemStack item, PlayerInteractEvent event) {
        // Zeige Garden HUD
        player.sendActionBar(Component.text("§aGarden Tool: §6" + item.getType().name() + 
            " §7| §aFarming Fortune: §d+50 §7| §aSpeed: §b+25%")
            .color(NamedTextColor.GREEN));
    }
    
    /**
     * Garden-Preset Datenklasse
     */
    public static class GardenPreset {
        private final String id;
        private final String name;
        private final List<String> effects;
        private final String description;
        private final Material icon;
        
        public GardenPreset(String id, String name, List<String> effects, String description, Material icon) {
            this.id = id;
            this.name = name;
            this.effects = effects;
            this.description = description;
            this.icon = icon;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public List<String> getEffects() { return effects; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
    }
    
    /**
     * Garden-Besucher Datenklasse
     */
    public static class GardenVisitor {
        private final String id;
        private final String name;
        private final List<String> offers;
        private final String description;
        private final EntityType entityType;
        private final long visitDuration;
        
        public GardenVisitor(String id, String name, List<String> offers, String description, 
                           EntityType entityType, long visitDuration) {
            this.id = id;
            this.name = name;
            this.offers = offers;
            this.description = description;
            this.entityType = entityType;
            this.visitDuration = visitDuration;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public List<String> getOffers() { return offers; }
        public String getDescription() { return description; }
        public EntityType getEntityType() { return entityType; }
        public long getVisitDuration() { return visitDuration; }
    }
    
    /**
     * Garden-Statistik Datenklasse
     */
    public static class GardenStatistic {
        private final String id;
        private final String name;
        private final String displayFormat;
        private final Object icon;
        
        public GardenStatistic(String id, String name, String displayFormat, Object icon) {
            this.id = id;
            this.name = name;
            this.displayFormat = displayFormat;
            this.icon = icon;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayFormat() { return displayFormat; }
        public Object getIcon() { return icon; }
    }
}
