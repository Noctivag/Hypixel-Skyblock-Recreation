package de.noctivag.skyblock.skyblock;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.worlds.WorldManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;

import java.util.UUID;

/**
 * Minimal implementation of island commands to provide basic SkyBlock-like behavior.
 *
 * Supported subcommands:
 * - /island create - creates a private island for the player (if not exists)
 * - /island visit <player> - teleport to another player's island if it exists
 * - /island home - teleport to your own island
 * - /island members - list island members
 * - /island invite <player> - invite/add a member
 * - /island kick <player> - remove a member
 * - /island info - show island info
 * - /island help - show help
 */
public class IslandCommandImpl {

    private final SkyblockPlugin SkyblockPlugin;
    private final WorldManager worldManager;
    private final de.noctivag.skyblock.skyblock.IslandManager islandManager;

    public IslandCommandImpl() {
        // Obtain the SkyblockPlugin main instance via JavaPlugin helper
        this.SkyblockPlugin = org.bukkit.plugin.java.JavaPlugin.getPlugin(de.noctivag.skyblock.SkyblockPlugin.class);
        if (this.SkyblockPlugin == null) {
            throw new IllegalStateException("SkyblockPlugin instance not found");
        }
        this.worldManager = (de.noctivag.skyblock.worlds.WorldManager) this.SkyblockPlugin.getSimpleWorldManager();
        this.islandManager = de.noctivag.skyblock.skyblock.IslandManager.getInstance(this.SkyblockPlugin);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text("§cDieser Befehl kann nur von Spielern benutzt werden."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "create":
                handleCreate(player, args);
                break;
            case "visit":
                handleVisit(player, args);
                break;
            case "home":
                handleHome(player);
                break;
            case "members":
                handleMembers(player);
                break;
            case "invite":
                handleInvite(player, args);
                break;
            case "kick":
                handleKick(player, args);
                break;
            case "transfer":
                handleTransfer(player, args);
                break;
            case "delete":
                handleDelete(player, args);
                break;
            case "info":
                handleInfo(player);
                break;
            case "help":
                sendHelp(player);
                break;
            default:
                player.sendMessage(Component.text("§cUnbekannter Insel-Befehl. Benutze /island help"));
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== Insel Befehle ==="));
        player.sendMessage(Component.text("§7/island create - Erstelle deine private Insel"));
        player.sendMessage(Component.text("§7/island visit <player> - Besuche die Insel eines Spielers"));
        player.sendMessage(Component.text("§7/island home - Teleportiere zu deiner Insel"));
        player.sendMessage(Component.text("§7/island members - Zeige Insel-Mitglieder"));
        player.sendMessage(Component.text("§7/island invite <player> - Lade einen Spieler ein"));
        player.sendMessage(Component.text("§7/island kick <player> - Entferne einen Spieler von deiner Insel"));
        player.sendMessage(Component.text("§7/island transfer <player> - Übertrage die Insel an einen anderen Spieler"));
        player.sendMessage(Component.text("§7/island delete confirm - Lösche deine Insel (irreversibel). Nutze 'confirm' um zu bestätigen."));
        player.sendMessage(Component.text("§7/island info - Zeige Insel-Informationen"));
        player.sendMessage(Component.text("§7/island help - Zeige diese Hilfe"));
    }

    private void handleTransfer(Player player, String[] args) {
        // permission check
        if (!player.hasPermission("basicsplugin.island.transfer")) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung, eine Insel zu übertragen."));
            return;
        }
        if (args.length < 2) {
            player.sendMessage(Component.text("§cBenutzung: /island transfer <player>"));
            return;
        }
        UUID owner = player.getUniqueId();
        if (islandManager.getIslandByOwner(owner) == null) {
            player.sendMessage(Component.text("§cDu besitzt keine Insel, die du übertragen könntest."));
            return;
        }

        String targetName = args[1];
        // Accept offline players as transfer targets
        org.bukkit.OfflinePlayer offline = Bukkit.getOfflinePlayer(targetName);
        if (offline == null || offline.getUniqueId() == null) {
            player.sendMessage(Component.text("§cSpieler nicht gefunden."));
            return;
        }
        UUID newOwner = offline.getUniqueId();
        if (newOwner.equals(owner)) {
            player.sendMessage(Component.text("§cDu kannst die Insel nicht an dich selbst übertragen."));
            return;
        }
        if (islandManager.getIslandByOwner(newOwner) != null) {
            player.sendMessage(Component.text("§cDer Ziel-Spieler besitzt bereits eine Insel."));
            return;
        }

        boolean ok = islandManager.transferOwnership(owner, newOwner);
        if (!ok) {
            player.sendMessage(Component.text("§cÜbertragung fehlgeschlagen."));
            return;
        }

        player.sendMessage(Component.text("§aDeine Insel wurde an §e" + (offline.getName() == null ? newOwner.toString() : offline.getName()) + "§a übertragen."));
        if (offline.isOnline() && offline.getPlayer() != null) {
            offline.getPlayer().sendMessage(Component.text("§aDir wurde die Insel von §e" + player.getName() + "§a übertragen."));
        }
    }

