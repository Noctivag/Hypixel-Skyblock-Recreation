package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class RareDiamond extends ReforgeStone {
    
    public RareDiamond() {
        super(Reforge.REINFORCED, 500000, Rarity.EPIC);
    }

    @Override
    protected Material getMaterial() {
        return Material.DIAMOND;
    }

    @Override
    protected String getDisplayName() {
        return "Rare Diamond";
    }

    @Override
    protected String getApplicableItems() {
        return "§bArmor";
    }
}
