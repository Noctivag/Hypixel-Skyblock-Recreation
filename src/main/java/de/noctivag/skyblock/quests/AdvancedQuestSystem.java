package de.noctivag.skyblock.quests;
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
import org.bukkit.plugin.Plugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

// import org.bukkit.potion.PotionEffect;
// import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedQuestSystem implements Listener {

    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerQuestData> playerQuestData = new ConcurrentHashMap<>();
    private final Map<QuestType, List<Quest>> quests = new HashMap<>();

    public AdvancedQuestSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;

        initializeQuests();
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void initializeQuests() {
        // Tutorial Quests
        List<Quest> tutorialQuests = new ArrayList<>();
        tutorialQuests.add(new Quest(
            "Welcome to Skyblock", "§aWelcome to Skyblock", Material.GRASS_BLOCK,
            "§7Welcome to your new island! Let's get started.",
            QuestType.TUTORIAL, QuestRarity.COMMON, 1, Arrays.asList("§7- Place your first block", "§7- Break your first block"),
            Arrays.asList("§7- 100 coins", "§7- 1x Wooden Sword"), Arrays.asList("§7- Place any block", "§7- Break any block")
        ));
        tutorialQuests.add(new Quest(
            "First Tools", "§eFirst Tools", Material.WOODEN_PICKAXE,
            "§7Craft your first tools to get started.",
            QuestType.TUTORIAL, QuestRarity.COMMON, 2, Arrays.asList("§7- Craft a wooden pickaxe", "§7- Craft a wooden axe"),
            Arrays.asList("§7- 200 coins", "§7- 1x Wooden Pickaxe"), Arrays.asList("§7- Craft wooden pickaxe", "§7- Craft wooden axe")
        ));
        tutorialQuests.add(new Quest(
            "First House", "§6First House", Material.OAK_PLANKS,
            "§7Build your first house to protect yourself.",
            QuestType.TUTORIAL, QuestRarity.COMMON, 3, Arrays.asList("§7- Build a house", "§7- Place a door"),
            Arrays.asList("§7- 300 coins", "§7- 1x Oak Door"), Arrays.asList("§7- Build house", "§7- Place door")
        ));
        quests.put(QuestType.TUTORIAL, tutorialQuests);

        // Farming Quests
        List<Quest> farmingQuests = new ArrayList<>();
        farmingQuests.add(new Quest(
            "Wheat Farmer", "§eWheat Farmer", Material.WHEAT,
            "§7Harvest wheat to become a farmer.",
            QuestType.FARMING, QuestRarity.COMMON, 1, Arrays.asList("§7- Harvest 100 wheat", "§7- Plant 50 wheat seeds"),
            Arrays.asList("§7- 500 coins", "§7- 1x Wheat Sack"), Arrays.asList("§7- Harvest wheat", "§7- Plant seeds")
        ));
        farmingQuests.add(new Quest(
            "Carrot Master", "§6Carrot Master", Material.CARROT,
            "§7Master the art of carrot farming.",
            QuestType.FARMING, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Harvest 200 carrots", "§7- Plant 100 carrot seeds"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Carrot Sack"), Arrays.asList("§7- Harvest carrots", "§7- Plant seeds")
        ));
        farmingQuests.add(new Quest(
            "Potato King", "§cPotato King", Material.POTATO,
            "§7Become the king of potato farming.",
            QuestType.FARMING, QuestRarity.RARE, 3, Arrays.asList("§7- Harvest 500 potatoes", "§7- Plant 250 potato seeds"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Potato Sack"), Arrays.asList("§7- Harvest potatoes", "§7- Plant seeds")
        ));
        quests.put(QuestType.FARMING, farmingQuests);

        // Mining Quests
        List<Quest> miningQuests = new ArrayList<>();
        miningQuests.add(new Quest(
            "Stone Miner", "§7Stone Miner", Material.COBBLESTONE,
            "§7Mine stone to become a miner.",
            QuestType.MINING, QuestRarity.COMMON, 1, Arrays.asList("§7- Mine 1000 cobblestone", "§7- Craft 100 stone"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Cobblestone Sack"), Arrays.asList("§7- Mine cobblestone", "§7- Craft stone")
        ));
        miningQuests.add(new Quest(
            "Coal Collector", "§8Coal Collector", Material.COAL,
            "§7Collect coal for fuel and crafting.",
            QuestType.MINING, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Mine 500 coal", "§7- Craft 100 torches"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Coal Sack"), Arrays.asList("§7- Mine coal", "§7- Craft torches")
        ));
        miningQuests.add(new Quest(
            "Iron Hunter", "§fIron Hunter", Material.IRON_INGOT,
            "§7Hunt for iron to craft better tools.",
            QuestType.MINING, QuestRarity.RARE, 3, Arrays.asList("§7- Mine 200 iron ore", "§7- Craft 50 iron ingots"),
            Arrays.asList("§7- 5000 coins", "§7- 1x Iron Sack"), Arrays.asList("§7- Mine iron ore", "§7- Craft iron ingots")
        ));
        quests.put(QuestType.MINING, miningQuests);

        // Combat Quests
        List<Quest> combatQuests = new ArrayList<>();
        combatQuests.add(new Quest(
            "Zombie Slayer", "§2Zombie Slayer", Material.ROTTEN_FLESH,
            "§7Slay zombies to become a warrior.",
            QuestType.COMBAT, QuestRarity.COMMON, 1, Arrays.asList("§7- Kill 50 zombies", "§7- Collect 100 rotten flesh"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Rotten Flesh Sack"), Arrays.asList("§7- Kill zombies", "§7- Collect rotten flesh")
        ));
        combatQuests.add(new Quest(
            "Skeleton Hunter", "§fSkeleton Hunter", Material.BONE,
            "§7Hunt skeletons for their bones.",
            QuestType.COMBAT, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Kill 75 skeletons", "§7- Collect 150 bones"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Bone Sack"), Arrays.asList("§7- Kill skeletons", "§7- Collect bones")
        ));
        combatQuests.add(new Quest(
            "Spider Exterminator", "§8Spider Exterminator", Material.STRING,
            "§7Exterminate spiders for their silk.",
            QuestType.COMBAT, QuestRarity.RARE, 3, Arrays.asList("§7- Kill 100 spiders", "§7- Collect 200 string"),
            Arrays.asList("§7- 5000 coins", "§7- 1x String Sack"), Arrays.asList("§7- Kill spiders", "§7- Collect string")
        ));
        quests.put(QuestType.COMBAT, combatQuests);

        // Foraging Quests
        List<Quest> foragingQuests = new ArrayList<>();
        foragingQuests.add(new Quest(
            "Oak Logger", "§6Oak Logger", Material.OAK_LOG,
            "§7Chop down oak trees for wood.",
            QuestType.FORAGING, QuestRarity.COMMON, 1, Arrays.asList("§7- Chop 100 oak logs", "§7- Craft 50 oak planks"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Oak Log Sack"), Arrays.asList("§7- Chop oak logs", "§7- Craft oak planks")
        ));
        foragingQuests.add(new Quest(
            "Birch Cutter", "§fBirch Cutter", Material.BIRCH_LOG,
            "§7Cut down birch trees for wood.",
            QuestType.FORAGING, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Chop 150 birch logs", "§7- Craft 75 birch planks"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Birch Log Sack"), Arrays.asList("§7- Chop birch logs", "§7- Craft birch planks")
        ));
        foragingQuests.add(new Quest(
            "Spruce Harvester", "§2Spruce Harvester", Material.SPRUCE_LOG,
            "§7Harvest spruce trees for wood.",
            QuestType.FORAGING, QuestRarity.RARE, 3, Arrays.asList("§7- Chop 200 spruce logs", "§7- Craft 100 spruce planks"),
            Arrays.asList("§7- 5000 coins", "§7- 1x Spruce Log Sack"), Arrays.asList("§7- Chop spruce logs", "§7- Craft spruce planks")
        ));
        quests.put(QuestType.FORAGING, foragingQuests);

        // Fishing Quests
        List<Quest> fishingQuests = new ArrayList<>();
        fishingQuests.add(new Quest(
            "Fish Catcher", "§bFish Catcher", Material.COD,
            "§7Catch fish to become a fisherman.",
            QuestType.FISHING, QuestRarity.COMMON, 1, Arrays.asList("§7- Catch 50 fish", "§7- Cook 25 fish"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Fish Sack"), Arrays.asList("§7- Catch fish", "§7- Cook fish")
        ));
        fishingQuests.add(new Quest(
            "Salmon Hunter", "§cSalmon Hunter", Material.SALMON,
            "§7Hunt for salmon in the waters.",
            QuestType.FISHING, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Catch 75 salmon", "§7- Cook 50 salmon"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Salmon Sack"), Arrays.asList("§7- Catch salmon", "§7- Cook salmon")
        ));
        fishingQuests.add(new Quest(
            "Tropical Explorer", "§dTropical Explorer", Material.TROPICAL_FISH,
            "§7Explore tropical waters for exotic fish.",
            QuestType.FISHING, QuestRarity.RARE, 3, Arrays.asList("§7- Catch 100 tropical fish", "§7- Catch 25 pufferfish"),
            Arrays.asList("§7- 5000 coins", "§7- 1x Tropical Fish Sack"), Arrays.asList("§7- Catch tropical fish", "§7- Catch pufferfish")
        ));
        quests.put(QuestType.FISHING, fishingQuests);

        // Enchanting Quests
        List<Quest> enchantingQuests = new ArrayList<>();
        enchantingQuests.add(new Quest(
            "Enchanting Novice", "§dEnchanting Novice", Material.ENCHANTED_BOOK,
            "§7Learn the basics of enchanting.",
            QuestType.ENCHANTING, QuestRarity.COMMON, 1, Arrays.asList("§7- Enchant 10 items", "§7- Use 50 experience levels"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Enchanted Book"), Arrays.asList("§7- Enchant items", "§7- Use experience")
        ));
        enchantingQuests.add(new Quest(
            "Enchanting Apprentice", "§5Enchanting Apprentice", Material.ENCHANTING_TABLE,
            "§7Become an apprentice enchanter.",
            QuestType.ENCHANTING, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Enchant 25 items", "§7- Use 100 experience levels"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Enchanting Table"), Arrays.asList("§7- Enchant items", "§7- Use experience")
        ));
        enchantingQuests.add(new Quest(
            "Enchanting Master", "§cEnchanting Master", Material.NETHER_STAR,
            "§7Master the art of enchanting.",
            QuestType.ENCHANTING, QuestRarity.RARE, 3, Arrays.asList("§7- Enchant 50 items", "§7- Use 200 experience levels"),
            Arrays.asList("§7- 5000 coins", "§7- 1x Nether Star"), Arrays.asList("§7- Enchant items", "§7- Use experience")
        ));
        quests.put(QuestType.ENCHANTING, enchantingQuests);

        // Alchemy Quests
        List<Quest> alchemyQuests = new ArrayList<>();
        alchemyQuests.add(new Quest(
            "Alchemy Beginner", "§5Alchemy Beginner", Material.POTION,
            "§7Begin your journey in alchemy.",
            QuestType.ALCHEMY, QuestRarity.COMMON, 1, Arrays.asList("§7- Brew 20 potions", "§7- Use 100 nether wart"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Potion"), Arrays.asList("§7- Brew potions", "§7- Use nether wart")
        ));
        alchemyQuests.add(new Quest(
            "Alchemy Student", "§dAlchemy Student", Material.BREWING_STAND,
            "§7Study the art of alchemy.",
            QuestType.ALCHEMY, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Brew 50 potions", "§7- Use 250 nether wart"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Brewing Stand"), Arrays.asList("§7- Brew potions", "§7- Use nether wart")
        ));
        alchemyQuests.add(new Quest(
            "Alchemy Expert", "§cAlchemy Expert", Material.CAULDRON,
            "§7Become an expert alchemist.",
            QuestType.ALCHEMY, QuestRarity.RARE, 3, Arrays.asList("§7- Brew 100 potions", "§7- Use 500 nether wart"),
            Arrays.asList("§7- 5000 coins", "§7- 1x Cauldron"), Arrays.asList("§7- Brew potions", "§7- Use nether wart")
        ));
        quests.put(QuestType.ALCHEMY, alchemyQuests);

        // Taming Quests
        List<Quest> tamingQuests = new ArrayList<>();
        tamingQuests.add(new Quest(
            "Animal Friend", "§aAnimal Friend", Material.BONE,
            "§7Make friends with animals.",
            QuestType.TAMING, QuestRarity.COMMON, 1, Arrays.asList("§7- Tame 5 animals", "§7- Feed 20 animals"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Bone"), Arrays.asList("§7- Tame animals", "§7- Feed animals")
        ));
        tamingQuests.add(new Quest(
            "Pet Trainer", "§ePet Trainer", Material.LEAD,
            "§7Train pets to be your companions.",
            QuestType.TAMING, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Tame 10 animals", "§7- Feed 50 animals"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Lead"), Arrays.asList("§7- Tame animals", "§7- Feed animals")
        ));
        tamingQuests.add(new Quest(
            "Master Tamer", "§6Master Tamer", Material.GOLDEN_APPLE,
            "§7Become a master of taming.",
            QuestType.TAMING, QuestRarity.RARE, 3, Arrays.asList("§7- Tame 20 animals", "§7- Feed 100 animals"),
            Arrays.asList("§7- 5000 coins", "§7- 1x Golden Apple"), Arrays.asList("§7- Tame animals", "§7- Feed animals")
        ));
        quests.put(QuestType.TAMING, tamingQuests);

        // Carpentry Quests
        List<Quest> carpentryQuests = new ArrayList<>();
        carpentryQuests.add(new Quest(
            "Wood Worker", "§6Wood Worker", Material.WOODEN_AXE,
            "§7Work with wood to create items.",
            QuestType.CARPENTRY, QuestRarity.COMMON, 1, Arrays.asList("§7- Craft 50 wooden items", "§7- Use 100 wood"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Wooden Axe"), Arrays.asList("§7- Craft wooden items", "§7- Use wood")
        ));
        carpentryQuests.add(new Quest(
            "Furniture Maker", "§eFurniture Maker", Material.CRAFTING_TABLE,
            "§7Make furniture for your home.",
            QuestType.CARPENTRY, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Craft 100 wooden items", "§7- Use 250 wood"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Crafting Table"), Arrays.asList("§7- Craft wooden items", "§7- Use wood")
        ));
        carpentryQuests.add(new Quest(
            "Master Carpenter", "§cMaster Carpenter", Material.ANVIL,
            "§7Become a master carpenter.",
            QuestType.CARPENTRY, QuestRarity.RARE, 3, Arrays.asList("§7- Craft 200 wooden items", "§7- Use 500 wood"),
            Arrays.asList("§7- 5000 coins", "§7- 1x Anvil"), Arrays.asList("§7- Craft wooden items", "§7- Use wood")
        ));
        quests.put(QuestType.CARPENTRY, carpentryQuests);

        // Runecrafting Quests
        List<Quest> runecraftingQuests = new ArrayList<>();
        runecraftingQuests.add(new Quest(
            "Rune Learner", "§5Rune Learner", Material.LAPIS_LAZULI,
            "§7Learn the ancient art of runecrafting.",
            QuestType.RUNECRAFTING, QuestRarity.COMMON, 1, Arrays.asList("§7- Craft 10 runes", "§7- Use 50 lapis lazuli"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Lapis Lazuli"), Arrays.asList("§7- Craft runes", "§7- Use lapis lazuli")
        ));
        runecraftingQuests.add(new Quest(
            "Rune Apprentice", "§dRune Apprentice", Material.ENCHANTING_TABLE,
            "§7Become an apprentice runecrafter.",
            QuestType.RUNECRAFTING, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Craft 25 runes", "§7- Use 100 lapis lazuli"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Enchanting Table"), Arrays.asList("§7- Craft runes", "§7- Use lapis lazuli")
        ));
        runecraftingQuests.add(new Quest(
            "Rune Master", "§cRune Master", Material.NETHER_STAR,
            "§7Master the art of runecrafting.",
            QuestType.RUNECRAFTING, QuestRarity.RARE, 3, Arrays.asList("§7- Craft 50 runes", "§7- Use 200 lapis lazuli"),
            Arrays.asList("§7- 5000 coins", "§7- 1x Nether Star"), Arrays.asList("§7- Craft runes", "§7- Use lapis lazuli")
        ));
        quests.put(QuestType.RUNECRAFTING, runecraftingQuests);

        // Social Quests
        List<Quest> socialQuests = new ArrayList<>();
        socialQuests.add(new Quest(
            "Friendly Player", "§aFriendly Player", Material.PLAYER_HEAD,
            "§7Make friends with other players.",
            QuestType.SOCIAL, QuestRarity.COMMON, 1, Arrays.asList("§7- Chat with 10 players", "§7- Trade with 5 players"),
            Arrays.asList("§7- 1000 coins", "§7- 1x Player Head"), Arrays.asList("§7- Chat with players", "§7- Trade with players")
        ));
        socialQuests.add(new Quest(
            "Party Leader", "§eParty Leader", Material.WHITE_BANNER,
            "§7Lead a party of players.",
            QuestType.SOCIAL, QuestRarity.UNCOMMON, 2, Arrays.asList("§7- Create a party", "§7- Invite 5 players"),
            Arrays.asList("§7- 2000 coins", "§7- 1x Banner"), Arrays.asList("§7- Create party", "§7- Invite players")
        ));
        socialQuests.add(new Quest(
            "Guild Master", "§6Guild Master", Material.GOLDEN_HELMET,
            "§7Master the art of guild leadership.",
            QuestType.SOCIAL, QuestRarity.RARE, 3, Arrays.asList("§7- Create a guild", "§7- Invite 10 players"),
            Arrays.asList("§7- 5000 coins", "§7- 1x Golden Helmet"), Arrays.asList("§7- Create guild", "§7- Invite players")
        ));
        quests.put(QuestType.SOCIAL, socialQuests);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        String displayName = LegacyComponentSerializer.legacySection().serialize(meta.displayName());

        if (displayName.contains("Quest") || displayName.contains("Quests")) {
            openQuestGUI(player);
        }
    }

    public void openQuestGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lQuests"));

        // Add quest categories
        addGUIItem(gui, 10, Material.GRASS_BLOCK, "§a§lTutorial", "§7Tutorial quests for beginners.");
        addGUIItem(gui, 11, Material.WHEAT, "§e§lFarming", "§7Farming quests for farmers.");
        addGUIItem(gui, 12, Material.DIAMOND_PICKAXE, "§7§lMining", "§7Mining quests for miners.");
        addGUIItem(gui, 13, Material.DIAMOND_SWORD, "§c§lCombat", "§7Combat quests for warriors.");
        addGUIItem(gui, 14, Material.OAK_LOG, "§6§lForaging", "§7Foraging quests for lumberjacks.");
        addGUIItem(gui, 15, Material.FISHING_ROD, "§b§lFishing", "§7Fishing quests for fishermen.");
        addGUIItem(gui, 16, Material.ENCHANTED_BOOK, "§d§lEnchanting", "§7Enchanting quests for enchanters.");
        addGUIItem(gui, 17, Material.POTION, "§5§lAlchemy", "§7Alchemy quests for alchemists.");
        addGUIItem(gui, 18, Material.BONE, "§a§lTaming", "§7Taming quests for tamers.");

        // Add more quest categories
        addGUIItem(gui, 19, Material.WOODEN_AXE, "§6§lCarpentry", "§7Carpentry quests for carpenters.");
        addGUIItem(gui, 20, Material.LAPIS_LAZULI, "§5§lRunecrafting", "§7Runecrafting quests for runecrafters.");
        addGUIItem(gui, 21, Material.PLAYER_HEAD, "§a§lSocial", "§7Social quests for social players.");

        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the quest menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");

        player.openInventory(gui);
        player.sendMessage("§aQuest GUI geöffnet!");
    }

    public void openCategoryGUI(Player player, QuestType category) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lQuests - " + category.getDisplayName()));

        List<Quest> categoryQuests = quests.get(category);
        if (categoryQuests != null) {
            int slot = 10;
            for (Quest quest : categoryQuests) {
                if (slot >= 44) break; // Don't overflow into navigation area

                addGUIItem(gui, slot, quest.getMaterial(), quest.getDisplayName(), quest.getDescription());
                slot++;
            }
        }

        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to main quests.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the quest menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");

        player.openInventory(gui);
    }

    public void openQuestGUI(Player player, Quest quest) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lQuest - " + quest.getDisplayName()));

        // Add quest information
        addGUIItem(gui, 10, quest.getMaterial(), quest.getDisplayName(), quest.getDescription());

        // Add objectives
        int slot = 19;
        for (String objective : quest.getObjectives()) {
            if (slot >= 44) break;
            addGUIItem(gui, slot, Material.PAPER, "§7" + objective, "§7Objective description.");
            slot++;
        }

        // Add rewards
        slot = 28;
        for (String reward : quest.getRewards()) {
            if (slot >= 44) break;
            addGUIItem(gui, slot, Material.EMERALD, "§e" + reward, "§7Reward.");
            slot++;
        }

        // Add requirements
        slot = 37;
        for (String requirement : quest.getRequirements()) {
            if (slot >= 44) break;
            addGUIItem(gui, slot, Material.IRON_INGOT, "§c" + requirement, "§7Requirement.");
            slot++;
        }

        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to category.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the quest menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");

        player.openInventory(gui);
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

    public PlayerQuestData getPlayerQuestData(UUID playerId) {
        return playerQuestData.computeIfAbsent(playerId, k -> new PlayerQuestData(playerId));
    }

    public List<Quest> getQuests(QuestType category) {
        return quests.getOrDefault(category, new ArrayList<>());
    }

    public enum QuestType {
        TUTORIAL("§aTutorial", "§7Tutorial quests for beginners"),
        FARMING("§eFarming", "§7Farming quests for farmers"),
        MINING("§7Mining", "§7Mining quests for miners"),
        COMBAT("§cCombat", "§7Combat quests for warriors"),
        FORAGING("§6Foraging", "§7Foraging quests for lumberjacks"),
        FISHING("§bFishing", "§7Fishing quests for fishermen"),
        ENCHANTING("§dEnchanting", "§7Enchanting quests for enchanters"),
        ALCHEMY("§5Alchemy", "§7Alchemy quests for alchemists"),
        TAMING("§aTaming", "§7Taming quests for tamers"),
        CARPENTRY("§6Carpentry", "§7Carpentry quests for carpenters"),
        RUNECRAFTING("§5Runecrafting", "§7Runecrafting quests for runecrafters"),
        SOCIAL("§aSocial", "§7Social quests for social players");

        private final String displayName;
        private final String description;

        QuestType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }

    public enum QuestRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0),
        MYTHIC("§dMythic", 10.0);

        private final String displayName;
        private final double multiplier;

        QuestRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    public static class Quest {
        private final String name;
        private final String displayName;
        private final Material material;
        private final String description;
        private final QuestType type;
        private final QuestRarity rarity;
        private final int level;
        private final List<String> objectives;
        private final List<String> rewards;
        private final List<String> requirements;

        public Quest(String name, String displayName, Material material, String description,
                    QuestType type, QuestRarity rarity, int level, List<String> objectives,
                    List<String> rewards, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.material = material;
            this.description = description;
            this.type = type;
            this.rarity = rarity;
            this.level = level;
            this.objectives = objectives;
            this.rewards = rewards;
            this.requirements = requirements;
        }

        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getMaterial() { return material; }
        public String getDescription() { return description; }
        public QuestType getType() { return type; }
        public QuestRarity getRarity() { return rarity; }
        public int getLevel() { return level; }
        public List<String> getObjectives() { return objectives; }
        public List<String> getRewards() { return rewards; }
        public List<String> getRequirements() { return requirements; }
    }

    public static class PlayerQuestData {
        private final UUID playerId;
        private final Map<String, Boolean> completedQuests = new HashMap<>();
        private final Map<String, Integer> questProgress = new HashMap<>();
        private long lastUpdate;

        public PlayerQuestData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }

        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }

        public void completeQuest(String questName) {
            completedQuests.put(questName, true);
        }

        public void setQuestProgress(String questName, int progress) {
            questProgress.put(questName, progress);
        }

        public boolean isQuestCompleted(String questName) {
            return completedQuests.getOrDefault(questName, false);
        }

        public int getQuestProgress(String questName) {
            return questProgress.getOrDefault(questName, 0);
        }

        public long getLastUpdate() { return lastUpdate; }
    }
}
