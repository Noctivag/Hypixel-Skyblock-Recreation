package de.noctivag.skyblock.reforge;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * System to handle reforging of items
 */
public class ReforgeSystem {

    private final SkyblockPlugin plugin;
    private final Map<UUID, Reforge> pendingReforges = new HashMap<>();
    
    public ReforgeSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Apply a reforge to an item
     */
    public boolean applyReforge(Player player, ItemStack item, Reforge reforge, boolean useCoins) {
        if (item == null || item.getType().isAir()) {
            player.sendMessage("§cInvalid item!");
            return false;
        }

        // Check if item type matches reforge type
        if (!isValidItemType(item, reforge)) {
            player.sendMessage("§cThis reforge cannot be applied to this item!");
            return false;
        }

        // Check if player can afford it (if using coins)
        if (useCoins && reforge.requiresStone()) {
            int cost = getReforgeCost(reforge);
            // TODO: Check player balance
        }

        // Apply reforge to item
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            // Update lore with reforge
            List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
            
            // Remove old reforge if exists
            lore.removeIf(line -> line.contains("⚒ Reforge:"));
            
            // Add new reforge
            lore.add("§d⚒ Reforge: " + reforge.getDisplayName());
            
            // Update stats in lore
            updateStatsInLore(lore, reforge);
            
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        player.sendMessage("§aSuccessfully applied §d" + reforge.getDisplayName() + " §areforge!");
        return true;
    }

    /**
     * Check if item type is valid for reforge
     */
    private boolean isValidItemType(ItemStack item, Reforge reforge) {
        String typeName = item.getType().name();
        
        return switch (reforge.getType()) {
            case WEAPON -> typeName.contains("SWORD") || typeName.contains("AXE");
            case BOW -> typeName.contains("BOW") || typeName.contains("CROSSBOW");
            case ARMOR -> typeName.contains("HELMET") || typeName.contains("CHESTPLATE") ||
                         typeName.contains("LEGGINGS") || typeName.contains("BOOTS");
            case TOOL -> typeName.contains("PICKAXE") || typeName.contains("AXE") ||
                        typeName.contains("HOE") || typeName.contains("SHOVEL");
        };
    }

    /**
     * Get cost of a reforge based on requirements
     */
    private int getReforgeCost(Reforge reforge) {
        // Base costs
        return switch (reforge.getType()) {
            case WEAPON -> 50000;
            case BOW -> 50000;
            case ARMOR -> 25000;
            case TOOL -> 10000;
        };
    }

    /**
     * Update item stats in lore based on reforge
     */
    private void updateStatsInLore(List<String> lore, Reforge reforge) {
        // This would update the stat lines in the lore
        // For now, just add a separator
        if (!lore.isEmpty() && !lore.get(lore.size() - 1).isEmpty()) {
            lore.add("");
        }
    }

    /**
     * Remove reforge from an item
     */
    public boolean removeReforge(Player player, ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasLore()) {
            List<String> lore = new ArrayList<>(meta.getLore());
            boolean removed = lore.removeIf(line -> line.contains("⚒ Reforge:"));
            
            if (removed) {
                meta.setLore(lore);
                item.setItemMeta(meta);
                player.sendMessage("§aReforge removed!");
                return true;
            }
        }

        player.sendMessage("§cNo reforge to remove!");
        return false;
    }

    /**
     * Get the current reforge on an item
     */
    public Reforge getReforge(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta.hasLore()) {
            for (String line : meta.getLore()) {
                if (line.contains("⚒ Reforge:")) {
                    String reforgeName = line.replace("§d⚒ Reforge: ", "").toUpperCase();
                    try {
                        return Reforge.valueOf(reforgeName);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                }
            }
        }

        return null;
    }
}
