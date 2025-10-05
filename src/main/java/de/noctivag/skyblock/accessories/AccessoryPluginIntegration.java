package de.noctivag.skyblock.accessories;

import org.bukkit.plugin.java.JavaPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import de.noctivag.skyblock.talismans.AdvancedTalismanSystem;
import de.noctivag.skyblock.commands.AccessoryCommands;
import org.bukkit.Bukkit;
import de.noctivag.skyblock.SkyblockPlugin;

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

    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;

    private AccessoryBagSystem accessoryBagSystem;
    private EnrichmentSystem enrichmentSystem;
    private AccessoryIntegrationSystem integrationSystem;
    private AdvancedTalismanSystem talismanSystem;
    private AccessoryCommands accessoryCommands;

    public AccessoryPluginIntegration(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
    }

    public void initialize() {
        // Initialize talisman system first
        talismanSystem = new AdvancedTalismanSystem(SkyblockPlugin, databaseManager);

        // Initialize accessory systems
        accessoryBagSystem = new AccessoryBagSystem(SkyblockPlugin, databaseManager);
        enrichmentSystem = new EnrichmentSystem(SkyblockPlugin, databaseManager);

        // Initialize integration system
        integrationSystem = new AccessoryIntegrationSystem(
            SkyblockPlugin, databaseManager, accessoryBagSystem, enrichmentSystem, talismanSystem
        );

        // Initialize commands
        accessoryCommands = new AccessoryCommands(
            accessoryBagSystem, enrichmentSystem, integrationSystem, talismanSystem
        );

        // Register commands
        registerCommands();

        SkyblockPlugin.getLogger().info("Accessory systems initialized successfully!");
    }

    private void registerCommands() {
        JavaPlugin javaPlugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin(SkyblockPlugin.getName());
        if (javaPlugin != null) {
            var c1 = javaPlugin.getCommand("accessorybag");
            if (c1 != null) c1.setExecutor(accessoryCommands); else SkyblockPlugin.getLogger().warning("Command 'accessorybag' not defined in SkyblockPlugin.yml");
            var c2 = javaPlugin.getCommand("talismans");
            if (c2 != null) c2.setExecutor(accessoryCommands); else SkyblockPlugin.getLogger().warning("Command 'talismans' not defined in SkyblockPlugin.yml");
            var c3 = javaPlugin.getCommand("enrich");
            if (c3 != null) c3.setExecutor(accessoryCommands); else SkyblockPlugin.getLogger().warning("Command 'enrich' not defined in SkyblockPlugin.yml");
            var c4 = javaPlugin.getCommand("magicalpower");
            if (c4 != null) c4.setExecutor(accessoryCommands); else SkyblockPlugin.getLogger().warning("Command 'magicalpower' not defined in SkyblockPlugin.yml");
            var c5 = javaPlugin.getCommand("accessorystats");
            if (c5 != null) c5.setExecutor(accessoryCommands); else SkyblockPlugin.getLogger().warning("Command 'accessorystats' not defined in SkyblockPlugin.yml");
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
        SkyblockPlugin.getLogger().info("Shutting down accessory systems...");
        // Cleanup if needed
    }
}
