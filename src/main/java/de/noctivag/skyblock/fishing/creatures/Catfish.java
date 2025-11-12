package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class Catfish extends SeaCreature {
    public Catfish(int fishingLevel) {
        super("CATFISH", EntityType.OCELOT, null, 12000.0, 200.0, 100.0, 200.0, 15, "RARE");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §9Huh? A §9Catfish!";
    }

    @Override
    public String getName() {
        return "§9Catfish";
    }

    @Override
    public String getLootTableId() {
        return "catfish";
    }

    @Override
    public int getXPReward() {
        return 100;
    }
}
