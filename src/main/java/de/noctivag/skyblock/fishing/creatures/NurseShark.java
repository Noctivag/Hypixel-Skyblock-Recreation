package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class NurseShark extends SeaCreature {
    public NurseShark(int fishingLevel) {
        super("NURSE_SHARK", EntityType.DROWNED, null, 3000.0, 80.0, 40.0, 80.0, 8, "UNCOMMON");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §aA §aNurse Shark §aappeared!";
    }

    @Override
    public String getName() {
        return "§aNurse Shark";
    }

    @Override
    public String getLootTableId() {
        return "nurse_shark";
    }

    @Override
    public int getXPReward() {
        return 40;
    }
}
