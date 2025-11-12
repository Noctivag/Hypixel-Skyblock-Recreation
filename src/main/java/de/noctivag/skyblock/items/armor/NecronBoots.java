package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class NecronBoots extends CustomItem {
    public NecronBoots() {
        super("NECRON_BOOTS", "Necron Boots", ItemRarity.LEGENDARY, ItemType.BOOTS, Material.DIAMOND_BOOTS);
        setHealth(80);
        setDefense(60);
        setStrength(20);
        setIntelligence(30);
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
