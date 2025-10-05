package de.noctivag.skyblock.gui;

import org.bukkit.event.inventory.ClickType;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;

/**
 * Messages GUI - Customize join/leave messages
 */
public class MessagesGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;

    public MessagesGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§6§l💬 Messages").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        setupItems();
        playOpenSound();
    }

    private void setupItems() {
        // Header
        setItem(4, createGuiItem(Material.WRITABLE_BOOK, "§6§l💬 Messages",
            "§7Customize your join/leave messages",
            "§eChoose your message type"));

        // Join Messages
        setItem(10, createGuiItem(Material.EMERALD, "§a§l✅ Join Messages",
            "§7Customize your join messages",
            "§7• Set custom join messages",
            "§7• Use placeholders",
            "§7• Preview messages",
            "§eClick to open"));

        // Leave Messages
        setItem(11, createGuiItem(Material.REDSTONE, "§c§l❌ Leave Messages",
            "§7Customize your leave messages",
            "§7• Set custom leave messages",
            "§7• Use placeholders",
            "§7• Preview messages",
            "§eClick to open"));

        // Message Presets
        setItem(12, createGuiItem(Material.BOOK, "§b§l📚 Message Presets",
            "§7Use predefined message templates",
            "§7• Various themes available",
            "§7• Easy to customize",
            "§7• Quick setup",
            "§eClick to open"));

        // Message Settings
        setItem(13, createGuiItem(Material.REDSTONE_TORCH, "§e§l⚙️ Message Settings",
            "§7Configure message behavior",
            "§7• Enable/disable messages",
            "§7• Sound settings",
            "§7• Display options",
            "§eClick to open"));

        // Message History
        setItem(14, createGuiItem(Material.CLOCK, "§6§l🕐 Message History",
            "§7View your message history",
            "§7• Previous messages",
            "§7• Usage statistics",
            "§7• Restore old messages",
            "§eClick to open"));

        // Back
        setItem(49, createGuiItem(Material.BARRIER, "§c§lBack",
            "§7Return to main menu"));
    }

    public void onClick(Player player, int slot, ItemStack item, ClickType clickType) {
        switch (slot) {
            case 10 -> openJoinMessageGUI(player);
            case 11 -> openLeaveMessageGUI(player);
            case 12 -> openMessagePresetsGUI(player);
            case 13 -> openMessageSettingsGUI(player);
            case 14 -> openMessageHistoryGUI(player);
            case 49 -> {
                player.closeInventory();
                new UnifiedMainMenuSystem(SkyblockPlugin, player, UnifiedMainMenuSystem.MenuMode.ENHANCED).open(player);
            }
        }
    }

    private void openJoinMessageGUI(Player player) {
        new JoinMessageGUI(SkyblockPlugin, player).open(player);
    }

    private void openLeaveMessageGUI(Player player) {
        player.sendMessage(Component.text("§aLeave Message GUI wird geöffnet..."));
        // TODO: Implement leave message GUI
    }

    private void openMessagePresetsGUI(Player player) {
        new JoinMessagePresetsGUI(SkyblockPlugin).open(player);
    }

    private void openMessageSettingsGUI(Player player) {
        player.sendMessage(Component.text("§aMessage Settings GUI wird geöffnet..."));
        // TODO: Implement message settings GUI
    }

    private void openMessageHistoryGUI(Player player) {
        player.sendMessage(Component.text("§aMessage History GUI wird geöffnet..."));
        // TODO: Implement message history GUI
    }

    private void playOpenSound() {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
    }

    public ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        
        return item;
    }
}
