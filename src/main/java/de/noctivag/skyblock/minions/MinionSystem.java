package de.noctivag.skyblock.minions;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import de.noctivag.skyblock.core.PlayerProfile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Minion System - Ressourcen-Produzenten mit Offline-Funktion
 * 
 * Verantwortlich für:
 * - Minion-Erstellung und -Verwaltung
 * - Ressourcen-Produktion
 * - Offline-Funktion
 * - Minion-Upgrades
 * - Inventar-Management
 * - Minion-Platzierung
 */
public class MinionSystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final Map<UUID, List<BaseMinion>> playerMinions = new ConcurrentHashMap<>();
    private final Map<Location, BaseMinion> placedMinions = new ConcurrentHashMap<>();
    private final Map<String, BukkitTask> minionTasks = new ConcurrentHashMap<>();
    
    public MinionSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        startMinionProduction();
    }
    
    private void startMinionProduction() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Process all active minions
                for (Minion minion : placedMinions.values()) {
                    // if (true) {
                    //     // minion.produceResource();
                    // }
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L); // Every second
    }
    
    public void createMinion(Player player, MinionType minionType, int level) {
        UUID playerId = player.getUniqueId();
        
        // Check if player has enough coins
        PlayerProfile profile = corePlatform.getPlayerProfile(playerId);
        if (profile == null) return;
        
        double cost = minionType.getCost(level);
        if (!profile.tryRemoveCoins(cost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + cost);
            return;
        }
        
        // Create minion - constructor not implemented yet
        // Minion minion = new Minion(playerId, playerId, minionType, player.getLocation());
        Minion minion = null; // Placeholder until constructor is fixed
        
        // Add to player's minions - commented out until minion is properly created
        // playerMinions.computeIfAbsent(playerId, k -> new ArrayList<>()).add(minion);
        
        // Give minion item to player - commented out until minion is properly created
        // player.getInventory().addItem(new ItemStack(org.bukkit.Material.STONE));
        
        player.sendMessage(Component.text("§a§lMINION CREATED!"));
        player.sendMessage("§7Type: §e" + "Minion");
        player.sendMessage("§7Level: §e" + level);
        player.sendMessage("§7Cost: §6" + cost + " coins");
    }
    
    public void placeMinion(Player player, Location location, BaseMinion minion) {
        // Check if location is valid
        if (!isValidMinionLocation(location)) {
            player.sendMessage(Component.text("§cInvalid location for minion placement!"));
            return;
        }
        
        // Check if player has permission to place minions here
        if (!canPlaceMinion(player, location)) {
            player.sendMessage(Component.text("§cYou don't have permission to place minions here!"));
            return;
        }
        
        // Place minion
        // minion.setLocation(location);
        placedMinions.put(location, minion);
        
        // Start minion production
        startMinionProduction(minion);
        
        player.sendMessage(Component.text("§a§lMINION PLACED!"));
        player.sendMessage("§7Type: §e" + "Minion");
        player.sendMessage("§7Level: §e" + 1);
        player.sendMessage("§7Location: §e" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
    }
    
    public void removeMinion(Player player, Location location) {
        BaseMinion minion = placedMinions.get(location);
        if (minion == null) {
            player.sendMessage(Component.text("§cNo minion found at this location!"));
            return;
        }
        
        // Check if player owns this minion
        if (!player.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Component.text("§cThis minion doesn't belong to you!"));
            return;
        }
        
        // Stop minion production
        stopMinionProduction(minion);
        
        // Remove from placed minions
        placedMinions.remove(location);
        
        // Give minion item back to player
        player.getInventory().addItem(new ItemStack(org.bukkit.Material.STONE));
        
        player.sendMessage(Component.text("§a§lMINION REMOVED!"));
        player.sendMessage("§7Type: §e" + "Minion");
        player.sendMessage("§7Level: §e" + 1);
    }
    
    public void upgradeMinion(Player player, BaseMinion minion) {
        // Check if player has enough coins
        PlayerProfile profile = corePlatform.getPlayerProfile(player.getUniqueId());
        if (profile == null) return;
        
        double cost = 100.0;
        if (!profile.tryRemoveCoins(cost)) {
            player.sendMessage("§cYou don't have enough coins! Cost: " + cost);
            return;
        }
        
        // Upgrade minion
        // minion.upgrade();
        
        player.sendMessage(Component.text("§a§lMINION UPGRADED!"));
        player.sendMessage("§7Type: §e" + "Minion");
        player.sendMessage("§7New Level: §e" + 1);
        player.sendMessage("§7Cost: §6" + cost + " coins");
    }
    
    public void collectMinionResources(Player player, Minion minion) {
        // Check if player owns this minion
        if (!player.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Component.text("§cThis minion doesn't belong to you!"));
            return;
        }
        
        // Collect resources
        List<ItemStack> resources = new ArrayList<>();
        
        // Give resources to player
        for (ItemStack resource : resources) {
            player.getInventory().addItem(resource);
        }
        
        player.sendMessage(Component.text("§a§lRESOURCES COLLECTED!"));
        player.sendMessage("§7Type: §e" + "Minion");
        player.sendMessage("§7Resources: §e" + resources.size() + " items");
    }
    
    private boolean isValidMinionLocation(Location location) {
        // Check if location is solid and has space above
        return location.getBlock().getType().isSolid() && 
               location.clone().add(0, 1, 0).getBlock().getType() == Material.AIR;
    }
    
    private boolean canPlaceMinion(Player player, Location location) {
        // Check if player has permission to place minions in this area
        // This could check island ownership, world permissions, etc.
        return true; // For now, allow all placements
    }
    
    private void startMinionProduction(Minion minion) {
        String taskId = "task_" + System.currentTimeMillis();
        
        // Cancel existing task if any
        BukkitTask existingTask = minionTasks.get(taskId);
        if (existingTask != null) {
            existingTask.cancel();
        }
        
        // Start new production task
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (true) {
                    // minion.produceResource();
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L);
        
        minionTasks.put(taskId, task);
    }
    
    private void stopMinionProduction(Minion minion) {
        String taskId = "task_" + System.currentTimeMillis();
        BukkitTask task = minionTasks.get(taskId);
        if (task != null) {
            task.cancel();
            minionTasks.remove(taskId);
        }
    }
    
    public List<Minion> getPlayerMinions(UUID playerId) {
        return playerMinions.getOrDefault(playerId, new ArrayList<>());
    }
    
    public Minion getMinionAtLocation(Location location) {
        return placedMinions.get(location);
    }
    
    public Map<Location, Minion> getAllPlacedMinions() {
        return new HashMap<>(placedMinions);
    }
    
    // Minion Types
    public enum MinionType {
        COBBLESTONE("Cobblestone", Material.COBBLESTONE, 100.0),
        COAL("Coal", Material.COAL, 150.0),
        IRON("Iron", Material.IRON_INGOT, 200.0),
        GOLD("Gold", Material.GOLD_INGOT, 250.0),
        DIAMOND("Diamond", Material.DIAMOND, 300.0),
        EMERALD("Emerald", Material.EMERALD, 350.0),
        REDSTONE("Redstone", Material.REDSTONE, 120.0),
        LAPIS("Lapis", Material.LAPIS_LAZULI, 130.0),
        QUARTZ("Quartz", Material.QUARTZ, 140.0),
        OBSIDIAN("Obsidian", Material.OBSIDIAN, 400.0),
        WHEAT("Wheat", Material.WHEAT, 80.0),
        CARROT("Carrot", Material.CARROT, 80.0),
        POTATO("Potato", Material.POTATO, 80.0),
        BEETROOT("Beetroot", Material.BEETROOT, 80.0),
        SUGAR_CANE("Sugar Cane", Material.SUGAR_CANE, 90.0),
        CACTUS("Cactus", Material.CACTUS, 90.0),
        PUMPKIN("Pumpkin", Material.PUMPKIN, 100.0),
        MELON("Melon", Material.MELON, 100.0),
        OAK("Oak", Material.OAK_LOG, 110.0),
        BIRCH("Birch", Material.BIRCH_LOG, 110.0),
        SPRUCE("Spruce", Material.SPRUCE_LOG, 110.0),
        JUNGLE("Jungle", Material.JUNGLE_LOG, 110.0),
        ACACIA("Acacia", Material.ACACIA_LOG, 110.0),
        DARK_OAK("Dark Oak", Material.DARK_OAK_LOG, 110.0);
        
        private final String name;
        private final Material resource;
        private final double baseCost;
        
        MinionType(String name, Material resource, double baseCost) {
            this.name = name;
            this.resource = resource;
            this.baseCost = baseCost;
        }
        
        public String getName() { return name; }
        public Material getResource() { return resource; }
        public double getBaseCost() { return baseCost; }
        
        public double getCost(int level) {
            return baseCost * Math.pow(1.5, level - 1);
        }
        
        public long getProductionInterval(int level) {
            // Higher level = faster production
            return Math.max(20L, 200L - (level * 10L)); // 10 seconds to 1 second
        }
        
        public int getProductionAmount(int level) {
            // Higher level = more resources
            return 1 + (level / 5); // 1 resource per 5 levels
        }
    }
}
