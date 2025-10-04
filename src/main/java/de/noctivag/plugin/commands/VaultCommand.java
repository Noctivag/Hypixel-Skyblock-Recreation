package de.noctivag.plugin.commands;

import de.noctivag.plugin.core.api.Service;
import de.noctivag.plugin.core.api.SystemStatus;
import de.noctivag.plugin.features.menu.SkyblockMenuSystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

/**
 * Vault Command - Opens comprehensive SkyBlock menu with all items and systems
 */
public class VaultCommand implements CommandExecutor, Service {
    
    private final SkyblockMenuSystem menuSystem;
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    
    public VaultCommand(SkyblockMenuSystem menuSystem) {
        this.menuSystem = menuSystem;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern verwendet werden!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length == 0) {
            openMainMenu(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "help":
            case "hilfe":
                showHelp(player);
                break;
                
            case "main":
            case "haupt":
                openMainMenu(player);
                break;
                
            case "profile":
            case "profil":
                openProfileMenu(player);
                break;
                
            case "weapons":
            case "waffen":
                openWeaponsMenu(player);
                break;
                
            case "swords":
            case "schwerter":
                openSwordsMenu(player);
                break;
                
            default:
                player.sendMessage("§cUnbekannter Befehl! Verwende §e/vault help §cfür Hilfe.");
                break;
        }
        
        return true;
    }
    
    /**
     * Show help message
     */
    private void showHelp(Player player) {
        player.sendMessage("§6§l=== VAULT BEFEHLE ===");
        player.sendMessage("§e/vault §7- Öffnet das Hauptmenü");
        player.sendMessage("§e/vault help §7- Zeigt diese Hilfe an");
        player.sendMessage("§e/vault main §7- Öffnet das Hauptmenü");
        player.sendMessage("§e/vault profile §7- Öffnet das Profil-Menü");
        player.sendMessage("§e/vault weapons §7- Öffnet das Waffen-Menü");
        player.sendMessage("§e/vault swords §7- Öffnet das Schwerter-Menü");
    }
    
    /**
     * Open main SkyBlock menu
     */
    private void openMainMenu(Player player) {
        menuSystem.openMainMenu(player);
        player.sendMessage("§6§lVault §7→ §6SkyBlock Hauptmenü geöffnet!");
        player.sendMessage("§7Verwende §e/vault help §7für alle verfügbaren Menüs!");
    }
    
    /**
     * Open profile menu
     */
    private void openProfileMenu(Player player) {
        menuSystem.openProfileMenu(player);
        player.sendMessage("§6§lVault §7→ §6Profil-Menü geöffnet!");
    }
    
    /**
     * Open weapons menu
     */
    private void openWeaponsMenu(Player player) {
        menuSystem.openWeaponsMenu(player);
        player.sendMessage("§6§lVault §7→ §6Waffen-Menü geöffnet!");
    }
    
    /**
     * Open swords menu
     */
    private void openSwordsMenu(Player player) {
        menuSystem.openSwordsMenu(player);
        player.sendMessage("§6§lVault §7→ §6Schwerter-Menü geöffnet!");
    }
    
    @Override
    public CompletableFuture<Void> initialize() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.INITIALIZING;
            status = SystemStatus.ENABLED;
        });
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        return CompletableFuture.runAsync(() -> {
            status = SystemStatus.SHUTTING_DOWN;
            status = SystemStatus.UNINITIALIZED;
        });
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }
    
    @Override
    public String getName() {
        return "VaultCommand";
    }
}
