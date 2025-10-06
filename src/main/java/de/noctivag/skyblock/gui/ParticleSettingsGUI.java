package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Particle Settings GUI - Configure particle effects
 */
public class ParticleSettingsGUI extends Menu {
    
    public ParticleSettingsGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8Particle Settings", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Particle Effects
        inventory.setItem(10, createItem(Material.FIREWORK_STAR, "§aParticle Effects", 
            "§7Configure your particle effects",
            "§7• Enable/Disable particles",
            "§7• Change particle density",
            "§7• Select particle types",
            "",
            "§eClick to configure"));
        
        // Particle Density
        inventory.setItem(12, createItem(Material.REDSTONE, "§cParticle Density", 
            "§7Adjust particle density",
            "§7• Low: Fewer particles",
            "§7• Medium: Normal particles",
            "§7• High: More particles",
            "",
            "§eClick to change"));
        
        // Particle Types
        inventory.setItem(14, createItem(Material.NETHER_STAR, "§dParticle Types", 
            "§7Select particle types",
            "§7• Hearts",
            "§7• Stars",
            "§7• Clouds",
            "§7• Custom shapes",
            "",
            "§eClick to select"));
        
        // Close button
        inventory.setItem(49, createItem(Material.BARRIER, "§cClose", 
            "§7Close this menu", "", "§eClick to close"));
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        switch (slot) {
            case 10 -> {
                // Particle Effects configuration
                player.sendMessage("§aOpening particle effects configuration...");
            }
            case 12 -> {
                // Particle Density
                player.sendMessage("§cChanging particle density...");
            }
            case 14 -> {
                // Particle Types
                player.sendMessage("§dOpening particle types selection...");
            }
            case 49 -> {
                // Close
                player.closeInventory();
            }
        }
    }
}
