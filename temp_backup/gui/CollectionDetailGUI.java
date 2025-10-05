package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.CollectionType;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.Collection;
import de.noctivag.skyblock.models.CollectionItem;
import de.noctivag.skyblock.models.PlayerProfile;
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

public class CollectionDetailGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final PlayerProfileService playerProfileService;
    private final CollectionType collectionType;
    private int currentPage = 0;
    private final int itemsPerPage = 28;

    public CollectionDetailGUI(SkyblockPluginRefactored plugin, Player player, CollectionType collectionType) {
        super(player, 54);
        this.plugin = plugin;
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        this.collectionType = collectionType;
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getMenuTitle() {
        return "§8" + collectionType.getDisplayName() + " - Seite " + (currentPage + 1);
    }

    @Override
    public void setMenuItems() {
        fillBorderWith(Material.GRAY_STAINED_GLASS_PANE);

        PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        Collection collection = profile.getCollection(collectionType);
        if (collection == null) {
            collection = new Collection(collectionType);
        }

        // Title item
        ItemStack titleItem = new ItemStack(collectionType.getIcon());
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§l" + collectionType.getDisplayName());
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Fortschritt: §a" + collection.getTotalCollected() + "§7/§e" + collection.getTotalItems());
        titleLore.add("§7Level: §b" + collection.getLevel());
        titleLore.add("§7XP: §d" + collection.getExperience());
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Collection items
        List<CollectionItem> collectionItems = collection.getItems();
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, collectionItems.size());

        int slot = 20;
        for (int i = startIndex; i < endIndex; i++) {
            if (slot >= 45) break; // Don't go beyond the border
            
            CollectionItem item = collectionItems.get(i);
            ItemStack itemStack = new ItemStack(item.getMaterial());
            ItemMeta itemMeta = itemStack.getItemMeta();
            
            // Set display name
            if (item.isUnlocked()) {
                itemMeta.setDisplayName("§a" + item.getName());
            } else {
                itemMeta.setDisplayName("§7" + item.getName());
            }
            
            List<String> itemLore = new ArrayList<>();
            itemLore.add("§7Benötigt: §e" + item.getRequiredAmount());
            itemLore.add("§7Gesammelt: §a" + item.getCollectedAmount());
            
            if (item.isUnlocked()) {
                itemLore.add("§a✓ Freigeschaltet!");
            } else {
                int remaining = item.getRequiredAmount() - item.getCollectedAmount();
                itemLore.add("§c✗ Noch " + remaining + " benötigt");
            }
            
            if (item.getReward() != null) {
                itemLore.add("");
                itemLore.add("§6Belohnung: §e" + item.getReward());
            }
            
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

        if (endIndex < collectionItems.size()) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName("§eNächste Seite →");
            nextPage.setItemMeta(nextMeta);
            inventory.setItem(53, nextPage);
        }

        // Collection rewards
        ItemStack rewardsItem = new ItemStack(Material.CHEST);
        ItemMeta rewardsMeta = rewardsItem.getItemMeta();
        rewardsMeta.setDisplayName("§d§lCollection Belohnungen");
        List<String> rewardsLore = new ArrayList<>();
        rewardsLore.add("§7Freigeschaltete Rezepte:");
        
        List<String> unlockedRecipes = collection.getUnlockedRecipes();
        if (unlockedRecipes.isEmpty()) {
            rewardsLore.add("§cKeine Rezepte freigeschaltet");
        } else {
            for (String recipe : unlockedRecipes) {
                rewardsLore.add("§a✓ " + recipe);
            }
        }
        
        rewardsMeta.setLore(rewardsLore);
        rewardsItem.setItemMeta(rewardsMeta);
        inventory.setItem(40, rewardsItem);

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
            PlayerProfile profile = playerProfileService.loadProfile(player.getUniqueId()).join();
            if (profile != null) {
                Collection collection = profile.getCollection(collectionType);
                if (collection != null) {
                    int maxPages = (int) Math.ceil((double) collection.getItems().size() / itemsPerPage);
                    if (currentPage < maxPages - 1) {
                        currentPage++;
                        setMenuItems();
                    }
                }
            }
            return;
        }
    }
}
