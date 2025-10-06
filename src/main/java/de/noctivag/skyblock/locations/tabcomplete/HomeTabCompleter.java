package de.noctivag.skyblock.locations.tabcomplete;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeTabCompleter implements TabCompleter {
    private final SkyblockPlugin SkyblockPlugin;

    public HomeTabCompleter(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
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
            Object locationManager = SkyblockPlugin.getLocationManager();
            try {
                java.util.Set<String> homeNames = (java.util.Set<String>) locationManager.getClass().getMethod("getHomeNames", Player.class).invoke(locationManager, player);
                for (String homeName : homeNames) {
                    if (homeName.toLowerCase().startsWith(partial)) {
                        completions.add(homeName);
                    }
                }
            } catch (Exception e) {
                // If reflection fails, no completions will be added
            }
        }

        return completions;
    }
}
