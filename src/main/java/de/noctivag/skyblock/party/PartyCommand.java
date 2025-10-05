package de.noctivag.skyblock.party;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;
    private final PartyManager partyManager;

    public PartyCommand(SkyblockPlugin SkyblockPlugin, PartyManager partyManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.partyManager = partyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Component.text("§e/party invite <Spieler>, /party accept <Leader>, /party leave, /party disband"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list": {
                Party party = partyManager.getParty(player.getUniqueId());
                if (party == null) { player.sendMessage(Component.text("§cDu bist in keiner Party.")); return true; }
                player.sendMessage(Component.text("§5Party-Mitglieder:"));
                for (var id : party.getMembers()) {
                    var p = Bukkit.getPlayer(id);
                    player.sendMessage("§7- " + (p != null ? p.getName() : id.toString()));
                }
                return true;
            }
            case "invite": {
                if (args.length < 2) { player.sendMessage(Component.text("§cNutzung: /party invite <Spieler>")); return true; }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) { player.sendMessage(Component.text("§cSpieler nicht gefunden.")); return true; }
                if (target.getUniqueId().equals(player.getUniqueId())) { player.sendMessage(Component.text("§cDu kannst dich nicht selbst einladen.")); return true; }
                if (partyManager.invite(player, target)) {
                    player.sendMessage("§aEinladung gesendet an §e" + target.getName());
                } else {
                    player.sendMessage(Component.text("§cSpieler ist bereits in der Party."));
                }
                return true;
            }
            case "accept": {
                if (args.length < 2) { player.sendMessage(Component.text("§cNutzung: /party accept <Leader>")); return true; }
                Player leader = Bukkit.getPlayer(args[1]);
                if (leader == null) { player.sendMessage(Component.text("§cLeader nicht gefunden.")); return true; }
                if (partyManager.accept(player, leader)) {
                    player.sendMessage(Component.text("§aParty beigetreten."));
                } else {
                    player.sendMessage(Component.text("§cKeine gültige Einladung gefunden."));
                }
                return true;
            }
            case "leave": {
                partyManager.leave(player);
                player.sendMessage(Component.text("§eDu hast die Party verlassen."));
                return true;
            }
            case "disband": {
                partyManager.disbandParty(player);
                return true;
            }
        }

        player.sendMessage(Component.text("§e/party invite <Spieler>, /party accept <Leader>, /party leave, /party disband"));
        return true;
    }
}


