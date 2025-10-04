package de.noctivag.plugin.travel;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Travel Command System - Hypixel Skyblock Style
 */
public class TravelCommand implements CommandExecutor, TabCompleter {
    private final Plugin plugin;
    private final TravelSystem travelSystem;

    public TravelCommand(Plugin plugin, TravelSystem travelSystem) {
        this.plugin = plugin;
        this.travelSystem = travelSystem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "create":
                handleCreateScroll(player, args);
                break;
            case "use":
                handleUseScroll(player, args);
                break;
            case "list":
                handleListScrolls(player, args);
                break;
            case "gui":
                handleTravelGUI(player, args);
                break;
            case "give":
                handleGiveScroll(player, args);
                break;
            case "remove":
                handleRemoveScroll(player, args);
                break;
            default:
                showHelp(player);
                break;
        }

        return true;
    }

    private void handleCreateScroll(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§cVerwendung: /travel create <name> <location>");
            return;
        }

        String scrollName = args[1];
        String location = args[2];

        if (travelSystem.createTravelScroll(player, scrollName, location)) {
            player.sendMessage("§aTravel Scroll '" + scrollName + "' erfolgreich erstellt!");
        } else {
            player.sendMessage("§cTravel Scroll konnte nicht erstellt werden!");
        }
    }

    private void handleUseScroll(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cVerwendung: /travel use <scroll-name>");
            return;
        }

        String scrollName = args[1];

        if (travelSystem.useTravelScroll(player, scrollName)) {
            player.sendMessage("§aTravel Scroll '" + scrollName + "' verwendet!");
        } else {
            player.sendMessage("§cTravel Scroll nicht gefunden oder nicht verwendbar!");
        }
    }

    private void handleListScrolls(Player player, String[] args) {
        List<TravelSystem.TravelScroll> scrolls = travelSystem.getPlayerScrolls(player);
        
        player.sendMessage("§6§l=== DEINE TRAVEL SCROLLS ===");
        if (scrolls.isEmpty()) {
            player.sendMessage("§7Du hast keine Travel Scrolls.");
        } else {
            for (TravelSystem.TravelScroll scroll : scrolls) {
                player.sendMessage("§7- " + scroll.getName() + " §8- " + scroll.getLocation());
            }
        }
    }

    private void handleTravelGUI(Player player, String[] args) {
        travelSystem.openTravelGUI(player);
    }

    private void handleGiveScroll(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§cVerwendung: /travel give <player> <scroll-name>");
            return;
        }

        String targetName = args[1];
        String scrollName = args[2];

        Player target = plugin.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage("§cSpieler nicht gefunden!");
            return;
        }

        if (travelSystem.giveTravelScroll(player, target, scrollName)) {
            player.sendMessage("§aTravel Scroll '" + scrollName + "' an " + target.getName() + " gegeben!");
            target.sendMessage("§aDu hast einen Travel Scroll von " + player.getName() + " erhalten!");
        } else {
            player.sendMessage("§cTravel Scroll konnte nicht gegeben werden!");
        }
    }

    private void handleRemoveScroll(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cVerwendung: /travel remove <scroll-name>");
            return;
        }

        String scrollName = args[1];

        if (travelSystem.removeTravelScroll(player, scrollName)) {
            player.sendMessage("§aTravel Scroll '" + scrollName + "' entfernt!");
        } else {
            player.sendMessage("§cTravel Scroll nicht gefunden!");
        }
    }

    private void showHelp(Player player) {
        player.sendMessage("§6§l=== TRAVEL BEFEHLE ===");
        player.sendMessage("§e/travel create <name> <location> §7- Erstellt einen Travel Scroll");
        player.sendMessage("§e/travel use <scroll-name> §7- Verwendet einen Travel Scroll");
        player.sendMessage("§e/travel list §7- Listet deine Travel Scrolls");
        player.sendMessage("§e/travel gui §7- Öffnet das Travel GUI");
        player.sendMessage("§e/travel give <player> <scroll-name> §7- Gibt einen Travel Scroll");
        player.sendMessage("§e/travel remove <scroll-name> §7- Entfernt einen Travel Scroll");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("create", "use", "list", "gui", "give", "remove"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("use") || args[0].equalsIgnoreCase("remove")) {
                if (sender instanceof Player player) {
                    List<TravelSystem.TravelScroll> scrolls = travelSystem.getPlayerScrolls(player);
                    for (TravelSystem.TravelScroll scroll : scrolls) {
                        completions.add(scroll.getName());
                    }
                }
            } else if (args[0].equalsIgnoreCase("give")) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                if (sender instanceof Player player) {
                    List<TravelSystem.TravelScroll> scrolls = travelSystem.getPlayerScrolls(player);
                    for (TravelSystem.TravelScroll scroll : scrolls) {
                        completions.add(scroll.getName());
                    }
                }
            }
        }

        return completions;
    }
}
