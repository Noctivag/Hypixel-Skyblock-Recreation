package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.services.BazaarService;
import de.noctivag.skyblock.services.PlayerProfileService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BazaarCategoryGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final BazaarService bazaarService;
    private final PlayerProfileService playerProfileService;
    private final String category;
    private int currentPage = 0;
    private final int itemsPerPage = 28;

    public BazaarCategoryGUI(SkyblockPluginRefactored plugin, Player player, String category) {
        super(player, 54);
        this.plugin = plugin;
        this.bazaarService = plugin.getServiceManager().getService(BazaarService.class);
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        this.category = category;
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getMenuTitle() {
        return "§8Bazaar - " + category.toUpperCase() + " - Seite " + (currentPage + 1);
    }

    @Override
    public void setMenuItems() {
        fillBorderWith(Material.GRAY_STAINED_GLASS_PANE);

        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        // Title item
        ItemStack titleItem = new ItemStack(getCategoryIcon());
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§l" + category.toUpperCase() + " Items");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Kaufe und verkaufe " + category + " Items");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Player coins display
        ItemStack coinsItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta coinsMeta = coinsItem.getItemMeta();
        coinsMeta.setDisplayName("§6§lDeine Coins");
        List<String> coinsLore = new ArrayList<>();
        coinsMeta.setLore(coinsLore);
        coinsItem.setItemMeta(coinsMeta);
        inventory.setItem(0, coinsItem);

        // Get items for this category
        List<Material> categoryItems = getCategoryItems();
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, categoryItems.size());

        // Display items for current page
        int slot = 20;
        for (int i = startIndex; i < endIndex; i++) {
            if (slot >= 45) break; // Don't go beyond the border
            
            Material material = categoryItems.get(i);
            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();
            
            itemMeta.setDisplayName("§a" + material.name());
            
            List<String> itemLore = new ArrayList<>();
            itemLore.add("§7Kaufpreis: §e" + String.format("%.2f", getBuyPrice(material)) + " Coins");
            itemLore.add("§7Verkaufspreis: §a" + String.format("%.2f", getSellPrice(material)) + " Coins");
            itemLore.add("");
            itemLore.add("§eLinksklick: §7Kaufen");
            itemLore.add("§eRechtsklick: §7Verkaufen");
            
            itemMeta.setLore(itemLore);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(slot, itemStack);
            
            slot++;
        }

        // Navigation buttons
        if (currentPage > 0) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevPage.getItemMeta();
            prevMeta.setDisplayName("§e← Vorherige Seite");
            prevPage.setItemMeta(prevMeta);
            inventory.setItem(45, prevPage);
        }

        if (endIndex < categoryItems.size()) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName("§eNächste Seite →");
            nextPage.setItemMeta(nextMeta);
            inventory.setItem(53, nextPage);
        }

        addNavigationButtons();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getInventory().equals(inventory)) return;
        
        event.setCancelled(true);
        
        Player clickedPlayer = (Player) event.getWhoClicked();
        if (!clickedPlayer.equals(player)) return;
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Handle navigation buttons
        if (event.getSlot() == 45) { // Back button
            clickedPlayer.closeInventory();
            return;
        }
        if (event.getSlot() == 53) { // Close button
            clickedPlayer.closeInventory();
            return;
        }
        
        // Handle page navigation
        if (event.getSlot() == 45 && clickedItem.getType() == Material.ARROW) {
            // Previous page
            if (currentPage > 0) {
                currentPage--;
                setMenuItems();
            }
            return;
        }
        if (event.getSlot() == 53 && clickedItem.getType() == Material.ARROW) {
            // Next page
            List<Material> categoryItems = getCategoryItems();
            int maxPages = (int) Math.ceil((double) categoryItems.size() / itemsPerPage);
            if (currentPage < maxPages - 1) {
                currentPage++;
                setMenuItems();
            }
            return;
        }
        
        // Handle item selection
        if (event.getSlot() >= 20 && event.getSlot() < 45) {
            handleItemClick(clickedPlayer, event.getSlot(), event.isLeftClick());
            return;
        }
    }

    private void handleItemClick(Player player, int slot, boolean isLeftClick) {
        List<Material> categoryItems = getCategoryItems();
        int startIndex = currentPage * itemsPerPage;
        int itemIndex = startIndex + (slot - 20);
        
        if (itemIndex >= 0 && itemIndex < categoryItems.size()) {
            Material material = categoryItems.get(itemIndex);
            
            if (isLeftClick) {
                // Buy item
                openBuyGUI(player, material);
            } else {
                // Sell item
                openSellGUI(player, material);
            }
        }
    }

    private void openBuyGUI(Player player, Material material) {
        player.closeInventory();
        // TODO: Implement BuyGUI
        player.sendMessage("§eBuy GUI für " + material.name() + " wird implementiert...");
    }

    private void openSellGUI(Player player, Material material) {
        player.closeInventory();
        // TODO: Implement SellGUI
        player.sendMessage("§eSell GUI für " + material.name() + " wird implementiert...");
    }

    private Material getCategoryIcon() {
        switch (category.toLowerCase()) {
            case "farming":
                return Material.WHEAT;
            case "mining":
                return Material.DIAMOND_PICKAXE;
            case "combat":
                return Material.IRON_SWORD;
            case "foraging":
                return Material.OAK_LOG;
            default:
                return Material.CHEST;
        }
    }

    private List<Material> getCategoryItems() {
        List<Material> items = new ArrayList<>();
        
        switch (category.toLowerCase()) {
            case "farming":
                items.add(Material.WHEAT);
                items.add(Material.CARROT);
                items.add(Material.POTATO);
                items.add(Material.PUMPKIN);
                items.add(Material.MELON);
                items.add(Material.SUGAR_CANE);
                items.add(Material.CACTUS);
                items.add(Material.COCOA_BEANS);
                break;
            case "mining":
                items.add(Material.COAL);
                items.add(Material.IRON_INGOT);
                items.add(Material.GOLD_INGOT);
                items.add(Material.DIAMOND);
                items.add(Material.EMERALD);
                items.add(Material.REDSTONE);
                items.add(Material.LAPIS_LAZULI);
                items.add(Material.QUARTZ);
                break;
            case "combat":
                items.add(Material.BONE);
                items.add(Material.ROTTEN_FLESH);
                items.add(Material.STRING);
                items.add(Material.SPIDER_EYE);
                items.add(Material.GUNPOWDER);
                items.add(Material.ENDER_PEARL);
                items.add(Material.BLAZE_ROD);
                items.add(Material.GHAST_TEAR);
                break;
            case "foraging":
                items.add(Material.OAK_LOG);
                items.add(Material.BIRCH_LOG);
                items.add(Material.SPRUCE_LOG);
                items.add(Material.JUNGLE_LOG);
                items.add(Material.ACACIA_LOG);
                items.add(Material.DARK_OAK_LOG);
                items.add(Material.APPLE);
                items.add(Material.STICK);
                break;
        }
        
        return items;
    }

    private double getBuyPrice(Material material) {
        // This would be fetched from the BazaarService in a real implementation
        return Math.random() * 100 + 10; // Placeholder
    }

    private double getSellPrice(Material material) {
        // This would be fetched from the BazaarService in a real implementation
        return Math.random() * 80 + 5; // Placeholder
    }
}