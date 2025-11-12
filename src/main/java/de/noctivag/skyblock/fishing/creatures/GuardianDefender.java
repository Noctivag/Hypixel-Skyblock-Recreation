package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class GuardianDefender extends SeaCreature {
    public GuardianDefender(int fishingLevel) {
        super("GUARDIAN_DEFENDER", EntityType.ELDER_GUARDIAN, null, 40000.0, 500.0, 250.0, 500.0, 22, "EPIC");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §5You've discovered a §5Guardian Defender §5of the sea!";
    }

    @Override
    public String getName() {
        return "§5Guardian Defender";
    }

    @Override
    public String getLootTableId() {
        return "guardian_defender";
    }

    @Override
    public int getXPReward() {
        return 250;
    }
}
