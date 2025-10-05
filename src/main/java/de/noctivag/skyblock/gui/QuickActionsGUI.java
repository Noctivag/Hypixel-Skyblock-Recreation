package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public class QuickActionsGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;

    public QuickActionsGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§d§l⚡ Quick Actions").color(NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        setupItems();
    }

    private void setupItems() {
        // Teleport to Spawn
        setItem(10, Material.ENDER_PEARL, "§b§lTeleport to Spawn", 
            "§7Quick teleport to spawn",
            "§7• Instant teleportation",
            "§7• No cooldown",
            "§7• Safe location",
            "",
            "§eClick to teleport");

        // Sort Inventory
        setItem(12, Material.CHEST, "§e§lSort Inventory", 
            "§7Automatically sort your inventory",
            "§7• Organize items by type",
            "§7• Stack similar items",
            "§7• Clean arrangement",
            "",
            "§eClick to sort");

        // Repair Items
        setItem(14, Material.ANVIL, "§f§lRepair Items", 
            "§7Repair your tools and armor",
            "§7• Fix durability",
            "§7• Cost: Experience",
            "§7• Preserve enchantments",
            "",
            "§eClick to repair");

        // Clear Chat
        setItem(16, Material.PAPER, "§7§lClear Chat", 
            "§7Clear your chat history",
            "§7• Remove all messages",
            "§7• Clean interface",
            "§7• Instant effect",
            "",
            "§eClick to clear");

        // Toggle Flight
        setItem(22, Material.FEATHER, "§a§lToggle Flight", 
            "§7Toggle flight mode",
            "§7• Enable/disable flying",
            "§7• Creative flight",
            "§7• Smooth movement",
            "",
            "§eClick to toggle");

        // Night Vision
        setItem(24, Material.GOLDEN_CARROT, "§6§lNight Vision", 
            "§7Toggle night vision effect",
            "§7• See in the dark",
            "§7• Temporary effect",
            "§7• 5 minute duration",
            "",
            "§eClick to activate");

        // Speed Boost
        setItem(26, Material.SUGAR, "§f§lSpeed Boost", 
            "§7Temporary speed increase",
            "§7• Faster movement",
            "§7• 3 minute duration",
            "§7• Stackable effect",
            "",
            "§eClick to activate");

        // Close button
        setItem(49, Material.BARRIER, "§c§lClose", 
            "§7Close quick actions",
            "",
            "§eClick to close");
    }

    public void setItem(int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(java.util.Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        
        getInventory().setItem(slot, item);
    }
}
