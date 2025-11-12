package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class EndStoneSword extends CustomItem {
    public EndStoneSword() {
        super("END_STONE_SWORD", "End Stone Sword", ItemRarity.EPIC, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(120);
        setStrength(80);
        setCritChance(10);
        setIntelligence(40);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Ender damage bonus");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
