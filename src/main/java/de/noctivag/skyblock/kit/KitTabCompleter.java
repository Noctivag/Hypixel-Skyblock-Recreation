package de.noctivag.skyblock.kit;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import de.noctivag.skyblock.SkyblockPlugin;

import java.util.ArrayList;
import java.util.List;

public class KitTabCompleter implements TabCompleter {
    private final KitManager kitManager;

    public KitTabCompleter(SkyblockPlugin SkyblockPlugin) {
        this.kitManager = (KitManager) SkyblockPlugin.getKitManager();
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
