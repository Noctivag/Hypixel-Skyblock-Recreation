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
 * Collections GUI - Sammlungen und -Verwaltung
 */
public class CollectionsGUI {
    
    private final SkyblockPlugin plugin;
    
    public CollectionsGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openCollectionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lCollections");
        
        // Collection Categories
        setItem(gui, 10, Material.COBBLESTONE, "§7§lCobblestone",
            "§7Cobblestone",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.COAL, "§8§lCoal",
            "§7Coal",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.IRON_INGOT, "§f§lIron",
            "§7Iron",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.GOLD_INGOT, "§6§lGold",
            "§7Gold",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.DIAMOND, "§b§lDiamond",
            "§7Diamond",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        // Farming Collections
        setItem(gui, 19, Material.WHEAT, "§e§lWheat",
            "§7Wheat",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.CARROT, "§6§lCarrot",
            "§7Carrot",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.POTATO, "§e§lPotato",
            "§7Potato",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.PUMPKIN, "§6§lPumpkin",
            "§7Pumpkin",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.MELON, "§a§lMelon",
            "§7Melon",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        // Foraging Collections
        setItem(gui, 28, Material.OAK_LOG, "§6§lOak Wood",
            "§7Oak Wood",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.BIRCH_LOG, "§f§lBirch Wood",
            "§7Birch Wood",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.SPRUCE_LOG, "§8§lSpruce Wood",
            "§7Spruce Wood",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.JUNGLE_LOG, "§a§lJungle Wood",
            "§7Jungle Wood",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.ACACIA_LOG, "§6§lAcacia Wood",
            "§7Acacia Wood",
            "§7• Current: §a0",
            "§7• Next Milestone: §a0",
            "§7• Progress: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        // Collection Features
        setItem(gui, 37, Material.BOOK, "§b§lCollection Guide",
            "§7Sammlungs-Anleitung",
            "§7• Collection Basics",
            "§7• Collection Tips",
            "§7• Collection Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lCollection Statistics",
            "§7Sammlungs-Statistiken",
            "§7• Total Collections: §a0",
            "§7• Collection Level: §a0",
            "§7• Collection Progress: §a0%",
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
