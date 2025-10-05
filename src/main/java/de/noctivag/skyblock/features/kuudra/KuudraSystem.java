package de.noctivag.skyblock.features.kuudra;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Kuudra System - Vollständiges Kuudra-Boss-System für Crimson Isles
 * 
 * Features:
 * - Kuudra-Boss-Kämpfe
 * - Kuudra-Ausrüstung
 * - Kuudra-Warnungen
 * - Visuelle Indikatoren
 * - Kuudra-Quest-System
 */
public class KuudraSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, KuudraPlayerData> playerData = new ConcurrentHashMap<>();
    private final Map<String, KuudraBoss> kuudraBosses = new HashMap<>();
    private final Map<String, KuudraEquipment> kuudraEquipment = new HashMap<>();
    private final Map<String, KuudraWarning> kuudraWarnings = new HashMap<>();
    
    public KuudraSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeKuudraBosses();
        initializeKuudraEquipment();
        initializeKuudraWarnings();
    }
    
    /**
     * Initialisiert alle Kuudra-Bosse
     */
    private void initializeKuudraBosses() {
        // Basic Kuudra
        kuudraBosses.put("BASIC_KUUDRA", new KuudraBoss(
            "Basic Kuudra",
            "BASIC",
            1000000, // 1M HP
            5000, // 5K Damage
            Arrays.asList("Kuudra Mandible", "Kuudra Skin", "Kuudra Claw"),
            "§7The basic form of Kuudra",
            EntityType.ENDER_DRAGON
        ));
        
        // Hot Kuudra
        kuudraBosses.put("HOT_KUUDRA", new KuudraBoss(
            "Hot Kuudra",
            "HOT",
            2000000, // 2M HP
            7500, // 7.5K Damage
            Arrays.asList("Hot Kuudra Mandible", "Hot Kuudra Skin", "Hot Kuudra Claw", "Hot Kuudra Heart"),
            "§7A fiery variant of Kuudra",
            EntityType.BLAZE
        ));
        
        // Burning Kuudra
        kuudraBosses.put("BURNING_KUUDRA", new KuudraBoss(
            "Burning Kuudra",
            "BURNING",
            3000000, // 3M HP
            10000, // 10K Damage
            Arrays.asList("Burning Kuudra Mandible", "Burning Kuudra Skin", "Burning Kuudra Claw", "Burning Kuudra Heart", "Burning Kuudra Eye"),
            "§7A burning variant of Kuudra",
            EntityType.MAGMA_CUBE
        ));
        
        // Fiery Kuudra
        kuudraBosses.put("FIERY_KUUDRA", new KuudraBoss(
            "Fiery Kuudra",
            "FIERY",
            4000000, // 4M HP
            12500, // 12.5K Damage
            Arrays.asList("Fiery Kuudra Mandible", "Fiery Kuudra Skin", "Fiery Kuudra Claw", "Fiery Kuudra Heart", "Fiery Kuudra Eye", "Fiery Kuudra Soul"),
            "§7A fiery variant of Kuudra",
            EntityType.GHAST
        ));
        
        // Infernal Kuudra
        kuudraBosses.put("INFERNAL_KUUDRA", new KuudraBoss(
            "Infernal Kuudra",
            "INFERNAL",
            5000000, // 5M HP
            15000, // 15K Damage
            Arrays.asList("Infernal Kuudra Mandible", "Infernal Kuudra Skin", "Infernal Kuudra Claw", "Infernal Kuudra Heart", "Infernal Kuudra Eye", "Infernal Kuudra Soul", "Infernal Kuudra Core"),
            "§7The most powerful form of Kuudra",
            EntityType.WITHER_SKELETON
        ));
        
        SkyblockPlugin.getLogger().info("§a[KuudraSystem] Initialized " + kuudraBosses.size() + " Kuudra bosses");
    }
    
    /**
     * Initialisiert alle Kuudra-Ausrüstung
     */
    private void initializeKuudraEquipment() {
        // Kuudra Helmet
        kuudraEquipment.put("KUUDRA_HELMET", new KuudraEquipment(
            "Kuudra Helmet",
            "HELMET",
            Material.LEATHER_HELMET,
            Arrays.asList("+100 Health", "+50 Defense", "+25 Intelligence"),
            "§7Helmet crafted from Kuudra materials",
            "BASIC"
        ));
        
        // Kuudra Chestplate
        kuudraEquipment.put("KUUDRA_CHESTPLATE", new KuudraEquipment(
            "Kuudra Chestplate",
            "CHESTPLATE",
            Material.LEATHER_CHESTPLATE,
            Arrays.asList("+200 Health", "+100 Defense", "+50 Intelligence"),
            "§7Chestplate crafted from Kuudra materials",
            "BASIC"
        ));
        
        // Kuudra Leggings
        kuudraEquipment.put("KUUDRA_LEGGINGS", new KuudraEquipment(
            "Kuudra Leggings",
            "LEGGINGS",
            Material.LEATHER_LEGGINGS,
            Arrays.asList("+150 Health", "+75 Defense", "+35 Intelligence"),
            "§7Leggings crafted from Kuudra materials",
            "BASIC"
        ));
        
        // Kuudra Boots
        kuudraEquipment.put("KUUDRA_BOOTS", new KuudraEquipment(
            "Kuudra Boots",
            "BOOTS",
            Material.LEATHER_BOOTS,
            Arrays.asList("+100 Health", "+50 Defense", "+25 Intelligence"),
            "§7Boots crafted from Kuudra materials",
            "BASIC"
        ));
        
        // Hot Kuudra Equipment
        kuudraEquipment.put("HOT_KUUDRA_HELMET", new KuudraEquipment(
            "Hot Kuudra Helmet",
            "HELMET",
            Material.LEATHER_HELMET,
            Arrays.asList("+150 Health", "+75 Defense", "+40 Intelligence", "+25 Strength"),
            "§7Helmet crafted from Hot Kuudra materials",
            "HOT"
        ));
        
        kuudraEquipment.put("HOT_KUUDRA_CHESTPLATE", new KuudraEquipment(
            "Hot Kuudra Chestplate",
            "CHESTPLATE",
            Material.LEATHER_CHESTPLATE,
            Arrays.asList("+300 Health", "+150 Defense", "+80 Intelligence", "+50 Strength"),
            "§7Chestplate crafted from Hot Kuudra materials",
            "HOT"
        ));
        
        // Burning Kuudra Equipment
        kuudraEquipment.put("BURNING_KUUDRA_HELMET", new KuudraEquipment(
            "Burning Kuudra Helmet",
            "HELMET",
            Material.LEATHER_HELMET,
            Arrays.asList("+200 Health", "+100 Defense", "+55 Intelligence", "+35 Strength"),
            "§7Helmet crafted from Burning Kuudra materials",
            "BURNING"
        ));
        
        kuudraEquipment.put("BURNING_KUUDRA_CHESTPLATE", new KuudraEquipment(
            "Burning Kuudra Chestplate",
            "CHESTPLATE",
            Material.LEATHER_CHESTPLATE,
            Arrays.asList("+400 Health", "+200 Defense", "+110 Intelligence", "+70 Strength"),
            "§7Chestplate crafted from Burning Kuudra materials",
            "BURNING"
        ));
        
        // Fiery Kuudra Equipment
        kuudraEquipment.put("FIERY_KUUDRA_HELMET", new KuudraEquipment(
            "Fiery Kuudra Helmet",
            "HELMET",
            Material.LEATHER_HELMET,
            Arrays.asList("+250 Health", "+125 Defense", "+70 Intelligence", "+45 Strength"),
            "§7Helmet crafted from Fiery Kuudra materials",
            "FIERY"
        ));
        
        kuudraEquipment.put("FIERY_KUUDRA_CHESTPLATE", new KuudraEquipment(
            "Fiery Kuudra Chestplate",
            "CHESTPLATE",
            Material.LEATHER_CHESTPLATE,
            Arrays.asList("+500 Health", "+250 Defense", "+140 Intelligence", "+90 Strength"),
            "§7Chestplate crafted from Fiery Kuudra materials",
            "FIERY"
        ));
        
        // Infernal Kuudra Equipment
        kuudraEquipment.put("INFERNAL_KUUDRA_HELMET", new KuudraEquipment(
            "Infernal Kuudra Helmet",
            "HELMET",
            Material.LEATHER_HELMET,
            Arrays.asList("+300 Health", "+150 Defense", "+85 Intelligence", "+55 Strength"),
            "§7Helmet crafted from Infernal Kuudra materials",
            "INFERNAL"
        ));
        
        kuudraEquipment.put("INFERNAL_KUUDRA_CHESTPLATE", new KuudraEquipment(
            "Infernal Kuudra Chestplate",
            "CHESTPLATE",
            Material.LEATHER_CHESTPLATE,
            Arrays.asList("+600 Health", "+300 Defense", "+170 Intelligence", "+110 Strength"),
            "§7Chestplate crafted from Infernal Kuudra materials",
            "INFERNAL"
        ));
        
        SkyblockPlugin.getLogger().info("§a[KuudraSystem] Initialized " + kuudraEquipment.size() + " Kuudra equipment pieces");
    }
    
    /**
     * Initialisiert alle Kuudra-Warnungen
     */
    private void initializeKuudraWarnings() {
        // Kuudra Spawn Warning
        kuudraWarnings.put("KUUDRA_SPAWN", new KuudraWarning(
            "Kuudra Spawn",
            "§c§lWARNING: Kuudra is spawning!",
            "§7A powerful Kuudra boss is about to spawn in the area!",
            EntityType.ENDER_DRAGON,
            5000L // 5 Sekunden
        ));
        
        // Kuudra Attack Warning
        kuudraWarnings.put("KUUDRA_ATTACK", new KuudraWarning(
            "Kuudra Attack",
            "§c§lWARNING: Kuudra is attacking!",
            "§7Kuudra is about to perform a powerful attack!",
            EntityType.BLAZE,
            3000L // 3 Sekunden
        ));
        
        // Kuudra Phase Change Warning
        kuudraWarnings.put("KUUDRA_PHASE", new KuudraWarning(
            "Kuudra Phase Change",
            "§e§lWARNING: Kuudra is changing phases!",
            "§7Kuudra is entering a new phase with different abilities!",
            EntityType.MAGMA_CUBE,
            4000L // 4 Sekunden
        ));
        
        // Kuudra Death Warning
        kuudraWarnings.put("KUUDRA_DEATH", new KuudraWarning(
            "Kuudra Death",
            "§a§lSUCCESS: Kuudra has been defeated!",
            "§7Kuudra has been defeated! Loot will be distributed shortly.",
            EntityType.GHAST,
            2000L // 2 Sekunden
        ));
        
        SkyblockPlugin.getLogger().info("§a[KuudraSystem] Initialized " + kuudraWarnings.size() + " Kuudra warnings");
    }
    
    /**
     * Gibt Kuudra-Boss-Daten zurück
     */
    public KuudraBoss getKuudraBoss(String bossId) {
        return kuudraBosses.get(bossId);
    }
    
    /**
     * Gibt alle Kuudra-Bosse zurück
     */
    public Map<String, KuudraBoss> getKuudraBosses() {
        return new HashMap<>(kuudraBosses);
    }
    
    /**
     * Gibt Kuudra-Ausrüstung zurück
     */
    public KuudraEquipment getKuudraEquipment(String equipmentId) {
        return kuudraEquipment.get(equipmentId);
    }
    
    /**
     * Gibt alle Kuudra-Ausrüstung zurück
     */
    public Map<String, KuudraEquipment> getKuudraEquipment() {
        return new HashMap<>(kuudraEquipment);
    }
    
    /**
     * Gibt Kuudra-Warnung zurück
     */
    public KuudraWarning getKuudraWarning(String warningId) {
        return kuudraWarnings.get(warningId);
    }
    
    /**
     * Gibt alle Kuudra-Warnungen zurück
     */
    public Map<String, KuudraWarning> getKuudraWarnings() {
        return new HashMap<>(kuudraWarnings);
    }
    
    /**
     * Gibt Spieler-Daten zurück
     */
    public KuudraPlayerData getPlayerData(Player player) {
        return playerData.computeIfAbsent(player.getUniqueId(), k -> new KuudraPlayerData(player.getUniqueId()));
    }
    
    /**
     * Startet einen Kuudra-Kampf
     */
    public void startKuudraFight(Player player, String bossId) {
        KuudraBoss boss = getKuudraBoss(bossId);
        if (boss == null) {
            player.sendMessage(Component.text("§cInvalid Kuudra boss!")
                .color(NamedTextColor.RED));
            return;
        }
        
        KuudraPlayerData data = getPlayerData(player);
        data.setCurrentBoss(bossId);
        data.setFightStartTime(java.lang.System.currentTimeMillis());
        
        // Zeige Warnung
        KuudraWarning warning = getKuudraWarning("KUUDRA_SPAWN");
        if (warning != null) {
            player.sendMessage(Component.text(warning.getTitle())
                .color(NamedTextColor.RED));
            player.sendMessage(Component.text(warning.getDescription())
                .color(NamedTextColor.GRAY));
        }
        
        // Spiele Sound
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
    }
    
    /**
     * Beendet einen Kuudra-Kampf
     */
    public void endKuudraFight(Player player, boolean success) {
        KuudraPlayerData data = getPlayerData(player);
        if (data.getCurrentBoss() == null) {
            return;
        }
        
        String bossId = data.getCurrentBoss();
        KuudraBoss boss = getKuudraBoss(bossId);
        
        if (success) {
            // Zeige Erfolgs-Warnung
            KuudraWarning warning = getKuudraWarning("KUUDRA_DEATH");
            if (warning != null) {
                player.sendMessage(Component.text(warning.getTitle())
                    .color(NamedTextColor.GREEN));
                player.sendMessage(Component.text(warning.getDescription())
                    .color(NamedTextColor.GRAY));
            }
            
            // Füge Drops hinzu
            for (String drop : boss.getDrops()) {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL)); // Placeholder
            }
            
            // Aktualisiere Statistiken
            data.incrementKills(bossId);
            data.addExperience(1000); // Placeholder XP
            
        } else {
            player.sendMessage(Component.text("§cKuudra fight failed!")
                .color(NamedTextColor.RED));
        }
        
        // Reset Kampf-Daten
        data.setCurrentBoss(null);
        data.setFightStartTime(0);
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Prüfe ob es sich um einen Kuudra-Boss handelt
        if (event.getEntity().customName() != null) {
            String customName = event.getEntity().customName().toString();
            if (customName.contains("Kuudra")) {
                // Finde Spieler in der Nähe
                event.getEntity().getWorld().getPlayers().stream()
                    .filter(player -> player.getLocation().distance(event.getEntity().getLocation()) <= 50)
                    .forEach(player -> endKuudraFight(player, true));
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) {
            return;
        }
        
        String displayName = meta.displayName().toString();
        
        // Prüfe ob es sich um Kuudra-Ausrüstung handelt
        if (displayName.contains("Kuudra")) {
            handleKuudraEquipment(player, item);
        }
    }
    
    /**
     * Behandelt Kuudra-Ausrüstung
     */
    private void handleKuudraEquipment(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        String displayName = meta.displayName().toString();
        
        // Finde passende Ausrüstung
        KuudraEquipment equipment = kuudraEquipment.values().stream()
            .filter(eq -> displayName.contains(eq.getName()))
            .findFirst()
            .orElse(null);
        
        if (equipment == null) {
            return;
        }
        
        // Zeige Ausrüstungs-Info
        player.sendMessage(Component.text("§a§lKUUDRA EQUIPMENT")
            .color(NamedTextColor.GREEN));
        player.sendMessage(Component.text("§7" + equipment.getDescription())
            .color(NamedTextColor.GRAY));
        
        for (String stat : equipment.getStats()) {
            player.sendMessage(Component.text("§7" + stat)
                .color(NamedTextColor.GRAY));
        }
    }
    
    /**
     * Kuudra-Boss Datenklasse
     */
    public static class KuudraBoss {
        private final String name;
        private final String tier;
        private final int health;
        private final int damage;
        private final List<String> drops;
        private final String description;
        private final Material icon;
        
        public KuudraBoss(String name, String tier, int health, int damage, List<String> drops, 
                          String description, EntityType entityType) {
            this.name = name;
            this.tier = tier;
            this.health = health;
            this.damage = damage;
            this.drops = drops;
            this.description = description;
            this.icon = Material.DRAGON_HEAD; // Default icon, since we're using EntityType
        }
        
        public String getName() { return name; }
        public String getTier() { return tier; }
        public int getHealth() { return health; }
        public int getDamage() { return damage; }
        public List<String> getDrops() { return drops; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
    }
    
    /**
     * Kuudra-Ausrüstung Datenklasse
     */
    public static class KuudraEquipment {
        private final String name;
        private final String type;
        private final Material material;
        private final List<String> stats;
        private final String description;
        private final String tier;
        
        public KuudraEquipment(String name, String type, Material material, List<String> stats, 
                              String description, String tier) {
            this.name = name;
            this.type = type;
            this.material = material;
            this.stats = stats;
            this.description = description;
            this.tier = tier;
        }
        
        public String getName() { return name; }
        public String getType() { return type; }
        public Material getMaterial() { return material; }
        public List<String> getStats() { return stats; }
        public String getDescription() { return description; }
        public String getTier() { return tier; }
    }
    
    /**
     * Kuudra-Warnung Datenklasse
     */
    public static class KuudraWarning {
        private final String name;
        private final String title;
        private final String description;
        private final Material icon;
        private final long duration;
        
        public KuudraWarning(String name, String title, String description, EntityType entityType, long duration) {
            this.name = name;
            this.title = title;
            this.description = description;
            this.icon = Material.REDSTONE_BLOCK; // Default icon for warnings
            this.duration = duration;
        }
        
        public String getName() { return name; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public long getDuration() { return duration; }
    }
    
    /**
     * Kuudra-Spieler-Daten
     */
    public static class KuudraPlayerData {
        private final UUID playerId;
        private String currentBoss;
        private long fightStartTime;
        private int experience;
        private final Map<String, Integer> kills = new HashMap<>();
        
        public KuudraPlayerData(UUID playerId) {
            this.playerId = playerId;
        }
        
        public UUID getPlayerId() { return playerId; }
        public String getCurrentBoss() { return currentBoss; }
        public void setCurrentBoss(String currentBoss) { this.currentBoss = currentBoss; }
        public long getFightStartTime() { return fightStartTime; }
        public void setFightStartTime(long fightStartTime) { this.fightStartTime = fightStartTime; }
        public int getExperience() { return experience; }
        public void addExperience(int experience) { this.experience += experience; }
        public void incrementKills(String bossId) { kills.put(bossId, kills.getOrDefault(bossId, 0) + 1); }
        public int getKills(String bossId) { return kills.getOrDefault(bossId, 0); }
    }
}
