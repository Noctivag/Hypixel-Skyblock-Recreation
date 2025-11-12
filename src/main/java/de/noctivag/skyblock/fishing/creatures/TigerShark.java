package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class TigerShark extends SeaCreature {
    public TigerShark(int fishingLevel) {
        super("TIGER_SHARK", EntityType.DROWNED, null, 20000.0, 300.0, 150.0, 300.0, 15, "EPIC");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §5A §5Tiger Shark §5appeared!";
    }

    @Override
    public String getName() {
        return "§5Tiger Shark";
    }

    @Override
    public String getLootTableId() {
        return "tiger_shark";
    }

    @Override
    public int getXPReward() {
        return 150;
    }
}
