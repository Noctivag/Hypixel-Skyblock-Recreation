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
 * Skills GUI - Fähigkeiten und -Verwaltung
 */
public class SkillsGUI {
    
    private final Plugin plugin;
    
    public SkillsGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openSkillsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lSkills");
        
        // Skill Categories
        setItem(gui, 10, Material.DIAMOND_SWORD, "§c§lCombat",
            "§7Kampf",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.DIAMOND_PICKAXE, "§b§lMining",
            "§7Mining",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.WHEAT, "§a§lFarming",
            "§7Farming",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.FISHING_ROD, "§9§lFishing",
            "§7Fishing",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.OAK_LOG, "§6§lForaging",
            "§7Foraging",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        // Advanced Skills
        setItem(gui, 19, Material.ENCHANTING_TABLE, "§5§lEnchanting",
            "§7Verzauberung",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.BREWING_STAND, "§9§lAlchemy",
            "§7Brauen",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.CRAFTING_TABLE, "§e§lCarpentry",
            "§7Handwerk",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.END_CRYSTAL, "§d§lRunecrafting",
            "§7Runen",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.GOLD_INGOT, "§6§lBanking",
            "§7Bank",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        // Skill Features
        setItem(gui, 28, Material.EXPERIENCE_BOTTLE, "§e§lXP Boosts",
            "§7XP-Boosts",
            "§7• Active Boosts: §a0",
            "§7• Boost Sources",
            "§7• Boost Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.DIAMOND, "§b§lSkill Upgrades",
            "§7Skill-Upgrades",
            "§7• Upgrade Skills",
            "§7• Skill Enhancements",
            "§7• Special Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.EMERALD, "§a§lSkill Rewards",
            "§7Skill-Belohnungen",
            "§7• Level Rewards",
            "§7• Milestone Rewards",
            "§7• Special Rewards",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.CLOCK, "§e§lSkill History",
            "§7Skill-Verlauf",
            "§7• Skill Progress",
            "§7• Skill Statistics",
            "§7• Skill Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lSkill Guide",
            "§7Skill-Anleitung",
            "§7• Skill Basics",
            "§7• Skill Tips",
            "§7• Skill Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Skill Information
        setItem(gui, 37, Material.BOOK, "§b§lSkill Information",
            "§7Skill-Informationen",
            "§7• Skill Stats",
            "§7• Skill Abilities",
            "§7• Skill Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lSkill Statistics",
            "§7Skill-Statistiken",
            "§7• Total Level: §a0",
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
