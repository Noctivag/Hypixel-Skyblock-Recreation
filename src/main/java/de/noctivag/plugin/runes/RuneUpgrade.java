package de.noctivag.plugin.runes;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Represents a rune upgrade with enhanced effects
 */
public class RuneUpgrade {
    
    private final String name;
    private final String baseRuneId;
    private final int level;
    private final List<String> effects;
    private final int cost;
    
    public RuneUpgrade(String name, String baseRuneId, int level, List<String> effects, int cost) {
        this.name = name;
        this.baseRuneId = baseRuneId;
        this.level = level;
        this.effects = effects;
        this.cost = cost;
    }
    
    public String getName() {
        return name;
    }
    
    public String getBaseRuneId() {
        return baseRuneId;
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
        return "RuneUpgrade{" +
                "name='" + name + '\'' +
                ", baseRuneId='" + baseRuneId + '\'' +
                ", level=" + level +
                ", effects=" + effects +
                ", cost=" + cost +
                '}';
    }
}
