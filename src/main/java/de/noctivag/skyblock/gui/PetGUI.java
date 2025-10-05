package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.inventory.ItemStack;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import java.util.stream.Collectors;

/**
 * Pet GUI - Haustiere und Begleiter
 */
public class PetGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public PetGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void open(Player player) {
        openPetGUI(player);
    }
    
    public void openPetGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lPets"));
        
        // Pet Management
        setItem(gui, 10, Material.BONE, "§6§lMy Pets",
            "§7Meine Haustiere",
            "§7• Active Pets: §a0",
            "§7• Total Pets: §a0",
            "§7• Pet Collection",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.EMERALD, "§a§lPet Shop",
            "§7Haustier-Shop",
            "§7• Buy Pets",
            "§7• Pet Categories",
            "§7• Special Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.EXPERIENCE_BOTTLE, "§e§lPet Leveling",
            "§7Haustier-Leveling",
            "§7• Level Up Pets",
            "§7• XP Management",
            "§7• Level Boosts",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.DIAMOND, "§b§lPet Upgrades",
            "§7Haustier-Upgrades",
            "§7• Upgrade Pets",
            "§7• Pet Abilities",
            "§7• Special Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.APPLE, "§a§lPet Care",
            "§7Haustier-Pflege",
            "§7• Feed Pets",
            "§7• Pet Health",
            "§7• Pet Happiness",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Features
        setItem(gui, 19, Material.CHEST, "§6§lPet Inventory",
            "§7Haustier-Inventar",
            "§7• Pet Items",
            "§7• Pet Equipment",
            "§7• Pet Storage",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.BOOK, "§b§lPet Guide",
            "§7Haustier-Anleitung",
            "§7• Pet Basics",
            "§7• Pet Tips",
            "§7• Pet Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.REDSTONE, "§c§lPet Settings",
            "§7Haustier-Einstellungen",
            "§7• Pet Display",
            "§7• Pet Notifications",
            "§7• Pet Preferences",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.CLOCK, "§e§lPet History",
            "§7Haustier-Verlauf",
            "§7• Pet Activities",
            "§7• Pet Statistics",
            "§7• Pet Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.EMERALD, "§a§lPet Rewards",
            "§7Haustier-Belohnungen",
            "§7• Pet Bonuses",
            "§7• Pet Rewards",
            "§7• Pet Achievements",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Categories
        setItem(gui, 28, Material.WOLF_SPAWN_EGG, "§6§lCombat Pets",
            "§7Kampf-Haustiere",
            "§7• Wolf Pets",
            "§7• Cat Pets",
            "§7• Special Combat Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.COW_SPAWN_EGG, "§a§lFarming Pets",
            "§7Farming-Haustiere",
            "§7• Cow Pets",
            "§7• Sheep Pets",
            "§7• Special Farming Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BAT_SPAWN_EGG, "§9§lMining Pets",
            "§7Mining-Haustiere",
            "§7• Bat Pets",
            "§7• Spider Pets",
            "§7• Special Mining Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.PARROT_SPAWN_EGG, "§d§lFishing Pets",
            "§7Fishing-Haustiere",
            "§7• Parrot Pets",
            "§7• Dolphin Pets",
            "§7• Special Fishing Pets",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.OCELOT_SPAWN_EGG, "§6§lForaging Pets",
            "§7Foraging-Haustiere",
            "§7• Ocelot Pets",
            "§7• Panda Pets",
            "§7• Special Foraging Pets",
            "",
            "§eKlicke zum Öffnen");
            
        // Pet Information
        setItem(gui, 37, Material.BOOK, "§b§lPet Information",
            "§7Haustier-Informationen",
            "§7• Pet Types",
            "§7• Pet Abilities",
            "§7• Pet Stats",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lPet Statistics",
            "§7Haustier-Statistiken",
            "§7• Total Pets: §a0",
            "§7• Pet Levels: §a0",
            "§7• Pet XP: §a0",
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
            meta.displayName(Component.text(name));
            meta.lore(Arrays.asList(lore).stream().map(Component::text).collect(java.util.stream.Collectors.toList()));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}
