package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class VampireTalisman extends BaseAccessory {
    public VampireTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.VAMPIRE);
        this.rarity = Rarity.EPIC;
        this.health = 10;
        this.intelligence = 5;    }
    @Override
    protected Material getMaterial() { return Material.REDSTONE; }
    @Override
    protected String getDisplayName() { return "Vampire"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Vampire power");
    }
}
