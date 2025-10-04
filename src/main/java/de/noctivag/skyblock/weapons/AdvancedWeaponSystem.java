package de.noctivag.skyblock.weapons;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Weapon System - Hypixel Skyblock Style
 * 
 * Features:
 * - Dynamic Weapons
 * - Weapon Abilities
 * - Weapon Stats
 * - Weapon Rarities
 * - Weapon Enchantments
 * - Weapon Reforges
 * - Weapon Gemstones
 * - Weapon Customization
 * - Weapon Progression
 */
public class AdvancedWeaponSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerWeapons> playerWeapons = new ConcurrentHashMap<>();
    private final Map<WeaponType, WeaponConfig> weaponConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> weaponTasks = new ConcurrentHashMap<>();
    
    public AdvancedWeaponSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeWeaponConfigs();
        startWeaponUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    private void initializeWeaponConfigs() {
        // Swords
        weaponConfigs.put(WeaponType.ASPECT_OF_THE_END, new WeaponConfig(
            "Aspect of the End", "§aAspect of the End", Material.DIAMOND_SWORD,
            "§7Teleport behind enemies and deal massive damage.",
            WeaponCategory.SWORD, WeaponRarity.RARE, 100, Arrays.asList("§7- Teleport Ability", "§7- High Damage"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 1x Diamond Sword")
        ));
        
        weaponConfigs.put(WeaponType.ASPECT_OF_THE_DRAGONS, new WeaponConfig(
            "Aspect of the Dragons", "§6Aspect of the Dragons", Material.DIAMOND_SWORD,
            "§7Summon dragon energy to devastate your enemies.",
            WeaponCategory.SWORD, WeaponRarity.LEGENDARY, 225, Arrays.asList("§7- Dragon Breath", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 1x Diamond Sword")
        ));
        
        weaponConfigs.put(WeaponType.MIDAS_SWORD, new WeaponConfig(
            "Midas Sword", "§6Midas Sword", Material.GOLDEN_SWORD,
            "§7A sword forged from pure gold with incredible power.",
            WeaponCategory.SWORD, WeaponRarity.LEGENDARY, 200, Arrays.asList("§7- Gold Touch", "§7- Wealth Bonus"),
            Arrays.asList("§7- 1x Gold Block", "§7- 1x Golden Sword")
        ));
        
        // Bows
        weaponConfigs.put(WeaponType.RUNAANS_BOW, new WeaponConfig(
            "Runaan's Bow", "§aRunaan's Bow", Material.BOW,
            "§7Shoots multiple arrows at once with deadly precision.",
            WeaponCategory.BOW, WeaponRarity.RARE, 120, Arrays.asList("§7- Triple Shot", "§7- High Accuracy"),
            Arrays.asList("§7- 1x String", "§7- 1x Bow")
        ));
        
        weaponConfigs.put(WeaponType.MOSQUITO_BOW, new WeaponConfig(
            "Mosquito Bow", "§cMosquito Bow", Material.BOW,
            "§7Drains life from enemies with each shot.",
            WeaponCategory.BOW, WeaponRarity.EPIC, 150, Arrays.asList("§7- Life Drain", "§7- Poison Effect"),
            Arrays.asList("§7- 1x Spider Eye", "§7- 1x Bow")
        ));
        
        // Additional Missing Weapons
        weaponConfigs.put(WeaponType.HYPERION, new WeaponConfig(
            "Hyperion", "§dHyperion", Material.DIAMOND_SWORD,
            "§7Teleports you 10 blocks ahead and implodes dealing massive damage.",
            WeaponCategory.SWORD, WeaponRarity.MYTHIC, 260, Arrays.asList("§7- Teleport", "§7- Implosion", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Wither Catalyst", "§7- 1x Diamond Sword")
        ));
        
        weaponConfigs.put(WeaponType.VALKYRIE, new WeaponConfig(
            "Valkyrie", "§6Valkyrie", Material.DIAMOND_SWORD,
            "§7A powerful sword with healing abilities.",
            WeaponCategory.SWORD, WeaponRarity.MYTHIC, 250, Arrays.asList("§7- Healing", "§7- High Damage"),
            Arrays.asList("§7- 1x Valkyrie Fragment", "§7- 1x Diamond Sword")
        ));
        
        weaponConfigs.put(WeaponType.SCYLLA, new WeaponConfig(
            "Scylla", "§5Scylla", Material.DIAMOND_SWORD,
            "§7A dark sword that drains enemy health.",
            WeaponCategory.SWORD, WeaponRarity.MYTHIC, 255, Arrays.asList("§7- Health Drain", "§7- Dark Magic"),
            Arrays.asList("§7- 1x Scylla Fragment", "§7- 1x Diamond Sword")
        ));
        
        weaponConfigs.put(WeaponType.ASTRAEA, new WeaponConfig(
            "Astraea", "§bAstraea", Material.DIAMOND_SWORD,
            "§7A divine sword with protective abilities.",
            WeaponCategory.SWORD, WeaponRarity.MYTHIC, 245, Arrays.asList("§7- Protection", "§7- Divine Power"),
            Arrays.asList("§7- 1x Astraea Fragment", "§7- 1x Diamond Sword")
        ));
        
        weaponConfigs.put(WeaponType.BONEMERANG, new WeaponConfig(
            "Bonemerang", "§fBonemerang", Material.BONE,
            "§7A bone that returns to you after hitting enemies.",
            WeaponCategory.BOW, WeaponRarity.EPIC, 180, Arrays.asList("§7- Returning", "§7- Bone Damage"),
            Arrays.asList("§7- 1x Bone", "§7- 1x String")
        ));
        
        weaponConfigs.put(WeaponType.SPIRIT_BOW, new WeaponConfig(
            "Spirit Bow", "§bSpirit Bow", Material.BOW,
            "§7A bow that channels spirit energy.",
            WeaponCategory.BOW, WeaponRarity.LEGENDARY, 200, Arrays.asList("§7- Spirit Energy", "§7- Ghost Damage"),
            Arrays.asList("§7- 1x Ghast Tear", "§7- 1x Bow")
        ));
        
        weaponConfigs.put(WeaponType.JUJU_SHORTBOW, new WeaponConfig(
            "Juju Shortbow", "§6Juju Shortbow", Material.BOW,
            "§7A shortbow with rapid fire capabilities.",
            WeaponCategory.BOW, WeaponRarity.LEGENDARY, 220, Arrays.asList("§7- Rapid Fire", "§7- High Speed"),
            Arrays.asList("§7- 1x Juju Fragment", "§7- 1x Bow")
        ));
        
        // Slayer Weapons
        weaponConfigs.put(WeaponType.REVENANT_FALCHION, new WeaponConfig(
            "Revenant Falchion", "§2Revenant Falchion", Material.IRON_SWORD,
            "§7A sword that deals extra damage to zombies.",
            WeaponCategory.SWORD, WeaponRarity.RARE, 120, Arrays.asList("§7- Zombie Damage", "§7- Undead Slayer"),
            Arrays.asList("§7- 1x Rotten Flesh", "§7- 1x Iron Sword")
        ));
        
        weaponConfigs.put(WeaponType.AXE_OF_THE_SHREDDED, new WeaponConfig(
            "Axe of the Shredded", "§8Axe of the Shredded", Material.IRON_AXE,
            "§7An axe that deals massive damage to spiders.",
            WeaponCategory.SWORD, WeaponRarity.EPIC, 180, Arrays.asList("§7- Spider Damage", "§7- Web Slayer"),
            Arrays.asList("§7- 1x String", "§7- 1x Iron Axe")
        ));
        
        weaponConfigs.put(WeaponType.VOIDEDGE_KATANA, new WeaponConfig(
            "Voidedge Katana", "§5Voidedge Katana", Material.IRON_SWORD,
            "§7A katana that deals extra damage to endermen.",
            WeaponCategory.SWORD, WeaponRarity.LEGENDARY, 200, Arrays.asList("§7- Enderman Damage", "§7- Void Slayer"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 1x Iron Sword")
        ));
        
        weaponConfigs.put(WeaponType.FIRE_VEIL_WAND, new WeaponConfig(
            "Fire Veil Wand", "§6Fire Veil Wand", Material.BLAZE_ROD,
            "§7A wand that creates a veil of fire around you.",
            WeaponCategory.SWORD, WeaponRarity.LEGENDARY, 190, Arrays.asList("§7- Fire Veil", "§7- Fire Damage"),
            Arrays.asList("§7- 1x Blaze Rod", "§7- 1x Stick")
        ));
        
        weaponConfigs.put(WeaponType.SPIRIT_SCEPTER, new WeaponConfig(
            "Spirit Scepter", "§bSpirit Scepter", Material.STICK,
            "§7A scepter that channels spirit magic.",
            WeaponCategory.SWORD, WeaponRarity.LEGENDARY, 210, Arrays.asList("§7- Spirit Magic", "§7- Magic Damage"),
            Arrays.asList("§7- 1x Ghast Tear", "§7- 1x Stick")
        ));
        
        weaponConfigs.put(WeaponType.FROZEN_SCYTHE, new WeaponConfig(
            "Frozen Scythe", "§bFrozen Scythe", Material.IRON_HOE,
            "§7A scythe that freezes enemies.",
            WeaponCategory.SWORD, WeaponRarity.EPIC, 160, Arrays.asList("§7- Freeze Effect", "§7- Ice Damage"),
            Arrays.asList("§7- 1x Ice", "§7- 1x Iron Hoe")
        ));
        
        weaponConfigs.put(WeaponType.ICE_SPRAY_WAND, new WeaponConfig(
            "Ice Spray Wand", "§bIce Spray Wand", Material.STICK,
            "§7A wand that sprays ice at enemies.",
            WeaponCategory.SWORD, WeaponRarity.LEGENDARY, 195, Arrays.asList("§7- Ice Spray", "§7- Freeze"),
            Arrays.asList("§7- 1x Ice", "§7- 1x Stick")
        ));
        
        weaponConfigs.put(WeaponType.GLACIAL_SCEPTER, new WeaponConfig(
            "Glacial Scepter", "§bGlacial Scepter", Material.STICK,
            "§7A scepter that creates glacial effects.",
            WeaponCategory.SWORD, WeaponRarity.LEGENDARY, 205, Arrays.asList("§7- Glacial Effect", "§7- Ice Magic"),
            Arrays.asList("§7- 1x Ice", "§7- 1x Stick")
        ));
        
        weaponConfigs.put(WeaponType.BONZO_STAFF, new WeaponConfig(
            "Bonzo Staff", "§aBonzo Staff", Material.STICK,
            "§7A staff that creates balloon effects.",
            WeaponCategory.SWORD, WeaponRarity.EPIC, 170, Arrays.asList("§7- Balloon Effect", "§7- Magic"),
            Arrays.asList("§7- 1x Balloon", "§7- 1x Stick")
        ));
        
        weaponConfigs.put(WeaponType.SCARF_STUDIES, new WeaponConfig(
            "Scarf Studies", "§cScarf Studies", Material.BOOK,
            "§7A book that studies scarf magic.",
            WeaponCategory.SWORD, WeaponRarity.EPIC, 175, Arrays.asList("§7- Scarf Magic", "§7- Knowledge"),
            Arrays.asList("§7- 1x Red Wool", "§7- 1x Book")
        ));
        
        weaponConfigs.put(WeaponType.THORN_BOW, new WeaponConfig(
            "Thorn Bow", "§2Thorn Bow", Material.BOW,
            "§7A bow that shoots thorn arrows.",
            WeaponCategory.BOW, WeaponRarity.EPIC, 185, Arrays.asList("§7- Thorn Arrows", "§7- Plant Damage"),
            Arrays.asList("§7- 1x Cactus", "§7- 1x Bow")
        ));
        
        weaponConfigs.put(WeaponType.LAST_BREATH, new WeaponConfig(
            "Last Breath", "§8Last Breath", Material.BOW,
            "§7A bow that takes the last breath of enemies.",
            WeaponCategory.BOW, WeaponRarity.LEGENDARY, 225, Arrays.asList("§7- Last Breath", "§7- Death Magic"),
            Arrays.asList("§7- 1x Wither Skull", "§7- 1x Bow")
        ));
    }
    
    private void startWeaponUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerWeapons> entry : playerWeapons.entrySet()) {
                    PlayerWeapons weapons = entry.getValue();
                    weapons.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ANVIL) {
            openWeaponGUI(player);
        }
    }
    
    private void openWeaponGUI(Player player) {
        player.sendMessage("§aWeapon GUI geöffnet!");
    }
    
    public ItemStack createWeapon(WeaponType type, int level) {
        WeaponConfig config = weaponConfigs.get(type);
        if (config == null) return null;
        
        ItemStack item = new ItemStack(config.getMaterial());
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(config.getDisplayName() + " §7Lv." + level);
        List<String> lore = new ArrayList<>();
        lore.add(config.getDescription());
        lore.add("");
        lore.add("§7Level: §a" + level);
        lore.add("§7Rarity: " + config.getRarity().getColor() + config.getRarity().getName());
        lore.add("§7Category: " + config.getCategory().getColor() + config.getCategory().getName());
        lore.add("");
        lore.addAll(config.getFeatures());
        lore.add("");
        lore.add("§7Click to use ability!");
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    public PlayerWeapons getPlayerWeapons(UUID playerId) {
        return playerWeapons.computeIfAbsent(playerId, k -> new PlayerWeapons(playerId));
    }
    
    public WeaponConfig getWeaponConfig(WeaponType type) {
        return weaponConfigs.get(type);
    }
    
    public List<WeaponType> getAllWeaponTypes() {
        return new ArrayList<>(weaponConfigs.keySet());
    }
    
    public enum WeaponType {
        // Swords
        ASPECT_OF_THE_END, ASPECT_OF_THE_DRAGONS, MIDAS_SWORD, HYPERION, VALKYRIE, SCYLLA, ASTRAEA,
        // Bows
        RUNAANS_BOW, MOSQUITO_BOW, BONEMERANG, SPIRIT_BOW, JUJU_SHORTBOW, THORN_BOW, LAST_BREATH,
        // Additional Weapons
        ENDER_BOW, MAGMA_BOW, VENOM_TOUCH, SILK_EDGE, ZOMBIE_SWORD, ORNATE_ZOMBIE_SWORD, FLORID_ZOMBIE_SWORD,
        REAPER_SCYTHE, REVENANT_FALCHION, AXE_OF_THE_SHREDDED, VOIDEDGE_KATANA, FIRE_VEIL_WAND, SPIRIT_SCEPTER,
        FROZEN_SCYTHE, ICE_SPRAY_WAND, GLACIAL_SCEPTER, BONZO_STAFF, SCARF_STUDIES,
        MACHINE_GUN_BOW, SAVANNA_BOW, END_STONE_BOW, HURRICANE_BOW, DEATH_BOW, MAGMA_BOW_2, VENOM_TOUCH_2,
        SILK_EDGE_2, ZOMBIE_SWORD_2, ORNATE_ZOMBIE_SWORD_2, FLORID_ZOMBIE_SWORD_2, REAPER_SCYTHE_2, REVENANT_FALCHION_2,
        AXE_OF_THE_SHREDDED_2, VOIDEDGE_KATANA_2, FIRE_VEIL_WAND_2, SPIRIT_SCEPTER_2, FROZEN_SCYTHE_2, ICE_SPRAY_WAND_2,
        GLACIAL_SCEPTER_2, BONZO_STAFF_2, SCARF_STUDIES_2, THORN_BOW_2, LAST_BREATH_2, MACHINE_GUN_BOW_2, SAVANNA_BOW_2,
        END_STONE_BOW_2, HURRICANE_BOW_2, DEATH_BOW_2, MAGMA_BOW_3, VENOM_TOUCH_3, SILK_EDGE_3, ZOMBIE_SWORD_3,
        ORNATE_ZOMBIE_SWORD_3, FLORID_ZOMBIE_SWORD_3, REAPER_SCYTHE_3, REVENANT_FALCHION_3, AXE_OF_THE_SHREDDED_3,
        VOIDEDGE_KATANA_3, FIRE_VEIL_WAND_3, SPIRIT_SCEPTER_3, FROZEN_SCYTHE_3, ICE_SPRAY_WAND_3, GLACIAL_SCEPTER_3,
        BONZO_STAFF_3, SCARF_STUDIES_3, THORN_BOW_3, LAST_BREATH_3, MACHINE_GUN_BOW_3, SAVANNA_BOW_3, END_STONE_BOW_3,
        HURRICANE_BOW_3, DEATH_BOW_3, MAGMA_BOW_4, VENOM_TOUCH_4, SILK_EDGE_4, ZOMBIE_SWORD_4, ORNATE_ZOMBIE_SWORD_4,
        FLORID_ZOMBIE_SWORD_4, REAPER_SCYTHE_4, REVENANT_FALCHION_4, AXE_OF_THE_SHREDDED_4, VOIDEDGE_KATANA_4,
        FIRE_VEIL_WAND_4, SPIRIT_SCEPTER_4, FROZEN_SCYTHE_4, ICE_SPRAY_WAND_4, GLACIAL_SCEPTER_4, BONZO_STAFF_4,
        SCARF_STUDIES_4, THORN_BOW_4, LAST_BREATH_4, MACHINE_GUN_BOW_4, SAVANNA_BOW_4, END_STONE_BOW_4, HURRICANE_BOW_4,
        DEATH_BOW_4, MAGMA_BOW_5, VENOM_TOUCH_5, SILK_EDGE_5, ZOMBIE_SWORD_5, ORNATE_ZOMBIE_SWORD_5, FLORID_ZOMBIE_SWORD_5,
        REAPER_SCYTHE_5, REVENANT_FALCHION_5, AXE_OF_THE_SHREDDED_5, VOIDEDGE_KATANA_5, FIRE_VEIL_WAND_5, SPIRIT_SCEPTER_5,
        FROZEN_SCYTHE_5, ICE_SPRAY_WAND_5, GLACIAL_SCEPTER_5, BONZO_STAFF_5, SCARF_STUDIES_5, THORN_BOW_5, LAST_BREATH_5,
        MACHINE_GUN_BOW_5, SAVANNA_BOW_5, END_STONE_BOW_5, HURRICANE_BOW_5, DEATH_BOW_5
    }
    
    public enum WeaponCategory {
        SWORD("§cSword", "§7Melee weapons for close combat"),
        BOW("§aBow", "§7Ranged weapons for long-distance combat");
        
        private final String name;
        private final String description;
        
        WeaponCategory(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getColor() { return name.substring(0, 2); }
    }
    
    public enum WeaponRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.2),
        RARE("§9Rare", 1.5),
        EPIC("§5Epic", 2.0),
        LEGENDARY("§6Legendary", 3.0),
        MYTHIC("§dMythic", 5.0),
        DIVINE("§bDivine", 10.0);
        
        private final String name;
        private final double multiplier;
        
        WeaponRarity(String name, double multiplier) {
            this.name = name;
            this.multiplier = multiplier;
        }
        
        public String getName() { return name; }
        public double getMultiplier() { return multiplier; }
        public String getColor() { return name.substring(0, 2); }
    }
    
    public static class WeaponConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final WeaponCategory category;
        private final WeaponRarity rarity;
        private final int baseDamage;
        private final List<String> features;
        private final List<String> requirements;
        
        public WeaponConfig(String name, String displayName, Material material, String description,
                           WeaponCategory category, WeaponRarity rarity, int baseDamage, List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.baseDamage = baseDamage;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public WeaponCategory getCategory() { return category; }
        public WeaponRarity getRarity() { return rarity; }
        public int getBaseDamage() { return baseDamage; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerWeapons {
        private final UUID playerId;
        private final Map<WeaponType, Integer> weaponLevels = new ConcurrentHashMap<>();
        private int totalWeapons = 0;
        private long totalWeaponTime = 0;
        private long lastUpdate;
        
        public PlayerWeapons(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;
            
            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }
        
        private void saveToDatabase() {
            // Save weapon data to database
        }
        
        public void addWeapon(WeaponType type, int level) {
            weaponLevels.put(type, level);
            totalWeapons++;
        }
        
        public int getWeaponLevel(WeaponType type) {
            return weaponLevels.getOrDefault(type, 0);
        }
        
        public int getTotalWeapons() { return totalWeapons; }
        public long getTotalWeaponTime() { return totalWeaponTime; }
        
        public UUID getPlayerId() { return playerId; }
        public Map<WeaponType, Integer> getWeaponLevels() { return weaponLevels; }
    }
}
