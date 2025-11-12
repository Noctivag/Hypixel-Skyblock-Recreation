package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class ZombieRingTalisman extends BaseAccessory {
    public ZombieRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.ZOMBIE);
        this.rarity = Rarity.UNCOMMON;
        this.health = 8;    }
    @Override
    protected Material getMaterial() { return Material.ROTTEN_FLESH; }
    @Override
    protected String getDisplayName() { return "ZombieRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Enhanced zombie defense");
    }
}
