package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class AdminMenu implements InventoryHolder {
    private final SkyblockPlugin plugin;
    private final Inventory inventory;

    public AdminMenu(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(this, 27, Component.text("§c§lAdmin-Menü"));
        initializeItems();
    }
    
    public AdminMenu(SkyblockPlugin plugin, Player player) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(this, 27, Component.text("§c§lAdmin-Menü"));
        initializeItems();
    }

    private void initializeItems() {
        // Vanish toggle
        setItem(11, createGuiItem(Material.ENDER_EYE, "§eVanish ein/aus",
            "§7Toggle deinen Sichtbarkeitsstatus"));

        // Player selector for invsee
        setItem(13, createGuiItem(Material.PLAYER_HEAD, "§eSpieler auswählen",
            "§7Öffne das Inventar eines Spielers"));

        // Ranks management
        setItem(15, createGuiItem(Material.NAME_TAG, "§eRänge verwalten",
            "§7Spieler-Ränge setzen"));

        // Command management
        setItem(17, createGuiItem(Material.COMMAND_BLOCK, "§eCommands verwalten",
            "§7Cooldowns, Kosten, Status"));

        // Back
        setItem(22, createGuiItem(Material.ARROW, "§7Zurück",
            "§7Zum Hauptmenü"));

        // Fill empty slots with filler
        ItemStack filler = createGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) inventory.setItem(i, filler);
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private void setItem(int slot, ItemStack item) { inventory.setItem(slot, item); }

    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) meta.lore(Arrays.stream(lore).map(Component::text).toList());
            item.setItemMeta(meta);
        }
        return item;
    }
}
