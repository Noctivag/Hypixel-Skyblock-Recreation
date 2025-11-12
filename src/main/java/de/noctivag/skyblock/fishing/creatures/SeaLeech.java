package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SeaLeech extends SeaCreature {
    public SeaLeech(int fishingLevel) {
        super("SEA_LEECH", EntityType.GUARDIAN, null, 25000.0, 350.0, 175.0, 350.0, 20, "EPIC");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §5Disgusting! A §5Sea Leech §5appeared!";
    }

    @Override
    public String getName() {
        return "§5Sea Leech";
    }

    @Override
    public String getLootTableId() {
        return "sea_leech";
    }

    @Override
    public int getXPReward() {
        return 175;
    }
}
