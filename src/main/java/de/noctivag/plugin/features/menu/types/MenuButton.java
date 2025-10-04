package de.noctivag.plugin.features.menu.types;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;

import java.util.List;

/**
 * Represents a menu button configuration
 */
public class MenuButton {
    
    private final Material material;
    private final String displayName;
    private final List<String> lore;
    private final String action;
    private final boolean clickable;
    
    public MenuButton(Material material, String displayName, List<String> lore, String action, boolean clickable) {
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.action = action;
        this.clickable = clickable;
    }
    
    public Material getMaterial() {
        return material;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public List<String> getLore() {
        return lore;
    }
    
    public String getAction() {
        return action;
    }
    
    public boolean isClickable() {
        return clickable;
    }
    
    /**
     * Create a filler button (non-clickable decorative item)
     */
    public static MenuButton createFiller(Material material, String displayName) {
        return new MenuButton(material, displayName, null, null, false);
    }
    
    /**
     * Create a functional button
     */
    public static MenuButton createButton(Material material, String displayName, List<String> lore, String action) {
        return new MenuButton(material, displayName, lore, action, true);
    }
    
    @Override
    public String toString() {
        return "MenuButton{" +
                "material=" + material +
                ", displayName='" + displayName + '\'' +
                ", action='" + action + '\'' +
                ", clickable=" + clickable +
                '}';
    }
}
