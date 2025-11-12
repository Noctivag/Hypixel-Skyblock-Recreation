package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

/**
 * Hyperion - Legendary Sword
 * Ability: Wither Impact - Massive AoE attack
 */
public class HyperionSword extends CustomItem {

    public HyperionSword() {
        super("HYPERION", "Hyperion", ItemRarity.LEGENDARY, ItemType.SWORD, Material.NETHERITE_SWORD);

        setDamage(260);
        setStrength(150);
        setCritDamage(125);
        setIntelligence(300);
        setFerocity(30);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Wither Impact §e§lRIGHT CLICK");
        lore.add("§7Deals §c300,000 §7damage to");
        lore.add("§7nearby enemies. Also applies the");
        lore.add("§7wither shield scroll ability reducing");
        lore.add("§7damage taken and granting an");
        lore.add("§7§cabsorption shield §7for §a5s§7.");
        lore.add("§8Mana Cost: §3150");
        lore.add("");
        lore.add("§6Ability: Implosion §e§lLEFT CLICK");
        lore.add("§7Deals §c300,000 §7damage to all");
        lore.add("§7enemies in a §a10 block §7radius.");
        lore.add("§8Mana Cost: §3300");
        return lore;
    }
}
