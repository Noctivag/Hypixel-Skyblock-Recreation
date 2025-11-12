package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class GoldSword extends CustomItem {
    public GoldSword() {
        super("GOLD_SWORD", "Shiny Gold Sword", ItemRarity.UNCOMMON, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(50);
        setStrength(30);
        setCritChance(0);
        setIntelligence(15);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Shiny and valuable");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
