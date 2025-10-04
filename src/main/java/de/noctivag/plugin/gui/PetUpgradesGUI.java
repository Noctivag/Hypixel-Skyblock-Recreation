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
 * Pet Upgrades GUI - Haustier-Upgrades und -Verbesserungen
 */
public class PetUpgradesGUI {
    
    private final Plugin plugin;
    
    public PetUpgradesGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openPetUpgradesGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lPet Upgrades");
        
        // Upgrade Types
        setItem(gui, 10, Material.DIAMOND, "§b§lStat Upgrades",
            "§7Stat-Upgrades",
            "§7• Health Upgrades",
            "§7• Damage Upgrades",
            "§7• Speed Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.ENCHANTED_BOOK, "§d§lAbility Upgrades",
            "§7Fähigkeits-Upgrades",
            "§7• Ability Enhancements",
            "§7• New Abilities",
            "§7• Special Abilities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EMERALD, "§a§lXP Upgrades",
            "§7XP-Upgrades",
            "§7• XP Boosts",
            "§7• XP Multipliers",
            "§7• XP Bonuses",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.GOLD_INGOT, "§6§lRarity Upgrades",
            "§7Seltenheits-Upgrades",
            "§7• Common to Uncommon",
            "§7• Uncommon to Rare",
            "§7• Rare to Epic",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.NETHER_STAR, "§d§lSpecial Upgrades",
            "§7Besondere Upgrades",
            "§7• Event Upgrades",
            "§7• Limited Upgrades",
            "§7• Exclusive Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        // Upgrade Categories
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lCombat Upgrades",
            "§7Kampf-Upgrades",
            "§7• Damage Upgrades",
            "§7• Attack Speed Upgrades",
            "§7• Critical Hit Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.DIAMOND_PICKAXE, "§b§lMining Upgrades",
            "§7Mining-Upgrades",
            "§7• Mining Speed Upgrades",
            "§7• Mining Fortune Upgrades",
            "§7• Mining Efficiency Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.WHEAT, "§a§lFarming Upgrades",
            "§7Farming-Upgrades",
            "§7• Farming Speed Upgrades",
            "§7• Farming Fortune Upgrades",
            "§7• Farming Efficiency Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.FISHING_ROD, "§9§lFishing Upgrades",
            "§7Fishing-Upgrades",
            "§7• Fishing Speed Upgrades",
            "§7• Fishing Luck Upgrades",
            "§7• Fishing Efficiency Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.OAK_LOG, "§6§lForaging Upgrades",
            "§7Foraging-Upgrades",
            "§7• Foraging Speed Upgrades",
            "§7• Foraging Fortune Upgrades",
            "§7• Foraging Efficiency Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        // Upgrade Materials
        setItem(gui, 28, Material.EMERALD, "§a§lUpgrade Materials",
            "§7Upgrade-Materialien",
            "§7• Common Materials",
            "§7• Rare Materials",
            "§7• Epic Materials",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.DIAMOND, "§b§lUpgrade Crystals",
            "§7Upgrade-Kristalle",
            "§7• XP Crystals",
            "§7• Stat Crystals",
            "§7• Ability Crystals",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.GOLD_INGOT, "§6§lUpgrade Essences",
            "§7Upgrade-Essenzen",
            "§7• Health Essences",
            "§7• Damage Essences",
            "§7• Speed Essences",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.NETHER_STAR, "§d§lUpgrade Fragments",
            "§7Upgrade-Fragmente",
            "§7• Ability Fragments",
            "§7• Stat Fragments",
            "§7• Special Fragments",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.ENCHANTED_BOOK, "§5§lUpgrade Books",
            "§7Upgrade-Bücher",
            "§7• Stat Books",
            "§7• Ability Books",
            "§7• Special Books",
            "",
            "§eKlicke zum Öffnen");
            
        // Upgrade Information
        setItem(gui, 37, Material.BOOK, "§b§lUpgrade Guide",
            "§7Upgrade-Anleitung",
            "§7• Upgrade Basics",
            "§7• Upgrade Tips",
            "§7• Upgrade Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lUpgrade Statistics",
            "§7Upgrade-Statistiken",
            "§7• Total Upgrades: §a0",
            "§7• Upgrade Success Rate: §a0%",
            "§7• Upgrade Cost: §a$0",
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
