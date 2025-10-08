package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Advanced Sub Menu GUI - Erweiterte Untermenüs für verschiedene Systeme
 */
public class AdvancedSubMenuGUI implements Listener {
    
    private final SkyblockPlugin plugin;
    
    public AdvancedSubMenuGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Öffnet das Skills-Untermenü
     */
    public void openSkillsSubMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "§lSkills - Untermenü");
        
        // Skill-Kategorien
        addSkillCategories(inventory);
        
        // Navigation
        addNavigationItems(inventory);
        
        player.openInventory(inventory);
    }
    
    /**
     * Öffnet das Collections-Untermenü
     */
    public void openCollectionsSubMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + "§lCollections - Untermenü");
        
        // Collection-Kategorien
        addCollectionCategories(inventory);
        
        // Navigation
        addNavigationItems(inventory);
        
        player.openInventory(inventory);
    }
    
    /**
     * Öffnet das Minions-Untermenü
     */
    public void openMinionsSubMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + "§lMinions - Untermenü");
        
        // Minion-Kategorien
        addMinionCategories(inventory);
        
        // Navigation
        addNavigationItems(inventory);
        
        player.openInventory(inventory);
    }
    
    /**
     * Öffnet das Pets-Untermenü
     */
    public void openPetsSubMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.LIGHT_PURPLE + "§lPets - Untermenü");
        
        // Pet-Kategorien
        addPetCategories(inventory);
        
        // Navigation
        addNavigationItems(inventory);
        
        player.openInventory(inventory);
    }
    
    /**
     * Öffnet das Dungeons-Untermenü
     */
    public void openDungeonsSubMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "§lDungeons - Untermenü");
        
        // Dungeon-Kategorien
        addDungeonCategories(inventory);
        
        // Navigation
        addNavigationItems(inventory);
        
        player.openInventory(inventory);
    }
    
    /**
     * Fügt Skill-Kategorien hinzu
     */
    private void addSkillCategories(Inventory inventory) {
        // Combat Skills
        ItemStack combatSkills = createMenuItem(
            Material.DIAMOND_SWORD,
            ChatColor.RED + "§lCombat Skills",
            Arrays.asList(
                ChatColor.GRAY + "Kampf-Fähigkeiten",
                ChatColor.YELLOW + "Level: " + ChatColor.GREEN + "25",
                ChatColor.YELLOW + "XP: " + ChatColor.AQUA + "1,250/2,500",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(10, combatSkills);
        
        // Mining Skills
        ItemStack miningSkills = createMenuItem(
            Material.DIAMOND_PICKAXE,
            ChatColor.GRAY + "§lMining Skills",
            Arrays.asList(
                ChatColor.GRAY + "Bergbau-Fähigkeiten",
                ChatColor.YELLOW + "Level: " + ChatColor.GREEN + "30",
                ChatColor.YELLOW + "XP: " + ChatColor.AQUA + "2,100/3,000",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(12, miningSkills);
        
        // Farming Skills
        ItemStack farmingSkills = createMenuItem(
            Material.WHEAT,
            ChatColor.GREEN + "§lFarming Skills",
            Arrays.asList(
                ChatColor.GRAY + "Landwirtschaft-Fähigkeiten",
                ChatColor.YELLOW + "Level: " + ChatColor.GREEN + "20",
                ChatColor.YELLOW + "XP: " + ChatColor.AQUA + "800/1,500",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(14, farmingSkills);
        
        // Foraging Skills
        ItemStack foragingSkills = createMenuItem(
            Material.OAK_LOG,
            ChatColor.DARK_GREEN + "§lForaging Skills",
            Arrays.asList(
                ChatColor.GRAY + "Forstwirtschaft-Fähigkeiten",
                ChatColor.YELLOW + "Level: " + ChatColor.GREEN + "18",
                ChatColor.YELLOW + "XP: " + ChatColor.AQUA + "650/1,200",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(16, foragingSkills);
        
        // Fishing Skills
        ItemStack fishingSkills = createMenuItem(
            Material.FISHING_ROD,
            ChatColor.AQUA + "§lFishing Skills",
            Arrays.asList(
                ChatColor.GRAY + "Angeln-Fähigkeiten",
                ChatColor.YELLOW + "Level: " + ChatColor.GREEN + "15",
                ChatColor.YELLOW + "XP: " + ChatColor.AQUA + "450/900",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(28, fishingSkills);
        
        // Enchanting Skills
        ItemStack enchantingSkills = createMenuItem(
            Material.ENCHANTING_TABLE,
            ChatColor.LIGHT_PURPLE + "§lEnchanting Skills",
            Arrays.asList(
                ChatColor.GRAY + "Verzauberungs-Fähigkeiten",
                ChatColor.YELLOW + "Level: " + ChatColor.GREEN + "12",
                ChatColor.YELLOW + "XP: " + ChatColor.AQUA + "300/600",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(30, enchantingSkills);
        
        // Alchemy Skills
        ItemStack alchemySkills = createMenuItem(
            Material.BREWING_STAND,
            ChatColor.DARK_PURPLE + "§lAlchemy Skills",
            Arrays.asList(
                ChatColor.GRAY + "Alchemie-Fähigkeiten",
                ChatColor.YELLOW + "Level: " + ChatColor.GREEN + "8",
                ChatColor.YELLOW + "XP: " + ChatColor.AQUA + "150/300",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(32, alchemySkills);
        
        // Taming Skills
        ItemStack tamingSkills = createMenuItem(
            Material.BONE,
            ChatColor.YELLOW + "§lTaming Skills",
            Arrays.asList(
                ChatColor.GRAY + "Zähmungs-Fähigkeiten",
                ChatColor.YELLOW + "Level: " + ChatColor.GREEN + "22",
                ChatColor.YELLOW + "XP: " + ChatColor.AQUA + "950/1,800",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(34, tamingSkills);
    }
    
    /**
     * Fügt Collection-Kategorien hinzu
     */
    private void addCollectionCategories(Inventory inventory) {
        // Farming Collections
        ItemStack farmingCollections = createMenuItem(
            Material.WHEAT,
            ChatColor.GREEN + "§lFarming Collections",
            Arrays.asList(
                ChatColor.GRAY + "Landwirtschaft-Sammlungen",
                ChatColor.YELLOW + "Items: " + ChatColor.AQUA + "15/25",
                ChatColor.YELLOW + "Progress: " + ChatColor.GREEN + "60%",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(10, farmingCollections);
        
        // Mining Collections
        ItemStack miningCollections = createMenuItem(
            Material.DIAMOND,
            ChatColor.AQUA + "§lMining Collections",
            Arrays.asList(
                ChatColor.GRAY + "Bergbau-Sammlungen",
                ChatColor.YELLOW + "Items: " + ChatColor.AQUA + "20/30",
                ChatColor.YELLOW + "Progress: " + ChatColor.GREEN + "67%",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(12, miningCollections);
        
        // Combat Collections
        ItemStack combatCollections = createMenuItem(
            Material.ROTTEN_FLESH,
            ChatColor.RED + "§lCombat Collections",
            Arrays.asList(
                ChatColor.GRAY + "Kampf-Sammlungen",
                ChatColor.YELLOW + "Items: " + ChatColor.AQUA + "12/20",
                ChatColor.YELLOW + "Progress: " + ChatColor.GREEN + "60%",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(14, combatCollections);
        
        // Foraging Collections
        ItemStack foragingCollections = createMenuItem(
            Material.OAK_LOG,
            ChatColor.DARK_GREEN + "§lForaging Collections",
            Arrays.asList(
                ChatColor.GRAY + "Forstwirtschaft-Sammlungen",
                ChatColor.YELLOW + "Items: " + ChatColor.AQUA + "8/15",
                ChatColor.YELLOW + "Progress: " + ChatColor.GREEN + "53%",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(16, foragingCollections);
        
        // Fishing Collections
        ItemStack fishingCollections = createMenuItem(
            Material.COD,
            ChatColor.BLUE + "§lFishing Collections",
            Arrays.asList(
                ChatColor.GRAY + "Angeln-Sammlungen",
                ChatColor.YELLOW + "Items: " + ChatColor.AQUA + "6/12",
                ChatColor.YELLOW + "Progress: " + ChatColor.GREEN + "50%",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(28, fishingCollections);
    }
    
    /**
     * Fügt Minion-Kategorien hinzu
     */
    private void addMinionCategories(Inventory inventory) {
        // My Minions
        ItemStack myMinions = createMenuItem(
            Material.VILLAGER_SPAWN_EGG,
            ChatColor.GREEN + "§lMy Minions",
            Arrays.asList(
                ChatColor.GRAY + "Deine aktiven Minions",
                ChatColor.YELLOW + "Active: " + ChatColor.AQUA + "8/12",
                ChatColor.YELLOW + "Total Level: " + ChatColor.GREEN + "156",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(10, myMinions);
        
        // Minion Upgrades
        ItemStack minionUpgrades = createMenuItem(
            Material.EMERALD,
            ChatColor.AQUA + "§lMinion Upgrades",
            Arrays.asList(
                ChatColor.GRAY + "Minion-Upgrades kaufen",
                ChatColor.YELLOW + "Available: " + ChatColor.GREEN + "15",
                ChatColor.YELLOW + "Owned: " + ChatColor.AQUA + "8",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(12, minionUpgrades);
        
        // Minion Fuel
        ItemStack minionFuel = createMenuItem(
            Material.COAL,
            ChatColor.DARK_GRAY + "§lMinion Fuel",
            Arrays.asList(
                ChatColor.GRAY + "Minion-Treibstoff",
                ChatColor.YELLOW + "Available: " + ChatColor.GREEN + "12",
                ChatColor.YELLOW + "Active: " + ChatColor.AQUA + "3",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(14, minionFuel);
        
        // Minion Storage
        ItemStack minionStorage = createMenuItem(
            Material.CHEST,
            ChatColor.YELLOW + "§lMinion Storage",
            Arrays.asList(
                ChatColor.GRAY + "Minion-Lagerung",
                ChatColor.YELLOW + "Capacity: " + ChatColor.AQUA + "640",
                ChatColor.YELLOW + "Used: " + ChatColor.GREEN + "320",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(16, minionStorage);
    }
    
    /**
     * Fügt Pet-Kategorien hinzu
     */
    private void addPetCategories(Inventory inventory) {
        // My Pets
        ItemStack myPets = createMenuItem(
            Material.BONE,
            ChatColor.YELLOW + "§lMy Pets",
            Arrays.asList(
                ChatColor.GRAY + "Deine Haustiere",
                ChatColor.YELLOW + "Owned: " + ChatColor.AQUA + "5",
                ChatColor.YELLOW + "Active: " + ChatColor.GREEN + "1",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(10, myPets);
        
        // Pet Shop
        ItemStack petShop = createMenuItem(
            Material.EMERALD,
            ChatColor.GREEN + "§lPet Shop",
            Arrays.asList(
                ChatColor.GRAY + "Haustiere kaufen",
                ChatColor.YELLOW + "Available: " + ChatColor.AQUA + "25",
                ChatColor.YELLOW + "Price Range: " + ChatColor.GOLD + "1K-100K",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(12, petShop);
        
        // Pet Upgrades
        ItemStack petUpgrades = createMenuItem(
            Material.NETHER_STAR,
            ChatColor.LIGHT_PURPLE + "§lPet Upgrades",
            Arrays.asList(
                ChatColor.GRAY + "Haustier-Upgrades",
                ChatColor.YELLOW + "Available: " + ChatColor.AQUA + "10",
                ChatColor.YELLOW + "Owned: " + ChatColor.GREEN + "3",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(14, petUpgrades);
    }
    
    /**
     * Fügt Dungeon-Kategorien hinzu
     */
    private void addDungeonCategories(Inventory inventory) {
        // Catacombs
        ItemStack catacombs = createMenuItem(
            Material.SKELETON_SKULL,
            ChatColor.DARK_RED + "§lCatacombs",
            Arrays.asList(
                ChatColor.GRAY + "Die Katakomben",
                ChatColor.YELLOW + "Floor: " + ChatColor.AQUA + "3",
                ChatColor.YELLOW + "Best Time: " + ChatColor.GREEN + "12:34",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(10, catacombs);
        
        // Dragon's Lair
        ItemStack dragonsLair = createMenuItem(
            Material.DRAGON_HEAD,
            ChatColor.DARK_PURPLE + "§lDragon's Lair",
            Arrays.asList(
                ChatColor.GRAY + "Drachenhöhle",
                ChatColor.YELLOW + "Tier: " + ChatColor.AQUA + "Superior",
                ChatColor.YELLOW + "Damage: " + ChatColor.GREEN + "2.5M",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(12, dragonsLair);
        
        // Slayer Dungeons
        ItemStack slayerDungeons = createMenuItem(
            Material.WITHER_SKELETON_SKULL,
            ChatColor.DARK_GRAY + "§lSlayer Dungeons",
            Arrays.asList(
                ChatColor.GRAY + "Slayer-Dungeons",
                ChatColor.YELLOW + "Level: " + ChatColor.AQUA + "7",
                ChatColor.YELLOW + "XP: " + ChatColor.GREEN + "15,000",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(14, slayerDungeons);
    }
    
    /**
     * Fügt Navigations-Items hinzu
     */
    private void addNavigationItems(Inventory inventory) {
        // Back Button
        ItemStack backButton = createMenuItem(
            Material.ARROW,
            ChatColor.YELLOW + "§lZurück",
            Arrays.asList(
                ChatColor.GRAY + "Zurück zum Hauptmenü",
                "",
                ChatColor.GREEN + "Klicke zum Navigieren!"
            )
        );
        inventory.setItem(45, backButton);
        
        // Close Button
        ItemStack closeButton = createMenuItem(
            Material.BARRIER,
            ChatColor.RED + "§lSchließen",
            Arrays.asList(
                ChatColor.GRAY + "Schließt das Menü",
                "",
                ChatColor.RED + "Klicke zum Schließen!"
            )
        );
        inventory.setItem(49, closeButton);
    }
    
    /**
     * Erstellt ein Menü-Item
     */
    private ItemStack createMenuItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.displayName(net.kyori.adventure.text.Component.text(name));
            meta.lore(lore.stream().map(net.kyori.adventure.text.Component::text).toList());
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Event-Handler für Inventory-Klicks
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        
        // Prüfe ob es ein Untermenü ist
        String title = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (!title.contains("Untermenü")) return;
        
        event.setCancelled(true);
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Handle Navigation
        if (clickedItem.getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }
        
        if (clickedItem.getType() == Material.ARROW) {
            // TODO: Implementiere Zurück-Navigation
            player.sendMessage(ChatColor.YELLOW + "Zurück-Navigation wird implementiert...");
            return;
        }
        
        // Handle Kategorie-Klicks
        String itemName = clickedItem.getItemMeta().displayName() != null ? 
            net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
                .serialize(clickedItem.getItemMeta().displayName()) : 
            clickedItem.getType().name();
        
        if (itemName.contains("Skills")) {
            de.noctivag.skyblock.gui.details.SkillsDetailGUI.open(player);
        } else if (itemName.contains("Collections")) {
            de.noctivag.skyblock.gui.details.CollectionsDetailGUI.open(player);
        } else if (itemName.contains("Minions")) {
            de.noctivag.skyblock.gui.details.MinionsDetailGUI.open(player);
        } else if (itemName.contains("Pets")) {
            de.noctivag.skyblock.gui.details.PetsDetailGUI.open(player);
        } else if (itemName.contains("Dungeons")) {
            de.noctivag.skyblock.gui.details.DungeonsDetailGUI.open(player);
        }
    }
}
