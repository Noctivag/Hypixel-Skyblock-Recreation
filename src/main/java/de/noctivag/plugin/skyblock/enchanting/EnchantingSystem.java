package de.noctivag.plugin.skyblock.enchanting;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enchanting System für Hypixel SkyBlock
 */
public class EnchantingSystem {
    
    private final Plugin plugin;
    private final Map<UUID, Integer> playerEnchantingLevels = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> playerEnchantingXP = new ConcurrentHashMap<>();
    
    // SkyBlock Enchantment Definition
    public static class SkyBlockEnchantment {
        private final String id;
        private final String name;
        private final String description;
        private final EnchantmentType type;
        private final int maxLevel;
        private final List<Material> applicableItems;
        private final Map<Integer, Double> levelCosts;
        private final Map<Integer, String> levelDescriptions;
        
        public SkyBlockEnchantment(String id, String name, String description, EnchantmentType type, 
                                 int maxLevel, List<Material> applicableItems, 
                                 Map<Integer, Double> levelCosts, Map<Integer, String> levelDescriptions) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.type = type;
            this.maxLevel = maxLevel;
            this.applicableItems = applicableItems;
            this.levelCosts = levelCosts;
            this.levelDescriptions = levelDescriptions;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public EnchantmentType getType() { return type; }
        public int getMaxLevel() { return maxLevel; }
        public List<Material> getApplicableItems() { return applicableItems; }
        public Map<Integer, Double> getLevelCosts() { return levelCosts; }
        public Map<Integer, String> getLevelDescriptions() { return levelDescriptions; }
        
        public double getCostForLevel(int level) {
            return levelCosts.getOrDefault(level, 0.0);
        }
        
        public String getDescriptionForLevel(int level) {
            return levelDescriptions.getOrDefault(level, description);
        }
    }
    
    // Enchantment Types
    public enum EnchantmentType {
        SWORD("Schwert", "Schwert-Verzauberungen"),
        BOW("Bogen", "Bogen-Verzauberungen"),
        ARMOR("Rüstung", "Rüstungs-Verzauberungen"),
        TOOL("Werkzeug", "Werkzeug-Verzauberungen"),
        FISHING("Angeln", "Angel-Verzauberungen"),
        MINING("Mining", "Mining-Verzauberungen"),
        FARMING("Farming", "Farming-Verzauberungen"),
        FORAGING("Foraging", "Foraging-Verzauberungen");
        
        private final String name;
        private final String description;
        
        EnchantmentType(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
    }
    
    // Enchantment Registry
    private final Map<String, SkyBlockEnchantment> enchantments = new HashMap<>();
    
    public EnchantingSystem(Plugin plugin) {
        this.plugin = plugin;
        initializeEnchantments();
    }
    
    /**
     * Initialisiert alle SkyBlock Verzauberungen
     */
    private void initializeEnchantments() {
        // Sword Enchantments
        registerEnchantment(new SkyBlockEnchantment(
            "sharpness", "Sharpness", "Erhöht Schaden", EnchantmentType.SWORD, 7,
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.NETHERITE_SWORD),
            Map.of(1, 10.0, 2, 20.0, 3, 40.0, 4, 80.0, 5, 160.0, 6, 320.0, 7, 640.0),
            Map.of(1, "+5% Schaden", 2, "+10% Schaden", 3, "+15% Schaden", 4, "+20% Schaden", 
                   5, "+25% Schaden", 6, "+30% Schaden", 7, "+35% Schaden")
        ));
        
        registerEnchantment(new SkyBlockEnchantment(
            "smite", "Smite", "Erhöht Schaden gegen Untote", EnchantmentType.SWORD, 7,
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.NETHERITE_SWORD),
            Map.of(1, 10.0, 2, 20.0, 3, 40.0, 4, 80.0, 5, 160.0, 6, 320.0, 7, 640.0),
            Map.of(1, "+8% Schaden gegen Untote", 2, "+16% Schaden gegen Untote", 3, "+24% Schaden gegen Untote", 
                   4, "+32% Schaden gegen Untote", 5, "+40% Schaden gegen Untote", 6, "+48% Schaden gegen Untote", 7, "+56% Schaden gegen Untote")
        ));
        
