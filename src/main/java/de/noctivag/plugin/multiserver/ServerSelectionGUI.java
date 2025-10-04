package de.noctivag.plugin.multiserver;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * GUI für Server-Auswahl
 */
public class ServerSelectionGUI implements Listener {
    
    private final Plugin plugin;
    private final ServerSwitcher serverSwitcher;
    private final Map<UUID, Inventory> openGUIs = new HashMap<>();
    
    public ServerSelectionGUI(Plugin plugin, ServerSwitcher serverSwitcher) {
        this.plugin = plugin;
        this.serverSwitcher = serverSwitcher;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Öffnet die Server-Auswahl-GUI für einen Spieler
     */
    public void openServerSelection(Player player) {
        Inventory gui = createServerSelectionGUI(player);
        openGUIs.put(player.getUniqueId(), gui);
        player.openInventory(gui);
    }
    
    /**
     * Erstellt die Server-Auswahl-GUI
     */
    private Inventory createServerSelectionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lServer-Auswahl"));
        
        // TODO: Implement proper HypixelProxySystem interface
        // Map<String, ServerType> serverTypes = ((HypixelProxySystem) plugin.getHypixelProxySystem()).getServerTypes();
        Map<String, Object> serverTypes = new HashMap<>(); // Placeholder
        
        int slot = 0;
        
        // Haupt-Server
        addServerCategory(gui, slot, "§a§lHaupt-Server", Material.EMERALD_BLOCK, Arrays.asList("skyblock_hub"));
        slot += 9;
        
        // Public Islands
        List<String> publicIslands = Arrays.asList(
            "spiders_den", "the_end", "the_park", "gold_mine", "deep_caverns",
            "dwarven_mines", "crystal_hollows", "the_barn", "mushroom_desert",
            "blazing_fortress", "the_nether", "crimson_isle", "rift", "kuudra"
        );
        addServerCategory(gui, slot, "§e§lPublic Islands", Material.GRASS_BLOCK, publicIslands);
        slot += 9;
        
        // Persistente Spieler-Instanzen
        addServerCategory(gui, slot, "§b§lPersistente Instanzen", Material.DIAMOND_BLOCK, Arrays.asList("private_island", "garden"));
        slot += 9;
        
        // Dungeons
        List<String> dungeons = Arrays.asList(
            "catacombs_entrance", "catacombs_floor_1", "catacombs_floor_2", "catacombs_floor_3",
            "catacombs_floor_4", "catacombs_floor_5", "catacombs_floor_6", "catacombs_floor_7"
        );
        addServerCategory(gui, slot, "§c§lDungeons", Material.NETHER_BRICK, dungeons);
        slot += 9;
        
        // Master Mode Dungeons
        List<String> masterMode = Arrays.asList(
            "master_mode_floor_1", "master_mode_floor_2", "master_mode_floor_3", "master_mode_floor_4",
            "master_mode_floor_5", "master_mode_floor_6", "master_mode_floor_7"
        );
        addServerCategory(gui, slot, "§4§lMaster Mode", Material.NETHERITE_BLOCK, masterMode);
        
        return gui;
    }
    
    /**
     * Fügt eine Server-Kategorie zur GUI hinzu
     */
    private void addServerCategory(Inventory gui, int startSlot, String categoryName, Material categoryMaterial, List<String> serverTypes) {
        // Kategorie-Header
        ItemStack categoryItem = new ItemStack(categoryMaterial);
        ItemMeta categoryMeta = categoryItem.getItemMeta();
        categoryMeta.displayName(Component.text(categoryName));
        categoryMeta.lore(Arrays.asList(
            Component.text("§7Klicke auf einen Server"),
            Component.text("§7um dorthin zu wechseln")
        ));
        categoryItem.setItemMeta(categoryMeta);
        gui.setItem(startSlot, categoryItem);
        
        // Server-Items
        int slot = startSlot + 1;
        for (String serverType : serverTypes) {
            if (slot >= startSlot + 9) break; // Nicht über Kategorie-Grenze hinaus
            
            // TODO: Implement proper HypixelProxySystem interface
            // ServerType type = ((HypixelProxySystem) plugin.getHypixelProxySystem()).getServerTypes().get(serverType);
            Object type = null; // Placeholder
            if (type != null) {
                // TODO: Implement proper ServerType interface
                // ItemStack serverItem = createServerItem((ServerType) type);
                // gui.setItem(slot, serverItem);
                slot++;
            }
        }
    }
    
