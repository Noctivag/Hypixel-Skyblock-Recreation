package de.noctivag.skyblock.items.accessories.speed;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class FeatherTalisman extends BaseAccessory {
    public FeatherTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.FEATHER);
        this.rarity = Rarity.COMMON;
        this.speed = 3;    }
    @Override
    protected Material getMaterial() { return Material.FEATHER; }
    @Override
    protected String getDisplayName() { return "Feather"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Feather falling");
    }
}
