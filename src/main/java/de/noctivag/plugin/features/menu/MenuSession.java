package de.noctivag.plugin.features.menu;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.features.menu.types.MenuType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Stack;

/**
 * Represents a player's menu session
 */
public class MenuSession {
    
    private final Player player;
    private final MenuType currentMenu;
    private final Inventory inventory;
    private final Stack<MenuType> menuHistory;
    
    public MenuSession(Player player, MenuType currentMenu, Inventory inventory) {
        this.player = player;
        this.currentMenu = currentMenu;
        this.inventory = inventory;
        this.menuHistory = new Stack<>();
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public MenuType getCurrentMenu() {
        return currentMenu;
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public Stack<MenuType> getMenuHistory() {
        return menuHistory;
    }
    
    public void pushMenu(MenuType menuType) {
        menuHistory.push(currentMenu);
    }
    
    public MenuType getPreviousMenu() {
        return menuHistory.isEmpty() ? null : menuHistory.peek();
    }
    
    public MenuType popMenu() {
        return menuHistory.isEmpty() ? null : menuHistory.pop();
    }
    
    public boolean hasPreviousMenu() {
        return !menuHistory.isEmpty();
    }
    
    public void close() {
        if (player.isOnline()) {
            player.closeInventory();
        }
    }
    
    @Override
    public String toString() {
        return "MenuSession{" +
                "player=" + player.getName() +
                ", currentMenu=" + currentMenu +
                ", historySize=" + menuHistory.size() +
                '}';
    }
}
