package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class FrozenBlazeChestplate extends CustomItem {
    public FrozenBlazeChestplate() {
        super("FROZEN_BLAZE_CHESTPLATE", "FrozenBlaze Chestplate", ItemRarity.LEGENDARY, ItemType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        setHealth(112);
        setDefense(120);
        setStrength(36);
        setIntelligence(100);
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
