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
 * Pet Leveling GUI - Haustier-Leveling und -XP
 */
public class PetLevelingGUI {
    
    private final Plugin plugin;
    
    public PetLevelingGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openPetLevelingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lPet Leveling");
        
        // Pet Leveling
        setItem(gui, 10, Material.EXPERIENCE_BOTTLE, "§e§lPet XP",
            "§7Haustier-XP",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "§7• XP Sources",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.DIAMOND, "§b§lPet Levels",
            "§7Haustier-Level",
            "§7• Current Level: §a0",
            "§7• Max Level: §a100",
            "§7• Level Benefits",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EMERALD, "§a§lXP Boosts",
            "§7XP-Boosts",
            "§7• Active Boosts: §a0",
            "§7• Boost Sources",
            "§7• Boost Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.GOLD_INGOT, "§6§lXP Items",
            "§7XP-Items",
            "§7• XP Bottles",
            "§7• XP Books",
            "§7• XP Crystals",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.NETHER_STAR, "§d§lSpecial XP",
            "§7Besondere XP",
            "§7• Event XP",
            "§7• Bonus XP",
            "§7• Special XP Sources",
            "",
            "§eKlicke zum Öffnen");
            
        // XP Sources
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lCombat XP",
            "§7Kampf-XP",
            "§7• Mob Kills",
            "§7• PvP Kills",
            "§7• Boss Kills",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.DIAMOND_PICKAXE, "§b§lMining XP",
            "§7Mining-XP",
            "§7• Ore Mining",
            "§7• Block Breaking",
            "§7• Special Mining",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.WHEAT, "§a§lFarming XP",
            "§7Farming-XP",
            "§7• Crop Harvesting",
            "§7• Animal Breeding",
            "§7• Special Farming",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.FISHING_ROD, "§9§lFishing XP",
            "§7Fishing-XP",
            "§7• Fish Catching",
            "§7• Treasure Finding",
            "§7• Special Fishing",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.OAK_LOG, "§6§lForaging XP",
            "§7Foraging-XP",
            "§7• Tree Chopping",
            "§7• Wood Gathering",
            "§7• Special Foraging",
            "",
            "§eKlicke zum Öffnen");
            
        // Level Benefits
        setItem(gui, 28, Material.EMERALD, "§a§lLevel Benefits",
            "§7Level-Vorteile",
            "§7• Stat Boosts",
            "§7• Ability Unlocks",
            "§7• Special Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.DIAMOND, "§b§lStat Boosts",
            "§7Stat-Boosts",
            "§7• Health Boosts",
            "§7• Damage Boosts",
            "§7• Speed Boosts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.ENCHANTED_BOOK, "§d§lAbility Unlocks",
            "§7Fähigkeits-Freischaltungen",
            "§7• New Abilities",
            "§7• Ability Upgrades",
            "§7• Special Abilities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.NETHER_STAR, "§5§lSpecial Features",
            "§7Besondere Features",
            "§7• Special Effects",
            "§7• Unique Abilities",
            "§7• Exclusive Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.CLOCK, "§e§lLevel Progress",
            "§7Level-Fortschritt",
            "§7• Progress Tracking",
            "§7• Level History",
            "§7• Progress Reports",
            "",
            "§eKlicke zum Öffnen");
            
        // Leveling Tools
        setItem(gui, 37, Material.BOOK, "§b§lLeveling Guide",
            "§7Leveling-Anleitung",
            "§7• Leveling Basics",
            "§7• Leveling Tips",
            "§7• Leveling Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lLeveling Statistics",
            "§7Leveling-Statistiken",
            "§7• Total XP: §a0",
            "§7• Average Level: §a0",
            "§7• Leveling Rate: §a0 XP/h",
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
