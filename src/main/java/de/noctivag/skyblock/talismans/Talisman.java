package de.noctivag.skyblock.talismans;

import org.bukkit.Material;

/**
 * Represents a talisman type/template
 */
public class Talisman {

    private final String id;
    private final String name;
    private final String displayName;
    private final Material material;
    private final String description;
    private final String category;
    private final TalismanRarity rarity;
    private final int maxLevel;
    private final double baseEffect;

    public Talisman(String id, String name, String displayName, Material material,
                   String description, String category, TalismanRarity rarity, int maxLevel, double baseEffect) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.material = material;
        this.description = description;
        this.category = category;
        this.rarity = rarity;
        this.maxLevel = maxLevel;
        this.baseEffect = baseEffect;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public Material getMaterial() { return material; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public TalismanRarity getRarity() { return rarity; }
    public int getMaxLevel() { return maxLevel; }
    public double getBaseEffect() { return baseEffect; }
}
