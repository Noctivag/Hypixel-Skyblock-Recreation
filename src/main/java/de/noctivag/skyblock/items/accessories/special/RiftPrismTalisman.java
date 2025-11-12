package de.noctivag.skyblock.items.accessories.special;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class RiftPrismTalisman extends BaseAccessory {
    public RiftPrismTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.RIFT_PRISM);
        this.rarity = Rarity.MYTHIC;
        this.intelligence = 25;
        this.speed = 15;    }
    @Override
    protected Material getMaterial() { return Material.PRISMARINE_CRYSTALS; }
    @Override
    protected String getDisplayName() { return "RiftPrism"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Rift energy");
    }
}
