package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class WerewolfChestplate extends CustomItem {
    public WerewolfChestplate() {
        super("WEREWOLF_CHESTPLATE", "Werewolf Chestplate", ItemRarity.LEGENDARY, ItemType.CHESTPLATE, Material.DIAMOND_CHESTPLATE);
        setHealth(112);
        setDefense(112);
        setStrength(48);
        setIntelligence(32);
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
