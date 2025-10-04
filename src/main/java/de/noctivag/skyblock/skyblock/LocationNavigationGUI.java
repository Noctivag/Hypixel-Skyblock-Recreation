package de.noctivag.skyblock.skyblock;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Location Navigation GUI - Vollständige Location-Navigation
 * 
 * Features:
 * - Alle SkyBlock Locations anzeigen
 * - Location-Requirements prüfen
 * - Location-Features anzeigen
 * - Teleportation zu Locations
 * - Location-Statistiken
 */
public class LocationNavigationGUI implements Listener {
    
    private final SkyblockPlugin plugin;
    private final SkyBlockLocationSystem locationSystem;
    
    public LocationNavigationGUI(SkyblockPlugin plugin, SkyBlockLocationSystem locationSystem) {
        this.plugin = plugin;
        this.locationSystem = locationSystem;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().title().toString();
        
        if (title.contains("Locations")) {
            event.setCancelled(true);
            handleLocationGUIClick(player, event.getSlot());
        }
    }
    
    private void handleLocationGUIClick(Player player, int slot) {
        switch (slot) {
            case 49:
                player.closeInventory();
                break;
            case 53:
                openLocationStatisticsGUI(player);
                break;
        }
        
        // Handle location-specific clicks
        if (slot >= 0 && slot < 45) {
            // This would handle teleportation to specific locations
            player.sendMessage("§aTeleportation to location would be implemented here!");
        }
    }
    
    public void openLocationGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lSkyBlock Locations"));
        
        // Add location categories
        addGUIItem(gui, 10, Material.GRASS_BLOCK, "§a§lPrivate Island", "§7Your personal island");
        addGUIItem(gui, 11, Material.NETHER_STAR, "§6§lThe Hub", "§7Central area with NPCs and shops");
        addGUIItem(gui, 12, Material.WHEAT, "§a§lFarming Areas", "§7The Barn and Mushroom Desert");
        addGUIItem(gui, 13, Material.OAK_LOG, "§2§lForaging Areas", "§7The Park");
        addGUIItem(gui, 14, Material.SPIDER_EYE, "§c§lCombat Areas", "§7Spider's Den, The End, Crimson Isle");
        addGUIItem(gui, 15, Material.DIAMOND_PICKAXE, "§6§lMining Areas", "§7Gold Mine, Deep Caverns, Dwarven Mines, Crystal Hollows");
        addGUIItem(gui, 16, Material.SNOW_BLOCK, "§f§lSpecial Areas", "§7Jerry's Workshop, Dungeon Hub, Rift Dimension");
        addGUIItem(gui, 17, Material.LILY_PAD, "§b§lFishing Areas", "§7Backwater Bayou");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the locations menu.");
        addGUIItem(gui, 53, Material.BOOK, "§b§lStatistics", "§7View location statistics.");
        
