package de.noctivag.plugin.features.minions.upgrades;
import org.bukkit.inventory.ItemStack;

public class MinionUpgrade {
    private final UpgradeType type;
    private final String name;
    private final String description;
    private final double efficiencyMultiplier;
    private final int cost;
    private final boolean isActive;

    public MinionUpgrade(UpgradeType type, String name, String description, double efficiencyMultiplier, int cost) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.efficiencyMultiplier = efficiencyMultiplier;
        this.cost = cost;
        this.isActive = true;
    }

    public MinionUpgrade(UpgradeType type, String name, String description, double efficiencyMultiplier, int cost, boolean isActive) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.efficiencyMultiplier = efficiencyMultiplier;
        this.cost = cost;
        this.isActive = isActive;
    }

    public UpgradeType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getEfficiencyMultiplier() {
        return efficiencyMultiplier;
    }

    public int getCost() {
        return cost;
    }

    public boolean isActive() {
        return isActive;
    }

    public MinionUpgrade setActive(boolean active) {
        return new MinionUpgrade(type, name, description, efficiencyMultiplier, cost, active);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MinionUpgrade that = (MinionUpgrade) obj;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
