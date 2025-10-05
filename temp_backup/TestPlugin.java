package de.noctivag.skyblock;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;

public class TestPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        getLogger().info("Test SkyblockPlugin enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("Test SkyblockPlugin disabled!");
    }
}
