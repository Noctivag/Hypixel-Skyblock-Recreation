package de.noctivag.skyblock.brewing;

import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.CustomGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Brewing GUI for managing brewing recipes
 */
public class BrewingGUI extends CustomGUI {
    
    private final SkyblockPlugin plugin;
    private final Player player;
    
    public BrewingGUI(SkyblockPlugin plugin, Player player) {
        super("Brewing Recipes", 54);
        this.plugin = plugin;
        this.player = player;
        setupItems();
    }
    
    @Override
    public void setupItems() {
        // Add brewing recipes
        int slot = 0;
        for (BrewingRecipe recipe : plugin.getBrewingManager().getBrewingSystem().getRecipes().values()) {
            if (slot >= 54) break;
            
            ItemStack item = new ItemStack(Material.BREWING_STAND);
            ItemMeta meta = item.getItemMeta();
            
            if (meta != null) {
                meta.displayName(Component.text("§e" + recipe.getName()));
                meta.lore(Arrays.asList(
                    "§7ID: " + recipe.getId(),
                    "§7Brewing Time: " + recipe.getBrewingTime() + " ticks",
                    "§7Experience: " + recipe.getExperience(),
                    "",
                    "§aClick to start brewing!"
                ).stream().map(Component::text).collect(Collectors.toList()));
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
