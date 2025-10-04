package de.noctivag.skyblock.gemstones;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a gemstone slot on an item
 */
public class GemstoneSlot {
    
    private final String name;
    private final GemstoneCategory category;
    private final int slotNumber;
    
    public GemstoneSlot(String name, GemstoneCategory category, int slotNumber) {
        this.name = name;
        this.category = category;
        this.slotNumber = slotNumber;
    }
    
    public String getName() {
        return name;
    }
    
    public GemstoneCategory getCategory() {
        return category;
    }
    
    public int getSlotNumber() {
        return slotNumber;
    }
    
    @Override
    public String toString() {
        return "GemstoneSlot{" +
                "name='" + name + '\'' +
                ", category=" + category +
                ", slotNumber=" + slotNumber +
                '}';
    }
}
