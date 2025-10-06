package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.gui.features.*;
import de.noctivag.skyblock.gui.framework.Menu;
import de.noctivag.skyblock.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Ultimate Main Menu - Zentraler Hub für alle Skyblock-Features
 */
public class UltimateMainMenu extends Menu {
    
    public UltimateMainMenu(SkyblockPlugin plugin, Player player) {
        super(plugin, player, "§8§lHYPIXEL SKYBLOCK", 54);
    }
    
    @Override
    public void setupItems() {
        fillBorders();
        
        // Skills
        setItem(10, Material.DIAMOND_PICKAXE, "Skills", "uncommon",
            "&7Verbessere deine Fähigkeiten",
            "&7und sammle XP in verschiedenen",
            "&7Bereichen wie Bergbau, Kampf, etc.",
            "",
            "&eKlicke zum Öffnen");
        
        // Collections
        setItem(12, Material.CHEST, "Sammlungen", "uncommon",
            "&7Sammle Items, um Belohnungen",
            "&7freizuschalten und deine",
            "&7Sammlungen zu erweitern",
            "",
            "&eKlicke zum Öffnen");
        
        // Auction House
        setItem(14, Material.GOLD_INGOT, "Auktionshaus", "legendary",
            "&7Kaufe und verkaufe Items",
            "&7mit anderen Spielern",
            "",
            "&eKlicke zum Öffnen");
        
        // Bazaar
        setItem(16, Material.EMERALD, "Bazaar", "uncommon",
            "&7Automatischer Marktplatz",
            "&7für schnelle Käufe und Verkäufe",
            "",
            "&eKlicke zum Öffnen");
        
        // Wardrobe
        setItem(28, Material.LEATHER_CHESTPLATE, "Garderobe", "uncommon",
            "&7Speichere und wechsle",
            "&7zwischen verschiedenen",
            "&7Rüstungssets",
            "",
            "&eKlicke zum Öffnen");
        
        // Trading
        setItem(30, Material.ANVIL, "Handel", "uncommon",
            "&7Handele direkt mit",
            "&7anderen Spielern",
            "",
            "&eKlicke zum Öffnen");
        
        // Bank
        setItem(32, Material.ENDER_CHEST, "Bank", "uncommon",
            "&7Verwalte dein Geld",
            "&7und sammle Zinsen",
            "",
            "&eKlicke zum Öffnen");
        
        // Dungeon Finder
        setItem(34, Material.ZOMBIE_HEAD, "Dungeon-Finder", "mythic",
            "&7Finde Gruppen für",
            "&7Dungeon-Abenteuer",
            "",
            "&eKlicke zum Öffnen");
        
        // Fast Travel
        setItem(19, Material.ENDER_PEARL, "Schnellreise", "uncommon",
            "&7Teleportiere schnell zu",
            "&7verschiedenen Orten",
            "",
            "&eKlicke zum Öffnen");
        
        // Recipe Book
        setItem(21, Material.BOOK, "Rezeptbuch", "uncommon",
            "&7Zeige alle verfügbaren",
            "&7Crafting-Rezepte",
            "",
            "&eKlicke zum Öffnen");
        
        // Calendar & Events
        setItem(23, Material.CLOCK, "Kalender & Events", "uncommon",
            "&7Zeige aktuelle und",
            "&7kommende Events",
            "",
            "&eKlicke zum Öffnen");
        
        // Friends
        setItem(25, Material.PLAYER_HEAD, "Freunde", "uncommon",
            "&7Verwalte deine Freunde",
            "&7und Party-System",
            "",
            "&eKlicke zum Öffnen");
        
        // Settings
        setItem(37, Material.COMPARATOR, "Einstellungen", "uncommon",
            "&7Passe deine Spiel-",
            "&7einstellungen an",
            "",
            "&eKlicke zum Öffnen");
        
        // Daily Rewards
        setItem(39, Material.CHEST, "Tägliche Belohnungen", "legendary",
            "&7Hole dir deine täglichen",
            "&7Belohnungen ab",
            "",
            "&eKlicke zum Öffnen");
        
        // Profile
        setItem(41, Material.PLAYER_HEAD, "Profil", "uncommon",
            "&7Zeige deine Statistiken",
            "&7und Fortschritte",
            "",
            "&eKlicke zum Öffnen");
        
        // Schließen-Button
        setCloseButton(49);
    }
    
    @Override
    public void handleMenuClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        
        switch (slot) {
            case 10:
                new SkillsGUI(plugin, player).open();
                break;
            case 12:
                new CollectionsGUI(plugin, player).open();
                break;
            case 14:
                new AuctionHouseGUI(plugin, player).open();
                break;
            case 16:
                new BazaarGUI(plugin, player).open();
                break;
            case 19:
                new FastTravelGUI(plugin, player).open();
                break;
            case 21:
                new RecipeBookGUI(plugin, player).open();
                break;
            case 23:
                new CalendarGUI(plugin, player).open();
                break;
            case 25:
                new FriendsGUI(plugin, player).open();
                break;
            case 28:
                new WardrobeGUI(plugin, player).open();
                break;
            case 30:
                // Trading benötigt einen Partner - zeige Partner-Auswahl
                player.sendMessage("§cTrading benötigt einen Partner! Verwende /trade <spieler>");
                break;
            case 32:
                new BankGUI(plugin, player).open();
                break;
            case 34:
                new DungeonFinderGUI(plugin, player).open();
                break;
            case 37:
                new SettingsGUI(plugin, player).open();
                break;
            case 39:
                new DailyRewardGUI(plugin, player).open();
                break;
            case 41:
                player.sendMessage("§aProfile - Coming Soon!");
                break;
            case 49:
                close();
                break;
        }
    }
}
