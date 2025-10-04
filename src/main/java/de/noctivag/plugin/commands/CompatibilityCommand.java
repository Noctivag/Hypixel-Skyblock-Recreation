package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.compatibility.CompatibilityManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

/**
 * CompatibilityCommand - Verwaltet Plugin-Kompatibilität
 */
public class CompatibilityCommand implements CommandExecutor, TabCompleter {
    
    private final CompatibilityManager compatibilityManager;
    
    public CompatibilityCommand(Plugin plugin) {
        this.compatibilityManager = (de.noctivag.plugin.compatibility.CompatibilityManager) plugin.getCompatibilityManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("basicsplugin.compatibility")) {
            sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
            return true;
        }
        
        if (args.length == 0) {
            showCompatibilityReport(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "check":
                showCompatibilityReport(sender);
                break;
            case "report":
                showDetailedReport(sender);
                break;
            case "recommendations":
                showRecommendations(sender);
                break;
            case "recheck":
                recheckCompatibility(sender);
                break;
            case "performance":
                showPerformanceReport(sender);
                break;
            case "help":
                sendHelp(sender);
                break;
            default:
                sender.sendMessage("§cUnbekannter Unterbefehl: " + subCommand);
                sendHelp(sender);
                break;
        }
        
        return true;
    }
    
    private void showCompatibilityReport(CommandSender sender) {
        sender.sendMessage("§6§l=== PLUGIN COMPATIBILITY REPORT ===");
        
        if (compatibilityManager.isCompatible()) {
            sender.sendMessage("§a✅ Plugin is fully compatible with your server!");
        } else {
            sender.sendMessage("§c❌ Compatibility issues detected!");
        }
        
        sender.sendMessage("");
        sender.sendMessage("§7Use §e/compatibility report §7for detailed information");
        sender.sendMessage("§7Use §e/compatibility recommendations §7for optimization tips");
        
        sender.sendMessage("§6================================");
    }
    
    private void showDetailedReport(CommandSender sender) {
        String report = compatibilityManager.getCompatibilityReport();
        String[] lines = report.split("\n");
        
        for (String line : lines) {
            sender.sendMessage(line);
        }
    }
    
    private void showRecommendations(CommandSender sender) {
        String recommendations = compatibilityManager.getRecommendations();
        String[] lines = recommendations.split("\n");
        
        for (String line : lines) {
            sender.sendMessage(line);
        }
    }
    
    private void recheckCompatibility(CommandSender sender) {
        sender.sendMessage("§e§lRechecking compatibility...");
        compatibilityManager.recheckCompatibility();
        sender.sendMessage("§a§lCompatibility check completed!");
        showCompatibilityReport(sender);
    }
    
    private void showPerformanceReport(CommandSender sender) {
        String performanceReport = compatibilityManager.getPerformanceOptimizer().getPerformanceReport();
        String[] lines = performanceReport.split("\n");
        
        for (String line : lines) {
            sender.sendMessage(line);
        }
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== COMPATIBILITY COMMAND HELP ===");
        sender.sendMessage("§7/compatibility §8- Zeigt Kompatibilitätsbericht");
        sender.sendMessage("§7/compatibility check §8- Zeigt Kompatibilitätsbericht");
        sender.sendMessage("§7/compatibility report §8- Zeigt detaillierten Bericht");
        sender.sendMessage("§7/compatibility recommendations §8- Zeigt Empfehlungen");
        sender.sendMessage("§7/compatibility recheck §8- Führt erneute Prüfung durch");
        sender.sendMessage("§7/compatibility performance §8- Zeigt Performance-Report");
        sender.sendMessage("§7/compatibility help §8- Zeigt diese Hilfe");
        sender.sendMessage("§6================================");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("check", "report", "recommendations", "recheck", "performance", "help");
        }
        return null;
    }
}
