package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RanksGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private Player target;

    public RanksGUI(SkyblockPlugin SkyblockPlugin, Player target) {
        super(54, Component.text("§6§lRang-Verwaltung"));
        this.SkyblockPlugin = SkyblockPlugin;
        this.target = target;
    }

    public RanksGUI(SkyblockPlugin SkyblockPlugin) {
        super(54, Component.text("§6§lRang-Verwaltung"));
        this.SkyblockPlugin = SkyblockPlugin;
        this.target = null;
    }

    public void open(Player admin) {
        clearInventory();

        // Header: target info or player selector
        if (target != null) {
            setItem(4, createGuiItem(Material.PLAYER_HEAD, "§eZiel: " + target.getName(),
                "§7Aktueller Rang: §e" + SkyblockPlugin.getRankManager().getDisplayName(SkyblockPlugin.getRankManager().getPlayerRank(target))));
        } else {
            setItem(4, createGuiItem(Material.PLAYER_HEAD, "§eSpieler auswählen",
                "§7Klicke, um einen Spieler auszuwählen"));
        }

        int slot = 18;
        for (String key : SkyblockPlugin.getRankManager().getAllRankKeys()) {
            String display = SkyblockPlugin.getRankManager().getDisplayName(key);
            var lore = new ArrayList<String>();
            lore.add("§7Klicke, um diesen Rang zu setzen");
            setItem(slot, createGuiItem(Material.NAME_TAG, "§e" + display, lore.toArray(new String[0])));
            slot += (slot % 9 == 7) ? 3 : 1;
        }

        setItem(48, createGuiItem(Material.ARROW, "§7Zurück", "§7Zum Admin-Menü"));
        setItem(49, createGuiItem(Material.PLAYER_HEAD, "§eSpieler wechseln", "§7Anderen Spieler auswählen"));
        admin.openInventory(getInventory());
    }

    public Player getTarget() { return target; }
    public void setTarget(Player target) { this.target = target; }
}


