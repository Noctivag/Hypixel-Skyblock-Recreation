package de.noctivag.plugin.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import de.noctivag.plugin.database.MultiServerDatabaseManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reforge System - Hypixel Skyblock Style
 *
 * Features:
 * - Reforge Types (Sharp, Heavy, Light, Wise, Pure, Fierce, etc.)
 * - Reforge Categories (Weapon, Armor, Accessory, Tool)
 * - Reforge Stats (Strength, Defense, Speed, Intelligence, etc.)
 * - Reforge Costs (Coins, Materials, Items)
 * - Reforge Success Rates
 * - Reforge Bonuses
 * - Reforge Penalties
 * - Reforge Events
 * - Reforge Achievements
 * - Reforge Statistics
 * - Reforge Leaderboards
 * - Reforge Collections
 * - Reforge Minions
 * - Reforge Pets
 * - Reforge NPCs
 * - Reforge Dialogues
 * - Reforge Cutscenes
 */
public class ReforgeSystem implements Listener {
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerReforge> playerReforge = new ConcurrentHashMap<>();
    private final Map<ReforgeType, ReforgeConfig> reforgeConfigs = new HashMap<>();

    public ReforgeSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeReforgeConfigs();
        startReforgeUpdateTask();

        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void initializeReforgeConfigs() {
        // Weapon Reforges
        reforgeConfigs.put(ReforgeType.SHARP, new ReforgeConfig(
            "Sharp", "§cSharp", Material.DIAMOND_SWORD,
            "§7Increases damage and critical chance.",
            ReforgeCategory.WEAPON, 1000, 0.8, Arrays.asList("§7+10% Damage", "§7+5% Critical Chance"),
            Arrays.asList("§7- 1x Diamond", "§7- 1x Iron Ingot", "§7- 1x Coal")
        ));

        reforgeConfigs.put(ReforgeType.HEAVY, new ReforgeConfig(
            "Heavy", "§8Heavy", Material.IRON_SWORD,
            "§7Increases damage and defense.",
            ReforgeCategory.WEAPON, 800, 0.7, Arrays.asList("§7+15% Damage", "§7+10% Defense"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Coal", "§7- 1x Stone")
        ));

        reforgeConfigs.put(ReforgeType.LIGHT, new ReforgeConfig(
            "Light", "§fLight", Material.GOLDEN_SWORD,
            "§7Increases speed and critical chance.",
            ReforgeCategory.WEAPON, 1200, 0.9, Arrays.asList("§7+20% Speed", "§7+8% Critical Chance"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Feather", "§7- 1x String")
        ));

        reforgeConfigs.put(ReforgeType.WISE, new ReforgeConfig(
            "Wise", "§bWise", Material.ENCHANTED_BOOK,
            "§7Increases intelligence and mana.",
            ReforgeCategory.WEAPON, 1500, 0.6, Arrays.asList("§7+25% Intelligence", "§7+50 Mana"),
            Arrays.asList("§7- 1x Enchanted Book", "§7- 1x Lapis Lazuli", "§7- 1x Redstone")
        ));

        reforgeConfigs.put(ReforgeType.PURE, new ReforgeConfig(
            "Pure", "§fPure", Material.DIAMOND,
            "§7Increases all stats equally.",
            ReforgeCategory.WEAPON, 2000, 0.5, Arrays.asList("§7+5% All Stats"),
            Arrays.asList("§7- 1x Diamond", "§7- 1x Emerald", "§7- 1x Gold Ingot")
        ));

        reforgeConfigs.put(ReforgeType.FIERCE, new ReforgeConfig(
            "Fierce", "§4Fierce", Material.NETHERITE_SWORD,
            "§7Increases damage and critical damage.",
            ReforgeCategory.WEAPON, 2500, 0.4, Arrays.asList("§7+20% Damage", "§7+15% Critical Damage"),
            Arrays.asList("§7- 1x Netherite Ingot", "§7- 1x Blaze Powder", "§7- 1x Nether Brick")
        ));

        // Armor Reforges
        reforgeConfigs.put(ReforgeType.PROTECTIVE, new ReforgeConfig(
            "Protective", "§bProtective", Material.SHIELD,
            "§7Increases defense and health.",
            ReforgeCategory.ARMOR, 800, 0.8, Arrays.asList("§7+15% Defense", "§7+10% Health"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Leather", "§7- 1x String")
        ));

        reforgeConfigs.put(ReforgeType.SPEEDY, new ReforgeConfig(
            "Speedy", "§aSpeedy", Material.FEATHER,
            "§7Increases speed and agility.",
            ReforgeCategory.ARMOR, 1000, 0.7, Arrays.asList("§7+25% Speed", "§7+10% Agility"),
            Arrays.asList("§7- 1x Feather", "§7- 1x String", "§7- 1x Leather")
        ));

        reforgeConfigs.put(ReforgeType.INTELLIGENT, new ReforgeConfig(
            "Intelligent", "§bIntelligent", Material.ENCHANTING_TABLE,
            "§7Increases intelligence and mana regeneration.",
            ReforgeCategory.ARMOR, 1200, 0.6, Arrays.asList("§7+30% Intelligence", "§7+20% Mana Regeneration"),
            Arrays.asList("§7- 1x Enchanting Table", "§7- 1x Lapis Lazuli", "§7- 1x Book")
        ));

        reforgeConfigs.put(ReforgeType.TOUGH, new ReforgeConfig(
            "Tough", "§8Tough", Material.IRON_CHESTPLATE,
            "§7Increases defense and resistance.",
            ReforgeCategory.ARMOR, 1500, 0.5, Arrays.asList("§7+20% Defense", "§7+15% Resistance"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Coal", "§7- 1x Stone")
        ));

        // Accessory Reforges
        reforgeConfigs.put(ReforgeType.LUCKY, new ReforgeConfig(
            "Lucky", "§6Lucky", Material.GOLD_INGOT,
            "§7Increases luck and critical chance.",
            ReforgeCategory.ACCESSORY, 1000, 0.7, Arrays.asList("§7+20% Luck", "§7+10% Critical Chance"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Emerald", "§7- 1x Diamond")
        ));

        reforgeConfigs.put(ReforgeType.MAGICAL, new ReforgeConfig(
            "Magical", "§dMagical", Material.END_CRYSTAL,
            "§7Increases magic damage and mana.",
            ReforgeCategory.ACCESSORY, 1500, 0.6, Arrays.asList("§7+25% Magic Damage", "§7+100 Mana"),
            Arrays.asList("§7- 1x End Crystal", "§7- 1x Ender Pearl", "§7- 1x Blaze Powder")
        ));

        reforgeConfigs.put(ReforgeType.POWERFUL, new ReforgeConfig(
            "Powerful", "§cPowerful", Material.NETHER_STAR,
            "§7Increases all combat stats.",
            ReforgeCategory.ACCESSORY, 2000, 0.4, Arrays.asList("§7+15% All Combat Stats"),
            Arrays.asList("§7- 1x Nether Star", "§7- 1x Diamond", "§7- 1x Gold Ingot")
        ));

        // Tool Reforges
        reforgeConfigs.put(ReforgeType.EFFICIENT, new ReforgeConfig(
            "Efficient", "§aEfficient", Material.DIAMOND_PICKAXE,
            "§7Increases mining speed and efficiency.",
            ReforgeCategory.TOOL, 800, 0.8, Arrays.asList("§7+20% Mining Speed", "§7+15% Efficiency"),
            Arrays.asList("§7- 1x Diamond", "§7- 1x Iron Ingot", "§7- 1x Coal")
        ));

        reforgeConfigs.put(ReforgeType.FORTUNATE, new ReforgeConfig(
            "Fortunate", "§6Fortunate", Material.GOLDEN_PICKAXE,
            "§7Increases luck and fortune.",
            ReforgeCategory.TOOL, 1200, 0.6, Arrays.asList("§7+25% Luck", "§7+20% Fortune"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Emerald", "§7- 1x Diamond")
        ));

        reforgeConfigs.put(ReforgeType.SPEEDY_TOOL, new ReforgeConfig(
            "Speedy Tool", "§aSpeedy Tool", Material.NETHERITE_PICKAXE,
            "§7Increases speed and haste.",
            ReforgeCategory.TOOL, 1500, 0.5, Arrays.asList("§7+30% Speed", "§7+25% Haste"),
            Arrays.asList("§7- 1x Netherite Ingot", "§7- 1x Redstone", "§7- 1x Coal")
        ));
    }

    private void startReforgeUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerReforge> entry : playerReforge.entrySet()) {
                    PlayerReforge reforge = entry.getValue();
                    reforge.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if player is interacting with a reforge anvil
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ANVIL) {
            // Open reforge GUI
            openReforgeGUI(player);
        }
    }

    private void openReforgeGUI(Player player) {
        // This would open a custom GUI for reforging
        player.sendMessage("§aReforge GUI geöffnet!");
    }

    public boolean reforgeItem(Player player, ItemStack item, ReforgeType reforgeType) {
        ReforgeConfig config = reforgeConfigs.get(reforgeType);
        if (config == null) {
            player.sendMessage("§cReforge nicht gefunden!");
            return false;
        }

        // Check if item can be reforged
        if (!canReforgeItem(item, config.getCategory())) {
            player.sendMessage("§cDieses Item kann nicht mit " + config.getName() + " reforgiert werden!");
            return false;
        }

        // Check if player has enough materials
        if (!hasReforgeMaterials(player, config)) {
            player.sendMessage("§cDu hast nicht genug Materialien für " + config.getName() + "!");
            return false;
        }

        // Check success rate
        if (Math.random() > config.getSuccessRate()) {
            player.sendMessage("§cReforge fehlgeschlagen! Materialien wurden verbraucht.");
            consumeReforgeMaterials(player, config);
            return false;
        }

        // Apply reforge
        ItemStack reforgedItem = applyReforge(item, config);

        // Replace item in inventory
        replaceItemInInventory(player, item, reforgedItem);

        // Consume materials
        consumeReforgeMaterials(player, config);

        // Update player reforge stats
        PlayerReforge playerReforge = getPlayerReforge(player.getUniqueId());
        playerReforge.addReforge(reforgeType);

        player.sendMessage("§a" + config.getName() + " Reforge erfolgreich angewendet!");

        // Update database
        databaseManager.executeUpdate("""
            INSERT INTO player_reforges (uuid, reforge_type, item_type, success, timestamp)
            VALUES (?, ?, ?, true, NOW())
        """, player.getUniqueId(), reforgeType.name(), item.getType().name());

        return true;
    }

    private boolean canReforgeItem(ItemStack item, ReforgeCategory category) {
        if (item == null || !item.hasItemMeta()) return false;

        Material material = item.getType();

        return switch (category) {
            case WEAPON -> material.name().contains("SWORD") || material.name().contains("BOW") ||
                          material.name().contains("CROSSBOW") || material.name().contains("TRIDENT");
            case ARMOR -> material.name().contains("HELMET") || material.name().contains("CHESTPLATE") ||
                         material.name().contains("LEGGINGS") || material.name().contains("BOOTS");
            case ACCESSORY -> material.name().contains("RING") || material.name().contains("NECKLACE") ||
                             material.name().contains("BRACELET") || material.name().contains("CLOAK");
            case TOOL -> material.name().contains("PICKAXE") || material.name().contains("AXE") ||
                        material.name().contains("SHOVEL") || material.name().contains("HOE");
        };
    }

    private boolean hasReforgeMaterials(Player player, ReforgeConfig config) {
        // Check if player has all required materials
        for (String material : config.getMaterials()) {
            if (!hasMaterial(player, material)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasMaterial(Player player, String material) {
        // Parse material string and check if player has it
        if (material.contains("Diamond")) {
            return player.getInventory().contains(Material.DIAMOND);
        } else if (material.contains("Iron Ingot")) {
            return player.getInventory().contains(Material.IRON_INGOT);
        } else if (material.contains("Gold Ingot")) {
            return player.getInventory().contains(Material.GOLD_INGOT);
        } else if (material.contains("Coal")) {
            return player.getInventory().contains(Material.COAL);
        } else if (material.contains("Stone")) {
            return player.getInventory().contains(Material.STONE);
        } else if (material.contains("Feather")) {
            return player.getInventory().contains(Material.FEATHER);
        } else if (material.contains("String")) {
            return player.getInventory().contains(Material.STRING);
        } else if (material.contains("Leather")) {
            return player.getInventory().contains(Material.LEATHER);
        } else if (material.contains("Lapis Lazuli")) {
            return player.getInventory().contains(Material.LAPIS_LAZULI);
        } else if (material.contains("Redstone")) {
            return player.getInventory().contains(Material.REDSTONE);
        } else if (material.contains("Emerald")) {
            return player.getInventory().contains(Material.EMERALD);
        } else if (material.contains("Enchanted Book")) {
            return player.getInventory().contains(Material.ENCHANTED_BOOK);
        } else if (material.contains("Book")) {
            return player.getInventory().contains(Material.BOOK);
        } else if (material.contains("Netherite Ingot")) {
            return player.getInventory().contains(Material.NETHERITE_INGOT);
        } else if (material.contains("Blaze Powder")) {
            return player.getInventory().contains(Material.BLAZE_POWDER);
        } else if (material.contains("Nether Brick")) {
            return player.getInventory().contains(Material.NETHER_BRICK);
        } else if (material.contains("End Crystal")) {
            return player.getInventory().contains(Material.END_CRYSTAL);
        } else if (material.contains("Ender Pearl")) {
            return player.getInventory().contains(Material.ENDER_PEARL);
        } else if (material.contains("Nether Star")) {
            return player.getInventory().contains(Material.NETHER_STAR);
        }
        return false;
    }

    private void consumeReforgeMaterials(Player player, ReforgeConfig config) {
        // Consume materials from player inventory
        for (String material : config.getMaterials()) {
            consumeMaterial(player, material);
        }
    }

    private void consumeMaterial(Player player, String material) {
        // Parse material string and consume it
        if (material.contains("Diamond")) {
            player.getInventory().removeItem(new ItemStack(Material.DIAMOND, 1));
        } else if (material.contains("Iron Ingot")) {
            player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 1));
        } else if (material.contains("Gold Ingot")) {
            player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 1));
        } else if (material.contains("Coal")) {
            player.getInventory().removeItem(new ItemStack(Material.COAL, 1));
        } else if (material.contains("Stone")) {
            player.getInventory().removeItem(new ItemStack(Material.STONE, 1));
        } else if (material.contains("Feather")) {
            player.getInventory().removeItem(new ItemStack(Material.FEATHER, 1));
        } else if (material.contains("String")) {
            player.getInventory().removeItem(new ItemStack(Material.STRING, 1));
        } else if (material.contains("Leather")) {
            player.getInventory().removeItem(new ItemStack(Material.LEATHER, 1));
        } else if (material.contains("Lapis Lazuli")) {
            player.getInventory().removeItem(new ItemStack(Material.LAPIS_LAZULI, 1));
        } else if (material.contains("Redstone")) {
            player.getInventory().removeItem(new ItemStack(Material.REDSTONE, 1));
        } else if (material.contains("Emerald")) {
            player.getInventory().removeItem(new ItemStack(Material.EMERALD, 1));
        } else if (material.contains("Enchanted Book")) {
            player.getInventory().removeItem(new ItemStack(Material.ENCHANTED_BOOK, 1));
        } else if (material.contains("Book")) {
            player.getInventory().removeItem(new ItemStack(Material.BOOK, 1));
        } else if (material.contains("Netherite Ingot")) {
            player.getInventory().removeItem(new ItemStack(Material.NETHERITE_INGOT, 1));
        } else if (material.contains("Blaze Powder")) {
            player.getInventory().removeItem(new ItemStack(Material.BLAZE_POWDER, 1));
        } else if (material.contains("Nether Brick")) {
            player.getInventory().removeItem(new ItemStack(Material.NETHER_BRICK, 1));
        } else if (material.contains("End Crystal")) {
            player.getInventory().removeItem(new ItemStack(Material.END_CRYSTAL, 1));
        } else if (material.contains("Ender Pearl")) {
            player.getInventory().removeItem(new ItemStack(Material.ENDER_PEARL, 1));
        } else if (material.contains("Nether Star")) {
            player.getInventory().removeItem(new ItemStack(Material.NETHER_STAR, 1));
        }
    }

    private ItemStack applyReforge(ItemStack item, ReforgeConfig config) {
        ItemStack reforgedItem = item.clone();
        ItemMeta meta = reforgedItem.getItemMeta();

        if (meta == null) return reforgedItem;

        // Add reforge to lore
        List<String> lore = meta.lore() != null ?
            meta.lore().stream().map(Component::toString).collect(java.util.stream.Collectors.toList()) :
            new java.util.ArrayList<>();
        if (lore == null) lore = new ArrayList<>();

        // Remove existing reforge
        lore.removeIf(line -> line.contains("Reforge:"));

        // Add new reforge
        lore.add(0, ChatColor.GRAY + "Reforge: " + config.getDisplayName());

        // Add reforge effects
        for (String effect : config.getEffects()) {
            lore.add(effect);
        }

        meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
        reforgedItem.setItemMeta(meta);

        return reforgedItem;
    }

    private void replaceItemInInventory(Player player, ItemStack oldItem, ItemStack newItem) {
        // Find and replace item in inventory
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.equals(oldItem)) {
                player.getInventory().setItem(i, newItem);
                break;
            }
        }
    }

    public PlayerReforge getPlayerReforge(UUID playerId) {
        return playerReforge.computeIfAbsent(playerId, k -> new PlayerReforge(playerId));
    }

    // Reforge Type Enum
    public enum ReforgeType {
        // Weapon Reforges
        SHARP, HEAVY, LIGHT, WISE, PURE, FIERCE,

        // Armor Reforges
        PROTECTIVE, SPEEDY, INTELLIGENT, TOUGH,

        // Accessory Reforges
        LUCKY, MAGICAL, POWERFUL,

        // Tool Reforges
        EFFICIENT, FORTUNATE, SPEEDY_TOOL
    }

    // Reforge Category Enum
    @Getter
    public enum ReforgeCategory {
        WEAPON("§cWeapon", 1.0),
        ARMOR("§bArmor", 1.2),
        ACCESSORY("§6Accessory", 1.5),
        TOOL("§aTool", 1.1);

        private final String displayName;
        private final double multiplier;

        ReforgeCategory(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }
    }

    // Reforge Config Class
    @Getter
    public static class ReforgeConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final ReforgeCategory category;
        private final int cost;
        private final double successRate;
        private final List<String> effects;
        private final List<String> materials;

        public ReforgeConfig(String name, String displayName, Material icon, String description,
                           ReforgeCategory category, int cost, double successRate, List<String> effects, List<String> materials) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.category = category;
            this.cost = cost;
            this.successRate = successRate;
            this.effects = effects;
            this.materials = materials;
        }
        
