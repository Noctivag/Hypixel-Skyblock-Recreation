package de.noctivag.skyblock.items.accessories.special;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class EnigmaSoul extends BaseAccessory {
    public EnigmaSoul() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.STANDALONE);
        this.rarity = Rarity.MYTHIC;
        this.intelligence = 30;
        this.speed = 15;
    }
    @Override
    protected Material getMaterial() { return Material.ENDER_PEARL; }
    @Override
    protected String getDisplayName() { return "Enigma Soul"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Enigmatic", "§7Grants mysterious rift powers.");
    }
}
