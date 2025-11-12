package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * Aspect of the Dragons - Legendary Sword
 * Ability: Dragon Rage - Deal massive damage
 */
public class AspectOfTheDragons extends CustomItem {

    public AspectOfTheDragons() {
        super("ASPECT_OF_THE_DRAGONS", "Aspect of the Dragons", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);

        setDamage(225);
        setStrength(100);
        setCritDamage(50);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Dragon Rage §e§lRIGHT CLICK");
        lore.add("§7All §c5 Dragon Fusions§7 combined!");
        lore.add("§7Deals up to §c+300% §7damage.");
        lore.add("§7Strike your enemies with powerful");
        lore.add("§7dragon energy dealing §c12,000");
        lore.add("§cdamage§7. Consecutive uses in a");
        lore.add("§7short time will §cpenalize§7 mana cost.");
        lore.add("§8Mana Cost: §3100");
        return lore;
    }
}
