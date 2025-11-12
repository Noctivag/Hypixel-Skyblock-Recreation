package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class ShadowAssassinHelmet extends CustomItem {
    public ShadowAssassinHelmet() {
        super("SHADOW_ASSASSIN_HELMET", "ShadowAssassin Helmet", ItemRarity.LEGENDARY, ItemType.HELMET, Material.DIAMOND_HELMET);
        setHealth(50);
        setDefense(62);
        setStrength(37);
        setIntelligence(25);
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
