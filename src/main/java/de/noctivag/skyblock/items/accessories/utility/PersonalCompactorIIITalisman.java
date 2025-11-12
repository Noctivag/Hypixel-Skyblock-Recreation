package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class PersonalCompactorIIITalisman extends BaseAccessory {
    public PersonalCompactorIIITalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.PERSONAL_COMPACTOR);
        this.rarity = Rarity.UNCOMMON;
        this.intelligence = 2;    }
    @Override
    protected Material getMaterial() { return Material.HOPPER; }
    @Override
    protected String getDisplayName() { return "PersonalCompactorIII"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Auto compact III");
    }
}
