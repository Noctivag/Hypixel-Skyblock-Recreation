package de.noctivag.plugin.reforge;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Represents a reforge type with its effects
 */
public class ReforgeType {
    
    private final String name;
    private final List<String> effects;
    private final ReforgeRarity rarity;
    
    public ReforgeType(String name, List<String> effects, ReforgeRarity rarity) {
        this.name = name;
        this.effects = effects;
        this.rarity = rarity;
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getEffects() {
        return effects;
    }
    
    public ReforgeRarity getRarity() {
        return rarity;
    }
    
    @Override
    public String toString() {
        return "ReforgeType{" +
                "name='" + name + '\'' +
                ", effects=" + effects +
                ", rarity=" + rarity +
                '}';
    }
}
