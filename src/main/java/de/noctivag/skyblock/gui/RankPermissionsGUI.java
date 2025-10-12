package de.noctivag.skyblock.gui;

import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Rank Permissions GUI
 */
public class RankPermissionsGUI extends CustomGUI {
    
    public RankPermissionsGUI() {
        super("§eRank Permissions", 54);
        setupItems();
    }
    
    public RankPermissionsGUI(SkyblockPlugin plugin, String rankKey) {
        super("§eRank Permissions", 54);
        this.rankKey = rankKey;
        setupItems();
    }
    
    private String rankKey;
    
    @Override
    public void setupItems() {
        // Add rank permissions
        ItemStack permissions = new ItemStack(Material.GOLD_INGOT);
        ItemMeta permissionsMeta = permissions.getItemMeta();
        permissionsMeta.displayName(Component.text("§eRank Permissions"));
        permissionsMeta.lore(Arrays.asList("§7Manage rank permissions", "§7Click to configure!").stream().map(Component::text).collect(Collectors.toList()));
        // Note: Using deprecated methods - consider updating to Adventure API
        permissions.setItemMeta(permissionsMeta);
        inventory.setItem(22, permissions);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(Component.text("§cClose"));
        // Note: Using deprecated methods - consider updating to Adventure API
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the rank permissions GUI for a player
     */
    public static void openForPlayer(Player player) {
        RankPermissionsGUI gui = new RankPermissionsGUI();
        gui.open(player);
    }
    
    public String getRankKey() {
        return rankKey;
    }
}

