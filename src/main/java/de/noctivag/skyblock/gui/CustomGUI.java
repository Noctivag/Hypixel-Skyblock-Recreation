package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CustomGUI {
    protected final SkyblockPlugin plugin;
    protected final String title;
    protected final int size;
    protected Inventory inventory;

    public CustomGUI(SkyblockPlugin plugin, String title, int size) {
        this.plugin = plugin;
        this.title = title;
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public CustomGUI(String title, int size) {
        this.plugin = null;
        this.title = title;
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public CustomGUI(int size, TextComponent title) {
        this.plugin = null;
        this.title = title.content();
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
    
    public void openGUI(Player player) {
        open(player);
    }

    public void setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    public void setItem(int slot, ItemStack item, String displayName, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName));
            if (lore != null) {
                meta.lore(lore.stream().map(Component::text).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        setItem(slot, item);
    }

    public void setItem(int slot, Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName));
            item.setItemMeta(meta);
        }
        setItem(slot, item);
    }

    public void setItem(int slot, Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName));
            if (lore != null && lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        setItem(slot, item);
    }

    public void setItem(int slot, Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName));
            if (lore != null) {
                meta.lore(lore.stream().map(Component::text).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        setItem(slot, item);
    }

    public void clearInventory() {
        inventory.clear();
    }

    public ItemStack createGuiItem(Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName));
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack createGuiItem(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName));
            if (lore != null && lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack createGuiItem(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName));
            if (lore != null) {
                meta.lore(lore.stream().map(Component::text).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack createPlayerHead(Player player, String displayName, String... lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(displayName));
            if (lore != null && lore.length > 0) {
                meta.lore(Arrays.stream(lore).map(Component::text).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
