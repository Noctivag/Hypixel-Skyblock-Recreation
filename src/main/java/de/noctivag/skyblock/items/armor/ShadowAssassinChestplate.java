package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class ShadowAssassinChestplate extends CustomItem {
    public ShadowAssassinChestplate() {
        super("SHADOW_ASSASSIN_CHESTPLATE", "ShadowAssassin Chestplate", ItemRarity.LEGENDARY, ItemType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        setHealth(80);
        setDefense(100);
        setStrength(60);
        setIntelligence(40);
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
