package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.items.MissingItemsSystem;
import de.noctivag.skyblock.items.MissingItemsSystem.ItemCategory;
import de.noctivag.skyblock.items.MissingItemsSystem.MissingItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * MissingItemsGUI - GUI für fehlende Items
 * 
 * Features:
 * - Kategorien-Übersicht
 * - Item-Details
 * - Crafting-Rezepte
 * - Statistiken
 */
public class MissingItemsGUI {
    
    private final MissingItemsSystem missingItemsSystem;
    private final Map<ItemCategory, List<MissingItem>> categorizedItems;
    
    public MissingItemsGUI(SkyblockPlugin plugin) {
        this.missingItemsSystem = new MissingItemsSystem(plugin);
        this.categorizedItems = missingItemsSystem.getMissingItemsByCategory();
    }
    
    /**
     * Öffnet das Hauptmenü für fehlende Items
     */
    public void openMainMenu(Player player) {
        org.bukkit.inventory.Inventory inventory = Bukkit.createInventory(null, 54, Component.text("§6§lMissing Items"));
        
        // Header
        inventory.setItem(4, createGuiItem(Material.BOOK, "§6§lMissing Items",
            "§7Übersicht aller fehlenden Hypixel SkyBlock Items",
            "",
            "§7Total Items: §a" + missingItemsSystem.getMissingItemCount(),
            "§7Kategorien: §a" + categorizedItems.size()));
        
        // Kategorien
        int slot = 10;
        for (ItemCategory category : ItemCategory.values()) {
            List<MissingItem> items = categorizedItems.get(category);
            int itemCount = items != null ? items.size() : 0;
            
            Material categoryMaterial = getCategoryMaterial(category);
            String categoryName = category.getDisplayName() + " §7(" + itemCount + ")";
            
            inventory.setItem(slot, createGuiItem(categoryMaterial, categoryName,
                "§7Klicke um " + category.getDisplayName().toLowerCase() + " Items anzuzeigen",
                "",
                "§7Items: §a" + itemCount));
            
            slot++;
            if (slot == 17) slot = 19;
            if (slot == 26) slot = 28;
            if (slot == 35) slot = 37;
        }
        
        // Statistiken
        inventory.setItem(49, createGuiItem(Material.PAPER, "§e§lStatistiken",
            "§7Zeigt detaillierte Statistiken",
            "",
            "§7Total Items: §a" + missingItemsSystem.getMissingItemCount(),
            "§7Kategorien: §a" + categorizedItems.size()));
        
        // Schließen
        inventory.setItem(53, createGuiItem(Material.BARRIER, "§c§lSchließen",
            "§7Schließe das Menü"));
        
        player.openInventory(inventory);
    }
    
    /**
     * Öffnet eine Kategorie-spezifische GUI
     */
    public void openCategoryGUI(Player player, ItemCategory category) {
        List<MissingItem> items = categorizedItems.get(category);
        if (items == null || items.isEmpty()) {
            player.sendMessage("§cKeine Items in dieser Kategorie gefunden!");
            return;
        }
        
        org.bukkit.inventory.Inventory inventory = Bukkit.createInventory(null, 54, Component.text(category.getDisplayName() + " §7Items"));
        
        // Header
        inventory.setItem(4, createGuiItem(getCategoryMaterial(category), category.getDisplayName() + " §7Items",
            "§7" + items.size() + " Items in dieser Kategorie",
            "",
            "§7Klicke auf ein Item für Details"));
        
        // Items
        int slot = 9;
        for (MissingItem item : items) {
            if (slot >= 45) break; // Max 36 items per page
            
            ItemStack itemStack = missingItemsSystem.createMissingItemStack(item.getName());
            if (itemStack != null) {
                inventory.setItem(slot, itemStack);
            }
            
            slot++;
        }
        
        // Zurück
        inventory.setItem(45, createGuiItem(Material.ARROW, "§e§lZurück",
            "§7Zurück zum Hauptmenü"));
        
        // Schließen
        inventory.setItem(53, createGuiItem(Material.BARRIER, "§c§lSchließen",
            "§7Schließe das Menü"));
        
        player.openInventory(inventory);
    }
    
