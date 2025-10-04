package de.noctivag.plugin.features.npcs.quests;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class QuestObjective {
    private final String description;
    private final int targetAmount;
    private final int currentAmount;
    private final Map<String, Object> parameters;

    public QuestObjective(String description, int targetAmount, int currentAmount, Map<String, Object> parameters) {
        this.description = description;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.parameters = parameters;
    }

    public String getDescription() {
        return description;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public boolean isCompleted() {
        return currentAmount >= targetAmount;
    }

    public double getProgressPercentage() {
        if (targetAmount == 0) return 100.0;
        return Math.min(100.0, (double) currentAmount / targetAmount * 100.0);
    }

    public int getRemainingAmount() {
        return Math.max(0, targetAmount - currentAmount);
    }

    public String getProgressBar() {
        int progress = (int) (getProgressPercentage() / 10);
        StringBuilder bar = new StringBuilder("§a");
        
        for (int i = 0; i < 10; i++) {
            if (i < progress) {
                bar.append("█");
            } else {
                bar.append("§7█");
            }
        }
        
        return bar.toString();
    }

    public String getFormattedObjective() {
        return String.format("%s §7(%d/%d)", description, currentAmount, targetAmount);
    }

    @Override
    public String toString() {
        return getFormattedObjective();
    }
}
