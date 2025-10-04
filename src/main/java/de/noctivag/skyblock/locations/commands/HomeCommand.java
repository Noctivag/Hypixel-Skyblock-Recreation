package de.noctivag.skyblock.locations.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.locations.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    private final SkyblockPlugin plugin;

    public HomeCommand(SkyblockPlugin plugin) {
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
            // Liste alle Homes auf
            java.util.Set<String> homes = plugin.getLocationManager().getHomeNames(player);
            if (homes.isEmpty()) {
                player.sendMessage("§cDu hast noch keine Homes gesetzt!");
            } else {
                player.sendMessage("§7Deine Homes: §e" + String.join("§7, §e", homes));
            }
            return true;
        }

        String homeName = args[0].toLowerCase();
        de.noctivag.plugin.locations.Home home = plugin.getLocationManager().getHome(player, homeName);

        if (home == null) {
            player.sendMessage("§cDas Home §e" + homeName + " §cexistiert nicht!");
            return true;
        }

        player.teleport(home.getLocation());
        player.sendMessage("§aDu wurdest zu deinem Home §e" + homeName + " §ateleportiert!");
        return true;
    }
}
