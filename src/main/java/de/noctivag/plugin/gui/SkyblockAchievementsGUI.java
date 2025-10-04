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
 * Skyblock Achievements GUI - Skyblock-Erfolge und -Meilensteine
 */
public class SkyblockAchievementsGUI {
    
    private final Plugin plugin;
    
    public SkyblockAchievementsGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openSkyblockAchievementsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lSkyblock Achievements");
        
        // Achievement Categories
        setItem(gui, 10, Material.GRASS_BLOCK, "§a§lIsland Achievements",
            "§7Insel-Erfolge",
            "§7• Island Builder",
            "§7• Island Master",
            "§7• Island Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.IRON_GOLEM_SPAWN_EGG, "§e§lMinion Achievements",
            "§7Minion-Erfolge",
            "§7• Minion Master",
            "§7• Minion Expert",
            "§7• Minion Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EXPERIENCE_BOTTLE, "§b§lSkill Achievements",
            "§7Skill-Erfolge",
            "§7• Skill Master",
            "§7• Skill Expert",
            "§7• Skill Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.BOOK, "§d§lCollection Achievements",
            "§7Sammlungs-Erfolge",
            "§7• Collection Master",
            "§7• Collection Expert",
            "§7• Collection Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.BONE, "§6§lPet Achievements",
            "§7Haustier-Erfolge",
            "§7• Pet Master",
            "§7• Pet Expert",
            "§7• Pet Legend",
            "",
            "§eKlicke zum Öffnen");
            
        // Combat Achievements
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lCombat Achievements",
            "§7Kampf-Erfolge",
            "§7• Combat Master",
            "§7• Battle Expert",
            "§7• War Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD, "§a§lEconomy Achievements",
            "§7Wirtschafts-Erfolge",
            "§7• Economy Master",
            "§7• Trade Expert",
            "§7• Market Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ENCHANTING_TABLE, "§5§lEnchanting Achievements",
            "§7Verzauberungs-Erfolge",
            "§7• Enchanting Master",
            "§7• Enchant Expert",
            "§7• Enchant Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.BREWING_STAND, "§9§lAlchemy Achievements",
            "§7Brauen-Erfolge",
            "§7• Alchemy Master",
            "§7• Brew Expert",
            "§7• Alchemy Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.CRAFTING_TABLE, "§e§lCarpentry Achievements",
            "§7Handwerks-Erfolge",
            "§7• Carpentry Master",
            "§7• Craft Expert",
            "§7• Carpentry Legend",
            "",
            "§eKlicke zum Öffnen");
            
        // Special Achievements
        setItem(gui, 28, Material.END_CRYSTAL, "§d§lRunecrafting Achievements",
            "§7Runen-Erfolge",
            "§7• Runecrafting Master",
            "§7• Rune Expert",
            "§7• Runecrafting Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.GOLD_INGOT, "§6§lBanking Achievements",
            "§7Bank-Erfolge",
            "§7• Banking Master",
            "§7• Bank Expert",
            "§7• Banking Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.NETHER_STAR, "§5§lSpecial Achievements",
            "§7Besondere Erfolge",
            "§7• Event Achievements",
            "§7• Limited Achievements",
            "§7• Exclusive Achievements",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§e§lNavigation Achievements",
            "§7Navigations-Erfolge",
            "§7• Navigation Master",
            "§7• Travel Expert",
            "§7• Navigation Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lGuide Achievements",
            "§7Anleitungs-Erfolge",
            "§7• Guide Master",
            "§7• Guide Expert",
            "§7• Guide Legend",
            "",
            "§eKlicke zum Öffnen");
            
        // Achievement Information
        setItem(gui, 37, Material.BOOK, "§b§lAchievement Guide",
            "§7Erfolgs-Anleitung",
            "§7• Achievement Basics",
            "§7• Achievement Tips",
            "§7• Achievement Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lAchievement Statistics",
            "§7Erfolgs-Statistiken",
            "§7• Total Achievements: §a0/100",
            "§7• Completion Rate: §a0%",
            "§7• Recent Achievements: §a0",
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
