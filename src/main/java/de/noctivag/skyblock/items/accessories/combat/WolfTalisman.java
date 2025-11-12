package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class WolfTalisman extends BaseAccessory {
    public WolfTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.WOLF);
        this.rarity = Rarity.COMMON;
        this.health = 2;
        this.speed = 1;    }
    @Override
    protected Material getMaterial() { return Material.BONE; }
    @Override
    protected String getDisplayName() { return "Wolf"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Grants small combat boost");
    }
}
