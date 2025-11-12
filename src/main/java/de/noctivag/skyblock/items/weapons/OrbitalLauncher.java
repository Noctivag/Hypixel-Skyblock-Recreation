package de.noctivag.skyblock.items.weapons;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;

public class OrbitalLauncher extends CustomItem {
    public OrbitalLauncher() {
        super("VOIDEDGE_KATANA", "Voidedge Katana", ItemRarity.LEGENDARY, ItemType.SWORD, Material.DIAMOND_SWORD);
        setDamage(255);
        setStrength(148);
        setCritChance(18);
        setIntelligence(92);
    }

    @Override
    protected List<String> getAbilityLore() {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§6Ability: Void slash");
        lore.add("§7Special weapon ability.");
        return lore;
    }
}
