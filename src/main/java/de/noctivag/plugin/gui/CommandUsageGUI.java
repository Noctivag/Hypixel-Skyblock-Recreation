package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandUsageGUI extends CustomGUI {
    private final Plugin plugin;

    public CommandUsageGUI(Plugin plugin) {
        super(54, Component.text("§c§lCommand-Verwaltung"));
        this.plugin = plugin;
    }

    public void open(Player admin) {
        clearInventory();

        // Header
        setItem(4, createGuiItem(Material.COMMAND_BLOCK, "§c§lCommand-Einstellungen",
            "§7Verwalte Cooldowns, Kosten und Status"));

        // Command entries
        String[] commands = {"tpa", "rtp", "back", "spawn", "home", "warp"};
        int slot = 18;
        for (String cmd : commands) {
            boolean enabled = plugin.getCommandManager().isCommandEnabled(cmd);
            int cooldown = (int) plugin.getCommandManager().getCooldown(cmd);
            int cost = (int) plugin.getCommandManager().getCost(cmd);
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Status: " + (enabled ? "§aAktiviert" : "§cDeaktiviert"));
            lore.add("§7Cooldown: §e" + cooldown + "s");
            lore.add("§7Kosten: §6" + cost + " Coins");
            lore.add("");
            lore.add("§eLinksklick: §7Toggle Status");
            lore.add("§eRechtsklick: §7Einstellungen");

            setItem(slot, createGuiItem(
                enabled ? Material.LIME_DYE : Material.GRAY_DYE,
                "§e/" + cmd,
                lore.toArray(new String[0])
            ));
            slot += (slot % 9 == 7) ? 3 : 1;
        }

        setItem(49, createGuiItem(Material.ARROW, "§7Zurück", "§7Zum Admin-Menü"));
        admin.openInventory(getInventory());
    }
}
