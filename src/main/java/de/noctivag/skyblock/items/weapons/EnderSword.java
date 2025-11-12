package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class EnderSword extends CustomItem {
    public EnderSword() {
        super("ENDER_SWORD", "Ender Sword", ItemRarity.RARE, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(100);
        setStrength(60);
        setCritChance(10);
        setIntelligence(40);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Ender teleport");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
