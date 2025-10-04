package de.noctivag.skyblock.locations.tabcomplete;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpTabCompleter implements TabCompleter {
    private final SkyblockPlugin plugin;

    public WarpTabCompleter(SkyblockPlugin plugin) {
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
            for (String warpName : plugin.getLocationManager().getWarpNames()) {
                if (warpName.toLowerCase().startsWith(partial)) {
                    completions.add(warpName);
                }
            }
        }

        return completions;
    }
}
