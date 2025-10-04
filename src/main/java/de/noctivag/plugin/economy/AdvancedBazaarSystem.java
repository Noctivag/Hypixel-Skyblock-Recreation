package de.noctivag.plugin.economy;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Bazaar System - Hypixel Skyblock Style
 */
public class AdvancedBazaarSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<Material, BazaarItem> bazaarItems = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> bazaarTasks = new ConcurrentHashMap<>();
    
    public AdvancedBazaarSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeBazaarItems();
        startBazaarUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeBazaarItems() {
        // Initialize bazaar items with default prices
        bazaarItems.put(Material.COBBLESTONE, new BazaarItem(Material.COBBLESTONE, 1.0, 0.5, 1.5));
        bazaarItems.put(Material.COAL, new BazaarItem(Material.COAL, 2.0, 1.0, 3.0));
        bazaarItems.put(Material.IRON_INGOT, new BazaarItem(Material.IRON_INGOT, 5.0, 3.0, 7.0));
        bazaarItems.put(Material.GOLD_INGOT, new BazaarItem(Material.GOLD_INGOT, 10.0, 8.0, 12.0));
        bazaarItems.put(Material.DIAMOND, new BazaarItem(Material.DIAMOND, 50.0, 40.0, 60.0));
        bazaarItems.put(Material.EMERALD, new BazaarItem(Material.EMERALD, 30.0, 25.0, 35.0));
        bazaarItems.put(Material.LAPIS_LAZULI, new BazaarItem(Material.LAPIS_LAZULI, 3.0, 2.0, 4.0));
        bazaarItems.put(Material.REDSTONE, new BazaarItem(Material.REDSTONE, 2.0, 1.5, 2.5));
    }
    
    private void startBazaarUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<Material, BazaarItem> entry : bazaarItems.entrySet()) {
                    BazaarItem item = entry.getValue();
                    item.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
            openBazaarGUI(player);
        }
    }
    
    private void openBazaarGUI(Player player) {
        player.sendMessage("§aBazaar GUI geöffnet!");
    }
    
    public void buyItem(Player player, Material material, int amount) {
        BazaarItem item = bazaarItems.get(material);
        if (item == null) {
            player.sendMessage("§cItem nicht im Bazaar verfügbar!");
            return;
        }
        
        double totalCost = item.getBuyPrice() * amount;
        // Check if player has enough money
        // Give item to player
        // Take money from player
        
        player.sendMessage("§a" + amount + "x " + material.name() + " gekauft für " + totalCost + " Coins!");
    }
    
    public void sellItem(Player player, Material material, int amount) {
        BazaarItem item = bazaarItems.get(material);
        if (item == null) {
            player.sendMessage("§cItem nicht im Bazaar verfügbar!");
            return;
        }
        
        double totalValue = item.getSellPrice() * amount;
        // Check if player has enough items
        // Take items from player
        // Give money to player
        
        player.sendMessage("§a" + amount + "x " + material.name() + " verkauft für " + totalValue + " Coins!");
    }
    
    public BazaarItem getBazaarItem(Material material) {
        return bazaarItems.get(material);
    }
    
    public List<BazaarItem> getAllBazaarItems() {
        return new ArrayList<>(bazaarItems.values());
    }
    
    public static class BazaarItem {
        private final Material material;
        private double buyPrice;
        private double sellPrice;
        private double instantBuyPrice;
        private double instantSellPrice;
        private long lastUpdate;
        
        public BazaarItem(Material material, double buyPrice, double sellPrice, double instantBuyPrice) {
            this.material = material;
            this.buyPrice = buyPrice;
            this.sellPrice = sellPrice;
            this.instantBuyPrice = instantBuyPrice;
            this.instantSellPrice = sellPrice;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                updatePrices();
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void updatePrices() {
            // Simulate price fluctuations
            double fluctuation = (Math.random() - 0.5) * 0.1; // ±5% fluctuation
            buyPrice *= (1 + fluctuation);
            sellPrice *= (1 + fluctuation);
            instantBuyPrice *= (1 + fluctuation);
            instantSellPrice *= (1 + fluctuation);
            
            // Ensure sell price is always lower than buy price
            if (sellPrice >= buyPrice) {
                sellPrice = buyPrice * 0.9;
            }
        }
        
        private void saveToDatabase() {
            // Save bazaar item data to database
        }
        
        public Material getMaterial() { return material; }
        public double getBuyPrice() { return buyPrice; }
        public double getSellPrice() { return sellPrice; }
        public double getInstantBuyPrice() { return instantBuyPrice; }
        public double getInstantSellPrice() { return instantSellPrice; }
    }
}
