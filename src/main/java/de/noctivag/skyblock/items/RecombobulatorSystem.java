package de.noctivag.skyblock.items;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * RecombobulatorSystem - Complete recombobulator system for Hypixel Skyblock
 * 
 * Features:
 * - Item rarity upgrading
 * - Recombobulator item creation
 * - Rarity progression system
 * - Cost calculation
 * - Success rates
 */
public class RecombobulatorSystem implements Service {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final Map<UUID, PlayerRecombobulatorData> playerData = new ConcurrentHashMap<>();
    private final Map<ItemRarity, ItemRarity> rarityProgression = new HashMap<>();
    private final Map<ItemRarity, Integer> recombobulatorCosts = new HashMap<>();
    
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public RecombobulatorSystem(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            
            // Initialize rarity progression
            initializeRarityProgression();
            
            // Initialize costs
            initializeCosts();
            
            status = SystemStatus.ENABLED;
            SkyblockPlugin.getLogger().info("§a[RecombobulatorSystem] Initialized recombobulator system");
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
        return "RecombobulatorSystem";
    }
    
    /**
     * Initialize rarity progression
     */
    private void initializeRarityProgression() {
        rarityProgression.put(ItemRarity.COMMON, ItemRarity.UNCOMMON);
        rarityProgression.put(ItemRarity.UNCOMMON, ItemRarity.RARE);
        rarityProgression.put(ItemRarity.RARE, ItemRarity.EPIC);
        rarityProgression.put(ItemRarity.EPIC, ItemRarity.LEGENDARY);
        rarityProgression.put(ItemRarity.LEGENDARY, ItemRarity.MYTHIC);
        // MYTHIC is the highest rarity, no progression
    }
    
    /**
     * Initialize recombobulator costs
     */
    private void initializeCosts() {
        recombobulatorCosts.put(ItemRarity.COMMON, 1000);      // 1,000 coins
        recombobulatorCosts.put(ItemRarity.UNCOMMON, 5000);    // 5,000 coins
        recombobulatorCosts.put(ItemRarity.RARE, 25000);       // 25,000 coins
        recombobulatorCosts.put(ItemRarity.EPIC, 100000);      // 100,000 coins
        recombobulatorCosts.put(ItemRarity.LEGENDARY, 500000); // 500,000 coins
        // MYTHIC cannot be recombobulated
    }
    
    /**
     * Get player recombobulator data
     */
    public PlayerRecombobulatorData getPlayerData(UUID playerId) {
        return playerData.computeIfAbsent(playerId, k -> new PlayerRecombobulatorData(playerId));
    }
    
    /**
     * Recombobulate an item
     */
    public boolean recombobulateItem(Player player, ItemStack item) {
        if (item == null || item.getType().isAir()) {
            player.sendMessage(Component.text("§cYou must hold an item to recombobulate!"));
            return false;
        }
        
        ItemRarity currentRarity = getItemRarity(item);
        if (currentRarity == null) {
            player.sendMessage(Component.text("§cThis item cannot be recombobulated!"));
            return false;
        }
        
        ItemRarity nextRarity = rarityProgression.get(currentRarity);
        if (nextRarity == null) {
            player.sendMessage(Component.text("§cThis item is already at maximum rarity!"));
            return false;
        }
        
        int cost = recombobulatorCosts.get(currentRarity);
        
        // Check if player has enough coins
        // TODO: Integrate with economy system
        player.sendMessage("§eRecombobulating " + item.getType().name() + " from " + 
                          currentRarity.getDisplayName() + " to " + nextRarity.getDisplayName());
        player.sendMessage("§6Cost: $" + cost);
        
        // Check if player has recombobulator
        if (!hasRecombobulator(player)) {
            player.sendMessage(Component.text("§cYou need a Recombobulator 3000 to recombobulate items!"));
            return false;
        }
        
        // Calculate success rate
        double successRate = calculateSuccessRate(currentRarity);
        
        // Simulate recombobulation
        boolean success = Math.random() < successRate;
        
        if (success) {
            // Upgrade item rarity
            upgradeItemRarity(item, nextRarity);
            player.sendMessage("§a§lSUCCESS! §aItem upgraded to " + nextRarity.getColor() + nextRarity.getDisplayName() + "§a!");
            
            // Update player statistics
            PlayerRecombobulatorData playerRecombobulatorData = getPlayerData(player.getUniqueId());
            playerRecombobulatorData.addSuccessfulRecombobulation();
            
        } else {
            player.sendMessage(Component.text("§c§lFAILED! §cThe recombobulation failed and the item was destroyed!"));
            item.setAmount(0); // Destroy the item
        }
        
        // Consume recombobulator
        consumeRecombobulator(player);
        
        return success;
    }
    
    /**
     * Get item rarity from item
     */
    public ItemRarity getItemRarity(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return ItemRarity.COMMON; // Default rarity
        }
        
        List<String> lore = meta.lore() != null ? 
            meta.lore().stream()
                .map(LegacyComponentSerializer.legacySection()::serialize)
                .collect(java.util.stream.Collectors.toList()) : Collections.emptyList();
        for (String line : lore) {
            for (ItemRarity rarity : ItemRarity.values()) {
                if (line.contains(rarity.getColor() + rarity.getDisplayName())) {
                    return rarity;
                }
            }
        }
        
