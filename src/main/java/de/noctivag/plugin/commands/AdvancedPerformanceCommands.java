package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.performance.AdvancedPerformanceManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.List;
import java.util.ArrayList;

public class AdvancedPerformanceCommands implements CommandExecutor, TabCompleter {
    private final Plugin plugin;
    private final AdvancedPerformanceManager performanceManager;

    public AdvancedPerformanceCommands(Plugin plugin, AdvancedPerformanceManager performanceManager) {
        this.plugin = plugin;
        this.performanceManager = performanceManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6Performance Commands:");
            sender.sendMessage("§e/performance stats §7- Show server performance statistics");
            sender.sendMessage("§e/performance optimize §7- Optimize server performance");
            sender.sendMessage("§e/performance monitor §7- Start performance monitoring");
            plugin.getLogger().info("Performance command executed by " + sender.getName());
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "stats":
                showPerformanceStats(sender);
                break;
            case "optimize":
                optimizePerformance(sender);
                break;
            case "monitor":
                toggleMonitoring(sender);
                break;
            default:
                sender.sendMessage("§cUnknown performance command. Use /performance for help.");
                break;
        }
        return true;
    }

    private void showPerformanceStats(CommandSender sender) {
        sender.sendMessage("§6§lServer Performance Statistics:");
        sender.sendMessage("§7TPS: §a" + performanceManager.getTPS());
        sender.sendMessage("§7Memory Usage: §a" + performanceManager.getMemoryUsed() / 1024 / 1024 + "MB");
        sender.sendMessage("§7Max Memory: §a" + performanceManager.getMemoryMax() / 1024 / 1024 + "MB");
        sender.sendMessage("§7Players Online: §a" + performanceManager.getPlayersOnline());
        sender.sendMessage("§7Chunks Loaded: §a" + performanceManager.getChunksLoaded());
        plugin.getLogger().info("Performance stats requested by " + sender.getName());
    }

    private void optimizePerformance(CommandSender sender) {
        System.gc(); // Force garbage collection
        performanceManager.cleanupResources();
        sender.sendMessage("§aPerformance optimization completed!");
        plugin.getLogger().info("Performance optimization executed by " + sender.getName());
    }

    private void toggleMonitoring(CommandSender sender) {
        if (performanceManager.isMonitoring()) {
            performanceManager.stopPerformanceMonitoring();
            sender.sendMessage("§cPerformance monitoring stopped!");
        } else {
            performanceManager.startPerformanceMonitoring();
            sender.sendMessage("§aPerformance monitoring started!");
        }
        plugin.getLogger().info("Performance monitoring toggled by " + sender.getName());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("stats");
            completions.add("optimize");
            completions.add("monitor");
        }
        // Use fields to avoid warnings
        if (plugin != null && performanceManager != null) {
            // Fields are used
        }
        return completions;
    }
}
