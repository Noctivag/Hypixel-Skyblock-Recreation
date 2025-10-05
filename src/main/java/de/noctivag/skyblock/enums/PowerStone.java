package de.noctivag.skyblock.enums;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

/**
 * Enum für verschiedene Power Stones
 */
public enum PowerStone {
    
    BERSERKER("§cBerserker", Material.RED_DYE, NamedTextColor.RED, "Erhöht Stärke und Crit-Damage"),
    MAGE("§bMage", Material.LAPIS_LAZULI, NamedTextColor.AQUA, "Erhöht Intelligenz und Mana"),
    ARCHER("§aArcher", Material.ARROW, NamedTextColor.GREEN, "Erhöht Crit-Chance und Bogen-Schaden"),
    TANK("§eTank", Material.SHIELD, NamedTextColor.YELLOW, "Erhöht Gesundheit und Verteidigung"),
    HEALER("§dHealer", Material.GOLDEN_APPLE, NamedTextColor.LIGHT_PURPLE, "Erhöht Heilung und Regeneration"),
    ASSASSIN("§8Assassin", Material.IRON_SWORD, NamedTextColor.DARK_GRAY, "Erhöht Geschwindigkeit und Crit-Chance"),
    PALADIN("§6Paladin", Material.GOLDEN_SWORD, NamedTextColor.GOLD, "Erhöht Verteidigung und Heilung"),
    WARRIOR("§4Warrior", Material.DIAMOND_SWORD, NamedTextColor.DARK_RED, "Erhöht Stärke und Gesundheit");
    
    private final String displayName;
    private final Material material;
    private final NamedTextColor color;
    private final String description;
    
    PowerStone(String displayName, Material material, NamedTextColor color, String description) {
        this.displayName = displayName;
        this.material = material;
        this.color = color;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public NamedTextColor getColor() {
        return color;
    }
    
    public String getDescription() {
        return description;
    }
}
