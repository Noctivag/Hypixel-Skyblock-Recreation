package de.noctivag.skyblock.slayers;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Slayer System - Hypixel Skyblock Style
 * Implements all 4 slayer types with unique mechanics and rewards
 */
public class AdvancedSlayerSystem implements Listener {

    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerSlayerData> playerSlayerData = new ConcurrentHashMap<>();
    private final Map<UUID, SlayerQuest> activeSlayerQuests = new ConcurrentHashMap<>();
    private final Map<SlayerType, SlayerConfig> slayerConfigs = new HashMap<>();
    private final Map<UUID, SlayerBoss> activeBosses = new ConcurrentHashMap<>();

    public AdvancedSlayerSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;

        initializeSlayerConfigs();
        startSlayerUpdateTask();
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void initializeSlayerConfigs() {
        // Zombie Slayer
        slayerConfigs.put(SlayerType.ZOMBIE, new SlayerConfig(
            SlayerType.ZOMBIE,
            "§2Zombie Slayer",
            "§7Fight undead creatures and their powerful bosses!",
            Material.ROTTEN_FLESH,
            Arrays.asList(
                new SlayerTier(1, "Revenant Horror", 100, 5000, Arrays.asList("Rotten Flesh", "Zombie Heart")),
                new SlayerTier(2, "Revenant Sycophant", 200, 15000, Arrays.asList("Rotten Flesh", "Zombie Heart", "Foul Flesh")),
                new SlayerTier(3, "Revenant Champion", 300, 50000, Arrays.asList("Rotten Flesh", "Zombie Heart", "Foul Flesh", "Pestilence Rune")),
                new SlayerTier(4, "Revenant Horror", 500, 100000, Arrays.asList("Rotten Flesh", "Zombie Heart", "Foul Flesh", "Pestilence Rune", "Beheaded Horror")),
                new SlayerTier(5, "Atoned Horror", 1000, 200000, Arrays.asList("Rotten Flesh", "Zombie Heart", "Foul Flesh", "Pestilence Rune", "Beheaded Horror", "Revenant Catalyst"))
            ),
            Arrays.asList(
                new SlayerReward("Revenant Falchion", Material.IRON_SWORD, 1000, "§cRevenant Falchion"),
                new SlayerReward("Revenant Armor", Material.LEATHER_CHESTPLATE, 5000, "§cRevenant Armor"),
                new SlayerReward("Wand of Mending", Material.STICK, 10000, "§aWand of Mending"),
                new SlayerReward("Revenant Catalyst", Material.NETHER_STAR, 50000, "§dRevenant Catalyst")
            )
        ));

        // Spider Slayer
        slayerConfigs.put(SlayerType.SPIDER, new SlayerConfig(
            SlayerType.SPIDER,
            "§8Spider Slayer",
            "§7Fight arachnid creatures and their powerful bosses!",
            Material.STRING,
            Arrays.asList(
                new SlayerTier(1, "Tarantula Broodfather", 100, 5000, Arrays.asList("String", "Spider Eye", "Tarantula Web")),
                new SlayerTier(2, "Tarantula Vermin", 200, 15000, Arrays.asList("String", "Spider Eye", "Tarantula Web", "Toxic Arrow Poison")),
                new SlayerTier(3, "Tarantula Beast", 300, 50000, Arrays.asList("String", "Spider Eye", "Tarantula Web", "Toxic Arrow Poison", "Spider Catalyst")),
                new SlayerTier(4, "Tarantula Broodfather", 500, 100000, Arrays.asList("String", "Spider Eye", "Tarantula Web", "Toxic Arrow Poison", "Spider Catalyst", "Tarantula Talisman")),
                new SlayerTier(5, "Mutant Tarantula", 1000, 200000, Arrays.asList("String", "Spider Eye", "Tarantula Web", "Toxic Arrow Poison", "Spider Catalyst", "Tarantula Talisman", "Digested Mosquito"))
            ),
            Arrays.asList(
                new SlayerReward("Spider Sword", Material.WOODEN_SWORD, 1000, "§8Spider Sword"),
                new SlayerReward("Tarantula Armor", Material.LEATHER_CHESTPLATE, 5000, "§8Tarantula Armor"),
                new SlayerReward("Spider Boots", Material.LEATHER_BOOTS, 10000, "§8Spider Boots"),
                new SlayerReward("Spider Catalyst", Material.NETHER_STAR, 50000, "§dSpider Catalyst")
            )
        ));

        // Wolf Slayer
        slayerConfigs.put(SlayerType.WOLF, new SlayerConfig(
            SlayerType.WOLF,
            "§fWolf Slayer",
            "§7Fight wolf creatures and their powerful bosses!",
            Material.BONE,
            Arrays.asList(
                new SlayerTier(1, "Pack Spirit", 100, 5000, Arrays.asList("Bone", "Wolf Tooth", "Spirit Rune")),
                new SlayerTier(2, "Soul of the Alpha", 200, 15000, Arrays.asList("Bone", "Wolf Tooth", "Spirit Rune", "Spirit Wing")),
                new SlayerTier(3, "Howling Spirit", 300, 50000, Arrays.asList("Bone", "Wolf Tooth", "Spirit Rune", "Spirit Wing", "Spirit Stone")),
                new SlayerTier(4, "Sven Packmaster", 500, 100000, Arrays.asList("Bone", "Wolf Tooth", "Spirit Rune", "Spirit Wing", "Spirit Stone", "Red Claw Egg")),
                new SlayerTier(5, "Sven Alpha", 1000, 200000, Arrays.asList("Bone", "Wolf Tooth", "Spirit Rune", "Spirit Wing", "Spirit Stone", "Red Claw Egg", "Overflux Capacitor"))
            ),
            Arrays.asList(
                new SlayerReward("Pooch Sword", Material.IRON_SWORD, 1000, "§fPooch Sword"),
                new SlayerReward("Sven Armor", Material.LEATHER_CHESTPLATE, 5000, "§fSven Armor"),
                new SlayerReward("Spirit Boots", Material.LEATHER_BOOTS, 10000, "§fSpirit Boots"),
                new SlayerReward("Overflux Capacitor", Material.NETHER_STAR, 50000, "§dOverflux Capacitor")
            )
        ));

        // Enderman Slayer
        slayerConfigs.put(SlayerType.ENDERMAN, new SlayerConfig(
            SlayerType.ENDERMAN,
            "§5Enderman Slayer",
            "§7Fight enderman creatures and their powerful bosses!",
            Material.ENDER_PEARL,
            Arrays.asList(
                new SlayerTier(1, "Voidling Devotee", 100, 5000, Arrays.asList("Ender Pearl", "End Stone", "Void Fragment")),
                new SlayerTier(2, "Voidling Radical", 200, 15000, Arrays.asList("Ender Pearl", "End Stone", "Void Fragment", "Voidling Rune")),
                new SlayerTier(3, "Voidling Fanatic", 300, 50000, Arrays.asList("Ender Pearl", "End Stone", "Void Fragment", "Voidling Rune", "Void Catalyst")),
                new SlayerTier(4, "Voidling Extremist", 500, 100000, Arrays.asList("Ender Pearl", "End Stone", "Void Fragment", "Voidling Rune", "Void Catalyst", "Void Edge")),
                new SlayerTier(5, "Voidling Fanatic", 1000, 200000, Arrays.asList("Ender Pearl", "End Stone", "Void Fragment", "Voidling Rune", "Void Catalyst", "Void Edge", "Judgement Core"))
            ),
            Arrays.asList(
                new SlayerReward("Void Sword", Material.DIAMOND_SWORD, 1000, "§5Void Sword"),
                new SlayerReward("Void Armor", Material.LEATHER_CHESTPLATE, 5000, "§5Void Armor"),
                new SlayerReward("Void Boots", Material.LEATHER_BOOTS, 10000, "§5Void Boots"),
                new SlayerReward("Judgement Core", Material.NETHER_STAR, 50000, "§dJudgement Core")
            )
        ));
    }

