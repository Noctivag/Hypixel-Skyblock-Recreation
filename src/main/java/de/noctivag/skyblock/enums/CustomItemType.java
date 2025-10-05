package de.noctivag.skyblock.enums;

import org.bukkit.Material;

/**
 * Enum für verschiedene Custom Item Typen
 */
public enum CustomItemType {
    
    // Wither Blades
    HYPERION("§6Hyperion", Material.IRON_SWORD, Rarity.LEGENDARY, "Wither Blade mit Implosion-Fähigkeit"),
    SCYLLA("§6Scylla", Material.IRON_SWORD, Rarity.LEGENDARY, "Wither Blade mit Heilungs-Fähigkeit"),
    VALKYRIE("§6Valkyrie", Material.IRON_SWORD, Rarity.LEGENDARY, "Wither Blade mit Teleportations-Fähigkeit"),
    ASTRAEA("§6Astraea", Material.IRON_SWORD, Rarity.LEGENDARY, "Wither Blade mit Blitz-Fähigkeit"),
    
    // Mage Weapons
    SPIRIT_SCEPTRE("§5Spirit Sceptre", Material.STICK, Rarity.EPIC, "Verschießt zielsuchende Fledermaus-Projektile"),
    YETI_SWORD("§5Yeti Sword", Material.IRON_SWORD, Rarity.EPIC, "Verursacht Schneeball-Schaden"),
    
    // Bows
    TERMINATOR("§6Terminator", Material.BOW, Rarity.LEGENDARY, "Feuert drei Pfeile gleichzeitig mit Salvation-Fähigkeit"),
    JUJU_SHORTBOW("§5Juju Shortbow", Material.BOW, Rarity.EPIC, "Schneller Bogen mit hohem Schaden"),
    
    // Axes
    DAEDALUS_AXE("§6Daedalus Axe", Material.DIAMOND_AXE, Rarity.LEGENDARY, "Erhöht Schaden pro 50 Stärke massiv"),
    GIANTS_SWORD("§6Giant's Sword", Material.DIAMOND_SWORD, Rarity.LEGENDARY, "Riesiges Schwert mit hohem Schaden"),
    
    // Tools
    DIVANS_DRILL("§6Divan's Drill", Material.DIAMOND_PICKAXE, Rarity.LEGENDARY, "Bester Bohrer für Gemstones"),
    DRILL_CONTAINMENT_UNIT("§6Drill Containment Unit", Material.ANVIL, Rarity.LEGENDARY, "Spezielle Werkbank für Bohrer-Upgrades"),
    
    // Armor Sets
    SHADOW_ASSASSIN_HELMET("§5Shadow Assassin Helmet", Material.LEATHER_HELMET, Rarity.EPIC, "Teil des Shadow Assassin Sets"),
    SHADOW_ASSASSIN_CHESTPLATE("§5Shadow Assassin Chestplate", Material.LEATHER_CHESTPLATE, Rarity.EPIC, "Teil des Shadow Assassin Sets"),
    SHADOW_ASSASSIN_LEGGINGS("§5Shadow Assassin Leggings", Material.LEATHER_LEGGINGS, Rarity.EPIC, "Teil des Shadow Assassin Sets"),
    SHADOW_ASSASSIN_BOOTS("§5Shadow Assassin Boots", Material.LEATHER_BOOTS, Rarity.EPIC, "Teil des Shadow Assassin Sets"),
    
    NECRON_HELMET("§6Necron's Helmet", Material.LEATHER_HELMET, Rarity.LEGENDARY, "Teil des Necron Sets"),
    NECRON_CHESTPLATE("§6Necron's Chestplate", Material.LEATHER_CHESTPLATE, Rarity.LEGENDARY, "Teil des Necron Sets"),
    NECRON_LEGGINGS("§6Necron's Leggings", Material.LEATHER_LEGGINGS, Rarity.LEGENDARY, "Teil des Necron Sets"),
    NECRON_BOOTS("§6Necron's Boots", Material.LEATHER_BOOTS, Rarity.LEGENDARY, "Teil des Necron Sets"),
    
    SUPERIOR_DRAGON_HELMET("§6Superior Dragon Helmet", Material.LEATHER_HELMET, Rarity.LEGENDARY, "Teil des Superior Dragon Sets"),
    SUPERIOR_DRAGON_CHESTPLATE("§6Superior Dragon Chestplate", Material.LEATHER_CHESTPLATE, Rarity.LEGENDARY, "Teil des Superior Dragon Sets"),
    SUPERIOR_DRAGON_LEGGINGS("§6Superior Dragon Leggings", Material.LEATHER_LEGGINGS, Rarity.LEGENDARY, "Teil des Superior Dragon Sets"),
    SUPERIOR_DRAGON_BOOTS("§6Superior Dragon Boots", Material.LEATHER_BOOTS, Rarity.LEGENDARY, "Teil des Superior Dragon Sets"),
    
    // Accessories
    OVERFLUX_CAPACITOR("§6Overflux Capacitor", Material.REDSTONE, Rarity.LEGENDARY, "Erhöht Schaden massiv"),
    WARDEN_HEART("§6Warden Heart", Material.NETHER_STAR, Rarity.LEGENDARY, "Erhöht Gesundheit und Verteidigung massiv");
    
    private final String displayName;
    private final Material material;
    private final Rarity rarity;
    private final String description;
    
    CustomItemType(String displayName, Material material, Rarity rarity, String description) {
        this.displayName = displayName;
        this.material = material;
        this.rarity = rarity;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public Rarity getRarity() {
        return rarity;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Prüft ob das Item ein Wither Blade ist
     */
    public boolean isWitherBlade() {
        return this == HYPERION || this == SCYLLA || this == VALKYRIE || this == ASTRAEA;
    }
    
    /**
     * Prüft ob das Item eine Waffe ist
     */
    public boolean isWeapon() {
        return material == Material.IRON_SWORD || material == Material.DIAMOND_SWORD || 
               material == Material.DIAMOND_AXE || material == Material.BOW;
    }
    
    /**
     * Prüft ob das Item eine Rüstung ist
     */
    public boolean isArmor() {
        return material == Material.LEATHER_HELMET || material == Material.LEATHER_CHESTPLATE ||
               material == Material.LEATHER_LEGGINGS || material == Material.LEATHER_BOOTS;
    }
    
    /**
     * Prüft ob das Item ein Werkzeug ist
     */
    public boolean isTool() {
        return material == Material.DIAMOND_PICKAXE || material == Material.ANVIL;
    }
}
