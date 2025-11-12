package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class TarantulaBoots extends CustomItem {
    public TarantulaBoots() {
        super("TARANTULA_BOOTS", "Tarantula Boots", ItemRarity.EPIC, ItemType.BOOTS, Material.DIAMOND_BOOTS);
        setHealth(30);
        setDefense(36);
        setStrength(6);
        setIntelligence(10);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Spider slayer");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
