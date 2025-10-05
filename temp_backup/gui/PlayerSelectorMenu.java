package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PlayerSelectorMenu implements InventoryHolder {
    private final Inventory inventory;
    private final Map<Integer, String> slotToPlayer = new HashMap<>();

    public PlayerSelectorMenu(SkyblockPlugin SkyblockPlugin) {
        this.inventory = Bukkit.createInventory(this, 54, Component.text("§6Spieler auswählen"));
        initializeItems();
    }

    private void initializeItems() {
        int slot = 10;
        for (Player p : Bukkit.getOnlinePlayers()) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text(p.getName()));
                meta.setOwningPlayer(p);
                head.setItemMeta(meta);
            }
            inventory.setItem(slot, head);
            slotToPlayer.put(slot, p.getName());
            slot++;
            if ((slot % 9) == 8) slot += 2; // skip right border
            if (slot >= 44) break;
        }

        // Back button
        ItemStack back = new ItemStack(Material.ARROW);
        inventory.setItem(49, back);
    }

    public void open(Player player) { player.openInventory(inventory); }

    public String getPlayerAtSlot(int slot) { return slotToPlayer.get(slot); }

    @NotNull
    @Override
    public Inventory getInventory() { return inventory; }
}
