package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class Onyx extends ReforgeStone {
    
    public Onyx() {
        super(Reforge.FRUITFUL, 100000, Rarity.UNCOMMON);
    }

    @Override
    protected Material getMaterial() {
        return Material.COAL;
    }

    @Override
    protected String getDisplayName() {
        return "Onyx";
    }

    @Override
    protected String getApplicableItems() {
        return "§bTools";
    }
}
