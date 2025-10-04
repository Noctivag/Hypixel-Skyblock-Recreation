package de.noctivag.plugin.bestiary;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.core.CorePlatform;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bestiary System - Hypixel Skyblock Style
 * 
 * Features:
 * - Track mob kills and statistics
 * - Bestiary levels and rewards
 * - Mob categories and tiers
 * - Bestiary GUI interface
 * - Bestiary achievements and milestones
 */
public class BestiarySystem implements Listener {
    private final Plugin plugin;
    private final CorePlatform corePlatform;
    private final Map<UUID, PlayerBestiary> playerBestiaries = new ConcurrentHashMap<>();
    private final Map<EntityType, BestiaryEntry> bestiaryEntries = new HashMap<>();
    
    public BestiarySystem(Plugin plugin, CorePlatform corePlatform) {
        this.plugin = plugin;
        this.corePlatform = corePlatform;
        initializeBestiaryEntries();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeBestiaryEntries() {
        // Common Mobs
        bestiaryEntries.put(EntityType.ZOMBIE, new BestiaryEntry(
            EntityType.ZOMBIE, "Zombie", BestiaryTier.COMMON, BestiaryCategory.UNDEAD,
            "§7A common undead creature", Arrays.asList("§7+1% Damage to Zombies"),
            Arrays.asList(10, 25, 50, 100, 250, 500, 1000, 2500, 5000, 10000)
        ));
        
        bestiaryEntries.put(EntityType.SKELETON, new BestiaryEntry(
            EntityType.SKELETON, "Skeleton", BestiaryTier.COMMON, BestiaryCategory.UNDEAD,
            "§7A bony undead archer", Arrays.asList("§7+1% Damage to Skeletons"),
            Arrays.asList(10, 25, 50, 100, 250, 500, 1000, 2500, 5000, 10000)
        ));
        
        bestiaryEntries.put(EntityType.SPIDER, new BestiaryEntry(
            EntityType.SPIDER, "Spider", BestiaryTier.COMMON, BestiaryCategory.ARACHNID,
            "§7A creepy crawly arachnid", Arrays.asList("§7+1% Damage to Spiders"),
            Arrays.asList(10, 25, 50, 100, 250, 500, 1000, 2500, 5000, 10000)
        ));
        
        bestiaryEntries.put(EntityType.CREEPER, new BestiaryEntry(
            EntityType.CREEPER, "Creeper", BestiaryTier.COMMON, BestiaryCategory.EXPLOSIVE,
            "§7A green explosive mob", Arrays.asList("§7+1% Damage to Creepers"),
            Arrays.asList(10, 25, 50, 100, 250, 500, 1000, 2500, 5000, 10000)
        ));
        
        // Uncommon Mobs
        bestiaryEntries.put(EntityType.ENDERMAN, new BestiaryEntry(
            EntityType.ENDERMAN, "Enderman", BestiaryTier.UNCOMMON, BestiaryCategory.ENDER,
            "§7A tall, dark, and mysterious creature", Arrays.asList("§7+2% Damage to Endermen"),
            Arrays.asList(5, 15, 30, 60, 150, 300, 600, 1500, 3000, 6000)
        ));
        
        bestiaryEntries.put(EntityType.BLAZE, new BestiaryEntry(
            EntityType.BLAZE, "Blaze", BestiaryTier.UNCOMMON, BestiaryCategory.NETHER,
            "§7A fiery nether creature", Arrays.asList("§7+2% Damage to Blazes"),
            Arrays.asList(5, 15, 30, 60, 150, 300, 600, 1500, 3000, 6000)
        ));
        
        bestiaryEntries.put(EntityType.GHAST, new BestiaryEntry(
            EntityType.GHAST, "Ghast", BestiaryTier.UNCOMMON, BestiaryCategory.NETHER,
            "§7A floating nether creature", Arrays.asList("§7+2% Damage to Ghasts"),
            Arrays.asList(5, 15, 30, 60, 150, 300, 600, 1500, 3000, 6000)
        ));
        
        // Rare Mobs
        bestiaryEntries.put(EntityType.WITHER_SKELETON, new BestiaryEntry(
            EntityType.WITHER_SKELETON, "Wither Skeleton", BestiaryTier.RARE, BestiaryCategory.NETHER,
            "§7A powerful nether skeleton", Arrays.asList("§7+3% Damage to Wither Skeletons"),
            Arrays.asList(3, 10, 20, 40, 100, 200, 400, 1000, 2000, 4000)
        ));
        
        bestiaryEntries.put(EntityType.ENDER_DRAGON, new BestiaryEntry(
            EntityType.ENDER_DRAGON, "Ender Dragon", BestiaryTier.LEGENDARY, BestiaryCategory.ENDER,
            "§7The ultimate end boss", Arrays.asList("§7+5% Damage to Ender Dragons"),
            Arrays.asList(1, 3, 5, 10, 25, 50, 100, 250, 500, 1000)
        ));
        
        bestiaryEntries.put(EntityType.WITHER, new BestiaryEntry(
            EntityType.WITHER, "Wither", BestiaryTier.LEGENDARY, BestiaryCategory.NETHER,
            "§7A powerful nether boss", Arrays.asList("§7+5% Damage to Withers"),
            Arrays.asList(1, 3, 5, 10, 25, 50, 100, 250, 500, 1000)
        ));
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            EntityType entityType = event.getEntity().getType();
            
            addKill(player, entityType);
        }
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        if (title.contains("Bestiary")) {
            event.setCancelled(true);
            handleBestiaryGUIClick(player, event.getSlot());
        }
    }
    
