package de.noctivag.skyblock.gui;

import de.noctivag.skyblock.SkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Skills GUI - Fähigkeiten und -Verwaltung
 * Features:
 * - Real-time skill progress display
 * - Interactive skill management
 * - Skill rewards and milestones
 * - Modern Adventure API integration
 */
public class SkillsGUI implements Listener {
    
    private final SkyblockPlugin SkyblockPlugin;
    
    public SkillsGUI(SkyblockPlugin SkyblockPlugin) {
        this.SkyblockPlugin = SkyblockPlugin;
        Bukkit.getPluginManager().registerEvents(this, SkyblockPlugin);
    }
    
    public void openSkillsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Component.text("§6§lSkills"));
        
        // Skill Categories
        setItem(gui, 10, Material.DIAMOND_SWORD, "§c§lCombat",
            "§7Kampf",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 11, Material.DIAMOND_PICKAXE, "§b§lMining",
            "§7Mining",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 12, Material.WHEAT, "§a§lFarming",
            "§7Farming",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 13, Material.FISHING_ROD, "§9§lFishing",
            "§7Fishing",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 14, Material.OAK_LOG, "§6§lForaging",
            "§7Foraging",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        // Advanced Skills
        setItem(gui, 19, Material.ENCHANTING_TABLE, "§5§lEnchanting",
            "§7Verzauberung",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 20, Material.BREWING_STAND, "§9§lAlchemy",
            "§7Brauen",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 21, Material.CRAFTING_TABLE, "§e§lCarpentry",
            "§7Handwerk",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 22, Material.END_CRYSTAL, "§d§lRunecrafting",
            "§7Runen",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 23, Material.GOLD_INGOT, "§6§lBanking",
            "§7Bank",
            "§7• Current Level: §a0",
            "§7• Current XP: §a0",
            "§7• XP to Next Level: §a0",
            "",
            "§eKlicke zum Öffnen");
            
        // Skill Features
        setItem(gui, 28, Material.EXPERIENCE_BOTTLE, "§e§lXP Boosts",
            "§7XP-Boosts",
            "§7• Active Boosts: §a0",
            "§7• Boost Sources",
            "§7• Boost Management",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 29, Material.DIAMOND, "§b§lSkill Upgrades",
            "§7Skill-Upgrades",
            "§7• Upgrade Skills",
            "§7• Skill Enhancements",
            "§7• Special Upgrades",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 30, Material.EMERALD, "§a§lSkill Rewards",
            "§7Skill-Belohnungen",
            "§7• Level Rewards",
            "§7• Milestone Rewards",
            "§7• Special Rewards",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 31, Material.CLOCK, "§e§lSkill History",
            "§7Skill-Verlauf",
            "§7• Skill Progress",
            "§7• Skill Statistics",
            "§7• Skill Reports",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 32, Material.BOOK, "§b§lSkill Guide",
            "§7Skill-Anleitung",
            "§7• Skill Basics",
            "§7• Skill Tips",
            "§7• Skill Strategies",
            "",
            "§eKlicke zum Öffnen");
            
        // Skill Information
        setItem(gui, 37, Material.BOOK, "§b§lSkill Information",
            "§7Skill-Informationen",
            "§7• Skill Stats",
            "§7• Skill Abilities",
            "§7• Skill Features",
            "",
            "§eKlicke zum Öffnen");
            
        setItem(gui, 40, Material.CLOCK, "§e§lSkill Statistics",
            "§7Skill-Statistiken",
            "§7• Total Level: §a0",
            "§7• Average Level: §a0",
            "§7• Total XP: §a0",
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
            List<Component> componentLore = Arrays.stream(lore).map(Component::text).collect(java.util.stream.Collectors.toList());
            meta.lore(componentLore);
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        String title = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection()
            .serialize(event.getView().title());
        
        if (!title.contains("Skills")) return;
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        ItemMeta clickedMeta = clicked.getItemMeta();
        String displayName = (clickedMeta != null && clickedMeta.displayName() != null) ?
            net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection()
                .serialize(clickedMeta.displayName()) : "";
        
        handleSkillClick(player, displayName);
    }
    
    private void handleSkillClick(Player player, String displayName) {
        if (displayName.contains("Back")) {
            // Return to main menu
            player.closeInventory();
            // You can integrate with your main menu system here
            player.sendMessage(Component.text("§aReturning to main menu..."));
        } else if (displayName.contains("Close")) {
            player.closeInventory();
        } else if (displayName.contains("XP Boosts")) {
            player.sendMessage(Component.text("§aOpening XP Boosts..."));
            // Implement XP boosts functionality
        } else if (displayName.contains("Skill Upgrades")) {
            player.sendMessage(Component.text("§aOpening Skill Upgrades..."));
            // Implement skill upgrades functionality
        } else if (displayName.contains("Skill Rewards")) {
            player.sendMessage(Component.text("§aOpening Skill Rewards..."));
            // Implement skill rewards functionality
        } else if (displayName.contains("Skill History")) {
            player.sendMessage(Component.text("§aOpening Skill History..."));
            // Implement skill history functionality
        } else if (displayName.contains("Skill Guide")) {
            player.sendMessage(Component.text("§aOpening Skill Guide..."));
            // Implement skill guide functionality
        } else if (displayName.contains("Skill Information")) {
            player.sendMessage(Component.text("§aOpening Skill Information..."));
            // Implement skill information functionality
        } else if (displayName.contains("Skill Statistics")) {
            player.sendMessage(Component.text("§aOpening Skill Statistics..."));
            // Implement skill statistics functionality
        } else {
            // Handle specific skill clicks
            player.sendMessage("§aOpening " + displayName + " skill details...");
            // Implement specific skill functionality
        }
    }
}
