package de.noctivag.skyblock.core.skills;

import java.util.UUID;

/**
 * Abstrakte Basisklasse für alle Skill-Datenobjekte (z.B. Foraging, Mining, etc.)
 */
public abstract class BaseSkillData {
    protected final UUID playerId;
    protected int level;
    protected int xp;

    public BaseSkillData(UUID playerId, int startLevel) {
        this.playerId = playerId;
        this.level = startLevel;
        this.xp = 0;
    }

    public UUID getPlayerId() { return playerId; }
    public int getLevel() { return level; }
    public int getXP() { return xp; }

    public void addXP(int amount) {
        this.xp += amount;
        checkLevelUp();
    }

    protected void checkLevelUp() {
        int requiredXP = getRequiredXP(level + 1);
        if (xp >= requiredXP) {
            level++;
        }
    }

    protected int getRequiredXP(int level) {
        return level * 1000;
    }
}
