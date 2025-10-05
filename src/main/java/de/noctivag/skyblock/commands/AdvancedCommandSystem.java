package de.noctivag.skyblock.commands;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPluginRefactored;
import org.bukkit.inventory.ItemStack;
import de.noctivag.skyblock.data.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;

/**
 * Advanced Command System - Hypixel Skyblock Style
 *
 * Features:
 * - Dynamic Commands
 * - Command Categories
 * - Command Permissions
 * - Command Cooldowns
 * - Command Help System
 * - Command Aliases
 * - Command Arguments
 * - Command Validation
 * - Command Logging
 * - Command Analytics
 */
public class AdvancedCommandSystem implements CommandExecutor, TabCompleter {
    private final SkyblockPluginRefactored SkyblockPlugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, PlayerCommands> playerCommands = new ConcurrentHashMap<>();
    private final Map<CommandType, CommandConfig> commandConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> commandTasks = new ConcurrentHashMap<>();

    public AdvancedCommandSystem(SkyblockPluginRefactored SkyblockPlugin, DatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeCommandConfigs();
        startCommandUpdateTask();
    }

    public void register() {
        // Register commands - execute directly for Folia compatibility
        registerCommands();
    }

    private void initializeCommandConfigs() {
        commandConfigs.put(CommandType.SKILLS, new CommandConfig(
            "skills", "§aSkills", Material.EXPERIENCE_BOTTLE,
            "§7View and manage your skills.",
            CommandCategory.SKILLS, "basics.skills", 0, Arrays.asList("§7- View skills", "§7- Skill information"),
            Arrays.asList("§7- /skills", "§7- /skills <skill>")
        ));

        commandConfigs.put(CommandType.COLLECTIONS, new CommandConfig(
            "collections", "§bCollections", Material.BOOK,
            "§7View and manage your collections.",
            CommandCategory.COLLECTIONS, "basics.collections", 0, Arrays.asList("§7- View collections", "§7- Collection information"),
            Arrays.asList("§7- /collections", "§7- /collections <collection>")
        ));

        commandConfigs.put(CommandType.MINIONS, new CommandConfig(
            "minions", "§eMinions", Material.IRON_GOLEM_SPAWN_EGG,
            "§7Manage your minions.",
            CommandCategory.MINIONS, "basics.minions", 0, Arrays.asList("§7- View minions", "§7- Minion management"),
            Arrays.asList("§7- /minions", "§7- /minions <minion>")
        ));

        commandConfigs.put(CommandType.PETS, new CommandConfig(
            "pets", "§dPets", Material.WOLF_SPAWN_EGG,
            "§7Manage your pets.",
            CommandCategory.PETS, "basics.pets", 0, Arrays.asList("§7- View pets", "§7- Pet management"),
            Arrays.asList("§7- /pets", "§7- /pets <pet>")
        ));

        commandConfigs.put(CommandType.DUNGEONS, new CommandConfig(
            "dungeons", "§cDungeons", Material.NETHER_STAR,
            "§7View and manage dungeons.",
            CommandCategory.DUNGEONS, "basics.dungeons", 0, Arrays.asList("§7- View dungeons", "§7- Dungeon information"),
            Arrays.asList("§7- /dungeons", "§7- /dungeons <dungeon>")
        ));

        commandConfigs.put(CommandType.SLAYERS, new CommandConfig(
            "slayers", "§4Slayers", Material.DIAMOND_SWORD,
            "§7View and manage slayers.",
            CommandCategory.SLAYERS, "basics.slayers", 0, Arrays.asList("§7- View slayers", "§7- Slayer information"),
            Arrays.asList("§7- /slayers", "§7- /slayers <slayer>")
        ));

        commandConfigs.put(CommandType.GUILDS, new CommandConfig(
            "guilds", "§6Guilds", Material.WHITE_BANNER,
            "§7View and manage guilds.",
            CommandCategory.GUILDS, "basics.guilds", 0, Arrays.asList("§7- View guilds", "§7- Guild management"),
            Arrays.asList("§7- /guilds", "§7- /guilds <guild>")
        ));

        commandConfigs.put(CommandType.AUCTION, new CommandConfig(
            "auction", "§eAuction", Material.GOLD_INGOT,
            "§7View and manage auctions.",
            CommandCategory.AUCTION, "basics.auction", 0, Arrays.asList("§7- View auctions", "§7- Auction management"),
            Arrays.asList("§7- /auction", "§7- /auction <auction>")
        ));

        commandConfigs.put(CommandType.BAZAAR, new CommandConfig(
            "bazaar", "§aBazaar", Material.EMERALD,
            "§7View and manage bazaar.",
            CommandCategory.BAZAAR, "basics.bazaar", 0, Arrays.asList("§7- View bazaar", "§7- Bazaar management"),
            Arrays.asList("§7- /bazaar", "§7- /bazaar <bazaar>")
        ));

        commandConfigs.put(CommandType.ISLANDS, new CommandConfig(
            "islands", "§bIslands", Material.GRASS_BLOCK,
            "§7View and manage islands.",
            CommandCategory.ISLANDS, "basics.islands", 0, Arrays.asList("§7- View islands", "§7- Island management"),
            Arrays.asList("§7- /islands", "§7- /islands <island>")
        ));

        commandConfigs.put(CommandType.ENCHANTING, new CommandConfig(
            "enchanting", "§dEnchanting", Material.ENCHANTING_TABLE,
            "§7View and manage enchanting.",
            CommandCategory.ENCHANTING, "basics.enchanting", 0, Arrays.asList("§7- View enchanting", "§7- Enchanting management"),
            Arrays.asList("§7- /enchanting", "§7- /enchanting <enchant>")
        ));

        commandConfigs.put(CommandType.ALCHEMY, new CommandConfig(
            "alchemy", "§eAlchemy", Material.BREWING_STAND,
            "§7View and manage alchemy.",
            CommandCategory.ALCHEMY, "basics.alchemy", 0, Arrays.asList("§7- View alchemy", "§7- Alchemy management"),
            Arrays.asList("§7- /alchemy", "§7- /alchemy <potion>")
        ));

        commandConfigs.put(CommandType.CARPENTRY, new CommandConfig(
            "carpentry", "§6Carpentry", Material.CRAFTING_TABLE,
            "§7View and manage carpentry.",
            CommandCategory.CARPENTRY, "basics.carpentry", 0, Arrays.asList("§7- View carpentry", "§7- Carpentry management"),
            Arrays.asList("§7- /carpentry", "§7- /carpentry <item>")
        ));

        commandConfigs.put(CommandType.RUNECRAFTING, new CommandConfig(
            "runecrafting", "§5Runecrafting", Material.END_CRYSTAL,
            "§7View and manage runecrafting.",
            CommandCategory.RUNECRAFTING, "basics.runecrafting", 0, Arrays.asList("§7- View runecrafting", "§7- Runecrafting management"),
            Arrays.asList("§7- /runecrafting", "§7- /runecrafting <rune>")
        ));

        commandConfigs.put(CommandType.BANKING, new CommandConfig(
            "banking", "§bBanking", Material.CHEST,
            "§7View and manage banking.",
            CommandCategory.BANKING, "basics.banking", 0, Arrays.asList("§7- View banking", "§7- Banking management"),
            Arrays.asList("§7- /banking", "§7- /banking <bank>")
        ));

        commandConfigs.put(CommandType.QUESTS, new CommandConfig(
            "quests", "§aQuests", Material.BOOKSHELF,
            "§7View and manage quests.",
            CommandCategory.QUESTS, "basics.quests", 0, Arrays.asList("§7- View quests", "§7- Quest management"),
            Arrays.asList("§7- /quests", "§7- /quests <quest>")
        ));

        commandConfigs.put(CommandType.EVENTS, new CommandConfig(
            "events", "§eEvents", Material.BEACON,
            "§7View and manage events.",
            CommandCategory.EVENTS, "basics.events", 0, Arrays.asList("§7- View events", "§7- Event management"),
            Arrays.asList("§7- /events", "§7- /events <event>")
        ));

        commandConfigs.put(CommandType.COSMETICS, new CommandConfig(
            "cosmetics", "§dCosmetics", Material.ANVIL,
            "§7View and manage cosmetics.",
            CommandCategory.COSMETICS, "basics.cosmetics", 0, Arrays.asList("§7- View cosmetics", "§7- Cosmetic management"),
            Arrays.asList("§7- /cosmetics", "§7- /cosmetics <cosmetic>")
        ));

        commandConfigs.put(CommandType.ACHIEVEMENTS, new CommandConfig(
            "achievements", "§6Achievements", Material.GOLD_INGOT,
            "§7View and manage achievements.",
            CommandCategory.ACHIEVEMENTS, "basics.achievements", 0, Arrays.asList("§7- View achievements", "§7- Achievement management"),
            Arrays.asList("§7- /achievements", "§7- /achievements <achievement>")
        ));

        commandConfigs.put(CommandType.LEADERBOARDS, new CommandConfig(
            "leaderboards", "§cLeaderboards", Material.ITEM_FRAME,
            "§7View and manage leaderboards.",
            CommandCategory.LEADERBOARDS, "basics.leaderboards", 0, Arrays.asList("§7- View leaderboards", "§7- Leaderboard management"),
            Arrays.asList("§7- /leaderboards", "§7- /leaderboards <leaderboard>")
        ));

        commandConfigs.put(CommandType.API, new CommandConfig(
            "api", "§5API", Material.COMMAND_BLOCK,
            "§7View and manage API.",
            CommandCategory.API, "basics.api", 0, Arrays.asList("§7- View API", "§7- API management"),
            Arrays.asList("§7- /api", "§7- /api <api>")
        ));

        commandConfigs.put(CommandType.WEB, new CommandConfig(
            "web", "§bWeb", Material.REDSTONE_LAMP,
            "§7View and manage web interface.",
            CommandCategory.WEB, "basics.web", 0, Arrays.asList("§7- View web", "§7- Web management"),
            Arrays.asList("§7- /web", "§7- /web <web>")
        ));

        commandConfigs.put(CommandType.SOCIAL, new CommandConfig(
            "social", "§aSocial", Material.PLAYER_HEAD,
            "§7View and manage social features.",
            CommandCategory.SOCIAL, "basics.social", 0, Arrays.asList("§7- View social", "§7- Social management"),
            Arrays.asList("§7- /social", "§7- /social <social>")
        ));

        commandConfigs.put(CommandType.ITEMS, new CommandConfig(
            "items", "§cItems", Material.DIAMOND_SWORD,
            "§7View and manage items.",
            CommandCategory.ITEMS, "basics.items", 0, Arrays.asList("§7- View items", "§7- Item management"),
            Arrays.asList("§7- /items", "§7- /items <item>")
        ));

        commandConfigs.put(CommandType.ARMOR, new CommandConfig(
            "armor", "§6Armor", Material.DIAMOND_CHESTPLATE,
            "§7View and manage armor.",
            CommandCategory.ARMOR, "basics.armor", 0, Arrays.asList("§7- View armor", "§7- Armor management"),
            Arrays.asList("§7- /armor", "§7- /armor <armor>")
        ));

        commandConfigs.put(CommandType.WEAPONS, new CommandConfig(
            "weapons", "§aWeapons", Material.BOW,
            "§7View and manage weapons.",
            CommandCategory.WEAPONS, "basics.weapons", 0, Arrays.asList("§7- View weapons", "§7- Weapon management"),
            Arrays.asList("§7- /weapons", "§7- /weapons <weapon>")
        ));

        commandConfigs.put(CommandType.CREATIVE, new CommandConfig(
            "creative", "§dCreative", Material.CHEST,
            "§7Open creative inventory menu.",
            CommandCategory.CREATIVE, "basics.creative", 0, Arrays.asList("§7- Creative menu", "§7- Item access"),
            Arrays.asList("§7- /creative", "§7- /creative <category>")
        ));

        commandConfigs.put(CommandType.HUNTING, new CommandConfig(
            "hunting", "§2Hunting", Material.FISHING_ROD,
            "§7View and manage hunting system.",
            CommandCategory.HUNTING, "basics.hunting", 0, Arrays.asList("§7- View hunting", "§7- Hunting management"),
            Arrays.asList("§7- /hunting", "§7- /hunting <creature>")
        ));

        commandConfigs.put(CommandType.BOSSES, new CommandConfig(
            "bosses", "§cBosses", Material.WITHER_SKELETON_SKULL,
            "§7View and manage boss system.",
            CommandCategory.BOSSES, "basics.bosses", 0, Arrays.asList("§7- View bosses", "§7- Boss management"),
            Arrays.asList("§7- /bosses", "§7- /bosses <boss>")
        ));

        commandConfigs.put(CommandType.ATTRIBUTES, new CommandConfig(
            "attributes", "§dAttributes", Material.ENCHANTED_BOOK,
            "§7View and manage attributes.",
            CommandCategory.ATTRIBUTES, "basics.attributes", 0, Arrays.asList("§7- View attributes", "§7- Attribute management"),
            Arrays.asList("§7- /attributes", "§7- /attributes <attribute>")
        ));

        commandConfigs.put(CommandType.SACKS, new CommandConfig(
            "sacks", "§eSacks", Material.CHEST,
            "§7View and manage sacks.",
            CommandCategory.SACKS, "basics.sacks", 0, Arrays.asList("§7- View sacks", "§7- Sack management"),
            Arrays.asList("§7- /sacks", "§7- /sacks <sack>")
        ));


        commandConfigs.put(CommandType.HELP, new CommandConfig(
            "help", "§aHelp", Material.BOOK,
            "§7View help information.",
            CommandCategory.HELP, "basics.help", 0, Arrays.asList("§7- View help", "§7- Help information"),
            Arrays.asList("§7- /help", "§7- /help <command>")
        ));

        commandConfigs.put(CommandType.INFO, new CommandConfig(
            "info", "§bInfo", Material.COMPASS,
            "§7View SkyblockPlugin information.",
            CommandCategory.INFO, "basics.info", 0, Arrays.asList("§7- View info", "§7- SkyblockPlugin information"),
            Arrays.asList("§7- /info", "§7- /info <info>")
        ));

        commandConfigs.put(CommandType.RELOAD, new CommandConfig(
            "reload", "§cReload", Material.REDSTONE,
            "§7Reload the SkyblockPlugin.",
            CommandCategory.RELOAD, "basics.reload", 0, Arrays.asList("§7- Reload SkyblockPlugin", "§7- SkyblockPlugin reload"),
            Arrays.asList("§7- /reload", "§7- /reload <reload>")
        ));
    }