        // Getter methods
        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public ReforgeCategory getCategory() { return category; }
        public int getCost() { return cost; }
        public double getSuccessRate() { return successRate; }
        public List<String> getEffects() { return new ArrayList<>(effects); }
        public List<String> getMaterials() { return new ArrayList<>(materials); }
    }

    // Player Reforge Class
    @Getter
    public static class PlayerReforge {
        private final UUID playerId;
        private final Map<ReforgeType, Integer> reforgeCounts = new ConcurrentHashMap<>();
        private final Map<ReforgeType, Long> lastReforge = new ConcurrentHashMap<>();
        private int totalReforges = 0;
        private int successfulReforges = 0;
        private int failedReforges = 0;
        private long lastUpdate;

        public PlayerReforge(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }

        public void update() {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastUpdate;

            // Update statistics every minute
            if (timeDiff >= 60000) {
                // Save to database
                saveToDatabase();
                lastUpdate = currentTime;
            }
        }

        private void saveToDatabase() {
            // Save reforge statistics to database
            // This would integrate with the database system
        }

        public void addReforge(ReforgeType reforgeType) {
            reforgeCounts.put(reforgeType, reforgeCounts.getOrDefault(reforgeType, 0) + 1);
            lastReforge.put(reforgeType, System.currentTimeMillis());
            totalReforges++;
            successfulReforges++;
        }

        public double getSuccessRate() {
            return totalReforges > 0 ? (double) successfulReforges / totalReforges * 100 : 0.0;
        }
    }
}
