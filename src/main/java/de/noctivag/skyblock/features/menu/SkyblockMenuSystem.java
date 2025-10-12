package de.noctivag.skyblock.features.menu;
import net.kyori.adventure.text.Component;

import java.util.stream.Collectors;
import de.noctivag.skyblock.core.api.Service;
import de.noctivag.skyblock.core.api.SystemStatus;
import de.noctivag.skyblock.features.menu.config.MenuConfig;
import de.noctivag.skyblock.features.menu.types.MenuType;
import de.noctivag.skyblock.features.menu.types.MenuButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * SkyBlock Menu System - Simplified Implementation
 */
public class SkyblockMenuSystem implements Service {
    // Event-Listener für Menü-Klicks (vereinfachte Variante)
    public void handleMenuClick(Player player, String menuId, int slot) {
        if (menuId.equals("main_menu")) {
            switch (slot) {
                case 14: // Collections
                    openCollectionsMenu(player);
                    break;
                case 15: // Stats
                    openStatsMenu(player);
                    break;
                case 17: // Recipe Book
                    openRecipeBookMenu(player);
                    break;
                case 19: // Leveling
                    openLevelingMenu(player);
                    break;
                default:
                    // Andere Slots: nichts tun oder Standardverhalten
                    break;
            }
        }
    }
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private final Map<String, MenuConfig> menuConfigs = new HashMap<>();

    // Menü-IDs für Untermenüs
    private static final String COLLECTIONS_MENU_ID = "collections_menu";
    private static final String STATS_MENU_ID = "stats_menu";
    private static final String RECIPE_BOOK_MENU_ID = "recipe_book_menu";
    private static final String LEVELING_MENU_ID = "leveling_menu";

    @Override
    public void initialize() {
        status = SystemStatus.RUNNING;
        initializeAllMenus();
    }

    @Override
    public void shutdown() {
        status = SystemStatus.UNINITIALIZED;
        menuConfigs.clear();
    }

    @Override
    public String getName() {
        return "SkyblockMenuSystem";
    }

