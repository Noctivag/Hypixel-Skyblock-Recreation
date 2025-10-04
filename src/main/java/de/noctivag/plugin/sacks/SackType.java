package de.noctivag.plugin.sacks;

import org.bukkit.Material;
import java.util.List;

/**
 * Sack Type class for different sack types
 */
public class SackType {
    private final String name;
    private final Material icon;
    private final SackCategory category;
    private final List<String> description;
    private final int cost;
    private final int capacity;

    public SackType(String name, Material icon, SackCategory category, List<String> description, int cost, int capacity) {
        this.name = name;
        this.icon = icon;
        this.category = category;
        this.description = description;
        this.cost = cost;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }

    public SackCategory getCategory() {
        return category;
    }

    public List<String> getDescription() {
        return description;
    }

    public int getCost() {
        return cost;
    }

    public int getCapacity() {
        return capacity;
    }

    public Material getMaterial() {
        return icon;
    }

    @Override
    public String toString() {
        return name;
    }
}
