package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class HydraHead extends SeaCreature {
    public HydraHead(int fishingLevel) {
        super("HYDRA_HEAD", EntityType.ZOMBIE, null, 100000.0, 1000.0, 500.0, 1000.0, 28, "LEGENDARY");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §6The §6Hydra §6has arrived!";
    }

    @Override
    public String getName() {
        return "§6Hydra";
    }

    @Override
    public String getLootTableId() {
        return "hydra";
    }

    @Override
    public int getXPReward() {
        return 500;
    }
}
