package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class SorrowChestplate extends CustomItem {
    public SorrowChestplate() {
        super("SORROW_CHESTPLATE", "Sorrow Chestplate", ItemRarity.LEGENDARY, ItemType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        setHealth(128);
        setDefense(128);
        setStrength(28);
        setIntelligence(72);
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
