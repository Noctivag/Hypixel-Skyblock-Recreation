package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class WarpedStone extends ReforgeStone {
    
    public WarpedStone() {
        super(Reforge.WARPED, 500000, Rarity.EPIC);
    }

    @Override
    protected Material getMaterial() {
        return Material.ENDER_PEARL;
    }

    @Override
    protected String getDisplayName() {
        return "Warped Stone";
    }

    @Override
    protected String getApplicableItems() {
        return "§bAOTE";
    }
}
