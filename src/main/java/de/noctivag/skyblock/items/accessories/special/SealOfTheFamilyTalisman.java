package de.noctivag.skyblock.items.accessories.special;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class SealOfTheFamilyTalisman extends BaseAccessory {
    public SealOfTheFamilyTalisman() {
        super(AccessoryTier.RELIC, AccessoryFamily.SEAL_OF_THE_FAMILY);
        this.rarity = Rarity.LEGENDARY;
        this.strength = 15;
        this.critDamage = 15;    }
    @Override
    protected Material getMaterial() { return Material.ENCHANTED_BOOK; }
    @Override
    protected String getDisplayName() { return "SealOfTheFamily"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Family seal");
    }
}
