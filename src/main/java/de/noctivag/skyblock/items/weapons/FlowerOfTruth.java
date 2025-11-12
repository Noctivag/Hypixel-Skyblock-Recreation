package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * Flower of Truth - Legendary Sword
 * Ability: Heat-Seeking Rose
 */
public class FlowerOfTruth extends CustomItem {

    public FlowerOfTruth() {
        super("FLOWER_OF_TRUTH", "Flower of Truth", ItemRarity.LEGENDARY, ItemType.SWORD, Material.RED_TULIP);

        setDamage(160);
        setStrength(120);
        setCritDamage(60);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Heat-Seeking Rose §e§lRIGHT CLICK");
        lore.add("§7Shoots a heat-seeking rose");
        lore.add("§7that ricochets between enemies,");
        lore.add("§7damaging up to §a3 §7of your");
        lore.add("§7foes! Damage multiplies as more");
        lore.add("§7enemies are hit.");
        lore.add("§8Mana Cost: §3100");
        return lore;
    }
}
