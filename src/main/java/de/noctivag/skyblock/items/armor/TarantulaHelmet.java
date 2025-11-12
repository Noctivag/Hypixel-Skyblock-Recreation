package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class TarantulaHelmet extends CustomItem {
    public TarantulaHelmet() {
        super("TARANTULA_HELMET", "Tarantula Helmet", ItemRarity.EPIC, ItemType.HELMET, Material.DIAMOND_HELMET);
        setHealth(37);
        setDefense(45);
        setStrength(7);
        setIntelligence(12);
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
