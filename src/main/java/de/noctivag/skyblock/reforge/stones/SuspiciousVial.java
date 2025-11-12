package de.noctivag.skyblock.reforge.stones;

import de.noctivag.skyblock.reforge.Reforge;
import de.noctivag.skyblock.reforge.ReforgeStone;
import org.bukkit.Material;

public class SuspiciousVial extends ReforgeStone {
    
    public SuspiciousVial() {
        super(Reforge.SUSPICIOUS, 100000, Rarity.RARE);
    }

    @Override
    protected Material getMaterial() {
        return Material.POTION;
    }

    @Override
    protected String getDisplayName() {
        return "Suspicious Vial";
    }

    @Override
    protected String getApplicableItems() {
        return "§bSwords";
    }
}
