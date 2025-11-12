package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class NecronHelmet extends CustomItem {
    public NecronHelmet() {
        super("NECRON_HELMET", "Necron Helmet", ItemRarity.LEGENDARY, ItemType.HELMET, Material.DIAMOND_HELMET);
        setHealth(100);
        setDefense(75);
        setStrength(25);
        setIntelligence(37);
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
