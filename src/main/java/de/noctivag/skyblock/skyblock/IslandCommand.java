package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class IslandCommand implements CommandExecutor {
    private final IslandCommandImpl impl = new IslandCommandImpl();

    public IslandCommand() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return impl.onCommand(sender, command, label, args);
    }
}

