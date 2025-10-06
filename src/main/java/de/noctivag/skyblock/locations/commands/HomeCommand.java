package de.noctivag.skyblock.locations.commands;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.locations.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;

    public HomeCommand(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
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
            Object locationManager = SkyblockPlugin.getLocationManager();
            java.util.Set<String> homes = new java.util.HashSet<>();
            try {
                homes = (java.util.Set<String>) locationManager.getClass().getMethod("getHomeNames", Player.class).invoke(locationManager, player);
            } catch (Exception e) {
                // If method doesn't exist, return empty set
            }
            if (homes.isEmpty()) {
                player.sendMessage(Component.text("§cDu hast noch keine Homes gesetzt!"));
            } else {
                player.sendMessage("§7Deine Homes: §e" + String.join("§7, §e", homes));
            }
            return true;
        }

        String homeName = args[0].toLowerCase();
        Object locationManager = SkyblockPlugin.getLocationManager();
        de.noctivag.skyblock.locations.Home home = null;
        try {
            home = (de.noctivag.skyblock.locations.Home) locationManager.getClass().getMethod("getHome", Player.class, String.class).invoke(locationManager, player, homeName);
        } catch (Exception e) {
            // If method doesn't exist, home will be null
        }

        if (home == null) {
            player.sendMessage("§cDas Home §e" + homeName + " §cexistiert nicht!");
            return true;
        }

        player.teleport(home.getLocation());
        player.sendMessage("§aDu wurdest zu deinem Home §e" + homeName + " §ateleportiert!");
        return true;
    }
}
