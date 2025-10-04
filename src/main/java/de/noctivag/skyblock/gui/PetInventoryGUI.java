package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Pet Inventory GUI - Haustier-Inventar und -Ausrüstung
 */
public class PetInventoryGUI {
    
    private final SkyblockPlugin plugin;
    
    public PetInventoryGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openPetInventoryGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lPet Inventory");
        
        // Pet Equipment
        setItem(gui, 10, Material.DIAMOND_SWORD, "§c§lPet Weapons",
            "§7Haustier-Waffen",
            "§7• Pet Swords",
            "§7• Pet Bows",
            "§7• Special Weapons",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.DIAMOND_CHESTPLATE, "§b§lPet Armor",
            "§7Haustier-Rüstung",
            "§7• Pet Helmets",
            "§7• Pet Chestplates",
            "§7• Complete Sets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.ENCHANTED_BOOK, "§d§lPet Accessories",
            "§7Haustier-Accessoires",
            "§7• Pet Rings",
            "§7• Pet Necklaces",
            "§7• Pet Bracelets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.NETHER_STAR, "§5§lPet Artifacts",
            "§7Haustier-Artefakte",
            "§7• Pet Artifacts",
            "§7• Pet Relics",
            "§7• Pet Treasures",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.EMERALD, "§a§lPet Gems",
            "§7Haustier-Edelsteine",
            "§7• Pet Gems",
            "§7• Pet Crystals",
            "§7• Pet Stones",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Items
        setItem(gui, 19, Material.APPLE, "§a§lPet Food",
            "§7Haustier-Futter",
            "§7• Basic Food",
            "§7• Premium Food",
            "§7• Special Food",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.POTION, "§d§lPet Potions",
            "§7Haustier-Tränke",
            "§7• Health Potions",
            "§7• Happiness Potions",
            "§7• Special Potions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ENCHANTED_BOOK, "§5§lPet Treats",
            "§7Haustier-Leckerlis",
            "§7• Basic Treats",
            "§7• Premium Treats",
            "§7• Special Treats",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.NETHER_STAR, "§d§lPet Toys",
            "§7Haustier-Spielzeug",
            "§7• Basic Toys",
            "§7• Premium Toys",
            "§7• Special Toys",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.EMERALD, "§a§lPet Tools",
            "§7Haustier-Werkzeuge",
            "§7• Pet Tools",
            "§7• Pet Equipment",
            "§7• Pet Gear",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Storage
        setItem(gui, 28, Material.CHEST, "§6§lPet Storage",
            "§7Haustier-Lager",
            "§7• Pet Chests",
            "§7• Pet Barrels",
            "§7• Pet Containers",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.ENDER_CHEST, "§d§lPet Ender Storage",
            "§7Haustier-Ender-Lager",
            "§7• Pet Ender Chests",
            "§7• Pet Ender Barrels",
            "§7• Pet Ender Containers",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.SHULKER_BOX, "§b§lPet Shulker Storage",
            "§7Haustier-Shulker-Lager",
            "§7• Pet Shulker Boxes",
            "§7• Pet Shulker Barrels",
            "§7• Pet Shulker Containers",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.BARREL, "§6§lPet Barrel Storage",
            "§7Haustier-Fass-Lager",
            "§7• Pet Barrels",
            "§7• Pet Casks",
            "§7• Pet Vessels",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.HOPPER, "§7§lPet Hopper Storage",
            "§7Haustier-Trichter-Lager",
            "§7• Pet Hoppers",
            "§7• Pet Collectors",
            "§7• Pet Gatherers",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Inventory Management
        setItem(gui, 37, Material.BOOK, "§b§lPet Inventory Guide",
            "§7Haustier-Inventar-Anleitung",
            "§7• Inventory Basics",
            "§7• Inventory Tips",
            "§7• Inventory Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lPet Inventory Statistics",
            "§7Haustier-Inventar-Statistiken",
            "§7• Total Items: §a0",
            "§7• Inventory Value: §a$0",
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
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
