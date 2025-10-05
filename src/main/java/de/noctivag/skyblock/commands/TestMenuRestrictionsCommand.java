package de.noctivag.skyblock.commands;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

/**
 * Test command to verify menu restrictions work properly
 */
public class TestMenuRestrictionsCommand implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;

    public TestMenuRestrictionsCommand(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern ausgeführt werden!");
            return true;
        }

        if (!player.hasPermission("basics.admin")) {
            player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Component.text("§e/testmenurestrictions <menu|island>"));
            return true;
        }

        String type = args[0].toLowerCase();

        switch (type) {
            case "menu":
                giveMenuItem(player);
                break;
            case "island":
                giveIslandItem(player);
                break;
            default:
                player.sendMessage(Component.text("§cUngültiger Typ! Verwende: menu oder island"));
                break;
        }

        return true;
    }

    private void giveMenuItem(Player player) {
        ItemStack menuItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = menuItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§6✧ Hauptmenü ✧"));
            meta.lore(java.util.Arrays.asList(
                Component.text("§7Klicke um das Hauptmenü zu öffnen"),
                Component.text("§c§lTEST ITEM - Nicht droppbar!")
            ));
            menuItem.setItemMeta(meta);
        }

        // Give item in last slot (slot 8)
        player.getInventory().setItem(8, menuItem);
        player.sendMessage(Component.text("§aMenü-Item wurde in den letzten Slot (Slot 8) gelegt!"));
        player.sendMessage(Component.text("§eVersuche es zu droppen - es sollte blockiert werden!"));
    }

    private void giveIslandItem(Player player) {
        ItemStack islandItem = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta meta = islandItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§a§lIsland Menu"));
            meta.lore(java.util.Arrays.asList(
                Component.text("§7Klicke um das Insel-Menü zu öffnen"),
                Component.text("§c§lTEST ITEM - Nicht droppbar!")
            ));
            islandItem.setItemMeta(meta);
        }

        // Give item in last slot (slot 8)
        player.getInventory().setItem(8, islandItem);
        player.sendMessage(Component.text("§aInsel-Menü-Item wurde in den letzten Slot (Slot 8) gelegt!"));
        player.sendMessage(Component.text("§eVersuche es zu droppen - es sollte blockiert werden!"));
    }
}
