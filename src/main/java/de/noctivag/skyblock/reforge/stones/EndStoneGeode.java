package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class EndStoneGeode extends ReforgeStone {
    
    public EndStoneGeode() {
        super(Reforge.WARPED_ARMOR, 300000, Rarity.RARE);
    }

    @Override
    protected Material getMaterial() {
        return Material.END_STONE;
    }

    @Override
    protected String getDisplayName() {
        return "End Stone Geode";
    }

    @Override
    protected String getApplicableItems() {
        return "§bArmor";
    }
}
