package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.dungeons.DungeonManager;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DungeonGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final DungeonManager dungeonManager;

    public DungeonGUI(SkyblockPluginRefactored plugin, Player player) {
        super(player);
        this.plugin = plugin;
        this.dungeonManager = plugin.getServiceManager().getService(DungeonManager.class);
        this.inventory = Bukkit.createInventory(this, 54, "§8Dungeons");
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void setMenuItems() {
        fillBorderWith(Material.GRAY_STAINED_GLASS_PANE);

        // Title item
        ItemStack titleItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§lDungeons");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Wähle einen Dungeon zum Spielen");
        titleLore.add("§7Jeder Dungeon hat verschiedene Schwierigkeitsgrade!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Catacombs Floor 1
        ItemStack catacombsF1 = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta catacombsF1Meta = catacombsF1.getItemMeta();
        catacombsF1Meta.setDisplayName("§c§lCatacombs Floor 1");
        List<String> catacombsF1Lore = new ArrayList<>();
        catacombsF1Lore.add("§7Boss: §cBonzo");
        catacombsF1Lore.add("§7Schwierigkeit: §aEinfach");
        catacombsF1Lore.add("§7Empfohlenes Level: §e5");
        catacombsF1Lore.add("§7Max. Spieler: §b5");
        catacombsF1Lore.add("");
        catacombsF1Lore.add("§7Belohnungen:");
        catacombsF1Lore.add("§a• 1000 Coins");
        catacombsF1Lore.add("§a• 500 XP");
        catacombsF1Lore.add("§a• Dungeon Key");
        catacombsF1Lore.add("");
        catacombsF1Lore.add("§eKlicke um zu starten!");
        catacombsF1Meta.setLore(catacombsF1Lore);
        catacombsF1.setItemMeta(catacombsF1Meta);
        inventory.setItem(20, catacombsF1);

        // Catacombs Floor 2
        ItemStack catacombsF2 = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemMeta catacombsF2Meta = catacombsF2.getItemMeta();
        catacombsF2Meta.setDisplayName("§c§lCatacombs Floor 2");
        List<String> catacombsF2Lore = new ArrayList<>();
        catacombsF2Lore.add("§7Boss: §cScarf");
        catacombsF2Lore.add("§7Schwierigkeit: §eMittel");
        catacombsF2Lore.add("§7Empfohlenes Level: §e10");
        catacombsF2Lore.add("§7Max. Spieler: §b5");
        catacombsF2Lore.add("");
        catacombsF2Lore.add("§7Belohnungen:");
        catacombsF2Lore.add("§a• 2500 Coins");
        catacombsF2Lore.add("§a• 1000 XP");
        catacombsF2Lore.add("§a• Dungeon Key");
        catacombsF2Lore.add("§a• Seltene Items");
        catacombsF2Lore.add("");
        catacombsF2Lore.add("§cBenötigt: Catacombs Floor 1 abgeschlossen");
        catacombsF2Meta.setLore(catacombsF2Lore);
        catacombsF2.setItemMeta(catacombsF2Meta);
        inventory.setItem(22, catacombsF2);

        // Catacombs Floor 3
        ItemStack catacombsF3 = new ItemStack(Material.DRAGON_HEAD);
        ItemMeta catacombsF3Meta = catacombsF3.getItemMeta();
        catacombsF3Meta.setDisplayName("§c§lCatacombs Floor 3");
        List<String> catacombsF3Lore = new ArrayList<>();
        catacombsF3Lore.add("§7Boss: §cThe Professor");
        catacombsF3Lore.add("§7Schwierigkeit: §6Schwer");
        catacombsF3Lore.add("§7Empfohlenes Level: §e15");
        catacombsF3Lore.add("§7Max. Spieler: §b5");
        catacombsF3Lore.add("");
        catacombsF3Lore.add("§7Belohnungen:");
        catacombsF3Lore.add("§a• 5000 Coins");
        catacombsF3Lore.add("§a• 2000 XP");
        catacombsF3Lore.add("§a• Dungeon Key");
        catacombsF3Lore.add("§a• Epische Items");
        catacombsF3Lore.add("");
        catacombsF3Lore.add("§cBenötigt: Catacombs Floor 2 abgeschlossen");
        catacombsF3Meta.setLore(catacombsF3Lore);
        catacombsF3.setItemMeta(catacombsF3Meta);
        inventory.setItem(24, catacombsF3);

        // Dungeon Info
        ItemStack infoItem = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = infoItem.getItemMeta();
        infoMeta.setDisplayName("§b§lDungeon-Informationen");
        List<String> infoLore = new ArrayList<>();
        infoLore.add("§7Dungeons sind kooperative Abenteuer");
        infoLore.add("§7mit anderen Spielern. Jeder Dungeon");
        infoLore.add("§7hat einen einzigartigen Boss und");
        infoLore.add("§7verschiedene Belohnungen.");
        infoLore.add("");
        infoLore.add("§7Tipp: Wähle die richtige Klasse");
        infoLore.add("§7für dein Team aus!");
        infoMeta.setLore(infoLore);
        infoItem.setItemMeta(infoMeta);
        inventory.setItem(31, infoItem);

        // Class Selection
        ItemStack classItem = new ItemStack(Material.IRON_SWORD);
        ItemMeta classMeta = classItem.getItemMeta();
        classMeta.setDisplayName("§e§lKlassen-Auswahl");
        List<String> classLore = new ArrayList<>();
        classLore.add("§7Wähle deine Dungeon-Klasse");
        classLore.add("§7bevor du einen Dungeon startest!");
        classLore.add("");
        classLore.add("§7Verfügbare Klassen:");
        classLore.add("§a• Bogenschütze - Fernkampf");
        classLore.add("§b• Magier - Magie-Schäden");
        classLore.add("§c• Berserker - Nahkampf");
        classLore.add("§d• Heiler - Team-Heilung");
        classLore.add("§6• Tank - Verteidigung");
        classLore.add("");
        classLore.add("§eKlicke um Klasse zu wählen!");
        classMeta.setLore(classLore);
        classItem.setItemMeta(classMeta);
        inventory.setItem(33, classItem);

        // Current Dungeon Status
        if (dungeonManager != null && dungeonManager.isPlayerInDungeon(player)) {
            ItemStack statusItem = new ItemStack(Material.REDSTONE);
            ItemMeta statusMeta = statusItem.getItemMeta();
            statusMeta.setDisplayName("§c§lAktueller Dungeon");
            List<String> statusLore = new ArrayList<>();
            statusLore.add("§7Du bist derzeit in einem Dungeon!");
            statusLore.add("§7Verlasse den aktuellen Dungeon");
            statusLore.add("§7bevor du einen neuen startest.");
            statusLore.add("");
            statusLore.add("§eKlicke um Dungeon zu verlassen!");
            statusMeta.setLore(statusLore);
            statusItem.setItemMeta(statusMeta);
            inventory.setItem(40, statusItem);
        }

        addNavigationButtons();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getInventory().equals(inventory)) return;
        
        event.setCancelled(true);
        
        Player clickedPlayer = (Player) event.getWhoClicked();
        if (!clickedPlayer.equals(player)) return;
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Handle navigation buttons
        if (event.getSlot() == 45) { // Back button
            clickedPlayer.closeInventory();
            return;
        }
        if (event.getSlot() == 53) { // Close button
            clickedPlayer.closeInventory();
            return;
        }
        
        // Handle dungeon selection
        switch (event.getSlot()) {
            case 20: // Catacombs Floor 1
                startDungeon(clickedPlayer, "catacombs", 1);
                break;
            case 22: // Catacombs Floor 2
                startDungeon(clickedPlayer, "catacombs", 2);
                break;
            case 24: // Catacombs Floor 3
                startDungeon(clickedPlayer, "catacombs", 3);
                break;
            case 33: // Class Selection
                openClassSelection(clickedPlayer);
                break;
            case 40: // Leave current dungeon
                leaveDungeon(clickedPlayer);
                break;
        }
    }

    private void startDungeon(Player player, String dungeonType, int floor) {
        if (dungeonManager == null) {
            player.sendMessage("§cDungeon-System ist nicht verfügbar!");
            return;
        }

        if (dungeonManager.isPlayerInDungeon(player)) {
            player.sendMessage("§cDu bist bereits in einem Dungeon!");
            return;
        }

        if (dungeonManager.startDungeon(player, dungeonType, floor)) {
            player.closeInventory();
        } else {
            player.sendMessage("§cFehler beim Starten des Dungeons!");
        }
    }

    private void openClassSelection(Player player) {
        player.closeInventory();
        new ClassSelectionGUI(plugin, player).open();
    }

    private void leaveDungeon(Player player) {
        if (dungeonManager == null) {
            player.sendMessage("§cDungeon-System ist nicht verfügbar!");
            return;
        }

        if (dungeonManager.leaveDungeon(player)) {
            player.closeInventory();
        } else {
            player.sendMessage("§cFehler beim Verlassen des Dungeons!");
        }
    }
}
