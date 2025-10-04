package de.noctivag.plugin.features.events;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.events.types.RewardType;
import de.noctivag.plugin.features.events.types.RewardRarity;
import org.bukkit.Material;

/**
 * Event Reward class for event rewards
 */
public class EventReward {
    private String id;
    private String name;
    private String description;
    private RewardType type;
    private RewardRarity rarity;
    private Material icon;
    private double value;
    private int amount;
    private String[] requirements;

    public EventReward(String id, String name, String description, RewardType type, RewardRarity rarity, Material icon, double value, int amount, String[] requirements) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.rarity = rarity;
        this.icon = icon;
        this.value = value;
        this.amount = amount;
        this.requirements = requirements;
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

    public RewardType getType() {
        return type;
    }

    public RewardRarity getRarity() {
        return rarity;
    }

    public Material getIcon() {
        return icon;
    }

    public double getValue() {
        return value;
    }

    public int getAmount() {
        return amount;
    }

    public String[] getRequirements() {
        return requirements;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(RewardType type) {
        this.type = type;
    }

    public void setRarity(RewardRarity rarity) {
        this.rarity = rarity;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setRequirements(String[] requirements) {
        this.requirements = requirements;
    }
}