    private void handleDelete(Player player, String[] args) {
        // permission check
        if (!player.hasPermission("basicsplugin.island.delete")) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung, deine Insel zu löschen."));
            return;
        }
        UUID owner = player.getUniqueId();
        if (islandManager.getIslandByOwner(owner) == null) {
            player.sendMessage(Component.text("§cDu besitzt keine Insel."));
            return;
        }
        // require explicit confirmation
        if (args.length < 2 || !"confirm".equalsIgnoreCase(args[1])) {
            player.sendMessage(Component.text("§cWarnung: Dieser Befehl löscht deine Insel unwiderruflich."));
            player.sendMessage(Component.text("§7Wenn du sicher bist, benutze: /island delete confirm"));
            return;
        }

        boolean removed = islandManager.deleteIsland(owner);
        if (removed) {
            player.sendMessage(Component.text("§aDeine Insel wurde gelöscht."));
        } else {
            player.sendMessage(Component.text("§cFehler beim Löschen der Insel."));
        }
    }

    private void handleCreate(Player player, String[] args) {
        UUID id = player.getUniqueId();

        if (islandManager.getIslandByOwner(id) != null) {
            player.sendMessage(Component.text("§eDu besitzt bereits eine Insel. Teleportiere dich..."));
            teleportToOwnerIsland(player, id);
            return;
        }

        player.sendMessage(Component.text("§aErstelle Insel..."));

        boolean created = islandManager.createIsland(id);
        if (!created) {
            player.sendMessage(Component.text("§cFehler beim Erstellen der Insel. Versuche später erneut."));
            return;
        }

        // Teleport player to their island world
        de.noctivag.skyblock.skyblock.IslandManager.Island island = islandManager.getIslandByOwner(id);
        if (island == null) {
            player.sendMessage(Component.text("§cFehler: Insel nicht gefunden nach Erstellung."));
            return;
        }

        World world = worldManager.getWorld(island.getWorldName());
        if (world == null) {
            // fallback to shared private world
            world = worldManager.getWorld("skyblock_private");
            if (world == null) {
                player.sendMessage(Component.text("§cKonnte keine Insel-Welt erstellen oder finden."));
                return;
            }
            player.sendMessage(Component.text("§eInsel wurde erstellt, du wurdest zur privaten Inselwelt teleportiert (Platzhalter)."));
        }

        Location spawn = world.getSpawnLocation();
        Bukkit.getScheduler().runTask(SkyblockPlugin, () -> player.teleport(spawn));
        player.sendMessage(Component.text("§aInsel erstellt: Du wurdest zu deiner Insel teleportiert."));
    }

    private void handleVisit(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cBenutzung: /island visit <player>"));
            return;
        }

        String targetName = args[1];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            player.sendMessage(Component.text("§cSpieler nicht online oder nicht gefunden."));
            return;
        }

        UUID targetId = target.getUniqueId();
        if (islandManager.getIslandByOwner(targetId) == null) {
            player.sendMessage(Component.text("§cDieser Spieler besitzt keine Insel."));
            return;
        }

        teleportToOwnerIsland(player, targetId);
        player.sendMessage(Component.text("§aDu wurdest zur Insel von §e" + target.getName() + "§a teleportiert."));
    }

    private void handleHome(Player player) {
        UUID id = player.getUniqueId();
        if (islandManager.getIslandByOwner(id) == null) {
            player.sendMessage(Component.text("§eDu besitzt noch keine spezielle Insel. Benutze /island create"));
            return;
        }
        teleportToOwnerIsland(player, id);
        player.sendMessage(Component.text("§aDu wurdest zu deiner Insel teleportiert."));
    }

    private void handleMembers(Player player) {
        UUID id = player.getUniqueId();
        if (islandManager.getIslandByOwner(id) == null) {
            player.sendMessage(Component.text("§cDu besitzt keine Insel."));
            return;
        }
        de.noctivag.skyblock.skyblock.IslandManager.Island island = islandManager.getIslandByOwner(id);
        player.sendMessage(Component.text("§6§l=== Insel Mitglieder ==="));
        for (UUID member : new java.util.ArrayList<>(island.getMembers())) {
            String name = Bukkit.getOfflinePlayer(member).getName();
            player.sendMessage(Component.text("§7- §e" + (name == null ? member.toString() : name)));
        }
    }

    private void handleInvite(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cBenutzung: /island invite <player>"));
            return;
        }
        UUID id = player.getUniqueId();
        if (islandManager.getIslandByOwner(id) == null) {
            player.sendMessage(Component.text("§cDu besitzt keine Insel. Erstelle zuerst eine mit /island create"));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            player.sendMessage(Component.text("§cSpieler nicht online gefunden."));
            return;
        }
        boolean added = islandManager.addMember(id, target.getUniqueId());
        if (added) {
            player.sendMessage(Component.text("§aSpieler eingeladen: §e" + target.getName()));
            target.sendMessage(Component.text("§aDu wurdest zur Insel von §e" + player.getName() + "§a eingeladen."));
        } else {
            player.sendMessage(Component.text("§cKonnte Spieler nicht hinzufügen (vielleicht bereits Mitglied)."));
        }
    }

    private void handleKick(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("§cBenutzung: /island kick <player>"));
            return;
        }
        UUID id = player.getUniqueId();
        if (islandManager.getIslandByOwner(id) == null) {
            player.sendMessage(Component.text("§cDu besitzt keine Insel."));
            return;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            player.sendMessage(Component.text("§cSpieler nicht online gefunden."));
            return;
        }
        boolean removed = islandManager.removeMember(id, target.getUniqueId());
        if (removed) {
            player.sendMessage(Component.text("§aSpieler entfernt: §e" + target.getName()));
            target.sendMessage(Component.text("§cDu wurdest von der Insel von §e" + player.getName() + "§c entfernt."));
        } else {
            player.sendMessage(Component.text("§cKonnte Spieler nicht entfernen (vielleicht war er nicht Mitglied oder du bist nicht der Besitzer)."));
        }
    }

    private void handleInfo(Player player) {
        UUID id = player.getUniqueId();
        if (islandManager.getIslandByOwner(id) == null) {
            player.sendMessage(Component.text("§cDu besitzt keine Insel."));
            return;
        }
        de.noctivag.skyblock.skyblock.IslandManager.Island island = islandManager.getIslandByOwner(id);
        player.sendMessage(Component.text("§6§l=== Insel Informationen ==="));
        player.sendMessage(Component.text("§7Inhaber: §e" + Bukkit.getOfflinePlayer(island.getOwner()).getName()));
        player.sendMessage(Component.text("§7Welt: §e" + island.getWorldName()));
        player.sendMessage(Component.text("§7Mitglieder: §e" + island.getMembers().size()));
    }

    private void teleportToOwnerIsland(Player player, UUID ownerId) {
        de.noctivag.skyblock.skyblock.IslandManager.Island island = islandManager.getIslandByOwner(ownerId);
        if (island == null) {
            player.sendMessage(Component.text("§cDie Insel wurde nicht gefunden."));
            return;
        }
        World world = worldManager.getWorld(island.getWorldName());
        if (world == null) {
            world = worldManager.getWorld("skyblock_private");
            if (world == null) {
                player.sendMessage(Component.text("§cKonnte die Insel-Welt nicht finden."));
                return;
            }
            player.sendMessage(Component.text("§eHinweis: Ziel-Insel ist ein Platzhalter in der privaten Inselwelt."));
        }
        Location spawn = world.getSpawnLocation();
        Bukkit.getScheduler().runTask(SkyblockPlugin, () -> player.teleport(spawn));
    }

    private String getPlayerIslandWorldName(UUID playerId) {
        // Use a deterministic world name for player's island so it can be created/loaded on demand
        return "island_" + playerId.toString().replace('-', '_');
    }
}
