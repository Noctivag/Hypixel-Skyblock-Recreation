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
 * Minions GUI - Minions und -Verwaltung
 */
public class MinionsGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public MinionsGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openMinionsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lMinions"));
        
        // Minion Management
        setItem(gui, 10, Material.IRON_GOLEM_SPAWN_EGG, "§e§lMy Minions",
            "§7Meine Minions",
            "§7• Active Minions: §a0",
            "§7• Total Minions: §a0",
            "§7• Minion Collection",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lMinion Shop",
            "§7Minion-Shop",
            "§7• Buy Minions",
            "§7• Minion Categories",
            "§7• Special Minions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.DIAMOND, "§b§lMinion Upgrades",
            "§7Minion-Upgrades",
            "§7• Upgrade Minions",
            "§7• Minion Enhancements",
            "§7• Special Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.EXPERIENCE_BOTTLE, "§e§lMinion Leveling",
            "§7Minion-Leveling",
            "§7• Level Up Minions",
            "§7• XP Management",
            "§7• Level Boosts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.CHEST, "§6§lMinion Storage",
            "§7Minion-Lager",
            "§7• Minion Items",
            "§7• Minion Equipment",
            "§7• Minion Storage",
            "",
            "§eKlicke zum Öffnen");
            
        // Minion Types
        setItem(gui, 19, Material.DIAMOND_PICKAXE, "§b§lMining Minions",
            "§7Mining-Minions",
            "§7• Ore Minions",
            "§7• Stone Minions",
            "§7• Special Mining Minions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.WHEAT, "§a§lFarming Minions",
            "§7Farming-Minions",
            "§7• Crop Minions",
            "§7• Animal Minions",
            "§7• Special Farming Minions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.FISHING_ROD, "§9§lFishing Minions",
            "§7Fishing-Minions",
            "§7• Fish Minions",
            "§7• Treasure Minions",
            "§7• Special Fishing Minions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.OAK_LOG, "§6§lForaging Minions",
            "§7Foraging-Minions",
            "§7• Wood Minions",
            "§7• Tree Minions",
            "§7• Special Foraging Minions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.DIAMOND_SWORD, "§c§lCombat Minions",
            "§7Kampf-Minions",
            "§7• Mob Minions",
            "§7• Boss Minions",
            "§7• Special Combat Minions",
            "",
            "§eKlicke zum Öffnen");
            
        // Minion Features
        setItem(gui, 28, Material.CLOCK, "§e§lMinion Schedule",
            "§7Minion-Zeitplan",
            "§7• Minion Activities",
            "§7• Minion Schedule",
            "§7• Minion Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.BELL, "§a§lMinion Notifications",
            "§7Minion-Benachrichtigungen",
            "§7• Minion Alerts",
            "§7• Minion Notifications",
            "§7• Minion Reminders",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.PAPER, "§b§lMinion Statistics",
            "§7Minion-Statistiken",
            "§7• Minion Performance",
            "§7• Minion Efficiency",
            "§7• Minion Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§e§lMinion Discovery",
            "§7Minion-Entdeckung",
            "§7• New Minions",
            "§7• Rare Minions",
            "§7• Hidden Minions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lMinion Guide",
            "§7Minion-Anleitung",
            "§7• Minion Basics",
            "§7• Minion Tips",
            "§7• Minion Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Minion Information
        setItem(gui, 37, Material.BOOK, "§b§lMinion Information",
            "§7Minion-Informationen",
            "§7• Minion Stats",
            "§7• Minion Abilities",
            "§7• Minion Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lMinion Statistics",
            "§7Minion-Statistiken",
            "§7• Total Minions: §a0",
            "§7• Minion Level: §a0",
            "§7• Minion Efficiency: §a0%",
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
