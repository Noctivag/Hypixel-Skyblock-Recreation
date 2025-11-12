package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class DragonClaw extends ReforgeStone {
    
    public DragonClaw() {
        super(Reforge.FABLED, 1000000, Rarity.EPIC);
    }

    @Override
    protected Material getMaterial() {
        return Material.DRAGON_HEAD;
    }

    @Override
    protected String getDisplayName() {
        return "Dragon Claw";
    }

    @Override
    protected String getApplicableItems() {
        return "§bEpic+ Swords";
    }
}
