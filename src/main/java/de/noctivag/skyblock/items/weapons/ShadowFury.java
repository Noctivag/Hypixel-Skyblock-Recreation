package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * Shadow Fury - Legendary Sword
 * Ability: Shadow Fury
 */
public class ShadowFury extends CustomItem {

    public ShadowFury() {
        super("SHADOW_FURY", "Shadow Fury", ItemRarity.LEGENDARY, ItemType.SWORD, Material.NETHERITE_SWORD);

        setDamage(310);
        setStrength(110);
        setCritDamage(80);
        setAttackSpeed(30);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Shadow Fury §e§lRIGHT CLICK");
        lore.add("§7Rapidly teleports you to up to");
        lore.add("§75 §7enemies within §a12 blocks§7,");
        lore.add("§7hitting them for §c20,000 §7each.");
        lore.add("§8Mana Cost: §3200");
        lore.add("");
        lore.add("§7§8This item can be reforged!");
        return lore;
    }
}
