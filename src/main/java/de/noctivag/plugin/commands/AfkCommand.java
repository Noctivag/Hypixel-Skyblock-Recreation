package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AfkCommand implements CommandExecutor {
    private static final Set<UUID> afk = new HashSet<>();

    public static boolean isAfk(UUID id) { return afk.contains(id); }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) { sender.sendMessage("Players only"); return true; }
        UUID id = player.getUniqueId();
        boolean nowAfk;
        if (afk.contains(id)) { afk.remove(id); nowAfk = false; } else { afk.add(id); nowAfk = true; }
        player.setSleepingIgnored(nowAfk);
        player.sendMessage(nowAfk ? "§eDu bist jetzt §7AFK" : "§aAFK §7deaktiviert");
        return true;
    }
}
