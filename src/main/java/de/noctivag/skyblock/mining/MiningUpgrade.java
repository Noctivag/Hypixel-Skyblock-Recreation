package de.noctivag.skyblock.mining;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Represents a mining upgrade with its properties
 */
public class MiningUpgrade {
    
    private final String name;
    private final MiningUpgradeType type;
    private final int level;
    private final List<String> effects;
    private final int cost;
    
    public MiningUpgrade(String name, MiningUpgradeType type, int level, List<String> effects, int cost) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.effects = effects;
        this.cost = cost;
    }
    
    public String getName() {
        return name;
    }
    
    public MiningUpgradeType getType() {
        return type;
    }
    
    public int getLevel() {
        return level;
    }
    
    public List<String> getEffects() {
        return effects;
    }
    
    public int getCost() {
        return cost;
    }
    
    @Override
    public String toString() {
        return "MiningUpgrade{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", level=" + level +
                ", effects=" + effects +
                ", cost=" + cost +
                '}';
    }
}
