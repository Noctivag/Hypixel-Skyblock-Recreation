package de.noctivag.skyblock.wardrobe;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.database.DatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Wardrobe System - Manages armor sets and quick equipment changes
 */
public class WardrobeSystem implements Service {
    
    private final SkyblockPlugin plugin;
    private final DatabaseManager databaseManager;
    private SystemStatus status = SystemStatus.DISABLED;
    
    // Wardrobe storage
    private final Map<UUID, Map<String, ArmorSet>> playerWardrobes = new ConcurrentHashMap<>();
    private final Map<UUID, String> playerActiveSets = new ConcurrentHashMap<>();
    
    // Default armor sets
    private final Map<String, ArmorSet> defaultSets = new HashMap<>();
    
    public WardrobeSystem(SkyblockPlugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        plugin.getLogger().info("Initializing WardrobeSystem...");
        
        try {
            // Load default armor sets
            loadDefaultSets();
            
            // Load player data
            loadPlayerData();
            
            status = SystemStatus.RUNNING;
            plugin.getLogger().info("WardrobeSystem initialized with " + defaultSets.size() + " default sets.");
            
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to initialize WardrobeSystem", e);
            status = SystemStatus.ERROR;
        }
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        plugin.getLogger().info("Shutting down WardrobeSystem...");
        
        // Save all player data
        saveAllPlayerData();
        
        status = SystemStatus.DISABLED;
        plugin.getLogger().info("WardrobeSystem shut down.");
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public String getName() {
        return "WardrobeSystem";
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled && status == SystemStatus.DISABLED) {
            initialize();
        } else if (!enabled && status == SystemStatus.RUNNING) {
            shutdown();
        }
    }
    
    /**
     * Load default armor sets
     */
    private void loadDefaultSets() {
        // Leather Armor Set
        defaultSets.put("leather", new ArmorSet("leather", "Leather Armor", "&f",
            "Basic leather armor set", Arrays.asList(
                new ItemStack(Material.LEATHER_HELMET),
                new ItemStack(Material.LEATHER_CHESTPLATE),
                new ItemStack(Material.LEATHER_LEGGINGS),
                new ItemStack(Material.LEATHER_BOOTS)
            )));
        
        // Iron Armor Set
        defaultSets.put("iron", new ArmorSet("iron", "Iron Armor", "&7",
            "Durable iron armor set", Arrays.asList(
                new ItemStack(Material.IRON_HELMET),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.IRON_BOOTS)
            )));
        
        // Diamond Armor Set
        defaultSets.put("diamond", new ArmorSet("diamond", "Diamond Armor", "&b",
            "Strong diamond armor set", Arrays.asList(
                new ItemStack(Material.DIAMOND_HELMET),
                new ItemStack(Material.DIAMOND_CHESTPLATE),
                new ItemStack(Material.DIAMOND_LEGGINGS),
                new ItemStack(Material.DIAMOND_BOOTS)
            )));
        
        // Netherite Armor Set
        defaultSets.put("netherite", new ArmorSet("netherite", "Netherite Armor", "&8",
            "Ultimate netherite armor set", Arrays.asList(
                new ItemStack(Material.NETHERITE_HELMET),
                new ItemStack(Material.NETHERITE_CHESTPLATE),
                new ItemStack(Material.NETHERITE_LEGGINGS),
                new ItemStack(Material.NETHERITE_BOOTS)
            )));
        
        // Chainmail Armor Set
        defaultSets.put("chainmail", new ArmorSet("chainmail", "Chainmail Armor", "&7",
            "Flexible chainmail armor set", Arrays.asList(
                new ItemStack(Material.CHAINMAIL_HELMET),
                new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                new ItemStack(Material.CHAINMAIL_LEGGINGS),
                new ItemStack(Material.CHAINMAIL_BOOTS)
            )));
        
        // Gold Armor Set
        defaultSets.put("gold", new ArmorSet("gold", "Gold Armor", "&6",
            "Enchantable gold armor set", Arrays.asList(
                new ItemStack(Material.GOLDEN_HELMET),
                new ItemStack(Material.GOLDEN_CHESTPLATE),
                new ItemStack(Material.GOLDEN_LEGGINGS),
                new ItemStack(Material.GOLDEN_BOOTS)
            )));
        
