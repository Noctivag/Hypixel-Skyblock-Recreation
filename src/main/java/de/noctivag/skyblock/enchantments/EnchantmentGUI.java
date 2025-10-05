package de.noctivag.skyblock.enchantments;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
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
 * Enchantment GUI - Hypixel SkyBlock Style Enchantment Interface
 * 
 * Features:
 * - Enchantment table interface
 * - Enchantment books and application
 * - Enchantment removal and upgrading
 * - Enchantment preview and costs
 * - Enchantment combinations
 */
public class EnchantmentGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final CustomEnchantmentSystem enchantmentSystem;
    
    public EnchantmentGUI(SkyblockPlugin SkyblockPlugin, CustomEnchantmentSystem enchantmentSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.enchantmentSystem = enchantmentSystem;
    }
    
    public void openEnchantmentTable(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6⚔ Enchantment Table"));
        
        // Enchantment categories
        gui.setItem(10, createCategoryItem(Material.DIAMOND_SWORD, "§cCombat Enchantments", 
            "§7Weapon and combat related enchantments", "combat"));
        
        gui.setItem(11, createCategoryItem(Material.DIAMOND_CHESTPLATE, "§bArmor Enchantments", 
            "§7Armor and protection related enchantments", "armor"));
        
        gui.setItem(12, createCategoryItem(Material.DIAMOND_PICKAXE, "§eTool Enchantments", 
            "§7Mining and tool related enchantments", "tools"));
        
        gui.setItem(13, createCategoryItem(Material.ENCHANTED_BOOK, "§dSpecial Enchantments", 
            "§7Unique and special enchantments", "special"));
        
        gui.setItem(14, createCategoryItem(Material.NETHER_STAR, "§6Legendary Enchantments", 
            "§7Powerful legendary enchantments", "legendary"));
        
        // Enchantment books
        gui.setItem(19, createEnchantmentBook("Sharpness", 5, 1500));
        gui.setItem(20, createEnchantmentBook("Power", 5, 1200));
        gui.setItem(21, createEnchantmentBook("Protection", 5, 1800));
        gui.setItem(22, createEnchantmentBook("Growth", 5, 2000));
        gui.setItem(23, createEnchantmentBook("Giant Killer", 4, 3500));
        
        gui.setItem(28, createEnchantmentBook("Execute", 4, 3200));
        gui.setItem(29, createEnchantmentBook("First Strike", 4, 2800));
        gui.setItem(30, createEnchantmentBook("Triple-Strike", 4, 4000));
        gui.setItem(31, createEnchantmentBook("Efficiency", 7, 2200));
        gui.setItem(32, createEnchantmentBook("Fortune", 3, 2500));
        
        gui.setItem(37, createEnchantmentBook("Smelting Touch", 1, 5000));
        gui.setItem(38, createEnchantmentBook("Experience", 4, 1800));
        gui.setItem(39, createEnchantmentBook("Looting", 4, 2000));
        gui.setItem(40, createEnchantmentBook("Luck", 5, 3000));
        gui.setItem(41, createEnchantmentBook("Scavenger", 4, 2200));
        
        gui.setItem(46, createEnchantmentBook("Vampirism", 5, 4500));
        gui.setItem(47, createEnchantmentBook("Life Steal", 3, 6000));
        gui.setItem(48, createEnchantmentBook("Syphon", 3, 6000));
        gui.setItem(49, createEnchantmentBook("Thunderlord", 5, 8000));
        gui.setItem(50, createEnchantmentBook("Thorns", 3, 7500));
        
        // Control buttons
        gui.setItem(45, createButton(Material.RED_WOOL, "§cRemove Enchantments", 
            "§7Remove all enchantments from an item", "remove"));
        
        gui.setItem(53, createButton(Material.GREEN_WOOL, "§aApply Enchantments", 
            "§7Apply selected enchantments to an item", "apply"));
        
        player.openInventory(gui);
    }
    
    public void openEnchantmentCategory(Player player, String category) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6⚔ " + getCategoryTitle(category));
        
        List<CustomEnchantmentSystem.CustomEnchantment> categoryEnchantments = 
            getEnchantmentsByCategory(category);
        
        int slot = 10;
        for (CustomEnchantmentSystem.CustomEnchantment enchantment : categoryEnchantments) {
            if (slot >= 44) break;
            
            gui.setItem(slot, createEnchantmentItem(enchantment));
            slot++;
            
            if (slot % 9 == 8) slot += 2; // Skip to next row
        }
        
        // Back button
        gui.setItem(49, createButton(Material.ARROW, "§e← Back", "§7Return to main menu", "back"));
        
        player.openInventory(gui);
    }
    
    private ItemStack createCategoryItem(Material material, String name, String lore, String category) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize(name));
        meta.lore(Arrays.asList(
            LegacyComponentSerializer.legacySection().deserialize(lore),
            LegacyComponentSerializer.legacySection().deserialize(""),
            LegacyComponentSerializer.legacySection().deserialize("§eClick to view!")
        ));
        
        NamespacedKey key = new NamespacedKey(SkyblockPlugin, "category");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, category);
        
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createEnchantmentBook(String enchantmentName, int level, double cost) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        
        CustomEnchantmentSystem.CustomEnchantment enchantment = 
            enchantmentSystem.getEnchantment(enchantmentName);
        
        if (enchantment != null) {
            meta.displayName(LegacyComponentSerializer.legacySection().deserialize("§d" + enchantmentName + " " + level));
            meta.lore(Arrays.asList(
                LegacyComponentSerializer.legacySection().deserialize(enchantment.getDescription()),
                LegacyComponentSerializer.legacySection().deserialize(""),
                LegacyComponentSerializer.legacySection().deserialize("§7Level: §a" + level + "§7/§c" + enchantment.getMaxLevel()),
                LegacyComponentSerializer.legacySection().deserialize("§7Rarity: " + enchantment.getRarity().getDisplayName()),
                LegacyComponentSerializer.legacySection().deserialize("§7Cost: §6" + String.format("%.0f", cost) + " coins"),
                LegacyComponentSerializer.legacySection().deserialize(""),
                LegacyComponentSerializer.legacySection().deserialize("§eClick to purchase!")
            ));
            
            NamespacedKey key = new NamespacedKey(SkyblockPlugin, "enchantment_book");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, 
                enchantmentName + ":" + level + ":" + cost);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    private ItemStack createEnchantmentItem(CustomEnchantmentSystem.CustomEnchantment enchantment) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize("§d" + enchantment.getName()));
        meta.lore(Arrays.asList(
            LegacyComponentSerializer.legacySection().deserialize(enchantment.getDescription()),
            LegacyComponentSerializer.legacySection().deserialize(""),
            LegacyComponentSerializer.legacySection().deserialize("§7Max Level: §c" + enchantment.getMaxLevel()),
            LegacyComponentSerializer.legacySection().deserialize("§7Rarity: " + enchantment.getRarity().getDisplayName()),
            LegacyComponentSerializer.legacySection().deserialize(""),
            LegacyComponentSerializer.legacySection().deserialize("§eClick to view levels!")
        ));
        
        NamespacedKey key = new NamespacedKey(SkyblockPlugin, "enchantment_item");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, enchantment.getName());
        
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
        
        NamespacedKey key = new NamespacedKey(SkyblockPlugin, "button_action");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, action);
        
        item.setItemMeta(meta);
        return item;
    }
    
    private String getCategoryTitle(String category) {
        return switch (category) {
            case "combat" -> "Combat Enchantments";
            case "armor" -> "Armor Enchantments";
            case "tools" -> "Tool Enchantments";
            case "special" -> "Special Enchantments";
            case "legendary" -> "Legendary Enchantments";
            default -> "Enchantments";
        };
    }
    
    private List<CustomEnchantmentSystem.CustomEnchantment> getEnchantmentsByCategory(String category) {
        return switch (category) {
            case "combat" -> new ArrayList<>(enchantmentSystem.getEnchantmentsByTarget(org.bukkit.enchantments.EnchantmentTarget.WEAPON));
            case "armor" -> new ArrayList<>(enchantmentSystem.getEnchantmentsByTarget(org.bukkit.enchantments.EnchantmentTarget.ARMOR));
            case "tools" -> new ArrayList<>(enchantmentSystem.getEnchantmentsByTarget(org.bukkit.enchantments.EnchantmentTarget.TOOL));
            case "special" -> new ArrayList<>(enchantmentSystem.getEnchantmentsByRarity(CustomEnchantmentSystem.EnchantmentRarity.UNCOMMON));
            case "legendary" -> new ArrayList<>(enchantmentSystem.getEnchantmentsByRarity(CustomEnchantmentSystem.EnchantmentRarity.LEGENDARY));
            default -> new ArrayList<>();
        };
    }
}
