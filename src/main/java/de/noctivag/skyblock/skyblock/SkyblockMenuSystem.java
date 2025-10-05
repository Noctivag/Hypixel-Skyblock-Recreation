package de.noctivag.skyblock.skyblock;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.*;

/**
 * Hypixel Skyblock-style Menu System
 * Features:
 * - Main Skyblock menu with all major features
 * - Skills menu with progress bars
 * - Collections menu with unlockable rewards
 * - Profile menu with statistics
 * - Fast travel menu
 * - Settings and customization
 */
public class SkyblockMenuSystem implements Listener {

    private final HealthManaSystem healthManaSystem;
    private final Map<String, String> travelMap = new HashMap<>();

    public SkyblockMenuSystem(SkyblockPlugin SkyblockPlugin, HealthManaSystem healthManaSystem) {
        this.healthManaSystem = healthManaSystem;

        // Initialize travel destinations mapping (display -> world name)
        travelMap.put("Hub", "hub");
        travelMap.put("Deep Caverns", "deep_caverns");
        travelMap.put("Blazing Fortress", "blazing_fortress");
        travelMap.put("The End", "the_end");
        travelMap.put("Jerry's Workshop", "jerrys_workshop");
        travelMap.put("Desert", "desert");
        travelMap.put("Frozen", "frozen");
        travelMap.put("Mushroom Desert", "mushroom_desert");
        travelMap.put("Spider's Den", "spiders_den");
        travelMap.put("Fishing Area", "fishing_area");
        travelMap.put("Dungeon Hub", "dungeon_hub");
        travelMap.put("Dragon's Nest", "dragons_nest");

        // register using the SkyblockPlugin instance passed in
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }

    public void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lSkyBlock Menu"));

        // Main navigation items
        addMenuItem(gui, 10, Material.DIAMOND_SWORD, "§c§lSkills",
            Arrays.asList("§7View your skill progress", "§7and unlock new abilities", "", "§eClick to open!"));

        addMenuItem(gui, 11, Material.CHEST, "§a§lCollections",
            Arrays.asList("§7Track your collection progress", "§7and unlock new recipes", "", "§eClick to open!"));

        addMenuItem(gui, 12, Material.PLAYER_HEAD, "§b§lProfile",
            Arrays.asList("§7View your player statistics", "§7and achievements", "", "§eClick to open!"));

        addMenuItem(gui, 13, Material.ENDER_PEARL, "§d§lFast Travel",
            Arrays.asList("§7Quickly travel between", "§7different locations", "", "§eClick to open!"));

        addMenuItem(gui, 14, Material.IRON_GOLEM_SPAWN_EGG, "§e§lMinions",
            Arrays.asList("§7Manage your minions", "§7and their upgrades", "", "§eClick to open!"));

        addMenuItem(gui, 15, Material.WOLF_SPAWN_EGG, "§6§lPets",
            Arrays.asList("§7View and manage your pets", "§7and their abilities", "", "§eClick to open!"));

        addMenuItem(gui, 16, Material.EMERALD, "§2§lBanking",
            Arrays.asList("§7Access your bank account", "§7and manage your coins", "", "§eClick to open!"));

        addMenuItem(gui, 19, Material.ENCHANTED_BOOK, "§5§lQuests",
            Arrays.asList("§7View available quests", "§7and track your progress", "", "§eClick to open!"));

        addMenuItem(gui, 20, Material.ANVIL, "§7§lCrafting",
            Arrays.asList("§7Access crafting recipes", "§7and create new items", "", "§eClick to open!"));

        addMenuItem(gui, 21, Material.GOLD_INGOT, "§6§lAuction House",
            Arrays.asList("§7Buy and sell items", "§7with other players", "", "§eClick to open!"));

        addMenuItem(gui, 22, Material.BOOK, "§9§lRecipe Book",
            Arrays.asList("§7View all unlocked recipes", "§7and crafting guides", "", "§eClick to open!"));

        addMenuItem(gui, 23, Material.COMPASS, "§3§lBestiary",
            Arrays.asList("§7Track defeated mobs", "§7and unlock rewards", "", "§eClick to open!"));

        addMenuItem(gui, 24, Material.NETHER_STAR, "§c§lSlayers",
            Arrays.asList("§7Fight powerful bosses", "§7and earn unique rewards", "", "§eClick to open!"));

