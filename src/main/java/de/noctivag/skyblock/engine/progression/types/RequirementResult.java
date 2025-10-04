package de.noctivag.skyblock.engine.progression.types;

import java.util.List;

/**
 * Requirement Result
 * 
 * Contains the result of checking requirements for a player,
 * including whether all requirements are met and any failures.
 */
public class RequirementResult {
    
    private final boolean meetsAllRequirements;
    private final String message;
    private final List<RequirementFailure> failures;
    
    public RequirementResult(boolean meetsAllRequirements, String message, List<RequirementFailure> failures) {
        this.meetsAllRequirements = meetsAllRequirements;
        this.message = message;
        this.failures = failures;
    }
    
    public boolean isMeetsAllRequirements() {
        return meetsAllRequirements;
    }
    
    public String getMessage() {
        return message;
    }
    
    public List<RequirementFailure> getFailures() {
        return failures;
    }
    
    public boolean hasFailures() {
        return failures != null && !failures.isEmpty();
    }
    
    public int getFailureCount() {
        return failures != null ? failures.size() : 0;
    }
    
    @Override
    public String toString() {
        if (meetsAllRequirements) {
            return "✅ " + message;
        } else {
            return "❌ " + message + " (" + getFailureCount() + " failures)";
        }
    }
}
