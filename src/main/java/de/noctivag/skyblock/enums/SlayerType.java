package de.noctivag.skyblock.enums;

import org.bukkit.Material;

/**
 * Enum für Slayer-Boss-Typen
 * Definiert alle verfügbaren Slayer-Bosse und ihre Eigenschaften
 */
public enum SlayerType {
    
    REVENANT_HORROR(Material.ROTTEN_FLESH, "Revenant Horror", "Zombie-Slayer", 
                   "Bekämpfe mächtige Zombie-Bosse", 1, 5),
    TARANTULA_BROODFATHER(Material.SPIDER_EYE, "Tarantula Broodfather", "Spider-Slayer",
                         "Bekämpfe riesige Spinnen-Bosse", 1, 5),
    SVEN_PACKMASTER(Material.WOLF_SPAWN_EGG, "Sven Packmaster", "Wolf-Slayer",
                   "Bekämpfe wilde Wolf-Rudel", 1, 5),
    VOIDGLOOM_SERAPH(Material.ENDERMAN_SPAWN_EGG, "Voidgloom Seraph", "Enderman-Slayer",
                    "Bekämpfe mächtige Enderman-Bosse", 1, 5),
    INFERNO_DEMONLORD(Material.BLAZE_SPAWN_EGG, "Inferno Demonlord", "Blaze-Slayer",
                     "Bekämpfe höllische Blaze-Bosse", 1, 5);
    
    private final Material material;
    private final String englishName;
    private final String germanName;
    private final String description;
    private final int minTier;
    private final int maxTier;
    
    SlayerType(Material material, String englishName, String germanName, String description, int minTier, int maxTier) {
        this.material = material;
        this.englishName = englishName;
        this.germanName = germanName;
        this.description = description;
        this.minTier = minTier;
        this.maxTier = maxTier;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public String getEnglishName() {
        return englishName;
    }
    
    public String getGermanName() {
        return germanName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getMinTier() {
        return minTier;
    }
    
    public int getMaxTier() {
        return maxTier;
    }
    
    /**
     * Gibt den Slayer-Namen in der gewünschten Sprache zurück
     * @param useGerman true für Deutsch, false für Englisch
     * @return Slayer-Name
     */
    public String getName(boolean useGerman) {
        return useGerman ? germanName : englishName;
    }
    
    /**
     * Gibt die Kosten für einen Slayer-Tier zurück
     * @param tier Der Tier
     * @return Kosten in Coins
     */
    public double getCostForTier(int tier) {
        if (tier < minTier || tier > maxTier) {
            return 0;
        }
        
        // Formel: baseCost * (tier^2)
        double baseCost = 1000; // Basis-Kosten
        return baseCost * (tier * tier);
    }
    
    /**
     * Gibt die XP-Belohnung für einen Slayer-Tier zurück
     * @param tier Der Tier
     * @return XP-Belohnung
     */
    public int getXPRewardForTier(int tier) {
        if (tier < minTier || tier > maxTier) {
            return 0;
        }
        
        // Formel: baseXP * tier
        int baseXP = 100; // Basis-XP
        return baseXP * tier;
    }
    
    /**
     * Gibt den Slayer anhand des englischen Namens zurück
     * @param name Englischer Name
     * @return SlayerType oder null
     */
    public static SlayerType fromEnglishName(String name) {
        for (SlayerType slayer : values()) {
            if (slayer.englishName.equalsIgnoreCase(name)) {
                return slayer;
            }
        }
        return null;
    }
    
    /**
     * Gibt den Slayer anhand des deutschen Namens zurück
     * @param name Deutscher Name
     * @return SlayerType oder null
     */
    public static SlayerType fromGermanName(String name) {
        for (SlayerType slayer : values()) {
            if (slayer.germanName.equalsIgnoreCase(name)) {
                return slayer;
            }
        }
        return null;
    }
}
