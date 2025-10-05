package de.noctivag.skyblock.pets.gui;

import de.noctivag.skyblock.gui.CustomGUI;
import de.noctivag.skyblock.pets.PetType;
import de.noctivag.skyblock.pets.PetService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Pet Shop GUI - GUI for purchasing pets
 */
public class PetShopGUI extends CustomGUI {
    
    private final PetService petService;
    
    public PetShopGUI(Player player, PetService petService) {
        super(player, "§cPet Shop", 54);
        this.petService = petService;
    }
    
    @Override
    protected void setupItems() {
        // Get available pet types
        Map<String, PetType> availablePetTypes = petService.getAvailablePetTypes();
        
        // Show pet types in inventory
        int slot = 10;
        for (PetType petType : availablePetTypes.values()) {
            if (slot >= 44) break; // Don't exceed inventory size
            
            ItemStack petItem = new ItemStack(petType.getIcon());
            ItemMeta petMeta = petItem.getItemMeta();
            if (petMeta != null) {
                petMeta.setDisplayName("§c" + petType.getName());
                petMeta.setLore(Arrays.asList(
                    "§7" + petType.getDescription(),
                    "",
                    "§7Max Level: §c" + petType.getMaxLevel(),
                    "§7Base Health: §c" + String.format("%.1f", petType.getBaseHealth()),
                    "§7Base Damage: §c" + String.format("%.1f", petType.getBaseDamage()),
                    "§7Base Defense: §c" + String.format("%.1f", petType.getBaseDefense()),
                    "",
                    "§7Price: §e1,000 coins",
                    "§eClick to purchase"
                ));
                petItem.setItemMeta(petMeta);
            }
            inventory.setItem(slot, petItem);
            
            slot += 2; // Skip every other slot for better layout
        }
        
        // Add navigation items
        setupNavigation();
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
}

