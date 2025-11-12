package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class SorrowBoots extends CustomItem {
    public SorrowBoots() {
        super("SORROW_BOOTS", "Sorrow Boots", ItemRarity.LEGENDARY, ItemType.BOOTS, Material.DIAMOND_BOOTS);
        setHealth(64);
        setDefense(64);
        setStrength(14);
        setIntelligence(36);
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
