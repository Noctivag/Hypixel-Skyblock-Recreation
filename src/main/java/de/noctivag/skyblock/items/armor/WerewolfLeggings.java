package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class WerewolfLeggings extends CustomItem {
    public WerewolfLeggings() {
        super("WEREWOLF_LEGGINGS", "Werewolf Leggings", ItemRarity.LEGENDARY, ItemType.LEGGINGS, Material.DIAMOND_LEGGINGS);
        setHealth(84);
        setDefense(84);
        setStrength(36);
        setIntelligence(24);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Full Set Bonus: Wolf transformation");
        lore.add("§7Powerful set bonus effect.");
        return lore;
    }
}
