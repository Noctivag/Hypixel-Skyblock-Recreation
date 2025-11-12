package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class WitherBlood extends ReforgeStone {
    
    public WitherBlood() {
        super(Reforge.WITHERED, 750000, Rarity.LEGENDARY);
    }

    @Override
    protected Material getMaterial() {
        return Material.NETHER_STAR;
    }

    @Override
    protected String getDisplayName() {
        return "Wither Blood";
    }

    @Override
    protected String getApplicableItems() {
        return "§bSwords";
    }
}
