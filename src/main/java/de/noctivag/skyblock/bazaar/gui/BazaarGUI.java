package de.noctivag.skyblock.bazaar.gui;

import de.noctivag.skyblock.gui.CustomGUI;
import de.noctivag.skyblock.bazaar.BazaarOrder;
import de.noctivag.skyblock.bazaar.BazaarService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Bazaar GUI - GUI for the bazaar
 */
public class BazaarGUI extends CustomGUI {
    
    private final BazaarService bazaarService;
    
    private final Player player;
    
    public BazaarGUI(Player player, BazaarService bazaarService) {
        super("§cBazaar", 54);
        this.player = player;
        this.bazaarService = bazaarService;
    }
    
    @Override
    public void setupItems() {
        // Get player's orders
        Map<java.util.UUID, BazaarOrder> playerOrders = bazaarService.getPlayerOrders(player);
        
        // Show player's orders
        setupPlayerOrders(playerOrders);
        
        // Add navigation items
        setupNavigation();
    }
    
    private void setupPlayerOrders(Map<java.util.UUID, BazaarOrder> playerOrders) {
        int slot = 10;
        for (BazaarOrder order : playerOrders.values()) {
            if (slot >= 44) break; // Don't exceed inventory size
            
            ItemStack orderItem = new ItemStack(Material.PAPER);
            ItemMeta orderMeta = orderItem.getItemMeta();
            if (orderMeta != null) {
                orderMeta.displayName(net.kyori.adventure.text.Component.text(order.getOrderType().getColorCode() + order.getOrderType().getDisplayName() + " Order"));
                List<net.kyori.adventure.text.Component> lore = Arrays.asList(
                    net.kyori.adventure.text.Component.text("§7Item: §c" + order.getItemId()),
                    net.kyori.adventure.text.Component.text("§7Amount: §c" + order.getAmount()),
                    net.kyori.adventure.text.Component.text("§7Price per Unit: §c" + String.format("%.2f", order.getPricePerUnit())),
                    net.kyori.adventure.text.Component.text("§7Total Price: §c" + String.format("%.2f", order.getTotalPrice())),
                    net.kyori.adventure.text.Component.text("§7Status: §a" + (order.isActive() ? "Active" : "Inactive")),
                    net.kyori.adventure.text.Component.text(""),
                    net.kyori.adventure.text.Component.text("§eClick to cancel")
                );
                orderMeta.lore(lore);
                orderItem.setItemMeta(orderMeta);
            }
            inventory.setItem(slot, orderItem);
            
            slot += 2; // Skip every other slot for better layout
        }
    }
    
    private void setupNavigation() {
        // Close button
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        if (closeMeta != null) {
            closeMeta.displayName(net.kyori.adventure.text.Component.text("§cClose"));
            closeItem.setItemMeta(closeMeta);
        }
        inventory.setItem(49, closeItem);
    }
}