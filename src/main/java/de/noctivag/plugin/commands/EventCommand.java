package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.events.EventManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventCommand implements CommandExecutor, TabCompleter {
    private final Plugin plugin;
    private final EventManager eventManager;

    public EventCommand(Plugin plugin) {
        this.plugin = plugin;
        this.eventManager = (de.noctivag.plugin.events.EventManager) plugin.getEventManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        if (args.length == 0) {
            // Open event menu
            new de.noctivag.plugin.gui.EventMenu(plugin).open(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "join" -> {
                if (args.length < 2) {
                    player.sendMessage("§cVerwendung: /event join <event-id>");
                    player.sendMessage("§7Verfügbare Events:");
                    player.sendMessage("§7- ender_dragon, wither, custom_boss");
                    player.sendMessage("§7- elder_guardian, ravager, phantom_king");
                    player.sendMessage("§7- blaze_king, enderman_lord");
                    return true;
                }
                
                String eventId = args[1].toLowerCase();
                eventManager.joinEvent(player, eventId);
            }
            case "leave" -> {
                if (!eventManager.isPlayerInEvent(player)) {
                    player.sendMessage("§cDu bist in keinem Event!");
                    return true;
                }
                
                eventManager.leaveEvent(player);
            }
            case "list" -> {
                var events = eventManager.getAvailableEvents();
                if (events.isEmpty()) {
                    player.sendMessage("§7Keine aktiven Events verfügbar.");
                } else {
                    player.sendMessage("§e§lVerfügbare Events:");
                    for (var event : events) {
                        player.sendMessage("§7- §f" + event.getName() + " §7(" + 
                            event.getPlayers().size() + "/" + event.getMaxPlayers() + " Spieler)");
                    }
                }
            }
            case "status" -> {
                if (eventManager.isPlayerInEvent(player)) {
                    String currentEvent = eventManager.getPlayerEvent(player);
                    player.sendMessage("§aDu bist im Event: §e" + currentEvent);
                } else {
                    player.sendMessage("§7Du bist in keinem Event.");
                }
            }
            case "help" -> {
                player.sendMessage("§e§lEvent Commands:");
                player.sendMessage("§7/event §7- Öffnet das Event-Menü");
                player.sendMessage("§7/event join <id> §7- Tritt einem Event bei");
                player.sendMessage("§7/event leave §7- Verlässt das aktuelle Event");
                player.sendMessage("§7/event list §7- Zeigt verfügbare Events");
                player.sendMessage("§7/event status §7- Zeigt deinen Event-Status");
            }
            default -> {
                player.sendMessage("§cUnbekannter Befehl! Nutze §e/event help §cfür Hilfe.");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("join", "leave", "list", "status", "help");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
            return Arrays.asList("ender_dragon", "wither", "custom_boss", "elder_guardian", 
                                "ravager", "phantom_king", "blaze_king", "enderman_lord");
        }
        return new ArrayList<>();
    }
}
