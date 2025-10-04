package de.noctivag.skyblock.accessories;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.talismans.AdvancedTalismanSystem;
import de.noctivag.skyblock.commands.AccessoryCommands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * AccessoryPluginIntegration - Main integration class for all accessory systems
 *
 * This class initializes and integrates all accessory-related systems:
 * - AccessoryBagSystem
 * - EnrichmentSystem
 * - AccessoryIntegrationSystem
 * - Command registration
 */
public class AccessoryPluginIntegration {

    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;

    private AccessoryBagSystem accessoryBagSystem;
    private EnrichmentSystem enrichmentSystem;
    private AccessoryIntegrationSystem integrationSystem;
    private AdvancedTalismanSystem talismanSystem;
    private AccessoryCommands accessoryCommands;

    public AccessoryPluginIntegration(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
    }

    public void initialize() {
        // Initialize talisman system first
        talismanSystem = new AdvancedTalismanSystem(plugin, databaseManager);

        // Initialize accessory systems
        accessoryBagSystem = new AccessoryBagSystem(plugin, databaseManager);
        enrichmentSystem = new EnrichmentSystem(plugin, databaseManager);

        // Initialize integration system
        integrationSystem = new AccessoryIntegrationSystem(
            plugin, databaseManager, accessoryBagSystem, enrichmentSystem, talismanSystem
        );

        // Initialize commands
        accessoryCommands = new AccessoryCommands(
            accessoryBagSystem, enrichmentSystem, integrationSystem, talismanSystem
        );

        // Register commands
        registerCommands();

        plugin.getLogger().info("Accessory systems initialized successfully!");
    }

    private void registerCommands() {
        JavaSkyblockPlugin  (JavaPlugin) Bukkit.getPluginManager().getPlugin(plugin.getName());
        if (javaPlugin != null) {
            var c1 = javaPlugin.getCommand("accessorybag");
            if (c1 != null) c1.setExecutor(accessoryCommands); else plugin.getLogger().warning("Command 'accessorybag' not defined in plugin.yml");
            var c2 = javaPlugin.getCommand("talismans");
            if (c2 != null) c2.setExecutor(accessoryCommands); else plugin.getLogger().warning("Command 'talismans' not defined in plugin.yml");
            var c3 = javaPlugin.getCommand("enrich");
            if (c3 != null) c3.setExecutor(accessoryCommands); else plugin.getLogger().warning("Command 'enrich' not defined in plugin.yml");
            var c4 = javaPlugin.getCommand("magicalpower");
            if (c4 != null) c4.setExecutor(accessoryCommands); else plugin.getLogger().warning("Command 'magicalpower' not defined in plugin.yml");
            var c5 = javaPlugin.getCommand("accessorystats");
            if (c5 != null) c5.setExecutor(accessoryCommands); else plugin.getLogger().warning("Command 'accessorystats' not defined in plugin.yml");
        }
    }

    public AccessoryBagSystem getAccessoryBagSystem() {
        return accessoryBagSystem;
    }

    public EnrichmentSystem getEnrichmentSystem() {
        return enrichmentSystem;
    }

    public AccessoryIntegrationSystem getIntegrationSystem() {
        return integrationSystem;
    }

    public AdvancedTalismanSystem getTalismanSystem() {
        return talismanSystem;
    }

    public void shutdown() {
        plugin.getLogger().info("Shutting down accessory systems...");
        // Cleanup if needed
    }
}
