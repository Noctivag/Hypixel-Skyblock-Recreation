package de.noctivag.skyblock.models;

import de.noctivag.skyblock.enums.MinionUpgradeType;

/**
 * Repräsentiert ein Minion-Upgrade
 */
public class MinionUpgrade {
    
    private final MinionUpgradeType type;
    private final int level;
    private final long appliedTime;
    
    public MinionUpgrade(MinionUpgradeType type, int level) {
        this.type = type;
        this.level = level;
        this.appliedTime = System.currentTimeMillis();
    }
    
    public MinionUpgrade(MinionUpgradeType type, int level, long appliedTime) {
        this.type = type;
        this.level = level;
        this.appliedTime = appliedTime;
    }
    
    public MinionUpgradeType getType() {
        return type;
    }
    
    public int getLevel() {
        return level;
    }
    
    public long getAppliedTime() {
        return appliedTime;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MinionUpgrade that = (MinionUpgrade) obj;
        return type == that.type && level == that.level;
    }
    
    @Override
    public int hashCode() {
        return type.hashCode() * 31 + level;
    }
}
