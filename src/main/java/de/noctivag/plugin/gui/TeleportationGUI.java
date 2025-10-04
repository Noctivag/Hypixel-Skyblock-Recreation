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
 * Teleportation GUI - Teleportation und Navigation
 */
public class TeleportationGUI {
    
    private final Plugin plugin;
    
    public TeleportationGUI(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void openTeleportationGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lTeleportation");
        
        // Main Locations
        setItem(gui, 10, Material.GRASS_BLOCK, "§a§lSpawn",
            "§7Hauptspawn",
            "§7• Server Spawn",
            "§7• Safe Zone",
            "§7• Welcome Area",
            "",
            "§eKlicke zum Teleportieren");
            
        setItem(gui, 11, Material.DIAMOND_ORE, "§b§lMining World",
            "§7Mining-Welt",
            "§7• Ores & Resources",
            "§7• Mining Areas",
            "§7• Mining Events",
            "",
            "§eKlicke zum Teleportieren");
            
        setItem(gui, 12, Material.WHEAT, "§a§lFarming World",
            "§7Farming-Welt",
            "§7• Crops & Farming",
            "§7• Farming Areas",
            "§7• Farming Events",
            "",
            "§eKlicke zum Teleportieren");
            
        setItem(gui, 13, Material.FISHING_ROD, "§9§lFishing World",
            "§7Fishing-Welt",
            "§7• Fishing Spots",
            "§7• Fishing Areas",
            "§7• Fishing Events",
            "",
            "§eKlicke zum Teleportieren");
            
        setItem(gui, 14, Material.OAK_LOG, "§6§lForaging World",
            "§7Foraging-Welt",
            "§7• Trees & Wood",
            "§7• Foraging Areas",
            "§7• Foraging Events",
            "",
            "§eKlicke zum Teleportieren");
            
        // Special Locations
        setItem(gui, 19, Material.ENDER_PEARL, "§d§lEnd World",
            "§7End-Dimension",
            "§7• End Islands",
            "§7• End Cities",
            "§7• End Bosses",
            "",
            "§eKlicke zum Teleportieren");
            
        setItem(gui, 20, Material.NETHERRACK, "§c§lNether World",
            "§7Nether-Dimension",
            "§7• Nether Fortresses",
            "§7• Nether Areas",
            "§7• Nether Bosses",
            "",
            "§eKlicke zum Teleportieren");
            
        setItem(gui, 21, Material.STONE_BRICKS, "§7§lDungeons",
            "§7Dungeon-Bereiche",
            "§7• Dungeon Entrances",
            "§7• Dungeon Areas",
            "§7• Dungeon Bosses",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.NETHER_STAR, "§5§lBoss Areas",
            "§7Boss-Bereiche",
            "§7• World Bosses",
            "§7• Raid Bosses",
            "§7• Special Bosses",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.EMERALD, "§a§lMarket",
            "§7Markt-Bereich",
            "§7• Auction House",
            "§7• Bazaar",
            "§7• NPC Shops",
            "",
            "§eKlicke zum Teleportieren");
            
        // Personal Locations
        setItem(gui, 28, Material.GRASS_BLOCK, "§a§lMy Island",
            "§7Deine Insel",
            "§7• Private Island",
            "§7• Island Home",
            "§7• Island Spawn",
            "",
            "§eKlicke zum Teleportieren");
            
        setItem(gui, 29, Material.RED_BED, "§c§lMy Home",
            "§7Dein Zuhause",
            "§7• Personal Home",
            "§7• Home Location",
            "§7• Home Settings",
            "",
            "§eKlicke zum Teleportieren");
            
        setItem(gui, 30, Material.COMPASS, "§e§lMy Warps",
            "§7Deine Warps",
            "§7• Personal Warps",
            "§7• Warp Management",
            "§7• Warp Settings",
            "",
            "§eKlicke zum Öffnen");
            
        // Teleportation Features
        setItem(gui, 37, Material.ENDER_PEARL, "§d§lRandom Teleport",
            "§7Zufällige Teleportation",
            "§7• RTP Command",
            "§7• Random Location",
            "§7• Safe Teleportation",
            "",
            "§eKlicke zum Teleportieren");
            
        setItem(gui, 40, Material.CLOCK, "§e§lTeleport History",
            "§7Teleportations-Verlauf",
            "§7• Last Locations",
            "§7• Teleport History",
            "§7• Back Command",
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
