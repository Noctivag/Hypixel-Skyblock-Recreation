package de.noctivag.plugin;
import org.bukkit.inventory.ItemStack;

import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        getLogger().info("Test plugin enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("Test plugin disabled!");
    }
}
