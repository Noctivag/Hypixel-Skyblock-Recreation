package de.noctivag.plugin.minions;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Minion - Einzelner Minion mit Produktionslogik
 *
 * Verantwortlich für:
 * - Ressourcen-Produktion
 * - Inventar-Management
 * - Upgrade-System
 * - Offline-Funktion
 * - Produktions-Timer
 */
public class Minion {
    private final UUID ownerId;
    private final MinionSystem.MinionType type;
    private int level;
    private Location location;
    private boolean isActive;
    private final Queue<ItemStack> inventory = new ConcurrentLinkedQueue<>();
    private final int maxInventorySize = 64;
    private long lastProductionTime;
    private long totalProduced;
    private final Map<String, Object> upgrades = new HashMap<>();

    public Minion(UUID ownerId, MinionSystem.MinionType type, int level) {
        this.ownerId = ownerId;
        this.type = type;
        this.level = level;
        this.isActive = false;
        this.lastProductionTime = System.currentTimeMillis();
        this.totalProduced = 0;

        initializeUpgrades();
    }

    private void initializeUpgrades() {
        upgrades.put("speed", 0);
        upgrades.put("storage", 0);
        upgrades.put("fuel", 0);
        upgrades.put("auto_sell", false);
        upgrades.put("compactor", false);
        upgrades.put("super_compactor", false);
    }

    public void produceResource() {
        if (!isActive || location == null) return;

        long currentTime = System.currentTimeMillis();
        long productionInterval = getProductionInterval();

        if (currentTime - lastProductionTime >= productionInterval) {
            // Check if inventory has space
            if (inventory.size() >= maxInventorySize) {
                return; // Inventory full
            }

            // Produce resource
            ItemStack resource = new ItemStack(type.getResource(), getProductionAmount());

            // Apply upgrades before adding to inventory
            applyUpgrades(resource);

            inventory.offer(resource);

            lastProductionTime = currentTime;
            totalProduced++;

            // Auto-sell if enabled
            if (hasUpgrade("auto_sell")) {
                autoSellResources();
            }
        }
    }

    private void applyUpgrades(ItemStack resource) {
        // Apply compactor upgrade
        if (hasUpgrade("compactor")) {
            compactResource(resource);
        }

        // Apply super compactor upgrade
        if (hasUpgrade("super_compactor")) {
            superCompactResource(resource);
        }
    }

    private void autoSellResources() {
        // Auto-sell resources when inventory is full or periodically
        if (inventory.size() >= maxInventorySize * 0.8) { // Sell at 80% capacity
            sellAllResources();
        }
    }

    public void sellAllResources() {
        double totalValue = 0.0;
        Map<String, Integer> soldResources = new HashMap<>();

        while (!inventory.isEmpty()) {
            ItemStack item = inventory.poll();
            if (item != null) {
                String resourceName = item.getType().name();
                int amount = item.getAmount();
                double price = getResourcePrice(item.getType());

                totalValue += amount * price;
                soldResources.put(resourceName, soldResources.getOrDefault(resourceName, 0) + amount);
            }
        }

        // Store sold resources data for economy integration
        storeSoldResources(soldResources, totalValue);
    }

    private void storeSoldResources(Map<String, Integer> resources, double totalValue) {
        // This will be integrated with the economy system
        // For now, just log the transaction
        // Replace System.out with the plugin logger to avoid direct stdout usage
        try {
            org.bukkit.plugin.java.JavaPlugin.getPlugin(de.noctivag.plugin.Plugin.class)
                    .getLogger().info("Minion " + type.getName() + " sold resources: " + resources + " for " + totalValue + " coins");
        } catch (Exception ex) {
            // Fallback: if plugin instance isn't available for some reason, log to standard java logger
            java.util.logging.Logger.getLogger(Minion.class.getName()).info("Minion " + type.getName() + " sold resources: " + resources + " for " + totalValue + " coins");
        }
    }

