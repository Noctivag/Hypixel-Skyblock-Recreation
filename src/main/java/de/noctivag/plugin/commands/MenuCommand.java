package de.noctivag.plugin.commands;
import org.bukkit.inventory.ItemStack;

import de.noctivag.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

public class MenuCommand implements CommandExecutor {
    private final Plugin plugin;
    
    public MenuCommand(Plugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von Spielern verwendet werden!");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Check permission
        if (!player.hasPermission("basicsplugin.menu")) {
            player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
            return true;
        }
        
        openMainMenu(player);
        return true;
    }
    
    private void openMainMenu(Player player) {
        // Create main menu inventory
        Inventory menu = Bukkit.createInventory(null, 54, Component.text("§6§lBasicsPlugin Hauptmenü"));
        
        // Add menu items
        addMenuItem(menu, 10, Material.DIAMOND, "§b§lSkyblock", 
            Arrays.asList("§7Klicke hier um das", "§7Skyblock-System zu öffnen", "", "§e➤ Rechtsklick zum Öffnen"));
        
        addMenuItem(menu, 12, Material.EMERALD, "§a§lEconomy", 
            Arrays.asList("§7Klicke hier um dein", "§7Geld zu verwalten", "", "§e➤ Rechtsklick zum Öffnen"));
        
        addMenuItem(menu, 14, Material.NETHER_STAR, "§d§lCosmetics", 
            Arrays.asList("§7Klicke hier um das", "§7Kosmetik-System zu öffnen", "", "§e➤ Rechtsklick zum Öffnen"));
        
        addMenuItem(menu, 16, Material.GOLD_INGOT, "§6§lDaily Rewards", 
            Arrays.asList("§7Klicke hier um deine", "§7täglichen Belohnungen zu erhalten", "", "§e➤ Rechtsklick zum Öffnen"));
        
        addMenuItem(menu, 20, Material.IRON_SWORD, "§c§lCombat", 
            Arrays.asList("§7Klicke hier um deine", "§7Combat-Statistiken zu sehen", "", "§e➤ Rechtsklick zum Öffnen"));
        
        addMenuItem(menu, 22, Material.DIAMOND_PICKAXE, "§e§lSkills", 
            Arrays.asList("§7Klicke hier um deine", "§7Skills zu verwalten", "", "§e➤ Rechtsklick zum Öffnen"));
        
        addMenuItem(menu, 24, Material.BOOK, "§9§lCollections", 
            Arrays.asList("§7Klicke hier um deine", "§7Sammlungen zu sehen", "", "§e➤ Rechtsklick zum Öffnen"));
        
        addMenuItem(menu, 28, Material.WOLF_SPAWN_EGG, "§a§lPets", 
            Arrays.asList("§7Klicke hier um deine", "§7Haustiere zu verwalten", "", "§e➤ Rechtsklick zum Öffnen"));
        
        addMenuItem(menu, 30, Material.VILLAGER_SPAWN_EGG, "§b§lNPCs", 
            Arrays.asList("§7Klicke hier um das", "§7NPC-System zu öffnen", "", "§e➤ Rechtsklick zum Öffnen"));
        
        addMenuItem(menu, 32, Material.ENDER_PEARL, "§5§lEvents", 
            Arrays.asList("§7Klicke hier um Events", "§7zu verwalten", "", "§e➤ Rechtsklick zum Öffnen"));
        
        addMenuItem(menu, 34, Material.REDSTONE, "§c§lSettings", 
            Arrays.asList("§7Klicke hier um die", "§7Einstellungen zu öffnen", "", "§e➤ Rechtsklick zum Öffnen"));
        
        // Add close button
        addMenuItem(menu, 49, Material.BARRIER, "§c§lSchließen", 
            Arrays.asList("§7Klicke hier um das", "§7Menü zu schließen", "", "§e➤ Rechtsklick zum Schließen"));
        
        // Fill empty slots with glass panes
        ItemStack glassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glassPane.getItemMeta();
        glassMeta.displayName(Component.text(" "));
        glassPane.setItemMeta(glassMeta);
        
        for (int i = 0; i < 54; i++) {
            if (menu.getItem(i) == null) {
                menu.setItem(i, glassPane);
            }
        }
        
        player.openInventory(menu);
        player.sendMessage("§a§lBasicsPlugin Hauptmenü geöffnet!");
    }
    
    private void addMenuItem(Inventory inventory, int slot, Material material, String name, java.util.List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(Component.text(name));
        meta.lore(lore.stream().map(Component::text).toList());
        
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }
}
