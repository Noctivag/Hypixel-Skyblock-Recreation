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
 * Skyblock GUI - Skyblock-Features und -Funktionen
 */
public class SkyblockGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public SkyblockGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openSkyblockGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lSkyblock"));
        
        // Skyblock Features
        setItem(gui, 10, Material.GRASS_BLOCK, "§a§lPrivate Island",
            "§7Private Insel",
            "§7• Island Management",
            "§7• Island Upgrades",
            "§7• Island Teleportation",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.IRON_GOLEM_SPAWN_EGG, "§e§lMinions",
            "§7Minions",
            "§7• Minion Management",
            "§7• Minion Upgrades",
            "§7• Minion Collections",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EXPERIENCE_BOTTLE, "§b§lSkills",
            "§7Fähigkeiten",
            "§7• Skill Overview",
            "§7• XP Tracking",
            "§7• Level Progression",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.BOOK, "§d§lCollections",
            "§7Sammlungen",
            "§7• Collection Progress",
            "§7• Collection Rewards",
            "§7• Collection Milestones",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.BONE, "§6§lPets",
            "§7Haustiere",
            "§7• Pet Management",
            "§7• Pet Leveling",
            "§7• Pet Abilities",
            "",
            "§eKlicke zum Öffnen");
            
        // Skyblock Systems
        setItem(gui, 19, Material.EMERALD, "§a§lEconomy",
            "§7Wirtschaft",
            "§7• Auction House",
            "§7• Bazaar",
            "§7• Banking System",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.DIAMOND_SWORD, "§c§lCombat",
            "§7Kampf",
            "§7• Combat System",
            "§7• PvP System",
            "§7• Boss System",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ENCHANTING_TABLE, "§5§lEnchanting",
            "§7Verzauberung",
            "§7• Enchantment Table",
            "§7• Custom Enchants",
            "§7• Enchantment Books",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.BREWING_STAND, "§9§lAlchemy",
            "§7Brauen",
            "§7• Potion Brewing",
            "§7• Custom Potions",
            "§7• Alchemy XP",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.CRAFTING_TABLE, "§e§lCarpentry",
            "§7Handwerk",
            "§7• Advanced Crafting",
            "§7• Custom Recipes",
            "§7• Carpentry XP",
            "",
            "§eKlicke zum Öffnen");
            
        // Skyblock Areas
        setItem(gui, 28, Material.END_CRYSTAL, "§d§lRunecrafting",
            "§7Runen",
            "§7• Rune Creation",
            "§7• Rune Effects",
            "§7• Runecrafting XP",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.GOLD_INGOT, "§6§lBanking",
            "§7Bank",
            "§7• Bank Account",
            "§7• Interest Rates",
            "§7• Bank Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.NETHER_STAR, "§5§lSpecial Features",
            "§7Besondere Features",
            "§7• Rare Events",
            "§7• Special Items",
            "§7• Hidden Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§e§lNavigation",
            "§7Navigation",
            "§7• World Navigation",
            "§7• Area Navigation",
            "§7• Teleportation",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lSkyblock Guide",
            "§7Skyblock-Anleitung",
            "§7• Skyblock Basics",
            "§7• Skyblock Tips",
            "§7• Skyblock Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Skyblock Information
        setItem(gui, 37, Material.BOOK, "§b§lSkyblock Information",
            "§7Skyblock-Informationen",
            "§7• Skyblock Rules",
            "§7• Skyblock Terms",
            "§7• Skyblock Policies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lSkyblock Statistics",
            "§7Skyblock-Statistiken",
            "§7• Skyblock Level: §a0",
            "§7• Skyblock Progress: §a0%",
            "§7• Skyblock Rank: §a0",
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