    public void addKill(Player player, EntityType entityType) {
        BestiaryEntry entry = bestiaryEntries.get(entityType);
        if (entry == null) return;
        
        PlayerBestiary bestiary = getPlayerBestiary(player.getUniqueId());
        MobKillData killData = bestiary.getMobKillData(entityType);
        
        int oldKills = killData.getKills();
        int oldLevel = killData.getLevel();
        
        killData.addKill();
        
        int newKills = killData.getKills();
        int newLevel = killData.getLevel();
        
        if (newLevel > oldLevel) {
            player.sendMessage("§a§lBESTIARY LEVEL UP!");
            player.sendMessage("§7Mob: §e" + entry.getName());
            player.sendMessage("§7Level: §e" + oldLevel + " §7→ §e" + newLevel);
            player.sendMessage("§7Kills: §e" + newKills);
            
            // Apply level rewards
            applyBestiaryRewards(player, entry, newLevel);
        }
    }
    
    private void applyBestiaryRewards(Player player, BestiaryEntry entry, int level) {
        if (level <= entry.getRewards().size()) {
            String reward = entry.getRewards().get(level - 1);
            player.sendMessage("§aReward: " + reward);
            
            // Apply the actual reward here
            // This would need to be implemented based on the reward type
        }
    }
    
    public void openBestiaryGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lBestiary");
        
        // Bestiary categories
        addGUIItem(gui, 10, Material.ROTTEN_FLESH, "§8§lUndead", 
            Arrays.asList("§7Track undead creatures", "§7• Zombies, Skeletons", "", "§eClick to view"));
        
        addGUIItem(gui, 11, Material.SPIDER_EYE, "§8§lArachnid", 
            Arrays.asList("§7Track arachnid creatures", "§7• Spiders, Cave Spiders", "", "§eClick to view"));
        
        addGUIItem(gui, 12, Material.GUNPOWDER, "§a§lExplosive", 
            Arrays.asList("§7Track explosive creatures", "§7• Creepers, TNT", "", "§eClick to view"));
        
        addGUIItem(gui, 13, Material.ENDER_PEARL, "§5§lEnder", 
            Arrays.asList("§7Track ender creatures", "§7• Endermen, Ender Dragons", "", "§eClick to view"));
        
        addGUIItem(gui, 14, Material.BLAZE_POWDER, "§c§lNether", 
            Arrays.asList("§7Track nether creatures", "§7• Blazes, Ghasts, Withers", "", "§eClick to view"));
        
        // Statistics
        PlayerBestiary bestiary = getPlayerBestiary(player.getUniqueId());
        addGUIItem(gui, 19, Material.GOLD_INGOT, "§6§lStatistics", 
            Arrays.asList("§7Total Kills: §e" + bestiary.getTotalKills(),
                         "§7Mobs Tracked: §e" + bestiary.getTrackedMobs().size(),
                         "§7Bestiary Level: §e" + bestiary.getOverallLevel(), "", "§eClick to view"));
        
