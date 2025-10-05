package de.noctivag.skyblock.commands;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.features.menu.SkyblockMenuSystem;
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
                player.sendMessage(Component.text("§cUnbekannter Befehl! Verwende §e/vault help §cfür Hilfe."));
                break;
        }
        
        return true;
    }
    
    /**
     * Show help message
     */
    private void showHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== VAULT BEFEHLE ==="));
        player.sendMessage(Component.text("§e/vault §7- Öffnet das Hauptmenü"));
        player.sendMessage(Component.text("§e/vault help §7- Zeigt diese Hilfe an"));
        player.sendMessage(Component.text("§e/vault main §7- Öffnet das Hauptmenü"));
        player.sendMessage(Component.text("§e/vault profile §7- Öffnet das Profil-Menü"));
        player.sendMessage(Component.text("§e/vault weapons §7- Öffnet das Waffen-Menü"));
        player.sendMessage(Component.text("§e/vault swords §7- Öffnet das Schwerter-Menü"));
    }
    
    /**
     * Open main SkyBlock menu
     */
    private void openMainMenu(Player player) {
        menuSystem.openMainMenu(player);
        player.sendMessage(Component.text("§6§lVault §7→ §6SkyBlock Hauptmenü geöffnet!"));
        player.sendMessage(Component.text("§7Verwende §e/vault help §7für alle verfügbaren Menüs!"));
    }
    
    /**
     * Open profile menu
     */
    private void openProfileMenu(Player player) {
        menuSystem.openProfileMenu(player);
        player.sendMessage(Component.text("§6§lVault §7→ §6Profil-Menü geöffnet!"));
    }
    
    /**
     * Open weapons menu
     */
    private void openWeaponsMenu(Player player) {
        menuSystem.openWeaponsMenu(player);
        player.sendMessage(Component.text("§6§lVault §7→ §6Waffen-Menü geöffnet!"));
    }
    
    /**
     * Open swords menu
     */
    private void openSwordsMenu(Player player) {
        menuSystem.openSwordsMenu(player);
        player.sendMessage(Component.text("§6§lVault §7→ §6Schwerter-Menü geöffnet!"));
    }
    
    @Override
    public void initialize() {
        status = SystemStatus.INITIALIZING;
        status = SystemStatus.RUNNING;
    }
    
    @Override
    public void shutdown() {
        status = SystemStatus.SHUTTING_DOWN;
        status = SystemStatus.UNINITIALIZED;
    }
    
    @Override
    public String getName() {
        return "VaultCommand";
    }
    
    @Override
    public SystemStatus getStatus() {
        return status;
    }
    
    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            status = SystemStatus.RUNNING;
        } else {
            status = SystemStatus.UNINITIALIZED;
        }
    }
}
