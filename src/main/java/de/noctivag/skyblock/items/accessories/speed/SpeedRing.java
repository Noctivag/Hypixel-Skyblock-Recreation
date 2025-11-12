package de.noctivag.skyblock.items.accessories.speed;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SpeedRing extends BaseAccessory {
    public SpeedRing() {
        super(AccessoryTier.RING, AccessoryFamily.SPEED);
        this.rarity = Rarity.UNCOMMON;
        this.speed = 2;
    }
    @Override
    protected Material getMaterial() { return Material.SUGAR; }
    @Override
    protected String getDisplayName() { return "Speed Ring"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Grants §f✦ Speed§7.");
    }
}
