package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * Livid Dagger - Legendary Sword
 * Ability: Throw + Attack Speed
 */
public class LividDagger extends CustomItem {

    public LividDagger() {
        super("LIVID_DAGGER", "Livid Dagger", ItemRarity.LEGENDARY, ItemType.SWORD, Material.GOLDEN_SWORD);

        setDamage(110);
        setStrength(60);
        setCritDamage(100);
        setAttackSpeed(100);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Throw §e§lRIGHT CLICK");
        lore.add("§7Throw your dagger at enemies,");
        lore.add("§7dealing §c30,000 §7damage.");
        lore.add("§8Cooldown: §a3s");
        lore.add("");
        lore.add("§7§8This item can be reforged!");
        return lore;
    }
}
