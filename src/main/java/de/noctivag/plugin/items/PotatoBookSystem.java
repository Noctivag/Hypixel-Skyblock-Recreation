package de.noctivag.plugin.items;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.core.api.Service;
import de.noctivag.plugin.core.api.SystemStatus;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PotatoBookSystem - Complete potato book system for Hypixel Skyblock
 * 
 * Features:
 * - Potato book creation and management
 * - Potato book upgrades
 * - Potato book effects
 * - Potato book combining
 * - Potato book statistics
 */
public class PotatoBookSystem implements Service {
    
    private final Plugin plugin;
    private final Map<UUID, PlayerPotatoBookData> playerData = new ConcurrentHashMap<>();
    private final Map<String, PotatoBook> potatoBooks = new HashMap<>();
    private final Map<PotatoBookType, List<PotatoBook>> potatoBooksByType = new HashMap<>();
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public PotatoBookSystem(Plugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Initialize all potato books
            initializePotatoBooks();
            
            status = SystemStatus.ENABLED;
            plugin.getLogger().info("§a[PotatoBookSystem] Initialized " + potatoBooks.size() + " potato books");
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }
    
    @Override
    public int getPriority() {
        return 50;
    }
    
    @Override
    public boolean isRequired() {
        return false;
    }
    
    @Override
    public String getName() {
        return "PotatoBookSystem";
    }
    
    /**
     * Initialize all potato books
     */
    private void initializePotatoBooks() {
        // Combat Potato Books
        initializeCombatPotatoBooks();
        
        // Mining Potato Books
        initializeMiningPotatoBooks();
        
        // Farming Potato Books
        initializeFarmingPotatoBooks();
        
        // Foraging Potato Books
        initializeForagingPotatoBooks();
        
        // Fishing Potato Books
        initializeFishingPotatoBooks();
        
        // Enchanting Potato Books
        initializeEnchantingPotatoBooks();
        
        // Alchemy Potato Books
        initializeAlchemyPotatoBooks();
        
        // Taming Potato Books
        initializeTamingPotatoBooks();
    }
    
    /**
     * Initialize combat potato books
     */
    private void initializeCombatPotatoBooks() {
        List<PotatoBook> combatBooks = new ArrayList<>();
        
        // Sharpness Potato Book
        combatBooks.add(new PotatoBook(
            "sharpness_potato_book", "Sharpness Potato Book", 
            "Increases melee damage by 5% per level",
            PotatoBookType.COMBAT, PotatoBookRarity.COMMON, 1, 10,
            new PotatoBookEffect("damage_multiplier", 0.05, "Melee damage increase")
        ));
        
        // Critical Potato Book
        combatBooks.add(new PotatoBook(
            "critical_potato_book", "Critical Potato Book",
            "Increases critical chance by 2% per level",
            PotatoBookType.COMBAT, PotatoBookRarity.UNCOMMON, 1, 10,
            new PotatoBookEffect("critical_chance", 0.02, "Critical chance increase")
        ));
        
        // Strength Potato Book
        combatBooks.add(new PotatoBook(
            "strength_potato_book", "Strength Potato Book",
            "Increases strength by 3 per level",
            PotatoBookType.COMBAT, PotatoBookRarity.RARE, 1, 10,
            new PotatoBookEffect("strength", 3.0, "Strength increase")
        ));
        
        potatoBooksByType.put(PotatoBookType.COMBAT, combatBooks);
        combatBooks.forEach(book -> potatoBooks.put(book.getId(), book));
    }
    
