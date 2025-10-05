package de.noctivag.skyblock.items;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced Item System - Hypixel Skyblock Style
 *
 * Features:
 * - Dynamic Items
 * - Item Categories
 * - Item Abilities
 * - Item Stats
 * - Item Rarities
 * - Item Enchantments
 * - Item Reforges
 * - Item Gemstones
 * - Item Customization
 * - Item Progression
 */
public class AdvancedItemSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerItems> playerItems = new ConcurrentHashMap<>();
    private final Map<ItemType, ItemConfig> itemConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> itemTasks = new ConcurrentHashMap<>();

    public AdvancedItemSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeItemConfigs();
        startItemUpdateTask();
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }

    private void initializeItemConfigs() {
        // Swords
        itemConfigs.put(ItemType.ASPECT_OF_THE_END, new ItemConfig(
            "Aspect of the End", "§aAspect of the End", Material.DIAMOND_SWORD,
            "§7Teleport behind enemies and deal massive damage.",
            ItemCategory.SWORD, ItemRarity.RARE, 100, Arrays.asList("§7- Teleport Ability", "§7- High Damage"),
            Arrays.asList("§7- 1x Ender Pearl", "§7- 1x Diamond Sword")
        ));

        itemConfigs.put(ItemType.ASPECT_OF_THE_DRAGONS, new ItemConfig(
            "Aspect of the Dragons", "§6Aspect of the Dragons", Material.DIAMOND_SWORD,
            "§7Summon dragon energy to devastate your enemies.",
            ItemCategory.SWORD, ItemRarity.LEGENDARY, 225, Arrays.asList("§7- Dragon Breath", "§7- Massive Damage"),
            Arrays.asList("§7- 1x Dragon Egg", "§7- 1x Diamond Sword")
        ));

        itemConfigs.put(ItemType.MIDAS_SWORD, new ItemConfig(
            "Midas Sword", "§6Midas Sword", Material.GOLDEN_SWORD,
            "§7A sword forged from pure gold with incredible power.",
            ItemCategory.SWORD, ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Gold Touch", "§7- Wealth Bonus"),
            Arrays.asList("§7- 1x Gold Block", "§7- 1x Golden Sword")
        ));

        itemConfigs.put(ItemType.HYPERION, new ItemConfig(
            "Hyperion", "§dHyperion", Material.DIAMOND_SWORD,
            "§7The ultimate sword with devastating abilities.",
            ItemCategory.SWORD, ItemRarity.MYTHIC, 300, Arrays.asList("§7- Wither Impact", "§7- Teleportation"),
            Arrays.asList("§7- 1x Wither Catalyst", "§7- 1x Diamond Sword")
        ));

        itemConfigs.put(ItemType.VALKYRIE, new ItemConfig(
            "Valkyrie", "§cValkyrie", Material.DIAMOND_SWORD,
            "§7A powerful sword with berserker abilities.",
            ItemCategory.SWORD, ItemRarity.MYTHIC, 280, Arrays.asList("§7- Berserker Rage", "§7- High Damage"),
            Arrays.asList("§7- 1x Berserker Fragment", "§7- 1x Diamond Sword")
        ));

        itemConfigs.put(ItemType.SCYLLA, new ItemConfig(
            "Scylla", "§bScylla", Material.DIAMOND_SWORD,
            "§7A mystical sword with healing abilities.",
            ItemCategory.SWORD, ItemRarity.MYTHIC, 260, Arrays.asList("§7- Healing Aura", "§7- Life Steal"),
            Arrays.asList("§7- 1x Healing Fragment", "§7- 1x Diamond Sword")
        ));

        itemConfigs.put(ItemType.ASTRAEA, new ItemConfig(
            "Astraea", "§eAstraea", Material.DIAMOND_SWORD,
            "§7A defensive sword with protection abilities.",
            ItemCategory.SWORD, ItemRarity.MYTHIC, 240, Arrays.asList("§7- Protection Aura", "§7- Defense Boost"),
            Arrays.asList("§7- 1x Protection Fragment", "§7- 1x Diamond Sword")
        ));

        // Bows
        itemConfigs.put(ItemType.RUNAANS_BOW, new ItemConfig(
            "Runaan's Bow", "§aRunaan's Bow", Material.BOW,
            "§7Shoots multiple arrows at once with deadly precision.",
            ItemCategory.BOW, ItemRarity.RARE, 120, Arrays.asList("§7- Triple Shot", "§7- High Accuracy"),
            Arrays.asList("§7- 1x String", "§7- 1x Bow")
        ));

        itemConfigs.put(ItemType.MOSQUITO_BOW, new ItemConfig(
            "Mosquito Bow", "§cMosquito Bow", Material.BOW,
            "§7Drains life from enemies with each shot.",
            ItemCategory.BOW, ItemRarity.EPIC, 150, Arrays.asList("§7- Life Drain", "§7- Poison Effect"),
            Arrays.asList("§7- 1x Spider Eye", "§7- 1x Bow")
        ));

        itemConfigs.put(ItemType.BONEMERANG, new ItemConfig(
            "Bonemerang", "§fBonemerang", Material.BONE,
            "§7A boomerang that returns to you after hitting enemies.",
            ItemCategory.BOW, ItemRarity.EPIC, 180, Arrays.asList("§7- Boomerang Effect", "§7- Return Damage"),
            Arrays.asList("§7- 1x Bone", "§7- 1x String")
        ));

        itemConfigs.put(ItemType.SPIRIT_BOW, new ItemConfig(
            "Spirit Bow", "§dSpirit Bow", Material.BOW,
            "§7A mystical bow that shoots spirit arrows.",
            ItemCategory.BOW, ItemRarity.LEGENDARY, 200, Arrays.asList("§7- Spirit Arrows", "§7- Mystical Power"),
            Arrays.asList("§7- 1x Spirit Fragment", "§7- 1x Bow")
        ));

        itemConfigs.put(ItemType.JUJU_SHORTBOW, new ItemConfig(
            "Juju Shortbow", "§6Juju Shortbow", Material.BOW,
            "§7A shortbow with incredible speed and power.",
            ItemCategory.BOW, ItemRarity.LEGENDARY, 220, Arrays.asList("§7- High Speed", "§7- Rapid Fire"),
            Arrays.asList("§7- 1x Juju Fragment", "§7- 1x Bow")
        ));

        // Armor Sets
        itemConfigs.put(ItemType.DRAGON_ARMOR, new ItemConfig(
            "Dragon Armor", "§6Dragon Armor", Material.DIAMOND_CHESTPLATE,
            "§7Armor forged from dragon scales with incredible protection.",
            ItemCategory.ARMOR, ItemRarity.LEGENDARY, 300, Arrays.asList("§7- Dragon Protection", "§7- Fire Resistance"),
            Arrays.asList("§7- 1x Dragon Scale", "§7- 1x Diamond Armor")
        ));

        itemConfigs.put(ItemType.SUPERIOR_ARMOR, new ItemConfig(
            "Superior Armor", "§6Superior Armor", Material.DIAMOND_CHESTPLATE,
            "§7The ultimate armor set with superior stats.",
            ItemCategory.ARMOR, ItemRarity.LEGENDARY, 350, Arrays.asList("§7- Superior Stats", "§7- All-around Bonus"),
            Arrays.asList("§7- 1x Superior Fragment", "§7- 1x Diamond Armor")
        ));

        itemConfigs.put(ItemType.UNSTABLE_ARMOR, new ItemConfig(
            "Unstable Armor", "§5Unstable Armor", Material.DIAMOND_CHESTPLATE,
            "§7Unstable armor with random effects.",
            ItemCategory.ARMOR, ItemRarity.LEGENDARY, 320, Arrays.asList("§7- Random Effects", "§7- Unstable Power"),
            Arrays.asList("§7- 1x Unstable Fragment", "§7- 1x Diamond Armor")
        ));

        itemConfigs.put(ItemType.STRONG_ARMOR, new ItemConfig(
            "Strong Armor", "§cStrong Armor", Material.DIAMOND_CHESTPLATE,
            "§7Strong armor focused on damage.",
            ItemCategory.ARMOR, ItemRarity.LEGENDARY, 330, Arrays.asList("§7- High Damage", "§7- Strength Bonus"),
            Arrays.asList("§7- 1x Strong Fragment", "§7- 1x Diamond Armor")
        ));

        itemConfigs.put(ItemType.YOUNG_ARMOR, new ItemConfig(
            "Young Armor", "§fYoung Armor", Material.DIAMOND_CHESTPLATE,
            "§7Young armor focused on speed.",
            ItemCategory.ARMOR, ItemRarity.LEGENDARY, 310, Arrays.asList("§7- Speed Bonus", "§7- Agility"),
            Arrays.asList("§7- 1x Young Fragment", "§7- 1x Diamond Armor")
        ));

        itemConfigs.put(ItemType.OLD_ARMOR, new ItemConfig(
            "Old Armor", "§7Old Armor", Material.DIAMOND_CHESTPLATE,
            "§7Old armor focused on defense.",
            ItemCategory.ARMOR, ItemRarity.LEGENDARY, 340, Arrays.asList("§7- High Defense", "§7- Protection"),
            Arrays.asList("§7- 1x Old Fragment", "§7- 1x Diamond Armor")
        ));

        itemConfigs.put(ItemType.PROTECTOR_ARMOR, new ItemConfig(
            "Protector Armor", "§aProtector Armor", Material.DIAMOND_CHESTPLATE,
            "§7Protector armor with maximum defense.",
            ItemCategory.ARMOR, ItemRarity.LEGENDARY, 360, Arrays.asList("§7- Maximum Defense", "§7- Protection"),
            Arrays.asList("§7- 1x Protector Fragment", "§7- 1x Diamond Armor")
        ));

        itemConfigs.put(ItemType.WISE_ARMOR, new ItemConfig(
            "Wise Armor", "§bWise Armor", Material.DIAMOND_CHESTPLATE,
            "§7Wise armor focused on intelligence.",
            ItemCategory.ARMOR, ItemRarity.LEGENDARY, 290, Arrays.asList("§7- Intelligence Bonus", "§7- Mana Boost"),
            Arrays.asList("§7- 1x Wise Fragment", "§7- 1x Diamond Armor")
        ));

        // Tools
        itemConfigs.put(ItemType.DRILL, new ItemConfig(
            "Drill", "§eDrill", Material.DIAMOND_PICKAXE,
            "§7An advanced mining tool with incredible efficiency.",
            ItemCategory.TOOL, ItemRarity.EPIC, 180, Arrays.asList("§7- Fast Mining", "§7- Auto-Smelting"),
            Arrays.asList("§7- 1x Redstone", "§7- 1x Diamond Pickaxe")
        ));

        itemConfigs.put(ItemType.TREE_CAPITATOR, new ItemConfig(
            "Tree Capitator", "§aTree Capitator", Material.DIAMOND_AXE,
            "§7Chops down entire trees with a single swing.",
            ItemCategory.TOOL, ItemRarity.RARE, 140, Arrays.asList("§7- Tree Chopping", "§7- Efficiency Bonus"),
            Arrays.asList("§7- 1x Oak Log", "§7- 1x Diamond Axe")
        ));

        itemConfigs.put(ItemType.GOLDEN_PICKAXE, new ItemConfig(
            "Golden Pickaxe", "§6Golden Pickaxe", Material.GOLDEN_PICKAXE,
            "§7A golden pickaxe with fortune and efficiency.",
            ItemCategory.TOOL, ItemRarity.RARE, 120, Arrays.asList("§7- Fortune", "§7- Efficiency"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Stick")
        ));

        itemConfigs.put(ItemType.DIAMOND_PICKAXE, new ItemConfig(
            "Diamond Pickaxe", "§bDiamond Pickaxe", Material.DIAMOND_PICKAXE,
            "§7A diamond pickaxe with high durability.",
            ItemCategory.TOOL, ItemRarity.UNCOMMON, 100, Arrays.asList("§7- High Durability", "§7- Fast Mining"),
            Arrays.asList("§7- 1x Diamond", "§7- 1x Stick")
        ));

        itemConfigs.put(ItemType.EMERALD_PICKAXE, new ItemConfig(
            "Emerald Pickaxe", "§aEmerald Pickaxe", Material.DIAMOND_PICKAXE,
            "§7An emerald pickaxe with special abilities.",
            ItemCategory.TOOL, ItemRarity.EPIC, 160, Arrays.asList("§7- Special Abilities", "§7- High Efficiency"),
            Arrays.asList("§7- 1x Emerald", "§7- 1x Stick")
        ));

        // Accessories
        itemConfigs.put(ItemType.TALISMAN_OF_POWER, new ItemConfig(
            "Talisman of Power", "§6Talisman of Power", Material.GOLD_NUGGET,
            "§7Increases all your stats significantly.",
            ItemCategory.ACCESSORY, ItemRarity.RARE, 80, Arrays.asList("§7- Stat Boost", "§7- All Stats"),
            Arrays.asList("§7- 1x Gold Nugget", "§7- 1x Enchanted Book")
        ));

        itemConfigs.put(ItemType.RING_OF_LOVE, new ItemConfig(
            "Ring of Love", "§dRing of Love", Material.GOLD_INGOT,
            "§7Increases health regeneration and love.",
            ItemCategory.ACCESSORY, ItemRarity.EPIC, 100, Arrays.asList("§7- Health Regen", "§7- Love Bonus"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Rose")
        ));

        itemConfigs.put(ItemType.ARTIFACT_OF_POWER, new ItemConfig(
            "Artifact of Power", "§6Artifact of Power", Material.NETHER_STAR,
            "§7A powerful artifact that boosts all abilities.",
            ItemCategory.ACCESSORY, ItemRarity.LEGENDARY, 150, Arrays.asList("§7- Power Boost", "§7- All Abilities"),
            Arrays.asList("§7- 1x Nether Star", "§7- 1x Enchanted Book")
        ));

        itemConfigs.put(ItemType.RELIC_OF_POWER, new ItemConfig(
            "Relic of Power", "§dRelic of Power", Material.END_CRYSTAL,
            "§7An ancient relic with incredible power.",
            ItemCategory.ACCESSORY, ItemRarity.MYTHIC, 200, Arrays.asList("§7- Ancient Power", "§7- Incredible Boost"),
            Arrays.asList("§7- 1x End Crystal", "§7- 1x Enchanted Book")
        ));
    }

    private void startItemUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (SkyblockPlugin.isEnabled()) {
                try {
                    for (Map.Entry<UUID, PlayerItems> entry : playerItems.entrySet()) {
                        PlayerItems items = entry.getValue();
                        items.update();
                    }
                    Thread.sleep(1000); // Every second = 1000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ANVIL) {
            openItemGUI(player);
        }
    }

    private void openItemGUI(Player player) {
        player.sendMessage(Component.text("§aItem GUI geöffnet!"));
    }

    public ItemStack createItem(ItemType type, int level) {
        ItemConfig config = itemConfigs.get(type);
        if (config == null) return null;

        ItemStack item = new ItemStack(config.getMaterial());
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(config.getDisplayName() + " §7Lv." + level));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text(config.getDescription()));
        lore.add(Component.text(""));
        lore.add(Component.text("§7Level: §a" + level));
        lore.add(Component.text("§7Rarity: " + config.getRarity().getColor() + config.getRarity().getName()));
        lore.add(Component.text("§7Category: " + config.getCategory().getColor() + config.getCategory().getName()));
        lore.add(Component.text(""));
        config.getFeatures().forEach(feature -> lore.add(Component.text(feature)));
        lore.add(Component.text(""));
        lore.add(Component.text("§7Click to use ability!"));

        meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public PlayerItems getPlayerItems(UUID playerId) {
        return playerItems.computeIfAbsent(playerId, k -> new PlayerItems(playerId));
    }

    public ItemConfig getItemConfig(ItemType type) {
        return itemConfigs.get(type);
    }

    public List<ItemType> getAllItemTypes() {
        return new ArrayList<>(itemConfigs.keySet());
    }

    public enum ItemType {
        // Swords
        ASPECT_OF_THE_END, ASPECT_OF_THE_DRAGONS, MIDAS_SWORD, HYPERION, VALKYRIE, SCYLLA, ASTRAEA,
        // Bows
        RUNAANS_BOW, MOSQUITO_BOW, BONEMERANG, SPIRIT_BOW, JUJU_SHORTBOW,
        // Armor Sets
        DRAGON_ARMOR, SUPERIOR_ARMOR, UNSTABLE_ARMOR, STRONG_ARMOR, YOUNG_ARMOR, OLD_ARMOR, PROTECTOR_ARMOR, WISE_ARMOR,
        // Tools
        DRILL, TREE_CAPITATOR, GOLDEN_PICKAXE, DIAMOND_PICKAXE, EMERALD_PICKAXE,
        // Accessories
        TALISMAN_OF_POWER, RING_OF_LOVE, ARTIFACT_OF_POWER, RELIC_OF_POWER,
        // Additional Items
        ENDER_BOW, MAGMA_BOW, VENOM_TOUCH, SILK_EDGE, ZOMBIE_SWORD, ORNATE_ZOMBIE_SWORD, FLORID_ZOMBIE_SWORD,
        REAPER_SCYTHE, REVENANT_FALCHION, AXE_OF_THE_SHREDDED, VOIDEDGE_KATANA, FIRE_VEIL_WAND, SPIRIT_SCEPTER,
        FROZEN_SCYTHE, ICE_SPRAY_WAND, GLACIAL_SCEPTER, BONZO_STAFF, SCARF_STUDIES, THORN_BOW, LAST_BREATH,
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

    public enum ItemCategory {
        SWORD("§cSword", "§7Melee weapons for close combat"),
        BOW("§aBow", "§7Ranged weapons for long-distance combat"),
        ARMOR("§bArmor", "§7Protective gear for defense"),
        TOOL("§eTool", "§7Tools for gathering resources"),
        ACCESSORY("§dAccessory", "§7Special items with unique effects");

        private final String name;
        private final String description;

        ItemCategory(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getColor() { return name.substring(0, 2); }
    }

    public enum ItemRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.2),
        RARE("§9Rare", 1.5),
        EPIC("§5Epic", 2.0),
        LEGENDARY("§6Legendary", 3.0),
        MYTHIC("§dMythic", 5.0),
        DIVINE("§bDivine", 10.0);

        private final String name;
        private final double multiplier;

        ItemRarity(String name, double multiplier) {
            this.name = name;
            this.multiplier = multiplier;
        }

        public String getName() { return name; }
        public double getMultiplier() { return multiplier; }
        public String getColor() { return name.substring(0, 2); }
    }

    public static class ItemConfig {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final ItemCategory category;
        private final ItemRarity rarity;
        private final int baseDamage;
        private final List<String> features;
        private final List<String> requirements;

        public ItemConfig(String name, String displayName, Material material, String description,
                         ItemCategory category, ItemRarity rarity, int baseDamage, List<String> features, List<String> requirements) {
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
        public ItemCategory getCategory() { return category; }
        public ItemRarity getRarity() { return rarity; }
        public int getBaseDamage() { return baseDamage; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }

    public static class PlayerItems {
        private final UUID playerId;
        private final Map<ItemType, Integer> itemLevels = new ConcurrentHashMap<>();
        private int totalItems = 0;
        private long totalItemTime = 0;
        private long lastUpdate;

        public PlayerItems(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }

        public void update() {
            long currentTime = java.lang.System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;

            if (timeDiff >= 60000) {
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }

        private void saveToDatabase() {
            // Save item data to database
        }

        public void addItem(ItemType type, int level) {
            itemLevels.put(type, level);
            totalItems++;
        }

        public int getItemLevel(ItemType type) {
            return itemLevels.getOrDefault(type, 0);
        }

        public int getTotalItems() { return totalItems; }
        public long getTotalItemTime() { return totalItemTime; }

        public UUID getPlayerId() { return playerId; }
        public Map<ItemType, Integer> getItemLevels() { return itemLevels; }
    }

    // Missing method for ItemCommands
    public void openSpecialItemsGUI(Player player) {
        // Placeholder implementation
        player.sendMessage(Component.text("§7Opening Special Items GUI..."));
        // TODO: Implement actual special items GUI
    }
}
