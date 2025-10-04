package de.noctivag.plugin.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MiningAreaSystem implements Listener {
    private final Plugin plugin;
    private final Map<String, MiningArea> miningAreas = new HashMap<>();
    private final Map<UUID, String> playerCurrentArea = new ConcurrentHashMap<>();
    
    public MiningAreaSystem(Plugin plugin) {
        this.plugin = plugin;
        initializeMiningAreas();
        startAreaCheckTimer();
    }
    
    private void initializeMiningAreas() {
        // Create different mining areas like in Hypixel Skyblock
        
        // Deep Caverns - Beginner area
        MiningArea deepCaverns = new MiningArea("deep_caverns", "Deep Caverns", 
            new Location(Bukkit.getWorlds().get(0), 0, 50, 0),
            new Location(Bukkit.getWorlds().get(0), 100, 100, 100));
        deepCaverns.addAllowedBlock(Material.COBBLESTONE, 1, 0, 1);
        deepCaverns.addAllowedBlock(Material.COAL_ORE, 2, 1, 1);
        deepCaverns.addAllowedBlock(Material.IRON_ORE, 3, 2, 1);
        deepCaverns.addAllowedBlock(Material.GOLD_ORE, 4, 3, 1);
        deepCaverns.addAllowedBlock(Material.DIAMOND_ORE, 5, 4, 1);
        deepCaverns.addAllowedBlock(Material.EMERALD_ORE, 6, 5, 1);
        deepCaverns.addAllowedBlock(Material.REDSTONE_ORE, 2, 1, 1);
        deepCaverns.addAllowedBlock(Material.LAPIS_ORE, 3, 2, 1);
        deepCaverns.addAllowedBlock(Material.NETHER_QUARTZ_ORE, 4, 3, 1);
        deepCaverns.addAllowedBlock(Material.OBSIDIAN, 7, 6, 1);
        deepCaverns.setRegenerationTime(20L * 60L); // 1 minute
        miningAreas.put("deep_caverns", deepCaverns);
        
        // Dwarven Mines - Intermediate area
        MiningArea dwarvenMines = new MiningArea("dwarven_mines", "Dwarven Mines",
            new Location(Bukkit.getWorlds().get(0), 200, 50, 200),
            new Location(Bukkit.getWorlds().get(0), 300, 100, 300));
        dwarvenMines.addAllowedBlock(Material.STONE, 1, 0, 1);
        dwarvenMines.addAllowedBlock(Material.COAL_ORE, 2, 1, 1);
        dwarvenMines.addAllowedBlock(Material.IRON_ORE, 3, 2, 1);
        dwarvenMines.addAllowedBlock(Material.GOLD_ORE, 4, 3, 1);
        dwarvenMines.addAllowedBlock(Material.DIAMOND_ORE, 5, 4, 1);
        dwarvenMines.addAllowedBlock(Material.EMERALD_ORE, 6, 5, 1);
        dwarvenMines.addAllowedBlock(Material.REDSTONE_ORE, 2, 1, 1);
        dwarvenMines.addAllowedBlock(Material.LAPIS_ORE, 3, 2, 1);
        dwarvenMines.addAllowedBlock(Material.NETHER_QUARTZ_ORE, 4, 3, 1);
        dwarvenMines.addAllowedBlock(Material.OBSIDIAN, 7, 6, 1);
        dwarvenMines.addAllowedBlock(Material.ANCIENT_DEBRIS, 8, 7, 1);
        dwarvenMines.addAllowedBlock(Material.ANCIENT_DEBRIS, 9, 8, 1);
        dwarvenMines.setRegenerationTime(20L * 90L); // 1.5 minutes
        miningAreas.put("dwarven_mines", dwarvenMines);
        
        // Crystal Hollows - Advanced area
        MiningArea crystalHollows = new MiningArea("crystal_hollows", "Crystal Hollows",
            new Location(Bukkit.getWorlds().get(0), 400, 50, 400),
            new Location(Bukkit.getWorlds().get(0), 500, 100, 500));
        crystalHollows.addAllowedBlock(Material.END_STONE, 1, 0, 1);
        crystalHollows.addAllowedBlock(Material.END_STONE_BRICKS, 2, 1, 1);
        crystalHollows.addAllowedBlock(Material.PURPUR_BLOCK, 3, 2, 1);
        crystalHollows.addAllowedBlock(Material.PURPUR_PILLAR, 4, 3, 1);
        crystalHollows.addAllowedBlock(Material.END_ROD, 5, 4, 1);
        crystalHollows.addAllowedBlock(Material.END_CRYSTAL, 6, 5, 1);
        crystalHollows.addAllowedBlock(Material.DRAGON_EGG, 7, 6, 1);
        crystalHollows.addAllowedBlock(Material.ELYTRA, 8, 7, 1);
        crystalHollows.addAllowedBlock(Material.SHULKER_BOX, 9, 8, 1);
        crystalHollows.setRegenerationTime(20L * 120L); // 2 minutes
        miningAreas.put("crystal_hollows", crystalHollows);
        
        // The End - Expert area
        MiningArea theEnd = new MiningArea("the_end", "The End",
            new Location(Bukkit.getWorlds().get(0), 600, 50, 600),
            new Location(Bukkit.getWorlds().get(0), 700, 100, 700));
        theEnd.addAllowedBlock(Material.END_STONE, 1, 0, 1);
        theEnd.addAllowedBlock(Material.END_STONE_BRICKS, 2, 1, 1);
        theEnd.addAllowedBlock(Material.PURPUR_BLOCK, 3, 2, 1);
        theEnd.addAllowedBlock(Material.PURPUR_PILLAR, 4, 3, 1);
        theEnd.addAllowedBlock(Material.END_ROD, 5, 4, 1);
        theEnd.addAllowedBlock(Material.END_CRYSTAL, 6, 5, 1);
        theEnd.addAllowedBlock(Material.DRAGON_EGG, 7, 6, 1);
        theEnd.addAllowedBlock(Material.ELYTRA, 8, 7, 1);
        theEnd.addAllowedBlock(Material.SHULKER_BOX, 9, 8, 1);
        theEnd.addAllowedBlock(Material.ENDER_CHEST, 10, 9, 1);
        theEnd.setRegenerationTime(20L * 150L); // 2.5 minutes
        miningAreas.put("the_end", theEnd);
        
        // The Nether - Expert area
        MiningArea theNether = new MiningArea("the_nether", "The Nether",
            new Location(Bukkit.getWorlds().get(0), 800, 50, 800),
            new Location(Bukkit.getWorlds().get(0), 900, 100, 900));
        theNether.addAllowedBlock(Material.NETHERRACK, 1, 0, 1);
        theNether.addAllowedBlock(Material.NETHER_BRICK, 2, 1, 1);
        theNether.addAllowedBlock(Material.NETHER_BRICK_FENCE, 3, 2, 1);
        theNether.addAllowedBlock(Material.NETHER_BRICK_STAIRS, 4, 3, 1);
        theNether.addAllowedBlock(Material.NETHER_BRICK_SLAB, 5, 4, 1);
        theNether.addAllowedBlock(Material.NETHER_BRICK_WALL, 6, 5, 1);
        theNether.addAllowedBlock(Material.NETHER_WART_BLOCK, 7, 6, 1);
        theNether.addAllowedBlock(Material.WARPED_WART_BLOCK, 8, 7, 1);
        theNether.addAllowedBlock(Material.CRIMSON_NYLIUM, 9, 8, 1);
        theNether.addAllowedBlock(Material.WARPED_NYLIUM, 10, 9, 1);
        theNether.addAllowedBlock(Material.ANCIENT_DEBRIS, 11, 10, 1);
        theNether.addAllowedBlock(Material.ANCIENT_DEBRIS, 12, 11, 1);
        theNether.setRegenerationTime(20L * 180L); // 3 minutes
        miningAreas.put("the_nether", theNether);
        
        // Overworld - Beginner area
        MiningArea overworld = new MiningArea("overworld", "Overworld",
            new Location(Bukkit.getWorlds().get(0), 1000, 50, 1000),
            new Location(Bukkit.getWorlds().get(0), 1100, 100, 1100));
        overworld.addAllowedBlock(Material.STONE, 1, 0, 1);
        overworld.addAllowedBlock(Material.COBBLESTONE, 1, 0, 1);
        overworld.addAllowedBlock(Material.COAL_ORE, 2, 1, 1);
        overworld.addAllowedBlock(Material.IRON_ORE, 3, 2, 1);
        overworld.addAllowedBlock(Material.GOLD_ORE, 4, 3, 1);
        overworld.addAllowedBlock(Material.DIAMOND_ORE, 5, 4, 1);
        overworld.addAllowedBlock(Material.EMERALD_ORE, 6, 5, 1);
        overworld.addAllowedBlock(Material.REDSTONE_ORE, 2, 1, 1);
        overworld.addAllowedBlock(Material.LAPIS_ORE, 3, 2, 1);
        overworld.addAllowedBlock(Material.NETHER_QUARTZ_ORE, 4, 3, 1);
        overworld.addAllowedBlock(Material.OBSIDIAN, 7, 6, 1);
        overworld.addAllowedBlock(Material.ANCIENT_DEBRIS, 8, 7, 1);
        overworld.addAllowedBlock(Material.ANCIENT_DEBRIS, 9, 8, 1);
        overworld.setRegenerationTime(20L * 30L); // 30 seconds
        miningAreas.put("overworld", overworld);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();
        
        // Check if player is in creative mode - allow them to break any block
        if (player.getGameMode() == GameMode.CREATIVE) {
            return; // Allow creative mode players to break any block
        }
        
        // Check if block is in a mining area
        MiningArea area = getMiningArea(location);
        if (area == null) {
            // Block is not in a mining area, allow breaking
            return;
        }
        
        // Check if player has required level to break this block
        if (!canPlayerBreakBlock(player, block, area)) {
            event.setCancelled(true);
            player.sendMessage("§cYou need Mining Level " + area.getRequiredLevel(block.getType()) + " to break this block!");
            player.sendMessage("§7Your Mining Level: §e" + getPlayerMiningLevel(player));
            return;
        }
        
        // Give mining XP
        giveMiningXP(player, block, area);
        
        // Add to collection
        plugin.getSkyblockManager().addCollection(player, block.getType(), 1);
        
        // Give block drops
        giveBlockDrops(player, block, area);
        
        // Send success message
        player.sendMessage("§a§lBLOCK BROKEN!");
        player.sendMessage("§7Block: §e" + block.getType().name());
        player.sendMessage("§7XP Gained: §e" + area.getXPAmount(block.getType()));
        player.sendMessage("§7Area: §e" + area.getName());
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();
        
        // Check if player is in creative mode - allow them to place any block
        if (player.getGameMode() == GameMode.CREATIVE) {
            return; // Allow creative mode players to place any block
        }
        
        // Check if block is in a mining area
        MiningArea area = getMiningArea(location);
        if (area == null) {
            // Block is not in a mining area, allow placing
            return;
        }
        
        // Check if player has required level to place this block
        if (!canPlayerPlaceBlock(player, block, area)) {
            event.setCancelled(true);
            player.sendMessage("§cYou need Mining Level " + area.getRequiredLevel(block.getType()) + " to place this block!");
            player.sendMessage("§7Your Mining Level: §e" + getPlayerMiningLevel(player));
            return;
        }
        
        // Send success message
        player.sendMessage("§a§lBLOCK PLACED!");
        player.sendMessage("§7Block: §e" + block.getType().name());
        player.sendMessage("§7Area: §e" + area.getName());
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        
        // Check if player entered a new mining area
        MiningArea newArea = getMiningArea(location);
        String currentArea = playerCurrentArea.get(player.getUniqueId());
        
        if (newArea != null && !newArea.getId().equals(currentArea)) {
            // Player entered a new mining area
            playerCurrentArea.put(player.getUniqueId(), newArea.getId());
            player.sendMessage("§a§lENTERED MINING AREA!");
            player.sendMessage("§7Area: §e" + newArea.getName());
            player.sendMessage("§7Description: §7" + getAreaDescription(newArea.getId()));
        } else if (newArea == null && currentArea != null) {
            // Player left a mining area
            playerCurrentArea.remove(player.getUniqueId());
            player.sendMessage("§c§lLEFT MINING AREA!");
            player.sendMessage("§7Area: §e" + getMiningArea(currentArea).getName());
        }
    }
    
    private boolean canPlayerBreakBlock(Player player, Block block, MiningArea area) {
        // Get player's mining level
        int playerLevel = getPlayerMiningLevel(player);
        
        // Get required level for this block
        int requiredLevel = area.getRequiredLevel(block.getType());
        
        return playerLevel >= requiredLevel;
    }
    
    private boolean canPlayerPlaceBlock(Player player, Block block, MiningArea area) {
        // Get player's mining level
        int playerLevel = getPlayerMiningLevel(player);
        
        // Get required level for this block
        int requiredLevel = area.getRequiredLevel(block.getType());
        
        return playerLevel >= requiredLevel;
    }
    
    private int getPlayerMiningLevel(Player player) {
        return plugin.getSkyblockManager().getSkills(player.getUniqueId())
            .getLevel(SkyblockManager.SkyblockSkill.MINING);
    }
    
    private void giveMiningXP(Player player, Block block, MiningArea area) {
        // Get XP amount for this block
        double xp = area.getXPAmount(block.getType());
        
        // Add mining XP
        plugin.getSkyblockManager().addSkillXP(player, SkyblockManager.SkyblockSkill.MINING, xp);
        
        // Add foraging XP for certain blocks
        if (block.getType().name().contains("LOG") || block.getType().name().contains("LEAVES")) {
            plugin.getSkyblockManager().addSkillXP(player, SkyblockManager.SkyblockSkill.FORAGING, xp);
        }
    }
    
    private void giveBlockDrops(Player player, Block block, MiningArea area) {
        // Get drop amount for this block
        int dropAmount = area.getDropAmount(block.getType());
        
        // Give drops
        player.getInventory().addItem(new ItemStack(block.getType(), dropAmount));
        
        // Give bonus drops based on area
        giveBonusDrops(player, block, area);
    }
    
    private void giveBonusDrops(Player player, Block block, MiningArea area) {
        // Give bonus drops based on area and block type
        switch (area.getId()) {
            case "deep_caverns" -> {
                if (block.getType() == Material.DIAMOND_ORE) {
                    player.getInventory().addItem(new ItemStack(Material.DIAMOND, 1));
                }
                if (block.getType() == Material.EMERALD_ORE) {
                    player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
                }
            }
            case "dwarven_mines" -> {
                if (block.getType() == Material.ANCIENT_DEBRIS) {
                    player.getInventory().addItem(new ItemStack(Material.NETHERITE_SCRAP, 1));
                }
                if (block.getType() == Material.ANCIENT_DEBRIS) {
                    player.getInventory().addItem(new ItemStack(Material.NETHERITE_INGOT, 1));
                }
            }
            case "crystal_hollows" -> {
                if (block.getType() == Material.END_CRYSTAL) {
                    player.getInventory().addItem(new ItemStack(Material.END_CRYSTAL, 1));
                }
                if (block.getType() == Material.DRAGON_EGG) {
                    player.getInventory().addItem(new ItemStack(Material.DRAGON_EGG, 1));
                }
            }
            case "the_end" -> {
                if (block.getType() == Material.ELYTRA) {
                    player.getInventory().addItem(new ItemStack(Material.ELYTRA, 1));
                }
                if (block.getType() == Material.SHULKER_BOX) {
                    player.getInventory().addItem(new ItemStack(Material.SHULKER_BOX, 1));
                }
            }
            case "the_nether" -> {
                if (block.getType() == Material.ANCIENT_DEBRIS) {
                    player.getInventory().addItem(new ItemStack(Material.NETHERITE_SCRAP, 1));
                }
                if (block.getType() == Material.ANCIENT_DEBRIS) {
                    player.getInventory().addItem(new ItemStack(Material.NETHERITE_INGOT, 1));
                }
            }
        }
    }
    
    private String getAreaDescription(String areaId) {
        return switch (areaId) {
            case "deep_caverns" -> "Beginner mining area with basic ores";
            case "dwarven_mines" -> "Intermediate mining area with rare ores";
            case "crystal_hollows" -> "Advanced mining area with end materials";
            case "the_end" -> "Expert mining area with end game materials";
            case "the_nether" -> "Expert mining area with nether materials";
            case "overworld" -> "Basic mining area for beginners";
            default -> "Unknown mining area";
        };
    }
    
    private void startAreaCheckTimer() {
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Location location = player.getLocation();
                        MiningArea area = getMiningArea(location);
                        String currentArea = playerCurrentArea.get(player.getUniqueId());
                        
                        if (area == null && currentArea != null) {
                            // Player left a mining area
                            playerCurrentArea.remove(player.getUniqueId());
                            player.sendMessage("§c§lLEFT MINING AREA!");
                            player.sendMessage("§7Area: §e" + currentArea);
                        }
                    }
                    Thread.sleep(20L * 5L * 50); // Check every 5 seconds = 5,000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    private MiningArea getMiningArea(Location location) {
        for (MiningArea area : miningAreas.values()) {
            if (area.isWithinBounds(location)) {
                return area;
            }
        }
        return null;
    }
    
    public MiningArea getMiningArea(String id) {
        return miningAreas.get(id);
    }
    
    public Map<String, MiningArea> getAllMiningAreas() {
        return new HashMap<>(miningAreas);
    }
    
    
    public static class MiningArea {
        private final String id;
        private final String name;
        private final Location minBound;
        private final Location maxBound;
        private final Map<Material, Integer> requiredLevels = new HashMap<>();
        private final Map<Material, Double> xpAmounts = new HashMap<>();
        private final Map<Material, Integer> dropAmounts = new HashMap<>();
        private long regenerationTime = 20L * 60L; // 1 minute default
        
        public MiningArea(String id, String name, Location minBound, Location maxBound) {
            this.id = id;
            this.name = name;
            this.minBound = minBound;
            this.maxBound = maxBound;
        }
        
        public void addAllowedBlock(Material material, int requiredLevel, double xpAmount, int dropAmount) {
            requiredLevels.put(material, requiredLevel);
            xpAmounts.put(material, xpAmount);
            dropAmounts.put(material, dropAmount);
        }
        
        public boolean isWithinBounds(Location location) {
            return location.getX() >= minBound.getX() && location.getX() <= maxBound.getX() &&
                   location.getY() >= minBound.getY() && location.getY() <= maxBound.getY() &&
                   location.getZ() >= minBound.getZ() && location.getZ() <= maxBound.getZ();
        }
        
        public int getRequiredLevel(Material material) {
            return requiredLevels.getOrDefault(material, 0);
        }
        
        public double getXPAmount(Material material) {
            return xpAmounts.getOrDefault(material, 0.0);
        }
        
        public int getDropAmount(Material material) {
            return dropAmounts.getOrDefault(material, 1);
        }
        
        public void setRegenerationTime(long regenerationTime) {
            this.regenerationTime = regenerationTime;
        }
        
        public long getRegenerationTime() {
            return regenerationTime;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public Location getMinBound() { return minBound; }
        public Location getMaxBound() { return maxBound; }
        public Map<Material, Integer> getRequiredLevels() { return new HashMap<>(requiredLevels); }
        public Map<Material, Double> getXPAmounts() { return new HashMap<>(xpAmounts); }
        public Map<Material, Integer> getDropAmounts() { return new HashMap<>(dropAmounts); }
    }
    
    // Missing methods for MiningCommand
    
    public MiningArea getPlayerCurrentArea(UUID playerId) {
        String areaId = playerCurrentArea.get(playerId);
        return areaId != null ? miningAreas.get(areaId) : null;
    }
}
