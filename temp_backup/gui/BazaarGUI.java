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

public class BazaarGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final BazaarService bazaarService;
    private final PlayerProfileService playerProfileService;
    private int currentPage = 0;
    private final int itemsPerPage = 28;

    public BazaarGUI(SkyblockPluginRefactored plugin, Player player) {
        super(player, 54);
        this.plugin = plugin;
        this.bazaarService = plugin.getServiceManager().getService(BazaarService.class);
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getMenuTitle() {
        return "§8Bazaar - Seite " + (currentPage + 1);
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
        ItemStack titleItem = new ItemStack(Material.EMERALD);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§a§lBazaar");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Kaufe und verkaufe Items");
        titleLore.add("§7mit anderen Spielern!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Player coins display
        ItemStack coinsItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta coinsMeta = coinsItem.getItemMeta();
        coinsMeta.setDisplayName("§6§lDeine Coins");
        List<String> coinsLore = new ArrayList<>();
        coinsLore.add("§7Verfügbare Coins: §e" + String.format("%,.0f", profile.getCoins()));
        coinsMeta.setLore(coinsLore);
        coinsItem.setItemMeta(coinsMeta);
        inventory.setItem(0, coinsItem);

        // Bazaar categories
        ItemStack farmingCategory = new ItemStack(Material.WHEAT);
        ItemMeta farmingMeta = farmingCategory.getItemMeta();
        farmingMeta.setDisplayName("§a§lFarming Items");
        List<String> farmingLore = new ArrayList<>();
        farmingLore.add("§7Wheat, Carrot, Potato,");
        farmingLore.add("§7Pumpkin, Melon, etc.");
        farmingLore.add("");
        farmingLore.add("§eKlicke um Farming Items zu sehen!");
        farmingMeta.setLore(farmingLore);
        farmingCategory.setItemMeta(farmingMeta);
        inventory.setItem(20, farmingCategory);

        ItemStack miningCategory = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta miningMeta = miningCategory.getItemMeta();
        miningMeta.setDisplayName("§b§lMining Items");
        List<String> miningLore = new ArrayList<>();
        miningLore.add("§7Coal, Iron, Gold,");
        miningLore.add("§7Diamond, Emerald, etc.");
        miningLore.add("");
        miningLore.add("§eKlicke um Mining Items zu sehen!");
        miningMeta.setLore(miningLore);
        miningCategory.setItemMeta(miningMeta);
        inventory.setItem(21, miningCategory);

        ItemStack combatCategory = new ItemStack(Material.IRON_SWORD);
        ItemMeta combatMeta = combatCategory.getItemMeta();
        combatMeta.setDisplayName("§c§lCombat Items");
        List<String> combatLore = new ArrayList<>();
        combatLore.add("§7Bone, Rotten Flesh,");
        combatLore.add("§7String, Spider Eye, etc.");
        combatLore.add("");
        combatLore.add("§eKlicke um Combat Items zu sehen!");
        combatMeta.setLore(combatLore);
        combatCategory.setItemMeta(combatMeta);
        inventory.setItem(22, combatCategory);

        ItemStack foragingCategory = new ItemStack(Material.OAK_LOG);
        ItemMeta foragingMeta = foragingCategory.getItemMeta();
        foragingMeta.setDisplayName("§2§lForaging Items");
        List<String> foragingLore = new ArrayList<>();
        foragingLore.add("§7Oak Log, Birch Log,");
        foragingLore.add("§7Spruce Log, Jungle Log, etc.");
        foragingLore.add("");
        foragingLore.add("§eKlicke um Foraging Items zu sehen!");
        foragingMeta.setLore(foragingLore);
        foragingCategory.setItemMeta(foragingMeta);
        inventory.setItem(23, foragingCategory);

        // My orders
        ItemStack myOrders = new ItemStack(Material.BOOK);
        ItemMeta myOrdersMeta = myOrders.getItemMeta();
        myOrdersMeta.setDisplayName("§d§lMeine Orders");
        List<String> myOrdersLore = new ArrayList<>();
        myOrdersLore.add("§7Zeige deine aktiven");
        myOrdersLore.add("§7Kauf- und Verkaufsorders");
        myOrdersLore.add("");
        myOrdersLore.add("§eKlicke um deine Orders zu sehen!");
        myOrdersMeta.setLore(myOrdersLore);
        myOrders.setItemMeta(myOrdersMeta);
        inventory.setItem(40, myOrders);

        // Quick sell
        ItemStack quickSell = new ItemStack(Material.HOPPER);
        ItemMeta quickSellMeta = quickSell.getItemMeta();
        quickSellMeta.setDisplayName("§6§lQuick Sell");
        List<String> quickSellLore = new ArrayList<>();
        quickSellLore.add("§7Verkaufe alle Items in");
        quickSellLore.add("§7deinem Inventar schnell!");
        quickSellLore.add("");
        quickSellLore.add("§eKlicke um Quick Sell zu öffnen!");
        quickSellMeta.setLore(quickSellLore);
        quickSell.setItemMeta(quickSellMeta);
        inventory.setItem(42, quickSell);

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
        
        // Handle category selection
        if (event.getSlot() >= 20 && event.getSlot() <= 23) {
            handleCategoryClick(clickedPlayer, event.getSlot());
            return;
        }
        
        // Handle my orders
        if (event.getSlot() == 40) {
            openMyOrders(clickedPlayer);
            return;
        }
        
        // Handle quick sell
        if (event.getSlot() == 42) {
            openQuickSell(clickedPlayer);
            return;
        }
    }

    private void handleCategoryClick(Player player, int slot) {
        String category = "";
        switch (slot) {
            case 20:
                category = "farming";
                break;
            case 21:
                category = "mining";
                break;
            case 22:
                category = "combat";
                break;
            case 23:
                category = "foraging";
                break;
        }
        
        // Open category GUI
        BazaarCategoryGUI categoryGUI = new BazaarCategoryGUI(plugin, player, category);
        categoryGUI.open();
    }

    private void openMyOrders(Player player) {
        player.closeInventory();
        // TODO: Implement MyOrdersGUI
        player.sendMessage("§eMy Orders GUI wird implementiert...");
    }

    private void openQuickSell(Player player) {
        player.closeInventory();
        // TODO: Implement QuickSellGUI
        player.sendMessage("§eQuick Sell GUI wird implementiert...");
    }
}