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
 * Teleportation GUI - Advanced teleportation and navigation
 */
public class TeleportationGUI extends CustomGUI {
    private final SkyblockPlugin SkyblockPlugin;
    private final Player player;

    public TeleportationGUI(SkyblockPlugin SkyblockPlugin, Player player) {
        super(54, Component.text("§6§l🧭 Teleportation").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        this.SkyblockPlugin = SkyblockPlugin;
        this.player = player;
        setupItems();
        playOpenSound();
    }

    private void setupItems() {
        // Header
        setItem(4, createGuiItem(Material.COMPASS, "§6§l🧭 Teleportation",
            "§7Advanced teleportation and navigation",
            "§eChoose your destination"));

        // Home System
        setItem(10, createGuiItem(Material.RED_BED, "§a§l🏠 Home System",
            "§7Teleport to your homes",
            "§7• Multiple homes support",
            "§7• Set and manage homes",
            "§eClick to open"));

        // TPA System
        setItem(11, createGuiItem(Material.ENDER_PEARL, "§b§l📞 TPA System",
            "§7Teleport to other players",
            "§7• Send teleport requests",
            "§7• Accept/deny requests",
            "§eClick to open"));

        // Random Teleport
        setItem(12, createGuiItem(Material.MAGENTA_GLAZED_TERRACOTTA, "§d§l🎲 Random Teleport",
            "§7Teleport to random locations",
            "§7• Safe teleportation",
            "§7• Cooldown system",
            "§eClick to teleport"));

        // Spawn
        setItem(13, createGuiItem(Material.BEACON, "§e§l⭐ Spawn",
            "§7Teleport to server spawn",
            "§7• Main spawn location",
            "§7• Always available",
            "§eClick to teleport"));

        // Warps
        setItem(14, createGuiItem(Material.ENDER_EYE, "§5§l🌍 Warps",
            "§7Access warp system",
            "§7• Public and private warps",
            "§7• Categorized warps",
            "§eClick to open"));

        // Back
        setItem(49, createGuiItem(Material.BARRIER, "§c§lBack",
            "§7Return to main menu"));
    }

    public void onClick(Player player, int slot, ItemStack item, ClickType clickType) {
        switch (slot) {
            case 10 -> openHomeGUI(player);
            case 11 -> openTPAGUI(player);
            case 12 -> performRandomTeleport(player);
            case 13 -> teleportToSpawn(player);
            case 14 -> openWarpsGUI(player);
            case 49 -> {
                player.closeInventory();
                new UnifiedMainMenuSystem(SkyblockPlugin, player, UnifiedMainMenuSystem.MenuMode.ENHANCED).open(player);
            }
        }
    }

    private void openHomeGUI(Player player) {
        player.sendMessage(Component.text("§aHome-System wird geöffnet..."));
        // TODO: Implement home GUI
    }

    private void openTPAGUI(Player player) {
        player.sendMessage(Component.text("§aTPA-System wird geöffnet..."));
        // TODO: Implement TPA GUI
    }

    private void performRandomTeleport(Player player) {
        player.sendMessage(Component.text("§aRandom Teleport wird ausgeführt..."));
        // TODO: Implement random teleport
    }

    private void teleportToSpawn(Player player) {
        if (player.getWorld().getSpawnLocation() != null) {
            player.teleport(player.getWorld().getSpawnLocation());
            player.sendMessage(Component.text("§aDu wurdest zum Spawn teleportiert!"));
        } else {
            player.sendMessage(Component.text("§cSpawn-Location nicht gefunden!"));
        }
    }

    private void openWarpsGUI(Player player) {
        new WarpGUI(SkyblockPlugin, player).open(player);
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
