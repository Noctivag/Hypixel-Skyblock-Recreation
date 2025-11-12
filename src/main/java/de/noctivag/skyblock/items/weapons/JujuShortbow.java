package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * Juju Shortbow - Epic Bow
 * Ability: Triple Shot
 */
public class JujuShortbow extends CustomItem {

    public JujuShortbow() {
        super("JUJU_SHORTBOW", "Juju Shortbow", ItemRarity.EPIC, ItemType.BOW, Material.BOW);

        setDamage(140);
        setStrength(40);
        setCritDamage(80);
        setAttackSpeed(75);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Triple Shot");
        lore.add("§7Shoot §a3 arrows §7at once!");
        lore.add("§7The 2 extra arrows deal");
        lore.add("§740% §7of the damage and");
        lore.add("§7home §7towards nearby targets.");
        return lore;
    }
}
