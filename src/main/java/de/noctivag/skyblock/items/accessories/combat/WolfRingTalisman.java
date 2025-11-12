package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class WolfRingTalisman extends BaseAccessory {
    public WolfRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.WOLF);
        this.rarity = Rarity.UNCOMMON;
        this.health = 4;
        this.speed = 2;    }
    @Override
    protected Material getMaterial() { return Material.BONE; }
    @Override
    protected String getDisplayName() { return "WolfRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Grants combat boost");
    }
}
