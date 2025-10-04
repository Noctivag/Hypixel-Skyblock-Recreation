package de.noctivag.skyblock.skyblock.items;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;

/**
 * Zentrale Klasse für alle Hypixel SkyBlock Items
 */
public class SkyBlockItems {
    
    // Item-Kategorien
    public enum ItemCategory {
        SWORD("Schwerter", Material.DIAMOND_SWORD, NamedTextColor.RED),
        BOW("Bögen", Material.BOW, NamedTextColor.GREEN),
        ARMOR("Rüstung", Material.DIAMOND_CHESTPLATE, NamedTextColor.BLUE),
        TOOL("Werkzeuge", Material.DIAMOND_PICKAXE, NamedTextColor.GOLD),
        ACCESSORY("Accessoires", Material.GOLDEN_APPLE, NamedTextColor.LIGHT_PURPLE),
        CONSUMABLE("Verbrauchsmaterial", Material.POTION, NamedTextColor.YELLOW),
        BLOCK("Blöcke", Material.STONE, NamedTextColor.GRAY),
        MISC("Verschiedenes", Material.ENDER_PEARL, NamedTextColor.WHITE);
        
        private final String displayName;
        private final Material material;
        private final TextColor color;
        
        ItemCategory(String displayName, Material material, TextColor color) {
            this.displayName = displayName;
            this.material = material;
            this.color = color;
        }
        
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public TextColor getColor() { return color; }
    }
    
    // Rarity-System
    public enum Rarity {
        COMMON("§fGewöhnlich", NamedTextColor.WHITE, 0),
        UNCOMMON("§aUngewöhnlich", NamedTextColor.GREEN, 1),
        RARE("§9Selten", NamedTextColor.BLUE, 2),
        EPIC("§5Episch", NamedTextColor.DARK_PURPLE, 3),
        LEGENDARY("§6Legendär", NamedTextColor.GOLD, 4),
        MYTHIC("§dMythisch", NamedTextColor.LIGHT_PURPLE, 5),
        DIVINE("§bGöttlich", NamedTextColor.AQUA, 6),
        SPECIAL("§cSpeziell", NamedTextColor.RED, 7),
        VERY_SPECIAL("§c§lSehr Speziell", NamedTextColor.RED, 8);
        
        private final String displayName;
        private final TextColor color;
        private final int level;
        
        Rarity(String displayName, TextColor color, int level) {
            this.displayName = displayName;
            this.color = color;
            this.level = level;
        }
        
        public String getDisplayName() { return displayName; }
        public TextColor getColor() { return color; }
        public int getLevel() { return level; }
    }
    
    // SkyBlock Item Definition
    public static class SkyBlockItem {
        private final String id;
        private final String name;
        private final ItemCategory category;
        private final Rarity rarity;
        private final Material material;
        private final int customModelData;
        private final List<String> description;
        private final Map<String, Object> stats;
        private final List<String> requirements;
        private final String skillRequirement;
        private final int skillLevel;
        
        public SkyBlockItem(String id, String name, ItemCategory category, Rarity rarity, 
                           Material material, int customModelData, List<String> description,
                           Map<String, Object> stats, List<String> requirements,
                           String skillRequirement, int skillLevel) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.rarity = rarity;
            this.material = material;
            this.customModelData = customModelData;
            this.description = description;
            this.stats = stats;
            this.requirements = requirements;
            this.skillRequirement = skillRequirement;
            this.skillLevel = skillLevel;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public ItemCategory getCategory() { return category; }
        public Rarity getRarity() { return rarity; }
        public Material getMaterial() { return material; }
        public int getCustomModelData() { return customModelData; }
        public List<String> getDescription() { return description; }
        public Map<String, Object> getStats() { return stats; }
        public List<String> getRequirements() { return requirements; }
        public String getSkillRequirement() { return skillRequirement; }
        public int getSkillLevel() { return skillLevel; }
        
