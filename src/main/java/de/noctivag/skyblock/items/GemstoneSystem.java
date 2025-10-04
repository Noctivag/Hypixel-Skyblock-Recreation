package de.noctivag.skyblock.items;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import de.noctivag.skyblock.database.MultiServerDatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Gemstone System - Hypixel Skyblock Style
 *
 * Features:
 * - Gemstone Types (Ruby, Sapphire, Emerald, Amethyst, Topaz, Jasper, Onyx, etc.)
 * - Gemstone Qualities (Rough, Flawed, Fine, Flawless, Perfect)
 * - Gemstone Slots (Weapon, Armor, Accessory, Tool)
 * - Gemstone Stats (Strength, Defense, Speed, Intelligence, etc.)
 * - Gemstone Upgrades
 * - Gemstone Combinations
 * - Gemstone Mining
 * - Gemstone Crafting
 * - Gemstone Trading
 * - Gemstone Events
 * - Gemstone Achievements
 * - Gemstone Statistics
 * - Gemstone Leaderboards
 * - Gemstone Collections
 * - Gemstone Minions
 * - Gemstone Pets
 * - Gemstone NPCs
 * - Gemstone Dialogues
 * - Gemstone Cutscenes
 */
public class GemstoneSystem implements Listener {
    private final SkyblockPlugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerGemstones> playerGemstones = new ConcurrentHashMap<>();
    private final Map<GemstoneType, GemstoneConfig> gemstoneConfigs = new HashMap<>();
    private final Map<UUID, BukkitTask> gemstoneTasks = new ConcurrentHashMap<>();

