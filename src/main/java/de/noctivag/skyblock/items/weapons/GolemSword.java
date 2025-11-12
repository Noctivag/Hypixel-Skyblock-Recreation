package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class GolemSword extends CustomItem {
    public GolemSword() {
        super("GOLEM_SWORD", "Golem Sword", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(110);
        setStrength(70);
        setCritChance(0);
        setIntelligence(45);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Iron strength");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
