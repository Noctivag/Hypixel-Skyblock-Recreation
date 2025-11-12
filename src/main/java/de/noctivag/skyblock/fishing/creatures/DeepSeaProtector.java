package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class DeepSeaProtector extends SeaCreature {
    public DeepSeaProtector(int fishingLevel) {
        super("DEEP_SEA_PROTECTOR", EntityType.GUARDIAN, null, 75000.0, 800.0, 400.0, 800.0, 25, "LEGENDARY");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §6The §6Deep Sea Protector §6has emerged!";
    }

    @Override
    public String getName() {
        return "§6Deep Sea Protector";
    }

    @Override
    public String getLootTableId() {
        return "deep_sea_protector";
    }

    @Override
    public int getXPReward() {
        return 400;
    }
}
