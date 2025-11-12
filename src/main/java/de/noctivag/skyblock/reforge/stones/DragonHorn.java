package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class DragonHorn extends ReforgeStone {
    
    public DragonHorn() {
        super(Reforge.RENOWNED, 1000000, Rarity.LEGENDARY);
    }

    @Override
    protected Material getMaterial() {
        return Material.DRAGON_HEAD;
    }

    @Override
    protected String getDisplayName() {
        return "Dragon Horn";
    }

    @Override
    protected String getApplicableItems() {
        return "§bArmor";
    }
}
