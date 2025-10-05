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
 * Slayer Menu GUI - GUI for slayer quests
 */
public class SlayerMenuGUI extends CustomGUI {
    
    private final SlayerService slayerService;
    
    public SlayerMenuGUI(Player player, SlayerService slayerService) {
        super(player, "§cSlayer Menu", 54);
        this.slayerService = slayerService;
    }
    
    @Override
    protected void setupItems() {
        // Check if player has an active quest
        SlayerQuest activeQuest = slayerService.getActiveQuest(player);
        
        if (activeQuest != null) {
            // Show active quest
            ItemStack activeQuestItem = new ItemStack(Material.ZOMBIE_HEAD);
            ItemMeta meta = activeQuestItem.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§cActive Slayer Quest");
                meta.setLore(Arrays.asList(
                    "§7Type: §c" + activeQuest.getSlayerType(),
                    "§7Tier: §c" + activeQuest.getTier(),
                    "§7Time Remaining: §c" + formatTime(activeQuest.getRemainingTime()),
                    "",
                    "§eClick to cancel quest"
                ));
                activeQuestItem.setItemMeta(meta);
            }
            inventory.setItem(22, activeQuestItem);
        } else {
            // Show available slayer types
            setupSlayerTypes();
        }
        
        // Add navigation items
        setupNavigation();
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
                "",
                "§eClick to start quest"
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
                "",
                "§eClick to start quest"
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
                "",
                "§eClick to start quest"
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

