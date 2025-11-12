package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class PiggyBankTalisman extends BaseAccessory {
    public PiggyBankTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.PIGGY_BANK);
        this.rarity = Rarity.UNCOMMON;
        this.intelligence = 5;    }
    @Override
    protected Material getMaterial() { return Material.GOLD_BLOCK; }
    @Override
    protected String getDisplayName() { return "PiggyBank"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Earn interest");
    }
}
