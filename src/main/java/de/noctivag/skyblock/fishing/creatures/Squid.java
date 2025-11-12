package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class Squid extends SeaCreature {
    public Squid(int fishingLevel) {
        super("SQUID", EntityType.SQUID, null, 250.0, 10.0, 5.0, 10.0, 0, "COMMON");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §fA §fSquid §7was caught!";
    }

    @Override
    public String getName() {
        return "§fSquid";
    }

    @Override
    public String getLootTableId() {
        return "squid";
    }

    @Override
    public int getXPReward() {
        return 5;
    }
}
