package de.noctivag.plugin.reforges;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;

/**
 * Reforge GUI - Hypixel SkyBlock Style Reforge Interface
 * 
 * Features:
 * - Reforge table interface
 * - Item preview with stats
 * - Reforge selection and application
 * - Cost calculation and payment
 * - Reforge history and undo
 */
public class ReforgeGUI {
    
    private final Plugin plugin;
    private final ReforgeSystem reforgeSystem;
    
    public ReforgeGUI(Plugin plugin, ReforgeSystem reforgeSystem) {
        this.plugin = plugin;
        this.reforgeSystem = reforgeSystem;
    }
    
    public void openReforgeTable(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6⚔ Reforge Table");
        
        // Item slot for reforge
        gui.setItem(13, createItemSlot());
        
        // Reforge categories
        gui.setItem(20, createCategoryItem(Material.DIAMOND_SWORD, "§cWeapon Reforges", 
            "§7Reforges for swords and axes", "weapon"));
        
        gui.setItem(21, createCategoryItem(Material.DIAMOND_CHESTPLATE, "§bArmor Reforges", 
            "§7Reforges for armor pieces", "armor"));
        
        gui.setItem(22, createCategoryItem(Material.DIAMOND_PICKAXE, "§eTool Reforges", 
            "§7Reforges for tools", "tool"));
        
        gui.setItem(23, createCategoryItem(Material.BOW, "§aBow Reforges", 
            "§7Reforges for bows", "bow"));
        
        gui.setItem(24, createCategoryItem(Material.NETHER_STAR, "§6Special Reforges", 
            "§7Rare and special reforges", "special"));
        
        // Control buttons
        gui.setItem(45, createButton(Material.RED_WOOL, "§cRemove Reforge", 
            "§7Remove current reforge from item", "remove"));
        
        gui.setItem(46, createButton(Material.YELLOW_WOOL, "§eUndo Reforge", 
            "§7Undo last reforge (if available)", "undo"));
        
        gui.setItem(53, createButton(Material.GREEN_WOOL, "§aApply Reforge", 
            "§7Apply selected reforge to item", "apply"));
        
        player.openInventory(gui);
    }
    
    public void openReforgeCategory(Player player, String category, ItemStack item) {
        if (item == null || item.getType().isAir()) {
            player.sendMessage("§cPlease place an item in the reforge slot first!");
            return;
        }
        
        Inventory gui = Bukkit.createInventory(null, 54, "§6⚔ " + getCategoryTitle(category) + " Reforges");
        
        List<ReforgeSystem.Reforge> categoryReforges = getReforgesByCategory(category, item.getType());
        
        int slot = 10;
        for (ReforgeSystem.Reforge reforge : categoryReforges) {
            if (slot >= 44) break;
            
            gui.setItem(slot, createReforgeItem(reforge, item));
            slot++;
            
            if (slot % 9 == 8) slot += 2; // Skip to next row
        }
        
        // Back button
        gui.setItem(49, createButton(Material.ARROW, "§e← Back", "§7Return to main menu", "back"));
        
        player.openInventory(gui);
    }
    
