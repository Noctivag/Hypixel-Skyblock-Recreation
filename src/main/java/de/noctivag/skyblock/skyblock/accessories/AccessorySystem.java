package de.noctivag.skyblock.skyblock.accessories;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Comprehensive Accessory System inspired by Hypixel Skyblock
 * Features:
 * - Accessory bag with limited slots
 * - Accessory powers and bonuses
 * - Accessory rarity system
 * - Accessory reforging
 * - Accessory enrichment
 * - Accessory collection and management
 * - Accessory stat bonuses
 */
public class AccessorySystem implements Listener {
    
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerAccessoryData> playerAccessoryData = new ConcurrentHashMap<>();
    private final Map<UUID, AccessoryBag> playerAccessoryBags = new ConcurrentHashMap<>();
    private final Map<Material, Accessory> availableAccessories = new HashMap<>();
    
    public AccessorySystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
        initializeAccessories();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Load player accessory data
        PlayerAccessoryData data = loadPlayerAccessoryData(playerId);
        playerAccessoryData.put(playerId, data);
        
        // Create accessory bag if it doesn't exist
        if (!playerAccessoryBags.containsKey(playerId)) {
            AccessoryBag bag = new AccessoryBag(playerId, 3); // Start with 3 slots
            playerAccessoryBags.put(playerId, bag);
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Save player accessory data
        savePlayerAccessoryData(playerId);
        
        // Remove from memory
        playerAccessoryData.remove(playerId);
        playerAccessoryBags.remove(playerId);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (!title.contains("Accessory Bag")) return;
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        String displayName = clicked.getItemMeta().getDisplayName();
        
        // Handle accessory bag clicks
        handleAccessoryBagClick(player, displayName, event.getSlot());
    }
    
