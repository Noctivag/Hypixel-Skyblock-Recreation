package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class WerewolfBoots extends CustomItem {
    public WerewolfBoots() {
        super("WEREWOLF_BOOTS", "Werewolf Boots", ItemRarity.LEGENDARY, ItemType.BOOTS, Material.DIAMOND_BOOTS);
        setHealth(56);
        setDefense(56);
        setStrength(24);
        setIntelligence(16);
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
