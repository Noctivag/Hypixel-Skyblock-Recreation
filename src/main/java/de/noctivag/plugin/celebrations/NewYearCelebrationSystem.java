package de.noctivag.plugin.celebrations;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * New Year Celebration System - Hypixel Skyblock Style
 */
public class NewYearCelebrationSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCelebrationData> playerCelebrationData = new ConcurrentHashMap<>();
    private final Map<CelebrationType, CelebrationConfig> celebrationConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> celebrationTasks = new ConcurrentHashMap<>();
    
    public NewYearCelebrationSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeCelebrationConfigs();
        startCelebrationUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeCelebrationConfigs() {
        celebrationConfigs.put(CelebrationType.FIREWORKS, new CelebrationConfig(
            "Fireworks", "§eFireworks", Material.FIREWORK_ROCKET,
            "§7Celebrate the new year with fireworks!",
            CelebrationCategory.DECORATION, CelebrationRarity.COMMON, 1,
            Arrays.asList("§7- Launch fireworks", "§7- Create beautiful displays", "§7- Celebrate with others"),
            Arrays.asList("§7- 1x Firework Rocket", "§7- 1x Gunpowder")
        ));
        
        celebrationConfigs.put(CelebrationType.PARTY_HAT, new CelebrationConfig(
            "Party Hat", "§aParty Hat", Material.LEATHER_HELMET,
            "§7Wear a festive party hat for the new year!",
            CelebrationCategory.COSMETIC, CelebrationRarity.COMMON, 1,
            Arrays.asList("§7- Festive appearance", "§7- New year spirit", "§7- Party vibes"),
            Arrays.asList("§7- 1x Leather", "§7- 1x Dye")
        ));
        
        celebrationConfigs.put(CelebrationType.CONFETTI, new CelebrationConfig(
            "Confetti", "§dConfetti", Material.PAPER,
            "§7Throw confetti to celebrate the new year!",
            CelebrationCategory.DECORATION, CelebrationRarity.COMMON, 1,
            Arrays.asList("§7- Throw confetti", "§7- Colorful celebration", "§7- Festive atmosphere"),
            Arrays.asList("§7- 1x Paper", "§7- 1x Dye")
        ));
        
        celebrationConfigs.put(CelebrationType.NEW_YEAR_CAKE, new CelebrationConfig(
            "New Year Cake", "§6New Year Cake", Material.CAKE,
            "§7A special cake to celebrate the new year!",
            CelebrationCategory.FOOD, CelebrationRarity.UNCOMMON, 1,
            Arrays.asList("§7- Special cake", "§7- New year treat", "§7- Celebration food"),
            Arrays.asList("§7- 1x Cake", "§7- 1x Sugar", "§7- 1x Egg")
        ));
        
        celebrationConfigs.put(CelebrationType.CELEBRATION_BANNER, new CelebrationConfig(
            "Celebration Banner", "§cCelebration Banner", Material.WHITE_BANNER,
            "§7A festive banner for the new year celebration!",
            CelebrationCategory.DECORATION, CelebrationRarity.UNCOMMON, 1,
            Arrays.asList("§7- Festive banner", "§7- Decorative item", "§7- Celebration decoration"),
            Arrays.asList("§7- 1x Banner", "§7- 1x Dye", "§7- 1x Stick")
        ));
        
        celebrationConfigs.put(CelebrationType.NEW_YEAR_GIFT, new CelebrationConfig(
            "New Year Gift", "§5New Year Gift", Material.CHEST,
            "§7A special gift box for the new year!",
            CelebrationCategory.GIFT, CelebrationRarity.RARE, 1,
            Arrays.asList("§7- Special gift", "§7- New year surprise", "§7- Celebration reward"),
            Arrays.asList("§7- 1x Chest", "§7- 1x Wrapping Paper", "§7- 1x Bow")
        ));
    }
    
    private void startCelebrationUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerCelebrationData> entry : playerCelebrationData.entrySet()) {
                    PlayerCelebrationData celebrationData = entry.getValue();
                    celebrationData.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 60L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("New Year") || displayName.contains("Celebration")) {
            openCelebrationGUI(player);
        }
    }
    
    public void openCelebrationGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§e§lNew Year Celebration");
        
        addGUIItem(gui, 10, Material.FIREWORK_ROCKET, "§e§lFireworks", "§7Launch fireworks to celebrate!");
        addGUIItem(gui, 11, Material.LEATHER_HELMET, "§a§lParty Hat", "§7Wear a festive party hat!");
        addGUIItem(gui, 12, Material.PAPER, "§d§lConfetti", "§7Throw confetti to celebrate!");
        addGUIItem(gui, 13, Material.CAKE, "§6§lNew Year Cake", "§7Enjoy a special new year cake!");
        addGUIItem(gui, 14, Material.WHITE_BANNER, "§c§lCelebration Banner", "§7Display a festive banner!");
        addGUIItem(gui, 15, Material.CHEST, "§5§lNew Year Gift", "§7Open a special new year gift!");
        
        player.openInventory(gui);
        player.sendMessage("§aNew Year Celebration GUI geöffnet!");
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
    
    public void useCelebrationItem(Player player, CelebrationType celebrationType) {
        PlayerCelebrationData celebrationData = getPlayerCelebrationData(player.getUniqueId());
        CelebrationConfig config = celebrationConfigs.get(celebrationType);
        
        if (config == null) return;
        
        celebrationData.addCelebrationUsage(celebrationType);
        
        switch (celebrationType) {
            case FIREWORKS:
                player.sendMessage("§e§l🎆 FIREWORKS LAUNCHED! 🎆");
                player.sendMessage("§7Happy New Year!");
                break;
            case PARTY_HAT:
                player.sendMessage("§a§l🎉 PARTY HAT EQUIPPED! 🎉");
                player.sendMessage("§7You're ready to party!");
                break;
            case CONFETTI:
                player.sendMessage("§d§l🎊 CONFETTI THROWN! 🎊");
                player.sendMessage("§7Let's celebrate!");
                break;
            case NEW_YEAR_CAKE:
                player.sendMessage("§6§l🍰 NEW YEAR CAKE ENJOYED! 🍰");
                player.sendMessage("§7Delicious celebration treat!");
                break;
            case CELEBRATION_BANNER:
                player.sendMessage("§c§l🏳️ CELEBRATION BANNER DISPLAYED! 🏳️");
                player.sendMessage("§7Festive decoration active!");
                break;
            case NEW_YEAR_GIFT:
                player.sendMessage("§5§l🎁 NEW YEAR GIFT OPENED! 🎁");
                player.sendMessage("§7Happy New Year surprise!");
                break;
            case BALLOONS:
                player.sendMessage("§e§l🎈 BALLOONS RELEASED! 🎈");
                player.sendMessage("§7Floating celebration!");
                break;
            case STREAMERS:
                player.sendMessage("§a§l🎀 STREAMERS UNFURLED! 🎀");
                player.sendMessage("§7Colorful celebration!");
                break;
            case PARTY_POPPERS:
                player.sendMessage("§d§l🎉 PARTY POPPERS FIRED! 🎉");
                player.sendMessage("§7Pop goes the celebration!");
                break;
            case CELEBRATION_MUSIC:
                player.sendMessage("§b§l🎵 CELEBRATION MUSIC PLAYING! 🎵");
                player.sendMessage("§7Dance to the New Year beat!");
                break;
            case NEW_YEAR_DECORATIONS:
                player.sendMessage("§6§l🏮 NEW YEAR DECORATIONS PLACED! 🏮");
                player.sendMessage("§7Festive atmosphere created!");
                break;
        }
    }
    
    public PlayerCelebrationData getPlayerCelebrationData(UUID playerId) {
        return playerCelebrationData.computeIfAbsent(playerId, k -> new PlayerCelebrationData(playerId));
    }
    
    public enum CelebrationType {
        FIREWORKS, PARTY_HAT, CONFETTI, NEW_YEAR_CAKE, CELEBRATION_BANNER, NEW_YEAR_GIFT,
        BALLOONS, STREAMERS, PARTY_POPPERS, CELEBRATION_MUSIC, NEW_YEAR_DECORATIONS
    }
    
    public enum CelebrationCategory {
        DECORATION("§eDecoration", "§7Decorative items"),
        COSMETIC("§aCosmetic", "§7Cosmetic items"),
        FOOD("§6Food", "§7Food items"),
        GIFT("§5Gift", "§7Gift items"),
        MUSIC("§bMusic", "§7Music items"),
        PARTY("§dParty", "§7Party items");
        
        private final String displayName;
        private final String description;
        
        CelebrationCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum CelebrationRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0);
        
        private final String displayName;
        private final double multiplier;
        
        CelebrationRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class CelebrationConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final CelebrationCategory category;
        private final CelebrationRarity rarity;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public CelebrationConfig(String name, String displayName, Material icon, String description,
                                CelebrationCategory category, CelebrationRarity rarity, int maxLevel,
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
        public CelebrationCategory getCategory() { return category; }
        public CelebrationRarity getRarity() { return rarity; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerCelebrationData {
        private final UUID playerId;
        private final Map<CelebrationType, Integer> celebrationUsage = new HashMap<>();
        private long lastUpdate;
        
        public PlayerCelebrationData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void addCelebrationUsage(CelebrationType type) {
            celebrationUsage.put(type, celebrationUsage.getOrDefault(type, 0) + 1);
        }
        
        public int getCelebrationUsage(CelebrationType type) {
            return celebrationUsage.getOrDefault(type, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