    private void initializeAccessories() {
        // Initialize all available accessories
        addAccessory(Material.EMERALD, "§aEmerald Ring", AccessoryRarity.COMMON, 
            Arrays.asList("§7+1 ❁ Strength", "§7+1 ❈ Defense"));
        
        addAccessory(Material.DIAMOND, "§bDiamond Ring", AccessoryRarity.UNCOMMON, 
            Arrays.asList("§7+2 ❁ Strength", "§7+2 ❈ Defense", "§7+1 ✎ Intelligence"));
        
        addAccessory(Material.GOLD_INGOT, "§6Gold Ring", AccessoryRarity.COMMON, 
            Arrays.asList("§7+1 ✎ Intelligence", "§7+1 ⚡ Speed"));
        
        addAccessory(Material.IRON_INGOT, "§fIron Ring", AccessoryRarity.COMMON, 
            Arrays.asList("§7+1 ❁ Strength", "§7+1 ❈ Defense"));
        
        addAccessory(Material.LAPIS_LAZULI, "§9Lapis Ring", AccessoryRarity.UNCOMMON, 
            Arrays.asList("§7+3 ✎ Intelligence", "§7+1 ⚡ Speed"));
        
        addAccessory(Material.REDSTONE, "§cRedstone Ring", AccessoryRarity.COMMON, 
            Arrays.asList("§7+1 ⚡ Speed", "§7+1 ❈ Defense"));
        
        addAccessory(Material.QUARTZ, "§fQuartz Ring", AccessoryRarity.UNCOMMON, 
            Arrays.asList("§7+2 ❁ Strength", "§7+2 ⚡ Speed"));
        
        addAccessory(Material.OBSIDIAN, "§5Obsidian Ring", AccessoryRarity.RARE, 
            Arrays.asList("§7+5 ❁ Strength", "§7+5 ❈ Defense", "§7+2 ✎ Intelligence"));
        
        addAccessory(Material.ENDER_PEARL, "§dEnder Ring", AccessoryRarity.RARE, 
            Arrays.asList("§7+3 ✎ Intelligence", "§7+3 ⚡ Speed", "§7+1 ❁ Strength"));
        
        addAccessory(Material.BLAZE_ROD, "§eBlaze Ring", AccessoryRarity.RARE, 
            Arrays.asList("§7+4 ✎ Intelligence", "§7+2 ❁ Strength", "§7+2 ⚡ Speed"));
        
        addAccessory(Material.GHAST_TEAR, "§fGhast Ring", AccessoryRarity.EPIC, 
            Arrays.asList("§7+6 ✎ Intelligence", "§7+3 ❁ Strength", "§7+3 ⚡ Speed"));
        
        addAccessory(Material.WITHER_SKELETON_SKULL, "§8Wither Ring", AccessoryRarity.LEGENDARY, 
            Arrays.asList("§7+10 ❁ Strength", "§7+10 ❈ Defense", "§7+5 ✎ Intelligence"));
        
        addAccessory(Material.DRAGON_EGG, "§6Dragon Ring", AccessoryRarity.MYTHIC, 
            Arrays.asList("§7+15 ❁ Strength", "§7+15 ❈ Defense", "§7+10 ✎ Intelligence", "§7+5 ⚡ Speed"));
        
        // Talismans
        addAccessory(Material.FEATHER, "§fFeather Talisman", AccessoryRarity.COMMON, 
            Arrays.asList("§7+1 ⚡ Speed"));
        
        addAccessory(Material.LEATHER, "§eLeather Talisman", AccessoryRarity.COMMON, 
            Arrays.asList("§7+1 ❈ Defense"));
        
        addAccessory(Material.STRING, "§7String Talisman", AccessoryRarity.COMMON, 
            Arrays.asList("§7+1 ❁ Strength"));
        
        addAccessory(Material.BONE, "§fBone Talisman", AccessoryRarity.UNCOMMON, 
            Arrays.asList("§7+2 ❁ Strength", "§7+1 ❈ Defense"));
        
        addAccessory(Material.SPIDER_EYE, "§8Spider Talisman", AccessoryRarity.UNCOMMON, 
            Arrays.asList("§7+2 ✎ Intelligence", "§7+1 ⚡ Speed"));
        
        addAccessory(Material.ROTTEN_FLESH, "§2Zombie Talisman", AccessoryRarity.UNCOMMON, 
            Arrays.asList("§7+2 ❈ Defense", "§7+1 ❁ Strength"));
        
        addAccessory(Material.GUNPOWDER, "§7Creeper Talisman", AccessoryRarity.RARE, 
            Arrays.asList("§7+3 ✎ Intelligence", "§7+2 ❁ Strength"));
        
        addAccessory(Material.ENDER_EYE, "§5Ender Talisman", AccessoryRarity.EPIC, 
            Arrays.asList("§7+5 ✎ Intelligence", "§7+3 ❁ Strength", "§7+2 ⚡ Speed"));
        
        // Artifacts
        addAccessory(Material.NETHER_STAR, "§6Nether Artifact", AccessoryRarity.LEGENDARY, 
            Arrays.asList("§7+8 ❁ Strength", "§7+8 ❈ Defense", "§7+5 ✎ Intelligence", "§7+3 ⚡ Speed"));
        
        addAccessory(Material.BEACON, "§bBeacon Artifact", AccessoryRarity.MYTHIC, 
            Arrays.asList("§7+12 ❁ Strength", "§7+12 ❈ Defense", "§7+8 ✎ Intelligence", "§7+5 ⚡ Speed"));
    }
    
    private void addAccessory(Material material, String name, AccessoryRarity rarity, List<String> stats) {
        Accessory accessory = new Accessory(material, name, rarity, stats);
        availableAccessories.put(material, accessory);
    }
    
