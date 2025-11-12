package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class BlueShark extends SeaCreature {
    public BlueShark(int fishingLevel) {
        super("BLUE_SHARK", EntityType.DROWNED, null, 5000.0, 120.0, 60.0, 120.0, 10, "RARE");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §9A §9Blue Shark §9appeared!";
    }

    @Override
    public String getName() {
        return "§9Blue Shark";
    }

    @Override
    public String getLootTableId() {
        return "blue_shark";
    }

    @Override
    public int getXPReward() {
        return 60;
    }
}
