package de.noctivag.plugin.advancedmechanics;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.database.MultiServerDatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedMechanicsSystem implements Listener {
    
    private final Plugin plugin;
    private final MultiServerDatabaseManager databaseManager;
    private final Map<UUID, PlayerMechanicsData> playerMechanicsData = new ConcurrentHashMap<>();
    
    public AdvancedMechanicsSystem(Plugin plugin, MultiServerDatabaseManager databaseManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !item.hasItemMeta()) return;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;
        
        String displayName = meta.getDisplayName();
        
        if (displayName.contains("Advanced Mechanics") || displayName.contains("Mechanics")) {
            openMechanicsGUI(player);
        }
    }
    
    public void openMechanicsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§c§lAdvanced Mechanics");
        
        // Add mechanics categories
        addGUIItem(gui, 10, Material.ENCHANTED_BOOK, "§d§lEnchanting", "§7Advanced enchanting mechanics.");
        addGUIItem(gui, 11, Material.ANVIL, "§7§lForging", "§7Advanced forging mechanics.");
        addGUIItem(gui, 12, Material.BREWING_STAND, "§5§lAlchemy", "§7Advanced alchemy mechanics.");
        addGUIItem(gui, 13, Material.CRAFTING_TABLE, "§6§lCrafting", "§7Advanced crafting mechanics.");
        addGUIItem(gui, 14, Material.FURNACE, "§8§lSmelting", "§7Advanced smelting mechanics.");
        addGUIItem(gui, 15, Material.CAULDRON, "§b§lBrewing", "§7Advanced brewing mechanics.");
        addGUIItem(gui, 16, Material.ENCHANTING_TABLE, "§c§lRunes", "§7Advanced rune mechanics.");
        addGUIItem(gui, 17, Material.NETHER_STAR, "§e§lFusion", "§7Advanced fusion mechanics.");
        addGUIItem(gui, 18, Material.DRAGON_EGG, "§d§lTransmutation", "§7Advanced transmutation mechanics.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lPrevious Page", "§7Go to previous page.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the mechanics menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
        player.sendMessage("§aAdvanced Mechanics GUI geöffnet!");
    }
    
    public void openEnchantingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§d§lAdvanced Enchanting");
        
        // Add enchanting features
        addGUIItem(gui, 10, Material.ENCHANTED_BOOK, "§d§lEnchant", "§7Enchant items with advanced mechanics.");
        addGUIItem(gui, 11, Material.ANVIL, "§7§lCombine", "§7Combine enchantments.");
        addGUIItem(gui, 12, Material.GRINDSTONE, "§8§lDisenchant", "§7Remove enchantments.");
        addGUIItem(gui, 13, Material.ENCHANTING_TABLE, "§c§lEnchanting Table", "§7Use enchanting table.");
        addGUIItem(gui, 14, Material.BOOKSHELF, "§6§lBookshelf", "§7Manage bookshelves.");
        addGUIItem(gui, 15, Material.EXPERIENCE_BOTTLE, "§a§lExperience", "§7Manage experience.");
        addGUIItem(gui, 16, Material.LAPIS_LAZULI, "§9§lLapis Lazuli", "§7Manage lapis lazuli.");
        addGUIItem(gui, 17, Material.NETHER_STAR, "§e§lSpecial Enchants", "§7Special enchantments.");
        addGUIItem(gui, 18, Material.DRAGON_EGG, "§d§lLegendary Enchants", "§7Legendary enchantments.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to mechanics menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the enchanting menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openForgingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§7§lAdvanced Forging");
        
        // Add forging features
        addGUIItem(gui, 10, Material.ANVIL, "§7§lAnvil", "§7Use anvil for forging.");
        addGUIItem(gui, 11, Material.SMITHING_TABLE, "§8§lSmithing Table", "§7Use smithing table.");
        addGUIItem(gui, 12, Material.FURNACE, "§6§lFurnace", "§7Use furnace for smelting.");
        addGUIItem(gui, 13, Material.BLAST_FURNACE, "§c§lBlast Furnace", "§7Use blast furnace.");
        addGUIItem(gui, 14, Material.SMOKER, "§8§lSmoker", "§7Use smoker.");
        addGUIItem(gui, 15, Material.CAMPFIRE, "§6§lCampfire", "§7Use campfire.");
        addGUIItem(gui, 16, Material.SOUL_CAMPFIRE, "§5§lSoul Campfire", "§7Use soul campfire.");
        addGUIItem(gui, 17, Material.NETHER_STAR, "§e§lSpecial Forging", "§7Special forging techniques.");
        addGUIItem(gui, 18, Material.DRAGON_EGG, "§d§lLegendary Forging", "§7Legendary forging techniques.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to mechanics menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the forging menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openAlchemyGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§5§lAdvanced Alchemy");
        
        // Add alchemy features
        addGUIItem(gui, 10, Material.BREWING_STAND, "§5§lBrewing Stand", "§7Use brewing stand.");
        addGUIItem(gui, 11, Material.CAULDRON, "§b§lCauldron", "§7Use cauldron.");
        addGUIItem(gui, 12, Material.POTION, "§d§lPotion", "§7Create potions.");
        addGUIItem(gui, 13, Material.SPLASH_POTION, "§c§lSplash Potion", "§7Create splash potions.");
        addGUIItem(gui, 14, Material.LINGERING_POTION, "§5§lLingering Potion", "§7Create lingering potions.");
        addGUIItem(gui, 15, Material.TIPPED_ARROW, "§e§lTipped Arrow", "§7Create tipped arrows.");
        addGUIItem(gui, 16, Material.NETHER_WART, "§c§lNether Wart", "§7Manage nether wart.");
        addGUIItem(gui, 17, Material.NETHER_STAR, "§e§lSpecial Alchemy", "§7Special alchemy techniques.");
        addGUIItem(gui, 18, Material.DRAGON_EGG, "§d§lLegendary Alchemy", "§7Legendary alchemy techniques.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to mechanics menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the alchemy menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openCraftingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lAdvanced Crafting");
        
        // Add crafting features
        addGUIItem(gui, 10, Material.CRAFTING_TABLE, "§6§lCrafting Table", "§7Use crafting table.");
        addGUIItem(gui, 11, Material.CARTOGRAPHY_TABLE, "§b§lCartography Table", "§7Use cartography table.");
        addGUIItem(gui, 12, Material.FLETCHING_TABLE, "§e§lFletching Table", "§7Use fletching table.");
        addGUIItem(gui, 13, Material.LOOM, "§d§lLoom", "§7Use loom.");
        addGUIItem(gui, 14, Material.STONECUTTER, "§7§lStonecutter", "§7Use stonecutter.");
        addGUIItem(gui, 15, Material.COMPOSTER, "§a§lComposter", "§7Use composter.");
        addGUIItem(gui, 16, Material.NETHER_STAR, "§e§lSpecial Crafting", "§7Special crafting techniques.");
        addGUIItem(gui, 17, Material.DRAGON_EGG, "§d§lLegendary Crafting", "§7Legendary crafting techniques.");
        addGUIItem(gui, 18, Material.ENCHANTED_BOOK, "§c§lEnchanted Crafting", "§7Enchanted crafting techniques.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to mechanics menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the crafting menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openSmeltingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§8§lAdvanced Smelting");
        
        // Add smelting features
        addGUIItem(gui, 10, Material.FURNACE, "§8§lFurnace", "§7Use furnace.");
        addGUIItem(gui, 11, Material.BLAST_FURNACE, "§c§lBlast Furnace", "§7Use blast furnace.");
        addGUIItem(gui, 12, Material.SMOKER, "§7§lSmoker", "§7Use smoker.");
        addGUIItem(gui, 13, Material.CAMPFIRE, "§6§lCampfire", "§7Use campfire.");
        addGUIItem(gui, 14, Material.SOUL_CAMPFIRE, "§5§lSoul Campfire", "§7Use soul campfire.");
        addGUIItem(gui, 15, Material.NETHER_STAR, "§e§lSpecial Smelting", "§7Special smelting techniques.");
        addGUIItem(gui, 16, Material.DRAGON_EGG, "§d§lLegendary Smelting", "§7Legendary smelting techniques.");
        addGUIItem(gui, 17, Material.ENCHANTED_BOOK, "§c§lEnchanted Smelting", "§7Enchanted smelting techniques.");
        addGUIItem(gui, 18, Material.ANVIL, "§7§lAnvil Smelting", "§7Anvil smelting techniques.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to mechanics menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the smelting menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openBrewingGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§b§lAdvanced Brewing");
        
        // Add brewing features
        addGUIItem(gui, 10, Material.BREWING_STAND, "§b§lBrewing Stand", "§7Use brewing stand.");
        addGUIItem(gui, 11, Material.CAULDRON, "§5§lCauldron", "§7Use cauldron.");
        addGUIItem(gui, 12, Material.POTION, "§d§lPotion", "§7Create potions.");
        addGUIItem(gui, 13, Material.SPLASH_POTION, "§c§lSplash Potion", "§7Create splash potions.");
        addGUIItem(gui, 14, Material.LINGERING_POTION, "§5§lLingering Potion", "§7Create lingering potions.");
        addGUIItem(gui, 15, Material.TIPPED_ARROW, "§e§lTipped Arrow", "§7Create tipped arrows.");
        addGUIItem(gui, 16, Material.NETHER_WART, "§c§lNether Wart", "§7Manage nether wart.");
        addGUIItem(gui, 17, Material.NETHER_STAR, "§e§lSpecial Brewing", "§7Special brewing techniques.");
        addGUIItem(gui, 18, Material.DRAGON_EGG, "§d§lLegendary Brewing", "§7Legendary brewing techniques.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to mechanics menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the brewing menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openRunesGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§c§lAdvanced Runes");
        
        // Add rune features
        addGUIItem(gui, 10, Material.ENCHANTING_TABLE, "§c§lRune Table", "§7Use rune table.");
        addGUIItem(gui, 11, Material.ENCHANTED_BOOK, "§d§lRune Book", "§7Create rune books.");
        addGUIItem(gui, 12, Material.LAPIS_LAZULI, "§9§lLapis Lazuli", "§7Manage lapis lazuli.");
        addGUIItem(gui, 13, Material.EXPERIENCE_BOTTLE, "§a§lExperience", "§7Manage experience.");
        addGUIItem(gui, 14, Material.BOOKSHELF, "§6§lBookshelf", "§7Manage bookshelves.");
        addGUIItem(gui, 15, Material.NETHER_STAR, "§e§lSpecial Runes", "§7Special rune techniques.");
        addGUIItem(gui, 16, Material.DRAGON_EGG, "§d§lLegendary Runes", "§7Legendary rune techniques.");
        addGUIItem(gui, 17, Material.ENCHANTED_BOOK, "§c§lEnchanted Runes", "§7Enchanted rune techniques.");
        addGUIItem(gui, 18, Material.ANVIL, "§7§lAnvil Runes", "§7Anvil rune techniques.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to mechanics menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the runes menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openFusionGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§e§lAdvanced Fusion");
        
        // Add fusion features
        addGUIItem(gui, 10, Material.NETHER_STAR, "§e§lFusion Core", "§7Use fusion core.");
        addGUIItem(gui, 11, Material.ANVIL, "§7§lFusion Anvil", "§7Use fusion anvil.");
        addGUIItem(gui, 12, Material.ENCHANTING_TABLE, "§c§lFusion Table", "§7Use fusion table.");
        addGUIItem(gui, 13, Material.CRAFTING_TABLE, "§6§lFusion Crafting", "§7Use fusion crafting.");
        addGUIItem(gui, 14, Material.FURNACE, "§8§lFusion Furnace", "§7Use fusion furnace.");
        addGUIItem(gui, 15, Material.BREWING_STAND, "§5§lFusion Brewing", "§7Use fusion brewing.");
        addGUIItem(gui, 16, Material.DRAGON_EGG, "§d§lLegendary Fusion", "§7Legendary fusion techniques.");
        addGUIItem(gui, 17, Material.ENCHANTED_BOOK, "§c§lEnchanted Fusion", "§7Enchanted fusion techniques.");
        addGUIItem(gui, 18, Material.ANVIL, "§7§lAnvil Fusion", "§7Anvil fusion techniques.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to mechanics menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the fusion menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    public void openTransmutationGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, net.kyori.adventure.text.Component.text("§d§lAdvanced Transmutation"));
        
        // Add transmutation features
        addGUIItem(gui, 10, Material.DRAGON_EGG, "§d§lTransmutation Core", "§7Use transmutation core.");
        addGUIItem(gui, 11, Material.ANVIL, "§7§lTransmutation Anvil", "§7Use transmutation anvil.");
        addGUIItem(gui, 12, Material.ENCHANTING_TABLE, "§c§lTransmutation Table", "§7Use transmutation table.");
        addGUIItem(gui, 13, Material.CRAFTING_TABLE, "§6§lTransmutation Crafting", "§7Use transmutation crafting.");
        addGUIItem(gui, 14, Material.FURNACE, "§8§lTransmutation Furnace", "§7Use transmutation furnace.");
        addGUIItem(gui, 15, Material.BREWING_STAND, "§5§lTransmutation Brewing", "§7Use transmutation brewing.");
        addGUIItem(gui, 16, Material.NETHER_STAR, "§e§lSpecial Transmutation", "§7Special transmutation techniques.");
        addGUIItem(gui, 17, Material.ENCHANTED_BOOK, "§c§lEnchanted Transmutation", "§7Enchanted transmutation techniques.");
        addGUIItem(gui, 18, Material.ANVIL, "§7§lAnvil Transmutation", "§7Anvil transmutation techniques.");
        
        // Add navigation items
        addGUIItem(gui, 45, Material.ARROW, "§7§lBack", "§7Go back to mechanics menu.");
        addGUIItem(gui, 49, Material.BARRIER, "§c§lClose", "§7Close the transmutation menu.");
        addGUIItem(gui, 53, Material.ARROW, "§7§lNext Page", "§7Go to next page.");
        
        player.openInventory(gui);
    }
    
    private void addGUIItem(Inventory gui, int slot, Material material, String name, String description) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(description));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    public PlayerMechanicsData getPlayerMechanicsData(UUID playerId) {
        return playerMechanicsData.computeIfAbsent(playerId, k -> new PlayerMechanicsData(playerId));
    }
    
    public static class PlayerMechanicsData {
        private final UUID playerId;
        private final Map<String, Integer> mechanicsLevels = new HashMap<>();
        private final Map<String, Long> mechanicsCooldowns = new HashMap<>();
        private long lastUpdate;
        
        public PlayerMechanicsData(UUID playerId) {
            this.playerId = playerId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void update() {
            this.lastUpdate = System.currentTimeMillis();
        }
        
        public void setMechanicsLevel(String mechanics, int level) {
            mechanicsLevels.put(mechanics, level);
        }
        
        public int getMechanicsLevel(String mechanics) {
            return mechanicsLevels.getOrDefault(mechanics, 0);
        }
        
        public void setMechanicsCooldown(String mechanics, long cooldown) {
            mechanicsCooldowns.put(mechanics, cooldown);
        }
        
        public long getMechanicsCooldown(String mechanics) {
            return mechanicsCooldowns.getOrDefault(mechanics, 0L);
        }
        
        public long getLastUpdate() {
            return lastUpdate;
        }
    }
}
