package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class FrozenBlazeBoots extends CustomItem {
    public FrozenBlazeBoots() {
        super("FROZEN_BLAZE_BOOTS", "FrozenBlaze Boots", ItemRarity.LEGENDARY, ItemType.BOOTS, Material.DIAMOND_BOOTS);
        setHealth(56);
        setDefense(60);
        setStrength(18);
        setIntelligence(50);
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
