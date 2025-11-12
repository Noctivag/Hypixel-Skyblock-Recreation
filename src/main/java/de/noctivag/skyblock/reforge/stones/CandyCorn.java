package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class CandyCorn extends ReforgeStone {
    
    public CandyCorn() {
        super(Reforge.CANDIED, 50000, Rarity.UNCOMMON);
    }

    @Override
    protected Material getMaterial() {
        return Material.COOKIE;
    }

    @Override
    protected String getDisplayName() {
        return "Candy Corn";
    }

    @Override
    protected String getApplicableItems() {
        return "§bArmor";
    }
}
