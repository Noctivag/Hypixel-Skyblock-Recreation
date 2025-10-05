package de.noctivag.skyblock;
import net.kyori.adventure.text.Component;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BasicCommands implements CommandExecutor {
    private final SkyblockPlugin SkyblockPlugin;

    public BasicCommands(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern verwendet werden!");
            return true;
        }

        Player player = (Player) sender;
        String cmdName = command.getName().toLowerCase();

        switch (cmdName) {
            case "heal":
                if (!player.hasPermission("basicsplugin.heal")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                player.setHealth(20.0);
                player.setFoodLevel(20);
                player.setSaturation(20);
                player.sendMessage(Component.text("§aDu wurdest vollständig geheilt!"));
                break;

            case "feed":
                if (!player.hasPermission("basicsplugin.feed")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                player.setFoodLevel(20);
                player.setSaturation(20);
                player.sendMessage(Component.text("§aDu wurdest gesättigt!"));
                break;

            case "clearinventory":
            case "ci":
            case "clear":
                if (!player.hasPermission("basicsplugin.clearinventory")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                player.getInventory().clear();
                player.sendMessage(Component.text("§aDein Inventar wurde geleert!"));
                break;

            case "craftingtable":
            case "craft":
            case "workbench":
                if (!player.hasPermission("basicsplugin.workbench")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                player.openWorkbench(null, true);
                player.sendMessage(Component.text("§aWerkbank geöffnet!"));
                break;

            case "anvil":
                if (!player.hasPermission("basicsplugin.anvil")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                Inventory anvil = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.ANVIL);
                player.openInventory(anvil);
                player.sendMessage(Component.text("§aAmboss geöffnet!"));
                break;

            case "enderchest":
            case "ec":
                if (!player.hasPermission("basicsplugin.enderchest")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                player.openInventory(player.getEnderChest());
                player.sendMessage(Component.text("§aEndertruhe geöffnet!"));
                break;

            case "grindstone":
                if (!player.hasPermission("basicsplugin.grindstone")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                Inventory grindstone = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.GRINDSTONE);
                player.openInventory(grindstone);
                player.sendMessage(Component.text("§aSchleifstein geöffnet!"));
                break;

            case "smithingtable":
                if (!player.hasPermission("basicsplugin.smithingtable")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                Inventory smithing = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.SMITHING);
                player.openInventory(smithing);
                player.sendMessage(Component.text("§aSchmiedetisch geöffnet!"));
                break;

            case "stonecutter":
                if (!player.hasPermission("basicsplugin.stonecutter")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                Inventory stonecutter = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.STONECUTTER);
                player.openInventory(stonecutter);
                player.sendMessage(Component.text("§aSteinsäger geöffnet!"));
                break;

            case "loom":
                if (!player.hasPermission("basicsplugin.loom")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                Inventory loom = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.LOOM);
                player.openInventory(loom);
                player.sendMessage(Component.text("§aWebstuhl geöffnet!"));
                break;

            case "cartography":
            case "cartographytable":
                if (!player.hasPermission("basicsplugin.cartography")) {
                    player.sendMessage(Component.text("§cDu hast keine Berechtigung für diesen Befehl!"));
                    return true;
                }
                Inventory cartography = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.CARTOGRAPHY);
                player.openInventory(cartography);
                player.sendMessage(Component.text("§aKartentisch geöffnet!"));
                break;

            default:
                player.sendMessage("§cUnbekannter Befehl: " + cmdName);
                break;
        }

        return true;
    }
}
