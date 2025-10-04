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
 * Pet Guide GUI - Haustier-Anleitung und -Tipps
 */
public class PetGuideGUI {
    
    private final SkyblockPlugin plugin;
    
    public PetGuideGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openPetGuideGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lPet Guide");
        
        // Getting Started
        setItem(gui, 10, Material.BOOK, "§a§lGetting Started",
            "§7Erste Schritte",
            "§7• Pet Basics",
            "§7• First Pet",
            "§7• Beginner Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.BONE, "§6§lPet Types",
            "§7Haustier-Typen",
            "§7• Combat Pets",
            "§7• Farming Pets",
            "§7• Special Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EXPERIENCE_BOTTLE, "§e§lPet Leveling",
            "§7Haustier-Leveling",
            "§7• XP System",
            "§7• Level Benefits",
            "§7• Leveling Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.DIAMOND, "§b§lPet Upgrades",
            "§7Haustier-Upgrades",
            "§7• Upgrade Types",
            "§7• Upgrade Materials",
            "§7• Upgrade Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.APPLE, "§a§lPet Care",
            "§7Haustier-Pflege",
            "§7• Health Management",
            "§7• Happiness System",
            "§7• Care Tips",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Features
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lCombat Pets",
            "§7Kampf-Haustiere",
            "§7• Combat Abilities",
            "§7• Combat Tips",
            "§7• Combat Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.WHEAT, "§a§lFarming Pets",
            "§7Farming-Haustiere",
            "§7• Farming Abilities",
            "§7• Farming Tips",
            "§7• Farming Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.DIAMOND_PICKAXE, "§b§lMining Pets",
            "§7Mining-Haustiere",
            "§7• Mining Abilities",
            "§7• Mining Tips",
            "§7• Mining Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.FISHING_ROD, "§9§lFishing Pets",
            "§7Fishing-Haustiere",
            "§7• Fishing Abilities",
            "§7• Fishing Tips",
            "§7• Fishing Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.OAK_LOG, "§6§lForaging Pets",
            "§7Foraging-Haustiere",
            "§7• Foraging Abilities",
            "§7• Foraging Tips",
            "§7• Foraging Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Tips
        setItem(gui, 28, Material.EMERALD, "§a§lPet Tips",
            "§7Haustier-Tipps",
            "§7• General Tips",
            "§7• Advanced Tips",
            "§7• Expert Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.DIAMOND, "§b§lPet Strategies",
            "§7Haustier-Strategien",
            "§7• Leveling Strategies",
            "§7• Upgrade Strategies",
            "§7• Care Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.GOLD_INGOT, "§6§lPet Optimization",
            "§7Haustier-Optimierung",
            "§7• Performance Tips",
            "§7• Efficiency Tips",
            "§7• Optimization Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.NETHER_STAR, "§d§lPet Mastery",
            "§7Haustier-Meisterschaft",
            "§7• Master Tips",
            "§7• Expert Strategies",
            "§7• Advanced Techniques",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.ENCHANTED_BOOK, "§5§lPet Secrets",
            "§7Haustier-Geheimnisse",
            "§7• Hidden Tips",
            "§7• Secret Strategies",
            "§7• Advanced Secrets",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Information
        setItem(gui, 37, Material.BOOK, "§b§lPet Information",
            "§7Haustier-Informationen",
            "§7• Pet Stats",
            "§7• Pet Abilities",
            "§7• Pet Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lPet Statistics",
            "§7Haustier-Statistiken",
            "§7• Pet Count: §a0",
            "§7• Average Level: §a0",
            "§7• Total XP: §a0",
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