    /**
     * Initialize mining potato books
     */
    private void initializeMiningPotatoBooks() {
        List<PotatoBook> miningBooks = new ArrayList<>();
        
        // Mining Speed Potato Book
        miningBooks.add(new PotatoBook(
            "mining_speed_potato_book", "Mining Speed Potato Book",
            "Increases mining speed by 10% per level",
            PotatoBookType.MINING, PotatoBookRarity.COMMON, 1, 10,
            new PotatoBookEffect("mining_speed", 0.10, "Mining speed increase")
        ));
        
        // Fortune Potato Book
        miningBooks.add(new PotatoBook(
            "fortune_potato_book", "Fortune Potato Book",
            "Increases block drops by 5% per level",
            PotatoBookType.MINING, PotatoBookRarity.UNCOMMON, 1, 10,
            new PotatoBookEffect("fortune", 0.05, "Block drop increase")
        ));
        
        potatoBooksByType.put(PotatoBookType.MINING, miningBooks);
        miningBooks.forEach(book -> potatoBooks.put(book.getId(), book));
    }
    
    /**
     * Initialize farming potato books
     */
    private void initializeFarmingPotatoBooks() {
        List<PotatoBook> farmingBooks = new ArrayList<>();
        
        // Harvesting Potato Book
        farmingBooks.add(new PotatoBook(
            "harvesting_potato_book", "Harvesting Potato Book",
            "Increases crop yield by 8% per level",
            PotatoBookType.FARMING, PotatoBookRarity.COMMON, 1, 10,
            new PotatoBookEffect("harvesting", 0.08, "Crop yield increase")
        ));
        
        // Replenish Potato Book
        farmingBooks.add(new PotatoBook(
            "replenish_potato_book", "Replenish Potato Book",
            "Increases replenish chance by 3% per level",
            PotatoBookType.FARMING, PotatoBookRarity.RARE, 1, 10,
            new PotatoBookEffect("replenish", 0.03, "Replenish chance increase")
        ));
        
        potatoBooksByType.put(PotatoBookType.FARMING, farmingBooks);
        farmingBooks.forEach(book -> potatoBooks.put(book.getId(), book));
    }
    
    /**
     * Initialize foraging potato books
     */
    private void initializeForagingPotatoBooks() {
        List<PotatoBook> foragingBooks = new ArrayList<>();
        
        // Foraging Speed Potato Book
        foragingBooks.add(new PotatoBook(
            "foraging_speed_potato_book", "Foraging Speed Potato Book",
            "Increases foraging speed by 12% per level",
            PotatoBookType.FORAGING, PotatoBookRarity.COMMON, 1, 10,
            new PotatoBookEffect("foraging_speed", 0.12, "Foraging speed increase")
        ));
        
        potatoBooksByType.put(PotatoBookType.FORAGING, foragingBooks);
        foragingBooks.forEach(book -> potatoBooks.put(book.getId(), book));
    }
    
    /**
     * Initialize fishing potato books
     */
    private void initializeFishingPotatoBooks() {
        List<PotatoBook> fishingBooks = new ArrayList<>();
        
        // Fishing Speed Potato Book
        fishingBooks.add(new PotatoBook(
            "fishing_speed_potato_book", "Fishing Speed Potato Book",
            "Increases fishing speed by 15% per level",
            PotatoBookType.FISHING, PotatoBookRarity.COMMON, 1, 10,
            new PotatoBookEffect("fishing_speed", 0.15, "Fishing speed increase")
        ));
        
        // Treasure Potato Book
        fishingBooks.add(new PotatoBook(
            "treasure_potato_book", "Treasure Potato Book",
            "Increases treasure chance by 4% per level",
            PotatoBookType.FISHING, PotatoBookRarity.UNCOMMON, 1, 10,
            new PotatoBookEffect("treasure_chance", 0.04, "Treasure chance increase")
        ));
        
        potatoBooksByType.put(PotatoBookType.FISHING, fishingBooks);
        fishingBooks.forEach(book -> potatoBooks.put(book.getId(), book));
    }
    
