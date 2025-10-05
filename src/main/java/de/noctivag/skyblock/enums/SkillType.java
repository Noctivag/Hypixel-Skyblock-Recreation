package de.noctivag.skyblock.enums;

import org.bukkit.Material;

/**
 * Enum für alle verfügbaren Skill-Typen
 * Definiert Material, Namen und Beschreibungen für jeden Skill
 */
public enum SkillType {
    
    FARMING(Material.WHEAT, "Farming", "Landwirtschaft", "Erhöht die Ernte-Ausbeute und Farm-Effizienz"),
    MINING(Material.DIAMOND_PICKAXE, "Mining", "Bergbau", "Erhöht die Mining-Geschwindigkeit und Erz-Ausbeute"),
    FORAGING(Material.OAK_LOG, "Foraging", "Forstwirtschaft", "Erhöht die Holz-Sammlung und Baum-Effizienz"),
    FISHING(Material.FISHING_ROD, "Fishing", "Angeln", "Erhöht die Angel-Chancen und Fisch-Qualität"),
    COMBAT(Material.DIAMOND_SWORD, "Combat", "Kampf", "Erhöht den Schaden und die Kampf-Effizienz"),
    ENCHANTING(Material.ENCHANTING_TABLE, "Enchanting", "Verzauberung", "Erhöht die Verzauberungs-Qualität"),
    ALCHEMY(Material.BREWING_STAND, "Alchemy", "Alchemie", "Erhöht die Trank-Effizienz und -Dauer"),
    CARPENTRY(Material.CRAFTING_TABLE, "Carpentry", "Tischlerei", "Erhöht die Handwerks-Effizienz"),
    RUNECRAFTING(Material.END_STONE, "Runecrafting", "Runen-Handwerk", "Erhöht die Runen-Herstellung"),
    SOCIAL(Material.PLAYER_HEAD, "Social", "Sozial", "Erhöht die Team-Boni und Kooperation");
    
    private final Material material;
    private final String englishName;
    private final String germanName;
    private final String description;
    
    SkillType(Material material, String englishName, String germanName, String description) {
        this.material = material;
        this.englishName = englishName;
        this.germanName = germanName;
        this.description = description;
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
    
    /**
     * Gibt den Skill-Namen in der gewünschten Sprache zurück
     * @param useGerman true für Deutsch, false für Englisch
     * @return Skill-Name
     */
    public String getName(boolean useGerman) {
        return useGerman ? germanName : englishName;
    }
    
    /**
     * Gibt den Skill anhand des englischen Namens zurück
     * @param name Englischer Name
     * @return SkillType oder null
     */
    public static SkillType fromEnglishName(String name) {
        for (SkillType skill : values()) {
            if (skill.englishName.equalsIgnoreCase(name)) {
                return skill;
            }
        }
        return null;
    }
    
    /**
     * Gibt den Skill anhand des deutschen Namens zurück
     * @param name Deutscher Name
     * @return SkillType oder null
     */
    public static SkillType fromGermanName(String name) {
        for (SkillType skill : values()) {
            if (skill.germanName.equalsIgnoreCase(name)) {
                return skill;
            }
        }
        return null;
    }
}
