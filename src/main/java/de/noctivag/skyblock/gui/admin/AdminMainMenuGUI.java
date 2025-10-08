package de.noctivag.skyblock.gui.admin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdminMainMenuGUI {
    private static final String TITLE = "§cAdmin Menü";
    private final Player player;
    private Inventory gui;

    public AdminMainMenuGUI(Player player) {
        this.player = player;
        open();
    }

    public void open() {
        gui = Bukkit.createInventory(null, 27, TITLE);
        // Weltverwaltung
        ItemStack worldManager = new ItemStack(Material.COMPASS);
        ItemMeta wmMeta = worldManager.getItemMeta();
        wmMeta.setDisplayName("§bWeltverwaltung");
        wmMeta.setLore(List.of("§7Verwalte alle Welten und Statistiken."));
        worldManager.setItemMeta(wmMeta);
        gui.setItem(10, worldManager);
        // Arsenal
        ItemStack arsenal = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta arsenalMeta = arsenal.getItemMeta();
        arsenalMeta.setDisplayName("§cArsenal");
        arsenalMeta.setLore(List.of("§7Verwalte Admin-Waffen und Tools."));
        arsenal.setItemMeta(arsenalMeta);
        gui.setItem(12, arsenal);
        // Weitere Admin-Funktionen (Platzhalter)
        ItemStack more = new ItemStack(Material.BOOK);
        ItemMeta moreMeta = more.getItemMeta();
        moreMeta.setDisplayName("§eWeitere Admin-Funktionen");
        List<String> lore = new ArrayList<>();
        lore.add("§7- Spieler verwalten");
        lore.add("§7- Servereinstellungen");
        lore.add("§7- ...");
        moreMeta.setLore(lore);
        more.setItemMeta(moreMeta);
        gui.setItem(14, more);
        player.openInventory(gui);
    }

    public void handleClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(TITLE)) return;
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot == 10) {
            new AdminWorldManagerGUI((de.noctivag.skyblock.SkyblockPlugin) Bukkit.getPluginManager().getPlugin("SkyblockPlugin"), player);
        } else if (slot == 12) {
            player.performCommand("arsenal");
        } else if (slot == 14) {
            player.sendMessage("§eWeitere Admin-Funktionen folgen!");
        }
    }
}
