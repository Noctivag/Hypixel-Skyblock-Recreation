package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPluginRefactored;
import de.noctivag.skyblock.enums.DungeonClass;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.services.ClassManager;
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

public class ClassSelectionGUI extends Menu implements Listener {

    private final SkyblockPluginRefactored plugin;
    private final ClassManager classManager;

    public ClassSelectionGUI(SkyblockPluginRefactored plugin, Player player) {
        super(player, 54);
        this.plugin = plugin;
        this.classManager = plugin.getServiceManager().getService(ClassManager.class);
        this.inventory = Bukkit.createInventory(this, 54, "§8Dungeon-Klassen");
        
        // Register this as an event listener
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public String getMenuTitle() {
        return "§8Dungeon-Klassen";
    }

    @Override
    public void setMenuItems() {
        fillBorderWith(Material.GRAY_STAINED_GLASS_PANE);

        // Title item in the center
        ItemStack titleItem = new ItemStack(Material.NETHER_STAR);
        ItemMeta titleMeta = titleItem.getItemMeta();
        titleMeta.setDisplayName("§6§lDungeon-Klassen");
        List<String> titleLore = new ArrayList<>();
        titleLore.add("§7Wähle deine bevorzugte Dungeon-Klasse");
        titleLore.add("§7Jede Klasse hat einzigartige Fähigkeiten!");
        titleMeta.setLore(titleLore);
        titleItem.setItemMeta(titleMeta);
        inventory.setItem(4, titleItem);

        // Current class display
        DungeonClass currentClass = classManager.getPlayerClass(player);
        ItemStack currentClassItem = new ItemStack(currentClass.getIcon());
        ItemMeta currentMeta = currentClassItem.getItemMeta();
        currentMeta.setDisplayName("§a§lAktuelle Klasse: " + currentClass.getDisplayName());
        List<String> currentLore = new ArrayList<>();
        currentLore.add(currentClass.getDescription());
        currentLore.add("");
        currentLore.add("§7Klicke auf eine andere Klasse um zu wechseln!");
        currentMeta.setLore(currentLore);
        currentClassItem.setItemMeta(currentMeta);
        inventory.setItem(13, currentClassItem);

        // Class selection items
        int[] classSlots = {20, 22, 24, 30, 32}; // Positions for the 5 classes
        DungeonClass[] classes = DungeonClass.values();

        for (int i = 0; i < classes.length && i < classSlots.length; i++) {
            DungeonClass dungeonClass = classes[i];
            ItemStack classItem = new ItemStack(dungeonClass.getIcon());
            ItemMeta classMeta = classItem.getItemMeta();
            
            // Highlight current class
            if (dungeonClass == currentClass) {
                classMeta.setDisplayName("§a§l✓ " + dungeonClass.getDisplayName() + " §a§l(AKTIV)");
            } else {
                classMeta.setDisplayName(dungeonClass.getDisplayName());
            }
            
            List<String> classLore = new ArrayList<>();
            classLore.add(dungeonClass.getDescription());
            classLore.add("");
            
            // Add class-specific abilities
            switch (dungeonClass) {
                case ARCHER:
                    classLore.add("§7• §a+25% Fernkampf-Schaden");
                    classLore.add("§7• §a+15% Pfeil-Geschwindigkeit");
                    classLore.add("§7• §aSpezial: Explosive Pfeile");
                    break;
                case MAGE:
                    classLore.add("§7• §a+30% Magie-Schaden");
                    classLore.add("§7• §a+20% Mana-Regeneration");
                    classLore.add("§7• §aSpezial: Teleportation");
                    break;
                case BERSERKER:
                    classLore.add("§7• §a+35% Nahkampf-Schaden");
                    classLore.add("§7• §a+10% Angriffsgeschwindigkeit");
                    classLore.add("§7• §aSpezial: Wut-Modus");
                    break;
                case HEALER:
                    classLore.add("§7• §a+50% Heilung-Effektivität");
                    classLore.add("§7• §a+25% Team-Heilung");
                    classLore.add("§7• §aSpezial: Gruppen-Heilung");
                    break;
                case TANK:
                    classLore.add("§7• §a+40% Verteidigung");
                    classLore.add("§7• §a+20% Schild-Blockierung");
                    classLore.add("§7• §aSpezial: Schild-Wall");
                    break;
            }
            
            classLore.add("");
            if (dungeonClass == currentClass) {
                classLore.add("§a§lDiese Klasse ist bereits aktiv!");
            } else {
                classLore.add("§eKlicke um diese Klasse zu wählen!");
            }
            
            classMeta.setLore(classLore);
            classItem.setItemMeta(classMeta);
            inventory.setItem(classSlots[i], classItem);
        }

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
        
        // Handle class selection
        int[] classSlots = {20, 22, 24, 30, 32};
        DungeonClass[] classes = DungeonClass.values();
        
        for (int i = 0; i < classSlots.length && i < classes.length; i++) {
            if (event.getSlot() == classSlots[i]) {
                DungeonClass selectedClass = classes[i];
                DungeonClass currentClass = classManager.getPlayerClass(clickedPlayer);
                
                if (selectedClass == currentClass) {
                    clickedPlayer.sendMessage("§cDu hast bereits die " + selectedClass.getDisplayName() + " §cKlasse gewählt!");
                    return;
                }
                
                if (!classManager.canChangeClass(clickedPlayer)) {
                    clickedPlayer.sendMessage("§cDu kannst deine Klasse derzeit nicht ändern!");
                    return;
                }
                
                if (classManager.setPlayerClass(clickedPlayer, selectedClass)) {
                    // Refresh the GUI to show the new selection
                    setMenuItems();
                    clickedPlayer.sendMessage("§aKlasse erfolgreich zu " + selectedClass.getDisplayName() + " §ageändert!");
                } else {
                    clickedPlayer.sendMessage("§cFehler beim Ändern der Klasse!");
                }
                break;
            }
        }
    }
}
