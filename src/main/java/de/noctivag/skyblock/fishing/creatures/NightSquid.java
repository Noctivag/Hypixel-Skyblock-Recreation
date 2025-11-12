package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class NightSquid extends SeaCreature {
    public NightSquid(int fishingLevel) {
        super("NIGHT_SQUID", EntityType.SQUID, null, 750.0, 30.0, 15.0, 30.0, 3, "COMMON");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §fA §fNight Squid §7appeared!";
    }

    @Override
    public String getName() {
        return "§fNight Squid";
    }

    @Override
    public String getLootTableId() {
        return "night_squid";
    }

    @Override
    public int getXPReward() {
        return 15;
    }
}
