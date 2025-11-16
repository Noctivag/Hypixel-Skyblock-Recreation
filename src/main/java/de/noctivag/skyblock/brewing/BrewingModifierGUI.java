package de.noctivag.skyblock.brewing;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.CustomGUI;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * GUI for managing brewing modifiers
 */
public class BrewingModifierGUI extends CustomGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    private final BrewingModifierManager modifierManager;
    
    public BrewingModifierGUI(SkyblockPlugin plugin, Player player, BrewingModifierManager modifierManager) {
        super("Brewing Modifiers", 54);
        this.plugin = plugin;
        this.player = player;
        this.modifierManager = modifierManager;
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add modifier items
        int slot = 0;
        for (BrewingModifierType type : BrewingModifierType.values()) {
            if (slot >= 54) break;
            
            BrewingModifier modifier = modifierManager.getModifier(player.getUniqueId(), type);
            
            ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta meta = item.getItemMeta();
            
            if (meta != null) {
                if (modifier != null) {
                    meta.displayName(Component.text("§a" + type.name() + " §7(" + modifier.getMultiplier() + "x)"));
                    meta.lore(Arrays.asList(
                        "§7Type: " + type.name(),
                        "§7Multiplier: " + modifier.getMultiplier(),
                        "§7Description: " + modifier.getDescription(),
                        "",
                        "§cClick to remove modifier"
                    ).stream().map(Component::text).collect(Collectors.toList()));
                } else {
                    meta.displayName(Component.text("§7" + type.name() + " §8(No modifier)"));
                    meta.lore(Arrays.asList(
                        "§7Type: " + type.name(),
                        "§7Multiplier: 1.0x",
                        "§7Description: No modifier applied",
                        "",
                        "§aClick to add modifier"
                    ).stream().map(Component::text).collect(Collectors.toList()));
                }
                item.setItemMeta(meta);
            }
            
            inventory.setItem(slot, item);
            slot++;
        }
        
        // Add close button
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeButton.getItemMeta();
        if (closeMeta != null) {
            closeMeta.displayName(Component.text("§cClose"));
            closeButton.setItemMeta(closeMeta);
        }
        inventory.setItem(53, closeButton);
    }
}
