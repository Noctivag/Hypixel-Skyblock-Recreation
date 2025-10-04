package de.noctivag.plugin.friends;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendsCommand implements CommandExecutor {
    private final FriendsManager friendsManager;

    public FriendsCommand(FriendsManager friendsManager) {
        this.friendsManager = friendsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§e/friends add <Spieler>, /friends accept <Spieler>, /friends remove <Spieler>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add": {
                if (args.length < 2) { player.sendMessage("§cNutzung: /friends add <Spieler>"); return true; }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) { player.sendMessage("§cSpieler nicht gefunden."); return true; }
                friendsManager.requestFriend(player.getUniqueId(), target.getUniqueId());
                player.sendMessage("§aAnfrage gesendet an §e" + target.getName());
                target.sendMessage("§e" + player.getName() + " §7hat dir eine Freundschaftsanfrage gesendet. Nutze §a/friends accept " + player.getName());
                return true;
            }
            case "accept": {
                if (args.length < 2) { player.sendMessage("§cNutzung: /friends accept <Spieler>"); return true; }
                Player from = Bukkit.getPlayer(args[1]);
                if (from == null) { player.sendMessage("§cSpieler nicht gefunden."); return true; }
                if (friendsManager.accept(player, from)) {
                    player.sendMessage("§aIhr seid jetzt Freunde.");
                    from.sendMessage("§a" + player.getName() + " §7hat deine Anfrage akzeptiert.");
                } else {
                    player.sendMessage("§cKeine Anfrage gefunden.");
                }
                return true;
            }
            case "remove": {
                if (args.length < 2) { player.sendMessage("§cNutzung: /friends remove <Spieler>"); return true; }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) { player.sendMessage("§cSpieler nicht gefunden."); return true; }
                if (friendsManager.removeFriend(player, target)) {
                    player.sendMessage("§eFreund entfernt.");
                } else {
                    player.sendMessage("§cWar kein Freund.");
                }
                return true;
            }
        }

        player.sendMessage("§e/friends add <Spieler>, /friends accept <Spieler>, /friends remove <Spieler>");
        return true;
    }
}


