package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class BlessedFruit extends ReforgeStone {
    
    public BlessedFruit() {
        super(Reforge.SPIRITUAL, 500000, Rarity.LEGENDARY);
    }

    @Override
    protected Material getMaterial() {
        return Material.APPLE;
    }

    @Override
    protected String getDisplayName() {
        return "Blessed Fruit";
    }

    @Override
    protected String getApplicableItems() {
        return "§bBows";
    }
}