        plugin.getLogger().info("Loaded " + defaultSets.size() + " default armor sets.");
    }
    
    /**
     * Load player data
     */
    private void loadPlayerData() {
        // This would load from database in a real implementation
        // For now, we'll initialize empty wardrobes
    }
    
    /**
     * Save all player data
     */
    private void saveAllPlayerData() {
        // This would save to database in a real implementation
    }
    
    /**
     * Get a player's wardrobe
     */
    public Map<String, ArmorSet> getPlayerWardrobe(Player player) {
        return playerWardrobes.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>(defaultSets));
    }
    
    /**
     * Get a player's armor set
     */
    public ArmorSet getPlayerArmorSet(Player player, String setName) {
        Map<String, ArmorSet> wardrobe = getPlayerWardrobe(player);
        return wardrobe.get(setName);
    }
    
    /**
     * Save a player's current armor as a set
     */
    public boolean saveCurrentArmorAsSet(Player player, String setName, String displayName, String description) {
        PlayerInventory inventory = player.getInventory();
        
        List<ItemStack> armor = Arrays.asList(
            inventory.getHelmet(),
            inventory.getChestplate(),
            inventory.getLeggings(),
            inventory.getBoots()
        );
        
        // Check if player has any armor equipped
        boolean hasArmor = armor.stream().anyMatch(item -> item != null && item.getType() != Material.AIR);
        if (!hasArmor) {
            player.sendMessage("§cYou need to wear armor to save a set!");
            return false;
        }
        
        ArmorSet armorSet = new ArmorSet(setName, displayName, "&a", description, armor);
        Map<String, ArmorSet> wardrobe = getPlayerWardrobe(player);
        wardrobe.put(setName, armorSet);
        
        player.sendMessage("§aArmor set '" + displayName + "' saved successfully!");
        return true;
    }
    
    /**
     * Equip an armor set
     */
    public boolean equipArmorSet(Player player, String setName) {
        ArmorSet armorSet = getPlayerArmorSet(player, setName);
        if (armorSet == null) {
            player.sendMessage("§cArmor set '" + setName + "' not found!");
            return false;
        }
        
        PlayerInventory inventory = player.getInventory();
        
        // Store current armor in inventory if there's space
        List<ItemStack> currentArmor = Arrays.asList(
            inventory.getHelmet(),
            inventory.getChestplate(),
            inventory.getLeggings(),
            inventory.getBoots()
        );
        
        // Check if there's space for current armor
        boolean hasSpace = true;
        for (ItemStack item : currentArmor) {
            if (item != null && item.getType() != Material.AIR) {
                if (inventory.firstEmpty() == -1) {
                    hasSpace = false;
                    break;
                }
            }
        }
        
        if (!hasSpace) {
            player.sendMessage("§cNot enough inventory space to store current armor!");
            return false;
        }
        
        // Store current armor
        for (ItemStack item : currentArmor) {
            if (item != null && item.getType() != Material.AIR) {
                inventory.addItem(item);
            }
        }
        
        // Equip new armor
        List<ItemStack> newArmor = armorSet.getArmor();
        inventory.setHelmet(newArmor.get(0));
        inventory.setChestplate(newArmor.get(1));
        inventory.setLeggings(newArmor.get(2));
        inventory.setBoots(newArmor.get(3));
        
        playerActiveSets.put(player.getUniqueId(), setName);
        player.sendMessage("§aEquipped armor set: " + armorSet.getDisplayName());
        return true;
    }
    
    /**
     * Delete an armor set
     */
    public boolean deleteArmorSet(Player player, String setName) {
        Map<String, ArmorSet> wardrobe = getPlayerWardrobe(player);
        
        if (!wardrobe.containsKey(setName)) {
            player.sendMessage("§cArmor set '" + setName + "' not found!");
            return false;
        }
        
        if (defaultSets.containsKey(setName)) {
            player.sendMessage("§cCannot delete default armor sets!");
            return false;
        }
        
        wardrobe.remove(setName);
        if (playerActiveSets.get(player.getUniqueId()).equals(setName)) {
            playerActiveSets.remove(player.getUniqueId());
        }
        
        player.sendMessage("§aArmor set '" + setName + "' deleted successfully!");
        return true;
    }
    
    /**
     * Get the player's active armor set
     */
    public String getActiveArmorSet(Player player) {
        return playerActiveSets.get(player.getUniqueId());
    }
    
    /**
     * Get all default armor sets
     */
    public Map<String, ArmorSet> getDefaultSets() {
        return new HashMap<>(defaultSets);
    }
    
    /**
     * Get armor set names for a player
     */
    public Set<String> getArmorSetNames(Player player) {
        return getPlayerWardrobe(player).keySet();
    }
    
    /**
     * Check if a player has an armor set
     */
    public boolean hasArmorSet(Player player, String setName) {
        return getPlayerWardrobe(player).containsKey(setName);
    }
    
    /**
     * Get the number of armor sets a player has
     */
    public int getArmorSetCount(Player player) {
        return getPlayerWardrobe(player).size();
    }
    
    /**
     * Get the maximum number of armor sets a player can have
     */
    public int getMaxArmorSets(Player player) {
        // This could be based on player level, rank, or other factors
        return 10; // Default maximum
    }
}
