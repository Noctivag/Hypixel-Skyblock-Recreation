package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.services.BazaarService;
import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Kategorie-GUI für den Bazaar
 * Zeigt alle Items einer bestimmten Kategorie an
 */
public class BazaarCategoryGUI extends Menu {
    
    private final SkyblockPlugin plugin;
    private final BazaarService bazaarService;
    private final String categoryName;
    
    // Item-Slots (5x4 Grid)
    private static final int[] ITEM_SLOTS = {
        19, 20, 21, 22, 23,  // Reihe 1
        28, 29, 30, 31, 32,  // Reihe 2
        37, 38, 39, 40, 41,  // Reihe 3
        46, 47, 48, 49, 50   // Reihe 4
    };
    
    public BazaarCategoryGUI(Player player, SkyblockPlugin plugin, String categoryName) {
        super(player, 54); // 6 Reihen
        this.plugin = plugin;
        this.bazaarService = plugin.getServiceManager().getService(BazaarService.class);
        this.categoryName = categoryName;
    }
    
    @Override
    public String getMenuTitle() {
        return "§6§lBazaar - " + categoryName;
    }
    
    @Override
    public void setMenuItems() {
        // Fülle Border
        fillBorderWith(BORDER_MATERIAL);
        
        // Zeige Items der Kategorie
        displayCategoryItems();
        
        // Navigation-Buttons
        addNavigationButtons();
    }
    
    /**
     * Zeigt alle Items der Kategorie an
     */
    private void displayCategoryItems() {
        List<String> items = getItemsForCategory(categoryName);
        Map<String, Double> instantBuyPrices = bazaarService.getInstantBuyPrices();
        Map<String, Double> instantSellPrices = bazaarService.getInstantSellPrices();
        
        for (int i = 0; i < Math.min(items.size(), ITEM_SLOTS.length); i++) {
            String itemName = items.get(i);
            int slot = ITEM_SLOTS[i];
            
            ItemStack itemStack = createItemStack(itemName, instantBuyPrices, instantSellPrices);
            inventory.setItem(slot, itemStack);
        }
        
        // Fülle leere Slots
        for (int i = items.size(); i < ITEM_SLOTS.length; i++) {
            ItemStack emptySlot = createItem(Material.AIR, " ", null);
            inventory.setItem(ITEM_SLOTS[i], emptySlot);
        }
    }
    
    /**
     * Gibt die Items für eine Kategorie zurück
     * @param categoryName Der Kategorie-Name
     * @return Liste der Item-Namen
     */
    private List<String> getItemsForCategory(String categoryName) {
        List<String> items = new ArrayList<>();
        
        switch (categoryName.toLowerCase()) {
            case "farming":
                items.addAll(Arrays.asList("WHEAT", "CARROT", "POTATO", "BEETROOT", "PUMPKIN", "MELON"));
                break;
            case "mining":
                items.addAll(Arrays.asList("COAL", "IRON_INGOT", "GOLD_INGOT", "DIAMOND", "EMERALD", "REDSTONE"));
                break;
            case "foraging":
                items.addAll(Arrays.asList("OAK_LOG", "BIRCH_LOG", "SPRUCE_LOG", "JUNGLE_LOG", "ACACIA_LOG", "DARK_OAK_LOG"));
                break;
            case "fishing":
                items.addAll(Arrays.asList("RAW_FISH", "RAW_SALMON", "CLOWNFISH", "PUFFERFISH", "PRISMARINE_SHARD", "PRISMARINE_CRYSTALS"));
                break;
            case "combat":
                items.addAll(Arrays.asList("ROTTEN_FLESH", "BONE", "STRING", "SPIDER_EYE", "GUNPOWDER", "BLAZE_ROD"));
                break;
            case "enchanting":
                items.addAll(Arrays.asList("EXPERIENCE_BOTTLE", "ENCHANTED_BOOK", "LAPIS_LAZULI", "ENDER_PEARL", "GHAST_TEAR", "GLOWSTONE_DUST"));
                break;
            case "alchemy":
                items.addAll(Arrays.asList("NETHER_WART", "GLOWSTONE_DUST", "REDSTONE", "GUNPOWDER", "SPIDER_EYE", "FERMENTED_SPIDER_EYE"));
                break;
            case "carpentry":
                items.addAll(Arrays.asList("STICK", "PLANKS", "WOOD", "WOOL", "LEATHER", "STRING"));
                break;
            case "special":
                items.addAll(Arrays.asList("NETHER_STAR", "DRAGON_EGG", "ELYTRA", "BEACON", "CONDUIT", "TOTEM_OF_UNDYING"));
                break;
        }
        
        return items;
    }
    
