package de.noctivag.plugin.kit;
import org.bukkit.inventory.ItemStack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.noctivag.plugin.Plugin;

public class KitCommand implements CommandExecutor {
    private final KitManager kitManager;

    public KitCommand(Plugin plugin) {
        this.kitManager = (KitManager) plugin.getKitManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§cVerwendung: /kit <Name>");
            player.sendMessage("§7Verfügbare Kits: §e" + String.join("§7, §e", kitManager.getKitNames()));
            return true;
        }

        String kitName = args[0];
        kitManager.giveKit(player, kitName);
        return true;
    }
}
