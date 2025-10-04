package de.noctivag.plugin.locations.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand implements CommandExecutor {
    private final Plugin plugin;

    public SetHomeCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§cBitte gib einen Namen für dein Home an!");
            return true;
        }

        String homeName = args[0];
        int currentHomes = plugin.getLocationManager().getPlayerHomeCount(player);
        int maxHomes = plugin.getLocationManager().getMaxHomes();

        try {
            plugin.getLocationManager().setHome(player, homeName, player.getLocation());
            player.sendMessage("§aHome §e" + homeName + " §awurde gesetzt! §7(" + currentHomes + "/" + maxHomes + ")");
        } catch (IllegalStateException e) {
            player.sendMessage("§cDu hast bereits die maximale Anzahl an Homes erreicht! §7(" + maxHomes + ")");
        }

        return true;
    }
}
