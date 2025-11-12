package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SpiderTalisman extends BaseAccessory {
    public SpiderTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.SPIDER);
        this.rarity = Rarity.COMMON;
        this.strength = 2;    }
    @Override
    protected Material getMaterial() { return Material.SPIDER_EYE; }
    @Override
    protected String getDisplayName() { return "Spider"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Grants strength against spiders");
    }
}
