package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SeaGuardian extends SeaCreature {
    public SeaGuardian(int fishingLevel) {
        super("SEA_GUARDIAN", EntityType.GUARDIAN, null, 1500.0, 50.0, 25.0, 50.0, 5, "UNCOMMON");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §aAn §aUncommon §aSea Guardian §7appeared!";
    }

    @Override
    public String getName() {
        return "§aSea Guardian";
    }

    @Override
    public String getLootTableId() {
        return "sea_guardian";
    }

    @Override
    public int getXPReward() {
        return 25;
    }
}
