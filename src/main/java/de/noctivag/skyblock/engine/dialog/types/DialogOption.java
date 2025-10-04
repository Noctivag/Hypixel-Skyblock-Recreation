package de.noctivag.skyblock.engine.dialog.types;

import java.util.*;

/**
 * Dialog Option
 * 
 * Represents a player choice in a dialog conversation.
 * Contains the option text, target node, and associated actions.
 */
public class DialogOption {
    
    private final String optionId;
    private final String displayText;
    private final String nextNodeId;
    private final List<DialogCondition> conditions;
    private final List<DialogAction> actions;
    
    public DialogOption(String optionId, String displayText, String nextNodeId, List<DialogCondition> conditions) {
        this.optionId = optionId;
        this.displayText = displayText;
        this.nextNodeId = nextNodeId;
        this.conditions = conditions != null ? new ArrayList<>(conditions) : new ArrayList<>();
        this.actions = new ArrayList<>();
    }
    
    public DialogOption(String optionId, String displayText, String nextNodeId, List<DialogCondition> conditions, List<DialogAction> actions) {
        this.optionId = optionId;
        this.displayText = displayText;
        this.nextNodeId = nextNodeId;
        this.conditions = conditions != null ? new ArrayList<>(conditions) : new ArrayList<>();
        this.actions = actions != null ? new ArrayList<>(actions) : new ArrayList<>();
    }
    
    public String getOptionId() {
        return optionId;
    }
    
    public String getDisplayText() {
        return displayText;
    }
    
    public String getNextNodeId() {
        return nextNodeId;
    }
    
    public List<DialogCondition> getConditions() {
        return new ArrayList<>(conditions);
    }
    
    public List<DialogAction> getActions() {
        return new ArrayList<>(actions);
    }
    
    /**
     * Add a condition
     */
    public void addCondition(DialogCondition condition) {
        conditions.add(condition);
    }
    
    /**
     * Add multiple conditions
     */
    public void addConditions(List<DialogCondition> conditions) {
        this.conditions.addAll(conditions);
    }
    
    /**
     * Add an action
     */
    public void addAction(DialogAction action) {
        actions.add(action);
    }
    
    /**
     * Add multiple actions
     */
    public void addActions(List<DialogAction> actions) {
        this.actions.addAll(actions);
    }
    
