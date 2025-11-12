package de.noctivag.skyblock.items.accessories.speed;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SpeedTalisman extends BaseAccessory {
    public SpeedTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.SPEED);
        this.rarity = Rarity.COMMON;
        this.speed = 1;
    }
    @Override
    protected Material getMaterial() { return Material.SUGAR; }
    @Override
    protected String getDisplayName() { return "Speed Talisman"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Grants a small", "§7amount of §f✦ Speed§7.");
    }
}
