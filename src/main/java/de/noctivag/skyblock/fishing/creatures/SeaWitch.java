package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class SeaWitch extends SeaCreature {
    public SeaWitch(int fishingLevel) {
        super("SEA_WITCH", EntityType.WITCH, null, 2500.0, 75.0, 35.0, 75.0, 7, "UNCOMMON");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §aA spooky §aSea Witch §aappeared!";
    }

    @Override
    public String getName() {
        return "§aSea Witch";
    }

    @Override
    public String getLootTableId() {
        return "sea_witch";
    }

    @Override
    public int getXPReward() {
        return 35;
    }
}
