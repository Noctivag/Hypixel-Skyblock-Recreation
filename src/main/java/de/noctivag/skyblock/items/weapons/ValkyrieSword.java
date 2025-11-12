package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * Valkyrie - Legendary Sword
 * Ability: Valkyrie
 */
public class ValkyrieSword extends CustomItem {

    public ValkyrieSword() {
        super("VALKYRIE", "Valkyrie", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);

        setDamage(150);
        setStrength(80);
        setCritChance(20);
        setCritDamage(70);
        setFerocity(20);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Valkyrie §e§lRIGHT CLICK");
        lore.add("§7Charge forward, gaining");
        lore.add("§7§f+200% §c❁ Strength §7and");
        lore.add("§7knocking back enemies for §a3s§7.");
        lore.add("§8Mana Cost: §375");
        lore.add("");
        lore.add("§7§8This item can be reforged!");
        return lore;
    }
}
