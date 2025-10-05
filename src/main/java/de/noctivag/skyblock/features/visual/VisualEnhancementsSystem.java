package de.noctivag.skyblock.features.visual;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;

/**
 * Visual Enhancements System - Anpassbare UI-Elemente und visuelle Verbesserungen
 * 
 * Features:
 * - 5 verschiedene Gesundheitsleisten-Stile
 * - 5 anpassbare Farbschemata
 * - 6 verschiedene Animations-Effekte
 * - Anpassbare HUD-Elemente
 */
public class VisualEnhancementsSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<String, HealthBarStyle> healthBarStyles = new HashMap<>();
    private final Map<String, ColorScheme> colorSchemes = new HashMap<>();
    private final Map<String, AnimationEffect> animationEffects = new HashMap<>();
    private final Map<String, VisualEnhancement> visualEnhancements = new HashMap<>();
    private final Map<UUID, String> playerHealthBarStyles = new HashMap<>();
    private final Map<UUID, String> playerColorSchemes = new HashMap<>();
    
    public VisualEnhancementsSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeHealthBarStyles();
        initializeColorSchemes();
        initializeAnimationEffects();
        initializeVisualEnhancements();
        startVisualUpdateTask();
    }
    
    /**
     * Initialisiert alle Gesundheitsleisten-Stile
     */
    private void initializeHealthBarStyles() {
        healthBarStyles.put("CLASSIC", new HealthBarStyle(
            "CLASSIC",
            "Classic Health Bar",
            "§c❤❤❤❤❤❤❤❤❤❤",
            "Traditional heart-based health display",
            Material.RED_DYE
        ));
        
        healthBarStyles.put("MODERN", new HealthBarStyle(
            "MODERN",
            "Modern Health Bar",
            "§a▰▰▰▰▰▰▰▰▰▰",
            "Sleek modern bar design",
            Material.LIME_DYE
        ));
        
        healthBarStyles.put("MINIMAL", new HealthBarStyle(
            "MINIMAL",
            "Minimal Health Bar",
            "§f█████████████",
            "Clean minimal design",
            Material.WHITE_DYE
        ));
        
        healthBarStyles.put("RAINBOW", new HealthBarStyle(
            "RAINBOW",
            "Rainbow Health Bar",
            "§c█§6█§e█§a█§b█§9█§d█§c█§6█§e█",
            "Colorful rainbow health bar",
            Material.PINK_DYE
        ));
        
        healthBarStyles.put("NEON", new HealthBarStyle(
            "NEON",
            "Neon Health Bar",
            "§b▬▬▬▬▬▬▬▬▬▬",
            "Bright neon style health bar",
            Material.CYAN_DYE
        ));
    }
    
    /**
     * Initialisiert alle Farbschemata
     */
    private void initializeColorSchemes() {
        colorSchemes.put("DEFAULT", new ColorScheme(
            "DEFAULT",
            "Default Colors",
            "§f", "§7", "§6", "§a", "§c"
        ));
        
        colorSchemes.put("DARK", new ColorScheme(
            "DARK",
            "Dark Theme",
            "§8", "§7", "§f", "§b", "§4"
        ));
        
        colorSchemes.put("NEON", new ColorScheme(
            "NEON",
            "Neon Colors",
            "§b", "§d", "§e", "§a", "§c"
        ));
        
        colorSchemes.put("OCEAN", new ColorScheme(
            "OCEAN",
            "Ocean Theme",
            "§3", "§9", "§b", "§a", "§1"
        ));
        
        colorSchemes.put("FIRE", new ColorScheme(
            "FIRE",
            "Fire Theme",
            "§6", "§e", "§c", "§4", "§8"
        ));
    }
    
    /**
     * Initialisiert alle Animations-Effekte
     */
    private void initializeAnimationEffects() {
        animationEffects.put("FADE", new AnimationEffect(
            "FADE",
            "Fade Animation",
            "Smooth fade in/out effects",
            "Elements smoothly appear and disappear",
            Material.GLASS
        ));
        
        animationEffects.put("SLIDE", new AnimationEffect(
            "SLIDE",
            "Slide Animation",
            "Elements slide in from sides",
            "Smooth sliding transitions for UI elements",
            Material.PISTON
        ));
        
        animationEffects.put("BOUNCE", new AnimationEffect(
            "BOUNCE",
            "Bounce Animation",
            "Bouncy entrance effects",
            "Elements bounce when appearing",
            Material.SLIME_BALL
        ));
        
        animationEffects.put("PULSE", new AnimationEffect(
            "PULSE",
            "Pulse Animation",
            "Pulsing size changes",
            "Elements pulse to draw attention",
            Material.REDSTONE
        ));
        
        animationEffects.put("ROTATE", new AnimationEffect(
            "ROTATE",
            "Rotate Animation",
            "Spinning rotation effects",
            "Elements rotate smoothly",
            Material.COMPASS
        ));
        
        animationEffects.put("TYPEWRITER", new AnimationEffect(
            "TYPEWRITER",
            "Typewriter Animation",
            "Text appears letter by letter",
            "Text types out character by character",
            Material.WRITABLE_BOOK
        ));
    }
    
    /**
     * Initialisiert alle visuellen Verbesserungen
     */
    private void initializeVisualEnhancements() {
        visualEnhancements.put("CUSTOM_SCOREBOARD", new VisualEnhancement(
            "CUSTOM_SCOREBOARD",
            "Custom Scoreboard",
            Arrays.asList("Animated scores", "Custom colors", "Player stats"),
            "Enhanced scoreboard with animations and colors",
            Material.PAPER
        ));
        
        visualEnhancements.put("DAMAGE_NUMBERS", new VisualEnhancement(
            "DAMAGE_NUMBERS",
            "Damage Numbers",
            Arrays.asList("Floating damage", "Critical hit effects", "Color coding"),
            "Show floating damage numbers above entities",
            Material.IRON_SWORD
        ));
        
        visualEnhancements.put("PARTICLE_EFFECTS", new VisualEnhancement(
            "PARTICLE_EFFECTS",
            "Enhanced Particles",
            Arrays.asList("Trail effects", "Ability indicators", "Environmental particles"),
            "Enhanced particle effects for abilities and movement",
            Material.BLAZE_POWDER
        ));
        
        visualEnhancements.put("ITEM_GLOW", new VisualEnhancement(
            "ITEM_GLOW",
            "Item Glow Effects",
            Arrays.asList("Rare item glow", "Enchantment shimmer", "Quality indicators"),
            "Special glow effects for rare and enchanted items",
            Material.GLOWSTONE_DUST
        ));
        
        visualEnhancements.put("SPELL_INDICATORS", new VisualEnhancement(
            "SPELL_INDICATORS",
            "Spell Indicators",
            Arrays.asList("Casting effects", "Cooldown timers", "Range indicators"),
            "Visual indicators for spell casting and cooldowns",
            Material.ENDER_EYE
        ));
        
        visualEnhancements.put("HUD_ANIMATIONS", new VisualEnhancement(
            "HUD_ANIMATIONS",
            "Animated HUD",
            Arrays.asList("Smooth transitions", "Dynamic updates", "Interactive elements"),
            "Animated and interactive HUD elements",
            Material.REDSTONE_TORCH
        ));
        
        visualEnhancements.put("WEATHER_EFFECTS", new VisualEnhancement(
            "WEATHER_EFFECTS",
            "Enhanced Weather",
            Arrays.asList("Dynamic weather", "Atmospheric effects", "Seasonal changes"),
            "Enhanced weather effects and atmospheric changes",
            Material.WATER_BUCKET
        ));
        
        visualEnhancements.put("LIGHTING_SYSTEM", new VisualEnhancement(
            "LIGHTING_SYSTEM",
            "Dynamic Lighting",
            Arrays.asList("Real-time shadows", "Dynamic light sources", "Ambient lighting"),
            "Advanced lighting system with dynamic shadows",
            Material.TORCH
        ));
    }
    
    /**
     * Startet die visuelle Update-Task
     */
    private void startVisualUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updatePlayerVisuals();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L); // Jede Sekunde
    }
    
    /**
     * Aktualisiert visuelle Elemente für alle Spieler
     */
    private void updatePlayerVisuals() {
        for (Player player : SkyblockPlugin.getServer().getOnlinePlayers()) {
            updatePlayerHealthBar(player);
            updatePlayerHUD(player);
        }
    }
    
    /**
     * Aktualisiert die Gesundheitsleiste eines Spielers
     */
    private void updatePlayerHealthBar(Player player) {
        String styleId = playerHealthBarStyles.getOrDefault(player.getUniqueId(), "CLASSIC");
        HealthBarStyle style = healthBarStyles.get(styleId);
        
        if (style != null) {
            double health = player.getHealth();
            double maxHealth = 20.0; // Standard max health value
            int healthBars = (int) Math.round((health / maxHealth) * 10);
            
            StringBuilder healthDisplay = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                if (i < healthBars) {
                    healthDisplay.append(style.getFullBar().charAt(2)); // Get colored character
                } else {
                    healthDisplay.append("§8").append(style.getFullBar().charAt(2)); // Gray for empty
                }
            }
            
            player.sendActionBar(Component.text("§cHealth: " + healthDisplay.toString() + 
                " §7(" + (int)health + "/" + (int)maxHealth + ")")
                .color(NamedTextColor.RED));
        }
    }
    
    /**
     * Aktualisiert das HUD eines Spielers
     */
    private void updatePlayerHUD(Player player) {
        String colorSchemeId = playerColorSchemes.getOrDefault(player.getUniqueId(), "DEFAULT");
        ColorScheme colorScheme = colorSchemes.get(colorSchemeId);
        
        if (colorScheme != null) {
            // Hier würden zusätzliche HUD-Updates implementiert
        }
    }
    
    /**
     * Setzt den Gesundheitsleisten-Stil für einen Spieler
     */
    public void setHealthBarStyle(Player player, String styleId) {
        if (healthBarStyles.containsKey(styleId)) {
            playerHealthBarStyles.put(player.getUniqueId(), styleId);
            HealthBarStyle style = healthBarStyles.get(styleId);
            player.sendMessage(Component.text("§a§lGESUNDHEITSLEISTEN-STIL GEÄNDERT!")
                .color(NamedTextColor.GREEN));
            player.sendMessage(Component.text("§7§l» §6" + style.getName())
                .color(NamedTextColor.GOLD));
        }
    }
    
    /**
     * Setzt das Farbschema für einen Spieler
     */
    public void setColorScheme(Player player, String schemeId) {
        if (colorSchemes.containsKey(schemeId)) {
            playerColorSchemes.put(player.getUniqueId(), schemeId);
            ColorScheme scheme = colorSchemes.get(schemeId);
            player.sendMessage(Component.text("§a§lFARBSCHEMA GEÄNDERT!")
                .color(NamedTextColor.GREEN));
            player.sendMessage(Component.text("§7§l» §6" + scheme.getName())
                .color(NamedTextColor.GOLD));
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Lade gespeicherte Einstellungen aus der Datenbank
        playerHealthBarStyles.putIfAbsent(player.getUniqueId(), "CLASSIC");
        playerColorSchemes.putIfAbsent(player.getUniqueId(), "DEFAULT");
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Speichere Einstellungen in der Datenbank
        // Entferne aus Memory Maps
        playerHealthBarStyles.remove(player.getUniqueId());
        playerColorSchemes.remove(player.getUniqueId());
    }
    
    /**
     * Gesundheitsleisten-Stil Datenklasse
     */
    public static class HealthBarStyle {
        private final String id;
        private final String name;
        private final String fullBar;
        private final String description;
        private final Material icon;
        
        public HealthBarStyle(String id, String name, String fullBar, String description, Material icon) {
            this.id = id;
            this.name = name;
            this.fullBar = fullBar;
            this.description = description;
            this.icon = icon;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getFullBar() { return fullBar; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
    }
    
    /**
     * Farbschema Datenklasse
     */
    public static class ColorScheme {
        private final String id;
        private final String name;
        private final String primaryColor;
        private final String secondaryColor;
        private final String accentColor;
        private final String successColor;
        private final String errorColor;
        
        public ColorScheme(String id, String name, String primaryColor, String secondaryColor,
                          String accentColor, String successColor, String errorColor) {
            this.id = id;
            this.name = name;
            this.primaryColor = primaryColor;
            this.secondaryColor = secondaryColor;
            this.accentColor = accentColor;
            this.successColor = successColor;
            this.errorColor = errorColor;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getPrimaryColor() { return primaryColor; }
        public String getSecondaryColor() { return secondaryColor; }
        public String getAccentColor() { return accentColor; }
        public String getSuccessColor() { return successColor; }
        public String getErrorColor() { return errorColor; }
    }
    
    /**
     * Animations-Effekt Datenklasse
     */
    public static class AnimationEffect {
        private final String id;
        private final String name;
        private final String type;
        private final String description;
        private final Material icon;
        
        public AnimationEffect(String id, String name, String type, String description, Material icon) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.description = description;
            this.icon = icon;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
    }
    
    /**
     * Visuelle Verbesserungs-Datenklasse
     */
    public static class VisualEnhancement {
        private final String id;
        private final String name;
        private final List<String> features;
        private final String description;
        private final Material icon;
        
        public VisualEnhancement(String id, String name, List<String> features, String description, Material icon) {
            this.id = id;
            this.name = name;
            this.features = features;
            this.description = description;
            this.icon = icon;
        }
        
        public String getId() { return id; }
        public String getName() { return name; }
        public List<String> getFeatures() { return features; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
    }
}
