package de.noctivag.skyblock.farming;

import de.noctivag.skyblock.core.skills.BaseSkillData;
import java.util.UUID;

/**
 * PlayerFarmingData - Skill-Daten für das Farming-System (analog Hypixel Skyblock)
 */
public class PlayerFarmingData extends BaseSkillData {
    public PlayerFarmingData(UUID playerId) {
        super(playerId, 1);
    }

    public int getFarmingLevel() { return level; }
    public int getFarmingXP() { return xp; }
    public void addFarmingXP(int xp) { addXP(xp); }
}
