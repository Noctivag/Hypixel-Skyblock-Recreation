package de.noctivag.skyblock.engine.progression;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents the result of a skill progression action
 */
public class SkillProgressResult {
    private final UUID playerId;
    private final String skillName;
    private final int previousLevel;
    private final int newLevel;
    private final double previousExp;
    private final double newExp;
    private final double expGained;
    private final LocalDateTime timestamp;
    private final Map<String, Object> metadata;

    public SkillProgressResult(UUID playerId, String skillName, int previousLevel, int newLevel,
                              double previousExp, double newExp, double expGained) {
        this.playerId = playerId;
        this.skillName = skillName;
        this.previousLevel = previousLevel;
        this.newLevel = newLevel;
        this.previousExp = previousExp;
        this.newExp = newExp;
        this.expGained = expGained;
        this.timestamp = LocalDateTime.now();
        this.metadata = new HashMap<>();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getSkillName() {
        return skillName;
    }

    public int getPreviousLevel() {
        return previousLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public double getPreviousExp() {
        return previousExp;
    }

    public double getNewExp() {
        return newExp;
    }

    public double getExpGained() {
        return expGained;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getMetadata() {
        return new HashMap<>(metadata);
    }

    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public boolean isLevelUp() {
        return newLevel > previousLevel;
    }

    public int getLevelsGained() {
        return newLevel - previousLevel;
    }

    public double getExpProgress() {
        // Calculate progress towards next level (0.0 to 1.0)
        double currentLevelExp = getExpForLevel(newLevel);
        double nextLevelExp = getExpForLevel(newLevel + 1);
        return (newExp - currentLevelExp) / (nextLevelExp - currentLevelExp);
    }

    private double getExpForLevel(int level) {
        // Simplified exp calculation - in real implementation this would be more complex
        return level * 100.0;
    }

    @Override
    public String toString() {
        return String.format("SkillProgressResult{playerId=%s, skill='%s', level %d->%d, exp %.1f->%.1f (+%.1f)}", 
                           playerId, skillName, previousLevel, newLevel, previousExp, newExp, expGained);
    }
}
