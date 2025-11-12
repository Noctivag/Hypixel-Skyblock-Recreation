package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * Scylla - Legendary Bow
 * Ability: Ice Shot - Freeze enemies
 */
public class ScyllaBow extends CustomItem {

    public ScyllaBow() {
        super("SCYLLA", "Scylla", ItemRarity.LEGENDARY, ItemType.BOW, Material.BOW);

        setDamage(160);
        setStrength(80);
        setCritDamage(100);
        setAttackSpeed(50);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Ice Shot");
        lore.add("§7Every arrow shot freezes enemies");
        lore.add("§7for §a2 seconds §7and deals");
        lore.add("§7§c+50% §7damage to frozen enemies.");
        lore.add("");
        lore.add("§6Ability: Frost Barrage §e§lRIGHT CLICK");
        lore.add("§7Fire §a5 arrows §7in quick");
        lore.add("§7succession dealing massive damage.");
        lore.add("§8Mana Cost: §3100");
        return lore;
    }
}
