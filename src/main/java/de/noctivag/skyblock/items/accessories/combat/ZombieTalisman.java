package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class ZombieTalisman extends BaseAccessory {
    public ZombieTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.ZOMBIE);
        this.rarity = Rarity.COMMON;
        this.health = 4;    }
    @Override
    protected Material getMaterial() { return Material.ROTTEN_FLESH; }
    @Override
    protected String getDisplayName() { return "Zombie"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Zombie defense");
    }
}
