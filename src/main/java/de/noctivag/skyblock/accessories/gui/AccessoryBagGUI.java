package de.noctivag.skyblock.accessories.gui;

import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import de.noctivag.skyblock.gui.CustomGUI;
import de.noctivag.skyblock.accessories.Accessory;
import de.noctivag.skyblock.accessories.AccessoryBag;
import de.noctivag.skyblock.accessories.MagicalPowerService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Accessory Bag GUI - GUI for managing accessories
 */
public class AccessoryBagGUI extends CustomGUI {
    
    private final MagicalPowerService magicalPowerService;
    
    private final Player player;
    
    public AccessoryBagGUI(Player player, MagicalPowerService magicalPowerService) {
        super("§cAccessory Bag", 54);
        this.player = player;
        this.magicalPowerService = magicalPowerService;
    }
    
    @Override
    public void setupItems() {
        // Get player's accessory bag
        AccessoryBag accessoryBag = magicalPowerService.getPlayerAccessoryBag(player);
        
        // Show magical power
        double magicalPower = magicalPowerService.getPlayerMagicalPower(player);
        ItemStack powerItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta powerMeta = powerItem.getItemMeta();
        if (powerMeta != null) {
            powerMeta.displayName(Component.text("§cMagical Power"));
            powerMeta.lore(Arrays.asList(
                "§7Current Power: §c" + String.format("%.1f", magicalPower),
                "",
                "§7Magical Power increases your",
                "§7stat bonuses from accessories"
            ).stream().map(Component::text).collect(Collectors.toList()));
            powerItem.setItemMeta(powerMeta);
        }
        inventory.setItem(4, powerItem);
        
        // Show accessories
        setupAccessories(accessoryBag);
        
        // Add navigation items
        setupNavigation();
    }
    
    private void setupAccessories(AccessoryBag accessoryBag) {
        List<Accessory> accessories = accessoryBag.getAccessories();
        
        // Show accessories in inventory
        int slot = 18;
        for (Accessory accessory : accessories) {
            if (slot >= 44) break; // Don't exceed inventory size
            
            ItemStack accessoryItem = new ItemStack(accessory.getMaterial());
            ItemMeta accessoryMeta = accessoryItem.getItemMeta();
            if (accessoryMeta != null) {
                accessoryMeta.displayName(Component.text(accessory.getName()));
                accessoryMeta.lore(Arrays.asList(
                    "§7" + accessory.getDescription(),
                    "",
                    "§7Rarity: §c" + accessory.getRarity().name(),
                    "§7Stats:",
                    "§7- " + accessory.getStatType().name() + ": §c+" + accessory.getStatValue(),
                    "",
                    "§eClick to remove"
                ).stream().map(Component::text).collect(Collectors.toList()));
                accessoryItem.setItemMeta(accessoryMeta);
            }
            inventory.setItem(slot, accessoryItem);
            
            slot += 2; // Skip every other slot for better layout
        }
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
