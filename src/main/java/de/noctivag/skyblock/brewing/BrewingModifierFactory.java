package de.noctivag.skyblock.brewing;

/**
 * Factory for creating brewing modifiers
 */
public class BrewingModifierFactory {
    
    /**
     * Create speed modifier
     */
    public static BrewingModifier createSpeedModifier(double multiplier) {
        return new BrewingModifier("Speed", multiplier, "Increases brewing speed");
    }
    
    /**
     * Create quality modifier
     */
    public static BrewingModifier createQualityModifier(double multiplier) {
        return new BrewingModifier("Quality", multiplier, "Increases potion quality");
    }
    
    /**
     * Create experience modifier
     */
    public static BrewingModifier createExperienceModifier(double multiplier) {
        return new BrewingModifier("Experience", multiplier, "Increases experience gained");
    }
    
    /**
     * Create duration modifier
     */
    public static BrewingModifier createDurationModifier(double multiplier) {
        return new BrewingModifier("Duration", multiplier, "Increases effect duration");
    }
    
    /**
     * Create amplifier modifier
     */
    public static BrewingModifier createAmplifierModifier(double multiplier) {
        return new BrewingModifier("Amplifier", multiplier, "Increases effect amplifier");
    }
    
    /**
     * Create custom modifier
     */
    public static BrewingModifier createCustomModifier(String name, double multiplier, String description) {
        return new BrewingModifier(name, multiplier, description);
    }
}
