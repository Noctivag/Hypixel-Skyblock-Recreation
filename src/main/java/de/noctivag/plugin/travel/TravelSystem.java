package de.noctivag.plugin.travel;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.data.DatabaseManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Travel System - Hypixel Skyblock Style
 * 
 * Features:
 * - Travel scroll creation
 * - Travel scroll usage
 * - Travel scroll management
 * - Travel scroll GUI
 * - Travel scroll trading
 */
public class TravelSystem {
    private final Plugin plugin;
    private final DatabaseManager databaseManager;
    private final Map<UUID, List<TravelScroll>> playerScrolls = new HashMap<>();

    public TravelSystem(Plugin plugin, DatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public boolean createTravelScroll(Player player, String name, String location) {
        TravelScroll scroll = new TravelScroll(name, location, player.getUniqueId());
        
        List<TravelScroll> scrolls = playerScrolls.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        scrolls.add(scroll);

        // Give scroll item to player
        ItemStack scrollItem = new ItemStack(Material.PAPER);
        ItemMeta meta = scrollItem.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§6§lTravel Scroll: " + name));
            meta.lore(Arrays.asList(
                Component.text("§7Location: §e" + location),
                Component.text("§7Right-click to use"),
                Component.text("§8Created by: " + player.getName())
            ));
            scrollItem.setItemMeta(meta);
        }
        
        player.getInventory().addItem(scrollItem);
        return true;
    }

    public boolean useTravelScroll(Player player, String scrollName) {
        List<TravelScroll> scrolls = playerScrolls.get(player.getUniqueId());
        if (scrolls == null) return false;

        for (TravelScroll scroll : scrolls) {
            if (scroll.getName().equalsIgnoreCase(scrollName)) {
                // Teleport player to location
                if (plugin.getLocationManager() != null) {
                    plugin.getLocationManager().teleportToLocation(player, scroll.getLocation());
                    return true;
                }
            }
        }
        return false;
    }

    public List<TravelScroll> getPlayerScrolls(Player player) {
        return playerScrolls.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

    public void openTravelGUI(Player player) {
        // Create travel GUI
        org.bukkit.inventory.Inventory gui = org.bukkit.Bukkit.createInventory(null, 54, Component.text("§6§lTravel System"));
        
        List<TravelScroll> scrolls = getPlayerScrolls(player);
        for (int i = 0; i < Math.min(scrolls.size(), 45); i++) {
            TravelScroll scroll = scrolls.get(i);
            ItemStack scrollItem = new ItemStack(Material.PAPER);
            ItemMeta meta = scrollItem.getItemMeta();
            if (meta != null) {
                meta.displayName(Component.text("§6§l" + scroll.getName()));
                meta.lore(Arrays.asList(
                    Component.text("§7Location: §e" + scroll.getLocation()),
                    Component.text("§eClick to use")
                ));
                scrollItem.setItemMeta(meta);
            }
            gui.setItem(i, scrollItem);
        }

        player.openInventory(gui);
    }

    public boolean giveTravelScroll(Player giver, Player receiver, String scrollName) {
        List<TravelScroll> giverScrolls = playerScrolls.get(giver.getUniqueId());
        if (giverScrolls == null) return false;

        for (TravelScroll scroll : giverScrolls) {
            if (scroll.getName().equalsIgnoreCase(scrollName)) {
                // Remove from giver
                giverScrolls.remove(scroll);
                
                // Add to receiver
                List<TravelScroll> receiverScrolls = playerScrolls.computeIfAbsent(receiver.getUniqueId(), k -> new ArrayList<>());
                receiverScrolls.add(scroll);
                
                return true;
            }
        }
        return false;
    }

    public boolean removeTravelScroll(Player player, String scrollName) {
        List<TravelScroll> scrolls = playerScrolls.get(player.getUniqueId());
        if (scrolls == null) return false;

        return scrolls.removeIf(scroll -> scroll.getName().equalsIgnoreCase(scrollName));
    }

    public static class TravelScroll {
        private final String name;
        private final String location;
        private final UUID creator;

        public TravelScroll(String name, String location, UUID creator) {
            this.name = name;
            this.location = location;
            this.creator = creator;
        }

        public String getName() { return name; }
        public String getLocation() { return location; }
        public UUID getCreator() { return creator; }
    }
}
