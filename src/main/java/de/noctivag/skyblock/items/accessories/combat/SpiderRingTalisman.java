package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SpiderRingTalisman extends BaseAccessory {
    public SpiderRingTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.SPIDER);
        this.rarity = Rarity.UNCOMMON;
        this.strength = 4;    }
    @Override
    protected Material getMaterial() { return Material.SPIDER_EYE; }
    @Override
    protected String getDisplayName() { return "SpiderRing"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Enhanced strength against spiders");
    }
}
