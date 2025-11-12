package de.noctivag.skyblock.items.accessories;

import de.noctivag.skyblock.items.CustomItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all accessories (Talismans, Rings, Artifacts, Relics)
 */
public abstract class BaseAccessory extends CustomItem {

    protected AccessoryTier tier;
    protected AccessoryFamily family;
    protected int magicalPower;
    protected boolean enrichable;

    public enum AccessoryTier {
        TALISMAN("Talisman", 1),
        RING("Ring", 2),
        ARTIFACT("Artifact", 3),
        RELIC("Relic", 4);

        private final String name;
        private final int tier;

        AccessoryTier(String name, int tier) {
            this.name = name;
            this.tier = tier;
        }

        public String getName() { return name; }
        public int getTier() { return tier; }
    }

    public enum AccessoryFamily {
        // Combat Families
        BAT, SPIDER, WOLF, RED_CLAW, SKELETON, ZOMBIE, VAMPIRE,
        TARANTULA, REVENANT, SVEN,

        // Skill Families
        SPEED, FARMING, MINING, FORAGING, FISHING, COMBAT_SKILL,
        ALCHEMY, ENCHANTING, CARPENTRY, RUNECRAFTING,

        // Stat Families
        FEATHER, CANDY, POTION_AFFINITY, SCAVENGER, NIGHT_VISION,
        PERSONAL_COMPACTOR, PIGGY_BANK,

        // Special Families
        HEGEMONY, TREASURE, SOULFLOW, PULSE, MELODY,
        CROOKED_ARTIFACT, SEAL_OF_THE_FAMILY, RIFT_PRISM,

        // Resource Families
        WOOD_AFFINITY, MINE_AFFINITY, SEA_CREATURE, BAIT,
        MAGNETIC, GRAVITY, HASTE,

        // Single-Tier (No Upgrades)
        STANDALONE
    }

    public BaseAccessory(AccessoryTier tier, AccessoryFamily family) {
        this.tier = tier;
        this.family = family;
        this.magicalPower = calculateMagicalPower();
        this.enrichable = false;
    }

    protected int calculateMagicalPower() {
        int base = switch (rarity) {
            case COMMON -> 3;
            case UNCOMMON -> 5;
            case RARE -> 8;
            case EPIC -> 12;
            case LEGENDARY -> 16;
            case MYTHIC -> 22;
            case DIVINE, SPECIAL -> 25;
        };

        // Higher tiers give more magical power
        return base + (tier.getTier() - 1) * 2;
    }

    @Override
    public ItemStack create() {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // Set display name with tier
            meta.setDisplayName(getRarityColor() + tier.getName() + " - " + getDisplayName());

            // Create lore
            List<String> lore = new ArrayList<>();
            lore.add("");

            // Add stats
            if (health > 0) lore.add("§a+" + health + " ❤ Health");
            if (defense > 0) lore.add("§a+" + defense + " ❈ Defense");
            if (strength > 0) lore.add("§c+" + strength + " ❁ Strength");
            if (critDamage > 0) lore.add("§9+" + critDamage + "% ☠ Crit Damage");
            if (critChance > 0) lore.add("§9+" + critChance + "% ☣ Crit Chance");
            if (speed > 0) lore.add("§f+" + speed + " ✦ Speed");
            if (intelligence > 0) lore.add("§b+" + intelligence + " ✎ Intelligence");

            // Add ability lore
            List<String> abilityLore = getAbilityLore();
            if (!abilityLore.isEmpty()) {
                lore.add("");
                lore.addAll(abilityLore);
            }

            // Add magical power
            lore.add("");
            lore.add("§d⚚ Magical Power: §5" + magicalPower);

            // Add family info
            if (family != AccessoryFamily.STANDALONE) {
                lore.add("§7Family: §e" + family.name());
            }

            // Add enrichment info
            if (enrichable) {
                lore.add("");
                lore.add("§7This accessory can be §5Enriched§7!");
            }

            lore.add("");
            lore.add(getRarityColor() + rarity.name() + " ACCESSORY");

            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

    public AccessoryTier getTier() {
        return tier;
    }

    public AccessoryFamily getFamily() {
        return family;
    }

    public int getMagicalPower() {
        return magicalPower;
    }

    public boolean isEnrichable() {
        return enrichable;
    }

    protected abstract Material getMaterial();
    protected abstract String getDisplayName();
}
