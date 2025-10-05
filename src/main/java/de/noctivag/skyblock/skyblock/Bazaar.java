package de.noctivag.skyblock.skyblock;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Bazaar {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<Material, BazaarItem> items = new ConcurrentHashMap<>();
    private final Map<UUID, Map<Material, Integer>> playerOrders = new ConcurrentHashMap<>();
    
    public Bazaar(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        initializeBazaarItems();
        startPriceUpdateTimer();
    }
    
    private void initializeBazaarItems() {
        // Initialize common Skyblock items with starting prices
        addBazaarItem(Material.COBBLESTONE, 0.5, 0.6);
        addBazaarItem(Material.COAL, 2.0, 2.5);
        addBazaarItem(Material.IRON_INGOT, 5.0, 6.0);
        addBazaarItem(Material.GOLD_INGOT, 8.0, 10.0);
        addBazaarItem(Material.DIAMOND, 15.0, 18.0);
        addBazaarItem(Material.EMERALD, 12.0, 15.0);
        addBazaarItem(Material.REDSTONE, 1.0, 1.2);
        addBazaarItem(Material.LAPIS_LAZULI, 1.5, 2.0);
        addBazaarItem(Material.QUARTZ, 3.0, 4.0);
        addBazaarItem(Material.OBSIDIAN, 20.0, 25.0);
        addBazaarItem(Material.GLOWSTONE, 2.5, 3.0);
        addBazaarItem(Material.ENDER_PEARL, 10.0, 12.0);
        addBazaarItem(Material.BLAZE_ROD, 8.0, 10.0);
        addBazaarItem(Material.GHAST_TEAR, 15.0, 18.0);
        addBazaarItem(Material.SLIME_BALL, 3.0, 4.0);
        addBazaarItem(Material.MAGMA_CREAM, 5.0, 6.0);
        addBazaarItem(Material.BONE, 1.0, 1.5);
        addBazaarItem(Material.STRING, 0.8, 1.0);
        addBazaarItem(Material.SPIDER_EYE, 2.0, 2.5);
        addBazaarItem(Material.ROTTEN_FLESH, 0.5, 0.7);
        addBazaarItem(Material.ARROW, 0.3, 0.4);
        addBazaarItem(Material.FEATHER, 1.0, 1.2);
        addBazaarItem(Material.LEATHER, 2.0, 2.5);
        addBazaarItem(Material.PORKCHOP, 1.5, 2.0);
        addBazaarItem(Material.BEEF, 1.5, 2.0);
        addBazaarItem(Material.CHICKEN, 1.0, 1.5);
        addBazaarItem(Material.MUTTON, 1.5, 2.0);
        addBazaarItem(Material.RABBIT, 1.0, 1.5);
        addBazaarItem(Material.WHEAT, 0.5, 0.7);
        addBazaarItem(Material.CARROT, 0.5, 0.7);
        addBazaarItem(Material.POTATO, 0.5, 0.7);
        addBazaarItem(Material.BEETROOT, 0.5, 0.7);
        addBazaarItem(Material.SUGAR_CANE, 1.0, 1.2);
        addBazaarItem(Material.CACTUS, 1.0, 1.2);
        addBazaarItem(Material.PUMPKIN, 2.0, 2.5);
        addBazaarItem(Material.MELON, 1.0, 1.2);
        addBazaarItem(Material.OAK_LOG, 1.0, 1.2);
        addBazaarItem(Material.BIRCH_LOG, 1.0, 1.2);
        addBazaarItem(Material.SPRUCE_LOG, 1.0, 1.2);
        addBazaarItem(Material.JUNGLE_LOG, 1.0, 1.2);
        addBazaarItem(Material.ACACIA_LOG, 1.0, 1.2);
        addBazaarItem(Material.DARK_OAK_LOG, 1.0, 1.2);
        addBazaarItem(Material.OAK_LEAVES, 0.2, 0.3);
        addBazaarItem(Material.BIRCH_LEAVES, 0.2, 0.3);
        addBazaarItem(Material.SPRUCE_LEAVES, 0.2, 0.3);
        addBazaarItem(Material.JUNGLE_LEAVES, 0.2, 0.3);
        addBazaarItem(Material.ACACIA_LEAVES, 0.2, 0.3);
        addBazaarItem(Material.DARK_OAK_LEAVES, 0.2, 0.3);
        addBazaarItem(Material.SAND, 0.3, 0.4);
        addBazaarItem(Material.GRAVEL, 0.3, 0.4);
        addBazaarItem(Material.CLAY, 1.0, 1.2);
        addBazaarItem(Material.SOUL_SAND, 2.0, 2.5);
        addBazaarItem(Material.NETHERRACK, 0.5, 0.7);
        addBazaarItem(Material.NETHER_BRICK, 1.0, 1.2);
        addBazaarItem(Material.END_STONE, 3.0, 4.0);
        addBazaarItem(Material.PRISMARINE_SHARD, 5.0, 6.0);
        addBazaarItem(Material.PRISMARINE_CRYSTALS, 8.0, 10.0);
        addBazaarItem(Material.SPONGE, 15.0, 18.0);
        addBazaarItem(Material.WET_SPONGE, 20.0, 25.0);
        addBazaarItem(Material.SEA_LANTERN, 12.0, 15.0);
        addBazaarItem(Material.KELP, 0.5, 0.7);
        addBazaarItem(Material.DRIED_KELP, 1.0, 1.2);
        addBazaarItem(Material.HEART_OF_THE_SEA, 50.0, 60.0);
        addBazaarItem(Material.NAUTILUS_SHELL, 25.0, 30.0);
        addBazaarItem(Material.TRIDENT, 100.0, 120.0);
        addBazaarItem(Material.PHANTOM_MEMBRANE, 8.0, 10.0);
        addBazaarItem(Material.ELYTRA, 200.0, 250.0);
        addBazaarItem(Material.DRAGON_HEAD, 500.0, 600.0);
        addBazaarItem(Material.DRAGON_EGG, 1000.0, 1200.0);
        addBazaarItem(Material.NETHER_STAR, 300.0, 350.0);
        addBazaarItem(Material.WITHER_SKELETON_SKULL, 150.0, 180.0);
        addBazaarItem(Material.WITHER_ROSE, 100.0, 120.0);
        addBazaarItem(Material.TOTEM_OF_UNDYING, 250.0, 300.0);
        addBazaarItem(Material.SHULKER_SHELL, 80.0, 100.0);
        addBazaarItem(Material.END_CRYSTAL, 200.0, 250.0);
        addBazaarItem(Material.ENDER_EYE, 15.0, 18.0);
        addBazaarItem(Material.ENDER_PEARL, 10.0, 12.0);
        addBazaarItem(Material.ENDER_CHEST, 50.0, 60.0);
        addBazaarItem(Material.ANVIL, 30.0, 35.0);
        addBazaarItem(Material.ENCHANTING_TABLE, 40.0, 50.0);
        addBazaarItem(Material.BREWING_STAND, 20.0, 25.0);
        addBazaarItem(Material.CAULDRON, 10.0, 12.0);
        addBazaarItem(Material.HOPPER, 25.0, 30.0);
        addBazaarItem(Material.DROPPER, 15.0, 18.0);
        addBazaarItem(Material.DISPENSER, 20.0, 25.0);
        addBazaarItem(Material.PISTON, 12.0, 15.0);
        addBazaarItem(Material.STICKY_PISTON, 18.0, 22.0);
        addBazaarItem(Material.REDSTONE, 1.0, 1.2);
        addBazaarItem(Material.REDSTONE_TORCH, 2.0, 2.5);
        addBazaarItem(Material.REDSTONE_LAMP, 8.0, 10.0);
        addBazaarItem(Material.REDSTONE_BLOCK, 9.0, 11.0);
        addBazaarItem(Material.REPEATER, 3.0, 4.0);
        addBazaarItem(Material.COMPARATOR, 5.0, 6.0);
        addBazaarItem(Material.OBSERVER, 15.0, 18.0);
        addBazaarItem(Material.DAYLIGHT_DETECTOR, 10.0, 12.0);
        addBazaarItem(Material.TRIPWIRE_HOOK, 2.0, 2.5);
        addBazaarItem(Material.TRIPWIRE, 0.5, 0.7);
        addBazaarItem(Material.LEVER, 1.0, 1.2);
        addBazaarItem(Material.STONE_BUTTON, 0.5, 0.7);
        addBazaarItem(Material.OAK_BUTTON, 0.5, 0.7);
        addBazaarItem(Material.STONE_PRESSURE_PLATE, 1.0, 1.2);
        addBazaarItem(Material.OAK_PRESSURE_PLATE, 1.0, 1.2);
        addBazaarItem(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, 2.0, 2.5);
        addBazaarItem(Material.LIGHT_WEIGHTED_PRESSURE_PLATE, 2.0, 2.5);
        addBazaarItem(Material.TARGET, 8.0, 10.0);
        addBazaarItem(Material.LECTERN, 15.0, 18.0);
        addBazaarItem(Material.JUKEBOX, 20.0, 25.0);
        addBazaarItem(Material.NOTE_BLOCK, 5.0, 6.0);
        addBazaarItem(Material.MUSIC_DISC_13, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_CAT, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_BLOCKS, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_CHIRP, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_FAR, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_MALL, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_MELLOHI, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_STAL, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_STRAD, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_WARD, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_11, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_WAIT, 50.0, 60.0);
        addBazaarItem(Material.MUSIC_DISC_PIGSTEP, 100.0, 120.0);
        addBazaarItem(Material.MUSIC_DISC_OTHERSIDE, 100.0, 120.0);
        addBazaarItem(Material.MUSIC_DISC_5, 100.0, 120.0);
        addBazaarItem(Material.MUSIC_DISC_RELIC, 100.0, 120.0);
    }
    
    private void addBazaarItem(Material material, double buyPrice, double sellPrice) {
        items.put(material, new BazaarItem(material, buyPrice, sellPrice));
    }
    
    private void startPriceUpdateTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Update prices based on supply and demand
                for (BazaarItem item : items.values()) {
                    item.updatePrices();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20 * 60 * 5L); // Update every 5 minutes
    }
    
    public boolean buyItem(Player player, Material material, int amount) {
        BazaarItem item = items.get(material);
        if (item == null) return false;
        
        double totalCost = item.getBuyPrice() * amount;
        if (!SkyblockPlugin.getEconomyManager().hasBalance(player, totalCost)) return false;
        
        SkyblockPlugin.getEconomyManager().withdrawMoney(player, totalCost);
        player.getInventory().addItem(new ItemStack(material, amount));
        
        // Update supply/demand
        item.addBuyOrder(amount);
        
        player.sendMessage(Component.text("§a§lITEM PURCHASED!"));
        player.sendMessage("§7Item: §e" + material.name());
        player.sendMessage("§7Amount: §e" + amount);
        player.sendMessage("§7Total Cost: §6" + SkyblockPlugin.getEconomyManager().formatMoney(totalCost));
        
        return true;
    }
    
    public boolean sellItem(Player player, Material material, int amount) {
        BazaarItem item = items.get(material);
        if (item == null) return false;
        
        if (!player.getInventory().contains(material, amount)) return false;
        
        double totalEarnings = item.getSellPrice() * amount;
        SkyblockPlugin.getEconomyManager().giveMoney(player, totalEarnings);
        
        // Remove items from inventory
        ItemStack toRemove = new ItemStack(material, amount);
        player.getInventory().removeItem(toRemove);
        
        // Update supply/demand
        item.addSellOrder(amount);
        
        player.sendMessage(Component.text("§a§lITEM SOLD!"));
        player.sendMessage("§7Item: §e" + material.name());
        player.sendMessage("§7Amount: §e" + amount);
        player.sendMessage("§7Total Earnings: §6" + SkyblockPlugin.getEconomyManager().formatMoney(totalEarnings));
        
        return true;
    }
    
    public BazaarItem getBazaarItem(Material material) {
        return items.get(material);
    }
    
    public Map<Material, BazaarItem> getAllBazaarItems() {
        return new HashMap<>(items);
    }
    
    public static class BazaarItem {
        private final Material material;
        private double buyPrice;
        private double sellPrice;
        private int buyOrders;
        private int sellOrders;
        private long lastUpdate;
        
        public BazaarItem(Material material, double buyPrice, double sellPrice) {
            this.material = material;
            this.buyPrice = buyPrice;
            this.sellPrice = sellPrice;
            this.buyOrders = 0;
            this.sellOrders = 0;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void updatePrices() {
            long currentTime = java.lang.System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            // Update prices based on supply and demand
            if (timeDiff >= 300000) { // 5 minutes
                double demandFactor = 1.0 + (buyOrders * 0.01);
                double supplyFactor = 1.0 - (sellOrders * 0.01);
                
                buyPrice *= demandFactor;
                sellPrice *= supplyFactor;
                
                // Reset orders
                buyOrders = 0;
                sellOrders = 0;
                lastUpdate = currentTime;
            }
        }
        
        public void addBuyOrder(int amount) {
            buyOrders += amount;
        }
        
        public void addSellOrder(int amount) {
            sellOrders += amount;
        }
        
        // Getters
        public Material getMaterial() { return material; }
        public double getBuyPrice() { return buyPrice; }
        public double getSellPrice() { return sellPrice; }
        public int getBuyOrders() { return buyOrders; }
        public int getSellOrders() { return sellOrders; }
    }
}
