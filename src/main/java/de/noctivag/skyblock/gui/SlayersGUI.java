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
 * Slayers GUI - Slayer-Quests und -Bosse
 */
public class SlayersGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public SlayersGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void open(Player player) {
        openSlayersGUI(player);
    }
    
    public void openSlayersGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lSlayers"));
        
        // Slayer Types
        setItem(gui, 10, Material.ROTTEN_FLESH, "§c§lZombie Slayer",
            "§7Zombie Slayer",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.SPIDER_EYE, "§8§lSpider Slayer",
            "§7Spider Slayer",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.WOLF_SPAWN_EGG, "§f§lWolf Slayer",
            "§7Wolf Slayer",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.ENDER_PEARL, "§d§lEnderman Slayer",
            "§7Enderman Slayer",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.BLAZE_POWDER, "§6§lBlaze Slayer",
            "§7Blaze Slayer",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        // Slayer Features
        setItem(gui, 19, Material.DIAMOND_SWORD, "§c§lSlayer Weapons",
            "§7Slayer-Waffen",
            "§7• Slayer Swords",
            "§7• Slayer Bows",
            "§7• Special Slayer Weapons",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.DIAMOND_CHESTPLATE, "§b§lSlayer Armor",
            "§7Slayer-Rüstung",
            "§7• Slayer Helmets",
            "§7• Slayer Chestplates",
            "§7• Complete Slayer Sets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ENCHANTED_BOOK, "§d§lSlayer Enchants",
            "§7Slayer-Verzauberungen",
            "§7• Slayer Enchantments",
            "§7• Rare Slayer Enchants",
            "§7• Custom Slayer Enchants",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.POTION, "§5§lSlayer Potions",
            "§7Slayer-Tränke",
            "§7• Slayer Health Potions",
            "§7• Slayer Damage Potions",
            "§7• Special Slayer Potions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHER_STAR, "§d§lSlayer Items",
            "§7Slayer-Items",
            "§7• Slayer Tools",
            "§7• Slayer Accessories",
            "§7• Special Slayer Items",
            "",
            "§eKlicke zum Öffnen");
            
        // Slayer Management
        setItem(gui, 28, Material.CLOCK, "§e§lSlayer Schedule",
            "§7Slayer-Zeitplan",
            "§7• Slayer Activities",
            "§7• Slayer Schedule",
            "§7• Slayer Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.BELL, "§a§lSlayer Notifications",
            "§7Slayer-Benachrichtigungen",
            "§7• Slayer Alerts",
            "§7• Slayer Notifications",
            "§7• Slayer Reminders",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.PAPER, "§b§lSlayer Statistics",
            "§7Slayer-Statistiken",
            "§7• Slayer Performance",
            "§7• Slayer Efficiency",
            "§7• Slayer Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§e§lSlayer Discovery",
            "§7Slayer-Entdeckung",
            "§7• New Slayers",
            "§7• Rare Slayers",
            "§7• Hidden Slayers",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lSlayer Guide",
            "§7Slayer-Anleitung",
            "§7• Slayer Basics",
            "§7• Slayer Tips",
            "§7• Slayer Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Slayer Information
        setItem(gui, 37, Material.BOOK, "§b§lSlayer Information",
            "§7Slayer-Informationen",
            "§7• Slayer Stats",
            "§7• Slayer Abilities",
            "§7• Slayer Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lSlayer Statistics",
            "§7Slayer-Statistiken",
            "§7• Total Slayers: §a0",
            "§7• Slayer Level: §a0",
            "§7• Slayer Efficiency: §a0%",
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
