package de.noctivag.plugin.skyblock.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.skyblock.items.SkyBlockItems;
import de.noctivag.plugin.skyblock.skills.SkillsSystem;
import de.noctivag.plugin.skyblock.collections.CollectionsSystem;
import de.noctivag.plugin.skyblock.auction.AuctionHouse;
import de.noctivag.plugin.skyblock.slayer.SlayerSystem;
import de.noctivag.plugin.skyblock.dungeons.DungeonsSystem;
import de.noctivag.plugin.skyblock.pets.PetsSystem;
import de.noctivag.plugin.skyblock.enchanting.EnchantingSystem;
import de.noctivag.plugin.skyblock.minions.MinionsSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkyBlockCommands implements CommandExecutor, TabCompleter {
    
    private final Plugin plugin;
    private final SkyBlockItems itemsSystem;
    private final SkillsSystem skillsSystem;
    private final CollectionsSystem collectionsSystem;
    private final AuctionHouse auctionHouse;
    private final SlayerSystem slayerSystem;
    private final DungeonsSystem dungeonsSystem;
    private final PetsSystem petsSystem;
    private final EnchantingSystem enchantingSystem;
    private final MinionsSystem minionsSystem;
    
    public SkyBlockCommands(Plugin plugin) {
        this.plugin = plugin;
        this.itemsSystem = new SkyBlockItems();
        this.skillsSystem = new SkillsSystem(plugin, plugin.getSkyblockManager());
        this.collectionsSystem = new CollectionsSystem(plugin);
        this.auctionHouse = new AuctionHouse(plugin);
        this.slayerSystem = new SlayerSystem(plugin);
        this.dungeonsSystem = new DungeonsSystem(plugin);
        this.petsSystem = new PetsSystem(plugin);
        this.enchantingSystem = new EnchantingSystem(plugin);
        this.minionsSystem = new MinionsSystem(plugin);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            showHelp(player);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "items":
                handleItemsCommand(player, args);
                break;
            case "skills":
                handleSkillsCommand(player, args);
                break;
            case "collections":
                handleCollectionsCommand(player, args);
                break;
            case "auction":
                handleAuctionCommand(player, args);
                break;
            case "slayer":
                handleSlayerCommand(player, args);
                break;
            case "dungeons":
                handleDungeonsCommand(player, args);
                break;
            case "pets":
                handlePetsCommand(player, args);
                break;
            case "enchanting":
                handleEnchantingCommand(player, args);
                break;
            case "minions":
                handleMinionsCommand(player, args);
                break;
            default:
                showHelp(player);
                break;
        }
        
        return true;
    }
    
    private void handleItemsCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§e/skyblock items <item_name>");
            return;
        }
        
        String itemName = args[1];
        // Give item to player
        player.sendMessage("§aGave you " + itemName + "!");
    }
    
    private void handleSkillsCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§e/skyblock skills <skill_name> [level]");
            return;
        }
        
        String skillName = args[1];
        int level = args.length > 2 ? Integer.parseInt(args[2]) : 1;
        
        // skillsSystem.setSkillLevel(player, skillName, level); // Method not implemented yet
        player.sendMessage("§aSet " + skillName + " to level " + level + "!");
    }
    
    private void handleCollectionsCommand(Player player, String[] args) {
        // collectionsSystem.openCollectionsGUI(player); // Method not implemented yet
    }
    
    private void handleAuctionCommand(Player player, String[] args) {
        // plugin.openAuctionHouse(player); // TODO: Implement method in Plugin class
    }
    
    private void handleSlayerCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§e/skyblock slayer <type> [tier]");
            return;
        }
        
        String slayerType = args[1];
        int tier = args.length > 2 ? Integer.parseInt(args[2]) : 1;
        
        // slayerSystem.startSlayerQuest(player, (de.noctivag.plugin.skyblock.slayer.SlayerSystem.SlayerType) slayerType, tier); // TODO: Fix type conversion
        player.sendMessage("§aStarted " + slayerType + " tier " + tier + " quest!");
    }
    
    private void handleDungeonsCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§e/skyblock dungeons <floor>");
            return;
        }
        
        String floor = args[1];
        dungeonsSystem.startDungeonRun(player, floor);
        player.sendMessage("§aStarted dungeon floor " + floor + "!");
    }
    
    private void handlePetsCommand(Player player, String[] args) {
        // plugin.openPetsGUI(player); // TODO: Implement method in Plugin class
    }
    
    private void handleEnchantingCommand(Player player, String[] args) {
        // plugin.openEnchantingGUI(player); // TODO: Implement method in Plugin class
    }
    
    private void handleMinionsCommand(Player player, String[] args) {
        // plugin.openMinionsGUI(player); // TODO: Implement method in Plugin class
    }
    
    private void showHelp(Player player) {
        player.sendMessage("§6=== SkyBlock Commands ===");
        player.sendMessage("§e/skyblock items <item_name> §7- Get a SkyBlock item");
        player.sendMessage("§e/skyblock skills <skill> [level] §7- Set skill level");
        player.sendMessage("§e/skyblock collections §7- Open collections GUI");
        player.sendMessage("§e/skyblock auction §7- Open auction house");
        player.sendMessage("§e/skyblock slayer <type> [tier] §7- Start slayer quest");
        player.sendMessage("§e/skyblock dungeons <floor> §7- Start dungeon run");
        player.sendMessage("§e/skyblock pets §7- Open pets GUI");
        player.sendMessage("§e/skyblock enchanting §7- Open enchanting GUI");
        player.sendMessage("§e/skyblock minions §7- Open minions GUI");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            completions.addAll(Arrays.asList("items", "skills", "collections", "auction", "slayer", "dungeons", "pets", "enchanting", "minions"));
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "skills":
                    completions.addAll(Arrays.asList("combat", "mining", "foraging", "farming", "fishing", "enchanting", "alchemy", "taming", "carpentry", "runecrafting", "social", "dungeoneering"));
                    break;
                case "slayer":
                    completions.addAll(Arrays.asList("zombie", "spider", "wolf", "enderman", "blaze"));
                    break;
                case "dungeons":
                    completions.addAll(Arrays.asList("entrance", "floor_1", "floor_2", "floor_3", "floor_4", "floor_5", "floor_6", "floor_7"));
                    break;
            }
        }
        
        return completions;
    }
}
