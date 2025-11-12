package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class TarantulaChestplate extends CustomItem {
    public TarantulaChestplate() {
        super("TARANTULA_CHESTPLATE", "Tarantula Chestplate", ItemRarity.EPIC, ItemType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        setHealth(60);
        setDefense(72);
        setStrength(12);
        setIntelligence(20);
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
