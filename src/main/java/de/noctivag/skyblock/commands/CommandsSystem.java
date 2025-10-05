package de.noctivag.skyblock.commands;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Commands System - Hypixel Skyblock Style
 */
public class CommandsSystem implements Listener {
    private final SkyblockPlugin SkyblockPlugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerCommandsData> playerCommandsData = new ConcurrentHashMap<>();
    private final Map<CommandType, CommandConfig> commandConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> commandTasks = new ConcurrentHashMap<>();
    
    public CommandsSystem(SkyblockPlugin SkyblockPlugin, MultiServerDatabaseManager databaseManager) {
        this.SkyblockPlugin = SkyblockPlugin;
        this.databaseManager = databaseManager;
        initializeCommandConfigs();
        startCommandsUpdateTask();
        
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    private void initializeCommandConfigs() {
        commandConfigs.put(CommandType.SKYBLOCK_COMMANDS, new CommandConfig(
            "SkyBlock Commands", "§bSkyBlock Commands", Material.COMPASS,
            "§7Access to SkyBlock specific commands.",
            CommandCategory.SKYBLOCK, CommandRarity.COMMON, 1,
            Arrays.asList("§7- /island", "§7- /warp", "§7- /spawn"),
            Arrays.asList("§7- SkyBlock access", "§7- Basic commands")
        ));
        
        commandConfigs.put(CommandType.ECONOMY_COMMANDS, new CommandConfig(
            "Economy Commands", "§6Economy Commands", Material.GOLD_INGOT,
            "§7Access to economy related commands.",
            CommandCategory.ECONOMY, CommandRarity.COMMON, 1,
            Arrays.asList("§7- /bazaar", "§7- /auction", "§7- /bank"),
            Arrays.asList("§7- Economy access", "§7- Trading commands")
        ));
        
        commandConfigs.put(CommandType.SOCIAL_COMMANDS, new CommandConfig(
            "Social Commands", "§aSocial Commands", Material.PLAYER_HEAD,
            "§7Access to social interaction commands.",
            CommandCategory.SOCIAL, CommandRarity.COMMON, 1,
            Arrays.asList("§7- /party", "§7- /friends", "§7- /guild"),
            Arrays.asList("§7- Social access", "§7- Interaction commands")
        ));
    }
    
    private void startCommandsUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerCommandsData> entry : playerCommandsData.entrySet()) {
                    PlayerCommandsData commandsData = entry.getValue();
                    commandsData.update();
                }
            }
        }.runTaskTimer(SkyblockPlugin, 0L, 20L * 60L);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Commands")) {
            openCommandsGUI(player);
        }
    }
    
    public void openCommandsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§b§lCommands"));
        
        addGUIItem(gui, 10, Material.COMPASS, "§b§lSkyBlock Commands", "§7Access SkyBlock commands.");
        addGUIItem(gui, 11, Material.GOLD_INGOT, "§6§lEconomy Commands", "§7Access economy commands.");
        addGUIItem(gui, 12, Material.PLAYER_HEAD, "§a§lSocial Commands", "§7Access social commands.");
        
        player.openInventory(gui);
        player.sendMessage(Component.text("§aCommands GUI geöffnet!"));
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(description).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerCommandsData getPlayerCommandsData(UUID playerId) {
        return playerCommandsData.computeIfAbsent(playerId, k -> new PlayerCommandsData(playerId));
    }
    
    public enum CommandType {
        SKYBLOCK_COMMANDS, ECONOMY_COMMANDS, SOCIAL_COMMANDS, ADMIN_COMMANDS,
        MODERATOR_COMMANDS, HELPER_COMMANDS, VIP_COMMANDS, MVP_COMMANDS
    }
    
    public enum CommandCategory {
        SKYBLOCK("§bSkyBlock", "§7SkyBlock commands"),
        ECONOMY("§6Economy", "§7Economy commands"),
        SOCIAL("§aSocial", "§7Social commands"),
        ADMIN("§cAdmin", "§7Admin commands"),
        MODERATOR("§9Moderator", "§7Moderator commands"),
        HELPER("§eHelper", "§7Helper commands"),
        VIP("§dVIP", "§7VIP commands"),
        MVP("§6MVP", "§7MVP commands");
        
        private final String displayName;
        private final String description;
        
        CommandCategory(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum CommandRarity {
        COMMON("§fCommon", 1.0),
        UNCOMMON("§aUncommon", 1.5),
        RARE("§9Rare", 2.0),
        EPIC("§5Epic", 3.0),
        LEGENDARY("§6Legendary", 5.0);
        
        private final String displayName;
        private final double multiplier;
        
        CommandRarity(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
        
        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }
    
    public static class CommandConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final CommandCategory category;
        private final CommandRarity rarity;
        private final int maxLevel;
        private final List<String> features;
        private final List<String> requirements;
        
        public CommandConfig(String name, String displayName, Material icon, String description,
                           CommandCategory category, CommandRarity rarity, int maxLevel,
                           List<String> features, List<String> requirements) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.rarity = rarity;
            this.maxLevel = maxLevel;
            this.features = features;
            this.requirements = requirements;
        }
        
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public CommandCategory getCategory() { return category; }
        public CommandRarity getRarity() { return rarity; }
        public int getMaxLevel() { return maxLevel; }
        public List<String> getFeatures() { return features; }
        public List<String> getRequirements() { return requirements; }
    }
    
    public static class PlayerCommandsData {
        private final UUID playerId;
        private final Map<CommandType, Integer> commandLevels = new HashMap<>();
        private final Map<CommandType, Boolean> unlockedCommands = new HashMap<>();
        private long lastUpdate;
        
        public PlayerCommandsData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = java.lang.System.currentTimeMillis();
        }
        
        public void addCommand(CommandType type) {
            commandLevels.put(type, commandLevels.getOrDefault(type, 0) + 1);
            unlockedCommands.put(type, true);
        }
        
        public boolean hasCommand(CommandType type) {
            return unlockedCommands.getOrDefault(type, false);
        }
        
        public int getCommandLevel(CommandType type) {
            return commandLevels.getOrDefault(type, 0);
        }
        
        public long getLastUpdate() { return lastUpdate; }
    }
}
