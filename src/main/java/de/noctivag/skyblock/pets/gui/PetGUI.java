package de.noctivag.skyblock.pets.gui;

import de.noctivag.skyblock.gui.CustomGUI;
import de.noctivag.skyblock.pets.Pet;
import de.noctivag.skyblock.pets.PetBag;
import de.noctivag.skyblock.pets.PetService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Pet GUI - GUI for managing pets
 */
public class PetGUI extends CustomGUI {
    
    private final PetService petService;
    private final Player player;
    
    public PetGUI(Player player, PetService petService) {
        super("§cPet Menu", 54);
        this.petService = petService;
        this.player = player; // Store player reference
    }
    
    @Override
    public void setupItems() {
        // Get player's pet bag
        PetBag petBag = petService.getPlayerPetBag(player);
        
        // Show active pet
        Pet activePet = petBag.getActivePet();
        if (activePet != null) {
            setupActivePet(activePet);
        } else {
            setupNoActivePet();
        }
        
        // Show pet collection
        setupPetCollection(petBag);
        
        // Add navigation items
        setupNavigation();
    }
    
    private void setupActivePet(Pet activePet) {
        ItemStack activePetItem = new ItemStack(org.bukkit.Material.BONE);
        ItemMeta activePetMeta = activePetItem.getItemMeta();
        if (activePetMeta != null) {
            activePetMeta.setDisplayName("§cActive Pet: " + "Pet");
            activePetMeta.setLore(Arrays.asList(
                "§7Level: §c" + activePet.getLevel(),
                "§7Experience: §c" + String.format("%.1f", 50.0),
                "§7Health: §c" + String.format("%.1f", 100.0),
                "§7Damage: §c" + String.format("%.1f", 25.0),
                "§7Defense: §c" + String.format("%.1f", 15.0),
                "",
                "§eClick to deactivate"
            ));
            activePetItem.setItemMeta(activePetMeta);
        }
        inventory.setItem(4, activePetItem);
    }
    
    private void setupNoActivePet() {
        ItemStack noActivePetItem = new ItemStack(Material.BARRIER);
        ItemMeta noActivePetMeta = noActivePetItem.getItemMeta();
        if (noActivePetMeta != null) {
            noActivePetMeta.setDisplayName("§cNo Active Pet");
            noActivePetMeta.setLore(Arrays.asList(
                "§7You don't have an active pet",
                "§7Select a pet from your collection below",
                "",
                "§eClick to view pet collection"
            ));
            noActivePetItem.setItemMeta(noActivePetMeta);
        }
        inventory.setItem(4, noActivePetItem);
    }
    
    private void setupPetCollection(PetBag petBag) {
        List<Pet> pets = petBag.getPets();
        
        // Show pets in inventory
        int slot = 18;
        for (Pet pet : pets) {
            if (slot >= 44) break; // Don't exceed inventory size
            
            ItemStack petItem = new ItemStack(org.bukkit.Material.BONE);
            ItemMeta petMeta = petItem.getItemMeta();
            if (petMeta != null) {
                petMeta.setDisplayName("§c" + "Pet");
                petMeta.setLore(Arrays.asList(
                    "§7Level: §c" + pet.getLevel(),
                    "§7Experience: §c" + String.format("%.1f", 50.0),
                    "§7Health: §c" + String.format("%.1f", 100.0),
                    "§7Damage: §c" + String.format("%.1f", 25.0),
                    "§7Defense: §c" + String.format("%.1f", 15.0),
                    "",
                    pet.isActive() ? "§aCurrently Active" : "§eClick to activate"
                ));
                petItem.setItemMeta(petMeta);
            }
            inventory.setItem(slot, petItem);
            
            slot += 2; // Skip every other slot for better layout
        }
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

