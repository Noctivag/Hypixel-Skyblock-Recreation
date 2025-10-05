package de.noctivag.skyblock.brewing;

import java.util.Map;

/**
 * Utility class for brewing modifiers
 */
public class BrewingModifierUtils {
    
    /**
     * Calculate total multiplier for type
     */
    public static double calculateTotalMultiplier(Map<BrewingModifierType, BrewingModifier> modifiers, BrewingModifierType type) {
        BrewingModifier modifier = modifiers.get(type);
        return modifier != null ? modifier.getMultiplier() : 1.0;
    }
    
    /**
     * Calculate combined multiplier
     */
    public static double calculateCombinedMultiplier(Map<BrewingModifierType, BrewingModifier> modifiers, BrewingModifierType... types) {
        double total = 1.0;
        for (BrewingModifierType type : types) {
            total *= calculateTotalMultiplier(modifiers, type);
        }
        return total;
    }
    
    /**
     * Check if modifiers are compatible
     */
    public static boolean areModifiersCompatible(BrewingModifier modifier1, BrewingModifier modifier2) {
        if (modifier1 == null || modifier2 == null) {
            return true;
        }
        
        // Check if modifiers have conflicting effects
        // Implementation would check for specific conflicts
        
        return true; // Placeholder
    }
    
    /**
     * Get modifier description
     */
    public static String getModifierDescription(BrewingModifier modifier) {
        if (modifier == null) {
            return "No modifier";
        }
        
        return modifier.getName() + " (" + modifier.getMultiplier() + "x): " + modifier.getDescription();
    }
}
