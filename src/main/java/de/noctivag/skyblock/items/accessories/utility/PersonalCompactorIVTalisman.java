package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class PersonalCompactorIVTalisman extends BaseAccessory {
    public PersonalCompactorIVTalisman() {
        super(AccessoryTier.RING, AccessoryFamily.PERSONAL_COMPACTOR);
        this.rarity = Rarity.RARE;
        this.intelligence = 4;    }
    @Override
    protected Material getMaterial() { return Material.HOPPER; }
    @Override
    protected String getDisplayName() { return "PersonalCompactorIV"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Auto compact IV");
    }
}
