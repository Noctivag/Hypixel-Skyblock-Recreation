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
 * Features GUI - Plugin-Features und Funktionen
 */
public class FeaturesGUI {
    
    private final Plugin plugin;
    
    public FeaturesGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openFeaturesGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lPlugin Features");
        
        // Core Features
        setItem(gui, 10, Material.DIAMOND, "§b§lCore Features",
            "§7Grundlegende Features",
            "§7• Player Management",
            "§7• Economy System",
            "§7• Skill System",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lEconomy Features",
            "§7Wirtschafts-Features",
            "§7• Auction House",
            "§7• Bazaar",
            "§7• Banking System",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EXPERIENCE_BOTTLE, "§e§lSkill Features",
            "§7Skill-Features",
            "§7• 8 Skill Categories",
            "§7• XP System",
            "§7• Skill Boosts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.BOOK, "§d§lCollection Features",
            "§7Collection-Features",
            "§7• Item Collections",
            "§7• Collection Rewards",
            "§7• Collection Progress",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.IRON_GOLEM_SPAWN_EGG, "§f§lMinion Features",
            "§7Minion-Features",
            "§7• Minion Management",
            "§7• Minion Upgrades",
            "§7• Minion Collections",
            "",
            "§eKlicke zum Öffnen");
            
        // Advanced Features
        setItem(gui, 19, Material.BONE, "§6§lPet Features",
            "§7Pet-Features",
            "§7• Pet Management",
            "§7• Pet Leveling",
            "§7• Pet Abilities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.GRASS_BLOCK, "§a§lIsland Features",
            "§7Insel-Features",
            "§7• Private Islands",
            "§7• Island Upgrades",
            "§7• Island Teleportation",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ENCHANTING_TABLE, "§5§lEnchanting Features",
            "§7Verzauberungs-Features",
            "§7• Enchantment Table",
            "§7• Custom Enchants",
            "§7• Enchantment Books",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.BREWING_STAND, "§9§lAlchemy Features",
            "§7Brau-Features",
            "§7• Potion Brewing",
            "§7• Custom Potions",
            "§7• Alchemy XP",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.CRAFTING_TABLE, "§e§lCarpentry Features",
            "§7Handwerks-Features",
            "§7• Advanced Crafting",
            "§7• Custom Recipes",
            "§7• Carpentry XP",
            "",
            "§eKlicke zum Öffnen");
            
        // Special Features
        setItem(gui, 28, Material.END_CRYSTAL, "§d§lRunecrafting Features",
            "§7Runen-Features",
            "§7• Rune Creation",
            "§7• Rune Effects",
            "§7• Runecrafting XP",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.GOLD_INGOT, "§6§lBanking Features",
            "§7Bank-Features",
            "§7• Bank Account",
            "§7• Interest Rates",
            "§7• Bank Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.NETHER_STAR, "§5§lSpecial Features",
            "§7Besondere Features",
            "§7• Rare Events",
            "§7• Special Items",
            "§7• Hidden Features",
            "",
            "§eKlicke zum Öffnen");
            
        // Feature Information
        setItem(gui, 37, Material.BOOK, "§b§lFeature Guide",
            "§7Feature-Anleitung",
            "§7• Feature Overview",
            "§7• Feature Usage",
            "§7• Feature Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lFeature Statistics",
            "§7Feature-Statistiken",
            "§7• Active Features: §a12",
            "§7• Feature Usage: §a85%",
            "§7• Feature Performance: §aExcellent",
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
