package de.noctivag.skyblock.items.accessories.mining;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class MagneticTalisman extends BaseAccessory {
    public MagneticTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.MAGNETIC);
        this.rarity = Rarity.UNCOMMON;
        this.speed = 1;    }
    @Override
    protected Material getMaterial() { return Material.IRON_INGOT; }
    @Override
    protected String getDisplayName() { return "Magnetic"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Pulls items");
    }
}
