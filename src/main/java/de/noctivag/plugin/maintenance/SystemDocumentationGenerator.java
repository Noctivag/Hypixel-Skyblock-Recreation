package de.noctivag.plugin.maintenance;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * SystemDocumentationGenerator - Generiert automatische System-Dokumentation
 * 
 * Features:
 * - Auto-generierte System-Docs
 * - API-Dokumentation
 * - Command-Referenz
 * - GUI-Navigation-Guide
 */
public class SystemDocumentationGenerator {
    
    private final Plugin plugin;
    private final File docsFolder;
    
    public SystemDocumentationGenerator(Plugin plugin) {
        this.plugin = plugin;
        this.docsFolder = new File(plugin.getDataFolder(), "docs");
        if (!docsFolder.exists()) {
            docsFolder.mkdirs();
        }
    }
    
    /**
     * Generiert alle Dokumentationen
     */
    public void generateAllDocumentation() {
        plugin.getLogger().info("§e[Documentation] Generating system documentation...");
        
        generateSystemOverview();
        generateCommandReference();
        generateAPIDocumentation();
        generateGUIGuide();
        generateConfigurationGuide();
        generateTroubleshootingGuide();
        
        plugin.getLogger().info("§a[Documentation] Documentation generation completed!");
    }
    
    /**
     * Generiert System-Übersicht
     */
    private void generateSystemOverview() {
        try {
            File file = new File(docsFolder, "system-overview.md");
            FileWriter writer = new FileWriter(file);
            
            writer.write("# Plugin System Overview\n\n");
            writer.write("## Plugin Information\n");
            writer.write("- **Name**: " + plugin.getDescription().getName() + "\n");
            writer.write("- **Version**: " + plugin.getDescription().getVersion() + "\n");
            writer.write("- **Author**: " + plugin.getDescription().getAuthors() + "\n");
            writer.write("- **Description**: " + plugin.getDescription().getDescription() + "\n\n");
            
            writer.write("## Core Systems\n");
            writer.write("1. **Skills System** - 12 different skills with progression\n");
            writer.write("2. **Collections System** - 50+ collectible items\n");
            writer.write("3. **Minions System** - Automated resource production\n");
            writer.write("4. **Pets System** - Companion pets with abilities\n");
            writer.write("5. **Guilds System** - Player organizations\n");
            writer.write("6. **Economy System** - Multi-server economy\n");
            writer.write("7. **Dungeons System** - Multi-level dungeons\n");
            writer.write("8. **Slayers System** - Boss hunting system\n");
            writer.write("9. **Auction House** - Player trading\n");
            writer.write("10. **Bazaar System** - Instant trading\n\n");
            
            writer.write("## Database Tables\n");
            String[] tables = {
                "server_info", "player_profiles", "skyblock_islands", "island_members",
                "player_skills", "player_collections", "player_slayers", "player_minions",
                "player_pets", "auction_house", "bazaar_orders", "player_dungeons",
                "server_events", "guilds", "guild_members", "leaderboards"
            };
            
            for (String table : tables) {
                writer.write("- `" + table + "`\n");
            }
            
            writer.close();
            plugin.getLogger().info("§a[Documentation] System overview generated");
            
        } catch (IOException e) {
            plugin.getLogger().warning("Error generating system overview: " + e.getMessage());
        }
    }
    
