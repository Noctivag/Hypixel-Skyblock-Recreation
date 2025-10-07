package de.noctivag.skyblock.slayers.gui;

import de.noctivag.skyblock.gui.CustomGUI;
import de.noctivag.skyblock.slayers.SlayerService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Slayer Tier Selection GUI - GUI for selecting slayer tier
 */
public class SlayerTierSelectionGUI extends CustomGUI {
    
    private final SlayerService slayerService;
    private final String slayerType;
    private final Player player;
    
    public SlayerTierSelectionGUI(Player player, SlayerService slayerService, String slayerType) {
        super("§cSelect " + slayerType + " Tier", 27);
        this.player = player;
        this.slayerService = slayerService;
        this.slayerType = slayerType;
    }
    
    public void setupItems() {
        // Tier I
        ItemStack tier1Item = new ItemStack(Material.IRON_SWORD);
        ItemMeta tier1Meta = tier1Item.getItemMeta();
        if (tier1Meta != null) {
            tier1Meta.setDisplayName("§aTier I");
            tier1Meta.setLore(Arrays.asList(
                "§7Difficulty: §aEasy",
                "§7Health: §a1,000",
                "§7Damage: §a50",
                "§7Reward: §aBasic",
                "",
                "§eClick to start Tier I quest"
            ));
            tier1Item.setItemMeta(tier1Meta);
        }
        inventory.setItem(10, tier1Item);
        
        // Tier II
        ItemStack tier2Item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta tier2Meta = tier2Item.getItemMeta();
        if (tier2Meta != null) {
            tier2Meta.setDisplayName("§eTier II");
            tier2Meta.setLore(Arrays.asList(
                "§7Difficulty: §eMedium",
                "§7Health: §e5,000",
                "§7Damage: §e100",
                "§7Reward: §eGood",
                "",
                "§eClick to start Tier II quest"
            ));
            tier2Item.setItemMeta(tier2Meta);
        }
        inventory.setItem(12, tier2Item);
        
        // Tier III
        ItemStack tier3Item = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta tier3Meta = tier3Item.getItemMeta();
        if (tier3Meta != null) {
            tier3Meta.setDisplayName("§cTier III");
            tier3Meta.setLore(Arrays.asList(
                "§7Difficulty: §cHard",
                "§7Health: §c25,000",
                "§7Damage: §c200",
                "§7Reward: §cGreat",
                "",
                "§eClick to start Tier III quest"
            ));
            tier3Item.setItemMeta(tier3Meta);
        }
        inventory.setItem(14, tier3Item);
        
        // Tier IV
        ItemStack tier4Item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta tier4Meta = tier4Item.getItemMeta();
        if (tier4Meta != null) {
            tier4Meta.setDisplayName("§5Tier IV");
            tier4Meta.setLore(Arrays.asList(
                "§7Difficulty: §5Extreme",
                "§7Health: §5100,000",
                "§7Damage: §5400",
                "§7Reward: §5Legendary",
                "",
                "§eClick to start Tier IV quest"
            ));
            tier4Item.setItemMeta(tier4Meta);
        }
        inventory.setItem(16, tier4Item);
        
        // Back button
        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemMeta backMeta = backItem.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName("§cBack");
            backItem.setItemMeta(backMeta);
        }
        inventory.setItem(22, backItem);
    }
    
    /**
     * Get the slayer type
     */
    public String getSlayerType() {
        return slayerType;
    }
}

