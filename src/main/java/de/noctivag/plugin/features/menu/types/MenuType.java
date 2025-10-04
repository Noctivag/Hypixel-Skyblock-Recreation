package de.noctivag.plugin.features.menu.types;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * All menu types from Hypixel SkyBlock
 */
public enum MenuType {
    // Main Menus
    MAIN_MENU("MAIN_MENU", "§6§lSkyBlock Menü"),
    PLAYER_PROFILE("PLAYER_PROFILE", "§6§lIhr SkyBlock-Profil"),
    
    // Core Menus
    SKILLS_MENU("SKILLS_MENU", "§6§lSkills"),
    COLLECTIONS_MENU("COLLECTIONS_MENU", "§6§lCollections"),
    STATS_MENU("STATS_MENU", "§6§lStats"),
    EQUIPMENT_MENU("EQUIPMENT_MENU", "§6§lEquipment"),
    QUESTS_MENU("QUESTS_MENU", "§6§lQuests"),
    
    // Combat Menus
    WEAPONS_MENU("WEAPONS_MENU", "§6§lWaffen"),
    ARMOR_MENU("ARMOR_MENU", "§6§lRüstungen"),
    ACCESSORIES_MENU("ACCESSORIES_MENU", "§6§lAccessories"),
    ENCHANTMENTS_MENU("ENCHANTMENTS_MENU", "§6§lEnchantments"),
    REFORGING_MENU("REFORGING_MENU", "§6§lReforging"),
    POTIONS_MENU("POTIONS_MENU", "§6§lPotionen"),
    
    // Weapon Submenus
    SWORDS_SUBMENU("SWORDS_SUBMENU", "§6§lSchwerter"),
    BOWS_SUBMENU("BOWS_SUBMENU", "§6§lBögen"),
    STAVES_SUBMENU("STAVES_SUBMENU", "§6§lStäbe"),
    SPECIAL_WEAPONS_SUBMENU("SPECIAL_WEAPONS_SUBMENU", "§6§lSpezielle Waffen"),
    
    // Tools Menus
    TOOLS_MENU("TOOLS_MENU", "§6§lWerkzeuge"),
    PICKAXES_SUBMENU("PICKAXES_SUBMENU", "§6§lSpitzhacken"),
    DRILLS_SUBMENU("DRILLS_SUBMENU", "§6§lBohrer"),
    HOES_SUBMENU("HOES_SUBMENU", "§6§lHacken"),
    AXES_SUBMENU("AXES_SUBMENU", "§6§lÄxte"),
    SHOVELS_SUBMENU("SHOVELS_SUBMENU", "§6§lSchaufeln"),
    FISHING_RODS_SUBMENU("FISHING_RODS_SUBMENU", "§6§lAngelruten"),
    
    // Pet and Minion Menus
    PETS_MENU("PETS_MENU", "§6§lPets"),
    MINIONS_MENU("MINIONS_MENU", "§6§lMinions"),
    PET_CARE_MENU("PET_CARE_MENU", "§6§lPet Care"),
    MINION_UPGRADES_MENU("MINION_UPGRADES_MENU", "§6§lMinion Upgrades"),
    
    // Economy Menus
    ECONOMY_MENU("ECONOMY_MENU", "§6§lEconomy"),
    BAZAAR_MENU("BAZAAR_MENU", "§6§lBazaar"),
    AUCTION_HOUSE_MENU("AUCTION_HOUSE_MENU", "§6§lAuction House"),
    BANK_MENU("BANK_MENU", "§6§lBank"),
    PERSONAL_VAULT_MENU("PERSONAL_VAULT_MENU", "§6§lPersonal Vault"),
    
    // Game Systems Menus
    TRAVEL_MENU("TRAVEL_MENU", "§6§lReiseziele"),
    LOCATIONS_MENU("LOCATIONS_MENU", "§6§lLocations"),
    NPCS_MENU("NPCS_MENU", "§6§lNPCs"),
    EVENTS_MENU("EVENTS_MENU", "§6§lEvents"),
    CALENDAR_MENU("CALENDAR_MENU", "§6§lCalendar"),
    
    // Dungeon Menus
    DUNGEONS_MENU("DUNGEONS_MENU", "§6§lDungeons"),
    CATACOMBS_MENU("CATACOMBS_MENU", "§6§lCatacombs"),
    DUNGEON_CLASSES_MENU("DUNGEON_CLASSES_MENU", "§6§lDungeon Classes"),
    DUNGEON_LOOT_MENU("DUNGEON_LOOT_MENU", "§6§lDungeon Loot"),
    
