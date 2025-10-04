package de.noctivag.plugin.features.menu.types;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for a menu
 */
public class MenuConfig {
    
    private final MenuType menuType;
    private final String title;
    private final int size;
    private final Map<Integer, MenuButton> buttons;
    
    public MenuConfig(MenuType menuType, String title, int size) {
        this.menuType = menuType;
        this.title = title;
        this.size = size;
        this.buttons = new HashMap<>();
    }
    
    public void setButton(int slot, MenuButton button) {
        buttons.put(slot, button);
    }
    
    public MenuButton getButton(int slot) {
        return buttons.get(slot);
    }
    
    public MenuType getMenuType() {
        return menuType;
    }
    
    public String getTitle() {
        return title;
    }
    
    public int getSize() {
        return size;
    }
    
    public Map<Integer, MenuButton> getButtons() {
        return new HashMap<>(buttons);
    }
    
    @Override
    public String toString() {
        return "MenuConfig{" +
                "menuType=" + menuType +
                ", title='" + title + '\'' +
                ", size=" + size +
                ", buttons=" + buttons.size() +
                '}';
    }
}
