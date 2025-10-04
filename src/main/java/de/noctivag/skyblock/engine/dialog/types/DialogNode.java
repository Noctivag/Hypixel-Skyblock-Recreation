package de.noctivag.skyblock.engine.dialog.types;

import java.util.*;

/**
 * Dialog Node
 * 
 * Represents a single dialog node in a conversation tree.
 * Contains the NPC's message, available options, and conditions.
 */
public class DialogNode {
    
    private final String nodeId;
    private final String npcId;
    private final String message;
    private final boolean isStartNode;
    private final List<DialogCondition> conditions;
    private final List<DialogOption> options;
    private final List<DialogAction> startActions;
    private final List<DialogAction> endActions;
    
    public DialogNode(String nodeId, String npcId, String message, boolean isStartNode, List<DialogCondition> conditions) {
        this.nodeId = nodeId;
        this.npcId = npcId;
        this.message = message;
        this.isStartNode = isStartNode;
        this.conditions = conditions != null ? new ArrayList<>(conditions) : new ArrayList<>();
        this.options = new ArrayList<>();
        this.startActions = new ArrayList<>();
        this.endActions = new ArrayList<>();
    }
    
    public String getNodeId() {
        return nodeId;
    }
    
    public String getNpcId() {
        return npcId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public boolean isStartNode() {
        return isStartNode;
    }
    
    public List<DialogCondition> getConditions() {
        return new ArrayList<>(conditions);
    }
    
    public List<DialogOption> getOptions() {
        return new ArrayList<>(options);
    }
    
    public List<DialogAction> getStartActions() {
        return new ArrayList<>(startActions);
    }
    
    public List<DialogAction> getEndActions() {
        return new ArrayList<>(endActions);
    }
    
    /**
     * Add a dialog option
     */
    public void addOption(DialogOption option) {
        options.add(option);
    }
    
    /**
     * Add multiple dialog options
     */
    public void addOptions(List<DialogOption> options) {
        this.options.addAll(options);
    }
    
    /**
     * Add a start action
     */
    public void addStartAction(DialogAction action) {
        startActions.add(action);
    }
    
    /**
     * Add multiple start actions
     */
    public void addStartActions(List<DialogAction> actions) {
        startActions.addAll(actions);
    }
    
    /**
     * Add an end action
     */
    public void addEndAction(DialogAction action) {
        endActions.add(action);
    }
    
    /**
     * Add multiple end actions
     */
    public void addEndActions(List<DialogAction> actions) {
        endActions.addAll(actions);
    }
    
    /**
     * Get a specific option by ID
     */
    public DialogOption getOption(String optionId) {
        return options.stream()
            .filter(option -> option.getOptionId().equals(optionId))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Check if this node is available for a player
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
     * Get available options for a player
     */
    public List<DialogOption> getAvailableOptions(UUID playerId) {
        return options.stream()
            .filter(option -> option.isAvailable(playerId))
            .toList();
    }
    
    /**
     * Execute start actions
     */
    public void executeStartActions(UUID playerId) {
        for (DialogAction action : startActions) {
            action.execute(playerId);
        }
    }
    
    /**
     * Execute end actions
     */
    public void executeEndActions(UUID playerId) {
        for (DialogAction action : endActions) {
            action.execute(playerId);
        }
    }
    
    /**
     * Get node type based on options
     */
    public NodeType getNodeType() {
        if (options.isEmpty()) {
            return NodeType.END;
        } else if (options.size() == 1) {
            return NodeType.LINEAR;
        } else {
            return NodeType.BRANCHING;
        }
    }
    
    /**
     * Get node difficulty based on conditions
     */
    public NodeDifficulty getNodeDifficulty() {
        int conditionCount = conditions.size();
        int complexConditions = (int) conditions.stream()
            .filter(condition -> condition.getComplexity() == DialogCondition.ConditionComplexity.COMPLEX)
            .count();
        
        if (complexConditions >= 3 || conditionCount >= 5) {
            return NodeDifficulty.EXPERT;
        } else if (complexConditions >= 2 || conditionCount >= 3) {
            return NodeDifficulty.ADVANCED;
        } else if (complexConditions >= 1 || conditionCount >= 2) {
            return NodeDifficulty.INTERMEDIATE;
        } else {
            return NodeDifficulty.BASIC;
        }
    }
    
    /**
     * Get node category based on content
     */
    public NodeCategory getNodeCategory() {
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("quest") || lowerMessage.contains("mission")) {
            return NodeCategory.QUEST;
        } else if (lowerMessage.contains("shop") || lowerMessage.contains("buy") || lowerMessage.contains("sell")) {
            return NodeCategory.SHOP;
        } else if (lowerMessage.contains("craft") || lowerMessage.contains("recipe")) {
            return NodeCategory.CRAFTING;
        } else if (lowerMessage.contains("info") || lowerMessage.contains("tell me")) {
            return NodeCategory.INFORMATION;
        } else if (lowerMessage.contains("reward") || lowerMessage.contains("claim")) {
            return NodeCategory.REWARD;
        } else {
            return NodeCategory.GENERAL;
        }
    }
    
    /**
     * Get formatted message with NPC name
     */
    public String getFormattedMessage() {
        return String.format("§e%s: §f%s", npcId, message);
    }
    
    /**
     * Get formatted options list
     */
    public List<String> getFormattedOptions() {
        List<String> formattedOptions = new ArrayList<>();
        
        for (int i = 0; i < options.size(); i++) {
            DialogOption option = options.get(i);
            formattedOptions.add(String.format("§a[%d] §f%s", i + 1, option.getDisplayText()));
        }
        
        return formattedOptions;
    }
    
    /**
     * Get node summary
     */
    public String getNodeSummary() {
        return String.format("Node: %s | Type: %s | Options: %d | Conditions: %d", 
            nodeId, getNodeType(), options.size(), conditions.size());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        DialogNode that = (DialogNode) obj;
        return nodeId.equals(that.nodeId);
    }
    
    @Override
    public int hashCode() {
        return nodeId.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("DialogNode{nodeId='%s', npcId='%s', message='%s', options=%d}", 
            nodeId, npcId, message, options.size());
    }
    
    /**
     * Node types
     */
    public enum NodeType {
        LINEAR("Linear"),
        BRANCHING("Branching"),
        END("End");
        
        private final String displayName;
        
        NodeType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Node difficulty levels
     */
    public enum NodeDifficulty {
        BASIC("Basic"),
        INTERMEDIATE("Intermediate"),
        ADVANCED("Advanced"),
        EXPERT("Expert");
        
        private final String displayName;
        
        NodeDifficulty(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Node categories
     */
    public enum NodeCategory {
        QUEST("Quest"),
        SHOP("Shop"),
        CRAFTING("Crafting"),
        INFORMATION("Information"),
        REWARD("Reward"),
        GENERAL("General");
        
        private final String displayName;
        
        NodeCategory(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
