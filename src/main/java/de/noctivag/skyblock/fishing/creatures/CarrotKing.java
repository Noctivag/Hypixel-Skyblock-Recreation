package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class CarrotKing extends SeaCreature {
    public CarrotKing(int fishingLevel) {
        super("CARROT_KING", EntityType.RABBIT, null, 15000.0, 250.0, 125.0, 250.0, 18, "RARE");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §9Is this even a fish? It's the §9Carrot King!";
    }

    @Override
    public String getName() {
        return "§9Carrot King";
    }

    @Override
    public String getLootTableId() {
        return "carrot_king";
    }

    @Override
    public int getXPReward() {
        return 125;
    }
}
