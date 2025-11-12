package de.noctivag.skyblock.items.accessories.special;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class CrownOfGreed extends BaseAccessory {
    public CrownOfGreed() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.STANDALONE);
        this.rarity = Rarity.LEGENDARY;
        this.strength = 10;
        this.intelligence = 10;
        this.health = 25;
    }
    @Override
    protected Material getMaterial() { return Material.GOLDEN_HELMET; }
    @Override
    protected String getDisplayName() { return "Crown of Greed"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Greed", "§7Grants §6+20% §7coin drops.");
    }
}
