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
 * Skyblock Storage GUI - Skyblock-Lager und -Speicher
 */
public class SkyblockStorageGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public SkyblockStorageGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openSkyblockStorageGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lSkyblock Storage"));
        
        // Storage Types
        setItem(gui, 10, Material.CHEST, "§6§lSkyblock Chests",
            "§7Skyblock-Truhen",
            "§7• Basic Chests",
            "§7• Premium Chests",
            "§7• Special Chests",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.ENDER_CHEST, "§d§lSkyblock Ender Storage",
            "§7Skyblock-Ender-Lager",
            "§7• Ender Chests",
            "§7• Ender Barrels",
            "§7• Ender Containers",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.SHULKER_BOX, "§b§lSkyblock Shulker Storage",
            "§7Skyblock-Shulker-Lager",
            "§7• Shulker Boxes",
            "§7• Shulker Barrels",
            "§7• Shulker Containers",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.BARREL, "§6§lSkyblock Barrel Storage",
            "§7Skyblock-Fass-Lager",
            "§7• Barrels",
            "§7• Casks",
            "§7• Vessels",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.HOPPER, "§7§lSkyblock Hopper Storage",
            "§7Skyblock-Trichter-Lager",
            "§7• Hoppers",
            "§7• Collectors",
            "§7• Gatherers",
            "",
            "§eKlicke zum Öffnen");
            
        // Storage Features
        setItem(gui, 19, Material.EMERALD, "§a§lAuto Sort",
            "§7Automatisches Sortieren",
            "§7• Sort All Items",
            "§7• Sort by Category",
            "§7• Sort by Value",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 20, Material.DIAMOND, "§b§lItem Search",
            "§7Item-Suche",
            "§7• Search Items",
            "§7• Filter Items",
            "§7• Find Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.GOLD_INGOT, "§6§lItem Value",
            "§7Item-Wert",
            "§7• Calculate Value",
            "§7• Value Analysis",
            "§7• Value Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.NETHER_STAR, "§d§lItem Transfer",
            "§7Item-Transfer",
            "§7• Transfer Items",
            "§7• Move Items",
            "§7• Copy Items",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.CLOCK, "§e§lItem History",
            "§7Item-Verlauf",
            "§7• Item History",
            "§7• Item Statistics",
            "§7• Item Reports",
            "",
            "§eKlicke zum Öffnen");
            
        // Storage Categories
        setItem(gui, 28, Material.DIAMOND_SWORD, "§c§lWeapons",
            "§7Waffen",
            "§7• Swords",
            "§7• Bows",
            "§7• Special Weapons",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.DIAMOND_CHESTPLATE, "§b§lArmor",
            "§7Rüstung",
            "§7• Helmets",
            "§7• Chestplates",
            "§7• Complete Sets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.ENCHANTED_BOOK, "§d§lEnchanted Books",
            "§7Verzauberte Bücher",
            "§7• Enchantment Books",
            "§7• Rare Enchants",
            "§7• Custom Enchants",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.EMERALD, "§a§lMaterials",
            "§7Materialien",
            "§7• Ores",
            "§7• Gems",
            "§7• Special Materials",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.NETHER_STAR, "§5§lSpecial Items",
            "§7Besondere Items",
            "§7• Rare Items",
            "§7• Event Items",
            "§7• Limited Items",
            "",
            "§eKlicke zum Öffnen");
            
        // Storage Management
        setItem(gui, 37, Material.BOOK, "§b§lStorage Guide",
            "§7Lager-Anleitung",
            "§7• Storage Basics",
            "§7• Storage Tips",
            "§7• Storage Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lStorage Statistics",
            "§7Lager-Statistiken",
            "§7• Total Items: §a0",
            "§7• Storage Value: §a$0",
            "§7• Storage Usage: §a0%",
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
            meta.lore(Arrays.asList(lore).stream()
                .map(line -> Component.text(line))
                .collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