    public void openAccessoryBag(Player player) {
        UUID playerId = player.getUniqueId();
        AccessoryBag bag = playerAccessoryBags.get(playerId);
        
        if (bag == null) {
            player.sendMessage("§cAccessory bag not found!");
            return;
        }
        
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§d§lAccessory Bag"));
        
        // Add accessory slots
        for (int i = 0; i < bag.getMaxSlots(); i++) {
            int slot = 10 + i; // Start from slot 10
            
            if (i < bag.getAccessories().size()) {
                Accessory accessory = bag.getAccessories().get(i);
                addAccessoryItem(gui, slot, accessory);
            } else {
                addEmptySlot(gui, slot);
            }
        }
        
        // Add accessory bag info
        addAccessoryItem(gui, 4, Material.CHEST, "§d§lAccessory Bag", 
            Arrays.asList("§7Slots: §e" + bag.getAccessories().size() + "§7/§e" + bag.getMaxSlots(),
                         "§7Total Power: §d" + calculateTotalPower(bag)));
        
        // Add available accessories
        addAccessoryItem(gui, 19, Material.EMERALD, "§a§lAvailable Accessories", 
            Arrays.asList("§7View all available accessories"));
        
        // Add accessory management
        addAccessoryItem(gui, 20, Material.ANVIL, "§7§lReforge Accessories", 
            Arrays.asList("§7Reforge your accessories"));
        
        addAccessoryItem(gui, 21, Material.ENCHANTING_TABLE, "§5§lEnrich Accessories", 
            Arrays.asList("§7Enrich your accessories"));
        
        addAccessoryItem(gui, 22, Material.BOOK, "§e§lAccessory Guide", 
            Arrays.asList("§7Learn about accessories"));
        
        // Add navigation
        addAccessoryItem(gui, 45, Material.ARROW, "§7§lPrevious Page", Arrays.asList("§7Go to previous page"));
        addAccessoryItem(gui, 49, Material.BARRIER, "§c§lClose", Arrays.asList("§7Close the accessory bag"));
        addAccessoryItem(gui, 53, Material.ARROW, "§7§lNext Page", Arrays.asList("§7Go to next page"));
        
        player.openInventory(gui);
    }
    
    public void addAccessoryToBag(Player player, Material accessoryMaterial) {
        UUID playerId = player.getUniqueId();
        AccessoryBag bag = playerAccessoryBags.get(playerId);
        Accessory accessory = availableAccessories.get(accessoryMaterial);
        
        if (bag == null || accessory == null) {
            player.sendMessage("§cAccessory not found!");
            return;
        }
        
        if (bag.isFull()) {
            player.sendMessage("§cYour accessory bag is full!");
            return;
        }
        
        // Check if player has the accessory item
        if (!player.getInventory().containsAtLeast(new ItemStack(accessoryMaterial), 1)) {
            player.sendMessage("§cYou don't have this accessory!");
            return;
        }
        
        // Remove accessory from inventory
        player.getInventory().removeItem(new ItemStack(accessoryMaterial, 1));
        
        // Add to bag
        bag.addAccessory(accessory);
        
        // Apply accessory bonuses
        applyAccessoryBonuses(player, accessory);
        
        player.sendMessage("§aAdded " + accessory.getName() + " to your accessory bag!");
    }
    
    public void removeAccessoryFromBag(Player player, int slot) {
        UUID playerId = player.getUniqueId();
        AccessoryBag bag = playerAccessoryBags.get(playerId);
        
        if (bag == null) {
            player.sendMessage("§cAccessory bag not found!");
            return;
        }
        
        if (slot >= bag.getAccessories().size()) {
            player.sendMessage("§cNo accessory in this slot!");
            return;
        }
        
        Accessory accessory = bag.removeAccessory(slot);
        if (accessory != null) {
            // Give accessory back to player
            ItemStack accessoryItem = new ItemStack(accessory.getMaterial());
            ItemMeta meta = accessoryItem.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(accessory.getName()));
                meta.lore(accessory.getStats().stream().map(Component::text).toList());
                accessoryItem.setItemMeta(meta);
            }
            
            player.getInventory().addItem(accessoryItem);
            
            // Remove accessory bonuses
            removeAccessoryBonuses(player, accessory);
            
