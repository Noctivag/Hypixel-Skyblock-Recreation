package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class ShadowAssassinLeggings extends CustomItem {
    public ShadowAssassinLeggings() {
        super("SHADOW_ASSASSIN_LEGGINGS", "ShadowAssassin Leggings", ItemRarity.LEGENDARY, ItemType.LEGGINGS, Material.DIAMOND_LEGGINGS);
        setHealth(60);
        setDefense(75);
        setStrength(45);
        setIntelligence(30);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Speed boost");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
