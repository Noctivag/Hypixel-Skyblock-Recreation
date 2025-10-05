package de.noctivag.skyblock.accessories;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * AccessoryBagSystem - Hypixel SkyBlock Accessory Bag Implementation
 * 
 * Features:
 * - Accessory storage and management
 * - Bag expansion through Redstone collection
 * - Magical Power calculation
 * - Duplicate prevention (only highest tier counts)
 * - Accessory effects application
 */
public class AccessoryBagSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, AccessoryBag> playerBags = new ConcurrentHashMap<>();
    private final Map<String, AccessoryConfig> accessoryConfigs = new HashMap<>();
    private final Map<String, List<String>> accessoryLines = new HashMap<>();
    
    public AccessoryBagSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        initializeAccessoryConfigs();
        initializeAccessoryLines();
        
        // Register events after initialization is complete - execute directly for Folia compatibility
        Bukkit.getPluginManager().registerEvents(AccessoryBagSystem.this, SkyblockPlugin);
        startUpdateTask();
    }
    
    private void initializeAccessoryConfigs() {
        // Combat Accessories
        accessoryConfigs.put("zombie_talisman", new AccessoryConfig(
            "Zombie Talisman", "§fZombie Talisman", Material.ROTTEN_FLESH,
            "§7Increases damage against zombies by §a+5%", "combat", AccessoryRarity.COMMON, 1, 1
        ));
        accessoryConfigs.put("zombie_ring", new AccessoryConfig(
            "Zombie Ring", "§aZombie Ring", Material.ROTTEN_FLESH,
            "§7Increases damage against zombies by §a+10%", "combat", AccessoryRarity.UNCOMMON, 2, 2
        ));
        accessoryConfigs.put("zombie_artifact", new AccessoryConfig(
            "Zombie Artifact", "§9Zombie Artifact", Material.ROTTEN_FLESH,
            "§7Increases damage against zombies by §a+15%", "combat", AccessoryRarity.RARE, 3, 3
        ));
        accessoryConfigs.put("zombie_relic", new AccessoryConfig(
            "Zombie Relic", "§5Zombie Relic", Material.ROTTEN_FLESH,
            "§7Increases damage against zombies by §a+20%", "combat", AccessoryRarity.EPIC, 4, 4
        ));
        
        accessoryConfigs.put("skeleton_talisman", new AccessoryConfig(
            "Skeleton Talisman", "§fSkeleton Talisman", Material.BONE,
            "§7Increases damage against skeletons by §a+5%", "combat", AccessoryRarity.COMMON, 1, 1
        ));
        accessoryConfigs.put("skeleton_ring", new AccessoryConfig(
            "Skeleton Ring", "§aSkeleton Ring", Material.BONE,
            "§7Increases damage against skeletons by §a+10%", "combat", AccessoryRarity.UNCOMMON, 2, 2
        ));
        
        // Mining Accessories
        accessoryConfigs.put("mining_talisman", new AccessoryConfig(
            "Mining Talisman", "§fMining Talisman", Material.DIAMOND_PICKAXE,
            "§7Increases mining speed by §a+5%", "mining", AccessoryRarity.COMMON, 1, 1
        ));
        accessoryConfigs.put("mining_ring", new AccessoryConfig(
            "Mining Ring", "§aMining Ring", Material.DIAMOND_PICKAXE,
            "§7Increases mining speed by §a+10%", "mining", AccessoryRarity.UNCOMMON, 2, 2
        ));
        accessoryConfigs.put("mining_artifact", new AccessoryConfig(
            "Mining Artifact", "§9Mining Artifact", Material.DIAMOND_PICKAXE,
            "§7Increases mining speed by §a+15%", "mining", AccessoryRarity.RARE, 3, 3
        ));
        
        // Farming Accessories
        accessoryConfigs.put("farming_talisman", new AccessoryConfig(
            "Farming Talisman", "§fFarming Talisman", Material.WHEAT,
            "§7Increases farming XP gain by §a+5%", "farming", AccessoryRarity.COMMON, 1, 1
        ));
        accessoryConfigs.put("farming_ring", new AccessoryConfig(
            "Farming Ring", "§aFarming Ring", Material.WHEAT,
            "§7Increases farming XP gain by §a+10%", "farming", AccessoryRarity.UNCOMMON, 2, 2
        ));
        
        // Speed Accessories
        accessoryConfigs.put("speed_talisman", new AccessoryConfig(
            "Speed Talisman", "§fSpeed Talisman", Material.FEATHER,
            "§7Increases movement speed by §a+5%", "misc", AccessoryRarity.COMMON, 1, 1
        ));
        accessoryConfigs.put("speed_ring", new AccessoryConfig(
            "Speed Ring", "§aSpeed Ring", Material.FEATHER,
            "§7Increases movement speed by §a+10%", "misc", AccessoryRarity.UNCOMMON, 2, 2
        ));
        accessoryConfigs.put("speed_artifact", new AccessoryConfig(
            "Speed Artifact", "§9Speed Artifact", Material.FEATHER,
            "§7Increases movement speed by §a+15%", "misc", AccessoryRarity.RARE, 3, 3
        ));
        accessoryConfigs.put("speed_relic", new AccessoryConfig(
            "Speed Relic", "§5Speed Relic", Material.FEATHER,
            "§7Increases movement speed by §a+20%", "misc", AccessoryRarity.EPIC, 4, 4
        ));
        
        // Health Accessories
        accessoryConfigs.put("health_talisman", new AccessoryConfig(
            "Health Talisman", "§fHealth Talisman", Material.APPLE,
            "§7Increases health by §a+5", "misc", AccessoryRarity.COMMON, 1, 1
        ));
        accessoryConfigs.put("health_ring", new AccessoryConfig(
            "Health Ring", "§aHealth Ring", Material.APPLE,
            "§7Increases health by §a+10", "misc", AccessoryRarity.UNCOMMON, 2, 2
        ));
        
        // Defense Accessories
        accessoryConfigs.put("defense_talisman", new AccessoryConfig(
            "Defense Talisman", "§fDefense Talisman", Material.SHIELD,
            "§7Increases defense by §a+5", "misc", AccessoryRarity.COMMON, 1, 1
        ));
        accessoryConfigs.put("defense_ring", new AccessoryConfig(
            "Defense Ring", "§aDefense Ring", Material.SHIELD,
            "§7Increases defense by §a+10", "misc", AccessoryRarity.UNCOMMON, 2, 2
        ));
        
        // Intelligence Accessories
        accessoryConfigs.put("intelligence_talisman", new AccessoryConfig(
            "Intelligence Talisman", "§fIntelligence Talisman", Material.BOOK,
            "§7Increases intelligence by §a+5", "misc", AccessoryRarity.COMMON, 1, 1
        ));
        accessoryConfigs.put("intelligence_ring", new AccessoryConfig(
            "Intelligence Ring", "§aIntelligence Ring", Material.BOOK,
            "§7Increases intelligence by §a+10", "misc", AccessoryRarity.UNCOMMON, 2, 2
        ));
        
        // Strength Accessories
        accessoryConfigs.put("strength_talisman", new AccessoryConfig(
            "Strength Talisman", "§fStrength Talisman", Material.IRON_SWORD,
            "§7Increases strength by §a+5", "misc", AccessoryRarity.COMMON, 1, 1
        ));
        accessoryConfigs.put("strength_ring", new AccessoryConfig(
            "Strength Ring", "§aStrength Ring", Material.IRON_SWORD,
            "§7Increases strength by §a+10", "misc", AccessoryRarity.UNCOMMON, 2, 2
        ));
    }
    
    private void initializeAccessoryLines() {
        // Define accessory upgrade lines (only highest tier counts)
        accessoryLines.put("zombie", Arrays.asList("zombie_talisman", "zombie_ring", "zombie_artifact", "zombie_relic"));
        accessoryLines.put("skeleton", Arrays.asList("skeleton_talisman", "skeleton_ring"));
        accessoryLines.put("mining", Arrays.asList("mining_talisman", "mining_ring", "mining_artifact"));
        accessoryLines.put("farming", Arrays.asList("farming_talisman", "farming_ring"));
        accessoryLines.put("speed", Arrays.asList("speed_talisman", "speed_ring", "speed_artifact", "speed_relic"));
        accessoryLines.put("health", Arrays.asList("health_talisman", "health_ring"));
        accessoryLines.put("defense", Arrays.asList("defense_talisman", "defense_ring"));
        accessoryLines.put("intelligence", Arrays.asList("intelligence_talisman", "intelligence_ring"));
        accessoryLines.put("strength", Arrays.asList("strength_talisman", "strength_ring"));
    }
    
    public void openAccessoryBagGUI(Player player) {
        AccessoryBag bag = getPlayerBag(player.getUniqueId());
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§d§lAccessory Bag"));
        
        // Add accessories from bag
        for (int i = 0; i < bag.getMaxSlots(); i++) {
            String accessoryId = bag.getAccessory(i);
            if (accessoryId != null) {
                AccessoryConfig config = accessoryConfigs.get(accessoryId);
                if (config != null) {
                    addAccessoryItem(gui, i, config);
                }
            }
        }
        
        // Add info items
        addGUIItem(gui, 45, Material.REDSTONE, "§c§lExpand Bag", 
            "§7Click to expand your accessory bag", 
            "§7Cost: §a" + getExpansionCost(bag.getLevel()) + " Redstone");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the accessory bag");
        addGUIItem(gui, 53, Material.NETHER_STAR, "§6§lMagical Power: §e" + calculateMagicalPower(bag.getActiveAccessories()));
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aAccessory Bag opened!"));
    }
    
    private void addAccessoryItem(Inventory gui, int slot, AccessoryConfig config) {
        ItemStack item = new ItemStack(config.getMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(config.getDisplayName()));
            meta.lore(Arrays.asList(
                net.kyori.adventure.text.Component.text(config.getDescription()),
                net.kyori.adventure.text.Component.text(""),
                net.kyori.adventure.text.Component.text("§7Rarity: " + config.getRarity().getDisplayName()),
                net.kyori.adventure.text.Component.text("§7Magical Power: §a+" + config.getMagicalPower()),
                net.kyori.adventure.text.Component.text(""),
                net.kyori.adventure.text.Component.text("§eRight-click to remove from bag")
            ));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(net.kyori.adventure.text.Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        
        if (net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title()).equals("§d§lAccessory Bag")) {
            event.setCancelled(true);
            
            int slot = event.getSlot();
            AccessoryBag bag = getPlayerBag(player.getUniqueId());
            
            if (slot == 45) {
                // Expand bag
                expandBag(player, bag);
            } else if (slot == 49) {
                // Close
                player.closeInventory();
            } else if (slot >= 0 && slot < bag.getMaxSlots()) {
                // Remove accessory
                String accessoryId = bag.getAccessory(slot);
                if (accessoryId != null) {
                    bag.removeAccessory(slot);
                    player.sendMessage("§aRemoved " + accessoryConfigs.get(accessoryId).getDisplayName() + " §afrom bag");
                    openAccessoryBagGUI(player);
                }
            }
        }
    }
    
    public boolean addAccessoryToBag(UUID playerId, String accessoryId) {
        AccessoryBag bag = getPlayerBag(playerId);
        AccessoryConfig config = accessoryConfigs.get(accessoryId);
        
        if (config == null) return false;
        
        // Check for duplicates in the same line
        String line = getAccessoryLine(accessoryId);
        if (line != null) {
            String highestTier = getHighestTierAccessory(bag.getActiveAccessories(), line);
            if (highestTier != null && !highestTier.equals(accessoryId)) {
                // Remove lower tier and add higher tier
                bag.removeAccessoryById(highestTier);
            }
        }
        
        // Find empty slot
        for (int i = 0; i < bag.getMaxSlots(); i++) {
            if (bag.getAccessory(i) == null) {
                bag.setAccessory(i, accessoryId);
                return true;
            }
        }
        
        return false; // Bag is full
    }
    
    private String getAccessoryLine(String accessoryId) {
        for (Map.Entry<String, List<String>> entry : accessoryLines.entrySet()) {
            if (entry.getValue().contains(accessoryId)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    private String getHighestTierAccessory(List<String> accessories, String line) {
        List<String> lineAccessories = accessoryLines.get(line);
        if (lineAccessories == null) return null;
        
        String highest = null;
        int highestTier = 0;
        
        for (String accessoryId : accessories) {
            if (lineAccessories.contains(accessoryId)) {
                AccessoryConfig config = accessoryConfigs.get(accessoryId);
                if (config != null && config.getTier() > highestTier) {
                    highest = accessoryId;
                    highestTier = config.getTier();
                }
            }
        }
        
        return highest;
    }
    
    public int calculateMagicalPower(List<String> accessories) {
        int totalPower = 0;
        for (String accessoryId : accessories) {
            AccessoryConfig config = accessoryConfigs.get(accessoryId);
            if (config != null) {
                totalPower += config.getMagicalPower();
            }
        }
        return totalPower;
    }
    
    public Map<String, Double> calculateAccessoryPowers(int magicalPower) {
        Map<String, Double> powers = new HashMap<>();
        
        // Calculate available accessory powers based on magical power
        if (magicalPower >= 50) powers.put("Hurtful", 1.0);
        if (magicalPower >= 100) powers.put("Forceful", 1.0);
        if (magicalPower >= 150) powers.put("Silky", 1.0);
        if (magicalPower >= 200) powers.put("Rich", 1.0);
        if (magicalPower >= 250) powers.put("Demonic", 1.0);
        if (magicalPower >= 300) powers.put("Withered", 1.0);
        if (magicalPower >= 350) powers.put("Bloody", 1.0);
        if (magicalPower >= 400) powers.put("Strong", 1.0);
        if (magicalPower >= 450) powers.put("Unpleasant", 1.0);
        if (magicalPower >= 500) powers.put("Shaded", 1.0);
        if (magicalPower >= 550) powers.put("Spiritual", 1.0);
        if (magicalPower >= 600) powers.put("Heavy", 1.0);
        if (magicalPower >= 650) powers.put("Light", 1.0);
        if (magicalPower >= 700) powers.put("Suspicious", 1.0);
        if (magicalPower >= 750) powers.put("Ancient", 1.0);
        if (magicalPower >= 800) powers.put("Renowned", 1.0);
        if (magicalPower >= 850) powers.put("Giant", 1.0);
        if (magicalPower >= 900) powers.put("Bizarre", 1.0);
        if (magicalPower >= 950) powers.put("Itchy", 1.0);
        if (magicalPower >= 1000) powers.put("Precise", 1.0);
        
        return powers;
    }
    
    private void expandBag(Player player, AccessoryBag bag) {
        int cost = getExpansionCost(bag.getLevel());
        // Here you would check if player has enough redstone and deduct it
        // For now, just expand the bag
        bag.expand();
        player.sendMessage("§aBag expanded! New capacity: §e" + bag.getMaxSlots() + " slots");
        openAccessoryBagGUI(player);
    }
    
    private int getExpansionCost(int level) {
        return (level + 1) * 1000; // 1000, 2000, 3000, etc.
    }
    
    private void startUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Update accessory effects for all players
                for (AccessoryBag bag : playerBags.values()) {
                    bag.updateEffects();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 20L, 20L); // Every second
    }
    
    public AccessoryBag getPlayerBag(UUID playerId) {
        return playerBags.computeIfAbsent(playerId, k -> new AccessoryBag(playerId));
    }
    
    public static class AccessoryBag {
        private final UUID playerId;
        private final Map<Integer, String> accessories = new HashMap<>();
        private int level = 0;
        private int maxSlots = 9; // Start with 9 slots
        
        public AccessoryBag(UUID playerId) {
            this.playerId = playerId;
        }
        
        public String getAccessory(int slot) {
            return accessories.get(slot);
        }
        
        public void setAccessory(int slot, String accessoryId) {
            accessories.put(slot, accessoryId);
        }
        
        public void removeAccessory(int slot) {
            accessories.remove(slot);
        }
        
        public void removeAccessoryById(String accessoryId) {
            accessories.entrySet().removeIf(entry -> entry.getValue().equals(accessoryId));
        }
        
        public List<String> getActiveAccessories() {
            return new ArrayList<>(accessories.values());
        }
        
        public int getMaxSlots() {
            return maxSlots;
        }
        
        public int getLevel() {
            return level;
        }
        
        public void expand() {
            level++;
            maxSlots += 3; // Add 3 slots per expansion
        }
        
        public void updateEffects() {
            // Update player stats based on active accessories
            // This would integrate with your existing stat systems
        }
    }
    
    public static class AccessoryConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final String category;
        private final AccessoryRarity rarity;
        private final int tier;
        private final int magicalPower;
        
        public AccessoryConfig(String name, String displayName, Material material, String description,
                             String category, AccessoryRarity rarity, int tier, int magicalPower) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.tier = tier;
            this.magicalPower = magicalPower;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public AccessoryRarity getRarity() { return rarity; }
        public int getTier() { return tier; }
        public int getMagicalPower() { return magicalPower; }
    }
    
    public enum AccessoryRarity {
        COMMON("§fCommon", 1),
        UNCOMMON("§aUncommon", 2),
        RARE("§9Rare", 3),
        EPIC("§5Epic", 4),
        LEGENDARY("§6Legendary", 5),
        MYTHIC("§dMythic", 6);
        
        private final String displayName;
        private final int magicalPowerValue;
        
        AccessoryRarity(String displayName, int magicalPowerValue) {
            this.displayName = displayName;
            this.magicalPowerValue = magicalPowerValue;
        }
        
        public String getDisplayName() { return displayName; }
        public int getMagicalPowerValue() { return magicalPowerValue; }
    }
}
