package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class CandyRingTalisman extends BaseAccessory {
    public CandyRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.CANDY);
        this.rarity = Rarity.RARE;
        this.speed = 4;    }
    @Override
    protected Material getMaterial() { return Material.COOKIE; }
    @Override
    protected String getDisplayName() { return "CandyRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Sweeter tooth");
    }
}
