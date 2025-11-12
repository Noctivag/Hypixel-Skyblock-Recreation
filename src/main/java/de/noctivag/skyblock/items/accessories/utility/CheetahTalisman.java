package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class CheetahTalisman extends BaseAccessory {
    public CheetahTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.STANDALONE);
        this.rarity = Rarity.EPIC;
        this.speed = 10;    }
    @Override
    protected Material getMaterial() { return Material.LEATHER; }
    @Override
    protected String getDisplayName() { return "Cheetah"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Cheetah speed");
    }
}