        /**
         * Erstellt ein ItemStack für dieses SkyBlock Item
         */
        public ItemStack createItemStack() {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            
            // Name mit Rarity-Farbe
            meta.displayName(Component.text(rarity.getDisplayName() + " " + name)
                .color(rarity.getColor()));
            
            // Lore
            List<Component> lore = new ArrayList<>();
            
            // Kategorie
            lore.add(Component.text(category.getDisplayName())
                .color(category.getColor()));
            
            // Leerzeile
            lore.add(Component.text(""));
            
            // Beschreibung
            for (String desc : description) {
                lore.add(Component.text(desc).color(NamedTextColor.GRAY));
            }
            
            // Stats
            if (!stats.isEmpty()) {
                lore.add(Component.text(""));
                for (Map.Entry<String, Object> entry : stats.entrySet()) {
                    lore.add(Component.text("§7" + entry.getKey() + ": §f" + entry.getValue())
                        .color(NamedTextColor.GRAY));
                }
            }
            
            // Requirements
            if (!requirements.isEmpty()) {
                lore.add(Component.text(""));
                lore.add(Component.text("§cAnforderungen:").color(NamedTextColor.RED));
                for (String req : requirements) {
                    lore.add(Component.text("§7- " + req).color(NamedTextColor.GRAY));
                }
            }
            
            // Skill Requirement
            if (skillRequirement != null && skillLevel > 0) {
                lore.add(Component.text(""));
                lore.add(Component.text("§e" + skillRequirement + " Level " + skillLevel + " erforderlich")
                    .color(NamedTextColor.YELLOW));
            }
            
            // Rarity
            lore.add(Component.text(""));
            lore.add(Component.text(rarity.getDisplayName()).color(rarity.getColor()));
            
            meta.lore(lore);
            
            // Custom Model Data
            if (customModelData > 0) {
                meta.setCustomModelData(customModelData);
            }
            
            item.setItemMeta(meta);
            return item;
        }
    }
    
    // Item Registry
    private static final Map<String, SkyBlockItem> ITEMS = new HashMap<>();
    
