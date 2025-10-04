package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class EventScheduleGUI extends CustomGUI {
    private final SkyblockPlugin plugin;
    private final Player player;

    public EventScheduleGUI(SkyblockPlugin plugin, Player player) {
        super(54, Component.text("§6§l⏰ Event Schedule").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.plugin = plugin;
        this.player = player;
        setupItems();
    }

    private void setupItems() {
        // Today's Events
        setItem(10, Material.SUNFLOWER, "§e§lToday's Events", 
            "§7View events happening today",
            "§7• Daily events",
            "§7• Special events",
            "§7• Time schedules",
            "",
            "§eClick to view");

        // Weekly Events
        setItem(12, Material.CLOCK, "§b§lWeekly Events", 
            "§7View this week's events",
            "§7• Weekly specials",
            "§7• Recurring events",
            "§7• Event calendar",
            "",
            "§eClick to view");

        // Monthly Events
        setItem(14, Material.PAPER, "§d§lMonthly Events", 
            "§7View monthly celebrations",
            "§7• Seasonal events",
            "§7• Anniversary events",
            "§7• Special occasions",
            "",
            "§eClick to view");

        // Event History
        setItem(16, Material.BOOK, "§f§lEvent History", 
            "§7View past events",
            "§7• Completed events",
            "§7• Participation records",
            "§7• Achievement history",
            "",
            "§eClick to view");

        // Close button
        setItem(49, Material.BARRIER, "§c§lClose", 
            "§7Close the event schedule",
            "",
            "§eClick to close");
    }

    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).toList());
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
