package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class SorrowHelmet extends CustomItem {
    public SorrowHelmet() {
        super("SORROW_HELMET", "Sorrow Helmet", ItemRarity.LEGENDARY, ItemType.HELMET, Material.DIAMOND_HELMET);
        setHealth(80);
        setDefense(80);
        setStrength(17);
        setIntelligence(45);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Sadness aura");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
