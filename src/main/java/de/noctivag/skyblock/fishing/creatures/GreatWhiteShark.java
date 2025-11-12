package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class GreatWhiteShark extends SeaCreature {
    public GreatWhiteShark(int fishingLevel) {
        super("GREAT_WHITE_SHARK", EntityType.DROWNED, null, 60000.0, 700.0, 350.0, 700.0, 20, "LEGENDARY");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §6A §6Great White Shark §6has been caught!";
    }

    @Override
    public String getName() {
        return "§6Great White Shark";
    }

    @Override
    public String getLootTableId() {
        return "great_white_shark";
    }

    @Override
    public int getXPReward() {
        return 350;
    }
}