    private ItemStack createItemSlot() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize("§cItem Slot"));
        meta.lore(Arrays.asList(
            LegacyComponentSerializer.legacySection().deserialize("§7Place an item here to reforge it"),
            LegacyComponentSerializer.legacySection().deserialize(""),
            LegacyComponentSerializer.legacySection().deserialize("§eCompatible items:"),
            LegacyComponentSerializer.legacySection().deserialize("§7- Weapons (Swords, Axes)"),
            LegacyComponentSerializer.legacySection().deserialize("§7- Armor (Helmet, Chestplate, Leggings, Boots)"),
            LegacyComponentSerializer.legacySection().deserialize("§7- Tools (Pickaxe, Axe, Shovel)"),
            LegacyComponentSerializer.legacySection().deserialize("§7- Bows and Crossbows")
        ));
        
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createCategoryItem(Material material, String name, String lore, String category) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize(name));
        meta.lore(Arrays.asList(
            LegacyComponentSerializer.legacySection().deserialize(lore),
            LegacyComponentSerializer.legacySection().deserialize(""),
            LegacyComponentSerializer.legacySection().deserialize("§eClick to view reforges!")
        ));
        
        NamespacedKey key = new NamespacedKey(plugin, "reforge_category");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, category);
        
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createReforgeItem(ReforgeSystem.Reforge reforge, ItemStack targetItem) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize("§d" + reforge.getName() + " Reforge"));
        
        List<Component> lore = new ArrayList<>();
        lore.add(LegacyComponentSerializer.legacySection().deserialize(reforge.getDescription()));
        lore.add(LegacyComponentSerializer.legacySection().deserialize(""));
        lore.add(LegacyComponentSerializer.legacySection().deserialize("§7Rarity: " + reforge.getRarity().getDisplayName()));
        lore.add(LegacyComponentSerializer.legacySection().deserialize("§7Cost: §6" + String.format("%.0f", reforgeSystem.calculateReforgeCost(reforge, targetItem)) + " coins"));
        lore.add(LegacyComponentSerializer.legacySection().deserialize(""));
        lore.add(LegacyComponentSerializer.legacySection().deserialize("§7Stats:"));
        
        // Add stat preview
        Map<String, Double> stats = reforgeSystem.calculateReforgeStats(reforge, targetItem);
        for (Map.Entry<String, Double> stat : stats.entrySet()) {
            lore.add(LegacyComponentSerializer.legacySection().deserialize("§7- " + stat.getKey() + ": §a+" + String.format("%.1f", stat.getValue())));
        }
        
        lore.add(LegacyComponentSerializer.legacySection().deserialize(""));
        
        // Check if item already has this reforge
        ReforgeSystem.Reforge currentReforge = reforgeSystem.getItemReforge(targetItem);
        if (currentReforge != null && currentReforge.getName().equals(reforge.getName())) {
            lore.add(LegacyComponentSerializer.legacySection().deserialize("§cAlready applied to this item!"));
        } else {
            lore.add(LegacyComponentSerializer.legacySection().deserialize("§eClick to apply this reforge!"));
        }
        
        meta.lore(lore);
        
        NamespacedKey key = new NamespacedKey(plugin, "reforge_select");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, reforge.getName());
        
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createButton(Material material, String name, String lore, String action) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize(name));
        meta.lore(Arrays.asList(
            LegacyComponentSerializer.legacySection().deserialize(lore),
            LegacyComponentSerializer.legacySection().deserialize(""),
            LegacyComponentSerializer.legacySection().deserialize("§eClick to " + action + "!")
        ));
        
        NamespacedKey key = new NamespacedKey(plugin, "reforge_action");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, action);
        
        item.setItemMeta(meta);
        return item;
    }
    
    private String getCategoryTitle(String category) {
        return switch (category) {
            case "weapon" -> "Weapon";
            case "armor" -> "Armor";
            case "tool" -> "Tool";
            case "bow" -> "Bow";
            case "special" -> "Special";
            default -> "Reforge";
        };
    }
    
    private List<ReforgeSystem.Reforge> getReforgesByCategory(String category, Material material) {
        return switch (category) {
            case "weapon" -> new ArrayList<>(reforgeSystem.getAllReforges().stream()
                .filter(r -> r.getCompatibleMaterials().contains(material) && 
                           (material.name().contains("SWORD") || material.name().contains("AXE")))
                .toList());
            case "armor" -> new ArrayList<>(reforgeSystem.getAllReforges().stream()
                .filter(r -> r.getCompatibleMaterials().contains(material) && 
                           (material.name().contains("HELMET") || material.name().contains("CHESTPLATE") ||
                            material.name().contains("LEGGINGS") || material.name().contains("BOOTS")))
                .toList());
            case "tool" -> new ArrayList<>(reforgeSystem.getAllReforges().stream()
                .filter(r -> r.getCompatibleMaterials().contains(material) && 
                           (material.name().contains("PICKAXE") || material.name().contains("SHOVEL") ||
                            material.name().contains("HOE")))
                .toList());
            case "bow" -> new ArrayList<>(reforgeSystem.getAllReforges().stream()
                .filter(r -> r.getCompatibleMaterials().contains(material) && 
                           (material == Material.BOW || material == Material.CROSSBOW))
                .toList());
            case "special" -> new ArrayList<>(reforgeSystem.getReforgesByRarity(ReforgeSystem.ReforgeRarity.LEGENDARY));
            default -> new ArrayList<>();
        };
    }
    
    public void showReforgePreview(Player player, ItemStack item, ReforgeSystem.Reforge reforge) {
        if (item == null || item.getType().isAir()) return;
        
        player.sendMessage("§6=== " + reforge.getName() + " Reforge Preview ===");
        player.sendMessage("§7Item: §f" + item.getType().name());
        player.sendMessage("§7Description: §f" + reforge.getDescription());
        player.sendMessage("§7Rarity: " + reforge.getRarity().getDisplayName());
        player.sendMessage("§7Cost: §6" + String.format("%.0f", reforgeSystem.calculateReforgeCost(reforge, item)) + " coins");
        player.sendMessage("");
        player.sendMessage("§7Stats that will be added:");
        
        Map<String, Double> stats = reforgeSystem.calculateReforgeStats(reforge, item);
        for (Map.Entry<String, Double> stat : stats.entrySet()) {
            player.sendMessage("§7- " + stat.getKey() + ": §a+" + String.format("%.1f", stat.getValue()));
        }
        
        player.sendMessage("");
        player.sendMessage("§eUse §a/reforge apply " + reforge.getName() + " §eto apply this reforge!");
    }
}
