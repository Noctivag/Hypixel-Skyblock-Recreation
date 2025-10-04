package de.noctivag.skyblock.brewing.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.brewing.AdvancedBrewingSystem;
import de.noctivag.skyblock.brewing.gui.BrewingGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command handler for brewing system
 */
public class BrewingCommand implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin plugin;
    private final AdvancedBrewingSystem brewingSystem;
    
    public BrewingCommand(SkyblockPlugin plugin, AdvancedBrewingSystem brewingSystem) {
        this.plugin = plugin;
        this.brewingSystem = brewingSystem;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        
        if (args.length == 0) {
            // Open brewing GUI
            BrewingGUI gui = new BrewingGUI(plugin, brewingSystem);
            gui.openBrewingGUI(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "gui", "menu" -> {
                BrewingGUI gui = new BrewingGUI(plugin, brewingSystem);
                gui.openBrewingGUI(player);
            }
            case "station" -> {
                BrewingGUI gui = new BrewingGUI(plugin, brewingSystem);
                gui.openBrewingStationGUI(player);
            }
            case "stats", "statistics" -> {
                BrewingGUI gui = new BrewingGUI(plugin, brewingSystem);
                gui.openBrewingStatisticsGUI(player);
            }
            case "brew" -> {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /brewing brew <recipe>");
                    return true;
                }
                brewPotion(player, args[1]);
            }
            case "info" -> {
                showBrewingInfo(player);
            }
            case "help" -> {
                showBrewingHelp(player);
            }
            default -> {
                player.sendMessage("§cUnknown subcommand! Use /brewing help for help.");
            }
        }
        
        return true;
    }
    
    private void brewPotion(Player player, String recipeName) {
        String recipeId = recipeName.toLowerCase().replace(" ", "_");
        
        if (brewingSystem.brewPotion(player, recipeId)) {
            player.sendMessage("§aSuccessfully brewed " + recipeName + "!");
        } else {
            player.sendMessage("§cFailed to brew " + recipeName + "! Check ingredients and coins.");
        }
    }
    
    private void showBrewingInfo(Player player) {
        var data = brewingSystem.getPlayerBrewingData(player.getUniqueId());
        
        player.sendMessage("§6§l=== Brewing Information ===");
        player.sendMessage("§7Level: §a" + data.getLevel());
        player.sendMessage("§7Experience: §a" + data.getExperience());
        player.sendMessage("§7Coins: §a" + data.getCoins());
        player.sendMessage("§7Brewed Potions: §a" + data.getBrewedPotions());
        player.sendMessage("§7Experience to Next Level: §a" + data.getExperienceToNextLevel());
    }
    
    private void showBrewingHelp(Player player) {
        player.sendMessage("§6§l=== Brewing Commands ===");
        player.sendMessage("§7/brewing - Open brewing GUI");
        player.sendMessage("§7/brewing gui - Open brewing GUI");
        player.sendMessage("§7/brewing station - Open brewing station");
        player.sendMessage("§7/brewing stats - View brewing statistics");
        player.sendMessage("§7/brewing brew <recipe> - Brew a potion");
        player.sendMessage("§7/brewing info - Show brewing information");
        player.sendMessage("§7/brewing help - Show this help");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("gui", "menu", "station", "stats", "statistics", "brew", "info", "help");
            for (String subCommand : subCommands) {
                if (subCommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("brew")) {
            // Add recipe names for brewing
            var recipes = brewingSystem.getAllBrewingRecipes();
            for (String recipeId : recipes.keySet()) {
                String recipeName = recipeId.replace("_", " ");
                if (recipeName.toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(recipeName);
                }
            }
        }
        
        return completions;
    }
}
