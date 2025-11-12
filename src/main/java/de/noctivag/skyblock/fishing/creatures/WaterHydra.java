package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class WaterHydra extends SeaCreature {
    public WaterHydra(int fishingLevel) {
        super("WATER_HYDRA", EntityType.ZOMBIE, null, 50000.0, 600.0, 300.0, 600.0, 15, "LEGENDARY");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §6The §6Water Hydra §6has come to test your strength!";
    }

    @Override
    public String getName() {
        return "§6Water Hydra";
    }

    @Override
    public String getLootTableId() {
        return "water_hydra";
    }

    @Override
    public int getXPReward() {
        return 300;
    }
}
