package de.noctivag.skyblock.items.accessories.special;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class MelodyTalisman extends BaseAccessory {
    public MelodyTalisman() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.MELODY);
        this.rarity = Rarity.MYTHIC;
        this.intelligence = 20;
        this.speed = 10;    }
    @Override
    protected Material getMaterial() { return Material.NOTE_BLOCK; }
    @Override
    protected String getDisplayName() { return "Melody"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§7Musical power");
    }
}