    private double getResourcePrice(Material material) {
        return switch (material) {
            case COBBLESTONE -> 1.0;
            case STONE -> 2.0;
            case IRON_INGOT -> 5.0;
            case GOLD_INGOT -> 10.0;
            case DIAMOND -> 50.0;
            case EMERALD -> 100.0;
            case WHEAT -> 1.0;
            case CARROT -> 1.5;
            case POTATO -> 1.2;
            case OAK_LOG -> 2.0;
            case BIRCH_LOG -> 2.5;
            case SPRUCE_LOG -> 2.2;
            case JUNGLE_LOG -> 3.0;
            case ACACIA_LOG -> 2.8;
            case DARK_OAK_LOG -> 3.5;
            default -> 1.0;
        };
    }

    private void compactResource(ItemStack resource) {
        // Convert 9 items to 1 block
        if (resource.getAmount() >= 9) {
            Material compacted = getCompactedMaterial(resource.getType());
            if (compacted != null) {
                resource.setType(compacted);
                resource.setAmount(resource.getAmount() / 9);
            }
        }
    }

    private void superCompactResource(ItemStack resource) {
        // Convert 9 blocks to 1 super block
        if (resource.getAmount() >= 9) {
            Material superCompacted = getSuperCompactedMaterial(resource.getType());
            if (superCompacted != null) {
                resource.setType(superCompacted);
                resource.setAmount(resource.getAmount() / 9);
            }
        }
    }

    private Material getCompactedMaterial(Material material) {
        return switch (material) {
            case COBBLESTONE -> Material.COBBLESTONE;
            case COAL -> Material.COAL_BLOCK;
            case IRON_INGOT -> Material.IRON_BLOCK;
            case GOLD_INGOT -> Material.GOLD_BLOCK;
            case DIAMOND -> Material.DIAMOND_BLOCK;
            case EMERALD -> Material.EMERALD_BLOCK;
            case REDSTONE -> Material.REDSTONE_BLOCK;
            case LAPIS_LAZULI -> Material.LAPIS_BLOCK;
            case QUARTZ -> Material.QUARTZ_BLOCK;
            case OBSIDIAN -> Material.OBSIDIAN;
            case WHEAT -> Material.HAY_BLOCK;
            case CARROT -> Material.CARROT;
            case POTATO -> Material.POTATO;
            case BEETROOT -> Material.BEETROOT;
            case SUGAR_CANE -> Material.SUGAR_CANE;
            case CACTUS -> Material.CACTUS;
            case PUMPKIN -> Material.PUMPKIN;
            case MELON -> Material.MELON;
            case OAK_LOG -> Material.OAK_LOG;
            case BIRCH_LOG -> Material.BIRCH_LOG;
            case SPRUCE_LOG -> Material.SPRUCE_LOG;
            case JUNGLE_LOG -> Material.JUNGLE_LOG;
            case ACACIA_LOG -> Material.ACACIA_LOG;
            case DARK_OAK_LOG -> Material.DARK_OAK_LOG;
            default -> null;
        };
    }

    private Material getSuperCompactedMaterial(Material material) {
        return switch (material) {
            case COBBLESTONE -> Material.COBBLESTONE;
            case COAL_BLOCK -> Material.COAL_BLOCK;
            case IRON_BLOCK -> Material.IRON_BLOCK;
            case GOLD_BLOCK -> Material.GOLD_BLOCK;
            case DIAMOND_BLOCK -> Material.DIAMOND_BLOCK;
            case EMERALD_BLOCK -> Material.EMERALD_BLOCK;
            case REDSTONE_BLOCK -> Material.REDSTONE_BLOCK;
            case LAPIS_BLOCK -> Material.LAPIS_BLOCK;
            case QUARTZ_BLOCK -> Material.QUARTZ_BLOCK;
            case OBSIDIAN -> Material.OBSIDIAN;
            case HAY_BLOCK -> Material.HAY_BLOCK;
            case CARROT -> Material.CARROT;
            case POTATO -> Material.POTATO;
            case BEETROOT -> Material.BEETROOT;
            case SUGAR_CANE -> Material.SUGAR_CANE;
            case CACTUS -> Material.CACTUS;
            case PUMPKIN -> Material.PUMPKIN;
            case MELON -> Material.MELON;
            case OAK_LOG -> Material.OAK_LOG;
            case BIRCH_LOG -> Material.BIRCH_LOG;
            case SPRUCE_LOG -> Material.SPRUCE_LOG;
            case JUNGLE_LOG -> Material.JUNGLE_LOG;
            case ACACIA_LOG -> Material.ACACIA_LOG;
            case DARK_OAK_LOG -> Material.DARK_OAK_LOG;
            default -> null;
        };
    }

