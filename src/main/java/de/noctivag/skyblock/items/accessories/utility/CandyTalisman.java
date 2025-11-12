package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class CandyTalisman extends BaseAccessory {
    public CandyTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.CANDY);
        this.rarity = Rarity.UNCOMMON;
        this.speed = 2;    }
    @Override
    protected Material getMaterial() { return Material.COOKIE; }
    @Override
    protected String getDisplayName() { return "Candy"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Sweet tooth");
    }
}
