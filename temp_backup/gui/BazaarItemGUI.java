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
 * Item-spezifische GUI für den Bazaar
 * Zeigt Kauf-/Verkaufsangebote, Graphen und Handels-Buttons für ein spezifisches Item
 */
public class BazaarItemGUI extends Menu {
    
    private final SkyblockPlugin plugin;
    private final BazaarService bazaarService;
    private final String itemName;
    
    // Button-Slots
    private static final int INSTANT_BUY_SLOT = 19;
    private static final int INSTANT_SELL_SLOT = 21;
    private static final int PLACE_BUY_ORDER_SLOT = 23;
    private static final int PLACE_SELL_ORDER_SLOT = 25;
    private static final int VIEW_ORDERS_SLOT = 28;
    private static final int PRICE_GRAPH_SLOT = 30;
    private static final int ITEM_INFO_SLOT = 32;
    
    public BazaarItemGUI(Player player, SkyblockPlugin plugin, String itemName) {
        super(player, 54); // 6 Reihen
        this.plugin = plugin;
        this.bazaarService = plugin.getServiceManager().getService(BazaarService.class);
        this.itemName = itemName;
    }
    
    @Override
    public String getMenuTitle() {
        return "§6§lBazaar - " + itemName;
    }
    
    @Override
    public void setMenuItems() {
        // Fülle Border
        fillBorderWith(BORDER_MATERIAL);
        
        // Zeige Item-Informationen und Buttons
        displayItemInfo();
        displayActionButtons();
        
        // Navigation-Buttons
        addNavigationButtons();
    }
    
    /**
     * Zeigt Item-Informationen an
     */
    private void displayItemInfo() {
        Map<String, Double> instantBuyPrices = bazaarService.getInstantBuyPrices();
        Map<String, Double> instantSellPrices = bazaarService.getInstantSellPrices();
        
        Double buyPrice = instantBuyPrices.get(itemName);
        Double sellPrice = instantSellPrices.get(itemName);
        
        // Item-Info in der Mitte
        ItemStack itemInfo = new ItemStack(getMaterialForItem(itemName));
        ItemMeta meta = itemInfo.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6" + itemName)
                .color(NamedTextColor.GOLD));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Bazaar-Informationen");
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
            lore.add("§7Aktive Orders:");
            lore.add("§7Buy-Orders: §6" + bazaarService.getBuyOrders(itemName).size());
            lore.add("§7Sell-Orders: §6" + bazaarService.getSellOrders(itemName).size());
            
            meta.lore(lore.stream().map(Component::text).toList());
        }
        
        itemInfo.setItemMeta(meta);
        inventory.setItem(ITEM_INFO_SLOT, itemInfo);
    }
    
    /**
     * Zeigt Aktions-Buttons an
     */
    private void displayActionButtons() {
        Map<String, Double> instantBuyPrices = bazaarService.getInstantBuyPrices();
        Map<String, Double> instantSellPrices = bazaarService.getInstantSellPrices();
        
        Double buyPrice = instantBuyPrices.get(itemName);
        Double sellPrice = instantSellPrices.get(itemName);
        
        // Instant-Buy Button
        if (buyPrice != null) {
            ItemStack instantBuyButton = createItem(Material.EMERALD,
                Component.text("§a§lInstant-Buy").color(NamedTextColor.GREEN),
                Arrays.asList(
                    "§7Kaufe sofort für §a" + buyPrice + "§7 Coins pro Stück",
                    "",
                    "§eKlicke zum Kaufen!"
                ));
            inventory.setItem(INSTANT_BUY_SLOT, instantBuyButton);
        } else {
            ItemStack disabledButton = createItem(Material.GRAY_STAINED_GLASS_PANE,
                Component.text("§c§lInstant-Buy").color(NamedTextColor.RED),
                Arrays.asList(
                    "§7Nicht verfügbar",
                    "",
                    "§cDieses Item kann nicht instant gekauft werden"
                ));
            inventory.setItem(INSTANT_BUY_SLOT, disabledButton);
        }
        
        // Instant-Sell Button
        if (sellPrice != null) {
            ItemStack instantSellButton = createItem(Material.REDSTONE,
                Component.text("§c§lInstant-Sell").color(NamedTextColor.RED),
                Arrays.asList(
                    "§7Verkaufe sofort für §c" + sellPrice + "§7 Coins pro Stück",
                    "",
                    "§eKlicke zum Verkaufen!"
                ));
            inventory.setItem(INSTANT_SELL_SLOT, instantSellButton);
        } else {
            ItemStack disabledButton = createItem(Material.GRAY_STAINED_GLASS_PANE,
                Component.text("§c§lInstant-Sell").color(NamedTextColor.RED),
                Arrays.asList(
                    "§7Nicht verfügbar",
                    "",
                    "§cDieses Item kann nicht instant verkauft werden"
                ));
            inventory.setItem(INSTANT_SELL_SLOT, disabledButton);
        }
        
        // Place-Buy-Order Button
        ItemStack placeBuyOrderButton = createItem(Material.GOLD_INGOT,
            Component.text("§6§lPlace Buy-Order").color(NamedTextColor.GOLD),
            Arrays.asList(
                "§7Platziere eine Kauf-Order",
                "§7mit deinem gewünschten Preis",
                "",
                "§eKlicke zum Erstellen!"
            ));
        inventory.setItem(PLACE_BUY_ORDER_SLOT, placeBuyOrderButton);
        
        // Place-Sell-Order Button
        ItemStack placeSellOrderButton = createItem(Material.IRON_INGOT,
            Component.text("§7§lPlace Sell-Order").color(NamedTextColor.GRAY),
            Arrays.asList(
                "§7Platziere eine Verkaufs-Order",
                "§7mit deinem gewünschten Preis",
                "",
                "§eKlicke zum Erstellen!"
            ));
        inventory.setItem(PLACE_SELL_ORDER_SLOT, placeSellOrderButton);
        
        // View-Orders Button
        ItemStack viewOrdersButton = createItem(Material.BOOK,
            Component.text("§b§lView Orders").color(NamedTextColor.AQUA),
            Arrays.asList(
                "§7Zeige alle aktiven Orders",
                "§7für dieses Item an",
                "",
                "§eKlicke zum Anzeigen!"
            ));
        inventory.setItem(VIEW_ORDERS_SLOT, viewOrdersButton);
        
        // Price-Graph Button (Platzhalter)
        ItemStack priceGraphButton = createItem(Material.PAINTING,
            Component.text("§d§lPrice Graph").color(NamedTextColor.LIGHT_PURPLE),
            Arrays.asList(
                "§7Zeige Preis-Verlauf",
                "§7der letzten 24 Stunden",
                "",
                "§eKlicke zum Anzeigen!"
            ));
        inventory.setItem(PRICE_GRAPH_SLOT, priceGraphButton);
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
