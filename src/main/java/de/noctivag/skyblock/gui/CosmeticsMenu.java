package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Cosmetics Menu GUI
 */
public class CosmeticsMenu extends CustomGUI {
    
    public CosmeticsMenu() {
        super("§dCosmetics Menu", 54);
        setupItems();
    }
    
    public CosmeticsMenu(SkyblockPlugin plugin, Object cosmeticsManager) {
        super("§dCosmetics Menu", 54);
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add cosmetics items
        ItemStack pets = new ItemStack(Material.BONE);
        ItemMeta petsMeta = pets.getItemMeta();
        petsMeta.setDisplayName("§dPets");
        petsMeta.setLore(Arrays.asList("§7Manage your pets", "§7Click to open!"));
        pets.setItemMeta(petsMeta);
        inventory.setItem(10, pets);
        
        ItemStack accessories = new ItemStack(Material.DIAMOND);
        ItemMeta accessoriesMeta = accessories.getItemMeta();
        accessoriesMeta.setDisplayName("§dAccessories");
        accessoriesMeta.setLore(Arrays.asList("§7Manage your accessories", "§7Click to open!"));
        accessories.setItemMeta(accessoriesMeta);
        inventory.setItem(12, accessories);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the cosmetics menu for a player
     */
    public static void openForPlayer(Player player) {
        CosmeticsMenu menu = new CosmeticsMenu();
        menu.open(player);
    }
}