    /**
     * Check if this option is available for a player
     */
    public boolean isAvailable(UUID playerId) {
        for (DialogCondition condition : conditions) {
            if (!condition.isMet(playerId)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Execute all actions associated with this option
     */
    public void executeActions(UUID playerId) {
        for (DialogAction action : actions) {
            action.execute(playerId);
        }
    }
    
    /**
     * Get the next dialog node
     */
    public DialogNode getNextNode() {
        if (nextNodeId == null) {
            return null; // End of dialog
        }
        
        // TODO: Implement node lookup
        // This would return the next dialog node from the dialog engine
        return null; // Placeholder
    }
    
    /**
     * Get option type based on content
     */
    public OptionType getOptionType() {
        String lowerText = displayText.toLowerCase();
        
        if (lowerText.contains("yes") || lowerText.contains("accept") || lowerText.contains("agree")) {
            return OptionType.ACCEPTANCE;
        } else if (lowerText.contains("no") || lowerText.contains("decline") || lowerText.contains("disagree")) {
            return OptionType.DECLINE;
        } else if (lowerText.contains("back") || lowerText.contains("return")) {
            return OptionType.BACK;
        } else if (lowerText.contains("info") || lowerText.contains("tell me") || lowerText.contains("explain")) {
            return OptionType.INFORMATION;
        } else if (lowerText.contains("shop") || lowerText.contains("buy") || lowerText.contains("sell")) {
            return OptionType.SHOP;
        } else if (lowerText.contains("quest") || lowerText.contains("mission")) {
            return OptionType.QUEST;
        } else if (lowerText.contains("reward") || lowerText.contains("claim")) {
            return OptionType.REWARD;
        } else {
            return OptionType.GENERAL;
        }
    }
    
    /**
     * Get option difficulty based on conditions
     */
    public OptionDifficulty getOptionDifficulty() {
        int conditionCount = conditions.size();
        int complexConditions = (int) conditions.stream()
            .filter(condition -> condition.getComplexity() == DialogCondition.ConditionComplexity.COMPLEX)
            .count();
        
        if (complexConditions >= 2 || conditionCount >= 4) {
            return OptionDifficulty.EXPERT;
        } else if (complexConditions >= 1 || conditionCount >= 3) {
            return OptionDifficulty.ADVANCED;
        } else if (conditionCount >= 2) {
            return OptionDifficulty.INTERMEDIATE;
        } else {
            return OptionDifficulty.BASIC;
        }
    }
    
    /**
     * Get option priority based on type and conditions
     */
    public int getPriority() {
        int basePriority = switch (getOptionType()) {
            case ACCEPTANCE -> 100;
            case QUEST -> 90;
            case SHOP -> 80;
            case REWARD -> 70;
            case INFORMATION -> 60;
            case GENERAL -> 50;
            case BACK -> 40;
            case DECLINE -> 30;
        };
        
        // Reduce priority based on difficulty
        int difficultyPenalty = switch (getOptionDifficulty()) {
            case BASIC -> 0;
            case INTERMEDIATE -> -10;
            case ADVANCED -> -20;
            case EXPERT -> -30;
        };
        
        return basePriority + difficultyPenalty;
    }
    
    /**
     * Get formatted option text with priority indicator
     */
    public String getFormattedText() {
        String priorityIndicator = switch (getOptionDifficulty()) {
            case BASIC -> "§a"; // Green
            case INTERMEDIATE -> "§e"; // Yellow
            case ADVANCED -> "§6"; // Gold
            case EXPERT -> "§c"; // Red
        };
        
        return priorityIndicator + displayText;
    }
    
    /**
     * Check if this option ends the dialog
     */
    public boolean isEndOption() {
        return nextNodeId == null;
    }
    
    /**
     * Check if this option has conditions
     */
    public boolean hasConditions() {
        return !conditions.isEmpty();
    }
    
    /**
     * Check if this option has actions
     */
    public boolean hasActions() {
        return !actions.isEmpty();
    }
    
    /**
     * Get condition summary
     */
    public String getConditionSummary() {
        if (conditions.isEmpty()) {
            return "No conditions";
        }
        
        return String.format("%d condition(s): %s", 
            conditions.size(), 
            conditions.stream()
                .map(DialogCondition::getDescription)
                .reduce((a, b) -> a + ", " + b)
                .orElse(""));
    }
    
    /**
     * Get action summary
     */
    public String getActionSummary() {
        if (actions.isEmpty()) {
            return "No actions";
        }
        
        return String.format("%d action(s): %s", 
            actions.size(), 
            actions.stream()
                .map(DialogAction::getDescription)
                .reduce((a, b) -> a + ", " + b)
                .orElse(""));
    }
    
    /**
     * Get option summary
     */
    public String getOptionSummary() {
        return String.format("Option: %s | Type: %s | Difficulty: %s | Priority: %d", 
            optionId, getOptionType(), getOptionDifficulty(), getPriority());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        DialogOption that = (DialogOption) obj;
        return optionId.equals(that.optionId);
    }
    
    @Override
    public int hashCode() {
        return optionId.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("DialogOption{optionId='%s', displayText='%s', nextNodeId='%s', conditions=%d, actions=%d}", 
            optionId, displayText, nextNodeId, conditions.size(), actions.size());
    }
    
    /**
     * Option types
     */
    public enum OptionType {
        ACCEPTANCE("Acceptance"),
        DECLINE("Decline"),
        BACK("Back"),
        INFORMATION("Information"),
        SHOP("Shop"),
        QUEST("Quest"),
        REWARD("Reward"),
        GENERAL("General");
        
        private final String displayName;
        
        OptionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Option difficulty levels
     */
    public enum OptionDifficulty {
        BASIC("Basic"),
        INTERMEDIATE("Intermediate"),
        ADVANCED("Advanced"),
        EXPERT("Expert");
        
        private final String displayName;
        
        OptionDifficulty(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
