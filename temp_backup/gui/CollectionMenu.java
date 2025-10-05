package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.CollectionType;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.Collection;
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

public class CollectionMenu extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final PlayerProfileService playerProfileService;

    public CollectionMenu(SkyblockPluginRefactored plugin, Player player) {
        super(player, 54);
        this.plugin = plugin;
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getMenuTitle() {
        return "§8Collections";
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
        ItemStack titleItem = new ItemStack(Material.BOOK);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§lCollections");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Sammle Items um neue");
        titleLore.add("§7Rezepte freizuschalten!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Collection categories
        int slot = 20;
        for (CollectionType collectionType : CollectionType.values()) {
            if (slot >= 45) break; // Don't go beyond the border
            
            Collection collection = profile.getCollection(collectionType);
            if (collection == null) {
                collection = new Collection(collectionType);
            }
            
            ItemStack collectionItem = new ItemStack(collectionType.getIcon());
            ItemMeta collectionMeta = collectionItem.getItemMeta();
            collectionMeta.setDisplayName(collectionType.getDisplayName());
            
            List<String> collectionLore = new ArrayList<>();
            collectionLore.add("§7Fortschritt: §a" + collection.getTotalCollected() + "§7/§e" + collection.getTotalItems());
            collectionLore.add("§7Level: §b" + collection.getLevel());
            collectionLore.add("§7XP: §d" + collection.getExperience());
            collectionLore.add("");
            collectionLore.add("§7Freigeschaltete Rezepte:");
            
            // Show unlocked recipes
            List<String> unlockedRecipes = collection.getUnlockedRecipes();
            if (unlockedRecipes.isEmpty()) {
                collectionLore.add("§cKeine Rezepte freigeschaltet");
            } else {
                for (String recipe : unlockedRecipes) {
                    collectionLore.add("§a✓ " + recipe);
                }
            }
            
            collectionLore.add("");
            collectionLore.add("§eKlicke um Details anzuzeigen!");
            
            collectionMeta.setLore(collectionLore);
            collectionItem.setItemMeta(collectionMeta);
            inventory.setItem(slot, collectionItem);
            
            slot++;
        }

        // Collection statistics
        ItemStack statsItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta statsMeta = statsItem.getItemMeta();
        statsMeta.setDisplayName("§d§lCollection Statistiken");
        List<String> statsLore = new ArrayList<>();
        
        int totalCollected = profile.getCollections().values().stream()
            .mapToInt(Collection::getTotalCollected)
            .sum();
        int totalItems = profile.getCollections().values().stream()
            .mapToInt(Collection::getTotalItems)
            .sum();
        
        statsLore.add("§7Gesamt gesammelt: §a" + totalCollected);
        statsLore.add("§7Gesamt Items: §e" + totalItems);
        statsLore.add("§7Fortschritt: §b" + String.format("%.1f", (double) totalCollected / totalItems * 100) + "%");
        statsLore.add("");
        statsLore.add("§7Collection Level: §d" + profile.getCollections().values().stream()
            .mapToInt(Collection::getLevel)
            .sum());
        
        statsMeta.setLore(statsLore);
        statsItem.setItemMeta(statsMeta);
        inventory.setItem(40, statsItem);

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
        
        // Handle collection selection
        if (event.getSlot() >= 20 && event.getSlot() < 45) {
            handleCollectionClick(clickedPlayer, event.getSlot());
            return;
        }
    }

    private void handleCollectionClick(Player player, int slot) {
        CollectionType[] collectionTypes = CollectionType.values();
        int collectionIndex = slot - 20;
        
        if (collectionIndex >= 0 && collectionIndex < collectionTypes.length) {
            CollectionType collectionType = collectionTypes[collectionIndex];
            
            // Open detailed collection GUI
            CollectionDetailGUI detailGUI = new CollectionDetailGUI(plugin, player, collectionType);
            detailGUI.open();
        }
    }
}
