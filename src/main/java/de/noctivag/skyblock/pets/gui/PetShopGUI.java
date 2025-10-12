package de.noctivag.skyblock.pets.gui;

import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
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
    private final Player player;
    
    public PetShopGUI(Player player, PetService petService) {
        super("§cPet Shop", 54);
        this.petService = petService;
        this.player = player; // Store player reference
    }
    
    @Override
    public void setupItems() {
        // Get available pet types
        Map<String, PetType> availablePetTypes = petService.getAvailablePetTypes();
        
        // Show pet types in inventory
        int slot = 10;
        for (PetType petType : availablePetTypes.values()) {
            if (slot >= 44) break; // Don't exceed inventory size
            
            ItemStack petItem = new ItemStack(org.bukkit.Material.BONE);
            ItemMeta petMeta = petItem.getItemMeta();
            if (petMeta != null) {
                petMeta.displayName(Component.text("§c" + "Pet"));
                petMeta.lore(Arrays.asList(
                    "§7" + "A loyal companion",
                    "",
                    "§7Max Level: §c" + 100,
                    "§7Base Health: §c" + String.format("%.1f", 100.0),
                    "§7Base Damage: §c" + String.format("%.1f", 25.0),
                    "§7Base Defense: §c" + String.format("%.1f", 15.0),
                    "",
                    "§7Price: §e1,000 coins",
                    "§eClick to purchase"
                ).stream().map(Component::text).collect(Collectors.toList()));
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
            closeMeta.displayName(Component.text("§cClose"));
            closeItem.setItemMeta(closeMeta);
        }
        inventory.setItem(49, closeItem);
    }
}

