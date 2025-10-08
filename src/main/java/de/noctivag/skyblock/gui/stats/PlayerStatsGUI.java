package de.noctivag.skyblock.gui.stats;

import de.noctivag.skyblock.stats.StatManager;
import de.noctivag.skyblock.stats.StatType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class PlayerStatsGUI {
    private final Player player;
    private Inventory gui;

    public PlayerStatsGUI(Player player) {
        this.player = player;
        open();
    }

    public void open() {
        gui = Bukkit.createInventory(player, 27, Component.text("§bDeine Skyblock-Stats"));
        var stats = StatManager.getFinalStats(player);
        int slot = 10;
        for (StatType type : StatType.values()) {
            double value = stats.get(type);
            if (value != 0) {
                ItemStack statItem = new ItemStack(Material.PAPER);
                ItemMeta meta = statItem.getItemMeta();
                meta.displayName(Component.text("§a" + type.name().replace('_', ' ')));
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("§7Wert: §b" + value));
                meta.lore(lore);
                statItem.setItemMeta(meta);
                gui.setItem(slot++, statItem);
            }
        }
        player.openInventory(gui);
    }
}
