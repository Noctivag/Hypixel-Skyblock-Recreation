package de.noctivag.skyblock.stats;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;

/**
 * StatContainer import - now in separate file
 */

/**
 * API für das zentrale Stat-System (Spieler, Items, Buffs)
 */
public class StatManager {
    // Basis-Stats pro Spieler
    private static final Map<Player, StatContainer> baseStats = new HashMap<>();

    // Additive Modifikatoren (z.B. Rüstung, Talismane, Buffs)
    private static final Map<Player, StatContainer> modifiers = new HashMap<>();

    // Berechnet die finalen Stats für einen Spieler
    public static StatContainer getFinalStats(Player player) {
        StatContainer result = new StatContainer();
        StatContainer base = baseStats.getOrDefault(player, new StatContainer());
        StatContainer mod = modifiers.getOrDefault(player, new StatContainer());
        for (StatType type : StatType.values()) {
            double value = base.get(type) + mod.get(type);
            result.set(type, value);
        }
        return result;
    }

    // Setzt einen Basiswert
    public static void setBaseStat(Player player, StatType type, double value) {
        baseStats.computeIfAbsent(player, p -> new StatContainer()).set(type, value);
    }

    // Fügt einen Modifikator hinzu (z.B. durch Item)
    public static void addModifier(Player player, StatType type, double value) {
        modifiers.computeIfAbsent(player, p -> new StatContainer()).add(type, value);
    }

    // Entfernt alle Modifikatoren (z.B. bei Ausziehen von Items)
    public static void clearModifiers(Player player) {
        modifiers.remove(player);
    }

    // Beispiel: Stats aus Item-Lore lesen (vereinfachte Demo)
    public static StatContainer getStatsFromItem(ItemStack item) {
        StatContainer stats = new StatContainer();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) return stats;
        for (String line : item.getItemMeta().getLore()) {
            for (StatType type : StatType.values()) {
                if (line.toUpperCase().contains(type.name())) {
                    String[] parts = line.replaceAll("[^0-9.-]", " ").trim().split(" ");
                    for (String part : parts) {
                        try {
                            double val = Double.parseDouble(part);
                            stats.add(type, val);
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }
        }
        return stats;
    }
}
