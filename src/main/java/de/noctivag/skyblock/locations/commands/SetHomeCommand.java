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

public class SetHomeCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;

    public SetHomeCommand(SkyblockPlugin SkyblockPlugin) {
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
            player.sendMessage(Component.text("§cBitte gib einen Namen für dein Home an!"));
            return true;
        }

        String homeName = args[0];
        int currentHomes = SkyblockPlugin.getLocationManager().getPlayerHomeCount(player);
        int maxHomes = SkyblockPlugin.getLocationManager().getMaxHomes();

        try {
            SkyblockPlugin.getLocationManager().setHome(player, homeName, player.getLocation());
            player.sendMessage("§aHome §e" + homeName + " §awurde gesetzt! §7(" + currentHomes + "/" + maxHomes + ")");
        } catch (IllegalStateException e) {
            player.sendMessage("§cDu hast bereits die maximale Anzahl an Homes erreicht! §7(" + maxHomes + ")");
        }

        return true;
    }
}
