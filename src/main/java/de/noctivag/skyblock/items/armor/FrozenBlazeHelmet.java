package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class FrozenBlazeHelmet extends CustomItem {
    public FrozenBlazeHelmet() {
        super("FROZEN_BLAZE_HELMET", "FrozenBlaze Helmet", ItemRarity.LEGENDARY, ItemType.HELMET, Material.DIAMOND_HELMET);
        setHealth(70);
        setDefense(75);
        setStrength(22);
        setIntelligence(62);
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
