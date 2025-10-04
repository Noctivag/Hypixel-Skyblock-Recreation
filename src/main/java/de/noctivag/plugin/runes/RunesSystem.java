package de.noctivag.plugin.runes;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Runes System - Hypixel Skyblock Style
 * 
 * Features:
 * - Rune application to items
 * - Rune removal from items
 * - Rune combination system
 * - Rune statistics
 * - Rune GUI interface
 */
public class RunesSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, Map<RuneType, Integer>> playerRuneStats = new HashMap<>();

    public RunesSystem(Plugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public boolean applyRune(Player player, ItemStack item, RuneType type) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        // Add rune lore
        List<Component> lore = meta.lore();
        if (lore == null) lore = new ArrayList<>();
        
        lore.add(Component.text("§7" + type.getDisplayName() + " Rune"));
        meta.lore(lore);
        item.setItemMeta(meta);

        // Update stats
        Map<RuneType, Integer> stats = playerRuneStats.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        stats.put(type, stats.getOrDefault(type, 0) + 1);

        return true;
    }

    public boolean removeRune(Player player, ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        List<Component> lore = meta.lore();
        if (lore == null) return false;

        // Remove rune lore
        lore.removeIf(line -> line.toString().contains("Rune"));
        meta.lore(lore);
        item.setItemMeta(meta);

        return true;
    }

    public boolean combineRunes(Player player, RuneType type1, RuneType type2) {
        Map<RuneType, Integer> stats = playerRuneStats.get(player.getUniqueId());
        if (stats == null) return false;

        if (stats.getOrDefault(type1, 0) < 1 || stats.getOrDefault(type2, 0) < 1) {
            return false;
        }

        // Remove runes
        stats.put(type1, stats.get(type1) - 1);
        stats.put(type2, stats.get(type2) - 1);

        // Add combined rune (example: combine two basic runes into one advanced)
        RuneType combinedType = RuneType.ADVANCED; // Example
        stats.put(combinedType, stats.getOrDefault(combinedType, 0) + 1);

        return true;
    }

    public Map<RuneType, Integer> getPlayerRuneStats(Player player) {
        return playerRuneStats.getOrDefault(player.getUniqueId(), new HashMap<>());
    }

    public void openRuneGUI(Player player) {
        // Create rune GUI
        org.bukkit.inventory.Inventory gui = org.bukkit.Bukkit.createInventory(null, 54, Component.text("§6§lRunes System"));
        
        // Add rune items
        for (RuneType type : RuneType.values()) {
            ItemStack runeItem = new ItemStack(type.getMaterial());
            ItemMeta meta = runeItem.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(type.getDisplayName()));
                meta.lore(Arrays.asList(
                    Component.text("§7" + type.getDescription()),
                    Component.text("§eKlicke, um diese Rune zu verwenden")
                ));
                runeItem.setItemMeta(meta);
            }
            gui.addItem(runeItem);
        }

        player.openInventory(gui);
    }

    public enum RuneType {
        BASIC("§fBasic Rune", "§7A basic rune", Material.PAPER),
        ADVANCED("§aAdvanced Rune", "§7An advanced rune", Material.ENCHANTED_BOOK),
        LEGENDARY("§6Legendary Rune", "§7A legendary rune", Material.NETHER_STAR),
        MYTHIC("§dMythic Rune", "§7A mythic rune", Material.DRAGON_EGG);

        private final String displayName;
        private final String description;
        private final Material material;

        RuneType(String displayName, String description, Material material) {
            this.displayName = displayName;
            this.description = description;
            this.material = material;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getMaterial() { return material; }
    }
}
