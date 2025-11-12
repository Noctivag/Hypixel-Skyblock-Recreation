package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class NightVisionTalisman extends BaseAccessory {
    public NightVisionTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.NIGHT_VISION);
        this.rarity = Rarity.RARE;
        this.intelligence = 4;    }
    @Override
    protected Material getMaterial() { return Material.ENDER_EYE; }
    @Override
    protected String getDisplayName() { return "NightVision"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7See in dark");
    }
}
