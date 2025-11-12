package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class MoltenCube extends ReforgeStone {
    
    public MoltenCube() {
        super(Reforge.CUBIC, 750000, Rarity.LEGENDARY);
    }

    @Override
    protected Material getMaterial() {
        return Material.MAGMA_CREAM;
    }

    @Override
    protected String getDisplayName() {
        return "Molten Cube";
    }

    @Override
    protected String getApplicableItems() {
        return "§bArmor";
    }
}
