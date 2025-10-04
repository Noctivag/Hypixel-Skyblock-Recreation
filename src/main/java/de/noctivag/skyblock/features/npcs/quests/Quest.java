package de.noctivag.skyblock.features.npcs.quests;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.features.npcs.types.CompleteNPCType;

public class Quest {
    private final String id;
    private final String name;
    private final String description;
    private final QuestType type;
    private final QuestRarity rarity;
    private final int level;
    private final CompleteNPCType questGiver;
    private final QuestReward reward;
    private final QuestObjective objective;

    public Quest(String id, String name, String description, QuestType type, QuestRarity rarity, 
                 int level, CompleteNPCType questGiver, QuestReward reward, QuestObjective objective) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.rarity = rarity;
        this.level = level;
        this.questGiver = questGiver;
        this.reward = reward;
        this.objective = objective;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public QuestType getType() {
        return type;
    }

    public QuestRarity getRarity() {
        return rarity;
    }

    public int getLevel() {
        return level;
    }

    public CompleteNPCType getQuestGiver() {
        return questGiver;
    }

    public QuestReward getReward() {
        return reward;
    }

    public QuestObjective getObjective() {
        return objective;
    }

    public String getFormattedName() {
        return rarity.getColorCode() + "[" + type.getDisplayName() + "] " + name;
    }

    @Override
    public String toString() {
        return getFormattedName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Quest quest = (Quest) obj;
        return id.equals(quest.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
