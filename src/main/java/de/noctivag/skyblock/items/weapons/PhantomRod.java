package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class PhantomRod extends CustomItem {
    public PhantomRod() {
        super("PHANTOM_ROD", "Phantom Rod", ItemRarity.LEGENDARY, ItemType.FISHING_ROD, Material.DIAMOND_SWORD);
        setDamage(140);
        setStrength(80);
        setCritChance(0);
        setIntelligence(65);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Ghost fish");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