    public GemstoneSystem(SkyblockPlugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        initializeGemstoneConfigs();
        startGemstoneUpdateTask();

        // Register events
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void initializeGemstoneConfigs() {
        // Ruby Gemstones
        gemstoneConfigs.put(GemstoneType.RUBY_ROUGH, new GemstoneConfig(
            "Rough Ruby", "§cRough Ruby", Material.REDSTONE,
            "§7A rough ruby gemstone that provides strength.",
            GemstoneQuality.ROUGH, GemstoneSlot.WEAPON, 10, 0.1, Arrays.asList("§7+1 Strength"),
            Arrays.asList("§7- 1x Redstone", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.RUBY_FLAWED, new GemstoneConfig(
            "Flawed Ruby", "§cFlawed Ruby", Material.REDSTONE,
            "§7A flawed ruby gemstone that provides strength.",
            GemstoneQuality.FLAWED, GemstoneSlot.WEAPON, 25, 0.2, Arrays.asList("§7+2 Strength"),
            Arrays.asList("§7- 2x Rough Ruby", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.RUBY_FINE, new GemstoneConfig(
            "Fine Ruby", "§cFine Ruby", Material.REDSTONE,
            "§7A fine ruby gemstone that provides strength.",
            GemstoneQuality.FINE, GemstoneSlot.WEAPON, 50, 0.3, Arrays.asList("§7+3 Strength"),
            Arrays.asList("§7- 2x Flawed Ruby", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.RUBY_FLAWLESS, new GemstoneConfig(
            "Flawless Ruby", "§cFlawless Ruby", Material.REDSTONE,
            "§7A flawless ruby gemstone that provides strength.",
            GemstoneQuality.FLAWLESS, GemstoneSlot.WEAPON, 100, 0.4, Arrays.asList("§7+4 Strength"),
            Arrays.asList("§7- 2x Fine Ruby", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.RUBY_PERFECT, new GemstoneConfig(
            "Perfect Ruby", "§cPerfect Ruby", Material.REDSTONE,
            "§7A perfect ruby gemstone that provides strength.",
            GemstoneQuality.PERFECT, GemstoneSlot.WEAPON, 200, 0.5, Arrays.asList("§7+5 Strength"),
            Arrays.asList("§7- 2x Flawless Ruby", "§7- 1x Coal")
        ));

        // Sapphire Gemstones
        gemstoneConfigs.put(GemstoneType.SAPPHIRE_ROUGH, new GemstoneConfig(
            "Rough Sapphire", "§bRough Sapphire", Material.LAPIS_LAZULI,
            "§7A rough sapphire gemstone that provides intelligence.",
            GemstoneQuality.ROUGH, GemstoneSlot.ARMOR, 10, 0.1, Arrays.asList("§7+1 Intelligence"),
            Arrays.asList("§7- 1x Lapis Lazuli", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.SAPPHIRE_FLAWED, new GemstoneConfig(
            "Flawed Sapphire", "§bFlawed Sapphire", Material.LAPIS_LAZULI,
            "§7A flawed sapphire gemstone that provides intelligence.",
            GemstoneQuality.FLAWED, GemstoneSlot.ARMOR, 25, 0.2, Arrays.asList("§7+2 Intelligence"),
            Arrays.asList("§7- 2x Rough Sapphire", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.SAPPHIRE_FINE, new GemstoneConfig(
            "Fine Sapphire", "§bFine Sapphire", Material.LAPIS_LAZULI,
            "§7A fine sapphire gemstone that provides intelligence.",
            GemstoneQuality.FINE, GemstoneSlot.ARMOR, 50, 0.3, Arrays.asList("§7+3 Intelligence"),
            Arrays.asList("§7- 2x Flawed Sapphire", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.SAPPHIRE_FLAWLESS, new GemstoneConfig(
            "Flawless Sapphire", "§bFlawless Sapphire", Material.LAPIS_LAZULI,
            "§7A flawless sapphire gemstone that provides intelligence.",
            GemstoneQuality.FLAWLESS, GemstoneSlot.ARMOR, 100, 0.4, Arrays.asList("§7+4 Intelligence"),
            Arrays.asList("§7- 2x Fine Sapphire", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.SAPPHIRE_PERFECT, new GemstoneConfig(
            "Perfect Sapphire", "§bPerfect Sapphire", Material.LAPIS_LAZULI,
            "§7A perfect sapphire gemstone that provides intelligence.",
            GemstoneQuality.PERFECT, GemstoneSlot.ARMOR, 200, 0.5, Arrays.asList("§7+5 Intelligence"),
            Arrays.asList("§7- 2x Flawless Sapphire", "§7- 1x Coal")
        ));

        // Emerald Gemstones
        gemstoneConfigs.put(GemstoneType.EMERALD_ROUGH, new GemstoneConfig(
            "Rough Emerald", "§aRough Emerald", Material.EMERALD,
            "§7A rough emerald gemstone that provides luck.",
            GemstoneQuality.ROUGH, GemstoneSlot.ACCESSORY, 10, 0.1, Arrays.asList("§7+1 Luck"),
            Arrays.asList("§7- 1x Emerald", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.EMERALD_FLAWED, new GemstoneConfig(
            "Flawed Emerald", "§aFlawed Emerald", Material.EMERALD,
            "§7A flawed emerald gemstone that provides luck.",
            GemstoneQuality.FLAWED, GemstoneSlot.ACCESSORY, 25, 0.2, Arrays.asList("§7+2 Luck"),
            Arrays.asList("§7- 2x Rough Emerald", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.EMERALD_FINE, new GemstoneConfig(
            "Fine Emerald", "§aFine Emerald", Material.EMERALD,
            "§7A fine emerald gemstone that provides luck.",
            GemstoneQuality.FINE, GemstoneSlot.ACCESSORY, 50, 0.3, Arrays.asList("§7+3 Luck"),
            Arrays.asList("§7- 2x Flawed Emerald", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.EMERALD_FLAWLESS, new GemstoneConfig(
            "Flawless Emerald", "§aFlawless Emerald", Material.EMERALD,
            "§7A flawless emerald gemstone that provides luck.",
            GemstoneQuality.FLAWLESS, GemstoneSlot.ACCESSORY, 100, 0.4, Arrays.asList("§7+4 Luck"),
            Arrays.asList("§7- 2x Fine Emerald", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.EMERALD_PERFECT, new GemstoneConfig(
            "Perfect Emerald", "§aPerfect Emerald", Material.EMERALD,
            "§7A perfect emerald gemstone that provides luck.",
            GemstoneQuality.PERFECT, GemstoneSlot.ACCESSORY, 200, 0.5, Arrays.asList("§7+5 Luck"),
            Arrays.asList("§7- 2x Flawless Emerald", "§7- 1x Coal")
        ));

        // Amethyst Gemstones
        gemstoneConfigs.put(GemstoneType.AMETHYST_ROUGH, new GemstoneConfig(
            "Rough Amethyst", "§dRough Amethyst", Material.AMETHYST_SHARD,
            "§7A rough amethyst gemstone that provides magic damage.",
            GemstoneQuality.ROUGH, GemstoneSlot.WEAPON, 10, 0.1, Arrays.asList("§7+1 Magic Damage"),
            Arrays.asList("§7- 1x Amethyst Shard", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.AMETHYST_FLAWED, new GemstoneConfig(
            "Flawed Amethyst", "§dFlawed Amethyst", Material.AMETHYST_SHARD,
            "§7A flawed amethyst gemstone that provides magic damage.",
            GemstoneQuality.FLAWED, GemstoneSlot.WEAPON, 25, 0.2, Arrays.asList("§7+2 Magic Damage"),
            Arrays.asList("§7- 2x Rough Amethyst", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.AMETHYST_FINE, new GemstoneConfig(
            "Fine Amethyst", "§dFine Amethyst", Material.AMETHYST_SHARD,
            "§7A fine amethyst gemstone that provides magic damage.",
            GemstoneQuality.FINE, GemstoneSlot.WEAPON, 50, 0.3, Arrays.asList("§7+3 Magic Damage"),
            Arrays.asList("§7- 2x Flawed Amethyst", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.AMETHYST_FLAWLESS, new GemstoneConfig(
            "Flawless Amethyst", "§dFlawless Amethyst", Material.AMETHYST_SHARD,
            "§7A flawless amethyst gemstone that provides magic damage.",
            GemstoneQuality.FLAWLESS, GemstoneSlot.WEAPON, 100, 0.4, Arrays.asList("§7+4 Magic Damage"),
            Arrays.asList("§7- 2x Fine Amethyst", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.AMETHYST_PERFECT, new GemstoneConfig(
            "Perfect Amethyst", "§dPerfect Amethyst", Material.AMETHYST_SHARD,
            "§7A perfect amethyst gemstone that provides magic damage.",
            GemstoneQuality.PERFECT, GemstoneSlot.WEAPON, 200, 0.5, Arrays.asList("§7+5 Magic Damage"),
            Arrays.asList("§7- 2x Flawless Amethyst", "§7- 1x Coal")
        ));

        // Topaz Gemstones
        gemstoneConfigs.put(GemstoneType.TOPAZ_ROUGH, new GemstoneConfig(
            "Rough Topaz", "§eRough Topaz", Material.GOLD_INGOT,
            "§7A rough topaz gemstone that provides speed.",
            GemstoneQuality.ROUGH, GemstoneSlot.ARMOR, 10, 0.1, Arrays.asList("§7+1 Speed"),
            Arrays.asList("§7- 1x Gold Ingot", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.TOPAZ_FLAWED, new GemstoneConfig(
            "Flawed Topaz", "§eFlawed Topaz", Material.GOLD_INGOT,
            "§7A flawed topaz gemstone that provides speed.",
            GemstoneQuality.FLAWED, GemstoneSlot.ARMOR, 25, 0.2, Arrays.asList("§7+2 Speed"),
            Arrays.asList("§7- 2x Rough Topaz", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.TOPAZ_FINE, new GemstoneConfig(
            "Fine Topaz", "§eFine Topaz", Material.GOLD_INGOT,
            "§7A fine topaz gemstone that provides speed.",
            GemstoneQuality.FINE, GemstoneSlot.ARMOR, 50, 0.3, Arrays.asList("§7+3 Speed"),
            Arrays.asList("§7- 2x Flawed Topaz", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.TOPAZ_FLAWLESS, new GemstoneConfig(
            "Flawless Topaz", "§eFlawless Topaz", Material.GOLD_INGOT,
            "§7A flawless topaz gemstone that provides speed.",
            GemstoneQuality.FLAWLESS, GemstoneSlot.ARMOR, 100, 0.4, Arrays.asList("§7+4 Speed"),
            Arrays.asList("§7- 2x Fine Topaz", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.TOPAZ_PERFECT, new GemstoneConfig(
            "Perfect Topaz", "§ePerfect Topaz", Material.GOLD_INGOT,
            "§7A perfect topaz gemstone that provides speed.",
            GemstoneQuality.PERFECT, GemstoneSlot.ARMOR, 200, 0.5, Arrays.asList("§7+5 Speed"),
            Arrays.asList("§7- 2x Flawless Topaz", "§7- 1x Coal")
        ));

        // Jasper Gemstones
        gemstoneConfigs.put(GemstoneType.JASPER_ROUGH, new GemstoneConfig(
            "Rough Jasper", "§6Rough Jasper", Material.IRON_INGOT,
            "§7A rough jasper gemstone that provides defense.",
            GemstoneQuality.ROUGH, GemstoneSlot.ARMOR, 10, 0.1, Arrays.asList("§7+1 Defense"),
            Arrays.asList("§7- 1x Iron Ingot", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.JASPER_FLAWED, new GemstoneConfig(
            "Flawed Jasper", "§6Flawed Jasper", Material.IRON_INGOT,
            "§7A flawed jasper gemstone that provides defense.",
            GemstoneQuality.FLAWED, GemstoneSlot.ARMOR, 25, 0.2, Arrays.asList("§7+2 Defense"),
            Arrays.asList("§7- 2x Rough Jasper", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.JASPER_FINE, new GemstoneConfig(
            "Fine Jasper", "§6Fine Jasper", Material.IRON_INGOT,
            "§7A fine jasper gemstone that provides defense.",
            GemstoneQuality.FINE, GemstoneSlot.ARMOR, 50, 0.3, Arrays.asList("§7+3 Defense"),
            Arrays.asList("§7- 2x Flawed Jasper", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.JASPER_FLAWLESS, new GemstoneConfig(
            "Flawless Jasper", "§6Flawless Jasper", Material.IRON_INGOT,
            "§7A flawless jasper gemstone that provides defense.",
            GemstoneQuality.FLAWLESS, GemstoneSlot.ARMOR, 100, 0.4, Arrays.asList("§7+4 Defense"),
            Arrays.asList("§7- 2x Fine Jasper", "§7- 1x Coal")
        ));

        gemstoneConfigs.put(GemstoneType.JASPER_PERFECT, new GemstoneConfig(
            "Perfect Jasper", "§6Perfect Jasper", Material.IRON_INGOT,
            "§7A perfect jasper gemstone that provides defense.",
            GemstoneQuality.PERFECT, GemstoneSlot.ARMOR, 200, 0.5, Arrays.asList("§7+5 Defense"),
            Arrays.asList("§7- 2x Flawless Jasper", "§7- 1x Coal")
        ));

        // Onyx Gemstones
        gemstoneConfigs.put(GemstoneType.ONYX_ROUGH, new GemstoneConfig(
            "Rough Onyx", "§8Rough Onyx", Material.COAL,
            "§7A rough onyx gemstone that provides critical damage.",
            GemstoneQuality.ROUGH, GemstoneSlot.WEAPON, 10, 0.1, Arrays.asList("§7+1 Critical Damage"),
            Arrays.asList("§7- 1x Coal", "§7- 1x Stone")
        ));

        gemstoneConfigs.put(GemstoneType.ONYX_FLAWED, new GemstoneConfig(
            "Flawed Onyx", "§8Flawed Onyx", Material.COAL,
            "§7A flawed onyx gemstone that provides critical damage.",
            GemstoneQuality.FLAWED, GemstoneSlot.WEAPON, 25, 0.2, Arrays.asList("§7+2 Critical Damage"),
            Arrays.asList("§7- 2x Rough Onyx", "§7- 1x Stone")
        ));

        gemstoneConfigs.put(GemstoneType.ONYX_FINE, new GemstoneConfig(
            "Fine Onyx", "§8Fine Onyx", Material.COAL,
            "§7A fine onyx gemstone that provides critical damage.",
            GemstoneQuality.FINE, GemstoneSlot.WEAPON, 50, 0.3, Arrays.asList("§7+3 Critical Damage"),
            Arrays.asList("§7- 2x Flawed Onyx", "§7- 1x Stone")
        ));

        gemstoneConfigs.put(GemstoneType.ONYX_FLAWLESS, new GemstoneConfig(
            "Flawless Onyx", "§8Flawless Onyx", Material.COAL,
            "§7A flawless onyx gemstone that provides critical damage.",
            GemstoneQuality.FLAWLESS, GemstoneSlot.WEAPON, 100, 0.4, Arrays.asList("§7+4 Critical Damage"),
            Arrays.asList("§7- 2x Fine Onyx", "§7- 1x Stone")
        ));

        gemstoneConfigs.put(GemstoneType.ONYX_PERFECT, new GemstoneConfig(
            "Perfect Onyx", "§8Perfect Onyx", Material.COAL,
            "§7A perfect onyx gemstone that provides critical damage.",
            GemstoneQuality.PERFECT, GemstoneSlot.WEAPON, 200, 0.5, Arrays.asList("§7+5 Critical Damage"),
            Arrays.asList("§7- 2x Flawless Onyx", "§7- 1x Stone")
        ));
    }

    private void startGemstoneUpdateTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, PlayerGemstones> entry : playerGemstones.entrySet()) {
                    PlayerGemstones gemstones = entry.getValue();
                    gemstones.update();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if player is interacting with a gemstone table
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
            // Open gemstone GUI
            openGemstoneGUI(player);
        }
    }

    private void openGemstoneGUI(Player player) {
        // This would open a custom GUI for gemstones
        player.sendMessage("§aGemstone GUI geöffnet!");
    }

    public boolean upgradeGemstone(Player player, GemstoneType currentGemstone, GemstoneType targetGemstone) {
        GemstoneConfig currentConfig = gemstoneConfigs.get(currentGemstone);
        GemstoneConfig targetConfig = gemstoneConfigs.get(targetGemstone);

        if (currentConfig == null || targetConfig == null) {
            player.sendMessage("§cGemstone nicht gefunden!");
            return false;
        }

        // Check if upgrade is valid
        if (!isValidUpgrade(currentGemstone, targetGemstone)) {
            player.sendMessage("§cUngültiges Upgrade!");
            return false;
        }

        // Check if player has enough materials
        if (!hasUpgradeMaterials(player, targetConfig)) {
            player.sendMessage("§cDu hast nicht genug Materialien für " + targetConfig.getName() + "!");
            return false;
        }

        // Check success rate
        if (Math.random() > targetConfig.getSuccessRate()) {
            player.sendMessage("§cUpgrade fehlgeschlagen! Materialien wurden verbraucht.");
            consumeUpgradeMaterials(player, targetConfig);
            return false;
        }

        // Create upgraded gemstone
        ItemStack upgradedGemstone = createGemstoneItem(targetConfig);

        // Give gemstone to player
        player.getInventory().addItem(upgradedGemstone);

        // Consume materials
        consumeUpgradeMaterials(player, targetConfig);

        // Update player gemstone stats
        PlayerGemstones playerGemstones = getPlayerGemstones(player.getUniqueId());
        playerGemstones.addGemstone(targetGemstone);

        player.sendMessage("§a" + targetConfig.getName() + " erfolgreich hergestellt!");

        // Update database
        databaseManager.executeUpdate("""
            INSERT INTO player_gemstones (uuid, gemstone_type, quality, success, timestamp)
            VALUES (?, ?, ?, true, NOW())
        """, player.getUniqueId(), targetGemstone.name(), targetConfig.getQuality().name());

        return true;
    }

    private boolean isValidUpgrade(GemstoneType current, GemstoneType target) {
        // Check if target is a valid upgrade from current
        GemstoneConfig currentConfig = gemstoneConfigs.get(current);
        GemstoneConfig targetConfig = gemstoneConfigs.get(target);

        if (currentConfig == null || targetConfig == null) return false;

        // Check if they are the same gemstone type
        String currentName = current.name().split("_")[0];
        String targetName = target.name().split("_")[0];

        if (!currentName.equals(targetName)) return false;

        // Check if target is the next quality level
        GemstoneQuality currentQuality = currentConfig.getQuality();
        GemstoneQuality targetQuality = targetConfig.getQuality();

        return targetQuality.ordinal() == currentQuality.ordinal() + 1;
    }

    private boolean hasUpgradeMaterials(Player player, GemstoneConfig config) {
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
        if (material.contains("Redstone")) {
            return player.getInventory().contains(Material.REDSTONE);
        } else if (material.contains("Lapis Lazuli")) {
            return player.getInventory().contains(Material.LAPIS_LAZULI);
        } else if (material.contains("Emerald")) {
            return player.getInventory().contains(Material.EMERALD);
        } else if (material.contains("Amethyst Shard")) {
            return player.getInventory().contains(Material.AMETHYST_SHARD);
        } else if (material.contains("Gold Ingot")) {
            return player.getInventory().contains(Material.GOLD_INGOT);
        } else if (material.contains("Iron Ingot")) {
            return player.getInventory().contains(Material.IRON_INGOT);
        } else if (material.contains("Coal")) {
            return player.getInventory().contains(Material.COAL);
        } else if (material.contains("Stone")) {
            return player.getInventory().contains(Material.STONE);
        }
        return false;
    }

    private void consumeUpgradeMaterials(Player player, GemstoneConfig config) {
        // Consume materials from player inventory
        for (String material : config.getMaterials()) {
            consumeMaterial(player, material);
        }
    }

    private void consumeMaterial(Player player, String material) {
        // Parse material string and consume it
        if (material.contains("Redstone")) {
            player.getInventory().removeItem(new ItemStack(Material.REDSTONE, 1));
        } else if (material.contains("Lapis Lazuli")) {
            player.getInventory().removeItem(new ItemStack(Material.LAPIS_LAZULI, 1));
        } else if (material.contains("Emerald")) {
            player.getInventory().removeItem(new ItemStack(Material.EMERALD, 1));
        } else if (material.contains("Amethyst Shard")) {
            player.getInventory().removeItem(new ItemStack(Material.AMETHYST_SHARD, 1));
        } else if (material.contains("Gold Ingot")) {
            player.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 1));
        } else if (material.contains("Iron Ingot")) {
            player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 1));
        } else if (material.contains("Coal")) {
            player.getInventory().removeItem(new ItemStack(Material.COAL, 1));
        } else if (material.contains("Stone")) {
            player.getInventory().removeItem(new ItemStack(Material.STONE, 1));
        }
    }

    private ItemStack createGemstoneItem(GemstoneConfig config) {
        ItemStack gemstone = new ItemStack(config.getIcon());
        ItemMeta meta = gemstone.getItemMeta();

        if (meta == null) return gemstone;

        // Set display name
        meta.displayName(Component.text(config.getDisplayName()));

        // Set lore
        List<String> lore = new ArrayList<>();
        lore.add(config.getDescription());
        lore.add("");
        lore.addAll(config.getEffects());
        lore.add("");
        lore.add(ChatColor.GRAY + "Quality: " + config.getQuality().getDisplayName());
        lore.add(ChatColor.GRAY + "Slot: " + config.getSlot().getDisplayName());
        lore.add(ChatColor.GRAY + "Success Rate: " + String.format("%.0f", config.getSuccessRate() * 100) + "%");

        meta.lore(lore.stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
        gemstone.setItemMeta(meta);

        return gemstone;
    }

    public PlayerGemstones getPlayerGemstones(UUID playerId) {
        return playerGemstones.computeIfAbsent(playerId, k -> new PlayerGemstones(playerId));
    }

    public GemstoneConfig getGemstoneConfig(GemstoneType type) {
        return gemstoneConfigs.get(type);
    }

    public List<GemstoneType> getAllGemstoneTypes() {
        return new ArrayList<>(gemstoneConfigs.keySet());
    }

    // Gemstone Type Enum
    public enum GemstoneType {
        // Ruby Gemstones
        RUBY_ROUGH, RUBY_FLAWED, RUBY_FINE, RUBY_FLAWLESS, RUBY_PERFECT,

        // Sapphire Gemstones
        SAPPHIRE_ROUGH, SAPPHIRE_FLAWED, SAPPHIRE_FINE, SAPPHIRE_FLAWLESS, SAPPHIRE_PERFECT,

        // Emerald Gemstones
        EMERALD_ROUGH, EMERALD_FLAWED, EMERALD_FINE, EMERALD_FLAWLESS, EMERALD_PERFECT,

        // Amethyst Gemstones
        AMETHYST_ROUGH, AMETHYST_FLAWED, AMETHYST_FINE, AMETHYST_FLAWLESS, AMETHYST_PERFECT,

        // Topaz Gemstones
        TOPAZ_ROUGH, TOPAZ_FLAWED, TOPAZ_FINE, TOPAZ_FLAWLESS, TOPAZ_PERFECT,

        // Jasper Gemstones
        JASPER_ROUGH, JASPER_FLAWED, JASPER_FINE, JASPER_FLAWLESS, JASPER_PERFECT,

        // Onyx Gemstones
        ONYX_ROUGH, ONYX_FLAWED, ONYX_FINE, ONYX_FLAWLESS, ONYX_PERFECT
    }

    // Gemstone Quality Enum
    public enum GemstoneQuality {
        ROUGH("§7Rough", 1.0),
        FLAWED("§fFlawed", 1.5),
        FINE("§aFine", 2.0),
        FLAWLESS("§bFlawless", 2.5),
        PERFECT("§6Perfect", 3.0);

        private final String displayName;
        private final double multiplier;

        GemstoneQuality(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    // Gemstone Slot Enum
    public enum GemstoneSlot {
        WEAPON("§cWeapon", 1.0),
        ARMOR("§bArmor", 1.2),
        ACCESSORY("§6Accessory", 1.5),
        TOOL("§aTool", 1.1);

        private final String displayName;
        private final double multiplier;

        GemstoneSlot(String displayName, double multiplier) {
            this.displayName = displayName;
            this.multiplier = multiplier;
        }

        public String getDisplayName() { return displayName; }
        public double getMultiplier() { return multiplier; }
    }

    // Gemstone Config Class
    public static class GemstoneConfig {
        private final String name;
        private final String displayName;
        private final Material icon;
        private final String description;
        private final GemstoneQuality quality;
        private final GemstoneSlot slot;
        private final int cost;
        private final double successRate;
        private final List<String> effects;
        private final List<String> materials;

        public GemstoneConfig(String name, String displayName, Material icon, String description,
                            GemstoneQuality quality, GemstoneSlot slot, int cost, double successRate,
                            List<String> effects, List<String> materials) {
            this.name = name;
            this.displayName = displayName;
            this.icon = icon;
            this.description = description;
            this.quality = quality;
            this.slot = slot;
            this.cost = cost;
            this.successRate = successRate;
            this.effects = effects;
            this.materials = materials;
        }

        public String getName() { return name; }
        public String getDisplayName() { return displayName; }
        public Material getIcon() { return icon; }
        public String getDescription() { return description; }
        public GemstoneQuality getQuality() { return quality; }
        public GemstoneSlot getSlot() { return slot; }
        public int getCost() { return cost; }
        public double getSuccessRate() { return successRate; }
        public List<String> getEffects() { return effects; }
        public List<String> getMaterials() { return materials; }
    }

    // Player Gemstones Class
    public static class PlayerGemstones {
        private final UUID playerId;
        private final Map<GemstoneType, Integer> gemstoneCounts = new ConcurrentHashMap<>();
        private final Map<GemstoneQuality, Integer> qualityCounts = new ConcurrentHashMap<>();
        private int totalGemstones = 0;
        private int successfulUpgrades = 0;
        private int failedUpgrades = 0;
        private long lastUpdate;

        public PlayerGemstones(UUID playerId) {
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
            // Save gemstone statistics to database
            // This would integrate with the database system
        }

        public void addGemstone(GemstoneType gemstoneType) {
            gemstoneCounts.put(gemstoneType, gemstoneCounts.getOrDefault(gemstoneType, 0) + 1);

            // Get quality from gemstone type
            String qualityName = gemstoneType.name().split("_")[1];
            GemstoneQuality quality = GemstoneQuality.valueOf(qualityName);
            qualityCounts.put(quality, qualityCounts.getOrDefault(quality, 0) + 1);

            totalGemstones++;
            successfulUpgrades++;
        }

        public void addFailedUpgrade() {
            totalGemstones++;
            failedUpgrades++;
        }

        public int getGemstoneCount(GemstoneType gemstoneType) {
            return gemstoneCounts.getOrDefault(gemstoneType, 0);
        }

        public int getQualityCount(GemstoneQuality quality) {
            return qualityCounts.getOrDefault(quality, 0);
        }

        public double getSuccessRate() {
            return totalGemstones > 0 ? (double) successfulUpgrades / totalGemstones * 100 : 0.0;
        }

        // Getters
        public UUID getPlayerId() { return playerId; }
        public Map<GemstoneType, Integer> getGemstoneCounts() { return gemstoneCounts; }
        public Map<GemstoneQuality, Integer> getQualityCounts() { return qualityCounts; }
        public int getTotalGemstones() { return totalGemstones; }
        public int getSuccessfulUpgrades() { return successfulUpgrades; }
        public int getFailedUpgrades() { return failedUpgrades; }
    }
}
