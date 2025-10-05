package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command for managing brewing modifiers
 */
public class BrewingModifierCommand implements CommandExecutor {
    
    private final SkyblockPlugin plugin;
    private final BrewingModifierManager modifierManager;
    
    public BrewingModifierCommand(SkyblockPlugin plugin, BrewingModifierManager modifierManager) {
        this.plugin = plugin;
        this.modifierManager = modifierManager;
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
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "add":
                if (args.length < 3) {
                    player.sendMessage("§cUsage: /brewingmodifier add <type> <multiplier>");
                    return true;
                }
                addModifier(player, args[1], args[2]);
                break;
            case "remove":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /brewingmodifier remove <type>");
                    return true;
                }
                removeModifier(player, args[1]);
                break;
            case "list":
                listModifiers(player);
                break;
            case "clear":
                clearModifiers(player);
                break;
            case "help":
                showHelp(player);
                break;
            default:
                player.sendMessage("§cUnknown subcommand. Use /brewingmodifier help for help.");
                break;
        }
        
        return true;
    }
    
    private void addModifier(Player player, String typeStr, String multiplierStr) {
        try {
            BrewingModifierType type = BrewingModifierType.valueOf(typeStr.toUpperCase());
            double multiplier = Double.parseDouble(multiplierStr);
            
            BrewingModifier modifier = BrewingModifierFactory.createCustomModifier(
                type.name(), multiplier, "Custom modifier"
            );
            
            modifierManager.addModifier(player.getUniqueId(), type, modifier);
            player.sendMessage("§aAdded brewing modifier: " + type.name() + " (" + multiplier + "x)");
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid modifier type or multiplier!");
        }
    }
    
    private void removeModifier(Player player, String typeStr) {
        try {
            BrewingModifierType type = BrewingModifierType.valueOf(typeStr.toUpperCase());
            modifierManager.removeModifier(player.getUniqueId(), type);
            player.sendMessage("§aRemoved brewing modifier: " + type.name());
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cInvalid modifier type!");
        }
    }
    
    private void listModifiers(Player player) {
        var modifiers = modifierManager.getPlayerModifiers(player.getUniqueId());
        if (modifiers.isEmpty()) {
            player.sendMessage("§eYou have no brewing modifiers.");
            return;
        }
        
        player.sendMessage("§6§lYour Brewing Modifiers:");
        for (var entry : modifiers.entrySet()) {
            BrewingModifier modifier = entry.getValue();
            player.sendMessage("§e" + entry.getKey().name() + ": " + modifier.getMultiplier() + "x");
        }
    }
    
    private void clearModifiers(Player player) {
        modifierManager.clearPlayerModifiers(player.getUniqueId());
        player.sendMessage("§aCleared all brewing modifiers.");
    }
    
    private void showHelp(Player player) {
        player.sendMessage("§6§lBrewing Modifier Commands:");
        player.sendMessage("§e/brewingmodifier add <type> <multiplier> §7- Add a modifier");
        player.sendMessage("§e/brewingmodifier remove <type> §7- Remove a modifier");
        player.sendMessage("§e/brewingmodifier list §7- List your modifiers");
        player.sendMessage("§e/brewingmodifier clear §7- Clear all modifiers");
        player.sendMessage("§e/brewingmodifier help §7- Show this help");
    }
}