    @Override
    public SystemStatus getStatus() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status == SystemStatus.RUNNING;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            status = SystemStatus.RUNNING;
        } else {
            status = SystemStatus.UNINITIALIZED;
        }
    }

    /**
     * Initialize all menu configurations based on Hypixel SkyBlock Wiki
     */
    private void initializeAllMenus() {
        // Main SkyBlock Menu
        MenuConfig mainMenu = new MenuConfig("main_menu", "Main Menu", "§6§lSkyBlock Menü", Material.CHEST, 54,
            Arrays.asList("Main SkyBlock menu"), new HashMap<>(), new ArrayList<>());
        menuConfigs.put("main_menu", mainMenu);

        // Profile Menu
        MenuConfig profileMenu = new MenuConfig("profile_menu", "Profile Menu", "§6§lIhr SkyBlock-Profil", Material.PLAYER_HEAD, 54,
            Arrays.asList("Profile menu"), new HashMap<>(), new ArrayList<>());
        menuConfigs.put("profile_menu", profileMenu);

        // Collections Menu
        MenuConfig collectionsMenu = new MenuConfig(COLLECTIONS_MENU_ID, "Collections Menu", "§e§lCollections", Material.BOOK, 54,
            Arrays.asList("Alle SkyBlock Collections"), new HashMap<>(), new ArrayList<>());
        menuConfigs.put(COLLECTIONS_MENU_ID, collectionsMenu);

        // Stats Menu
        MenuConfig statsMenu = new MenuConfig(STATS_MENU_ID, "Stats Menu", "§a§lStats", Material.PAPER, 54,
            Arrays.asList("Alle SkyBlock Stats"), new HashMap<>(), new ArrayList<>());
        menuConfigs.put(STATS_MENU_ID, statsMenu);

        // Recipe Book Menu
        MenuConfig recipeBookMenu = new MenuConfig(RECIPE_BOOK_MENU_ID, "Recipe Book", "§b§lRecipe Book", Material.CRAFTING_TABLE, 54,
            Arrays.asList("Alle freigeschalteten Rezepte"), new HashMap<>(), new ArrayList<>());
        menuConfigs.put(RECIPE_BOOK_MENU_ID, recipeBookMenu);

        // Leveling Menu
        MenuConfig levelingMenu = new MenuConfig(LEVELING_MENU_ID, "Leveling Menu", "§6§lLeveling", Material.EXPERIENCE_BOTTLE, 54,
            Arrays.asList("Leveling Übersicht"), new HashMap<>(), new ArrayList<>());
        menuConfigs.put(LEVELING_MENU_ID, levelingMenu);

        // Weapons Menu
        MenuConfig weaponsMenu = new MenuConfig("weapons_menu", "Weapons Menu", "§6§lWaffen", Material.DIAMOND_SWORD, 54,
            Arrays.asList("Weapons menu"), new HashMap<>(), new ArrayList<>());
        menuConfigs.put("weapons_menu", weaponsMenu);

        // Swords Menu
        MenuConfig swordsMenu = new MenuConfig("swords_menu", "Swords Menu", "§6§lSchwerter", Material.IRON_SWORD, 54,
            Arrays.asList("Swords menu"), new HashMap<>(), new ArrayList<>());
        menuConfigs.put("swords_menu", swordsMenu);
    }

    /**
     * Open main menu for player
     */
    public void openMainMenu(Player player) {
        MenuConfig menuConfig = menuConfigs.get("main_menu");
        if (menuConfig == null) {
            player.sendMessage(Component.text("§cMenu not found!"));
            return;
        }

        Inventory inventory = createInventoryFromConfig(menuConfig);
        player.openInventory(inventory);
    }

    /**
     * Open profile menu for player
     */
    public void openProfileMenu(Player player) {
        MenuConfig menuConfig = menuConfigs.get("profile_menu");
        if (menuConfig == null) {
            player.sendMessage(Component.text("§cProfile menu not found!"));
            return;
        }

        Inventory inventory = createInventoryFromConfig(menuConfig);
        player.openInventory(inventory);
    }

    /**
     * Open weapons menu for player
     */
    public void openWeaponsMenu(Player player) {
        MenuConfig menuConfig = menuConfigs.get("weapons_menu");
        if (menuConfig == null) {
            player.sendMessage(Component.text("§cWeapons menu not found!"));
            return;
        }

        Inventory inventory = createInventoryFromConfig(menuConfig);
        player.openInventory(inventory);
    }

    /**
     * Open swords menu for player
     */
    public void openSwordsMenu(Player player) {
        MenuConfig menuConfig = menuConfigs.get("swords_menu");
        if (menuConfig == null) {
            player.sendMessage(Component.text("§cSwords menu not found!"));
            return;
        }

        Inventory inventory = createInventoryFromConfig(menuConfig);
        player.openInventory(inventory);
    }

    /**
     * Create inventory from menu configuration
     */
    private Inventory createInventoryFromConfig(MenuConfig config) {
        Inventory inventory = org.bukkit.Bukkit.createInventory(null, config.getSize(), config.getDisplayName());

        // Add basic items based on menu type
        if (config.getId().equals("main_menu")) {
            // Profile button
            ItemStack profileItem = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta profileMeta = profileItem.getItemMeta();
            profileMeta.displayName(Component.text("§6§lIhr SkyBlock-Profil"));
            profileMeta.lore(Arrays.asList("§7Zeigen Sie Ihre Skills, Stats", "§7und Collections an").stream().map(Component::text).collect(Collectors.toList()));
            profileItem.setItemMeta(profileMeta);
            inventory.setItem(10, profileItem);

            // Skills button
            ItemStack skillsItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
            ItemMeta skillsMeta = skillsItem.getItemMeta();
            skillsMeta.displayName(Component.text("§a§lSkills"));
            skillsMeta.lore(Arrays.asList("§7Zeigen Sie Ihre Skill-Level", "§7und Fortschritte an").stream().map(Component::text).collect(Collectors.toList()));
            skillsItem.setItemMeta(skillsMeta);
            inventory.setItem(12, skillsItem);

            // Collections button (klickbar)
            ItemStack collectionsItem = new ItemStack(Material.BOOK);
            ItemMeta collectionsMeta = collectionsItem.getItemMeta();
            collectionsMeta.displayName(Component.text("§e§lCollections"));
            collectionsMeta.lore(Arrays.asList("§7Zeigen Sie Ihre Collections", "§7und Fortschritte an", "§e§oKlicken zum Öffnen!").stream().map(Component::text).collect(Collectors.toList()));
            collectionsItem.setItemMeta(collectionsMeta);
            inventory.setItem(14, collectionsItem);

            // Stats button (klickbar)
            ItemStack statsItem = new ItemStack(Material.PAPER);
            ItemMeta statsMeta = statsItem.getItemMeta();
            statsMeta.displayName(Component.text("§a§lStats"));
            statsMeta.lore(Arrays.asList("§7Zeigen Sie Ihre Statistiken", "§a§oKlicken zum Öffnen!").stream().map(Component::text).collect(Collectors.toList()));
            statsItem.setItemMeta(statsMeta);
            inventory.setItem(15, statsItem);

            // Recipe Book button (klickbar)
            ItemStack recipeBookItem = new ItemStack(Material.CRAFTING_TABLE);
            ItemMeta recipeBookMeta = recipeBookItem.getItemMeta();
            recipeBookMeta.displayName(Component.text("§b§lRecipe Book"));
            recipeBookMeta.lore(Arrays.asList("§7Alle freigeschalteten Rezepte", "§b§oKlicken zum Öffnen!").stream().map(Component::text).collect(Collectors.toList()));
            recipeBookItem.setItemMeta(recipeBookMeta);
            inventory.setItem(17, recipeBookItem);

            // Leveling button (klickbar)
            ItemStack levelingItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
            ItemMeta levelingMeta = levelingItem.getItemMeta();
            levelingMeta.displayName(Component.text("§6§lLeveling"));
            levelingMeta.lore(Arrays.asList("§7Leveling Übersicht", "§6§oKlicken zum Öffnen!").stream().map(Component::text).collect(Collectors.toList()));
            levelingItem.setItemMeta(levelingMeta);
            inventory.setItem(19, levelingItem);

            // Pets button
            ItemStack petsItem = new ItemStack(Material.BONE);
            ItemMeta petsMeta = petsItem.getItemMeta();
            petsMeta.displayName(Component.text("§d§lPets"));
            petsMeta.lore(Arrays.asList("§7Verwalten Sie Ihre Pets").stream().map(Component::text).collect(Collectors.toList()));
            petsItem.setItemMeta(petsMeta);
            inventory.setItem(16, petsItem);

            // Weapons button
            ItemStack weaponsItem = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta weaponsMeta = weaponsItem.getItemMeta();
            weaponsMeta.displayName(Component.text("§c§lWaffen"));
            weaponsMeta.lore(Arrays.asList("§7Zeigen Sie Ihre Waffen", "§7und Rüstungen an").stream().map(Component::text).collect(Collectors.toList()));
            weaponsItem.setItemMeta(weaponsMeta);
            inventory.setItem(28, weaponsItem);

            // Island button
            ItemStack islandItem = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta islandMeta = islandItem.getItemMeta();
            islandMeta.displayName(Component.text("§2§lInsel"));
            islandMeta.lore(Arrays.asList("§7Verwalten Sie Ihre Insel").stream().map(Component::text).collect(Collectors.toList()));
            islandItem.setItemMeta(islandMeta);
            inventory.setItem(30, islandItem);

            // Minions button
            ItemStack minionsItem = new ItemStack(Material.VILLAGER_SPAWN_EGG);
            ItemMeta minionsMeta = minionsItem.getItemMeta();
            minionsMeta.displayName(Component.text("§b§lMinions"));
            minionsMeta.lore(Arrays.asList("§7Verwalten Sie Ihre Minions").stream().map(Component::text).collect(Collectors.toList()));
            minionsItem.setItemMeta(minionsMeta);
            inventory.setItem(32, minionsItem);

            // Slayer button
            ItemStack slayerItem = new ItemStack(Material.ROTTEN_FLESH);
            ItemMeta slayerMeta = slayerItem.getItemMeta();
            slayerMeta.displayName(Component.text("§4§lSlayer"));
            slayerMeta.lore(Arrays.asList("§7Slayer Quests und Bosse").stream().map(Component::text).collect(Collectors.toList()));
            slayerItem.setItemMeta(slayerMeta);
            inventory.setItem(34, slayerItem);

            // Dungeons button
            ItemStack dungeonsItem = new ItemStack(Material.STONE_BRICKS);
            ItemMeta dungeonsMeta = dungeonsItem.getItemMeta();
            dungeonsMeta.displayName(Component.text("§5§lDungeons"));
            dungeonsMeta.lore(Arrays.asList("§7Dungeon Runs und Bosse").stream().map(Component::text).collect(Collectors.toList()));
            dungeonsItem.setItemMeta(dungeonsMeta);
            inventory.setItem(46, dungeonsItem);
        }
        return inventory;
    }

    // Untermenü-Öffner
    public void openCollectionsMenu(Player player) {
        MenuConfig menuConfig = menuConfigs.get(COLLECTIONS_MENU_ID);
        if (menuConfig == null) {
            player.sendMessage(Component.text("§cCollections-Menü nicht gefunden!"));
            return;
        }
        Inventory inventory = createInventoryFromConfig(menuConfig);
        player.openInventory(inventory);
    }

    public void openStatsMenu(Player player) {
        MenuConfig menuConfig = menuConfigs.get(STATS_MENU_ID);
        if (menuConfig == null) {
            player.sendMessage(Component.text("§cStats-Menü nicht gefunden!"));
            return;
        }
        Inventory inventory = createInventoryFromConfig(menuConfig);
        player.openInventory(inventory);
    }

    public void openRecipeBookMenu(Player player) {
        MenuConfig menuConfig = menuConfigs.get(RECIPE_BOOK_MENU_ID);
        if (menuConfig == null) {
            player.sendMessage(Component.text("§cRecipe Book-Menü nicht gefunden!"));
            return;
        }
        Inventory inventory = createInventoryFromConfig(menuConfig);
        player.openInventory(inventory);
    }

    public void openLevelingMenu(Player player) {
        MenuConfig menuConfig = menuConfigs.get(LEVELING_MENU_ID);
        if (menuConfig == null) {
            player.sendMessage(Component.text("§cLeveling-Menü nicht gefunden!"));
            return;
        }
        Inventory inventory = createInventoryFromConfig(menuConfig);
        player.openInventory(inventory);
    }

    /**
     * Get menu configuration by ID
     */
    public MenuConfig getMenuConfig(String id) {
        return menuConfigs.get(id);
    }

    /**
     * Get all menu configurations
     */
    public Map<String, MenuConfig> getAllMenuConfigs() {
        return new HashMap<>(menuConfigs);
    }
}
