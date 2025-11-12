package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class DivanHelmet extends CustomItem {
    public DivanHelmet() {
        super("DIVAN_HELMET", "Divan Helmet", ItemRarity.LEGENDARY, ItemType.HELMET, Material.DIAMOND_HELMET);
        setHealth(75);
        setDefense(100);
        setStrength(12);
        setIntelligence(50);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Mining power");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
