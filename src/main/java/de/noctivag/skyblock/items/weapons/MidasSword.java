package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class MidasSword extends CustomItem {
    public MidasSword() {
        super("MIDAS_SWORD", "Midas Sword", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(200);
        setStrength(120);
        setCritChance(20);
        setIntelligence(80);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Greed ability");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
