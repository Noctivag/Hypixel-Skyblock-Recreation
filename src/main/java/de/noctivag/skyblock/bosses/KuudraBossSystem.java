package de.noctivag.skyblock.bosses;
import net.kyori.adventure.text.Component;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class KuudraBossSystem implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerBossData> playerBossData = new ConcurrentHashMap<>();
    private final Map<BossType, BossConfig> bossConfigs = new HashMap<>();
    private final Map<BossPhase, PhaseConfig> phaseConfigs = new HashMap<>();
    
    public KuudraBossSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        
        initializeBossConfigs();
        initializePhaseConfigs();
        startBossUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeBossConfigs() {
        // Kuudra Boss
        bossConfigs.put(BossType.KUUDRA, new BossConfig(
            "Kuudra", "§c§lKuudra", Material.WITHER_SKELETON_SKULL,
            "§7A powerful sea creature boss.",
            BossRarity.MYTHIC, 1000, Arrays.asList("§7- Sea Attacks", "§7- Tentacle Strikes", "§7- Water Control"),
            Arrays.asList("§7- 1x Kuudra Shard", "§7- 1x Kuudra Scale", "§7- 1x Sea Crystal")
        ));
        
        bossConfigs.put(BossType.ENDER_DRAGON, new BossConfig(
            "Ender Dragon", "§5§lEnder Dragon", Material.DRAGON_HEAD,
            "§7The legendary end dragon boss.",
            BossRarity.LEGENDARY, 800, Arrays.asList("§7- Dragon Breath", "§7- Flight", "§7- End Crystals"),
            Arrays.asList("§7- 1x Dragon Shard", "§7- 1x Dragon Scale", "§7- 1x Dragon Egg")
        ));
        
        bossConfigs.put(BossType.WITHER, new BossConfig(
            "Wither", "§8§lWither", Material.WITHER_SKELETON_SKULL,
            "§7A powerful wither boss.",
            BossRarity.LEGENDARY, 600, Arrays.asList("§7- Wither Effect", "§7- Skull Projectiles", "§7- Regeneration"),
            Arrays.asList("§7- 1x Wither Shard", "§7- 1x Nether Star", "§7- 1x Wither Rose")
        ));
        
        bossConfigs.put(BossType.SEA_LEVIATHAN, new BossConfig(
            "Sea Leviathan", "§b§lSea Leviathan", Material.PRISMARINE_SHARD,
            "§7A massive sea creature boss.",
            BossRarity.EPIC, 500, Arrays.asList("§7- Water Attacks", "§7- Tidal Waves", "§7- Ocean Control"),
            Arrays.asList("§7- 1x Leviathan Shard", "§7- 1x Prismarine Scale", "§7- 1x Ocean Crystal")
        ));
        
        bossConfigs.put(BossType.VOID_LORD, new BossConfig(
            "Void Lord", "§5§lVoid Lord", Material.END_CRYSTAL,
            "§7A powerful void entity boss.",
            BossRarity.MYTHIC, 1200, Arrays.asList("§7- Void Attacks", "§7- Teleportation", "§7- Void Control"),
            Arrays.asList("§7- 1x Void Shard", "§7- 1x Void Crystal", "§7- 1x End Stone")
        ));
    }
    
    private void initializePhaseConfigs() {
        // Kuudra Phases
        phaseConfigs.put(BossPhase.KUUDRA_PHASE_1, new PhaseConfig(
            "Kuudra Phase 1", "§c§lKuudra Phase 1", Material.WATER_BUCKET,
            "§7Kuudra emerges from the depths.",
            PhaseDifficulty.EASY, 200, Arrays.asList("§7- Basic Water Attacks", "§7- Tentacle Strikes"),
            Arrays.asList("§7- 1x Kuudra Fragment", "§7- 1x Sea Water")
        ));
        
        phaseConfigs.put(BossPhase.KUUDRA_PHASE_2, new PhaseConfig(
            "Kuudra Phase 2", "§c§lKuudra Phase 2", Material.PRISMARINE_SHARD,
            "§7Kuudra becomes more aggressive.",
            PhaseDifficulty.MEDIUM, 300, Arrays.asList("§7- Enhanced Water Attacks", "§7- Multiple Tentacles"),
            Arrays.asList("§7- 1x Kuudra Fragment", "§7- 1x Prismarine Shard")
        ));
        
        phaseConfigs.put(BossPhase.KUUDRA_PHASE_3, new PhaseConfig(
            "Kuudra Phase 3", "§c§lKuudra Phase 3", Material.HEART_OF_THE_SEA,
            "§7Kuudra reaches its final form.",
            PhaseDifficulty.HARD, 500, Arrays.asList("§7- Devastating Water Attacks", "§7- Sea Control", "§7- Tidal Waves"),
            Arrays.asList("§7- 1x Kuudra Shard", "§7- 1x Heart of the Sea")
        ));
        
        // Ender Dragon Phases
        phaseConfigs.put(BossPhase.DRAGON_PHASE_1, new PhaseConfig(
            "Dragon Phase 1", "§5§lDragon Phase 1", Material.END_STONE,
            "§7The dragon awakens.",
            PhaseDifficulty.EASY, 150, Arrays.asList("§7- Basic Dragon Breath", "§7- Flight Attacks"),
            Arrays.asList("§7- 1x Dragon Fragment", "§7- 1x End Stone")
        ));
        
        phaseConfigs.put(BossPhase.DRAGON_PHASE_2, new PhaseConfig(
            "Dragon Phase 2", "§5§lDragon Phase 2", Material.END_CRYSTAL,
            "§7The dragon becomes more powerful.",
            PhaseDifficulty.MEDIUM, 250, Arrays.asList("§7- Enhanced Dragon Breath", "§7- Crystal Healing"),
            Arrays.asList("§7- 1x Dragon Fragment", "§7- 1x End Crystal")
        ));
        
        phaseConfigs.put(BossPhase.DRAGON_PHASE_3, new PhaseConfig(
            "Dragon Phase 3", "§5§lDragon Phase 3", Material.DRAGON_EGG,
            "§7The dragon reaches its peak power.",
            PhaseDifficulty.HARD, 400, Arrays.asList("§7- Devastating Dragon Breath", "§7- End Control", "§7- Dragon Roar"),
            Arrays.asList("§7- 1x Dragon Shard", "§7- 1x Dragon Egg")
        ));
    }
    
    private void startBossUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                updateAllPlayerBossData();
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L); // Update every minute
    }
    
    private void updateAllPlayerBossData() {
        for (PlayerBossData data : playerBossData.values()) {
            data.update();
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Boss Summoner")) {
            openBossGUI(player);
        }
    }
    
    public void openBossGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§c§lBoss System"));
        
        // Add boss categories
        addGUIItem(gui, 10, Material.WITHER_SKELETON_SKULL, "§c§lKuudra", "§7Fight the Kuudra boss.");
        addGUIItem(gui, 11, Material.DRAGON_HEAD, "§5§lEnder Dragon", "§7Fight the Ender Dragon boss.");
        addGUIItem(gui, 12, Material.WITHER_SKELETON_SKULL, "§8§lWither", "§7Fight the Wither boss.");
        addGUIItem(gui, 13, Material.PRISMARINE_SHARD, "§b§lSea Leviathan", "§7Fight the Sea Leviathan boss.");
        addGUIItem(gui, 14, Material.END_CRYSTAL, "§5§lVoid Lord", "§7Fight the Void Lord boss.");
        
        // Add boss phases
        addGUIItem(gui, 19, Material.WATER_BUCKET, "§c§lKuudra Phase 1", "§7Fight Kuudra Phase 1.");
        addGUIItem(gui, 20, Material.PRISMARINE_SHARD, "§c§lKuudra Phase 2", "§7Fight Kuudra Phase 2.");
        addGUIItem(gui, 21, Material.HEART_OF_THE_SEA, "§c§lKuudra Phase 3", "§7Fight Kuudra Phase 3.");
        addGUIItem(gui, 22, Material.END_STONE, "§5§lDragon Phase 1", "§7Fight Dragon Phase 1.");
        addGUIItem(gui, 23, Material.END_CRYSTAL, "§5§lDragon Phase 2", "§7Fight Dragon Phase 2.");
        addGUIItem(gui, 24, Material.DRAGON_EGG, "§5§lDragon Phase 3", "§7Fight Dragon Phase 3.");
        
        // Add boss rewards
        addGUIItem(gui, 28, Material.NETHER_STAR, "§6§lBoss Rewards", "§7View boss rewards.");
        addGUIItem(gui, 29, Material.ENCHANTED_BOOK, "§b§lBoss Enchantments", "§7View boss enchantments.");
        addGUIItem(gui, 30, Material.BEACON, "§e§lBoss Mastery", "§7View boss mastery.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the GUI.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aBoss GUI geöffnet!"));
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(description).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerBossData getPlayerBossData(UUID playerId) {
        return playerBossData.computeIfAbsent(playerId, k -> new PlayerBossData(playerId));
    }
    
    public BossConfig getBossConfig(BossType type) {
        return bossConfigs.get(type);
    }
    
    public PhaseConfig getPhaseConfig(BossPhase phase) {
        return phaseConfigs.get(phase);
    }
    
    public List<BossType> getAllBossTypes() {
        return new ArrayList<>(bossConfigs.keySet());
    }
    
    public List<BossPhase> getAllBossPhases() {
        return new ArrayList<>(phaseConfigs.keySet());
    }
    
    public enum BossType {
        KUUDRA, ENDER_DRAGON, WITHER, SEA_LEVIATHAN, VOID_LORD
    }
    
    public enum BossPhase {
        KUUDRA_PHASE_1, KUUDRA_PHASE_2, KUUDRA_PHASE_3,
        DRAGON_PHASE_1, DRAGON_PHASE_2, DRAGON_PHASE_3
    }
    
    public enum BossRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);
        
        private final String displayName;
        private final double multiplier;
        
        BossRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public enum PhaseDifficulty {
        EASY("§aEasy", 1.0),
        MEDIUM("§eMedium", 1.5),
        HARD("§cHard", 2.0),
        EXTREME("§5Extreme", 3.0);
        
        private final String displayName;
        private final double multiplier;
        
        PhaseDifficulty(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class BossConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final BossRarity rarity;
        private final int health;
        private final List<String> abilities;
        private final List<String> drops;
        
        public BossConfig(String name, String displayName, Material material, String description,
                         BossRarity rarity, int health, List<String> abilities, List<String> drops) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.rarity = rarity;
            this.health = health;
            this.abilities = abilities;
            this.drops = drops;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public BossRarity getRarity() { return rarity; }
        public int getHealth() { return health; }
        public List<String> getAbilities() { return abilities; }
        public List<String> getDrops() { return drops; }
    }
    
    public static class PhaseConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final PhaseDifficulty difficulty;
        private final int health;
        private final List<String> abilities;
        private final List<String> drops;
        
        public PhaseConfig(String name, String displayName, Material material, String description,
                          PhaseDifficulty difficulty, int health, List<String> abilities, List<String> drops) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.difficulty = difficulty;
            this.health = health;
            this.abilities = abilities;
            this.drops = drops;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public PhaseDifficulty getDifficulty() { return difficulty; }
        public int getHealth() { return health; }
        public List<String> getAbilities() { return abilities; }
        public List<String> getDrops() { return drops; }
    }
    
    public static class PlayerBossData {
        private final UUID playerId;
        private final Map<BossType, Integer> bossesKilled = new HashMap<>();
        private final Map<BossPhase, Integer> phasesCompleted = new HashMap<>();
        private final Map<BossType, Integer> bossLevels = new HashMap<>();
        private long lastUpdate;
        
        public PlayerBossData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void addBossKill(BossType type) {
            bossesKilled.put(type, bossesKilled.getOrDefault(type, 0) + 1);
        }
        
        public void addPhaseCompletion(BossPhase phase) {
            phasesCompleted.put(phase, phasesCompleted.getOrDefault(phase, 0) + 1);
        }
        
        public void levelUpBoss(BossType type) {
            bossLevels.put(type, bossLevels.getOrDefault(type, 0) + 1);
        }
        
        public int getBossesKilled(BossType type) {
            return bossesKilled.getOrDefault(type, 0);
        }
        
        public int getPhasesCompleted(BossPhase phase) {
            return phasesCompleted.getOrDefault(phase, 0);
        }
        
        public int getBossLevel(BossType type) {
            return bossLevels.getOrDefault(type, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
