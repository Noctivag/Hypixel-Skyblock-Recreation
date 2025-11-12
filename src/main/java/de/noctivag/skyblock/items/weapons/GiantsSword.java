package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class GiantsSword extends CustomItem {
    public GiantsSword() {
        super("GIANTS_SWORD", "Giant's Sword", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(500);
        setStrength(200);
        setCritDamage(150);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Giant's Slam §e§lRIGHT CLICK");
        lore.add("§7Slam the ground dealing");
        lore.add("§7§c100,000 §7damage to nearby");
        lore.add("§7enemies and slowing them.");
        lore.add("§8Mana Cost: §3150");
        return lore;
    }
}
