package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Guild GUI - Gilden-System
 */
public class GuildGUI {
    
    private final SkyblockPlugin plugin;
    
    public GuildGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openGuildGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lGuild System");
        
        // Guild Management
        setItem(gui, 10, Material.WHITE_BANNER, "§a§lMy Guild",
            "§7Deine Gilde",
            "§7• Guild Name: §aNone",
            "§7• Guild Level: §a0",
            "§7• Guild Members: §a0/50",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lCreate Guild",
            "§7Gilde erstellen",
            "§7• Neue Gilde gründen",
            "§7• Guild Name wählen",
            "§7• Guild Tag wählen",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.ARROW, "§a§lJoin Guild",
            "§7Gilde beitreten",
            "§7• Guild-Einladungen",
            "§7• Öffentliche Gilden",
            "§7• Guild beitreten",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.BARRIER, "§c§lLeave Guild",
            "§7Gilde verlassen",
            "§7• Guild verlassen",
            "§7• Guild auflösen",
            "§7• Guild-Mitglieder entfernen",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.ENCHANTED_BOOK, "§b§lGuild Settings",
            "§7Guild-Einstellungen",
            "§7• Guild-Regeln",
            "§7• Guild-Berechtigungen",
            "§7• Guild-Chat",
            "",
            "§eKlicke zum Öffnen");
            
        // Guild Features
        setItem(gui, 19, Material.DIAMOND, "§b§lGuild Bank",
            "§7Guild-Bank",
            "§7• Guild Coins: §a0",
            "§7• Guild Items",
            "§7• Bank Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EXPERIENCE_BOTTLE, "§e§lGuild XP",
            "§7Guild-Erfahrung",
            "§7• Guild Level: §a0",
            "§7• Guild XP: §a0",
            "§7• XP Boosts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.NETHER_STAR, "§d§lGuild Upgrades",
            "§7Guild-Upgrades",
            "§7• Guild Size",
            "§7• Guild Features",
            "§7• Guild Bonuses",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.CAKE, "§6§lGuild Events",
            "§7Guild-Events",
            "§7• Guild Meetings",
            "§7• Guild Activities",
            "§7• Guild Competitions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.BOOK, "§b§lGuild History",
            "§7Guild-Verlauf",
            "§7• Guild Activities",
            "§7• Member Changes",
            "§7• Guild Achievements",
            "",
            "§eKlicke zum Öffnen");
            
        // Guild Social
        setItem(gui, 28, Material.WRITABLE_BOOK, "§e§lGuild Chat",
            "§7Guild-Chat",
            "§7• Guild Messages",
            "§7• Chat Settings",
            "§7• Chat History",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.COMPASS, "§a§lGuild Members",
            "§7Guild-Mitglieder",
            "§7• Member List",
            "§7• Member Ranks",
            "§7• Member Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.EMERALD, "§a§lGuild Rewards",
            "§7Guild-Belohnungen",
            "§7• Guild Bonuses",
            "§7• Member Rewards",
            "§7• Achievement Rewards",
            "",
            "§eKlicke zum Öffnen");
            
        // Guild Information
        setItem(gui, 37, Material.BOOK, "§b§lGuild Guide",
            "§7Guild-Anleitung",
            "§7• Guild System",
            "§7• Guild Commands",
            "§7• Guild Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lGuild Statistics",
            "§7Guild-Statistiken",
            "§7• Guild Level: §a0",
            "§7• Total Members: §a0",
            "§7• Guild Activity: §a0%",
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
