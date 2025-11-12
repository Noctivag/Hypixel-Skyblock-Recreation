package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class OpticalLens extends ReforgeStone {
    
    public OpticalLens() {
        super(Reforge.PRECISE, 150000, Rarity.RARE);
    }

    @Override
    protected Material getMaterial() {
        return Material.GLASS;
    }

    @Override
    protected String getDisplayName() {
        return "Optical Lens";
    }

    @Override
    protected String getApplicableItems() {
        return "§bBows";
    }
}