        registerEnchantment(new SkyBlockEnchantment(
            "bane_of_arthropods", "Bane of Arthropods", "Erhöht Schaden gegen Gliederfüßer", EnchantmentType.SWORD, 7,
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.NETHERITE_SWORD),
            Map.of(1, 10.0, 2, 20.0, 3, 40.0, 4, 80.0, 5, 160.0, 6, 320.0, 7, 640.0),
            Map.of(1, "+8% Schaden gegen Gliederfüßer", 2, "+16% Schaden gegen Gliederfüßer", 3, "+24% Schaden gegen Gliederfüßer", 
                   4, "+32% Schaden gegen Gliederfüßer", 5, "+40% Schaden gegen Gliederfüßer", 6, "+48% Schaden gegen Gliederfüßer", 7, "+56% Schaden gegen Gliederfüßer")
        ));
        
        registerEnchantment(new SkyBlockEnchantment(
            "looting", "Looting", "Erhöht Drop-Chance", EnchantmentType.SWORD, 5,
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.NETHERITE_SWORD),
            Map.of(1, 15.0, 2, 30.0, 3, 60.0, 4, 120.0, 5, 240.0),
            Map.of(1, "+10% Drop-Chance", 2, "+20% Drop-Chance", 3, "+30% Drop-Chance", 
                   4, "+40% Drop-Chance", 5, "+50% Drop-Chance")
        ));
        
        registerEnchantment(new SkyBlockEnchantment(
            "fire_aspect", "Fire Aspect", "Verursacht Brandschaden", EnchantmentType.SWORD, 3,
            Arrays.asList(Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.NETHERITE_SWORD),
            Map.of(1, 20.0, 2, 40.0, 3, 80.0),
            Map.of(1, "3 Sekunden Brand", 2, "5 Sekunden Brand", 3, "7 Sekunden Brand")
        ));
        
        // Bow Enchantments
        registerEnchantment(new SkyBlockEnchantment(
            "power", "Power", "Erhöht Pfeilschaden", EnchantmentType.BOW, 7,
            Arrays.asList(Material.BOW, Material.CROSSBOW),
            Map.of(1, 10.0, 2, 20.0, 3, 40.0, 4, 80.0, 5, 160.0, 6, 320.0, 7, 640.0),
            Map.of(1, "+5% Pfeilschaden", 2, "+10% Pfeilschaden", 3, "+15% Pfeilschaden", 4, "+20% Pfeilschaden", 
                   5, "+25% Pfeilschaden", 6, "+30% Pfeilschaden", 7, "+35% Pfeilschaden")
        ));
        
        registerEnchantment(new SkyBlockEnchantment(
            "punch", "Punch", "Stößt Feinde zurück", EnchantmentType.BOW, 3,
            Arrays.asList(Material.BOW, Material.CROSSBOW),
            Map.of(1, 15.0, 2, 30.0, 3, 60.0),
            Map.of(1, "+2 Blöcke Rückstoß", 2, "+4 Blöcke Rückstoß", 3, "+6 Blöcke Rückstoß")
        ));
        
        registerEnchantment(new SkyBlockEnchantment(
            "flame", "Flame", "Pfeile entzünden Feinde", EnchantmentType.BOW, 1,
            Arrays.asList(Material.BOW, Material.CROSSBOW),
            Map.of(1, 25.0),
            Map.of(1, "Pfeile entzünden Feinde")
        ));
        
        // Armor Enchantments
        registerEnchantment(new SkyBlockEnchantment(
            "protection", "Protection", "Reduziert Schaden", EnchantmentType.ARMOR, 7,
            Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
                         Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
                         Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS),
            Map.of(1, 10.0, 2, 20.0, 3, 40.0, 4, 80.0, 5, 160.0, 6, 320.0, 7, 640.0),
            Map.of(1, "+2% Schadensreduktion", 2, "+4% Schadensreduktion", 3, "+6% Schadensreduktion", 4, "+8% Schadensreduktion", 
                   5, "+10% Schadensreduktion", 6, "+12% Schadensreduktion", 7, "+14% Schadensreduktion")
        ));
        
        registerEnchantment(new SkyBlockEnchantment(
            "fire_protection", "Fire Protection", "Reduziert Feuerschaden", EnchantmentType.ARMOR, 5,
            Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
                         Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
                         Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS),
            Map.of(1, 15.0, 2, 30.0, 3, 60.0, 4, 120.0, 5, 240.0),
            Map.of(1, "+5% Feuerschadensreduktion", 2, "+10% Feuerschadensreduktion", 3, "+15% Feuerschadensreduktion", 
                   4, "+20% Feuerschadensreduktion", 5, "+25% Feuerschadensreduktion")
        ));
        
        // Tool Enchantments
        registerEnchantment(new SkyBlockEnchantment(
            "efficiency", "Efficiency", "Erhöht Abbau-Geschwindigkeit", EnchantmentType.TOOL, 7,
            Arrays.asList(Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE,
                         Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SHOVEL, Material.IRON_HOE,
                         Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE),
            Map.of(1, 10.0, 2, 20.0, 3, 40.0, 4, 80.0, 5, 160.0, 6, 320.0, 7, 640.0),
            Map.of(1, "+10% Abbau-Geschwindigkeit", 2, "+20% Abbau-Geschwindigkeit", 3, "+30% Abbau-Geschwindigkeit", 4, "+40% Abbau-Geschwindigkeit", 
                   5, "+50% Abbau-Geschwindigkeit", 6, "+60% Abbau-Geschwindigkeit", 7, "+70% Abbau-Geschwindigkeit")
        ));
        
        registerEnchantment(new SkyBlockEnchantment(
            "fortune", "Fortune", "Erhöht Drop-Menge", EnchantmentType.TOOL, 5,
            Arrays.asList(Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE,
                         Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SHOVEL, Material.IRON_HOE,
                         Material.NETHERITE_PICKAXE, Material.NETHERITE_AXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE),
            Map.of(1, 15.0, 2, 30.0, 3, 60.0, 4, 120.0, 5, 240.0),
            Map.of(1, "+10% Drop-Menge", 2, "+20% Drop-Menge", 3, "+30% Drop-Menge", 
                   4, "+40% Drop-Menge", 5, "+50% Drop-Menge")
        ));
        
        // Fishing Enchantments
        registerEnchantment(new SkyBlockEnchantment(
            "luck_of_the_sea", "Luck of the Sea", "Erhöht Angel-Glück", EnchantmentType.FISHING, 5,
            Arrays.asList(Material.FISHING_ROD),
            Map.of(1, 20.0, 2, 40.0, 3, 80.0, 4, 160.0, 5, 320.0),
            Map.of(1, "+10% Angel-Glück", 2, "+20% Angel-Glück", 3, "+30% Angel-Glück", 
                   4, "+40% Angel-Glück", 5, "+50% Angel-Glück")
        ));
        
        registerEnchantment(new SkyBlockEnchantment(
            "lure", "Lure", "Erhöht Angel-Geschwindigkeit", EnchantmentType.FISHING, 5,
            Arrays.asList(Material.FISHING_ROD),
            Map.of(1, 15.0, 2, 30.0, 3, 60.0, 4, 120.0, 5, 240.0),
            Map.of(1, "+10% Angel-Geschwindigkeit", 2, "+20% Angel-Geschwindigkeit", 3, "+30% Angel-Geschwindigkeit", 
                   4, "+40% Angel-Geschwindigkeit", 5, "+50% Angel-Geschwindigkeit")
        ));
    }
    
    /**
     * Registriert eine Verzauberung
     */
    private void registerEnchantment(SkyBlockEnchantment enchantment) {
        enchantments.put(enchantment.getId(), enchantment);
    }
    
    /**
     * Verzaubert ein Item
     */
    public boolean enchantItem(Player player, ItemStack item, String enchantmentId, int level) {
        SkyBlockEnchantment enchantment = enchantments.get(enchantmentId);
        if (enchantment == null) {
            player.sendMessage("§cVerzauberung nicht gefunden!");
            return false;
        }
        
        if (level < 1 || level > enchantment.getMaxLevel()) {
            player.sendMessage("§cUngültiges Level!");
            return false;
        }
        
        if (!enchantment.getApplicableItems().contains(item.getType())) {
            player.sendMessage("§cDiese Verzauberung kann nicht auf dieses Item angewendet werden!");
            return false;
        }
        
        // Prüfe Enchanting Level
        int playerLevel = getEnchantingLevel(player.getUniqueId());
        int requiredLevel = getRequiredLevelForEnchantment(enchantment, level);
        if (playerLevel < requiredLevel) {
            player.sendMessage("§cDu benötigst Enchanting Level " + requiredLevel + "!");
            return false;
        }
        
        // Prüfe Kosten
        double cost = enchantment.getCostForLevel(level);
        // Hier würde normalerweise das Economy-System verwendet werden
        
        // Wende Verzauberung an
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // Hier würde die Verzauberung angewendet werden
            // meta.addEnchant(Enchantment.getByName(enchantmentId), level, true);
            item.setItemMeta(meta);
        }
        
        player.sendMessage("§aItem erfolgreich verzaubert!");
        player.sendMessage("§e" + enchantment.getName() + " " + level);
        player.sendMessage("§7" + enchantment.getDescriptionForLevel(level));
        
        return true;
    }
    
    /**
     * Fügt Enchanting XP hinzu
     */
    public void addEnchantingXP(UUID playerId, int xp) {
        // TODO: Fix data structure design - these should be simple Integer values, not Maps
        // Map<UUID, Integer> xpMap = playerEnchantingXP.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>());
        // Map<UUID, Integer> levelMap = playerEnchantingLevels.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>());
        int currentXP = 0; // Placeholder
        int currentLevel = 0; // Placeholder
        
        int newXP = currentXP + xp;
        // xpMap.put(playerId, newXP); // TODO: Fix data structure
        
        // Prüfe Level-Up
        int newLevel = calculateEnchantingLevel(newXP);
        if (newLevel > currentLevel) {
            // levelMap.put(playerId, newLevel); // TODO: Fix data structure
            onEnchantingLevelUp(playerId, newLevel);
        }
    }
    
    /**
     * Berechnet Enchanting Level basierend auf XP
     */
    private int calculateEnchantingLevel(int totalXP) {
        int level = 0;
        int xpNeeded = 0;
        
        for (int i = 1; i <= 60; i++) {
            xpNeeded += getXpRequiredForLevel(i);
            if (totalXP >= xpNeeded) {
                level = i;
            } else {
                break;
            }
        }
        
        return level;
    }
    
    /**
     * Gibt XP-Anforderung für ein Level zurück
     */
    private int getXpRequiredForLevel(int level) {
        return switch (level) {
            case 1 -> 50;
            case 2 -> 125;
            case 3 -> 200;
            case 4 -> 300;
            case 5 -> 500;
            case 6 -> 750;
            case 7 -> 1000;
            case 8 -> 1500;
            case 9 -> 2000;
            case 10 -> 3500;
            default -> 1000 + (level - 10) * 500;
        };
    }
    
    /**
     * Wird aufgerufen wenn ein Spieler ein Enchanting Level aufsteigt
     */
    private void onEnchantingLevelUp(UUID playerId, int newLevel) {
        Player player = plugin.getServer().getPlayer(playerId);
        if (player != null) {
            player.sendMessage("§6§lENCHANTING LEVEL UP!");
            player.sendMessage("§e✨ Enchanting Level " + newLevel);
            player.sendMessage("§7Du kannst jetzt höhere Verzauberungen anwenden!");
            
            // Spiele Sound und Effekte
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
    }
    
    /**
     * Gibt das Enchanting Level eines Spielers zurück
     */
    public int getEnchantingLevel(UUID playerId) {
        // TODO: Fix data structure design
        // return playerEnchantingLevels.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
        //     .getOrDefault(playerId, 0);
        return 0; // Placeholder
    }
    
    /**
     * Gibt die Enchanting XP eines Spielers zurück
     */
    public int getEnchantingXP(UUID playerId) {
        // TODO: Fix data structure design
        // return playerEnchantingXP.computeIfAbsent(playerId, k -> new ConcurrentHashMap<>())
        //     .getOrDefault(playerId, 0);
        return 0; // Placeholder
    }
    
    /**
     * Gibt das erforderliche Level für eine Verzauberung zurück
     */
    private int getRequiredLevelForEnchantment(SkyBlockEnchantment enchantment, int level) {
        return switch (enchantment.getType()) {
            case SWORD -> level * 2;
            case BOW -> level * 2;
            case ARMOR -> level * 3;
            case TOOL -> level * 2;
            case FISHING -> level * 3;
            case MINING -> level * 2;
            case FARMING -> level * 2;
            case FORAGING -> level * 2;
        };
    }
    
    /**
     * Gibt alle Verzauberungen zurück
     */
    public Map<String, SkyBlockEnchantment> getAllEnchantments() {
        return new HashMap<>(enchantments);
    }
    
    /**
     * Gibt Verzauberungen eines bestimmten Typs zurück
     */
    public List<SkyBlockEnchantment> getEnchantmentsByType(EnchantmentType type) {
        return enchantments.values().stream()
            .filter(enchantment -> enchantment.getType() == type)
            .toList();
    }
    
    /**
     * Gibt eine Verzauberung zurück
     */
    public SkyBlockEnchantment getEnchantment(String id) {
        return enchantments.get(id);
    }
}
