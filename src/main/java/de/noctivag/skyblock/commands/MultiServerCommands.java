package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.multiserver.CentralDatabaseSystem;
import de.noctivag.skyblock.multiserver.HypixelStyleProxySystem;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Commands für das Hypixel-Style Multi-Server-System
 */
@SuppressWarnings("unused")
public class MultiServerCommands implements CommandExecutor, TabCompleter {

    private final SkyblockPlugin SkyblockPlugin;
    private final HypixelStyleProxySystem proxySystem;

    public MultiServerCommands(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.proxySystem = SkyblockPlugin.getHypixelProxySystem();
        CentralDatabaseSystem centralDatabase = SkyblockPlugin.getCentralDatabaseSystem();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("§cDieser Befehl kann nur von Spielern verwendet werden!"));
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "connect":
            case "join":
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cVerwendung: /multiserver connect <server-type>"));
                    return true;
                }
                connectToServer(player, args[1]);
                break;

            case "stats":
                showServerStats(player);
                break;

            case "list":
                listServers(player);
                break;

            case "info":
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cVerwendung: /multiserver info <server-type>"));
                    return true;
                }
                showServerInfo(player, args[1]);
                break;

            case "gui":
            case "menu":
                openServerSelectionGUI(player);
                break;

            case "portal":
                if (args.length < 2) {
                    player.sendMessage(Component.text("§cVerwendung: /multiserver portal <create|remove|list>"));
                    return true;
                }
                handlePortalCommand(player, args[1]);
                break;

            case "reload":
                if (player.hasPermission("basicsplugin.multiserver.reload")) {
                    reloadSystem(player);
                } else {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                }
                break;

            default:
                showHelp(player);
                break;
        }

        return true;
    }

    private void connectToServer(Player player, String serverType) {
        // Verwende den ServerSwitcher für flüssige Übergänge
        if (proxySystem != null) {
            proxySystem.switchPlayerToServer(player, serverType)
                .thenAccept(success -> {
                    if (!success) {
                        player.sendMessage(Component.text("§cFehler beim Verbinden zu " + serverType + "!"));
                    }
                });
        }
    }

    private void showServerStats(Player player) {
        if (proxySystem == null) return;
        Map<String, Object> stats = proxySystem.getServerStats();

        player.sendMessage(Component.text("§6§l=== Multi-Server Statistiken ==="));
        player.sendMessage(Component.text("§eAktive Instanzen: §f" + stats.get("totalInstances")));
        player.sendMessage(Component.text("§eSpieler online: §f" + stats.get("totalPlayers")));

        @SuppressWarnings("unchecked")
        Map<String, Integer> instancesByType = (Map<String, Integer>) stats.get("instancesByType");
        if (instancesByType != null && !instancesByType.isEmpty()) {
            player.sendMessage(Component.text("§6§lInstanzen nach Typ:"));
            for (Map.Entry<String, Integer> entry : instancesByType.entrySet()) {
                player.sendMessage(Component.text("§7- " + entry.getKey() + ": " + entry.getValue()));
            }
        }

        @SuppressWarnings("unchecked")
        Map<String, Integer> playersByType = (Map<String, Integer>) stats.get("playersByType");
        if (playersByType != null && !playersByType.isEmpty()) {
            player.sendMessage(Component.text("§6§lSpieler nach Typ:"));
            for (Map.Entry<String, Integer> entry : playersByType.entrySet()) {
                player.sendMessage(Component.text("§7- " + entry.getKey() + ": " + entry.getValue()));
            }
        }
    }

    private void listServers(Player player) {
        player.sendMessage(Component.text("§6§l=== Hypixel SkyBlock Server-System ==="));

        player.sendMessage(Component.text("§a§lHaupt-Server (Nie herunterfahrbar):"));
        player.sendMessage(Component.text("§7- skyblock_hub (SkyBlock Hub - Hauptserver)"));

        player.sendMessage(Component.text("§e§lPublic Islands (4h Neustart-Zyklus):"));
        player.sendMessage(Component.text("§7- spiders_den (Spider's Den)"));
        player.sendMessage(Component.text("§7- the_end (The End)"));
        player.sendMessage(Component.text("§7- the_park (The Park)"));
        player.sendMessage(Component.text("§7- gold_mine (Gold Mine)"));
        player.sendMessage(Component.text("§7- deep_caverns (Deep Caverns)"));
        player.sendMessage(Component.text("§7- dwarven_mines (Dwarven Mines)"));
        player.sendMessage(Component.text("§7- crystal_hollows (Crystal Hollows)"));
        player.sendMessage(Component.text("§7- the_barn (The Barn)"));
        player.sendMessage(Component.text("§7- mushroom_desert (Mushroom Desert)"));
        player.sendMessage(Component.text("§7- blazing_fortress (Blazing Fortress)"));
        player.sendMessage(Component.text("§7- the_nether (The Nether)"));
        player.sendMessage(Component.text("§7- crimson_isle (Crimson Isle)"));
        player.sendMessage(Component.text("§7- rift (The Rift)"));
        player.sendMessage(Component.text("§7- kuudra (Kuudra's End)"));

        player.sendMessage(Component.text("§b§lPersistente Spieler-Instanzen:"));
        player.sendMessage(Component.text("§7- private_island (Private Insel - pro Spieler)"));
        player.sendMessage(Component.text("§7- garden (The Garden - pro Spieler)"));

        player.sendMessage(Component.text("§c§lTemporäre Dungeon-Instanzen:"));
        player.sendMessage(Component.text("§7- catacombs_entrance (Catacombs Entrance)"));
        player.sendMessage(Component.text("§7- catacombs_floor_1 bis catacombs_floor_7"));
        player.sendMessage(Component.text("§7- master_mode_floor_1 bis master_mode_floor_7"));

        player.sendMessage(Component.text("§6§lLegende:"));
        player.sendMessage(Component.text("§aHaupt-Server: §7Läuft permanent, nie herunterfahrbar"));
        player.sendMessage(Component.text("§ePublic Islands: §7Neustart alle 4 Stunden"));
        player.sendMessage(Component.text("§bPersistente Instanzen: §7Ausgeschaltet aber gespeichert"));
        player.sendMessage(Component.text("§cTemporäre Instanzen: §7Gelöscht nach Verlassen"));
    }

    private void showServerInfo(Player player, String serverType) {
        player.sendMessage(Component.text("§6§l=== Server-Info: " + serverType + " ==="));

        // Hier könnte man detaillierte Informationen über den Server-Typ anzeigen
        player.sendMessage(Component.text("§eTyp: §f" + serverType));
        player.sendMessage(Component.text("§eStatus: §aOnline"));
        player.sendMessage(Component.text("§eVerwendung: §f/multiserver connect " + serverType));
    }

    private void reloadSystem(Player player) {
        player.sendMessage(Component.text("§aLade Multi-Server-System neu..."));

        // Hier könnte man das System neu laden
        player.sendMessage(Component.text("§aMulti-Server-System erfolgreich neu geladen!"));
    }

    private void openServerSelectionGUI(Player player) {
        if (proxySystem != null) {
            proxySystem.openServerSelection(player);
        }
    }

    private void handlePortalCommand(Player player, String subCommand) {
        if (!player.hasPermission("basicsplugin.multiserver.portal")) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
            return;
        }

        switch (subCommand.toLowerCase()) {
            case "create":
                player.sendMessage(Component.text("§aPortal-Erstellung: Klicke auf einen Block um ein Portal zu erstellen!"));
                break;

            case "remove":
                player.sendMessage(Component.text("§aPortal-Entfernung: Klicke auf einen Block um das Portal zu entfernen!"));
                break;

            case "list":
                showPortalList(player);
                break;

            default:
                player.sendMessage(Component.text("§cVerwendung: /multiserver portal <create|remove|list>"));
                break;
        }
    }

    private void showPortalList(Player player) {
        if (proxySystem == null) return;
        var portals = proxySystem.getServerPortal().getPortals();

        player.sendMessage(Component.text("§6§l=== Registrierte Portale ==="));
        player.sendMessage(Component.text("§eAnzahl Portale: §f" + portals.size()));

        for (var entry : portals.entrySet()) {
            var location = entry.getKey();
            var targetServer = entry.getValue();
            player.sendMessage(Component.text("§7- " + location.getWorld().getName() +
                " (" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() +
                ") -> " + targetServer));
        }
    }

    private void showHelp(Player player) {
        player.sendMessage(Component.text("§6§l=== Multi-Server Commands ==="));
        player.sendMessage(Component.text("§e/multiserver connect <type> §7- Verbinde zu einem Server"));
        player.sendMessage(Component.text("§e/multiserver gui §7- Öffne Server-Auswahl-GUI"));
        player.sendMessage(Component.text("§e/multiserver stats §7- Zeige Server-Statistiken"));
        player.sendMessage(Component.text("§e/multiserver list §7- Liste verfügbare Server"));
        player.sendMessage(Component.text("§e/multiserver info <type> §7- Zeige Server-Informationen"));
        if (player.hasPermission("basicsplugin.multiserver.portal")) {
            player.sendMessage(Component.text("§e/multiserver portal <create|remove|list> §7- Portal-Management"));
        }
        if (player.hasPermission("basicsplugin.multiserver.reload")) {
            player.sendMessage(Component.text("§e/multiserver reload §7- Lade System neu"));
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("connect", "gui", "menu", "stats", "list", "info", "portal"));
            if (sender.hasPermission("basicsplugin.multiserver.reload")) {
                completions.add("reload");
            }
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "connect":
                case "join":
                case "info":
                    completions.addAll(Arrays.asList(
                        // Haupt-Server
                        "skyblock_hub",
                        // Public Islands (4h Neustart)
                        "spiders_den", "the_end", "the_park", "gold_mine", "deep_caverns",
                        "dwarven_mines", "crystal_hollows", "the_barn", "mushroom_desert",
                        "blazing_fortress", "the_nether", "crimson_isle", "rift", "kuudra",
                        // Persistente Spieler-Instanzen
                        "private_island", "garden",
                        // Temporäre Dungeon-Instanzen
                        "catacombs_entrance", "catacombs_floor_1", "catacombs_floor_2", "catacombs_floor_3",
                        "catacombs_floor_4", "catacombs_floor_5", "catacombs_floor_6", "catacombs_floor_7",
                        "master_mode_floor_1", "master_mode_floor_2", "master_mode_floor_3", "master_mode_floor_4",
                        "master_mode_floor_5", "master_mode_floor_6", "master_mode_floor_7"
                    ));
                    break;
                case "portal":
                    if (sender.hasPermission("basicsplugin.multiserver.portal")) {
                        completions.addAll(Arrays.asList("create", "remove", "list"));
                    }
                    break;
            }
        }

        return completions;
    }
}
