package de.noctivag.skyblock.gui;

import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.cosmetics.ParticleShape;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Advanced Cosmetics Menu GUI
 */
public class AdvancedCosmeticsMenu extends CustomGUI {
    
    public AdvancedCosmeticsMenu() {
        super("§dAdvanced Cosmetics", 54);
        setupItems();
    }
    
    public AdvancedCosmeticsMenu(SkyblockPlugin plugin, Player player) {
        super("§dAdvanced Cosmetics", 54);
        this.player = player;
        setupItems();
    }
    
    private Player player;
    
    @Override
    public void open() {
        if (player != null) {
            open(player);
        }
    }
    
    @Override
    public void setupItems() {
        // Add advanced cosmetics items
        ItemStack cosmetics = new ItemStack(Material.NETHER_STAR);
        ItemMeta cosmeticsMeta = cosmetics.getItemMeta();
        cosmeticsMeta.displayName(Component.text("§dAdvanced Cosmetics"));
        cosmeticsMeta.lore(Arrays.asList("§7Advanced cosmetic options", "§7Click to open!").stream().map(Component::text).collect(Collectors.toList()));
        cosmetics.setItemMeta(cosmeticsMeta);
        inventory.setItem(22, cosmetics);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.displayName(Component.text("§cClose"));
        close.setItemMeta(closeMeta);
        inventory.setItem(49, close);
    }
    
    /**
     * Open the advanced cosmetics menu for a player
     */
    public static void openForPlayer(Player player) {
        AdvancedCosmeticsMenu menu = new AdvancedCosmeticsMenu();
        menu.open(player);
    }
    
    public ParticleShape getShapeAtSlot(int slot) {
        // TODO: Implement particle shape mapping
        return null;
    }
}

