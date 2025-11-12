package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class ShadowAssassinBoots extends CustomItem {
    public ShadowAssassinBoots() {
        super("SHADOW_ASSASSIN_BOOTS", "ShadowAssassin Boots", ItemRarity.LEGENDARY, ItemType.BOOTS, Material.DIAMOND_BOOTS);
        setHealth(40);
        setDefense(50);
        setStrength(30);
        setIntelligence(20);
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
