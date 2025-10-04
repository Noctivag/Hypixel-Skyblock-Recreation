package de.noctivag.skyblock.network;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Network Commands - Commands für das Multi-Server-Netzwerk
 * 
 * Verantwortlich für:
 * - /network status - Zeigt Netzwerk-Status
 * - /network servers - Zeigt alle Server
 * - /network transfer <server> - Transfer zu Server
 * - /island create - Erstellt Insel
 * - /island visit <player> - Besucht Insel
 * - /island members - Zeigt Insel-Mitglieder
 * - /island invite <player> - Lädt Spieler ein
 * - /island kick <player> - Entfernt Spieler
 */
public class NetworkCommands implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin plugin;
    private final SimpleNetworkManager networkManager;
    
    public NetworkCommands(SkyblockPlugin plugin, SimpleNetworkManager networkManager) {
        this.plugin = plugin;
        this.networkManager = networkManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("§cThis command can only be used by players!"));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!networkManager.isInitialized()) {
            player.sendMessage(Component.text("§cNetwork system is not initialized!"));
            return true;
        }
        
        switch (command.getName().toLowerCase()) {
            case "network":
                handleNetworkCommand(player, args);
                break;
            case "island":
                handleIslandCommand(player, args);
                break;
            case "transfer":
                handleTransferCommand(player, args);
                break;
        }
        
        return true;
    }
    
    private void handleNetworkCommand(Player player, String[] args) {
        if (args.length == 0) {
            showNetworkHelp(player);
            return;
        }
        
        switch (args[0].toLowerCase()) {
            case "status":
                showNetworkStatus(player);
                break;
            case "servers":
                showServers(player);
                break;
            case "info":
                showNetworkInfo(player);
                break;
            case "help":
                showNetworkHelp(player);
                break;
            default:
                player.sendMessage(Component.text("§cUnknown network command! Use /network help for help."));
                break;
        }
    }
    
    private void handleIslandCommand(Player player, String[] args) {
        if (args.length == 0) {
            showIslandHelp(player);
            return;
        }
        
        switch (args[0].toLowerCase()) {
            case "create":
                createIsland(player, args);
                break;
            case "visit":
                visitIsland(player, args);
                break;
            case "members":
                showIslandMembers(player);
                break;
            case "invite":
                invitePlayer(player, args);
                break;
            case "kick":
                kickPlayer(player, args);
                break;
            case "info":
                showIslandInfo(player);
                break;
            case "help":
                showIslandHelp(player);
                break;
            default:
                player.sendMessage(Component.text("§cUnknown island command! Use /island help for help."));
                break;
        }
    }
    
    private void handleTransferCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(Component.text("§cUsage: /transfer <server_type>"));
            return;
        }
        
        NetworkArchitecture.ServerType serverType;
        try {
            serverType = NetworkArchitecture.ServerType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(Component.text("§cInvalid server type! Available types: " + 
                Arrays.toString(NetworkArchitecture.ServerType.values())));
            return;
        }
        
        NetworkArchitecture.TransferReason reason = NetworkArchitecture.TransferReason.HUB_RETURN;
        if (args.length > 1) {
            try {
                reason = NetworkArchitecture.TransferReason.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                // Use default reason
            }
        }
        
        player.sendMessage(Component.text("§aTransferring to " + serverType.getName() + "..."));
        
        networkManager.transferPlayer(player, serverType, reason).thenAccept(success -> {
            if (success) {
                player.sendMessage(Component.text("§aTransfer successful!"));
            } else {
                player.sendMessage(Component.text("§cTransfer failed!"));
            }
        });
    }
    
    private void showNetworkStatus(Player player) {
        player.sendMessage(Component.text("§6§l=== Network Status ==="));
        player.sendMessage(Component.text("§7Server ID: §e" + networkManager.getServerId()));
        player.sendMessage(Component.text("§7Server Type: §e" + networkManager.getServerType().getName()));
        player.sendMessage(Component.text("§7Status: §a" + (networkManager.isInitialized() ? "Connected" : "Disconnected")));
        
        if (networkManager.isInitialized()) {
            player.sendMessage(Component.text("§7Server Type: §e" + networkManager.getServerType().getName()));
            player.sendMessage(Component.text("§7Server ID: §e" + networkManager.getServerId()));
            player.sendMessage(Component.text("§7Online Players: §e" + networkManager.getPlayerServerMap().size()));
        }
    }
    
    private void showServers(Player player) {
        player.sendMessage(Component.text("§6§l=== Network Servers ==="));
        
        if (!networkManager.isInitialized()) {
            player.sendMessage(Component.text("§cNetwork not initialized!"));
            return;
        }
        
        player.sendMessage(Component.text("§e§lAvailable Server Types:"));
        for (NetworkArchitecture.ServerType serverType : NetworkArchitecture.ServerType.values()) {
            String status = serverType == networkManager.getServerType() ? "§aCurrent" : "§7Available";
            player.sendMessage(Component.text("§7- " + serverType.getName() + " §7(" + status + "§7)"));
        }
    }
    
    private void showNetworkInfo(Player player) {
        player.sendMessage(Component.text("§6§l=== Network Information ==="));
        player.sendMessage(Component.text("§7This server is part of a multi-server network"));
        player.sendMessage(Component.text("§7that allows players to transfer between different"));
        player.sendMessage(Component.text("§7server types for various activities."));
        player.sendMessage(Component.text("§7"));
        player.sendMessage(Component.text("§7Available Server Types:"));
        for (NetworkArchitecture.ServerType serverType : NetworkArchitecture.ServerType.values()) {
            player.sendMessage(Component.text("§7- §e" + serverType.getName() + "§7: " + serverType.getDescription()));
        }
    }
    
    private void showNetworkHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== Network Commands ==="));
        player.sendMessage(Component.text("§7/network status - Show network status"));
        player.sendMessage(Component.text("§7/network servers - List all servers"));
        player.sendMessage(Component.text("§7/network info - Show network information"));
        player.sendMessage(Component.text("§7/transfer <server_type> - Transfer to server"));
        player.sendMessage(Component.text("§7"));
        player.sendMessage(Component.text("§7Server Types: " + Arrays.toString(NetworkArchitecture.ServerType.values())));
    }
    
    private void createIsland(Player player, String[] args) {
        NetworkArchitecture.IslandType islandType = NetworkArchitecture.IslandType.PRIVATE;
        
        if (args.length > 1) {
            try {
                islandType = NetworkArchitecture.IslandType.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                player.sendMessage(Component.text("§cInvalid island type! Available types: " + 
                    Arrays.toString(NetworkArchitecture.IslandType.values())));
                return;
            }
        }
        
        player.sendMessage(Component.text("§aCreating " + islandType.getName() + " island..."));
        
        boolean success = networkManager.createIsland(player, islandType);
        if (success) {
            player.sendMessage(Component.text("§aIsland created successfully!"));
        } else {
            player.sendMessage(Component.text("§cFailed to create island!"));
        }
    }
    
    private void visitIsland(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cUsage: /island visit <player>"));
            return;
        }
        
        String targetPlayerName = args[1];
        // In a real implementation, you would look up the player's UUID
        // For now, we'll use a placeholder
        java.util.UUID targetPlayerId = java.util.UUID.randomUUID();
        
        player.sendMessage(Component.text("§aTransferring to " + targetPlayerName + "'s island..."));
        
        boolean success = networkManager.transferPlayerToIsland(player, targetPlayerId);
        if (success) {
            player.sendMessage(Component.text("§aTransfer successful!"));
        } else {
            player.sendMessage(Component.text("§cTransfer failed!"));
        }
    }
    
    private void showIslandMembers(Player player) {
        // Implementation would depend on your island system
        player.sendMessage(Component.text("§6§l=== Island Members ==="));
        player.sendMessage(Component.text("§7This feature will be implemented with the island system."));
    }
    
    private void invitePlayer(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cUsage: /island invite <player>"));
            return;
        }
        
        String targetPlayerName = args[1];
        // Implementation would depend on your island system
        player.sendMessage(Component.text("§aInviting " + targetPlayerName + " to your island..."));
        player.sendMessage(Component.text("§7This feature will be implemented with the island system."));
    }
    
    private void kickPlayer(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cUsage: /island kick <player>"));
            return;
        }
        
        String targetPlayerName = args[1];
        // Implementation would depend on your island system
        player.sendMessage(Component.text("§aRemoving " + targetPlayerName + " from your island..."));
        player.sendMessage(Component.text("§7This feature will be implemented with the island system."));
    }
    
    private void showIslandInfo(Player player) {
        // Implementation would depend on your island system
        player.sendMessage(Component.text("§6§l=== Island Information ==="));
        player.sendMessage(Component.text("§7This feature will be implemented with the island system."));
    }
    
    private void showIslandHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== Island Commands ==="));
        player.sendMessage(Component.text("§7/island create [type] - Create an island"));
        player.sendMessage(Component.text("§7/island visit <player> - Visit player's island"));
        player.sendMessage(Component.text("§7/island members - Show island members"));
        player.sendMessage(Component.text("§7/island invite <player> - Invite player to island"));
        player.sendMessage(Component.text("§7/island kick <player> - Remove player from island"));
        player.sendMessage(Component.text("§7/island info - Show island information"));
        player.sendMessage(Component.text("§7"));
        player.sendMessage(Component.text("§7Island Types: " + Arrays.toString(NetworkArchitecture.IslandType.values())));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (!(sender instanceof Player)) {
            return completions;
        }
        
        switch (command.getName().toLowerCase()) {
            case "network":
                if (args.length == 1) {
                    List<String> subcommands = Arrays.asList("status", "servers", "info", "help");
                    for (String subcommand : subcommands) {
                        if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                            completions.add(subcommand);
                        }
                    }
                }
                break;
                
            case "island":
                if (args.length == 1) {
                    List<String> subcommands = Arrays.asList("create", "visit", "members", "invite", "kick", "info", "help");
                    for (String subcommand : subcommands) {
                        if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                            completions.add(subcommand);
                        }
                    }
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("create")) {
                        for (NetworkArchitecture.IslandType type : NetworkArchitecture.IslandType.values()) {
                            if (type.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                                completions.add(type.name().toLowerCase());
                            }
                        }
                    }
                }
                break;
                
            case "transfer":
                if (args.length == 1) {
                    for (NetworkArchitecture.ServerType serverType : NetworkArchitecture.ServerType.values()) {
                        if (serverType.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                            completions.add(serverType.name().toLowerCase());
                        }
                    }
                } else if (args.length == 2) {
                    for (NetworkArchitecture.TransferReason reason : NetworkArchitecture.TransferReason.values()) {
                        if (reason.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                            completions.add(reason.name().toLowerCase());
                        }
                    }
                }
                break;
        }
        
        return completions;
    }
}
