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
 * Pet Social GUI - Haustier-Soziales und -Gemeinschaft
 */
public class PetSocialGUI {
    
    private final Plugin plugin;
    
    public PetSocialGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openPetSocialGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lPet Social");
        
        // Social Features
        setItem(gui, 10, Material.PLAYER_HEAD, "§a§lPet Friends",
            "§7Haustier-Freunde",
            "§7• Pet Friends: §a0",
            "§7• Friend Requests: §a0",
            "§7• Friend Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.CAKE, "§d§lPet Parties",
            "§7Haustier-Partys",
            "§7• Pet Parties: §a0",
            "§7• Party Invitations: §a0",
            "§7• Party Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EMERALD, "§a§lPet Trading",
            "§7Haustier-Handel",
            "§7• Pet Trading",
            "§7• Pet Exchange",
            "§7• Pet Gifts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.BOOK, "§b§lPet Chat",
            "§7Haustier-Chat",
            "§7• Pet Messages",
            "§7• Pet Communication",
            "§7• Pet Social",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.COMPASS, "§e§lPet Events",
            "§7Haustier-Events",
            "§7• Pet Events",
            "§7• Pet Competitions",
            "§7• Pet Activities",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Community
        setItem(gui, 19, Material.DIAMOND, "§b§lPet Community",
            "§7Haustier-Gemeinschaft",
            "§7• Community Hub",
            "§7• Community Events",
            "§7• Community Activities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD_BLOCK, "§a§lPet Guilds",
            "§7Haustier-Gilden",
            "§7• Pet Guilds",
            "§7• Guild Management",
            "§7• Guild Activities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.GOLD_BLOCK, "§6§lPet Teams",
            "§7Haustier-Teams",
            "§7• Pet Teams",
            "§7• Team Management",
            "§7• Team Activities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.DIAMOND_BLOCK, "§b§lPet Alliances",
            "§7Haustier-Allianzen",
            "§7• Pet Alliances",
            "§7• Alliance Management",
            "§7• Alliance Activities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHERITE_BLOCK, "§5§lPet Clans",
            "§7Haustier-Clans",
            "§7• Pet Clans",
            "§7• Clan Management",
            "§7• Clan Activities",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Social Tools
        setItem(gui, 28, Material.CLOCK, "§e§lPet Social History",
            "§7Haustier-Sozial-Verlauf",
            "§7• Social Activities",
            "§7• Social History",
            "§7• Social Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.BELL, "§a§lPet Social Notifications",
            "§7Haustier-Sozial-Benachrichtigungen",
            "§7• Social Alerts",
            "§7• Social Notifications",
            "§7• Social Reminders",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.PAPER, "§b§lPet Social Statistics",
            "§7Haustier-Sozial-Statistiken",
            "§7• Social Activity",
            "§7• Social Engagement",
            "§7• Social Performance",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§e§lPet Social Discovery",
            "§7Haustier-Sozial-Entdeckung",
            "§7• Find Pet Friends",
            "§7• Discover Pet Events",
            "§7• Explore Pet Community",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lPet Social Guide",
            "§7Haustier-Sozial-Anleitung",
            "§7• Social Basics",
            "§7• Social Tips",
            "§7• Social Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Social Information
        setItem(gui, 37, Material.BOOK, "§b§lPet Social Information",
            "§7Haustier-Sozial-Informationen",
            "§7• Social Rules",
            "§7• Social Terms",
            "§7• Social Policies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lPet Social Statistics",
            "§7Haustier-Sozial-Statistiken",
            "§7• Social Score: §a0",
            "§7• Social Level: §a0",
            "§7• Social Rank: §a0",
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