    /**
     * Erstellt ein ItemStack für ein Bazaar-Item
     * @param itemName Der Item-Name
     * @param instantBuyPrices Instant-Buy-Preise
     * @param instantSellPrices Instant-Sell-Preise
     * @return ItemStack
     */
    private ItemStack createItemStack(String itemName, Map<String, Double> instantBuyPrices, Map<String, Double> instantSellPrices) {
        Material material = getMaterialForItem(itemName);
        Double buyPrice = instantBuyPrices.get(itemName);
        Double sellPrice = instantSellPrices.get(itemName);
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6" + itemName)
                .color(NamedTextColor.GOLD));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Kategorie: §e" + categoryName);
            lore.add("");
            
            if (buyPrice != null) {
                lore.add("§7Instant-Buy: §a" + buyPrice + "§7 Coins");
            } else {
                lore.add("§7Instant-Buy: §cNicht verfügbar");
            }
            
            if (sellPrice != null) {
                lore.add("§7Instant-Sell: §c" + sellPrice + "§7 Coins");
            } else {
                lore.add("§7Instant-Sell: §cNicht verfügbar");
            }
            
            lore.add("");
            lore.add("§eKlicke für Details!");
            
            meta.lore(lore.stream().map(Component::text).toList());
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Gibt das Material für ein Item zurück
     * @param itemName Der Item-Name
     * @return Material
     */
    private Material getMaterialForItem(String itemName) {
        try {
            return Material.valueOf(itemName);
        } catch (IllegalArgumentException e) {
            // Fallback für Items, die nicht direkt als Material existieren
            switch (itemName) {
                case "RAW_FISH":
                    return Material.COD;
                case "RAW_SALMON":
                    return Material.SALMON;
                case "CLOWNFISH":
                    return Material.TROPICAL_FISH;
                case "PUFFERFISH":
                    return Material.PUFFERFISH;
                case "EXPERIENCE_BOTTLE":
                    return Material.EXPERIENCE_BOTTLE;
                case "ENCHANTED_BOOK":
                    return Material.ENCHANTED_BOOK;
                case "LAPIS_LAZULI":
                    return Material.LAPIS_LAZULI;
                case "ENDER_PEARL":
                    return Material.ENDER_PEARL;
                case "GHAST_TEAR":
                    return Material.GHAST_TEAR;
                case "GLOWSTONE_DUST":
                    return Material.GLOWSTONE_DUST;
                case "NETHER_WART":
                    return Material.NETHER_WART;
                case "REDSTONE":
                    return Material.REDSTONE;
                case "GUNPOWDER":
                    return Material.GUNPOWDER;
                case "SPIDER_EYE":
                    return Material.SPIDER_EYE;
                case "FERMENTED_SPIDER_EYE":
                    return Material.FERMENTED_SPIDER_EYE;
                case "STICK":
                    return Material.STICK;
                case "PLANKS":
                    return Material.OAK_PLANKS;
                case "WOOD":
                    return Material.OAK_WOOD;
                case "WOOL":
                    return Material.WHITE_WOOL;
                case "LEATHER":
                    return Material.LEATHER;
                case "STRING":
                    return Material.STRING;
                case "NETHER_STAR":
                    return Material.NETHER_STAR;
                case "DRAGON_EGG":
                    return Material.DRAGON_EGG;
                case "ELYTRA":
                    return Material.ELYTRA;
                case "BEACON":
                    return Material.BEACON;
                case "CONDUIT":
                    return Material.CONDUIT;
                case "TOTEM_OF_UNDYING":
                    return Material.TOTEM_OF_UNDYING;
                case "ROTTEN_FLESH":
                    return Material.ROTTEN_FLESH;
                case "BONE":
                    return Material.BONE;
                case "BLAZE_ROD":
                    return Material.BLAZE_ROD;
                case "PRISMARINE_SHARD":
                    return Material.PRISMARINE_SHARD;
                case "PRISMARINE_CRYSTALS":
                    return Material.PRISMARINE_CRYSTALS;
                default:
                    return Material.STONE;
            }
        }
    }
}
