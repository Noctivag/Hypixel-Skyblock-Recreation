package de.noctivag.skyblock.festivals;

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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * Spooky Festival System - Hypixel Skyblock Style
 */
public class SpookyFestivalSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerSpookyData> playerSpookyData = new ConcurrentHashMap<>();
    private final Map<SpookyType, SpookyConfig> spookyConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> spookyTasks = new ConcurrentHashMap<>();
    
    public SpookyFestivalSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeSpookyConfigs();
        startSpookyUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeSpookyConfigs() {
        spookyConfigs.put(SpookyType.CANDY, new SpookyConfig(
            "Candy", "§eCandy", Material.SUGAR,
            "§7Collect candy during the spooky festival!",
            SpookyCategory.CURRENCY, SpookyRarity.COMMON, 1,
            Arrays.asList("§7- Collect candy", "§7- Trade for rewards", "§7- Festival currency"),
            Arrays.asList("§7- Kill mobs", "§7- Find candy bags")
        ));
        
        spookyConfigs.put(SpookyType.SPOOKY_ARMOR, new SpookyConfig(
            "Spooky Armor", "§5Spooky Armor", Material.LEATHER_CHESTPLATE,
            "§7Wear spooky armor for the festival!",
            SpookyCategory.ARMOR, SpookyRarity.UNCOMMON, 1,
            Arrays.asList("§7- Spooky appearance", "§7- Festival bonus", "§7- Special effects"),
            Arrays.asList("§7- 1x Leather", "§7- 1x Purple Dye", "§7- 1x Black Dye")
        ));
        
        spookyConfigs.put(SpookyType.PUMPKIN_HEAD, new SpookyConfig(
            "Pumpkin Head", "§6Pumpkin Head", Material.PUMPKIN,
            "§7Wear a spooky pumpkin head!",
            SpookyCategory.COSMETIC, SpookyRarity.COMMON, 1,
            Arrays.asList("§7- Spooky appearance", "§7- Halloween vibes", "§7- Festival spirit"),
            Arrays.asList("§7- 1x Pumpkin", "§7- 1x Carved Pumpkin")
        ));
        
        spookyConfigs.put(SpookyType.SPOOKY_PET, new SpookyConfig(
            "Spooky Pet", "§5Spooky Pet", Material.SKELETON_SKULL,
            "§7A spooky pet for the festival!",
            SpookyCategory.PET, SpookyRarity.RARE, 1,
            Arrays.asList("§7- Spooky companion", "§7- Festival bonus", "§7- Special abilities"),
            Arrays.asList("§7- 1x Skeleton Skull", "§7- 1x Bone", "§7- 1x String")
        ));
        
        spookyConfigs.put(SpookyType.HAUNTED_HOUSE, new SpookyConfig(
            "Haunted House", "§8Haunted House", Material.DARK_OAK_WOOD,
            "§7Build a haunted house for the festival!",
            SpookyCategory.BUILDING, SpookyRarity.EPIC, 1,
            Arrays.asList("§7- Spooky building", "§7- Festival decoration", "§7- Haunted atmosphere"),
            Arrays.asList("§7- 1x Dark Oak Wood", "§7- 1x Cobweb", "§7- 1x Torch")
        ));
        
        spookyConfigs.put(SpookyType.SPOOKY_WEAPON, new SpookyConfig(
            "Spooky Weapon", "§5Spooky Weapon", Material.IRON_SWORD,
            "§7A spooky weapon for the festival!",
            SpookyCategory.WEAPON, SpookyRarity.UNCOMMON, 1,
            Arrays.asList("§7- Spooky weapon", "§7- Festival bonus", "§7- Special effects"),
            Arrays.asList("§7- 1x Iron Sword", "§7- 1x Purple Dye", "§7- 1x Black Dye")
        ));
    }
    
    private void startSpookyUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerSpookyData> entry : playerSpookyData.entrySet()) {
                    PlayerSpookyData spookyData = entry.getValue();
                    spookyData.update();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L);
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        
        Player player = event.getEntity().getKiller();
        PlayerSpookyData spookyData = getPlayerSpookyData(player.getUniqueId());
        
        // Give candy for killing mobs during spooky festival
        spookyData.addCandy(1);
        player.sendMessage(Component.text("§e+1 Candy! §7(Spooky Festival)"));
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Spooky") || displayName.contains("Halloween")) {
            openSpookyGUI(player);
        }
    }
    
    public void openSpookyGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§5§lSpooky Festival"));
        
        addGUIItem(gui, 10, Material.SUGAR, "§e§lCandy", "§7Collect candy during the festival!");
        addGUIItem(gui, 11, Material.LEATHER_CHESTPLATE, "§5§lSpooky Armor", "§7Wear spooky armor!");
        addGUIItem(gui, 12, Material.PUMPKIN, "§6§lPumpkin Head", "§7Wear a spooky pumpkin head!");
        addGUIItem(gui, 13, Material.SKELETON_SKULL, "§5§lSpooky Pet", "§7Get a spooky pet!");
        addGUIItem(gui, 14, Material.DARK_OAK_WOOD, "§8§lHaunted House", "§7Build a haunted house!");
        addGUIItem(gui, 15, Material.IRON_SWORD, "§5§lSpooky Weapon", "§7Get a spooky weapon!");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aSpooky Festival GUI geöffnet!"));
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            meta.lore(Arrays.asList(net.kyori.adventure.text.Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public void useSpookyItem(Player player, SpookyType spookyType) {
        PlayerSpookyData spookyData = getPlayerSpookyData(player.getUniqueId());
        SpookyConfig config = spookyConfigs.get(spookyType);
        
        if (config == null) return;
        
        spookyData.addSpookyUsage(spookyType);
        
        switch (spookyType) {
            case CANDY:
                player.sendMessage(Component.text("§e§l🍭 CANDY COLLECTED! 🍭"));
                player.sendMessage(Component.text("§7Spooky festival candy!"));
                break;
            case SPOOKY_ARMOR:
                player.sendMessage(Component.text("§5§l👻 SPOOKY ARMOR EQUIPPED! 👻"));
                player.sendMessage(Component.text("§7You look spooky!"));
                break;
            case PUMPKIN_HEAD:
                player.sendMessage(Component.text("§6§l🎃 PUMPKIN HEAD EQUIPPED! 🎃"));
                player.sendMessage(Component.text("§7Spooky Halloween vibes!"));
                break;
            case SPOOKY_PET:
                player.sendMessage(Component.text("§5§l💀 SPOOKY PET SUMMONED! 💀"));
                player.sendMessage(Component.text("§7Your spooky companion!"));
                break;
            case HAUNTED_HOUSE:
                player.sendMessage(Component.text("§8§l🏚️ HAUNTED HOUSE BUILT! 🏚️"));
                player.sendMessage(Component.text("§7Spooky building complete!"));
                break;
            case SPOOKY_WEAPON:
                player.sendMessage(Component.text("§5§l⚔️ SPOOKY WEAPON EQUIPPED! ⚔️"));
                player.sendMessage(Component.text("§7Spooky weapon ready!"));
                break;
        }
    }
    
    public PlayerSpookyData getPlayerSpookyData(UUID playerId) {
        return playerSpookyData.computeIfAbsent(playerId, k -> new PlayerSpookyData(playerId));
    }
    
    public enum SpookyType {
        CANDY, SPOOKY_ARMOR, PUMPKIN_HEAD, SPOOKY_PET, HAUNTED_HOUSE, SPOOKY_WEAPON,
        SPOOKY_DECORATIONS, HALLOWEEN_COSTUME, SPOOKY_MUSIC, SPOOKY_EFFECTS
    }
    
    public enum SpookyCategory {
        CURRENCY("§eCurrency", "§7Festival currency"),
        ARMOR("§5Armor", "§7Spooky armor"),
        COSMETIC("§6Cosmetic", "§7Spooky cosmetics"),
        PET("§5Pet", "§7Spooky pets"),
        BUILDING("§8Building", "§7Spooky buildings"),
        WEAPON("§5Weapon", "§7Spooky weapons"),
        DECORATION("§eDecoration", "§7Spooky decorations"),
        MUSIC("§bMusic", "§7Spooky music");
        
        private final String displayName;
        private final String description;
        
        SpookyCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum SpookyRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0);
        
        private final String displayName;
        private final double multiplier;
        
        SpookyRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class SpookyConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final SpookyCategory category;
        private final SpookyRarity rarity;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public SpookyConfig(String name, String displayName, Material icon, String description,
                           SpookyCategory category, SpookyRarity rarity, int maxLevel,
                           List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.maxLevel = maxLevel;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public SpookyCategory getCategory() { return category; }
        public SpookyRarity getRarity() { return rarity; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerSpookyData {
        private final UUID playerId;
        private int candy;
        private final Map<SpookyType, Integer> spookyUsage = new HashMap<>();
        private long lastUpdate;
        
        public PlayerSpookyData(UUID playerId) {
            this.playerId = playerId;
            this.candy = 0;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void addCandy(int amount) {
            this.candy += amount;
        }
        
        public void addSpookyUsage(SpookyType type) {
            spookyUsage.put(type, spookyUsage.getOrDefault(type, 0) + 1);
        }
        
        public int getCandy() {
            return candy;
        }
        
        public int getSpookyUsage(SpookyType type) {
            return spookyUsage.getOrDefault(type, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