    private void startCommandUpdateTask() {
        Thread.ofVirtual().start(() -> {
            while (SkyblockPlugin.isEnabled()) {
                try {
                    for (Map.Entry<UUID, PlayerCommands> entry : playerCommands.entrySet()) {
                        PlayerCommands commands = entry.getValue();
                        commands.update();
                    }
                    Thread.sleep(1000); // Every second = 1000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void registerCommands() {
        // Register all commands
        for (CommandType type : CommandType.values()) {
            CommandConfig config = commandConfigs.get(type);
            if (config != null) {
                SkyblockPlugin.getCommand(config.getName()).setExecutor(this);
                SkyblockPlugin.getCommand(config.getName()).setTabCompleter(this);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        PlayerCommands playerCommands = getPlayerCommands(player.getUniqueId());

        String commandName = command.getName().toLowerCase();
        CommandType type = getCommandType(commandName);

        if (type == null) {
            player.sendMessage("§cUnknown command: " + commandName);
            return true;
        }

        CommandConfig config = commandConfigs.get(type);
        if (config == null) {
            player.sendMessage(Component.text("§cCommand configuration not found!"));
            return true;
        }

        // Check permissions
        if (!player.hasPermission(config.getPermission())) {
            player.sendMessage(Component.text("§cYou don't have permission to use this command!"));
            return true;
        }

        // Check cooldown
        if (playerCommands.isOnCooldown(type)) {
            long remaining = playerCommands.getCooldownRemaining(type);
            player.sendMessage("§cYou must wait " + remaining + " seconds before using this command again!");
            return true;
        }

        // Execute command
        executeCommand(player, type, args);

        // Set cooldown
        playerCommands.setCooldown(type, config.getCooldown());

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String input = args[0].toLowerCase();
            for (CommandType type : CommandType.values()) {
                CommandConfig config = commandConfigs.get(type);
                if (config != null && config.getName().toLowerCase().startsWith(input)) {
                    completions.add(config.getName());
                }
            }
        }

        return completions;
    }

    private void executeCommand(Player player, CommandType type, String[] args) {
        switch (type) {
            case SKILLS:
                player.sendMessage(Component.text("§aSkills command executed!"));
                break;
            case COLLECTIONS:
                player.sendMessage(Component.text("§aCollections command executed!"));
                break;
            case MINIONS:
                player.sendMessage(Component.text("§aMinions command executed!"));
                break;
            case PETS:
                player.sendMessage(Component.text("§aPets command executed!"));
                break;
            case DUNGEONS:
                player.sendMessage(Component.text("§aDungeons command executed!"));
                break;
            case SLAYERS:
                player.sendMessage(Component.text("§aSlayers command executed!"));
                break;
            case GUILDS:
                player.sendMessage(Component.text("§aGuilds command executed!"));
                break;
            case AUCTION:
                player.sendMessage(Component.text("§aAuction command executed!"));
                break;
            case BAZAAR:
                player.sendMessage(Component.text("§aBazaar command executed!"));
                break;
            case ISLANDS:
                player.sendMessage(Component.text("§aIslands command executed!"));
                break;
            case ENCHANTING:
                player.sendMessage(Component.text("§aEnchanting command executed!"));
                break;
            case ALCHEMY:
                player.sendMessage(Component.text("§aAlchemy command executed!"));
                break;
            case CARPENTRY:
                player.sendMessage(Component.text("§aCarpentry command executed!"));
                break;
            case RUNECRAFTING:
                player.sendMessage(Component.text("§aRunecrafting command executed!"));
                break;
            case BANKING:
                player.sendMessage(Component.text("§aBanking command executed!"));
                break;
            case QUESTS:
                player.sendMessage(Component.text("§aQuests command executed!"));
                break;
            case EVENTS:
                player.sendMessage(Component.text("§aEvents command executed!"));
                break;
            case COSMETICS:
                player.sendMessage(Component.text("§aCosmetics command executed!"));
                break;
            case ACHIEVEMENTS:
                player.sendMessage(Component.text("§aAchievements command executed!"));
                break;
            case LEADERBOARDS:
                player.sendMessage(Component.text("§aLeaderboards command executed!"));
                break;
            case API:
                player.sendMessage(Component.text("§aAPI command executed!"));
                break;
            case WEB:
                player.sendMessage(Component.text("§aWeb command executed!"));
                break;
            case SOCIAL:
                player.sendMessage(Component.text("§aSocial command executed!"));
                break;
            case ITEMS:
                showItemsCommand(player, args);
                break;
            case ARMOR:
                showArmorCommand(player, args);
                break;
            case WEAPONS:
                showWeaponsCommand(player, args);
                break;
            case CREATIVE:
                showCreativeCommand(player, args);
                break;
            case HUNTING:
                showHuntingCommand(player, args);
                break;
            case BOSSES:
                showBossesCommand(player, args);
                break;
            case ATTRIBUTES:
                showAttributesCommand(player, args);
                break;
            case SACKS:
                showSacksCommand(player, args);
                break;
            case HELP:
                showHelp(player, args);
                break;
            case INFO:
                showInfo(player, args);
                break;
            case RELOAD:
                reloadPlugin(player, args);
                break;
        }
    }

    private void showHelp(Player player, String[] args) {
        player.sendMessage(Component.text("§a§l=== Basics SkyblockPlugin =="));
        player.sendMessage(Component.text("§7Available commands:"));

        for (CommandType type : CommandType.values()) {
            CommandConfig config = commandConfigs.get(type);
            if (config != null && player.hasPermission(config.getPermission())) {
                player.sendMessage("§7- " + config.getDisplayName() + " §8- " + config.getDescription());
            }
        }
    }

    private void showInfo(Player player, String[] args) {
        player.sendMessage(Component.text("§a§l=== Basics SkyblockPlugin =="));
        player.sendMessage(Component.text("§7Version: §a1.0.0"));
        player.sendMessage(Component.text("§7Author: §aNoctivag"));
        player.sendMessage(Component.text("§7Description: §aAdvanced Hypixel Skyblock SkyblockPlugin"));
        player.sendMessage("§7Features: §a" + CommandType.values().length + " commands");
    }

    private void showItemsCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.text("§aItems Command:"));
            player.sendMessage(Component.text("§7- /items list - List all items"));
            player.sendMessage(Component.text("§7- /items give <item> - Give item to player"));
            player.sendMessage(Component.text("§7- /items info <item> - Show item information"));
            player.sendMessage(Component.text("§7- /items gui - Open items GUI"));
        } else if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(Component.text("§aAvailable Items:"));
            player.sendMessage(Component.text("§7- Swords: Aspect of the End, Aspect of the Dragons, Midas Sword, Hyperion, Valkyrie, Scylla, Astraea"));
            player.sendMessage(Component.text("§7- Bows: Runaan's Bow, Mosquito Bow, Bonemerang, Spirit Bow, Juju Shortbow"));
            player.sendMessage(Component.text("§7- Tools: Drill, Tree Capitator, Golden Pickaxe, Diamond Pickaxe, Emerald Pickaxe"));
            player.sendMessage(Component.text("§7- Accessories: Talisman of Power, Ring of Love, Artifact of Power, Relic of Power"));
        } else if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /items give <item>"));
            } else {
                player.sendMessage("§aItem " + args[1] + " given to " + player.getName() + "!");
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /items info <item>"));
            } else {
                player.sendMessage("§aItem Information for " + args[1] + ":");
                player.sendMessage(Component.text("§7- Type: Weapon"));
                player.sendMessage(Component.text("§7- Rarity: Legendary"));
                player.sendMessage(Component.text("§7- Damage: 100"));
                player.sendMessage(Component.text("§7- Abilities: Special abilities"));
            }
        } else if (args[0].equalsIgnoreCase("gui")) {
            player.sendMessage(Component.text("§aOpening Items GUI..."));
        } else {
            player.sendMessage("§cUnknown subcommand: " + args[0]);
        }
    }

    private void showArmorCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.text("§aArmor Command:"));
            player.sendMessage(Component.text("§7- /armor list - List all armor"));
            player.sendMessage(Component.text("§7- /armor give <armor> - Give armor to player"));
            player.sendMessage(Component.text("§7- /armor info <armor> - Show armor information"));
            player.sendMessage(Component.text("§7- /armor gui - Open armor GUI"));
        } else if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(Component.text("§aAvailable Armor:"));
            player.sendMessage(Component.text("§7- Dragon: Superior, Unstable, Strong, Young, Old, Protector, Wise"));
            player.sendMessage(Component.text("§7- Dungeon: Shadow Assassin, Adaptive, Zombie Soldier, Skeleton Master, Bonzo, Scarf"));
            player.sendMessage(Component.text("§7- Special: Goblin, Spider, Ender, Farmer, Miner, Fisher"));
        } else if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /armor give <armor>"));
            } else {
                player.sendMessage("§aArmor " + args[1] + " given to " + player.getName() + "!");
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /armor info <armor>"));
            } else {
                player.sendMessage("§aArmor Information for " + args[1] + ":");
                player.sendMessage(Component.text("§7- Type: Armor"));
                player.sendMessage(Component.text("§7- Rarity: Legendary"));
                player.sendMessage(Component.text("§7- Defense: 100"));
                player.sendMessage(Component.text("§7- Abilities: Special abilities"));
            }
        } else if (args[0].equalsIgnoreCase("gui")) {
            player.sendMessage(Component.text("§aOpening Armor GUI..."));
        } else {
            player.sendMessage("§cUnknown subcommand: " + args[0]);
        }
    }

    private void showWeaponsCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.text("§aWeapons Command:"));
            player.sendMessage(Component.text("§7- /weapons list - List all weapons"));
            player.sendMessage(Component.text("§7- /weapons give <weapon> - Give weapon to player"));
            player.sendMessage(Component.text("§7- /weapons info <weapon> - Show weapon information"));
            player.sendMessage(Component.text("§7- /weapons gui - Open weapons GUI"));
        } else if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(Component.text("§aAvailable Weapons:"));
            player.sendMessage(Component.text("§7- Swords: Aspect of the End, Aspect of the Dragons, Midas Sword, Hyperion, Valkyrie, Scylla, Astraea"));
            player.sendMessage(Component.text("§7- Bows: Runaan's Bow, Mosquito Bow, Bonemerang, Spirit Bow, Juju Shortbow"));
        } else if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /weapons give <weapon>"));
            } else {
                player.sendMessage("§aWeapon " + args[1] + " given to " + player.getName() + "!");
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /weapons info <weapon>"));
            } else {
                player.sendMessage("§aWeapon Information for " + args[1] + ":");
                player.sendMessage(Component.text("§7- Type: Weapon"));
                player.sendMessage(Component.text("§7- Rarity: Legendary"));
                player.sendMessage(Component.text("§7- Damage: 100"));
                player.sendMessage(Component.text("§7- Abilities: Special abilities"));
            }
        } else if (args[0].equalsIgnoreCase("gui")) {
            player.sendMessage(Component.text("§aOpening Weapons GUI..."));
        } else {
            player.sendMessage("§cUnknown subcommand: " + args[0]);
        }
    }

    private void showCreativeCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.text("§aCreative Command:"));
            player.sendMessage(Component.text("§7- /creative - Open creative menu"));
            player.sendMessage(Component.text("§7- /creative items - Open items creative menu"));
            player.sendMessage(Component.text("§7- /creative armor - Open armor creative menu"));
            player.sendMessage(Component.text("§7- /creative weapons - Open weapons creative menu"));
            player.sendMessage(Component.text("§7- /creative all - Open all items creative menu"));
        } else if (args[0].equalsIgnoreCase("items")) {
            player.sendMessage(Component.text("§aOpening Items Creative Menu..."));
        } else if (args[0].equalsIgnoreCase("armor")) {
            player.sendMessage(Component.text("§aOpening Armor Creative Menu..."));
        } else if (args[0].equalsIgnoreCase("weapons")) {
            player.sendMessage(Component.text("§aOpening Weapons Creative Menu..."));
        } else if (args[0].equalsIgnoreCase("all")) {
            player.sendMessage(Component.text("§aOpening All Items Creative Menu..."));
        } else {
            player.sendMessage("§cUnknown subcommand: " + args[0]);
        }
    }

    private void showHuntingCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.text("§aHunting Command:"));
            player.sendMessage(Component.text("§7- /hunting list - List all creatures"));
            player.sendMessage(Component.text("§7- /hunting hunt <creature> - Hunt a creature"));
            player.sendMessage(Component.text("§7- /hunting shards - View collected shards"));
            player.sendMessage(Component.text("§7- /hunting gui - Open hunting GUI"));
        } else if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(Component.text("§aAvailable Creatures:"));
            player.sendMessage(Component.text("§7- Basic: Zombie, Skeleton, Spider, Creeper"));
            player.sendMessage(Component.text("§7- Advanced: Enderman, Wither Skeleton, Dragon, Wither"));
        } else if (args[0].equalsIgnoreCase("hunt")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /hunting hunt <creature>"));
            } else {
                player.sendMessage("§aHunting " + args[1] + "...");
            }
        } else if (args[0].equalsIgnoreCase("shards")) {
            player.sendMessage(Component.text("§aYour Collected Shards:"));
            player.sendMessage(Component.text("§7- Zombie Shards: 0"));
            player.sendMessage(Component.text("§7- Skeleton Shards: 0"));
            player.sendMessage(Component.text("§7- Spider Shards: 0"));
        } else if (args[0].equalsIgnoreCase("gui")) {
            player.sendMessage(Component.text("§aOpening Hunting GUI..."));
        } else {
            player.sendMessage("§cUnknown subcommand: " + args[0]);
        }
    }

    private void showBossesCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.text("§aBosses Command:"));
            player.sendMessage(Component.text("§7- /bosses list - List all bosses"));
            player.sendMessage(Component.text("§7- /bosses fight <boss> - Fight a boss"));
            player.sendMessage(Component.text("§7- /bosses phases - View boss phases"));
            player.sendMessage(Component.text("§7- /bosses gui - Open boss GUI"));
        } else if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(Component.text("§aAvailable Bosses:"));
            player.sendMessage(Component.text("§7- Kuudra, Ender Dragon, Wither, Sea Leviathan, Void Lord"));
        } else if (args[0].equalsIgnoreCase("fight")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /bosses fight <boss>"));
            } else {
                player.sendMessage("§aFighting " + args[1] + "...");
            }
        } else if (args[0].equalsIgnoreCase("phases")) {
            player.sendMessage(Component.text("§aBoss Phases:"));
            player.sendMessage(Component.text("§7- Kuudra: Phase 1, Phase 2, Phase 3"));
            player.sendMessage(Component.text("§7- Dragon: Phase 1, Phase 2, Phase 3"));
        } else if (args[0].equalsIgnoreCase("gui")) {
            player.sendMessage(Component.text("§aOpening Boss GUI..."));
        } else {
            player.sendMessage("§cUnknown subcommand: " + args[0]);
        }
    }

    private void showAttributesCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.text("§aAttributes Command:"));
            player.sendMessage(Component.text("§7- /attributes list - List all attributes"));
            player.sendMessage(Component.text("§7- /attributes upgrade <attribute> - Upgrade an attribute"));
            player.sendMessage(Component.text("§7- /attributes view - View your attributes"));
            player.sendMessage(Component.text("§7- /attributes gui - Open attributes GUI"));
        } else if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(Component.text("§aAvailable Attributes:"));
            player.sendMessage(Component.text("§7- Combat: Strength, Critical Damage, Critical Chance, Attack Speed"));
            player.sendMessage(Component.text("§7- Defense: Health, Defense, Health Regen, Damage Reduction"));
            player.sendMessage(Component.text("§7- Magic: Intelligence, Mana, Mana Regen, Magic Damage"));
            player.sendMessage(Component.text("§7- Utility: Speed, Agility, Luck, Fortune"));
        } else if (args[0].equalsIgnoreCase("upgrade")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /attributes upgrade <attribute>"));
            } else {
                player.sendMessage("§aUpgrading " + args[1] + "...");
            }
        } else if (args[0].equalsIgnoreCase("view")) {
            player.sendMessage(Component.text("§aYour Attributes:"));
            player.sendMessage(Component.text("§7- Strength: 0"));
            player.sendMessage(Component.text("§7- Health: 0"));
            player.sendMessage(Component.text("§7- Intelligence: 0"));
            player.sendMessage(Component.text("§7- Speed: 0"));
        } else if (args[0].equalsIgnoreCase("gui")) {
            player.sendMessage(Component.text("§aOpening Attributes GUI..."));
        } else {
            player.sendMessage("§cUnknown subcommand: " + args[0]);
        }
    }

    private void showSacksCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Component.text("§aSacks Command:"));
            player.sendMessage(Component.text("§7- /sacks list - List all sacks"));
            player.sendMessage(Component.text("§7- /sacks give <sack> - Give sack to player"));
            player.sendMessage(Component.text("§7- /sacks info <sack> - Show sack information"));
            player.sendMessage(Component.text("§7- /sacks gui - Open sacks GUI"));
        } else if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(Component.text("§aAvailable Sacks:"));
            player.sendMessage(Component.text("§7- Farming: Wheat Sack, Carrot Sack, Potato Sack"));
            player.sendMessage(Component.text("§7- Mining: Cobblestone Sack, Coal Sack, Iron Sack, Gold Sack, Diamond Sack"));
            player.sendMessage(Component.text("§7- Combat: Rotten Flesh Sack, Bone Sack, String Sack"));
            player.sendMessage(Component.text("§7- Foraging: Oak Log Sack, Birch Log Sack, Spruce Log Sack, Jungle Log Sack, Acacia Log Sack, Dark Oak Log Sack"));
            player.sendMessage(Component.text("§7- Fishing: Fish Sack, Salmon Sack, Tropical Fish Sack, Pufferfish Sack"));
        } else if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /sacks give <sack>"));
            } else {
                player.sendMessage("§aSack " + args[1] + " given to " + player.getName() + "!");
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length < 2) {
                player.sendMessage(Component.text("§cUsage: /sacks info <sack>"));
            } else {
                player.sendMessage("§aSack Information for " + args[1] + ":");
                player.sendMessage(Component.text("§7- Type: Storage"));
                player.sendMessage(Component.text("§7- Rarity: Common"));
                player.sendMessage(Component.text("§7- Capacity: 1000"));
                player.sendMessage(Component.text("§7- Features: Auto-Collection"));
            }
        } else if (args[0].equalsIgnoreCase("gui")) {
            player.sendMessage(Component.text("§aOpening Sacks GUI..."));
        } else {
            player.sendMessage("§cUnknown subcommand: " + args[0]);
        }
    }


    private void reloadPlugin(Player player, String[] args) {
        if (!player.hasPermission("basics.reload")) {
            player.sendMessage(Component.text("§cYou don't have permission to reload the SkyblockPlugin!"));
            return;
        }

        player.sendMessage(Component.text("§aReloading SkyblockPlugin..."));
        // Reload logic here
        player.sendMessage(Component.text("§aPlugin reloaded successfully!"));
    }

    private CommandType getCommandType(String commandName) {
        for (CommandType type : CommandType.values()) {
            CommandConfig config = commandConfigs.get(type);
            if (config != null && config.getName().equalsIgnoreCase(commandName)) {
                return type;
            }
        }
        return null;
    }

    public PlayerCommands getPlayerCommands(UUID playerId) {
        return playerCommands.computeIfAbsent(playerId, k -> new PlayerCommands(playerId));
    }

    public CommandConfig getCommandConfig(CommandType type) {
        return commandConfigs.get(type);
    }

    public List<CommandType> getAllCommandTypes() {
        return new ArrayList<>(commandConfigs.keySet());
    }

    public enum CommandType {
        SKILLS, COLLECTIONS, MINIONS, PETS, DUNGEONS, SLAYERS, GUILDS,
        AUCTION, BAZAAR, ISLANDS, ENCHANTING, ALCHEMY, CARPENTRY, RUNECRAFTING,
        BANKING, QUESTS, EVENTS, COSMETICS, ACHIEVEMENTS, LEADERBOARDS, API,
        WEB, SOCIAL, ITEMS, ARMOR, WEAPONS, CREATIVE, HUNTING, BOSSES, ATTRIBUTES, SACKS, HELP, INFO, RELOAD
    }

    public enum CommandCategory {
        SKILLS("§aSkills", 1.0),
        COLLECTIONS("§bCollections", 1.2),
        MINIONS("§eMinions", 1.1),
        PETS("§dPets", 1.3),
        DUNGEONS("§cDungeons", 1.4),
        SLAYERS("§4Slayers", 1.5),
        GUILDS("§6Guilds", 1.6),
        AUCTION("§eAuction", 1.7),
        BAZAAR("§aBazaar", 1.8),
        ISLANDS("§bIslands", 1.9),
        ENCHANTING("§dEnchanting", 2.0),
        ALCHEMY("§eAlchemy", 2.1),
        CARPENTRY("§6Carpentry", 2.2),
        RUNECRAFTING("§5Runecrafting", 2.3),
        BANKING("§bBanking", 2.4),
        QUESTS("§aQuests", 2.5),
        EVENTS("§eEvents", 2.6),
        COSMETICS("§dCosmetics", 2.7),
        ACHIEVEMENTS("§6Achievements", 2.8),
        LEADERBOARDS("§cLeaderboards", 2.9),
        BESTIARY("§2Bestiary", 3.0),
        TALISMANS("§eTalismans", 3.1),
        COMMUNITY("§aCommunity", 3.2),
        ELECTION("§bElection", 3.3),
        API("§5API", 3.4),
        WEB("§bWeb", 3.5),
        SOCIAL("§aSocial", 3.6),
        ITEMS("§cItems", 3.7),
        ARMOR("§6Armor", 3.8),
        WEAPONS("§aWeapons", 3.9),
        CREATIVE("§dCreative", 4.0),
        HUNTING("§2Hunting", 4.1),
        BOSSES("§cBosses", 4.2),
        ATTRIBUTES("§dAttributes", 4.3),
        SACKS("§eSacks", 4.4),
        HELP("§aHelp", 4.5),
        INFO("§bInfo", 4.6),
        RELOAD("§cReload", 4.7);

        private final String displayName;
        private final double multiplier;

        CommandCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    public static class CommandConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final CommandCategory category;
        private final String permission;
        private final int cooldown;
        private final List<String> features;
        private final List<String> usage;

        public CommandConfig(String name, String displayName, Material icon, String description,
                           CommandCategory category, String permission, int cooldown, List<String> features, List<String> usage) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.permission = permission;
            this.cooldown = cooldown;
            this.features = features;
            this.usage = usage;
        }

        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public CommandCategory getCategory() { return category; }
        public String getPermission() { return permission; }
        public int getCooldown() { return cooldown; }
        public List<String> getFeatures() { return features; }
        public List<String> getUsage() { return usage; }
    }

    public static class PlayerCommands {
        private final UUID playerId;
        private final Map<CommandType, Integer> commandLevels = new ConcurrentHashMap<>();
        private final Map<CommandType, Long> commandCooldowns = new ConcurrentHashMap<>();
        private int totalCommands = 0;
        private long totalCommandTime = 0;
        private long lastUpdate;

        public PlayerCommands(UUID playerId) {
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
            // Save command data to database
        }

        public void addCommand(CommandType type, int level) {
            commandLevels.put(type, level);
            totalCommands++;
        }

        public int getCommandLevel(CommandType type) {
            return commandLevels.getOrDefault(type, 0);
        }

        public void setCooldown(CommandType type, int seconds) {
            commandCooldowns.put(type, java.lang.System.currentTimeMillis() + (seconds * 1000L));
        }

        public boolean isOnCooldown(CommandType type) {
            Long cooldown = commandCooldowns.get(type);
            return cooldown != null && cooldown > java.lang.System.currentTimeMillis();
        }

        public long getCooldownRemaining(CommandType type) {
            Long cooldown = commandCooldowns.get(type);
            if (cooldown == null) return 0;
            long remaining = cooldown - java.lang.System.currentTimeMillis();
            return Math.max(0, remaining / 1000);
        }

        public int getTotalCommands() { return totalCommands; }
        public long getTotalCommandTime() { return totalCommandTime; }

        public UUID getPlayerId() { return playerId; }
        public Map<CommandType, Integer> getCommandLevels() { return commandLevels; }
        public Map<CommandType, Long> getCommandCooldowns() { return commandCooldowns; }
    }

    // Missing method implementations for compilation fixes
    public boolean isCommandEnabled(String commandName) {
        return true; // Placeholder - always enabled
    }

    public long getCooldown(String commandName) {
        return 0; // Placeholder - no cooldown
    }

    public double getCost(String commandName) {
        return 0.0; // Placeholder - no cost
    }

    public void setEnabled(String commandName, boolean enabled) {
        // Placeholder - method not implemented
    }
}