    /**
     * Generiert Command-Referenz
     */
    private void generateCommandReference() {
        try {
            File file = new File(docsFolder, "command-reference.md");
            FileWriter writer = new FileWriter(file);
            
            writer.write("# Command Reference\n\n");
            
            File pluginFile = new File(plugin.getDataFolder().getParentFile(), "plugin.yml");
            if (pluginFile.exists()) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(pluginFile);
                
                if (config.contains("commands")) {
                    for (String commandName : config.getConfigurationSection("commands").getKeys(false)) {
                        String path = "commands." + commandName;
                        
                        writer.write("## /" + commandName + "\n");
                        writer.write("**Description**: " + config.getString(path + ".description", "No description") + "\n");
                        writer.write("**Usage**: " + config.getString(path + ".usage", "No usage specified") + "\n");
                        writer.write("**Permission**: " + config.getString(path + ".permission", "No permission required") + "\n");
                        
                        List<String> aliases = config.getStringList(path + ".aliases");
                        if (!aliases.isEmpty()) {
                            writer.write("**Aliases**: " + String.join(", ", aliases) + "\n");
                        }
                        
                        writer.write("\n");
                    }
                }
            }
            
            writer.close();
            plugin.getLogger().info("§a[Documentation] Command reference generated");
            
        } catch (IOException e) {
            plugin.getLogger().warning("Error generating command reference: " + e.getMessage());
        }
    }
    
    /**
     * Generiert API-Dokumentation
     */
    private void generateAPIDocumentation() {
        try {
            File file = new File(docsFolder, "api-documentation.md");
            FileWriter writer = new FileWriter(file);
            
            writer.write("# API Documentation\n\n");
            
            // Document main plugin class
            writer.write("## Plugin Class\n");
            writer.write("**Class**: `de.noctivag.plugin.Plugin`\n");
            writer.write("**Description**: Main plugin class containing all system managers\n\n");
            
            // Document system managers
            String[] managers = {
                "SkillsSystem", "CollectionsSystem", "MinionsSystem", "PetsSystem",
                "GuildsSystem", "EconomySystem", "DungeonsSystem", "SlayersSystem",
                "AuctionHouseSystem", "BazaarSystem", "DatabaseManager", "RankManager"
            };
            
            for (String manager : managers) {
                writer.write("## " + manager + "\n");
                writer.write("**Class**: `de.noctivag.plugin." + manager.toLowerCase() + "." + manager + "`\n");
                writer.write("**Description**: Manages " + manager.toLowerCase() + " functionality\n\n");
            }
            
            writer.close();
            plugin.getLogger().info("§a[Documentation] API documentation generated");
            
        } catch (IOException e) {
            plugin.getLogger().warning("Error generating API documentation: " + e.getMessage());
        }
    }
    
    /**
     * Generiert GUI-Guide
     */
    private void generateGUIGuide() {
        try {
            File file = new File(docsFolder, "gui-guide.md");
            FileWriter writer = new FileWriter(file);
            
            writer.write("# GUI Navigation Guide\n\n");
            
            writer.write("## Main Menu\n");
            writer.write("The main menu is accessible via `/menu` or `/gui` command.\n\n");
            
            writer.write("### Available GUIs\n");
            String[] guis = {
                "AdvancedGUISystem", "UltimateMainMenu", "FeatureBookGUI", "AdminMenu",
                "SettingsGUI", "ModerationGUI", "RestartGUI", "QuickActionsGUI",
                "ChatChannelsGUI", "ReportsGUI", "DiscordGUI", "GuildSystemGUI",
                "EventScheduleGUI", "CosmeticsMenu", "ParticleSettingsGUI", "JoinMessagePresetsGUI",
                "BankGUI", "ShopGUI", "StatisticsGUI", "PartyGUI", "RulesGUI", "TournamentGUI",
                "DuelSystemGUI", "FriendsGUI", "JoinMessageGUI", "PvPArenaGUI", "EventRewardsGUI",
                "CustomGUI", "WarpGUI", "HelpGUI", "SupportGUI", "TeleportGUI", "ServerInfoGUI",
                "JobsGUI", "LeaderboardGUI", "EconomyGUI", "AuctionHouseGUI", "MobArenaGUI",
                "KitShopGUI", "WebsiteGUI", "BattlePassGUI", "QuestGUI", "InfoGUI",
                "MyCosmeticsGUI", "CosmeticShopGUI", "GadgetsGUI", "MessagesMenu",
                "EnhancedMainMenu", "AnimatedGUI", "UltimateEventGUI", "UltimateGUISystem",
                "CommandUsageGUI", "FeatureToggleListener", "PotatoBookGUI", "RecombobulatorGUI",
                "DungeonStarGUI", "PetItemGUI", "ArmorAbilityGUI", "WeaponAbilityGUI",
                "NPCCreationGUI", "NPCManagementGUI", "NPCEditGUI", "EnhancedPetGUI"
            };
            
            for (String gui : guis) {
                writer.write("- **" + gui + "** - " + getGUIDescription(gui) + "\n");
            }
            
            writer.close();
            plugin.getLogger().info("§a[Documentation] GUI guide generated");
            
        } catch (IOException e) {
            plugin.getLogger().warning("Error generating GUI guide: " + e.getMessage());
        }
    }
    
    /**
     * Generiert Konfigurations-Guide
     */
    private void generateConfigurationGuide() {
        try {
            File file = new File(docsFolder, "configuration-guide.md");
            FileWriter writer = new FileWriter(file);
            
            writer.write("# Configuration Guide\n\n");
            
            writer.write("## Main Configuration\n");
            writer.write("The main configuration file is `config.yml` in the plugin data folder.\n\n");
            
            writer.write("### Database Configuration\n");
            writer.write("```yaml\n");
            writer.write("database:\n");
            writer.write("  host: localhost\n");
            writer.write("  port: 3306\n");
            writer.write("  database: plugin_db\n");
            writer.write("  username: user\n");
            writer.write("  password: password\n");
            writer.write("  ssl: false\n");
            writer.write("```\n\n");
            
            writer.write("### Economy Configuration\n");
            writer.write("```yaml\n");
            writer.write("economy:\n");
            writer.write("  starting_balance: 1000\n");
            writer.write("  max_balance: 1000000\n");
            writer.write("  currency_symbol: $\n");
            writer.write("```\n\n");
            
            writer.write("### Skills Configuration\n");
            writer.write("```yaml\n");
            writer.write("skills:\n");
            writer.write("  max_level: 50\n");
            writer.write("  xp_multiplier: 1.0\n");
            writer.write("  enable_skills: true\n");
            writer.write("```\n\n");
            
            writer.close();
            plugin.getLogger().info("§a[Documentation] Configuration guide generated");
            
        } catch (IOException e) {
            plugin.getLogger().warning("Error generating configuration guide: " + e.getMessage());
        }
    }
    
    /**
     * Generiert Troubleshooting-Guide
     */
    private void generateTroubleshootingGuide() {
        try {
            File file = new File(docsFolder, "troubleshooting-guide.md");
            FileWriter writer = new FileWriter(file);
            
            writer.write("# Troubleshooting Guide\n\n");
            
            writer.write("## Common Issues\n\n");
            
            writer.write("### Database Connection Issues\n");
            writer.write("**Problem**: Plugin cannot connect to database\n");
            writer.write("**Solution**:\n");
            writer.write("1. Check database credentials in config.yml\n");
            writer.write("2. Ensure database server is running\n");
            writer.write("3. Check firewall settings\n");
            writer.write("4. Verify database exists\n\n");
            
            writer.write("### Permission Issues\n");
            writer.write("**Problem**: Commands not working\n");
            writer.write("**Solution**:\n");
            writer.write("1. Check player permissions\n");
            writer.write("2. Verify permission plugin is loaded\n");
            writer.write("3. Check plugin.yml permissions\n\n");
            
            writer.write("### GUI Issues\n");
            writer.write("**Problem**: GUIs not opening\n");
            writer.write("**Solution**:\n");
            writer.write("1. Check for errors in console\n");
            writer.write("2. Verify GUI classes exist\n");
            writer.write("3. Check inventory size limits\n\n");
            
            writer.write("### Performance Issues\n");
            writer.write("**Problem**: Server lag\n");
            writer.write("**Solution**:\n");
            writer.write("1. Check database query performance\n");
            writer.write("2. Monitor memory usage\n");
            writer.write("3. Optimize configuration settings\n");
            writer.write("4. Check for memory leaks\n\n");
            
            writer.close();
            plugin.getLogger().info("§a[Documentation] Troubleshooting guide generated");
            
        } catch (IOException e) {
            plugin.getLogger().warning("Error generating troubleshooting guide: " + e.getMessage());
        }
    }
    
    /**
     * Gibt eine GUI-Beschreibung zurück
     */
    private String getGUIDescription(String guiName) {
        Map<String, String> descriptions = new HashMap<>();
        descriptions.put("AdvancedGUISystem", "Main GUI system manager");
        descriptions.put("UltimateMainMenu", "Primary navigation menu");
        descriptions.put("FeatureBookGUI", "Plugin features overview");
        descriptions.put("AdminMenu", "Administrative tools and settings");
        descriptions.put("SettingsGUI", "Player settings and preferences");
        descriptions.put("ModerationGUI", "Moderation tools and utilities");
        descriptions.put("RestartGUI", "Server restart management");
        descriptions.put("QuickActionsGUI", "Quick action shortcuts");
        descriptions.put("ChatChannelsGUI", "Chat channel management");
        descriptions.put("ReportsGUI", "Player reports and appeals");
        descriptions.put("DiscordGUI", "Discord integration");
        descriptions.put("GuildSystemGUI", "Guild management interface");
        descriptions.put("EventScheduleGUI", "Event scheduling and management");
        descriptions.put("CosmeticsMenu", "Cosmetic items and customization");
        descriptions.put("ParticleSettingsGUI", "Particle effect settings");
        descriptions.put("JoinMessagePresetsGUI", "Join message customization");
        descriptions.put("BankGUI", "Player banking interface");
        descriptions.put("ShopGUI", "In-game shop interface");
        descriptions.put("StatisticsGUI", "Player statistics display");
        descriptions.put("PartyGUI", "Party management interface");
        descriptions.put("RulesGUI", "Server rules display");
        descriptions.put("TournamentGUI", "Tournament management");
        descriptions.put("DuelSystemGUI", "Duel system interface");
        descriptions.put("FriendsGUI", "Friends list management");
        descriptions.put("JoinMessageGUI", "Join message editor");
        descriptions.put("PvPArenaGUI", "PvP arena management");
        descriptions.put("EventRewardsGUI", "Event reward distribution");
        descriptions.put("CustomGUI", "Custom GUI builder");
        descriptions.put("WarpGUI", "Warp location management");
        descriptions.put("HelpGUI", "Help and information display");
        descriptions.put("SupportGUI", "Support ticket system");
        descriptions.put("TeleportGUI", "Teleportation interface");
        descriptions.put("ServerInfoGUI", "Server information display");
        descriptions.put("JobsGUI", "Job system interface");
        descriptions.put("LeaderboardGUI", "Leaderboard display");
        descriptions.put("EconomyGUI", "Economy management interface");
        descriptions.put("AuctionHouseGUI", "Auction house interface");
        descriptions.put("MobArenaGUI", "Mob arena management");
        descriptions.put("KitShopGUI", "Kit shop interface");
        descriptions.put("WebsiteGUI", "Website integration");
        descriptions.put("BattlePassGUI", "Battle pass system");
        descriptions.put("QuestGUI", "Quest system interface");
        descriptions.put("InfoGUI", "General information display");
        descriptions.put("MyCosmeticsGUI", "Personal cosmetics management");
        descriptions.put("CosmeticShopGUI", "Cosmetic shop interface");
        descriptions.put("GadgetsGUI", "Gadget management interface");
        descriptions.put("MessagesMenu", "Message management interface");
        descriptions.put("EnhancedMainMenu", "Enhanced main menu");
        descriptions.put("AnimatedGUI", "Animated GUI system");
        descriptions.put("UltimateEventGUI", "Ultimate event management");
        descriptions.put("UltimateGUISystem", "Ultimate GUI system");
        descriptions.put("CommandUsageGUI", "Command usage display");
        descriptions.put("FeatureToggleListener", "Feature toggle management");
        descriptions.put("PotatoBookGUI", "Potato book system");
        descriptions.put("RecombobulatorGUI", "Recombobulator system");
        descriptions.put("DungeonStarGUI", "Dungeon star system");
        descriptions.put("PetItemGUI", "Pet item management");
        descriptions.put("ArmorAbilityGUI", "Armor ability system");
        descriptions.put("WeaponAbilityGUI", "Weapon ability system");
        descriptions.put("NPCCreationGUI", "NPC creation interface");
        descriptions.put("NPCManagementGUI", "NPC management interface");
        descriptions.put("NPCEditGUI", "NPC editing interface");
        descriptions.put("EnhancedPetGUI", "Enhanced pet management");
        
        return descriptions.getOrDefault(guiName, "GUI interface");
    }
    
    /**
     * Erstellt ein GUI-Item für die Dokumentation
     */
    public org.bukkit.inventory.ItemStack createDocumentationItem() {
        org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(org.bukkit.Material.BOOK);
        org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName("§6§lSystem Documentation");
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Generated Documentation:");
            lore.add("§7• System Overview");
            lore.add("§7• Command Reference");
            lore.add("§7• API Documentation");
            lore.add("§7• GUI Navigation Guide");
            lore.add("§7• Configuration Guide");
            lore.add("§7• Troubleshooting Guide");
            lore.add("");
            lore.add("§eClick to regenerate documentation");
            lore.add("§eRight-click to open docs folder");
            
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        
        return item;
    }
}
