package de.noctivag.skyblock.engine.dialog;

import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents an active dialog session between a player and NPC
 */
public class DialogSession {
    private final UUID sessionId;
    private final UUID playerId;
    private final String npcId;
    private final List<DialogStep> steps;
    private final Map<String, Object> variables;
    private final LocalDateTime startTime;
    private int currentStep;
    private boolean active;
    private String lastResponse;

    public DialogSession(UUID sessionId, UUID playerId, String npcId) {
        this.sessionId = sessionId;
        this.playerId = playerId;
        this.npcId = npcId;
        this.steps = new ArrayList<>();
        this.variables = new HashMap<>();
        this.startTime = LocalDateTime.now();
        this.currentStep = 0;
        this.active = true;
        this.lastResponse = null;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getNpcId() {
        return npcId;
    }

    public List<DialogStep> getSteps() {
        return new ArrayList<>(steps);
    }

    public void addStep(DialogStep step) {
        steps.add(step);
    }

    public DialogStep getCurrentStep() {
        if (currentStep < steps.size()) {
            return steps.get(currentStep);
        }
        return null;
    }

    public boolean nextStep() {
        if (currentStep < steps.size() - 1) {
            currentStep++;
            return true;
        }
        return false;
    }

    public boolean previousStep() {
        if (currentStep > 0) {
            currentStep--;
            return true;
        }
        return false;
    }

    public void setCurrentStep(int step) {
        if (step >= 0 && step < steps.size()) {
            this.currentStep = step;
        }
    }

    public Map<String, Object> getVariables() {
        return new HashMap<>(variables);
    }

    public void setVariable(String key, Object value) {
        variables.put(key, value);
    }

    public Object getVariable(String key) {
        return variables.get(key);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public de.noctivag.skyblock.engine.dialog.types.DialogNode getCurrentNode() {
        if (currentStep >= 0 && currentStep < steps.size()) {
            return steps.get(currentStep).getNode();
        }
        return null;
    }
    
    public void setCurrentNode(de.noctivag.skyblock.engine.dialog.types.DialogNode node) {
        // This would need to be implemented based on your dialog structure
        // For now, we'll just update the current step
        this.currentStep = 0;
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(String lastResponse) {
        this.lastResponse = lastResponse;
    }

    public boolean isCompleted() {
        return currentStep >= steps.size() - 1;
    }

    public long getDuration() {
        return java.time.Duration.between(startTime, LocalDateTime.now()).toSeconds();
    }

    /**
     * Represents a step in the dialog
     */
    public static class DialogStep {
        private final String id;
        private final String message;
        private final List<String> options;
        private final Map<String, Object> conditions;
        private final Map<String, Object> actions;

        public DialogStep(String id, String message) {
            this.id = id;
            this.message = message;
            this.options = new ArrayList<>();
            this.conditions = new HashMap<>();
            this.actions = new HashMap<>();
        }

        public String getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }

        public List<String> getOptions() {
            return new ArrayList<>(options);
        }

        public void addOption(String option) {
            options.add(option);
        }

        public Map<String, Object> getConditions() {
            return new HashMap<>(conditions);
        }

        public void setCondition(String key, Object value) {
            conditions.put(key, value);
        }

        public Map<String, Object> getActions() {
            return new HashMap<>(actions);
        }

        public void setAction(String key, Object value) {
            actions.put(key, value);
        }
        
        public de.noctivag.skyblock.engine.dialog.types.DialogNode getNode() {
            // Create a simple DialogNode from this step
            List<de.noctivag.skyblock.engine.dialog.types.DialogCondition> conditionList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                conditionList.add(new de.noctivag.skyblock.engine.dialog.types.DialogCondition(
                    entry.getKey(), "==", entry.getValue().toString(), "Condition: " + entry.getKey()
                ));
            }
            return new de.noctivag.skyblock.engine.dialog.types.DialogNode(
                id, "npc", message, false, conditionList
            );
        }

        @Override
        public String toString() {
            return String.format("DialogStep{id='%s', message='%s', options=%d}", 
                               id, message.length() > 50 ? message.substring(0, 50) + "..." : message, options.size());
        }
    }
}
