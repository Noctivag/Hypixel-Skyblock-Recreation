package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BazaarGUI extends Menu {
    public BazaarGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lBazaar", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        setCloseButton(49);
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        if (event.getSlot() == 49) close();
    }
}