        addMenuItem(gui, 25, Material.ENDER_CHEST, "§5§lDungeons",
            Arrays.asList("§7Explore dangerous dungeons", "§7and find rare loot", "", "§eClick to open!"));

        // Player info section
        addPlayerInfo(gui, player);

        // Navigation
        addMenuItem(gui, 45, Material.ARROW, "§7§lPrevious Page", Collections.singletonList("§7Go to previous page"));
        addMenuItem(gui, 49, Material.BARRIER, "§c§lClose", Collections.singletonList("§7Close the menu"));
        addMenuItem(gui, 53, Material.ARROW, "§7§lNext Page", Collections.singletonList("§7Go to next page"));

        player.openInventory(gui);
    }

    public void openSkillsMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§c§lSkills"));

        // Skill categories
        addSkillItem(gui, 10, Material.DIAMOND_SWORD, "§c§lCombat",
            "§7Fight monsters and bosses", 25, 100, player);

        addSkillItem(gui, 11, Material.DIAMOND_PICKAXE, "§7§lMining",
            "§7Mine ores and gems", 15, 100, player);

        addSkillItem(gui, 12, Material.WHEAT, "§e§lFarming",
            "§7Grow crops and plants", 8, 100, player);

        addSkillItem(gui, 13, Material.OAK_LOG, "§6§lForaging",
            "§7Chop trees and gather wood", 12, 100, player);

        addSkillItem(gui, 14, Material.FISHING_ROD, "§b§lFishing",
            "§7Catch fish and sea creatures", 5, 100, player);

        addSkillItem(gui, 15, Material.ENCHANTING_TABLE, "§d§lEnchanting",
            "§7Enchant items and books", 3, 100, player);

        addSkillItem(gui, 16, Material.BREWING_STAND, "§5§lAlchemy",
            "§7Brew potions and elixirs", 2, 100, player);

        addSkillItem(gui, 19, Material.ANVIL, "§7§lCarpentry",
            "§7Craft furniture and blocks", 1, 100, player);

        addSkillItem(gui, 20, Material.ENCHANTING_TABLE, "§9§lRunecrafting",
            "§7Create magical runes", 0, 100, player);

        addSkillItem(gui, 21, Material.TOTEM_OF_UNDYING, "§c§lTaming",
            "§7Bond with pets and mounts", 4, 100, player);

        addSkillItem(gui, 22, Material.HEART_OF_THE_SEA, "§3§lSocial",
            "§7Interact with other players", 1, 100, player);

        addSkillItem(gui, 23, Material.DRAGON_EGG, "§6§lCatacombs",
            "§7Explore the catacombs", 0, 100, player);

        // Navigation
        addMenuItem(gui, 45, Material.ARROW, "§7§lBack", Collections.singletonList("§7Return to main menu"));
        addMenuItem(gui, 49, Material.BARRIER, "§c§lClose", Collections.singletonList("§7Close the menu"));

        player.openInventory(gui);
    }

    public void openCollectionsMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§a§lCollections"));

        // Collection categories
        addCollectionItem(gui, 10, Material.WHEAT, "§e§lWheat",
            "§7Wheat Collection", 150, 1000, player);

        addCollectionItem(gui, 11, Material.CARROT, "§6§lCarrot",
            "§7Carrot Collection", 75, 500, player);

        addCollectionItem(gui, 12, Material.POTATO, "§e§lPotato",
            "§7Potato Collection", 200, 1000, player);

        addCollectionItem(gui, 13, Material.PUMPKIN, "§6§lPumpkin",
            "§7Pumpkin Collection", 50, 500, player);

        addCollectionItem(gui, 14, Material.MELON, "§a§lMelon",
            "§7Melon Collection", 100, 1000, player);

        addCollectionItem(gui, 15, Material.SUGAR_CANE, "§e§lSugar Cane",
            "§7Sugar Cane Collection", 300, 2000, player);

        addCollectionItem(gui, 16, Material.CACTUS, "§a§lCactus",
            "§7Cactus Collection", 80, 500, player);

        addCollectionItem(gui, 19, Material.COBBLESTONE, "§7§lCobblestone",
            "§7Cobblestone Collection", 500, 5000, player);

        addCollectionItem(gui, 20, Material.COAL, "§8§lCoal",
            "§7Coal Collection", 200, 2000, player);

        addCollectionItem(gui, 21, Material.IRON_INGOT, "§f§lIron",
            "§7Iron Collection", 150, 1500, player);

        addCollectionItem(gui, 22, Material.GOLD_INGOT, "§6§lGold",
            "§7Gold Collection", 100, 1000, player);

        addCollectionItem(gui, 23, Material.DIAMOND, "§b§lDiamond",
            "§7Diamond Collection", 50, 500, player);

        addCollectionItem(gui, 24, Material.EMERALD, "§a§lEmerald",
            "§7Emerald Collection", 30, 300, player);

        addCollectionItem(gui, 25, Material.REDSTONE, "§c§lRedstone",
            "§7Redstone Collection", 400, 4000, player);

        addCollectionItem(gui, 28, Material.LAPIS_LAZULI, "§9§lLapis Lazuli",
            "§7Lapis Lazuli Collection", 250, 2500, player);

        addCollectionItem(gui, 29, Material.QUARTZ, "§f§lQuartz",
            "§7Quartz Collection", 120, 1200, player);

        addCollectionItem(gui, 30, Material.OBSIDIAN, "§5§lObsidian",
            "§7Obsidian Collection", 25, 250, player);

        // Navigation
        addMenuItem(gui, 45, Material.ARROW, "§7§lBack", Collections.singletonList("§7Return to main menu"));
        addMenuItem(gui, 49, Material.BARRIER, "§c§lClose", Collections.singletonList("§7Close the menu"));

        player.openInventory(gui);
    }

    public void openProfileMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§b§lProfile - " + player.getName()));

        // Player statistics
        addStatItem(gui, 10, Material.DIAMOND_SWORD, "§c§lCombat Level",
            "§7Level: §e25", "§7XP: §a1,250 / 2,000");

        addStatItem(gui, 11, Material.DIAMOND_PICKAXE, "§7§lMining Level",
            "§7Level: §e15", "§7XP: §a750 / 1,000");

        addStatItem(gui, 12, Material.WHEAT, "§e§lFarming Level",
            "§7Level: §e8", "§7XP: §a400 / 500");

        addStatItem(gui, 13, Material.OAK_LOG, "§6§lForaging Level",
            "§7Level: §e12", "§7XP: §a600 / 800");

        addStatItem(gui, 14, Material.FISHING_ROD, "§b§lFishing Level",
            "§7Level: §e5", "§7XP: §a250 / 300");

        // Health and Mana info
        double currentHealth = healthManaSystem.getCurrentHealth(player);
        double maxHealth = healthManaSystem.getMaxHealth(player);
        double currentMana = healthManaSystem.getCurrentMana(player);
        double maxMana = healthManaSystem.getMaxMana(player);

        addStatItem(gui, 19, Material.RED_DYE, "§c§lHealth",
            "§7Current: §c" + String.format("%.0f", currentHealth),
            "§7Maximum: §c" + String.format("%.0f", maxHealth));

        addStatItem(gui, 20, Material.LAPIS_LAZULI, "§b§lMana",
            "§7Current: §b" + String.format("%.0f", currentMana),
            "§7Maximum: §b" + String.format("%.0f", maxMana));

        // Other stats
        addStatItem(gui, 21, Material.EMERALD, "§a§lCoins",
            "§7Balance: §a1,250,000", "§7Bank: §a5,000,000");

        addStatItem(gui, 22, Material.EXPERIENCE_BOTTLE, "§e§lSkyBlock Level",
            "§7Level: §e42", "§7XP: §a2,100 / 3,000");

        addStatItem(gui, 23, Material.TOTEM_OF_UNDYING, "§6§lPets",
            "§7Active: §eWolf", "§7Total: §a5 pets");

        addStatItem(gui, 24, Material.IRON_GOLEM_SPAWN_EGG, "§e§lMinions",
            "§7Active: §e8/10", "§7Total: §a15 minions");

        // Navigation
        addMenuItem(gui, 45, Material.ARROW, "§7§lBack", Collections.singletonList("§7Return to main menu"));
        addMenuItem(gui, 49, Material.BARRIER, "§c§lClose", Collections.singletonList("§7Close the menu"));

        player.openInventory(gui);
    }

    public void openFastTravelMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§d§lFast Travel"));

        // Travel destinations
        addTravelItem(gui, 10, Material.GRASS_BLOCK, "§a§lHub",
            "§7The main hub island", "§7Cost: §aFree");

        addTravelItem(gui, 11, Material.DIAMOND_ORE, "§7§lDeep Caverns",
            "§7Mining area with rare ores", "§7Cost: §aFree");

        addTravelItem(gui, 12, Material.NETHERRACK, "§c§lBlazing Fortress",
            "§7Nether-themed combat area", "§7Cost: §aFree");

        addTravelItem(gui, 13, Material.END_STONE, "§5§lThe End",
            "§7End dimension with dragons", "§7Cost: §aFree");

        addTravelItem(gui, 14, Material.SNOW_BLOCK, "§f§lJerry's Workshop",
            "§7Special event area", "§7Cost: §aFree");

        addTravelItem(gui, 15, Material.SAND, "§e§lDesert",
            "§7Desert biome with temples", "§7Cost: §aFree");

        addTravelItem(gui, 16, Material.ICE, "§b§lFrozen",
            "§7Ice biome with unique mobs", "§7Cost: §aFree");

        addTravelItem(gui, 19, Material.MYCELIUM, "§d§lMushroom Desert",
            "§7Mushroom biome area", "§7Cost: §aFree");

        addTravelItem(gui, 20, Material.SOUL_SAND, "§8§lSpider's Den",
            "§7Spider-themed combat area", "§7Cost: §aFree");

        addTravelItem(gui, 21, Material.PRISMARINE, "§3§lFishing Area",
            "§7Dedicated fishing location", "§7Cost: §aFree");

        addTravelItem(gui, 22, Material.OBSIDIAN, "§5§lDungeon Hub",
            "§7Access to all dungeons", "§7Cost: §aFree");

        addTravelItem(gui, 23, Material.DRAGON_EGG, "§6§lDragon's Nest",
            "§7Dragon boss area", "§7Cost: §aFree");

        // Navigation
        addMenuItem(gui, 45, Material.ARROW, "§7§lBack", Collections.singletonList("§7Return to main menu"));
        addMenuItem(gui, 49, Material.BARRIER, "§c§lClose", Collections.singletonList("§7Close the menu"));

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(event.getView().title());

        if (!title.contains("SkyBlock Menu") && !title.contains("Skills") &&
            !title.contains("Collections") && !title.contains("Profile") &&
            !title.contains("Fast Travel")) {
            return;
        }

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        ItemMeta clickedMeta = clicked.getItemMeta();
        String displayName = (clickedMeta != null && clickedMeta.displayName() != null) ?
            net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(clickedMeta.displayName()) : "";

        // Handle main menu clicks
        if (title.contains("SkyBlock Menu")) {
            handleMainMenuClick(player, displayName);
        }
        // Handle other menu clicks
        else if (title.contains("Skills")) {
            handleSkillsMenuClick(player, displayName);
        }
        else if (title.contains("Collections")) {
            handleCollectionsMenuClick(player, displayName);
        }
        else if (title.contains("Profile")) {
            handleProfileMenuClick(player, displayName);
        }
        else if (title.contains("Fast Travel")) {
            handleFastTravelMenuClick(player, displayName);
        }
    }

    private void handleMainMenuClick(Player player, String displayName) {
        if (displayName.contains("Skills")) {
            openSkillsMenu(player);
        } else if (displayName.contains("Collections")) {
            openCollectionsMenu(player);
        } else if (displayName.contains("Profile")) {
            openProfileMenu(player);
        } else if (displayName.contains("Fast Travel")) {
            openFastTravelMenu(player);
        } else if (displayName.contains("Close")) {
            player.closeInventory();
        } else if (displayName.contains("Back")) {
            openMainMenu(player);
        }
    }

    private void handleSkillsMenuClick(Player player, String displayName) {
        if (displayName.contains("Back") || displayName.contains("Close")) {
            if (displayName.contains("Back")) {
                openMainMenu(player);
            } else {
                player.closeInventory();
            }
        } else {
            // Open specific skill details
            player.sendMessage("§aOpening " + displayName + " details...");
        }
    }

    private void handleCollectionsMenuClick(Player player, String displayName) {
        if (displayName.contains("Back") || displayName.contains("Close")) {
            if (displayName.contains("Back")) {
                openMainMenu(player);
            } else {
                player.closeInventory();
            }
        } else {
            // Open specific collection details
            player.sendMessage("§aOpening " + displayName + " collection...");
        }
    }

    private void handleProfileMenuClick(Player player, String displayName) {
        if (displayName.contains("Back") || displayName.contains("Close")) {
            if (displayName.contains("Back")) {
                openMainMenu(player);
            } else {
                player.closeInventory();
            }
        }
    }

    private void handleFastTravelMenuClick(Player player, String displayName) {
        if (displayName.contains("Back") || displayName.contains("Close")) {
            if (displayName.contains("Back")) {
                openMainMenu(player);
            } else {
                player.closeInventory();
            }
            return;
        }

        // Find a matching travel key in our map
        String key = null;
        for (String k : travelMap.keySet()) {
            if (displayName.contains(k)) { key = k; break; }
        }

        if (key == null) {
            player.sendMessage("§cUnknown travel destination: " + displayName);
            return;
        }

        String worldName = travelMap.get(key);
        var world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage("§cDas Ziel ist nicht verfügbar (Welt nicht gefunden): " + key);
            return;
        }

        player.sendMessage("§aTeleporting to " + key + "...");
        player.closeInventory();
        player.teleport(world.getSpawnLocation());
    }

    private void addMenuItem(Inventory gui, int slot, Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(name));
            List<Component> componentLore = new ArrayList<>();
            for (String line : lore) {
                componentLore.add(Component.text(line));
            }
            meta.lore(componentLore);
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }

    private void addSkillItem(Inventory gui, int slot, Material material, String name,
                             String description, int level, int maxLevel, Player player) {
        List<String> lore = new ArrayList<>();
        lore.add(description);
        lore.add("");
        lore.add("§7Level: §e" + level + "§7/§e" + maxLevel);
        lore.add("§7Progress: §a" + (level * 100 / maxLevel) + "%");
        lore.add("");
        lore.add("§eClick to view details!");

        addMenuItem(gui, slot, material, name, lore);
    }

    private void addCollectionItem(Inventory gui, int slot, Material material, String name,
                                  String description, int collected, int max, Player player) {
        List<String> lore = new ArrayList<>();
        lore.add(description);
        lore.add("");
        lore.add("§7Collected: §a" + collected + "§7/§a" + max);
        lore.add("§7Progress: §a" + (collected * 100 / max) + "%");
        lore.add("");
        lore.add("§eClick to view rewards!");

        addMenuItem(gui, slot, material, name, lore);
    }

    private void addStatItem(Inventory gui, int slot, Material material, String name,
                            String line1, String line2) {
        List<String> lore = Arrays.asList(line1, line2);
        addMenuItem(gui, slot, material, name, lore);
    }

    private void addTravelItem(Inventory gui, int slot, Material material, String name,
                              String description, String cost) {
        List<String> lore = Arrays.asList(description, "", cost, "", "§eClick to travel!");
        addMenuItem(gui, slot, material, name, lore);
    }

    private void addPlayerInfo(Inventory gui, Player player) {
        // Add player head and basic info
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = playerHead.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text("§b§l" + player.getName()));

            double currentHealth = healthManaSystem.getCurrentHealth(player);
            double maxHealth = healthManaSystem.getMaxHealth(player);
            double currentMana = healthManaSystem.getCurrentMana(player);
            double maxMana = healthManaSystem.getMaxMana(player);

            List<String> lore = Arrays.asList(
                "§7Health: §c" + String.format("%.0f", currentHealth) + "§7/§c" + String.format("%.0f", maxHealth),
                "§7Mana: §b" + String.format("%.0f", currentMana) + "§7/§b" + String.format("%.0f", maxMana),
                "§7Level: §e42",
                "§7Coins: §a1,250,000"
            );

            List<Component> componentLore = new ArrayList<>();
            for (String line : lore) {
                componentLore.add(Component.text(line));
            }
            meta.lore(componentLore);
            playerHead.setItemMeta(meta);
        }
        gui.setItem(4, playerHead);
    }
}
