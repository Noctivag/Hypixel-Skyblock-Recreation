package de.noctivag.skyblock.slayers.gui;

import de.noctivag.skyblock.gui.CustomGUI;
import de.noctivag.skyblock.slayers.SlayerService;
import de.noctivag.skyblock.slayers.quests.SlayerQuest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Advanced Slayer GUI - Advanced GUI for slayer quests
 */
public class AdvancedSlayerGUI extends CustomGUI {
    
    private final SlayerService slayerService;
    private final Player player;
    
    public AdvancedSlayerGUI(Player player, SlayerService slayerService) {
        super("§cAdvanced Slayer Menu", 54);
        this.player = player;
        this.slayerService = slayerService;
    }
    
    public void setupItems() {
        // Check if player has an active quest
        SlayerQuest activeQuest = slayerService.getActiveQuest(player);
        
        if (activeQuest != null) {
            // Show active quest details
            setupActiveQuest(activeQuest);
        } else {
            // Show available slayer types
            setupSlayerTypes();
        }
        
        // Add navigation items
        setupNavigation();
    }
    
    private void setupActiveQuest(SlayerQuest quest) {
        // Active quest info
        ItemStack questInfoItem = new ItemStack(Material.BOOK);
        ItemMeta questInfoMeta = questInfoItem.getItemMeta();
        if (questInfoMeta != null) {
            questInfoMeta.setDisplayName("§cActive Quest Information");
            questInfoMeta.setLore(Arrays.asList(
                "§7Type: §c" + quest.getSlayerType(),
                "§7Tier: §c" + quest.getTier(),
                "§7Time Remaining: §c" + formatTime(quest.getRemainingTime()),
                "§7Status: §a" + (quest.isCompleted() ? "Completed" : "In Progress"),
                "",
                "§eClick to view details"
            ));
            questInfoItem.setItemMeta(questInfoMeta);
        }
        inventory.setItem(22, questInfoItem);
        
        // Cancel quest button
        ItemStack cancelItem = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = cancelItem.getItemMeta();
        if (cancelMeta != null) {
            cancelMeta.setDisplayName("§cCancel Quest");
            cancelMeta.setLore(Arrays.asList(
                "§7Cancel your current slayer quest",
                "§7Warning: This will forfeit all progress!",
                "",
                "§eClick to cancel"
            ));
            cancelItem.setItemMeta(cancelMeta);
        }
        inventory.setItem(40, cancelItem);
    }
    
    private void setupSlayerTypes() {
        // Revenant Horror
        ItemStack revenantItem = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta revenantMeta = revenantItem.getItemMeta();
        if (revenantMeta != null) {
            revenantMeta.setDisplayName("§cRevenant Horror");
            revenantMeta.setLore(Arrays.asList(
                "§7Undead slayer boss",
                "§7Drops: Rotten Flesh, Revenant Viscera",
                "§7Special: Healing abilities",
                "",
                "§eClick to select tier"
            ));
            revenantItem.setItemMeta(revenantMeta);
        }
        inventory.setItem(20, revenantItem);
        
        // Tarantula Broodmother
        ItemStack tarantulaItem = new ItemStack(Material.SPIDER_EYE);
        ItemMeta tarantulaMeta = tarantulaItem.getItemMeta();
        if (tarantulaMeta != null) {
            tarantulaMeta.setDisplayName("§cTarantula Broodmother");
            tarantulaMeta.setLore(Arrays.asList(
                "§7Spider slayer boss",
                "§7Drops: Spider Eye, Tarantula Web",
                "§7Special: Web attacks",
                "",
                "§eClick to select tier"
            ));
            tarantulaItem.setItemMeta(tarantulaMeta);
        }
        inventory.setItem(22, tarantulaItem);
        
        // Sven Packmaster
        ItemStack svenItem = new ItemStack(Material.WOLF_SPAWN_EGG);
        ItemMeta svenMeta = svenItem.getItemMeta();
        if (svenMeta != null) {
            svenMeta.setDisplayName("§cSven Packmaster");
            svenMeta.setLore(Arrays.asList(
                "§7Wolf slayer boss",
                "§7Drops: Wolf Tooth, Sven Packmaster Fur",
                "§7Special: Pack attacks",
                "",
                "§eClick to select tier"
            ));
            svenItem.setItemMeta(svenMeta);
        }
        inventory.setItem(24, svenItem);
    }
    
    private void setupNavigation() {
        // Close button
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        if (closeMeta != null) {
            closeMeta.setDisplayName("§cClose");
            closeItem.setItemMeta(closeMeta);
        }
        inventory.setItem(49, closeItem);
    }
    
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        if (minutes > 0) {
            return String.format("%d:%02d", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }
}

