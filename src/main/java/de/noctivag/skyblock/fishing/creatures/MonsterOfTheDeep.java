package de.noctivag.skyblock.fishing.creatures;

import de.noctivag.skyblock.fishing.SeaCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class MonsterOfTheDeep extends SeaCreature {
    public MonsterOfTheDeep(int fishingLevel) {
        super("MONSTER_OF_THE_DEEP", EntityType.ZOMBIE, null, 7500.0, 150.0, 75.0, 150.0, 12, "RARE");
    }

    @Override
    public String getCatchMessage() {
        return "§7§lCATCH! §9The §9Monster of the Deep §9has come to test your strength!";
    }

    @Override
    public String getName() {
        return "§9Monster of the Deep";
    }

    @Override
    public String getLootTableId() {
        return "monster_of_the_deep";
    }

    @Override
    public int getXPReward() {
        return 75;
    }
}
