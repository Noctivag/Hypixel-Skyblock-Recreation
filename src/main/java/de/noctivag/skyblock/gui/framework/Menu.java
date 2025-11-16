package de.noctivag.skyblock.gui.framework;

import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Erweiterte abstrakte Menu-Basisklasse für einheitliche GUI-Implementierung
 * Bietet standardisierte Funktionalität für alle Skyblock-Menüs
 */
public abstract class Menu implements InventoryHolder, Listener {
    
    protected final SkyblockPlugin plugin;
    protected final Player player;
    protected Inventory inventory;
    protected String title;
    protected int size;
    
    // Standard-Farben für einheitliches Design
    protected static final Material BORDER_MATERIAL = Material.BLACK_STAINED_GLASS_PANE;
    protected static final Material BUTTON_MATERIAL = Material.LIGHT_GRAY_STAINED_GLASS_PANE;
    protected static final Material CONFIRM_MATERIAL = Material.LIME_STAINED_GLASS_PANE;
    protected static final Material CANCEL_MATERIAL = Material.RED_STAINED_GLASS_PANE;
    protected static final Material SPECIAL_MATERIAL = Material.YELLOW_STAINED_GLASS_PANE;
    
    public Menu(SkyblockPlugin plugin, Player player, String title, int size) {
        this.plugin = plugin;
        this.player = player;
        this.title = title;
        this.size = size;
        this.inventory = Bukkit.createInventory(this, size, title);
        
        // Registriere Event-Listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Öffnet das Menü für den Spieler
     */
    public void open() {
        setupItems();
        player.openInventory(inventory);
    }
    
    /**
     * Öffnet das Menü für einen spezifischen Spieler
     */
    public void open(Player targetPlayer) {
        setupItems();
        targetPlayer.openInventory(inventory);
    }
    
    /**
     * Schließt das Menü
     */
    public void close() {
        player.closeInventory();
    }
    
    /**
     * Erstellt ein Item mit Namen und Lore
     */
    protected ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore.length > 0) {
                meta.lore(Arrays.asList(lore).stream().map(Component::text).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    
    /**
     * Erstellt ein Item mit Namen und Lore-Liste
     */
    protected ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            if (lore != null && !lore.isEmpty()) {
                meta.lore(lore.stream().map(Component::text).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    
    /**
     * Füllt die Ränder des Inventars mit Border-Items
     */
    protected void fillBorders() {
        ItemStack border = ItemBuilder.border().build();
        
        // Obere Reihe (0-8)
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, border);
        }
        
        // Untere Reihe (45-53 für 54er Inventar, 36-44 für 45er, etc.)
        int bottomStart = size - 9;
        for (int i = bottomStart; i < size; i++) {
            inventory.setItem(i, border);
        }
        
        // Linke und rechte Spalten
        for (int i = 9; i < bottomStart; i += 9) {
            inventory.setItem(i, border); // Links
            inventory.setItem(i + 8, border); // Rechts
        }
    }
    
    /**
     * Füllt alle leeren Slots mit einem Item
     */
    protected void fillEmptySlots(Material material, String name) {
        ItemStack filler = createItem(material, name);
        for (int i = 0; i < size; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }
    }
    
    /**
     * Füllt alle leeren Slots mit einem Filler-Item
     */
    protected void fillEmptySlots() {
        ItemStack filler = ItemBuilder.filler().build();
        for (int i = 0; i < size; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }
    }
    
    /**
     * Erstellt ein Item mit ItemBuilder
     */
    protected ItemBuilder createBuilder(Material material) {
        return ItemBuilder.of(material, plugin);
    }
    
    /**
     * Erstellt ein Item mit ItemBuilder und Amount
     */
    protected ItemBuilder createBuilder(Material material, int amount) {
        return ItemBuilder.of(material, amount, plugin);
    }
    
    /**
     * Setzt ein Item an einer bestimmten Position
     */
    protected void setItem(int slot, ItemBuilder builder) {
        inventory.setItem(slot, builder.build());
    }
    
    /**
     * Setzt ein Item an einer bestimmten Position (ohne Rarity)
     */
    protected void setItemSimple(int slot, Material material, String name, String... lore) {
        inventory.setItem(slot, createBuilder(material).name(name).lore(lore).build());
    }
    
    /**
     * Setzt ein Item an einer bestimmten Position mit Hypixel-Style
     */
    protected void setItem(int slot, Material material, String name, String rarity, String... lore) {
        ItemBuilder builder = createBuilder(material).name(name).lore(lore);
        
        switch (rarity.toLowerCase()) {
            case "common":
                builder.common(name, lore);
                break;
            case "uncommon":
                builder.uncommon(name, lore);
                break;
            case "rare":
                builder.rare(name, lore);
                break;
            case "epic":
                builder.epic(name, lore);
                break;
            case "legendary":
                builder.legendary(name, lore);
                break;
            case "mythic":
                builder.mythic(name, lore);
                break;
            case "special":
                builder.special(name, lore);
                break;
            default:
                builder.name(name).lore(lore);
                break;
        }
        
        inventory.setItem(slot, builder.build());
    }
    
    /**
     * Setzt einen Schließen-Button
     */
    protected void setCloseButton(int slot) {
        inventory.setItem(slot, ItemBuilder.closeButton().build());
    }
    
    /**
     * Setzt einen Zurück-Button
     */
    protected void setBackButton(int slot) {
        inventory.setItem(slot, ItemBuilder.backButton().build());
    }
    
    /**
     * Setzt einen Weiter-Button
     */
    protected void setNextButton(int slot) {
        inventory.setItem(slot, ItemBuilder.nextButton().build());
    }
    
    /**
     * Event-Handler für Inventory-Klicks
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(inventory)) return;
        if (!event.getWhoClicked().equals(player)) return;
        
        event.setCancelled(true);
        handleMenuClick(event);
    }
    
    /**
     * Event-Handler für Inventory-Schließen
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!event.getInventory().equals(inventory)) return;
        if (!event.getPlayer().equals(player)) return;
        
        handleMenuClose();
    }
    
    /**
     * Abstrakte Methode zum Setup der Menü-Items
     * Muss von jeder Unterklasse implementiert werden
     */
    public abstract void setupItems();
    
    /**
     * Abstrakte Methode zur Behandlung von Menü-Klicks
     * Muss von jeder Unterklasse implementiert werden
     */
    public abstract void handleMenuClick(InventoryClickEvent event);
    
    /**
     * Optionale Methode zur Behandlung des Menü-Schließens
     * Kann von Unterklassen überschrieben werden
     */
    protected void handleMenuClose() {
        // Standard-Implementierung: nichts zu tun
    }
    
    // Getter-Methoden
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public String getTitle() {
        return title;
    }
    
    public int getSize() {
        return size;
    }
}
