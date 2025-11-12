package de.noctivag.skyblock.items.accessories.combat;
import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.List;

public class RevenantViscera extends BaseAccessory {
    public RevenantViscera() {
        super(AccessoryTier.ARTIFACT, AccessoryFamily.REVENANT);
        this.rarity = Rarity.LEGENDARY;
        this.health = 15;
        this.defense = 10;
        this.enrichable = true;
    }
    @Override
    protected Material getMaterial() { return Material.BONE; }
    @Override
    protected String getDisplayName() { return "Revenant Viscera"; }
    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList("§6Ability: Undead", "§7Grants immunity to Poison.");
    }
}
