package de.noctivag.skyblock.locations.tabcomplete;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeTabCompleter implements TabCompleter {
    private final SkyblockPlugin plugin;

    public HomeTabCompleter(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return completions;
        }

        Player player = (Player) sender;

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            for (String homeName : plugin.getLocationManager().getHomeNames(player)) {
                if (homeName.toLowerCase().startsWith(partial)) {
                    completions.add(homeName);
                }
            }
        }

        return completions;
    }
}
