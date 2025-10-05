package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * Hauptmenü für den Bazaar
 * Zeigt Kategorien von handelbaren Items an
 */
public class BazaarMainMenuGUI extends Menu {
    
    private final SkyblockPlugin plugin;
    
    // Kategorie-Slots
    private static final int[] CATEGORY_SLOTS = {
        19, 20, 21, 22, 23,  // Reihe 1
        28, 29, 30, 31, 32,  // Reihe 2
        37, 38, 39, 40, 41   // Reihe 3
    };
    
    public BazaarMainMenuGUI(Player player, SkyblockPlugin plugin) {
        super(player, 54); // 6 Reihen
        this.plugin = plugin;
    }
    
    @Override
    public String getMenuTitle() {
        return "§6§lBazaar";
    }
    
    @Override
    public void setMenuItems() {
        // Fülle Border
        fillBorderWith(BORDER_MATERIAL);
        
        // Zeige Kategorien
        displayCategories();
        
        // Navigation-Buttons
        addNavigationButtons();
    }
    
    /**
     * Zeigt alle verfügbaren Kategorien an
     */
    private void displayCategories() {
        // Farming-Kategorie
        ItemStack farmingCategory = createItem(Material.WHEAT,
            Component.text("§a§lFarming").color(NamedTextColor.GREEN),
            Arrays.asList(
                "§7Landwirtschaftliche Produkte",
                "§7Weizen, Karotten, Kartoffeln, etc.",
                "",
                "§eKlicke zum Öffnen!"
            ));
        inventory.setItem(CATEGORY_SLOTS[0], farmingCategory);
        
        // Mining-Kategorie
        ItemStack miningCategory = createItem(Material.DIAMOND_PICKAXE,
            Component.text("§9§lMining").color(NamedTextColor.BLUE),
            Arrays.asList(
                "§7Bergbau-Produkte",
                "§7Kohle, Eisen, Gold, Diamanten, etc.",
                "",
                "§eKlicke zum Öffnen!"
            ));
        inventory.setItem(CATEGORY_SLOTS[1], miningCategory);
        
        // Foraging-Kategorie
        ItemStack foragingCategory = createItem(Material.OAK_LOG,
            Component.text("§6§lForaging").color(NamedTextColor.GOLD),
            Arrays.asList(
                "§7Forstwirtschaft-Produkte",
                "§7Eichenholz, Birkenholz, etc.",
                "",
                "§eKlicke zum Öffnen!"
            ));
        inventory.setItem(CATEGORY_SLOTS[2], foragingCategory);
        
        // Fishing-Kategorie
        ItemStack fishingCategory = createItem(Material.FISHING_ROD,
            Component.text("§b§lFishing").color(NamedTextColor.AQUA),
            Arrays.asList(
                "§7Angel-Produkte",
                "§7Fische, Schätze, etc.",
                "",
                "§eKlicke zum Öffnen!"
            ));
        inventory.setItem(CATEGORY_SLOTS[3], fishingCategory);
        
        // Combat-Kategorie
        ItemStack combatCategory = createItem(Material.DIAMOND_SWORD,
            Component.text("§c§lCombat").color(NamedTextColor.RED),
            Arrays.asList(
                "§7Kampf-Produkte",
                "§7Mob-Drops, Waffen, etc.",
                "",
                "§eKlicke zum Öffnen!"
            ));
        inventory.setItem(CATEGORY_SLOTS[4], combatCategory);
        
        // Enchanting-Kategorie
        ItemStack enchantingCategory = createItem(Material.ENCHANTING_TABLE,
            Component.text("§5§lEnchanting").color(NamedTextColor.DARK_PURPLE),
            Arrays.asList(
                "§7Verzauberungs-Produkte",
                "§7Erfahrung, Bücher, etc.",
                "",
                "§eKlicke zum Öffnen!"
            ));
        inventory.setItem(CATEGORY_SLOTS[5], enchantingCategory);
        
        // Alchemy-Kategorie
        ItemStack alchemyCategory = createItem(Material.BREWING_STAND,
            Component.text("§d§lAlchemy").color(NamedTextColor.LIGHT_PURPLE),
            Arrays.asList(
                "§7Alchemie-Produkte",
                "§7Tränke, Zutaten, etc.",
                "",
                "§eKlicke zum Öffnen!"
            ));
        inventory.setItem(CATEGORY_SLOTS[6], alchemyCategory);
        
        // Carpentry-Kategorie
        ItemStack carpentryCategory = createItem(Material.CRAFTING_TABLE,
            Component.text("§e§lCarpentry").color(NamedTextColor.YELLOW),
            Arrays.asList(
                "§7Tischlerei-Produkte",
                "§7Handwerks-Items, etc.",
                "",
                "§eKlicke zum Öffnen!"
            ));
        inventory.setItem(CATEGORY_SLOTS[7], carpentryCategory);
        
        // Special-Kategorie
        ItemStack specialCategory = createItem(Material.NETHER_STAR,
            Component.text("§6§lSpecial").color(NamedTextColor.GOLD),
            Arrays.asList(
                "§7Spezielle Produkte",
                "§7Seltene Items, etc.",
                "",
                "§eKlicke zum Öffnen!"
            ));
        inventory.setItem(CATEGORY_SLOTS[8], specialCategory);
        
        // Fülle leere Slots
        for (int i = 9; i < CATEGORY_SLOTS.length; i++) {
            ItemStack emptySlot = createItem(Material.AIR, " ", null);
            inventory.setItem(CATEGORY_SLOTS[i], emptySlot);
        }
    }
}