    /**
     * Öffnet eine Item-Detail-GUI
     */
    public void openItemDetailGUI(Player player, MissingItem item) {
        org.bukkit.inventory.Inventory inventory = Bukkit.createInventory(null, 54, Component.text("§6§l" + item.getDisplayName()));
        
        // Header
        inventory.setItem(4, createGuiItem(item.getMaterial(), item.getDisplayName(),
            "§7" + item.getDescription(),
            "",
            "§7Rarity: " + item.getRarity().getDisplayName(),
            "§7Category: " + item.getCategory().getDisplayName(),
            "§7Base Damage: " + item.getBaseDamage()));
        
        // Features
        List<String> featuresLore = new ArrayList<>();
        featuresLore.add("§7Item-Features:");
        featuresLore.add("");
        featuresLore.addAll(item.getFeatures());
        inventory.setItem(19, createGuiItem(Material.ENCHANTED_BOOK, "§a§lFeatures", featuresLore.toArray(new String[0])));
        
        // Requirements
        List<String> requirementsLore = new ArrayList<>();
        requirementsLore.add("§7Crafting-Requirements:");
        requirementsLore.add("");
        requirementsLore.addAll(item.getRequirements());
        inventory.setItem(21, createGuiItem(Material.CRAFTING_TABLE, "§e§lRequirements", requirementsLore.toArray(new String[0])));
        
        // Statistiken
        inventory.setItem(23, createGuiItem(Material.PAPER, "§b§lStatistiken",
            "§7Item-Statistiken:",
            "",
            "§7Rarity: " + item.getRarity().getDisplayName(),
            "§7Category: " + item.getCategory().getDisplayName(),
            "§7Base Damage: " + item.getBaseDamage(),
            "§7Multiplier: " + item.getRarity().getMultiplier() + "x"));
        
        // Crafting
        inventory.setItem(25, createGuiItem(Material.ANVIL, "§6§lCrafting",
            "§7Crafting-Information:",
            "",
            "§7Klicke um Crafting-Rezept anzuzeigen"));
        
        // Zurück
        inventory.setItem(45, createGuiItem(Material.ARROW, "§e§lZurück",
            "§7Zurück zur Kategorie"));
        
        // Schließen
        inventory.setItem(53, createGuiItem(Material.BARRIER, "§c§lSchließen",
            "§7Schließe das Menü"));
        
        player.openInventory(inventory);
    }
    
    /**
     * Öffnet eine Statistiken-GUI
     */
    public void openStatisticsGUI(Player player) {
        org.bukkit.inventory.Inventory inventory = Bukkit.createInventory(null, 54, Component.text("§6§lMissing Items Statistiken"));
        
        // Header
        inventory.setItem(4, createGuiItem(Material.PAPER, "§6§lMissing Items Statistiken",
            "§7Detaillierte Statistiken über fehlende Items",
            "",
            "§7Total Items: §a" + missingItemsSystem.getMissingItemCount()));
        
        // Kategorie-Statistiken
        int slot = 9;
        for (ItemCategory category : ItemCategory.values()) {
            List<MissingItem> items = categorizedItems.get(category);
            int itemCount = items != null ? items.size() : 0;
            
            Material categoryMaterial = getCategoryMaterial(category);
            String categoryName = category.getDisplayName() + " §7(" + itemCount + ")";
            
            inventory.setItem(slot, createGuiItem(categoryMaterial, categoryName,
                "§7Items in dieser Kategorie: §a" + itemCount,
                "",
                "§7Klicke für Details"));
            
            slot++;
            if (slot == 18) slot = 27;
            if (slot == 36) slot = 45;
        }
        
        // Gesamtstatistiken
        inventory.setItem(49, createGuiItem(Material.BOOK, "§e§lGesamtstatistiken",
            "§7Gesamtstatistiken:",
            "",
            "§7Total Items: §a" + missingItemsSystem.getMissingItemCount(),
            "§7Kategorien: §a" + categorizedItems.size(),
            "§7Implementiert: §c0",
            "§7Fehlend: §a" + missingItemsSystem.getMissingItemCount()));
        
        // Zurück
        inventory.setItem(45, createGuiItem(Material.ARROW, "§e§lZurück",
            "§7Zurück zum Hauptmenü"));
        
        // Schließen
        inventory.setItem(53, createGuiItem(Material.BARRIER, "§c§lSchließen",
            "§7Schließe das Menü"));
        
        player.openInventory(inventory);
    }
    
    /**
     * Gibt das Material für eine Kategorie zurück
     */
    private Material getCategoryMaterial(ItemCategory category) {
        switch (category) {
            case SWORD:
                return Material.DIAMOND_SWORD;
            case BOW:
                return Material.BOW;
            case WAND:
                return Material.BLAZE_ROD;
            case STAFF:
                return Material.STICK;
            case ARMOR:
                return Material.DIAMOND_CHESTPLATE;
            case ACCESSORY:
                return Material.GOLD_INGOT;
            case TOOL:
                return Material.DIAMOND_PICKAXE;
            case SPECIAL:
                return Material.BOOK;
            default:
                return Material.BARRIER;
        }
    }
    
    /**
     * Erstellt ein GUI-Item
     */
    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.stream(lore).map(line -> Component.text(line)).toList());
            item.setItemMeta(meta);
        }
        
        return item;
    }
}
