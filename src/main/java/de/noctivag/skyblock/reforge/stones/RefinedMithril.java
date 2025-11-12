package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class RefinedMithril extends ReforgeStone {
    
    public RefinedMithril() {
        super(Reforge.MITHRAIC, 250000, Rarity.RARE);
    }

    @Override
    protected Material getMaterial() {
        return Material.PRISMARINE_CRYSTALS;
    }

    @Override
    protected String getDisplayName() {
        return "Refined Mithril";
    }

    @Override
    protected String getApplicableItems() {
        return "§bTools";
    }
}
