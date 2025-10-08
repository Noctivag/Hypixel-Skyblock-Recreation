package de.noctivag.skyblock.gui.accessory;

import de.noctivag.skyblock.items.Accessory;
import de.noctivag.skyblock.items.AccessoryBag;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.List;

public class AccessoryBagGUI {
    private final Player player;
    private final AccessoryBag bag;
    private Inventory gui;

    public AccessoryBagGUI(Player player, AccessoryBag bag) {
        this.player = player;
        this.bag = bag;
        open();
    }

    public void open() {
        gui = Bukkit.createInventory(player, 27, Component.text("§dAccessory Bag"));
        List<Accessory> accessories = bag.getAll();
        int slot = 0;
        for (Accessory acc : accessories) {
            ItemStack item = new ItemStack(Material.NETHER_STAR);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(acc.getName()));
            meta.lore(List.of(Component.text("§8Rarity: " + acc.getRarity())));
            item.setItemMeta(meta);
            gui.setItem(slot++, item);
        }
        player.openInventory(gui);
    }
}
