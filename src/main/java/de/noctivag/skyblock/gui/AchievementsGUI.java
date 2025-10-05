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
 * Achievements GUI - Erfolge und Meilensteine
 */
public class AchievementsGUI {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public AchievementsGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
    }
    
    public void openAchievementsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lAchievements"));
        
        // Achievement Categories
        setItem(gui, 10, Material.DIAMOND_SWORD, "§c§lCombat Achievements",
            "§7Kampf-bezogene Erfolge",
            "§7• Mob Kills",
            "§7• PvP Victories",
            "§7• Boss Defeats",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.DIAMOND_PICKAXE, "§b§lMining Achievements",
            "§7Mining-bezogene Erfolge",
            "§7• Ores Mined",
            "§7• Mining Levels",
            "§7• Special Blocks",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.WHEAT, "§a§lFarming Achievements",
            "§7Farming-bezogene Erfolge",
            "§7• Crops Harvested",
            "§7• Farming Levels",
            "§7• Special Crops",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 16, Material.FISHING_ROD, "§9§lFishing Achievements",
            "§7Fishing-bezogene Erfolge",
            "§7• Fish Caught",
            "§7• Fishing Levels",
            "§7• Rare Fish",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 19, Material.OAK_LOG, "§6§lForaging Achievements",
            "§7Foraging-bezogene Erfolge",
            "§7• Trees Chopped",
            "§7• Foraging Levels",
            "§7• Special Trees",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.ENCHANTING_TABLE, "§d§lEnchanting Achievements",
            "§7Enchanting-bezogene Erfolge",
            "§7• Items Enchanted",
            "§7• Enchanting Levels",
            "§7• Rare Enchants",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.BREWING_STAND, "§5§lAlchemy Achievements",
            "§7Alchemy-bezogene Erfolge",
            "§7• Potions Brewed",
            "§7• Alchemy Levels",
            "§7• Special Potions",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 25, Material.CRAFTING_TABLE, "§e§lCarpentry Achievements",
            "§7Carpentry-bezogene Erfolge",
            "§7• Items Crafted",
            "§7• Carpentry Levels",
            "§7• Special Recipes",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 28, Material.IRON_GOLEM_SPAWN_EGG, "§f§lMinion Achievements",
            "§7Minion-bezogene Erfolge",
            "§7• Minions Placed",
            "§7• Minion Upgrades",
            "§7• Minion Collections",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.BONE, "§6§lPet Achievements",
            "§7Pet-bezogene Erfolge",
            "§7• Pets Obtained",
            "§7• Pet Levels",
            "§7• Pet Abilities",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.GRASS_BLOCK, "§a§lIsland Achievements",
            "§7Island-bezogene Erfolge",
            "§7• Island Upgrades",
            "§7• Island Size",
            "§7• Island Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 34, Material.EMERALD, "§a§lEconomy Achievements",
            "§7Economy-bezogene Erfolge",
            "§7• Coins Earned",
            "§7• Items Sold",
            "§7• Market Activity",
            "",
            "§eKlicke zum Öffnen");
            
        // Special Achievements
        setItem(gui, 37, Material.NETHER_STAR, "§d§lSpecial Achievements",
            "§7Besondere Erfolge",
            "§7• Rare Events",
            "§7• Special Milestones",
            "§7• Hidden Achievements",
            "",
            "§eKlicke zum Öffnen");
            
        // Achievement Stats
        setItem(gui, 40, Material.BOOK, "§b§lAchievement Stats",
            "§7Deine Erfolgs-Statistiken",
            "§7• Total Achievements: §a0/100",
            "§7• Completion Rate: §a0%",
            "§7• Recent Achievements: §a0",
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
