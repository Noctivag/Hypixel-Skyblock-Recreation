package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class YetiSword extends CustomItem {
    public YetiSword() {
        super("YETI_SWORD", "Yeti Sword", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(190);
        setStrength(140);
        setIntelligence(200);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Yeti Storm");
        lore.add("§7Each §9☠ Crit §7gives");
        lore.add("§7§c+5 ❁ Strength §7per hit.");
        lore.add("§7Max §c+100 ❁ Strength§7.");
        return lore;
    }
}
