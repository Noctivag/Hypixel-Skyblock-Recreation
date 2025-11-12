package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SeaEmperor extends SeaCreature {
    public SeaEmperor(int fishingLevel) {
        super("SEA_EMPEROR", EntityType.GUARDIAN, null, 150000.0, 1500.0, 750.0, 1500.0, 30, "LEGENDARY");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §6The §6§lSEA EMPEROR §6arises from the depths!";
    }

    @Override
    public String getName() {
        return "§6§lSea Emperor";
    }

    @Override
    public String getLootTableId() {
        return "sea_emperor";
    }

    @Override
    public int getXPReward() {
        return 750;
    }
}
