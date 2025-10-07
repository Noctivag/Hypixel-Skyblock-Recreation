package de.noctivag.skyblock.skyblock;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Skyblock Menu System - Handles main menu GUI
 */
public class SkyblockMenuSystem {
    
    private final SkyblockPlugin plugin;
    
    public SkyblockMenuSystem(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Open main menu for player
     */
    public void openMainMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "§6Skyblock Menu");
        
        // Add menu items
        addMenuItems(menu);
        
        player.openInventory(menu);
    }
    
    /**
     * Open skills menu for player
     */
    public void openSkillsMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "§bSkills Menu");

        // Skills categories
        addSkillItems(menu);

        player.openInventory(menu);
    }

    /**
     * Open collections menu for player
     */
    public void openCollectionsMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "§eCollections Menu");

        // Collection categories
        addCollectionItems(menu);

        player.openInventory(menu);
    }

    /**
     * Open profile menu for player
     */
    public void openProfileMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "§aProfile Menu");

        // Profile information
        addProfileItems(menu, player);

        player.openInventory(menu);
    }

    /**
     * Open fast travel menu for player
     */
    public void openFastTravelMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, "§dFast Travel");

        // Travel destinations
        addTravelItems(menu);

        player.openInventory(menu);
    }

    /**
     * Add skill items to menu
     */
    private void addSkillItems(Inventory menu) {
        // Mining Skill
        ItemStack mining = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta miningMeta = mining.getItemMeta();
        miningMeta.setDisplayName("§bMining");
        miningMeta.setLore(Arrays.asList("§7Mine ores and gems", "§7Level up your mining skill!"));
        mining.setItemMeta(miningMeta);
        menu.setItem(10, mining);

        // Farming Skill
        ItemStack farming = new ItemStack(Material.WHEAT);
        ItemMeta farmingMeta = farming.getItemMeta();
        farmingMeta.setDisplayName("§aFarming");
        farmingMeta.setLore(Arrays.asList("§7Grow crops and harvest", "§7Become a master farmer!"));
        farming.setItemMeta(farmingMeta);
        menu.setItem(12, farming);

        // Combat Skill
        ItemStack combat = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta combatMeta = combat.getItemMeta();
        combatMeta.setDisplayName("§cCombat");
        combatMeta.setLore(Arrays.asList("§7Fight monsters and bosses", "§7Master the art of combat!"));
        combat.setItemMeta(combatMeta);
        menu.setItem(14, combat);

        // Fishing Skill
        ItemStack fishing = new ItemStack(Material.FISHING_ROD);
        ItemMeta fishingMeta = fishing.getItemMeta();
        fishingMeta.setDisplayName("§bFishing");
        fishingMeta.setLore(Arrays.asList("§7Catch fish and sea creatures", "§7Become a fishing master!"));
        fishing.setItemMeta(fishingMeta);
        menu.setItem(16, fishing);
    }

    /**
     * Add collection items to menu
     */
    private void addCollectionItems(Inventory menu) {
        // Mining Collections
        ItemStack mining = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta miningMeta = mining.getItemMeta();
        miningMeta.setDisplayName("§6Mining Collections");
        miningMeta.setLore(Arrays.asList("§7Ores, gems, and minerals", "§7Track your mining progress!"));
        mining.setItemMeta(miningMeta);
        menu.setItem(10, mining);

        // Farming Collections
        ItemStack farming = new ItemStack(Material.WHEAT);
        ItemMeta farmingMeta = farming.getItemMeta();
        farmingMeta.setDisplayName("§aFarming Collections");
        farmingMeta.setLore(Arrays.asList("§7Crops and plants", "§7Track your farming progress!"));
        farming.setItemMeta(farmingMeta);
        menu.setItem(12, farming);

        // Combat Collections
        ItemStack combat = new ItemStack(Material.IRON_SWORD);
        ItemMeta combatMeta = combat.getItemMeta();
        combatMeta.setDisplayName("§cCombat Collections");
        combatMeta.setLore(Arrays.asList("§7Monster drops and loot", "§7Track your combat progress!"));
        combat.setItemMeta(combatMeta);
        menu.setItem(14, combat);

        // Fishing Collections
        ItemStack fishing = new ItemStack(Material.FISHING_ROD);
        ItemMeta fishingMeta = fishing.getItemMeta();
        fishingMeta.setDisplayName("§bFishing Collections");
        fishingMeta.setLore(Arrays.asList("§7Fish and sea creatures", "§7Track your fishing progress!"));
        fishing.setItemMeta(fishingMeta);
        menu.setItem(16, fishing);
    }

    /**
     * Add profile items to menu
     */
    private void addProfileItems(Inventory menu, Player player) {
        // Player Stats
        ItemStack stats = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta statsMeta = stats.getItemMeta();
        statsMeta.setDisplayName("§ePlayer Statistics");
        statsMeta.setLore(Arrays.asList(
            "§7Level: §f" + getPlayerLevel(player),
            "§7Coins: §f" + getPlayerCoins(player),
            "§7Playtime: §f" + getPlayerPlaytime(player)
        ));
        stats.setItemMeta(statsMeta);
        menu.setItem(13, stats);

        // Inventory
        ItemStack inventory = new ItemStack(Material.CHEST);
        ItemMeta inventoryMeta = inventory.getItemMeta();
        inventoryMeta.setDisplayName("§aInventory");
        inventoryMeta.setLore(Arrays.asList("§7View your items", "§7Click to open!"));
        inventory.setItemMeta(inventoryMeta);
        menu.setItem(21, inventory);

        // Ender Chest
        ItemStack enderChest = new ItemStack(Material.ENDER_CHEST);
        ItemMeta enderChestMeta = enderChest.getItemMeta();
        enderChestMeta.setDisplayName("§5Ender Chest");
        enderChestMeta.setLore(Arrays.asList("§7Access your ender chest", "§7Click to open!"));
        enderChest.setItemMeta(enderChestMeta);
        menu.setItem(23, enderChest);

        // Settings
        ItemStack settings = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta settingsMeta = settings.getItemMeta();
        settingsMeta.setDisplayName("§cSettings");
        settingsMeta.setLore(Arrays.asList("§7Configure your preferences", "§7Click to open!"));
        settings.setItemMeta(settingsMeta);
        menu.setItem(31, settings);
    }

    /**
     * Add travel items to menu
     */
    private void addTravelItems(Inventory menu) {
        // Hub
        ItemStack hub = new ItemStack(Material.NETHER_STAR);
        ItemMeta hubMeta = hub.getItemMeta();
        hubMeta.setDisplayName("§eHub");
        hubMeta.setLore(Arrays.asList("§7Return to the main hub", "§7Click to travel!"));
        hub.setItemMeta(hubMeta);
        menu.setItem(10, hub);

        // Your Island
        ItemStack island = new ItemStack(Material.GRASS_BLOCK);
        ItemMeta islandMeta = island.getItemMeta();
        islandMeta.setDisplayName("§aYour Island");
        islandMeta.setLore(Arrays.asList("§7Visit your private island", "§7Click to travel!"));
        island.setItemMeta(islandMeta);
        menu.setItem(12, island);

        // Mining Area
        ItemStack mining = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta miningMeta = mining.getItemMeta();
        miningMeta.setDisplayName("§7Mining Area");
        miningMeta.setLore(Arrays.asList("§7Mine ores and explore", "§7Click to travel!"));
        mining.setItemMeta(miningMeta);
        menu.setItem(14, mining);

        // Farming Area
        ItemStack farming = new ItemStack(Material.WHEAT);
        ItemMeta farmingMeta = farming.getItemMeta();
        farmingMeta.setDisplayName("§aFarming Area");
        farmingMeta.setLore(Arrays.asList("§7Grow crops and farm", "§7Click to travel!"));
        farming.setItemMeta(farmingMeta);
        menu.setItem(16, farming);
    }

    /**
     * Helper methods for player data (placeholder implementations)
     */
    private int getPlayerLevel(Player player) {
        // TODO: Get actual player level from profile
        return 1;
    }

    private double getPlayerCoins(Player player) {
        // TODO: Get actual player coins from profile
        return 1000.0;
    }

    private String getPlayerPlaytime(Player player) {
        // TODO: Get actual player playtime from profile
        return "1h 30m";
    }

    /**
     * Add items to menu
     */
    private void addMenuItems(Inventory menu) {
        // Skills
        ItemStack skills = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta skillsMeta = skills.getItemMeta();
        skillsMeta.setDisplayName("§bSkills");
        skillsMeta.setLore(Arrays.asList("§7View and manage your skills", "§7Click to open!"));
        skills.setItemMeta(skillsMeta);
        menu.setItem(10, skills);
        
        // Collections
        ItemStack collections = new ItemStack(Material.CHEST);
        ItemMeta collectionsMeta = collections.getItemMeta();
        collectionsMeta.setDisplayName("§aCollections");
        collectionsMeta.setLore(Arrays.asList("§7View your item collections", "§7Click to open!"));
        collections.setItemMeta(collectionsMeta);
        menu.setItem(12, collections);
        
        // Bank
        ItemStack bank = new ItemStack(Material.GOLD_INGOT);
        ItemMeta bankMeta = bank.getItemMeta();
        bankMeta.setDisplayName("§6Bank");
        bankMeta.setLore(Arrays.asList("§7Manage your coins", "§7Click to open!"));
        bank.setItemMeta(bankMeta);
        menu.setItem(14, bank);
        
        // Pets
        ItemStack pets = new ItemStack(Material.BONE);
        ItemMeta petsMeta = pets.getItemMeta();
        petsMeta.setDisplayName("§dPets");
        petsMeta.setLore(Arrays.asList("§7Manage your pets", "§7Click to open!"));
        pets.setItemMeta(petsMeta);
        menu.setItem(16, pets);
        
        // Close button
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName("§cClose");
        close.setItemMeta(closeMeta);
        menu.setItem(49, close);
    }
}