        return ItemRarity.COMMON; // Default rarity
    }
    
    /**
     * Upgrade item rarity
     */
    private void upgradeItemRarity(ItemStack item, ItemRarity newRarity) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        
        List<String> lore = meta.lore() != null ? 
            meta.lore().stream()
                .map(LegacyComponentSerializer.legacySection()::serialize)
                .collect(java.util.stream.Collectors.toList()) : new ArrayList<>();
        
        // Remove old rarity line
        lore.removeIf(line -> {
            for (ItemRarity rarity : ItemRarity.values()) {
                if (line.contains(rarity.getColor() + rarity.getDisplayName())) {
                    return true;
                }
            }
            return false;
        });
        
        // Add new rarity line
        lore.add(newRarity.getColor() + newRarity.getDisplayName());
        
        List<Component> loreComponents = lore.stream()
            .map(Component::text)
            .collect(java.util.stream.Collectors.toList());
        meta.lore(loreComponents);
        item.setItemMeta(meta);
    }
    
    /**
     * Check if player has recombobulator
     */
    private boolean hasRecombobulator(Player player) {
        // Check inventory for recombobulator
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isRecombobulator(item)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if item is a recombobulator
     */
    private boolean isRecombobulator(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return false;
        
        String displayName = meta.displayName() != null ? 
            LegacyComponentSerializer.legacySection().serialize(meta.displayName()) : "";
        return displayName.contains("Recombobulator 3000");
    }
    
    /**
     * Consume recombobulator
     */
    private void consumeRecombobulator(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isRecombobulator(item)) {
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    item.setAmount(0);
                }
                break;
            }
        }
    }
    
    /**
     * Calculate success rate based on rarity
     */
    private double calculateSuccessRate(ItemRarity rarity) {
        switch (rarity) {
            case COMMON:
                return 0.95; // 95% success rate
            case UNCOMMON:
                return 0.90; // 90% success rate
            case RARE:
                return 0.80; // 80% success rate
            case EPIC:
                return 0.70; // 70% success rate
            case LEGENDARY:
                return 0.60; // 60% success rate
            default:
                return 0.50; // 50% success rate
        }
    }
    
    /**
     * Create recombobulator item
     */
    public ItemStack createRecombobulator() {
        ItemStack recombobulator = new ItemStack(org.bukkit.Material.REDSTONE);
        ItemMeta meta = recombobulator.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§d§lRecombobulator 3000"));
            
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Combine with any item to upgrade"));
            lore.add(Component.text("§7its rarity by one tier!"));
            lore.add(Component.text(""));
            lore.add(Component.text("§7Success rates:"));
            lore.add(Component.text("§fCommon → Uncommon: §a95%"));
            lore.add(Component.text("§aUncommon → Rare: §a90%"));
            lore.add(Component.text("§9Rare → Epic: §e80%"));
            lore.add(Component.text("§5Epic → Legendary: §e70%"));
            lore.add(Component.text("§6Legendary → Mythic: §c60%"));
            lore.add(Component.text(""));
            lore.add(Component.text("§c§lWARNING: §cFailed recombobulations"));
            lore.add(Component.text("§cwill destroy the item!"));
            lore.add(Component.text(""));
            lore.add(Component.text("§d§lMYTHIC RARE"));
            
            meta.lore(lore);
            recombobulator.setItemMeta(meta);
        }
        
        return recombobulator;
    }
    
    /**
     * Get recombobulator cost for rarity
     */
    public int getRecombobulatorCost(ItemRarity rarity) {
        return recombobulatorCosts.getOrDefault(rarity, 0);
    }
    
    /**
     * Get next rarity in progression
     */
    public ItemRarity getNextRarity(ItemRarity currentRarity) {
        return rarityProgression.get(currentRarity);
    }
    
    /**
     * Check if item can be recombobulated
     */
    public boolean canRecombobulate(ItemStack item) {
        ItemRarity rarity = getItemRarity(item);
        return rarityProgression.containsKey(rarity);
    }
    
    /**
     * Get player recombobulator data (for GUI compatibility)
     */
    public PlayerRecombobulator getPlayerRecombobulator(UUID playerId) {
        return new PlayerRecombobulator(playerId);
    }
    
    /**
     * Player recombobulator data class for GUI compatibility
     */
    public static class PlayerRecombobulator {
        private final UUID playerId;
        
        public PlayerRecombobulator(UUID playerId) {
            this.playerId = playerId;
        }
        
        public UUID getPlayerId() {
            return playerId;
        }

        // Methods expected by RecombobulatorGUI
        public int getFailedRecombobulations() {
            return 0; // Placeholder
        }
        
        public int getTotalRecombobulations() {
            return 0; // Placeholder
        }
        
        public int getSuccessfulRecombobulations() {
            return 0; // Placeholder
        }
        
        public double getSuccessRate() {
            return 0.0; // Placeholder
        }
    }
    
    /**
     * Player Recombobulator Data class
     */
    public static class PlayerRecombobulatorData {
        private final UUID playerId;
        private int recombobulationsUsed;
        private long lastRecombobulation;
        
        public PlayerRecombobulatorData(UUID playerId) {
            this.playerId = playerId;
            this.recombobulationsUsed = 0;
            this.lastRecombobulation = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getRecombobulationsUsed() { return recombobulationsUsed; }
        public void setRecombobulationsUsed(int recombobulationsUsed) { this.recombobulationsUsed = recombobulationsUsed; }
        public long getLastRecombobulation() { return lastRecombobulation; }
        public void setLastRecombobulation(long lastRecombobulation) { this.lastRecombobulation = lastRecombobulation; }
        public void addSuccessfulRecombobulation() { 
            this.recombobulationsUsed++;
            this.lastRecombobulation = java.lang.System.currentTimeMillis();
        }
    }
    
}