    public List<ItemStack> collectResources() {
        List<ItemStack> resources = new ArrayList<>();

        while (!inventory.isEmpty()) {
            ItemStack resource = inventory.poll();
            if (resource != null) {
                resources.add(resource);
            }
        }

        return resources;
    }

    public void upgrade() {
        level++;

        // Apply level-based upgrades
        if (level % 5 == 0) {
            // Every 5 levels, increase storage
            upgrades.put("storage", (Integer) upgrades.get("storage") + 1);
        }

        if (level % 10 == 0) {
            // Every 10 levels, increase speed
            upgrades.put("speed", (Integer) upgrades.get("speed") + 1);
        }
    }

    public void addUpgrade(String upgradeType, Object value) {
        upgrades.put(upgradeType, value);
    }

    public boolean hasUpgrade(String upgradeType) {
        Object value = upgrades.get(upgradeType);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Integer) {
            return (Integer) value > 0;
        }
        return false;
    }

    public int getUpgradeLevel(String upgradeType) {
        Object value = upgrades.get(upgradeType);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return 0;
    }

    public long getProductionInterval() {
        long baseInterval = type.getProductionInterval(level);

        // Apply speed upgrades
        int speedUpgrade = getUpgradeLevel("speed");
        baseInterval = Math.max(20L, baseInterval - (speedUpgrade * 10L));

        return baseInterval;
    }

    public int getProductionAmount() {
        int baseAmount = type.getProductionAmount(level);

        // Apply upgrades
        int speedUpgrade = getUpgradeLevel("speed");
        baseAmount += speedUpgrade;

        return baseAmount;
    }

    public int getMaxInventorySize() {
        int baseSize = maxInventorySize;

        // Apply storage upgrades
        int storageUpgrade = getUpgradeLevel("storage");
        baseSize += storageUpgrade * 32;

        return baseSize;
    }

    public ItemStack createMinionItem() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text("§6§l" + type.getName() + " Minion"));

            List<String> lore = new ArrayList<>();
            lore.add("§7Level: §e" + level);
            lore.add("§7Type: §e" + type.getName());
            lore.add("§7Production: §e" + getProductionAmount() + " items");
            lore.add("§7Interval: §e" + (getProductionInterval() / 20) + " seconds");
            lore.add("§7Inventory: §e" + inventory.size() + "/" + getMaxInventorySize());
            lore.add("§7Total Produced: §e" + totalProduced);
            lore.add("");
            lore.add("§7Upgrades:");
            lore.add("§7• Speed: §e" + getUpgradeLevel("speed"));
            lore.add("§7• Storage: §e" + getUpgradeLevel("storage"));
            lore.add("§7• Fuel: §e" + getUpgradeLevel("fuel"));
            lore.add("§7• Auto Sell: §e" + (hasUpgrade("auto_sell") ? "Yes" : "No"));
            lore.add("§7• Compactor: §e" + (hasUpgrade("compactor") ? "Yes" : "No"));
            lore.add("§7• Super Compactor: §e" + (hasUpgrade("super_compactor") ? "Yes" : "No"));
            lore.add("");
            lore.add("§eRight-click to place!");

            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }

        return item;
    }

    public String getTaskId() {
        return ownerId.toString() + "_" + type.name() + "_" + level;
    }

    // Getters and Setters
    public UUID getOwnerId() { return ownerId; }
    public MinionSystem.MinionType getType() { return type; }
    public int getLevel() { return level; }
    public Location getLocation() { return location; }
    public boolean isActive() { return isActive; }
    public Queue<ItemStack> getInventory() { return new ConcurrentLinkedQueue<>(inventory); }
    public long getLastProductionTime() { return lastProductionTime; }
    public long getTotalProduced() { return totalProduced; }
    public Map<String, Object> getUpgrades() { return new HashMap<>(upgrades); }

    public void setLocation(Location location) { this.location = location; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setLevel(int level) { this.level = level; }

    public String getName() {
        return type.getName() + " Minion";
    }
}
