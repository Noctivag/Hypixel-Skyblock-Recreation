package de.noctivag.plugin.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dungeon Star System - Hypixel Skyblock Style
 * 
 * Features:
 * - Dungeon Stars (1-5 stars)
 * - Dungeon Bonuses (only active in dungeons)
 * - Star Requirements
 * - Essence Crafting
 * - Star Upgrades
 * - Dungeon Stats
 */
public class DungeonStarSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerDungeonStars> playerDungeonStars = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> dungeonStarTasks = new ConcurrentHashMap<>();
    
    public DungeonStarSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        startDungeonStarUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void startDungeonStarUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerDungeonStars> entry : playerDungeonStars.entrySet()) {
                    PlayerDungeonStars dungeonStars = entry.getValue();
                    dungeonStars.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Dungeon Star") || displayName.contains("Essence")) {
            openDungeonStarGUI(player);
        }
    }
    
    private void openDungeonStarGUI(Player player) {
        player.sendMessage("§aDungeon Star GUI geöffnet!");
    }
    
    public boolean addDungeonStar(Player player, ItemStack item, int starLevel) {
        if (item == null || !item.hasItemMeta()) {
            player.sendMessage("§cUngültiges Item!");
            return false;
        }
        
        // Check if item is dungeon item
        if (!isDungeonItem(item)) {
            player.sendMessage("§cDieses Item ist kein Dungeon-Item!");
            return false;
        }
        
        // Check current star level
        int currentStars = getCurrentStarLevel(item);
        if (currentStars >= 5) {
            player.sendMessage("§cDieses Item hat bereits die maximale Anzahl von Sternen (5)!");
            return false;
        }
        
        if (starLevel <= currentStars) {
            player.sendMessage("§cDu kannst nur höhere Sterne hinzufügen!");
            return false;
        }
        
        // Check if player has required essence
        EssenceType requiredEssence = getRequiredEssence(item);
        int requiredAmount = getRequiredEssenceAmount(starLevel);
        
        if (!hasEssence(player, requiredEssence, requiredAmount)) {
            player.sendMessage("§cDu hast nicht genug " + requiredEssence.getDisplayName() + "!");
            player.sendMessage("§7Benötigt: §a" + requiredAmount + "x " + requiredEssence.getDisplayName());
            return false;
        }
        
        // Apply dungeon star
        ItemMeta meta = item.getItemMeta();
        String displayName = meta.getDisplayName();
        
        // Add star to display name
        String newDisplayName = addStarToDisplayName(displayName, starLevel);
        meta.setDisplayName(newDisplayName);
        
        // Update lore
        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        
        // Add or update dungeon star line
        boolean found = false;
        for (int i = 0; i < lore.size(); i++) {
            if (lore.get(i).contains("Dungeon Stars:")) {
                lore.set(i, "§6Dungeon Stars: §a" + starLevel + "§6/5");
                found = true;
                break;
            }
        }
        
        if (!found) {
            lore.add("§6Dungeon Stars: §a" + starLevel + "§6/5");
        }
        
        // Add dungeon bonus info
        lore.add("§7Dungeon Bonus: §a+" + (starLevel * 10) + "% §7Stats in Dungeons");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        // Remove essence from player
        removeEssence(player, requiredEssence, requiredAmount);
        
        // Update player stats
        PlayerDungeonStars playerDungeonStars = getPlayerDungeonStars(player.getUniqueId());
        playerDungeonStars.addDungeonStar(starLevel);
        
        player.sendMessage("§aDungeon Star erfolgreich hinzugefügt! §6(" + starLevel + "/5)");
        
        // Update database
        databaseManager.executeUpdate("""
            INSERT INTO player_dungeon_stars (uuid, item_type, star_level, essence_type, essence_amount, timestamp)
            VALUES (?, ?, ?, ?, ?, NOW())
        """, player.getUniqueId(), item.getType().name(), starLevel, requiredEssence.name(), requiredAmount);
        
        return true;
    }
    
    private boolean isDungeonItem(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return false;
        
        List<String> lore = item.getItemMeta().getLore();
        for (String line : lore) {
            if (line.contains("Dungeon Item") || line.contains("Catacombs")) {
                return true;
            }
        }
        return false;
    }
    
    private int getCurrentStarLevel(ItemStack item) {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0;
        
        List<String> lore = item.getItemMeta().getLore();
        for (String line : lore) {
            if (line.contains("Dungeon Stars:")) {
                try {
                    String count = line.split("§a")[1].split("§6")[0];
                    return Integer.parseInt(count);
                } catch (Exception e) {
                    return 0;
                }
            }
        }
        return 0;
    }
    
    private EssenceType getRequiredEssence(ItemStack item) {
        // Determine essence type based on item type
        Material material = item.getType();
        if (material.name().contains("HELMET") || material.name().contains("CHESTPLATE") || 
            material.name().contains("LEGGINGS") || material.name().contains("BOOTS")) {
            return EssenceType.DIAMOND;
        } else if (material.name().contains("SWORD")) {
            return EssenceType.DIAMOND;
        } else if (material.name().contains("BOW")) {
            return EssenceType.DIAMOND;
        } else {
            return EssenceType.DIAMOND; // Default
        }
    }
    
    private int getRequiredEssenceAmount(int starLevel) {
        return switch (starLevel) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 8;
            case 5 -> 16;
            default -> 0;
        };
    }
    
    private boolean hasEssence(Player player, EssenceType essenceType, int amount) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                if (item.getItemMeta().getDisplayName().contains(essenceType.getDisplayName())) {
                    return item.getAmount() >= amount;
                }
            }
        }
        return false;
    }
    
    private void removeEssence(Player player, EssenceType essenceType, int amount) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                if (item.getItemMeta().getDisplayName().contains(essenceType.getDisplayName())) {
                    if (item.getAmount() > amount) {
                        item.setAmount(item.getAmount() - amount);
                    } else {
                        player.getInventory().remove(item);
                    }
                    return;
                }
            }
        }
    }
    
    private String addStarToDisplayName(String displayName, int starLevel) {
        // Add stars to the end of the display name
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < starLevel; i++) {
            stars.append("§6✦");
        }
        return displayName + " " + stars.toString();
    }
    
    public ItemStack createDungeonStar(int level) {
        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = star.getItemMeta();
        
        meta.setDisplayName("§6Dungeon Star " + level);
        List<String> lore = new ArrayList<>();
        lore.add("§7A star that can be applied to");
        lore.add("§7dungeon items to increase their");
        lore.add("§7stats in dungeons.");
        lore.add("");
        lore.add("§7Level: §a" + level + "§7/5");
        lore.add("§7Dungeon Bonus: §a+" + (level * 10) + "% §7Stats");
        lore.add("");
        lore.add("§6Right-click to apply!");
        
        meta.setLore(lore);
        star.setItemMeta(meta);
        
        return star;
    }
    
    public ItemStack createEssence(EssenceType essenceType) {
        ItemStack essence = new ItemStack(essenceType.getMaterial());
        ItemMeta meta = essence.getItemMeta();
        
        meta.setDisplayName(essenceType.getDisplayName());
        List<String> lore = new ArrayList<>();
        lore.add("§7A magical essence used to");
        lore.add("§7upgrade dungeon items with");
        lore.add("§7dungeon stars.");
        lore.add("");
        lore.add("§7Type: " + essenceType.getColor() + essenceType.getName());
        lore.add("§7Used for: §aDungeon Star Upgrades");
        lore.add("");
        lore.add("§6Right-click to use!");
        
        meta.setLore(lore);
        essence.setItemMeta(meta);
        
        return essence;
    }
    
    public PlayerDungeonStars getPlayerDungeonStars(UUID playerId) {
        return playerDungeonStars.computeIfAbsent(playerId, k -> new PlayerDungeonStars(playerId));
    }
    
    public enum EssenceType {
        DIAMOND("§bDiamond Essence", "§bDiamond", Material.DIAMOND),
        GOLD("§6Gold Essence", "§6Gold", Material.GOLD_INGOT),
        IRON("§fIron Essence", "§fIron", Material.IRON_INGOT),
        EMERALD("§aEmerald Essence", "§aEmerald", Material.EMERALD),
        LAPIS("§9Lapis Essence", "§9Lapis", Material.LAPIS_LAZULI),
        REDSTONE("§cRedstone Essence", "§cRedstone", Material.REDSTONE),
        QUARTZ("§fQuartz Essence", "§fQuartz", Material.QUARTZ),
        OBSIDIAN("§5Obsidian Essence", "§5Obsidian", Material.OBSIDIAN);
        
        private final String displayName;
        private final String name;
        private final Material material;
        
        EssenceType(String displayName, String name, Material material) {
            this.displayName = displayName;
            this.name = name;
            this.material = material;
        }
        
        public String getDisplayName() { return displayName; }
        public String getName() { return name; }
        public Material getMaterial() { return material; }
        public String getColor() { return displayName.substring(0, 2); }
    }
    
    public static class PlayerDungeonStars {
        private final UUID playerId;
        private final Map<Integer, Integer> starCounts = new ConcurrentHashMap<>();
        private int totalStars = 0;
        private int totalEssenceUsed = 0;
        private long lastUpdate;
        
        public PlayerDungeonStars(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save dungeon star data to database
        }
        
        public void addDungeonStar(int starLevel) {
            starCounts.put(starLevel, starCounts.getOrDefault(starLevel, 0) + 1);
            totalStars++;
        }
        
        public void addEssenceUsed(int amount) {
            totalEssenceUsed += amount;
        }
        
        public int getStarCount(int starLevel) {
            return starCounts.getOrDefault(starLevel, 0);
        }
        
        public int getTotalStars() {
            return totalStars;
        }
        
        public int getTotalEssenceUsed() {
            return totalEssenceUsed;
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public Map<Integer, Integer> getStarCounts() { return starCounts; }
    }
}
