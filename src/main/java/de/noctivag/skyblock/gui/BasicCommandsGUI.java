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
 * Basic Commands GUI - Grundlegende Befehle
 */
public class BasicCommandsGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public BasicCommandsGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openGUI(Player player) {
        openBasicCommandsGUI(player);
    }
    
    public void openBasicCommandsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lBasic Commands"));
        
        // Teleportation Commands
        setItem(gui, 10, Material.GRASS_BLOCK, "§a§lSpawn",
            "§7Teleportiere zum Spawn",
            "§7• /spawn",
            "§7• Teleportation zum Hauptspawn",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 11, Material.RED_BED, "§c§lHome",
            "§7Teleportiere nach Hause",
            "§7• /home",
            "§7• Teleportation zu deinem Zuhause",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 12, Material.COMPASS, "§e§lRandom Teleport",
            "§7Zufällige Teleportation",
            "§7• /rtp",
            "§7• Teleportation zu zufälliger Position",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 13, Material.ENDER_PEARL, "§d§lBack",
            "§7Zurück zur letzten Position",
            "§7• /back",
            "§7• Teleportation zur letzten Position",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 14, Material.ARROW, "§a§lTeleport Request",
            "§7Teleportations-Anfrage",
            "§7• /tpa <player>",
            "§7• Anfrage an Spieler senden",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Öffnen");
            
        // Player Commands
        setItem(gui, 19, Material.PLAYER_HEAD, "§b§lPlayer Info",
            "§7Spieler-Informationen",
            "§7• /playerinfo <player>",
            "§7• Informationen über Spieler",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.EMERALD, "§a§lBalance",
            "§7Kontostand anzeigen",
            "§7• /balance",
            "§7• Dein aktueller Kontostand",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 21, Material.EXPERIENCE_BOTTLE, "§e§lSkills",
            "§7Fähigkeiten anzeigen",
            "§7• /skills",
            "§7• Deine aktuellen Fähigkeiten",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 22, Material.BOOK, "§d§lCollections",
            "§7Sammlungen anzeigen",
            "§7• /collections",
            "§7• Deine aktuellen Sammlungen",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 23, Material.IRON_GOLEM_SPAWN_EGG, "§f§lMinions",
            "§7Minions anzeigen",
            "§7• /minions",
            "§7• Deine aktuellen Minions",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        // Utility Commands
        setItem(gui, 28, Material.CRAFTING_TABLE, "§6§lWorkbench",
            "§7Werkbank öffnen",
            "§7• /workbench",
            "§7• Öffnet eine Werkbank",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 29, Material.ENCHANTING_TABLE, "§5§lEnchanting Table",
            "§7Verzauberungstisch öffnen",
            "§7• /enchanting",
            "§7• Öffnet einen Verzauberungstisch",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 30, Material.ANVIL, "§7§lAnvil",
            "§7Amboss öffnen",
            "§7• /anvil",
            "§7• Öffnet einen Amboss",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 31, Material.ENDER_CHEST, "§d§lEnder Chest",
            "§7Ender-Truhe öffnen",
            "§7• /enderchest",
            "§7• Öffnet deine Ender-Truhe",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        setItem(gui, 32, Material.CHEST, "§6§lTrash",
            "§7Mülleimer öffnen",
            "§7• /trash",
            "§7• Öffnet einen Mülleimer",
            "§7• Kostenlos",
            "",
            "§eKlicke zum Ausführen");
            
        // Social Commands
        setItem(gui, 37, Material.CAKE, "§d§lParty",
            "§7Party-Befehle",
            "§7• /party create",
            "§7• /party invite <player>",
            "§7• /party leave",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.EMERALD, "§a§lFriends",
            "§7Freunde-Befehle",
            "§7• /friend add <player>",
            "§7• /friend remove <player>",
            "§7• /friend list",
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