    /**
     * Initialisiert alle SkyBlock Items
     */
    public static void initializeItems() {
        // === SCHWERTER ===
        registerItem(new SkyBlockItem(
            "ASPECT_OF_THE_END",
            "Aspect of the End",
            ItemCategory.SWORD,
            Rarity.RARE,
            Material.DIAMOND_SWORD,
            1001,
            Arrays.asList(
                "§7Ein legendäres Schwert",
                "§7aus dem End-Dimension.",
                "",
                "§7§7Teleportiert dich 8 Blöcke",
                "§7§7in Blickrichtung."
            ),
            Map.of(
                "Schaden", "100",
                "Stärke", "100",
                "Kritische Chance", "25%",
                "Kritischer Schaden", "50%"
            ),
            Arrays.asList("End Dimension erkunden"),
            "Combat",
            12
        ));
        
        registerItem(new SkyBlockItem(
            "ASPECT_OF_THE_DRAGONS",
            "Aspect of the Dragons",
            ItemCategory.SWORD,
            Rarity.LEGENDARY,
            Material.DIAMOND_SWORD,
            1002,
            Arrays.asList(
                "§7Ein mächtiges Schwert",
                "§7aus Drachenblut geschmiedet.",
                "",
                "§7§7Schießt einen Drachenatem",
                "§7§7der Feinde verletzt."
            ),
            Map.of(
                "Schaden", "225",
                "Stärke", "100",
                "Kritische Chance", "25%",
                "Kritischer Schaden", "50%"
            ),
            Arrays.asList("Drachen besiegen"),
            "Combat",
            18
        ));
        
        registerItem(new SkyBlockItem(
            "HYPERION",
            "Hyperion",
            ItemCategory.SWORD,
            Rarity.MYTHIC,
            Material.DIAMOND_SWORD,
            1003,
            Arrays.asList(
                "§7Ein katastrophales Schwert",
                "§7aus Wither-Katalysatoren.",
                "",
                "§7§7Teleportiert dich und",
                "§7§7verursacht Explosionsschaden."
            ),
            Map.of(
                "Schaden", "260",
                "Intelligenz", "300",
                "Kritische Chance", "25%",
                "Kritischer Schaden", "50%"
            ),
            Arrays.asList("Wither-Katalysatoren sammeln"),
            "Catacombs",
            30
        ));
        
        // === BÖGEN ===
        registerItem(new SkyBlockItem(
            "RAGNA_ROCK",
            "Runaan's Bow",
            ItemCategory.BOW,
            Rarity.RARE,
            Material.BOW,
            2001,
            Arrays.asList(
                "§7Ein magischer Bogen",
                "§7der 3 Pfeile gleichzeitig",
                "§7schießt.",
                "",
                "§7§7Schießt 2 zusätzliche",
                "§7§7Pfeile auf nahe Feinde."
            ),
            Map.of(
                "Schaden", "160",
                "Stärke", "50",
                "Kritische Chance", "25%",
                "Kritischer Schaden", "50%"
            ),
            Arrays.asList("Spider's Den erkunden"),
            "Combat",
            9
        ));
        
        registerItem(new SkyBlockItem(
            "MAGMA_BOW",
            "Magma Bow",
            ItemCategory.BOW,
            Rarity.EPIC,
            Material.BOW,
            2002,
            Arrays.asList(
                "§7Ein Bogen aus Magma",
                "§7der Feinde verbrennt.",
                "",
                "§7§7Verursacht Brandschaden",
                "§7§7über 3 Sekunden."
            ),
            Map.of(
                "Schaden", "180",
                "Stärke", "75",
                "Kritische Chance", "25%",
                "Kritischer Schaden", "50%"
            ),
            Arrays.asList("Blazing Fortress erkunden"),
            "Combat",
            15
        ));
        
        // === RÜSTUNG ===
        registerItem(new SkyBlockItem(
            "DRAGON_ARMOR",
            "Dragon Armor",
            ItemCategory.ARMOR,
            Rarity.LEGENDARY,
            Material.DIAMOND_CHESTPLATE,
            3001,
            Arrays.asList(
                "§7Eine mächtige Rüstung",
                "§7aus Drachenschuppen.",
                "",
                "§7§7Gewährt Flugfähigkeit",
                "§7§7und Feuerresistenz."
            ),
            Map.of(
                "Verteidigung", "500",
                "Gesundheit", "200",
                "Stärke", "50",
                "Kritische Chance", "10%"
            ),
            Arrays.asList("Drachen besiegen"),
            "Combat",
            16
        ));
        
        registerItem(new SkyBlockItem(
            "SUPERIOR_DRAGON_ARMOR",
            "Superior Dragon Armor",
            ItemCategory.ARMOR,
            Rarity.MYTHIC,
            Material.DIAMOND_CHESTPLATE,
            3002,
            Arrays.asList(
                "§7Die stärkste Drachenrüstung",
                "§7mit überlegenen Eigenschaften.",
                "",
                "§7§7Erhöht alle Statistiken",
                "§7§7um 5%."
            ),
            Map.of(
                "Verteidigung", "600",
                "Gesundheit", "250",
                "Stärke", "75",
                "Kritische Chance", "15%"
            ),
            Arrays.asList("Superior Dragon besiegen"),
            "Combat",
            20
        ));
        
        // === WERKZEUGE ===
        registerItem(new SkyBlockItem(
            "DIAMOND_PICKAXE",
            "Diamond Pickaxe",
            ItemCategory.TOOL,
            Rarity.UNCOMMON,
            Material.DIAMOND_PICKAXE,
            4001,
            Arrays.asList(
                "§7Ein Standard-Diamantspitzhacke",
                "§7für das Mining."
            ),
            Map.of(
                "Mining-Geschwindigkeit", "100%",
                "Mining-Fortune", "0%"
            ),
            Arrays.asList("Mining Level 1"),
            "Mining",
            1
        ));
        
        registerItem(new SkyBlockItem(
            "STONKS",
            "Stonk",
            ItemCategory.TOOL,
            Rarity.RARE,
            Material.GOLDEN_PICKAXE,
            4002,
            Arrays.asList(
                "§7Ein magisches Werkzeug",
                "§7das Blöcke sofort abbaut.",
                "",
                "§7§7Bricht Blöcke sofort",
                "§7§7ohne Verzögerung."
            ),
            Map.of(
                "Mining-Geschwindigkeit", "∞",
                "Mining-Fortune", "0%"
            ),
            Arrays.asList("Mining Level 5"),
            "Mining",
            5
        ));
        
        registerItem(new SkyBlockItem(
            "JUNGLE_AXE",
            "Jungle Axe",
            ItemCategory.TOOL,
            Rarity.UNCOMMON,
            Material.WOODEN_AXE,
            4003,
            Arrays.asList(
                "§7Eine Axt für das",
                "§7Foraging im Dschungel.",
                "",
                "§7§7Bricht mehrere Blöcke",
                "§7§7gleichzeitig."
            ),
            Map.of(
                "Foraging-Geschwindigkeit", "150%",
                "Foraging-Fortune", "10%"
            ),
            Arrays.asList("Foraging Level 3"),
            "Foraging",
            3
        ));
        
        // === ACCESSOIRES ===
        registerItem(new SkyBlockItem(
            "ZOMBIE_TALISMAN",
            "Zombie Talisman",
            ItemCategory.ACCESSORY,
            Rarity.COMMON,
            Material.ROTTEN_FLESH,
            5001,
            Arrays.asList(
                "§7Ein Talisman der",
                "§7Zombie-Kraft verleiht.",
                "",
                "§7§7+5% Schaden gegen Zombies"
            ),
            Map.of(
                "Schaden gegen Zombies", "+5%"
            ),
            Arrays.asList("Zombie töten"),
            null,
            0
        ));
        
        registerItem(new SkyBlockItem(
            "SPIDER_TALISMAN",
            "Spider Talisman",
            ItemCategory.ACCESSORY,
            Rarity.UNCOMMON,
            Material.SPIDER_EYE,
            5002,
            Arrays.asList(
                "§7Ein Talisman der",
                "§7Spinnen-Kraft verleiht.",
                "",
                "§7§7+10% Schaden gegen Spinnen"
            ),
            Map.of(
                "Schaden gegen Spinnen", "+10%"
            ),
            Arrays.asList("Spider's Den erkunden"),
            "Combat",
            5
        ));
        
        // === VERBRAUCHSMATERIAL ===
        registerItem(new SkyBlockItem(
            "HEALING_POTION",
            "Healing Potion",
            ItemCategory.CONSUMABLE,
            Rarity.COMMON,
            Material.POTION,
            6001,
            Arrays.asList(
                "§7Eine Heilungstrank",
                "§7die Gesundheit wiederherstellt.",
                "",
                "§7§7Stellt 100 Gesundheit wieder her"
            ),
            Map.of(
                "Heilung", "100 HP"
            ),
            Arrays.asList("Alchemie Level 1"),
            "Alchemy",
            1
        ));
        
        registerItem(new SkyBlockItem(
            "MANA_POTION",
            "Mana Potion",
            ItemCategory.CONSUMABLE,
            Rarity.UNCOMMON,
            Material.POTION,
            6002,
            Arrays.asList(
                "§7Eine Manatrank",
                "§7die Mana wiederherstellt.",
                "",
                "§7§7Stellt 200 Mana wieder her"
            ),
            Map.of(
                "Mana", "200 MP"
            ),
            Arrays.asList("Alchemie Level 5"),
            "Alchemy",
            5
        ));
        
        // === BLÖCKE ===
        registerItem(new SkyBlockItem(
            "ENCHANTED_DIAMOND",
            "Enchanted Diamond",
            ItemCategory.BLOCK,
            Rarity.UNCOMMON,
            Material.DIAMOND,
            7001,
            Arrays.asList(
                "§7Ein verzauberter Diamant",
                "§7mit magischen Eigenschaften.",
                "",
                "§7§7Wird für Crafting verwendet"
            ),
            Map.of(
                "Wert", "160 Diamanten"
            ),
            Arrays.asList("Mining Level 2"),
            "Mining",
            2
        ));
        
        registerItem(new SkyBlockItem(
            "ENCHANTED_EMERALD",
            "Enchanted Emerald",
            ItemCategory.BLOCK,
            Rarity.RARE,
            Material.EMERALD,
            7002,
            Arrays.asList(
                "§7Ein verzauberter Smaragd",
                "§7mit magischen Eigenschaften.",
                "",
                "§7§7Wird für Crafting verwendet"
            ),
            Map.of(
                "Wert", "160 Smaragde"
            ),
            Arrays.asList("Mining Level 8"),
            "Mining",
            8
        ));
        
        // === VERSCHIEDENES ===
        registerItem(new SkyBlockItem(
            "GRAVITY_TALISMAN",
            "Gravity Talisman",
            ItemCategory.ACCESSORY,
            Rarity.EPIC,
            Material.ENDER_PEARL,
            8001,
            Arrays.asList(
                "§7Ein Talisman der",
                "§7Schwerkraft manipuliert.",
                "",
                "§7§7Reduziert Fallschaden"
            ),
            Map.of(
                "Fallschaden", "-50%"
            ),
            Arrays.asList("The End erkunden"),
            "Combat",
            12
        ));
        
        registerItem(new SkyBlockItem(
            "SPEED_ARTIFACT",
            "Speed Artifact",
            ItemCategory.ACCESSORY,
            Rarity.LEGENDARY,
            Material.SUGAR,
            8002,
            Arrays.asList(
                "§7Ein Artefakt das",
                "§7Geschwindigkeit verleiht.",
                "",
                "§7§7+25% Bewegungsgeschwindigkeit"
            ),
            Map.of(
                "Geschwindigkeit", "+25%"
            ),
            Arrays.asList("Dungeons erkunden"),
            "Catacombs",
            20
        ));
    }
    
