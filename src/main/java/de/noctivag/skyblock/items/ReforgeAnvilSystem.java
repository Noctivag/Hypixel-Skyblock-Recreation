package de.noctivag.skyblock.items;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.core.CorePlatform;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Reforge Anvil System - Special block for reforging items
 * 
 * Features:
 * - Custom reforge anvil block
 * - Reforge GUI interface
 * - Reforge preview system
 * - Reforge history tracking
 * - Reforge bonuses and penalties
 * - Reforge success rates
 */
public class ReforgeAnvilSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final CorePlatform corePlatform;
    private final ReforgeStoneSystem reforgeStoneSystem;
    private final Map<UUID, ReforgeHistory> playerReforgeHistory = new HashMap<>();
    private final Map<UUID, ReforgePreview> activePreviews = new HashMap<>();
    
    public ReforgeAnvilSystem(SkyblockPlugin SkyblockPlugin, CorePlatform corePlatform, ReforgeStoneSystem reforgeStoneSystem) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.corePlatform = corePlatform;
        this.reforgeStoneSystem = reforgeStoneSystem;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        
        Block block = event.getClickedBlock();
        if (block == null) return;
        
        // Check if it's a reforge anvil (custom block or anvil)
        if (isReforgeAnvil(block)) {
            event.setCancelled(true);
            openReforgeGUI(event.getPlayer());
        }
    }
    
    private boolean isReforgeAnvil(Block block) {
        // Check for custom reforge anvil block or regular anvil
        return block.getType() == Material.ANVIL || 
               block.getType() == Material.CHIPPED_ANVIL || 
               block.getType() == Material.DAMAGED_ANVIL ||
               block.getType() == Material.ENCHANTING_TABLE; // Using enchanting table as reforge anvil
    }
    
    public void openReforgeGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§e§lReforge Anvil"));
        
        // Add reforge stone categories
        addGUIItem(gui, 10, Material.DIAMOND_SWORD, "§c§lWeapon Stones", 
            Arrays.asList("§7Reforge stones for weapons", "§7• Swords, Bows, Crossbows", "", "§eClick to view"));
        addGUIItem(gui, 11, Material.IRON_CHESTPLATE, "§b§lArmor Stones", 
            Arrays.asList("§7Reforge stones for armor", "§7• Helmets, Chestplates, etc.", "", "§eClick to view"));
        addGUIItem(gui, 12, Material.GOLD_INGOT, "§6§lAccessory Stones", 
            Arrays.asList("§7Reforge stones for accessories", "§7• Rings, Necklaces, etc.", "", "§eClick to view"));
        addGUIItem(gui, 13, Material.DIAMOND_PICKAXE, "§a§lTool Stones", 
            Arrays.asList("§7Reforge stones for tools", "§7• Pickaxes, Axes, etc.", "", "§eClick to view"));
        
        // Add utility items
        addGUIItem(gui, 19, Material.BOOK, "§9§lReforge History", 
            Arrays.asList("§7View your reforge history", "§7• Success rates", "§7• Failed attempts", "", "§eClick to view"));
        addGUIItem(gui, 20, Material.ENDER_EYE, "§d§lReforge Preview", 
            Arrays.asList("§7Preview reforge results", "§7• See potential stats", "§7• Calculate success rates", "", "§eClick to preview"));
        addGUIItem(gui, 21, Material.EMERALD, "§a§lReforge Bonuses", 
            Arrays.asList("§7View available bonuses", "§7• Success rate boosts", "§7• Stat multipliers", "", "§eClick to view"));
        
        // Add information items
        addGUIItem(gui, 28, Material.KNOWLEDGE_BOOK, "§b§lReforge Guide", 
            Arrays.asList("§7Learn about reforging", "§7• How to reforge", "§7• Success rates", "§7• Tips and tricks", "", "§eClick to read"));
        addGUIItem(gui, 29, Material.GOLD_INGOT, "§6§lReforge Achievements", 
            Arrays.asList("§7View reforge achievements", "§7• Milestones", "§7• Rewards", "", "§eClick to view"));
        addGUIItem(gui, 30, Material.PLAYER_HEAD, "§e§lReforge Statistics", 
            Arrays.asList("§7Your reforge statistics", "§7• Total reforges", "§7• Success rate", "§7• Favorite stones", "", "§eClick to view"));
        
        // Add close button
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", 
            Arrays.asList("§7Close the reforge anvil", "", "§eClick to close"));
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aReforge Anvil opened!"));
    }
    
    public void openReforgeStoneGUI(Player player, ReforgeStoneSystem.ReforgeCategory category) {
        Inventory gui = Bukkit.createInventory(null, 54, "§e§l" + category.name() + " Reforge Stones");
        
        int slot = 10;
        for (ReforgeStoneSystem.ReforgeStone stone : reforgeStoneSystem.getAllReforgeStones().values()) {
            if (stone.getCategory() == category) {
                ItemStack stoneItem = reforgeStoneSystem.createReforgeStoneItem(stone.getId());
                if (stoneItem != null) {
                    gui.setItem(slot, stoneItem);
                    slot++;
                    if (slot % 9 == 8) slot += 2; // Skip to next row
                }
            }
        }
        
        // Add back button
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to main reforge menu", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public void openReforgePreviewGUI(Player player, ItemStack item, ReforgeStoneSystem.ReforgeStone stone) {
        Inventory gui = Bukkit.createInventory(null, 27, Component.text("§e§lReforge Preview"));
        
        // Show original item
        addGUIItem(gui, 10, item.getType(), "§7Original Item", 
            Arrays.asList("§7Current item stats", "§7• " + item.getType().name(), "", "§7This is your current item"));
        
        // Show reforge stone
                ItemStack stoneItem = reforgeStoneSystem.createReforgeStoneItem(stone.getId());
        if (stoneItem != null) {
            gui.setItem(12, stoneItem);
        }
        
        // Show preview result
        ItemStack previewItem = createReforgePreview(item, stone);
        addGUIItem(gui, 14, previewItem.getType(), "§aPreview Result", 
            Arrays.asList("§7Potential reforge result", "§7• " + previewItem.getType().name(), "", "§7This is what your item could become"));
        
        // Show success rate
        double successRate = calculateSuccessRate(player, stone);
        addGUIItem(gui, 16, Material.ENDER_EYE, "§bSuccess Rate: §e" + (successRate * 100) + "%", 
            Arrays.asList("§7Chance of successful reforge", "§7• Base rate: " + (stone.getSuccessRate() * 100) + "%", 
                         "§7• Player bonus: " + getPlayerBonus(player) + "%", 
                         "§7• Final rate: " + (successRate * 100) + "%"));
        
        // Add action buttons
        addGUIItem(gui, 22, Material.EMERALD, "§a§lConfirm Reforge", 
            Arrays.asList("§7Proceed with reforging", "§7• Cost: " + stone.getCost() + " coins", "§7• Success rate: " + (successRate * 100) + "%", "", "§eClick to confirm"));
        addGUIItem(gui, 24, Material.REDSTONE, "§c§lCancel", 
            Arrays.asList("§7Cancel reforging", "", "§eClick to cancel"));
        
        player.openInventory(gui);
    }
    
    public void openReforgeHistoryGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§e§lReforge History"));
        
        ReforgeHistory history = playerReforgeHistory.get(player.getUniqueId());
        if (history == null) {
            history = new ReforgeHistory(player.getUniqueId());
            playerReforgeHistory.put(player.getUniqueId(), history);
        }
        
        // Show statistics
        addGUIItem(gui, 4, Material.BOOK, "§b§lYour Statistics", 
            Arrays.asList("§7Total Reforges: §e" + history.getTotalReforges(),
                         "§7Successful: §a" + history.getSuccessfulReforges(),
                         "§7Failed: §c" + history.getFailedReforges(),
                         "§7Success Rate: §e" + String.format("%.1f", history.getSuccessRate()) + "%"));
        
        // Show recent reforges
        int slot = 18;
        for (ReforgeEntry entry : history.getRecentReforges()) {
            if (slot >= 45) break;
            
            Material material = entry.isSuccess() ? Material.EMERALD : Material.REDSTONE;
            String status = entry.isSuccess() ? "§aSuccess" : "§cFailed";
            
            addGUIItem(gui, slot, material, status + " §7- " + entry.getStoneName(), 
                Arrays.asList("§7Item: §e" + entry.getItemType(),
                             "§7Stone: " + entry.getStoneName(),
                             "§7Date: §e" + entry.getDate(),
                             "§7Result: " + status));
            slot++;
        }
        
        // Add back button
        addGUIItem(gui, 49, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to main reforge menu", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public boolean performReforge(Player player, ItemStack item, ReforgeStoneSystem.ReforgeStone stone) {
        // Calculate reforge cost
        double cost = 1000.0; // Base cost for reforging
        
        // Check if player has enough coins
        if (SkyblockPlugin.getEconomyManager() != null) {
            double playerBalance = SkyblockPlugin.getEconomyManager().getBalance(player);
            if (playerBalance < cost) {
                player.sendMessage("§cDu hast nicht genug Coins! Benötigt: " + cost + ", Hast: " + playerBalance);
                return false;
            }
        }
        
        // Check if player has enough coins (placeholder)
        if (stone.getCost() > 1000000) { // Arbitrary high cost check
            player.sendMessage("§cYou don't have enough coins! Cost: " + stone.getCost());
            return false;
        }
        
        // Check if item is compatible
        if (!reforgeStoneSystem.canUseStone(item, stone)) {
            player.sendMessage("§cThis item cannot be reforged with " + stone.getDisplayName() + "!");
            return false;
        }
        
        // Calculate success rate
        double successRate = calculateSuccessRate(player, stone);
        
        // Attempt reforge
        boolean success = Math.random() < successRate;
        
        // Consume coins through economy manager
        if (SkyblockPlugin.getEconomyManager() != null) {
            SkyblockPlugin.getEconomyManager().withdrawMoney(player, cost);
            player.sendMessage("§e" + cost + " Coins wurden für das Reforge verwendet.");
        }
        // profile.removeCoins(stone.getCost());
        
        // Record in history
        ReforgeHistory history = playerReforgeHistory.computeIfAbsent(player.getUniqueId(), k -> new ReforgeHistory(player.getUniqueId()));
        history.addReforge(new ReforgeEntry(item.getType().name(), stone.getDisplayName(), success, new Date()));
        
        if (success) {
            // Apply reforge
            ItemStack reforgedItem = applyReforge(item, stone);
            
            // Replace item in inventory
            replaceItemInInventory(player, item, reforgedItem);
            
            player.sendMessage(Component.text("§a§lREFORGE SUCCESSFUL!"));
            player.sendMessage("§7Item: §e" + item.getType().name());
            player.sendMessage("§7Stone: " + stone.getDisplayName());
            player.sendMessage("§7Cost: §6" + stone.getCost() + " coins");
        } else {
            player.sendMessage(Component.text("§c§lREFORGE FAILED!"));
            player.sendMessage("§7Item: §e" + item.getType().name());
            player.sendMessage("§7Stone: " + stone.getDisplayName());
            player.sendMessage("§7Cost: §6" + stone.getCost() + " coins (consumed)");
        }
        
        return success;
    }
    
    private double calculateSuccessRate(Player player, ReforgeStoneSystem.ReforgeStone stone) {
        double baseRate = stone.getSuccessRate();
        double playerBonus = getPlayerBonus(player);
        return Math.min(1.0, baseRate + (playerBonus / 100.0));
    }
    
    private double getPlayerBonus(Player player) {
        ReforgeHistory history = playerReforgeHistory.get(player.getUniqueId());
        if (history == null) return 0.0;
        
        // Bonus based on success rate
        double successRate = history.getSuccessRate();
        if (successRate >= 80) return 10.0;
        if (successRate >= 60) return 5.0;
        if (successRate >= 40) return 2.0;
        return 0.0;
    }
    
    private ItemStack createReforgePreview(ItemStack item, ReforgeStoneSystem.ReforgeStone stone) {
        return applyReforge(item.clone(), stone);
    }
    
    private ItemStack applyReforge(ItemStack item, ReforgeStoneSystem.ReforgeStone stone) {
        ItemStack reforgedItem = item.clone();
        ItemMeta meta = reforgedItem.getItemMeta();
        
        if (meta == null) return reforgedItem;
        
        // Add reforge to lore
        List<String> lore = meta.lore() != null ? 
            meta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()) : 
            new java.util.ArrayList<>();
        if (lore == null) lore = new ArrayList<>();
        
        // Remove existing reforge
        lore.removeIf(line -> line.contains("Reforge:"));
        
        // Add new reforge
        lore.add(0, "§7Reforge: " + stone.getDisplayName());
        
        // Add reforge effects
        for (String effect : stone.getEffects()) {
            lore.add(effect);
        }
        
        meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
        reforgedItem.setItemMeta(meta);
        
        return reforgedItem;
    }
    
    private void replaceItemInInventory(Player player, ItemStack oldItem, ItemStack newItem) {
        // Find and replace item in inventory
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.equals(oldItem)) {
                player.getInventory().setItem(i, newItem);
                break;
            }
        }
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, List<String> description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(description.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    // History Classes
    public static class ReforgeHistory {
        private final UUID playerId;
        private final List<ReforgeEntry> reforgeEntries = new ArrayList<>();
        private int totalReforges = 0;
        private int successfulReforges = 0;
        private int failedReforges = 0;
        
        public ReforgeHistory(UUID playerId) {
            this.playerId = playerId;
        }
        
        public void addReforge(ReforgeEntry entry) {
            reforgeEntries.add(entry);
            totalReforges++;
            
            if (entry.isSuccess()) {
                successfulReforges++;
            } else {
                failedReforges++;
            }
            
            // Keep only last 50 entries
            if (reforgeEntries.size() > 50) {
                reforgeEntries.remove(0);
            }
        }
        
        public double getSuccessRate() {
            return totalReforges > 0 ? (double) successfulReforges / totalReforges * 100 : 0.0;
        }
        
        public List<ReforgeEntry> getRecentReforges() {
            List<ReforgeEntry> recent = new ArrayList<>(reforgeEntries);
            Collections.reverse(recent);
            return recent;
        }
        
        // Getters
        public UUID getPlayerId() { return playerId; }
        public List<ReforgeEntry> getReforgeEntries() { return reforgeEntries; }
        public int getTotalReforges() { return totalReforges; }
        public int getSuccessfulReforges() { return successfulReforges; }
        public int getFailedReforges() { return failedReforges; }
    }
    
    public static class ReforgeEntry {
        private final String itemType;
        private final String stoneName;
        private final boolean success;
        private final Date date;
        
        public ReforgeEntry(String itemType, String stoneName, boolean success, Date date) {
            this.itemType = itemType;
            this.stoneName = stoneName;
            this.success = success;
            this.date = date;
        }
        
        // Getters
        public String getItemType() { return itemType; }
        public String getStoneName() { return stoneName; }
        public boolean isSuccess() { return success; }
        public Date getDate() { return date; }
    }
    
    public static class ReforgePreview {
        private final ItemStack originalItem;
        private final ItemStack previewItem;
        private final ReforgeStoneSystem.ReforgeStone stone;
        private final double successRate;
        
        public ReforgePreview(ItemStack originalItem, ItemStack previewItem, 
                            ReforgeStoneSystem.ReforgeStone stone, double successRate) {
            this.originalItem = originalItem;
            this.previewItem = previewItem;
            this.stone = stone;
            this.successRate = successRate;
        }
        
        // Getters
        public ItemStack getOriginalItem() { return originalItem; }
        public ItemStack getPreviewItem() { return previewItem; }
        public ReforgeStoneSystem.ReforgeStone getStone() { return stone; }
        public double getSuccessRate() { return successRate; }
    }
}
