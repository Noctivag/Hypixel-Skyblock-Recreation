package de.noctivag.plugin.features.dungeons.loot;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a special dungeon item
 */
public class DungeonItem {
    
    private final String name;
    private final String description;
    private final ItemRarity rarity;
    private final ItemType type;
    private final int requiredFloor;
    
    public DungeonItem(String name, String description) {
        this(name, description, ItemRarity.RARE, ItemType.WEAPON, 1);
    }
    
    public DungeonItem(String name, String description, ItemRarity rarity, ItemType type, int requiredFloor) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.type = type;
        this.requiredFloor = requiredFloor;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public ItemRarity getRarity() {
        return rarity;
    }
    
    public ItemType getType() {
        return type;
    }
    
    public int getRequiredFloor() {
        return requiredFloor;
    }
    
    @Override
    public String toString() {
        return rarity.getColor() + name + " §7- " + description;
    }
}