        player.openInventory(gui);
        player.sendMessage("§aLocation Navigation GUI geöffnet!");
    }
    
    public void openFarmingLocationsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lFarming Locations"));
        
        // Add farming locations
        addLocationItem(gui, 10, "the_barn", player);
        addLocationItem(gui, 11, "mushroom_desert", player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the farming locations menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to locations menu.");
        
        player.openInventory(gui);
    }
    
    public void openCombatLocationsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§c§lCombat Locations"));
        
        // Add combat locations
        addLocationItem(gui, 10, "spiders_den", player);
        addLocationItem(gui, 11, "the_end", player);
        addLocationItem(gui, 12, "crimson_isle", player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the combat locations menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to locations menu.");
        
        player.openInventory(gui);
    }
    
    public void openMiningLocationsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lMining Locations"));
        
        // Add mining locations
        addLocationItem(gui, 10, "gold_mine", player);
        addLocationItem(gui, 11, "deep_caverns", player);
        addLocationItem(gui, 12, "dwarven_mines", player);
        addLocationItem(gui, 13, "crystal_hollows", player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the mining locations menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to locations menu.");
        
        player.openInventory(gui);
    }
    
    public void openSpecialLocationsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§f§lSpecial Locations"));
        
        // Add special locations
        addLocationItem(gui, 10, "jerrys_workshop", player);
        addLocationItem(gui, 11, "dungeon_hub", player);
        addLocationItem(gui, 12, "rift_dimension", player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the special locations menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to locations menu.");
        
        player.openInventory(gui);
    }
    
    public void openLocationStatisticsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§b§lLocation Statistics"));
        
        // Add statistics
        addStatisticsItem(gui, 22, player);
        
        // Add discovered locations
        addDiscoveredLocationsItem(gui, 31, player);
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the statistics menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lBack", "§7Go back to locations menu.");
        
        player.openInventory(gui);
    }
    
    private void addLocationItem(Inventory gui, int slot, String locationId, Player player) {
        SkyBlockLocationSystem.SkyBlockLocation location = locationSystem.getLocation(locationId);
        if (location == null) return;
        
        ItemStack item = new ItemStack(location.getIcon());
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text(location.getDisplayName()));
            
            List<String> lore = new ArrayList<>();
            lore.add(location.getDescription());
            lore.add("");
            
            // Check if player can access
            boolean canAccess = locationSystem.canAccessLocation(player, location);
            lore.add("§7Access: " + (canAccess ? "§a✓ Available" : "§c✗ Locked"));
            
            // Show requirements
            lore.add("§7Requirements:");
            for (SkyBlockLocationSystem.LocationRequirement requirement : location.getRequirements()) {
                String status = requirement.isMet(player) ? "§a✓" : "§c✗";
                lore.add("  " + status + " §7" + requirement.getDescription());
            }
            
            lore.add("");
            lore.add("§7Type: " + location.getType().getDisplayName());
            lore.add("");
            lore.add("§7Features:");
            for (SkyBlockLocationSystem.LocationFeature feature : location.getFeatureDetails()) {
                lore.add("  " + feature.getDisplayName() + " §7- " + feature.getDescription());
            }
            
            lore.add("");
            lore.add("§eClick to teleport!");
            
            meta.lore(lore.stream().map(Component::text).toList());
            item.setItemMeta(meta);
        }
        
        gui.setItem(slot, item);
    }
    
    private void addStatisticsItem(Inventory gui, int slot, Player player) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§6§lLocation Statistics"));
            
            List<String> lore = new ArrayList<>();
            
            Set<String> discoveredLocations = locationSystem.getDiscoveredLocations(player.getUniqueId());
            Collection<SkyBlockLocationSystem.SkyBlockLocation> allLocations = locationSystem.getAllLocations();
            Collection<SkyBlockLocationSystem.SkyBlockLocation> accessibleLocations = locationSystem.getAccessibleLocations(player);
            
            lore.add("§7Total Locations: §e" + allLocations.size());
            lore.add("§7Discovered: §a" + discoveredLocations.size());
            lore.add("§7Accessible: §b" + accessibleLocations.size());
            lore.add("§7Completion: §d" + String.format("%.1f", (double) discoveredLocations.size() / allLocations.size() * 100) + "%");
            lore.add("");
            lore.add("§7Current Location: §e" + getCurrentLocationName(player));
            lore.add("");
            lore.add("§7Location Types:");
            lore.add("  §aPrivate: §7" + countLocationsByType(allLocations, SkyBlockLocationSystem.LocationType.PRIVATE));
            lore.add("  §6Public: §7" + countLocationsByType(allLocations, SkyBlockLocationSystem.LocationType.PUBLIC));
            lore.add("  §eEvent: §7" + countLocationsByType(allLocations, SkyBlockLocationSystem.LocationType.EVENT));
            lore.add("  §dSpecial: §7" + countLocationsByType(allLocations, SkyBlockLocationSystem.LocationType.SPECIAL));
            
            meta.lore(lore.stream().map(Component::text).toList());
            item.setItemMeta(meta);
        }
        
        gui.setItem(slot, item);
    }
    
    private void addDiscoveredLocationsItem(Inventory gui, int slot, Player player) {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("§b§lDiscovered Locations"));
            
            List<String> lore = new ArrayList<>();
            
            Set<String> discoveredLocations = locationSystem.getDiscoveredLocations(player.getUniqueId());
            
            if (discoveredLocations.isEmpty()) {
                lore.add("§7No locations discovered yet!");
                lore.add("§7Explore the world to discover new locations.");
            } else {
                lore.add("§7Discovered Locations:");
                for (String locationId : discoveredLocations) {
                    SkyBlockLocationSystem.SkyBlockLocation location = locationSystem.getLocation(locationId);
                    if (location != null) {
                        lore.add("  §a✓ §7" + location.getDisplayName());
                    }
                }
            }
            
            meta.lore(lore.stream().map(Component::text).toList());
            item.setItemMeta(meta);
        }
        
        gui.setItem(slot, item);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(Component.text(description)));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    // Helper methods
    private String getCurrentLocationName(Player player) {
        SkyBlockLocationSystem.SkyBlockLocation currentLocation = locationSystem.getPlayerLocation(player.getUniqueId());
        return currentLocation != null ? currentLocation.getDisplayName() : "§7Unknown";
    }
    
    private int countLocationsByType(Collection<SkyBlockLocationSystem.SkyBlockLocation> locations, SkyBlockLocationSystem.LocationType type) {
        return (int) locations.stream().filter(location -> location.getType() == type).count();
    }
}
