package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.skyblock.SkyblockMainSystem;
import de.noctivag.skyblock.skyblock.SkyblockMainSystem.PlayerSkyblockData;
import de.noctivag.skyblock.skyblock.SkyblockManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@SuppressWarnings("unused")
public class IslandCommand implements CommandExecutor {
    private final SkyblockPlugin plugin;

    public IslandCommand(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        // Prefer the new SkyblockMainSystem for menus/profiles, but fall back to legacy SkyblockManager for island operations.
        SkyblockMainSystem skyMain = plugin.getSkyblockMainSystem();
        SkyblockManager legacy = plugin.getSkyblockManager();
        if (skyMain == null && legacy == null) {
            player.sendMessage("§cSkyblock systems are not initialized yet.");
            return true;
        }

        if (args.length == 0) {
            // Öffne das Skyblock-Hauptmenü (verwende die neue SkyblockMainSystem API wenn vorhanden)
            if (skyMain != null) {
                skyMain.openMainMenu(player);
            } else {
                player.sendMessage("§eSkyblock menu ist derzeit nicht verfügbar. Verwende /skyblock wenn das System geladen ist.");
            }
            return true;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "create":
                // SkyblockMainSystem verwaltet Profile beim Join; keine create-API vorhanden.
                player.sendMessage("§eProfile/Island-Erstellung wird automatisch beim Join verwaltet. Nutze /skyblock für Menüs.");
                return true;
            case "visit":
                if (args.length < 2) {
                    player.sendMessage("§7Usage: /island visit <player>");
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    player.sendMessage("§cPlayer not found or not online.");
                    return true;
                }
                // Direkter Insel-Teleport ist nicht implementiert in SkyblockMainSystem.
                // Falls wir den legacy SkyblockManager haben, könnten wir ggf. teleportToIsland für den Zielspieler ausführen,
                // aber diese API teleportiert aktuell nur den Aufrufer zu seiner eigenen Insel. Daher bleibt Visit unimplemented.
                player.sendMessage("§eVisit ist derzeit nicht implementiert. Verwende /skyblock für verfügbare Optionen.");
                return true;
            case "invite":
            case "uninvite":
            case "trustlist":
            case "hub":
                // Diese Funktionen sind in der aktuellen SkyblockMainSystem-API nicht vorhanden.
                player.sendMessage("§eDieser Subbefehl ist derzeit nicht implementiert auf diesem Server.");
                return true;
            case "profile":
                if (skyMain != null) {
                    PlayerSkyblockData prof = skyMain.getPlayerData(player);
                    if (prof == null) {
                        player.sendMessage("§cNo profile found for " + player.getName() + ".");
                        return true;
                    }
                    player.sendMessage("§6Skyblock Profile for " + player.getName() + ":");
                    player.sendMessage("§7UUID: §e" + prof.getPlayerId());
                    player.sendMessage("§7Joined (ms): §e" + prof.getJoinTime() + " (" + new Date(prof.getJoinTime()) + ")");
                    player.sendMessage("§7Playtime (ms): §e" + prof.getTotalPlayTime());
                    player.sendMessage("§7Level: §e" + prof.getSkyblockLevel());
                    player.sendMessage("§7XP: §e" + prof.getSkyblockXP());
                } else {
                    // Legacy fallback: we don't have a direct API to read profile fields; inform the player
                    player.sendMessage("§eProfile info not available (legacy skyblock system loaded). Use /skyblock on join to manage profiles.");
                }
                return true;
            default:
                player.sendMessage("§cUnknown subcommand. Use /island [create|visit|invite|uninvite|trustlist|hub|profile]");
                return true;
        }
    }
}
