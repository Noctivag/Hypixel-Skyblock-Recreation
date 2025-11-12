package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class DragonScale extends ReforgeStone {
    
    public DragonScale() {
        super(Reforge.SPIKED, 400000, Rarity.EPIC);
    }

    @Override
    protected Material getMaterial() {
        return Material.DRAGON_EGG;
    }

    @Override
    protected String getDisplayName() {
        return "Dragon Scale";
    }

    @Override
    protected String getApplicableItems() {
        return "§bArmor";
    }
}
