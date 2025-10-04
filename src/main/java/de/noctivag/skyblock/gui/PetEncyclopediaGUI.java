package de.noctivag.skyblock.gui;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Pet Encyclopedia GUI - Haustier-Enzyklopädie und -Wissen
 */
public class PetEncyclopediaGUI {
    
    private final SkyblockPlugin plugin;
    
    public PetEncyclopediaGUI(SkyblockPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void openPetEncyclopediaGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, "§6§lPet Encyclopedia");
        
        // Pet Categories
        setItem(gui, 10, Material.WOLF_SPAWN_EGG, "§6§lCombat Pets",
            "§7Kampf-Haustiere",
            "§7• Wolf Pets",
            "§7• Cat Pets",
            "§7• Special Combat Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.COW_SPAWN_EGG, "§a§lFarming Pets",
            "§7Farming-Haustiere",
            "§7• Cow Pets",
            "§7• Sheep Pets",
            "§7• Special Farming Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.BAT_SPAWN_EGG, "§9§lMining Pets",
            "§7Mining-Haustiere",
            "§7• Bat Pets",
            "§7• Spider Pets",
            "§7• Special Mining Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.PARROT_SPAWN_EGG, "§d§lFishing Pets",
            "§7Fishing-Haustiere",
            "§7• Parrot Pets",
            "§7• Dolphin Pets",
            "§7• Special Fishing Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.OCELOT_SPAWN_EGG, "§6§lForaging Pets",
            "§7Foraging-Haustiere",
            "§7• Ocelot Pets",
            "§7• Panda Pets",
            "§7• Special Foraging Pets",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Information
        setItem(gui, 19, Material.BOOK, "§b§lPet Information",
            "§7Haustier-Informationen",
            "§7• Pet Stats",
            "§7• Pet Abilities",
            "§7• Pet Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.ENCHANTED_BOOK, "§d§lPet Abilities",
            "§7Haustier-Fähigkeiten",
            "§7• Combat Abilities",
            "§7• Utility Abilities",
            "§7• Special Abilities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.DIAMOND, "§b§lPet Stats",
            "§7Haustier-Stats",
            "§7• Health Stats",
            "§7• Damage Stats",
            "§7• Speed Stats",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.EMERALD, "§a§lPet Features",
            "§7Haustier-Features",
            "§7• Special Features",
            "§7• Unique Features",
            "§7• Exclusive Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.NETHER_STAR, "§d§lPet Lore",
            "§7Haustier-Lore",
            "§7• Pet History",
            "§7• Pet Background",
            "§7• Pet Stories",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Knowledge
        setItem(gui, 28, Material.CLOCK, "§e§lPet History",
            "§7Haustier-Geschichte",
            "§7• Pet Origins",
            "§7• Pet Evolution",
            "§7• Pet Development",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.PAPER, "§b§lPet Statistics",
            "§7Haustier-Statistiken",
            "§7• Pet Population",
            "§7• Pet Distribution",
            "§7• Pet Trends",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BELL, "§a§lPet Updates",
            "§7Haustier-Updates",
            "§7• Recent Updates",
            "§7• Update History",
            "§7• Future Updates",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.COMPASS, "§e§lPet Discovery",
            "§7Haustier-Entdeckung",
            "§7• New Pets",
            "§7• Rare Pets",
            "§7• Hidden Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lPet Research",
            "§7Haustier-Forschung",
            "§7• Pet Studies",
            "§7• Pet Analysis",
            "§7• Pet Reports",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Encyclopedia Tools
        setItem(gui, 37, Material.BOOK, "§b§lEncyclopedia Guide",
            "§7Enzyklopädie-Anleitung",
            "§7• Encyclopedia Basics",
            "§7• Encyclopedia Tips",
            "§7• Encyclopedia Usage",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lEncyclopedia Statistics",
            "§7Enzyklopädie-Statistiken",
            "§7• Total Entries: §a0",
            "§7• Discovered Pets: §a0",
            "§7• Knowledge Level: §a0%",
            "",
            "§eKlicke zum Öffnen");
            
        // Navigation
        setItem(gui, 45, Material.ARROW, "§7« Back",
            "§7Zurück zum Hauptmenü");
            
        setItem(gui, 49, Material.BARRIER, "§c§lClose",
            "§7GUI schließen");
        
        player.openInventory(gui);
    }
    
    private void setItem(Inventory gui, int slot, Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
