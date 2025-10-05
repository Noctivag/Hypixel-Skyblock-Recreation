package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.gui.framework.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdvancedDungeonGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;

    public AdvancedDungeonGUI(SkyblockPluginRefactored plugin, Player player) {
        super(player, 54);
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(this, 54, "§8Advanced Dungeons");
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getMenuTitle() {
        return "§8Advanced Dungeons";
    }

    @Override
    public void setMenuItems() {
        fillBorderWith(Material.GRAY_STAINED_GLASS_PANE);

        // Title item
        ItemStack titleItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§lAdvanced Dungeons");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Erweiterte Dungeon-Inhalte");
        titleLore.add("§7mit neuen Mobs und Bossen!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Catacombs Floor 1
        ItemStack catacombsF1 = new ItemStack(Material.SKELETON_SKULL);
        ItemMeta catacombsF1Meta = catacombsF1.getItemMeta();
        catacombsF1Meta.setDisplayName("§c§lCatacombs Floor 1");
        List<String> catacombsF1Lore = new ArrayList<>();
        catacombsF1Lore.add("§7Boss: §cBonzo");
        catacombsF1Lore.add("§7Schwierigkeit: §aEinfach");
        catacombsF1Lore.add("§7Empfohlenes Level: §e5");
        catacombsF1Lore.add("§7Max. Spieler: §b5");
        catacombsF1Lore.add("");
        catacombsF1Lore.add("§7Neue Mobs:");
        catacombsF1Lore.add("§7• Lost Adventurer");
        catacombsF1Lore.add("§7• Shadow Assassin");
        catacombsF1Lore.add("§7• Felsworn");
        catacombsF1Lore.add("§7• Withermancer");
        catacombsF1Lore.add("");
        catacombsF1Lore.add("§7Belohnungen:");
        catacombsF1Lore.add("§a• 1000 Coins");
        catacombsF1Lore.add("§a• 500 XP");
        catacombsF1Lore.add("§a• Dungeon Key");
        catacombsF1Lore.add("§a• Shadow Assassin Armor");
        catacombsF1Lore.add("");
        catacombsF1Lore.add("§eKlicke um zu starten!");
        catacombsF1Meta.setLore(catacombsF1Lore);
        catacombsF1.setItemMeta(catacombsF1Meta);
        inventory.setItem(20, catacombsF1);

        // Catacombs Floor 2
        ItemStack catacombsF2 = new ItemStack(Material.WITHER_SKELETON_SKULL);
        ItemMeta catacombsF2Meta = catacombsF2.getItemMeta();
        catacombsF2Meta.setDisplayName("§c§lCatacombs Floor 2");
        List<String> catacombsF2Lore = new ArrayList<>();
        catacombsF2Lore.add("§7Boss: §cScarf");
        catacombsF2Lore.add("§7Schwierigkeit: §eMittel");
        catacombsF2Lore.add("§7Empfohlenes Level: §e10");
        catacombsF2Lore.add("§7Max. Spieler: §b5");
        catacombsF2Lore.add("");
        catacombsF2Lore.add("§7Neue Mobs:");
        catacombsF2Lore.add("§7• Strong Lost Adventurer");
        catacombsF2Lore.add("§7• Elite Shadow Assassin");
        catacombsF2Lore.add("§7• Ancient Felsworn");
        catacombsF2Lore.add("§7• Master Withermancer");
        catacombsF2Lore.add("");
        catacombsF2Lore.add("§7Belohnungen:");
        catacombsF2Lore.add("§a• 2500 Coins");
        catacombsF2Lore.add("§a• 1000 XP");
        catacombsF2Lore.add("§a• Dungeon Key");
        catacombsF2Lore.add("§a• Seltene Items");
        catacombsF2Lore.add("§a• Spirit Sceptre");
        catacombsF2Lore.add("");
        catacombsF2Lore.add("§cBenötigt: Catacombs Floor 1 abgeschlossen");
        catacombsF2Meta.setLore(catacombsF2Lore);
        catacombsF2.setItemMeta(catacombsF2Meta);
        inventory.setItem(22, catacombsF2);

        // Catacombs Floor 3
        ItemStack catacombsF3 = new ItemStack(Material.DRAGON_HEAD);
        ItemMeta catacombsF3Meta = catacombsF3.getItemMeta();
        catacombsF3Meta.setDisplayName("§c§lCatacombs Floor 3");
        List<String> catacombsF3Lore = new ArrayList<>();
        catacombsF3Lore.add("§7Boss: §cThe Professor");
        catacombsF3Lore.add("§7Schwierigkeit: §6Schwer");
        catacombsF3Lore.add("§7Empfohlenes Level: §e15");
        catacombsF3Lore.add("§7Max. Spieler: §b5");
        catacombsF3Lore.add("");
        catacombsF3Lore.add("§7Neue Mobs:");
        catacombsF3Lore.add("§7• Legendary Lost Adventurer");
        catacombsF3Lore.add("§7• Shadow Lord");
        catacombsF3Lore.add("§7• Felsworn Titan");
        catacombsF3Lore.add("§7• Withermancer Archmage");
        catacombsF3Lore.add("");
        catacombsF3Lore.add("§7Belohnungen:");
        catacombsF3Lore.add("§a• 5000 Coins");
        catacombsF3Lore.add("§a• 2000 XP");
        catacombsF3Lore.add("§a• Dungeon Key");
        catacombsF3Lore.add("§a• Epische Items");
        catacombsF3Lore.add("§a• Wither Blades");
        catacombsF3Lore.add("");
        catacombsF3Lore.add("§cBenötigt: Catacombs Floor 2 abgeschlossen");
        catacombsF3Meta.setLore(catacombsF3Lore);
        catacombsF3.setItemMeta(catacombsF3Meta);
        inventory.setItem(24, catacombsF3);

        // Dungeon Mobs Info
        ItemStack mobsInfo = new ItemStack(Material.SPAWNER);
        ItemMeta mobsInfoMeta = mobsInfo.getItemMeta();
        mobsInfoMeta.setDisplayName("§b§lDungeon Mobs");
        List<String> mobsInfoLore = new ArrayList<>();
        mobsInfoLore.add("§7Neue Dungeon-Mobs:");
        mobsInfoLore.add("");
        mobsInfoLore.add("§7§lLost Adventurer:");
        mobsInfoLore.add("§7• Fähigkeit: Charge & Shield");
        mobsInfoLore.add("§7• Drops: Iron, Arrows, Diamonds");
        mobsInfoLore.add("");
        mobsInfoLore.add("§8§lShadow Assassin:");
        mobsInfoLore.add("§7• Fähigkeit: Stealth & Teleport");
        mobsInfoLore.add("§7• Drops: Ender Pearls, Shadow Armor");
        mobsInfoLore.add("");
        mobsInfoLore.add("§7§lFelsworn:");
        mobsInfoLore.add("§7• Fähigkeit: Rock Throw & Ground Slam");
        mobsInfoLore.add("§7• Drops: Stone, Iron, Stone Bricks");
        mobsInfoLore.add("");
        mobsInfoLore.add("§8§lWithermancer:");
        mobsInfoLore.add("§7• Fähigkeit: Death Ray & Wither Aura");
        mobsInfoLore.add("§7• Drops: Wither Skulls, Nether Stars");
        mobsInfoMeta.setLore(mobsInfoLore);
        mobsInfo.setItemMeta(mobsInfoMeta);
        inventory.setItem(31, mobsInfo);

        // Dungeon Items Info
        ItemStack itemsInfo = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta itemsInfoMeta = itemsInfo.getItemMeta();
        itemsInfoMeta.setDisplayName("§6§lDungeon Items");
        List<String> itemsInfoLore = new ArrayList<>();
        itemsInfoLore.add("§7Endgame-Items:");
        itemsInfoLore.add("");
        itemsInfoLore.add("§8§lWither Blades:");
        itemsInfoLore.add("§7• Hyperion: Implosion-Fähigkeit");
        itemsInfoLore.add("§7• Scylla: Heilungs-Fähigkeit");
        itemsInfoLore.add("§7• Astraea: Verteidigungs-Fähigkeit");
        itemsInfoLore.add("§7• Valkyrie: Geschwindigkeits-Fähigkeit");
        itemsInfoLore.add("");
        itemsInfoLore.add("§b§lSpirit Sceptre:");
        itemsInfoLore.add("§7• Magier-Waffe");
        itemsInfoLore.add("§7• Zielsuchende Fledermaus-Projektile");
        itemsInfoLore.add("");
        itemsInfoLore.add("§8§lShadow Assassin Armor:");
        itemsInfoLore.add("§7• Teleportationsfähigkeiten");
        itemsInfoLore.add("§7• Crit-Schaden Verbesserung");
        itemsInfoLore.add("");
        itemsInfoLore.add("§c§lNecron's Armor:");
        itemsInfoLore.add("§7• Bestes Endgame-Rüstungsset");
        itemsInfoLore.add("§7• Massive Stat-Boni");
        itemsInfoMeta.setLore(itemsInfoLore);
        itemsInfo.setItemMeta(itemsInfoMeta);
        inventory.setItem(33, itemsInfo);

        addNavigationButtons();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getInventory().equals(inventory)) return;
        
        event.setCancelled(true);
        
        Player clickedPlayer = (Player) event.getWhoClicked();
        if (!clickedPlayer.equals(player)) return;
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Handle navigation buttons
        if (event.getSlot() == 45) { // Back button
            clickedPlayer.closeInventory();
            return;
        }
        if (event.getSlot() == 53) { // Close button
            clickedPlayer.closeInventory();
            return;
        }
        
        // Handle dungeon selection
        switch (event.getSlot()) {
            case 20: // Catacombs Floor 1
                startDungeon(clickedPlayer, "catacombs", 1);
                break;
            case 22: // Catacombs Floor 2
                startDungeon(clickedPlayer, "catacombs", 2);
                break;
            case 24: // Catacombs Floor 3
                startDungeon(clickedPlayer, "catacombs", 3);
                break;
        }
    }

    private void startDungeon(Player player, String dungeonType, int floor) {
        // Get DungeonManager from ServiceManager
        var dungeonManager = plugin.getServiceManager().getService(de.noctivag.skyblock.dungeons.DungeonManager.class);
        if (dungeonManager == null) {
            player.sendMessage("§cDungeon-System ist nicht verfügbar!");
            return;
        }

        if (dungeonManager.isPlayerInDungeon(player)) {
            player.sendMessage("§cDu bist bereits in einem Dungeon!");
            return;
        }

        if (dungeonManager.startDungeon(player, dungeonType, floor)) {
            player.closeInventory();
        } else {
            player.sendMessage("§cFehler beim Starten des Dungeons!");
        }
    }
}
