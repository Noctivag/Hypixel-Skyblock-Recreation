package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.PowerStone;
import de.noctivag.skyblock.enums.Rarity;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.models.AccessoryBag;
import de.noctivag.skyblock.models.PlayerProfile;
import de.noctivag.skyblock.services.MagicalPowerService;
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
import java.util.Map;

public class AccessoryBagGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final PlayerProfileService playerProfileService;
    private final MagicalPowerService magicalPowerService;

    public AccessoryBagGUI(SkyblockPluginRefactored plugin, Player player) {
        super(player);
        this.plugin = plugin;
        this.playerProfileService = plugin.getServiceManager().getService(PlayerProfileService.class);
        this.magicalPowerService = plugin.getServiceManager().getService(MagicalPowerService.class);
        this.inventory = Bukkit.createInventory(this, 54, "§8Accessory Bag");
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void setMenuItems() {
        fillBorderWith(Material.GRAY_STAINED_GLASS_PANE);

        PlayerProfile profile = playerProfileService.getCachedProfile(player.getUniqueId());
        if (profile == null) {
            player.sendMessage("§cFehler beim Laden deines Profils!");
            return;
        }

        AccessoryBag accessoryBag = profile.getAccessoryBag();
        PowerStone activePowerStone = profile.getActivePowerStone();

        // Title item
        ItemStack titleItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§lAccessory Bag");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Verwalte deine Accessories und");
        titleLore.add("§7Magical Power!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Magical Power display
        int totalMP = magicalPowerService.calculateTotalMagicalPower(profile);
        ItemStack mpItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta mpMeta = mpItem.getItemMeta();
        mpMeta.setDisplayName("§d§lMagical Power: §f" + totalMP);
        List<String> mpLore = new ArrayList<>();
        mpLore.add("§7Deine aktuelle Magical Power");
        mpLore.add("§7basierend auf deinen Accessories");
        mpLore.add("");
        mpLore.add("§7Accessories: §e" + accessoryBag.getUniqueAccessoryCount());
        mpMeta.setLore(mpLore);
        mpItem.setItemMeta(mpMeta);
        inventory.setItem(13, mpItem);

        // Active Power Stone
        ItemStack powerStoneItem = new ItemStack(Material.EMERALD);
        ItemMeta powerStoneMeta = powerStoneItem.getItemMeta();
        powerStoneMeta.setDisplayName("§a§lAktiver Power Stone: " + activePowerStone.getDisplayName());
        List<String> powerStoneLore = new ArrayList<>();
        powerStoneLore.add(activePowerStone.getDescription());
        powerStoneLore.add("");
        powerStoneLore.add("§7Stat-Boni:");
        Map<String, Integer> statBonuses = magicalPowerService.calculateStatBonuses(profile);
        for (Map.Entry<String, Integer> entry : statBonuses.entrySet()) {
            powerStoneLore.add("§a+" + entry.getValue() + " " + entry.getKey());
        }
        powerStoneLore.add("");
        powerStoneLore.add("§eKlicke um Power Stone zu ändern!");
        powerStoneMeta.setLore(powerStoneLore);
        powerStoneItem.setItemMeta(powerStoneMeta);
        inventory.setItem(22, powerStoneItem);

        // Accessories display
        Map<String, Rarity> accessories = accessoryBag.getAccessories();
        int slot = 28; // Start slot for accessories
        
        for (Map.Entry<String, Rarity> entry : accessories.entrySet()) {
            if (slot >= 45) break; // Don't go beyond the border
            
            String accessoryName = entry.getKey();
            Rarity rarity = entry.getValue();
            
            ItemStack accessoryItem = new ItemStack(Material.GOLD_NUGGET);
            ItemMeta accessoryMeta = accessoryItem.getItemMeta();
            accessoryMeta.setDisplayName(rarity.getColor() + accessoryName);
            List<String> accessoryLore = new ArrayList<>();
            accessoryLore.add("§7Rarity: " + rarity.getDisplayName());
            accessoryLore.add("§7Magical Power: §d+" + magicalPowerService.getMagicalPowerForRarity(rarity));
            accessoryLore.add("");
            accessoryLore.add("§cKlicke um zu entfernen!");
            accessoryMeta.setLore(accessoryLore);
            accessoryItem.setItemMeta(accessoryMeta);
            inventory.setItem(slot, accessoryItem);
            
            slot++;
        }

        // Add sample accessories for testing
        if (accessories.isEmpty()) {
            ItemStack sampleItem = new ItemStack(Material.CHEST);
            ItemMeta sampleMeta = sampleItem.getItemMeta();
            sampleMeta.setDisplayName("§7Keine Accessories");
            List<String> sampleLore = new ArrayList<>();
            sampleLore.add("§7Du hast noch keine Accessories!");
            sampleLore.add("§7Sammle sie durch verschiedene");
            sampleLore.add("§7Aktivitäten im Spiel.");
            sampleMeta.setLore(sampleLore);
            sampleItem.setItemMeta(sampleMeta);
            inventory.setItem(31, sampleItem);
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
        
        // Handle power stone change
        if (event.getSlot() == 22) {
            openPowerStoneSelection(clickedPlayer);
            return;
        }
        
        // Handle accessory removal
        if (event.getSlot() >= 28 && event.getSlot() < 45) {
            removeAccessory(clickedPlayer, event.getSlot());
            return;
        }
    }

    private void openPowerStoneSelection(Player player) {
        player.closeInventory();
        // In a real implementation, you would open a power stone selection GUI
        player.sendMessage("§ePower Stone Auswahl wird geöffnet...");
    }

    private void removeAccessory(Player player, int slot) {
        PlayerProfile profile = playerProfileService.getCachedProfile(player.getUniqueId());
        if (profile == null) return;
        
        AccessoryBag accessoryBag = profile.getAccessoryBag();
        Map<String, Rarity> accessories = accessoryBag.getAccessories();
        
        // Find the accessory at this slot
        int accessoryIndex = slot - 28;
        String[] accessoryNames = accessories.keySet().toArray(new String[0]);
        
        if (accessoryIndex >= 0 && accessoryIndex < accessoryNames.length) {
            String accessoryName = accessoryNames[accessoryIndex];
            magicalPowerService.removeAccessory(profile, accessoryName);
            
            player.sendMessage("§aAccessory " + accessoryName + " entfernt!");
            
            // Refresh the GUI
            setMenuItems();
        }
    }
}