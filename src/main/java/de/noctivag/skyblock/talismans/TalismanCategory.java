package de.noctivag.skyblock.talismans;
import org.bukkit.Material;

/**
 * Categories for talismans
 */
public class TalismanCategory {

    private final String name;
    private final Material icon;
    private final String description;

    public TalismanCategory(String name, Material icon, String description) {
        this.name = name;
        this.icon = icon;
        this.description = description;
    }

    public String getName() { return name; }
    public Material getIcon() { return icon; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return name;
    }
}
