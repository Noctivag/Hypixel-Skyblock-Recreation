package de.noctivag.skyblock.features.menu.config;
import org.bukkit.inventory.ItemStack;

import org.bukkit.Material;
import java.util.List;
import java.util.Map;

/**
 * Menu Configuration Class
 */
public class MenuConfig {
    private final String id;
    private final String name;
    private final String displayName;
    private final Material icon;
    private final int size;
    private final List<String> description;
    private final Map<String, Object> properties;
    private final List<MenuItem> items;
    
    public MenuConfig(String id, String name, String displayName, Material icon, int size,
                     List<String> description, Map<String, Object> properties, List<MenuItem> items) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.icon = icon;
        this.size = size;
        this.description = description;
        this.properties = properties;
        this.items = items;
    }
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public Material getIcon() { return icon; }
    public int getSize() { return size; }
    public List<String> getDescription() { return description; }
    public Map<String, Object> getProperties() { return properties; }
    public List<MenuItem> getItems() { return items; }
    
    public static class MenuItem {
        private final int slot;
        private final String name;
        private final Material material;
        private final List<String> lore;
        private final String action;
        
        public MenuItem(int slot, String name, Material material, List<String> lore, String action) {
            this.slot = slot;
            this.name = name;
            this.material = material;
            this.lore = lore;
            this.action = action;
        }
        
        public int getSlot() { return slot; }
        public String getName() { return name; }
        public Material getMaterial() { return material; }
        public List<String> getLore() { return lore; }
        public String getAction() { return action; }
    }
}