    private void startSlayerUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (plugin.isEnabled()) {
                try {
                    updateActiveBosses();
                    updateSlayerQuests();
                    Thread.sleep(1000); // Every second = 1000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void updateActiveBosses() {
        for (Map.Entry<UUID, SlayerBoss> entry : activeBosses.entrySet()) {
            SlayerBoss boss = entry.getValue();
            if (boss.isAlive()) {
                boss.update();
            } else {
                activeBosses.remove(entry.getKey());
            }
        }
    }

    private void updateSlayerQuests() {
        for (Map.Entry<UUID, SlayerQuest> entry : activeSlayerQuests.entrySet()) {
            SlayerQuest quest = entry.getValue();
            if (quest.isActive()) {
                quest.update();
            } else {
                activeSlayerQuests.remove(entry.getKey());
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

        if (displayName.contains("Slayer") || displayName.contains("slayer")) {
            openSlayerGUI(player);
        }
    }

    public void openSlayerGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§c§lSlayer System"));

        // Add slayer types
        addGUIItem(gui, 10, Material.ROTTEN_FLESH, "§2§lZombie Slayer", "§7Fight undead creatures!");
        addGUIItem(gui, 11, Material.STRING, "§8§lSpider Slayer", "§7Fight arachnid creatures!");
        addGUIItem(gui, 12, Material.BONE, "§f§lWolf Slayer", "§7Fight wolf creatures!");
        addGUIItem(gui, 13, Material.ENDER_PEARL, "§5§lEnderman Slayer", "§7Fight enderman creatures!");

        // Add slayer management items
        addGUIItem(gui, 18, Material.BOOK, "§6§lMy Slayer Progress", "§7View your slayer progress.");
        addGUIItem(gui, 19, Material.DIAMOND_SWORD, "§c§lStart Slayer Quest", "§7Start a new slayer quest.");
        addGUIItem(gui, 20, Material.CHEST, "§e§lSlayer Rewards", "§7View available rewards.");
        addGUIItem(gui, 21, Material.EMERALD, "§a§lSlayer Shop", "§7Buy slayer items.");
        addGUIItem(gui, 22, Material.BARRIER, "§c§lCancel Quest", "§7Cancel your current quest.");

        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the slayer menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");

        player.openInventory(gui);
        player.sendMessage("§aSlayer GUI opened!");
    }

    public void openSlayerTypeGUI(Player player, SlayerType type) {
        SlayerConfig config = slayerConfigs.get(type);
        if (config == null) return;

        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§c§l" + config.getDisplayName()));

        // Add tier information
        int slot = 18;
        for (SlayerTier tier : config.getTiers()) {
            if (slot >= 45) break;

            List<String> lore = new ArrayList<>();
            lore.add("§7Tier: §e" + tier.getTier());
            lore.add("§7Health: §c" + tier.getHealth());
            lore.add("§7Cost: §6" + tier.getCost() + " coins");
            lore.add("§7");
            lore.add("§7Drops:");
            for (String drop : tier.getDrops()) {
                lore.add("§7- " + drop);
            }
            lore.add("§7");
            lore.add("§7Click to start quest!");

            addGUIItemWithLore(gui, slot, config.getIcon(), tier.getName(), lore);
            slot++;
        }

        // Add rewards
        addGUIItem(gui, 4, Material.DIAMOND, "§6§lRewards", "§7View available rewards for this slayer type.");

        // Navigation
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to slayer menu");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the slayer menu");

        player.openInventory(gui);
    }

    public void startSlayerQuest(Player player, SlayerType type, int tier) {
        SlayerConfig config = slayerConfigs.get(type);
        if (config == null) return;

        SlayerTier slayerTier = config.getTiers().stream()
            .filter(t -> t.getTier() == tier)
            .findFirst()
            .orElse(null);

        if (slayerTier == null) return;

        // Check if player has enough coins
        // This would integrate with the economy system

        // Create slayer quest
        SlayerQuest quest = new SlayerQuest(player.getUniqueId(), type, tier, slayerTier);
        activeSlayerQuests.put(player.getUniqueId(), quest);

        // Spawn slayer boss
        spawnSlayerBoss(player, type, tier, slayerTier);

        player.sendMessage("§aStarted " + slayerTier.getName() + " quest!");
    }

    private void spawnSlayerBoss(Player player, SlayerType type, int tier, SlayerTier slayerTier) {
        Location spawnLocation = player.getLocation().add(10, 0, 10);

        SlayerBoss boss = new SlayerBoss(
            player.getUniqueId(),
            type,
            tier,
            slayerTier,
            spawnLocation
        );

        activeBosses.put(player.getUniqueId(), boss);
        boss.spawn();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        // Check if it's a slayer boss
        for (SlayerBoss boss : activeBosses.values()) {
            if (boss.getEntity() != null && boss.getEntity().equals(entity)) {
                boss.onDeath();
                break;
            }
        }
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

    private void addGUIItemWithLore(Inventory gui, int slot, Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            List<Component> componentLore = new ArrayList<>();
            for (String line : lore) {
                componentLore.add(Component.text(line));
            }
            meta.lore(componentLore);
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }

    public PlayerSlayerData getPlayerSlayerData(UUID playerId) {
        return playerSlayerData.computeIfAbsent(playerId, k -> new PlayerSlayerData(playerId));
    }

    public SlayerQuest getActiveSlayerQuest(UUID playerId) {
        return activeSlayerQuests.get(playerId);
    }

    public SlayerBoss getActiveSlayerBoss(UUID playerId) {
        return activeBosses.get(playerId);
    }

    public enum SlayerType {
        ZOMBIE("§2Zombie", "§7Fight undead creatures"),
        SPIDER("§8Spider", "§7Fight arachnid creatures"),
        WOLF("§fWolf", "§7Fight wolf creatures"),
        ENDERMAN("§5Enderman", "§7Fight enderman creatures");

        private final String displayName;
        private final String description;

        SlayerType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }

    public static class SlayerConfig {
        private final SlayerType type;
        private final String displayName;
        private final String description;
        private final Material icon;
        private final List<SlayerTier> tiers;
        private final List<SlayerReward> rewards;

        public SlayerConfig(SlayerType type, String displayName, String description, Material icon,
                           List<SlayerTier> tiers, List<SlayerReward> rewards) {
            this.type = type;
            this.displayName = displayName;
            this.description = description;
            this.icon = icon;
            this.tiers = tiers;
            this.rewards = rewards;
        }

        public SlayerType getType() { return type; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public Material getIcon() { return icon; }
        public List<SlayerTier> getTiers() { return tiers; }
        public List<SlayerReward> getRewards() { return rewards; }
    }

    public static class SlayerTier {
        private final int tier;
        private final String name;
        private final int health;
        private final int cost;
        private final List<String> drops;

        public SlayerTier(int tier, String name, int health, int cost, List<String> drops) {
            this.tier = tier;
            this.name = name;
            this.health = health;
            this.cost = cost;
            this.drops = drops;
        }

        public int getTier() { return tier; }
        public String getName() { return name; }
        public int getHealth() { return health; }
        public int getCost() { return cost; }
        public List<String> getDrops() { return drops; }
    }

    public static class SlayerReward {
        private final String name;
        private final Material material;
        private final int cost;
        private final String displayName;

        public SlayerReward(String name, Material material, int cost, String displayName) {
            this.name = name;
            this.material = material;
            this.cost = cost;
            this.displayName = displayName;
        }

        public String getName() { return name; }
        public Material getMaterial() { return material; }
        public int getCost() { return cost; }
        public String getDisplayName() { return displayName; }
    }

    public static class PlayerSlayerData {
        private final UUID playerId;
        private final Map<SlayerType, Integer> slayerLevels = new HashMap<>();
        private final Map<SlayerType, Integer> slayerXP = new HashMap<>();
        private final Map<SlayerType, Integer> slayerKills = new HashMap<>();
        private final Map<SlayerType, Integer> slayerBossKills = new HashMap<>();

        public PlayerSlayerData(UUID playerId) {
            this.playerId = playerId;
            initializeSlayerData();
        }

        private void initializeSlayerData() {
            for (SlayerType type : SlayerType.values()) {
                slayerLevels.put(type, 0);
                slayerXP.put(type, 0);
                slayerKills.put(type, 0);
                slayerBossKills.put(type, 0);
            }
        }

        public UUID getPlayerId() { return playerId; }
        public int getSlayerLevel(SlayerType type) { return slayerLevels.getOrDefault(type, 0); }
        public int getSlayerXP(SlayerType type) { return slayerXP.getOrDefault(type, 0); }
        public int getSlayerKills(SlayerType type) { return slayerKills.getOrDefault(type, 0); }
        public int getSlayerBossKills(SlayerType type) { return slayerBossKills.getOrDefault(type, 0); }

        public void addSlayerXP(SlayerType type, int xp) {
            slayerXP.put(type, slayerXP.getOrDefault(type, 0) + xp);
            checkLevelUp(type);
        }

        public void addSlayerKill(SlayerType type) {
            slayerKills.put(type, slayerKills.getOrDefault(type, 0) + 1);
        }

        public void addSlayerBossKill(SlayerType type) {
            slayerBossKills.put(type, slayerBossKills.getOrDefault(type, 0) + 1);
        }

        private void checkLevelUp(SlayerType type) {
            int currentLevel = slayerLevels.getOrDefault(type, 0);
            int currentXP = slayerXP.getOrDefault(type, 0);
            int requiredXP = getRequiredXP(currentLevel + 1);

            if (currentXP >= requiredXP) {
                slayerLevels.put(type, currentLevel + 1);
                // Level up rewards and notifications
            }
        }

        private int getRequiredXP(int level) {
            return level * 1000; // Simple XP formula
        }
    }

    public static class SlayerQuest {
        private final UUID playerId;
        private final SlayerType type;
        private final int tier;
        private final SlayerTier slayerTier;
        private final long startTime;
        private boolean active;

        public SlayerQuest(UUID playerId, SlayerType type, int tier, SlayerTier slayerTier) {
            this.playerId = playerId;
            this.type = type;
            this.tier = tier;
            this.slayerTier = slayerTier;
            this.startTime = System.currentTimeMillis();
            this.active = true;
        }

        public UUID getPlayerId() { return playerId; }
        public SlayerType getType() { return type; }
        public int getTier() { return tier; }
        public SlayerTier getSlayerTier() { return slayerTier; }
        public long getStartTime() { return startTime; }
        public boolean isActive() { return active; }
        public void setActive(boolean active) { this.active = active; }

        public void update() {
            // Quest update logic
        }
    }

    public static class SlayerBoss {
        private final UUID playerId;
        private final SlayerType type;
        private final int tier;
        private final SlayerTier slayerTier;
        private final Location spawnLocation;
        private Entity entity;
        private int health;
        private boolean alive;

        public SlayerBoss(UUID playerId, SlayerType type, int tier, SlayerTier slayerTier, Location spawnLocation) {
            this.playerId = playerId;
            this.type = type;
            this.tier = tier;
            this.slayerTier = slayerTier;
            this.spawnLocation = spawnLocation;
            this.health = slayerTier.getHealth();
            this.alive = false;
        }

        public void spawn() {
            // Spawn boss entity based on type
            switch (type) {
                case ZOMBIE:
                    entity = spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
                    break;
                case SPIDER:
                    entity = spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SPIDER);
                    break;
                case WOLF:
                    entity = spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WOLF);
                    break;
                case ENDERMAN:
                    entity = spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ENDERMAN);
                    break;
            }

            if (entity != null) {
                alive = true;
                // Set boss properties
                entity.customName(Component.text(slayerTier.getName()));
                entity.setCustomNameVisible(true);
            }
        }

        public void update() {
            if (entity != null && entity.isValid()) {
                // Boss AI and behavior
            } else {
                alive = false;
            }
        }

        public void onDeath() {
            alive = false;
            // Give rewards and XP
        }

        public UUID getPlayerId() { return playerId; }
        public SlayerType getType() { return type; }
        public int getTier() { return tier; }
        public SlayerTier getSlayerTier() { return slayerTier; }
        public Entity getEntity() { return entity; }
        public int getHealth() { return health; }
        public boolean isAlive() { return alive; }
    }

    // Missing method for ItemCommands
    public void openSlayerWeaponsGUI(Player player) {
        // Placeholder implementation
        player.sendMessage("§7Opening Slayer Weapons GUI...");
        // TODO: Implement actual slayer weapons GUI
    }
}
