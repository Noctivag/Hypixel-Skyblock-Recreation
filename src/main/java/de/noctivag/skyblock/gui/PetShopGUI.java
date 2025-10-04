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
 * Pet Shop GUI - Haustier-Shop und -Kauf
 */
public class PetShopGUI {
    
    private final SkyblockPlugin plugin;
    
    public PetShopGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openPetShopGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lPet Shop");
        
        // Pet Categories
        setItem(gui, 10, Material.WOLF_SPAWN_EGG, "§6§lCombat Pets",
            "§7Kampf-Haustiere",
            "§7• Wolf Pets",
            "§7• Cat Pets",
            "§7• Special Combat Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.COW_SPAWN_EGG, "§a§lFarming Pets",
            "§7Farming-Haustiere",
            "§7• Cow Pets",
            "§7• Sheep Pets",
            "§7• Special Farming Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.BAT_SPAWN_EGG, "§9§lMining Pets",
            "§7Mining-Haustiere",
            "§7• Bat Pets",
            "§7• Spider Pets",
            "§7• Special Mining Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.PARROT_SPAWN_EGG, "§d§lFishing Pets",
            "§7Fishing-Haustiere",
            "§7• Parrot Pets",
            "§7• Dolphin Pets",
            "§7• Special Fishing Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.OCELOT_SPAWN_EGG, "§6§lForaging Pets",
            "§7Foraging-Haustiere",
            "§7• Ocelot Pets",
            "§7• Panda Pets",
            "§7• Special Foraging Pets",
            "",
            "§eKlicke zum Öffnen");
            
        // Special Pets
        setItem(gui, 19, Material.NETHER_STAR, "§d§lSpecial Pets",
            "§7Besondere Haustiere",
            "§7• Rare Pets",
            "§7• Event Pets",
            "§7• Limited Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD_BLOCK, "§a§lPremium Pets",
            "§7Premium-Haustiere",
            "§7• VIP Pets",
            "§7• Premium Pets",
            "§7• Exclusive Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.GOLD_BLOCK, "§6§lLuxury Pets",
            "§7Luxus-Haustiere",
            "§7• Expensive Pets",
            "§7• Luxury Pets",
            "§7• High-End Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.DIAMOND_BLOCK, "§b§lElite Pets",
            "§7Elite-Haustiere",
            "§7• Elite Pets",
            "§7• Master Pets",
            "§7• Legendary Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHERITE_BLOCK, "§5§lMythic Pets",
            "§7Mythische Haustiere",
            "§7• Mythic Pets",
            "§7• Ancient Pets",
            "§7• Divine Pets",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Services
        setItem(gui, 28, Material.ANVIL, "§7§lPet Services",
            "§7Haustier-Services",
            "§7• Pet Training",
            "§7• Pet Healing",
            "§7• Pet Enhancement",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.ENCHANTING_TABLE, "§d§lPet Enchanting",
            "§7Haustier-Verzauberung",
            "§7• Pet Enchantments",
            "§7• Pet Abilities",
            "§7• Pet Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.CRAFTING_TABLE, "§6§lPet Crafting",
            "§7Haustier-Handwerk",
            "§7• Pet Items",
            "§7• Pet Equipment",
            "§7• Pet Accessories",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.BREWING_STAND, "§5§lPet Potions",
            "§7Haustier-Tränke",
            "§7• Pet Potions",
            "§7• Pet Elixirs",
            "§7• Pet Brews",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.EMERALD, "§a§lPet Exchange",
            "§7Haustier-Austausch",
            "§7• Pet Trading",
            "§7• Pet Exchange",
            "§7• Pet Conversion",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Information
        setItem(gui, 37, Material.BOOK, "§b§lPet Information",
            "§7Haustier-Informationen",
            "§7• Pet Types",
            "§7• Pet Abilities",
            "§7• Pet Stats",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lPet Shop Statistics",
            "§7Haustier-Shop-Statistiken",
            "§7• Total Pets: §a0",
            "§7• Pet Sales: §a0",
            "§7• Shop Revenue: §a$0",
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
