package de.noctivag.skyblock.bazaar;

import org.bukkit.Material;

/**
 * Bazaar Item for the Bazaar System
 */
public class BazaarItem {
    private final Material material;
    private final String name;
    private final double buyPrice;
    private final double sellPrice;
    private final int maxStock;
    private final int minStock;

    public BazaarItem(Material material, String name, double buyPrice, double sellPrice, int maxStock, int minStock) {
        this.material = material;
        this.name = name;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.maxStock = maxStock;
        this.minStock = minStock;
    }

    public Material getMaterial() { return material; }
    public String getName() { return name; }
    public double getBuyPrice() { return buyPrice; }
    public double getSellPrice() { return sellPrice; }
    public int getMaxStock() { return maxStock; }
    public int getMinStock() { return minStock; }
}
