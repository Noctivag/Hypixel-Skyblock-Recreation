package de.noctivag.skyblock.gui.features;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class AuctionHouseGUI extends Menu {
    public AuctionHouseGUI(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lAuction House", 54);
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
