package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class NecronChestplate extends CustomItem {
    public NecronChestplate() {
        super("NECRON_CHESTPLATE", "Necron Chestplate", ItemRarity.LEGENDARY, ItemType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        setHealth(160);
        setDefense(120);
        setStrength(40);
        setIntelligence(60);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Catacombs power");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
