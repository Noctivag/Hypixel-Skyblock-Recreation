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
 * Enhanced Main Menu GUI - Erweitertes Hauptmenü mit Untermenü-Integration
 */
public class EnhancedMainMenuGUI implements Listener {
    
    private final SkyblockPlugin plugin;
    private final AdvancedSubMenuGUI subMenuGUI;
    
    public EnhancedMainMenuGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.subMenuGUI = new AdvancedSubMenuGUI(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Öffnet das erweiterte Hauptmenü
     */
    public void openMainMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + "§lSkyblock - Hauptmenü");
        
        // Fülle das Menü mit Kategorien
        fillMainMenu(inventory, player);
        
        player.openInventory(inventory);
    }
    
    /**
     * Füllt das Hauptmenü mit Kategorien
     */
    private void fillMainMenu(Inventory inventory, Player player) {
        // Skills Kategorie
        ItemStack skillsCategory = createCategoryItem(
            Material.DIAMOND_SWORD,
            ChatColor.AQUA + "§lSkills",
            Arrays.asList(
                ChatColor.GRAY + "Verwalte deine Fähigkeiten",
                ChatColor.YELLOW + "Combat: " + ChatColor.GREEN + "25",
                ChatColor.YELLOW + "Mining: " + ChatColor.GREEN + "30",
                ChatColor.YELLOW + "Farming: " + ChatColor.GREEN + "20",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(10, skillsCategory);
        
        // Collections Kategorie
        ItemStack collectionsCategory = createCategoryItem(
            Material.CHEST,
            ChatColor.GREEN + "§lCollections",
            Arrays.asList(
                ChatColor.GRAY + "Sammle Items und verdiene Belohnungen",
                ChatColor.YELLOW + "Farming: " + ChatColor.AQUA + "15/25",
                ChatColor.YELLOW + "Mining: " + ChatColor.AQUA + "20/30",
                ChatColor.YELLOW + "Combat: " + ChatColor.AQUA + "12/20",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(12, collectionsCategory);
        
        // Minions Kategorie
        ItemStack minionsCategory = createCategoryItem(
            Material.VILLAGER_SPAWN_EGG,
            ChatColor.DARK_PURPLE + "§lMinions",
            Arrays.asList(
                ChatColor.GRAY + "Verwalte deine Minions",
                ChatColor.YELLOW + "Active: " + ChatColor.AQUA + "8/12",
                ChatColor.YELLOW + "Total Level: " + ChatColor.GREEN + "156",
                ChatColor.YELLOW + "Production: " + ChatColor.GOLD + "2.5K/h",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(14, minionsCategory);
        
        // Pets Kategorie
        ItemStack petsCategory = createCategoryItem(
            Material.BONE,
            ChatColor.LIGHT_PURPLE + "§lPets",
            Arrays.asList(
                ChatColor.GRAY + "Verwalte deine Haustiere",
                ChatColor.YELLOW + "Owned: " + ChatColor.AQUA + "5",
                ChatColor.YELLOW + "Active: " + ChatColor.GREEN + "1",
                ChatColor.YELLOW + "Total XP: " + ChatColor.GOLD + "15K",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(16, petsCategory);
        
        // Dungeons Kategorie
        ItemStack dungeonsCategory = createCategoryItem(
            Material.SKELETON_SKULL,
            ChatColor.DARK_RED + "§lDungeons",
            Arrays.asList(
                ChatColor.GRAY + "Erkunde gefährliche Dungeons",
                ChatColor.YELLOW + "Catacombs Floor: " + ChatColor.AQUA + "3",
                ChatColor.YELLOW + "Best Time: " + ChatColor.GREEN + "12:34",
                ChatColor.YELLOW + "Slayer Level: " + ChatColor.GOLD + "7",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(28, dungeonsCategory);
        
        // Slayers Kategorie
        ItemStack slayersCategory = createCategoryItem(
            Material.WITHER_SKELETON_SKULL,
            ChatColor.DARK_GRAY + "§lSlayers",
            Arrays.asList(
                ChatColor.GRAY + "Bekämpfe mächtige Bosse",
                ChatColor.YELLOW + "Zombie: " + ChatColor.AQUA + "Level 5",
                ChatColor.YELLOW + "Spider: " + ChatColor.AQUA + "Level 3",
                ChatColor.YELLOW + "Wolf: " + ChatColor.AQUA + "Level 2",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(30, slayersCategory);
        
        // Travel Kategorie
        ItemStack travelCategory = createCategoryItem(
            Material.ENDER_PEARL,
            ChatColor.DARK_AQUA + "§lTravel",
            Arrays.asList(
                ChatColor.GRAY + "Reise zu verschiedenen Orten",
                ChatColor.YELLOW + "Available: " + ChatColor.AQUA + "15",
                ChatColor.YELLOW + "Unlocked: " + ChatColor.GREEN + "8",
                ChatColor.YELLOW + "Cooldown: " + ChatColor.GOLD + "5m",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(32, travelCategory);
        
        // Trading Kategorie
        ItemStack tradingCategory = createCategoryItem(
            Material.EMERALD,
            ChatColor.GOLD + "§lTrading",
            Arrays.asList(
                ChatColor.GRAY + "Handel mit anderen Spielern",
                ChatColor.YELLOW + "Active Trades: " + ChatColor.AQUA + "2",
                ChatColor.YELLOW + "Total Trades: " + ChatColor.GREEN + "45",
                ChatColor.YELLOW + "Reputation: " + ChatColor.GOLD + "★★★★★",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(34, tradingCategory);
        
        // Arsenal Kategorie (nur für OP)
        if (player.isOp()) {
            ItemStack arsenalCategory = createCategoryItem(
                Material.NETHER_STAR,
                ChatColor.RED + "§lArsenal",
                Arrays.asList(
                    ChatColor.GRAY + "Alle Items aus dem Plugin",
                    ChatColor.YELLOW + "Weapons: " + ChatColor.AQUA + "25",
                    ChatColor.YELLOW + "Armor: " + ChatColor.AQUA + "15",
                    ChatColor.YELLOW + "Tools: " + ChatColor.AQUA + "20",
                    "",
                    ChatColor.RED + "OP ONLY - Klicke zum Öffnen!"
                )
            );
            inventory.setItem(40, arsenalCategory);
        }
        
        // Settings Kategorie
        ItemStack settingsCategory = createCategoryItem(
            Material.REDSTONE,
            ChatColor.RED + "§lSettings",
            Arrays.asList(
                ChatColor.GRAY + "Einstellungen und Konfiguration",
                ChatColor.YELLOW + "Notifications: " + ChatColor.GREEN + "ON",
                ChatColor.YELLOW + "Sounds: " + ChatColor.GREEN + "ON",
                ChatColor.YELLOW + "Particles: " + ChatColor.GREEN + "ON",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(42, settingsCategory);
        
        // Profile Kategorie
        ItemStack profileCategory = createCategoryItem(
            Material.PLAYER_HEAD,
            ChatColor.YELLOW + "§lProfile",
            Arrays.asList(
                ChatColor.GRAY + "Dein Spielerprofil",
                ChatColor.YELLOW + "Level: " + ChatColor.AQUA + "42",
                ChatColor.YELLOW + "Coins: " + ChatColor.GOLD + "125,000",
                ChatColor.YELLOW + "Playtime: " + ChatColor.GREEN + "45h",
                "",
                ChatColor.GREEN + "Klicke zum Öffnen!"
            )
        );
        inventory.setItem(44, profileCategory);
        
        // Navigation Items
        addNavigationItems(inventory);
    }
    
    /**
     * Fügt Navigations-Items hinzu
     */
    private void addNavigationItems(Inventory inventory) {
        // Close Button
        ItemStack closeButton = createCategoryItem(
            Material.BARRIER,
            ChatColor.RED + "§lSchließen",
            Arrays.asList(
                ChatColor.GRAY + "Schließt das Hauptmenü",
                "",
                ChatColor.RED + "Klicke zum Schließen!"
            )
        );
        inventory.setItem(49, closeButton);
    }
    
    /**
     * Erstellt ein Kategorie-Item
     */
    private ItemStack createCategoryItem(Material material, String name, List<String> lore) {
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
        
        // Prüfe ob es das Hauptmenü ist
        if (!net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(event.getView().title()).contains("Hauptmenü")) return;
        
        event.setCancelled(true);
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        String itemName = clickedItem.getItemMeta().displayName() != null ? 
            net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
                .serialize(clickedItem.getItemMeta().displayName()) : 
            clickedItem.getType().name();
        
        // Handle Kategorie-Klicks
        if (itemName.contains("Skills")) {
            subMenuGUI.openSkillsSubMenu(player);
        } else if (itemName.contains("Collections")) {
            subMenuGUI.openCollectionsSubMenu(player);
        } else if (itemName.contains("Minions")) {
            subMenuGUI.openMinionsSubMenu(player);
        } else if (itemName.contains("Pets")) {
            subMenuGUI.openPetsSubMenu(player);
        } else if (itemName.contains("Dungeons")) {
            subMenuGUI.openDungeonsSubMenu(player);
        } else if (itemName.contains("Slayers")) {
            de.noctivag.skyblock.gui.details.SlayersDetailGUI.open(player);
        } else if (itemName.contains("Trading")) {
            de.noctivag.skyblock.gui.details.TradingDetailGUI.open(player);
        } else if (itemName.contains("Profile")) {
            de.noctivag.skyblock.gui.details.ProfileDetailGUI.open(player);
        } else if (itemName.contains("Settings")) {
            de.noctivag.skyblock.gui.details.SettingsDetailGUI.open(player);
        } else if (itemName.contains("Arsenal")) {
            if (player.isOp()) {
                de.noctivag.skyblock.gui.details.ArsenalDetailGUI.open(player);
            } else {
                player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung für das Arsenal!");
            }
        } else if (itemName.contains("Schließen")) {
            player.closeInventory();
        } else {
            player.sendMessage(ChatColor.YELLOW + "Unbekannte Kategorie: " + itemName);
        }
    }
}
