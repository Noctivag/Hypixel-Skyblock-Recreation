package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class FrozenBlazeLeggings extends CustomItem {
    public FrozenBlazeLeggings() {
        super("FROZEN_BLAZE_LEGGINGS", "FrozenBlaze Leggings", ItemRarity.LEGENDARY, ItemType.LEGGINGS, Material.DIAMOND_LEGGINGS);
        setHealth(84);
        setDefense(90);
        setStrength(27);
        setIntelligence(75);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Ice and fire");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