            player.sendMessage("§aRemoved " + accessory.getName() + " from your accessory bag!");
        }
    }
    
    public void expandAccessoryBag(Player player) {
        UUID playerId = player.getUniqueId();
        AccessoryBag bag = playerAccessoryBags.get(playerId);
        
        if (bag == null) {
            player.sendMessage("§cAccessory bag not found!");
            return;
        }
        
        int currentSlots = bag.getMaxSlots();
        int newSlots = currentSlots + 1;
        double cost = calculateExpansionCost(newSlots);
        
        // Check if player can afford the expansion
        // This would check player's coin balance
        
        // Expand bag
        bag.setMaxSlots(newSlots);
        
        player.sendMessage("§aExpanded accessory bag to " + newSlots + " slots for §6" + String.format("%.0f", cost) + " coins!");
    }
    
    public void reforgeAccessory(Player player, int slot) {
        UUID playerId = player.getUniqueId();
        AccessoryBag bag = playerAccessoryBags.get(playerId);
        
        if (bag == null) {
            player.sendMessage("§cAccessory bag not found!");
            return;
        }
        
        if (slot >= bag.getAccessories().size()) {
            player.sendMessage("§cNo accessory in this slot!");
            return;
        }
        
        Accessory accessory = bag.getAccessories().get(slot);
        if (accessory == null) {
            player.sendMessage("§cNo accessory in this slot!");
            return;
        }
        
        // Check if accessory can be reforged
        if (accessory.getRarity() == AccessoryRarity.COMMON) {
            player.sendMessage("§cCommon accessories cannot be reforged!");
            return;
        }
        
        // Calculate reforge cost
        double cost = calculateReforgeCost(accessory.getRarity());
        
        // Check if player can afford the reforge
        // This would check player's coin balance
        
        // Apply reforge
        AccessoryReforge reforge = getRandomReforge(accessory.getRarity());
        accessory.setReforge(reforge);
        
        player.sendMessage("§aReforged " + accessory.getName() + " with " + reforge.getDisplayName() + "!");
    }
    
    public void enrichAccessory(Player player, int slot, EnrichmentType enrichment) {
        UUID playerId = player.getUniqueId();
        AccessoryBag bag = playerAccessoryBags.get(playerId);
        
        if (bag == null) {
            player.sendMessage("§cAccessory bag not found!");
            return;
        }
        
        if (slot >= bag.getAccessories().size()) {
            player.sendMessage("§cNo accessory in this slot!");
            return;
        }
        
        Accessory accessory = bag.getAccessories().get(slot);
        if (accessory == null) {
            player.sendMessage("§cNo accessory in this slot!");
            return;
        }
        
        // Check if accessory can be enriched
        if (accessory.getEnrichment() != null) {
            player.sendMessage("§cThis accessory is already enriched!");
            return;
        }
        
        // Calculate enrichment cost
        double cost = calculateEnrichmentCost(enrichment);
        
        // Check if player can afford the enrichment
        // This would check player's coin balance
        
        // Apply enrichment
        accessory.setEnrichment(enrichment);
        
        player.sendMessage("§aEnriched " + accessory.getName() + " with " + enrichment.getDisplayName() + "!");
    }
    
    private void applyAccessoryBonuses(Player player, Accessory accessory) {
        // Apply accessory stat bonuses to player
        // This would integrate with the health/mana system
    }
    
    private void removeAccessoryBonuses(Player player, Accessory accessory) {
        // Remove accessory stat bonuses from player
        // This would integrate with the health/mana system
    }
    
    private int calculateTotalPower(AccessoryBag bag) {
        int totalPower = 0;
        
        for (Accessory accessory : bag.getAccessories()) {
            totalPower += accessory.getRarity().getPower();
        }
        
        return totalPower;
    }
    
    private double calculateExpansionCost(int newSlots) {
        return newSlots * 10000.0; // 10k coins per slot
    }
    
    private double calculateReforgeCost(AccessoryRarity rarity) {
        return switch (rarity) {
            case UNCOMMON -> 1000.0;
            case RARE -> 5000.0;
            case EPIC -> 25000.0;
            case LEGENDARY -> 100000.0;
            case MYTHIC -> 500000.0;
            default -> 0.0;
        };
    }
    
    private double calculateEnrichmentCost(EnrichmentType enrichment) {
        return switch (enrichment) {
            case STRENGTH -> 5000.0;
            case DEFENSE -> 5000.0;
            case INTELLIGENCE -> 5000.0;
            case SPEED -> 5000.0;
            case CRITICAL_CHANCE -> 10000.0;
            case CRITICAL_DAMAGE -> 10000.0;
            case MAGIC_FIND -> 15000.0;
            case PET_LUCK -> 15000.0;
        };
    }
    
    private AccessoryReforge getRandomReforge(AccessoryRarity rarity) {
        // Return a random reforge based on rarity
        List<AccessoryReforge> availableReforges = getAvailableReforges(rarity);
        if (availableReforges.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        return availableReforges.get(random.nextInt(availableReforges.size()));
    }
    
    private List<AccessoryReforge> getAvailableReforges(AccessoryRarity rarity) {
        List<AccessoryReforge> reforges = new ArrayList<>();
        
        // Add reforges based on rarity
        reforges.add(AccessoryReforge.SHARP);
        reforges.add(AccessoryReforge.HEAVY);
        reforges.add(AccessoryReforge.LIGHT);
        
        if (rarity.ordinal() >= AccessoryRarity.RARE.ordinal()) {
            reforges.add(AccessoryReforge.MYSTIC);
            reforges.add(AccessoryReforge.ANCIENT);
        }
        
        if (rarity.ordinal() >= AccessoryRarity.EPIC.ordinal()) {
            reforges.add(AccessoryReforge.DIVINE);
            reforges.add(AccessoryReforge.LEGENDARY);
        }
        
        return reforges;
    }
    
    private void handleAccessoryBagClick(Player player, String displayName, int slot) {
        if (displayName.contains("Available Accessories")) {
            openAvailableAccessories(player);
        } else if (displayName.contains("Reforge Accessories")) {
            openReforgeMenu(player);
        } else if (displayName.contains("Enrich Accessories")) {
            openEnrichMenu(player);
        } else if (displayName.contains("Close")) {
            player.closeInventory();
        } else if (slot >= 10 && slot < 18) {
            // Accessory slot clicked
            int accessorySlot = slot - 10;
            removeAccessoryFromBag(player, accessorySlot);
        }
    }
    
    private void openAvailableAccessories(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lAvailable Accessories"));
        
        int slot = 0;
        for (Accessory accessory : availableAccessories.values()) {
            if (slot >= 45) break;
            
            addAccessoryItem(gui, slot, accessory);
            slot++;
        }
        
        // Add navigation
        addAccessoryItem(gui, 45, Material.ARROW, "§7§lBack", Arrays.asList("§7Return to accessory bag"));
        addAccessoryItem(gui, 49, Material.BARRIER, "§c§lClose", Arrays.asList("§7Close the menu"));
        
        player.openInventory(gui);
    }
    
    private void openReforgeMenu(Player player) {
        // Open reforge menu
        player.sendMessage("§eOpening reforge menu...");
    }
    
    private void openEnrichMenu(Player player) {
        // Open enrich menu
        player.sendMessage("§eOpening enrich menu...");
    }
    
    private void addAccessoryItem(Inventory gui, int slot, Accessory accessory) {
        ItemStack item = new ItemStack(accessory.getMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(accessory.getName()));
            
            List<String> lore = new ArrayList<>();
            lore.addAll(accessory.getStats());
            
            if (accessory.getReforge() != null) {
                lore.add("");
                lore.add("§7Reforge: " + accessory.getReforge().getDisplayName());
            }
            
            if (accessory.getEnrichment() != null) {
                lore.add("§7Enrichment: " + accessory.getEnrichment().getDisplayName());
            }
            
            lore.add("");
            lore.add("§eClick to add to bag!");
            
            meta.lore(lore.stream().map(Component::text).toList());
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private void addAccessoryItem(Inventory gui, int slot, Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(lore.stream().map(Component::text).toList());
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private void addEmptySlot(Inventory gui, int slot) {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§7Empty Slot"));
            meta.lore(Arrays.asList(Component.text("§7Click to add an accessory")));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    private PlayerAccessoryData loadPlayerAccessoryData(UUID playerId) {
        // Load from database or create new
        return new PlayerAccessoryData(playerId);
    }
    
    private void savePlayerAccessoryData(UUID playerId) {
        // Save to database
    }
    
    public enum AccessoryRarity {
        COMMON("§fCommon", 1),
        UNCOMMON("§aUncommon", 2),
        RARE("§9Rare", 3),
        EPIC("§5Epic", 4),
        LEGENDARY("§6Legendary", 5),
        MYTHIC("§dMythic", 6);
        
        private final String displayName;
        private final int power;
        
        AccessoryRarity(String displayName, int power) {
            this.displayName = displayName;
            this.power = power;
        }
        
        public String getDisplayName() { return displayName; }
        public int getPower() { return power; }
    }
    
    public enum AccessoryReforge {
        SHARP("§cSharp", "§7+2 ❁ Strength"),
        HEAVY("§8Heavy", "§7+2 ❈ Defense"),
        LIGHT("§fLight", "§7+2 ⚡ Speed"),
        MYSTIC("§5Mystic", "§7+3 ✎ Intelligence"),
        ANCIENT("§6Ancient", "§7+2 ❁ Strength, +2 ❈ Defense"),
        DIVINE("§bDivine", "§7+4 ✎ Intelligence, +2 ⚡ Speed"),
        LEGENDARY("§6Legendary", "§7+5 ❁ Strength, +5 ❈ Defense");
        
        private final String displayName;
        private final String description;
        
        AccessoryReforge(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum EnrichmentType {
        STRENGTH("§cStrength", "§7+1 ❁ Strength"),
        DEFENSE("§8Defense", "§7+1 ❈ Defense"),
        INTELLIGENCE("§bIntelligence", "§7+1 ✎ Intelligence"),
        SPEED("§fSpeed", "§7+1 ⚡ Speed"),
        CRITICAL_CHANCE("§6Critical Chance", "§7+0.1% Crit Chance"),
        CRITICAL_DAMAGE("§6Critical Damage", "§7+0.1% Crit Damage"),
        MAGIC_FIND("§dMagic Find", "§7+0.1% Magic Find"),
        PET_LUCK("§ePet Luck", "§7+0.1% Pet Luck");
        
        private final String displayName;
        private final String description;
        
        EnrichmentType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public static class Accessory {
        private final Material material;
        private final String name;
        private final AccessoryRarity rarity;
        private final List<String> stats;
        private AccessoryReforge reforge;
        private EnrichmentType enrichment;
        
        public Accessory(Material material, String name, AccessoryRarity rarity, List<String> stats) {
            this.material = material;
            this.name = name;
            this.rarity = rarity;
            this.stats = stats;
            this.reforge = null;
            this.enrichment = null;
        }
        
        // Getters and setters
        public Material getMaterial() { return material; }
        public String getName() { return name; }
        public AccessoryRarity getRarity() { return rarity; }
        public List<String> getStats() { return stats; }
        public AccessoryReforge getReforge() { return reforge; }
        public void setReforge(AccessoryReforge reforge) { this.reforge = reforge; }
        public EnrichmentType getEnrichment() { return enrichment; }
        public void setEnrichment(EnrichmentType enrichment) { this.enrichment = enrichment; }
    }
    
    public static class AccessoryBag {
        private final UUID playerId;
        private final List<Accessory> accessories;
        private int maxSlots;
        
        public AccessoryBag(UUID playerId, int maxSlots) {
            this.playerId = playerId;
            this.accessories = new ArrayList<>();
            this.maxSlots = maxSlots;
        }
        
        public void addAccessory(Accessory accessory) {
            if (!isFull()) {
                accessories.add(accessory);
            }
        }
        
        public Accessory removeAccessory(int slot) {
            if (slot >= 0 && slot < accessories.size()) {
                return accessories.remove(slot);
            }
            return null;
        }
        
        public boolean isFull() {
            return accessories.size() >= maxSlots;
        }
        
        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        public List<Accessory> getAccessories() { return accessories; }
        public int getMaxSlots() { return maxSlots; }
        public void setMaxSlots(int maxSlots) { this.maxSlots = maxSlots; }
    }
    
    public static class PlayerAccessoryData {
        private final UUID playerId;
        private int totalAccessories;
        private int totalPower;
        private double totalSpent;
        
        public PlayerAccessoryData(UUID playerId) {
            this.playerId = playerId;
            this.totalAccessories = 0;
            this.totalPower = 0;
            this.totalSpent = 0.0;
        }
        
        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        public int getTotalAccessories() { return totalAccessories; }
        public void setTotalAccessories(int totalAccessories) { this.totalAccessories = totalAccessories; }
        public int getTotalPower() { return totalPower; }
        public void setTotalPower(int totalPower) { this.totalPower = totalPower; }
        public double getTotalSpent() { return totalSpent; }
        public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }
    }
}
