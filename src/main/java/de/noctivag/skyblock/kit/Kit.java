package de.noctivag.skyblock.kit;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit {
    private final String name;
    private final List<ItemStack> items;
    private final long cooldown;
    private final String permission;

    public Kit(String name, List<ItemStack> items, long cooldown, String permission) {
        this.name = name;
        this.items = items;
        this.cooldown = cooldown;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public long getCooldown() {
        return cooldown;
    }

    public String getPermission() {
        return permission;
    }
}
