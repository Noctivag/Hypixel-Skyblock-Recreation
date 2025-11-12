package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SeaWalker extends SeaCreature {
    public SeaWalker(int fishingLevel) {
        super("SEA_WALKER", EntityType.ZOMBIE, null, 500.0, 20.0, 10.0, 20.0, 1, "COMMON");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §fA §fSea Walker §7walked up!";
    }

    @Override
    public String getName() {
        return "§fSea Walker";
    }

    @Override
    public String getLootTableId() {
        return "sea_walker";
    }

    @Override
    public int getXPReward() {
        return 10;
    }
}
