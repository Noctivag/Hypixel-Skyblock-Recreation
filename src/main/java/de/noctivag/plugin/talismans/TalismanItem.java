package de.noctivag.plugin.talismans;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Represents a talisman item owned by a player
 */
public class TalismanItem {
    
    private final String talismanType;
    private final UUID ownerId;
    private final int level;
    private final boolean equipped;
    
    public TalismanItem(String talismanType, UUID ownerId, int level) {
        this.talismanType = talismanType;
        this.ownerId = ownerId;
        this.level = level;
        this.equipped = false;
    }
    
    public TalismanItem(String talismanType, UUID ownerId, int level, boolean equipped) {
        this.talismanType = talismanType;
        this.ownerId = ownerId;
        this.level = level;
        this.equipped = equipped;
    }
    
    public String getTalismanType() {
        return talismanType;
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
        return "TalismanItem{" +
                "talismanType='" + talismanType + '\'' +
                ", ownerId=" + ownerId +
                ", level=" + level +
                ", equipped=" + equipped +
                '}';
    }
}