    // Crafting Menus
    CRAFTING_MENU("CRAFTING_MENU", "§6§lCrafting"),
    RECIPE_BOOK_MENU("RECIPE_BOOK_MENU", "§6§lRecipe Book"),
    QUICK_CRAFTING_MENU("QUICK_CRAFTING_MENU", "§6§lQuick Crafting"),
    
    // Settings Menus
    SETTINGS_MENU("SETTINGS_MENU", "§6§lSettings"),
    SKYBLOCK_SETTINGS_MENU("SKYBLOCK_SETTINGS_MENU", "§6§lSkyBlock Settings"),
    PROFILE_MANAGEMENT_MENU("PROFILE_MANAGEMENT_MENU", "§6§lProfile Management"),
    
    // Storage Menus
    WARDROBE_MENU("WARDROBE_MENU", "§6§lWardrobe"),
    SACK_OF_SACKS_MENU("SACK_OF_SACKS_MENU", "§6§lSack of Sacks"),
    QUIVER_MENU("QUIVER_MENU", "§6§lQuiver"),
    FISHING_BAG_MENU("FISHING_BAG_MENU", "§6§lFishing Bag"),
    ACCESSORY_BAG_MENU("ACCESSORY_BAG_MENU", "§6§lAccessory Bag"),
    POTION_BAG_MENU("POTION_BAG_MENU", "§6§lPotion Bag"),
    
    // Special Menus
    BOOSTER_COOKIE_MENU("BOOSTER_COOKIE_MENU", "§6§lBooster Cookie"),
    GAME_GUIDES_MENU("GAME_GUIDES_MENU", "§6§lGame Guides"),
    TUTORIAL_MENU("TUTORIAL_MENU", "§6§lTutorial"),
    HELP_MENU("HELP_MENU", "§6§lHelp"),
    
    // Mobs and Bosses
    MOBS_MENU("MOBS_MENU", "§6§lMobs"),
    BOSSES_MENU("BOSSES_MENU", "§6§lBosses"),
    SLAYER_MENU("SLAYER_MENU", "§6§lSlayer"),
    
    // Magic and Enchantments
    ENCHANTMENT_TABLE_MENU("ENCHANTMENT_TABLE_MENU", "§6§lEnchantment Table"),
    ENCHANTMENTS_SUBMENU("ENCHANTMENTS_SUBMENU", "§6§lEnchantments"),
    REFORGING_SUBMENU("REFORGING_SUBMENU", "§6§lReforging"),
    UPGRADES_SUBMENU("UPGRADES_SUBMENU", "§6§lUpgrades");
    
    private final String id;
    private final String title;
    
    MenuType(String id, String title) {
        this.id = id;
        this.title = title;
    }
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    /**
     * Get menu type by ID
     */
    public static MenuType getById(String id) {
        for (MenuType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * Get main menu types
     */
    public static List<MenuType> getMainMenus() {
        return Arrays.asList(
            MAIN_MENU, PLAYER_PROFILE, SKILLS_MENU, COLLECTIONS_MENU, 
            EQUIPMENT_MENU, QUESTS_MENU
        );
    }
    
    /**
     * Get combat menu types
     */
    public static List<MenuType> getCombatMenus() {
        return Arrays.asList(
            WEAPONS_MENU, ARMOR_MENU, ACCESSORIES_MENU, ENCHANTMENTS_MENU,
            REFORGING_MENU, POTIONS_MENU
        );
    }
    
    /**
     * Get tool menu types
     */
    public static List<MenuType> getToolMenus() {
        return Arrays.asList(
            TOOLS_MENU, PICKAXES_SUBMENU, DRILLS_SUBMENU, HOES_SUBMENU,
            AXES_SUBMENU, SHOVELS_SUBMENU, FISHING_RODS_SUBMENU
        );
    }
    
    /**
     * Get economy menu types
     */
    public static List<MenuType> getEconomyMenus() {
        return Arrays.asList(
            ECONOMY_MENU, BAZAAR_MENU, AUCTION_HOUSE_MENU, BANK_MENU, PERSONAL_VAULT_MENU
        );
    }
    
    /**
     * Get storage menu types
     */
    public static List<MenuType> getStorageMenus() {
        return Arrays.asList(
            WARDROBE_MENU, SACK_OF_SACKS_MENU, QUIVER_MENU, FISHING_BAG_MENU,
            ACCESSORY_BAG_MENU, POTION_BAG_MENU
        );
    }
    
    @Override
    public String toString() {
        return title;
    }
}
