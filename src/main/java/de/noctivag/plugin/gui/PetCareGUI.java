package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Pet Care GUI - Haustier-Pflege und -Gesundheit
 */
public class PetCareGUI {
    
    private final Plugin plugin;
    
    public PetCareGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openPetCareGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lPet Care");
        
        // Pet Health
        setItem(gui, 10, Material.APPLE, "§a§lPet Health",
            "§7Haustier-Gesundheit",
            "§7• Current Health: §a100%",
            "§7• Health Status: §aHealthy",
            "§7• Health Recovery",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.GOLDEN_APPLE, "§6§lPet Happiness",
            "§7Haustier-Glück",
            "§7• Current Happiness: §a100%",
            "§7• Happiness Status: §aHappy",
            "§7• Happiness Boosts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.BREAD, "§e§lPet Hunger",
            "§7Haustier-Hunger",
            "§7• Current Hunger: §a100%",
            "§7• Hunger Status: §aFull",
            "§7• Feeding Schedule",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.WATER_BUCKET, "§b§lPet Thirst",
            "§7Haustier-Durst",
            "§7• Current Thirst: §a100%",
            "§7• Thirst Status: §aHydrated",
            "§7• Hydration Schedule",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.RED_BED, "§c§lPet Energy",
            "§7Haustier-Energie",
            "§7• Current Energy: §a100%",
            "§7• Energy Status: §aEnergetic",
            "§7• Rest Schedule",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Care Actions
        setItem(gui, 19, Material.APPLE, "§a§lFeed Pet",
            "§7Haustier füttern",
            "§7• Give Food",
            "§7• Feeding Options",
            "§7• Feeding Schedule",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.WATER_BUCKET, "§b§lHydrate Pet",
            "§7Haustier hydratisieren",
            "§7• Give Water",
            "§7• Hydration Options",
            "§7• Hydration Schedule",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.RED_BED, "§c§lRest Pet",
            "§7Haustier ausruhen",
            "§7• Put to Rest",
            "§7• Rest Options",
            "§7• Rest Schedule",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.GOLDEN_APPLE, "§6§lPlay with Pet",
            "§7Mit Haustier spielen",
            "§7• Play Activities",
            "§7• Play Options",
            "§7• Play Schedule",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.EMERALD, "§a§lPet Grooming",
            "§7Haustier-Pflege",
            "§7• Grooming Options",
            "§7• Grooming Tools",
            "§7• Grooming Schedule",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Care Items
        setItem(gui, 28, Material.BREAD, "§e§lPet Food",
            "§7Haustier-Futter",
            "§7• Basic Food",
            "§7• Premium Food",
            "§7• Special Food",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.WATER_BUCKET, "§b§lPet Drinks",
            "§7Haustier-Getränke",
            "§7• Basic Drinks",
            "§7• Premium Drinks",
            "§7• Special Drinks",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.POTION, "§d§lPet Potions",
            "§7Haustier-Tränke",
            "§7• Health Potions",
            "§7• Happiness Potions",
            "§7• Special Potions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.ENCHANTED_BOOK, "§5§lPet Treats",
            "§7Haustier-Leckerlis",
            "§7• Basic Treats",
            "§7• Premium Treats",
            "§7• Special Treats",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.NETHER_STAR, "§d§lPet Toys",
            "§7Haustier-Spielzeug",
            "§7• Basic Toys",
            "§7• Premium Toys",
            "§7• Special Toys",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Care Information
        setItem(gui, 37, Material.BOOK, "§b§lPet Care Guide",
            "§7Haustier-Pflege-Anleitung",
            "§7• Care Basics",
            "§7• Care Tips",
            "§7• Care Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lPet Care Statistics",
            "§7Haustier-Pflege-Statistiken",
            "§7• Care Score: §a100%",
            "§7• Care Streak: §a0 days",
            "§7• Total Care: §a0 times",
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
