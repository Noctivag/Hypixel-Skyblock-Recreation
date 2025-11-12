package de.noctivag.skyblock.items.accessories.special;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class HegemonyTalisman extends BaseAccessory {
    public HegemonyTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.HEGEMONY);
        this.rarity = Rarity.LEGENDARY;
        this.strength = 20;
        this.critDamage = 20;    }
    @Override
    protected Material getMaterial() { return Material.NETHER_STAR; }
    @Override
    protected String getDisplayName() { return "Hegemony"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Double magical power");
    }
}
