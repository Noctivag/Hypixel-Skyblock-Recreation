package de.noctivag.skyblock.listeners;

import de.noctivag.skyblock.gui.admin.AdminMainMenuGUI;
import de.noctivag.skyblock.gui.admin.AdminWorldManagerGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AdminGUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
    String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title());
        if (title.contains("§cAdmin Menu")) {
            // AdminMenu (neues System)
            new de.noctivag.skyblock.gui.AdminMenu().handleClick(event);
        } else if (title.contains("§cAdmin Menü")) {
            // Altes AdminMainMenuGUI
            new AdminMainMenuGUI(player).handleClick(event);
        } else if (title.contains("§cAdmin Weltverwaltung")) {
            new AdminWorldManagerGUI(de.noctivag.skyblock.SkyblockPlugin.getInstance(), player).handleClick(event);
        } else if (title.contains("§bWelt: ")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            String worldName = null;
            if (player.hasMetadata("admin_world_action")) {
                worldName = player.getMetadata("admin_world_action").get(0).asString();
            }
            if (worldName == null) {
                player.sendMessage("§cFehler: Weltname nicht gefunden.");
                player.closeInventory();
                return;
            }
            de.noctivag.skyblock.worlds.WorldManager wm = de.noctivag.skyblock.SkyblockPlugin.getInstance().getWorldManager();
            switch (slot) {
                case 2 -> {
                    // Spieler teleportieren
                    player.closeInventory();
                    player.sendMessage("§7Gib im Chat ein: /tpworld <Spieler> <Welt>");
                    player.removeMetadata("admin_world_action", de.noctivag.skyblock.SkyblockPlugin.getInstance());
                }
                case 4 -> {
                    // Spieler kicken
                    player.closeInventory();
                    player.sendMessage("§7Gib im Chat ein: /kickfromworld <Spieler> <Welt>");
                    player.removeMetadata("admin_world_action", de.noctivag.skyblock.SkyblockPlugin.getInstance());
                }
                case 6 -> {
                    // Whitelist verwalten (Dummy)
                    player.sendMessage("§eDie Whitelist-Verwaltung wird demnächst verfügbar sein.");
                    player.closeInventory();
                    player.removeMetadata("admin_world_action", de.noctivag.skyblock.SkyblockPlugin.getInstance());
                }
                case 10 -> {
                    // Teleport
                    org.bukkit.Location loc = wm.getSafeSpawnLocation(worldName);
                    if (loc != null) {
                        player.teleport(loc);
                        player.sendMessage("§aTeleportiert zu Welt: " + worldName);
                    } else {
                        player.sendMessage("§cTeleport fehlgeschlagen: Welt nicht gefunden.");
                    }
                    player.removeMetadata("admin_world_action", de.noctivag.skyblock.SkyblockPlugin.getInstance());
                    player.closeInventory();
                }
                case 12 -> {
                    wm.unloadWorld(worldName);
                    player.sendMessage("§cWelt entladen: " + worldName);
                    player.removeMetadata("admin_world_action", de.noctivag.skyblock.SkyblockPlugin.getInstance());
                    player.closeInventory();
                }
                case 14 -> {
                    wm.unloadWorld(worldName);
                    wm.removeWorld(worldName);
                    player.sendMessage("§4Welt gelöscht: " + worldName);
                    player.removeMetadata("admin_world_action", de.noctivag.skyblock.SkyblockPlugin.getInstance());
                    player.closeInventory();
                }
                case 26 -> {
                    // Zurück
                    player.removeMetadata("admin_world_action", de.noctivag.skyblock.SkyblockPlugin.getInstance());
                    new AdminWorldManagerGUI(de.noctivag.skyblock.SkyblockPlugin.getInstance(), player);
                }
            }
        }
    }
}
