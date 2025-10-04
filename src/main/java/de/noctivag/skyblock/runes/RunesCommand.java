package de.noctivag.skyblock.runes;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
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
 * Runes Command System - Hypixel Skyblock Style
 */
public class RunesCommand implements CommandExecutor, TabCompleter {
    private final SkyblockPlugin plugin;
    private final RunesSystem runesSystem;

    public RunesCommand(SkyblockPlugin plugin, RunesSystem runesSystem) {
        this.plugin = plugin;
        this.runesSystem = runesSystem;
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
            case "apply":
                handleApplyRune(player, args);
                break;
            case "remove":
                handleRemoveRune(player, args);
                break;
            case "combine":
                handleCombineRunes(player, args);
                break;
            case "stats":
                handleRuneStats(player, args);
                break;
            case "gui":
                handleRuneGUI(player, args);
                break;
            case "list":
                handleListRunes(player, args);
                break;
            default:
                showHelp(player);
                break;
        }

        return true;
    }

    private void handleApplyRune(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cVerwendung: /runes apply <rune-type>");
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage("§cDu musst ein Item in der Hand halten!");
            return;
        }

        String runeType = args[1].toUpperCase();
        try {
            RunesSystem.RuneType type = RunesSystem.RuneType.valueOf(runeType);
            if (runesSystem.applyRune(player, item, type)) {
                player.sendMessage("§aRune " + type.getDisplayName() + " erfolgreich angewendet!");
            } else {
                player.sendMessage("§cRune konnte nicht angewendet werden!");
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cUngültiger Rune-Typ: " + runeType);
        }
    }

    private void handleRemoveRune(Player player, String[] args) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage("§cDu musst ein Item in der Hand halten!");
            return;
        }

        if (runesSystem.removeRune(player, item)) {
            player.sendMessage("§aRune erfolgreich entfernt!");
        } else {
            player.sendMessage("§cKeine Rune auf diesem Item gefunden!");
        }
    }

    private void handleCombineRunes(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§cVerwendung: /runes combine <rune1> <rune2>");
            return;
        }

        String rune1Type = args[1].toUpperCase();
        String rune2Type = args[2].toUpperCase();

        try {
            RunesSystem.RuneType type1 = RunesSystem.RuneType.valueOf(rune1Type);
            RunesSystem.RuneType type2 = RunesSystem.RuneType.valueOf(rune2Type);

            if (runesSystem.combineRunes(player, type1, type2)) {
                player.sendMessage("§aRunes erfolgreich kombiniert!");
            } else {
                player.sendMessage("§cRunes konnten nicht kombiniert werden!");
            }
        } catch (IllegalArgumentException e) {
            player.sendMessage("§cUngültige Rune-Typen!");
        }
    }

    private void handleRuneStats(Player player, String[] args) {
        Map<RunesSystem.RuneType, Integer> runeStats = runesSystem.getPlayerRuneStats(player);
        
        player.sendMessage("§6§l=== RUNE STATISTIKEN ===");
        for (Map.Entry<RunesSystem.RuneType, Integer> entry : runeStats.entrySet()) {
            player.sendMessage("§7" + entry.getKey().getDisplayName() + ": §a" + entry.getValue());
        }
    }

    private void handleRuneGUI(Player player, String[] args) {
        runesSystem.openRuneGUI(player);
    }

    private void handleListRunes(Player player, String[] args) {
        player.sendMessage("§6§l=== VERFÜGBARE RUNES ===");
        for (RunesSystem.RuneType type : RunesSystem.RuneType.values()) {
            player.sendMessage("§7- " + type.getDisplayName() + " §8- " + type.getDescription());
        }
    }

    private void showHelp(Player player) {
        player.sendMessage("§6§l=== RUNES BEFEHLE ===");
        player.sendMessage("§e/runes apply <type> §7- Wendet eine Rune auf das Item in der Hand an");
        player.sendMessage("§e/runes remove §7- Entfernt eine Rune vom Item in der Hand");
        player.sendMessage("§e/runes combine <rune1> <rune2> §7- Kombiniert zwei Runes");
        player.sendMessage("§e/runes stats §7- Zeigt deine Rune-Statistiken");
        player.sendMessage("§e/runes gui §7- Öffnet das Rune-GUI");
        player.sendMessage("§e/runes list §7- Listet alle verfügbaren Runes auf");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("apply", "remove", "combine", "stats", "gui", "list"));
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("apply") || args[0].equalsIgnoreCase("combine")) {
                for (RunesSystem.RuneType type : RunesSystem.RuneType.values()) {
                    completions.add(type.name().toLowerCase());
                }
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("combine")) {
            for (RunesSystem.RuneType type : RunesSystem.RuneType.values()) {
                completions.add(type.name().toLowerCase());
            }
        }

        return completions;
    }
}
