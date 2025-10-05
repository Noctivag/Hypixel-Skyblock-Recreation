package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

public class EnhancedMainMenu extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;

    public EnhancedMainMenu(SkyblockPlugin SkyblockPlugin) {
        super(54, Component.text("§6§lEnhanced Hauptmenü"));
        this.SkyblockPlugin = SkyblockPlugin;
        setupItems();
    }

    private void setupItems() {
        // Row 1 - Core Features
        setItem(10, Material.PLAYER_HEAD, "§a§lProfil", 
            "§7Zeige dein Profil und Statistiken",
            "§eKlicke zum Öffnen");
        
        setItem(11, Material.DIAMOND_SWORD, "§c§lEvents & Bosses", 
            "§7Nimm an Events teil und kämpfe gegen Bosses",
            "§eKlicke zum Öffnen");
        
        setItem(12, Material.NETHER_STAR, "§d§lCosmetics", 
            "§7Kaufe und aktiviere Cosmetics",
            "§eKlicke zum Öffnen");
        
        setItem(13, Material.GOLD_INGOT, "§6§lAchievements", 
            "§7Schaue deine Achievements an",
            "§eKlicke zum Öffnen");
        
        setItem(14, Material.CHEST, "§e§lDaily Rewards", 
            "§7Hole deine täglichen Belohnungen",
            "§eKlicke zum Öffnen");
        
        // Row 2 - Social Features
        setItem(15, Material.CAKE, "§b§lParty System", 
            "§7Erstelle oder trete einer Party bei",
            "§eKlicke zum Öffnen");
        
        setItem(16, Material.PLAYER_HEAD, "§b§lFreunde", 
            "§7Verwalte deine Freunde",
            "§eKlicke zum Öffnen");
        
        setItem(17, Material.COMPASS, "§e§lTeleportation", 
            "§7Warp, Home und Teleportation",
            "§eKlicke zum Öffnen");
        
        setItem(18, Material.ENDER_CHEST, "§5§lWarps", 
            "§7Besuche verschiedene Warps",
            "§eKlicke zum Öffnen");
        
        setItem(19, Material.BOOK, "§f§lKits", 
            "§7Kaufe und verwende Kits",
            "§eKlicke zum Öffnen");
        
        // Row 3 - Utility Features
        setItem(20, Material.ANVIL, "§7§lBasic Commands", 
            "§7Nickname, Prefix und Werkzeuge",
            "§eKlicke zum Öffnen");
        
        setItem(21, Material.WRITABLE_BOOK, "§a§lJoin/Leave Messages", 
            "§7Bearbeite deine Join/Leave Nachrichten",
            "§eKlicke zum Öffnen");
        
        setItem(22, Material.REDSTONE, "§c§lEinstellungen", 
            "§7Plugin-Einstellungen und Features",
            "§eKlicke zum Öffnen");
        
        setItem(23, Material.COMMAND_BLOCK, "§6§lAdmin Panel", 
            "§7Admin-Tools und Verwaltung",
            "§eKlicke zum Öffnen");
        
        setItem(24, Material.EMERALD, "§a§lEconomy", 
            "§7Geld und Wirtschaftssystem",
            "§eKlicke zum Öffnen");
        
        // Row 4 - Information
        setItem(25, Material.KNOWLEDGE_BOOK, "§b§lPlugin Features", 
            "§7Alle verfügbaren Features",
            "§eKlicke zum Öffnen");
        
        setItem(26, Material.CLOCK, "§e§lEvent Zeitplan", 
            "§7Zeigt kommende Events",
            "§eKlicke zum Öffnen");
        
        setItem(27, Material.BARRIER, "§c§lSchließen", 
            "§7Schließe das Menü",
            "§eKlicke zum Schließen");
        
        // Decorative items
        setItem(0, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(1, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(2, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(3, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(4, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(5, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(6, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(7, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(8, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        
        setItem(9, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(17, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(26, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(35, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(44, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(53, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        
        // Bottom row
        setItem(45, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(46, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(47, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(48, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(49, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(50, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(51, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
        setItem(52, Material.GRAY_STAINED_GLASS_PANE, "§7", "");
    }

    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
