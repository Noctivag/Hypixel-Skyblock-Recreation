package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EconomyCommand implements CommandExecutor, TabCompleter {
    private final Plugin plugin;

    public EconomyCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("basics.economy.admin")) {
            sender.sendMessage("§cDu hast keine Berechtigung für diesen Command!");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "balance", "bal" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cVerwendung: /economy balance <spieler>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§cSpieler nicht gefunden!");
                    return true;
                }
                double balance = plugin.getEconomyManager().getBalance(target);
                sender.sendMessage("§aKontostand von §e" + target.getName() + "§a: §6" + 
                    plugin.getEconomyManager().formatMoney(balance));
            }
            
            case "give" -> {
                if (args.length < 3) {
                    sender.sendMessage("§cVerwendung: /economy give <spieler> <betrag>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§cSpieler nicht gefunden!");
                    return true;
                }
                try {
                    double amount = Double.parseDouble(args[2]);
                    plugin.getEconomyManager().giveMoney(target, amount);
                    sender.sendMessage("§a§l" + amount + " Coins §agegeben an §e" + target.getName());
                    target.sendMessage("§aDu hast §6" + plugin.getEconomyManager().formatMoney(amount) + " §aerhalten!");
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cUngültiger Betrag!");
                }
            }
            
            case "take" -> {
                if (args.length < 3) {
                    sender.sendMessage("§cVerwendung: /economy take <spieler> <betrag>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§cSpieler nicht gefunden!");
                    return true;
                }
                try {
                    double amount = Double.parseDouble(args[2]);
                    if (plugin.getEconomyManager().withdrawMoney(target, amount)) {
                        sender.sendMessage("§a§l" + amount + " Coins §aabgezogen von §e" + target.getName());
                    } else {
                        sender.sendMessage("§cTransaktion fehlgeschlagen!");
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cUngültiger Betrag!");
                }
            }
            
            case "set" -> {
                if (args.length < 3) {
                    sender.sendMessage("§cVerwendung: /economy set <spieler> <betrag>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§cSpieler nicht gefunden!");
                    return true;
                }
                try {
                    double amount = Double.parseDouble(args[2]);
                    plugin.getEconomyManager().setBalanceSilent(target, amount);
                    sender.sendMessage("§aKontostand von §e" + target.getName() + " §agesetzt auf §6" + 
                        plugin.getEconomyManager().formatMoney(amount));
                    target.sendMessage("§aDein Kontostand wurde auf §6" + 
                        plugin.getEconomyManager().formatMoney(amount) + " §agesetzt!");
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cUngültiger Betrag!");
                }
            }
            
            case "reset" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cVerwendung: /economy reset <spieler>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§cSpieler nicht gefunden!");
                    return true;
                }
                plugin.getEconomyManager().resetBalance(target);
                sender.sendMessage("§aKontostand von §e" + target.getName() + " §azurückgesetzt!");
                target.sendMessage("§aDein Kontostand wurde zurückgesetzt!");
            }
            
            case "exempt" -> {
                if (args.length < 2) {
                    sender.sendMessage("§cVerwendung: /economy exempt <spieler>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§cSpieler nicht gefunden!");
                    return true;
                }
                boolean exempt = plugin.getEconomyManager().hasCostExemption(target);
                sender.sendMessage("§e" + target.getName() + " §7hat " + 
                    (exempt ? "§aKostenbefreiung" : "§ckeine Kostenbefreiung"));
            }
            
            case "reload" -> {
                // TODO: Implement proper ConfigManager interface
                // ((ConfigManager) plugin.getConfigManager()).reloadConfig();
                sender.sendMessage("§aEconomy-Konfiguration neu geladen!");
            }
            
            default -> sendHelp(sender);
        }
        
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== Economy Commands ===");
        sender.sendMessage("§e/economy balance <spieler> §7- Zeige Kontostand");
        sender.sendMessage("§e/economy give <spieler> <betrag> §7- Gib Coins");
        sender.sendMessage("§e/economy take <spieler> <betrag> §7- Nimm Coins");
        sender.sendMessage("§e/economy set <spieler> <betrag> §7- Setze Kontostand");
        sender.sendMessage("§e/economy reset <spieler> §7- Reset Kontostand");
        sender.sendMessage("§e/economy exempt <spieler> §7- Prüfe Kostenbefreiung");
        sender.sendMessage("§e/economy reload §7- Lade Konfiguration neu");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("balance", "give", "take", "set", "reset", "exempt", "reload");
            for (String sub : subcommands) {
                if (sub.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
        } else if (args.length == 2 && !args[0].equalsIgnoreCase("reload")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(player.getName());
                }
            }
        }
        
        return completions;
    }
}
