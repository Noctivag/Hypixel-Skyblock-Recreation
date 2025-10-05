package de.noctivag.skyblock.skyblock;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BlockRegenerationSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<Location, BlockData> blockRegeneration = new ConcurrentHashMap<>();
    private final Map<Location, BukkitTask> regenerationTasks = new ConcurrentHashMap<>();
    private final Map<Location, Long> blockBreakTimes = new ConcurrentHashMap<>();

    // Regeneration times for different areas (in ticks)
    private final Map<String, Long> areaRegenerationTimes = new HashMap<>();

    public BlockRegenerationSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        initializeRegenerationTimes();
        startBlockRegenerationTimer();
    }

    private void initializeRegenerationTimes() {
        // Set regeneration times for different areas (in ticks)
        // 20 ticks = 1 second
        areaRegenerationTimes.put("deep_caverns", 20L * 60L); // 1 minute
        areaRegenerationTimes.put("dwarven_mines", 20L * 90L); // 1.5 minutes
        areaRegenerationTimes.put("crystal_hollows", 20L * 120L); // 2 minutes
        areaRegenerationTimes.put("the_end", 20L * 150L); // 2.5 minutes
        areaRegenerationTimes.put("the_nether", 20L * 180L); // 3 minutes
        areaRegenerationTimes.put("overworld", 20L * 30L); // 30 seconds
        areaRegenerationTimes.put("default", 20L * 60L); // 1 minute default
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();

        // Check if player is in creative mode - allow them to break any block without regeneration
        if (player.getGameMode() == GameMode.CREATIVE) {
            return; // Allow creative mode players to break any block
        }

        // Check if block is in a mining area
        if (!isInMiningArea(location)) {
            return; // Block is not in a mining area, no regeneration needed
        }

        // Store original block data for regeneration
        BlockData blockData = new BlockData(block.getType(), block.getBlockData());
        blockRegeneration.put(location, blockData);
        blockBreakTimes.put(location, java.lang.System.currentTimeMillis());

        // Start regeneration timer
        startBlockRegeneration(location, blockData);

        // Send message to player
        player.sendMessage(Component.text("§a§lBLOCK BROKEN!"));
        player.sendMessage("§7Block: §e" + block.getType().name());
        player.sendMessage("§7Regeneration: §e" + getRegenerationTime(location) + " seconds");
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();

        // Check if player is in creative mode - allow them to place any block without regeneration
        if (player.getGameMode() == GameMode.CREATIVE) {
            return; // Allow creative mode players to place any block
        }

        // Check if block is in a mining area
        if (!isInMiningArea(location)) {
            return; // Block is not in a mining area, no regeneration needed
        }

        // Store original block data for regeneration
        BlockData blockData = new BlockData(block.getType(), block.getBlockData());
        blockRegeneration.put(location, blockData);
        blockBreakTimes.put(location, java.lang.System.currentTimeMillis());

        // Start regeneration timer
        startBlockRegeneration(location, blockData);

        // Send message to player
        player.sendMessage(Component.text("§a§lBLOCK PLACED!"));
        player.sendMessage("§7Block: §e" + block.getType().name());
        player.sendMessage("§7Regeneration: §e" + getRegenerationTime(location) + " seconds");
    }

    private void startBlockRegeneration(Location location, BlockData blockData) {
        // Cancel existing regeneration task if any
        BukkitTask existingTask = regenerationTasks.get(location);
        if (existingTask != null) {
            existingTask.cancel();
        }

        // Get regeneration time based on area
        long regenerationTime = getRegenerationTime(location);

        // Start regeneration task
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // Regenerate block
                Block block = location.getBlock();
                block.setType(blockData.getType());
                block.setBlockData(blockData.getBlockData());

                // Remove from tracking
                blockRegeneration.remove(location);
                regenerationTasks.remove(location);
                blockBreakTimes.remove(location);

                // Send message to nearby players
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getLocation().distance(location) <= 10) {
                        player.sendMessage(Component.text("§a§lBLOCK REGENERATED!"));
                        player.sendMessage("§7Block: §e" + blockData.getType().name());
                        player.sendMessage("§7Location: §e" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
                    }
                }
            }
        }.runTaskLater(SkyblockPlugin, regenerationTime);

        regenerationTasks.put(location, task);
    }

    private long getRegenerationTime(Location location) {
        // Get regeneration time based on area
        String area = getMiningArea(location);
        return areaRegenerationTimes.getOrDefault(area, areaRegenerationTimes.get("default"));
    }

    private String getMiningArea(Location location) {
        // Check which mining area the location is in
        if (isInArea(location, 0, 50, 0, 100, 100, 100)) {
            return "deep_caverns";
        } else if (isInArea(location, 200, 50, 200, 300, 100, 300)) {
            return "dwarven_mines";
        } else if (isInArea(location, 400, 50, 400, 500, 100, 500)) {
            return "crystal_hollows";
        } else if (isInArea(location, 600, 50, 600, 700, 100, 700)) {
            return "the_end";
        } else if (isInArea(location, 800, 50, 800, 900, 100, 900)) {
            return "the_nether";
        } else if (isInArea(location, 1000, 50, 1000, 1100, 100, 1100)) {
            return "overworld";
        }
        return "default";
    }

    private boolean isInArea(Location location, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return location.getX() >= minX && location.getX() <= maxX &&
               location.getY() >= minY && location.getY() <= maxY &&
               location.getZ() >= minZ && location.getZ() <= maxZ;
    }

    private boolean isInMiningArea(Location location) {
        return !getMiningArea(location).equals("default");
    }

    private void startBlockRegenerationTimer() {
        // Use virtual thread for Folia compatibility
        Thread.ofVirtual().start(() -> {
            try {
                while (SkyblockPlugin.isEnabled()) {
                    // Check for expired regeneration tasks
                    List<Location> expiredLocations = new ArrayList<>();
                    for (Map.Entry<Location, BukkitTask> entry : regenerationTasks.entrySet()) {
                        if (entry.getValue().isCancelled()) {
                            expiredLocations.add(entry.getKey());
                        }
                    }

                    // Clean up expired tasks
                    for (Location location : expiredLocations) {
                        regenerationTasks.remove(location);
                        blockRegeneration.remove(location);
                        blockBreakTimes.remove(location);
                    }

                    // Check for blocks that should regenerate soon
                    for (Map.Entry<Location, Long> entry : blockBreakTimes.entrySet()) {
                        Location location = entry.getKey();
                        long breakTime = entry.getValue();
                        long regenerationTime = getRegenerationTime(location);
                        long timeRemaining = regenerationTime - (java.lang.System.currentTimeMillis() - breakTime);

                        if (timeRemaining <= 5000 && timeRemaining > 0) { // 5 seconds before regeneration
                            // Send warning to nearby players
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getLocation().distance(location) <= 10) {
                                    player.sendMessage(Component.text("§e§lBLOCK REGENERATING SOON!"));
                                    player.sendMessage("§7Block: §e" + blockRegeneration.get(location).getType().name());
                                    player.sendMessage("§7Time remaining: §e" + (timeRemaining / 1000) + " seconds");
                                }
                            }
                        }
                    }
                    
                    Thread.sleep(1000); // Check every second = 1000 ms
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * Stops all block regeneration tasks and clears associated data.
     */
    public void stopAllTasks() {
        for (BukkitTask task : regenerationTasks.values()) {
            task.cancel();
        }
        regenerationTasks.clear();
        blockRegeneration.clear();
        blockBreakTimes.clear();
        SkyblockPlugin.getLogger().info("All block regeneration tasks have been stopped.");
    }

    public Map<Location, BlockData> getBlockRegeneration() {
        return new HashMap<>(blockRegeneration);
    }

    public Map<Location, BukkitTask> getRegenerationTasks() {
        return new HashMap<>(regenerationTasks);
    }

    public Map<Location, Long> getBlockBreakTimes() {
        return new HashMap<>(blockBreakTimes);
    }

    public static class BlockData {
        private final Material type;
        private final org.bukkit.block.data.BlockData blockData;

        public BlockData(Material type, org.bukkit.block.data.BlockData blockData) {
            this.type = type;
            this.blockData = blockData;
        }

        public Material getType() { return type; }
        public org.bukkit.block.data.BlockData getBlockData() { return blockData; }
    }
}
