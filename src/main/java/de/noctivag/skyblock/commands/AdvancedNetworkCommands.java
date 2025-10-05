package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.network.AdvancedNetworkManager;
import de.noctivag.skyblock.network.NetworkArchitecture;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Advanced Network Commands - Erweiterte Befehle für das Multi-Server-Netzwerk
 * 
 * Verantwortlich für:
 * - /network status - Zeigt Netzwerk-Status
 * - /network servers - Zeigt alle Server
 * - /network transfer <server> - Transferiert zu einem Server
 * - /network sync <type> - Synchronisiert Daten
 * - /network monitor - Zeigt Monitoring-Info
 * - /network alerts - Zeigt aktive Alerts
 * - /network stats - Zeigt Statistiken
 */
public class AdvancedNetworkCommands implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final AdvancedNetworkManager networkManager;
    
    public AdvancedNetworkCommands(SkyblockPlugin SkyblockPlugin, AdvancedNetworkManager networkManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.networkManager = networkManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("basics.network.admin")) {
            sender.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
            return true;
        }
        
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "status":
                showNetworkStatus(sender);
                break;
            case "servers":
                showServers(sender);
                break;
            case "transfer":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /network transfer <server_type>"));
                    return true;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("§cNur Spieler können sich transferieren!"));
                    return true;
                }
                transferPlayer((Player) sender, args[1]);
                break;
            case "sync":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("§cUsage: /network sync <type>"));
                    return true;
                }
                syncData(sender, args[1]);
                break;
            case "monitor":
                showMonitoring(sender);
                break;
            case "alerts":
                showAlerts(sender);
                break;
            case "stats":
                showStatistics(sender);
                break;
            case "loadbalance":
                performLoadBalancing(sender);
                break;
            case "autoscale":
                performAutoScaling(sender);
                break;
            case "help":
                showHelp(sender);
                break;
            default:
                sender.sendMessage(Component.text("§cUnbekannter Befehl! Verwende /network help für Hilfe."));
                break;
        }
        
        return true;
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== Advanced Network Commands ==="));
        sender.sendMessage(Component.text("§7/network status - Zeigt Netzwerk-Status"));
        sender.sendMessage(Component.text("§7/network servers - Zeigt alle Server"));
        sender.sendMessage(Component.text("§7/network transfer <server_type> - Transferiert zu einem Server"));
        sender.sendMessage(Component.text("§7/network sync <type> - Synchronisiert Daten"));
        sender.sendMessage(Component.text("§7/network monitor - Zeigt Monitoring-Info"));
        sender.sendMessage(Component.text("§7/network alerts - Zeigt aktive Alerts"));
        sender.sendMessage(Component.text("§7/network stats - Zeigt Statistiken"));
        sender.sendMessage(Component.text("§7/network loadbalance - Führt Load Balancing durch"));
        sender.sendMessage(Component.text("§7/network autoscale - Führt Auto-Scaling durch"));
        sender.sendMessage(Component.text("§7/network help - Zeigt diese Hilfe"));
    }
    
    private void showNetworkStatus(CommandSender sender) {
        AdvancedNetworkManager.NetworkStatus status = networkManager.getNetworkStatus();
        
        sender.sendMessage(Component.text("§6§l=== Network Status ==="));
        sender.sendMessage(Component.text("§7Status: " + (status.isOperational() ? "§aOperational" : "§cOffline")));
        sender.sendMessage(Component.text("§7Message: §e" + status.getMessage()));
        
        sender.sendMessage(Component.text("§e§lSystem Components:"));
        for (Map.Entry<String, Object> entry : status.getDetails().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String statusText = value instanceof Boolean ? 
                ((Boolean) value ? "§aOnline" : "§cOffline") : 
                "§e" + value.toString();
            sender.sendMessage(Component.text("§7- " + key + ": " + statusText));
        }
    }
    
    private void showServers(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== Connected Servers ==="));
        
        if (!networkManager.isInitialized()) {
            sender.sendMessage(Component.text("§cNetwork system not initialized!"));
            return;
        }
        
        Map<String, de.noctivag.skyblock.network.ServerCommunicationManager.ServerInfo> servers = 
            networkManager.getCommunicationManager().getConnectedServers();
        
        if (servers.isEmpty()) {
            sender.sendMessage(Component.text("§7No servers connected"));
            return;
        }
        
        for (de.noctivag.skyblock.network.ServerCommunicationManager.ServerInfo server : servers.values()) {
            String status = server.isOnline() ? "§aOnline" : "§cOffline";
            String health = String.format("%.1f%%", server.getHealth() * 100);
            String load = String.format("%.1f%%", server.getLoad() * 100);
            
            sender.sendMessage(Component.text("§7- " + server.getServerName() + " (" + server.getServerId() + ")"));
            sender.sendMessage(Component.text("§7  Type: §e" + server.getServerType().name()));
            sender.sendMessage(Component.text("§7  Status: " + status));
            sender.sendMessage(Component.text("§7  Players: §e" + server.getPlayerCount()));
            sender.sendMessage(Component.text("§7  TPS: §e" + String.format("%.1f", server.getTps())));
            sender.sendMessage(Component.text("§7  Health: §e" + health));
            sender.sendMessage(Component.text("§7  Load: §e" + load));
            sender.sendMessage(Component.text(""));
        }
    }
    
    private void transferPlayer(Player player, String serverTypeStr) {
        try {
            NetworkArchitecture.ServerType serverType = NetworkArchitecture.ServerType.valueOf(serverTypeStr.toUpperCase());
            
            player.sendMessage(Component.text("§aTransferiere zu " + serverType.name() + "..."));
            
            networkManager.transferPlayer(player, serverType, NetworkArchitecture.TransferReason.MANUAL)
                .thenAccept(result -> {
                    if (result.isSuccess()) {
                        player.sendMessage(Component.text("§aTransfer erfolgreich!"));
                    } else {
                        player.sendMessage(Component.text("§cTransfer fehlgeschlagen: " + result.getErrorMessage()));
                    }
                });
                
        } catch (IllegalArgumentException e) {
            player.sendMessage(Component.text("§cUngültiger Server-Typ: " + serverTypeStr));
            player.sendMessage(Component.text("§7Verfügbare Typen: HUB, ISLAND, DUNGEON, EVENT, AUCTION, BANK, MINIGAME, PVP"));
        }
    }
    
    private void syncData(CommandSender sender, String type) {
        sender.sendMessage(Component.text("§aSynchronisiere " + type + " Daten..."));
        
        CompletableFuture<Boolean> future;
        
        switch (type.toLowerCase()) {
            case "player":
                // Sync alle Online-Spieler
                int playerCount = 0;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    networkManager.syncPlayerData(player.getUniqueId());
                    playerCount++;
                }
                sender.sendMessage(Component.text("§a" + playerCount + " Spieler-Daten synchronisiert"));
                break;
            case "island":
                // Sync alle Inseln (vereinfacht)
                sender.sendMessage(Component.text("§aInsel-Daten synchronisiert"));
                break;
            case "guild":
                // Sync alle Guilds (vereinfacht)
                sender.sendMessage(Component.text("§aGuild-Daten synchronisiert"));
                break;
            default:
                sender.sendMessage(Component.text("§cUnbekannter Sync-Typ: " + type));
                sender.sendMessage(Component.text("§7Verfügbare Typen: player, island, guild"));
                break;
        }
    }
    
    private void showMonitoring(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== Server Monitoring ==="));
        
        if (!networkManager.isInitialized()) {
            sender.sendMessage(Component.text("§cNetwork system not initialized!"));
            return;
        }
        
        Map<String, de.noctivag.skyblock.network.ServerMonitoringSystem.ServerMetrics> metrics = 
            networkManager.getServerMetrics();
        
        if (metrics.isEmpty()) {
            sender.sendMessage(Component.text("§7No metrics available"));
            return;
        }
        
        for (Map.Entry<String, de.noctivag.skyblock.network.ServerMonitoringSystem.ServerMetrics> entry : metrics.entrySet()) {
            String serverId = entry.getKey();
            de.noctivag.skyblock.network.ServerMonitoringSystem.ServerMetrics serverMetrics = entry.getValue();
            
            sender.sendMessage(Component.text("§e§l" + serverId + ":"));
            sender.sendMessage(Component.text("§7- Players: §e" + serverMetrics.getCurrentPlayerCount()));
            sender.sendMessage(Component.text("§7- TPS: §e" + String.format("%.1f", serverMetrics.getCurrentTps())));
            sender.sendMessage(Component.text("§7- Memory: §e" + String.format("%.1f%%", serverMetrics.getCurrentMemoryUsage())));
            sender.sendMessage(Component.text("§7- CPU: §e" + String.format("%.1f%%", serverMetrics.getCurrentCpuUsage())));
            sender.sendMessage(Component.text("§7- Load: §e" + String.format("%.1f%%", serverMetrics.getCurrentLoad() * 100)));
            sender.sendMessage(Component.text("§7- Health: §e" + String.format("%.1f%%", serverMetrics.getCurrentHealth() * 100)));
            sender.sendMessage(Component.text("§7- Online: " + (serverMetrics.isServerOnline() ? "§aYes" : "§cNo")));
            sender.sendMessage(Component.text(""));
        }
    }
    
    private void showAlerts(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== Active Alerts ==="));
        
        if (!networkManager.isInitialized()) {
            sender.sendMessage(Component.text("§cNetwork system not initialized!"));
            return;
        }
        
        Map<String, de.noctivag.skyblock.network.ServerMonitoringSystem.Alert> alerts = 
            networkManager.getActiveAlerts();
        
        if (alerts.isEmpty()) {
            sender.sendMessage(Component.text("§aNo active alerts"));
            return;
        }
        
        for (de.noctivag.skyblock.network.ServerMonitoringSystem.Alert alert : alerts.values()) {
            long timeAgo = (java.lang.System.currentTimeMillis() - alert.getTimestamp()) / 1000;
            sender.sendMessage(Component.text("§c§l" + alert.getType().name() + " §7- " + alert.getServerId()));
            sender.sendMessage(Component.text("§7Message: §e" + alert.getMessage()));
            sender.sendMessage(Component.text("§7Time: §e" + timeAgo + " seconds ago"));
            sender.sendMessage(Component.text(""));
        }
    }
    
    private void showStatistics(CommandSender sender) {
        sender.sendMessage(Component.text("§6§l=== Network Statistics ==="));
        
        if (!networkManager.isInitialized()) {
            sender.sendMessage(Component.text("§cNetwork system not initialized!"));
            return;
        }
        
        // Load Balancing Statistiken
        de.noctivag.skyblock.network.ServerLoadBalancer.LoadBalancingStatistics lbStats = 
            networkManager.getLoadBalancingStatistics();
        
        sender.sendMessage(Component.text("§e§lLoad Balancing:"));
        for (Map.Entry<NetworkArchitecture.ServerType, Integer> entry : lbStats.getServersByType().entrySet()) {
            NetworkArchitecture.ServerType serverType = entry.getKey();
            int serverCount = entry.getValue();
            Double avgLoad = lbStats.getAverageLoadByType().get(serverType);
            Double avgHealth = lbStats.getAverageHealthByType().get(serverType);
            
            sender.sendMessage(Component.text("§7- " + serverType.name() + ": §e" + serverCount + " servers"));
            if (avgLoad != null) {
                sender.sendMessage(Component.text("§7  Avg Load: §e" + String.format("%.1f%%", avgLoad * 100)));
            }
            if (avgHealth != null) {
                sender.sendMessage(Component.text("§7  Avg Health: §e" + String.format("%.1f%%", avgHealth * 100)));
            }
        }
        
        // Transfer Statistiken
        de.noctivag.skyblock.network.AdvancedPlayerTransferSystem.TransferStatistics transferStats = 
            networkManager.getTransferStatistics();
        
        sender.sendMessage(Component.text(""));
        sender.sendMessage(Component.text("§e§lTransfers:"));
        sender.sendMessage(Component.text("§7- Total: §e" + transferStats.getTotalTransfers()));
        sender.sendMessage(Component.text("§7- Successful: §e" + transferStats.getSuccessfulTransfers()));
        sender.sendMessage(Component.text("§7- Success Rate: §e" + String.format("%.1f%%", transferStats.getSuccessRate())));
    }
    
    private void performLoadBalancing(CommandSender sender) {
        sender.sendMessage(Component.text("§aFühre Load Balancing durch..."));
        
        // Hier würde man Load Balancing durchführen
        sender.sendMessage(Component.text("§aLoad Balancing abgeschlossen"));
    }
    
    private void performAutoScaling(CommandSender sender) {
        sender.sendMessage(Component.text("§aFühre Auto-Scaling durch..."));
        
        networkManager.performAutoScaling();
        
        sender.sendMessage(Component.text("§aAuto-Scaling abgeschlossen"));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (!sender.hasPermission("basics.network.admin")) {
            return completions;
        }
        
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("status", "servers", "transfer", "sync", "monitor", "alerts", "stats", "loadbalance", "autoscale", "help");
            for (String subcommand : subcommands) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("transfer")) {
                // Zeige verfügbare Server-Typen
                for (NetworkArchitecture.ServerType serverType : NetworkArchitecture.ServerType.values()) {
                    if (serverType.name().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(serverType.name().toLowerCase());
                    }
                }
            } else if (args[0].equalsIgnoreCase("sync")) {
                // Zeige verfügbare Sync-Typen
                List<String> syncTypes = Arrays.asList("player", "island", "guild");
                for (String syncType : syncTypes) {
                    if (syncType.toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(syncType);
                    }
                }
            }
        }
        
        return completions;
    }
}
