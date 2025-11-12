package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class GoldenDragonHelmet extends CustomItem {
    public GoldenDragonHelmet() {
        super("GOLDEN_DRAGON_HELMET", "GoldenDragon Helmet", ItemRarity.LEGENDARY, ItemType.HELMET, Material.DIAMOND_HELMET);
        setHealth(75);
        setDefense(75);
        setStrength(20);
        setIntelligence(62);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Dragon power");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