    /**
     * Registriert ein SkyBlock Item
     */
    private static void registerItem(SkyBlockItem item) {
        ITEMS.put(item.getId(), item);
    }
    
    /**
     * Gibt ein SkyBlock Item anhand der ID zurück
     */
    public static SkyBlockItem getItem(String id) {
        return ITEMS.get(id);
    }
    
    /**
     * Gibt alle registrierten Items zurück
     */
    public static Map<String, SkyBlockItem> getAllItems() {
        return new HashMap<>(ITEMS);
    }
    
    /**
     * Gibt Items einer bestimmten Kategorie zurück
     */
    public static List<SkyBlockItem> getItemsByCategory(ItemCategory category) {
        return ITEMS.values().stream()
            .filter(item -> item.getCategory() == category)
            .toList();
    }
    
    /**
     * Gibt Items einer bestimmten Rarity zurück
     */
    public static List<SkyBlockItem> getItemsByRarity(Rarity rarity) {
        return ITEMS.values().stream()
            .filter(item -> item.getRarity() == rarity)
            .toList();
    }
    
    /**
     * Gibt Items für ein bestimmtes Skill-Level zurück
     */
    public static List<SkyBlockItem> getItemsForSkillLevel(String skill, int level) {
        return ITEMS.values().stream()
            .filter(item -> item.getSkillRequirement() != null && 
                           item.getSkillRequirement().equals(skill) && 
                           item.getSkillLevel() <= level)
            .toList();
    }
    
    /**
     * Sucht Items nach Namen
     */
    public static List<SkyBlockItem> searchItems(String query) {
        String lowerQuery = query.toLowerCase();
        return ITEMS.values().stream()
            .filter(item -> item.getName().toLowerCase().contains(lowerQuery) ||
                           item.getId().toLowerCase().contains(lowerQuery))
            .toList();
    }
}
