package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * NPC Shops GUI - NPC-Shops und Händler
 */
public class NPCShopsGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public NPCShopsGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openNPCShopsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lNPC Shops"));
        
        // Shop Categories
        setItem(gui, 10, Material.DIAMOND_SWORD, "§c§lWeapon Shop",
            "§7Waffen-Shop",
            "§7• Swords",
            "§7• Bows",
            "§7• Special Weapons",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.DIAMOND_CHESTPLATE, "§b§lArmor Shop",
            "§7Rüstungs-Shop",
            "§7• Helmets",
            "§7• Chestplates",
            "§7• Complete Sets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.ENCHANTED_BOOK, "§d§lEnchanting Shop",
            "§7Verzauberungs-Shop",
            "§7• Enchantment Books",
            "§7• Rare Enchants",
            "§7• Custom Enchants",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.BREWING_STAND, "§5§lPotion Shop",
            "§7Trank-Shop",
            "§7• Health Potions",
            "§7• Special Potions",
            "§7• Custom Potions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.EMERALD, "§a§lMaterial Shop",
            "§7Material-Shop",
            "§7• Ores",
            "§7• Gems",
            "§7• Special Materials",
            "",
            "§eKlicke zum Öffnen");
            
        // Special Shops
        setItem(gui, 19, Material.NETHER_STAR, "§d§lSpecial Shop",
            "§7Besonderer Shop",
            "§7• Rare Items",
            "§7• Event Items",
            "§7• Limited Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD_BLOCK, "§a§lPremium Shop",
            "§7Premium-Shop",
            "§7• VIP Items",
            "§7• Premium Items",
            "§7• Exclusive Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.GOLD_BLOCK, "§6§lLuxury Shop",
            "§7Luxus-Shop",
            "§7• Expensive Items",
            "§7• Luxury Items",
            "§7• High-End Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.DIAMOND_BLOCK, "§b§lElite Shop",
            "§7Elite-Shop",
            "§7• Elite Items",
            "§7• Master Items",
            "§7• Legendary Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHERITE_BLOCK, "§5§lMythic Shop",
            "§7Mythischer Shop",
            "§7• Mythic Items",
            "§7• Ancient Items",
            "§7• Divine Items",
            "",
            "§eKlicke zum Öffnen");
            
        // Shop Services
        setItem(gui, 28, Material.ANVIL, "§7§lRepair Shop",
            "§7Reparatur-Shop",
            "§7• Item Repair",
            "§7• Enchantment Repair",
            "§7• Special Repair",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.ENCHANTING_TABLE, "§d§lEnchanting Service",
            "§7Verzauberungs-Service",
            "§7• Item Enchanting",
            "§7• Enchantment Removal",
            "§7• Custom Enchanting",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.CRAFTING_TABLE, "§6§lCrafting Service",
            "§7Handwerks-Service",
            "§7• Item Crafting",
            "§7• Custom Crafting",
            "§7• Special Crafting",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.BREWING_STAND, "§5§lBrewing Service",
            "§7Brau-Service",
            "§7• Potion Brewing",
            "§7• Custom Brewing",
            "§7• Special Brewing",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.EMERALD, "§a§lExchange Service",
            "§7Tausch-Service",
            "§7• Item Exchange",
            "§7• Currency Exchange",
            "§7• Special Exchange",
            "",
            "§eKlicke zum Öffnen");
            
        // Shop Information
        setItem(gui, 37, Material.BOOK, "§b§lShop Guide",
            "§7Shop-Anleitung",
            "§7• How to Buy",
            "§7• Shop Tips",
            "§7• Shop Etiquette",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lShop Hours",
            "§7Shop-Öffnungszeiten",
            "§7• Opening Hours",
            "§7• Special Hours",
            "§7• Holiday Hours",
            "",
            "§eKlicke zum Öffnen");
            
        // Navigation
        setItem(gui, 45, Material.ARROW, "§7« Back",
            "§7Zurück zum Hauptmenü");
            
        setItem(gui, 49, Material.BARRIER, "§c§lClose",
            "§7GUI schließen");
        
        player.openInventory(gui);
    }
    
    private void setItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(lore).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
