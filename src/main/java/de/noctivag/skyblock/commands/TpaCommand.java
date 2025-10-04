package de.noctivag.skyblock.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaCommand implements CommandExecutor {
    private final SkyblockPlugin plugin;
    private final Map<UUID, UUID> pending = new HashMap<>(); // target -> requester

    public TpaCommand(SkyblockPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { sender.sendMessage("Players only"); return true; }
        if (args.length == 0) {
            player.sendMessage("§e/tpa <Spieler> | /tpaccept | /tpdeny");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "accept":
            case "tpaccept": {
                UUID requesterId = pending.remove(player.getUniqueId());
                if (requesterId == null) { player.sendMessage("§cKeine Anfrage."); return true; }
                Player requester = Bukkit.getPlayer(requesterId);
                if (requester == null) { player.sendMessage("§cAnfragender offline."); return true; }
                // TODO: Implement proper TeleportManager interface
                // ((TeleportManager) plugin.getTeleportManager()).setLastLocation(requester, requester.getLocation());
                requester.teleport(player.getLocation());
                player.sendMessage("§aAnfrage akzeptiert.");
                requester.sendMessage("§aTeleportiere zu §e" + player.getName());
                return true;
            }
            case "deny":
            case "tpdeny": {
                UUID requesterId = pending.remove(player.getUniqueId());
                if (requesterId == null) { player.sendMessage("§cKeine Anfrage."); return true; }
                Player requester = Bukkit.getPlayer(requesterId);
                if (requester != null) requester.sendMessage("§cDeine TPA-Anfrage wurde abgelehnt.");
                player.sendMessage("§eAnfrage abgelehnt.");
                return true;
            }
            default: {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) { player.sendMessage("§cSpieler nicht gefunden."); return true; }
                if (target.getUniqueId().equals(player.getUniqueId())) { player.sendMessage("§cUngültig."); return true; }
                pending.put(target.getUniqueId(), player.getUniqueId());
                player.sendMessage("§aAnfrage gesendet an §e" + target.getName());
                target.sendMessage("§e" + player.getName() + " §7möchte sich zu dir teleportieren. §a/tpaccept §7oder §c/tpdeny");
                return true;
            }
        }
    }
}
