package de.noctivag.plugin.features.npcs.quests;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class QuestReward {
    private final double coins;
    private final int experience;
    private final List<String> items;
    private final Map<String, Object> customRewards;

    public QuestReward(double coins, int experience, List<String> items, Map<String, Object> customRewards) {
        this.coins = coins;
        this.experience = experience;
        this.items = items;
        this.customRewards = customRewards;
    }

    public double getCoins() {
        return coins;
    }

    public int getExperience() {
        return experience;
    }

    public List<String> getItems() {
        return items;
    }

    public Map<String, Object> getCustomRewards() {
        return customRewards;
    }

    public boolean hasCoins() {
        return coins > 0;
    }

    public boolean hasExperience() {
        return experience > 0;
    }

    public boolean hasItems() {
        return items != null && !items.isEmpty();
    }

    public boolean hasCustomRewards() {
        return customRewards != null && !customRewards.isEmpty();
    }

    public String getFormattedReward() {
        StringBuilder sb = new StringBuilder();
        
        if (hasCoins()) {
            sb.append("§6").append(coins).append(" Coins");
        }
        
        if (hasExperience()) {
            if (sb.length() > 0) sb.append(" §7| ");
            sb.append("§a").append(experience).append(" XP");
        }
        
        if (hasItems()) {
            if (sb.length() > 0) sb.append(" §7| ");
            sb.append("§b").append(items.size()).append(" Items");
        }
        
        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormattedReward();
    }
}
