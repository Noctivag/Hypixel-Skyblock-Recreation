package de.noctivag.plugin.features.menu;

import de.noctivag.plugin.core.api.Service;
import de.noctivag.plugin.core.api.SystemStatus;
import de.noctivag.plugin.features.menu.config.MenuConfig;
import de.noctivag.plugin.features.menu.types.MenuType;
import de.noctivag.plugin.features.menu.types.MenuButton;
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
    private SystemStatus status = SystemStatus.UNINITIALIZED;
    private final Map<String, MenuConfig> menuConfigs = new HashMap<>();
    
    @Override
    public CompletableFuture<Void> initialize() {
        status = SystemStatus.ENABLED;
        initializeAllMenus();
        return CompletableFuture.completedFuture(null);
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        status = SystemStatus.UNINITIALIZED;
        menuConfigs.clear();
        return CompletableFuture.completedFuture(null);
    }
    
    @Override
    public boolean isInitialized() {
        return status == SystemStatus.ENABLED;
    }
    
    @Override
    public String getName() {
        return "SkyblockMenuSystem";
    }
    
    /**
     * Initialize all menu configurations based on Hypixel SkyBlock Wiki
     */
    private void initializeAllMenus() {
        // Main SkyBlock Menu - simplified implementation
        MenuConfig mainMenu = new MenuConfig("main_menu", "Main Menu", "§6§lSkyBlock Menü", Material.CHEST, 54, 
            Arrays.asList("Main SkyBlock menu"), new HashMap<>(), new ArrayList<>());
        
        // Store the menu configuration
        menuConfigs.put("main_menu", mainMenu);
        
        // Profile Menu
        MenuConfig profileMenu = new MenuConfig("profile_menu", "Profile Menu", "§6§lIhr SkyBlock-Profil", Material.PLAYER_HEAD, 54,
            Arrays.asList("Profile menu"), new HashMap<>(), new ArrayList<>());
        menuConfigs.put("profile_menu", profileMenu);
        
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
            player.sendMessage("§cMenu not found!");
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
            player.sendMessage("§cProfile menu not found!");
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
            player.sendMessage("§cWeapons menu not found!");
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
            player.sendMessage("§cSwords menu not found!");
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
            profileMeta.setDisplayName("§6§lIhr SkyBlock-Profil");
            profileMeta.setLore(Arrays.asList("§7Zeigen Sie Ihre Skills, Stats", "§7und Collections an"));
            profileItem.setItemMeta(profileMeta);
            inventory.setItem(10, profileItem);
            
            // Skills button
            ItemStack skillsItem = new ItemStack(Material.EXPERIENCE_BOTTLE);
            ItemMeta skillsMeta = skillsItem.getItemMeta();
            skillsMeta.setDisplayName("§a§lSkills");
            skillsMeta.setLore(Arrays.asList("§7Zeigen Sie Ihre Skill-Level", "§7und Fortschritte an"));
            skillsItem.setItemMeta(skillsMeta);
            inventory.setItem(12, skillsItem);
            
            // Collections button
            ItemStack collectionsItem = new ItemStack(Material.BOOK);
            ItemMeta collectionsMeta = collectionsItem.getItemMeta();
            collectionsMeta.setDisplayName("§e§lCollections");
            collectionsMeta.setLore(Arrays.asList("§7Zeigen Sie Ihre Collections", "§7und Fortschritte an"));
            collectionsItem.setItemMeta(collectionsMeta);
            inventory.setItem(14, collectionsItem);
            
            // Pets button
            ItemStack petsItem = new ItemStack(Material.BONE);
            ItemMeta petsMeta = petsItem.getItemMeta();
            petsMeta.setDisplayName("§d§lPets");
            petsMeta.setLore(Arrays.asList("§7Verwalten Sie Ihre Pets"));
            petsItem.setItemMeta(petsMeta);
            inventory.setItem(16, petsItem);
            
            // Weapons button
            ItemStack weaponsItem = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta weaponsMeta = weaponsItem.getItemMeta();
            weaponsMeta.setDisplayName("§c§lWaffen");
            weaponsMeta.setLore(Arrays.asList("§7Zeigen Sie Ihre Waffen", "§7und Rüstungen an"));
            weaponsItem.setItemMeta(weaponsMeta);
            inventory.setItem(28, weaponsItem);
            
            // Island button
            ItemStack islandItem = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta islandMeta = islandItem.getItemMeta();
            islandMeta.setDisplayName("§2§lInsel");
            islandMeta.setLore(Arrays.asList("§7Verwalten Sie Ihre Insel"));
            islandItem.setItemMeta(islandMeta);
            inventory.setItem(30, islandItem);
            
            // Minions button
            ItemStack minionsItem = new ItemStack(Material.VILLAGER_SPAWN_EGG);
            ItemMeta minionsMeta = minionsItem.getItemMeta();
            minionsMeta.setDisplayName("§b§lMinions");
            minionsMeta.setLore(Arrays.asList("§7Verwalten Sie Ihre Minions"));
            minionsItem.setItemMeta(minionsMeta);
            inventory.setItem(32, minionsItem);
            
            // Slayer button
            ItemStack slayerItem = new ItemStack(Material.ROTTEN_FLESH);
            ItemMeta slayerMeta = slayerItem.getItemMeta();
            slayerMeta.setDisplayName("§4§lSlayer");
            slayerMeta.setLore(Arrays.asList("§7Slayer Quests und Bosse"));
            slayerItem.setItemMeta(slayerMeta);
            inventory.setItem(34, slayerItem);
            
            // Dungeons button
            ItemStack dungeonsItem = new ItemStack(Material.STONE_BRICKS);
            ItemMeta dungeonsMeta = dungeonsItem.getItemMeta();
            dungeonsMeta.setDisplayName("§5§lDungeons");
            dungeonsMeta.setLore(Arrays.asList("§7Dungeon Runs und Bosse"));
            dungeonsItem.setItemMeta(dungeonsMeta);
            inventory.setItem(46, dungeonsItem);
        }
        
        return inventory;
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
