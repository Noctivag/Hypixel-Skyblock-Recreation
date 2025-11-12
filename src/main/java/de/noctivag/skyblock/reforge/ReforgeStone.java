package de.noctivag.skyblock.reforge;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all Reforge Stones
 */
public abstract class ReforgeStone {

    protected Reforge reforge;
    protected int cost;
    protected Rarity rarity;

    public enum Rarity {
        COMMON("§f"),
        UNCOMMON("§a"),
        RARE("§9"),
        EPIC("§5"),
        LEGENDARY("§6"),
        MYTHIC("§d");

        private final String color;
        Rarity(String color) { this.color = color; }
        public String getColor() { return color; }
    }

    public ReforgeStone(Reforge reforge, int cost, Rarity rarity) {
        this.reforge = reforge;
        this.cost = cost;
        this.rarity = rarity;
    }

    /**
     * Create the ItemStack for this reforge stone
     */
    public ItemStack create() {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(rarity.getColor() + getDisplayName());

            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§7Apply the " + rarity.getColor() + reforge.getDisplayName() + " §7reforge");
            lore.add("§7to your " + getApplicableItems() + "§7.");
            lore.add("");

            // Add reforge stats
            if (reforge.getStrength() > 0) lore.add("§7§c+" + reforge.getStrength() + " ❁ Strength");
            if (reforge.getCritChance() > 0) lore.add("§7§9+" + reforge.getCritChance() + "% ☣ Crit Chance");
            if (reforge.getCritDamage() > 0) lore.add("§7§9+" + reforge.getCritDamage() + "% ☠ Crit Damage");
            if (reforge.getAttackSpeed() > 0) lore.add("§7§e+" + reforge.getAttackSpeed() + "% ⚔ Attack Speed");
            if (reforge.getHealth() > 0) lore.add("§7§a+" + reforge.getHealth() + " ❤ Health");
            if (reforge.getDefense() > 0) lore.add("§7§a+" + reforge.getDefense() + " ❈ Defense");
            if (reforge.getIntelligence() > 0) lore.add("§7§b+" + reforge.getIntelligence() + " ✎ Intelligence");

            lore.add("");
            lore.add("§7Cost: §6" + cost + " Coins");
            lore.add("");
            lore.add(rarity.getColor() + rarity.name() + " REFORGE STONE");

            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

    protected abstract Material getMaterial();
    protected abstract String getDisplayName();
    protected abstract String getApplicableItems();

    public Reforge getReforge() { return reforge; }
    public int getCost() { return cost; }
    public Rarity getRarity() { return rarity; }
}
