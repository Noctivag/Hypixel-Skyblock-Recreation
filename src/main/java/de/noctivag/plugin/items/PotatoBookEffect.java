package de.noctivag.plugin.items;

/**
 * Potato book effect class
 */
public class PotatoBookEffect {
    
    private final String effectType;
    private final double value;
    private final String description;
    
    public PotatoBookEffect(String effectType, double value, String description) {
        this.effectType = effectType;
        this.value = value;
        this.description = description;
    }
    
    public String getEffectType() {
        return effectType;
    }
    
    public double getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
}
