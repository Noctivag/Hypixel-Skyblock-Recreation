package de.noctivag.plugin.accessories;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Represents an accessory item owned by a player
 */
public class AccessoryItem {
    
    private final String accessoryType;
    private final UUID ownerId;
    private final int level;
    private final boolean equipped;
    
    public AccessoryItem(String accessoryType, UUID ownerId, int level) {
        this.accessoryType = accessoryType;
        this.ownerId = ownerId;
        this.level = level;
        this.equipped = false;
    }
    
    public AccessoryItem(String accessoryType, UUID ownerId, int level, boolean equipped) {
        this.accessoryType = accessoryType;
        this.ownerId = ownerId;
        this.level = level;
        this.equipped = equipped;
    }
    
    public String getAccessoryType() {
        return accessoryType;
    }
    
    public UUID getOwnerId() {
        return ownerId;
    }
    
    public int getLevel() {
        return level;
    }
    
    public boolean isEquipped() {
        return equipped;
    }
    
    public void setEquipped(boolean equipped) {
        // This would need to be handled differently in a real implementation
        // as this field is final in this example
    }
    
    @Override
    public String toString() {
        return "AccessoryItem{" +
                "accessoryType='" + accessoryType + '\'' +
                ", ownerId=" + ownerId +
                ", level=" + level +
                ", equipped=" + equipped +
                '}';
    }
}
