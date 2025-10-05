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
 * Help GUI - Hilfe und Support
 */
public class HelpGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public HelpGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openHelpGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lHelp & Support"));
        
        // Getting Started
        setItem(gui, 10, Material.BOOK, "§a§lGetting Started",
            "§7Erste Schritte",
            "§7• SkyblockPlugin Overview",
            "§7• Basic Commands",
            "§7• First Steps Guide",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.COMPASS, "§b§lNavigation Guide",
            "§7Navigations-Anleitung",
            "§7• GUI Navigation",
            "§7• Menu System",
            "§7• Command System",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.DIAMOND, "§b§lFeature Guide",
            "§7Feature-Anleitung",
            "§7• Core Features",
            "§7• Advanced Features",
            "§7• Special Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.EMERALD, "§a§lEconomy Guide",
            "§7Wirtschafts-Anleitung",
            "§7• Economy System",
            "§7• Trading Guide",
            "§7• Market Guide",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.EXPERIENCE_BOTTLE, "§e§lSkill Guide",
            "§7Skill-Anleitung",
            "§7• Skill System",
            "§7• XP Guide",
            "§7• Skill Boosts",
            "",
            "§eKlicke zum Öffnen");
            
        // Commands Help
        setItem(gui, 19, Material.COMMAND_BLOCK, "§d§lCommands Help",
            "§7Befehls-Hilfe",
            "§7• Basic Commands",
            "§7• Admin Commands",
            "§7• Command Syntax",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.WRITABLE_BOOK, "§e§lCommand Reference",
            "§7Befehls-Referenz",
            "§7• Complete Command List",
            "§7• Command Parameters",
            "§7• Command Examples",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ANVIL, "§6§lCommand Examples",
            "§7Befehls-Beispiele",
            "§7• Common Commands",
            "§7• Advanced Commands",
            "§7• Command Tips",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.REDSTONE, "§c§lCommand Troubleshooting",
            "§7Befehls-Fehlerbehebung",
            "§7• Common Issues",
            "§7• Error Messages",
            "§7• Solutions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.CLOCK, "§e§lCommand History",
            "§7Befehls-Verlauf",
            "§7• Recent Commands",
            "§7• Command Usage",
            "§7• Command Statistics",
            "",
            "§eKlicke zum Öffnen");
            
        // FAQ and Support
        setItem(gui, 28, Material.BOOK, "§e§lFAQ",
            "§7Häufig gestellte Fragen",
            "§7• Common Questions",
            "§7• Quick Answers",
            "§7• Problem Solutions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.EMERALD, "§a§lContact Support",
            "§7Support kontaktieren",
            "§7• Support Channels",
            "§7• Bug Reports",
            "§7• Feature Requests",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BOOK, "§b§lDocumentation",
            "§7Dokumentation",
            "§7• SkyblockPlugin Documentation",
            "§7• API Documentation",
            "§7• Developer Guide",
            "",
            "§eKlicke zum Öffnen");
            
        // Tutorials and Guides
        setItem(gui, 37, Material.CRAFTING_TABLE, "§6§lTutorials",
            "§7Tutorials",
            "§7• Step-by-Step Guides",
            "§7• Video Tutorials",
            "§7• Interactive Tutorials",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.NETHER_STAR, "§d§lAdvanced Help",
            "§7Erweiterte Hilfe",
            "§7• Advanced Features",
            "§7• Expert Tips",
            "§7• Advanced Configuration",
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
