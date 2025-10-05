package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Brewing command handler
 */
public class BrewingCommand implements CommandExecutor {
    
    private final SkyblockPlugin plugin;
    
    public BrewingCommand(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            // Open brewing GUI
            BrewingGUI gui = new BrewingGUI(plugin, player);
            gui.open(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "list":
                listRecipes(player);
                break;
            case "start":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /brewing start <recipe_id>");
                    return true;
                }
                startBrewing(player, args[1]);
                break;
            case "help":
                showHelp(player);
                break;
            default:
                player.sendMessage("§cUnknown subcommand. Use /brewing help for help.");
                break;
        }
        
        return true;
    }
    
    private void listRecipes(Player player) {
        player.sendMessage("§6§lBrewing Recipes:");
        for (BrewingRecipe recipe : plugin.getBrewingManager().getBrewingSystem().getRecipes().values()) {
            player.sendMessage("§e" + recipe.getName() + " §7(ID: " + recipe.getId() + ")");
        }
    }
    
    private void startBrewing(Player player, String recipeId) {
        if (plugin.getBrewingManager().startBrewing(player, recipeId)) {
            player.sendMessage("§aStarted brewing: " + recipeId);
        } else {
            player.sendMessage("§cFailed to start brewing: " + recipeId);
        }
    }
    
    private void showHelp(Player player) {
        player.sendMessage("§6§lBrewing Commands:");
        player.sendMessage("§e/brewing §7- Open brewing GUI");
        player.sendMessage("§e/brewing list §7- List all recipes");
        player.sendMessage("§e/brewing start <recipe_id> §7- Start brewing a recipe");
        player.sendMessage("§e/brewing help §7- Show this help");
    }
}
