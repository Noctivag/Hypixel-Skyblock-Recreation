package de.noctivag.skyblock.locations.commands;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;

    public SetWarpCommand(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("SkyblockPlugin.admin")) {
            player.sendMessage(Component.text("§cDazu hast du keine Berechtigung!"));
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(Component.text("§cVerwendung: /setwarp <Name> [Berechtigung] [Beschreibung]"));
            return true;
        }

        String warpName = args[0];
        String permission = args.length > 1 ? args[1] : "";

        // Kombiniere alle verbleibenden Argumente als Beschreibung
        StringBuilder description = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            description.append(args[i]).append(" ");
        }

        SkyblockPlugin.getLocationManager().setWarp(warpName, player.getLocation(),
            permission, description.toString().trim());

        player.sendMessage("§aWarp §e" + warpName + " §awurde gesetzt!");
        if (!permission.isEmpty()) {
            player.sendMessage("§7Berechtigung: §e" + permission);
        }
        return true;
    }
}
