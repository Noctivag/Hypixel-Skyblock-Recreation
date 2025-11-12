package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class DemonSword extends CustomItem {
    public DemonSword() {
        super("DEMON_SWORD", "Demon Sword", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(230);
        setStrength(140);
        setCritChance(20);
        setIntelligence(85);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Demonic power");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
