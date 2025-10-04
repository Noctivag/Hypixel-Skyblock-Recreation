package de.noctivag.plugin.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.rewards.DailyReward;
import de.noctivag.plugin.rewards.DailyRewardManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DailyRewardGUI implements InventoryHolder {
    private final Plugin plugin;
    private final Inventory inventory;

    public DailyRewardGUI(Plugin plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(this, 54, Component.text("Tägliche Belohnungen"));
        initializeItems();
    }

    private void initializeItems() {
        DailyRewardManager rewardManager = plugin.getDailyRewardManager();
        for (int i = 1; i <= 7; i++) {
            DailyReward reward = rewardManager.getReward(i);
            if (reward != null) {
                inventory.setItem(i + 27, createRewardItem(reward));
            }
        }
        
        // Back button
        ItemStack backButton = new ItemStack(org.bukkit.Material.ARROW);
        ItemMeta backMeta = backButton.getItemMeta();
        if (backMeta != null) {
            backMeta.displayName(Component.text("§7Zurück"));
            backMeta.lore(Arrays.asList(Component.text("§7Zum Hauptmenü")));
            backButton.setItemMeta(backMeta);
        }
        inventory.setItem(49, backButton);
    }

    private ItemStack createRewardItem(DailyReward reward) {
        ItemStack item = new ItemStack(reward.getDisplayIcon());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§eTag " + reward.getDay()));
            List<Component> lore = new ArrayList<>();
            reward.getRewards().forEach(r -> lore.add(Component.text("§7- " + r.getAmount() + "x " + r.getType().toString())));
            meta.lore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }


    public void open(Player player) {
        player.openInventory(inventory);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
