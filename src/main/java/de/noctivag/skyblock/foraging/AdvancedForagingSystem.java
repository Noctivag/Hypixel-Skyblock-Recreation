package de.noctivag.skyblock.foraging;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Advanced Foraging System - Hypixel Skyblock Style
 * Implements Treecapitator mechanics, Foraging Islands, and special tree types
 */
public class AdvancedForagingSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerForagingData> playerForagingData = new ConcurrentHashMap<>();
    
    public AdvancedForagingSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();
        
        if (isLog(blockType)) {
            PlayerForagingData data = getPlayerForagingData(player.getUniqueId());
            TreeType treeType = getTreeType(blockType);
            
            if (treeType != null) {
                int xp = calculateForagingXP(player, data, treeType);
                data.addForagingXP(xp);
                
                // Check for treecapitator
                if (hasTreecapitator(player)) {
                    // Break entire tree
                    breakTree(event.getBlock(), player, treeType);
                }
                
                player.sendMessage("§aChopped " + treeType.getDisplayName() + "! +" + xp + " Foraging XP");
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());
        
        if (displayName.contains("Foraging") || displayName.contains("foraging")) {
            openForagingGUI(player);
        }
    }
    
    public void openForagingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lForaging System"));
        
        // Add foraging locations
        addGUIItem(gui, 10, Material.OAK_LOG, "§a§lSpawn Island", "§7A peaceful foraging spot");
        addGUIItem(gui, 11, Material.BIRCH_LOG, "§f§lBirch Park", "§7A birch tree park");
        addGUIItem(gui, 12, Material.SPRUCE_LOG, "§2§lSpruce Woods", "§7A spruce tree forest");
        addGUIItem(gui, 13, Material.DARK_OAK_LOG, "§8§lDark Thicket", "§7A dark forest");
        addGUIItem(gui, 14, Material.ACACIA_LOG, "§6§lSavanna Woodland", "§7A savanna woodland");
        addGUIItem(gui, 15, Material.JUNGLE_LOG, "§a§lJungle Island", "§7A jungle island");
        
        // Add foraging management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Foraging Progress", "§7View your foraging progress.");
        addGUIItem(gui, 19, Material.DIAMOND_AXE, "§c§lForaging Tools", "§7View available foraging tools.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lForaging Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lForaging Shop", "§7Buy foraging items.");
        addGUIItem(gui, 22, Material.GOLD_INGOT, "§6§lForaging Contests", "§7Join foraging contests.");
        
        // Add tree types
        addGUIItem(gui, 27, Material.OAK_LOG, "§6§lOak Tree", "§7A common oak tree");
        addGUIItem(gui, 28, Material.BIRCH_LOG, "§f§lBirch Tree", "§7A common birch tree");
        addGUIItem(gui, 29, Material.SPRUCE_LOG, "§2§lSpruce Tree", "§7A common spruce tree");
        addGUIItem(gui, 30, Material.JUNGLE_LOG, "§a§lJungle Tree", "§7A common jungle tree");
        addGUIItem(gui, 31, Material.ACACIA_LOG, "§6§lAcacia Tree", "§7A common acacia tree");
        addGUIItem(gui, 32, Material.DARK_OAK_LOG, "§8§lDark Oak Tree", "§7A common dark oak tree");
        
        // Add special items
        addGUIItem(gui, 36, Material.DIAMOND_AXE, "§6§lTreecapitator", "§7Chops entire trees");
        addGUIItem(gui, 37, Material.GOLDEN_AXE, "§e§lGolden Axe", "§7A golden axe");
        addGUIItem(gui, 38, Material.IRON_AXE, "§7§lIron Axe", "§7An iron axe");
        addGUIItem(gui, 39, Material.STONE_AXE, "§8§lStone Axe", "§7A stone axe");
        addGUIItem(gui, 40, Material.WOODEN_AXE, "§6§lWooden Axe", "§7A wooden axe");
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the foraging menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aForaging GUI opened!"));
    }
    
    private boolean isLog(Material material) {
        return material == Material.OAK_LOG || material == Material.BIRCH_LOG || 
               material == Material.SPRUCE_LOG || material == Material.JUNGLE_LOG || 
               material == Material.ACACIA_LOG || material == Material.DARK_OAK_LOG;
    }
    
    private TreeType getTreeType(Material material) {
        return switch (material) {
            case OAK_LOG -> TreeType.OAK;
            case BIRCH_LOG -> TreeType.BIRCH;
            case SPRUCE_LOG -> TreeType.SPRUCE;
            case JUNGLE_LOG -> TreeType.JUNGLE;
            case ACACIA_LOG -> TreeType.ACACIA;
            case DARK_OAK_LOG -> TreeType.DARK_OAK;
            default -> null;
        };
    }
    
    private int calculateForagingXP(Player player, PlayerForagingData data, TreeType treeType) {
        int baseXP = 10;
        int level = data.getForagingLevel();
        int xpMultiplier = 1 + (level / 10);
        int treeMultiplier = treeType.getRequiredLevel();
        
        return baseXP * xpMultiplier * treeMultiplier;
    }
    
    private boolean hasTreecapitator(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return false;
        
        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());
        return displayName.contains("Treecapitator");
    }
    
    private void breakTree(org.bukkit.block.Block block, Player player, TreeType treeType) {
        // Treecapitator logic - break entire tree
        // This would implement the tree-breaking algorithm
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerForagingData getPlayerForagingData(UUID playerId) {
        return playerForagingData.computeIfAbsent(playerId, k -> new PlayerForagingData(playerId));
    }
    
    public enum ForagingLocation {
        SPAWN("§aSpawn Island", "§7A peaceful foraging spot"),
        BIRCH_PARK("§fBirch Park", "§7A birch tree park"),
        SPRUCE_WOODS("§2Spruce Woods", "§7A spruce tree forest"),
        DARK_THICKET("§8Dark Thicket", "§7A dark forest"),
        SAVANNA_WOODLAND("§6Savanna Woodland", "§7A savanna woodland"),
        JUNGLE_ISLAND("§aJungle Island", "§7A jungle island");
        
        private final String displayName;
        private final String description;
        
        ForagingLocation(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum TreeType {
        OAK("§6Oak Tree", "§7A common oak tree", 1),
        BIRCH("§fBirch Tree", "§7A common birch tree", 2),
        SPRUCE("§2Spruce Tree", "§7A common spruce tree", 3),
        JUNGLE("§aJungle Tree", "§7A common jungle tree", 4),
        ACACIA("§6Acacia Tree", "§7A common acacia tree", 5),
        DARK_OAK("§8Dark Oak Tree", "§7A common dark oak tree", 6);
        
        private final String displayName;
        private final String description;
        private final int requiredLevel;
        
        TreeType(String displayName, String description, int requiredLevel) {
            this.displayName = displayName;
            this.description = description;
            this.requiredLevel = requiredLevel;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public int getRequiredLevel() { return requiredLevel; }
    }
    
    public static class PlayerForagingData {
        private final UUID playerId;
        private int foragingLevel;
        private int foragingXP;
        
        public PlayerForagingData(UUID playerId) {
            this.playerId = playerId;
            this.foragingLevel = 1;
            this.foragingXP = 0;
        }
        
        public UUID getPlayerId() { return playerId; }
        public int getForagingLevel() { return foragingLevel; }
        public int getForagingXP() { return foragingXP; }
        
        public void addForagingXP(int xp) {
            this.foragingXP += xp;
            checkLevelUp();
        }
        
        private void checkLevelUp() {
            int requiredXP = getRequiredXP(foragingLevel + 1);
            if (foragingXP >= requiredXP) {
                foragingLevel++;
            }
        }
        
        private int getRequiredXP(int level) {
            return level * 1000;
        }
    }
}
