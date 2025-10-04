package de.noctivag.plugin.qualityoflife;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class QualityOfLifeSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerQoLData> playerQoLData = new ConcurrentHashMap<>();
    
    public QualityOfLifeSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        PlayerQoLData data = getPlayerQoLData(playerId);
        
        // Apply saved effects
        applySavedEffects(player, data);
        
        // Send welcome message
        player.sendMessage("§aWillkommen zurück, " + player.getName() + "!");
        player.sendMessage("§7Verwende /qol für Quality of Life Features!");
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        PlayerQoLData data = getPlayerQoLData(playerId);
        
        // Save current effects
        saveCurrentEffects(player, data);
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        PlayerQoLData data = getPlayerQoLData(playerId);
        
        // Apply saved effects after respawn
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            applySavedEffects(player, data);
        }, 20L); // 1 second delay
    }
    
    public void openQoLGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§a§lQuality of Life");
        
        // Add QoL categories
        addGUIItem(gui, 10, Material.POTION, "§d§lEffects", "§7Manage your potion effects.");
        addGUIItem(gui, 11, Material.COMPASS, "§e§lNavigation", "§7Navigation and waypoints.");
        addGUIItem(gui, 12, Material.CLOCK, "§6§lTime", "§7Time and weather control.");
        addGUIItem(gui, 13, Material.ENDER_PEARL, "§5§lTeleportation", "§7Teleportation features.");
        addGUIItem(gui, 14, Material.CHEST, "§b§lInventory", "§7Inventory management.");
        addGUIItem(gui, 15, Material.ENCHANTED_BOOK, "§c§lEnchantments", "§7Enchantment management.");
        addGUIItem(gui, 16, Material.ANVIL, "§7§lRepair", "§7Item repair and maintenance.");
        addGUIItem(gui, 17, Material.EMERALD, "§a§lEconomy", "§7Economy and trading.");
        addGUIItem(gui, 18, Material.BOOK, "§f§lInformation", "§7Information and statistics.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the QoL menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aQuality of Life GUI geöffnet!");
    }
    
    public void openEffectsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§d§lEffects");
        
        // Add effect categories
        addGUIItem(gui, 10, Material.POTION, "§d§lPotion Effects", "§7Manage potion effects.");
        addGUIItem(gui, 11, Material.GOLDEN_APPLE, "§6§lFood Effects", "§7Manage food effects.");
        addGUIItem(gui, 12, Material.ENCHANTED_BOOK, "§b§lEnchantment Effects", "§7Manage enchantment effects.");
        addGUIItem(gui, 13, Material.BEACON, "§e§lBeacon Effects", "§7Manage beacon effects.");
        addGUIItem(gui, 14, Material.NETHER_STAR, "§c§lSpecial Effects", "§7Manage special effects.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to QoL menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the effects menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openNavigationGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§e§lNavigation");
        
        // Add navigation features
        addGUIItem(gui, 10, Material.COMPASS, "§e§lCompass", "§7Set compass target.");
        addGUIItem(gui, 11, Material.ENDER_PEARL, "§5§lWaypoints", "§7Manage waypoints.");
        addGUIItem(gui, 12, Material.MAP, "§a§lMap", "§7View world map.");
        addGUIItem(gui, 13, Material.CLOCK, "§6§lCoordinates", "§7Show coordinates.");
        addGUIItem(gui, 14, Material.BEACON, "§b§lBeacon", "§7Beacon navigation.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to QoL menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the navigation menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openTimeGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lTime & Weather");
        
        // Add time features
        addGUIItem(gui, 10, Material.CLOCK, "§6§lTime", "§7Control time.");
        addGUIItem(gui, 11, Material.WATER_BUCKET, "§b§lWeather", "§7Control weather.");
        addGUIItem(gui, 12, Material.SUNFLOWER, "§e§lDay", "§7Set to day.");
        addGUIItem(gui, 13, Material.END_CRYSTAL, "§5§lNight", "§7Set to night.");
        addGUIItem(gui, 14, Material.CLOCK, "§7§lDawn", "§7Set to dawn.");
        addGUIItem(gui, 15, Material.CLOCK, "§6§lDusk", "§7Set to dusk.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to QoL menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the time menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openTeleportationGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§5§lTeleportation");
        
        // Add teleportation features
        addGUIItem(gui, 10, Material.ENDER_PEARL, "§5§lTeleport", "§7Teleport to location.");
        addGUIItem(gui, 11, Material.ENDER_EYE, "§d§lEnd Portal", "§7Teleport to End Portal.");
        addGUIItem(gui, 12, Material.NETHER_PORTAL, "§c§lNether Portal", "§7Teleport to Nether Portal.");
        addGUIItem(gui, 13, Material.ENDER_CHEST, "§b§lEnd City", "§7Teleport to End City.");
        addGUIItem(gui, 14, Material.NETHER_STAR, "§6§lStronghold", "§7Teleport to Stronghold.");
        addGUIItem(gui, 15, Material.DRAGON_EGG, "§e§lDragon", "§7Teleport to Dragon.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to QoL menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the teleportation menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openInventoryGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§b§lInventory Management");
        
        // Add inventory features
        addGUIItem(gui, 10, Material.CHEST, "§b§lSort Inventory", "§7Sort your inventory.");
        addGUIItem(gui, 11, Material.ENDER_CHEST, "§5§lEnder Chest", "§7Access ender chest.");
        addGUIItem(gui, 12, Material.SHULKER_BOX, "§d§lShulker Box", "§7Access shulker box.");
        addGUIItem(gui, 13, Material.BUNDLE, "§6§lBundle", "§7Access bundle.");
        addGUIItem(gui, 14, Material.BARREL, "§7§lBarrel", "§7Access barrel.");
        addGUIItem(gui, 15, Material.CHEST, "§e§lChest", "§7Access chest.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to QoL menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the inventory menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openEnchantmentsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§c§lEnchantments");
        
        // Add enchantment features
        addGUIItem(gui, 10, Material.ENCHANTED_BOOK, "§c§lEnchant", "§7Enchant items.");
        addGUIItem(gui, 11, Material.ANVIL, "§7§lRepair", "§7Repair items.");
        addGUIItem(gui, 12, Material.GRINDSTONE, "§8§lDisenchant", "§7Remove enchantments.");
        addGUIItem(gui, 13, Material.ENCHANTING_TABLE, "§d§lEnchanting Table", "§7Use enchanting table.");
        addGUIItem(gui, 14, Material.BOOKSHELF, "§6§lBookshelf", "§7Manage bookshelves.");
        addGUIItem(gui, 15, Material.EXPERIENCE_BOTTLE, "§a§lExperience", "§7Manage experience.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to QoL menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the enchantments menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openRepairGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§7§lRepair & Maintenance");
        
        // Add repair features
        addGUIItem(gui, 10, Material.ANVIL, "§7§lAnvil", "§7Use anvil for repair.");
        addGUIItem(gui, 11, Material.GRINDSTONE, "§8§lGrindstone", "§7Use grindstone for repair.");
        addGUIItem(gui, 12, Material.CRAFTING_TABLE, "§6§lCrafting Table", "§7Use crafting table.");
        addGUIItem(gui, 13, Material.SMITHING_TABLE, "§7§lSmithing Table", "§7Use smithing table.");
        addGUIItem(gui, 14, Material.CARTOGRAPHY_TABLE, "§b§lCartography Table", "§7Use cartography table.");
        addGUIItem(gui, 15, Material.FLETCHING_TABLE, "§e§lFletching Table", "§7Use fletching table.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to QoL menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the repair menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openEconomyGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§a§lEconomy & Trading");
        
        // Add economy features
        addGUIItem(gui, 10, Material.EMERALD, "§a§lBalance", "§7Check your balance.");
        addGUIItem(gui, 11, Material.GOLD_INGOT, "§6§lPay", "§7Pay another player.");
        addGUIItem(gui, 12, Material.DIAMOND, "§b§lTrade", "§7Trade with another player.");
        addGUIItem(gui, 13, Material.EMERALD_BLOCK, "§a§lBank", "§7Access your bank.");
        addGUIItem(gui, 14, Material.GOLD_BLOCK, "§6§lShop", "§7Access the shop.");
        addGUIItem(gui, 15, Material.DIAMOND_BLOCK, "§b§lMarket", "§7Access the market.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to QoL menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the economy menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openInformationGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§f§lInformation & Statistics");
        
        // Add information features
        addGUIItem(gui, 10, Material.BOOK, "§f§lPlayer Info", "§7View player information.");
        addGUIItem(gui, 11, Material.MAP, "§a§lWorld Info", "§7View world information.");
        addGUIItem(gui, 12, Material.CLOCK, "§6§lTime Info", "§7View time information.");
        addGUIItem(gui, 13, Material.COMPASS, "§e§lLocation Info", "§7View location information.");
        addGUIItem(gui, 14, Material.ENDER_PEARL, "§5§lServer Info", "§7View server information.");
        addGUIItem(gui, 15, Material.NETHER_STAR, "§c§lPlugin Info", "§7View plugin information.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to QoL menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the information menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(description));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private void applySavedEffects(Player player, PlayerQoLData data) {
        // Apply saved potion effects
        for (Map.Entry<PotionEffectType, Integer> entry : data.getSavedEffects().entrySet()) {
            player.addPotionEffect(new PotionEffect(entry.getKey(), Integer.MAX_VALUE, entry.getValue()));
        }
    }
    
    private void saveCurrentEffects(Player player, PlayerQoLData data) {
        // Save current potion effects
        data.getSavedEffects().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            data.getSavedEffects().put(effect.getType(), effect.getAmplifier());
        }
    }
    
    public PlayerQoLData getPlayerQoLData(UUID playerId) {
        return playerQoLData.computeIfAbsent(playerId, k -> new PlayerQoLData(playerId));
    }
    
    public static class PlayerQoLData {
        private final UUID playerId;
        private final Map<PotionEffectType, Integer> savedEffects = new HashMap<>();
        private long lastUpdate;
        
        public PlayerQoLData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public Map<PotionEffectType, Integer> getSavedEffects() {
            return savedEffects;
        }
        
        public long getLastUpdate() {
            return lastUpdate;
        }
    }
}
