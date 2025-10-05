package de.noctivag.skyblock.gui.framework;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Abstrakte Basis-Klasse für alle GUI-Menüs
 * Implementiert ein einheitliches Framework für alle Inventar-basierten Interfaces
 */
public abstract class Menu implements InventoryHolder {
    
    protected final Player player;
    protected Inventory inventory;
    protected final int size;
    
    // Standard-Navigation-Slots
    protected static final int BACK_BUTTON_SLOT = 45;
    protected static final int CLOSE_BUTTON_SLOT = 53;
    
    // Standard-Border-Material
    protected static final Material BORDER_MATERIAL = Material.GRAY_STAINED_GLASS_PANE;
    
    /**
     * Konstruktor für das Menu
     * @param player Der Spieler, für den das Menu erstellt wird
     * @param size Die Größe des Inventars (muss durch 9 teilbar sein)
     */
    public Menu(Player player, int size) {
        this.player = player;
        this.size = size;
        this.inventory = Bukkit.createInventory(this, size, getMenuTitle());
    }
    
    /**
     * Öffnet das Menu für den Spieler
     */
    public void open() {
        setMenuItems();
        player.openInventory(inventory);
    }
    
    /**
     * Abstrakte Methode, die von jeder Unterklasse implementiert werden muss
     * Hier werden alle Menu-Items gesetzt
     */
    public abstract void setMenuItems();
    
    /**
     * Gibt den Titel des Menüs zurück
     * @return Menu-Titel
     */
    public abstract String getMenuTitle();
    
    /**
     * Füllt den Rahmen des Inventars mit Platzhalter-Items
     * @param material Das Material für die Border-Items
     */
    protected void fillBorderWith(Material material) {
        ItemStack borderItem = createItem(material, " ", null);
        
        // Obere Reihe
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, borderItem);
        }
        
        // Untere Reihe
        for (int i = size - 9; i < size; i++) {
            inventory.setItem(i, borderItem);
        }
        
        // Linke Spalte
        for (int i = 9; i < size - 9; i += 9) {
            inventory.setItem(i, borderItem);
        }
        
        // Rechte Spalte
        for (int i = 17; i < size - 9; i += 9) {
            inventory.setItem(i, borderItem);
        }
    }
    
    /**
     * Fügt Standard-Navigation-Buttons hinzu
     */
    protected void addNavigationButtons() {
        // Zurück-Button (Player Head)
        ItemStack backButton = createPlayerHead("MHF_ArrowLeft", 
            Component.text("§e← Zurück").color(NamedTextColor.YELLOW),
            Arrays.asList("§7Klicke, um zurückzugehen"));
        inventory.setItem(BACK_BUTTON_SLOT, backButton);
        
        // Schließen-Button (Barrier)
        ItemStack closeButton = createItem(Material.BARRIER,
            Component.text("§c✖ Schließen").color(NamedTextColor.RED),
            Arrays.asList("§7Klicke, um das Menu zu schließen"));
        inventory.setItem(CLOSE_BUTTON_SLOT, closeButton);
    }
    
    /**
     * Erstellt ein ItemStack mit Namen und Lore
     * @param material Das Material des Items
     * @param name Der Name des Items
     * @param lore Die Lore des Items
     * @return Erstelltes ItemStack
     */
    protected ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            if (name != null && !name.isEmpty()) {
                meta.displayName(Component.text(name));
            }
            
            if (lore != null && !lore.isEmpty()) {
                meta.lore(lore.stream().map(Component::text).toList());
            }
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Erstellt ein ItemStack mit Component-Namen und Lore
     * @param material Das Material des Items
     * @param name Der Component-Name des Items
     * @param lore Die Lore des Items
     * @return Erstelltes ItemStack
     */
    protected ItemStack createItem(Material material, Component name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            if (name != null) {
                meta.displayName(name);
            }
            
            if (lore != null && !lore.isEmpty()) {
                meta.lore(lore.stream().map(Component::text).toList());
            }
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Erstellt einen Player Head mit Namen und Lore
     * @param texture Der Texture-String für den Kopf
     * @param name Der Name des Items
     * @param lore Die Lore des Items
     * @return Erstelltes ItemStack mit Player Head
     */
    protected ItemStack createPlayerHead(String texture, Component name, List<String> lore) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        
        if (meta != null) {
            meta.displayName(name);
            
            if (lore != null && !lore.isEmpty()) {
                meta.lore(lore.stream().map(Component::text).toList());
            }
            
            // Setze den Texture (vereinfacht - in der echten Implementierung würde man Base64 verwenden)
            meta.setOwner(texture);
        }
        
        head.setItemMeta(meta);
        return head;
    }
    
    /**
     * Erstellt einen Fortschrittsbalken aus Unicode-Zeichen
     * @param current Der aktuelle Wert
     * @param max Der maximale Wert
     * @param length Die Länge des Balkens
     * @return Fortschrittsbalken als String
     */
    protected String createProgressBar(int current, int max, int length) {
        if (max <= 0) return "§7" + "░".repeat(length);
        
        double percentage = (double) current / max;
        int filled = (int) (percentage * length);
        int empty = length - filled;
        
        return "§a" + "█".repeat(filled) + "§7" + "░".repeat(empty);
    }
    
    /**
     * Formatiert eine Zahl mit Kommas
     * @param number Die zu formatierende Zahl
     * @return Formatierte Zahl als String
     */
    protected String formatNumber(long number) {
        return String.format("%,d", number);
    }
    
    /**
     * Formatiert eine Zahl mit Kommas
     * @param number Die zu formatierende Zahl
     * @return Formatierte Zahl als String
     */
    protected String formatNumber(double number) {
        if (number == (long) number) {
            return formatNumber((long) number);
        } else {
            return String.format("%,.2f", number);
        }
    }
    
    /**
     * Prüft ob ein Slot ein Navigation-Button ist
     * @param slot Der zu prüfende Slot
     * @return true wenn es ein Navigation-Button ist
     */
    public boolean isNavigationSlot(int slot) {
        return slot == BACK_BUTTON_SLOT || slot == CLOSE_BUTTON_SLOT;
    }
    
    /**
     * Behandelt Klicks auf Navigation-Buttons
     * @param slot Der geklickte Slot
     * @return true wenn der Klick behandelt wurde
     */
    public boolean handleNavigationClick(int slot) {
        if (slot == BACK_BUTTON_SLOT) {
            handleBackClick();
            return true;
        } else if (slot == CLOSE_BUTTON_SLOT) {
            handleCloseClick();
            return true;
        }
        return false;
    }
    
    /**
     * Behandelt Klicks auf den Zurück-Button
     */
    protected void handleBackClick() {
        // Standard-Implementierung: Schließe das Menu
        player.closeInventory();
    }
    
    /**
     * Behandelt Klicks auf den Schließen-Button
     */
    protected void handleCloseClick() {
        player.closeInventory();
    }
    
    // Getter-Methoden
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public int getSize() {
        return size;
    }
}
