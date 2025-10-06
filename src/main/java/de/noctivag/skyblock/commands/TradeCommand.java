package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.trading.TradingSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TradeCommand implements CommandExecutor, TabCompleter {
    
    private final TradingSystem tradingSystem;
    
    public TradeCommand(SkyblockPlugin plugin, TradingSystem tradingSystem) {
        this.tradingSystem = tradingSystem;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage("§cUsage: /trade <player>");
            player.sendMessage("§cUsage: /trade accept <player>");
            player.sendMessage("§cUsage: /trade deny <player>");
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "accept":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /trade accept <player>");
                    return true;
                }
                
                Player requester = Bukkit.getPlayer(args[1]);
                if (requester == null) {
                    player.sendMessage("§cPlayer not found!");
                    return true;
                }
                
                if (tradingSystem.acceptTradeRequest(player, requester)) {
                    player.sendMessage("§aTrade request accepted!");
                } else {
                    player.sendMessage("§cFailed to accept trade request!");
                }
                break;
                
            case "deny":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /trade deny <player>");
                    return true;
                }
                
                Player requester2 = Bukkit.getPlayer(args[1]);
                if (requester2 == null) {
                    player.sendMessage("§cPlayer not found!");
                    return true;
                }
                
                if (tradingSystem.denyTradeRequest(player, requester2)) {
                    player.sendMessage("§aTrade request denied!");
                } else {
                    player.sendMessage("§cFailed to deny trade request!");
                }
                break;
                
            default:
                // Send trade request
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§cPlayer not found!");
                    return true;
                }
                
                if (tradingSystem.sendTradeRequest(player, target)) {
                    player.sendMessage("§aTrade request sent!");
                } else {
                    player.sendMessage("§cFailed to send trade request!");
                }
                break;
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // First argument: accept, deny, or player name
            completions.add("accept");
            completions.add("deny");
            
            // Add online player names
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getName().equals(sender.getName())) {
                    completions.add(player.getName());
                }
            }
        } else if (args.length == 2) {
            // Second argument: player name for accept/deny
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getName().equals(sender.getName())) {
                    completions.add(player.getName());
                }
            }
        }
        
        return completions;
    }
}