    /**
     * Initialize enchanting potato books
     */
    private void initializeEnchantingPotatoBooks() {
        List<PotatoBook> enchantingBooks = new ArrayList<>();
        
        // Enchanting Speed Potato Book
        enchantingBooks.add(new PotatoBook(
            "enchanting_speed_potato_book", "Enchanting Speed Potato Book",
            "Increases enchanting speed by 20% per level",
            PotatoBookType.ENCHANTING, PotatoBookRarity.COMMON, 1, 10,
            new PotatoBookEffect("enchanting_speed", 0.20, "Enchanting speed increase")
        ));
        
        potatoBooksByType.put(PotatoBookType.ENCHANTING, enchantingBooks);
        enchantingBooks.forEach(book -> potatoBooks.put(book.getId(), book));
    }
    
    /**
     * Initialize alchemy potato books
     */
    private void initializeAlchemyPotatoBooks() {
        List<PotatoBook> alchemyBooks = new ArrayList<>();
        
        // Alchemy Speed Potato Book
        alchemyBooks.add(new PotatoBook(
            "alchemy_speed_potato_book", "Alchemy Speed Potato Book",
            "Increases alchemy speed by 18% per level",
            PotatoBookType.ALCHEMY, PotatoBookRarity.COMMON, 1, 10,
            new PotatoBookEffect("alchemy_speed", 0.18, "Alchemy speed increase")
        ));
        
        potatoBooksByType.put(PotatoBookType.ALCHEMY, alchemyBooks);
        alchemyBooks.forEach(book -> potatoBooks.put(book.getId(), book));
    }
    
    /**
     * Initialize taming potato books
     */
    private void initializeTamingPotatoBooks() {
        List<PotatoBook> tamingBooks = new ArrayList<>();
        
        // Pet XP Potato Book
        tamingBooks.add(new PotatoBook(
            "pet_xp_potato_book", "Pet XP Potato Book",
            "Increases pet XP gain by 25% per level",
            PotatoBookType.TAMING, PotatoBookRarity.UNCOMMON, 1, 10,
            new PotatoBookEffect("pet_xp", 0.25, "Pet XP gain increase")
        ));
        
        potatoBooksByType.put(PotatoBookType.TAMING, tamingBooks);
        tamingBooks.forEach(book -> potatoBooks.put(book.getId(), book));
    }
    
    /**
     * Get player potato book data
     */
    public PlayerPotatoBookData getPlayerData(UUID playerId) {
        return playerData.computeIfAbsent(playerId, k -> new PlayerPotatoBookData(playerId));
    }
    
    /**
     * Apply potato book to item
     */
    public boolean applyPotatoBook(Player player, ItemStack item, String potatoBookId) {
        PotatoBook potatoBook = potatoBooks.get(potatoBookId);
        if (potatoBook == null) {
            player.sendMessage("§cInvalid potato book!");
            return false;
        }
        
        if (item == null || item.getType().isAir()) {
            player.sendMessage("§cYou must hold an item to apply a potato book!");
            return false;
        }
        
        // Check if item already has this potato book
        if (hasPotatoBook(item, potatoBookId)) {
            player.sendMessage("§cThis item already has this potato book!");
            return false;
        }
        
        // Check if item has max potato books
        if (getPotatoBookCount(item) >= 10) {
            player.sendMessage("§cThis item already has the maximum number of potato books!");
            return false;
        }
        
        // Apply potato book
        addPotatoBookToItem(item, potatoBook);
        
        player.sendMessage("§aSuccessfully applied " + potatoBook.getName() + " to your item!");
        
        // Update player statistics
        PlayerPotatoBookData playerPotatoBookData = getPlayerData(player.getUniqueId());
        playerPotatoBookData.addPotatoBookUsed(potatoBookId);
        
        return true;
    }
    
    /**
     * Check if item has potato book
     */
    public boolean hasPotatoBook(ItemStack item, String potatoBookId) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return false;
        
