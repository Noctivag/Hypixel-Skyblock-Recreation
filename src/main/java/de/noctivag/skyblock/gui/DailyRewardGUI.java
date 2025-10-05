package de.noctivag.skyblock.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Daily Reward GUI
 */
public class DailyRewardGUI extends CustomGUI {
    
    public DailyRewardGUI() {
        super("§6Daily Rewards", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add daily rewards
        ItemStack rewards = new ItemStack(Material.GOLD_INGOT);
        ItemMeta rewardsMeta = rewards.getItemMeta();
        rewardsMeta.setDisplayName("§6Daily Rewards");
        rewardsMeta.setLore(Arrays.asList("§7Claim your daily rewards", "§7Click to claim!"));
        rewards.setItemMeta(rewardsMeta);
        inventory.setItem(22, rewards);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the daily reward GUI for a player
     */
    public static void openForPlayer(Player player) {
        DailyRewardGUI gui = new DailyRewardGUI();
        gui.open(player);
    }
}

