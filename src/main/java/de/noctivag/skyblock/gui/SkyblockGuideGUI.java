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
 * Skyblock Guide GUI - Skyblock-Anleitung und -Tipps
 */
public class SkyblockGuideGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public SkyblockGuideGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openSkyblockGuideGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lSkyblock Guide"));
        
        // Getting Started
        setItem(gui, 10, Material.BOOK, "§a§lGetting Started",
            "§7Erste Schritte",
            "§7• Skyblock Basics",
            "§7• First Steps",
            "§7• Beginner Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.GRASS_BLOCK, "§a§lPrivate Island",
            "§7Private Insel",
            "§7• Island Management",
            "§7• Island Upgrades",
            "§7• Island Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.IRON_GOLEM_SPAWN_EGG, "§e§lMinions",
            "§7Minions",
            "§7• Minion Basics",
            "§7• Minion Management",
            "§7• Minion Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.EXPERIENCE_BOTTLE, "§b§lSkills",
            "§7Fähigkeiten",
            "§7• Skill System",
            "§7• Skill Leveling",
            "§7• Skill Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.BOOK, "§d§lCollections",
            "§7Sammlungen",
            "§7• Collection System",
            "§7• Collection Progress",
            "§7• Collection Tips",
            "",
            "§eKlicke zum Öffnen");
            
        // Advanced Guide
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lCombat Guide",
            "§7Kampf-Anleitung",
            "§7• Combat Basics",
            "§7• Combat Tips",
            "§7• Combat Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD, "§a§lEconomy Guide",
            "§7Wirtschafts-Anleitung",
            "§7• Economy Basics",
            "§7• Economy Tips",
            "§7• Economy Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ENCHANTING_TABLE, "§5§lEnchanting Guide",
            "§7Verzauberungs-Anleitung",
            "§7• Enchanting Basics",
            "§7• Enchanting Tips",
            "§7• Enchanting Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.BREWING_STAND, "§9§lAlchemy Guide",
            "§7Brauen-Anleitung",
            "§7• Alchemy Basics",
            "§7• Alchemy Tips",
            "§7• Alchemy Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.CRAFTING_TABLE, "§e§lCarpentry Guide",
            "§7Handwerks-Anleitung",
            "§7• Carpentry Basics",
            "§7• Carpentry Tips",
            "§7• Carpentry Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Special Features
        setItem(gui, 28, Material.END_CRYSTAL, "§d§lRunecrafting Guide",
            "§7Runen-Anleitung",
            "§7• Runecrafting Basics",
            "§7• Runecrafting Tips",
            "§7• Runecrafting Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.GOLD_INGOT, "§6§lBanking Guide",
            "§7Bank-Anleitung",
            "§7• Banking Basics",
            "§7• Banking Tips",
            "§7• Banking Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.NETHER_STAR, "§5§lSpecial Features",
            "§7Besondere Features",
            "§7• Special Events",
            "§7• Special Items",
            "§7• Hidden Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§e§lNavigation Guide",
            "§7Navigations-Anleitung",
            "§7• World Navigation",
            "§7• Area Navigation",
            "§7• Teleportation",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lAdvanced Guide",
            "§7Erweiterte Anleitung",
            "§7• Advanced Tips",
            "§7• Expert Strategies",
            "§7• Master Techniques",
            "",
            "§eKlicke zum Öffnen");
            
        // Guide Information
        setItem(gui, 37, Material.BOOK, "§b§lGuide Information",
            "§7Anleitungs-Informationen",
            "§7• Guide Rules",
            "§7• Guide Terms",
            "§7• Guide Policies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lGuide Statistics",
            "§7Anleitungs-Statistiken",
            "§7• Guide Progress: §a0%",
            "§7• Guide Level: §a0",
            "§7• Guide Rank: §a0",
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
            meta.lore(Arrays.asList(lore).stream()
                .map(line -> Component.text(line))
                .collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
