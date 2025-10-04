package de.noctivag.skyblock.skyblock.skills;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.skyblock.SkyblockManager;
import de.noctivag.skyblock.skyblock.SkyblockSkills;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Umfassendes Skills-System für Hypixel SkyBlock
 */
public class SkillsSystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final SkyblockManager skyblockManager;
    private final Map<UUID, Map<String, Integer>> playerSkillXP = new ConcurrentHashMap<>();
    private final Map<UUID, Map<String, Integer>> playerSkillLevels = new ConcurrentHashMap<>();
    
    // Skill-Definitionen
    public enum Skill {
        COMBAT("Combat", "⚔", "Kampf", "Töte Monster um XP zu erhalten"),
        MINING("Mining", "⛏", "Bergbau", "Baue Blöcke ab um XP zu erhalten"),
        FORAGING("Foraging", "🌲", "Forstwirtschaft", "Fälle Bäume um XP zu erhalten"),
        FARMING("Farming", "🌾", "Landwirtschaft", "Ernte Pflanzen um XP zu erhalten"),
        FISHING("Fishing", "🎣", "Angeln", "Fische um XP zu erhalten"),
        ENCHANTING("Enchanting", "✨", "Verzauberung", "Verzaubere Items um XP zu erhalten"),
        ALCHEMY("Alchemy", "🧪", "Alchemie", "Braue Tränke um XP zu erhalten"),
        TAMING("Taming", "🐾", "Zähmung", "Zähme Tiere um XP zu erhalten"),
        CARPENTRY("Carpentry", "🔨", "Tischlerei", "Baue Items um XP zu erhalten"),
        RUNECRAFTING("Runecrafting", "🔮", "Runenhandwerk", "Erstelle Runen um XP zu erhalten"),
        SOCIAL("Social", "👥", "Sozial", "Interagiere mit anderen Spielern"),
        DUNGEONEERING("Dungeoneering", "🏰", "Dungeon", "Erkunde Dungeons um XP zu erhalten");
        
        private final String name;
        private final String icon;
        private final String germanName;
        private final String description;
        
        Skill(String name, String icon, String germanName, String description) {
            this.name = name;
            this.icon = icon;
            this.germanName = germanName;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getIcon() { return icon; }
        public String getGermanName() { return germanName; }
        public String getDescription() { return description; }
    }
    
    // XP-Tabellen für jedes Level
    private static final Map<Integer, Integer> XP_TABLE = new HashMap<>();
    
    static {
        // Hypixel SkyBlock XP-Tabelle
        XP_TABLE.put(1, 50);
        XP_TABLE.put(2, 125);
        XP_TABLE.put(3, 200);
        XP_TABLE.put(4, 300);
        XP_TABLE.put(5, 500);
        XP_TABLE.put(6, 750);
        XP_TABLE.put(7, 1000);
        XP_TABLE.put(8, 1500);
        XP_TABLE.put(9, 2000);
        XP_TABLE.put(10, 3500);
        XP_TABLE.put(11, 5000);
        XP_TABLE.put(12, 7500);
        XP_TABLE.put(13, 10000);
        XP_TABLE.put(14, 15000);
        XP_TABLE.put(15, 20000);
        XP_TABLE.put(16, 30000);
        XP_TABLE.put(17, 50000);
        XP_TABLE.put(18, 75000);
        XP_TABLE.put(19, 100000);
        XP_TABLE.put(20, 200000);
        XP_TABLE.put(21, 300000);
        XP_TABLE.put(22, 400000);
        XP_TABLE.put(23, 500000);
        XP_TABLE.put(24, 600000);
        XP_TABLE.put(25, 700000);
        XP_TABLE.put(26, 800000);
        XP_TABLE.put(27, 900000);
        XP_TABLE.put(28, 1000000);
        XP_TABLE.put(29, 1100000);
        XP_TABLE.put(30, 1200000);
        XP_TABLE.put(31, 1300000);
        XP_TABLE.put(32, 1400000);
        XP_TABLE.put(33, 1500000);
        XP_TABLE.put(34, 1600000);
        XP_TABLE.put(35, 1700000);
        XP_TABLE.put(36, 1800000);
        XP_TABLE.put(37, 1900000);
        XP_TABLE.put(38, 2000000);
        XP_TABLE.put(39, 2100000);
        XP_TABLE.put(40, 2200000);
        XP_TABLE.put(41, 2300000);
        XP_TABLE.put(42, 2400000);
        XP_TABLE.put(43, 2500000);
        XP_TABLE.put(44, 2600000);
        XP_TABLE.put(45, 2750000);
        XP_TABLE.put(46, 2900000);
        XP_TABLE.put(47, 3100000);
        XP_TABLE.put(48, 3400000);
        XP_TABLE.put(49, 3700000);
        XP_TABLE.put(50, 4000000);
        XP_TABLE.put(51, 4300000);
        XP_TABLE.put(52, 4600000);
        XP_TABLE.put(53, 4900000);
        XP_TABLE.put(54, 5200000);
        XP_TABLE.put(55, 5500000);
        XP_TABLE.put(56, 5800000);
        XP_TABLE.put(57, 6100000);
        XP_TABLE.put(58, 6400000);
        XP_TABLE.put(59, 6700000);
        XP_TABLE.put(60, 7000000);
    }
    
    // Material-zu-Skill Mappings
    private static final Map<Material, Skill> MINING_MATERIALS = new HashMap<>();
    private static final Map<Material, Skill> FORAGING_MATERIALS = new HashMap<>();
    private static final Map<Material, Skill> FARMING_MATERIALS = new HashMap<>();
    
    static {
        // Mining Materials
        MINING_MATERIALS.put(Material.COAL_ORE, Skill.MINING);
        MINING_MATERIALS.put(Material.IRON_ORE, Skill.MINING);
        MINING_MATERIALS.put(Material.GOLD_ORE, Skill.MINING);
        MINING_MATERIALS.put(Material.DIAMOND_ORE, Skill.MINING);
        MINING_MATERIALS.put(Material.EMERALD_ORE, Skill.MINING);
        MINING_MATERIALS.put(Material.REDSTONE_ORE, Skill.MINING);
        MINING_MATERIALS.put(Material.LAPIS_ORE, Skill.MINING);
        MINING_MATERIALS.put(Material.NETHER_QUARTZ_ORE, Skill.MINING);
        MINING_MATERIALS.put(Material.NETHER_GOLD_ORE, Skill.MINING);
        MINING_MATERIALS.put(Material.ANCIENT_DEBRIS, Skill.MINING);
        MINING_MATERIALS.put(Material.COBBLESTONE, Skill.MINING);
        MINING_MATERIALS.put(Material.STONE, Skill.MINING);
        MINING_MATERIALS.put(Material.DEEPSLATE, Skill.MINING);
        MINING_MATERIALS.put(Material.GRANITE, Skill.MINING);
        MINING_MATERIALS.put(Material.DIORITE, Skill.MINING);
        MINING_MATERIALS.put(Material.ANDESITE, Skill.MINING);
        MINING_MATERIALS.put(Material.SAND, Skill.MINING);
        MINING_MATERIALS.put(Material.GRAVEL, Skill.MINING);
        MINING_MATERIALS.put(Material.CLAY, Skill.MINING);
        
        // Foraging Materials
        FORAGING_MATERIALS.put(Material.OAK_LOG, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.BIRCH_LOG, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.SPRUCE_LOG, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.JUNGLE_LOG, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.ACACIA_LOG, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.DARK_OAK_LOG, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.MANGROVE_LOG, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.CHERRY_LOG, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.OAK_LEAVES, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.BIRCH_LEAVES, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.SPRUCE_LEAVES, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.JUNGLE_LEAVES, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.ACACIA_LEAVES, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.DARK_OAK_LEAVES, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.MANGROVE_LEAVES, Skill.FORAGING);
        FORAGING_MATERIALS.put(Material.CHERRY_LEAVES, Skill.FORAGING);
        
        // Farming Materials
        FARMING_MATERIALS.put(Material.WHEAT, Skill.FARMING);
        FARMING_MATERIALS.put(Material.CARROT, Skill.FARMING);
        FARMING_MATERIALS.put(Material.POTATO, Skill.FARMING);
        FARMING_MATERIALS.put(Material.BEETROOT, Skill.FARMING);
        FARMING_MATERIALS.put(Material.NETHER_WART, Skill.FARMING);
        FARMING_MATERIALS.put(Material.SUGAR_CANE, Skill.FARMING);
        FARMING_MATERIALS.put(Material.CACTUS, Skill.FARMING);
        FARMING_MATERIALS.put(Material.MELON, Skill.FARMING);
        FARMING_MATERIALS.put(Material.PUMPKIN, Skill.FARMING);
        FARMING_MATERIALS.put(Material.COCOA_BEANS, Skill.FARMING);
        FARMING_MATERIALS.put(Material.MUSHROOM_STEM, Skill.FARMING);
        FARMING_MATERIALS.put(Material.BROWN_MUSHROOM_BLOCK, Skill.FARMING);
        FARMING_MATERIALS.put(Material.RED_MUSHROOM_BLOCK, Skill.FARMING);
    }
    
    public SkillsSystem(SkyblockPlugin plugin, SkyblockManager skyblockManager) {
        this.plugin = plugin;
        this.skyblockManager = skyblockManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Gibt das aktuelle Level eines Skills für einen Spieler zurück
     */
    public int getSkillLevel(UUID playerId, Skill skill) {
        return playerSkillLevels.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
            .getOrDefault(skill.name(), 0);
    }
    
    /**
     * Gibt die aktuelle XP eines Skills für einen Spieler zurück
     */
    public int getSkillXP(UUID playerId, Skill skill) {
        return playerSkillXP.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
            .getOrDefault(skill.name(), 0);
    }
    
    /**
     * Fügt XP zu einem Skill hinzu
     */
    public void addSkillXP(UUID playerId, Skill skill, int xp) {
        Map<String, Integer> skillXP = playerSkillXP.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>());
        Map<String, Integer> skillLevels = playerSkillLevels.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>());
        
        int currentXP = skillXP.getOrDefault(skill.name(), 0);
        int currentLevel = skillLevels.getOrDefault(skill.name(), 0);
        
        int newXP = currentXP + xp;
        skillXP.put(skill.name(), newXP);
        
        // Prüfe Level-Up
        int newLevel = calculateLevel(newXP);
        if (newLevel > currentLevel) {
            skillLevels.put(skill.name(), newLevel);
            onSkillLevelUp(playerId, skill, newLevel);
        }
        
        // Speichere in SkyblockManager
        if (skyblockManager != null) {
            SkyblockSkills skills = skyblockManager.getSkills(playerId);
            if (skills != null) {
                // skills.setLevel(skill.name(), newLevel); // Method not implemented yet
            }
        }
    }
    
    /**
     * Berechnet das Level basierend auf XP
     */
    private int calculateLevel(int totalXP) {
        int level = 0;
        int xpNeeded = 0;
        
        for (int i = 1; i <= 60; i++) {
            xpNeeded += XP_TABLE.getOrDefault(i, 0);
            if (totalXP >= xpNeeded) {
                level = i;
            } else {
                break;
            }
        }
        
        return level;
    }
    
    /**
     * Berechnet die XP für das nächste Level
     */
    public int getXPForNextLevel(UUID playerId, Skill skill) {
        int currentLevel = getSkillLevel(playerId, skill);
        int currentXP = getSkillXP(playerId, skill);
        
        int xpNeeded = 0;
        for (int i = 1; i <= currentLevel + 1; i++) {
            xpNeeded += XP_TABLE.getOrDefault(i, 0);
        }
        
        return xpNeeded - currentXP;
    }
    
    /**
     * Event-Handler für Block-Break (Mining, Foraging, Farming)
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material material = block.getType();
        
        // Mining
        if (MINING_MATERIALS.containsKey(material)) {
            int xp = getMiningXP(material);
            addSkillXP(player.getUniqueId(), Skill.MINING, xp);
            sendXPMessage(player, Skill.MINING, xp);
        }
        
        // Foraging
        if (FORAGING_MATERIALS.containsKey(material)) {
            int xp = getForagingXP(material);
            addSkillXP(player.getUniqueId(), Skill.FORAGING, xp);
            sendXPMessage(player, Skill.FORAGING, xp);
        }
        
        // Farming
        if (FARMING_MATERIALS.containsKey(material)) {
            int xp = getFarmingXP(material);
            addSkillXP(player.getUniqueId(), Skill.FARMING, xp);
            sendXPMessage(player, Skill.FARMING, xp);
        }
    }
    
    /**
     * Event-Handler für Entity-Death (Combat)
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            int xp = getCombatXP(event.getEntity().getType().name());
            addSkillXP(player.getUniqueId(), Skill.COMBAT, xp);
            sendXPMessage(player, Skill.COMBAT, xp);
        }
    }
    
    /**
     * Event-Handler für Fishing
     */
    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Player player = event.getPlayer();
            int xp = getFishingXP();
            addSkillXP(player.getUniqueId(), Skill.FISHING, xp);
            sendXPMessage(player, Skill.FISHING, xp);
        }
    }
    
    /**
     * Gibt Mining-XP für ein Material zurück
     */
    private int getMiningXP(Material material) {
        return switch (material) {
            case COAL_ORE -> 5;
            case IRON_ORE -> 8;
            case GOLD_ORE -> 12;
            case DIAMOND_ORE -> 20;
            case EMERALD_ORE -> 25;
            case REDSTONE_ORE -> 3;
            case LAPIS_ORE -> 6;
            case NETHER_QUARTZ_ORE -> 4;
            case NETHER_GOLD_ORE -> 10;
            case ANCIENT_DEBRIS -> 50;
            case COBBLESTONE, STONE -> 1;
            case DEEPSLATE -> 2;
            case GRANITE, DIORITE, ANDESITE -> 1;
            case SAND, GRAVEL -> 1;
            case CLAY -> 2;
            default -> 1;
        };
    }
    
    /**
     * Gibt Foraging-XP für ein Material zurück
     */
    private int getForagingXP(Material material) {
        return switch (material) {
            case OAK_LOG, BIRCH_LOG, SPRUCE_LOG -> 2;
            case JUNGLE_LOG, ACACIA_LOG, DARK_OAK_LOG -> 3;
            case MANGROVE_LOG, CHERRY_LOG -> 4;
            case OAK_LEAVES, BIRCH_LEAVES, SPRUCE_LEAVES -> 1;
            case JUNGLE_LEAVES, ACACIA_LEAVES, DARK_OAK_LEAVES -> 2;
            case MANGROVE_LEAVES, CHERRY_LEAVES -> 3;
            default -> 1;
        };
    }
    
    /**
     * Gibt Farming-XP für ein Material zurück
     */
    private int getFarmingXP(Material material) {
        return switch (material) {
            case WHEAT -> 2;
            case CARROT, POTATO -> 3;
            case BEETROOT -> 2;
            case NETHER_WART -> 4;
            case SUGAR_CANE -> 2;
            case CACTUS -> 3;
            case MELON, PUMPKIN -> 5;
            case COCOA_BEANS -> 2;
            case MUSHROOM_STEM, BROWN_MUSHROOM_BLOCK, RED_MUSHROOM_BLOCK -> 4;
            default -> 1;
        };
    }
    
    /**
     * Gibt Combat-XP für eine Entity zurück
     */
    private int getCombatXP(String entityType) {
        return switch (entityType) {
            case "ZOMBIE" -> 5;
            case "SKELETON" -> 6;
            case "SPIDER" -> 4;
            case "CREEPER" -> 8;
            case "ENDERMAN" -> 15;
            case "BLAZE" -> 12;
            case "GHAST" -> 20;
            case "WITHER_SKELETON" -> 25;
            case "ENDER_DRAGON" -> 1000;
            case "WITHER" -> 500;
            default -> 2;
        };
    }
    
    /**
     * Gibt Fishing-XP zurück
     */
    private int getFishingXP() {
        return 5; // Base fishing XP
    }
    
    /**
     * Sendet XP-Nachricht an Spieler
     */
    private void sendXPMessage(Player player, Skill skill, int xp) {
        player.sendMessage("§a+" + xp + " " + skill.getIcon() + " " + skill.getGermanName() + " XP");
    }
    
    /**
     * Wird aufgerufen wenn ein Spieler ein Level aufsteigt
     */
    private void onSkillLevelUp(UUID playerId, Skill skill, int newLevel) {
        Player player = plugin.getServer().getPlayer(playerId);
        if (player != null) {
            player.sendMessage("§6§lSKILL LEVEL UP!");
            player.sendMessage("§e" + skill.getIcon() + " " + skill.getGermanName() + " Level " + newLevel);
            player.sendMessage("§7" + skill.getDescription());
            
            // Spiele Sound und Effekte
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
    }
    
    /**
     * Gibt alle Skills eines Spielers zurück
     */
    public Map<Skill, Integer> getAllSkillLevels(UUID playerId) {
        Map<Skill, Integer> levels = new HashMap<>();
        for (Skill skill : Skill.values()) {
            levels.put(skill, getSkillLevel(playerId, skill));
        }
        return levels;
    }
    
    /**
     * Gibt die Gesamt-Skill-Level eines Spielers zurück
     */
    public int getTotalSkillLevel(UUID playerId) {
        return getAllSkillLevels(playerId).values().stream()
            .mapToInt(Integer::intValue)
            .sum();
    }
    
    /**
     * Gibt die Skill-Boni für einen Spieler zurück
     */
    public Map<String, Double> getSkillBonuses(UUID playerId) {
        Map<String, Double> bonuses = new HashMap<>();
        Map<Skill, Integer> levels = getAllSkillLevels(playerId);
        
        // Combat Boni
        int combatLevel = levels.get(Skill.COMBAT);
        bonuses.put("damage", combatLevel * 0.5);
        bonuses.put("health", combatLevel * 2.0);
        
        // Mining Boni
        int miningLevel = levels.get(Skill.MINING);
        bonuses.put("mining_speed", miningLevel * 0.1);
        bonuses.put("mining_fortune", miningLevel * 0.05);
        
        // Foraging Boni
        int foragingLevel = levels.get(Skill.FORAGING);
        bonuses.put("foraging_speed", foragingLevel * 0.1);
        bonuses.put("foraging_fortune", foragingLevel * 0.05);
        
        // Farming Boni
        int farmingLevel = levels.get(Skill.FARMING);
        bonuses.put("farming_speed", farmingLevel * 0.1);
        bonuses.put("farming_fortune", farmingLevel * 0.05);
        
        // Fishing Boni
        int fishingLevel = levels.get(Skill.FISHING);
        bonuses.put("fishing_speed", fishingLevel * 0.1);
        bonuses.put("fishing_fortune", fishingLevel * 0.05);
        
        return bonuses;
    }
}
