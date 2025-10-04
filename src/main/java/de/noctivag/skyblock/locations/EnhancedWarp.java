package de.noctivag.skyblock.locations;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;
import org.bukkit.Material;

public class EnhancedWarp extends Warp {
    private final WarpCategory category;
    private final Material icon;
    private final int minLevel;
    private final double price;
    private final boolean featured;

    public EnhancedWarp(String name, Location location, String permission, String description,
                       WarpCategory category, Material icon, int minLevel, double price, boolean featured) {
        super(name, location, permission, description);
        this.category = category;
        this.icon = icon;
        this.minLevel = minLevel;
        this.price = price;
        this.featured = featured;
    }

    public WarpCategory getCategory() {
        return category;
    }

    public Material getIcon() {
        return icon;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public double getPrice() {
        return price;
    }

    public boolean isFeatured() {
        return featured;
    }

    public enum WarpCategory {
        SPAWN("Spawn", Material.GRASS_BLOCK, "§aSpawn-Bereiche"),
        GAMES("Spiele", Material.DIAMOND_SWORD, "§bSpiel-Welten"),
        SHOPS("Shops", Material.EMERALD, "§2Handel & Wirtschaft"),
        MINING("Mining", Material.DIAMOND_PICKAXE, "§7Bergbau-Zonen"),
        FARMING("Farming", Material.WHEAT, "§6Landwirtschaft"),
        EVENTS("Events", Material.NETHER_STAR, "§5Event-Bereiche"),
        OTHER("Sonstiges", Material.COMPASS, "§7Weitere Orte");

        private final String displayName;
        private final Material icon;
        private final String description;

        WarpCategory(String displayName, Material icon, String description) {
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Material getIcon() {
            return icon;
        }

        public String getDescription() {
            return description;
        }
    }
}