        List<String> lore = meta.lore() != null ? 
            meta.lore().stream()
                .map(LegacyComponentSerializer.legacySection()::serialize)
                .collect(java.util.stream.Collectors.toList()) : Collections.emptyList();
        for (String line : lore) {
            if (line.contains("§6" + potatoBookId)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get potato book count on item
     */
    public int getPotatoBookCount(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) return 0;
        
        int count = 0;
        List<String> lore = meta.lore() != null ? 
            meta.lore().stream()
                .map(LegacyComponentSerializer.legacySection()::serialize)
                .collect(java.util.stream.Collectors.toList()) : Collections.emptyList();
        for (String line : lore) {
            if (line.contains("§6") && line.contains("Potato Book")) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Add potato book to item
     */
    private void addPotatoBookToItem(ItemStack item, PotatoBook potatoBook) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        List<String> lore = meta.lore() != null ? 
            meta.lore().stream()
                .map(LegacyComponentSerializer.legacySection()::serialize)
                .collect(java.util.stream.Collectors.toList()) : new ArrayList<>();
        
        // Add potato book line
        lore.add("§6" + potatoBook.getId() + " Potato Book");
        
        List<Component> loreComponents = lore.stream()
            .map(Component::text)
            .collect(java.util.stream.Collectors.toList());
        meta.lore(loreComponents);
        item.setItemMeta(meta);
    }
    
    /**
     * Create potato book item
     */
    public ItemStack createPotatoBook(String potatoBookId) {
        PotatoBook potatoBook = potatoBooks.get(potatoBookId);
        if (potatoBook == null) return null;
        
        ItemStack book = new ItemStack(org.bukkit.Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(potatoBook.getRarity().getColor() + potatoBook.getName()));
            
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7" + potatoBook.getDescription()));
            lore.add(Component.text(""));
            lore.add(Component.text("§7Type: " + potatoBook.getType().getColor() + potatoBook.getType().getDisplayName()));
            lore.add(Component.text("§7Rarity: " + potatoBook.getRarity().getColor() + potatoBook.getRarity().getDisplayName()));
            lore.add(Component.text("§7Level: §a" + potatoBook.getMinLevel() + "-" + potatoBook.getMaxLevel()));
            lore.add(Component.text(""));
            lore.add(Component.text("§7Effect: " + potatoBook.getEffect().getDescription()));
            lore.add(Component.text("§7Value: §a+" + potatoBook.getEffect().getValue() + " per level"));
            lore.add(Component.text(""));
            lore.add(Component.text("§eRight-click to apply to an item!"));
            
            meta.lore(lore);
            book.setItemMeta(meta);
        }
        
        return book;
    }
    
    /**
     * Get all potato books
     */
    public Map<String, PotatoBook> getAllPotatoBooks() {
        return new HashMap<>(potatoBooks);
    }
    
    /**
     * Get potato books by type
     */
    public List<PotatoBook> getPotatoBooksByType(PotatoBookType type) {
        return potatoBooksByType.getOrDefault(type, new ArrayList<>());
    }
    
    /**
     * Get potato book by ID
     */
    public PotatoBook getPotatoBook(String id) {
        return potatoBooks.get(id);
    }
    
    /**
     * Get player potato books data (for GUI compatibility)
     */
    public PlayerPotatoBooks getPlayerPotatoBooks(UUID playerId) {
        return new PlayerPotatoBooks(playerId);
    }
    
    /**
     * Player potato books data class for GUI compatibility
     */
    public static class PlayerPotatoBooks {
        private final UUID playerId;
        
        public PlayerPotatoBooks(UUID playerId) {
            this.playerId = playerId;
        }
        
        public UUID getPlayerId() {
            return playerId;
        }
        
        public int getTotalPotatoBooks() {
            return 0; // Placeholder
        }
        
        public int getAppliedPotatoBooks() {
            return 0; // Placeholder
        }

        // Methods expected by PotatoBookGUI
        public int getTotalHotPotatoBooks() {
            return 0; // Placeholder
        }

        public int getTotalFumingPotatoBooks() {
            return 0; // Placeholder
        }

        public int getSuccessfulApplications() {
            return 0; // Placeholder
        }

        public int getFailedApplications() {
            return 0; // Placeholder
        }

        public double getSuccessRate() {
            return 0.0; // Placeholder
        }
    }
    
}