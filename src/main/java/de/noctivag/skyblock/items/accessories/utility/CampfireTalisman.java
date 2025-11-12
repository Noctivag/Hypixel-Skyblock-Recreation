package de.noctivag.skyblock.items.accessories.utility;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class CampfireTalisman extends BaseAccessory {
    public CampfireTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.STANDALONE);
        this.rarity = Rarity.RARE;
        this.health = 5;
        this.speed = 2;    }
    @Override
    protected Material getMaterial() { return Material.CAMPFIRE; }
    @Override
    protected String getDisplayName() { return "Campfire"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Campfire comfort");
    }
}
