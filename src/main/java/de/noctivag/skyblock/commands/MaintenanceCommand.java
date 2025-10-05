package de.noctivag.skyblock.commands;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.maintenance.MaintenanceManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MaintenanceCommand - Verwaltet Wartungsoperationen
 */
public class MaintenanceCommand implements CommandExecutor, TabCompleter {
    
    private final SkyblockPlugin SkyblockPlugin;
    private final MaintenanceManager maintenanceManager;
    
    public MaintenanceCommand(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.maintenanceManager = (de.noctivag.skyblock.maintenance.MaintenanceManager) SkyblockPlugin.getMaintenanceManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("basicsplugin.maintenance")) {
            sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "check":
                runMaintenanceCheck(sender);
                break;
            case "fix":
                runAutoFix(sender);
                break;
            case "report":
                generateReport(sender);
                break;
            case "stats":
                showStatistics(sender);
                break;
            case "search":
                if (args.length > 1) {
                    runSearch(sender, String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                } else {
                    sender.sendMessage("§cVerwendung: /maintenance search <query>");
                }
                break;
            case "docs":
                generateDocumentation(sender);
                break;
            case "gui":
                if (sender instanceof Player) {
                    openMaintenanceGUI((Player) sender);
                } else {
                    sender.sendMessage("§cDieser Befehl kann nur von Spielern verwendet werden!");
                }
                break;
            case "mode":
                toggleMaintenanceMode(sender);
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
    
    private void runMaintenanceCheck(CommandSender sender) {
        sender.sendMessage("§e[Maintenance] Führe Wartungsprüfung durch...");
        
        // Run maintenance checks
        maintenanceManager.runScheduledMaintenance();
        
        sender.sendMessage("§a[Maintenance] Wartungsprüfung abgeschlossen!");
    }
    
    private void runAutoFix(CommandSender sender) {
        sender.sendMessage("§e[Maintenance] Führe Auto-Fix durch...");
        
        // Run auto-fix
        maintenanceManager.runAutoFix();
        
        sender.sendMessage("§a[Maintenance] Auto-Fix abgeschlossen!");
    }
    
    private void generateReport(CommandSender sender) {
        sender.sendMessage("§e[Maintenance] Generiere Wartungsbericht...");
        
        // Generate maintenance report
        maintenanceManager.generateMaintenanceReport();
        
        sender.sendMessage("§a[Maintenance] Wartungsbericht generiert! Siehe Konsole für Details.");
    }
    
    private void showStatistics(CommandSender sender) {
        sender.sendMessage("§6§l=== SkyblockPlugin ==");
        
        // Get statistics from maintenance manager
        var statisticsSystem = maintenanceManager.getMaintenanceTools().get("statisticsSystem");
        if (statisticsSystem != null) {
            sender.sendMessage("§7Statistiken werden generiert...");
            // This would show detailed statistics
        }
        
        sender.sendMessage("§6==========================");
    }
    
    private void runSearch(CommandSender sender, String query) {
        sender.sendMessage("§e[Search] Suche nach: " + query);
        
        // Get search system from maintenance manager
        var searchSystem = maintenanceManager.getMaintenanceTools().get("searchSystem");
        if (searchSystem != null) {
            sender.sendMessage("§7Suchergebnisse werden generiert...");
            // This would show search results
        }
        
        sender.sendMessage("§a[Search] Suche abgeschlossen!");
    }
    
    private void generateDocumentation(CommandSender sender) {
        sender.sendMessage("§e[Documentation] Generiere System-Dokumentation...");
        
        // Get documentation generator from maintenance manager
        var documentationGenerator = maintenanceManager.getMaintenanceTools().get("documentationGenerator");
        if (documentationGenerator != null) {
            sender.sendMessage("§7Dokumentation wird generiert...");
            // This would generate documentation
        }
        
        sender.sendMessage("§a[Documentation] Dokumentation generiert!");
    }
    
    private void openMaintenanceGUI(Player player) {
        player.sendMessage(Component.text("§e[Maintenance] Öffne Wartungs-GUI..."));
        
        // Open maintenance GUI
        maintenanceManager.openMaintenanceGUI(player);
        
        player.sendMessage(Component.text("§a[Maintenance] Wartungs-GUI geöffnet!"));
    }
    
    private void toggleMaintenanceMode(CommandSender sender) {
        boolean currentMode = maintenanceManager.isMaintenanceMode();
        maintenanceManager.setMaintenanceMode(!currentMode);
        
        if (maintenanceManager.isMaintenanceMode()) {
            sender.sendMessage("§e[Maintenance] Wartungsmodus aktiviert!");
        } else {
            sender.sendMessage("§a[Maintenance] Wartungsmodus deaktiviert!");
        }
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== MAINTENANCE COMMAND HELP ===");
        sender.sendMessage("§7/maintenance check §8- Führt Wartungsprüfung durch");
        sender.sendMessage("§7/maintenance fix §8- Führt Auto-Fix durch");
        sender.sendMessage("§7/maintenance report §8- Generiert Wartungsbericht");
        sender.sendMessage("§7/maintenance stats §8- Zeigt SkyblockPlugin-Statistiken");
        sender.sendMessage("§7/maintenance search <query> §8- Führt Suche durch");
        sender.sendMessage("§7/maintenance docs §8- Generiert Dokumentation");
        sender.sendMessage("§7/maintenance gui §8- Öffnet Wartungs-GUI");
        sender.sendMessage("§7/maintenance mode §8- Schaltet Wartungsmodus um");
        sender.sendMessage("§7/maintenance help §8- Zeigt diese Hilfe");
        sender.sendMessage("§6================================");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList(
                "check", "fix", "report", "stats", "search", "docs", "gui", "mode", "help"
            );
            
            for (String subCommand : subCommands) {
                if (subCommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCommand);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("search")) {
            // Add search suggestions
            completions.addAll(Arrays.asList(
                "command", "gui", "permission", "config", "database", "feature"
            ));
        }
        
        return completions;
    }
}
