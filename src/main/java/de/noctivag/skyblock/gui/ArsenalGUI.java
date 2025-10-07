package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.items.SkyblockItemManager;
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
 * Arsenal GUI - Menü mit allen Items aus dem Plugin für OP-Spieler
 */
public class ArsenalGUI implements Listener {
    
    private final SkyblockPlugin plugin;
    private final SkyblockItemManager itemManager;
    
    public ArsenalGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
        this.itemManager = new SkyblockItemManager(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * Öffnet das Arsenal-Menü
     */
    public void openMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_PURPLE + "§lArsenal - Alle Items");
        
        // Fülle das Menü mit Items
        fillArsenalMenu(inventory);
        
        player.openInventory(inventory);
    }
    
    /**
     * Füllt das Arsenal-Menü mit Items
     */
    private void fillArsenalMenu(Inventory inventory) {
        int slot = 0;
        
        // Skyblock Items
        slot = addSkyblockItems(inventory, slot);
        
        // Weapons
        slot = addWeapons(inventory, slot);
        
        // Armor
        slot = addArmor(inventory, slot);
        
        // Tools
        slot = addTools(inventory, slot);
        
        // Accessories
        slot = addAccessories(inventory, slot);
        
        // Special Items
        slot = addSpecialItems(inventory, slot);
        
        // Minion Items
        slot = addMinionItems(inventory, slot);
        
        // Pet Items
        slot = addPetItems(inventory, slot);
        
        // Enchanted Items
        slot = addEnchantedItems(inventory, slot);
        
        // Navigation Items
        addNavigationItems(inventory);
    }
    
    /**
     * Fügt Skyblock Items hinzu
     */
    private int addSkyblockItems(Inventory inventory, int startSlot) {
        // Skyblock Sword
        ItemStack skyblockSword = createSkyblockItem(
            Material.DIAMOND_SWORD,
            ChatColor.AQUA + "§lSkyblock Sword",
            Arrays.asList(
                ChatColor.GRAY + "Das ultimative Schwert",
                ChatColor.YELLOW + "Damage: " + ChatColor.RED + "100",
                ChatColor.YELLOW + "Strength: " + ChatColor.RED + "50",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, skyblockSword);
        
        // Skyblock Bow
        ItemStack skyblockBow = createSkyblockItem(
            Material.BOW,
            ChatColor.AQUA + "§lSkyblock Bow",
            Arrays.asList(
                ChatColor.GRAY + "Der ultimative Bogen",
                ChatColor.YELLOW + "Damage: " + ChatColor.RED + "80",
                ChatColor.YELLOW + "Power: " + ChatColor.RED + "V",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, skyblockBow);
        
        return startSlot;
    }
    
    /**
     * Fügt Waffen hinzu
     */
    private int addWeapons(Inventory inventory, int startSlot) {
        // Aspect of the End
        ItemStack aspectOfTheEnd = createSkyblockItem(
            Material.DIAMOND_SWORD,
            ChatColor.LIGHT_PURPLE + "§lAspect of the End",
            Arrays.asList(
                ChatColor.GRAY + "Teleportiert dich 8 Blöcke",
                ChatColor.YELLOW + "Damage: " + ChatColor.RED + "100",
                ChatColor.YELLOW + "Strength: " + ChatColor.RED + "100",
                ChatColor.YELLOW + "Intelligence: " + ChatColor.AQUA + "50",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, aspectOfTheEnd);
        
        // Rogue Sword
        ItemStack rogueSword = createSkyblockItem(
            Material.WOODEN_SWORD,
            ChatColor.GREEN + "§lRogue Sword",
            Arrays.asList(
                ChatColor.GRAY + "Erhöht deine Geschwindigkeit",
                ChatColor.YELLOW + "Damage: " + ChatColor.RED + "20",
                ChatColor.YELLOW + "Speed: " + ChatColor.WHITE + "+25%",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, rogueSword);
        
        return startSlot;
    }
    
    /**
     * Fügt Rüstung hinzu
     */
    private int addArmor(Inventory inventory, int startSlot) {
        // Dragon Armor Set
        ItemStack dragonHelmet = createSkyblockItem(
            Material.DRAGON_HEAD,
            ChatColor.DARK_PURPLE + "§lDragon Helmet",
            Arrays.asList(
                ChatColor.GRAY + "Teil des Dragon Sets",
                ChatColor.YELLOW + "Health: " + ChatColor.RED + "+100",
                ChatColor.YELLOW + "Defense: " + ChatColor.GREEN + "+50",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, dragonHelmet);
        
        ItemStack dragonChestplate = createSkyblockItem(
            Material.ELYTRA,
            ChatColor.DARK_PURPLE + "§lDragon Chestplate",
            Arrays.asList(
                ChatColor.GRAY + "Teil des Dragon Sets",
                ChatColor.YELLOW + "Health: " + ChatColor.RED + "+150",
                ChatColor.YELLOW + "Defense: " + ChatColor.GREEN + "+75",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, dragonChestplate);
        
        return startSlot;
    }
    
    /**
     * Fügt Werkzeuge hinzu
     */
    private int addTools(Inventory inventory, int startSlot) {
        // Treecapitator
        ItemStack treecapitator = createSkyblockItem(
            Material.DIAMOND_AXE,
            ChatColor.GREEN + "§lTreecapitator",
            Arrays.asList(
                ChatColor.GRAY + "Fällt ganze Bäume",
                ChatColor.YELLOW + "Efficiency: " + ChatColor.AQUA + "X",
                ChatColor.YELLOW + "Unbreaking: " + ChatColor.AQUA + "III",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, treecapitator);
        
        // Silk Touch Pickaxe
        ItemStack silkTouchPickaxe = createSkyblockItem(
            Material.DIAMOND_PICKAXE,
            ChatColor.AQUA + "§lSilk Touch Pickaxe",
            Arrays.asList(
                ChatColor.GRAY + "Silk Touch Pickaxe",
                ChatColor.YELLOW + "Efficiency: " + ChatColor.AQUA + "V",
                ChatColor.YELLOW + "Silk Touch: " + ChatColor.AQUA + "I",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, silkTouchPickaxe);
        
        return startSlot;
    }
    
    /**
     * Fügt Accessoires hinzu
     */
    private int addAccessories(Inventory inventory, int startSlot) {
        // Talisman of Coins
        ItemStack talismanOfCoins = createSkyblockItem(
            Material.GOLD_INGOT,
            ChatColor.GOLD + "§lTalisman of Coins",
            Arrays.asList(
                ChatColor.GRAY + "Erhöht deine Münzen",
                ChatColor.YELLOW + "Coin Multiplier: " + ChatColor.GOLD + "+10%",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, talismanOfCoins);
        
        // Speed Talisman
        ItemStack speedTalisman = createSkyblockItem(
            Material.FEATHER,
            ChatColor.WHITE + "§lSpeed Talisman",
            Arrays.asList(
                ChatColor.GRAY + "Erhöht deine Geschwindigkeit",
                ChatColor.YELLOW + "Speed: " + ChatColor.WHITE + "+15%",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, speedTalisman);
        
        return startSlot;
    }
    
    /**
     * Fügt spezielle Items hinzu
     */
    private int addSpecialItems(Inventory inventory, int startSlot) {
        // Booster Cookie
        ItemStack boosterCookie = createSkyblockItem(
            Material.COOKIE,
            ChatColor.LIGHT_PURPLE + "§lBooster Cookie",
            Arrays.asList(
                ChatColor.GRAY + "Gibt dir verschiedene Boni",
                ChatColor.YELLOW + "Duration: " + ChatColor.GREEN + "4 Tage",
                ChatColor.YELLOW + "Effects: " + ChatColor.AQUA + "Multiple",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, boosterCookie);
        
        // God Potion
        ItemStack godPotion = createSkyblockItem(
            Material.POTION,
            ChatColor.DARK_PURPLE + "§lGod Potion",
            Arrays.asList(
                ChatColor.GRAY + "Gibt dir alle Effekte",
                ChatColor.YELLOW + "Duration: " + ChatColor.GREEN + "24 Stunden",
                ChatColor.YELLOW + "Effects: " + ChatColor.AQUA + "All",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, godPotion);
        
        return startSlot;
    }
    
    /**
     * Fügt Minion Items hinzu
     */
    private int addMinionItems(Inventory inventory, int startSlot) {
        // Minion Upgrade
        ItemStack minionUpgrade = createSkyblockItem(
            Material.EMERALD,
            ChatColor.GREEN + "§lMinion Upgrade",
            Arrays.asList(
                ChatColor.GRAY + "Upgraded deine Minions",
                ChatColor.YELLOW + "Speed: " + ChatColor.AQUA + "+25%",
                ChatColor.YELLOW + "Storage: " + ChatColor.AQUA + "+50%",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, minionUpgrade);
        
        // Minion Fuel
        ItemStack minionFuel = createSkyblockItem(
            Material.COAL,
            ChatColor.DARK_GRAY + "§lMinion Fuel",
            Arrays.asList(
                ChatColor.GRAY + "Beschleunigt deine Minions",
                ChatColor.YELLOW + "Speed Boost: " + ChatColor.AQUA + "+50%",
                ChatColor.YELLOW + "Duration: " + ChatColor.GREEN + "24h",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, minionFuel);
        
        return startSlot;
    }
    
    /**
     * Fügt Pet Items hinzu
     */
    private int addPetItems(Inventory inventory, int startSlot) {
        // Pet Food
        ItemStack petFood = createSkyblockItem(
            Material.BONE,
            ChatColor.YELLOW + "§lPet Food",
            Arrays.asList(
                ChatColor.GRAY + "Füttert deine Pets",
                ChatColor.YELLOW + "XP Boost: " + ChatColor.AQUA + "+25%",
                ChatColor.YELLOW + "Duration: " + ChatColor.GREEN + "1h",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, petFood);
        
        // Pet Upgrade Stone
        ItemStack petUpgradeStone = createSkyblockItem(
            Material.NETHER_STAR,
            ChatColor.LIGHT_PURPLE + "§lPet Upgrade Stone",
            Arrays.asList(
                ChatColor.GRAY + "Upgraded deine Pets",
                ChatColor.YELLOW + "Rarity: " + ChatColor.GOLD + "Legendary",
                ChatColor.YELLOW + "Effect: " + ChatColor.AQUA + "Random",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, petUpgradeStone);
        
        return startSlot;
    }
    
    /**
     * Fügt verzauberte Items hinzu
     */
    private int addEnchantedItems(Inventory inventory, int startSlot) {
        // Enchanted Diamond
        ItemStack enchantedDiamond = createSkyblockItem(
            Material.DIAMOND,
            ChatColor.AQUA + "§lEnchanted Diamond",
            Arrays.asList(
                ChatColor.GRAY + "Verzauberter Diamant",
                ChatColor.YELLOW + "Amount: " + ChatColor.AQUA + "160",
                ChatColor.YELLOW + "Used for: " + ChatColor.GREEN + "Crafting",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, enchantedDiamond);
        
        // Enchanted Iron
        ItemStack enchantedIron = createSkyblockItem(
            Material.IRON_INGOT,
            ChatColor.WHITE + "§lEnchanted Iron",
            Arrays.asList(
                ChatColor.GRAY + "Verzaubertes Eisen",
                ChatColor.YELLOW + "Amount: " + ChatColor.AQUA + "160",
                ChatColor.YELLOW + "Used for: " + ChatColor.GREEN + "Crafting",
                "",
                ChatColor.GREEN + "Klicke zum Erhalten!"
            )
        );
        inventory.setItem(startSlot++, enchantedIron);
        
        return startSlot;
    }
    
    /**
     * Fügt Navigations-Items hinzu
     */
    private void addNavigationItems(Inventory inventory) {
        // Previous Page
        ItemStack previousPage = createSkyblockItem(
            Material.ARROW,
            ChatColor.YELLOW + "§lVorherige Seite",
            Arrays.asList(
                ChatColor.GRAY + "Gehe zur vorherigen Seite",
                "",
                ChatColor.GREEN + "Klicke zum Navigieren!"
            )
        );
        inventory.setItem(45, previousPage);
        
        // Next Page
        ItemStack nextPage = createSkyblockItem(
            Material.ARROW,
            ChatColor.YELLOW + "§lNächste Seite",
            Arrays.asList(
                ChatColor.GRAY + "Gehe zur nächsten Seite",
                "",
                ChatColor.GREEN + "Klicke zum Navigieren!"
            )
        );
        inventory.setItem(53, nextPage);
        
        // Close Button
        ItemStack closeButton = createSkyblockItem(
            Material.BARRIER,
            ChatColor.RED + "§lSchließen",
            Arrays.asList(
                ChatColor.GRAY + "Schließt das Arsenal-Menü",
                "",
                ChatColor.RED + "Klicke zum Schließen!"
            )
        );
        inventory.setItem(49, closeButton);
    }
    
    /**
     * Erstellt ein Skyblock Item
     */
    private ItemStack createSkyblockItem(Material material, String name, List<String> lore) {
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
        
        // Prüfe ob es das Arsenal-Menü ist
        if (!net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(event.getView().title()).contains("Arsenal")) return;
        
        event.setCancelled(true);
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Prüfe OP-Berechtigung
        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "Du hast keine Berechtigung für dieses Menü!");
            player.closeInventory();
            return;
        }
        
        // Handle Navigation
        if (clickedItem.getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }
        
        if (clickedItem.getType() == Material.ARROW) {
            // TODO: Implementiere Seiten-Navigation
            player.sendMessage(ChatColor.YELLOW + "Seiten-Navigation wird implementiert...");
            return;
        }
        
        // Gib das Item dem Spieler
        ItemStack itemToGive = clickedItem.clone();
        player.getInventory().addItem(itemToGive);
        
        String itemName = clickedItem.getItemMeta().displayName() != null ? 
            net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText()
                .serialize(clickedItem.getItemMeta().displayName()) : 
            clickedItem.getType().name();
        player.sendMessage(ChatColor.GREEN + "Du hast " + ChatColor.YELLOW + itemName + ChatColor.GREEN + " erhalten!");
    }
}
