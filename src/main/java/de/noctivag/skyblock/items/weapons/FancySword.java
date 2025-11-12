package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class FancySword extends CustomItem {
    public FancySword() {
        super("FANCY_SWORD", "Fancy Sword", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(70);
        setStrength(40);
        setCritChance(5);
        setIntelligence(25);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Very fancy");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
