package de.noctivag.skyblock.gui.admin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
// import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.worlds.WorldManager;

public class AdminWorldManagerGUI {
    private static final String TITLE = "§cAdmin Weltverwaltung";
    private static final int PAGE_SIZE = 45;
    private int page = 0;
    private final SkyblockPlugin plugin;
    private final Player player;
    private final WorldManager worldManager;
    private Inventory gui;

    public AdminWorldManagerGUI(SkyblockPlugin plugin, Player player) {
        this(plugin, player, 0);
    }

    public AdminWorldManagerGUI(SkyblockPlugin plugin, Player player, int page) {
        this.plugin = plugin;
        this.player = player;
        this.worldManager = plugin.getWorldManager();
        this.page = page;
        open();
    }


    public void open() {
        Map<String, World> worlds = worldManager.getManagedWorlds();
        List<Map.Entry<String, World>> worldList = new ArrayList<>(worlds.entrySet());
        gui = Bukkit.createInventory(player, 54, Component.text(TITLE + " §7Seite " + (page + 1)));
        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, worldList.size());
        int slot = 0;
        for (int i = start; i < end; i++) {
            Map.Entry<String, World> entry = worldList.get(i);
            World world = entry.getValue();
            ItemStack item = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text("§a" + world.getName()));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("§7Umgebung: §e" + world.getEnvironment()));
            lore.add(Component.text("§7Spieler: §e" + world.getPlayers().size()));
            lore.add(Component.text("§7Spawn: §e" + world.getSpawnLocation().getBlockX() + ", " + world.getSpawnLocation().getBlockY() + ", " + world.getSpawnLocation().getBlockZ()));
            meta.lore(lore);
            item.setItemMeta(meta);
            gui.setItem(slot++, item);
        }
        // Paginierung: Vor/Zurück
        if (page > 0) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prev.getItemMeta();
            prevMeta.displayName(Component.text("§aVorherige Seite"));
            prev.setItemMeta(prevMeta);
            gui.setItem(45, prev);
        }
        if (end < worldList.size()) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = next.getItemMeta();
            nextMeta.displayName(Component.text("§aNächste Seite"));
            next.setItemMeta(nextMeta);
            gui.setItem(53, next);
        } else {
            // Option: Neue Welt erstellen
            ItemStack createWorld = new ItemStack(Material.ANVIL);
            ItemMeta createMeta = createWorld.getItemMeta();
            createMeta.displayName(Component.text("§bNeue Testwelt erstellen"));
            createMeta.lore(List.of(Component.text("§7Klicke, um eine neue Testwelt zu erstellen.")));
            createWorld.setItemMeta(createMeta);
            gui.setItem(53, createWorld);
        }
        player.openInventory(gui);
    }

    public void handleClick(InventoryClickEvent event) {
        String invTitle = event.getView().title().toString();
        if (!invTitle.contains(TITLE)) return;
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Map<String, World> worlds = worldManager.getManagedWorlds();
        List<String> worldNames = new ArrayList<>(worlds.keySet());
        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, worldNames.size());
        if (slot == 45 && page > 0) {
            new AdminWorldManagerGUI(plugin, player, page - 1);
            return;
        }
        if (slot == 53 && end < worldNames.size()) {
            new AdminWorldManagerGUI(plugin, player, page + 1);
            return;
        }
        if (slot == 53 && end == worldNames.size()) {
            // Neue Testwelt erstellen
            String testWorldName = "test_" + System.currentTimeMillis();
            worldManager.loadWorld(testWorldName);
            player.sendMessage("§aTestwelt wird erstellt: " + testWorldName);
            player.closeInventory();
            return;
        }
        int worldSlot = slot;
        if (worldSlot < (end - start)) {
            String worldName = worldNames.get(start + worldSlot);
            openWorldActionMenu(worldName);
        }
    }

    private void openWorldActionMenu(String worldName) {
    // Spieler-Management: Teleport, Kick, Whitelist
    ItemStack tpPlayer = new ItemStack(Material.PLAYER_HEAD);
    ItemMeta tpPlayerMeta = tpPlayer.getItemMeta();
    tpPlayerMeta.displayName(Component.text("§aSpieler teleportieren"));
    tpPlayerMeta.lore(List.of(Component.text("§7Teleportiert einen Spieler in diese Welt.")));
    tpPlayer.setItemMeta(tpPlayerMeta);
    actionMenu.setItem(2, tpPlayer);

    ItemStack kickPlayer = new ItemStack(Material.BARRIER);
    ItemMeta kickPlayerMeta = kickPlayer.getItemMeta();
    kickPlayerMeta.displayName(Component.text("§cSpieler kicken"));
    kickPlayerMeta.lore(List.of(Component.text("§7Entfernt einen Spieler aus dieser Welt.")));
    kickPlayer.setItemMeta(kickPlayerMeta);
    actionMenu.setItem(4, kickPlayer);

    ItemStack whitelist = new ItemStack(Material.PAPER);
    ItemMeta whitelistMeta = whitelist.getItemMeta();
    whitelistMeta.displayName(Component.text("§eWhitelist verwalten"));
    whitelistMeta.lore(List.of(Component.text("§7Verwalte die Spieler-Whitelist für diese Welt.", "§8(Demnächst verfügbar)")));
    whitelist.setItemMeta(whitelistMeta);
    actionMenu.setItem(6, whitelist);
        World world = worldManager.getWorld(worldName);
        Inventory actionMenu = Bukkit.createInventory(player, 27, Component.text("§bWelt: " + worldName));
        // Teleport
        ItemStack tp = new ItemStack(Material.ENDER_PEARL);
        ItemMeta tpMeta = tp.getItemMeta();
        tpMeta.displayName(Component.text("§aTeleportieren"));
        tpMeta.lore(List.of(Component.text("§7Teleportiere dich zu dieser Welt.")));
        tp.setItemMeta(tpMeta);
        actionMenu.setItem(10, tp);
        // Entladen
        ItemStack unload = new ItemStack(Material.BARRIER);
        ItemMeta unloadMeta = unload.getItemMeta();
        unloadMeta.displayName(Component.text("§cEntladen"));
        unloadMeta.lore(List.of(Component.text("§7Entlädt diese Welt.")));
        unload.setItemMeta(unloadMeta);
        actionMenu.setItem(12, unload);
        // Löschen
        ItemStack delete = new ItemStack(Material.TNT);
        ItemMeta delMeta = delete.getItemMeta();
        delMeta.displayName(Component.text("§4Löschen"));
        delMeta.lore(List.of(Component.text("§cLöscht diese Welt dauerhaft!")));
        delete.setItemMeta(delMeta);
        actionMenu.setItem(14, delete);
        // Statistiken
        if (world != null) {
            ItemStack stats = new ItemStack(Material.PAPER);
            ItemMeta statsMeta = stats.getItemMeta();
            List<Component> statsLore = new ArrayList<>();
            statsMeta.displayName(Component.text("§eStatistiken"));
            statsLore.add(Component.text("§7Entities: §b" + world.getEntities().size()));
            statsLore.add(Component.text("§7Spieler: §b" + world.getPlayers().size()));
            statsLore.add(Component.text("§7Seed: §b" + world.getSeed()));
            statsLore.add(Component.text("§7Zeit: §b" + world.getTime()));
            statsLore.add(Component.text("§7Wetter: §b" + (world.hasStorm() ? "Sturm" : "Klar")));
            statsMeta.lore(statsLore);
            stats.setItemMeta(statsMeta);
            actionMenu.setItem(16, stats);
            // Flags
            ItemStack flags = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
            ItemMeta flagsMeta = flags.getItemMeta();
            List<Component> flagsLore = new ArrayList<>();
            flagsMeta.displayName(Component.text("§bWelt-Flags"));
            flagsLore.add(Component.text("§7PvP: §b" + (world.getPVP() ? "An" : "Aus")));
            flagsLore.add(Component.text("§7Mob-Spawning: §b" + (world.getAllowAnimals() || world.getAllowMonsters() ? "An" : "Aus")));
            flagsLore.add(Component.text("§7Weather: §b" + (world.isThundering() ? "Gewitter" : (world.hasStorm() ? "Regen" : "Klar"))));
            flagsLore.add(Component.text("§7Time: §b" + world.getTime()));
            flagsMeta.lore(flagsLore);
            flags.setItemMeta(flagsMeta);
            actionMenu.setItem(18, flags);
        }
        // Backup (Dummy)
        ItemStack backup = new ItemStack(Material.CHEST);
        ItemMeta backupMeta = backup.getItemMeta();
        backupMeta.displayName(Component.text("§6Backup erstellen"));
        backupMeta.lore(List.of(Component.text("§7Erstellt ein Backup dieser Welt (demnächst).")));
        backup.setItemMeta(backupMeta);
        actionMenu.setItem(20, backup);
        // Zurück
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.displayName(Component.text("§7Zurück"));
        back.setItemMeta(backMeta);
        actionMenu.setItem(26, back);
        player.openInventory(actionMenu);
        // Speichere Weltname für spätere Aktionen
        player.setMetadata("admin_world_action", new org.bukkit.metadata.FixedMetadataValue(plugin, worldName));
    }
}
