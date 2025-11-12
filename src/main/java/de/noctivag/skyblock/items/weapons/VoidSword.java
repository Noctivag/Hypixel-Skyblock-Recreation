package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class VoidSword extends CustomItem {
    public VoidSword() {
        super("VOID_SWORD", "Void Sword", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(125);
        setStrength(75);
        setCritChance(5);
        setIntelligence(50);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Void strikes");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
