package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class BrokenPiggyBankTalisman extends BaseAccessory {
    public BrokenPiggyBankTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.PIGGY_BANK);
        this.rarity = Rarity.EPIC;
        this.intelligence = 15;    }
    @Override
    protected Material getMaterial() { return Material.GOLD_BLOCK; }
    @Override
    protected String getDisplayName() { return "BrokenPiggyBank"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Maximum interest");
    }
}
