package de.noctivag.skyblock.items.armor;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class WerewolfHelmet extends CustomItem {
    public WerewolfHelmet() {
        super("WEREWOLF_HELMET", "Werewolf Helmet", ItemRarity.LEGENDARY, ItemType.HELMET, Material.DIAMOND_HELMET);
        setHealth(70);
        setDefense(70);
        setStrength(30);
        setIntelligence(20);
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
