package de.noctivag.skyblock.engine.progression.types;

/**
 * Requirement Progress Item
 * 
 * Represents the progress status of a single requirement,
 * including current value, required value, and whether it's met.
 */
public class RequirementProgressItem {
    
    private final String description;
    private final Object currentValue;
    private final Object requiredValue;
    private final boolean met;
    
    public RequirementProgressItem(String description, Object currentValue, 
                                  Object requiredValue, boolean met) {
        this.description = description;
        this.currentValue = currentValue;
        this.requiredValue = requiredValue;
        this.met = met;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Object getCurrentValue() {
        return currentValue;
    }
    
    public Object getRequiredValue() {
        return requiredValue;
    }
    
    public boolean isMet() {
        return met;
    }
    
    public double getProgressPercentage() {
        if (currentValue instanceof Number current && requiredValue instanceof Number required) {
            double currentNum = current.doubleValue();
            double requiredNum = required.doubleValue();
            
            if (requiredNum == 0) return 100.0;
            
            double percentage = (currentNum / requiredNum) * 100.0;
            return Math.min(100.0, Math.max(0.0, percentage));
        }
        
        return met ? 100.0 : 0.0;
    }
    
    @Override
    public String toString() {
        String status = met ? "✅" : "❌";
        return String.format("%s %s: %s/%s (%.1f%%)", 
            status, description, currentValue, requiredValue, getProgressPercentage());
    }
}
