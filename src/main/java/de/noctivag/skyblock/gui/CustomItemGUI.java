package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.CustomItemType;
import de.noctivag.skyblock.enums.Rarity;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.CustomItem;
import de.noctivag.skyblock.services.CustomItemService;
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

public class CustomItemGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final CustomItemService customItemService;
    private int currentPage = 0;
    private final int itemsPerPage = 28;

    public CustomItemGUI(SkyblockPluginRefactored plugin, Player player) {
        super(player, 54);
        this.plugin = plugin;
        this.customItemService = plugin.getServiceManager().getService(CustomItemService.class);
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getMenuTitle() {
        return "§8Custom Items - Seite " + (currentPage + 1);
    }

    @Override
    public void setMenuItems() {
        fillBorderWith(Material.GRAY_STAINED_GLASS_PANE);

        // Title item
        ItemStack titleItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§lCustom Items");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Wähle ein Item aus um es zu erhalten!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Get all custom items
        List<CustomItemType> allItems = customItemService.getAllCustomItems();
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, allItems.size());

        // Display items for current page
        int slot = 20;
        for (int i = startIndex; i < endIndex; i++) {
            if (slot >= 45) break; // Don't go beyond the border
            
            CustomItemType itemType = allItems.get(i);
            CustomItem customItem = new CustomItem(itemType);
            ItemStack itemStack = customItem.toItemStack();
            
            // Add click instruction to lore
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.getLore();
                if (lore == null) lore = new ArrayList<>();
                lore.add("");
                lore.add("§eKlicke um Item zu erhalten!");
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
            }
            
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

        if (endIndex < allItems.size()) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName("§eNächste Seite →");
            nextPage.setItemMeta(nextMeta);
            inventory.setItem(53, nextPage);
        }

        // Category filters
        ItemStack weaponsFilter = new ItemStack(Material.IRON_SWORD);
        ItemMeta weaponsMeta = weaponsFilter.getItemMeta();
        weaponsMeta.setDisplayName("§c§lWaffen");
        List<String> weaponsLore = new ArrayList<>();
        weaponsLore.add("§7Zeige nur Waffen");
        weaponsMeta.setLore(weaponsLore);
        weaponsFilter.setItemMeta(weaponsMeta);
        inventory.setItem(46, weaponsFilter);

        ItemStack armorFilter = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemMeta armorMeta = armorFilter.getItemMeta();
        armorMeta.setDisplayName("§9§lRüstung");
        List<String> armorLore = new ArrayList<>();
        armorLore.add("§7Zeige nur Rüstung");
        armorMeta.setLore(armorLore);
        armorFilter.setItemMeta(armorMeta);
        inventory.setItem(47, armorFilter);

        ItemStack toolsFilter = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta toolsMeta = toolsFilter.getItemMeta();
        toolsMeta.setDisplayName("§a§lWerkzeuge");
        List<String> toolsLore = new ArrayList<>();
        toolsLore.add("§7Zeige nur Werkzeuge");
        toolsMeta.setLore(toolsLore);
        toolsFilter.setItemMeta(toolsMeta);
        inventory.setItem(48, toolsFilter);

        ItemStack accessoriesFilter = new ItemStack(Material.NETHER_STAR);
        ItemMeta accessoriesMeta = accessoriesFilter.getItemMeta();
        accessoriesMeta.setDisplayName("§d§lAccessoires");
        List<String> accessoriesLore = new ArrayList<>();
        accessoriesLore.add("§7Zeige nur Accessoires");
        accessoriesMeta.setLore(accessoriesLore);
        accessoriesFilter.setItemMeta(accessoriesMeta);
        inventory.setItem(49, accessoriesFilter);

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
            List<CustomItemType> allItems = customItemService.getAllCustomItems();
            int maxPages = (int) Math.ceil((double) allItems.size() / itemsPerPage);
            if (currentPage < maxPages - 1) {
                currentPage++;
                setMenuItems();
            }
            return;
        }
        
        // Handle category filters
        if (event.getSlot() >= 46 && event.getSlot() <= 49) {
            handleCategoryFilter(event.getSlot());
            return;
        }
        
        // Handle item selection
        if (event.getSlot() >= 20 && event.getSlot() < 45) {
            handleItemSelection(clickedPlayer, event.getSlot());
            return;
        }
    }

    private void handleCategoryFilter(int slot) {
        List<CustomItemType> filteredItems;
        
        switch (slot) {
            case 46: // Weapons
                filteredItems = customItemService.getAllCustomItems().stream()
                        .filter(CustomItemType::isWeapon)
                        .toList();
                break;
            case 47: // Armor
                filteredItems = customItemService.getAllCustomItems().stream()
                        .filter(CustomItemType::isArmor)
                        .toList();
                break;
            case 48: // Tools
                filteredItems = customItemService.getAllCustomItems().stream()
                        .filter(CustomItemType::isTool)
                        .toList();
                break;
            case 49: // Accessories
                filteredItems = customItemService.getAllCustomItems().stream()
                        .filter(item -> !item.isWeapon() && !item.isArmor() && !item.isTool())
                        .toList();
                break;
            default:
                return;
        }
        
        // TODO: Implement filtering logic
        player.sendMessage("§eFilter wird angewendet...");
    }

    private void handleItemSelection(Player player, int slot) {
        List<CustomItemType> allItems = customItemService.getAllCustomItems();
        int startIndex = currentPage * itemsPerPage;
        int itemIndex = startIndex + (slot - 20);
        
        if (itemIndex >= 0 && itemIndex < allItems.size()) {
            CustomItemType itemType = allItems.get(itemIndex);
            
            // Give item to player
            customItemService.giveCustomItem(player, itemType).thenAccept(success -> {
                if (success) {
                    // Refresh GUI
                    setMenuItems();
                }
            });
        }
    }
}