        // Achievements
        addGUIItem(gui, 20, Material.GOLD_INGOT, "§e§lAchievements", 
            Arrays.asList("§7View bestiary achievements", "§7• Milestones", "§7• Rewards", "", "§eClick to view"));
        
        // Close button
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", 
            Arrays.asList("§7Close the bestiary", "", "§eClick to close"));
        
        player.openInventory(gui);
    }
    
    public void openBestiaryCategoryGUI(Player player, BestiaryCategory category) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lBestiary - " + category.getDisplayName());
        
        List<BestiaryEntry> categoryEntries = bestiaryEntries.values().stream()
            .filter(entry -> entry.getCategory() == category)
            .toList();
        
        PlayerBestiary bestiary = getPlayerBestiary(player.getUniqueId());
        
        int slot = 10;
        for (BestiaryEntry entry : categoryEntries) {
            if (slot >= 44) break;
            
            MobKillData killData = bestiary.getMobKillData(entry.getEntityType());
            ItemStack itemStack = createBestiaryItemStack(entry, killData);
            gui.setItem(slot, itemStack);
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to bestiary", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    public void openBestiaryMobGUI(Player player, EntityType entityType) {
        BestiaryEntry entry = bestiaryEntries.get(entityType);
        if (entry == null) return;
        
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lBestiary - " + entry.getName());
        
        PlayerBestiary bestiary = getPlayerBestiary(player.getUniqueId());
        MobKillData killData = bestiary.getMobKillData(entityType);
        
        // Mob display
        ItemStack mobItem = new ItemStack(Material.SPAWNER);
        ItemMeta meta = mobItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§e" + entry.getName()));
            List<String> lore = Arrays.asList(
                "§7Tier: " + entry.getTier().getDisplayName(),
                "§7Category: " + entry.getCategory().getDisplayName(),
                "§7Kills: §e" + killData.getKills(),
                "§7Level: §e" + killData.getLevel(),
                "",
                "§7Description:",
                entry.getDescription()
            );
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            mobItem.setItemMeta(meta);
        }
        gui.setItem(4, mobItem);
        
        // Level milestones
        int slot = 10;
        for (int i = 0; i < entry.getMilestones().size() && slot < 18; i++) {
            int milestone = entry.getMilestones().get(i);
            boolean achieved = killData.getKills() >= milestone;
            
            Material material = achieved ? Material.EMERALD : Material.GRAY_DYE;
            String status = achieved ? "§a✓" : "§7✗";
            
            addGUIItem(gui, slot, material, status + " §7Level " + (i + 1), 
                Arrays.asList("§7Kills Required: §e" + milestone,
                             "§7Status: " + (achieved ? "§aAchieved" : "§cNot Achieved"),
                             "§7Reward: " + (i < entry.getRewards().size() ? entry.getRewards().get(i) : "§7None")));
            slot++;
        }
        
        // Navigation
        addGUIItem(gui, 22, Material.ARROW, "§7§l← Back", 
            Arrays.asList("§7Return to category", "", "§eClick to go back"));
        
        player.openInventory(gui);
    }
    
    private ItemStack createBestiaryItemStack(BestiaryEntry entry, MobKillData killData) {
        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§e" + entry.getName()));
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Tier: " + entry.getTier().getDisplayName());
            lore.add("§7Category: " + entry.getCategory().getDisplayName());
            lore.add("§7Kills: §e" + killData.getKills());
            lore.add("§7Level: §e" + killData.getLevel());
            lore.add("");
            lore.add("§7Description:");
            lore.add(entry.getDescription());
            lore.add("");
            lore.add("§eClick to view details");
            
            meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private void handleBestiaryGUIClick(Player player, int slot) {
        switch (slot) {
            case 10: // Undead
                openBestiaryCategoryGUI(player, BestiaryCategory.UNDEAD);
                break;
            case 11: // Arachnid
                openBestiaryCategoryGUI(player, BestiaryCategory.ARACHNID);
                break;
            case 12: // Explosive
                openBestiaryCategoryGUI(player, BestiaryCategory.EXPLOSIVE);
                break;
            case 13: // Ender
                openBestiaryCategoryGUI(player, BestiaryCategory.ENDER);
                break;
            case 14: // Nether
                openBestiaryCategoryGUI(player, BestiaryCategory.NETHER);
                break;
            case 19: // Statistics
                player.sendMessage("§eStatistics coming soon!");
                break;
            case 20: // Achievements
                player.sendMessage("§eAchievements coming soon!");
                break;
            case 49: // Close
                player.closeInventory();
                break;
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
    
    public PlayerBestiary getPlayerBestiary(UUID playerId) {
        return playerBestiaries.computeIfAbsent(playerId, k -> new PlayerBestiary(playerId));
    }
    
    public BestiaryEntry getBestiaryEntry(EntityType entityType) {
        return bestiaryEntries.get(entityType);
    }
    
    public Map<EntityType, BestiaryEntry> getAllBestiaryEntries() {
        return new HashMap<>(bestiaryEntries);
    }
    
    // Bestiary Classes
    public static class BestiaryEntry {
        private final EntityType entityType;
        private final String name;
        private final BestiaryTier tier;
        private final BestiaryCategory category;
        private final String description;
        private final List<String> rewards;
        private final List<Integer> milestones;
        
        public BestiaryEntry(EntityType entityType, String name, BestiaryTier tier, BestiaryCategory category,
                           String description, List<String> rewards, List<Integer> milestones) {
            this.entityType = entityType;
            this.name = name;
            this.tier = tier;
            this.category = category;
            this.description = description;
            this.rewards = rewards;
            this.milestones = milestones;
        }
        
        // Getters
        public EntityType getEntityType() { return entityType; }
        public String getName() { return name; }
        public BestiaryTier getTier() { return tier; }
        public BestiaryCategory getCategory() { return category; }
        public String getDescription() { return description; }
        public List<String> getRewards() { return rewards; }
        public List<Integer> getMilestones() { return milestones; }
    }
    
    public enum BestiaryTier {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0);
        
        private final String displayName;
        private final double multiplier;
        
        BestiaryTier(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public enum BestiaryCategory {
        UNDEAD("§8Undead", Material.ROTTEN_FLESH),
        ARACHNID("§8Arachnid", Material.SPIDER_EYE),
        EXPLOSIVE("§aExplosive", Material.GUNPOWDER),
        ENDER("§5Ender", Material.ENDER_PEARL),
        NETHER("§cNether", Material.BLAZE_POWDER);
        
        private final String displayName;
        private final Material icon;
        
        BestiaryCategory(String displayName, Material icon) {
            this.displayName = displayName;
            this.icon = icon;
        }
        
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
    }
    
    public static class PlayerBestiary {
        private final UUID playerId;
        private final Map<EntityType, MobKillData> mobKillData = new ConcurrentHashMap<>();
        
        public PlayerBestiary(UUID playerId) {
            this.playerId = playerId;
        }
        
        public MobKillData getMobKillData(EntityType entityType) {
            return mobKillData.computeIfAbsent(entityType, k -> new MobKillData(entityType));
        }
        
        public int getTotalKills() {
            return mobKillData.values().stream().mapToInt(MobKillData::getKills).sum();
        }
        
        public int getOverallLevel() {
            return mobKillData.values().stream().mapToInt(MobKillData::getLevel).sum();
        }
        
        public Set<EntityType> getTrackedMobs() {
            return mobKillData.keySet();
        }
        
        public UUID getPlayerId() { return playerId; }
        public Map<EntityType, MobKillData> getMobKillData() { return mobKillData; }
    }
    
    public static class MobKillData {
        private final EntityType entityType;
        private int kills;
        private int level;
        
        public MobKillData(EntityType entityType) {
            this.entityType = entityType;
            this.kills = 0;
            this.level = 0;
        }
        
        public void addKill() {
            this.kills++;
            updateLevel();
        }
        
        private void updateLevel() {
            // Simple level calculation based on kills
            this.level = Math.min(10, (int) Math.floor(Math.log10(kills + 1) * 3) + 1);
        }
        
        public EntityType getEntityType() { return entityType; }
        public int getKills() { return kills; }
        public int getLevel() { return level; }
    }
}