    /**
     * Erstellt ein Server-Item
     */
    private ItemStack createServerItem(ServerType serverType) {
        Material material = getServerMaterial(serverType);
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(Component.text("§f" + serverType.getDisplayName()));
        
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("§7Typ: §f" + getServerTypeDescription(serverType)));
        lore.add(Component.text("§7Max. Spieler: §f" + serverType.getMaxPlayers()));
        
        if (serverType.isNeverShutdown()) {
            lore.add(Component.text("§a§lPermanent Online"));
        } else if (serverType.hasRestartCycle()) {
            lore.add(Component.text("§e§lNeustart alle 4h"));
        } else if (serverType.isPlayerPersistent()) {
            lore.add(Component.text("§b§lPersistent"));
        } else {
            lore.add(Component.text("§c§lTemporär"));
        }
        
        lore.add(Component.text(""));
        lore.add(Component.text("§aKlicke zum Wechseln"));
        
        meta.lore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
    
    /**
     * Gibt das Material für einen Server-Typ zurück
     */
    private Material getServerMaterial(ServerType serverType) {
        String typeId = serverType.getTypeId();
        
        if (typeId.equals("skyblock_hub")) {
            return Material.EMERALD_BLOCK;
        } else if (typeId.equals("private_island")) {
            return Material.DIAMOND_BLOCK;
        } else if (typeId.equals("garden")) {
            return Material.WHEAT;
        } else if (typeId.startsWith("catacombs_")) {
            return Material.NETHER_BRICK;
        } else if (typeId.startsWith("master_mode_")) {
            return Material.NETHERITE_BLOCK;
        } else {
            return Material.GRASS_BLOCK;
        }
    }
    
    /**
     * Gibt die Beschreibung für einen Server-Typ zurück
     */
    private String getServerTypeDescription(ServerType serverType) {
        String typeId = serverType.getTypeId();
        
        if (typeId.equals("skyblock_hub")) {
            return "Haupt-Server";
        } else if (typeId.equals("private_island")) {
            return "Private Insel";
        } else if (typeId.equals("garden")) {
            return "Garten";
        } else if (typeId.startsWith("catacombs_")) {
            return "Dungeon";
        } else if (typeId.startsWith("master_mode_")) {
            return "Master Mode";
        } else {
            return "Public Island";
        }
    }
    
    /**
     * Event-Handler für GUI-Klicks
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        
        if (clickedInventory == null) return;
        
        // Prüfe ob es unsere GUI ist
        if (!openGUIs.containsKey(player.getUniqueId())) return;
        if (!clickedInventory.equals(openGUIs.get(player.getUniqueId()))) return;
        
        event.setCancelled(true);
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Prüfe ob es ein Server-Item ist
        String serverType = getServerTypeFromItem(clickedItem);
        if (serverType != null) {
            // Schließe GUI
            player.closeInventory();
            openGUIs.remove(player.getUniqueId());
            
            // Wechsle Server
            serverSwitcher.switchPlayerToServer(player, serverType)
                .thenAccept(success -> {
                    if (!success) {
                        player.sendMessage(Component.text("§cFehler beim Wechseln zu " + serverType + "!"));
                    }
                });
        }
    }
    
    /**
     * Extrahiert den Server-Typ aus einem Item
     */
    private String getServerTypeFromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) return null;
        
        String displayName = meta.displayName().toString();
        
        // TODO: Implement proper HypixelProxySystem interface
        // Map<String, ServerType> serverTypes = ((HypixelProxySystem) plugin.getHypixelProxySystem()).getServerTypes();
        // for (Map.Entry<String, ServerType> entry : serverTypes.entrySet()) {
        //     if (displayName.contains(entry.getValue().getDisplayName())) {
        //         return entry.getKey();
        //     }
        // }
        return null; // Placeholder
    }
    
    /**
     * Schließt die GUI für einen Spieler
     */
    public void closeGUI(Player player) {
        openGUIs.remove(player.getUniqueId());
        if (player.getOpenInventory() != null) {
            player.closeInventory();
        }
    }
    
    /**
     * Bereinigt alle offenen GUIs
     */
    public void cleanup() {
        for (UUID playerId : new HashSet<>(openGUIs.keySet())) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                closeGUI(player);
            }
        }
        openGUIs.clear();
    }
}
