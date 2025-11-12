package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * Terminator - Legendary Bow
 * Ability: Ender Warp + Rapid Fire
 */
public class TerminatorBow extends CustomItem {

    public TerminatorBow() {
        super("TERMINATOR", "Terminator", ItemRarity.LEGENDARY, ItemType.BOW, Material.BOW);

        setDamage(180);
        setStrength(100);
        setCritChance(30);
        setCritDamage(150);
        setAttackSpeed(100);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Ender Warp §e§lSNEAK + RIGHT CLICK");
        lore.add("§7Instantly warp to your arrow.");
        lore.add("§8Mana Cost: §3250");
        lore.add("");
        lore.add("§6Ability: Rapid Fire");
        lore.add("§7Shoot §a3 arrows §7at once!");
        lore.add("");
        lore.add("§7§8This item can be reforged!");
        return lore;
    }
}
