package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class CrackedPiggyBankTalisman extends BaseAccessory {
    public CrackedPiggyBankTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.PIGGY_BANK);
        this.rarity = Rarity.RARE;
        this.intelligence = 10;    }
    @Override
    protected Material getMaterial() { return Material.GOLD_BLOCK; }
    @Override
    protected String getDisplayName() { return "CrackedPiggyBank"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7More interest");
    }
}
