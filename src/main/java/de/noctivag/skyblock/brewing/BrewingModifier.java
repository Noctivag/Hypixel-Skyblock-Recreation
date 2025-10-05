package de.noctivag.skyblock.brewing;

/**
 * Brewing modifier
 */
public class BrewingModifier {
    
    private final String name;
    private final double multiplier;
    private final String description;
    
    public BrewingModifier(String name, double multiplier, String description) {
        this.name = name;
        this.multiplier = multiplier;
        this.description = description;
    }
    
    // Getters
    public String getName() { return name; }
    public double getMultiplier() { return multiplier; }
    public String getDescription() { return description; }
}
