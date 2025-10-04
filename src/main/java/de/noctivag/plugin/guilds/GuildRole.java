package de.noctivag.plugin.guilds;
import org.bukkit.inventory.ItemStack;

/**
 * GuildRole - Represents the role of a player in a guild
 */
public enum GuildRole {
    LEADER("Leader", 3),
    OFFICER("Officer", 2),
    MEMBER("Member", 1);
    
    private final String displayName;
    private final int permissionLevel;
    
    GuildRole(String displayName, int permissionLevel) {
        this.displayName = displayName;
        this.permissionLevel = permissionLevel;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getPermissionLevel() {
        return permissionLevel;
    }
    
    public boolean hasPermission(GuildRole other) {
        return this.permissionLevel >= other.permissionLevel;
    }
    
    public boolean canInvite() {
        return this == LEADER || this == OFFICER;
    }
    
    public boolean canKick() {
        return this == LEADER || this == OFFICER;
    }
    
    public boolean canPromote() {
        return this == LEADER;
    }
    
    public boolean canChangeSettings() {
        return this == LEADER || this == OFFICER;
    }
    
    public boolean canManageBank() {
        return this == LEADER || this == OFFICER;
    }
    
    public boolean canStartEvents() {
        return this == LEADER || this == OFFICER;
    }
}
