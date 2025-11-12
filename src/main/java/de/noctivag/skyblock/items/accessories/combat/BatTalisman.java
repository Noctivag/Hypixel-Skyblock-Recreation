package de.noctivag.skyblock.items.accessories.combat;

import de.noctivag.skyblock.items.accessories.BaseAccessory;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class BatTalisman extends BaseAccessory {

    public BatTalisman() {
        super(AccessoryTier.TALISMAN, AccessoryFamily.BAT);
        this.rarity = Rarity.COMMON;
        this.health = 1;
        this.speed = 1;
        this.intelligence = 1;
    }

    @Override
    protected Material getMaterial() {
        return Material.BONE;
    }

    @Override
    protected String getDisplayName() {
        return "Bat Talisman";
    }

    @Override
    protected List<String> getAbilityLore() {
        return Arrays.asList(
            "§6Ability: Night Vision",
            "§7Grants a small amount of stats",
            "§7during night time."
        );
    }
}
