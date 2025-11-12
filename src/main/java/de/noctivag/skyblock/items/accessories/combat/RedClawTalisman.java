package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class RedClawTalisman extends BaseAccessory {
    public RedClawTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.RED_CLAW);
        this.rarity = Rarity.EPIC;
        this.strength = 10;
        this.critDamage = 10;    }
    @Override
    protected Material getMaterial() { return Material.REDSTONE; }
    @Override
    protected String getDisplayName() { return "RedClaw"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Powerful artifact");
    }
}
