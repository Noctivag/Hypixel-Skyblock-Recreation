package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class TarantulaLeggings extends CustomItem {
    public TarantulaLeggings() {
        super("TARANTULA_LEGGINGS", "Tarantula Leggings", ItemRarity.EPIC, ItemType.LEGGINGS, Material.DIAMOND_LEGGINGS);
        setHealth(45);
        setDefense(54);
        setStrength(9);
        setIntelligence(15);
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
