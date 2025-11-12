package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class DirtBottle extends ReforgeStone {
    
    public DirtBottle() {
        super(Reforge.DIRTY, 200000, Rarity.RARE);
    }

    @Override
    protected Material getMaterial() {
        return Material.DIRT;
    }

    @Override
    protected String getDisplayName() {
        return "Dirt Bottle";
    }

    @Override
    protected String getApplicableItems() {
        return "§bSwords";
    }
}
