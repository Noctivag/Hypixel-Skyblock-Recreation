package de.noctivag.skyblock.bestiary;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Advanced Bestiary System - Tracks and manages creature encounters
 */
public class AdvancedBestiarySystem {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerBestiaryData> playerBestiaryData = new ConcurrentHashMap<>();
    private final Map<String, BestiaryEntry> bestiaryEntries = new HashMap<>();
    private final Map<String, BestiaryCategory> bestiaryCategories = new HashMap<>();
    
    public AdvancedBestiarySystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeBestiaryEntries();
    }
    
    private void initializeBestiaryEntries() {
        // Initialize bestiary categories
        bestiaryCategories.put("overworld", new BestiaryCategory("Overworld", Material.GRASS_BLOCK, "§aOverworld creatures"));
        bestiaryCategories.put("nether", new BestiaryCategory("Nether", Material.NETHERRACK, "§cNether creatures"));
        bestiaryCategories.put("end", new BestiaryCategory("End", Material.END_STONE, "§5End creatures"));
        bestiaryCategories.put("bosses", new BestiaryCategory("Bosses", Material.DRAGON_HEAD, "§6Boss creatures"));
        
        // Initialize bestiary entries
        bestiaryEntries.put("zombie", new BestiaryEntry("zombie", "Zombie", "§2Zombie", Material.ZOMBIE_HEAD, 
            "§7A common undead creature.", "overworld", BestiaryRarity.COMMON, 1));
        bestiaryEntries.put("skeleton", new BestiaryEntry("skeleton", "Skeleton", "§fSkeleton", Material.SKELETON_SKULL, 
            "§7A bony archer.", "overworld", BestiaryRarity.COMMON, 1));
        bestiaryEntries.put("creeper", new BestiaryEntry("creeper", "Creeper", "§aCreeper", Material.CREEPER_HEAD, 
            "§7An explosive creature.", "overworld", BestiaryRarity.COMMON, 1));
    }
    
    public void openBestiaryGUI(Player player) {
        Inventory gui = SkyblockPlugin.getServer().createInventory(null, 54, "§6§lBestiary");
        
        // Add bestiary categories
        addGUIItem(gui, 10, Material.GRASS_BLOCK, "§a§lOverworld", "§7Overworld creatures");
        addGUIItem(gui, 11, Material.NETHERRACK, "§c§lNether", "§7Nether creatures");
        addGUIItem(gui, 12, Material.END_STONE, "§5§lEnd", "§7End creatures");
        addGUIItem(gui, 13, Material.DRAGON_HEAD, "§6§lBosses", "§7Boss creatures");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the bestiary menu.");
        
        player.openInventory(gui);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public static class BestiaryEntry {
        private final String id;
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final String category;
        private final BestiaryRarity rarity;
        private final int level;
        
        public BestiaryEntry(String id, String name, String displayName, Material icon, 
                           String description, String category, BestiaryRarity rarity, int level) {
            this.id = id;
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.level = level;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public BestiaryRarity getRarity() { return rarity; }
        public int getLevel() { return level; }
    }
    
    public static class BestiaryCategory {
        private final String name;
        private final Material icon;
        private final String description;
        
        public BestiaryCategory(String name, Material icon, String description) {
            this.name = name;
            this.icon = icon;
            this.description = description;
        }
        
        // Getters
        public String getName() { return name; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
    }
    
    public static class PlayerBestiaryData {
        private final UUID playerId;
        private final Map<String, Integer> creatureKills = new HashMap<>();
        private final Map<String, Long> firstEncounter = new HashMap<>();
        private final Map<String, Long> lastEncounter = new HashMap<>();
        
        public PlayerBestiaryData(UUID playerId) {
            this.playerId = playerId;
        }
        
        // Getters and setters
        public UUID getPlayerId() { return playerId; }
        public Map<String, Integer> getCreatureKills() { return creatureKills; }
        public Map<String, Long> getFirstEncounter() { return firstEncounter; }
        public Map<String, Long> getLastEncounter() { return lastEncounter; }
    }
    
    public enum BestiaryRarity {
        COMMON("§fCommon", 1),
        UNCOMMON("§aUncommon", 2),
        RARE("§9Rare", 3),
        EPIC("§5Epic", 4),
        LEGENDARY("§6Legendary", 5),
        MYTHIC("§dMythic", 6);
        
        private final String displayName;
        private final int level;
        
        BestiaryRarity(String displayName, int level) {
            this.displayName = displayName;
            this.level = level;
        }
        
        public String getDisplayName() { return displayName; }
        public int getLevel() { return level; }
    }
}
