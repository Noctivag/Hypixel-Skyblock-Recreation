package de.noctivag.skyblock;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BasicCommands implements CommandExecutor {
    private final SkyblockPlugin plugin;

    public BasicCommands(SkyblockPlugin plugin) {
        this.plugin = plugin;
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
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                player.setHealth(20.0);
                player.setFoodLevel(20);
                player.setSaturation(20);
                player.sendMessage("§aDu wurdest vollständig geheilt!");
                break;

            case "feed":
                if (!player.hasPermission("basicsplugin.feed")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                player.setFoodLevel(20);
                player.setSaturation(20);
                player.sendMessage("§aDu wurdest gesättigt!");
                break;

            case "clearinventory":
            case "ci":
            case "clear":
                if (!player.hasPermission("basicsplugin.clearinventory")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                player.getInventory().clear();
                player.sendMessage("§aDein Inventar wurde geleert!");
                break;

            case "craftingtable":
            case "craft":
            case "workbench":
                if (!player.hasPermission("basicsplugin.workbench")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                player.openWorkbench(null, true);
                player.sendMessage("§aWerkbank geöffnet!");
                break;

            case "anvil":
                if (!player.hasPermission("basicsplugin.anvil")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                Inventory anvil = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.ANVIL);
                player.openInventory(anvil);
                player.sendMessage("§aAmboss geöffnet!");
                break;

            case "enderchest":
            case "ec":
                if (!player.hasPermission("basicsplugin.enderchest")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                player.openInventory(player.getEnderChest());
                player.sendMessage("§aEndertruhe geöffnet!");
                break;

            case "grindstone":
                if (!player.hasPermission("basicsplugin.grindstone")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                Inventory grindstone = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.GRINDSTONE);
                player.openInventory(grindstone);
                player.sendMessage("§aSchleifstein geöffnet!");
                break;

            case "smithingtable":
                if (!player.hasPermission("basicsplugin.smithingtable")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                Inventory smithing = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.SMITHING);
                player.openInventory(smithing);
                player.sendMessage("§aSchmiedetisch geöffnet!");
                break;

            case "stonecutter":
                if (!player.hasPermission("basicsplugin.stonecutter")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                Inventory stonecutter = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.STONECUTTER);
                player.openInventory(stonecutter);
                player.sendMessage("§aSteinsäger geöffnet!");
                break;

            case "loom":
                if (!player.hasPermission("basicsplugin.loom")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                Inventory loom = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.LOOM);
                player.openInventory(loom);
                player.sendMessage("§aWebstuhl geöffnet!");
                break;

            case "cartography":
            case "cartographytable":
                if (!player.hasPermission("basicsplugin.cartography")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    return true;
                }
                Inventory cartography = Bukkit.createInventory(null, org.bukkit.event.inventory.InventoryType.CARTOGRAPHY);
                player.openInventory(cartography);
                player.sendMessage("§aKartentisch geöffnet!");
                break;

            default:
                player.sendMessage("§cUnbekannter Befehl: " + cmdName);
                break;
        }

        return true;
    }
}
