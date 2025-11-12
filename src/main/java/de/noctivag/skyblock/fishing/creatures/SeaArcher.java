package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SeaArcher extends SeaCreature {
    public SeaArcher(int fishingLevel) {
        super("SEA_ARCHER", EntityType.SKELETON, null, 4000.0, 100.0, 50.0, 100.0, 10, "RARE");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §9A §9Rare §9Sea Archer §7emerged!";
    }

    @Override
    public String getName() {
        return "§9Sea Archer";
    }

    @Override
    public String getLootTableId() {
        return "sea_archer";
    }

    @Override
    public int getXPReward() {
        return 50;
    }
}
