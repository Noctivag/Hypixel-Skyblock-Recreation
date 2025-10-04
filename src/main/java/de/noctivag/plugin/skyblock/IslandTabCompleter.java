package de.noctivag.plugin.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class IslandTabCompleter implements TabCompleter {
    private static final List<String> SUBS = List.of("create","visit","home","members","invite","kick","transfer","delete","info","help");
    private final Plugin plugin;

    public IslandTabCompleter(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) return Collections.emptyList();
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            return SUBS.stream().filter(s -> s.startsWith(partial)).collect(Collectors.toList());
        }
        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            String partial = args[1].toLowerCase();

            // Use IslandManager for owner/member queries when available
            IslandManager islandManager = IslandManager.getInstance(plugin);

            switch (sub) {
                case "visit": {
                    // Suggest island owners' names (including offline) that start with the partial
                    List<String> names = new ArrayList<>();
                    for (java.util.UUID owner : islandManager.getAllOwners()) {
                        String name = Bukkit.getOfflinePlayer(owner).getName();
                        if (name != null && name.toLowerCase().startsWith(partial)) names.add(name);
                    }
                    names.sort(String.CASE_INSENSITIVE_ORDER);
                    return names;
                }

                case "invite": {
                    // Suggest online players not already members of the sender's island
                    if (!(sender instanceof Player)) return Collections.emptyList();
                    Player player = (Player) sender;
                    IslandManager.Island island = islandManager.getIslandByOwner(player.getUniqueId());
                    java.util.Set<java.util.UUID> members = island == null ? java.util.Collections.emptySet() : island.getMembers();
                    List<String> names = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getUniqueId().equals(player.getUniqueId())) continue; // skip self
                        if (members.contains(p.getUniqueId())) continue; // already a member
                        String name = p.getName();
                        if (name.toLowerCase().startsWith(partial)) names.add(name);
                    }
                    names.sort(String.CASE_INSENSITIVE_ORDER);
                    return names;
                }

                case "kick": {
                    // Suggest members of the sender's island
                    if (!(sender instanceof Player)) return Collections.emptyList();
                    Player player = (Player) sender;
                    IslandManager.Island island = islandManager.getIslandByOwner(player.getUniqueId());
                    if (island == null) return Collections.emptyList();
                    List<String> names = new ArrayList<>();
                    for (java.util.UUID member : island.getMembers()) {
                        // skip owner (cannot kick owner)
                        if (member.equals(player.getUniqueId())) continue;
                        String name = Bukkit.getOfflinePlayer(member).getName();
                        if (name != null && name.toLowerCase().startsWith(partial)) names.add(name);
                    }
                    names.sort(String.CASE_INSENSITIVE_ORDER);
                    return names;
                }

                case "transfer": {
                    // Suggest online players (exclude self and players who already have an island)
                    if (!(sender instanceof Player)) return Collections.emptyList();
                    Player player = (Player) sender;
                    List<String> names = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p.getUniqueId().equals(player.getUniqueId())) continue;
                        if (islandManager.hasIsland(p.getUniqueId())) continue; // target already has island
                        String name = p.getName();
                        if (name.toLowerCase().startsWith(partial)) names.add(name);
                    }
                    names.sort(String.CASE_INSENSITIVE_ORDER);
                    return names;
                }

                case "delete": {
                    // suggest confirmation literal 'confirm'
                    List<String> opts = new ArrayList<>();
                    if ("confirm".startsWith(partial)) opts.add("confirm");
                    return opts;
                }

                default: {
                    return Collections.emptyList();
                }
            }
        }
        return Collections.emptyList();
    }
}
