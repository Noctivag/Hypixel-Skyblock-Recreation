package de.noctivag.plugin.kit;
import org.bukkit.inventory.ItemStack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import de.noctivag.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class KitTabCompleter implements TabCompleter {
    private final KitManager kitManager;

    public KitTabCompleter(Plugin plugin) {
        this.kitManager = (KitManager) plugin.getKitManager();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            for (String kitName : kitManager.getKitNames()) {
                if (kitName.toLowerCase().startsWith(partial)) {
                    completions.add(kitName);
                }
            }
        }

        return completions;
    }
}
