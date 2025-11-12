package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * Aspect of the End - Legendary Sword
 * Ability: Instant Transmission - Teleport 8 blocks ahead
 */
public class AspectOfTheEnd extends CustomItem {

    public AspectOfTheEnd() {
        super("ASPECT_OF_THE_END", "Aspect of the End", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);

        setDamage(100);
        setStrength(100);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Instant Transmission §e§lRIGHT CLICK");
        lore.add("§7Teleport §a8 blocks §7ahead of");
        lore.add("§7you. Then gain §a+50 §c❁ Strength");
        lore.add("§7for §a5 seconds§7.");
        lore.add("§8Mana Cost: §350");
        return lore;
    }
}
