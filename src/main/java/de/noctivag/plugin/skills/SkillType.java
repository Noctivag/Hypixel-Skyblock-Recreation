package de.noctivag.plugin.skills;
import org.bukkit.inventory.ItemStack;

public enum SkillType {
    MINING("Mining"),
    FORAGING("Foraging"),
    FISHING("Fishing"),
    COMBAT("Combat"),
    FARMING("Farming"),
    ENCHANTING("Enchanting"),
    ALCHEMY("Alchemy"),
    TAMING("Taming"),
    CARPENTRY("Carpentry"),
    RUNECRAFTING("Runecrafting"),
    SOCIAL("Social"),
    DUNGEONEERING("Dungeoneering");

    private final String displayName;

    SkillType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
