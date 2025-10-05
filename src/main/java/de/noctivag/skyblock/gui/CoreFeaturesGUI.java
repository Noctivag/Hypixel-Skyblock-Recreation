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
 * Core Features GUI - Hauptfunktionen des Plugins
 */
public class CoreFeaturesGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public CoreFeaturesGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openCoreFeaturesGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lCore Features"));
        
        // Core Features
        setItem(gui, 10, Material.DIAMOND_SWORD, "§c§lCombat System",
            "§7Kampf und PvP Features",
            "§7• PvP Arenen",
            "§7• Combat Logging",
            "§7• Damage Tracking",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EMERALD, "§a§lEconomy System",
            "§7Wirtschaft und Handel",
            "§7• Coins & Bank",
            "§7• Auction House",
            "§7• Bazaar",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.EXPERIENCE_BOTTLE, "§b§lSkills System",
            "§7Fähigkeiten und Level",
            "§7• 8 Skill-Kategorien",
            "§7• XP & Leveling",
            "§7• Skill Boosts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 16, Material.BOOK, "§d§lCollections System",
            "§7Item-Sammlungen",
            "§7• Collection Progress",
            "§7• Collection Rewards",
            "§7• Collection Milestones",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 19, Material.IRON_GOLEM_SPAWN_EGG, "§e§lMinions System",
            "§7Automatisierte Produktion",
            "§7• Minion Management",
            "§7• Minion Upgrades",
            "§7• Minion Collections",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.BONE, "§6§lPets System",
            "§7Haustiere und Begleiter",
            "§7• Pet Management",
            "§7• Pet Leveling",
            "§7• Pet Abilities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.GRASS_BLOCK, "§a§lIslands System",
            "§7Private Inseln",
            "§7• Island Management",
            "§7• Island Upgrades",
            "§7• Island Teleportation",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 25, Material.ENCHANTING_TABLE, "§5§lEnchanting System",
            "§7Verzauberungen",
            "§7• Enchantment Table",
            "§7• Custom Enchants",
            "§7• Enchantment Books",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 28, Material.BREWING_STAND, "§9§lAlchemy System",
            "§7Brauen und Tränke",
            "§7• Potion Brewing",
            "§7• Custom Potions",
            "§7• Alchemy XP",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.CRAFTING_TABLE, "§6§lCarpentry System",
            "§7Handwerk und Bauen",
            "§7• Advanced Crafting",
            "§7• Custom Recipes",
            "§7• Carpentry XP",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.END_CRYSTAL, "§d§lRunecrafting System",
            "§7Runen und Magie",
            "§7• Rune Creation",
            "§7• Rune Effects",
            "§7• Runecrafting XP",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 34, Material.GOLD_INGOT, "§e§lBanking System",
            "§7Bank und Finanzen",
            "§7• Bank Account",
            "§7• Interest Rates",
            "§7• Bank Upgrades",
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
