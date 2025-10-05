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
 * Pet Achievements GUI - Haustier-Erfolge und -Meilensteine
 */
public class PetAchievementsGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public PetAchievementsGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openPetAchievementsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lPet Achievements"));
        
        // Achievement Categories
        setItem(gui, 10, Material.BONE, "§6§lPet Collection",
            "§7Haustier-Sammlung",
            "§7• Pet Collector",
            "§7• Pet Master",
            "§7• Pet Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EXPERIENCE_BOTTLE, "§e§lPet Leveling",
            "§7Haustier-Leveling",
            "§7• Level Master",
            "§7• XP Collector",
            "§7• Level Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.DIAMOND, "§b§lPet Upgrades",
            "§7Haustier-Upgrades",
            "§7• Upgrade Master",
            "§7• Enhancement Expert",
            "§7• Upgrade Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.APPLE, "§a§lPet Care",
            "§7Haustier-Pflege",
            "§7• Care Master",
            "§7• Health Expert",
            "§7• Care Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.EMERALD, "§a§lPet Training",
            "§7Haustier-Training",
            "§7• Training Master",
            "§7• Skill Expert",
            "§7• Training Legend",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Type Achievements
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lCombat Pet Achievements",
            "§7Kampf-Haustier-Erfolge",
            "§7• Combat Master",
            "§7• Battle Expert",
            "§7• War Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.WHEAT, "§a§lFarming Pet Achievements",
            "§7Farming-Haustier-Erfolge",
            "§7• Farming Master",
            "§7• Harvest Expert",
            "§7• Agriculture Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.DIAMOND_PICKAXE, "§b§lMining Pet Achievements",
            "§7Mining-Haustier-Erfolge",
            "§7• Mining Master",
            "§7• Ore Expert",
            "§7• Mining Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.FISHING_ROD, "§9§lFishing Pet Achievements",
            "§7Fishing-Haustier-Erfolge",
            "§7• Fishing Master",
            "§7• Catch Expert",
            "§7• Fishing Legend",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.OAK_LOG, "§6§lForaging Pet Achievements",
            "§7Foraging-Haustier-Erfolge",
            "§7• Foraging Master",
            "§7• Wood Expert",
            "§7• Foraging Legend",
            "",
            "§eKlicke zum Öffnen");
            
        // Special Achievements
        setItem(gui, 28, Material.NETHER_STAR, "§d§lSpecial Achievements",
            "§7Besondere Erfolge",
            "§7• Event Achievements",
            "§7• Limited Achievements",
            "§7• Exclusive Achievements",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.EMERALD_BLOCK, "§a§lPremium Achievements",
            "§7Premium-Erfolge",
            "§7• VIP Achievements",
            "§7• Premium Achievements",
            "§7• Elite Achievements",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.GOLD_BLOCK, "§6§lLuxury Achievements",
            "§7Luxus-Erfolge",
            "§7• Luxury Achievements",
            "§7• High-End Achievements",
            "§7• Exclusive Achievements",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.DIAMOND_BLOCK, "§b§lElite Achievements",
            "§7Elite-Erfolge",
            "§7• Elite Achievements",
            "§7• Master Achievements",
            "§7• Legendary Achievements",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.NETHERITE_BLOCK, "§5§lMythic Achievements",
            "§7Mythische Erfolge",
            "§7• Mythic Achievements",
            "§7• Ancient Achievements",
            "§7• Divine Achievements",
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
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(lore).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
